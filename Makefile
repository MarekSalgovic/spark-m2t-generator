generate: generate-pageRank generate-wordCount generate-datasetProcessing

generate-pageRank:
	java -Dfile.encoding=UTF-8 -classpath "./libs/*:./bin" -XX:+ShowCodeDetailsInExceptionMessages cz.vutbr.fit.dip.main.Main ./sparkPageRank/sparkPageRank.uml out/

generate-wordCount:
	java -Dfile.encoding=UTF-8 -classpath "./libs/*:./bin" -XX:+ShowCodeDetailsInExceptionMessages cz.vutbr.fit.dip.main.Main ./sparkWordCount/sparkWordCount.uml out/

generate-datasetProcessing:
	java -Dfile.encoding=UTF-8 -classpath "./libs/*:./bin" -XX:+ShowCodeDetailsInExceptionMessages cz.vutbr.fit.dip.main.Main ./dataset-processing/dataset-processing.uml out/

clean:
	rm -rf out/target out/src out/project/target out/project/project out/.bsp