package org.clustering.model;

import java.util.HashSet;
import java.util.Set;

public class DitanceUtil {

	private DitanceUtil() {
	}

	public static double calcDistance(Item item1, Item item2,
			Set<String> allKeywords, DistanceTypes distanceType) {
		switch (distanceType) {
		case JACCARD:
			return calcJacardDist(item1, item2, allKeywords);
		case AVERAGE_DISTANCE:
			return calcAvgDistance(item1, item2, allKeywords);
		default:
			return Double.NaN;
		}
	}

	private static double calcAvgDistance(Item item1, Item item2,
			Set<String> allKeywords) {
		double mutualKeywords = 0;
		for (String keyword : item1.getKeywords()) {
			if (item2.getKeywords().contains(keyword))
				mutualKeywords++;
		}
		double similarity = mutualKeywords / allKeywords.size();
		// double distance = 1/similarity;
		double distance = 1 - similarity;
		return distance;
	}

	private static double calcJacardDist(Item item1, Item item2,
			Set<String> keywords) {
		// d(A, B) == cardinality(intersect(A, B)) / cardinality(union(A, B))

		// Attention: Keywords are nonUniqueKeywords!!
		// remove from both items, the unique keywords.
		Set<String> i1Copy = new HashSet<String>();
		i1Copy.addAll(item1.getKeywords());
		i1Copy.retainAll(keywords);

		Set<String> i2Copy = new HashSet<String>();
		i2Copy.addAll(item2.getKeywords());
		i2Copy.retainAll(keywords);

		Set<String> intersect = new HashSet<String>();
		intersect.addAll(i1Copy);
		intersect.retainAll(i2Copy);

		Set<String> union = new HashSet<String>();
		union.addAll(i1Copy);
		union.addAll(i2Copy);

		int unionSize = union.size();
		if (unionSize == 0) {
			unionSize = 1;
		}

		return 1.0 - ((double) intersect.size() / (double) union.size());
	}

}
