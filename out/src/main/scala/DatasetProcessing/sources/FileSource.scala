package DatasetProcessing.sources


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
import DatasetProcessing.imported.City
// Start of user code imports
// End of user code
import DatasetProcessing.dataTypes._

class FileSource(spark: SparkSession) {
	val S_fileSource = true
	val TV_filePath = """abbrFilePath"""
	val TV_priority = """5"""
	val TV_format = """org.eclipse.uml2.uml.internal.impl.EnumerationLiteralImpl@3ed7821 (name: CSV, visibility: <unset>)"""
	val TV_options = """[]"""
	val TV_dataType = """org.eclipse.uml2.uml.internal.impl.DataTypeImpl@4e9658b5 (name: Abbr, visibility: <unset>) (isLeaf: false, isAbstract: false, isFinalSpecialization: false)"""
	
	def source(abbrFilePath: String) = {
		import spark.implicits._
		spark.read.format("CSV")
			.option("inferSchema", "true").option("header", "true")
			.load(abbrFilePath)
			.as[Abbr].rdd
		
	}
}
