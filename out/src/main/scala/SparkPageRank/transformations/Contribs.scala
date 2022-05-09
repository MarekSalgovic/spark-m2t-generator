package SparkPageRank.transformations


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
// Start of user code imports
// End of user code

class Contribs() {
	val S_rDDTransformation = true
	val TV_func = """join(ranks).values.flatMap{ case (urls, rank) =>
	        val size = urls.size
	        urls.map(url => (url, rank / size))
	      }"""
	
	def transform[T: TypeTag](rdd: RDD[T], ranks: RDD[(String, Double)]) = {
		typeOf[T] match {
			case type1 if type1 =:= typeOf[(String, Iterable[String])] =>
				rdd.asInstanceOf[RDD[(String, Iterable[String])]]
					.join(ranks).values.flatMap{ case (urls, rank) =>
					        val size = urls.size
					        urls.map(url => (url, rank / size))
					      }
		}
	}
	//Start of user code Contribs
	//End of user code
}
