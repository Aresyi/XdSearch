package com.starrymedia.xd.search.core.server;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.xml.sax.SAXException;

import com.starrymedia.xd.search.exception.SearchException;

/**
 * 
 * @author : Ares
 * @createTime : 2012-9-25 下午05:41:53
 * @version : 1.0
 * @description :
 */
public class LocalSolrServer implements SolrServerDevelop {

	static Logger logger = Logger.getLogger(LocalSolrServer.class);

	private SolrServer solrServer;
	
	public LocalSolrServer() {
	}
	

	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}
	
	public SolrServer getSolrServer() {
		return solrServer;
	}
	


	public LocalSolrServer(String solrServerName) {
		
		try{
			solrServer = XdSolrServerFactory.getSolrServer(solrServerName);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(solrServer == null){
			try {
				XdSolrServerFactory.initSolrServer();
				solrServer = XdSolrServerFactory.getSolrServer(solrServerName);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public SolrDocument findIndexItem(String keyword) throws SearchException {
		return ((SolrDocument) findIndexItems(keyword, 0, 0, 0,null,null).get(0));
	}



	public List<SolrDocument> findIndexItems(String keyword, int pagesize,
			int pageNo) throws Exception {
		return findIndexItems(keyword, pagesize, pageNo, 0,null,null);
	}

	@Override
	public List<SolrDocument> findIndexItems(String keyword, int pageSize,
			int pageNo, int start,String orderByField,ORDER order) throws SearchException {

		isNotEmptyAndNull(keyword);
		
		SolrQuery query;
		try {
			query = new SolrQuery();
			if (pageSize > 0){
				query.setRows(Integer.valueOf(pageSize));
			}
			if (pageNo > 0){
				--pageNo;
			}
			start += pageSize * pageNo;

			query.setQuery(keyword);
			if(orderByField == null){
				orderByField = "id";
			}
			if(order == null){
				order = SolrQuery.ORDER.desc;
			}
			query.setSortField(orderByField, order);
			query.setStart(start);
			
			SolrDocumentList list = this.getQueryResponse(query).getResults();
			
			return list;
		} catch (SearchException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}

	@Override
	public int getFoundCount(String keyword) throws SearchException {
		isNotEmptyAndNull(keyword);
		try {
			QueryResponse qResponse = this.getQueryResponse(new SolrQuery(keyword));
			return (int) qResponse.getResults().getNumFound();
		} catch (SearchException e) {
			e.printStackTrace();
			throw new SearchException(e);
		}
	}

	private QueryResponse getQueryResponse(SolrQuery query)
			throws SearchException {
		try {
			return solrServer.query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
			new SearchException(e);
		}
		return null;

	}

	private boolean isNotEmptyAndNull(String str) throws SearchException {
		if ((str == null) || (str.equals(""))){
			throw new SearchException("method verify Exception " + str+ " is null or length = 0 =========>> SolrHandleException");
		}
		return true;
	}


}
