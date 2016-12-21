package com.starrymedia.xd.search.exception;

import org.apache.log4j.Logger;

/**
 * 
 * @author : Ares
 * @createTime : Sep 25, 2012 1:10:25 PM
 * @version : 1.0
 * @description :
 */
public class SearchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4975741976764634938L;
	private Logger logger = Logger.getLogger("SearchException");


	/**
	 * 
	 * @param t
	 */
	public SearchException(Throwable t) {

		super(t);
		StackTraceElement elements[] = t.getStackTrace();
		String method = elements[0].getMethodName(); 
		String className = elements[0].getClassName();
		logger.error("Error in SearchException,called is===>" 
				+ className
				+ ",method is====>"
				+ method 
				+ ",reason is===>"
				+ t.getMessage());
	}

	/**
	 * 
	 */
	public SearchException() {
		super(); 
		logger.error("Error in SearchException");
	}

	/**
	 * 
	 * @param msg
	 */
	public SearchException(String msg) {
		super(msg);
		logger.error("Error in SearchException,reason is===>" + msg);
	}

}
