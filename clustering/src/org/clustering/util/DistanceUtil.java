package org.clustering.util;

import java.util.HashSet;
import java.util.Set;

import org.clustering.model.DistanceTypes;
import org.clustering.model.Item;

public class DistanceUtil {

	private DistanceUtil() {
	}

	public static double calcDistance(Item item1, Item item2,
			Set<String> allKeywords, DistanceTypes distanceType) {
		double distance = 0.0;
		switch (distanceType) {
		case JACCARD_SIMILARITY:
			distance = calcJacardDistance(item1, item2);
			break;
		case RUSSELL_AND_RAO_SIMILARITY:
			distance = calcRussellAndRaoDistance(item1, item2, allKeywords);
			break;
		case OTSUKA_SIMILARITY:
			distance = calcOtsukaDistance(item1, item2);
			break;
		case LANCE_AND_WILLIAMS_DIASTANCE:
			distance = calcLanceAndWilliamsDistance(item1, item2);
			break;
		case CHORD_DISTANCE:
			distance = calcChordDistance(item1, item2);
			break;
		default:
			distance = Double.NaN;
		}

		if (Double.isNaN(distance)) {
			throw new IllegalStateException();
		}
		return distance;
	}

	private static double calcChordDistance(Item item1, Item item2) {
		// chord distance = sqrt(2(1- (a / sqrt((a+b)(a+c)))))
		int positiveMatches = getPositiveMatches(item1, item2);
		int iAbsenceMismatches = getIAbsenceMismatches(item1, item2);
		int jAbsenceMismatches = getJAbsenceMismatches(item1, item2);
		double distance = Math.sqrt(2 * (1 - (positiveMatches / Math
				.sqrt((positiveMatches + iAbsenceMismatches)
						* (positiveMatches + jAbsenceMismatches)))));
		return distance;
	}

	private static double calcLanceAndWilliamsDistance(Item item1, Item item2) {
		// lance and williams distance = (b + c) / (2a + b + c)

		int positiveMatches = getPositiveMatches(item1, item2);
		int iAbsenceMismatches = getIAbsenceMismatches(item1, item2);
		int jAbsenceMismatches = getJAbsenceMismatches(item1, item2);

		double distance = (double) (iAbsenceMismatches + jAbsenceMismatches)
				/ (2 * positiveMatches + iAbsenceMismatches + jAbsenceMismatches);
		if (Double.isNaN(distance)) {
			throw new IllegalStateException();
		}

		return distance;
	}

	private static double calcOtsukaDistance(Item item1, Item item2) {
		// otsuka similarity = a / pow(((a+b)(a+c)), 0.5)
		int positiveMatches = getPositiveMatches(item1, item2);
		int iAbsenceMismatches = getIAbsenceMismatches(item1, item2);
		int jAbsenceMismatches = getJAbsenceMismatches(item1, item2);
		double similarity = (double) positiveMatches
				/ Math.pow((positiveMatches + iAbsenceMismatches)
						* (positiveMatches + jAbsenceMismatches), 0.5);
		double distance = 1.0 - similarity;
		return distance;
	}

	private static double calcRussellAndRaoDistance(Item item1, Item item2,
			Set<String> allKeywords) {
		int positiveMatches = getPositiveMatches(item1, item2);
		double similarity = (double) positiveMatches
				/ (double) allKeywords.size();
		double distance = 1.0 - similarity;
		return distance;
	}

	private static double calcJacardDistance(Item item1, Item item2) {
		// Jaccard = a / (a + b + c)
		int positiveMatches = getPositiveMatches(item1, item2);
		int iAbsenceMismatches = getIAbsenceMismatches(item1, item2);
		int jAbsenceMismatches = getJAbsenceMismatches(item1, item2);

		return 1.0 - ((double) positiveMatches / (double) (positiveMatches
				+ iAbsenceMismatches + jAbsenceMismatches));
	}

	private static int getPositiveMatches(Item item1, Item item2) {
		// those that both have
		Set<String> positiveMatches = new HashSet<String>();
		positiveMatches.addAll(item1.getKeywords());
		positiveMatches.retainAll(item2.getKeywords());
		return positiveMatches.size();

	}

	private static int getIAbsenceMismatches(Item itemI, Item itemJ) {
		// itemI does not have a feature and itemJ has it (0, 1)
		Set<String> iAbsenceMismatches = new HashSet<String>();
		iAbsenceMismatches.addAll(itemJ.getKeywords());
		iAbsenceMismatches.removeAll(itemI.getKeywords());
		return iAbsenceMismatches.size();
	}

	private static int getJAbsenceMismatches(Item itemI, Item itemJ) {
		// itemI has a feature and itemJ does not have it (1, 0)
		Set<String> jAbsenceMismatches = new HashSet<String>();
		jAbsenceMismatches.addAll(itemI.getKeywords());
		jAbsenceMismatches.removeAll(itemJ.getKeywords());
		return jAbsenceMismatches.size();
	}

	@SuppressWarnings("unused")
	private static int getNegativeMatches(Item item1, Item item2,
			Set<String> allKeywords) {
		Set<String> negativeMatches = new HashSet<String>();
		negativeMatches.addAll(allKeywords);
		negativeMatches.removeAll(item1.getKeywords());
		negativeMatches.removeAll(item2.getKeywords());
		return negativeMatches.size();
	}

}
