/**
 * 
 */
package com.starrymedia.xd.search.query.bo;

import java.io.Serializable;

/**
 * @author : Ares
 * @createTime : 2012-11-19 上午11:26:18
 * @version : 1.0
 * @description :
 */
public class ShopQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**查询者id*/
	private int userId;
	
	/** 查询关键字 */
	private String keyword;
	
	/** 国家 */
	private String country;  
	
	/** 省 */
	private String province; 
	
	/** 市/区 */
	private String city; 
	
	/** 商圈id */
	private String sowntown; 
	
	/**经度*/
	private double longitude;
	 
	/**纬度*/
	private double latitude;
	
	/**偏移距离(单位:km)*/
	private int offsetDis;
	
	/** 商品主分类 */
	private String categoryP; 
	
	/** 商品子分类 */
	private String categoryC;
	
	/**当前页*/
	private int pageNum ;
	
	/**页大小*/
	private int pageSize;
	
	/**排序条件(本周人气:useCount;偏移距离：offsetDis)*/
	private String orderBy;
	
	/**降序或升序*/
	private String descOrAsc;
	
	@Override
	public String toString(){
		return (
			new StringBuffer()
			.append("{")
			.append("userId:"+userId)
			.append(",keyword:"+keyword)
			.append(",country:"+country)
			.append(",province:"+province)
			.append(",city:"+city)
			.append(",sowntown:"+sowntown)
			.append(",longitude:"+longitude)
			.append(",latitude:"+latitude)
			.append(",categoryP:"+categoryP)
			.append(",categoryC:"+categoryC)
			.append(",orderBy:"+orderBy)
			.append(",descOrAsc:"+descOrAsc)
			.append(",pageNum:"+pageNum)
			.append(",pageSize:"+pageSize)
			.append("}")
			.toString()
		);
	}
	
	
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSowntown() {
		return sowntown;
	}

	public void setSowntown(String sowntown) {
		this.sowntown = sowntown;
	}

	public String getCategoryP() {
		return categoryP;
	}

	public void setCategoryP(String categoryP) {
		this.categoryP = categoryP;
	}

	public String getCategoryC() {
		return categoryC;
	}

	public void setCategoryC(String categoryC) {
		this.categoryC = categoryC;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getDescOrAsc() {
		return descOrAsc;
	}

	public void setDescOrAsc(String descOrAsc) {
		this.descOrAsc = descOrAsc;
	}


	public int getOffsetDis() {
		return offsetDis;
	}


	public void setOffsetDis(int offsetDis) {
		this.offsetDis = offsetDis;
	}
	
	
	

}
