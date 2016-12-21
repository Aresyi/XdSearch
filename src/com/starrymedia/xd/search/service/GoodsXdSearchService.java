package com.starrymedia.xd.search.service;

import java.util.Map;

import com.starrymedia.community.core.entity.MemberPromotion;
import com.starrymedia.xd.search.query.bo.GoodsQuery;
import com.starrymedia.xd.search.util.Pagination;


/**
 * @author : Ares
 * @createTime : 2012-10-11 下午12:00:46
 * @version : 1.0
 * @description :
 */
public interface GoodsXdSearchService {
	
	
	/**
	 * 活动搜索
	 * @param goodsQuery
	 * @return
	 */
	public Pagination<MemberPromotion> search(GoodsQuery goodsQuery);
	
	/**
	 * 关键字引导
	 * @param goodsQuery
	 * @return
	 */
	public Map<String,Long> ajaxSuggest(GoodsQuery goodsQuery);

}
