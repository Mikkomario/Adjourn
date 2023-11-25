package vf.adjourn.model.partial.file

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.InstantType
import utopia.flow.generic.model.mutable.DataType.IntType
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible

import java.time.Instant

object FileIndexData extends FromModelFactoryWithSchema[FileIndexData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = 
		ModelDeclaration(Vector(PropertyDeclaration("directoryId", IntType, Vector("directory_id")), 
			PropertyDeclaration("name", StringType), PropertyDeclaration("typeId", IntType, 
			Vector("type_id")), PropertyDeclaration("size", IntType), PropertyDeclaration("lastModified", 
			InstantType, Vector("last_modified"), isOptional = true)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		FileIndexData(valid("directoryId").getInt, valid("name").getString, valid("typeId").getInt, 
			valid("size").getInt, valid("lastModified").instant)
}

/**
  * @param directoryId Id of the directory where this file resides
  * @param name Name of this file
  * @param typeId Id that matches this file's type
  * @param size Size of this file in bytes
  * @param lastModified Time when this file was last modified (may be out of date)
  */
case class FileIndexData(directoryId: Int, name: String, typeId: Int, size: Int, 
	lastModified: Option[Instant] = None) 
	extends ModelConvertible
{
	// IMPLEMENTED	--------------------
	
	override def toModel = 
		Model(Vector("directoryId" -> directoryId, "name" -> name, "typeId" -> typeId, "size" -> size, 
			"lastModified" -> lastModified))
}

