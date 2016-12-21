package com.starrymedia.xd.search.util;

/**
 * @author : Ares.yi
 * @createTime : 2009-6-2 
 * @version : 1.0
 * @description :
 */
public class StringUtils {
	
	private StringUtils(){}

	/**
	 * 是否不为空
	 * @param s
	 * @return
	 */
	public static boolean isNotEmpty(String s){
		return (s != null && !"".equals(s.trim()));
	}
	
	/**
	 * 是否为空
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s){
		return ( ! isNotEmpty(s));
	}
	
	/**
	 * 当字符串src（如:15）不足count(如：4)长度时，要求在其前面或后面补零达到count长度（前补：0015；后补：1500）
	 * 
	 * @param src ：原字符串
	 * @param count : 最大值9
	 * @param preOrPost ：true前补、false后补
	 * @return
	 */
	public static String fillZero(String src,int count,boolean preOrPost){
		if(src == null){
			return "00";
		}
		if("-1".equals(src)){
			src = "99";
		}
		String s = "00000000";
		int len = src.length();
		if(len < count){
			if(preOrPost){
				src = s.substring(0,count-len) + src;
			}else{
				src = src + s.substring(0,count-len);
			}
		}
		
		return src;
	}

	/**
	 * 提取指定格式字符
	 * @param src
	 * @return
	 */
	public static String getCN_US(String src) {
		if (src == null || src.trim().length() == 0){
			return "";
		}
		src = src.trim();
		StringBuffer sb = new StringBuffer("");
		int lengh = src.length();
		for (int i = 0; i < lengh; i++){
			String s = src.substring(i, i + 1);
			if (s.matches("[\u4e00-\u9fa5]+")){
				sb.append(s);
			}else if (s.matches("[\\w]+")){
				sb.append(s);
			}else if (s.equals(" ")){
				sb.append(s);
			}
		}
			
		return sb.toString();
	}


	/**
	 * TEST
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(isNotEmpty(null));
	}
}
