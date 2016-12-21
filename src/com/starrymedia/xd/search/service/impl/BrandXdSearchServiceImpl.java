package com.starrymedia.xd.search.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.ModifiableSolrParams;

import com.starrymedia.community.core.dao.BrandDAO;
import com.starrymedia.community.core.entity.Brand;
import com.starrymedia.xd.search.common.Constants;
import com.starrymedia.xd.search.core.server.XdSolrServerFactory;
import com.starrymedia.xd.search.service.BrandXdSearchService;
import com.starrymedia.xd.search.util.Pagination;
import com.starrymedia.xd.search.util.QueryUtil;

/**
 * @author : Ares
 * @createTime : Sep 21, 2012 5:35:09 PM
 * @version : 1.0
 * @description :
 */
public class BrandXdSearchServiceImpl implements BrandXdSearchService {
	
	private static final Logger log = Logger.getLogger(BrandXdSearchServiceImpl.class);
	
	private BrandDAO brandDAO ;

	public BrandDAO getBrandDAO() {
		return brandDAO;
	}

	public void setBrandDAO(BrandDAO brandDAO) {
		this.brandDAO = brandDAO;
	}

	@Override
	public Pagination<Brand> findBrandByName(String name, Integer status, int pageNum,int pageSize){
		log.info("------Come SEARCH findBrandByName begin {name:"+name+" status:"+status+" pageNum:"+pageNum+" pageSize:"+pageSize+"} At "+new Date());
		
		List<Brand> brandList = new ArrayList<Brand>();
		
		String q = QueryUtil.getQuerySting(name);//对查询条件(关健词)进行过滤，去掉特殊符号
		
		SolrQuery query = new SolrQuery();
		query.set("start", (pageNum-1)*pageSize);
		query.set("rows", pageSize);
		query.set("fl", "*,score");
		if(status != null){
			ModifiableSolrParams params = new ModifiableSolrParams();  
			query.addFilterQuery("status:"+status); 
			//query.set("fq", status);
			params.add(query);
		}
//		query.set("qf","NAME^15.0 CATEGORY_SEARCH^5.0 LABEL_SEARCH^2.0 CBD_SEARCH^1.0 DISTRICT_SEARCH^1.0 ADDRESS^1.0 SERVICES_SEARCH^1.0 CONTENT^0.5");
//		query.set("bq","VIP:1^50.0");
		query.setSortField("score", SolrQuery.ORDER.desc);
//		query.set("hl", true);
		
		if(q != null && q.trim().length() > 0 ){
			query.setQuery(q);
		}else{
			query.setQuery("*:*");
		}
	
		
		int totalRecords = 0;
		SolrServer solrServer = XdSolrServerFactory.getSolrServer(Constants.BRAND_SOLR_SERVER_NAME);
		
		try {
			QueryResponse queryResponse = solrServer.query( query );
			
			List<SolrDocument> solrDocumentlist= queryResponse.getResults();
			for(SolrDocument d : solrDocumentlist){
				
				System.out.println(d);
				
				long id  = Long.valueOf(d.get("id").toString());
				
				Brand brand = this.brandDAO.findBrandByID(id);
				brandList.add(brand);
				
			}
			
			totalRecords = (int)queryResponse.getResults().getNumFound();
			
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		
		Pagination<Brand> result = new Pagination<Brand>();
		result.setCurrentPage(pageNum);
		result.setPageSize(pageSize);
		result.setRecords(brandList);
		result.setTotalRecords(totalRecords);
		
		log.info("------Come SEARCH findBrandByName end {name:"+name+" status:"+status+" pageNum:"+pageNum+" pageSize:"+pageSize+" Numfound:"+totalRecords+"} At "+new Date());
		
		return result;
	}

	/**
	 * TEST
	 * 
	 * @param args
	 */
	public static void main(String args[]){
		String q = QueryUtil.getQuerySting("ssssssss MMMMMMM (关健词)进行过滤，去掉特殊符号");
		
		System.out.print(q);
		
		BrandXdSearchService brandSearchService = new BrandXdSearchServiceImpl();
		brandSearchService.findBrandByName("星点", null, 0, 10);
	}



}
