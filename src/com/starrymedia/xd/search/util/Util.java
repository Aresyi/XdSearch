/**
 * 
 */
package com.starrymedia.xd.search.util;

import java.util.Calendar;

/**
 * @author : Ares
 * @createTime : 2012-10-16 下午02:49:39
 * @version : 1.0
 * @description :
 */
public class Util {
	
	/**质数表示星期*/
	public static final int WEEK[] = {2,3,5,7,11,13,17}; 
	
	/**质数表示发布渠道*/
	public static final int CHANNEL_TYPE[] = {2,3};
	
	
	private Util(){}
	
	/**
	 * 获取一个数的因子字符串(因子之间以" "隔开)
	 * @param geneArray
	 * @param num
	 * @return
	 */
	public static String getGeneStr(int geneArray[],Integer num){
		if(num == null){
			return "0";
		}
		
		int a [] = geneArray;
		
		StringBuilder sb = new StringBuilder();
		for(int i : a){
			if( num % i == 0 ){
				sb.append(i+" ");
			}
		}
		
		return sb.toString();
	}

	
	/**
	 * 将首字母转大写
	 * @param src
	 * @return
	 */
	public static  String indexToUpperCase(String src){//A-Z:65~90 a-z:97~122
		if(src == null || "".equals(src.trim())){
			return "";
		}
		int i = src.charAt(0);
		if(i>=97 && i<=122){
			return src.replaceFirst(src.charAt(0)+"", (char)(i-32)+""); //注意: 不可使用replace()方法
		}
		return src;
	}
	
	
	/**
	 * 
	 * 算指定日期是星期几
	 * @param date：时间戳
	 * @return
	 */
	public static int getDayOfWeek(long date){
		
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); 
			
		return (dayOfWeek-1);
	}
	
	/**
     * 根据经纬度，获取两点间的距离(网络资料)
     * 
     * @author zhijun.wu
     * @param lng1 经度
     * @param lat1 纬度
     * @param lng2
     * @param lat2
     * @return
     *
     * @date 2011-8-10
     */
    public static double distanceByLngLat(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = lat1 * Math.PI / 180;
        double radLat2 = lat2 * Math.PI / 180;
        double a = radLat1 - radLat2;
        double b = lng1 * Math.PI / 180 - lng2 * Math.PI / 180;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
        s = Math.round(s * 10000) / 10000;

        return s;
    }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getGeneStr(Util.WEEK,0)+"  " + Integer.MAX_VALUE);
	}

}
