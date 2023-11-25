package vf.adjourn.database.access.many.file.directory

import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.database.Connection
import utopia.vault.sql.Condition
import vf.adjourn.database.factory.file.DirectoryWithFilesFactory
import vf.adjourn.database.model.file.FileIndexModel
import vf.adjourn.model.combined.file.DirectoryWithFiles

import java.time.Instant

object ManyDirectoriesWithFilesAccess
{
	// NESTED	--------------------
	
	private class SubAccess(condition: Condition) extends ManyDirectoriesWithFilesAccess
	{
		// IMPLEMENTED	--------------------
		
		override def globalCondition = Some(condition)
	}
}

/**
  * A common trait for access points that return multiple directory with fileses at a time
  * @since 24.11.2023
  */
trait ManyDirectoriesWithFilesAccess
	extends ManyDirectoriesAccessLike[DirectoryWithFiles, ManyDirectoriesWithFilesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * directory ids of the accessible file indices
	  */
	def fileDirectoryIds(implicit connection: Connection) =
		pullColumn(fileModel.directoryIdColumn).map { v => v.getInt }
	
	/**
	  * names of the accessible file indices
	  */
	def fileNames(implicit connection: Connection) = pullColumn(fileModel.nameColumn).flatMap { _.string }
	
	/**
	  * type ids of the accessible file indices
	  */
	def fileTypeIds(implicit connection: Connection) = pullColumn(fileModel.typeIdColumn)
		.map { v => v.getInt }
	
	/**
	  * sizes of the accessible file indices
	  */
	def fileSizes(implicit connection: Connection) = pullColumn(fileModel.sizeColumn).map { v => v.getInt }
	
	/**
	  * last modifieds of the accessible file indices
	  */
	def fileLastModifieds(implicit connection: Connection) =
		pullColumn(fileModel.lastModifiedColumn).flatMap { v => v.instant }
	
	/**
	  * Model (factory) used for interacting the file indices associated with this directory with files
	  */
	protected def fileModel = FileIndexModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DirectoryWithFilesFactory
	
	override protected def self = this
	
	override def filter(filterCondition: Condition): ManyDirectoriesWithFilesAccess =
		new ManyDirectoriesWithFilesAccess.SubAccess(mergeCondition(filterCondition))
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the directory ids of the targeted file indices
	  * @param newDirectoryId A new directory id to assign
	  * @return Whether any file index was affected
	  */
	def fileDirectoryIds_=(newDirectoryId: Int)(implicit connection: Connection) = 
		putColumn(fileModel.directoryIdColumn, newDirectoryId)
	
	/**
	  * Updates the last modifieds of the targeted file indices
	  * @param newLastModified A new last modified to assign
	  * @return Whether any file index was affected
	  */
	def fileLastModifieds_=(newLastModified: Instant)(implicit connection: Connection) = 
		putColumn(fileModel.lastModifiedColumn, newLastModified)
	
	/**
	  * Updates the names of the targeted file indices
	  * @param newName A new name to assign
	  * @return Whether any file index was affected
	  */
	def fileNames_=(newName: String)(implicit connection: Connection) = putColumn(fileModel.nameColumn, 
		newName)
	
	/**
	  * Updates the sizes of the targeted file indices
	  * @param newSize A new size to assign
	  * @return Whether any file index was affected
	  */
	def fileSizes_=(newSize: Int)(implicit connection: Connection) = putColumn(fileModel.sizeColumn, newSize)
	
	/**
	  * Updates the type ids of the targeted file indices
	  * @param newTypeId A new type id to assign
	  * @return Whether any file index was affected
	  */
	def fileTypeIds_=(newTypeId: Int)(implicit connection: Connection) = 
		putColumn(fileModel.typeIdColumn, newTypeId)
}

