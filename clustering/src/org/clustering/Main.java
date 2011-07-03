package org.clustering;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.clustering.classifier.Classifier;
import org.clustering.evaluator.Evaluator;
import org.clustering.evaluator.KeywordCount;
import org.clustering.model.Cluster;
import org.clustering.model.Item;

public class Main {

	private static List<Item> items;
	private static Set<String> allKeywords;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		boolean printEvaluation = true;
		int kCluster = 10;
		
//		int filmId = getInputFilmId(args);
//		if(filmId == -1) {
//			System.out.println("Film Id invalid. Please give a file Id between 1 and 1682");
//			return;
//		}

		System.out.println("Start reading data: " + new Date());
		items = new ArrayList<Item>(1700);
		allKeywords = new HashSet<String>();
		readInput("data/keywords.txt");

		int numKeywords = allKeywords.size();
		for (int i = 0; i < items.size(); i++) {
			for (int j = i + 1; j < items.size(); j++) {
				items.get(i).calcDistance(items.get(j), numKeywords);
			}
		}
		System.out.println("End reading data: " + new Date());

		System.out.println("Start Clustering: " + new Date());
		Classifier classifier = new Classifier(kCluster, items);
		List<Cluster> clusters = classifier.createClusters();
		System.out.println("End Clustering: " + new Date());
	
//		printSimilarFilms(clusters, filmId);
		
		if(printEvaluation){
			Evaluator evaluator = new Evaluator();
			printTopTenKeywordsPerCluster(evaluator, clusters);
			printEvaluationData(evaluator,clusters);
		}

	}


	private static void printSimilarFilms(List<Cluster> clusters, int filmId) {
		Item film = findItemById(filmId);
		if(film == null){
			throw new IllegalStateException("Could not find the film with given id: " + filmId);
		}
		Cluster filmCluster = null;
		for(Cluster cluster : clusters){
			if(cluster.contains(film)){
				filmCluster = cluster;
				break;
			}
		}
		if(filmCluster == null){
			throw new IllegalStateException("The given film is not contained in any cluster. Id: " + filmId);
		}
		for(Item item : filmCluster.getMembers()){
			System.out.print(item.toString());
			System.out.println(item.getKeywords());
		}
		
	}

	private static Item findItemById(int filmId) {
		for(Item item : items){
			if(item.getItemNumber() == filmId){
				return item;
			}
		}
		return null;
	}

	private static int getInputFilmId(String[] args) {
		if(args.length > 1){
			return -1;
		}
		int filmId = -1;
		try{
			filmId = Integer.valueOf(args[0]);
			
		}catch (NumberFormatException e) {
			filmId = -1;
		}
		if(filmId < 1 && filmId > 1682){
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
		System.out.println("Avg per Cluster: "+avg+" Min: "+min+" Max: "+max);
		for(Cluster cluster : clusters) {
			System.out.println("Cluster "+cluster.getId()+"[ MAE: "+mae.get(cluster)+" MSE: "+mse.get(cluster)+" ]");
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
			System.out.printf("%s: [%s] \n", cluster.toString(), sbTopTenKeywords.toString());
		}
	}

	private static void readInput(String file) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			int indexOf = line.indexOf(":");
			if (indexOf == -1)
				continue;
			Item item = new Item(Integer.parseInt(line.substring(0, indexOf)));
			if (indexOf + 1 < line.length()) {
				String[] keywords = (line.substring(indexOf + 1)).split(";");
				for (String keyword : keywords) {
					keyword = keyword.trim();
					item.addKeyword(keyword);
					allKeywords.add(keyword);
				}
			}
			items.add(item);
		}
		reader.close();
	}

}
