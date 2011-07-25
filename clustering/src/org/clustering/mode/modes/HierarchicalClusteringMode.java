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
import org.clustering.model.DistanceTypes;
import org.clustering.model.HierarchicalCluster;
import org.clustering.model.Item;
import org.clustering.util.VisualisationUtil;

public class HierarchicalClusteringMode extends AbstractMode {

	public HierarchicalClusteringMode() {
		super("-hierarchical", "HAC");
	}

	public void _runCreateCluster() throws Exception {
		DataUtil dataUtil = new DataUtil();
		int nAtLeastKeywords = 5;
		dataUtil.readData(true, nAtLeastKeywords, DistanceTypes.OTSUKA_SIMILARITY);
		System.out.println("Start hierarchical clustering: " + new Date());
		HierarchicalClassifier classifier = new HierarchicalClassifier();

		ArrayList<Item> items = new ArrayList<Item>();
		// alien love
		// List<Integer> selected = Arrays.asList(183, 176, 665, 343, 135, 271,
		// 1303, 735, 1196, 316, 301, 1160, 1086);
		// family horror action
		List<Integer> selected = Arrays.asList(609, 1147, 585, 756, 1676, 419,
				493, 376, 1627, 166, 670, 573, 1490, 565, 445, 1425, 183, 436,
				552, 1224, 802, 1231, 1151, 912, 1088, 840, 144, 841);
		// List<Integer> selected = Arrays.asList(39, 809, 11, 336, 550, 752,
		// 248,
		// 79, 798, 1222, 1210, 302, 592, 644, 1150, 1524, 494, 291, 136,
		// 747, 569, 1042, 1081, 77, 821, 1482, 613, 1322, 485, 66, 949,
		// 756, 148, 111, 722, 1614);
		// love, kids, action
//		List<Integer> selected = Arrays.asList(1457, 1072, 764, 643, 1374, 535,
//				610, 1037, 869, 1614, 1566, 838, 1405, 799, 423, 1, 409, 63,
//				577, 94, 132, 1508, 419, 689, 176, 1657, 359, 798, 550, 298,
//				1027, 831, 1210, 2, 1217, 245);
		for (int i : selected) {
			for (Item item : dataUtil.getItems()) {
				if (item.getItemNumber() == i) {
					items.add(item);
					break;
				}
			}
		}

//		 List<Item> items = dataUtil.getItems();

		HierarchicalCluster root = classifier.createHierarchicalCluster(items,
				HierarchicalAlgorithm.AVERAGE_LINK_DISTANCE);
		System.out.println("End hierarchical clustering: " + new Date());

		System.out.println("Start writing result file " + new Date());
		System.out.println("Items in root: " + root.getItems().size());
		System.out.println("items in first child: "
				+ root.getFirstChild().getItems());
		System.out.println("items in second child: "
				+ root.getSecondChild().getItems());

		VisualisationUtil util = new VisualisationUtil(new HashSet<String>(),
				"results/hierarchical_clusters1.png");
		util.drawHierarchicalCluster(root, items);

		System.out.println("Finished: " + new Date());

	}
}
