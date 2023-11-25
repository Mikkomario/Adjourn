package vf.adjourn.database

import utopia.vault.database.Tables
import utopia.vault.model.immutable.Table
import vf.adjourn.util.Common._

/**
  * Used for accessing the database tables introduced in this project
  * @since 24.11.2023
  */
object AdjournTables extends Tables(cPool)
{
	// ATTRIBUTES   ----------------
	
	private val dbName = "adjourn_db"
	
	
	// COMPUTED	--------------------
	
	/**
	  * Table that contains directories (Represents a directory that may contain files)
	  */
	def directory = apply("directory")
	
	/**
	  * Table that contains file indices
	  */
	def fileIndex = apply("file_index")
	
	/**
	  * Table that contains file types (Represents a specific file type, such as a text file)
	  */
	def fileType = apply("file_type")
	
	
	// OTHER	--------------------
	
	private def apply(tableName: String): Table = apply(dbName, tableName)
}

