package org.clustering.classifier;

import java.util.ArrayList;
import java.util.List;

import org.clustering.model.HierarchicalCluster;
import org.clustering.model.Item;

public class HierarchicalClassifier {

	public HierarchicalCluster createHierarchicalCluster(List<Item> items) {

		List<HierarchicalCluster> initialClusters = createInitialClusters(items);

		while(initialClusters.size() > 1) {
			initialClusters = mergeClusters(initialClusters);
		}
		
		return initialClusters.get(0);
	}

	private List<HierarchicalCluster> mergeClusters(
			List<HierarchicalCluster> initialClusters) {
		ArrayList<HierarchicalCluster> list = new ArrayList<HierarchicalCluster>();
		while (initialClusters.size() > 1) {
			int mI = 0, mJ = 0;
			double maxSim = 0;
			for (int i = 0; i < initialClusters.size(); i++) {
				for (int j = i+1; j < initialClusters.size(); j++) {
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
			// remove j first, because j is always behind i
			initialClusters.remove(mJ);
			initialClusters.remove(mI);
			list.add(new HierarchicalCluster(maxSim, iCluster, jCluster));
		}
		if(initialClusters.size() == 1) {
			list.add(initialClusters.get(0));
		}
		return list;
	}

	private double getSimilarity(HierarchicalCluster firstCluster,
			HierarchicalCluster secondCluster) {
		return 0;
	}

	private List<HierarchicalCluster> createInitialClusters(List<Item> items) {
		ArrayList<HierarchicalCluster> result = new ArrayList<HierarchicalCluster>();
		for (Item item : items) {
			result.add(new HierarchicalCluster(item));
		}
		return result;
	}

}
