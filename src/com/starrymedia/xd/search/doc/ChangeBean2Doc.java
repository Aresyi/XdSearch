/**
 * 
 */
package com.starrymedia.xd.search.doc;

import org.apache.solr.common.SolrInputDocument;

/**
 * @author : Ares
 * @createTime : 2012-9-27 上午10:16:12
 * @version : 1.0
 * @description :
 * 		封装一个索引 doc: 将Bean/POJO转换Solr文档(SolrInputDocument)
 * 		<p>
 * 		提示：当全属性映射doc时，使用autoChange2Doc
 * 		<p>
 * 		注意：fieldName必须和schema中所定义的保持一致
 * 
 */
public interface ChangeBean2Doc<T> {

	/**
	 * 手动转换（麻烦，但更灵活）
	 * @param obj
	 * @return
	 */
	public SolrInputDocument change2Doc(T obj);
	
	/**
	 * 全属性映射
	 * @param t
	 * @return
	 */
	public SolrInputDocument autoChange2Doc(T obj);
}
