/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;

public class ZipFileUtil {

	private static final Logger log = LoggerFactory.getLogger(ZipFileUtil.class);

	/**
	 * 缓存区大小默认20480
	 */
	private final static int BUFFER_SIZE = 2 << 10;

	private final static String ENCODING = "UTF-8";

	public static void unzip(File zipArchive, File target) throws IOException {
		ZipFile zf = new ZipFile(zipArchive, ENCODING);
		if (!target.exists()) {
			target.mkdirs();
		}

		for (Enumeration<ZipArchiveEntry> entries = zf.getEntries(); entries.hasMoreElements();) {
			ZipArchiveEntry entry = entries.nextElement();
			String zipname = entry.getName();

			if (zipname.endsWith(".zip")) {
				String innerzip = StringUtils.removeEnd(zipname, ".zip");
				File innerfolder = new File(target + File.separator + innerzip);
				if (!innerfolder.exists()) {
					innerfolder.mkdirs();
				}
				ZipArchiveInputStream zais = new ZipArchiveInputStream(zf.getInputStream(entry), ENCODING, true);
				FileOutputStream fos = null;
				ZipArchiveEntry innerzae = null;
				while ((innerzae = zais.getNextZipEntry()) != null) {
					fos = new FileOutputStream(target + File.separator + innerzip + File.separator + innerzae.getName());
					IOUtils.copy(zais, fos);
				}
				zais.close();
				fos.flush();
				fos.close();

			} else {

				ZipArchiveEntry packinfo = zf.getEntry(zipname);
				String filename = target + File.separator + zipname;
				File entryFile = new File(filename);
				String entryFileDir = entryFile.getParent();
				File dirFile = new File(entryFileDir);
				if(!dirFile.exists()) {
					dirFile.mkdirs();
				}
				if (packinfo.isDirectory()) {
					File dir = new File(filename);
					if (!dir.exists()) {
						dir.mkdirs();
					}
				} else {
					FileOutputStream fos = new FileOutputStream(filename);
					InputStream is = zf.getInputStream(packinfo);
					IOUtils.copy(is, fos);
					is.close();
					fos.flush();
					fos.close();
				}

			}
		}
		zf.close();
	}

	/**
	 * 压缩文件夹
	 *
	 * @param sourceDIR
	 *            文件夹名称（包含路径）
	 * @param targetZipFile
	 *            生成zip文件名
	 * @author liuxiangwei
	 */
	public static void zipDir(String sourceDIR, String targetZipFile) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(targetZipFile);
			zos = new ZipOutputStream(new BufferedOutputStream(fos));
			zos.setEncoding(ENCODING);
			writeZip(new File(sourceDIR), "", zos);
		} catch (FileNotFoundException e) {
			log.error("创建ZIP文件失败",e);
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				log.error("创建ZIP文件失败",e);
			}

		}
	}

	private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
		if(file.exists()){
			if(file.isDirectory()){//处理文件夹
				parentPath+=file.getName()+File.separator;
				File [] files=file.listFiles();
				for(File f:files){
					writeZip(f, parentPath, zos);
				}
			}else{
				FileInputStream fis=null;
				try {
					fis=new FileInputStream(file);
					ZipEntry ze = new ZipEntry(parentPath + file.getName());
					zos.putNextEntry(ze);
					byte [] content=new byte[1024];
					int len;
					while((len=fis.read(content))!=-1){
						zos.write(content,0,len);
						zos.flush();
					}
				} catch (FileNotFoundException e) {
					log.error("创建ZIP文件失败",e);
				} catch (IOException e) {
					log.error("创建ZIP文件失败",e);
				}finally{
					try {
						if(fis!=null){
							fis.close();
						}
					}catch(IOException e){
						log.error("创建ZIP文件失败",e);
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		/*unzip(new File("C:\\Users\\Administrator\\Desktop\\20170210001批学籍信息导出表（模板）\\20170210001批学籍信息导出表（模板）.zip"),
				new File("C:\\Users\\Administrator\\Desktop\\20170210001批学籍信息导出表（模板）\\1"));*/
		zipDir("C:\\Users\\Administrator\\Desktop\\20170210001批学籍信息导出表（模板）\\1",
				"C:\\Users\\Administrator\\Desktop\\20170210001批学籍信息导出表（模板）\\1.zip");
	}

}
