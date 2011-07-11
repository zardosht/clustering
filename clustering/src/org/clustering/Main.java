package org.clustering;

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
import org.clustering.data.FileUtil;
import org.clustering.evaluator.Evaluator;
import org.clustering.evaluator.KeywordCount;
import org.clustering.model.Cluster;
import org.clustering.model.Item;
import org.clustering.model.ItemUtil;
import org.clustering.util.VisualisationUtil;

public class Main {

	private static FileUtil fileUtil;
	private static List<Item> items;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//readData();
		
		if (args.length == 1 && args[0].equals("-evaluate")) {
			evaluate();
		} else if (args.length == 2 && args[0].equals("-cluster")) {
			int kCluster = Integer.parseInt(args[1]);
			createCluster(kCluster);
		} else if (args.length == 3) {
			int inputFilmId = getInputFilmId(args);
			int kCluster = 20;
			try {
				kCluster = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if (inputFilmId == -1) {
				System.out
						.println("Film Id invalid. Please give a file Id between 1 and 1682");
				return;
			}
			boolean outputKeywords = new Boolean(args[2]);
			production(inputFilmId, "results/k" + kCluster + ".res",
					outputKeywords);

		} else {
			printUsage();
		}

	}

	private static void createCluster(int kCluster) throws Exception {
		Set<String> nonUniqueKeywords = readData();
		System.out.println("Start Clustering: " + new Date());
		Classifier classifier = new Classifier(kCluster, items);
		List<Cluster> clusters = classifier.createClusters();
		System.out.println("End Clustering: " + new Date());

		System.out.println("Start writing result file " + new Date());
		fileUtil.wirteClusteringResult(
				new File("results/k" + kCluster + ".res"), clusters);
		System.out.println("End writing result file " + new Date());

		printTopTenKeywordsPerCluster(clusters);
		
		new VisualisationUtil(nonUniqueKeywords, "results/clusteredItems.png").drawClusters(clusters);
	}
	
	private static Set<String> readData() throws FileNotFoundException, IOException{
		System.out.println("Start reading data: " + new Date());
		fileUtil = new FileUtil();
		fileUtil.readInput("data/keywords.txt");
		items = fileUtil.getItems();
		Set<String> allKeywords = fileUtil.getAllKeywords();
		Set<String> uniqueKeywords = fileUtil.getUniqueKeywords();
		Set<String> nonUniqueKeywords = fileUtil.getNonUniqueKeywords();
		System.out.println(String.format("%d out of %d keywords are unique.",
				uniqueKeywords.size(), allKeywords.size()));
		
		System.out.println("Starting Visualisation: " + new Date());
		new VisualisationUtil(nonUniqueKeywords, "results/items_before_clustering.png").drawItems(items);
		System.out.println("End Visualisation: " + new Date());

		System.out.println("Starting calcDistance: " + new Date());
		calcDistances(items, nonUniqueKeywords);
		System.out.println("End calcDistance: " + new Date());
		System.out.println("End reading data: " + new Date());
		return nonUniqueKeywords;
	}

	private static void printUsage() {
		System.out.println("Usage: ");
		System.out
				.println("To cluster the data set. kCluster is an integer for number of clusters:");
		System.out.println("java -jar clustering.jar -cluster -kCluster");
		System.out.println();
		System.out.println("To start evaluation:");
		System.out.println("java -jar clustering.jar -evaluate");
		System.out.println();
		System.out
				.println("To use the clustering results to find similar films. filmId must be between 1 and 1682; kCluster is one of 10, 20, or 70; outputKeyword to print out keywords of films (warning frightening output on the console ;) )");
		System.out.println("java -jar clustering.jar filmId kCluster outputKeywords");

	}

	private static void evaluate() throws FileNotFoundException, IOException {
		CSVWriter csvWriter = new CSVWriter(new File("./results/results.csv"),
				Arrays.asList("kCluster", "avgMae", "avgMse", "min", "max",
						"numClustersWithOneElement"));
		evaluateKs(csvWriter, true);
		csvWriter.close();
	}

	private static void evaluateKs(CSVWriter csvWriter, boolean avgDist)
			throws FileNotFoundException, IOException {

		readData();
		Evaluator evaluator = new Evaluator();
		for (int kCluster = 2; kCluster < 201; kCluster += 2) {
			System.out.println("Start Clustering for k: " + kCluster + " : "
					+ new Date());
			Classifier classifier = new Classifier(kCluster, items);
			List<Cluster> clusters = classifier.createClusters();
			writeCsvRecord(kCluster, clusters, evaluator, csvWriter);
			System.out.println("End Clustering: " + new Date());
		}

	}

	private static void writeCsvRecord(int kCluster, List<Cluster> clusters,
			Evaluator evaluator, CSVWriter csvWriter) {
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
				item1.setDistance(item1, 0.0);
				Item item2 = items.get(j);
				item2.setDistance(item2, 0.0);
				double distance = ItemUtil.calcDistance(item1, item2,
						nonUniqueKeywords, useJacard);
				item1.setDistance(item2, distance);
				item2.setDistance(item1, distance);
			}
		}
	}

	private static void production(int filmId, String file,
			boolean outputKeywords) throws Exception {
		System.out.println("Start reading result file " + new Date());
		FileUtil fileUtil = new FileUtil();
		fileUtil.readInput("data/keywords.txt");
		List<Item> items = fileUtil.getItems();
		List<Cluster> readClusteringResults = fileUtil.readClusteringResults(
				new File(file), items);
		System.out.println("End reading result file " + new Date());
		printSimilarFilms(readClusteringResults, filmId, items, outputKeywords);
	}

	private static void printSimilarFilms(List<Cluster> clusters, int filmId,
			List<Item> items, boolean outputKeywords) throws Exception {
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

		Map<Integer, String> map = FileUtil.importMoviesFromFile("data/u.item");

		System.out.println("Looking for similar movies of movie "
				+ film.getItemNumber() + " " + map.get(film.getItemNumber()));

		int max = 20;
		for (Item item : filmCluster.getMembers()) {
			System.out.print("Movie " + item.getItemNumber() + ": "
					+ map.get(item.getItemNumber()) + " ");
			if (outputKeywords)
				System.out.print(item.getKeywords());
			System.out.println("");
			if (max-- <= 0)
				break;
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
		if (args.length != 3) {
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

	private static void printTopTenKeywordsPerCluster(List<Cluster> clusters) {
		Evaluator evaluator = new Evaluator();
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
