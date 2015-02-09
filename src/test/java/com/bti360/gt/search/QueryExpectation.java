package com.bti360.gt.search;

public class QueryExpectation {
    private String query;
    private boolean hit;

    private QueryExpectation(String query, boolean hit) {
	this.query = query;
	this.hit = hit;
    }

    public static QueryExpectation hit(String query) {
	return new QueryExpectation(query, true);
    }

    public static QueryExpectation miss(String query) {
	return new QueryExpectation(query, false);
    }

    public String getQuery() {
	return query;
    }

    public boolean isHit() {
	return hit;
    }

    @Override
    public String toString() {
	return String.format("%s: %s", hit ? "Hit" : "Miss", query);
    }

}
