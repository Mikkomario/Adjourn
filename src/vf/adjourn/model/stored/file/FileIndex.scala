package vf.adjourn.model.stored.file

import utopia.vault.model.template.StoredModelConvertible
import vf.adjourn.database.access.single.file.index.DbSingleFileIndex
import vf.adjourn.model.partial.file.FileIndexData

/**
  * Represents a file index that has already been stored in the database
  * @param id id of this file index in the database
  * @param data Wrapped file index data
  * @since 24.11.2023
  */
case class FileIndex(id: Int, data: FileIndexData) extends StoredModelConvertible[FileIndexData]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this file index in the database
	  */
	def access = DbSingleFileIndex(id)
}

