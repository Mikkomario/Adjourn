package vf.adjourn.database.factory.file

import utopia.flow.generic.model.immutable.Model
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import vf.adjourn.database.AdjournTables
import vf.adjourn.model.partial.file.FileIndexData
import vf.adjourn.model.stored.file.FileIndex

/**
  * Used for reading file index data from the DB
  * @since 24.11.2023
  */
object FileIndexFactory extends FromValidatedRowModelFactory[FileIndex]
{
	// IMPLEMENTED	--------------------
	
	override def defaultOrdering = None
	
	override def table = AdjournTables.fileIndex
	
	override protected def fromValidatedModel(valid: Model) = 
		FileIndex(valid("id").getInt, FileIndexData(valid("directoryId").getInt, valid("name").getString, 
			valid("typeId").getInt, valid("size").getInt, valid("lastModified").instant))
}

