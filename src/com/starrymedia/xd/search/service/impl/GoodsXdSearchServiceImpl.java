package com.starrymedia.xd.search.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.starrymedia.community.core.dao.BrandDAO;
import com.starrymedia.community.core.dao.MemberPromotionDAO;
import com.starrymedia.community.core.dao.MemberPromotionLocationDAO;
import com.starrymedia.community.core.dao.MemberPromotionProductDAO;
import com.starrymedia.community.core.entity.Brand;
import com.starrymedia.community.core.entity.MemberPromotion;
import com.starrymedia.community.core.entity.MemberPromotionLocation;
import com.starrymedia.community.core.entity.MemberPromotionProduct;
import com.starrymedia.xd.search.common.MyLog;
import com.starrymedia.xd.search.common.SolrServerName;
import com.starrymedia.xd.search.core.server.XdSolrServerFactory;
import com.starrymedia.xd.search.query.bo.GoodsQuery;
import com.starrymedia.xd.search.service.GoodsXdSearchService;
import com.starrymedia.xd.search.util.Pagination;
import com.starrymedia.xd.search.util.QueryUtil;
import com.starrymedia.xd.search.util.StringUtils;
import com.starrymedia.xd.search.util.Util;

/**
 * @author : Ares
 * @createTime : 2012-10-11 下午01:28:38
 * @version : 1.0
 * @description :
 * 
 * <p>避免过多的方法调用浪费时间，故一些地方未做剥离子方法处理
 * <p>一些参数（如省、市）在前端处理后直接传过来，就无需服务端每次耗时处理。但为了兼容先前社区系统的SQL查询，故...
 */
public class GoodsXdSearchServiceImpl implements GoodsXdSearchService {
	
	private static final Logger log = Logger.getLogger(GoodsXdSearchServiceImpl.class);
	
	
	private SolrServer goodsSolrServer;
	
	
	private MemberPromotionDAO memberPromotionDAO; 
    private MemberPromotionProductDAO memberPromotionProductDAO;
    private MemberPromotionLocationDAO memberPromotionLocationDAO;
    private BrandDAO brandDAO;
	

	public MemberPromotionDAO getMemberPromotionDAO() {
		return memberPromotionDAO;
	}

	public void setMemberPromotionDAO(MemberPromotionDAO memberPromotionDAO) {
		this.memberPromotionDAO = memberPromotionDAO;
	}

	public MemberPromotionProductDAO getMemberPromotionProductDAO() {
		return memberPromotionProductDAO;
	}

	public void setMemberPromotionProductDAO(
			MemberPromotionProductDAO memberPromotionProductDAO) {
		this.memberPromotionProductDAO = memberPromotionProductDAO;
	}

	public MemberPromotionLocationDAO getMemberPromotionLocationDAO() {
		return memberPromotionLocationDAO;
	}

	public void setMemberPromotionLocationDAO(
			MemberPromotionLocationDAO memberPromotionLocationDAO) {
		this.memberPromotionLocationDAO = memberPromotionLocationDAO;
	}

	public BrandDAO getBrandDAO() {
		return brandDAO;
	}

	public void setBrandDAO(BrandDAO brandDAO) {
		this.brandDAO = brandDAO;
	}

	private SolrServer getGoodsSolrServer() {
		if(goodsSolrServer == null){
			goodsSolrServer = XdSolrServerFactory.getSolrServer(SolrServerName.GOODS);
		}
		return goodsSolrServer;
	}

	@Override
	public Pagination<MemberPromotion> search(GoodsQuery goodsQuery) {
		log.info("------Come goodsSearch begin "+goodsQuery.toString()+" At "+new Date());
		
		
		SolrQuery query = new SolrQuery();
		
		String keyword = goodsQuery.getKeyword();
		int pageNum = goodsQuery.getPageNum();
		int pageSize = goodsQuery.getPageSize();
		long now = goodsQuery.getNow();
		boolean isNeedSetHighlight = true;
		boolean isGroupSearch = false;
		
		String q = QueryUtil.getQuerySting(keyword);
		pageNum = pageNum > 0 ? pageNum : 1 ;
		
		if(q == null || q.trim().length() <= 0 ){
			q = "*:*";
			isNeedSetHighlight = false;
		}
		
		query.setQuery(q);
		query.setStart((pageNum-1)*pageSize);
		query.setRows(pageSize);
		query.setFields("id");
		query.set("qf", "mpName^5 goodsName^4 brandName^3 address^2");
		
		
		InfoWrap info = new  InfoWrap(goodsQuery);
		
		this.addMoreFilter(query, goodsQuery,info,now);
		
		SolrQuery.ORDER order = SolrQuery.ORDER.desc;
		if(goodsQuery.getDescOrAsc() != null){
			if("asc".equals(goodsQuery.getDescOrAsc().toLowerCase())){
				order = SolrQuery.ORDER.asc;
			}
		}
		
		if(StringUtils.isNotEmpty(goodsQuery.getOrderBy())){
			query.setSortField(goodsQuery.getOrderBy(),order);//特殊
		}else{
			query.setSortField("score", order);
		}
		
		if("brand".equals(goodsQuery.getLookType())){
			query.set("group", true);
			query.set("group.field", "brandUserId");
			query.set("group.cache.percent", 20);
			query.set("group.ngroups", true);
			
			isGroupSearch = true;
		}
		
		
		
		query.setFacet(true);
		String facetFields[] = null;
		boolean isNeedNewFacetPC = false,isNeedNewFacetCS = false;
		
		if(StringUtils.isNotEmpty(goodsQuery.getCategoryP()) && StringUtils.isEmpty(goodsQuery.getCity())){
			facetFields = new String[] {"city","sowntown"};
			isNeedNewFacetPC = true;
		}else if(StringUtils.isNotEmpty(goodsQuery.getCity()) && StringUtils.isEmpty(goodsQuery.getCategoryP())){
			facetFields = new String[] {"categoryP","categoryC"};
			isNeedNewFacetCS = true;
		}else if(StringUtils.isEmpty(goodsQuery.getCategoryP()) && StringUtils.isEmpty(goodsQuery.getCity())){
			facetFields = new String[] {"categoryP","categoryC","city","sowntown"};
		}else{
			isNeedNewFacetPC = true;
			isNeedNewFacetCS = true;
		}
		
		if(facetFields != null){
			query.addFacetField(facetFields);
		}
		
		
		if( isNeedSetHighlight ){
			query.setHighlight(true);
			query.setParam("hl.fl", "mpName,goodsName,brandName,address");
			query.setHighlightSimplePre("<font color='red'>");
			query.setHighlightSimplePost("</font>");
		}
		
		
		SolrServer solrServer = this.getGoodsSolrServer();
		
		QueryResponse queryResponse = null;
		
		Pagination<MemberPromotion> result = new Pagination<MemberPromotion>();
		result.setCurrentPage(pageNum);
		result.setPageSize(pageSize);
		
		try {
			queryResponse = solrServer.query( query );
			if(isGroupSearch){
				result = this.processGroupSearch(solrServer, queryResponse, query, isNeedSetHighlight, result);
			}else{
				result = this.processCommonSearch(solrServer,queryResponse,query, isNeedSetHighlight, q, result);
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		
		
		result.setFacetMap(this.wrapFacetResult(queryResponse));
		
		if(isNeedNewFacetPC){
			result.setFacetMap(this.reFacet(solrServer, query,goodsQuery,info,true, false,result.getFacetMap()));
		}
		
		if(isNeedNewFacetCS){
			result.setFacetMap(this.reFacet(solrServer, query,goodsQuery,info,false, true, result.getFacetMap()));
		}
		
		
		log.info("------Come goodsSearch end "+goodsQuery.toString()+" {Numfound:"+result.getTotalRecords()+"} At "+new Date()+"\n");
		
		return result;
	}
	
	@Override
	public Map<String,Long> ajaxSuggest(GoodsQuery goodsQuery) {
		log.info("------Come ajaxSuggest begin "+goodsQuery.toString()+" At "+new Date());
		
		long now = System.currentTimeMillis();
		
		String keyword = QueryUtil.getQuerySting(goodsQuery.getKeyword());
		
		SolrServer solrServer = this.getGoodsSolrServer();
		
		SolrQuery query = new SolrQuery();
        query.setQuery(keyword);
        query.setFacet(true);
        query.addFacetField("search");
        query.setFacetPrefix(keyword);
        query.setFacetMinCount(1);
        query.setFacetLimit(15);
        query.set("qf", "mpName^5 goodsName^4 brandName^3 address^2");
        
        
        InfoWrap info = new  InfoWrap(goodsQuery);
        this.addMoreFilter(query, goodsQuery,info,now);
        
        Map<String,Long> res = new HashMap<String,Long>();
		try {
			 QueryResponse response = solrServer.query(query);
			 List<FacetField> facets = response.getFacetFields();
		        
		        for (FacetField facet : facets) {
		        	
		        	List<Count> counts = facet.getValues();
		        	
		        	MyLog.logInfo(facet.getName()+":"+counts.size());
		        	
		        	MyLog.logInfo("----------------");
		        	
		        	for (int i =0 ; i < counts.size() ;i++) {
		        		Count count = counts.get(i);
		        		
		        		String v = count.getName();
		        		long c = count.getCount();
		        		
		        		MyLog.logInfo(count.getName() + ":" + count.getCount());
		        		
		        		res.put(v, c);
		        	}
		        	
		        	MyLog.logInfo("");
		        }
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
       
		
		return res;
	}
	
	/**
	 * 常规搜索查看方式
	 * @param solrServer
	 * @param queryResponse
	 * @param query
	 * @param isNeedSetHighlight
	 * @param q
	 * @param result
	 * @return
	 * @throws SolrServerException
	 */
	private Pagination<MemberPromotion> processCommonSearch(SolrServer solrServer,QueryResponse queryResponse,SolrQuery query,
				boolean isNeedSetHighlight, String q,Pagination<MemberPromotion> result) throws SolrServerException {

		List<MemberPromotion> memberPromotionList = new ArrayList<MemberPromotion>();

		int totalRecords = (int) queryResponse.getResults().getNumFound();

		if (totalRecords <= 0 && ( q.trim().length() > 3 && q.trim().length() < 20 )) {//否则没必要吖
			if(!q.contains(" ")){
				query.setQuery(QueryUtil.splitWord(q));//有些耗时滴
			}
			query.set("q.op", "OR");
			queryResponse = solrServer.query(query);
			totalRecords = (int) queryResponse.getResults().getNumFound();
			
			log.info("\t\t *** more word OR search ***{totalRecords: "+totalRecords+"}");
		}

		List<SolrDocument> solrDocumentlist = queryResponse.getResults();
		Map<String, Map<String, List<String>>> highlightMap = null;
		if (isNeedSetHighlight) {
			highlightMap = queryResponse.getHighlighting();
		}

		for (SolrDocument d : solrDocumentlist) {

			MyLog.logInfo(d);

			long id = Long.valueOf(d.get("id").toString());

			MemberPromotion m = this.memberPromotionDAO.selectMemberPromotionByID(id);

			Brand brand = this.brandDAO.findBrandByID(m.getBrandId());
			List<MemberPromotionProduct> memPromoProductList = this.memberPromotionProductDAO
					.findMemberPromotionProductByPromotionID(m.getId());
			List<MemberPromotionLocation> memPromoLocationList = this.memberPromotionLocationDAO
					.findMemberPromotionLocationByPromotionID(m.getId());

			if (highlightMap != null) {
				Map<String, List<String>> map = highlightMap.get(id + "");

				if (map != null) {

					Object mpName = map.get("mpName");
					Object brandName = map.get("brandName");
					Object address = map.get("address");

					if (mpName != null) {
						String s = mpName.toString();
						m.setName(this.interceptStr(s));
					}

					if (brandName != null) {
						String s = brandName.toString();
						brand.setName(this.interceptStr(s));
					}

					if (address != null) {// 有些笨拙
						String s[] = q.split(" ");
						for (MemberPromotionLocation mpl : memPromoLocationList) {
							String _address = mpl.getAddress();
							for (String _s : s) {
								_address = _address.replace(_s,
										"<font color='red'>" + _s + "</font>");
							}
							mpl.setAddress(_address);
						}
					}

					MyLog.logInfo(mpName);
					MyLog.logInfo(brandName);
					MyLog.logInfo(address);
				}
			}

			m.setBrand(brand);
			m.setMemberPromotionProductList(memPromoProductList);
			m.setMemberPromotionLocationList(memPromoLocationList);

			memberPromotionList.add(m);

		}

		result.setRecords(memberPromotionList);
		result.setTotalRecords(totalRecords);

		return result;
	}
	
	/**
	 * 按商家方式查看
	 * @param solrServer
	 * @param queryResponse
	 * @param query
	 * @param isNeedSetHighlight
	 * @param result
	 * @return
	 */
	private Pagination<MemberPromotion> processGroupSearch(SolrServer solrServer,QueryResponse queryResponse,SolrQuery query,
			boolean isNeedSetHighlight,Pagination<MemberPromotion> result) {
		log.info("\t\t [processGroupSearch to do...]");
		
		GroupResponse groupResponse = queryResponse.getGroupResponse();

		if (groupResponse == null) {
			return result;
		}
		List<MemberPromotion> memberPromotionList = new ArrayList<MemberPromotion>();

		List<GroupCommand> groupCommandList = groupResponse.getValues();
		GroupCommand groupCommand = groupCommandList.get(0);// 这里只按brandUserId分组

		int totalRecords = groupCommand.getNGroups();

		Map<String, Map<String, List<String>>> highlightMap = null;
		if (isNeedSetHighlight) {
			highlightMap = queryResponse.getHighlighting();
		}

		List<Group> groups = groupCommand.getValues();
		for (Group group : groups) {

			SolrDocumentList solrDocumentList = group.getResult();

			int sellCount = (int) solrDocumentList.getNumFound();

			for (SolrDocument d : solrDocumentList) {// 其实这里默认只取一条记录

				MyLog.logInfo(d);

				long id = Long.valueOf(d.get("id").toString());

				MemberPromotion m = this.memberPromotionDAO.selectMemberPromotionByID(id);
				Brand brand = this.brandDAO.findBrandByID(m.getBrandId());

				if (highlightMap != null) {
					Map<String, List<String>> map = highlightMap.get(id + "");

					if (map != null) {

						Object mpName = map.get("mpName");
						Object brandName = map.get("brandName");

						if (mpName != null) {
							String s = mpName.toString();
							m.setName(this.interceptStr(s));
						}

						if (brandName != null) {
							String s = brandName.toString();
							brand.setName(this.interceptStr(s));
						}

						MyLog.logInfo(mpName);
						MyLog.logInfo(brandName);
					}
				}
				m.setSellcount(sellCount);
				m.setBrand(brand);
				memberPromotionList.add(m);
			}

		}

		result.setRecords(memberPromotionList);
		result.setTotalRecords(totalRecords);
		return result;
	}
	
	/**
	 * 再次Facet
	 * @param solrServer
	 * @param query
	 * @param goodsQuery
	 * @param isNeedNewFacetPC
	 * @param isNeedNewFacetCS
	 * @param facetMap
	 * @return
	 */
	private Map<String, Map<String,Long>> reFacet(SolrServer solrServer,SolrQuery query,GoodsQuery goodsQuery,InfoWrap info,
			boolean isNeedNewFacetPC ,boolean isNeedNewFacetCS,Map<String, Map<String,Long>> facetMap){
		
		String facetFields[] = null;
		if(isNeedNewFacetPC){
			query.removeFacetField("city");
			query.removeFacetField("sowntown");
			
			query.removeFilterQuery("categoryP:"+goodsQuery.getCategoryP());
			query.removeFilterQuery("categoryC:"+goodsQuery.getCategoryC());
			
//			this.addCityFilter(goodsQuery,query,info);//注意这里
			
			facetFields = new String[] {"categoryP","categoryC"};
		}
		
		if(isNeedNewFacetCS){
			
			query.removeFacetField("categoryP");
			query.removeFacetField("categoryC");
			
			query.removeFilterQuery("province:"+info.province);
			query.removeFilterQuery("city:"+info.city);
			query.removeFilterQuery("sowntown:"+info.sowntown);
			
			this.addCategoryFilter(goodsQuery, query);
			
			facetFields = new String[] {"city","sowntown"};
		}
		query.addFacetField(facetFields);
		
		try {
			 QueryResponse response = solrServer.query(query);
			 facetMap.putAll(this.wrapFacetResult(response));
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return facetMap;
	}
	
	/**
	 * 包装切面搜索结果
	 * @param queryResponse
	 * @return
	 */
	private Map<String, Map<String,Long>> wrapFacetResult(QueryResponse queryResponse){
		
		Map<String, Map<String,Long>> facetMap = new LinkedHashMap<String, Map<String,Long>>();

		List<FacetField> facetFieldList = queryResponse.getFacetFields();
		
		if(facetFieldList == null || facetFieldList.isEmpty()){
			return facetMap;
		}
		try{
			for(FacetField f : facetFieldList){
				List<Count> counts = f.getValues();
				if(counts == null){
					continue;
				}
				
	        	MyLog.logInfo(f.getName()+":"+counts.size());
	        	MyLog.logInfo("----------------");
	        	
	        	Map<String,Long>  m = new LinkedHashMap<String,Long>();
	        	
	        	for (Count count : counts) {
	        		MyLog.logInfo(count.getName() + ":" + count.getCount());
	        		m.put(count.getName(), count.getCount());
	        	}
	        	
	        	MyLog.logInfo("");
	        	
	        	facetMap.put(f.getName(), m);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return facetMap;
	}

	
	/**
	 * 添加更多的过滤条件
	 * @param query
	 * @param goodsQuery
	 * @param info
	 * @param now
	 * @return
	 */
	private SolrQuery addMoreFilter(SolrQuery query,GoodsQuery goodsQuery,InfoWrap info,long now){
		
		query.addFilterQuery("caseType:{99 TO *}"); 
		query.addFilterQuery("showStartTime:{* TO "+now+"}"); 
		query.addFilterQuery("showEndTime:{"+now+" TO *}"); 
		
		
		if(goodsQuery.getStatus() != -1){
			query.addFilterQuery("status:"+goodsQuery.getStatus());
		}
		if(goodsQuery.getCaseType() != -1 && goodsQuery.getCaseType() != 200){
			query.addFilterQuery("caseType:"+goodsQuery.getCaseType()); 
		}
		if(goodsQuery.getBrandUserId() != -1){
			query.addFilterQuery("brandUserId:"+goodsQuery.getBrandUserId()); 
		}
		
		this.addCityFilter(goodsQuery, query,info);
		this.addCategoryFilter(goodsQuery, query);
		
		if(goodsQuery.getSpecifyQuery() == 1){
			
			goodsQuery.setQueryType(-1);
			
			if(goodsQuery.getTimerWeek() <= 0){
				goodsQuery.setTimerWeek(Util.WEEK[Util.getDayOfWeek(System.currentTimeMillis())-1]);
			}
			
			query.addFilterQuery(
					"(-timerWeek:"+goodsQuery.getTimerWeek()
					+ ") OR (timerBeginTime:{"+(now - goodsQuery.getToday())+" TO *} OR timerEndTime:{0 TO "+(now - goodsQuery.getToday())+"})" 
					+ " OR startTime:{"+now+" TO *}"
					);
		}

		if(goodsQuery.getQueryType() == 1){
			if(goodsQuery.getTimerWeek() != 0){
				query.addFilterQuery("timerWeek:"+goodsQuery.getTimerWeek()); 
			}
			if(goodsQuery.getToday() != 0){
				query.addFilterQuery("timerBeginTime:[0 TO "+(now - goodsQuery.getToday())+"]"); 
				query.addFilterQuery("timerEndTime:["+(now - goodsQuery.getToday())+" TO *]"); 
			}
			query.addFilterQuery("startTime:[0 TO "+now+"]"); 
			query.addFilterQuery("deadline:["+now+" TO *]"); 
		}
		
		if(goodsQuery.getChannelType() > 1){
			query.addFilterQuery("channelType:"+goodsQuery.getChannelType()); 
		}
		
		return query;
	}
	
	/**
	 * 添加省、市、地区筛选
	 * @param goodsQuery
	 * @param query
	 * @return
	 */
	private SolrQuery addCityFilter(GoodsQuery goodsQuery,SolrQuery query,InfoWrap info){
		
		if(StringUtils.isNotEmpty(goodsQuery.getProvince())){
			query.addFilterQuery("province:"+info.province); 
		}
		if(StringUtils.isNotEmpty(goodsQuery.getCity())){
			query.addFilterQuery("city:"+info.city); 
		}
		if(StringUtils.isNotEmpty(goodsQuery.getSowntown())){
			query.addFilterQuery("sowntown:"+info.sowntown); 
		}
		
		return query;
	}
	
	/**
	 * 添加分类筛选
	 * @param goodsQuery
	 * @param query
	 * @return
	 */
	private SolrQuery addCategoryFilter(GoodsQuery goodsQuery,SolrQuery query){
		
		if(StringUtils.isNotEmpty(goodsQuery.getCategoryP())){
			query.addFilterQuery("categoryP:"+goodsQuery.getCategoryP()); 
		}
		if(StringUtils.isNotEmpty(goodsQuery.getCategoryC())){
			query.addFilterQuery("categoryC:"+goodsQuery.getCategoryC()); 
		}
		
		return query;
	}
	
	/**
	 * 字符串处理
	 * @param src
	 * @return
	 */
	private String interceptStr(String src){
		if(src != null && (src.startsWith("[") && src.endsWith("]"))){
			src = src.substring(1, src.length()-1);
		}
		return src;
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
		
		InfoWrap(GoodsQuery goodsQuery){
			 country = StringUtils.fillZero(goodsQuery.getCountry(),2,true);
			 province = country + StringUtils.fillZero(goodsQuery.getProvince(),2,true);
			 city = province + StringUtils.fillZero(goodsQuery.getCity(),2,true);
			 sowntown =  city + StringUtils.fillZero(goodsQuery.getSowntown(),2,true);
		}
		
	}
	
	
	/**
	 * TEST
	 * 
	 * @param args
	 */
	public static void main(String args[]){
		String src = "[sssss]";
		if(src != null && (src.startsWith("["))){
			src = src.substring(1, src.length()-1);
		}
		System.out.print(src);
	}

}
