/**
 * 
 */
package com.starrymedia.xd.search.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import com.starrymedia.community.core.bo.ShopBo;
import com.starrymedia.community.core.comm.Constant;
import com.starrymedia.community.core.dao.BrandLocationDAO;
import com.starrymedia.community.core.entity.BrandLocation;
import com.starrymedia.xd.search.common.Constants;
import com.starrymedia.xd.search.core.server.XdSolrServerFactory;
import com.starrymedia.xd.search.doc.ChangeBean2Doc;
import com.starrymedia.xd.search.doc.ShopDoc;
import com.starrymedia.xd.search.util.StringUtils;
import com.starrymedia.xd.search.util.Util;

/**
 * @author : Ares
 * @createTime : 2012-11-16 下午05:13:53
 * @version : 1.0
 * @description :
 */
public class ShopIndexJob implements IndexManage {
	
	private static final Logger log = Logger.getLogger(ShopIndexJob.class);
	
	private BrandLocationDAO brandLocationDAO;
	
	
	public BrandLocationDAO getBrandLocationDAO() {
		return brandLocationDAO;
	}

	public void setBrandLocationDAO(BrandLocationDAO brandLocationDAO) {
		this.brandLocationDAO = brandLocationDAO;
	}

	private SolrServer getLocalSolrServer(){
		SolrServer server = XdSolrServerFactory.getSolrServer(Constants.SHOP_SOLR_SERVER_NAME);
		return server;
	}

	/* (non-Javadoc)
	 * @see com.starrymedia.xd.search.job.IndexManage#createIndex()
	 */
	@Override
	public void createIndex() {
		//10 => 11
		log.info("<<<<  shopIndexJob createIndex begin ... ");
		long begin = System.currentTimeMillis();
		
		int totalAdd = 0 ;
		
		
		try {
			SolrServer server = this.getLocalSolrServer();
			
			int week = Util.getDayOfWeek(begin);
			Integer need2Create[] = { Constant.TEXT_INDEX_STATUS_UNDO,Constant.TEXT_INDEX_STATUS_UPDATE,Constant.TEXT_INDEX_STATUS_DELETED}; 
		    List<ShopBo> shopBoList = this.brandLocationDAO.find2CreateIndex(1, need2Create, begin, begin-week*3600*1000*24);
				
		    if(shopBoList == null || shopBoList.isEmpty()){
					log.info("\t\t no new shop index need to create\n");
					return ;
		     }
				
			Map<Long,ShopDoc> shopDocCache = new LinkedHashMap<Long,ShopDoc>();
			for(ShopBo s : shopBoList){
				ShopDoc shopDoc = null;
				if(shopDocCache.containsKey(s.getId())){
					shopDoc = shopDocCache.get(s.getId());
					shopDoc = this.wrap(shopDoc, s, true);
				}else{
					shopDoc = new ShopDoc();
					shopDoc = this.wrap(shopDoc, s, false);
					
					shopDocCache.put(s.getId(), shopDoc);
				}
				
			}
			
			Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
				
			ChangeBean2Doc<ShopDoc> change = new ShopDoc();
				
			StringBuffer logIds  =  new StringBuffer("[");
				
			SolrInputDocument doc = null;
			for(Map.Entry<Long, ShopDoc> entiy : shopDocCache.entrySet()){
				long id = entiy.getKey();
				ShopDoc shopDoc = entiy.getValue();
					
				doc = change.change2Doc(shopDoc);
					
				//TODO :
				BrandLocation b = this.brandLocationDAO.findBrandLocationByID(id);
				b.setIndexStatus(Constant.TEXT_INDEX_STATUS_DO);
				this.brandLocationDAO.updateBrandLocation(b);
					
				docs.add(doc);
				logIds.append(id+",");
			}
			server.add(docs);
				
			server.commit();

			totalAdd = shopDocCache.size();
			
			log.info("Now add shop ids :"+logIds.replace(logIds.length()-1, logIds.length(), "]"));
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		long end = System.currentTimeMillis();
		log.info("<<<<  shopIndexJob createIndex end.Total add index:"+totalAdd+" used time:["+(end-begin)+"]ms \n");
	}
	

	/* (non-Javadoc)
	 * @see com.starrymedia.xd.search.job.IndexManage#deleteIndex()
	 */
	@Override
	public void deleteIndex() {
		//01 21 => 03
		log.info("<<<<  shopIndexJob deleteIndex begin ...");
		long begin = System.currentTimeMillis();
		
		List<BrandLocation> blList = this.brandLocationDAO.findNeed2DeleteIndex();
		if(blList == null || blList.isEmpty()){
			log.info("\t\t no new shop index need to delete\n");
			return ;
		}
		
		int totalDelete = blList.size();
		
		List<String> ids = new ArrayList<String>();
		for(BrandLocation bl : blList){
			
			ids.add(bl.getId()+"");
			
			bl.setIndexStatus(Constant.TEXT_INDEX_STATUS_DELETED);
			this.brandLocationDAO.updateBrandLocation(bl);
		}
		
		SolrServer server = this.getLocalSolrServer();
		
		try {
			server.deleteById(ids);
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		log.info("Now delete shop ids :"+ids.toString());
		log.info("<<<<  shopIndexJob deleteIndex end. Total delete index:"+totalDelete+" used time:["+(end-begin)+"]ms \n");
	}

	/* (non-Javadoc)
	 * @see com.starrymedia.xd.search.job.IndexManage#updateIndex()
	 */
	@Override
	public void updateIndex() {
		//12  13  => 11 放入在createIndex一起
	}
	
	private ShopDoc wrap(ShopDoc shopDoc ,ShopBo shop ,boolean flag){
		
		if(!flag){
			
			String country = StringUtils.fillZero(shop.getCountry()+"",2,true);
			String province = country + StringUtils.fillZero(shop.getProvince()+"",2,true);
			String city = province + StringUtils.fillZero(shop.getCity()+"",2,true);
			String sowntown =  city + StringUtils.fillZero(shop.getSowntownId()+"",2,true);
			
			shopDoc.setId(shop.getId());
			shopDoc.setAddress(shop.getAddress());
			shopDoc.setBrandId(shop.getBrandId());
			shopDoc.setBrandName(shop.getBrandName());
			shopDoc.setBrandUserId(shop.getBrandUserId());
			shopDoc.setCountry(country);
			shopDoc.setProvince(province);
			shopDoc.setCity(city);
			shopDoc.setSowntownId(sowntown);
			shopDoc.setLatitude(shop.getLatitude());
			shopDoc.setLongitude(shop.getLongitude());
			shopDoc.setShopName(shop.getShopName());
			shopDoc.setUseCount(shop.getUseCount());
			shopDoc.setLocation(shop.getLatitude()+","+shop.getLongitude());
		}
		shopDoc.setMpName(shop.getMpName());
		shopDoc.setGoodsName(shop.getGoodsName());
		
		String catId = shop.getCategory();
		String c[] = catId.split("\\,");
		
		for(String s : c){
			if(s.length() != 4){
				continue;
			}
			shopDoc.setCategoryP(s.substring(0, 2));
			shopDoc.setCategoryC(s);
		}
		
		return shopDoc;
	}

}
