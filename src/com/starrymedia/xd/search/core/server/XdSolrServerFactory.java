package com.starrymedia.xd.search.core.server;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrConfig;
import org.xml.sax.SAXException;

import com.starrymedia.xd.search.common.SolrServerName;

/**
 * 
 * @author : Ares
 * @createTime : 2012-9-25 下午07:31:13
 * @version : 1.0
 * @description :
 */
public class XdSolrServerFactory {

	static Logger log = Logger.getLogger(XdSolrServerFactory.class);

	public static Map<String,SolrServer> solrsServers = new HashMap<String,SolrServer>();

	/** solr/home */
	public static String solrDir;

	public static CoreContainer coreContainer = null;
	
	/**
	 * 初始化SolrServer
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static boolean initSolrServer()
			throws ParserConfigurationException, IOException, SAXException {
		
		coreContainer = getCoreContainer();
		
		solrDir = coreContainer.getSolrHome();
		
		File home = new File( solrDir );
	    File f = new File( home, "solr.xml" );
	    
	    coreContainer.load( solrDir, f );
		
		for(String s : coreContainer.getCoreNames()){
			log.info("CoreName:"+s);
			
			SolrServer solrServer = new EmbeddedSolrServer( coreContainer, s );
			solrsServers.put(s, solrServer);
		}

		return true;
	}


	/**
	 * 提供动态注册SolrServer(有需要时实现)
	 * @param solrServerName
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static boolean registerSolrServer(String solrServerName)
			throws ParserConfigurationException, IOException, SAXException {
		//TODO:提供动态注册SolrServer
		return false;
	}

	/**
	 * 删除指定solr实例
	 * @param solrServerName
	 */
	public static void unRegisterSolrServer(String solrServerName) {
		solrsServers.remove(solrServerName);

	}

	/**
	 * 获取指定solr实例
	 * @param solrServerName
	 */
	public static SolrServer getSolrServer(String solrServerName) {
		return solrsServers.get(solrServerName);
	}
	
	/**
	 * 获取指定solr实例
	 * @param solrServerName
	 */
	public static SolrServer getSolrServer(SolrServerName solrServerName) {
		return solrsServers.get(solrServerName.getName());
	}

	/**
	 * 检测是否已经包含指定solr实例
	 * @param solrServerName
	 * @return
	 */
	public static boolean isRegisterSolrServer(String solrServerName) {
		return solrsServers.containsKey(solrServerName);
	}

	public static int size() {
		return solrsServers.size();
	}

	/**
	 * （提供动态注册SolrServer时需要）
	 * @param solrdir
	 * @param configName
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	@SuppressWarnings("unused")
	private static SolrConfig getSolrConfig(String solrdir,String configName)
			throws ParserConfigurationException, IOException, SAXException {
		
		if(configName == null){
			configName = "solrconfig";
		}
		
		SolrConfig solrConfig = new SolrConfig(solrdir + "/conf/"+configName+".xml");
		return solrConfig;
	}

	private static CoreContainer getCoreContainer() {
		if (coreContainer == null) {
			coreContainer = new CoreContainer();
			return coreContainer;
		} else {
			return coreContainer;
		}
	}

}
