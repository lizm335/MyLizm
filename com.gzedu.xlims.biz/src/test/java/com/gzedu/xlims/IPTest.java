package com.gzedu.xlims;

import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.IPUtils;
import org.junit.Test;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 根据IP获取城市<br/>
 * 功能说明：使用淘宝IP地址库查询，接口文档见http://ip.taobao.com/instructions.php<br/>
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @date 2017年08月04日
 * @version 3.0
 */
public class IPTest {

    private static final String URL = "http://ip.taobao.com/service/getIpInfo.php";

    @Test
    public void example() throws Exception {
        String ip = "120.197.20.159";
        String resultJson = HttpClientUtils.doHttpGet(URL + "?ip="+ip, null, 6000, "UTF-8");
        Map<String, Object> result = GsonUtils.toBean(resultJson, Map.class);
        if(((Double) result.get("code")).intValue() == 0) {
            Map<String, Object> data = (Map) result.get("data");
            for (Entry<String, Object> e : data.entrySet()) {
                System.out.println(e.getKey() + "-" + e.getValue());
            }
        }
    }

    @Test
    public void example2() throws Exception {
        System.out.println(IPUtils.getAddress("120.197.20.159"));
    }

}
