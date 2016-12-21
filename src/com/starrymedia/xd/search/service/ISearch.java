/**
 * 
 */
package com.starrymedia.xd.search.service;

import org.apache.solr.client.solrj.SolrServer;

import com.starrymedia.xd.search.common.SolrServerName;

/**
 * @author : Ares
 * @createTime : 2012-11-23 下午03:49:51
 * @version : 1.0
 * @description :
 */
public interface ISearch {
	
	/**
	 * 获取指定SolrServer实例
	 * @param solrServerName
	 * @return
	 */
	public SolrServer getSolrServer(SolrServerName solrServerName);

}
