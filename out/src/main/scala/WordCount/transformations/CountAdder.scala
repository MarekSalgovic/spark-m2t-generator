package WordCount.transformations


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
import scala.math.random
import scala.io.StdIn
// Start of user code imports
// End of user code

class CountAdder() {
	val S_rDDTransformation = true
	val TV_func = """map(x=>(x,1L))"""
	
	def transform[T: TypeTag](rdd: RDD[T]) = {
		typeOf[T] match {
			case type1 if type1 =:= typeOf[String] =>
				rdd.asInstanceOf[RDD[String]]
					.map(x=>(x,1L))
		}
	}
	//Start of user code CountAdder
	//End of user code
}
