/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.common.vo;

import java.util.LinkedHashMap;
import java.util.Map;

import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;

/**
 * 
 * 功能说明：常见问题
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年6月27日
 * @version 2.5
 *
 */
public class OftenTypeRoleMap {

	/**
	 * roleId对应字典类型
	 */
	public final static Map<String, String> roleToTypeMap = new LinkedHashMap<String, String>() {

		private static final long serialVersionUID = 1L;

		{
			put("fcf94a20da1c44a1a31f94eafaf4b707", "1");// 学支问题
			put("be60d336bc1946a5a24f88d5ae594b17", "2");// 学习问题
			put("7c0d7c8a39f315d610377458894050ae", "3");// 教务问题
			put("449c71cbac1082b468a5941fbba4e5d6", "4");// 教学问题
			put("3f5f7ca336a64c42bc5d3a4c1986289e", "5");// 学籍问题
			put("409891a89c2e4e0ca12381e207e3d9bb", "6");// 教材问题
			put("03799e0b9fa7d6fe56c548df0cc3b150", "7");// 考务问题
			put("97e31c2c70a442208443751fdeede0ff", "8");// 毕业问题
			put("7ab10371c2f040df8289b09cde4b9510", "9");// 学习中心问题
			put("5f2034cdd64840e5946f72b5ab3a0ffb", "10");// 招生问题
			put("a7b3b1ddc82043e78df9ccdd49cad393", "11");// 运营问题
			put("9a6f05b3e24d456fb84435dd75e934c2", "12");// 学院问题

		}
	};

	public static String getTypeByRoleId(String roleId) {
		Map<String, String> roletotypemap2 = OftenTypeRoleMap.roleToTypeMap;
		String type = roletotypemap2.get(roleId);
		if (StringUtils.isBlank(type)) {
			type = "13";// 其他问题
		}
		return type;
	}

}
