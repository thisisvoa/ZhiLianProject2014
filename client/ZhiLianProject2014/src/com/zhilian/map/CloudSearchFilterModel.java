package com.zhilian.map;

import java.util.HashMap;
import java.util.Map;

import com.amap.api.cloud.search.CloudSearch;

public class CloudSearchFilterModel {
	private Map<String, String> filterRules = new HashMap<String, String>();
	private CloudSearch.Sortingrules sortingRules;
	
	public void addFilterRules(String filterKey, String filterValue) {
		filterRules.put(filterKey, filterValue);
	}
	
	public Map<String, String> getFilterRules() {
		return filterRules;
	}
	
	public void setSortingRules(String key, boolean isAscending) {
		sortingRules = new CloudSearch.Sortingrules(key, isAscending);
	}
	
	public CloudSearch.Sortingrules getSortingRules() {
		return sortingRules;
	}
}
