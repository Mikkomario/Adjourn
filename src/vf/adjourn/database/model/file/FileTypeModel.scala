package vf.adjourn.database.model.file

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.vault.model.immutable.StorableWithFactory
import utopia.vault.nosql.storable.DataInserter
import vf.adjourn.database.factory.file.FileTypeFactory
import vf.adjourn.model.partial.file.FileTypeData
import vf.adjourn.model.stored.file.FileType

/**
  * Used for constructing FileTypeModel instances and for inserting file types to the database
  * @since 24.11.2023
  */
object FileTypeModel extends DataInserter[FileTypeModel, FileType, FileTypeData]
{
	// ATTRIBUTES	--------------------
	
	/**
	  * Name of the property that contains file type name
	  */
	val nameAttName = "name"
	
	
	// COMPUTED	--------------------
	
	/**
	  * Column that contains file type name
	  */
	def nameColumn = table(nameAttName)
	
	/**
	  * The factory object used by this model type
	  */
	def factory = FileTypeFactory
	
	
	// IMPLEMENTED	--------------------
	
	override def table = factory.table
	
	override def apply(data: FileTypeData) = apply(None, data.name)
	
	override protected def complete(id: Value, data: FileTypeData) = FileType(id.getInt, data)
	
	
	// OTHER	--------------------
	
	/**
	  * @param id A file type id
	  * @return A model with that id
	  */
	def withId(id: Int) = apply(Some(id))
	
	/**
	  * @param name Name of this file type
	  * @return A model containing only the specified name
	  */
	def withName(name: String) = apply(name = name)
}

/**
  * Used for interacting with FileTypes in the database
  * @param id file type database id
  * @since 24.11.2023
  */
case class FileTypeModel(id: Option[Int] = None, name: String = "") extends StorableWithFactory[FileType]
{
	// IMPLEMENTED	--------------------
	
	override def factory = FileTypeModel.factory
	
	override def valueProperties = {
		import FileTypeModel._
		Vector("id" -> id, nameAttName -> name)
	}
	
	
	// OTHER	--------------------
	
	/**
	  * @param name Name of this file type
	  * @return A new copy of this model with the specified name
	  */
	def withName(name: String) = copy(name = name)
}

