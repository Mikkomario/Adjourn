package vf.adjourn.database.access.many.file.directory

import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.sql.Condition
import vf.adjourn.database.factory.file.DirectoryFactory
import vf.adjourn.model.stored.file.Directory

object ManyDirectoriesAccess
{
	// NESTED	--------------------
	
	private class ManyDirectoriesSubView(condition: Condition) extends ManyDirectoriesAccess
	{
		// IMPLEMENTED	--------------------
		
		override def globalCondition = Some(condition)
	}
}

/**
  * A common trait for access points which target multiple directories at a time
  * @since 24.11.2023
  */
trait ManyDirectoriesAccess 
	extends ManyDirectoriesAccessLike[Directory, ManyDirectoriesAccess] with ManyRowModelAccess[Directory]
{
	// IMPLEMENTED	--------------------
	
	override def factory = DirectoryFactory
	
	override protected def self = this
	
	override def filter(filterCondition: Condition): ManyDirectoriesAccess = 
		new ManyDirectoriesAccess.ManyDirectoriesSubView(mergeCondition(filterCondition))
}

