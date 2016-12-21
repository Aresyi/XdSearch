package com.ydj.xd.search.admin.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.core.SolrCore;

import com.starrymedia.xd.search.core.server.GetSolrServer;
import com.starrymedia.xd.search.core.server.SolrServerDevelop;

/**
 * @author : Ares
 * @createTime : 2012-9-25 下午12:54:53
 * @version : 1.0
 * @description :
 */
public class TestSuggestServlet extends BaseServlet {
	

	private static final long serialVersionUID = 1L;
	
	static Logger logger = Logger.getLogger(TestSuggestServlet.class);
	
	public final int rows = 10;
	

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doSearch(request, response);
		
		return ;
		
	}
	
	private void doSearch(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String q = request.getParameter("q");
		
		StringBuffer r=new StringBuffer("<ul style='margin-left:0px;margin-top:-3px;border:1px solid #000;list-style:none;width:252px;padding:0px;'>");
		
		if(q != null){
			SolrCore core = (SolrCore) request.getAttribute("org.apache.solr.SolrCore");//Solr服务启动时，放入此值
			
			SolrServerDevelop solrServer = null;
			 SolrServer solr = null;
			try {
				solrServer = new GetSolrServer().getServer(core.getName());
				solr = solrServer.getSolrServer();
			} catch (Exception e1) {
				e1.printStackTrace();
				solr = new CommonsHttpSolrServer("http://127.0.0.1:8686/Solr/starry-community/");  
			}
			  
		        SolrQuery params = new SolrQuery();  
		        params.set("qt", "/suggest");  
		        params.set("q", q);  
		        params.set("sort", "score desc");
			
		        QueryResponse resPe = null;  
		        
		        try {  
		        	resPe = solr.query(params);  
		            System.out.println("查询耗时：" + resPe.getQTime());  
		        } catch (SolrServerException e) {  
		            e.printStackTrace();  
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }
		        
		        Map<Integer,String> map  = new HashMap<Integer,String>();
		        int i = 0;
		        
		        SpellCheckResponse spellCheckResponse = resPe.getSpellCheckResponse();  
		        if (spellCheckResponse != null) {  
		            List<Suggestion> suggestionList = spellCheckResponse.getSuggestions();  
		            for (Suggestion suggestion : suggestionList) {  
		                System.out.println("NumFound: " + suggestion.getNumFound());  
		                System.out.println("Token: " + suggestion.getToken());  
		                System.out.println("Suggested: ");  
		                
		                List<String> suggestedWordList = suggestion.getAlternatives();  
		                for (String word : suggestedWordList) {  
		                    System.out.println(word + ", ");  
		                    map.put(i, word);
		                    i++;
		                }  
		                System.out.println();  
		            }  
		       }

			
				for(int j=0;j<map.size();j++){
					String v = map.get(j);
					r.append("<li id='li_"+j+"' style='line-height:16px;font-size:12px;padding:2px;' value='"+v+"'  context='"+v+"' onclick='set(this.value);' onmouseover='mover(this.value)'>"+v+"<span></span></li>");
				}
				
		}
		
		r.append("</ul>");
		
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(r);
		return ;
		
	}



}
