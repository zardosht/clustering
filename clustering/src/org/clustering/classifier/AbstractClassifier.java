package org.clustering.classifier;

import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import org.clustering.model.Cluster;
import org.clustering.model.Item;

public abstract class AbstractClassifier {

	private final int numOfClusters;
	private final List<Item> items;

	public AbstractClassifier(int numOfClusters, List<Item> items) {
		this.numOfClusters = numOfClusters;
		this.items = items;
	}

	
	
	public List<Cluster> createClusters(){
		List<Item> centroids = chooseInitialCentroids();
		return createClusters(centroids, new ArrayList<Cluster>());
	}
	
	private List<Item> chooseInitialCentroids() {
		return new ArrayList<Item>(numOfClusters);
	}


	public List<Cluster> createClusters(List<Item> centroids, List<Cluster> result){
		if(isCentroidsChanging(centroids)){
			return result;
		}else{
			
		}
	}
	
	
	
	



	protected abstract List<Item> chooseCentroids();


	protected abstract boolean isCentroidsChanging(List<Item> centroids);



	public int getNumOfClusters() {
		return numOfClusters;
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	
	
	
}
