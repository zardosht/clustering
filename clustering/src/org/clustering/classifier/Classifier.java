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
	private Random random = new Random();
	private int numRuns;

	public Classifier(int numOfClusters, List<Item> items) {
		this.numOfClusters = numOfClusters;
		this.items = items;
	}

	private List<Cluster> createInitialClusters(ClusterSeed seed) {
		List<Cluster> initialClusters = new ArrayList<Cluster>();
		Set<Item> initialCentroids = selectInitialCentroids(seed);
		int id = 1;
		for (Item centroid : initialCentroids) {
			Cluster cluster = createCluster(id, centroid);
			initialClusters.add(cluster);
			id++;
		}
		return initialClusters;
	}

	private Cluster createCluster(int id, Item centroid) {
		Cluster cluster = new Cluster(id, centroid);
		return cluster;
	}

	private Set<Item> selectInitialCentroids(ClusterSeed seed) {
		Set<Item> initialCentroids = new HashSet<Item>();

		switch (seed) {

		case RANDOM:
			while (initialCentroids.size() < numOfClusters) {
				int index = random.nextInt(items.size());
				Item centroid = items.get(index);
				initialCentroids.add(centroid);
			}
			break;

		case ICLUSTER:
			while (initialCentroids.size() < numOfClusters) {
				Cluster cluster = new Cluster(-1, null);
				int i = 10;
				while (i > 0) {
					int index = random.nextInt(items.size());
					Item item = items.get(index);
					if (!cluster.contains(item)) {
						cluster.addItem(item);
						i--;
					}
				}
				cluster.computeNewCnetroid();
				initialCentroids.add(cluster.getCentroid());
			}
			break;
		}

		return initialCentroids;
	}

	public List<Cluster> createClusters() {
		return createClusters(ClusterSeed.RANDOM);
	}

	public List<Cluster> createClusters(ClusterSeed seed) {
		List<Cluster> clusters = createInitialClusters(seed);
		numRuns = 0;
		do {
			for (Item item : items) {
				Cluster oldCluster = findCurrentCluster(item, clusters);
				Cluster newCluster = findNewCluster(item, clusters);
				if (oldCluster != null) {
					oldCluster.removeItem(item);
				}
				newCluster.addItem(item);
			}
			computeNewCentroids(clusters);
			numRuns++;
		} while (centroidsChanged(clusters));
		return clusters;
	}

	public int getNumRuns() {
		return numRuns;
	}

	private Cluster findNewCluster(Item item, List<Cluster> clusters) {
		double lastDist = Double.MAX_VALUE;
		Cluster newCluster = null;
		for (Cluster cluster : clusters) {
			Item centroid = cluster.getCentroid();
			// the distance of an item to itself is 0.0; this breaks the
			// algorithm. Therefore filter the centroid itself.
			if (item == centroid) {
				return cluster;
			}
			double newDist = item.getDistance(centroid);
			if (newDist < lastDist) {
				lastDist = newDist;
				newCluster = cluster;
			}
		}
		return newCluster;
	}

	private Cluster findCurrentCluster(Item item, List<Cluster> clusters) {
		for (Cluster cluster : clusters) {
			if (cluster.contains(item)) {
				return cluster;
			}
		}
		return null;
	}

	private void computeNewCentroids(List<Cluster> clusters) {
		for (Cluster cluster : clusters) {
			cluster.computeNewCnetroid();
		}
	}

	private boolean centroidsChanged(List<Cluster> clusters) {
		for (Cluster cluster : clusters) {
			if (cluster.centroidChanged()) {
				return true;
			}
		}
		return false;
	}

	public int getNumOfClusters() {
		return numOfClusters;
	}

	public List<Item> getItems() {
		return items;
	}

}
