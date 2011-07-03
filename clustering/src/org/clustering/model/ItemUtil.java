package org.clustering.model;

import java.util.Set;

public class ItemUtil {

	private ItemUtil() {
	}

	public static double calcDistance(Item item1, Item item2, Set<String> keywords) {
		double mutualKeywords = 0;
		for (String keyword : item1.getKeywords()) {
			if (item2.getKeywords().contains(keyword))
				mutualKeywords++;
		}
		double similarity = mutualKeywords / keywords.size();
		// double distance = 1/similarity;
		double distance = 1 - similarity;
		return distance;
	}

}
