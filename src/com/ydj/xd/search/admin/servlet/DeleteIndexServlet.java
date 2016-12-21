package com.ydj.xd.search.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.core.SolrCore;

import com.starrymedia.xd.search.core.server.GetSolrServer;
import com.starrymedia.xd.search.core.server.SolrServerDevelop;

/**
 * 
 * @author : Ares
 * @createTime : 2012-9-26 下午02:28:20
 * @version : 1.0
 * @description :
 */
public class DeleteIndexServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DeleteIndexServlet.class);

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if (doDeleteProcess(request, response)) {
			
			String from = request.getParameter("pageName");
			
			if ("form.jsp".equals(from)){
				//request.getRequestDispatcher("HightSearchServlet.so").forward(request, response);
			}
			if ("index.jsp".equals(from)){
				request.getRequestDispatcher("SearchServlet.so").forward(request, response);
			}
			
		} else {
			response.sendRedirect("error.jsp");
		}
	}

	private boolean doDeleteProcess(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			SolrCore core = (SolrCore) request.getAttribute("org.apache.solr.SolrCore");
			SolrServerDevelop localServer = new GetSolrServer().getServer(core.getName());
			
			SolrServer solrServer = localServer.getSolrServer();
			
			String tid = request.getParameter("id");
			solrServer.deleteById(tid);
			
			solrServer.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
		return false;
	}
}
