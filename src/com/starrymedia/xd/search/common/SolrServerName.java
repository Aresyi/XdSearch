/**
 * 
 */
package com.starrymedia.xd.search.common;

/**
 * @author : Ares
 * @createTime : 2012-11-23 下午03:53:44
 * @version : 1.0
 * @description :
 */
public enum SolrServerName {
	
	/**商家*/
	BRAND("brand"),
	
	/**商品*/
	GOODS("goods"),
	
	/**门店*/
	SHOP("shop");
	
	
	SolrServerName(String name){
		this.name = name;
	}
	
	private String name;
	
	public String getName(){
		return this.name;
	}

}
