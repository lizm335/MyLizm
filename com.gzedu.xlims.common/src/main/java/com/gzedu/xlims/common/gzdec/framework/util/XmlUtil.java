package com.gzedu.xlims.common.gzdec.framework.util;

import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlUtil {
	public static Document read(String xmlFile) throws DocumentException{
		SAXReader reader = new SAXReader();
		return reader.read(XmlUtil.class.getResourceAsStream(xmlFile));
	}
	
	public static Document createDocument(){
		return DocumentHelper.createDocument();
	}
	
	public static void write(Document doc,String xmlFile) throws IOException{
		FileWriter fw = new FileWriter(xmlFile);
		OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
		xmlFormat.setNewlines(true);
		xmlFormat.setEncoding("UTF-8");
		XMLWriter xmlWriter = new XMLWriter(fw, xmlFormat);
		xmlWriter.write(doc);
		xmlWriter.flush();
		xmlWriter.close();
	}
}
