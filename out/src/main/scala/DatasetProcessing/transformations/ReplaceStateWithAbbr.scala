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

class ReplaceStateWithAbbr(spark: SparkSession) {
	val S_datasetTransformation = true
	val TV_func = """joinWith(abbrs.toDS().as(\"abbr\"), $\"this.State\" === $\"abbr.State\", \"inner\")
	          .map(x => x._1.copy(State = x._2.Abbreviation))"""
	
	def transform[T: TypeTag](rdd: RDD[T], abbrs: RDD[Abbr]) = {
		import spark.implicits._
		typeOf[T] match {
			case type1 if type1 =:= typeOf[City] =>
				rdd.asInstanceOf[RDD[City]]
					.toDS().as("this").joinWith(abbrs.toDS().as("abbr"), $"this.State" === $"abbr.State", "inner")
					          .map(x => x._1.copy(State = x._2.Abbreviation)).as[City].rdd
			case type2 if type2 =:= typeOf[Team] =>
				rdd.asInstanceOf[RDD[Team]]
					.toDS().as("this").joinWith(abbrs.toDS().as("abbr"), $"this.State" === $"abbr.State", "inner")
					          .map(x => x._1.copy(State = x._2.Abbreviation)).as[Team].rdd
		}
	}
	//Start of user code ReplaceStateWithAbbr
	//End of user code
}
