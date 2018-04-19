package com.gzedu.xlims;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.gzedu.SignUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 招生平台-缴费支付接口
 * Created by Administrator on 2017/10/12.
 */
public class ZhaoshengSignTest {

    private static final Logger log = LoggerFactory.getLogger(ZhaoshengSignTest.class);

    private static final String URL = "http://eapi.tt.oucnet.com/orderapi/payment.html";

    @Test
    public void example() throws Exception {
        try {
            // 接口编号  :  0000000229  缴费支付接口
            Map<String, String> params = new HashMap<String, String>();
            params.put("order_sn", "201710114070961"); // 订单号
            params.put("pay_sn", "201710120000005"); // 缴费单号,15位字符,由调用方生成
            params.put("title", "补考费-中国特色社会主义理论体系概论"); // 缴费标题
            params.put("price", "0.02"); // 缴费金额

            params.put("type", "6"); // 缴费类型 转专业差价-4 考试费-6 教材费-7
            params.put("notify_", "http://test.study.tt.gzedu.com/pay/examFeeNotify"); // 异步回调地址
            params.put("return_", "http://test.study.tt.gzedu.com/pay/examFeeReturn"); // 同步回调地址

            // 额外参数;签名
            long time = DateUtils.getDate().getTime();
//            params.put("school_code", Objects.toString("041", ""));	// 院校code，非必填
            params.put("sign", SignUtil.formatUrlMap(params, time));
            params.put("appid", SignUtil.APPID);// APPID不需要参与加密
            params.put("time", String.valueOf(time));

            // 链接需要encode一次
            params.put("notify_", URLEncoder.encode("http://test.study.tt.gzedu.com/pay/examFeeNotify", Constants.CHARSET)); // 异步回调地址
            params.put("return_", URLEncoder.encode("http://test.study.tt.gzedu.com/pay/examFeeReturn", Constants.CHARSET)); // 同步回调地址

            StringBuffer paramsJoin = new StringBuffer();
            for (Map.Entry<String, String> item : params.entrySet()) {
                if (org.apache.commons.lang.StringUtils.isNotEmpty(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    paramsJoin.append(key).append("=").append(val).append("&");
                }
            }
            paramsJoin.setLength(paramsJoin.length() - 1);
            log.info("支付接口请求：{}?{}", URL, paramsJoin);
            /*String result = HttpClientUtils.doHttpGet(URL, params, 3000, "UTF-8");
            if (StringUtils.isNotEmpty(result)) {
                JSONObject json = JSONObject.fromObject(result);
                log.info("审核反馈结果：{}", json);
                int bo = (Integer) json.get("status");
                if (bo == 0) {
                    System.err.println("招生平台接口发生异常！错误信息：" + json.get("msg"));
                }
            }*/
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.err.println("招生平台接口发生异常！");
        }
    }

    @Test
    public void test() throws Exception {
        System.err.println(codeGenerator("xx", 12));
    }

    public static String codeGenerator(String type, int digit) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        long rs = 12;
        String index = String.valueOf(rs);
        String zero = "";
        for (int i = 0; i < digit - date.length() - index.length(); i++) {
            zero += "0";
        }
        return date + zero + index;
    }

}
