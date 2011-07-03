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

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Start: " + new Date());

		int kCluster = 10;

		List<Item> items = new ArrayList<Item>(1700);
		Set<String> allKeywords = new HashSet<String>();
		readInput("data/keywords.txt", items, allKeywords);

		int numKeywords = allKeywords.size();
		for (int i = 0; i < items.size(); i++) {
			for (int j = i + 1; j < items.size(); j++) {
				items.get(i).calcDistance(items.get(j), numKeywords);
			}
		}

		Classifier classifier = new Classifier(kCluster, items);
		List<Cluster> clusters = classifier.createClusters();
		Evaluator evaluator = new Evaluator();
		printTopTenKeywordsPerCluster(evaluator, clusters);
		System.out.println("End: " + new Date());

	}

	private static void printTopTenKeywordsPerCluster(Evaluator evaluator,
			List<Cluster> clusters) {
		Map<Cluster, List<KeywordCount>> topTenKeywordsPerCluster = evaluator
				.getTopTenKeywordsPerCluster(clusters);
		int i = 1;
		for (Cluster cluster : topTenKeywordsPerCluster.keySet()) {
			List<KeywordCount> topTenKeywordsInCluster = topTenKeywordsPerCluster
					.get(cluster);
			StringBuilder sb = new StringBuilder("");
			for (KeywordCount kc : topTenKeywordsInCluster) {
				sb.append(kc.toString());
				sb.append("; ");
			}
			System.out.printf("Cluster %d: [%s] \n", i++, sb.toString());
		}
	}

	private static void readInput(String file, List<Item> items,
			Set<String> allKeywords) throws FileNotFoundException, IOException {
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
