package org.clustering.evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.clustering.model.Cluster;
import org.clustering.model.Item;

public class Evaluator {

	public Evaluator() {

	}

	public Map<Cluster, List<KeywordCount>> getTopTenKeywordsPerCluster(
			List<Cluster> clusters) {
		Map<Cluster, List<KeywordCount>> result = new HashMap<Cluster, List<KeywordCount>>();

		for (Cluster cluster : clusters) {
			Set<String> allKeywordsInCluster = new HashSet<String>();
			for (Item item : cluster.getMembers()) {
				allKeywordsInCluster.addAll(item.getKeywords());
			}

			List<KeywordCount> keyWordCountsInCluster = new ArrayList<KeywordCount>();
			for (String keyword : allKeywordsInCluster) {
				int counter = 0;
				for (Item item : cluster.getMembers()) {
					if (item.getKeywords().contains(keyword))
						counter++;
				}
				keyWordCountsInCluster.add(new KeywordCount(keyword, counter));
			}
			Collections.sort(keyWordCountsInCluster,
					new Comparator<KeywordCount>() {
						@Override
						public int compare(KeywordCount kc1, KeywordCount kc2) {
							return kc1.getCount() > kc2.getCount() ? -1 : 1;
						}
					});
			List<KeywordCount> topTenInCluster = new ArrayList<KeywordCount>();
			int toIndex = keyWordCountsInCluster.size() > 10 ? 10
					: keyWordCountsInCluster.size();
			topTenInCluster.addAll(keyWordCountsInCluster.subList(0, toIndex));
			result.put(cluster, topTenInCluster);

		}
		return result;
	}

	public int getMaxItemPerCluster(List<Cluster> clusters) {
		int max = -1;
		for (Cluster cluster : clusters) {
			int size = cluster.getMembers().size();
			if (size > max) {
				max = size;
			}
		}
		return max;
	}

	public int getMinItemPerCluster(List<Cluster> clusters) {
		int min = -1;
		for (Cluster cluster : clusters) {
			int size = cluster.getMembers().size();
			if (size < min || min == -1) {
				min = size;
			}
		}
		return min;
	}

	public double getAvgItemPerCluster(List<Cluster> clusters) {
		double sum = 0;
		double count = 0;
		for (Cluster cluster : clusters) {
			sum += cluster.getMembers().size();
			count++;
		}
		return sum / count;
	}

	public Map<Cluster, Double> getMeanAbsoluteError(List<Cluster> clusters) {
		return getError(clusters, false);
	}

	public Map<Cluster, Double> getMeanSquaredError(List<Cluster> clusters) {
		return getError(clusters, true);
	}

	private HashMap<Cluster, Double> getError(List<Cluster> clusters,
			boolean square) {
		HashMap<Cluster, Double> result = new HashMap<Cluster, Double>();

		for (Cluster cluster : clusters) {
			if(cluster.getMembers().size() == 1){
				result.put(cluster, 0.0);
				continue;
			}
			Item centroid = cluster.getCentroid();
			double e = 0.0;
			int counter = 0;
			for (Item item : cluster.getMembers()) {
				if (item != centroid) {
					double distance = item.getDistance(centroid);
					e += (square) ? Math.pow(distance, 2) : distance;
					counter++;
				}
			}
			double err = e / (double) counter;
			result.put(cluster, err);
		}
		return result;
	}
	
	public int getNumClustersWithOneElements(List<Cluster> clusters){
		int count = 0;
		for(Cluster cluster : clusters){
			if(cluster.getMembers().size() == 1){
				count ++;
			}
		}
		return count;
	}

}
