package vf.adjourn.database.access.single.file.index

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.adjourn.database.factory.file.FileIndexFactory
import vf.adjourn.database.model.file.FileIndexModel
import vf.adjourn.model.stored.file.FileIndex

/**
  * Used for accessing individual file indices
  * @since 24.11.2023
  */
object DbFileIndex extends SingleRowModelAccess[FileIndex] with UnconditionalView with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Factory used for constructing database the interaction models
	  */
	protected def model = FileIndexModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = FileIndexFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted file index
	  * @return An access point to that file index
	  */
	def apply(id: Int) = DbSingleFileIndex(id)
	
	/**
	  * @param condition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique file indices.
	  * @return An access point to the file index that satisfies the specified condition
	  */
	protected def filterDistinct(condition: Condition) = UniqueFileIndexAccess(mergeCondition(condition))
}

