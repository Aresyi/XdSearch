package com.ydj.xd.search.admin.servlet;

import java.io.IOException;
import java.io.PrintWriter;

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
 * @createTime : 2012-9-26 下午02:38:51
 * @version : 1.0
 * @description :
 */
public class BatchDeleteIndexServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(BatchDeleteIndexServlet.class);

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String ids = request.getParameter("ids");
		String[] tids = ids.split(",");
		int len = tids.length;
		
		try {
			SolrCore core = (SolrCore) request.getAttribute("org.apache.solr.SolrCore");
			SolrServerDevelop localServer = new GetSolrServer().getServer(core.getName());
			SolrServer solrServer = localServer.getSolrServer();
			for (int i = 0; i < len; i++) {
				solrServer.deleteById(tids[i]);
			}
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println(ids);
		out.close();
	}

}
