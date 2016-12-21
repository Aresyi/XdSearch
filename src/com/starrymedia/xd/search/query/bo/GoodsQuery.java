package com.starrymedia.xd.search.query.bo;

import java.io.Serializable;


/**
 * @author : Ares
 * @createTime : 2012-10-11 下午03:09:12
 * @version : 1.0
 * @description :
 */
public class GoodsQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**查询者id*/
	private long userId;
	
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
	
	/** 商品主分类 */
	private String categoryP; 
	
	/** 商品子分类 */
	private String categoryC; 
	
	/** 商户用户id */
	private long brandUserId;
	
	/** 活动类型 */
	private int caseType;
	
	/** 活动状态 */
	private int status;
	
	/** 发布渠道 */
	private int channelType;
	
	/** 是否是打包销售（1:是打包销售;0:可自由购买本活动中产品） */
	private int isPackSale;
	
	/** 活动定时发布的星期（0表示全周发布） */
	private int timerWeek = -1 ;
	
	/**查询类型*/
	private int queryType;
	
	/**当天零点时间戳*/
	private long today;
	
	/**指定特殊查询*/
	private int specifyQuery;
	
	/**查询当前时间*/
	private long now ;
	
	/**页面查看方式*/
	private String lookType;
	
	
	/**当前页*/
	private int pageNum ;
	
	/**页大小*/
	private int pageSize;
	
	/**排序条件*/
	private String orderBy;
	
	/**降序或升序*/
	private String descOrAsc;
	
	
	
	
	@Override
	public String toString(){
		return (
			new StringBuffer()
			.append("{")
			.append("userId:"+userId)
			.append(",lookType:"+lookType)
			.append(",keyword:"+keyword)
			.append(",country:"+country)
			.append(",province:"+province)
			.append(",city:"+city)
			.append(",sowntown:"+sowntown)
			.append(",categoryP:"+categoryP)
			.append(",categoryC:"+categoryC)
			.append(",brandUserId:"+brandUserId)
			.append(",caseType:"+caseType)
			.append(",status:"+status)
			.append(",channelType:"+channelType)
			.append(",isPackSale:"+isPackSale)
			.append(",timerWeek:"+timerWeek)
			.append(",queryType:"+queryType)
			.append(",today:"+today)
			.append(",specifyQuery:"+specifyQuery)
			.append(",now:"+now)
			.append(",orderBy:"+orderBy)
			.append(",descOrAsc:"+descOrAsc)
			.append(",pageNum:"+pageNum)
			.append(",pageSize:"+pageSize)
			.append("}")
			.toString()
		);
	}
	
	
	
	
	
	
	

	public long getUserId() {
		return userId;
	}








	public void setUserId(long userId) {
		this.userId = userId;
	}








	public String getLookType() {
		return lookType;
	}








	public void setLookType(String lookType) {
		this.lookType = lookType;
	}








	public int getQueryType() {
		return queryType;
	}








	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}








	public long getToday() {
		return today;
	}








	public void setToday(long today) {
		this.today = today;
	}








	public int getSpecifyQuery() {
		return specifyQuery;
	}








	public void setSpecifyQuery(int specifyQuery) {
		this.specifyQuery = specifyQuery;
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

	public long getBrandUserId() {
		return brandUserId;
	}

	public void setBrandUserId(long brandUserId) {
		this.brandUserId = brandUserId;
	}

	public int getCaseType() {
		return caseType;
	}

	public void setCaseType(int caseType) {
		this.caseType = caseType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getChannelType() {
		return channelType;
	}

	public void setChannelType(int channelType) {
		this.channelType = channelType;
	}

	public int getIsPackSale() {
		return isPackSale;
	}

	public void setIsPackSale(int isPackSale) {
		this.isPackSale = isPackSale;
	}

	public int getTimerWeek() {
		return timerWeek;
	}

	public void setTimerWeek(int timerWeek) {
		this.timerWeek = timerWeek;
	}

	public long getNow() {
		return now;
	}

	public void setNow(long now) {
		this.now = now;
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
	

	
}
