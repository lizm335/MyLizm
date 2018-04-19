package com.gzedu.xlims.common.gzdec.framework.util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import sun.misc.BASE64Decoder;

/**
 * 其实docx属于zip的一种，这里只需要操作word/document.xml中的数据，其他的数据不用动
 * @author huangyifei
 *
 */
public class XmlToDocx {
	
	/**
	 * 
	 * @param documentFile  动态生成数据的docunment.xml文件
	 * @param docxTemplatePath	docx的模板路径
	 * @param image	图片 base64格式
	 * @param toFileName	需要导出的文件路径
	 * @throws ZipException
	 * @throws IOException
	 */
	
	public void outDocx(File documentFile,String docxTemplatePath,Map<String, String> image,String toFilePath) {
		ZipOutputStream zipout = null;
		try {
			File docxFile = new File(docxTemplatePath);
			ZipFile zipFile = new ZipFile(docxFile);			
			Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
			zipout = new ZipOutputStream(new FileOutputStream(toFilePath));

			int len=-1;
			byte[] buffer=new byte[1024];
			
			Map<String, String> imageRels = new HashMap<String, String>();
			for(Entry<String, String> e : image.entrySet()) {
				String zipEntryName = "media/"+e.getKey()+".jpeg";
				zipout.putNextEntry(new ZipEntry("word/" + zipEntryName));
				BASE64Decoder decoder = new BASE64Decoder();
				ByteArrayInputStream bais = null;
		        try {
		            // Base64解码
		            byte[] b = decoder.decodeBuffer(e.getValue());
		            for (int i = 0; i < b.length; ++i) {
		                if (b[i] < 0) {// 调整异常数据
		                    b[i] += 256;
		                }
		            }
		            bais = new ByteArrayInputStream(b);
		            while((len = bais.read(buffer))!=-1){
						zipout.write(buffer,0,len);
					}
		            imageRels.put(e.getKey(), zipEntryName);
		        } catch (Exception e2) {
		            e2.printStackTrace();
		        } finally {
		            if(bais != null) {
		                try {
		                	bais.close();
		                } catch (IOException e3) {

		                }
		            }
		        }
			}
			
			while(zipEntrys.hasMoreElements()) {
				ZipEntry next = zipEntrys.nextElement();
				InputStream is = zipFile.getInputStream(next);
				//把输入流的文件传到输出流中 如果是word/document.xml由我们输入
				zipout.putNextEntry(new ZipEntry(next.toString()));
				if("word/document.xml".equals(next.toString())){
					//InputStream in = new FileInputStream(new File(XmlToDocx.class.getClassLoader().getResource("").toURI().getPath()+"template/test.xml"));
					InputStream in = new FileInputStream(documentFile);
					while((len = in.read(buffer))!=-1){
						zipout.write(buffer,0,len);
					}
					in.close();
				} else if("word/_rels/document.xml.rels".equals(next.toString())){
					SAXReader saxReader = new SAXReader();
					Document doc = saxReader.read(is);
					Element root = doc.getRootElement();
					if(imageRels.size() > 0) {
						for(Entry<String, String> e : imageRels.entrySet()) {
							Element ele = root.addElement("Relationship");
							ele.addAttribute("Id", e.getKey());
							ele.addAttribute("Type", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image");
							ele.addAttribute("Target", e.getValue());
						}
					}
					ByteArrayOutputStream fw = new ByteArrayOutputStream();
					OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
					xmlFormat.setNewlines(true);
					xmlFormat.setEncoding("UTF-8");
					XMLWriter xmlWriter = new XMLWriter(fw, xmlFormat);
					xmlWriter.write(doc);
					xmlWriter.flush();
					xmlWriter.close();
					
					ByteArrayInputStream bais = new ByteArrayInputStream(fw.toByteArray());
					
					while((len = bais.read(buffer))!=-1){
						zipout.write(buffer,0,len);
					}
					bais.close();
				} else {
					while((len = is.read(buffer))!=-1){
						zipout.write(buffer,0,len);
					}
				}
				is.close();
			}
			zipout.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            if(zipout != null) {
                try {
                	zipout.close();
                } catch (IOException e3) {

                }
            }
        }
	}
}