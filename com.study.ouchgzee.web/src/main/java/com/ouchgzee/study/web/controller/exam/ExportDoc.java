package com.ouchgzee.study.web.controller.exam;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.gzdec.framework.util.XmlToDocx;
import com.ouchgzee.study.web.vo.AdminssionVo;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ExportDoc {
	
	private static final Log log = LogFactory.getLog(ExportDoc.class);

	private Configuration configuration = null;

	public ExportDoc() {
		configuration = new Configuration();
		configuration.setDefaultEncoding("UTF-8");
	}

	/**
	 * 装载dataMap
	 * 
	 * @param dataMap
	 * @param response
	 */
	public void create(Map<String, Object> dataMap, String basePath, String path, HttpServletResponse response) throws Exception {
		// 照片
		Map<String, String> image = new HashMap<String, String>();
		List<AdminssionVo> avos = (List<AdminssionVo>) dataMap.get("infoList");
		if(StringUtils.isNotBlank(avos.get(0).getStuPhoto())) {
			avos.get(0).setrId("rIdByStudentZp");
			image.put(avos.get(0).getrId(), avos.get(0).getStuPhoto());
		} else {
			avos.get(0).setrId("rId9");
		}
		configuration.setClassForTemplateLoading(this.getClass(), "/ftl");
		Template template = configuration.getTemplate("admissTicketNew.xml");
		String filePath = path + System.currentTimeMillis() + ((int) (Math.random() * 10000)) + ".xml";
		File xmlTempFile = new File(filePath);

        File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		if (!xmlTempFile.exists()) {
			xmlTempFile.createNewFile();
		}

		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xmlTempFile), "utf-8"));
		template.process(dataMap, out);
		out.close();
		
		//设置docx的模板路径 和文件名  
        String docxTemplate = "/template/admissTicketNew.docx";
        String toFileName = System.currentTimeMillis() + "" + ((int) (Math.random() * 10000)) + ".docx";
        String toFilePath = path + toFileName;
		//3.把填充完成的xml写入到docx中  
        XmlToDocx xtd = new XmlToDocx();  
        xtd.outDocx(xmlTempFile, basePath + docxTemplate, image, toFilePath);
        xmlTempFile.delete();

		// 导出时有界面，可选择下载路径
		response.addHeader("Content-Disposition",
				"attachment;filename=" + new String(toFileName.getBytes("utf-8"), "utf-8"));
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		OutputStream out1 = null;
		InputStream in = null;

		try {
			in = new FileInputStream(toFilePath);

			out1 = response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(in);
			BufferedOutputStream bos = new BufferedOutputStream(out1);

			byte[] buff = new byte[20480];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bis.close();
			bos.flush();
			bos.close();
			FileKit.delFile(toFilePath);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
		} finally {
			if (out1 != null)
				out1.close();
			if (in != null)
				in.close();
		}

	}

}
