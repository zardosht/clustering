package org.clustering.model;

public class ItemUtil {

	private ItemUtil() {
	}

	public static double calcDistance(Item item1, Item item2, int numKeywords) {
		double mutualKeywords = 0;
		for (String keyword : item1.getKeywords()) {
			if (item2.getKeywords().contains(keyword))
				mutualKeywords++;
		}
		double similarity = mutualKeywords / numKeywords;
		// double distance = 1/similarity;
		double distance = 1 - similarity;
		return distance;
	}

}
