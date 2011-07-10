package org.clustering.util;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import org.clustering.model.Cluster;
import org.clustering.model.Item;

public class VisualisationUtil {

	private String[] keywords;
	private String filename;

	public VisualisationUtil(Set<String> keywords, String outputFilename) {
		this.filename = outputFilename;
		this.keywords = keywords.toArray(new String[keywords.size()]);
	}

	public void drawItems(List<Item> items) throws IOException {
		int[][] itemKeywordMatrix = createItemKeywordMatrix(items);

		drawItems(itemKeywordMatrix);
	}

	/**
	 * @param items
	 * @return
	 */
	private int[][] createItemKeywordMatrix(List<Item> items) {
		int[][] itemKeywordMatrix = new int[items.size()][keywords.length];
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			for (int j = 0; j < keywords.length; j++) {
				String keyword = keywords[j];
				itemKeywordMatrix[i][j] = item.getKeywords().contains(keyword) ? 1 : 0;
			}
		}
		return itemKeywordMatrix;
	}

	private void drawItems(int[][] itemKeywordMatrix) throws IOException {
		BufferedImage bi = new BufferedImage(itemKeywordMatrix[0].length, itemKeywordMatrix.length, BufferedImage.TYPE_BYTE_BINARY);
		WritableRaster raster = bi.getRaster();
		for(int row = 0; row < itemKeywordMatrix.length; row ++){
			for(int column = 0; column < itemKeywordMatrix[row].length; column++){
				int value = itemKeywordMatrix[row][column];
				//the background is black, so 0 means draw nothing that means a black dot, and 1 means draw a white dot
				int pixel = 0; 
				if(value == 0){
					pixel = 1;
				}
				raster.setSample(column, row, 0, pixel);
			}
		}
		// Store the image using the PNG format.
		ImageIO.write(bi, "PNG", new File(filename));
	}

	public void drawClusters(List<Cluster> clusters) throws IOException {
		List<Item> clusteredItems = new ArrayList<Item>();
		for(Cluster cluster : clusters){
			for(Item item : cluster.getMembers()){
				clusteredItems.add(item);
			}
			Item clusterDelimiterDummyItem1 = createClusterDelimiterDummyItem();
			clusteredItems.add(clusterDelimiterDummyItem1);
		}
		drawItems(clusteredItems);
	}

	/**
	 * @return 
	 * 
	 */
	private Item createClusterDelimiterDummyItem() {
		Item clusterDelimiter = new Item(-1);
		for(String keyword : keywords){
			clusterDelimiter.addKeyword(keyword);
		}
		return clusterDelimiter;
	}

}
