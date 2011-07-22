package org.clustering.mode.modes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.clustering.data.CSVWriter;
import org.clustering.data.FileUtil;
import org.clustering.mode.AbstractMode;
import org.clustering.model.Item;

public class KeywordCountMode extends AbstractMode {

	public KeywordCountMode() {
		super("-keywordCount", "");
	}

	public void _runCount() throws Exception, IOException {
		FileUtil fileUtil = new FileUtil();
		fileUtil.readInput("data/keywords.txt");
		List<Item> items = fileUtil.getItems();
		HashMap<Integer, Integer> results = new HashMap<Integer, Integer>();
		int biggest = 0;
		int highestCount = 0;
		for (Item item : items) {
			int size = item.getKeywords().size();
			if (size > biggest) {
				biggest = size;
			}
			Integer integer = results.get(size);
			results.put(size, (integer == null) ? 1 : integer + 1);
			if (results.get(size) > highestCount) {
				highestCount = results.get(size);
			}
		}

		for (int i = 0; i < 5; i++) {
			System.out.println("i " + i + " :" + results.get(i));
		}
		System.out.println(biggest);
		System.out.println(highestCount);

		CSVWriter csvWriter = new CSVWriter(new File(
				"results/keywordDistribution.csv"), Arrays.asList("nr", "#"));
		for (int i = 0; i < biggest; i++) {
			Integer integer = results.get(i);
			if (integer != null) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("nr", i);
				map.put("#", integer);
				csvWriter.writeRecord(map);
			}
		}
		csvWriter.close();

		// int offset = 10;
		//
		// int width = biggest + 2 * offset;
		// int height = highestCount + 2 * offset;
		// BufferedImage image = new BufferedImage(width, height,
		// BufferedImage.BITMASK);
		// Graphics2D g = (Graphics2D) image.getGraphics();
		// g.setColor(Color.white);
		// g.fillRect(0, 0, width, height);
		// g.setColor(Color.black);
		//
		// for (int i = 0; i < biggest; i++) {
		// Integer integer = results.get(i);
		// if (integer != null) {
		// g.drawRect(i + offset, highestCount - integer + offset, 0, 0);
		// }
		// }
		//
		// // g.drawLine(offset, highestCount+offset+5, biggest+offset,
		// // highestCount+offset+5);
		//
		// ImageIO.write(image, "PNG", new File("results/keywordcount.png"));
	}

}
