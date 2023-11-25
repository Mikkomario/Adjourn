package vf.adjourn.database.access.single.file.directory

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.adjourn.model.stored.file.Directory

/**
  * An access point to individual directories, based on their id
  * @since 24.11.2023
  */
case class DbSingleDirectory(id: Int) extends UniqueDirectoryAccess with SingleIntIdModelAccess[Directory]

