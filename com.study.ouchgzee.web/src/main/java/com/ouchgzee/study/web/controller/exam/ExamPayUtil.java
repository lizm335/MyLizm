package com.ouchgzee.study.web.controller.exam;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.gzedu.SignUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class ExamPayUtil {

    /**
     * 获取补考费支付链接
     * 接口编号  :  0000000229  缴费支付接口
     * @return
     */
    protected static String getExamPayUrl(Map<String, String> params, String schoolCode) {
        // 接口编号  :  0000000229  缴费支付接口
        params.put("type", "6"); // 缴费类型 转专业差价-4 考试费-6 教材费-7

        // 额外参数;签名
        long time = DateUtils.getDate().getTime();
        if(StringUtils.isNotBlank(schoolCode)) {
            params.put("school_code", schoolCode);    // 院校code，非必填
        }
        params.put("sign", SignUtil.formatUrlMap(params, time));
        params.put("appid", SignUtil.APPID);// APPID不需要参与加密
        params.put("time", String.valueOf(time));

        try {
            // 链接需要encode一次
            params.put("return_", URLEncoder.encode(ObjectUtils.toString(params.get("return_")), Constants.CHARSET)); // 同步回调地址
            params.put("notify_", URLEncoder.encode(ObjectUtils.toString(params.get("notify_")), Constants.CHARSET)); // 异步回调地址
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuffer paramsJoin = new StringBuffer();
        for (Map.Entry<String, String> item : params.entrySet()) {
            if (org.apache.commons.lang.StringUtils.isNotEmpty(item.getKey())) {
                String key = item.getKey();
                String val = item.getValue();
                paramsJoin.append(key).append("=").append(val).append("&");
            }
        }
        paramsJoin.setLength(paramsJoin.length() - 1);
        return String.format("%s?%s",
                ObjectUtils.toString(AppConfig.getProperty("payHost")),
                paramsJoin);
    }

}
