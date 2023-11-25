package vf.adjourn.database.factory.file

import utopia.vault.nosql.factory.multi.MultiCombiningFactory
import vf.adjourn.model.combined.file.DirectoryWithFiles
import vf.adjourn.model.stored.file.{Directory, FileIndex}

/**
  * Used for reading directory with fileses from the database
  * @since 24.11.2023
  */
object DirectoryWithFilesFactory extends MultiCombiningFactory[DirectoryWithFiles, Directory, FileIndex]
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = FileIndexFactory
	
	override def isAlwaysLinked = false
	
	override def parentFactory = DirectoryFactory
	
	override def apply(directory: Directory, files: Vector[FileIndex]) = DirectoryWithFiles(directory, files)
}

