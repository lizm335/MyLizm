package com.gzedu.xlims.common.gzedu;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.lang3.ObjectUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/***
 * 利用map生成xml 或者 解析xml文件为map
 * @author xiaoming
 *
 */
public class AnalyXmlUtil {

	/**
	 * 获取请求中xml数据流
	 * @param request
	 * @return
	 */
	public static String getXmlString(HttpServletRequest request) {
		int len = request.getContentLength();
		StringBuffer sb = new StringBuffer();
		if (len > 1) {
			InputStream is;
			try {
				is = request.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "GBK"));
				String buffer = null;
				while ((buffer = br.readLine()) != null) {
					sb.append(buffer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString().trim();
	}

	/***
	 * 创建xml
	 * @param formMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String createXml(Map formMap) {
		if (formMap != null && !formMap.isEmpty()) {
			Document document = DocumentHelper.createDocument();
			Element data = document.addElement("tranceData");
			mapToXml(data, formMap);
			return document.asXML();
		} else {
			return "";
		}

	}
	
	public static String createXml(Map formMap,String encode){
		if(formMap!=null && !formMap.isEmpty()){
			Document document = DocumentHelper.createDocument();
			document.setXMLEncoding(encode);
			Element data = document.addElement("tranceData");
			mapToXml(data, formMap);
			return document.asXML();
		}else{
			return "";
		}
	}

	/***
	 * 将map转换成xml节点
	 * @param data
	 * @param formMap
	 */
	@SuppressWarnings("unchecked")
	private static void mapToXml(Element data, Map formMap) {
		Iterator iterator = (Iterator) formMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			String key = (String) entry.getKey();
			Object object = entry.getValue();
			if (object instanceof List) {
				List list = (List) entry.getValue();
				for (int i = 0; i < list.size(); i++) {
					Element node = data.addElement(key);
					Object obj = list.get(i);
					if (obj instanceof Map) {
						mapToXml(node, (Map) obj);
					} else if (obj instanceof LazyDynaBean) {
						mapToXml(node, ((LazyDynaBean) obj).getMap());
					}
				}
			} else if (object instanceof Map) {
				Element node = data.addElement(key);
				mapToXml(node, (Map) object);
			} else {
				Element node = data.addElement(key);
				node.addText(ObjectUtils.toString(object));
			}
		}
	}

	/***
	 * 将xmlString解析封装成map
	 * map 保存了 String List Map中的一种或多种数据类型
	 * 在获取map的中的值的时候更具具体的xml判断map中保存的数据类型
	 * <?xml version="1.0" encoding="UTF-8"?>
		<tranceData>
			<course>
				<phone>
					<telhome>21213156450</telhome>
					<telphone>222655213130</telphone>
				</phone>
				<sex>man0</sex>
				<age>13</age>
				<name>测试0</name>
			</course>
			<course>
				<phone>
					<telhome>21213156451</telhome>
					<telphone>222655213131</telphone>
				</phone>
				<sex>man1</sex>
				<age>14</age>
				<name>测试1</name>
			</course>
			<teacher>
				<tname>侧伽师瓜</tname>
				<tid>244465464646</tid>
			</teacher>
		</tranceData>
	 * 此时返回的map 中 包括了一个course的List 和一个map的 teacher
	 * 每个course都是一个Map 而对于course中phone 也是一个map
	 * @param xml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map parserXml(String xml) {
		Map map = new HashMap();
		if (xml != null && !"".equals(xml)) {
			SAXReader saxReader = new SAXReader();
			try {
				Document document = saxReader.read(new ByteArrayInputStream(xml
						.getBytes("GBK")));
				Element root = document.getRootElement();
				map = analyNode(root);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return map;
	}

	
	@SuppressWarnings("unchecked")
	public static Map parserXmlUTF(String xml) {
		Map map = new HashMap();
		if (xml != null && !"".equals(xml)) {
			SAXReader saxReader = new SAXReader();
			try {
				Document document = saxReader.read(new ByteArrayInputStream(xml
						.getBytes("UTF-8")));
				Element root = document.getRootElement();
				map = analyNode(root);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 解析xml的节点并封装Map
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map analyNode(Element element) {
		Map map = new HashMap();
		for (Iterator j = element.elementIterator(); j.hasNext();) {
			Element node = (Element) j.next();
			String key = node.getName();
			if (element.elements(key).size() > 1) {
				if (!map.containsKey(key)) {
					List list = new ArrayList();
					map.put(key, list);
				}
				if (!node.isTextOnly()) {
					List list = (List) map.get(key);
					list.add(analyNode(node));
				} else {
					map.put(key, node.getText());
				}
			} else {
				if (!node.isTextOnly()) {
					map.put(key, analyNode(node));
				} else {
					map.put(key, node.getText());
				}
			}
		}
		return map;
	}

	/**
	 * 获取解析出来的对象list
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List getList(Object object) {
		List list = new ArrayList();
		if (object != null && object instanceof List) {
			list = (List) object;
		} else if (object != null && object instanceof Map) {
			list.add((Map) object);
		}
		return list;
	}

	/**
	 * 
	 * @param responseString
	 *            UTF-8类型的字符串
	 * @return GBK类型的字符串
	 */
	public static String getUtfTurnGbk(String responseString) {
		if (responseString != null && !"".equals(responseString)) {
			int encoding = responseString.indexOf("UTF-8");
			if (encoding >= 0) {
				responseString = responseString.replace("UTF-8", "GBK");
			}
			Map resultMapXml = AnalyXmlUtil.parserXml(responseString);
			// 拿到同步状态码
			String status = ObjectUtils.toString(resultMapXml.get("status"));
			return status;
		}
		return "0";
	}
		/**
		 * 创建短信的xml
		 * @param formMap
		 * @return
		 */
	public static String createSJXml(Map<String, Object> formMap) {
		if (formMap != null && !formMap.isEmpty()) {
			Document document = DocumentHelper.createDocument();
			Element data = document.addElement("message");
			mapToXml(data, formMap);
			return document.asXML();
		} else {
			return "";
		}
	}

}
