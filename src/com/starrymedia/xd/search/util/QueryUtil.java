
package com.starrymedia.xd.search.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import com.starrymedia.xd.search.util.mtext.extract.CharNormalization;

/**
 * @author : Ares
 * @createTime : 2012-10-11 下午13:00:40
 * @version : 1.0
 * @description :
 */
public class QueryUtil {
	
	private QueryUtil(){}

	/**
	 * 获取过滤后的字符串
	 * @param q
	 * @return
	 */
	public static String getQuerySting(String q) {
		if (null == q || "".equals(q)){
			return "";
		}
		return CharNormalization.compositeTextConvert(StringUtils.getCN_US(q),
				true, false, false, false, false, false, false);
	}
	
	/**
	 * 使用IK进行分词
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	public static String splitWord(String keyword){
		
		if (keyword == null || keyword.length() <= 0) {
			return keyword;
		}
		
		StringBuffer q = new StringBuffer();
		Set<String> words = new HashSet<String>();

		IKSegmentation ik = new IKSegmentation(new StringReader(keyword),true);
		Lexeme lexeme = null;
		try {
			while((lexeme = ik.next()) != null){
			    String s = lexeme.getLexemeText();
			    if(!words.contains(s)){
			    	words.add(s);
			    	q.append(s+" ");
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return q.toString().trim();
	}


}
