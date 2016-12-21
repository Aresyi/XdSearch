package com.starrymedia.xd.search.job;

/**
 * @author : Ares
 * @createTime : Sep 21, 2012 5:33:17 PM
 * @version : 1.0
 * @description :
 * 
 * 1、当数据量大时，注意内存溢出，考虑分页建立索引；
 * 2、多线程建索引
 */
public interface IndexManage {
	
	/**
	 * 添加索引
	 */
	public void createIndex();

	/**
	 * 修改索引
	 * 
	 * 注意：1：可以直接修改；2：实行——先删除，再重建相应索引
	 */
	public void updateIndex();
	
	/**
	 * 删除索引
	 */
	public void deleteIndex();
}
