package vf.adjourn.database.factory.file

import utopia.flow.generic.model.immutable.Model
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import vf.adjourn.database.AdjournTables
import vf.adjourn.model.partial.file.FileTypeData
import vf.adjourn.model.stored.file.FileType

/**
  * Used for reading file type data from the DB
  * @since 24.11.2023
  */
object FileTypeFactory extends FromValidatedRowModelFactory[FileType]
{
	// IMPLEMENTED	--------------------
	
	override def defaultOrdering = None
	
	override def table = AdjournTables.fileType
	
	override protected def fromValidatedModel(valid: Model) = 
		FileType(valid("id").getInt, FileTypeData(valid("name").getString))
}

