package org.clustering.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import org.clustering.model.Cluster;
import org.clustering.model.HierarchicalCluster;
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
				itemKeywordMatrix[i][j] = item.getKeywords().contains(keyword) ? 1
						: 0;
			}
		}
		return itemKeywordMatrix;
	}

	private void drawItems(int[][] itemKeywordMatrix) throws IOException {
		BufferedImage bi = new BufferedImage(itemKeywordMatrix[0].length,
				itemKeywordMatrix.length, BufferedImage.TYPE_BYTE_BINARY);
		WritableRaster raster = bi.getRaster();
		for (int row = 0; row < itemKeywordMatrix.length; row++) {
			for (int column = 0; column < itemKeywordMatrix[row].length; column++) {
				int value = itemKeywordMatrix[row][column];
				// the background is black, so 0 means draw nothing that means a
				// black dot, and 1 means draw a white dot
				int pixel = 0;
				if (value == 0) {
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
		for (Cluster cluster : clusters) {
			for (Item item : cluster.getMembers()) {
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
		for (String keyword : keywords) {
			clusterDelimiter.addKeyword(keyword);
		}
		return clusterDelimiter;
	}

	/**
	 * ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
	 * ' ' Dendrogram
	 * 
	 * @throws IOException
	 */

	public void drawHierarchicalCluster(HierarchicalCluster root)
			throws IOException {
		ArrayList<Item> items = new ArrayList<Item>(root.getItems());

		int width = 1200;
		int height = items.size() * 20;
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.BITMASK);
		Graphics2D g = (Graphics2D) bi.getGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		final int xOffset = 10;
		final int yOffset = 10;
		final int yOffsetStep = 15;

		double xAxisOffset = drawYAxis(items, g, xOffset, yOffset, yOffsetStep);

		List<List<HierarchicalCluster>> levels = getLevels(root);

		// get level size
		final int levelwidth = (int) ((width - xAxisOffset - 50) / levels
				.size());

		HashMap<HierarchicalCluster, Point> middles = new HashMap<HierarchicalCluster, Point>();

		int levelOffset = (int) xAxisOffset;
		int iL = 1;
		boolean newOffset = false;
		for (List<HierarchicalCluster> level : levels) {
			// get min, max sim for scaling
			double minSim = 1;
			double maxSim = 0;
			for (HierarchicalCluster cluster : level) {
				if (cluster.getSimLevel() < minSim) {
					minSim = cluster.getSimLevel();
				}
				if (cluster.getSimLevel() > maxSim) {
					maxSim = cluster.getSimLevel();
				}
			}
			double simDiff = maxSim - minSim;
			// end

			for (HierarchicalCluster cluster : level) {
				// If leave
				if (cluster.getItem() != null) {
					Item item1 = cluster.getItem();
					int index1 = items.indexOf(item1);
					int y = yOffset + (index1 * yOffsetStep);
					middles.put(cluster, new Point((int) xAxisOffset, y));
					continue;
				}
				Point p1 = middles.get(cluster.getFirstChild());
				Point p2 = middles.get(cluster.getSecondChild());

				// int cWidth = (int) ((levelwidth *
				// Math.pow(cluster.getSimLevel(),2))/1);
				// int cWidth = (int) (levelwidth*0.4 + 0.6*levelwidth *
				// cluster.getSimLevel());
				// int cWidth = (int) (levelwidth*cluster.getSimLevel());
				int cWidth = (int) (((cluster.getSimLevel() - minSim) / simDiff)
						* levelwidth * 0.95);

				Random random = new Random();
				g.setColor(new Color(random.nextInt(256), random.nextInt(256),
						random.nextInt(256)));

				int newX = levelOffset + cWidth;

				g.drawLine(p1.x, p1.y, newX, p1.y);
				g.drawLine(p2.x, p2.y, newX, p2.y);
				g.drawLine(newX, p1.y, newX, p2.y);

				middles.put(cluster, new Point(newX,
						(Math.min(p1.y, p2.y) + (Math.abs(p1.y - p2.y) / 2))));
				newOffset = true;

			}
			// new offset only if something has been drawn
			if (newOffset) {
				levelOffset += levelwidth;
			}
//			g.setColor(Color.BLACK);
//			g.drawLine(levelOffset, 0, levelOffset, height);
			ImageIO.write(bi, "PNG",
					new File("results/hierViz/level_" + items.size() + "_"
							+ (iL++) + ".png"));
		}

		// Store the image using the PNG format.
		ImageIO.write(bi, "PNG", new File(filename));
	}

	private double drawYAxis(ArrayList<Item> items, Graphics2D g, int xOffset,
			int yOffset, int yOffsetStep) {
		g.setColor(Color.BLACK);
		Font font = new Font("Arial", 0, 10);
		double gwidth = 0;
		for (Item item : items) {
			GlyphVector glyphVector = font.createGlyphVector(
					new FontRenderContext(null, true, true),
					"" + item.getItemNumber());
			double w = glyphVector.getVisualBounds().getWidth();
			if (w > gwidth)
				gwidth = w;
			g.drawGlyphVector(glyphVector, xOffset, yOffset);
			yOffset += yOffsetStep;
		}
		double offset = xOffset * 2 + gwidth;
		g.fillRect((int) offset, 0, 1, yOffset);

		return offset + xOffset;
	}

	private List<List<HierarchicalCluster>> getLevels(HierarchicalCluster root) {
		List<List<HierarchicalCluster>> levels = new ArrayList<List<HierarchicalCluster>>();
		ArrayList<HierarchicalCluster> rootLevel = new ArrayList<HierarchicalCluster>();
		rootLevel.add(root);
		levels.add(rootLevel);
		addToLevel(levels, 1, root.getFirstChild());
		addToLevel(levels, 1, root.getSecondChild());
		Collections.reverse(levels);
		return levels;
	}

	private void addToLevel(List<List<HierarchicalCluster>> levels, int i,
			HierarchicalCluster node) {
		if (node != null) {
			if (levels.size() == i) {
				levels.add(new ArrayList<HierarchicalCluster>());
			}
			levels.get(i).add(node);
			if (node.getItem() == null) {
				addToLevel(levels, i + 1, node.getFirstChild());
				addToLevel(levels, i + 1, node.getSecondChild());
			}
		}
	}
}
