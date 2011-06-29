package org.clustering;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.clustering.classifier.Classifier;
import org.clustering.model.Cluster;
import org.clustering.model.Item;

public class Main {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		int kCluster = 2;
		boolean test = true;
		
		List<Item> items = new ArrayList<Item>(1700);
		Set<String> allKeywords = new HashSet<String>();
		
		if(test) {
			createTestInput(items,allKeywords);
		} else {
			readInput("data/keywords.txt", items, allKeywords);
		}
		
		int numKeywords = allKeywords.size();
		for(int i = 0; i < items.size(); i++) {
			for(int j = i+1;j < items.size(); j++) {
				items.get(i).calcDistance(items.get(j),numKeywords);
			}
		}
		
		if(test) validateDistances(items);
		
		Classifier classifier = new Classifier(kCluster, items);
		List<Cluster> clusters = classifier.createClusters();
		if(test) validateClustes(clusters, items);
	}

	private static void validateDistances(List<Item> items) {
		test(items.get(0).getDistance(items.get(1)) == 5.0/2.0);
	}
	
	private static void test(boolean b) {
		if(!b) throw new IllegalStateException("Failed test");
	}

	private static void validateClustes(List<Cluster> clusters, List<Item> items) {
		test(clusters.size()==2);
		Cluster cluster1 = clusters.get(0);
		Cluster cluster2 = clusters.get(1);

		test(cluster1.getMembers().size()==2);
		test(cluster2.getMembers().size()==2);
		
		if(cluster1.getMembers().contains(items.get(0))) {
			test(cluster1.getMembers().contains(items.get(0)));
			test(cluster1.getMembers().contains(items.get(1)));
			test(cluster2.getMembers().contains(items.get(2)));
			test(cluster2.getMembers().contains(items.get(3)));
			
		} else {
			test(cluster1.getMembers().contains(items.get(2)));
			test(cluster1.getMembers().contains(items.get(3)));
			test(cluster2.getMembers().contains(items.get(0)));
			test(cluster2.getMembers().contains(items.get(1)));
		}
	}

	private static void createTestInput(List<Item> items,
			Set<String> allKeywords) {
		Item item1 = new Item(1);
		item1.addKeyword("k1");
		item1.addKeyword("k3");
		items.add(item1);

		Item item2 = new Item(2);
		item2.addKeyword("k1");
		item2.addKeyword("k2");
		item2.addKeyword("k3");
		items.add(item2);
		
		Item item3 = new Item(3);
		item3.addKeyword("k3");
		item3.addKeyword("k5");
		items.add(item3);
		
		Item item4 = new Item(4);
		item4.addKeyword("k3");
		item4.addKeyword("k4");
		item4.addKeyword("k5");
		items.add(item4);
		
		allKeywords.add("k1");
		allKeywords.add("k2");
		allKeywords.add("k3");
		allKeywords.add("k4");
		allKeywords.add("k5");	
	}

	private static void readInput(String file, List<Item> items, Set<String> allKeywords)
			throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(
		file));
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
