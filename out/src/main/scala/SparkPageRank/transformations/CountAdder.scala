package SparkPageRank.transformations


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
// Start of user code imports
// End of user code

class CountAdder() {
	val S_rDDTransformation = true
	val TV_func = """mapValues(x=>1.0)"""
	
	def transform[T: TypeTag](rdd: RDD[T]) = {
		typeOf[T] match {
			case type1 if type1 =:= typeOf[(String, Iterable[String])] =>
				rdd.asInstanceOf[RDD[(String, Iterable[String])]]
					.mapValues(x=>1.0)
		}
	}
	//Start of user code Count Adder
	//End of user code
}
