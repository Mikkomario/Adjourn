package vf.adjourn.database.access.single.file.`type`

type

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.adjourn.model.stored.file.FileType

/**
  * An access point to individual file types, based on their id
  * @since 24.11.2023
  */
case class DbSingleFileType(id: Int) extends UniqueFileTypeAccess with SingleIntIdModelAccess[FileType]

