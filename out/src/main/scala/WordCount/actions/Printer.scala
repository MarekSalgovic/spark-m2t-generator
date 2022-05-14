package WordCount.actions


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
import scala.math.random
import scala.io.StdIn
// Start of user code imports
// End of user code

class Printer() {
	val S_rDDAction = true
	val TV_func = """collect()"""
	
	def action[T: TypeTag](rdd: RDD[T]) = {
		typeOf[T] match {
			case type1 if type1 =:= typeOf[(String, Long)] =>
				rdd.asInstanceOf[RDD[(String, Long)]]
					.collect()
		}
	}
	//Start of user code Printer
	//End of user code
}
