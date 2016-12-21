package com.starrymedia.xd.search.core.server;

/**
 * 
 * @author : Ares
 * @createTime : Sep 25, 2012 1:08:20 PM
 * @version : 1.0
 * @description :
 */
public class GetSolrServer {

	/** solrServer */
	public static LocalSolrServer solrServer;


	public static LocalSolrServer getServer() {
		if (solrServer == null) {
			solrServer = new LocalSolrServer("");
			return solrServer;
		} else {
			return solrServer;
		}
	}


	public LocalSolrServer getServer(String serverName) throws Exception {
		
		solrServer = new LocalSolrServer(serverName);
		
		return solrServer;
	}

}
