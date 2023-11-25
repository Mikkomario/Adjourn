package vf.adjourn.database.access.many.file.directory

import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.nosql.view.UnconditionalView

/**
  * The root access point when targeting multiple directory with fileses at a time
  * @since 24.11.2023
  */
object DbDirectoriesWithFiles extends ManyDirectoriesWithFilesAccess with UnconditionalView
{
	// OTHER	--------------------
	
	/**
	  * @param ids Ids of the targeted directory with fileses
	  * @return An access point to directory with fileses with the specified ids
	  */
	def apply(ids: Set[Int]) = new DbDirectoriesWithFilesSubset(ids)
	
	
	// NESTED	--------------------
	
	class DbDirectoriesWithFilesSubset(targetIds: Set[Int]) extends ManyDirectoriesWithFilesAccess
	{
		// IMPLEMENTED	--------------------
		
		override def globalCondition = Some(index in targetIds)
	}
}

