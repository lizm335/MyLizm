package com.gzedu.xlims.webservice;

import com.gzedu.xlims.common.HttpClientUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/26.
 */
public class HttpTest {

    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("gradeSpecialtyId", "168b50717f000001d6adb42bb346b611");
        String result = HttpClientUtils.doHttpPost("http://test.xllms.tt.gzedu.com/interface/signupdata/syncSignupData", params, 3000, "UTF-8");
        System.out.println(result);
    }

}
