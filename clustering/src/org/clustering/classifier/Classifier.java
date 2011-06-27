package org.clustering.classifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.clustering.model.Cluster;
import org.clustering.model.Item;

public class Classifier {

	private final int numOfClusters;
	private final List<Item> items;
	private Set<Item> oldCentroids;
	private Random random = new Random();

	public Classifier(int numOfClusters, List<Item> items) {
		oldCentroids = new HashSet<Item>();
		this.numOfClusters = numOfClusters;
		this.items = items;
	}

	public List<Cluster> createClusters() {
		List<Cluster> initialClusters = createInitialClusters();
		return createClusters(initialClusters);
	}

	private List<Cluster> createInitialClusters() {
		List<Cluster> initialClusters = new ArrayList<Cluster>();
		Set<Item> initialCentroids = selectInitialCentroids();
		for (Item centroid : initialCentroids) {
			Cluster cluster = new Cluster(centroid);
			initialClusters.add(cluster);
		}
		return initialClusters;
	}

	private Set<Item> selectInitialCentroids() {
		Set<Item> initialCentroids = new HashSet<Item>();
		while (initialCentroids.size() < numOfClusters) {
			int index = random.nextInt(items.size());
			Item centroid = items.get(index);
			initialCentroids.add(centroid);
		}
		return initialCentroids;
	}

	private List<Cluster> createClusters(List<Cluster> clusters) {
		if (!centroidsChanged(clusters)) {
			return clusters;
		} else {
			for (Item item : items) {
				double lastDist = Double.MAX_VALUE;
				int newClusterIndex = 0;
				int oldClusterIndex = 0;
				for (int clusterIndex = 0; clusterIndex < clusters.size();clusterIndex++) {
					Cluster cluster = clusters.get(clusterIndex);
					if(cluster.contains(item)){
						oldClusterIndex = clusterIndex;
					}
					double newDist = item.getDistance(cluster.getCentroid());
					if (newDist < lastDist) {
						lastDist = newDist;
						newClusterIndex = clusterIndex;
					}
				}
				if(oldClusterIndex!=newClusterIndex) {
					clusters.get(oldClusterIndex).removeItem(item);
					clusters.get(newClusterIndex).addItem(item);
				}
			}
			for (Cluster cluster : clusters) {
				cluster.computeNewCnetroid();
			}
			return createClusters(clusters);
		}
	}

	private boolean centroidsChanged(List<Cluster> clusters) {
		Set<Item> newCentroids = new HashSet<Item>();
		for (Cluster cluster : clusters) {
			newCentroids.add(cluster.getCentroid());
		}
		boolean result = !oldCentroids.equals(newCentroids);
		oldCentroids = newCentroids;
		return result;
	}

	public int getNumOfClusters() {
		return numOfClusters;
	}

	public List<Item> getItems() {
		return items;
	}

}
