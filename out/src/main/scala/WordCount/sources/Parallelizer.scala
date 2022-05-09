package WordCount.sources


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
import scala.math.random
import scala.io.StdIn
// Start of user code imports
// End of user code

class Parallelizer(spark: SparkSession) {
	val S_parallelize = true
	val TV_array = """Seq(\"hello world\", \"hello spark\", \"spark uml\")"""
	val TV_priority = """0"""
	
	def source() = {
		spark.sparkContext.parallelize(Seq("hello world", "hello spark", "spark uml"))
	}
}
