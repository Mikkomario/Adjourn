package vf.adjourn.database.access.many.file.`type`

type

import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import utopia.vault.sql.Condition
import vf.adjourn.database.factory.file.FileTypeFactory
import vf.adjourn.database.model.file.FileTypeModel
import vf.adjourn.model.stored.file.FileType

object ManyFileTypesAccess
{
	// NESTED	--------------------
	
	private class ManyFileTypesSubView(condition: Condition) extends ManyFileTypesAccess
	{
		// IMPLEMENTED	--------------------
		
		override def globalCondition = Some(condition)
	}
}

/**
  * A common trait for access points which target multiple file types at a time
  * @since 24.11.2023
  */
trait ManyFileTypesAccess 
	extends ManyRowModelAccess[FileType] with FilterableView[ManyFileTypesAccess] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * names of the accessible file types
	  */
	def names(implicit connection: Connection) = pullColumn(model.nameColumn).flatMap { _.string }
	
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
	/**
	  * Factory used for constructing database the interaction models
	  */
	protected def model = FileTypeModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = FileTypeFactory
	
	override protected def self = this
	
	override def filter(filterCondition: Condition): ManyFileTypesAccess = 
		new ManyFileTypesAccess.ManyFileTypesSubView(mergeCondition(filterCondition))
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the names of the targeted file types
	  * @param newName A new name to assign
	  * @return Whether any file type was affected
	  */
	def names_=(newName: String)(implicit connection: Connection) = putColumn(model.nameColumn, newName)
}

