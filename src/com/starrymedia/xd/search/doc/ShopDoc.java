/**
 * 
 */
package com.starrymedia.xd.search.doc;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.common.SolrInputDocument;

/**
 * @author : Ares
 * @createTime : 2012-11-16 下午01:28:56
 * @version : 1.0
 * @description :
 */
public class ShopDoc extends BaseChangeBean2Doc<ShopDoc> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** *************************************************************************************************** */
	
	/**门店Id*/
	@Field
	private long id;
	
	/**商户Id*/
	@Field
	private long  brandId;
	 
	/**商户绑定用户Id*/
	@Field
	private long brandUserId;
	
	/**商户名称*/
	@Field
	private String brandName;
	 
	/**门店名称*/
	@Field
	private String shopName;
	 
	/**门店地址*/
	@Field
	private String address;
	 
	/**国家*/
	@Field
	private String country;
	
	/**省*/
	@Field
	private String province;
	
	/**市*/
	@Field
	private String city;
	
	/**地区（商圈）*/
	@Field
	private String sowntown;
	
	/**经度*/
	@Field
	private double longitude;
	 
	/**纬度*/
	@Field
	private double latitude;
	
	/**经纬度*/
	@Field
	private String location;
	
	/**活动名称*/
	@Field
	private String  mpName;
	
	/**产品名称*/
	@Field
	private String goodsName;
	
	/**产品主分类*/
	@Field
	private String categoryP;
	
	/**产品子分类*/
	@Field
	private String categoryC;
	
	/**本周上门验证次数*/
	@Field
	private int useCount;
	
	
	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	/** *************************************************************************************************** */
	
	private transient Set<String> mpNameSet = new HashSet<String>();
	private transient Set<String> goodsNameSet = new HashSet<String>();
	private transient Set<String> categoryPSet = new HashSet<String>();
	private transient Set<String> categoryCSet = new HashSet<String>();
	
	
	@Override
	public SolrInputDocument change2Doc(ShopDoc obj) {
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


	public long getBrandId() {
		return brandId;
	}


	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}


	public long getBrandUserId() {
		return brandUserId;
	}


	public void setBrandUserId(long brandUserId) {
		this.brandUserId = brandUserId;
	}


	public String getBrandName() {
		return brandName;
	}


	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}


	public String getShopName() {
		return shopName;
	}


	public void setShopName(String shopName) {
		this.shopName = shopName;
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


	public void setSowntownId(String sowntown) {
		this.sowntown = sowntown;
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


	public String getMpName() {
		return this.mpName;
	}


	public void setMpName(String mpName) {
		this.mpNameSet.add(mpName);
		
		StringBuffer sbf = new StringBuffer();
		for(String s: mpNameSet){
			sbf.append(s).append(" ");
		}
		
		this.mpName = sbf.toString();
		
	}


	public String getGoodsName() {
		return this.goodsName;
	}


	public void setGoodsName(String goodsName) {
		this.goodsNameSet.add(goodsName);
		
		StringBuffer sbf = new StringBuffer();
		for(String s: goodsNameSet){
			sbf.append(s).append(" ");
		}
		this.goodsName = sbf.toString();
	}


	public int getUseCount() {
		return useCount;
	}


	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}


	public String getCategoryP() {
		return this.categoryP;
	}


	public void setCategoryP(String categoryP) {
		this.categoryPSet.add(categoryP);
		
		StringBuffer sbf = new StringBuffer();
		for(String s: categoryPSet){
			sbf.append(s).append(" ");
		}
		
		this.categoryP = sbf.toString();
		
	}


	public String getCategoryC() {
		return this.categoryC;
	}


	public void setCategoryC(String categoryC) {
		this.categoryCSet.add(categoryC);
		
		StringBuffer sbf = new StringBuffer();
		for(String s: categoryCSet){
			sbf.append(s).append(" ");
		}
			
		this.categoryC = sbf.toString();
	}

	

}
