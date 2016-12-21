package com.starrymedia.xd.search.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author : Ares
 * @createTime : 2012-10-18 上午10:43:12
 * @version : 1.0
 * @description :
 */
public class RightFilter implements Filter {

	/**
	 * 权限过滤
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest hRequest = (HttpServletRequest) request;
		HttpServletResponse hResponse = (HttpServletResponse) response;
		
		String url = hRequest.getRequestURI();
		
		String user = (String) hRequest.getSession().getAttribute("user");
		
		if (user == null && !url.contains("login.jsp")) {
			hResponse.sendRedirect(hRequest.getContextPath() + "/login.jsp");
		} else {
			chain.doFilter(hRequest, hResponse);
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO 这里可以一次加载所有admin-users.xml信息

	}

}
