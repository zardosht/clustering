package org.clustering;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.clustering.classifier.Classifier;
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
		boolean printEvaluation = true;
		int kCluster = 5;

		// int filmId = getInputFilmId(args);
		// if(filmId == -1) {
		// System.out.println("Film Id invalid. Please give a file Id between 1 and 1682");
		// return;
		// }

		System.out.println("Start reading data: " + new Date());
		FileUtil fileUtil = new FileUtil();
		fileUtil.readInput("data/keywords.txt");
		List<Item> items = fileUtil.getItems();
		Set<String> allKeywords = fileUtil.getAllKeywords();
		Set<String> uniqueKeywords = fileUtil.getUniqueKeywords();
		Set<String> nonUniqueKeywords = new HashSet<String>();
		nonUniqueKeywords.addAll(allKeywords);
		nonUniqueKeywords.removeAll(uniqueKeywords);
		System.out.println(String.format("%d out of %d keywords are unique.", uniqueKeywords.size(), allKeywords.size()));
		
		for (int i = 0; i < items.size(); i++) {
			for (int j = i + 1; j < items.size(); j++) {
				Item item1 = items.get(i);
				Item item2 = items.get(j);
				double distance = ItemUtil.calcDistance(item1, item2, nonUniqueKeywords);
				item1.setDistance(item2, distance);
				item2.setDistance(item1, distance);
			}
		}
		System.out.println("End reading data: " + new Date());
		

		System.out.println("Start Clustering: " + new Date());
		Classifier classifier = new Classifier(kCluster, items);
		List<Cluster> clusters = classifier.createClusters();
		System.out.println("End Clustering: " + new Date());
	
//		printSimilarFilms(clusters, filmId, items);
		
		if(printEvaluation){
			Evaluator evaluator = new Evaluator();
			printTopTenKeywordsPerCluster(evaluator, clusters);
			System.out.println();
			printEvaluationData(evaluator, clusters);
		}

	}

	private static void printSimilarFilms(List<Cluster> clusters, int filmId, List<Item> items) {
		Item film = findItemById(filmId, items);
		if(film == null){
			throw new IllegalStateException("Could not find the film with given id: " + filmId);
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
		for(Item item : items){
			if(item.getItemNumber() == filmId){
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
