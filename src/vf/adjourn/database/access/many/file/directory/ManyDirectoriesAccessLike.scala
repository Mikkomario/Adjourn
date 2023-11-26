package vf.adjourn.database.access.many.file.directory

import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import vf.adjourn.database.model.file.DirectoryModel

/**
  * A common trait for access points which target multiple directories or similar instances at a time
  * @since 24.11.2023
  */
trait ManyDirectoriesAccessLike[+A, +Repr] extends ManyModelAccess[A] with Indexed with FilterableView[Repr]
{
	// COMPUTED	--------------------
	
	/**
	  * parent ids of the accessible directories
	  */
	def parentIds(implicit connection: Connection) = pullColumn(model.parentIdColumn).flatMap { v => v.int }
	
	/**
	  * names of the accessible directories
	  */
	def names(implicit connection: Connection) = pullColumn(model.nameColumn).flatMap { _.string }
	
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
	/**
	  * Factory used for constructing database the interaction models
	  */
	protected def model = DirectoryModel
	
	/**
	 * @return Access to directories that don't have any parents
	 */
	def roots = filter(model.parentIdColumn.isNull)
	
	
	// OTHER	--------------------
	
	/**
	 * @param directoryId Id of the targeted directory
	 * @return Access to directories that exist directly below the specified directory
	 */
	def directlyUnderDirectory(directoryId: Int) = filter(model.withParentId(directoryId).toCondition)
	/**
	 * @param directoryIds Ids of the targeted directories
	 * @return Access to directories that appear directly under the specified directories
	 */
	def directlyUnderDirectories(directoryIds: Iterable[Int]) = filter(model.parentIdColumn in directoryIds)
	
	/**
	  * Updates the names of the targeted directories
	  * @param newName A new name to assign
	  * @return Whether any directory was affected
	  */
	def names_=(newName: String)(implicit connection: Connection) = putColumn(model.nameColumn, newName)
	
	/**
	  * Updates the parent ids of the targeted directories
	  * @param newParentId A new parent id to assign
	  * @return Whether any directory was affected
	  */
	def parentIds_=(newParentId: Int)(implicit connection: Connection) = 
		putColumn(model.parentIdColumn, newParentId)
}

