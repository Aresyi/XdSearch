/**
 * 
 */
package com.starrymedia.xd.search.util.develop.test;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 * @author : Ares
 * @createTime : 2012-9-28 下午02:19:34
 * @version : 1.0
 * @description :
 * 
 * 参考：http://wiki.apache.org/solr/FieldCollapsing
 */
public class TestGroup2 {
	
	
    public static void main(String[] args) throws MalformedURLException {  
        SolrServer solr = new CommonsHttpSolrServer("http://127.0.0.1:8686/Solr/goods/");  
        
        int pageSize = 20000;
  
        SolrQuery solrQuery = new SolrQuery();  
        solrQuery.setQuery("*:*");
        solrQuery.set("sort", "deadline desc");  
        solrQuery.set("start", (1-1)*pageSize);
        solrQuery.set("rows", pageSize);
        solrQuery.set("fl", "id,brandUserId"); // query.set("fl", "*,score");根据需要设置
        
        
        solrQuery.set("group", true);
        solrQuery.set("group.field", "brandUserId");
        solrQuery.set("group.cache.percent",20);
        solrQuery.set("group.ngroups",true); //Default is false
        solrQuery.set("group.limit",2); //Default is false
        
//        solrQuery.set("group.format", "grouped"); // grouped/simple
//        solrQuery.set("group.main",true); 
//        solrQuery.set("group.truncate",true); 
//        solrQuery.set("group.facet",false); 
        
        solrQuery.addFilterQuery("caseType:{99 TO *}");
        solrQuery.addFilterQuery("status:1");
        
  
        QueryResponse response = null;  
  
        try {  
            response = solr.query(solrQuery);  
            
            try{
            	//只有当 group.main = true 时 
            	System.out.println("Num of documents Found:"+response.getResults().getNumFound()); 
            	List<SolrDocument> solrDocumentlist= response.getResults();
    			
            	int i = 1;
    			for(SolrDocument d : solrDocumentlist){
    				System.out.println((i++)+": "+d);
    			}
            }catch(Exception e){
            	
            }
            
            
            if (response != null) {  
                GroupResponse groupResponse = response.getGroupResponse();  
                
                
                if (groupResponse != null) {  
                    List<GroupCommand> groupCommandList = groupResponse.getValues();  
                    for (GroupCommand groupCommand : groupCommandList) {  
                    	
                        System.out.println("GroupCommand Name : " + groupCommand.getName());  
                        System.out.println("Num of Groups Found: " + groupCommand.getNGroups());  
                        System.out.println("Num of documents Found: " + groupCommand.getMatches()+"\n\n");  
      
                        int i = 1;
                        List<Group> groups = groupCommand.getValues();  
                        for (Group group : groups) {  
                            System.out.println("group value: " + group.getGroupValue());  
                            
                            SolrDocumentList solrDocumentList = group.getResult();  
                            
                            System.out.println("Num of Documents in this group: " + solrDocumentList.getNumFound());  
                            System.out.println("start: " + solrDocumentList.getStart());  
                            System.out.println("Max score: " + solrDocumentList.getMaxScore());  
                            // solrDocumentList.get(index)  
                          
                            
                            for (SolrDocument doc : solrDocumentList) {  
                            	System.out.println(i+": "+doc);
                            	i++;
                            	
//                                System.out.println("the Fields of document:");  
//                                Collection<String> names = doc.getFieldNames();  
//                                for (String name : names) {  
//                                    System.out.println(name + ": " + doc.getFieldValue(name));  
//                                }  
                            	
                                System.out.println();  
                            }  
                            System.out.println("\n\n");  
                        }  
                        System.out.println("\n\n");  
                    }  
                }  
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

