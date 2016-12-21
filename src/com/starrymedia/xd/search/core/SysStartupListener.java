/**
 * 
 */
package com.starrymedia.xd.search.core;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.web.context.ContextLoaderListener;
import org.xml.sax.SAXException;

import com.starrymedia.xd.search.core.server.XdSolrServerFactory;

/**
 * @author : Ares
 * @createTime : 2012-9-26 上午11:11:50
 * @version : 1.0
 * @description :
 */
public class SysStartupListener extends ContextLoaderListener implements
		ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		
		outputMessage("[INFO] System start");
		
		super.contextInitialized(event);
		
		try {
			XdSolrServerFactory.initSolrServer();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
		outputMessage("[ERROR] System shutdown");
	}
	
	
	private static void outputMessage(String message) {

		String separated = "************************************************************************************************************************";
		String whitespace = " ";
		message = new StringBuilder(message).append(" at ").append(new Date())
				.append(".").toString();

		System.out.println(separated);
		System.out.print("*");

		int i = 1;
		int flag = separated.length() - message.length();
		for (; i < flag / 2; i++){
			System.out.print(whitespace);
		}

		System.out.print(message);

		for (int j = 1; j < flag - i; j++){
			System.out.print(whitespace);
		}

		System.out.println("*");
		System.out.println(separated);

	}

}
