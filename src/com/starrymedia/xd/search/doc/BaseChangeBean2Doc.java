/**
 * 
 */
package com.starrymedia.xd.search.doc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.solr.common.SolrInputDocument;

import com.starrymedia.xd.search.util.Util;

/**
 * @author : Ares
 * @createTime : 2012-9-27 上午10:31:03
 * @version : 1.0
 * @description :
 */
public abstract class BaseChangeBean2Doc<T> implements ChangeBean2Doc<T> {
	
	@Override
	public SolrInputDocument autoChange2Doc(T obj){
		
		if(obj == null){
			return null;
		}
		
		SolrInputDocument doc = new SolrInputDocument();
		
		Class<?> clazz = obj.getClass();
		
		Field field[] = clazz.getDeclaredFields();
		for(Field f :field){
			String name = f.getName();
			
			if(name.toLowerCase().equals("serialversionuid")){
				continue;
			}
			
			Method m = null;
			Object value = null;
			try {
				m = clazz.getDeclaredMethod("get"+Util.indexToUpperCase(name));
				value = m.invoke(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(value == null){
				value = "";
			}
			
			doc.addField(name,value);
		}
		
		return doc;
	}
	
	
}
