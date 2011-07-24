package org.clustering.mode.modes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.clustering.classifier.Classifier;
import org.clustering.data.CSVWriter;
import org.clustering.data.DataUtil;
import org.clustering.evaluator.Evaluator;
import org.clustering.mode.AbstractMode;
import org.clustering.model.Cluster;
import org.clustering.model.DistanceTypes;

public class EvaluateMode extends AbstractMode {

	public EvaluateMode() {
		super("-evaluate", "To run evalutation mode use: \njava -jar clustering.jar -evaluate");
	}
	
	public void _runEvaluate() throws Exception {
		CSVWriter csvWriter = new CSVWriter(new File("./results/results.csv"),
				Arrays.asList("kCluster", "avgMae", "avgMse", "min", "max",
						"numClustersWithOneElement", "RSS", "numRuns"));
		evaluateKs(csvWriter);
		csvWriter.close();
	}

	private void evaluateKs(CSVWriter csvWriter) throws Exception {
		DataUtil dataUtil = new DataUtil();
		int nAtLeastKeywords = 10;
		dataUtil.readData(true, nAtLeastKeywords);
		Set<String> keywords = dataUtil.getAtLeastNTimesKeywords(nAtLeastKeywords);
		Evaluator evaluator = new Evaluator(keywords,
				DistanceTypes.JACCARD_SIMILARITY);
		for (int kCluster = 2; kCluster < 201; kCluster += 2) {
			System.out.println("Start Clustering for k: " + kCluster + " : "
					+ new Date());
			Classifier classifier = new Classifier(kCluster, dataUtil.getItems());
			List<Cluster> clusters = classifier.createClusters();
			int numRuns = classifier.getNumRuns();
			writeCsvRecord(kCluster, clusters, numRuns, evaluator, csvWriter);
			System.out.println("End Clustering: " + new Date());
		}
	}
	
	private void writeCsvRecord(int kCluster, List<Cluster> clusters,
			int numRuns, Evaluator evaluator, CSVWriter csvWriter) {
		Map<String, Object> csvRecord = new HashMap<String, Object>();
		csvRecord.put("kCluster", kCluster);
	
		Double avgMae = evaluator.getAvgMeanAbsoluteError(clusters);
		csvRecord.put("avgMae", avgMae);
		
		Double avgMse = evaluator.getAvgMeanSquaredError(clusters);
		csvRecord.put("avgMse", avgMse);
		
		int min = evaluator.getMinItemPerCluster(clusters);
		csvRecord.put("min", min);
		
		int max = evaluator.getMaxItemPerCluster(clusters);
		csvRecord.put("max", max);
		
		int numClustersWithOneElement = evaluator
				.getNumClustersWithOneElements(clusters);
		csvRecord.put("numClustersWithOneElement", numClustersWithOneElement);
		
		double rss = evaluator.getRSS(clusters);
		csvRecord.put("RSS", rss);
		
		csvRecord.put("numRuns", numRuns);
		
		csvWriter.writeRecord(csvRecord);
	}
	
}
