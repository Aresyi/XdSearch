/**
 * 
 */
package com.starrymedia.xd.search.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import com.starrymedia.community.core.dao.BrandDAO;
import com.starrymedia.community.core.dao.BrandLocationDAO;
import com.starrymedia.community.core.dao.UserFavoriteShopStoreDAO;
import com.starrymedia.community.core.entity.Brand;
import com.starrymedia.community.core.entity.BrandLocation;
import com.starrymedia.community.core.entity.UserFavoritShopStore;
import com.starrymedia.xd.search.common.Constants;
import com.starrymedia.xd.search.common.MyLog;
import com.starrymedia.xd.search.core.server.XdSolrServerFactory;
import com.starrymedia.xd.search.query.bo.ShopQuery;
import com.starrymedia.xd.search.service.ShopXdSearchService;
import com.starrymedia.xd.search.util.Pagination;
import com.starrymedia.xd.search.util.QueryUtil;
import com.starrymedia.xd.search.util.StringUtils;
import com.starrymedia.xd.search.util.Util;

/**
 * @author : Ares
 * @createTime : 2012-11-19 上午11:33:53
 * @version : 1.0
 * @description :
 * 
 *  Solr4.0可以直接返回偏移距离
 */
public class ShopXdSearchServiceImpl implements ShopXdSearchService {
	
	private static final Logger log = Logger.getLogger(ShopXdSearchServiceImpl.class);
	
	
	private SolrServer shopSolrServer ;
	
	
	
	private BrandLocationDAO brandLocationDAO;
	
	private BrandDAO brandDAO;
	
	private UserFavoriteShopStoreDAO userFavoritShopStoreDAO;
	
	


	public UserFavoriteShopStoreDAO getUserFavoritShopStoreDAO() {
		return userFavoritShopStoreDAO;
	}


	public void setUserFavoritShopStoreDAO(
			UserFavoriteShopStoreDAO userFavoritShopStoreDAO) {
		this.userFavoritShopStoreDAO = userFavoritShopStoreDAO;
	}


	public BrandDAO getBrandDAO() {
		return brandDAO;
	}


	public void setBrandDAO(BrandDAO brandDAO) {
		this.brandDAO = brandDAO;
	}


	public BrandLocationDAO getBrandLocationDAO() {
		return brandLocationDAO;
	}


	public void setBrandLocationDAO(BrandLocationDAO brandLocationDAO) {
		this.brandLocationDAO = brandLocationDAO;
	}


	/* (non-Javadoc)
	 * @see com.starrymedia.xd.search.service.ShopXdSearchService#search(com.starrymedia.xd.search.query.bo.GoodsQuery)
	 */
	@Override
	public Pagination<BrandLocation> searchShop(ShopQuery shopQuery) {
		log.info("------Come shopSearch begin "+shopQuery.toString()+" At "+new Date());
		
		SolrQuery query = new SolrQuery();
		
		String keyword = shopQuery.getKeyword();
		int pageNum = shopQuery.getPageNum();
		int pageSize = shopQuery.getPageSize();
		
		String q = QueryUtil.getQuerySting(keyword);
		pageNum = pageNum > 0 ? pageNum : 1 ;
		
		if(q == null || q.trim().length() <= 0 ){
			q = "*:*";
		}
		
		query.setQuery(q);
		query.setStart((pageNum-1)*pageSize);
		query.setRows(pageSize);
		query.setFields("*,score");

		
		SolrQuery.ORDER order = SolrQuery.ORDER.desc;
		if(shopQuery.getDescOrAsc() != null){
			if("asc".equals(shopQuery.getDescOrAsc().toLowerCase())){
				order = SolrQuery.ORDER.asc;
			}
		}
		
		if(StringUtils.isNotEmpty(shopQuery.getOrderBy())){
			if("offsetDis".equals(shopQuery.getOrderBy())){
				query.set("sort", "geodist() "+shopQuery.getDescOrAsc());//特殊
			}else{
				query.setSortField(shopQuery.getOrderBy(),order);
			}
		}else{
			query.setSortField("score", order);
		}        
		
		  
		
//		测试使用
//		query.addFilterQuery("{!geofilt pt=31.200742,121.6068219 sfield=location d=5}");
//		query.addFilterQuery("{!geofilt}&sfield=location&pt=31.200742,121.6068219&d=5&sort=geodist() asc");
//		query.set("fl", "dis:geodist()");
		
		if(shopQuery.getLatitude() != 0 && shopQuery.getLongitude() != 0){
			
			String location = shopQuery.getLatitude()+","+shopQuery.getLongitude();
			
			query.addFilterQuery("{!bbox}");
			query.set("sfield", "location");
			query.set("pt",location);
			query.set("d", shopQuery.getOffsetDis());
		}
		
		InfoWrap info = new  InfoWrap(shopQuery);
		
		this.addMoreFilter(query, shopQuery,info);
		
		
		
		SolrServer solrServer = XdSolrServerFactory.getSolrServer(Constants.SHOP_SOLR_SERVER_NAME);
		
		QueryResponse queryResponse = null;
		
		Pagination<BrandLocation> result = new Pagination<BrandLocation>();
		result.setCurrentPage(pageNum);
		result.setPageSize(pageSize);
		
		try {
			queryResponse = solrServer.query( query );
			result = this.processCommonSearch(shopQuery,solrServer,queryResponse,query, q, result);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		
		log.info("------Come goodsSearch end "+shopQuery.toString()+" {Numfound:"+result.getTotalRecords()+"} At "+new Date()+"\n");
		
		return result;
	}

	
	/**
	 * 常规搜索查看方式
	 * @param shopQuery
	 * @param solrServer
	 * @param queryResponse
	 * @param query
	 * @param q
	 * @param result
	 * @return
	 * @throws SolrServerException
	 */
	private Pagination<BrandLocation> processCommonSearch(ShopQuery shopQuery,SolrServer solrServer,QueryResponse queryResponse,SolrQuery query,
				 String q,Pagination<BrandLocation> result) throws SolrServerException {

		List<BrandLocation> brandLocationList = new ArrayList<BrandLocation>();

		int totalRecords = (int) queryResponse.getResults().getNumFound();

		if (totalRecords <= 0 && ( q.trim().length() > 3 && q.trim().length() < 20 )) {//否则没必要吖
			if(!q.contains(" ")){
				query.setQuery(QueryUtil.splitWord(q));//有些耗时滴
			}
			query.set("q.op", "OR");
			queryResponse = solrServer.query(query);
			totalRecords = (int) queryResponse.getResults().getNumFound();
			
			log.info("\t\t *** shop more word OR search ***{totalRecords: "+totalRecords+"}");
		}

		List<SolrDocument> solrDocumentlist = queryResponse.getResults();

		for (SolrDocument d : solrDocumentlist) {

			MyLog.logInfo(d,true);

			long id = Long.valueOf(d.get("id").toString());

			BrandLocation b = this.brandLocationDAO.findBrandLocationByID(id);
			Brand brand = this.brandDAO.findBrandByID(b.getBrandID());
			
			b.setBrand(brand);
			
			if(shopQuery.getLatitude() != 0 && shopQuery.getLongitude() != 0){
				b.setDistance(Util.distanceByLngLat(b.getLongitude(), b.getLatitude(), shopQuery.getLongitude(), shopQuery.getLatitude()));
			}

			brandLocationList.add(b);

		}
		
		//指定用户是否收藏该门店处理
		if(brandLocationList.size() > 0 && shopQuery.getUserId() > 0){
			List<UserFavoritShopStore> favList = this.userFavoritShopStoreDAO.findUserFavoritList(shopQuery.getUserId());
			
			if(favList != null && favList.size() > 0){
				Map<Long,Long> cache = new HashMap<Long,Long>(brandLocationList.size());
				for(UserFavoritShopStore favShop : favList){
					cache.put(Long.valueOf(favShop.getShopId()), favShop.getCreateTime());
				}
				
				for(BrandLocation bl : brandLocationList){
					if(cache.containsKey(bl.getId())){
						bl.setFavoritedTime(cache.get(bl.getId()));
					}
				}
			}
		}

		result.setRecords(brandLocationList);
		result.setTotalRecords(totalRecords);

		return result;
	}
	
	/**
	 * 添加更多的过滤条件
	 * @param query
	 * @param shopQuery
	 * @param info
	 * @param now
	 * @return
	 */
	private SolrQuery addMoreFilter(SolrQuery query,ShopQuery shopQuery,InfoWrap info){
		
		this.addCityFilter(shopQuery, query,info);
		this.addCategoryFilter(shopQuery, query);
		
		return query;
	}
	
	/**
	 * 添加省、市、地区筛选
	 * @param shopQuery
	 * @param query
	 * @return
	 */
	private SolrQuery addCityFilter(ShopQuery shopQuery,SolrQuery query,InfoWrap info){
		
		if(StringUtils.isNotEmpty(shopQuery.getProvince())){
			query.addFilterQuery("province:"+info.province); 
		}
		if(StringUtils.isNotEmpty(shopQuery.getCity())){
			query.addFilterQuery("city:"+info.city); 
		}
		if(StringUtils.isNotEmpty(shopQuery.getSowntown())){
			query.addFilterQuery("sowntown:"+info.sowntown); 
		}
		
		return query;
	}
	
	/**
	 * 添加分类筛选
	 * @param shopQuery
	 * @param query
	 * @return
	 */
	private SolrQuery addCategoryFilter(ShopQuery shopQuery,SolrQuery query){
		
		if(StringUtils.isNotEmpty(shopQuery.getCategoryP())){
			query.addFilterQuery("categoryP:"+shopQuery.getCategoryP()); 
		}
		if(StringUtils.isNotEmpty(shopQuery.getCategoryC())){
			query.addFilterQuery("categoryC:"+shopQuery.getCategoryC()); 
		}
		
		return query;
	}
	
	/**
	 * 信息包装处理
	 * （未放GoodsQuery直接处理）
	 * @author : Ares
	 * @createTime : 2012-11-1 上午10:32:58
	 * @version : 1.0
	 * @description :
	 */
	private static class InfoWrap{
		
		String country ;
		String province ;
		String city ;
		String sowntown;
		
		InfoWrap(ShopQuery shopQuery){
			 country = StringUtils.fillZero(shopQuery.getCountry(),2,true);
			 province = country + StringUtils.fillZero(shopQuery.getProvince(),2,true);
			 city = province + StringUtils.fillZero(shopQuery.getCity(),2,true);
			 sowntown =  city + StringUtils.fillZero(shopQuery.getSowntown(),2,true);
		}
		
	}
}
