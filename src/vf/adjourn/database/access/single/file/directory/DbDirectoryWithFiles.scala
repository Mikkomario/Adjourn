package vf.adjourn.database.access.single.file.directory

import utopia.vault.nosql.access.single.model.SingleModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.adjourn.database.factory.file.DirectoryWithFilesFactory
import vf.adjourn.database.model.file.{DirectoryModel, FileIndexModel}
import vf.adjourn.model.combined.file.DirectoryWithFiles

/**
  * Used for accessing individual directory with fileses
  * @since 24.11.2023
  */
object DbDirectoryWithFiles extends SingleModelAccess[DirectoryWithFiles] with UnconditionalView with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with linked directories
	  */
	protected def model = DirectoryModel
	
	/**
	  * A database model (factory) used for interacting with the linked files
	  */
	protected def fileModel = FileIndexModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DirectoryWithFilesFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted directory with files
	  * @return An access point to that directory with files
	  */
	def apply(id: Int) = DbSingleDirectoryWithFiles(id)
	
	/**
	  * 
		@param condition Filter condition to apply in addition to this root view's condition. Should yield unique directory 
	  * with fileses.
	  * @return An access point to the directory with files that satisfies the specified condition
	  */
	protected
		 def filterDistinct(condition: Condition) = UniqueDirectoryWithFilesAccess(mergeCondition(condition))
}

