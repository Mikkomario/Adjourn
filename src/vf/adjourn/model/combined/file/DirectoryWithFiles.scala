package vf.adjourn.model.combined.file

import utopia.flow.view.template.Extender
import vf.adjourn.model.partial.file.DirectoryData
import vf.adjourn.model.stored.file.{Directory, FileIndex}

/**
  * Lists the files that appear within a directory
  * @since 24.11.2023
  */
case class DirectoryWithFiles(directory: Directory, files: Vector[FileIndex]) extends Extender[DirectoryData]
{
	// COMPUTED	--------------------
	
	/**
	  * Id of this directory in the database
	  */
	def id = directory.id
	
	
	// IMPLEMENTED	--------------------
	
	override def wrapped = directory.data
}

