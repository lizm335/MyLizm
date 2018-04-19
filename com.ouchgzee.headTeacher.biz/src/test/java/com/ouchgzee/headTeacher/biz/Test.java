
package com.ouchgzee.headTeacher.biz;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.HttpClientUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {
//		StringBuilder sbd = new StringBuilder();
//		sbd.append("gk2015002");
//		sbd.append(",");
//		sbd.append("687ffde92b27491aa8bceeede0813425");
//		sbd.append(",");
//		sbd.append(new Date().getTime());
//
//		System.err.println("要准备加密的字符串：" + sbd.toString());
//		String en = EncryptUtils.encrypt(sbd.toString());
//		System.err.println("加密结果：" + en);
//		System.err.println("解密结果：" + EncryptUtils.decrypt(en));



		Map params = new HashMap();
		params.put("formMap.PROJECT_CODE", Constants.PROJECT_CODE);
		//params.put("formMap.USER_ACCOUNT", userAccount);
		//params.put("formMap.USER_NAME", userName);

		String rsp = HttpClientUtils.doHttpGet("http://eomp.oucnet.cn/interface/erpInvoiceInterface/queryInvoice.do", params, Constants.TIMEOUT, Constants.CHARSET_GBK);

		if (StringUtils.isNotBlank(rsp)) {
			Gson gson = new GsonBuilder().create();
			Map map = gson.fromJson(rsp, HashMap.class);
			String result = ObjectUtils.toString(map.get("result"));
			if (StringUtils.equals("1", result)) {
				List<Map> invoiceList = (List<Map>) map.get("dataList");
				for (Map i : invoiceList) {
					System.err.println(i);
				}
			} else {
				System.err.println("==> 发票查询失败:"+ map.get("msg"));
			}
		}


		URL basePath = (new Test()).getClass().getResource("/template");
		System.out.println(basePath);
		System.out.println(basePath.getFile());


		// 日期格式yyyy-MM-dd
		Pattern pattern = Pattern.compile("^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])$");
		Matcher matcher = pattern.matcher("1993-07-08");
		System.out.println(matcher.matches());
	}

}
