package vf.adjourn.database.model.file

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.vault.model.immutable.StorableWithFactory
import utopia.vault.nosql.storable.DataInserter
import vf.adjourn.database.factory.file.DirectoryFactory
import vf.adjourn.model.partial.file.DirectoryData
import vf.adjourn.model.stored.file.Directory

/**
  * Used for constructing DirectoryModel instances and for inserting directories to the database
  * @since 24.11.2023
  */
object DirectoryModel extends DataInserter[DirectoryModel, Directory, DirectoryData]
{
	// ATTRIBUTES	--------------------
	
	/**
	  * Name of the property that contains directory parent id
	  */
	val parentIdAttName = "parentId"
	
	/**
	  * Name of the property that contains directory name
	  */
	val nameAttName = "name"
	
	
	// COMPUTED	--------------------
	
	/**
	  * Column that contains directory parent id
	  */
	def parentIdColumn = table(parentIdAttName)
	
	/**
	  * Column that contains directory name
	  */
	def nameColumn = table(nameAttName)
	
	/**
	  * The factory object used by this model type
	  */
	def factory = DirectoryFactory
	
	
	// IMPLEMENTED	--------------------
	
	override def table = factory.table
	
	override def apply(data: DirectoryData) = apply(None, data.parentId, data.name)
	
	override protected def complete(id: Value, data: DirectoryData) = Directory(id.getInt, data)
	
	
	// OTHER	--------------------
	
	/**
	  * @param id A directory id
	  * @return A model with that id
	  */
	def withId(id: Int) = apply(Some(id))
	
	/**
	  * @return A model containing only the specified name
	  */
	def withName(name: String) = apply(name = name)
	
	/**
	  * 
		@param parentId Id of the directory that contains this directory. None if this is the root directory / 
		drive.
	  * @return A model containing only the specified parent id
	  */
	def withParentId(parentId: Int) = apply(parentId = Some(parentId))
}

/**
  * Used for interacting with Directories in the database
  * @param id directory database id
  * @since 24.11.2023
  */
case class DirectoryModel(id: Option[Int] = None, parentId: Option[Int] = None, name: String = "") 
	extends StorableWithFactory[Directory]
{
	// IMPLEMENTED	--------------------
	
	override def factory = DirectoryModel.factory
	
	override def valueProperties = {
		import DirectoryModel._
		Vector("id" -> id, parentIdAttName -> parentId, nameAttName -> name)
	}
	
	
	// OTHER	--------------------
	
	/**
	  * @return A new copy of this model with the specified name
	  */
	def withName(name: String) = copy(name = name)
	
	/**
	  * 
		@param parentId Id of the directory that contains this directory. None if this is the root directory / 
		drive.
	  * @return A new copy of this model with the specified parent id
	  */
	def withParentId(parentId: Int) = copy(parentId = Some(parentId))
}

