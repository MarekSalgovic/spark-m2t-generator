package SparkPageRank.sources


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
// Start of user code imports
// End of user code

class FileSource(spark: SparkSession) {
	val S_fileSource = true
	val TV_options = """[]"""
	val TV_format = """org.eclipse.uml2.uml.internal.impl.EnumerationLiteralImpl@7544ac86 (name: Text, visibility: <unset>)"""
	val TV_dataType = """"""
	val TV_priority = """0"""
	val TV_filePath = """filePath"""
	
	def source(filePath: String) = {
			spark.sparkContext.textFile(filePath)
		
	}
}
