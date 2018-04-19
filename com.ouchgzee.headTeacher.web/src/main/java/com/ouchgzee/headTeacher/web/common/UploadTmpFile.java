/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.common;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * 文件上传，并保存为临时文件<br>
 * 功能说明：<br>
 * 文件上传<br>
 * 具体步骤：<br>
 * 1）获得磁盘文件条目工厂 DiskFileItemFactory要导包<br>
 * 2） 利用 request 获取 真实路径 ，供临时文件存储，和 最终文件存储 ，这两个存储位置可不同，也可相同<br>
 * 3）对 DiskFileItemFactory 对象设置一些 属性<br>
 * 4）高水平的API文件上传处理  ServletFileUpload upload = new ServletFileUpload(factory);<br>
 * 目的是调用 parseRequest（request）方法  获得 FileItem 集合list<br>
 * 5）在 FileItem 对象中 获取信息，   遍历， 判断 表单提交过来的信息 是否是 普通文本信息  另做处理<br>
 * 6）<br>
 *    第一种. 用第三方提供的item.write(new File(path,filename));直接写到磁盘上<br>
 *    第二种. 手动处理<br>
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月21日
 * @version 2.5
 */
public class UploadTmpFile {

    private static Log log = LogFactory.getLog(UploadTmpFile.class);

    /**
     * 临时文件的根路径
     */
    public final  static String TMP_ROOT = "tmp";

    /**
     * 文件上传，可多文件上传
     * @param request
     * @return
     */
    public static List<String> upload(HttpServletRequest request) throws Exception {
        List<String> filePaths = new ArrayList();

        request.setCharacterEncoding("utf-8");				//设置编码
        // 获取POST过来参数信息
        Map<String, String> params = new HashMap();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
            log.info("param name " + name + ", value " + valueStr);
        }

        //获得磁盘文件条目工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //获取文件需要上传到的路径
        String basePath = request.getServletContext().getRealPath(TMP_ROOT);
        String filePath = "default";
        if (params.get("folder") != null && !"".equals(params.get("folder").trim())) {
            filePath = params.get("folder");
        }
        filePath = File.separator + filePath + File.separator + DateFormatUtils.ISO_DATE_FORMAT.format(new Date());

        //如果没以下两行设置的话，上传大的 文件会占用 很多内存，
        //设置暂时存放的存储室 , 这个存储室，可以和 最终存储文件的目录不同
        /**
         * 原理 它是先存到暂时存储室，然后在真正写到对应目录的硬盘上，
         * 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tmp格式的
         * 然后再将其真正写到对应目录的硬盘上
         */
        File file = new File(basePath + filePath);
        if(!file.exists())
            file.mkdirs();
        factory.setRepository(file);
        //设置缓存的大小，当上传文件的容量超过该缓存(1G)时，直接放到暂时存储室
        factory.setSizeThreshold(1024*1024*1024);

        //高水平的API文件上传处理
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置单个文件的最大上传值(100M)
        upload.setFileSizeMax(100*1024*1024);
        // 设置整个request的最大值(1000M)
        upload.setSizeMax(1000*1024*1024);
        upload.setHeaderEncoding("UTF-8");

        List<FileItem> list = upload.parseRequest(request);
        for (FileItem item : list) {
            //获取表单的属性名字
            String name = item.getFieldName();
            //如果获取的表单信息是普通的文本信息
            if (item.isFormField()) {
                //获取用户具体输入的字符串，名字起得挺好，因为表单提交过来的是字符串类型的
                String value = item.getString("UTF-8");
                log.info("{\"name\": \"" + name + "\", \"value\": \"" + value + "\"}");
            }
            //对传入的非简单的字符串进行处理，比如说二进制的图片，电影这些
            else {
                /**
                 * 以下三步，主要获取上传文件的名字
                 */
                //获取路径名
                String value = item.getName();
                //索引到最后一个反斜杠
                int start = value.lastIndexOf("\\");
                //截取上传文件的字符串名字，加1是去掉反斜杠
                String fileName = value.substring(start + 1);

                log.info("{\"name\": \"" + name + "\", \"value\": \"" + fileName + "\"}");

                //真正写到磁盘上
                //它抛出的异常，用Exception捕捉
                //第三方提供的item.write(new File(path,filename));

                // 文件名防重复
                int dianIndex = fileName.lastIndexOf(".");
                if(dianIndex != -1) {
                    fileName = fileName.substring(0, dianIndex)
                            + "_" + Calendar.getInstance().getTimeInMillis()
                            + fileName.substring(dianIndex, fileName.length());
                }
                //手动写的
                InputStream in = item.getInputStream();
                OutputStream out = new FileOutputStream(new File(basePath + filePath, fileName));
                log.info("获取上传文件的总共的容量：" + item.getSize() + "bytes");
                int length = 0 ;
                byte[] bts = new byte[1024];

                // in.read(bts) 每次读到的数据存放在bts数组中
                while( (length = in.read(bts) ) != -1) {
                    //在   bts 数组中取出数据写到（输出流）磁盘上
                    out.write(bts, 0, length);
                }
                out.flush();
                out.close();
                in.close();
                filePaths.add((basePath + filePath + File.separator + fileName).replace("\\", "/"));
            }
        }
        return filePaths;
    }

    /**
     * 简单的文件上传，可多文件上传
     * @param request
     * @return
     * @throws Exception
     */
    public static List<String> uploadSimple(HttpServletRequest request) throws Exception {
        List<String> filePaths = new ArrayList();

        request.setCharacterEncoding("utf-8");				//设置编码
        // 检查我们有一个文件上传请求
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if(isMultipart) {
            //获取文件需要上传到的路径
            String basePath = request.getServletContext().getRealPath(TMP_ROOT);
            String filePath = "default";
            String folder = request.getParameter("folder");
            if (folder != null && !"".equals(folder.trim())) {
                filePath = folder;
            }
            filePath = File.separator + filePath + File.separator + DateFormatUtils.ISO_DATE_FORMAT.format(new Date());

            File file = new File(basePath + filePath);
            if (!file.exists())
                file.mkdirs();

            // 把文件存到/tmp
            DefaultMultipartHttpServletRequest multiReq = (DefaultMultipartHttpServletRequest) request;
            MultiValueMap<String, MultipartFile> multiValueMap = multiReq.getMultiFileMap();
            for (Iterator<Map.Entry<String, List<MultipartFile>>> iter = multiValueMap.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry<String, List<MultipartFile>> param = iter.next();
                List<MultipartFile> multipartFiles = param.getValue();
                for (MultipartFile item : multipartFiles) {
                    String fileName = item.getOriginalFilename();

                    log.info("{\"name\": \"" + param.getKey() + "\", \"value\": \"" + fileName + "\"}");

                    // 文件名防重复
                    int dianIndex = fileName.lastIndexOf(".");
                    if(dianIndex != -1) {
                        fileName = fileName.substring(0, dianIndex)
                                + "_" + Calendar.getInstance().getTimeInMillis()
                                + fileName.substring(dianIndex, fileName.length());
                    }
                    InputStream in = item.getInputStream();
                    OutputStream out = new FileOutputStream(new File(basePath + filePath, fileName));
                    log.info("获取上传文件的总共的容量：" + item.getSize() + "bytes");
                    int length = 0;
                    byte[] bts = new byte[1024];

                    // in.read(bts) 每次读到的数据存放在bts数组中
                    while ((length = in.read(bts)) != -1) {
                        //在   bts 数组中取出数据写到（输出流）磁盘上
                        out.write(bts, 0, length);
                    }
                    out.flush();
                    out.close();
                    in.close();
                    filePaths.add((basePath + filePath + File.separator + fileName).replace("\\", "/"));
                }
            }
        }
        return filePaths;
    }

}
