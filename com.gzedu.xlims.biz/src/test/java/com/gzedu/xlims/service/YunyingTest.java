package com.gzedu.xlims.service;

import java.util.HashMap;
import java.util.Map;

import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.UUIDUtils;

/**
 * Created by Administrator on 2017/8/24.
 */
public class YunyingTest {

    private static String YUNYING_SERVER = "http://usercenter.t.oucnet.cn";

    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "张三");
        params.put("id_card", "441823198703230455");
        params.put("phone", "18011837655");
        params.put("account", "441823198703230455");
        params.put("atid", UUIDUtils.random());
        params.put("source", "education");

        System.out.println(params);
//        String s = HttpClientUtils.doHttpPost(YUNYING_SERVER + "/receive/studentNew", params, 6000, Constants.CHARSET);
        System.out.println(GsonUtils.toJson(null));
    }

}
