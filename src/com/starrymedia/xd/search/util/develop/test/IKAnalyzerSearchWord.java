package com.starrymedia.xd.search.util.develop.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.AttributeImpl;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;

/**
 * 
 * @author : Ares
 * @createTime : 2012-10-30 下午03:17:06
 * @version : 1.0
 * @description :
 */
public class IKAnalyzerSearchWord {
	 private static String fieldName = "text";   
	    public  static void searchWord(String field ,String keyword) {   
	        if(null!=field&&!"".equals(field)){   
	            fieldName = field;   
	        }   
	        String text = "IK Analyzer是一个开源的，基于java语言开发的轻量级的中文分词工具包。从2006年12月推出1.0版开始， " +   
	                "IKAnalyzer已经推出了3个大版本。最初，它是以开源项目Luence为应用主体的，结合词典分词和文法分析算法的中文分词组件。" +   
	                "新版本的IK Analyzer 3.0则发展为面向Java的公用分词组件，独立于Lucene项目，同时提供了对Lucene的默认优化实现。 ";      
	        Analyzer analyzer = new IKAnalyzer();   
	        StringReader reader = new StringReader(text);      
	             
	        long startTime = System.currentTimeMillis();    //开始时间      
	        TokenStream ts = analyzer.tokenStream(" ", reader);      
	        Iterator<AttributeImpl> it = ts.getAttributeImplsIterator();   
	        while(it.hasNext()){   
	            System.out.println((AttributeImpl)it.next());   
	        }   
	        System.out.println("");      
	             
	        long endTime = System.currentTimeMillis();  //结束时间      
	        System.out.println("IK分词耗时" + new Float((endTime - startTime)) / 1000 + "秒!");    
	        Directory dir = null;   
	        IndexWriter writer = null;   
	        IndexSearcher searcher = null;   
	        try {   
	            dir = new RAMDirectory();   
	            writer = new IndexWriter(dir, analyzer, true,   
	                    IndexWriter.MaxFieldLength.LIMITED);   
	            System.out.println(IndexWriter.MaxFieldLength.LIMITED);   
	            Document doc = new Document();   
	            doc.add(new Field(fieldName, text, Field.Store.YES,   
	                    Field.Index.ANALYZED));   
	            writer.addDocument(doc);   
	            writer.close();   
	            //在索引其中使用IKSimilarity相似评估度   
	            searcher = new IndexSearcher(dir);   
	            searcher.setSimilarity(new IKSimilarity());   
	            Query query = IKQueryParser.parse(fieldName, keyword);   
	            TopDocs topDocs = searcher.search(query, 5);   
	            System.out.println("命中："+topDocs.totalHits);   
	            ScoreDoc[] scoreDocs = topDocs.scoreDocs;   
	            for (int i = 0; i < scoreDocs.length; i++) {   
	                Document targetDoc = searcher.doc(scoreDocs[i].doc);   
	                System.out.println("內容："+targetDoc.toString());   
	            }   
	        } catch (Exception e) {   
	            System.out.println(e);   
	        }finally{   
	            try {   
	                searcher.close();   
	            } catch (IOException e) {   
	                e.printStackTrace();   
	            }   
	            try {   
	                dir.close();   
	            } catch (IOException e) {   
	                e.printStackTrace();   
	            }   
	        }   
	    }   
	    public static void main(String[] args) {   
	        long a = System.currentTimeMillis();   
	        IKAnalyzerSearchWord.searchWord("","中文分词工具包");   
	        System.out.println(System.currentTimeMillis()-a);   
	    }   
}
