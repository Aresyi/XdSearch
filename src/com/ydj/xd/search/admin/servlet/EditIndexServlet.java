package com.ydj.xd.search.admin.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.SolrCore;

import com.starrymedia.xd.search.core.server.GetSolrServer;
import com.starrymedia.xd.search.core.server.SolrServerDevelop;
import com.starrymedia.xd.search.util.TypeChange;

/**
 * 
 * @author : Ares
 * @createTime : 2012-9-26 下午02:52:31
 * @version : 1.0
 * @description :
 */
public class EditIndexServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(EditIndexServlet.class);


	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if (doEditProcess(request, response)) {
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
	
	@SuppressWarnings("unchecked")
	private boolean doEditProcess(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			SolrCore core = (SolrCore) request.getAttribute("org.apache.solr.SolrCore");
			SolrServerDevelop localServer = new GetSolrServer().getServer(core.getName());

			String id = request.getParameter("id");
			String query = "id:" + id;
			SolrDocument solrDocument = localServer.findIndexItem(query);

			SolrInputDocument solrInputDocument = new SolrInputDocument();
			Iterator it = solrDocument.iterator();

			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				Object key = entry.getKey();
				String filedValue = request.getParameter(key.toString());
				Object o = null;
				try {
					o = TypeChange.isChange(entry.getValue().getClass().toString(), filedValue);
					if (o.equals(false)) {
						return false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.info(e);
				}

				solrInputDocument.setField(key.toString(), o);
			}
			String grade = request.getParameter("grade");
			if(grade == null || "".equals(grade.trim())){
				grade = "0";
			}
			solrInputDocument.setDocumentBoost(Float.parseFloat(grade));
			SolrServer server = localServer.getSolrServer();
			
			server.add(solrInputDocument);
			
			server.commit();
			return true;
		} catch (Exception e) {
			logger.info(e);
		}
		return false;
	}

	

}
