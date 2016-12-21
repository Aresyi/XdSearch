/**
 * 
 */
package com.starrymedia.xd.search.common;

/**
 * @author : Ares
 * @createTime : 2012-10-17 下午02:15:47
 * @version : 1.0
 * @description :
 */
public interface Log {
	
	/**
	 * 记录信息
	 * @param <T>
	 * @param t
	 */
	public <T> void logInfo(T t);
	
	/**
	 * 记录错误信息
	 * @param <T>
	 * @param t
	 */
	public <T> void logError(T t);
	
	/**
	 * 记录警告信息
	 * @param <T>
	 * @param t
	 */
	public <T> void warn(T t);
}
