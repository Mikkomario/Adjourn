package vf.adjourn.database.factory.file

import utopia.flow.generic.model.immutable.Model
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import vf.adjourn.database.AdjournTables
import vf.adjourn.model.partial.file.DirectoryData
import vf.adjourn.model.stored.file.Directory

/**
  * Used for reading directory data from the DB
  * @since 24.11.2023
  */
object DirectoryFactory extends FromValidatedRowModelFactory[Directory]
{
	// IMPLEMENTED	--------------------
	
	override def defaultOrdering = None
	
	override def table = AdjournTables.directory
	
	override protected def fromValidatedModel(valid: Model) = 
		Directory(valid("id").getInt, DirectoryData(valid("parentId").int, valid("name").getString))
}

