package vf.adjourn.database.access.many.file.`type`

type

import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.nosql.view.UnconditionalView

/**
  * The root access point when targeting multiple file types at a time
  * @since 24.11.2023
  */
object DbFileTypes extends ManyFileTypesAccess with UnconditionalView
{
	// OTHER	--------------------
	
	/**
	  * @param ids Ids of the targeted file types
	  * @return An access point to file types with the specified ids
	  */
	def apply(ids: Set[Int]) = new DbFileTypesSubset(ids)
	
	
	// NESTED	--------------------
	
	class DbFileTypesSubset(targetIds: Set[Int]) extends ManyFileTypesAccess
	{
		// IMPLEMENTED	--------------------
		
		override def globalCondition = Some(index in targetIds)
	}
}

