package com.starrymedia.xd.search.util.mtext.extract;

/**
 *
 * @author : Ares.yi
 * @createTime : 2010-7-20
 * @version : 1.0
 * @description :
 *  * <pre>
 * 		1. 转换成简体
 * 		2. 转换全角到半角
 * 		3. 过滤acsii
 *    </pre>
 */
public class CompositeExtractText implements ExtractText {

	public String getExtractedText(String src) {
		if (src == null) {
			return src;
		}
		return CharNormalization.compositeTextConvert(src, true, true, false,false, false, false, false);
	}
}
