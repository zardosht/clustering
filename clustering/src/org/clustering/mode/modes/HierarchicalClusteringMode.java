package org.clustering.mode.modes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.clustering.classifier.HierarchicalAlgorithm;
import org.clustering.classifier.HierarchicalClassifier;
import org.clustering.data.DataUtil;
import org.clustering.mode.AbstractMode;
import org.clustering.model.HierarchicalCluster;
import org.clustering.model.Item;
import org.clustering.util.VisualisationUtil;

public class HierarchicalClusteringMode extends AbstractMode {

	public HierarchicalClusteringMode() {
		super("-hierarchical", "HAC");
	}

	public void _runCreateCluster() throws Exception {
		DataUtil dataUtil = new DataUtil();
		dataUtil.readData(true);
		System.out.println("Start hierarchical clustering: " + new Date());
		HierarchicalClassifier classifier = new HierarchicalClassifier();

//		ArrayList<Item> items = new ArrayList<Item>();
//		List<Integer> selected = Arrays.asList(183, 176, 665, 343, 135, 271, 1303, 735,
//				1196, 316, 301, 1160, 1086);
//		for(Item item : dataUtil.getItems()) {
//			for(int i : selected) {
//				if(item.getItemNumber()==i) {
//					items.add(item);
//					break;
//				}
//			}
//		}

		List<Item> items = dataUtil.getItems();
		
		HierarchicalCluster root = classifier
				.createHierarchicalCluster(items,
						HierarchicalAlgorithm.SINGLE_LINK_DISTANCE);
		System.out.println("End hierarchical clustering: " + new Date());

		System.out.println("Start writing result file " + new Date());
		System.out.println("Items in root: " + root.getItems().size());
		System.out.println("items in first child: "
				+ root.getFirstChild().getItems());
		System.out.println("items in second child: "
				+ root.getSecondChild().getItems());

		VisualisationUtil util = new VisualisationUtil(new HashSet<String>(),
				"results/hierarchical_clusters.png");
		util.drawHierarchicalCluster(root);

		System.out.println("Finished: " + new Date());

	}
}
