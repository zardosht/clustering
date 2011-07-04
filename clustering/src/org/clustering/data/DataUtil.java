package org.clustering.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * COPIED FROM CRAWLER 
 *
 */
public class DataUtil {

	private DataUtil() {
	}

	public static Map<Integer, String> importMoviesFromFile(File file)
			throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		HashMap<Integer, String> titles = new HashMap<Integer, String>();
		String line = null;
		int lineNumber = 0;
		while ((line = reader.readLine()) != null) {
			lineNumber++;
			String[] split = line.split("\t");
			String id = split[0];
			String title = split[1];
			titles.put(Integer.parseInt(id), title);
		}
		return titles;
	}
}
