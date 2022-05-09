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

class TeamSource(spark: SparkSession) {
	val S_fileSource = true
	val TV_dataType = """org.eclipse.uml2.uml.internal.impl.DataTypeImpl@36ac8a63 (name: Team, visibility: <unset>) (isLeaf: false, isAbstract: false, isFinalSpecialization: false)"""
	val TV_priority = """1"""
	val TV_format = """org.eclipse.uml2.uml.internal.impl.EnumerationLiteralImpl@79b84841 (name: CSV, visibility: <unset>)"""
	val TV_filePath = """\"./teams.csv\""""
	val TV_options = """[]"""
	
	def source() = {
		import spark.implicits._
		spark.read.format("CSV")
			.option("inferSchema", "true").option("header", "true")
			.load("./teams.csv")
			.as[Team].rdd
		
	}
}
