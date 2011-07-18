package org.clustering.model;

import java.util.Set;

public class DistanceUtil {

	private DistanceUtil() {
	}

	public static double calcDistance(Item item1, Item item2,
			Set<String> allKeywords, DistanceTypes distanceType) {
		switch (distanceType) {
		case JACCARD_SIMILARITY:
			return calcJacardDistance(item1, item2, allKeywords);
		case RUSSELL_AND_RAO_SIMILARITY:
			return calcRussellAndRaoDistance(item1, item2, allKeywords);
		case OTSUKA_SIMILARITY:
			return calcOtsukaDistance(item1, item2, allKeywords);
		case PATTERN_DIFFERENCE_DISTANCE:
			return calcPatternDifferenceDistance(item1, item2, allKeywords);
		case CHORD_DISTANCE:
			return calcChordDistance(item1, item2, allKeywords);
		default:
			return Double.NaN;
		}
	}

	private static double calcChordDistance(Item item1, Item item2,
			Set<String> allKeywords) {
		// chord distance = sqrt(2(1- (a / sqrt((a+b)(a+c)))))
		int positiveMatches = getPositiveMatches(item1, item2, allKeywords);
		int iAbsenceMismatches = getIAbsenceMismatches(item1, item2,
				allKeywords);
		int jAbsenceMismatches = getJAbsenceMismatches(item1, item1,
				allKeywords);
		double distance = Math.sqrt(2 * (1 - (positiveMatches / Math
				.sqrt((positiveMatches + iAbsenceMismatches)
						* (positiveMatches + jAbsenceMismatches)))));
		return distance;
	}

	private static double calcPatternDifferenceDistance(Item item1, Item item2,
			Set<String> allKeywords) {
		// pattern-difference distance = 4bc / pow((a+b+c+d), 2)

		int positiveMatches = getPositiveMatches(item1, item2, allKeywords);
		int iAbsenceMismatches = getIAbsenceMismatches(item1, item2,
				allKeywords);
		int jAbsenceMismatches = getJAbsenceMismatches(item1, item1,
				allKeywords);
		int negativeMatches = getNegativeMatches(item1, item2, allKeywords);

		double distance = (double) 4
				* iAbsenceMismatches
				* jAbsenceMismatches
				/ Math.pow((positiveMatches + iAbsenceMismatches
						+ jAbsenceMismatches + negativeMatches), 2.0);
		return distance;
	}

	private static double calcOtsukaDistance(Item item1, Item item2,
			Set<String> allKeywords) {
		// otsuka similarity = a / pow(((a+b)(a+c)), 0.5)
		int positiveMatches = getPositiveMatches(item1, item2, allKeywords);
		int iAbsenceMismatches = getIAbsenceMismatches(item1, item2,
				allKeywords);
		int jAbsenceMismatches = getJAbsenceMismatches(item1, item1,
				allKeywords);
		double similarity = (double) positiveMatches
				/ Math.pow((positiveMatches + iAbsenceMismatches)
						* (positiveMatches + jAbsenceMismatches), 0.5);
		double distance = 1.0 - similarity;
		return distance;
	}

	private static double calcRussellAndRaoDistance(Item item1, Item item2,
			Set<String> allKeywords) {
		int positiveMatches = getPositiveMatches(item1, item2, allKeywords);
		double similarity = (double) positiveMatches
				/ (double) allKeywords.size();
		double distance = 1.0 - similarity;
		return distance;
	}

	private static double calcJacardDistance(Item item1, Item item2,
			Set<String> allKeywords) {
		// Jaccard = a / (a + b + c)
		int positiveMatches = getPositiveMatches(item1, item2, allKeywords);
		int iAbsenceMismatches = getIAbsenceMismatches(item1, item2,
				allKeywords);
		int jAbsenceMismatches = getJAbsenceMismatches(item1, item2,
				allKeywords);

		return 1.0 - ((double) positiveMatches / (double) (positiveMatches
				+ iAbsenceMismatches + jAbsenceMismatches));
	}

	private static int getPositiveMatches(Item item1, Item item2,
			Set<String> allKeywords) {
		int positiveMatches = 0;
		for (String keyword : allKeywords) {
			if (item1.getKeywords().contains(keyword)
					&& item2.getKeywords().contains(keyword)) {
				positiveMatches++;
			}
		}
		return positiveMatches;
	}

	private static int getIAbsenceMismatches(Item itemI, Item itemJ,
			Set<String> allKeywords) {
		// itemI does not have a feature and itemJ has it (0, 1)
		int iAbsenceMismatches = 0;
		for (String keyword : allKeywords) {
			if (!itemI.getKeywords().contains(keyword)
					&& itemJ.getKeywords().contains(keyword)) {
				iAbsenceMismatches++;
			}
		}

		return iAbsenceMismatches;
	}

	private static int getJAbsenceMismatches(Item itemI, Item itemJ,
			Set<String> allKeywords) {
		// itemI has a feature and itemJ does not have it (1, 0)
		int jAbsenceMismatches = 0;
		for (String keyword : allKeywords) {
			if (itemI.getKeywords().contains(keyword)
					&& !itemJ.getKeywords().contains(keyword)) {
				jAbsenceMismatches++;
			}
		}

		return jAbsenceMismatches;
	}

	private static int getNegativeMatches(Item item1, Item item2,
			Set<String> allKeywords) {
		int negativeMatches = 0;
		for (String keyword : allKeywords) {
			if (!item1.getKeywords().contains(keyword)
					&& !item2.getKeywords().contains(keyword)) {
				negativeMatches++;
			}
		}
		return negativeMatches;
	}

}
