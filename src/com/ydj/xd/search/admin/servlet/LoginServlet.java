package com.ydj.xd.search.admin.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.starrymedia.xd.search.util.StringUtils;

/**
 * 
 * @author : Ares
 * @createTime : 2012-10-18 上午11:32:53
 * @version : 1.0
 * @description :
 */
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String uname = request.getParameter("uname");
		String pwd = request.getParameter("pwd");
		
		if(StringUtils.isEmpty(uname) || StringUtils.isEmpty(pwd)){
			request.setAttribute("message", "Error userName or password");
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return ;
		}
		
		String path = getServletContext().getRealPath("/")+ "WEB-INF/admin-users.xml";
		String reaPwd = getPwd(uname.trim(), path);
		
		if (null != reaPwd && reaPwd.equals(pwd.trim())) {
			HttpSession session = request.getSession();
			session.setAttribute("user", uname);
			response.sendRedirect("index.jsp");
		} else{
			request.setAttribute("message", "Error userName or password");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

	}

	@SuppressWarnings("unchecked")
	private String getPwd(String username, String path) {
		String password = null;
		SAXReader saxReader = null;
		Document doc = null;
		try {
			saxReader = new SAXReader();
			doc = saxReader.read(new File(path));

		} catch (DocumentException e) {
			return password;
		}
		Element root = doc.getRootElement();

		List users = root.elements("user");
		Iterator it = users.iterator();
		while (it.hasNext()) {
			Element user = (Element) it.next();
			if (username.equals(user.attributeValue("username"))) {
				password = user.attributeValue("password");
				break;
			}
		}
		return password;
	}

}
