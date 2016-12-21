/**
 * 
 */
package com.ydj.xd.search.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * @author : Ares
 * @createTime : 2012-9-28 上午10:08:30
 * @version : 1.0
 * @description :
 */
public abstract class BaseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String user = (String) request.getSession().getAttribute("user");
		
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return ;
		}
		process(request,response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

	}
	
	/**
	 * 方便servlet拿到spring容器中的bean
	 * 
	 * @param beanName
	 * @return
	 */
	public Object getSpringBean(String beanName){
		
		ServletContext sc = this.getServletContext();
		
		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
	 	Object bean = context.getBean(beanName);
	 	
	 	return bean;
	}
	
	/**
	 * 具体处理
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public  abstract void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException ;

}
