package com.gzedu.xlims.web.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

import com.gzedu.xlims.common.EeSmsUtils;
import com.gzedu.xlims.common.JsonState;
import com.gzedu.xlims.common.gzedu.EmptyUtils;

public class ActionCacheLogKit {
	 /** 缓存标识 */
    private final static String SMS_CHACHE_SIGN = "organization_sms_";
    /** 缓存时间 15分钟 */
    private final static int    SMS_CHACHE_TIME = 900;

    /**
     * 保存操作日志到mongodb
     * 
     * @param operation
     *            保存到mongodb的日志对象
     * @return 保存结果(1:成功,0:失败)
     */
//    public static int saveToMongo(MongoOperation operation) {
//        int result = 0;
//        try {
//            operation.setCreateDt(DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
//
//            Map dbmap = BeanUtils.describe(operation);
//            dbmap.remove("class");
//            result = MongoUtil.addMongoDB(dbmap, "EXAM_OPERATION_LOG");
//        } catch (Exception e) {
//            LoggerUtil.error(operation.toString(), "error", e);
//            e.printStackTrace();
//        }
//        
//        return result;
//    }
    
    /**
     * 根据APP_ID MOBILE tparams模板参数(REAL_NAME)
     * 
     * @param param
     * @return 短信验证码
     */
    public static Map getSmsCode(Map param) {
        Map error = new HashMap();
        error.put("state", JsonState.ERROR.getKey());
        String phone = ObjectUtils.toString(param.get("MOBILE"));
        // 校验参数
        if (EmptyUtils.isEmpty(param.get("APP_ID"))) {
            error.put("msg", "appid为空 找不到模板");
            return error;
        }
        if (EmptyUtils.isEmpty(phone)) {
            error.put("msg", "手机号不能为空");
            return error;
        }
        
        try {
            Map successMap = EeSmsUtils.getSmsCode(param, ObjectUtils.toString(param.get("REAL_NAME")));
            if (EmptyUtils.isNotEmpty(successMap)) {// 发送成功 
                if (JsonState.SUCCESS.getKey().equals(ObjectUtils.toString(successMap.get("result")))) {
                    successMap.put("state", JsonState.SUCCESS.getKey());
//                    XmemCachedManager.replaceMemCached(SMS_CHACHE_SIGN + phone, ObjectUtils.toString(successMap.get("code")), SMS_CHACHE_TIME);
                    return successMap;
                }
            }
            error.put("msg", "发送短信验证失败");
            return error;
        } catch (Exception e) {
            e.printStackTrace();
            error.put("msg", "发送短信验证异常: " + e);
            return error;
        }
    }

    /** 根据手机号 获取缓存中的验证码值 判断 */
//    public static boolean checkSmsCode(String phone) {
//        String code = (String) XmemCachedManager.getMemCachedValue(SMS_CHACHE_SIGN + phone, "");
//        if (EmptyUtils.isEmpty(code) || "null".equals(code)) {
//            return false;
//        } else {
//            return true;
//        }
//    }
}
