/**
 * 
 */
package com.starrymedia.xd.search.util.develop.test;

import java.net.MalformedURLException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;

/**
 * @author : Ares
 * @createTime : 2012-9-28 下午02:19:34
 * @version : 1.0
 * @description :
 * 
 * 参考：http://wiki.apache.org/solr/SimpleFacetParameters
 */
public class TestFacet {
	
    public static void main(String[] args) throws MalformedURLException, SolrServerException {  
    	
    	try{
    		SolrServer server = new CommonsHttpSolrServer("http://127.0.0.1:8686/Solr/goods/");  
    		  
            SolrQuery query = new SolrQuery();//建立一个新的查询
            query.setQuery("*:*");
            query.setFacet(true);//设置facet=on
           query.addFacetField(new String[] { "categoryP","categoryC"});//设置需要facet的字段
//            query.addFacetField("brandUserId");//设置需要facet的字段
            query.setFacetLimit(10000);//限制facet返回的数量
            query.setFacetMinCount(1);
            
            query.addFilterQuery("caseType:{99 TO *}");
            query.addFilterQuery("status:1");
            
            QueryResponse response = server.query(query);
            
            System.out.println("总记录数："+response.getResults().getNumFound());
            
            List<FacetField> facets = response.getFacetFields();//返回的facet列表
            
            for (FacetField facet : facets) {
            	
            	List<Count> counts = facet.getValues();
            	
            	System.out.println(facet.getName()+":"+counts.size());
            	
            	System.out.println("----------------");
            	
            	
            	for (Count count : counts) {
            		System.out.println(count.getName() + ":" + count.getCount());
            	}
            	
            	System.out.println();
            }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        

  
    }  
}  

