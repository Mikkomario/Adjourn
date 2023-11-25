package vf.adjourn.controller.file

import utopia.flow.async.context.TwoThreadBuffer
import utopia.flow.async.context.TwoThreadBuffer.{Input, Output}
import utopia.flow.collection.immutable.caching.LazyTree
import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.immutable.caching.iterable.CachingSeq
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import vf.adjourn.database.access.many.file.directory.DbDirectories
import vf.adjourn.database.access.many.file.index.DbFileIndices
import vf.adjourn.database.model.file.DirectoryModel
import vf.adjourn.model.partial.file.{DirectoryData, FileIndexData}
import vf.adjourn.model.stored.file.{Directory, FileIndex}
import vf.adjourn.util.Common._

import java.nio.file.Path
import java.time.Instant

/**
 * A process for indexing files
 * @author Mikko Hilpinen
 * @since 24.11.2023, v0.1
 */
object IndexFiles
{
	def apply(root: Path) = {
		// Processes the files in multiple threads:
		//      - One lists files from the file system
		//      - One checks which have already been indexed
		//      - One checks the last modified times and queues indexing, if appropriate
		//      - One counts the sizes of those files
		//      - One combines read data together
		//      - One stores data to the database
		// Contains all read paths. Includes their last modified time. Groups by directory.
		val pathBuffer = new TwoThreadBuffer[(Path, Seq[(Path, Option[Instant])])](500)
		// Contains paths that had no existing index. Includes directory id.
		val newPathsBuffer = new TwoThreadBuffer[(Int, Path, Option[Instant])](500)
		// Contains paths that had an existing index (included)
		val existingPathsBuffer = new TwoThreadBuffer[(Path, FileIndex)](500)
		// Contains prepared indexing data (size, name, file type & last modified) for each new path
		val indexDataBuffer = new TwoThreadBuffer[FileIndexData](500)
		// Contains queued path index updates (index id, file size & last modified time)
		val updateBuffer = new TwoThreadBuffer[(Int, Long, Option[Instant])](500)
	}
	
	// NB: Doesn't close the buffer afterwards
	private def listFilesByDirectory(dirTree: LazyTree[Path],
	                                 buffer: Output[(Path, Seq[(Path, Option[Instant])])]): Unit =
	{
		// Lists all regular files and sub-directories in this directory
		val (regularFiles, subDirectories) = dirTree.children
			.divideBy { n => n.hasChildren || n.nav.isDirectory }.toTuple
		// Checks all last-modified times of the regular files and then adds them to the buffer
		buffer += (dirTree.nav -> regularFiles.map { n => n.nav -> n.nav.lastModified.toOption })
		
		// Handles the sub-directories recursively
		subDirectories.foreach { listFilesByDirectory(_, buffer) }
	}
	
	private def checkPathExistence(pathBuffer: Input[(Path, Seq[(Path, Option[Instant])])],
	                               newPathsBuffer: Output[(Int, Path, Option[Instant])],
	                               existingPathsBuffer: Output[(Path, FileIndex, Option[Instant])])
	                              (implicit connection: Connection) =
	{
		while (pathBuffer.hasNext) {
			val directories = pathBuffer.collectNextBetween(1, 100)
			// TODO: Handle the drive level and then use checkPathExistence to handle the directories. Remember to use normalized paths.
		}
	}
	
	// Here assumes that the parts -vector of NONE of the listed directories starts with the specified parent directory
	private def checkPathExistence(parentId: Int, directories: Vector[(Vector[String], Seq[(Path, Option[Instant])])],
	                               existingEntries: Vector[Directory],
	                               newPathsBuffer: Output[(Int, Path, Option[Instant])],
	                               existingPathsBuffer: Output[(Path, FileIndex, Option[Instant])])
	                              (implicit connection: Connection): Unit =
	{
		// Checks which of the directories exist in the index and which don't
		val existingDirMap = existingEntries.view.map { d => d.name -> d.id }.toMap
		// NB: Every listed directory parts -vector contains the inserted or existing directory as the first element
		// at this point
		val (newDirectories, existingDirectories) = directories.groupBy { _._1.head }
			.divideWith { case (dirName, dirEntries) =>
				existingDirMap.get(dirName) match {
					case Some(dirId) => Right(dirId -> dirEntries)
					case None => Left(DirectoryData(Some(parentId), dirName) -> dirEntries)
				}
			}
		
		// Inserts the new directories to the database and lists their files as new
		if (newDirectories.nonEmpty)
			insertNewDirectoriesAndFiles(newDirectories, newPathsBuffer)
		
		// Finds the directories that have been recorded to exist under the existing directories being processed
		if (existingDirectories.nonEmpty) {
			val existingSubDirectoryMap = DbDirectories(existingDirectories.map { _._1 }.toSet).subDirectories
				.pull.groupBy { _.parentId.get }
			// For existing directories, checks the sub-directories recursively
			// Each existing directory entry contains:
			// [(id of the existing directory, [entries for that directory and all sub-directories])]
			existingDirectories.foreach { case (dirId, dirEntries) =>
				val (subDirectoryEntries, thisDirectoryEntries) = dirEntries.divideBy { _._1 hasSize 1 }.toTuple
				// If files are listed for this directory, checks which of them exist in the index and which don't
				if (thisDirectoryEntries.exists { _._2.nonEmpty }) {
					val existingFileEntryMap = DbFileIndices.inDirectory(dirId).toMapBy { _.name }
					val (newPaths, existingIndexMatches) = thisDirectoryEntries.splitFlatMap { case (_, files) =>
						files.divideWith { case (path, lastModified) =>
							existingFileEntryMap.get(path.fileName) match {
								case Some(existingIndex) => Right((path, existingIndex, lastModified))
								case None => Left((dirId, path, lastModified))
							}
						}
					}
					newPathsBuffer ++= newPaths
					existingPathsBuffer ++= existingIndexMatches
				}
				// Handles the sub-directories recursively
				if (subDirectoryEntries.nonEmpty) {
					val existingSubDirectories = existingSubDirectoryMap.getOrElse(dirId, Vector.empty)
					checkPathExistence(dirId, subDirectoryEntries.map { case (parts, files) => parts.tail -> files },
						existingSubDirectories, newPathsBuffer, existingPathsBuffer)
				}
			}
		}
	}
	
	// Assumes that inserted directory name is listed as the first parts element in first newDirData elements
	// Consequently that every parts-vector contains at least one element
	private def insertNewDirectoriesAndFiles(newDirData: Vector[(DirectoryData, Vector[(Vector[String], Seq[(Path, Option[Instant])])])],
	                                         newPathsBuffer: Output[(Int, Path, Option[Instant])])
	                                        (implicit connection: Connection): Unit =
	{
		DirectoryModel.insert(newDirData.map { _._1 }).iterator.zip(newDirData)
			.foreach { case (inserted, (_, directoryEntries)) =>
				val (subDirectoryData, thisDirectoryData) = directoryEntries.divideBy { _._1 hasSize 1 }.toTuple
				// Adds the files from this directory to the new files -buffer
				newPathsBuffer ++= thisDirectoryData.flatMap { case (_, files) =>
					files.map { case (path, lastModified) => (inserted.id, path, lastModified) }
				}
				// Handles the sub-directories recursively
				if (subDirectoryData.nonEmpty) {
					val nextIterationData = subDirectoryData.map { case (parts, files) => parts.tail -> files }
						.groupBy { _._1.head }.toVector
						.map { case (dirName, dirEntries) => (DirectoryData(Some(inserted.id), dirName), dirEntries) }
					insertNewDirectoriesAndFiles(nextIterationData, newPathsBuffer)
				}
			}
	}
}
