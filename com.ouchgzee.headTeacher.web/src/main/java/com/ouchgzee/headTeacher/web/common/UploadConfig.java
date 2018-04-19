/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.common;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.gzdec.framework.fileupload.UploadFileParam;

/**
 * 文件上传回调配置<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月19日
 * @version 2.5
 */
public class UploadConfig {

    /**
     * 头像上传服务器回调本项目的地址
     */
    public final  static String HEAD_IMG_TRANSFER = "/upload/headimgtransfer";
    /**
     * 班级活动 活动的图片上传
     */
    public final  static String HEAD_IMG_ACTIVITY = "/upload/headimgactivity";
    /**
     * 报读资料 上传服务器回调本项目的地址
     BMB_Z:报名表正面
     BMB_F:报名表反面
     SFZ_Z:身份证正面
     SFZ_F:身份证反面
     BYZ_Z:毕业证正面
     BYZ_F:毕业证反面
     ZP:个人照片
     XLZ:学历证
     */
    public final static String IMG_TRANSFER_BMB_Z = "/upload/imgtransfer_BMB_Z";
    public final static String IMG_TRANSFER_BMB_F = "/upload/imgtransfer_BMB_F";
    public final static String IMG_TRANSFER_SFZ_Z = "/upload/imgtransfer_SFZ_Z";
    public final static String IMG_TRANSFER_SFZ_F = "/upload/imgtransfer_SFZ_F";
    public final static String IMG_TRANSFER_BYZ_Z = "/upload/imgtransfer_BYZ_Z";
    public final static String IMG_TRANSFER_BYZ_F = "/upload/imgtransfer_BYZ_F";
    public final static String IMG_TRANSFER_ZP = "/upload/imgtransfer_ZP";
    public final static String IMG_TRANSFER_XLZ = "/upload/imgtransfer_XLZ";

    /**
     * 生成回调URL
     * @param basePath
     * @return
     */
    public static String createUploadUrl(String basePath) {
        return createUploadUrl(basePath, HEAD_IMG_TRANSFER, null);
    }
    
    /**
     * 生成班级活动图片回调URL
     * @param basePath
     * @return
     */
    public static String createUploadUrlActivity(String basePath) {
        return createUploadUrl(basePath, HEAD_IMG_ACTIVITY, null);
    }

    public static String createUploadUrlByBMB_Z(String basePath, String studentId) {
        return createUploadUrl(basePath, IMG_TRANSFER_BMB_Z, studentId);
    }
    public static String createUploadUrlByBMB_F(String basePath, String studentId) {
        return createUploadUrl(basePath, IMG_TRANSFER_BMB_F, studentId);
    }
    public static String createUploadUrlBySFZ_Z(String basePath, String studentId) {
        return createUploadUrl(basePath, IMG_TRANSFER_SFZ_Z, studentId);
    }
    public static String createUploadUrlBySFZ_F(String basePath, String studentId) {
        return createUploadUrl(basePath, IMG_TRANSFER_SFZ_F, studentId);
    }
    public static String createUploadUrlByBYZ_Z(String basePath, String studentId) {
        return createUploadUrl(basePath, IMG_TRANSFER_BYZ_Z, studentId);
    }
    public static String createUploadUrlByBYZ_F(String basePath, String studentId) {
        return createUploadUrl(basePath, IMG_TRANSFER_BYZ_F, studentId);
    }
    public static String createUploadUrlByZP(String basePath, String studentId) {
        return createUploadUrl(basePath, IMG_TRANSFER_ZP, studentId);
    }
    public static String createUploadUrlByXLZ(String basePath, String studentId) {
        return createUploadUrl(basePath, IMG_TRANSFER_XLZ, studentId);
    }

    /**
     * 生成回调URL
     * @param basePath 网站根路径
     * @param transferUrl 回调地址
     * @param rtid 回调参数
     * @return
     */
    private static String createUploadUrl(String basePath, String transferUrl, String rtid) {
        String callbackUrl = basePath + transferUrl;

        UploadFileParam uploadFileParam = new UploadFileParam();
        uploadFileParam.setUrl(callbackUrl);
        if(rtid != null)
            uploadFileParam.setRtid(rtid);
        String urldata = uploadFileParam.toString();

        return AppConfig.getProperty("file.upload.url.action") + "?formMap.urldata=" + urldata;
    }

}
