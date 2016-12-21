/**
 * 
 */
package com.starrymedia.xd.search.util.develop.test;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

/**
 * @author : Ares
 * @createTime : 2012-9-28 下午02:19:34
 * @version : 1.0
 * @description :
 * 
 * http://wiki.apache.org/solr/SpatialSearch
 */
public class TestSpatial {
	
	
	private static String change(String src,int count){
		if("-1".equals(src)){
			src = "00";
		}
		String s = "00000000";
		int l = src.length();
		if(l < count){
			src = src+s.substring(0,count-l);
		}
		
		return src;
	}
	
	private  static String resetKeyword2(String keywords) throws Exception {
		StringBuffer q=new StringBuffer();
		if (keywords != null && keywords.length() > 0) {
			Set words = new HashSet();

			 IKSegmentation ik2 = new IKSegmentation(new StringReader(keywords),true);
			 Lexeme lexeme = null;
			    while((lexeme = ik2.next())!=null){
			    	String s = lexeme.getLexemeText();
			    	System.out.println(s);
			    	if(!words.contains(s)){
			    		words.add(s);
			    		q.append(s+" ");
			    	}
			    } 
		}
		System.out.println("q:"+q);
		return q.toString().trim();
	}
	
    public static void main(String[] args) throws Exception {  
    	
    	System.out.println("q:"+change("-1",2));
    	
    	
        SolrServer solr = new CommonsHttpSolrServer("http://127.0.0.1:8686/Solr/shop/");  
  
        SolrQuery solrQuery = new SolrQuery();  
        String token = "*:*";  
        solrQuery.set("q", token);  
        solrQuery.set("sort", "geodist() asc");  
        solrQuery.set("start", (1-1)*15);
        solrQuery.set("rows", 15);
        solrQuery.set("fl", "*,score");
        solrQuery.set("q.op", "OR");
        
        String localtion = "31.200742,121.6068219";
        int dis = 5;
        
//        solrQuery.addFilterQuery("{!geofilt pt="+localtion+" sfield=location d="+dis+"}");
//        solrQuery.addFilterQuery("{!bbox}&sfield=location&pt="+localtion+"&d="+dis);
        solrQuery.addFilterQuery("{!bbox}&sfield=location&pt=31.200742,121.6068219&d=50");
        
        QueryResponse response = null;  
  
        try {  
            response = solr.query(solrQuery);  
            long count = response.getResults().getNumFound();
            
            System.out.println("总记录数："+count+" 查询耗时：" + response.getQTime());
            
            List<SolrDocument> solrDocumentlist= response.getResults();
            
			for(SolrDocument d : solrDocumentlist){
				
				System.out.println(d);
			}
            
        } catch (SolrServerException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            ;  
        }  
        
  
    }  
}  

