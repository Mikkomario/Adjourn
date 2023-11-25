package vf.adjourn.database.model.file

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.vault.model.immutable.StorableWithFactory
import utopia.vault.nosql.storable.DataInserter
import vf.adjourn.database.factory.file.FileIndexFactory
import vf.adjourn.model.partial.file.FileIndexData
import vf.adjourn.model.stored.file.FileIndex

import java.time.Instant

/**
  * Used for constructing FileIndexModel instances and for inserting file indices to the database
  * @since 24.11.2023
  */
object FileIndexModel extends DataInserter[FileIndexModel, FileIndex, FileIndexData]
{
	// ATTRIBUTES	--------------------
	
	/**
	  * Name of the property that contains file index directory id
	  */
	val directoryIdAttName = "directoryId"
	
	/**
	  * Name of the property that contains file index name
	  */
	val nameAttName = "name"
	
	/**
	  * Name of the property that contains file index type id
	  */
	val typeIdAttName = "typeId"
	
	/**
	  * Name of the property that contains file index size
	  */
	val sizeAttName = "size"
	
	/**
	  * Name of the property that contains file index last modified
	  */
	val lastModifiedAttName = "lastModified"
	
	
	// COMPUTED	--------------------
	
	/**
	  * Column that contains file index directory id
	  */
	def directoryIdColumn = table(directoryIdAttName)
	
	/**
	  * Column that contains file index name
	  */
	def nameColumn = table(nameAttName)
	
	/**
	  * Column that contains file index type id
	  */
	def typeIdColumn = table(typeIdAttName)
	
	/**
	  * Column that contains file index size
	  */
	def sizeColumn = table(sizeAttName)
	
	/**
	  * Column that contains file index last modified
	  */
	def lastModifiedColumn = table(lastModifiedAttName)
	
	/**
	  * The factory object used by this model type
	  */
	def factory = FileIndexFactory
	
	
	// IMPLEMENTED	--------------------
	
	override def table = factory.table
	
	override def apply(data: FileIndexData) = 
		apply(None, Some(data.directoryId), data.name, Some(data.typeId), Some(data.size), data.lastModified)
	
	override protected def complete(id: Value, data: FileIndexData) = FileIndex(id.getInt, data)
	
	
	// OTHER	--------------------
	
	/**
	  * @param directoryId Id of the directory where this file resides
	  * @return A model containing only the specified directory id
	  */
	def withDirectoryId(directoryId: Int) = apply(directoryId = Some(directoryId))
	
	/**
	  * @param id A file index id
	  * @return A model with that id
	  */
	def withId(id: Int) = apply(Some(id))
	
	/**
	  * @param lastModified Time when this file was last modified (may be out of date)
	  * @return A model containing only the specified last modified
	  */
	def withLastModified(lastModified: Instant) = apply(lastModified = Some(lastModified))
	
	/**
	  * @param name Name of this file
	  * @return A model containing only the specified name
	  */
	def withName(name: String) = apply(name = name)
	
	/**
	  * @param size Size of this file in bytes
	  * @return A model containing only the specified size
	  */
	def withSize(size: Int) = apply(size = Some(size))
	
	/**
	  * @param typeId Id that matches this file's type
	  * @return A model containing only the specified type id
	  */
	def withTypeId(typeId: Int) = apply(typeId = Some(typeId))
}

/**
  * Used for interacting with FileIndices in the database
  * @param id file index database id
  * @since 24.11.2023
  */
case class FileIndexModel(id: Option[Int] = None, directoryId: Option[Int] = None, name: String = "", 
	typeId: Option[Int] = None, size: Option[Int] = None, lastModified: Option[Instant] = None) 
	extends StorableWithFactory[FileIndex]
{
	// IMPLEMENTED	--------------------
	
	override def factory = FileIndexModel.factory
	
	override def valueProperties = {
		import FileIndexModel._
		Vector("id" -> id, directoryIdAttName -> directoryId, nameAttName -> name, typeIdAttName -> typeId, 
			sizeAttName -> size, lastModifiedAttName -> lastModified)
	}
	
	
	// OTHER	--------------------
	
	/**
	  * @param directoryId Id of the directory where this file resides
	  * @return A new copy of this model with the specified directory id
	  */
	def withDirectoryId(directoryId: Int) = copy(directoryId = Some(directoryId))
	
	/**
	  * @param lastModified Time when this file was last modified (may be out of date)
	  * @return A new copy of this model with the specified last modified
	  */
	def withLastModified(lastModified: Instant) = copy(lastModified = Some(lastModified))
	
	/**
	  * @param name Name of this file
	  * @return A new copy of this model with the specified name
	  */
	def withName(name: String) = copy(name = name)
	
	/**
	  * @param size Size of this file in bytes
	  * @return A new copy of this model with the specified size
	  */
	def withSize(size: Int) = copy(size = Some(size))
	
	/**
	  * @param typeId Id that matches this file's type
	  * @return A new copy of this model with the specified type id
	  */
	def withTypeId(typeId: Int) = copy(typeId = Some(typeId))
}

