package vf.adjourn.database.access.many.file.index

import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import utopia.vault.sql.Condition
import vf.adjourn.database.factory.file.FileIndexFactory
import vf.adjourn.database.model.file.FileIndexModel
import vf.adjourn.model.stored.file.FileIndex

import java.time.Instant

object ManyFileIndicesAccess
{
	// NESTED	--------------------
	
	private class ManyFileIndicesSubView(condition: Condition) extends ManyFileIndicesAccess
	{
		// IMPLEMENTED	--------------------
		
		override def globalCondition = Some(condition)
	}
}

/**
  * A common trait for access points which target multiple file indices at a time
  * @since 24.11.2023
  */
trait ManyFileIndicesAccess 
	extends ManyRowModelAccess[FileIndex] with FilterableView[ManyFileIndicesAccess] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * directory ids of the accessible file indices
	  */
	def directoryIds(implicit connection: Connection) = pullColumn(model.directoryIdColumn)
		.map { v => v.getInt }
	
	/**
	  * names of the accessible file indices
	  */
	def names(implicit connection: Connection) = pullColumn(model.nameColumn).flatMap { _.string }
	
	/**
	  * type ids of the accessible file indices
	  */
	def typeIds(implicit connection: Connection) = pullColumn(model.typeIdColumn).map { v => v.getInt }
	
	/**
	  * sizes of the accessible file indices
	  */
	def sizes(implicit connection: Connection) = pullColumn(model.sizeColumn).map { v => v.getInt }
	
	/**
	  * last modifieds of the accessible file indices
	  */
	def lastModifieds(implicit connection: Connection) = 
		pullColumn(model.lastModifiedColumn).flatMap { v => v.instant }
	
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
	/**
	  * Factory used for constructing database the interaction models
	  */
	protected def model = FileIndexModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = FileIndexFactory
	
	override protected def self = this
	
	override def filter(filterCondition: Condition): ManyFileIndicesAccess = 
		new ManyFileIndicesAccess.ManyFileIndicesSubView(mergeCondition(filterCondition))
	
	
	// OTHER	--------------------
	
	/**
	 * @param directoryId Id of the targeted directory
	 * @return Access to file indices within that directory
	 */
	def inDirectory(directoryId: Int) = filter(model.withDirectoryId(directoryId).toCondition)
	
	/**
	  * Updates the directory ids of the targeted file indices
	  * @param newDirectoryId A new directory id to assign
	  * @return Whether any file index was affected
	  */
	def directoryIds_=(newDirectoryId: Int)(implicit connection: Connection) = 
		putColumn(model.directoryIdColumn, newDirectoryId)
	
	/**
	  * Updates the last modifieds of the targeted file indices
	  * @param newLastModified A new last modified to assign
	  * @return Whether any file index was affected
	  */
	def lastModifieds_=(newLastModified: Instant)(implicit connection: Connection) = 
		putColumn(model.lastModifiedColumn, newLastModified)
	
	/**
	  * Updates the names of the targeted file indices
	  * @param newName A new name to assign
	  * @return Whether any file index was affected
	  */
	def names_=(newName: String)(implicit connection: Connection) = putColumn(model.nameColumn, newName)
	
	/**
	  * Updates the sizes of the targeted file indices
	  * @param newSize A new size to assign
	  * @return Whether any file index was affected
	  */
	def sizes_=(newSize: Int)(implicit connection: Connection) = putColumn(model.sizeColumn, newSize)
	
	/**
	  * Updates the type ids of the targeted file indices
	  * @param newTypeId A new type id to assign
	  * @return Whether any file index was affected
	  */
	def typeIds_=(newTypeId: Int)(implicit connection: Connection) = putColumn(model.typeIdColumn, newTypeId)
}

