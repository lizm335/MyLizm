
package com.ouchgzee.headTeacher.biz;

import com.gzedu.xlims.common.gzedu.SSOUtil;

import java.util.Map;

public class SSOTest {


	/**
	 * 模拟登录
	 */
	public static void main(String[] args) {
		SSOUtil sso = new SSOUtil();
		String p = sso.getSignOnParam("gs001");
		System.err.println("\n" + p);

		String pp = "Y29tLm91Y2hnemVlLmdkemd3LDQ0MDk4MjIwMDAxMjI4NjcwNywxNTA2NjU1ODc5MzIyLEJDMkE2NzQyMUQ2ODkyMzBEMDYwMDc0QTkxNTgxM0JD";
		Map<String, String> param = sso.parseSignOnParam(pp); // 解析参数
		System.err.println("\n" + param);
	}

}
