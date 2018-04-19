/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.ExcelService;
import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.ToolUtil;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.ZipFileUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.web.controller.base.BaseController;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 测试控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年04月24日
 * @version 2.5
 */
@Controller
@RequestMapping( "/static" )
public class TestController extends BaseController {

	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;

	/**进入导入页面*/
	@RequestMapping(value = "test.html" )
	public String  test() {
		return "test/test";
	}


	/**进入导入页面*/
	@RequestMapping(value = "toImport.html" )
	public String  toImport() {
		return "test/stu_notice_import";
	}

	/**导入修改学员信息*/
	@ResponseBody
	@RequestMapping(value = "importStuInfo.html" )
	public Map  importStuInfo(HttpServletRequest request,  HttpServletResponse response,
							  @RequestParam(value = "file", required = false) MultipartFile file)throws Exception{
		String path = request.getSession().getServletContext().getRealPath("") + File.separator + "tmp" + File.separator+ "test" + File.separator;
		String fileName = file.getOriginalFilename();
		File targetFile = new File(path + "import" + File.separator, fileName);
		if(!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		file.transferTo(targetFile);

		Map result=new HashMap();
		String [] heads = new String[]{"学员学号","学员姓名","性别","民族","身份证号","专业名称","年级","入学时间"};
		String [] dbNames = new String[]{"xh","xm","xb","mz","sfzh","zymc","nj","rxsj"};
		String [] etitle = new String[]{"学员学号","学员姓名","性别","民族","身份证号","专业名称","年级","入学时间","失败原因"};
		String [] edbNames = new String[]{"xh","xm","xb","mz","sfzh","zymc","nj","rxsj","msg"};

		List<Map> successList = new ArrayList<Map>();//成功表格
		List<Map> errorList = new ArrayList<Map>();//失败表格
		List<String[]> datas = null;
		try {
			datas = com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil.readAsStringList(targetFile,1,heads.length);
			String[] dataTitles = datas.remove(0);// 标题校验
			for (int i = 0; i < heads.length; i++) {
				if (!dataTitles[i].trim().equals(heads[i])){
					result.put("exception", "请下载正确表格模版填写");
					return result;
				}
			}
		} catch (Exception e) {
			result.put("exception", "请下载正确表格模版填写");
			return result;
		}
		try {
			// 保存照片的地址
			String folderName = "入学通知书_" + UUIDUtils.random();
			String zps = path + File.separator + folderName;
			File zpFils = new File(zps);
			if (!zpFils.exists()) {
				zpFils.mkdirs();
			}

			//数据校验
			for(String[] data:datas){
				Map<String,String> param = addMap(data,"");
				OutputStream outStream = null;
				try {
					GjtStudentInfo studentInfo = gjtStudentInfoDao.findByXhAndXmAndIsDeleted(param.get("xh"), param.get("xm"), Constants.BOOLEAN_NO);
					if(studentInfo != null) {
						String xm = param.get("xm").replace("*", "");
						String sfzh = studentInfo.getSfzh().replace("*", "");
						String certificateFilePath = zps + File.separator + sfzh + "（" + xm + "）.jpg";
						outStream = new FileOutputStream(certificateFilePath);

						BufferedImage tag = writeStudentInfo(studentInfo, request);
						JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
						/** 压缩质量 */
						jep.setQuality(1f, true);
						JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(outStream, jep);
						en.encode(tag);
						outStream.flush();

						successList.add(param);
					} else {
						param.put("msg", "该学员不存在");
						errorList.add(param);
					}
				} catch (Exception e) {
					param.put("msg","无法生成通知书");
					errorList.add(param);
				} finally {
					try {
						if(outStream != null)
							outStream.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			// zip
			String outputFilePath = path + File.separator + folderName + ".zip";
			ZipFileUtil.zipDir(zps, outputFilePath);
			FileKit.delFile(zps);

			//保存失败/成功列表
			String successFile = ExcelService.renderExcelHadTitle(successList, Arrays.asList(heads), Arrays.asList(dbNames) ,"导入成功列表",path);
			String errorFile = ExcelService.renderExcelHadTitle(errorList, Arrays.asList(etitle), Arrays.asList(edbNames) ,"导入失败列表",path);
			result.put("all_num",successList.size()+errorList.size());
			result.put("success_num",successList.size());
			result.put("error_num",errorList.size());
//			result.put("success_file",successFile);
			result.put("success_file",folderName + ".zip");
			result.put("error_file",errorFile);
			result.put("msg","success");
		}catch (Exception e){
			result.put("msg","error");
			result.put("exception",e.getMessage());
		}
		return result;
	}

	/**下载文件*/
	@ResponseBody
	@RequestMapping(value = "download.html" )
	public void download(HttpServletRequest request,  HttpServletResponse response, String file) throws IOException {
		if("template".equals(file)) {
			String outputUrl = "学员信息模板.xls";
			HSSFWorkbook workbook = null;
			try {
				InputStream in = this.getClass().getResourceAsStream("/excel/test/stu_notice_import_template.xls");
				workbook = new HSSFWorkbook(in);
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.downloadExcelFile(request, response, workbook, outputUrl);
		} else {
			String path = request.getSession().getServletContext().getRealPath("") + File.separator + "tmp" + File.separator + "test" + File.separator;
			ToolUtil.download(path + file, response);
		}
	}

	/**自定义数组转MAP*/
	private Map<String,String> addMap(String[] data,String msg){
		Map<String,String>  re = new HashMap();
		if (EmptyUtils.isNotEmpty(msg)){
			re.put("msg",msg);
		}
		re.put("xh",data[0]);
		re.put("xm",data[1]);
		re.put("xb",data[2]);
		re.put("mz",data[3]);
		re.put("sfzh",data[4]);
		re.put("zymc",data[5]);
		re.put("nj",data[6]);
		re.put("rxsj",data[7]);
		return  re;
	}

	public static BufferedImage writeStudentInfo(GjtStudentInfo studentInfo, HttpServletRequest request) throws ImageFormatException, IOException {
		String xh = ObjectUtils.toString(studentInfo.getXh());
		String xm = ObjectUtils.toString(studentInfo.getXm());
		// Spring 上下文
		ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		CacheService cacheService = appContext.getBean(CacheService.class);
		String xb = cacheService.getCachedDictionaryName(CacheService.DictionaryKey.SEX, ObjectUtils.toString(studentInfo.getXbm()));
		if(xb == null) xb = "";
		String mz = cacheService.getCachedDictionaryName(CacheService.DictionaryKey.NATIONALITYCODE, studentInfo.getMzm());
		if(mz == null) mz = "";
//        String mz = ObjectUtils.toString(studentInfo.getNation());
		String csrq = ObjectUtils.toString(studentInfo.getCsrq());
		String sfzh = ObjectUtils.toString(studentInfo.getSfzh());
		String zymc = ObjectUtils.toString(studentInfo.getGjtSpecialty().getZymc());
		String gradeName = ObjectUtils.toString(studentInfo.getViewStudentInfo().getGradeName());
		//1.jpg是你的 主图片的路径
		String fullPath = request.getSession().getServletContext().getRealPath("");
		String noticePath = (fullPath + "\\static\\images\\stu_notice.jpg").replace("\\", "/").replace("//", "/");
		InputStream is = new FileInputStream(noticePath);
		//通过JPEG图象流创建JPEG数据流解码器
		JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);
		//解码当前JPEG数据流，返回BufferedImage对象
		BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();

		//得到画笔对象
		Graphics g = buffImg.getGraphics();
		g.setFont(new Font("Serif", Font.BOLD, 22));
		g.setColor(Color.BLACK);
		g.drawString(xh, 160, 512);
		g.drawString(xm, 220, 598);
		g.drawString(xb, 542, 598);
		g.drawString(mz, 685, 598);
//        g.drawString(csrq, 220, 600);
		g.drawString(sfzh, 220, 645);
		g.drawString(zymc, 220, 685);
		g.drawString(gradeName, 220, 728);
		g.setFont(new Font("Serif", Font.PLAIN, 25));
		Date now = studentInfo.getCreatedDt();
		g.drawString(now.getYear() - 110 + "", 582, 1095);
		String mm = now.getMonth() + 1 + "";
		mm = mm.length() == 1 ? "0" + mm : mm;
		g.drawString(mm, 635, 1095);
		String dd = now.getDate() + "";
		dd = dd.length() == 1 ? "0" + dd : dd;
		g.drawString(dd, 702, 1095);
		g.dispose();
		is.close();
		return buffImg;
	}

}