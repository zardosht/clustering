package org.clustering.classifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.clustering.model.HierarchicalCluster;
import org.clustering.model.Item;

public class HierarchicalClassifier {

	public HierarchicalCluster createHierarchicalCluster(List<Item> items,
			HierarchicalSimilarity similarityStrategy) {

		List<HierarchicalCluster> initialClusters = createInitialClusters(items);

		HierarchicalCluster rootCluster = mergeClusters(initialClusters,
				similarityStrategy);

		return rootCluster;
	}

	private HierarchicalCluster mergeClusters(
			List<HierarchicalCluster> initialClusters,
			HierarchicalSimilarity similarityStrategy) {
		while (initialClusters.size() > 1) {
			MergePair mergePair = getMergePair(initialClusters,
					similarityStrategy);
			HierarchicalCluster iCluster = initialClusters
					.get(mergePair.firsIndex);
			HierarchicalCluster jCluster = initialClusters
					.get(mergePair.secondIndex);
			// remove j first, because j is always behind i
			initialClusters.remove(mergePair.secondIndex);
			initialClusters.remove(mergePair.firsIndex);
			initialClusters.add(new HierarchicalCluster(mergePair.sim,
					iCluster, jCluster));
		}
		return initialClusters.get(0);
	}

	private MergePair getMergePair(List<HierarchicalCluster> clusters,
			HierarchicalSimilarity similarityStrategy) {
		switch (similarityStrategy) {
		case AVERAGE_INTER_SIMILARITY: {
			int mI = 0, mJ = 0;
			double maxSim = 0;
			for (int i = 0; i < clusters.size(); i++) {
				for (int j = i + 1; j < clusters.size(); j++) {
					double similarity = getAvgInterSimilarity(clusters.get(i),
							clusters.get(j));
					if (similarity > maxSim) {
						maxSim = similarity;
						mI = i;
						mJ = j;
					}
				}
			}
			MergePair mergePair = new MergePair();
			mergePair.firsIndex = mI;
			mergePair.secondIndex = mJ;
			mergePair.sim = maxSim;
			return mergePair;

		}
		case SIGLE_LINK_SIMILARITY: {
			int mI = 0, mJ = 0;
			double maxSim = 0;
			for (int i = 0; i < clusters.size(); i++) {
				for (int j = i + 1; j < clusters.size(); j++) {
					double similarity = getSingleLinkSimilarity(
							clusters.get(i), clusters.get(j));
					if (similarity > maxSim) {
						maxSim = similarity;
						mI = i;
						mJ = j;
					}
				}
			}
			MergePair mergePair = new MergePair();
			mergePair.firsIndex = mI;
			mergePair.secondIndex = mJ;
			mergePair.sim = maxSim;
			return mergePair;
		}
		case COMPLETE_LINK_SIMILARITY: {
			int mI = 0, mJ = 0;
			double minSim = Double.MAX_VALUE;
			for (int i = 0; i < clusters.size(); i++) {
				for (int j = i + 1; j < clusters.size(); j++) {
					double similarity = getCompleteLinkSimilarity(
							clusters.get(i), clusters.get(j));
					if (similarity < minSim) {
						minSim = similarity;
						mI = i;
						mJ = j;
					}
				}
			}
			MergePair mergePair = new MergePair();
			mergePair.firsIndex = mI;
			mergePair.secondIndex = mJ;
			mergePair.sim = minSim;
			return mergePair;
		}
		}

		return null;
	}

	private double getSingleLinkSimilarity(HierarchicalCluster c1,
			HierarchicalCluster c2) {
		// single link distance
		double singleLinkDistance = getSingleLinkDistance(c1, c2);
		return 1.0 - singleLinkDistance;
	}

	private double getAvgInterSimilarity(HierarchicalCluster c1,
			HierarchicalCluster c2) {
		// compare average inter distance
		double avgInterDistC1C2 = getAvgInterDistance(c1, c2);
		return 1.0 - avgInterDistC1C2;
	}

	private double getCompleteLinkSimilarity(HierarchicalCluster c1,
			HierarchicalCluster c2) {
		// Complete link similarity is the similarity of the most dissimilar
		// elements in two clusters
		// therefore the complete link distnace if the distance of the two most
		// apart elements
		HashSet<Item> itemsC1 = c1.getItems();
		HashSet<Item> itemsC2 = c2.getItems();

		double maxDis = 0.0;
		for (Item item1 : itemsC1) {
			for (Item item2 : itemsC2) {
				double distance = item1.getDistance(item2);
				if (distance > maxDis) {
					maxDis = distance;
				}
			}
		}

		return 1.0 - maxDis;
	}

	private double getSingleLinkDistance(HierarchicalCluster c1,
			HierarchicalCluster c2) {
		// single link similarity is the similarity of the most similar elements
		// inside two clusters.
		// therefore single link distance will be distance of the most close
		// elements
		HashSet<Item> itemsC1 = c1.getItems();
		HashSet<Item> itemsC2 = c2.getItems();

		double minDist = Double.MAX_VALUE;
		for (Item item1 : itemsC1) {
			for (Item item2 : itemsC2) {
				double distance = item1.getDistance(item2);
				if (distance < minDist) {
					minDist = distance;
				}
			}
		}

		return minDist;
	}

	private double getAvgInterDistance(HierarchicalCluster c1,
			HierarchicalCluster c2) {
		// for each item in C1 get its average distance to elements in C2
		// the distance of C1 to C2 is then average of these average distance.
		HashSet<Item> itemsC1 = c1.getItems();
		HashSet<Item> itemsC2 = c2.getItems();

		double sumAvgDist = 0.0;
		double avgInterDist = 0.0;
		for (Item item1 : itemsC1) {
			double sumItem = 0.0;
			for (Item item2 : itemsC2) {
				sumItem += item1.getDistance(item2);
			}
			sumAvgDist += sumItem / itemsC2.size();
		}

		avgInterDist = sumAvgDist / itemsC1.size();
		return avgInterDist;
	}

	private List<HierarchicalCluster> createInitialClusters(List<Item> items) {
		ArrayList<HierarchicalCluster> result = new ArrayList<HierarchicalCluster>();
		for (Item item : items) {
			result.add(new HierarchicalCluster(item));
		}
		return result;
	}

	private class MergePair {
		int firsIndex;
		int secondIndex;
		double sim;
	}

}
