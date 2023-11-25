package vf.adjourn.model.stored.file

import utopia.vault.model.template.StoredModelConvertible
import vf.adjourn.database.access.single.file.directory.DbSingleDirectory
import vf.adjourn.model.partial.file.DirectoryData

/**
  * Represents a directory that has already been stored in the database
  * @param id id of this directory in the database
  * @param data Wrapped directory data
  * @since 24.11.2023
  */
case class Directory(id: Int, data: DirectoryData) extends StoredModelConvertible[DirectoryData]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this directory in the database
	  */
	def access = DbSingleDirectory(id)
}

