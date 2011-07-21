package org.clustering.classifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.clustering.model.HierarchicalCluster;
import org.clustering.model.Item;

public class HierarchicalClassifier {

	public List<HierarchicalCluster> createHierarchicalCluster(List<Item> items) {

		List<HierarchicalCluster> initialClusters = createInitialClusters(items);

		mergeClusters(initialClusters);

		return null;
	}


	private HierarchicalCluster mergeClusters(List<HierarchicalCluster> initialClusters) {
		int mI = 0, mJ = 0;
		double maxSim = 0;
		for (int i = 0; i < initialClusters.size(); i++) {
			for (int j = i; j < initialClusters.size(); j++) {
				double similarity = getSimilarity(initialClusters.get(i),
						initialClusters.get(j));
				if (similarity > maxSim) {
					maxSim = similarity;
					mI = i;
					mJ = j;
				}
			}
		}
		HierarchicalCluster iCluster = initialClusters.get(mI);
		HierarchicalCluster jCluster = initialClusters.get(mJ);
		initialClusters.remove(mJ);
		initialClusters.remove(mI);
		return new HierarchicalCluster(maxSim,iCluster,jCluster);
	}

	private double getSimilarity(HierarchicalCluster firstCluster,
			HierarchicalCluster secondCluster) {
		return 0;
	}

	private List<HierarchicalCluster> createInitialClusters(List<Item> items) {
		ArrayList<HierarchicalCluster> result = new ArrayList<HierarchicalCluster>();
		for (Item item : items) {
			result.add(new HierarchicalCluster(1, item));
		}
		return result;
	}

}
