/**
 * 
 */
package com.starrymedia.xd.search.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import com.starrymedia.community.core.comm.Constant;
import com.starrymedia.community.core.dao.BrandDAO;
import com.starrymedia.community.core.dao.GoodsDAO;
import com.starrymedia.community.core.dao.MemberPromotionDAO;
import com.starrymedia.community.core.dao.MemberPromotionLocationDAO;
import com.starrymedia.community.core.dao.MemberPromotionProductDAO;
import com.starrymedia.community.core.entity.Brand;
import com.starrymedia.community.core.entity.Goods;
import com.starrymedia.community.core.entity.MemberPromotion;
import com.starrymedia.community.core.entity.MemberPromotionLocation;
import com.starrymedia.community.core.entity.MemberPromotionProduct;
import com.starrymedia.community.core.service.components.MemberPromotionComponent;
import com.starrymedia.xd.search.common.Constants;
import com.starrymedia.xd.search.core.server.XdSolrServerFactory;
import com.starrymedia.xd.search.doc.ChangeBean2Doc;
import com.starrymedia.xd.search.doc.GoodsDoc;
import com.starrymedia.xd.search.util.StringUtils;
import com.starrymedia.xd.search.util.Util;

/**
 * @author : Ares
 * @createTime : 2012-10-9 下午06:04:47
 * @version : 1.0
 * @description :  
 * 
 * 
 */
public class GoodsIndexJob implements IndexManage {
	
	private static final Logger log = Logger.getLogger(GoodsIndexJob.class);
	
	private MemberPromotionDAO memberPromotionDAO;
    private MemberPromotionProductDAO memberPromotionProductDAO;
    private MemberPromotionLocationDAO memberPromotionLocationDAO;
    private BrandDAO brandDAO;
    private GoodsDAO goodsDAO;
    private MemberPromotionComponent memberPromotionComponent;
    
    

    public MemberPromotionComponent getMemberPromotionComponent() {
		return memberPromotionComponent;
	}

	public void setMemberPromotionComponent(
			MemberPromotionComponent memberPromotionComponent) {
		this.memberPromotionComponent = memberPromotionComponent;
	}
	

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

	public GoodsDAO getGoodsDAO() {
		return goodsDAO;
	}

	public void setGoodsDAO(GoodsDAO goodsDAO) {
		this.goodsDAO = goodsDAO;
	}
	
	
	private SolrServer getLocalSolrServer(){
		SolrServer server = XdSolrServerFactory.getSolrServer(Constants.GOODS_SOLR_SERVER_NAME);
		return server;
	}

	/* (non-Javadoc)
	 * @see com.starrymedia.xd.search.job.IndexManage#createIndex()
	 */
	@Override
	public void createIndex() {
		log.info("<<<<  GoodsIndexJob createIndex begin ... ");
		long begin = System.currentTimeMillis();
		
		int totalAdd = 0 ;
		
		try {
			SolrServer server = this.getLocalSolrServer();
			
			SolrInputDocument doc = null;
			
			Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
			
				Integer need2Create[] = { Constant.TEXT_INDEX_STATUS_UNDO,Constant.TEXT_INDEX_STATUS_UPDATE}; 
				
				List<MemberPromotion> memberPromotionList = this.memberPromotionDAO.findMemberPromotionByStatusAndIndexStatus(1,need2Create);
				
//				List<MemberPromotion> memberPromotionList2 = this.memberPromotionDAO.findMemberPromotionByStatusAndIndexStatus(8,need2Create);//8暂停
//				
//				if(memberPromotionList2 != null){
//					memberPromotionList = memberPromotionList == null ? new ArrayList<MemberPromotion>() : memberPromotionList ;
//					memberPromotionList.addAll(memberPromotionList2);
//				}
				
				if(memberPromotionList == null || memberPromotionList.isEmpty()){
					log.info("\t\t no new goods index need to create\n");
					return ;
				}
				
				totalAdd = memberPromotionList.size();
				
				ChangeBean2Doc<GoodsDoc> change = new GoodsDoc();
				
				StringBuffer logIds  =  new StringBuffer("[");
				
				for(MemberPromotion m : memberPromotionList){
					
					GoodsDoc g = this.wrap(m);
					
					doc = change.change2Doc(g);
					
					//TODO :
					m.setIndexStatus(Constant.TEXT_INDEX_STATUS_DO);
					this.memberPromotionDAO.updateMemberPromotion(m);
					
					docs.add(doc);
					logIds.append(m.getId()+",");
				}
				server.add(docs);
				
				
				server.commit();
//				server.optimize();
				
				log.info("Now add ids :"+logIds.replace(logIds.length()-1, logIds.length(), "]"));
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		log.info("<<<<  GoodsIndexJob createIndex end.Total add index:"+totalAdd+" used time:["+(end-begin)+"]ms \n");
	}

	/* (non-Javadoc)
	 * @see com.starrymedia.xd.search.job.IndexManage#deleteIndex()
	 */
	@Override
	public void deleteIndex() {

		log.info("<<<<  GoodsIndexJob deleteIndex begin ...");
		long begin = System.currentTimeMillis();
		
		List<MemberPromotion> memberPromotionList = this.memberPromotionDAO.findClosedMemberPromotion(begin);
		if(memberPromotionList == null || memberPromotionList.isEmpty()){
			log.info("\t\t no new goods index need to delete\n");
			return ;
		}
		
		int totalDelete = memberPromotionList.size();
		
		List<String> ids = new ArrayList<String>();
		for(MemberPromotion m : memberPromotionList){
			
			ids.add(m.getId()+"");
			
			m.setIndexStatus(Constant.TEXT_INDEX_STATUS_DELETED);
			this.memberPromotionDAO.updateMemberPromotion(m);
			
			this.noteBrandLocationNeed2UpdateIndex(m);
		}
		
		SolrServer server = this.getLocalSolrServer();
		
		try {
			server.deleteById(ids);
			server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		log.info("Now delete ids :"+ids.toString());
		log.info("<<<<  GoodsIndexJob deleteIndex end. Total delete index:"+totalDelete+" used time:["+(end-begin)+"]ms \n");
	
	}
	
	/**
	 * 暂时借用这里——通知brandLocation也需要更新索引
	 */
	private void noteBrandLocationNeed2UpdateIndex(MemberPromotion mp){
		try{
			this.memberPromotionComponent.updateBrandLocationIndexStatus(mp);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.starrymedia.xd.search.job.IndexManage#updateIndex()
	 */
	@Override
	public void updateIndex() {
		
	}
	
	
	
	private GoodsDoc wrap(MemberPromotion m){
		
		Brand brand = this.brandDAO.findBrandByID(m.getBrandId());
		List<MemberPromotionProduct> memPromoProductList = this.memberPromotionProductDAO.findMemberPromotionProductByPromotionID(m.getId());
		List<MemberPromotionLocation> memPromoLocationList = this.memberPromotionLocationDAO.findMemberPromotionLocationByPromotionID(m.getId()); 
		
		StringBuffer address = new StringBuffer();
		StringBuffer goodsName = new StringBuffer();
		StringBuffer country = new StringBuffer();  
		StringBuffer province = new StringBuffer(); 
		StringBuffer city = new StringBuffer(); 
		StringBuffer sowntown = new StringBuffer(); 
		StringBuffer categoryP = new StringBuffer(); 
		StringBuffer categoryC = new StringBuffer(); 
		
		for(MemberPromotionProduct mp : memPromoProductList){
			goodsName.append(mp.getGoodsName()).append(" ");
			
			Goods goods = this.goodsDAO.findAnyGoodsByID(mp.getGoodsID());
			String catId = goods.getCatID();
			String c[] = catId.split("\\,");
			
			for(String s : c){
				if(s.length() != 4){
					continue;
				}
				categoryP.append(s.substring(0, 2)).append(" ");
				categoryC.append(s).append(" ");
			}
		}
		
		for(MemberPromotionLocation mpl : memPromoLocationList){
			
			String _country = StringUtils.fillZero(mpl.getCountry()+"",2,true);
			String _province = _country + StringUtils.fillZero(mpl.getProvince()+"",2,true);
			String _city = _province + StringUtils.fillZero(mpl.getCity()+"",2,true);
			String _sowntown =  _city + StringUtils.fillZero(mpl.getSowntownID()+"",2,true);
			
			country.append(_country).append(" ");
			province.append(_province).append(" ");
			city.append(_city).append(" ");
			sowntown.append(_sowntown).append(" ");
			address.append(mpl.getAddress()).append(" ");
		}
		
		GoodsDoc g = new GoodsDoc();
		
		g.setId(m.getId());
		g.setBrandName(brand.getName());
		g.setMpName(m.getName());
		g.setAddress(address.toString());
		g.setGoodsName(goodsName.toString());
		g.setCountry(country.toString());
		g.setProvince(province.toString());
		g.setCity(city.toString());
		g.setSowntown(sowntown.toString());
		g.setCategoryP(categoryP.toString());
		g.setCategoryC(categoryC.toString());
		g.setBrandUserId(m.getBrandUserID()+"");
		g.setStatus(m.getStatus());
		g.setCaseType(m.getCaseType());
		g.setChannelType(Util.getGeneStr(Util.CHANNEL_TYPE, m.getChannelType()));
		g.setBeginTime(m.getBeginTime());
		g.setEndTime(m.getEndTime());
		g.setStartTime(m.getStartTime());
		g.setDeadline(m.getDeadline());
		g.setTimerWeek(Util.getGeneStr(Util.WEEK, m.getTimerWeek()));
		g.setTimerBeginTime(m.getTimerBeginTime());
		g.setTimerEndTime(m.getTimerEndTime());
		g.setIsPackSale(m.getIsPackSale());
		
		return g;
	}
	

	/**
	 * TEST
	 * 
	 * @param args
	 */
	public static void main(String args[]){
		
	}
}
