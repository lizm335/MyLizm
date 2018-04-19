/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common.eefile;

import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.ImgBase64Convert;
import com.gzedu.xlims.common.UUIDUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * eefile文件上传工具
 * 功能说明：
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年1月22日
 * @version 2.5
 *
 */
public class EEFileUploadUtil {
	
	private static final Logger log = LoggerFactory.getLogger(EEFileUploadUtil.class);
	
	private final static String APPID = "APP038";
	
	private final static String ACCESS_KEY_ID = "b49ca70bac1082b01318bd3cecf317e4";
	
	private final static String ACCESS_KEY_SECRET = "17b16aedf2f46e32ccfbd6049090925f";
	
	private final static String ALIYUN_EEFILE_SERVER = "http://eefile.gzedu.com";
	
	/**
	 * 上传base64格式的图片
	 * @param base64Url base64的图片字符串
	 * @param tmpFolderPath 临时存放文件夹
	 * @return eefile图片地址
	 */
	public static String uploadImageBase64ToUrl(String base64Url, String tmpFolderPath) {
		File imageFile = null;
		try {
			// 获取临时文件
			log.info("获取临时文件...");
			imageFile = getImageFile(base64Url, tmpFolderPath);
			log.info("imageFile:" + imageFile.getAbsolutePath());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if(imageFile != null) {
			try {
				// 调接口上传到阿里云
				log.info("上传到阿里云...");
				String[] aliyuPaths = sendAliyu("/ossupload/uploadInterface.do", "/files2/xlims/image", imageFile);
				
				return aliyuPaths != null && aliyuPaths.length > 0 ? aliyuPaths[0] : null;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				try {
					// 删除临时文件
					imageFile.delete();
				} catch (Exception e2) {
					
				}
			}
		}
		return null;
	}

	/**
	 * 批量上传base64格式的图片
	 * @param base64Urls base64的图片字符串
	 * @param tmpFolderPath 临时存放文件夹
	 */
	public static String[] uploadImageBase64ToUrls(List<String> base64Urls, String tmpFolderPath) {
		String[] imageUrls = new String[base64Urls.size()];
		File[] imageFiles = new File[base64Urls.size()];
		for (int i = 0; i < base64Urls.size(); i++) {
			String base64Url = base64Urls.get(i);
			if(base64Url != null && !"".equals(base64Url.trim())) {
				try {
					// 获取临时文件
					imageFiles[i] = getImageFile(base64Url, tmpFolderPath);
				} catch (Exception e) {
					
				}
			}
		}
		try {
			// 调接口上传到阿里云
			String[] aliyuPaths = sendAliyu("/ossupload/uploadInterface.do", "/files2/xlims/image", imageFiles);
			int j = 0;
			for (int i = 0; i < imageFiles.length; i++) {
				if(imageFiles[i] != null) {
					imageUrls[i] = aliyuPaths[j];
					j++;
				}
			}
			return imageUrls;
		} catch (Exception e) {
			
		} finally {
			try {
				for (File imageFile : imageFiles) {
					if(imageFile != null) {
						// 删除临时文件
						imageFile.delete();
					}
				}
			} catch (Exception e2) {
				
			}
		}
		return null;
	}

	/**
	 * 生成图片路径
	 * @param tmpFolderPath
	 * @param suffix
	 * @return
	 */
	private static String createImgFilePath(String tmpFolderPath, String suffix) {
		String fileName = UUIDUtils.random() + "." + suffix;
		tmpFolderPath = tmpFolderPath.replace("\\", "/").replace("//", "/");
		File folder = new File(tmpFolderPath);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		return tmpFolderPath + File.separator + fileName;
	}
	
	/**
	 * 获取图片文件
	 * @param dataBase64Url
	 * @param tmpFolderPath
	 * @return
	 * @throws IOException
	 */
	private static File getImageFile(String dataBase64Url, String tmpFolderPath) throws IOException{
		int type = 1;
		String typeName = "jpg";
		
		String[] urlArr = dataBase64Url.split("base64,");
		String data = urlArr[0];
		String base64Url = urlArr[1];
		if(data.contains("png")) {
			type = 2;
			typeName = "png";
		} else if(data.contains("svg")) {
			type = 3;
			typeName = "png";
		}
		String imgFilePath = createImgFilePath(tmpFolderPath, typeName);
		
		switch (type) {
			case 3:
//				SvgToImg.convert2PNG(base64Url, imgFilePath);
				break;
			default:
				ImgBase64Convert.generateImage(base64Url, imgFilePath);
				break;
		}
	    return new File(imgFilePath);
	}
	
	/**
	 * 上传到阿里云eefile文件服务器
	 * @param interfacePath
	 * @param path
	 * @param files
	 * @return
	 */
	public static String[] sendAliyu(String interfacePath, String path, File...files){
		String[] resString = null;
		HttpClient client = null;
		try {
            HttpPost httppost = new HttpPost(new URI(ALIYUN_EEFILE_SERVER + interfacePath));
            StringBody comment = new StringBody("files");
            StringBody uuid = new StringBody(ACCESS_KEY_ID);
            StringBody key = new StringBody(ACCESS_KEY_SECRET);
            StringBody aLiPath = new StringBody(path);
            MultipartEntity reqEntity = new MultipartEntity();
            for (File file : files) {
            	if(file != null) {
            		reqEntity.addPart("files", new FileBody(file));//file1为请求后台的File upload;属性
            	}
			}
            reqEntity.addPart("formMap.FILE_TYPE", comment);//filename为请求后台的普通参数;属性
            reqEntity.addPart("formMap.ACCESS_KEY_ID", uuid);
            reqEntity.addPart("formMap.ACCESS_KEY_SECRET", key);
            reqEntity.addPart("formMap.filecwd", aLiPath);
            httppost.setEntity(reqEntity);

			client = new DefaultHttpClient();
			client.getConnectionManager().closeIdleConnections(10, TimeUnit.SECONDS);
            HttpResponse response = client.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("return statusCode: " + statusCode);
            if(statusCode == HttpStatus.SC_OK){
                HttpEntity resEntity = response.getEntity();
                String result = EntityUtils.toString(resEntity);//httpclient自带的工具类读取返回数据
                log.info("return result: " + result);

                Map<String, Object> resMap = GsonUtils.toBean(result, HashMap.class);
                List<Map<String, Object>> res = (List<Map<String, Object>>) resMap.get("resultList");
                if(res != null && res.size() > 0) {
                	resString = new String[res.size()];
                	for (int i = 0; i < res.size(); i++) {
						Map<String, Object> map = res.get(i);
	                	if(map.get("STATUS").toString().equals("1")) {
	                		resString[i] = map.get("FILE_PATH").toString();
	                    }
					}
                }
                EntityUtils.consume(resEntity);
            }
		} catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
            	client.getConnectionManager().shutdown();
            } catch (Exception ignore) {
            }
        }
		return resString;
	}

	/**
	 * 1.6.	检测和识别中华人别共和国第二代身份证信息
	 * @param imgUrl 身份证正反面图片地址
	 * @return
	 */
	public static Map<String, Object> getIDCardInfo(String imgUrl) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("formMap.imgUrl", imgUrl);
			String result = HttpClientUtils.doHttpPost(ALIYUN_EEFILE_SERVER + "/upload/getOcridcard.do", params, 10000, Consts.UTF_8.name());
			if(!"".equals(result)) {
				Map resMap = GsonUtils.toBean(result, HashMap.class);
				List<Map<String, Object>> cards = (List<Map<String, Object>>) resMap.get("cards");
				if (cards != null && cards.size() > 0) {
					return cards.get(0);
				} else {
					log.error("function getIDCardInfo error ======== " + result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyMap();
	}

	/**
	 * 识别图片是否是身份证
	 * @param imgUrl 身份证正反面图片地址
	 * @return
	 */
	public static boolean isIDCard(String imgUrl) {
		Map<String, Object> info = getIDCardInfo(imgUrl);
		Double type = (Double) info.get("type");
		return type != null ? type.intValue() == 1 : false;
	}

	/**
	 * 判断是否满足像素
	 * @param imgUrl 图片地址
	 * @param xresolution 默认150×210
	 * @param yresolution 默认150×210
	 * @return
	 */
	public static boolean isMeetPixel(String imgUrl, Integer xresolution, Integer yresolution) {
		if(xresolution == null || yresolution == null) {
			xresolution = 150;
			yresolution = 210;
		}
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("formMap.PATH", imgUrl);
			String result = HttpClientUtils.doHttpPost(ALIYUN_EEFILE_SERVER + "/ossupload/getImageInfo.do", params, 10000, Consts.UTF_8.name());
			Map resMap = GsonUtils.toBean(result, HashMap.class);
			if("0".equals((String) resMap.get("STATUS"))) {
				List<Map<String, Object>> resultList = (List<Map<String, Object>>) resMap.get("resultList");
				if (resultList != null && resultList.size() > 0) {
					Map<String, Object> entity = resultList.get(0);
					log.debug("getImageInfo result:" + entity);
					int x = NumberUtils.toInt((String) entity.get("XRESOLUTION"));
					int y = NumberUtils.toInt((String) entity.get("YRESOLUTION"));
					return x >= xresolution && y >= yresolution;
				}
			}
		} catch (Exception e) {

		}
		return true; // 默认让他通过
	}
	
	public static void main(String[] args) {
//		System.out.println(EEFileUploadUtil.isMeetPixel("http://eefile.download.eenet.com/files2/xlims/image/928df0a8bdc1ab04ea683d4ecd8d7e41.png", null, null));
//		for (int i = 0; i < 10; i++) {
//			System.out.println(EEFileUploadUtil.uploadImageBase64ToUrl("data:image/png;base64,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", "D:\\"));
//		}
//		System.out.println(EEFileUploadUtil.getIDCardInfo("http://eefile.download.eenet.com/files2/test1/test/eb52f709fb5357af57c3462c409111c3.jpg?x-oss-process=image/rotate,0/crop,x_0,y_0,w_1600,h_1059"));
		for (int i=0;i<10;i++) {
			final int ci = i + 1;
			new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println("第"+ci+"次请求：" + EEFileUploadUtil.getIDCardInfo("http://eefile.download.eenet.com/files2/test1/test/171a19e1971f63b84164742f4fcb1658.jpg?x-oss-process=image/resize,h_4000,w_4000/auto-orient,1/rotate,0/crop,x_348,y_74,w_2332,h_1542"));
				}
			}).start();

		}
//		System.out.println(EEFileUploadUtil.isIDCard("http://eefile.download.eenet.com/files2/xlims/image/82c1a8654a61b96fd609d59bd24d09cf.jpg"));
	}

}
