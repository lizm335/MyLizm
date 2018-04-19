package com.gzedu.xlims.common.gzdec.framework.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gzedu.xlims.common.UUIDUtils;
import com.thoughtworks.xstream.core.util.Base64Encoder;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class WordTemplateUtil {
	
	private static final Log log = LogFactory.getLog(WordTemplateUtil.class);
	
	public static final String FTL_TEMPLATE_PATH = "/ftltemplate/";
	
	public static final String IMG_TMP_PATH = "/imgtmp/";

	@SuppressWarnings("rawtypes")
	public static void createWord(Map dataMap, String templateName, OutputStream output) {
		try {
			// 创建配置实例
			Configuration configuration = new Configuration();

			// 设置编码
			configuration.setDefaultEncoding("UTF-8");

			// ftl模板文件统一放至ftltemplate下面
			configuration.setClassForTemplateLoading(WordTemplateUtil.class, FTL_TEMPLATE_PATH);

			// 获取模板
			Template template = configuration.getTemplate(templateName);

			Writer out = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
			template.process(dataMap, out);

			// 关闭流
			out.flush();
			out.close();
			output.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public static String getRemoteSourceEncode(String url, String tmpPath) throws Exception {
		String fileName = UUIDUtils.random();
		String filePath = tmpPath + IMG_TMP_PATH;
		File path = new File(filePath);
		if (!path.exists()) {
			path.mkdirs();
		}
		
		File file = new File(filePath, fileName);
		OutputStream output = new FileOutputStream(file);
		
		URL u = new java.net.URL(url);
		InputStream in = u.openStream();
		
		byte b[] = new byte[1024];
        int length = 0;
        while ((length = in.read(b, 0, 1024)) != -1){
        	output.write(b, 0, length);
        }
        
        in.close();
        output.close();
		
        in = new FileInputStream(file);
		byte[] data = new byte[in.available()];
		in.read(data);
		in.close();
		
		file.delete();
		
		Base64Encoder encoder = new Base64Encoder();
		return encoder.encode(data);
	}
	
	public static void main(String[] args) throws Exception {
		/*Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentName", "张三");
		params.put("studentCode", "2016481201");
		params.put("birthday", "19920805");
		params.put("sex", "男");
		params.put("nations", "汉");
		params.put("credentialsType", "身份证");
		params.put("credentialsNo", "441284199208051564");
		params.put("politicalStatus", "群众");
		params.put("studentType", "开放教育本科");
		params.put("trainingLevel", "专科起点本科");
		params.put("specialty", "行政管理");

		params.put("company", "远程教育");
		params.put("companyPhone", "020-888888");
		params.put("phone", "1305484114445");
		params.put("homePhone", "020-456744");
		params.put("email", "1546@eenet.com");

		params.put("practiceContent", "毕业实习单位及主要内容");
		params.put("graduationDesign", "毕业论文及毕业设计题目");
		params.put("awardRecord", "在校期间受过何种奖励和处分");
		params.put("evaluation", "自我鉴定自我鉴定自我鉴定自我鉴定自我鉴定自我鉴定自我鉴定");
		
		List<Map<String, String>> eduList = new ArrayList<Map<String,String>>();
		Map<String, String> edu1 = new HashMap<String, String>();
		edu1.put("beginTime", "2002.09");
		edu1.put("endTime", "2005.07");
		edu1.put("region", "广州");
		edu1.put("school", "广州中学初中部");
		edu1.put("test", "test");
		eduList.add(edu1);
		
		Map<String, String> edu2 = new HashMap<String, String>();
		edu2.put("beginTime", "2005.09");
		edu2.put("endTime", "2008.07");
		edu2.put("region", "广州");
		edu2.put("school", "广州中学高中部");
		edu2.put("test", "test");
		eduList.add(edu2);
		
		Map<String, String> edu3 = new HashMap<String, String>();
		edu3.put("beginTime", "2008.09");
		edu3.put("endTime", "20012.07");
		edu3.put("region", "广州");
		edu3.put("school", "广州大学");
		edu3.put("test", "test");
		eduList.add(edu3);

		params.put("eduList", eduList);
		params.put("photo", getRemoteSourceEncode("http://eefile.gzedu.com/files2/xlims/image/f75105b2b32d324c592456e567f04ea7.jpg", "d://"));
		
		OutputStream output = new FileOutputStream(new File("d://test.doc"));
		
		createWord(params, "国家开放大学毕业生登记表.ftl", output);*/
		
		System.out.println(URLEncoder.encode("http://xlims.tt.gzedu.com/new/graduation/record/download?studentId=cd3935291b77485c8e6c60764d00c445"));
	}

}
