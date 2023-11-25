package vf.adjourn.database.access.single.file.directory

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.adjourn.database.factory.file.DirectoryFactory
import vf.adjourn.database.model.file.DirectoryModel
import vf.adjourn.model.stored.file.Directory

/**
  * Used for accessing individual directories
  * @since 24.11.2023
  */
object DbDirectory extends SingleRowModelAccess[Directory] with UnconditionalView with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Factory used for constructing database the interaction models
	  */
	protected def model = DirectoryModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DirectoryFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted directory
	  * @return An access point to that directory
	  */
	def apply(id: Int) = DbSingleDirectory(id)
	
	/**
	  * @param condition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique directories.
	  * @return An access point to the directory that satisfies the specified condition
	  */
	protected def filterDistinct(condition: Condition) = UniqueDirectoryAccess(mergeCondition(condition))
}

