package vf.adjourn.database.access.single.file.`type`

type

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import utopia.vault.sql.Condition
import vf.adjourn.database.factory.file.FileTypeFactory
import vf.adjourn.database.model.file.FileTypeModel
import vf.adjourn.model.stored.file.FileType

object UniqueFileTypeAccess
{
	// OTHER	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	def apply(condition: Condition): UniqueFileTypeAccess = new _UniqueFileTypeAccess(condition)
	
	
	// NESTED	--------------------
	
	private class _UniqueFileTypeAccess(condition: Condition) extends UniqueFileTypeAccess
	{
		// IMPLEMENTED	--------------------
		
		override def globalCondition = Some(condition)
	}
}

/**
  * A common trait for access points that return individual and distinct file types.
  * @since 24.11.2023
  */
trait UniqueFileTypeAccess 
	extends SingleRowModelAccess[FileType] with FilterableView[UniqueFileTypeAccess] 
		with DistinctModelAccess[FileType, Option[FileType], Value] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Name of this file type. None if no file type (or value) was found.
	  */
	def name(implicit connection: Connection) = pullColumn(model.nameColumn).getString
	
	def id(implicit connection: Connection) = pullColumn(index).int
	
	/**
	  * Factory used for constructing database the interaction models
	  */
	protected def model = FileTypeModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = FileTypeFactory
	
	override protected def self = this
	
	override def filter(filterCondition: Condition): UniqueFileTypeAccess = 
		new UniqueFileTypeAccess._UniqueFileTypeAccess(mergeCondition(filterCondition))
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the names of the targeted file types
	  * @param newName A new name to assign
	  * @return Whether any file type was affected
	  */
	def name_=(newName: String)(implicit connection: Connection) = putColumn(model.nameColumn, newName)
}

