package com.ydj.xd.search.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author : Ares
 * @createTime : 2012-10-18 上午10:49:49
 * @version : 1.0
 * @description :
 */
public class LogoutServlet extends BaseServlet {

	private static final long serialVersionUID = -1120559093317244570L;


	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		response.sendRedirect(request.getContextPath() + "/login.jsp");
		
	}

}
