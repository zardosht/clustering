package org.clustering;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageChopper {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		String dir ="C:/Users/Otto/Desktop/cluster präsi/kCluster/";
		String img = "clusteredItems.png";
		
		BufferedImage read = ImageIO.read(new File(dir,img));
		int width = read.getWidth();
		int height = read.getHeight();
		
		int limit = 2000;
		
		int parts = width/2000;
		for(int i = 0; i < parts; i++) {
			BufferedImage image = new BufferedImage(limit, height, BufferedImage.BITMASK);
			image.getGraphics().drawImage(read, 0, 0, limit, height, i*limit, 0, (i+1)*limit, height, null);
			ImageIO.write(image, "PNG", new File(dir,"part_"+i+"_"+img));
		}
	}

}
