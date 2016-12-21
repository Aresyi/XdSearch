/**
 * 
 */
package com.starrymedia.xd.search.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @author : Ares.yi
 * @createTime : 2008-4-27 下午04:43:40
 * @version : 1.0
 * @description :
 */
public class Pagination<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**当前页*/
	private int currentPage;

	/**页大小*/
	private int pageSize;

	/**总页数*/
	private int totalPages;

	/**总记录数*/
	private int totalRecords;

	/**具体数据*/
	private List<T> records;
	
	/**分类聚合包装*/
	private Map<String, Map<String,Long>> facetMap;


	public int getCurrentPage() {
		return currentPage < 1 ? 1 : currentPage;
	}

	public void setCurrentPage(int currentPage) {
		currentPage = currentPage < 1 ? 1 : currentPage;
		this.currentPage = currentPage;
	}

	public void setCurrentPage(String pn) {
		try {
			setCurrentPage(Integer.parseInt(pn));
		} catch (Exception e) {
			this.currentPage = 1;
		}
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
		if (pageSize > 0){
			countPages();
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		if (totalRecords > 0){
			countPages();
		}
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public boolean hasNext() {
		return currentPage < totalPages;
	}

	public boolean hasPrev() {
		return currentPage > 1;
	}

	private void countPages() {
		totalPages = (totalRecords - 1) / pageSize + 1;
		currentPage = currentPage > totalPages ? totalPages : currentPage;
	}

	public Map<String, Map<String, Long>> getFacetMap() {
		return facetMap;
	}

	public void setFacetMap(Map<String, Map<String, Long>> facetMap) {
		this.facetMap = facetMap;
	}

	
	
}

