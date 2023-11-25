package vf.adjourn.database.access.single.file.index

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.adjourn.model.stored.file.FileIndex

/**
  * An access point to individual file indices, based on their id
  * @since 24.11.2023
  */
case class DbSingleFileIndex(id: Int) extends UniqueFileIndexAccess with SingleIntIdModelAccess[FileIndex]

