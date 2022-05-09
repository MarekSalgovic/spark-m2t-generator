package SparkPageRank.transformations


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
// Start of user code imports
// End of user code

class CalculateRanks() {
	val S_rDDTransformation = true
	val TV_func = """reduceByKey(_ + _).mapValues(0.15 + 0.85 * _)"""
	
	def transform[T: TypeTag](rdd: RDD[T]) = {
		typeOf[T] match {
			case type1 if type1 =:= typeOf[(String, Double)] =>
				rdd.asInstanceOf[RDD[(String, Double)]]
					.reduceByKey(_ + _).mapValues(0.15 + 0.85 * _)
		}
	}
	//Start of user code Calculate Ranks
	//End of user code
}
