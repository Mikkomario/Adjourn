package vf.adjourn.model.partial.file

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.IntType
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible

object DirectoryData extends FromModelFactoryWithSchema[DirectoryData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = 
		ModelDeclaration(Vector(PropertyDeclaration("parentId", IntType, Vector("parent_id"), 
			isOptional = true), PropertyDeclaration("name", StringType)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		DirectoryData(valid("parentId").int, valid("name").getString)
}

/**
  * Represents a directory that may contain files
  * @param parentId Id of the directory that contains this directory. None if this is the root directory / 
	drive.
  * @since 24.11.2023
  */
case class DirectoryData(parentId: Option[Int], name: String) extends ModelConvertible
{
	// IMPLEMENTED	--------------------
	
	override def toModel = Model(Vector("parentId" -> parentId, "name" -> name))
}

