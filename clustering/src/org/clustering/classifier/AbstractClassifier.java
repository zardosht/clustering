package org.clustering.classifier;

import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import org.clustering.model.Item;

public abstract class AbstractClassifier {

	private final int numOfClusters;
	private final List<Item> items;

	public AbstractClassifier(int numOfClusters, List<Item> items) {
		this.numOfClusters = numOfClusters;
		this.items = items;
	}

	
	
	public List<List<Item>> createClusters(){
		List<List<Item>> result = new ArrayList<List<Item>>();
		List<Item> centroids = chooseCentroids();
		return createClusters(centroids);
	}
	
	public List<List<Item>> createClusters(List<Item> centroids){
		List<List<Item>> result = new ArrayList<List<Item>>();
		if(isCentroidsChanging()){
			
		}
		List<Item> centroids = chooseCentroids();
		do{
			
			createClusters(centroids);
			
		}while();
		
		
		
		return result;
	}
	
	
	
	



	protected abstract List<Item> chooseCentroids();


	protected abstract boolean isCentroidsChanging();



	public int getNumOfClusters() {
		return numOfClusters;
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	
	
	
}
