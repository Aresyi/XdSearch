package com.starrymedia.xd.search.doc;

import java.io.Serializable;

import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.common.SolrInputDocument;

/**
 * @author : Ares
 * @createTime : 2012-10-9 下午06:07:11
 * @version : 1.0
 * @description :
 */
public class GoodsDoc extends BaseChangeBean2Doc<GoodsDoc> implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 唯一id */
	@Field
	private long id;
	
	/** 活动名称 */
	@Field
	private String mpName;
	
	/** 产品名称 */
	@Field
	private String goodsName;
	
	/** 商户名称 */
	@Field
	private String brandName;
	
	/** 地址 */
	@Field
	private String address;
	
	/** 国家 */
	@Field
	private String country;  
	
	/** 省 */
	@Field
	private String province; 
	
	/** 市/区 */
	@Field
	private String city; 
	
	/** 商圈id */
	@Field
	private String sowntown; 
	
	/** 商品主分类 */
	@Field
	private String categoryP; 
	
	/** 商品子分类 */
	@Field
	private String categoryC; 
	
	/** 商户用户id */
	@Field
	private String brandUserId;
	
	/** 活动类型 */
	@Field
	private int caseType;
	
	/** 活动状态 */
	@Field
	private int status;
	
	/** 发布渠道 */
	@Field
	private String channelType;
	
	/** 展示开始时间 */
	@Field("showStartTime")
	private long beginTime;
	
	/** 展示结束时间 */
	@Field("showEndTime")
	private long endTime;
	
	/** 有效期开始时间  */
	@Field
	private long startTime;
	
	/** 有效期结束时间 */
	@Field
	private long deadline;
	
	/** 是否是打包销售（1:是打包销售;0:可自由购买本活动中产品） */
	@Field
	private int isPackSale;
	
	/** 活动定时发布的星期（0表示全周发布） */
	@Field
	private String timerWeek ;
	
	/** 活动定时每天发布的具体时间段（开始时间）单位毫秒 */
	@Field
	private long timerBeginTime ;
	
	/** 活动定时每天发布的具体时间段（结束时间）单位毫秒 */
	@Field
	private long timerEndTime ;
	
	
	
	


	@Override
	public SolrInputDocument change2Doc(GoodsDoc obj) {
		
		if(obj == null){
			return null;
		}

		DocumentObjectBinder binder = new DocumentObjectBinder();
		SolrInputDocument doc = binder.toSolrInputDocument(obj);
		
		return doc;
	}
	
	
	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getMpName() {
		return mpName;
	}



	public void setMpName(String mpName) {
		this.mpName = mpName;
	}



	public String getGoodsName() {
		return goodsName;
	}



	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}



	public String getBrandName() {
		return brandName;
	}



	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
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



	public String getBrandUserId() {
		return brandUserId;
	}



	public void setBrandUserId(String brandUserId) {
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





	public String getChannelType() {
		return channelType;
	}



	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}



	public String getTimerWeek() {
		return timerWeek;
	}



	public void setTimerWeek(String timerWeek) {
		this.timerWeek = timerWeek;
	}



	public long getBeginTime() {
		return beginTime;
	}



	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}



	public long getEndTime() {
		return endTime;
	}



	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}



	public long getStartTime() {
		return startTime;
	}



	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}



	public long getDeadline() {
		return deadline;
	}



	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}


	public int getIsPackSale() {
		return isPackSale;
	}



	public void setIsPackSale(int isPackSale) {
		this.isPackSale = isPackSale;
	}



	public long getTimerBeginTime() {
		return timerBeginTime;
	}



	public void setTimerBeginTime(long timerBeginTime) {
		this.timerBeginTime = timerBeginTime;
	}



	public long getTimerEndTime() {
		return timerEndTime;
	}



	public void setTimerEndTime(long timerEndTime) {
		this.timerEndTime = timerEndTime;
	}



	/**
	 * TEST
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
