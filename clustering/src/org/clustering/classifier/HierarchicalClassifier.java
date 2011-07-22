package org.clustering.classifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.clustering.model.HierarchicalCluster;
import org.clustering.model.Item;

public class HierarchicalClassifier {

	public HierarchicalCluster createHierarchicalCluster(List<Item> items,
			HierarchicalAlgorithm similarityStrategy) {

		List<HierarchicalCluster> initialClusters = createInitialClusters(items);

		HierarchicalCluster rootCluster = mergeClusters(initialClusters,
				similarityStrategy);

		return rootCluster;
	}

	private HierarchicalCluster mergeClusters(
			List<HierarchicalCluster> initialClusters,
			HierarchicalAlgorithm similarityStrategy) {
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
			initialClusters.add(new HierarchicalCluster(mergePair.dist,
					iCluster, jCluster));
		}
		return initialClusters.get(0);
	}

	private MergePair getMergePair(List<HierarchicalCluster> clusters,
			HierarchicalAlgorithm similarityStrategy) {
		switch (similarityStrategy) {
		case AVERAGE_LINK_DISTANCE: {
			int mI = 0, mJ = 0;
			double maxSim = 0;
			for (int i = 0; i < clusters.size(); i++) {
				for (int j = i + 1; j < clusters.size(); j++) {
					double similarity = getAvgLinkDistance(clusters.get(i),
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
			mergePair.dist = maxSim;
			return mergePair;

		}
		case SINGLE_LINK_DISTANCE: {
			//merge the two clusters with the smallest minimum pairwise distance
			int mI = 0, mJ = 0;
			double minDist = Double.MAX_VALUE;
			for (int i = 0; i < clusters.size(); i++) {
				for (int j = i + 1; j < clusters.size(); j++) {
					double dist = getSingleLinkDistance(
							clusters.get(i), clusters.get(j));
					if (dist < minDist) {
						minDist = dist;
						mI = i;
						mJ = j;
					}
				}
			}
			MergePair mergePair = new MergePair();
			mergePair.firsIndex = mI;
			mergePair.secondIndex = mJ;
			mergePair.dist = minDist;
			return mergePair;
		}
		case COMPLETE_LINK_DISTANCE: {
			//merge clusters with the "smallest" "maximum pairwise distance"
			int mI = 0, mJ = 0;
			double minDist = Double.MAX_VALUE;
			for (int i = 0; i < clusters.size(); i++) {
				for (int j = i + 1; j < clusters.size(); j++) {
					double dist = getCompleteLinkDistance(
							clusters.get(i), clusters.get(j));
					if (dist < minDist) {
						minDist = dist;
						mI = i;
						mJ = j;
					}
				}
			}
			MergePair mergePair = new MergePair();
			mergePair.firsIndex = mI;
			mergePair.secondIndex = mJ;
			mergePair.dist = minDist;
			return mergePair;
		}
		}

		return null;
	}


	private double getCompleteLinkDistance(HierarchicalCluster c1,
			HierarchicalCluster c2) {
		//merge clusters with the "smallest" "maximum pairwise distance"
		//return maximum pairwise distance 
		HashSet<Item> itemsC1 = c1.getItems();
		HashSet<Item> itemsC2 = c2.getItems();

		double maxDist = 0.0;
		for (Item item1 : itemsC1) {
			for (Item item2 : itemsC2) {
				double distance = item1.getDistance(item2);
				if (distance > maxDist) {
					maxDist = distance;
				}
			}
		}

		return maxDist;
	}

	private double getSingleLinkDistance(HierarchicalCluster c1,
			HierarchicalCluster c2) {
		//merge the two clusters with the smallest minimum pairwise distance
		//return minimum pairwise distance
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

	private double getAvgLinkDistance(HierarchicalCluster c1,
			HierarchicalCluster c2) {
		//merge the two clusters with the smallest average distance between any two points in the clusters 
		//return average distance between each two point in two clusters.
		HashSet<Item> itemsC1 = c1.getItems();
		HashSet<Item> itemsC2 = c2.getItems();

		double sumDist = 0.0;
		int n = 0;
		for (Item item1 : itemsC1) {
			for (Item item2 : itemsC2) {
				sumDist += item1.getDistance(item2);
				n++;
			}
		}

		double avgDist = sumDist / n;
		return avgDist;
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
		double dist;
	}

}
