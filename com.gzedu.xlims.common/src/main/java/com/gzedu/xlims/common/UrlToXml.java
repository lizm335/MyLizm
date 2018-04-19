package com.gzedu.xlims.common;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class UrlToXml {
	/**
	 * 将URL解析参数后转成XML传回
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String eUploadUrlToMap(String url) throws Exception {
		String[] keyAndValue = null;
		Map formMap = new HashMap();

		String[] fullUrl = url.split("\\?");

		if (fullUrl.length > 0) {
			String preferences = ObjectUtils.toString(
					fullUrl[fullUrl.length - 1]).trim();

			String[] parmameters = preferences.split("&");

			for (int i = 0; i < parmameters.length; i++) {
				keyAndValue = ObjectUtils.toString(parmameters[i]).split("=");
					if(keyAndValue.length>1){
						formMap.put(ObjectUtils.toString(keyAndValue[0]).trim(),
								ObjectUtils.toString(keyAndValue[1]).trim());
					}else{
						formMap.put(ObjectUtils.toString(keyAndValue[0]).trim(),
								"");
					}
				}

		}
		UrlToXml utx = new UrlToXml();
		return utx.eUploadMapToXml(formMap);
	}

	/**
	 * map转成XML
	 * 
	 * @param formMap
	 * @return
	 * @throws Exception
	 */
	public String eUploadMapToXml(Map formMap) throws Exception {

		StringBuffer xml = new StringBuffer();
		
		Set<Entry<String, String>> set = formMap.entrySet(); 
		Iterator<Entry<String, String>> itor = set.iterator(); 
		
		xml.append(" <url> ");
		
		while(itor.hasNext()) 
		{ 
		Entry<String, String> entry = itor.next(); 
		xml.append("     <"+entry.getKey()+">" + entry.getValue()
				+ "</"+entry.getKey()+">");
		} 
		
		xml.append(" </url> ");		
		return xml.toString();
	}
	
	

	/**
	 * XML转MAP
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public Map eUploadXmlToMap(String xmlStr) throws Exception {
		Map formMap = new HashMap();

		// 主动创建document对象.
		org.dom4j.Document document = DocumentHelper.parseText(xmlStr);

		// 获取文档的根节点.
		Node root = document.getRootElement();

		// 取得某节点的单个子节点，并拿到内容后放入formMap
		if (((Element) root).element("app") != null) {
			Element appElm = ((Element) root).element("app");
			formMap.put("appCode", ObjectUtils.toString(appElm.getText()));
		}

		if (((Element) root).element("module") != null) {
			Element moduleElm = ((Element) root).element("module");
			formMap.put("moduleCode", ObjectUtils.toString(moduleElm.getText()));
		}
		
		if (((Element) root).element("moduleid") != null) {
			Element moduleidElm = ((Element) root).element("moduleid");
			formMap.put("moduleid", ObjectUtils.toString(moduleidElm.getText()));
		}		

		if (((Element) root).element("entity") != null) {
			Element entityElm = ((Element) root).element("entity");
			formMap.put("entityId", ObjectUtils.toString(entityElm.getText()));
		}

		if (((Element) root).element("type") != null) {
			Element typeElm = ((Element) root).element("type");
			formMap.put("uploadType", ObjectUtils.toString(typeElm.getText()));
		}

		if (((Element) root).element("rename") != null) {
			Element renameElm = ((Element) root).element("rename");
			formMap.put("rename", ObjectUtils.toString(renameElm.getText()));
		}

		if (((Element) root).element("name") != null) {
			Element nameElm = ((Element) root).element("name");
			formMap.put("name", ObjectUtils.toString(nameElm.getText()));
		}

		if (((Element) root).element("rtpath") != null) {
			Element rtpathElm = ((Element) root).element("rtpath");
			formMap.put("rtpath", ObjectUtils.toString(rtpathElm.getText()));
		}

		if (((Element) root).element("rtid") != null) {
			Element rtidElm = ((Element) root).element("rtid");
			formMap.put("rtid", ObjectUtils.toString(rtidElm.getText()));
		}

		if (((Element) root).element("size") != null) {
			Element sizeElm = ((Element) root).element("size");
			formMap.put("maxSize", ObjectUtils.toString(sizeElm.getText()));
		}

		if (((Element) root).element("types") != null) {
			Element typesElm = ((Element) root).element("types");
			formMap.put("fileTypes", ObjectUtils.toString(typesElm.getText()));
		}

		if (((Element) root).element("url") != null) {
			Element urlElm = ((Element) root).element("url");
			formMap.put("callbackUrl", ObjectUtils.toString(urlElm.getText()));
		}

		if (((Element) root).element("con") != null) {
			Element conElm = ((Element) root).element("con");
			formMap.put("con", ObjectUtils.toString(conElm.getText()));
		}
		
		if (((Element) root).element("opentype") != null) {
			Element opentypeElm = ((Element) root).element("opentype");
			formMap.put("opentype", ObjectUtils.toString(opentypeElm.getText()));
		}
		/**缩放类型：缩小(compress)还是放大(enlarge)**/
		if (((Element) root).element("zoom") != null) {
			Element opentypeElm = ((Element) root).element("zoom");
			formMap.put("zoom", ObjectUtils.toString(opentypeElm.getText()));
		}
		/**缩放宽度*/
		if (((Element) root).element("zoomwidth") != null) {
			Element opentypeElm = ((Element) root).element("zoomwidth");
			formMap.put("zoomwidth", ObjectUtils.toString(opentypeElm.getText()));
		}
		/**缩放高度*/
		if (((Element) root).element("zoomheight") != null) {
			Element opentypeElm = ((Element) root).element("zoomheight");
			formMap.put("zoomheight", ObjectUtils.toString(opentypeElm.getText()));
		}
		/**FTP上传相对路径地址*/
		if (((Element) root).element("ftpPath") != null) {
			Element ftpPathElm = ((Element) root).element("ftpPath");
			formMap.put("ftpPath", ObjectUtils.toString(ftpPathElm.getText()));
		}
		
		if (((Element) root).element("only") != null) {
			Element onlyElm = ((Element) root).element("only");
			formMap.put("only", ObjectUtils.toString(onlyElm.getText()));
		}

		return formMap;
		
	}

}
