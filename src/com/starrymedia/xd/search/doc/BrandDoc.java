/**
 * 
 */
package com.starrymedia.xd.search.doc;

import org.apache.solr.common.SolrInputDocument;

import com.starrymedia.community.core.entity.Brand;

/**
 * @author : Ares
 * @createTime : 2012-9-27 上午10:34:52
 * @version : 1.0
 * @description :
 */
public class BrandDoc extends BaseChangeBean2Doc<Brand> {

	@Override
	public SolrInputDocument change2Doc(Brand obj) {
		if(obj == null){
			return null;
		}
		SolrInputDocument doc = new SolrInputDocument();
		
		doc.addField("id", obj.getId());
		doc.addField("brandName", obj.getName());
		doc.addField("status", obj.getStatus());
		doc.addField("logo", obj.getLogo()== null ? "":obj.getLogo());
		doc.addField("imageFile", obj.getImageFile() == null ? "": obj.getImageFile());
		
		return doc;
	}
	
	
	/**
	 * TEST
	 * @param args
	 */
	public static void main(String args[]){
		ChangeBean2Doc<Brand> change = new BrandDoc();
		Brand brand = new Brand();
		brand.setName("brandName");
		change.autoChange2Doc(brand);
	}

}
