package vf.adjourn.database.access.single.file.directory

import utopia.vault.nosql.view.FilterableView
import utopia.vault.sql.Condition
import vf.adjourn.database.factory.file.DirectoryWithFilesFactory
import vf.adjourn.database.model.file.FileIndexModel
import vf.adjourn.model.combined.file.DirectoryWithFiles

object UniqueDirectoryWithFilesAccess
{
	// OTHER	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	def apply(condition: Condition): UniqueDirectoryWithFilesAccess = 
		new _UniqueDirectoryWithFilesAccess(condition)
	
	
	// NESTED	--------------------
	
	private class _UniqueDirectoryWithFilesAccess(condition: Condition) extends UniqueDirectoryWithFilesAccess
	{
		// IMPLEMENTED	--------------------
		
		override def globalCondition = Some(condition)
	}
}

/**
  * A common trait for access points that return distinct directory with fileses
  * @since 24.11.2023
  */
trait UniqueDirectoryWithFilesAccess 
	extends UniqueDirectoryAccessLike[DirectoryWithFiles] with FilterableView[UniqueDirectoryWithFilesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked files
	  */
	protected def fileModel = FileIndexModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DirectoryWithFilesFactory
	
	override protected def self = this
	
	override def filter(filterCondition: Condition): UniqueDirectoryWithFilesAccess = 
		new UniqueDirectoryWithFilesAccess._UniqueDirectoryWithFilesAccess(mergeCondition(filterCondition))
}

