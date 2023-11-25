package vf.adjourn.database.access.single.file.directory

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.adjourn.model.combined.file.DirectoryWithFiles

/**
  * An access point to individual directory with fileses, based on their directory id
  * @since 24.11.2023
  */
case class DbSingleDirectoryWithFiles(id: Int) 
	extends UniqueDirectoryWithFilesAccess with SingleIntIdModelAccess[DirectoryWithFiles]

