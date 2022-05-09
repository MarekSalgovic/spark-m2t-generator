package DatasetProcessing.actions


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
import DatasetProcessing.imported.City
// Start of user code imports
// End of user code
import DatasetProcessing.dataTypes._

class SaveToFile(spark: SparkSession) {
	val S_datasetAction = true
	val TV_func = """write.csv(outputPath)"""
	
	def action[T: TypeTag](rdd: RDD[T], outputPath: String) = {
		import spark.implicits._
		typeOf[T] match {
			case type1 if type1 =:= typeOf[Team] =>
				rdd.asInstanceOf[RDD[Team]]
					.toDS().write.csv(outputPath)
			case type2 if type2 =:= typeOf[City] =>
				rdd.asInstanceOf[RDD[City]]
					.toDS().write.csv(outputPath)
		}
	}
	//Start of user code Save To File
	//End of user code
}
