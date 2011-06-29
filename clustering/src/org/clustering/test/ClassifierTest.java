package org.clustering.test;

import java.util.List;
import java.util.Set;

import org.clustering.model.Cluster;
import org.clustering.model.Item;

public class ClassifierTest {
	
	
	public void createTestInput(List<Item> items, Set<String> allKeywords) {
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
	
	
	public  void validateDistances(List<Item> items) {
		test(items.get(0).getDistance(items.get(1)) == 1-(2.0/5.0));
	}
	
	public static void test(boolean b) {
		if(!b) throw new IllegalStateException("Failed test");
	}

	public static void validateClustes(List<Cluster> clusters, List<Item> items) {
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
}
