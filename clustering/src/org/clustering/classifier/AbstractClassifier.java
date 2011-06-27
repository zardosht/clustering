package org.clustering.classifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.clustering.model.Cluster;
import org.clustering.model.Item;

public abstract class AbstractClassifier {

	private final int numOfClusters;
	private final List<Item> items;
	private Set<Item> oldCentroids;
	private Random random = new Random();

	public AbstractClassifier(int numOfClusters, List<Item> items) {
		this.numOfClusters = numOfClusters;
		this.items = items;
	}

	
	
	public List<Cluster> createClusters(){
		List<Cluster> initialClusters = createInitialClusters();
		return createClusters(initialClusters);
	}
	
	private List<Cluster> createInitialClusters() {
		List<Cluster> initialClusters = new ArrayList<Cluster>();
		Set<Item> initialCentroids = selectInitialCentroids();
		for(Item centroid : initialCentroids){
			Cluster cluster = new Cluster(centroid);
			initialClusters.add(cluster);
		}
		return initialClusters;
	}




	private Set<Item> selectInitialCentroids() {
		Set<Item> initialCentroids = new HashSet<Item>();
		oldCentroids = new HashSet<Item>();		
		while(initialCentroids.size() < numOfClusters){
			int index = random.nextInt(items.size());
			Item centroid = items.get(index);
			initialCentroids.add(centroid);
			oldCentroids.add(centroid);
		}
		return initialCentroids;
	}



	private List<Cluster> createClusters(List<Cluster> clusters){
		if(!centroidsChanged(clusters)){
			return clusters;
		}else{
			for(Cluster cluster : clusters){
				cluster.computeNewCnetroid();
			}
			double lastDist = Double.MAX_VALUE;
			int newClusterIndex = 0;
			for(Item item : items){
				for(Cluster cluster : clusters){
					double newDist = item.getDistance(cluster.getCentroid());
					if(newDist < lastDist){
						lastDist = newDist;
						newClusterIndex = clusters.indexOf(cluster);
					}
				}
				Cluster newCluster = clusters.get(newClusterIndex);
				newCluster.addItem(item);
			}
			return createClusters(clusters);
		}
	}
	
	private boolean centroidsChanged(List<Cluster> clusters) {
		Set<Item> newCentroids = new HashSet<Item>();
		for(Cluster cluster : clusters){
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
