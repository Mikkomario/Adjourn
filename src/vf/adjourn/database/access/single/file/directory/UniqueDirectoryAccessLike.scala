package vf.adjourn.database.access.single.file.directory

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import vf.adjourn.database.model.file.DirectoryModel

/**
  * A common trait for access points which target individual directories or similar items at a time
  * @since 24.11.2023
  */
trait UniqueDirectoryAccessLike[+A] 
	extends SingleModelAccess[A] with DistinctModelAccess[A, Option[A], Value] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the directory that contains this directory. None if this is the root directory / 
	  * drive.. None if no directory (or value) was found.
	  */
	def parentId(implicit connection: Connection) = pullColumn(model.parentIdColumn).int
	
	/**
	  * The name of this directory. None if no directory (or value) was found.
	  */
	def name(implicit connection: Connection) = pullColumn(model.nameColumn).getString
	
	def id(implicit connection: Connection) = pullColumn(index).int
	
	/**
	  * Factory used for constructing database the interaction models
	  */
	protected def model = DirectoryModel
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the names of the targeted directories
	  * @param newName A new name to assign
	  * @return Whether any directory was affected
	  */
	def name_=(newName: String)(implicit connection: Connection) = putColumn(model.nameColumn, newName)
	
	/**
	  * Updates the parent ids of the targeted directories
	  * @param newParentId A new parent id to assign
	  * @return Whether any directory was affected
	  */
	def parentId_=(newParentId: Int)(implicit connection: Connection) = 
		putColumn(model.parentIdColumn, newParentId)
}

