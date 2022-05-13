package:
	sbt package

run-dataset: package
	spark-submit --class "DatasetProcessing.DatasetProcessingApp" ./target/scala-2.12/dip_2.12-1.0.0.jar

run-wordcount: package
	spark-submit --class "WordCount.WordCountApp" ./target/scala-2.12/dip_2.12-1.0.0.jar

run-pagerank: package
	spark-submit --class "SparkPageRank.SparkPageRankApp" ./target/scala-2.12/dip_2.12-1.0.0.jar 100 "./url_data.csv"

clean:
	rm -rf *-out.csv target project/target project/project