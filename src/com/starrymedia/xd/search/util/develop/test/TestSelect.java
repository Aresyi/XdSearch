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
 */
public class TestSelect {
	
	
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
//        token = resetKeyword2(token);
        solrQuery.set("q", token);  
        solrQuery.set("sort", "score desc");  
        solrQuery.set("start", (1-1)*15);
        solrQuery.set("rows", 15);
        solrQuery.set("fl", "*,score");
        solrQuery.set("q.op", "OR");
//        solrQuery.setQuery("*:*");
//        solrQuery.addFilterQuery("timerWeek:11");
        
        long now = System.currentTimeMillis();
        long today = 1350835200000L;
        long inr =  now - today ;
        System.out.println(now-today);
//        solrQuery.addFilterQuery(
//        		"-timerWeek:2 OR " +
//				" (timerBeginTime:{"+inr+" TO *} OR timerEndTime:{* TO "+inr+"})" +
//				" OR "+
//				" startTime:{"+now+" TO *}"
//				"");
        
//       solrQuery.addFilterQuery("timerBeginTime:{42862358 TO *}");
        
//        solrQuery.setHighlight(true)
//                 .setHighlightSimplePre("<font colr='red'>")
//                 .setHighlightSimplePost("</font>")
//                 .setStart(0) 
//                 .setRows(10);
//        solrQuery.setParam("hl.fl", "mpName,goodsName,brandName,address");
//        
  
        QueryResponse response = null;  
  
        try {  
            response = solr.query(solrQuery);  
            long count = response.getResults().getNumFound();
            
            System.out.println("总记录数："+count+" 查询耗时：" + response.getQTime());
            
            List<SolrDocument> solrDocumentlist= response.getResults();
            
            Map<String,Map<String,List<String>>> r = response.getHighlighting();
            
			for(SolrDocument d : solrDocumentlist){
				
				System.out.println(d);
				
//				String id= d.getFieldValue("id")+"";
//				
//				if(r != null){
//					Map<String,List<String>> map = r.get(id);
//					
//					if(map != null){
//					   System.out.println(map.get("mpName"));
//					   System.out.println(map.get("goodsName"));
//					   System.out.println(map.get("brandName"));
//					   System.out.println(map.get("address"));
//					}
//				}
			
				
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

