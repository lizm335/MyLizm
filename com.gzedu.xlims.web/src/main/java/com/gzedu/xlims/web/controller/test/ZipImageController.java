/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.test;

import com.gzedu.xlims.web.controller.base.BaseController;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器<br/>
 * 功能说明：图片压缩
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年05月09日
 * @version 2.5
 */
@Controller
@RequestMapping( "/static" )
public class ZipImageController extends BaseController {

	/**进入导入页面*/
	@RequestMapping(value = "test_zip_image.html" )
	public String  test() {
		return "test/test_zip_image";
	}


	/**进入导入页面*/
	@RequestMapping(value = "toZipImage.html" )
	public String  toImport() {
		return "test/zip_image_import";
	}

	/**导入修改学员信息*/
	@ResponseBody
	@RequestMapping(value = "importZipImage.html" )
	public Map  importStuInfo(HttpServletRequest request,  HttpServletResponse response,
							  @RequestParam(value = "file", required = false) MultipartFile file)throws Exception{
		String path = request.getSession().getServletContext().getRealPath("") + File.separator + "tmp" + File.separator+ "test" + File.separator;
		String fileName = file.getOriginalFilename();
		File targetFile = new File(path, fileName);
		if(!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}

		Map result=new HashMap();
		try {
			saveMinPhoto(file.getInputStream(), path + fileName, 3000, 0.9d);
			result.put("all_num",1);
			result.put("success_num",1);
			result.put("error_num",0);
//			result.put("success_file",successFile);
			result.put("success_file",fileName);
			result.put("error_file",null);
			result.put("msg","success");
		}catch (Exception e){
			result.put("msg","error");
			result.put("exception",e.getMessage());
		}
		return result;
	}

	/**
	 * 等比例压缩算法：
	 * 算法思想：根据压缩基数和压缩比来压缩原图，生产一张图片效果最接近原图的缩略图
	 * @param srcURL 原图地址
	 * @param deskURL 缩略图地址
	 * @param comBase 压缩基数
	 * @param scale 压缩限制(宽/高)比例  一般用1：
	 * 当scale>=1,缩略图height=comBase,width按原图宽高比例;若scale<1,缩略图width=comBase,height按原图宽高比例
	 * @return 是否压缩成功
	 */
	public static boolean saveMinPhoto(InputStream is, String deskURL, double comBase, double scale) {
		FileOutputStream deskImage = null;
		try {
			Image src = ImageIO.read(is);
			int srcHeight = src.getHeight(null);
			int srcWidth = src.getWidth(null);
			int deskHeight = 0;// 缩略图高
			int deskWidth = 0;// 缩略图宽
			double srcScale = (double) srcHeight / srcWidth;
			/**缩略图宽高算法*/
			if ((double) srcHeight > comBase || (double) srcWidth > comBase) {
				if (srcScale >= scale || 1 / srcScale > scale) {
					if (srcScale >= scale) {
						deskHeight = (int) comBase;
						deskWidth = srcWidth * deskHeight / srcHeight;
					} else {
						deskWidth = (int) comBase;
						deskHeight = srcHeight * deskWidth / srcWidth;
					}
				} else {
					if ((double) srcHeight > comBase) {
						deskHeight = (int) comBase;
						deskWidth = srcWidth * deskHeight / srcHeight;
					} else {
						deskWidth = (int) comBase;
						deskHeight = srcHeight * deskWidth / srcWidth;
					}
				}
			} else {
				deskHeight = srcHeight;
				deskWidth = srcWidth;
			}
			BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_3BYTE_BGR);
			tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); //绘制缩小后的图
			deskImage = new FileOutputStream(deskURL); //输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(deskImage);
			encoder.encode(tag); //近JPEG编码
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(deskImage != null) {
				try {
					deskImage.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

}