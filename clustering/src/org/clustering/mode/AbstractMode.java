package org.clustering.mode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.clustering.data.FileUtil;
import org.clustering.model.DistanceTypes;
import org.clustering.model.DistanceUtil;
import org.clustering.model.Item;
import org.clustering.util.VisualisationUtil;

public class AbstractMode {

	private FileUtil fileUtil;
	private List<Item> items;
	private Set<String> allKeywords;
	private Set<String> uniqueKeywords;
	private Set<String> nonUniqueKeywords;
	private final String modePrefix;
	private final String description;

	public AbstractMode(String modePrefix, String description) {
		this.modePrefix = modePrefix;
		this.description = description;

	}

	public boolean run(String[] args) throws Exception {
		Method execMethod = null;
		Object[] eArgs = null;

		try {
			execMethod = getModeExecMethod();
			if (execMethod == null)
				return false;

			// check modeprefix. e.g. "-cluster"
			if (modePrefix != null && !modePrefix.isEmpty()) {
				if (args.length == 0 || !modePrefix.equals(args[0])) {
					return false;
				}
				args = Arrays.copyOfRange(args, 1, args.length);
			}
			eArgs = new Object[execMethod.getParameterTypes().length];
			int i = 0;
			for (@SuppressWarnings("rawtypes") Class clazz : execMethod.getParameterTypes()) {
				if(i >= args.length) {
					return false;
				}
				
				if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
					eArgs[i] = Integer.parseInt(args[i]);
				} else if(Double.class.equals(clazz) || double.class.equals(clazz)) {
					eArgs[i] = Double.parseDouble(args[i]);
				} else if(String.class.equals(clazz)) {
					eArgs[i] = args[i];
				} else if(Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
					eArgs[i] = Boolean.parseBoolean(args[i]);
				} else {
					return false;
				}
				
				i++;
			}
		} catch (Exception e) {
			return false;
		}

		// invoke actual method
		execMethod.invoke(this, eArgs);
		return true;
	}

	private Method getModeExecMethod() {
		for (Method method : getClass().getMethods()) {
			if(method.getName().startsWith("_run")) {
				return method;
			}
			// WHY AREN'T THE F!&?%$§G ANNOTATIONS NOT RECOGNIZED
//			if (method.getAnnotation(ModeExec.class) != null) {
//				return method;
//			}
//			for(Annotation an : method.getAnnotations()) {
//				System.out.println(an);
//			}
		}
		return null;
	}

	public void readData() throws FileNotFoundException, IOException {
		System.out.println("Start reading data: " + new Date());
		fileUtil = new FileUtil();
		fileUtil.readInput("data/keywords.txt");
		items = fileUtil.getItems();
		removeItemsWithoutKeyword();
		allKeywords = fileUtil.getAllKeywords();
		uniqueKeywords = fileUtil.getUniqueKeywords();
		nonUniqueKeywords = fileUtil.getNonUniqueKeywords();
		System.out.println(String.format("%d out of %d keywords are unique.",
				uniqueKeywords.size(), allKeywords.size()));

		System.out.println("Starting Visualisation: " + new Date());
		new VisualisationUtil(nonUniqueKeywords,
				"results/items_before_clustering.png").drawItems(items);
		System.out.println("End Visualisation: " + new Date());

		System.out.println("Starting calcDistance: " + new Date());
		calcDistances(items, nonUniqueKeywords);
		System.out.println("End calcDistance: " + new Date());
		System.out.println("End reading data: " + new Date());
	}

	private void removeItemsWithoutKeyword() {
		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
			Item item = iterator.next();
			if (item.getKeywords().size() == 0) {
				iterator.remove();
			}
		}
	}

	/**
	 * @param items
	 * @param nonUniqueKeywords
	 */
	private void calcDistances(List<Item> items, Set<String> nonUniqueKeywords) {
		for (int i = 0; i < items.size(); i++) {
			for (int j = i + 1; j < items.size(); j++) {
				Item item1 = items.get(i);
				item1.setDistance(item1, 0.0);
				Item item2 = items.get(j);
				item2.setDistance(item2, 0.0);
				double distance = DistanceUtil.calcDistance(item1, item2,
						nonUniqueKeywords, DistanceTypes.OTSUKA_SIMILARITY);
				item1.setDistance(item2, distance);
				item2.setDistance(item1, distance);
			}
		}
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public List<Item> getItems() {
		return items;
	}

	public Set<String> getAllKeywords() {
		return allKeywords;
	}

	public Set<String> getUniqueKeywords() {
		return uniqueKeywords;
	}

	public Set<String> getNonUniqueKeywords() {
		return nonUniqueKeywords;
	}

	public String getDescriptino() {
		return description;
	}
}
