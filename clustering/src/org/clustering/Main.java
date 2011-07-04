package org.clustering;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.clustering.classifier.Classifier;
import org.clustering.data.CSVWriter;
import org.clustering.data.FileUtil;
import org.clustering.evaluator.Evaluator;
import org.clustering.evaluator.KeywordCount;
import org.clustering.model.Cluster;
import org.clustering.model.Item;
import org.clustering.model.ItemUtil;

public class Main {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		boolean production = true;
		boolean avgDist = true;

		if (production) {
			production(args, avgDist);
		}else{
			CSVWriter csvWriter = new CSVWriter(new File("./results/results.csv"), Arrays.asList("kCluster",
					"avgMae", "avgMse", "min", "max", "numClustersWithOneElement"));
			evaluateKs(csvWriter, avgDist);
			csvWriter.close();
		}

	}

	private static void evaluateKs(CSVWriter csvWriter, boolean avgDist)
			throws FileNotFoundException, IOException {
		
		System.out.println("Start reading data: " + new Date());
		FileUtil fileUtil = new FileUtil();
		fileUtil.readInput("data/keywords.txt");
		List<Item> items = fileUtil.getItems();
		Set<String> allKeywords = fileUtil.getAllKeywords();
		Set<String> uniqueKeywords = fileUtil.getUniqueKeywords();
		Set<String> nonUniqueKeywords = new HashSet<String>();
		nonUniqueKeywords.addAll(allKeywords);
		nonUniqueKeywords.removeAll(uniqueKeywords);

		calcDistances(items, nonUniqueKeywords);
		System.out.println("End reading data: " + new Date());
		Evaluator evaluator = new Evaluator();
		for (int kCluster = 2; kCluster < 201; kCluster += 2) {
			System.out.println("Start Clustering for k: " + kCluster + " : "
					+ new Date());
			Classifier classifier = new Classifier(kCluster, items, avgDist);
			List<Cluster> clusters = classifier.createClusters();
			writeCsvRecord(kCluster, clusters, evaluator, csvWriter);
			System.out.println("End Clustering: " + new Date());
		}

	}

	private static void writeCsvRecord(int kCluster, List<Cluster> clusters,
			Evaluator evaluator, CSVWriter csvWriter) {
		Map<String, Object> csvRecord = new HashMap<String, Object>();
		csvRecord.put("kCluster", kCluster);
		Double avgMae = evaluator
				.getAvgMeanAbsoluteError(clusters);
		csvRecord.put("avgMae", avgMae);
		Double avgMse = evaluator
				.getAvgMeanSquaredError(clusters);
		csvRecord.put("avgMse", avgMse);
		int min = evaluator.getMinItemPerCluster(clusters);
		csvRecord.put("min", min);
		int max = evaluator.getMaxItemPerCluster(clusters);
		csvRecord.put("max", max);
		int numClustersWithOneElement = evaluator
				.getNumClustersWithOneElements(clusters);
		csvRecord.put("numClustersWithOneElement", numClustersWithOneElement);
		csvWriter.writeRecord(csvRecord);
	}

	/**
	 * @param items
	 * @param nonUniqueKeywords
	 */
	private static void calcDistances(List<Item> items,
			Set<String> nonUniqueKeywords) {
		boolean useJacard = true;
		for (int i = 0; i < items.size(); i++) {
			for (int j = i + 1; j < items.size(); j++) {
				Item item1 = items.get(i);
				Item item2 = items.get(j);
				double distance = ItemUtil.calcDistance(item1, item2,
						nonUniqueKeywords, useJacard);
				item1.setDistance(item2, distance);
				item2.setDistance(item1, distance);
			}
		}
	}

	private static void production(String[] args, boolean avgDist) throws FileNotFoundException,
			IOException {
		boolean printEvaluation = true;
		int kCluster = 10;

		int filmId = getInputFilmId(args);
		if (filmId == -1) {
			System.out
					.println("Film Id invalid. Please give a file Id between 1 and 1682");
			return;
		}

		System.out.println("Start reading data: " + new Date());
		FileUtil fileUtil = new FileUtil();
		fileUtil.readInput("data/keywords.txt");
		List<Item> items = fileUtil.getItems();
		Set<String> allKeywords = fileUtil.getAllKeywords();
		Set<String> uniqueKeywords = fileUtil.getUniqueKeywords();
		Set<String> nonUniqueKeywords = new HashSet<String>();
		nonUniqueKeywords.addAll(allKeywords);
		nonUniqueKeywords.removeAll(uniqueKeywords);
		System.out.println(String.format("%d out of %d keywords are unique.",
				uniqueKeywords.size(), allKeywords.size()));

		calcDistances(items, nonUniqueKeywords);
		System.out.println("End reading data: " + new Date());

		System.out.println("Start Clustering: " + new Date());
		Classifier classifier = new Classifier(kCluster, items, avgDist);
		List<Cluster> clusters = classifier.createClusters();
		System.out.println("End Clustering: " + new Date());

		printSimilarFilms(clusters, filmId, items);
	}

	private static void printSimilarFilms(List<Cluster> clusters, int filmId,
			List<Item> items) {
		Item film = findItemById(filmId, items);
		if (film == null) {
			throw new IllegalStateException(
					"Could not find the film with given id: " + filmId);
		}
		Cluster filmCluster = null;
		for (Cluster cluster : clusters) {
			if (cluster.contains(film)) {
				filmCluster = cluster;
				break;
			}
		}
		if (filmCluster == null) {
			throw new IllegalStateException(
					"The given film is not contained in any cluster. Id: "
							+ filmId);
		}
		for (Item item : filmCluster.getMembers()) {
			System.out.print(item.toString());
			System.out.println(item.getKeywords());
		}

	}

	private static Item findItemById(int filmId, List<Item> items) {
		for (Item item : items) {
			if (item.getItemNumber() == filmId) {
				return item;
			}
		}
		return null;
	}

	private static int getInputFilmId(String[] args) {
		if (args.length > 1) {
			return -1;
		}
		int filmId = -1;
		try {
			filmId = Integer.valueOf(args[0]);

		} catch (NumberFormatException e) {
			filmId = -1;
		}
		if (filmId < 1 && filmId > 1682) {
			return -1;
		}
		return filmId;
	}

	private static void printEvaluationData(Evaluator evaluator,
			List<Cluster> clusters) {

		Map<Cluster, Double> mae = evaluator.getMeanAbsoluteError(clusters);
		Map<Cluster, Double> mse = evaluator.getMeanSquaredError(clusters);
		double avg = evaluator.getAvgItemPerCluster(clusters);
		int min = evaluator.getMinItemPerCluster(clusters);
		int max = evaluator.getMaxItemPerCluster(clusters);
		int numClustersWithOneElement = evaluator
				.getNumClustersWithOneElements(clusters);
		System.out
				.println(String
						.format("Avg per Cluster: %.2f ; Min: %d; Max: %d; NumClustersWithOneElement: %d",
								avg, min, max, numClustersWithOneElement));
		System.out.println();
		for (Cluster cluster : clusters) {
			System.out.println("Cluster " + cluster.getId() + "[ MAE: "
					+ mae.get(cluster) + " MSE: " + mse.get(cluster) + " ]");
		}

	}

	private static void printTopTenKeywordsPerCluster(Evaluator evaluator,
			List<Cluster> clusters) {
		Map<Cluster, List<KeywordCount>> topTenKeywordsPerCluster = evaluator
				.getTopTenKeywordsPerCluster(clusters);

		for (Cluster cluster : topTenKeywordsPerCluster.keySet()) {
			List<KeywordCount> topTenKeywordsInCluster = topTenKeywordsPerCluster
					.get(cluster);
			StringBuilder sbTopTenKeywords = new StringBuilder("");
			for (KeywordCount kc : topTenKeywordsInCluster) {
				sbTopTenKeywords.append(kc.toString());
				sbTopTenKeywords.append("; ");
			}
			System.out.printf("%s: [%s] \n", cluster.toString(),
					sbTopTenKeywords.toString());
		}
	}

}
