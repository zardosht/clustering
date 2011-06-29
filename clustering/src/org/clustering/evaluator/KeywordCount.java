package org.clustering.evaluator;

public class KeywordCount {

	private final String keyword;
	private final int count;

	public KeywordCount(String keyword, int count) {
		this.keyword = keyword;
		this.count = count;
	}

	public String getKeyword() {
		return keyword;
	}

	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		return keyword + String.format("(%d)", count);
	}
	
}