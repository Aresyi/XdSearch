/**
 * 
 */
package com.starrymedia.xd.search.service;

import com.starrymedia.community.core.entity.BrandLocation;
import com.starrymedia.xd.search.query.bo.ShopQuery;
import com.starrymedia.xd.search.util.Pagination;

/**
 * @author : Ares
 * @createTime : 2012-11-19 上午11:32:05
 * @version : 1.0
 * @description :
 */
public interface ShopXdSearchService {
	
	/**
	 * 门店搜索
	 * @param goodsQuery
	 * @return
	 */
	public Pagination<BrandLocation> searchShop(ShopQuery shopQuery);

}
