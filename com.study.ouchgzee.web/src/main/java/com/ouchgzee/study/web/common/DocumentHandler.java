package com.ouchgzee.study.web.common;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class DocumentHandler {

	private Configuration configuration = null;

	public DocumentHandler() {
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
	}

	public void createDoc(Map<String, Object> dataMap, String fileName) throws IOException {
		// dataMap 要填入模本的数据文件
		// 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
		// 这里我们的模板是放在template下面
		configuration.setDirectoryForTemplateLoading(
				new File(DocumentHandler.class.getClassLoader().getResource("").getPath() + "ftl"));
		Template t = null;
		try {
			// model.ftl为要装载的模板
			t = configuration.getTemplate("model.ftl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		File outFile = new File(fileName);
		Writer out = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outFile);
			OutputStreamWriter oWriter = new OutputStreamWriter(fos, "UTF-8");
			out = new BufferedWriter(oWriter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			t.process(dataMap, out);
			out.close();
			fos.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void createDocPath(Map<String, Object> dataMap, String fileName, String modelName) throws IOException {
		configuration.setDirectoryForTemplateLoading(
				new File(DocumentHandler.class.getClassLoader().getResource("").getPath() + "/ftl"));
		Template t = null;
		try {
			t = configuration.getTemplate(modelName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File outFile = new File(fileName);
		Writer out = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outFile);
			OutputStreamWriter oWriter = new OutputStreamWriter(fos, "UTF-8");
			out = new BufferedWriter(oWriter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			t.process(dataMap, out);
			out.close();
			fos.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		File file = new File(DocumentHandler.class.getClassLoader().getResource("").getPath());
		File[] f = file.listFiles();
		for (int i = 0; i < f.length; i++) {
			System.out.println(f[i].getName());
		}
		System.out.println(DocumentHandler.class.getClassLoader().getResource("").getPath());
	}
}
