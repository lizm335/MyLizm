/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.common;

import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.service.CacheService;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * 通知书<br/>
 * 功能说明：
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年1月11日
 * @version 2.5
 *
 */
public class AdmissionNoticeUtil {
	
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
        String yearName = ObjectUtils.toString(studentInfo.getGjtGrade().getGjtYear().getName());
        //1.jpg是你的 主图片的路径
        String fullPath = request.getSession().getServletContext().getRealPath("");
        String noticePath = (fullPath + "\\static\\images\\stu_notice.jpg").replace("\\", "/").replace("//", "/");
        // 国家开放大学实验学院
        if("9b2f42ececf64f38af621554d1ea5b79".equals(studentInfo.getXxId())) {
                noticePath = (fullPath + "\\static\\images\\stu_notice_beijing.jpg").replace("\\", "/").replace("//", "/");
        } else if("8cf8895c0e074b45a6e9d27b838acbc2".equals(studentInfo.getXxId())) {
                // 绵阳广播电视大学
                noticePath = (fullPath + "\\static\\images\\stu_notice_mianyang.jpg").replace("\\", "/").replace("//", "/");
        }
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
        g.drawString(yearName, 220, 728);
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
