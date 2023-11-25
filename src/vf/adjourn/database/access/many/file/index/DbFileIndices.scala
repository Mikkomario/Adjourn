package vf.adjourn.database.access.many.file.index

import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.nosql.view.UnconditionalView

/**
  * The root access point when targeting multiple file indices at a time
  * @since 24.11.2023
  */
object DbFileIndices extends ManyFileIndicesAccess with UnconditionalView
{
	// OTHER	--------------------
	
	/**
	  * @param ids Ids of the targeted file indices
	  * @return An access point to file indices with the specified ids
	  */
	def apply(ids: Set[Int]) = new DbFileIndicesSubset(ids)
	
	
	// NESTED	--------------------
	
	class DbFileIndicesSubset(targetIds: Set[Int]) extends ManyFileIndicesAccess
	{
		// IMPLEMENTED	--------------------
		
		override def globalCondition = Some(index in targetIds)
	}
}

