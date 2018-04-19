/**
 * Copyright(c) 2017 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.gzedu.SSOUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 功能说明：
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年11月21日
 * @version 3.0
 *
 */
public class SSOTest {
	
	public static void main(String[] args) {
		SSOUtil sso = new SSOUtil();
		String p = sso.getSignOnParam("ebc806cd61fb4982b3e54407662ee08b"); // 加密
		System.out.println(p);
		try {
			System.out.println(URLEncoder.encode("/wx/weixin/faq/teacher/faq_list.html", Constants.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        try {
			String encode = URLDecoder.decode("%7B%22message%22%3A%22ok%22%2C%22nu%22%3A%22580401101697%22%2C%22ischeck%22%3A%220%22%2C%22condition%22%3A%2200%22%2C%22com%22%3A%22tiantian%22%2C%22status%22%3A%22200%22%2C%22state%22%3A%220%22%2C%22data%22%3A%5B%7B%22time%22%3A%222018-01-14+21%3A34%3A43%22%2C%22ftime%22%3A%222018-01-14+21%3A34%3A43%22%2C%22context%22%3A%22%E8%B9%87%EE%82%A1%E6%AC%A2%E9%90%A2%E8%BE%A8%E6%BE%80%E5%AE%B8%E7%82%BA%E6%B3%A6%E9%8F%81%EF%BD%89%E6%AA%B0%E6%9D%A9%E6%84%AC%E5%BD%82%E5%AF%B0%EF%BF%BD%E7%AE%8D%E5%AE%B8%E7%82%B2%E5%9E%8E%E9%8E%B7%E3%84%A4%E8%85%91%E8%B9%87%EF%BF%BD%2C%22location%22%3A%22%22%7D%2C%7B%22time%22%3A%222018-01-14+21%3A32%3A37%22%2C%22ftime%22%3A%222018-01-14+21%3A32%3A37%22%2C%22context%22%3A%22%E8%B9%87%EE%82%A1%E6%AC%A2%E9%8D%92%E6%8B%8C%E6%8F%AA%E9%8F%89%EE%85%9E%E7%AA%9E%E9%97%86%E5%97%98%E6%9A%8E%E9%97%84%E5%97%9A%E7%B9%8D%E9%94%9B%E5%B1%BC%E7%AC%82%E6%B6%93%EF%BF%BD%E7%8F%AF%E9%8F%84%EE%88%9B%E6%A3%A4%E9%94%9B%E5%B1%BE%E5%A3%82%E9%8E%BB%E5%BF%93%E6%86%B3%E9%8F%84%EE%88%9D%E7%B9%98%E9%8D%99%EF%BD%87%E7%B2%8D9%22%2C%22location%22%3A%22%22%7D%2C%7B%22time%22%3A%222018-01-14+20%3A07%3A22%22%2C%22ftime%22%3A%222018-01-14+20%3A07%3A22%22%2C%22context%22%3A%22%E8%B9%87%EE%82%A1%E6%AC%A2%E9%90%A2%E8%BE%AB%E6%83%82%E7%81%9E%E5%8F%98%E7%AC%A2%E9%8D%96%E5%93%84%E5%9E%8E%E9%8D%8F%EE%84%80%E5%BE%83%280571-83869771%29%E9%8D%99%E6%88%9D%E7%B7%9A%E9%8F%89%EE%85%9E%E7%AA%9E%E9%97%86%E5%97%98%E6%9A%8E%22%2C%22location%22%3A%22%22%7D%2C%7B%22time%22%3A%222018-01-14+19%3A25%3A49%22%2C%22ftime%22%3A%222018-01-14+19%3A25%3A49%22%2C%22context%22%3A%22%E8%B9%87%EE%82%A1%E6%AC%A2%E9%90%A2%E8%BE%AB%E6%83%82%E7%81%9E%E5%8F%98%E7%AC%A2%E9%8D%96%E5%93%84%E5%9E%8E%E9%8D%8F%EE%84%80%E5%BE%83%280571-83869771%29%E9%8D%99%E6%88%9D%E7%B7%9A%E9%8F%89%EE%85%9E%E7%AA%9E%E9%97%86%E5%97%98%E6%9A%8E%22%2C%22location%22%3A%22%22%7D%2C%7B%22time%22%3A%222018-01-14+19%3A25%3A48%22%2C%22ftime%22%3A%222018-01-14+19%3A25%3A48%22%2C%22context%22%3A%22%E9%92%80%D1%83%E5%8C%97%E6%B6%93%E6%BB%83%E5%B0%AF%E9%8D%92%E5%97%97%E5%8F%95%E9%8D%99%EF%BF%BD0571-83869771%29%E5%AE%B8%E8%8C%B6%E7%B9%98%E7%90%9B%E5%B2%83%EE%97%96%E7%90%9A%E5%AC%AB%E5%A3%82%E9%8E%BB%EF%BF%BD%2C%22location%22%3A%22%22%7D%2C%7B%22time%22%3A%222018-01-14+17%3A26%3A52%22%2C%22ftime%22%3A%222018-01-14+17%3A26%3A52%22%2C%22context%22%3A%22%E9%92%80%D1%83%E5%8C%97%E6%B6%93%E6%BB%83%E5%B0%AF%E9%8D%92%E5%97%97%E5%8F%95%E9%8D%99%EF%BF%BD0571-83869771%29%E9%90%A8%E5%8B%AB%E7%AB%B6%E9%8D%A6%E6%B4%AA%E5%84%B4%E6%B6%93%EF%BF%BD%E5%87%A1%E9%8F%80%E6%9C%B5%E6%AC%A2%E9%94%9B%E5%B1%BE%E5%A3%82%E9%8E%BB%E5%BF%93%E6%86%B3%E9%8F%84%EE%88%99%E7%B9%9B%E5%AF%A4%E5%93%84%E7%B9%9D%22%2C%22location%22%3A%22%22%7D%5D%7D", Constants.CHARSET);
            System.out.println(encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
	}

}
