package WordCount


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
import scala.math.random
import scala.io.StdIn
// Start of user code imports
// End of user code
import WordCount.sources._
import WordCount.transformations._
import WordCount.actions._

object WordCountApp {
	val S_sparkApplication = true
	val TV_initialCodeBlock = """spark.sparkContext.setLogLevel(\"ERROR\")"""
	val TV_arguments = """[org.eclipse.emf.ecore.impl.DynamicEObjectImpl@427ae189 (eClass: org.eclipse.emf.ecore.impl.EClassImpl@4ced35ed (name: ProgramArgument) (instanceClassName: null) (abstract: false, interface: false)), org.eclipse.emf.ecore.impl.DynamicEObjectImpl@d1d8e1a (eClass: org.eclipse.emf.ecore.impl.EClassImpl@4ced35ed (name: ProgramArgument) (instanceClassName: null) (abstract: false, interface: false)), org.eclipse.emf.ecore.impl.DynamicEObjectImpl@5434e40c (eClass: org.eclipse.emf.ecore.impl.EClassImpl@4ced35ed (name: ProgramArgument) (instanceClassName: null) (abstract: false, interface: false)), org.eclipse.emf.ecore.impl.DynamicEObjectImpl@514de325 (eClass: org.eclipse.emf.ecore.impl.EClassImpl@4ced35ed (name: ProgramArgument) (instanceClassName: null) (abstract: false, interface: false))]"""
	val TV_master = """argMaster"""
	val TV_conf = """[org.eclipse.emf.ecore.impl.DynamicEObjectImpl@69d23296 (eClass: org.eclipse.emf.ecore.impl.EClassImpl@610db97e (name: Option) (instanceClassName: null) (abstract: false, interface: false)), org.eclipse.emf.ecore.impl.DynamicEObjectImpl@3fba233d (eClass: org.eclipse.emf.ecore.impl.EClassImpl@610db97e (name: Option) (instanceClassName: null) (abstract: false, interface: false))]"""
	val TV_imports = """[scala.math.random, scala.io.StdIn]"""
	
	
	def main(args: Array[String]) {
		//Start of user code pre-sparkSession
		//End of user code
		var argMaster = if (args.length > 0) args(0).toString else "local[*]"
		var argInt = if (args.length > 1) args(1).toLong else 1
		var argReal = if (args.length > 2) args(2).toDouble else 1.1
		var argBool = if (args.length > 3) args(3).toBoolean else true
				
		val conf = new SparkConf()
						.setMaster(argMaster)
						.setAppName("WordCount")
						.set("something", "val")
						.set("something2", "val2")
		val spark: SparkSession = SparkSession.builder.config(conf).getOrCreate()
		spark.sparkContext.setLogLevel("ERROR")
		//Start of user code post-sparkSession
		//End of user code
		
		// variables
		
		// sources
		val S_parallelizer = new Parallelizer(spark)
		
		// transformations
		val T_wordSplitter = new WordSplitter()
		val T_countAdder = new CountAdder()
		val T_counter = new Counter()
		
		// actions
		val A_printer = new Printer()
		

		

		val s_parallelizer = S_parallelizer.source().asInstanceOf[RDD[String]];
		val t_wordSplitter = T_wordSplitter.transform(s_parallelizer).asInstanceOf[RDD[String]];
		val t_countAdder = T_countAdder.transform(t_wordSplitter).asInstanceOf[RDD[(String, Long)]];
		val t_counter = T_counter.transform(t_countAdder).asInstanceOf[RDD[(String, Long)]];
		val a_printer = A_printer.action(t_counter);
		println(a_printer.mkString(" "))
		
		spark.stop()
		//Start of user code post-app
		//End of user code
	}
}
