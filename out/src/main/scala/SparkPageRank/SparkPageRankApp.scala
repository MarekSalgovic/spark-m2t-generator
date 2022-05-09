package SparkPageRank


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
// Start of user code imports
// End of user code
import SparkPageRank.sources._
import SparkPageRank.transformations._
import SparkPageRank.actions._

object SparkPageRankApp {
	val S_sparkApplication = true
	val TV_arguments = """[org.eclipse.emf.ecore.impl.DynamicEObjectImpl@17461db (eClass: org.eclipse.emf.ecore.impl.EClassImpl@2c0f7678 (name: ProgramArgument) (instanceClassName: null) (abstract: false, interface: false)), org.eclipse.emf.ecore.impl.DynamicEObjectImpl@3f1a4795 (eClass: org.eclipse.emf.ecore.impl.EClassImpl@2c0f7678 (name: ProgramArgument) (instanceClassName: null) (abstract: false, interface: false))]"""
	val TV_conf = """[]"""
	val TV_imports = """[]"""
	val TV_initialCodeBlock = """"""
	val TV_master = """\"local[*]\""""
	
	
	def main(args: Array[String]) {
		//Start of user code pre-sparkSession
		//End of user code
		var iters = if (args.length > 0) args(0).toLong else 10
		var filePath = if (args.length > 1) args(1).toString else ""
				
		val conf = new SparkConf()
						.setMaster("local[*]")
						.setAppName("Spark Page Rank")
		val spark: SparkSession = SparkSession.builder.config(conf).getOrCreate()
		
		//Start of user code post-sparkSession
		//End of user code
		
		// variables
		var ranks: RDD[(String, Double)] = null
		
		
		// sources
		val S_fileSource = new FileSource(spark)
		
		// transformations
		val T_splitter = new Splitter()
		val T_countAdder = new CountAdder()
		val T_contribs = new Contribs()
		val T_calculateRanks = new CalculateRanks()
		
		
		// actions
		val A_collector = new Collector()
		
		

		var t_contribs: RDD[(String, Double)] = null
		var t_calculateRanks: RDD[(String, Double)] = null
		

		val s_fileSource = S_fileSource.source(filePath).asInstanceOf[RDD[String]];
		val t_splitter = T_splitter.transform(s_fileSource).asInstanceOf[RDD[(String, Iterable[String])]];
		val t_countAdder = T_countAdder.transform(t_splitter).asInstanceOf[RDD[(String, Double)]];
		ranks = t_countAdder
		for(loop <- 1 to iters.toInt) {
		t_contribs = T_contribs.transform(t_splitter, ranks).asInstanceOf[RDD[(String, Double)]];
		t_calculateRanks = T_calculateRanks.transform(t_contribs).asInstanceOf[RDD[(String, Double)]];
		ranks = t_calculateRanks
		}
		val a_collector = A_collector.action(t_calculateRanks);
		
		spark.stop()
		//Start of user code post-app
		//End of user code
	}
}
