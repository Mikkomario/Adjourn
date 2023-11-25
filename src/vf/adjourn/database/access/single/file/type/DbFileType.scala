package vf.adjourn.database.access.single.file.`type`

type

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.adjourn.database.factory.file.FileTypeFactory
import vf.adjourn.database.model.file.FileTypeModel
import vf.adjourn.model.stored.file.FileType

/**
  * Used for accessing individual file types
  * @since 24.11.2023
  */
object DbFileType extends SingleRowModelAccess[FileType] with UnconditionalView with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Factory used for constructing database the interaction models
	  */
	protected def model = FileTypeModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = FileTypeFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted file type
	  * @return An access point to that file type
	  */
	def apply(id: Int) = DbSingleFileType(id)
	
	/**
	  * @param condition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique file types.
	  * @return An access point to the file type that satisfies the specified condition
	  */
	protected def filterDistinct(condition: Condition) = UniqueFileTypeAccess(mergeCondition(condition))
}

