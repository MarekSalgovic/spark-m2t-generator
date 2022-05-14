package SparkPageRank.transformations


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
// Start of user code imports
// End of user code

class Splitter() {
	val S_rDDTransformation = true
	val TV_func = """map{ s =>
	      val parts = s.split(\"\\s+\")
	      (parts(0), parts(1))
	    }.distinct().groupByKey()"""
	
	def transform[T: TypeTag](rdd: RDD[T]) = {
		typeOf[T] match {
			case type1 if type1 =:= typeOf[String] =>
				rdd.asInstanceOf[RDD[String]]
					.map{ s =>
					      val parts = s.split("\\s+")
					      (parts(0), parts(1))
					    }.distinct().groupByKey()
		}
	}
	//Start of user code Splitter
	//End of user code
}
