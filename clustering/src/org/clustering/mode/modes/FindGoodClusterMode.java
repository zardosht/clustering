package org.clustering.mode.modes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.clustering.classifier.Classifier;
import org.clustering.data.DataUtil;
import org.clustering.data.FileUtil;
import org.clustering.mode.AbstractMode;
import org.clustering.model.Cluster;
import org.clustering.model.Item;

public class FindGoodClusterMode extends AbstractMode {

	private DataUtil dataUtil;

	public FindGoodClusterMode() {
		super("-fgc", "");
	}

	public void _runFGC() throws Exception {
		dataUtil = new DataUtil();
		dataUtil.readData(true);
		Classifier classifier = new Classifier(10, dataUtil.getItems());
		List<Cluster> clusters = classifier.createClusters();
		
		Map<Integer, String> map = new FileUtil().importMoviesFromFile("data/u.item");

		for (Cluster cluster : clusters) {
			final Item centroid = cluster.getCentroid();
			ArrayList<Item> list = new ArrayList<Item>(cluster.getMembers());
			Collections.sort(list, new Comparator<Item>() {
				@Override
				public int compare(Item o1, Item o2) {
					return (centroid.getDistance(o1) < centroid.getDistance(o2))?-1:1;
				}
			});
			
//			System.out.println(list.get(0).getDistance(centroid));
//			System.out.println(list.get(list.size()-1).getDistance(centroid));
			
			int i = 0;
			String movies = "";
			String ids ="";
			for(Item item : list) {
				movies +="["+item.getItemNumber()+"] "+map.get(item.getItemNumber())+", ";
				ids += item.getItemNumber()+", ";
				if((i++)>10) break;
			}
			System.out.println(movies+"\n"+ids+"\n---\n");
		}

	}
}
