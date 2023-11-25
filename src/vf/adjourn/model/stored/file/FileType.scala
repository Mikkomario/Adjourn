package vf.adjourn.model.stored.file

import utopia.vault.model.template.StoredModelConvertible
import vf.adjourn.database.access.single.file.type.DbSingleFileType
import vf.adjourn.model.partial.file.FileTypeData

/**
  * Represents a file type that has already been stored in the database
  * @param id id of this file type in the database
  * @param data Wrapped file type data
  * @since 24.11.2023
  */
case class FileType(id: Int, data: FileTypeData) extends StoredModelConvertible[FileTypeData]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this file type in the database
	  */
	def access = DbSingleFileType(id)
}

