package SparkPageRank.actions


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
// Start of user code imports
// End of user code

class Collector() {
	val S_rDDAction = true
	val TV_func = """collect().foreach(tup => println(s\"${tup._1} has rank:  ${tup._2} .\"))"""
	
	def action[T: TypeTag](rdd: RDD[T]) = {
		typeOf[T] match {
			case type1 if type1 =:= typeOf[(String, Double)] =>
				rdd.asInstanceOf[RDD[(String, Double)]]
					.collect().foreach(tup => println(s"${tup._1} has rank:  ${tup._2} ."))
		}
	}
	//Start of user code Collector
	//End of user code
}
