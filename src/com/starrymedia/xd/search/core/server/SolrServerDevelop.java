package com.starrymedia.xd.search.core.server;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.common.SolrDocument;

import com.starrymedia.xd.search.exception.SearchException;

/**
 * 
 * @author : Ares
 * @createTime : Sep 25, 2012 1:12:07 PM
 * @version : 1.0
 * @description :
 */
public interface SolrServerDevelop {

	/**
	 * 获取索引Server
	 * @return
	 */
	public SolrServer getSolrServer();
	
	
	
	/**
	 * 查询keyword相应文档总记录数
	 * 
	 * @param keyword
	 * @return
	 * @throws SearchException
	 */
	public int getFoundCount(String keyword) throws SearchException;
	
	

	/**
	 * 查询keyword相应文档
	 * 
	 * @param query
	 * @return
	 * @throws SearchException
	 */
	public SolrDocument findIndexItem(String keyword) throws SearchException;

	
	/**
	 * 分页查询keyword相应文档
	 * 
	 * @param keyword
	 * @param pageSize
	 * @param pageNo
	 * @param start
	 * @param orderByField
	 * @param order
	 * @return
	 * @throws SearchException
	 */
	public List<SolrDocument> findIndexItems(String keyword, int pageSize,int pageNo, int start,String orderByField,ORDER order ) throws SearchException;

}
