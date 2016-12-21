package com.starrymedia.xd.search.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import com.starrymedia.community.core.comm.Constant;
import com.starrymedia.community.core.dao.BrandDAO;
import com.starrymedia.community.core.entity.Brand;
import com.starrymedia.xd.search.common.Constants;
import com.starrymedia.xd.search.core.server.XdSolrServerFactory;
import com.starrymedia.xd.search.doc.BrandDoc;
import com.starrymedia.xd.search.doc.ChangeBean2Doc;

/**
 * 
 * @author : Ares
 * @createTime : Sep 21, 2012 4:04:24 PM
 * @version : 1.0
 * @description :
 */
public class BrandIndexJob implements IndexManage{
	
	private static final Logger log = Logger.getLogger(BrandIndexJob.class);
	
	private BrandDAO brandDAO ;
	
	
	public BrandDAO getBrandDAO() {
		return brandDAO;
	}


	public void setBrandDAO(BrandDAO brandDAO) {
		this.brandDAO = brandDAO;
	}
	
	
	private SolrServer getLocalSolrServer(){
		SolrServer server = XdSolrServerFactory.getSolrServer(Constants.BRAND_SOLR_SERVER_NAME);
		return server;
	}
	
	
	@Override
	public void createIndex(){
		
		log.info("<<<<  BrandIndexJob createIndex begin... ");
		long begin = System.currentTimeMillis();
		
		int totalAdd = 0 ;
		try {
			SolrServer server = this.getLocalSolrServer();
			
			SolrInputDocument doc = null;
			
			Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
			
			List<Brand> brandList = this.getBrandDAO().findBrandsByTxtIdxStatus(Constant.TEXT_INDEX_STATUS_UNDO);
			if(brandList == null || brandList.isEmpty()){
				log.info("\t\t no new brand index need to create");
				return ;
			}
			
			totalAdd = brandList.size();
			
			ChangeBean2Doc<Brand> change = new BrandDoc();
			
			for(Brand b : brandList){
				
				doc = change.change2Doc(b);
				
				b.setTxtIdxStatus(Constant.TEXT_INDEX_STATUS_DO);
	            this.getBrandDAO().updateBrand(b);
				
				docs.add(doc);
			}
			server.add(docs);
		
			server.commit();
			//server.optimize();
			
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		log.info("<<<<  BrandIndexJob createIndex end.Total add index:"+totalAdd+" used time:["+(end-begin)+"]ms");
	}
	
	@Override
	public void updateIndex(){
		log.info("<<<<  BrandIndexJob updateIndex...");
		long begin = System.currentTimeMillis();
		
		List<Brand> brandList = this.getBrandDAO().findBrandsByTxtIdxStatus(Constant.TEXT_INDEX_STATUS_UPDATE);
		
		int totalUpdate = this.todoDelete(brandList, Constant.TEXT_INDEX_STATUS_UNDO);
		
		this.createIndex();
		
		long end = System.currentTimeMillis();
		log.info("<<<<  BrandIndexJob updateIndex end. Total update index:"+totalUpdate+" used time:["+(end-begin)+"]ms");
	}


	@Override
	public void deleteIndex() {
		log.info("<<<<  BrandIndexJob deleteIndex...");
		long begin = System.currentTimeMillis();
		
		List<Brand> brandList = this.getBrandDAO().findBrandsByTxtIdxStatus(Constant.TEXT_INDEX_STATUS_DELETED);
		
		int totalDelete = this.todoDelete(brandList, Constant.TEXT_INDEX_STATUS_DO);
		
		long end = System.currentTimeMillis();
		log.info("<<<<  BrandIndexJob deleteIndex end. Total delete index:"+totalDelete+" used time:["+(end-begin)+"]ms");
	}
	
	
	private int todoDelete(List<Brand> brandList,Integer txtIdxStatus){
		
		if(brandList == null || brandList.isEmpty()){
			return 0;
		}
		
		int total = brandList.size();
		
		try {
			SolrServer server = this.getLocalSolrServer();
			
			List<String> ids = new ArrayList<String>();
			for(Brand b : brandList){
		
				ids.add(b.getId()+"");
				
			    if (b.getTxtIdxDeleted().intValue() == Constant.TEXT_INDEX_DELETED) {
	                this.getBrandDAO().deleteBrandByID(b.getId());
	                continue;
	            }

				//TODO:修改数据库已删除相应索引
				b.setTxtIdxStatus(txtIdxStatus);
	            this.getBrandDAO().updateBrand(b);
			}
			
			server.deleteById(ids);
			server.commit();
			
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return total;
	}


}
