package com.starrymedia.xd.search.service;

import com.starrymedia.community.core.entity.Brand;
import com.starrymedia.xd.search.util.Pagination;

/**
 * @author : Ares
 * @createTime : Sep 21, 2012 5:33:17 PM
 * @version : 1.0
 * @description :
 */
public interface BrandXdSearchService {
	
	
	/**
	 * 依据名称搜索商家
	 * 
	 * @param name
	 * @param status
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
    public Pagination<Brand> findBrandByName(String name, Integer status, int pageNum,int pageSize);
    

}
