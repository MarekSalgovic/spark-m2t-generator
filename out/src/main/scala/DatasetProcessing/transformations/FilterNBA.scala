package DatasetProcessing.transformations


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
import DatasetProcessing.imported.City
// Start of user code imports
// End of user code
import DatasetProcessing.dataTypes._

class FilterNBA(spark: SparkSession) {
	val S_datasetTransformation = true
	val TV_func = """filter($\"this.League\" === \"NBA\")"""
	
	def transform[T: TypeTag](rdd: RDD[T]) = {
		import spark.implicits._
		typeOf[T] match {
			case type1 if type1 =:= typeOf[Team] =>
				rdd.asInstanceOf[RDD[Team]]
					.toDS().as("this").filter($"this.League" === "NBA").as[Team].rdd
		}
	}
	//Start of user code Filter NBA
	//End of user code
}
