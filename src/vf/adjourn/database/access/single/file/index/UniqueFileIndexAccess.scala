package vf.adjourn.database.access.single.file.index

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import utopia.vault.sql.Condition
import vf.adjourn.database.factory.file.FileIndexFactory
import vf.adjourn.database.model.file.FileIndexModel
import vf.adjourn.model.stored.file.FileIndex

import java.time.Instant

object UniqueFileIndexAccess
{
	// OTHER	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	def apply(condition: Condition): UniqueFileIndexAccess = new _UniqueFileIndexAccess(condition)
	
	
	// NESTED	--------------------
	
	private class _UniqueFileIndexAccess(condition: Condition) extends UniqueFileIndexAccess
	{
		// IMPLEMENTED	--------------------
		
		override def globalCondition = Some(condition)
	}
}

/**
  * A common trait for access points that return individual and distinct file indices.
  * @since 24.11.2023
  */
trait UniqueFileIndexAccess 
	extends SingleRowModelAccess[FileIndex] with FilterableView[UniqueFileIndexAccess] 
		with DistinctModelAccess[FileIndex, Option[FileIndex], Value] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the directory where this file resides. None if no file index (or value) was found.
	  */
	def directoryId(implicit connection: Connection) = pullColumn(model.directoryIdColumn).int
	
	/**
	  * Name of this file. None if no file index (or value) was found.
	  */
	def name(implicit connection: Connection) = pullColumn(model.nameColumn).getString
	
	/**
	  * Id that matches this file's type. None if no file index (or value) was found.
	  */
	def typeId(implicit connection: Connection) = pullColumn(model.typeIdColumn).int
	
	/**
	  * Size of this file in bytes. None if no file index (or value) was found.
	  */
	def size(implicit connection: Connection) = pullColumn(model.sizeColumn).int
	
	/**
	  * 
		Time when this file was last modified (may be out of date). None if no file index (or value) was found.
	  */
	def lastModified(implicit connection: Connection) = pullColumn(model.lastModifiedColumn).instant
	
	def id(implicit connection: Connection) = pullColumn(index).int
	
	/**
	  * Factory used for constructing database the interaction models
	  */
	protected def model = FileIndexModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = FileIndexFactory
	
	override protected def self = this
	
	override def filter(filterCondition: Condition): UniqueFileIndexAccess = 
		new UniqueFileIndexAccess._UniqueFileIndexAccess(mergeCondition(filterCondition))
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the directory ids of the targeted file indices
	  * @param newDirectoryId A new directory id to assign
	  * @return Whether any file index was affected
	  */
	def directoryId_=(newDirectoryId: Int)(implicit connection: Connection) = 
		putColumn(model.directoryIdColumn, newDirectoryId)
	
	/**
	  * Updates the last modifieds of the targeted file indices
	  * @param newLastModified A new last modified to assign
	  * @return Whether any file index was affected
	  */
	def lastModified_=(newLastModified: Instant)(implicit connection: Connection) = 
		putColumn(model.lastModifiedColumn, newLastModified)
	
	/**
	  * Updates the names of the targeted file indices
	  * @param newName A new name to assign
	  * @return Whether any file index was affected
	  */
	def name_=(newName: String)(implicit connection: Connection) = putColumn(model.nameColumn, newName)
	
	/**
	  * Updates the sizes of the targeted file indices
	  * @param newSize A new size to assign
	  * @return Whether any file index was affected
	  */
	def size_=(newSize: Int)(implicit connection: Connection) = putColumn(model.sizeColumn, newSize)
	
	/**
	  * Updates the type ids of the targeted file indices
	  * @param newTypeId A new type id to assign
	  * @return Whether any file index was affected
	  */
	def typeId_=(newTypeId: Int)(implicit connection: Connection) = putColumn(model.typeIdColumn, newTypeId)
}

