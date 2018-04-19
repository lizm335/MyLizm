package com.gzedu.xlims.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1.获取IP地址；2.根据IP获取城市<br/>
 * 功能说明：使用淘宝IP地址库查询，接口文档见http://ip.taobao.com/instructions.php<br/>
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @date 2017年08月04日
 * @version 3.0
 */
public class IPUtils {

	private static Logger log = LoggerFactory.getLogger(IPUtils.class);

    private static final String URL = "http://ip.taobao.com/service/getIpInfo.php";

	/**
	 * 获取IP地址
	 * 
	 * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
    	String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }
        
//        //使用代理，则获取第一个IP地址
//        if(StringUtils.isEmpty(ip) && ip.length() > 15) {
//			if(ip.indexOf(",") > 0) {
//				ip = ip.substring(0, ip.indexOf(","));
//			}
//		}
        
        return ip;
    }

    public static String getAddress(String ip) {
        String address = null;
        try {
            if(isIp(ip)) {
//                Map<String, Object> params = new HashMap<String, Object>();
                String resultJson = HttpClientUtils.doHttpGet(URL + "?ip=" + ip, null, 6000, "UTF-8");
                Map<String, Object> result = GsonUtils.toBean(resultJson, Map.class);
                if (((Double) result.get("code")).intValue() == 0) {
                    Map<String, Object> data = (Map) result.get("data");
                    address = (String) data.get("region") + data.get("city");
                }
            }
            if(StringUtils.isEmpty(address)) {
                address = "无法识别";
            }
        } catch (Exception e) {}
        return address;
    }

    // 判断ip地址
    public static boolean isIp(String ip) {
        String str = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(ip);
        return m.matches();
    }
	
}
