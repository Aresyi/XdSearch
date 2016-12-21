package com.ydj.xd.search.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.core.SolrCore;

import com.starrymedia.xd.search.core.server.GetSolrServer;
import com.starrymedia.xd.search.core.server.SolrServerDevelop;

/**
 * 
 * @author : Ares
 * @createTime : 2012-9-26 下午02:49:33
 * @version : 1.0
 * @description :
 */
public class InitEditIndexServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(InitEditIndexServlet.class);
	

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			SolrCore core = (SolrCore) request.getAttribute("org.apache.solr.SolrCore");
			SolrServerDevelop solrServer = new GetSolrServer().getServer(core.getName());

			String id = request.getParameter("id");
			String query = "id:" + id;
			SolrDocument solrDocument = solrServer.findIndexItem(query);

			request.setAttribute("rows", request.getParameter("rows"));
			request.setAttribute("start", request.getParameter("start"));
			request.setAttribute("pageNo", request.getParameter("pageNo"));
			request.setAttribute("solrDocument", solrDocument);
			
			request.getRequestDispatcher("edit_index.jsp").forward(request, response);
		} catch (Exception e) {
			logger.info(e);
		}
		
	}

}
