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
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;

/**
 * @author : Ares
 * @createTime : 2012-9-28 下午02:19:34
 * @version : 1.0
 * @description :
 */
public class TestGroup {
	
	
    public static void main(String[] args) throws MalformedURLException {  
        SolrServer solr = new CommonsHttpSolrServer("http://127.0.0.1:8686/Solr/goods/");  
  
        SolrQuery solrQuery = new SolrQuery();  
        solrQuery.setQuery("*:*");
        solrQuery.set("sort", "score desc");  
        solrQuery.set("start", (1-1)*1500);
        solrQuery.set("rows", 1500);
        solrQuery.set("fl", "*,score");
        
        solrQuery.set("group", true);
        solrQuery.set("group.field", "brandUserId");
        
  
        QueryResponse response = null;  
  
        try {  
            response = solr.query(solrQuery);  
            NamedList respNL = response.getResponse();
            NamedList groupInfo = (NamedList) respNL.get("grouped");
            NamedList thisGroupInfo = (NamedList) groupInfo.get("brandUserId");
            Number totalUngrouped = (Number) thisGroupInfo.get("matches");
            System.out.println("总匹配数："+totalUngrouped+" 查询耗时："+response.getQTime()+"ms");
            
            long totalNumberOfUngroupedDocuments = totalUngrouped.longValue();
            
            List<Object> groupData = (List<Object>) thisGroupInfo.get("groups");
            
            int numberOfGroupsReturnedOnThisPage = groupData.size();
            int i = 1;
            for(Object o : groupData) {
                    NamedList thisGroup = (NamedList) o;
                    SolrDocumentList sdl = (SolrDocumentList) thisGroup.get("doclist");
                    
                    long totalDocsInThisGroup = sdl.getNumFound();
                    
                    int totalDocs4ThisGroupReturned = sdl.size();
                    SolrDocument groupedDoc = sdl.get(0);
                    System.out.println(i+": totalDocsInThisGroup:"+totalDocsInThisGroup+" totalDocs4ThisGroupReturned :"+totalDocs4ThisGroupReturned +" "+groupedDoc);
                    i++;
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

