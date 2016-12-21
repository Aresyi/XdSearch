package com.ydj.xd.search.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.starrymedia.community.core.entity.Brand;
import com.starrymedia.xd.search.service.BrandXdSearchService;
import com.starrymedia.xd.search.util.Pagination;

/**
 * @author : Ares
 * @createTime : 2012-9-25 下午12:54:53
 * @version : 1.0
 * @description :
 */
public class TestSearchServlet extends BaseServlet {
	
	private static BrandXdSearchService brandSearchService ;

	private static final long serialVersionUID = 1L;
	
	static Logger logger = Logger.getLogger(TestSearchServlet.class);
	
	public final int rows = 10;
	
	
	public BrandXdSearchService getBrandSearchService() {
		if(brandSearchService == null){
			brandSearchService = (BrandXdSearchService) this.getSpringBean("brandSearchService");
		}
		return brandSearchService;
	}


	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doSearch(request, response);
		request.getRequestDispatcher("search_test.jsp").forward(request,response);
		
	}
	
	private void doSearch(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String q = request.getParameter("q");
		String _pageNum = request.getParameter("pageNum");
		
		int pageNum = 1 ;
		try{
			pageNum = Integer.valueOf(_pageNum);
		}catch(Exception e){
		}
		
		Pagination<Brand> result = this.getBrandSearchService().findBrandByName(q, 0,pageNum, rows);
		
		request.setAttribute("q", q);
		request.setAttribute("result", result);
		
	}



}
