package DatasetProcessing


import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
import DatasetProcessing.imported.City
// Start of user code imports
// End of user code
import DatasetProcessing.sources._
import DatasetProcessing.transformations._
import DatasetProcessing.actions._
import DatasetProcessing.dataTypes._

object DatasetProcessingApp {
	val S_sparkApplication = true
	val TV_imports = """[DatasetProcessing.imported.City]"""
	val TV_initialCodeBlock = """"""
	val TV_master = """\"local[*]\""""
	val TV_arguments = """[org.eclipse.emf.ecore.impl.DynamicEObjectImpl@201c3cda (eClass: org.eclipse.emf.ecore.impl.EClassImpl@33aa93c (name: ProgramArgument) (instanceClassName: null) (abstract: false, interface: false))]"""
	val TV_conf = """[]"""
	
	
	def main(args: Array[String]) {
		//Start of user code pre-sparkSession
		//End of user code
		var abbrFilePath = if (args.length > 0) args(0).toString else "./abbr.csv"
				
		val conf = new SparkConf()
						.setMaster("local[*]")
						.setAppName("Dataset Processing")
		val spark: SparkSession = SparkSession.builder.config(conf).getOrCreate()
		
		//Start of user code post-sparkSession
		//End of user code
		
		// variables
		var abbrs: RDD[Abbr] = null
		var outputPath: String = null
		
		// sources
		val S_fileSource = new FileSource(spark)
		val S_teamSource = new TeamSource(spark)
		val S_citySource = new CitySource(spark)
		
		// transformations
		val T_replaceStateWithAbbr = new ReplaceStateWithAbbr(spark)
		val T_filterNBA = new FilterNBA(spark)
		
		// actions
		val A_saveToFile = new SaveToFile(spark)
		

		

		val s_fileSource = S_fileSource.source(abbrFilePath);
		abbrs = s_fileSource
		val s_citySource = S_citySource.source();
		val t_replaceStateWithAbbr_city = T_replaceStateWithAbbr.transform(s_citySource, abbrs).asInstanceOf[RDD[City]];
		outputPath = "./cities-out.csv"
		val a_saveToFile_city = A_saveToFile.action(t_replaceStateWithAbbr_city, outputPath);
		val s_teamSource = S_teamSource.source();
		val t_replaceStateWithAbbr_team = T_replaceStateWithAbbr.transform(s_teamSource, abbrs).asInstanceOf[RDD[Team]];
		val t_filterNBA_team = T_filterNBA.transform(t_replaceStateWithAbbr_team);
		outputPath = "./teams-out.csv"
		val a_saveToFile_team = A_saveToFile.action(t_filterNBA_team, outputPath);
		
		spark.stop()
		//Start of user code post-app
		//End of user code
	}
}
