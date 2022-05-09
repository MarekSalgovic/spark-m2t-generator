all: pageRank wordCount datasetProcessing

pageRank:
	java -Dfile.encoding=UTF-8 -classpath "./libs/*:./bin" -XX:+ShowCodeDetailsInExceptionMessages cz.vutbr.fit.dip.main.Main ./sparkPageRank/sparkPageRank.uml out/

wordCount:
	java -Dfile.encoding=UTF-8 -classpath "./libs/*:./bin" -XX:+ShowCodeDetailsInExceptionMessages cz.vutbr.fit.dip.main.Main ./sparkWordCount/sparkWordCount.uml out/

datasetProcessing:
	java -Dfile.encoding=UTF-8 -classpath "./libs/*:./bin" -XX:+ShowCodeDetailsInExceptionMessages cz.vutbr.fit.dip.main.Main ./dataset-processing/dataset-processing.uml out/

clean:
	rm -rf out/*