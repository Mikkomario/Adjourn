package vf.adjourn.database.access.single.file.directory

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.FilterableView
import utopia.vault.sql.Condition
import vf.adjourn.database.factory.file.DirectoryFactory
import vf.adjourn.model.stored.file.Directory

object UniqueDirectoryAccess
{
	// OTHER	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	def apply(condition: Condition): UniqueDirectoryAccess = new _UniqueDirectoryAccess(condition)
	
	
	// NESTED	--------------------
	
	private class _UniqueDirectoryAccess(condition: Condition) extends UniqueDirectoryAccess
	{
		// IMPLEMENTED	--------------------
		
		override def globalCondition = Some(condition)
	}
}

/**
  * A common trait for access points that return individual and distinct directories.
  * @since 24.11.2023
  */
trait UniqueDirectoryAccess 
	extends UniqueDirectoryAccessLike[Directory] with SingleRowModelAccess[Directory] 
		with FilterableView[UniqueDirectoryAccess]
{
	// IMPLEMENTED	--------------------
	
	override def factory = DirectoryFactory
	
	override protected def self = this
	
	override def filter(filterCondition: Condition): UniqueDirectoryAccess = 
		new UniqueDirectoryAccess._UniqueDirectoryAccess(mergeCondition(filterCondition))
}

