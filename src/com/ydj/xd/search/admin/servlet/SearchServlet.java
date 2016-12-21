package com.ydj.xd.search.admin.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.core.SolrCore;

import com.starrymedia.xd.search.core.server.GetSolrServer;
import com.starrymedia.xd.search.core.server.SolrServerDevelop;

/**
 * @author : Ares
 * @createTime : 2012-9-25 下午12:54:53
 * @version : 1.0
 * @description :
 */
public class SearchServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	
	static Logger logger = Logger.getLogger(SearchServlet.class);
	
	public final int rows = 10;

	
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doSearch(request, response);
		String pageName = request.getParameter("pageName");
		request.getRequestDispatcher(pageName).forward(request, response);
	}
	

	@SuppressWarnings("unchecked")
	private void doSearch(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String keyword = request.getParameter("q");
		keyword = keyword.replace('@', '&');
		String query = keyword;

		String pageNo = request.getParameter("pageNo");
		int _pageNo = Integer.parseInt(pageNo);
		
		try {
			SolrCore core = (SolrCore) request.getAttribute("org.apache.solr.SolrCore");//Solr服务启动时，放入此值
			
			SolrServerDevelop solrServer = new GetSolrServer().getServer(core.getName());
			
			SolrDocumentList docList = (SolrDocumentList) solrServer.findIndexItems(query, rows, _pageNo, 0,null,null);

			int num = solrServer.getFoundCount(query);
			
			request.setAttribute("num", num);
			request.setAttribute("pageNo", _pageNo);
			
			List<HashMap> result = new ArrayList<HashMap>();
			HashSet<String> keySet = new HashSet<String>();

			String[] head = new String[0];
			if (docList.size() > 0) {
				SolrDocument sd = docList.get(0);
				head = new String[sd.getFieldNames().size()];
				sd.getFieldNames().toArray(head);
				for (int i = 0; i < head.length; i++) {
					if ("NAME".equals(head[i])) {
						String temp = head[0];
						head[0] = head[i];
						head[i] = temp;
					}
				}
			}

			for (int j = 0; j < docList.size(); j++) {
				SolrDocument sd = docList.get(j);
				
				Iterator it = sd.iterator();
				HashMap<String, String> doc = new HashMap<String, String>();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					String key = entry.getKey().toString();
					String value = entry.getValue().toString();

					keySet.add(key);
					doc.put(key, value);
				}
//				Object o = sd.getFirstValue("loadFactor");
				result.add(doc);
			}

			request.setAttribute("rows", rows);
			request.setAttribute("tableHead", head);
			request.setAttribute("q", keyword);
			request.setAttribute("result", result);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("");
			logger.info(e);
		}
	}


}
