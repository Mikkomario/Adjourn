package vf.adjourn.database.access.many.file.directory

import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.nosql.view.UnconditionalView

/**
  * The root access point when targeting multiple directories at a time
  * @since 24.11.2023
  */
object DbDirectories extends ManyDirectoriesAccess with UnconditionalView
{
	// OTHER	--------------------
	
	/**
	  * @param ids Ids of the targeted directories
	  * @return An access point to directories with the specified ids
	  */
	def apply(ids: Set[Int]) = new DbDirectoriesSubset(ids)
	
	
	// NESTED	--------------------
	
	class DbDirectoriesSubset(targetIds: Set[Int]) extends ManyDirectoriesAccess
	{
		// COMPUTED ----------------
		
		/**
		 * @return Access to directories directly under these directories
		 */
		def subDirectories = DbDirectories.directlyUnderDirectories(targetIds)
		
		
		// IMPLEMENTED	--------------------
		
		override def globalCondition = Some(index in targetIds)
	}
}

