/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.webservice.controller.gjt.message.vo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 功能说明：通知类型配置
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年6月12日
 * @version 2.5
 *
 */
public class MessageUserRoleMap {

	/**
	 * 接受者角色,相当于字典信息
	 */
	public final static Map<String, String> getUserRoleMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("7c0d7c8a39f315d610377458894050ae", "教务管理员");
			put("03799e0b9fa7d6fe56c548df0cc3b150", "考务管理员");
			put("449c71cbac1082b468a5941fbba4e5d6", "教学管理员");
			put("3f5f7ca336a64c42bc5d3a4c1986289e", "学籍管理员");
			put("409891a89c2e4e0ca12381e207e3d9bb", "教材管理员");
			put("97e31c2c70a442208443751fdeede0ff", "毕业管理员");
			put("7ab10371c2f040df8289b09cde4b9510", "学习中心管理员");
			put("fcf94a20da1c44a1a31f94eafaf4b707", "班主任");
			put("be60d336bc1946a5a24f88d5ae594b17", "辅导教师");
			put("699f6f83acf54548bfae7794915a3cf3", "督导教师");
			put("a7b3b1ddc82043e78df9ccdd49cad393", "运营管理员");
			put("9a6f05b3e24d456fb84435dd75e934c2", "学支管理员");
			put("2fbd4bd4099e48309aea815fe2a1dde2", "院校管理员");
			put("06141fff2ca84575bd9bc9fd55803b57", "招生点主任");
			put("student", "学员");
		}
	};

	/**
	 * 根据不同的roleId去不同的发送对象
	 * 
	 * @param roleId
	 * @return
	 */
	public static Map<String, String> getUserRoleMap(String roleId) {
		Map<String, String> map = new LinkedHashMap<String, String>();

		// 以下角色只发学生
		boolean bool = "a06d56ed7fe44b30811475968708703f".equals(roleId) || // 学习中心招生管理员
				"3701c78f920c4cd79165e12621267bc7".equals(roleId) || // 招生点招生管理员
				"06141fff2ca84575bd9bc9fd55803b57".equals(roleId) || // 招生点主任
				"7ab10371c2f040df8289b09cde4b9510".equals(roleId) || // 学习中心学支管理员
				"a87f2558fb814b20949a402b03c7ea4c".equals(roleId) || // 学习中心主任
				"a91c6d77661840ada0be7b82096c575d".equals(roleId);// 学习中心教学管理员

		if ("d4b27a66c0a87b010120da231915c223".equals(roleId)) {// 院长
			map.put("student", "学员");
			map.put("fcf94a20da1c44a1a31f94eafaf4b707", "班主任");
			map.put("7ab10371c2f040df8289b09cde4b9510", "学习中心管理员");
			map.put("449c71cbac1082b468a5941fbba4e5d6", "教学管理员");
			map.put("7c0d7c8a39f315d610377458894050ae", "教务管理员");
			map.put("2fbd4bd4099e48309aea815fe2a1dde2", "院校管理员");
			map.put("3f5f7ca336a64c42bc5d3a4c1986289e", "学籍管理员");
			map.put("409891a89c2e4e0ca12381e207e3d9bb", "教材管理员");
			map.put("97e31c2c70a442208443751fdeede0ff", "毕业管理员");
			map.put("a7b3b1ddc82043e78df9ccdd49cad393", "运营管理员");
			map.put("be60d336bc1946a5a24f88d5ae594b17", "辅导教师");
			map.put("699f6f83acf54548bfae7794915a3cf3", "督导教师");
			map.put("9a6f05b3e24d456fb84435dd75e934c2", "学支管理员");
			map.put("06141fff2ca84575bd9bc9fd55803b57", "招生点主任");
		} else if ("2fbd4bd4099e48309aea815fe2a1dde2".equals(roleId)) {// 院校管理员
			map.put("student", "学员");
			map.put("fcf94a20da1c44a1a31f94eafaf4b707", "班主任");
			map.put("7ab10371c2f040df8289b09cde4b9510", "学习中心管理员");
			map.put("449c71cbac1082b468a5941fbba4e5d6", "教学管理员");
			map.put("7c0d7c8a39f315d610377458894050ae", "教务管理员");
			map.put("3f5f7ca336a64c42bc5d3a4c1986289e", "学籍管理员");
			map.put("409891a89c2e4e0ca12381e207e3d9bb", "教材管理员");
			map.put("97e31c2c70a442208443751fdeede0ff", "毕业管理员");
			map.put("a7b3b1ddc82043e78df9ccdd49cad393", "运营管理员");
			map.put("be60d336bc1946a5a24f88d5ae594b17", "辅导教师");
			map.put("699f6f83acf54548bfae7794915a3cf3", "督导教师");
			map.put("9a6f05b3e24d456fb84435dd75e934c2", "学支管理员");
			map.put("06141fff2ca84575bd9bc9fd55803b57", "招生点主任");
		} else if ("bc2d0037e4d44f64b01f7f0d3ad47eb9".equals(roleId)) {// 院校管理员(教辅云)
			map.put("student", "学员");
			map.put("fcf94a20da1c44a1a31f94eafaf4b707", "班主任");
			map.put("be60d336bc1946a5a24f88d5ae594b17", "辅导教师");
			map.put("699f6f83acf54548bfae7794915a3cf3", "督导教师");
		} else if (bool) {
			map.put("student", "学员");
		} else {// 其他角色 除去自身
			map.put("student", "学员");
			map.put("fcf94a20da1c44a1a31f94eafaf4b707", "班主任");
			map.put("7ab10371c2f040df8289b09cde4b9510", "学习中心管理员");
			map.put("449c71cbac1082b468a5941fbba4e5d6", "教学管理员");
			map.put("03799e0b9fa7d6fe56c548df0cc3b150", "考务管理员");
			map.put("7c0d7c8a39f315d610377458894050ae", "教务管理员");
			map.put("3f5f7ca336a64c42bc5d3a4c1986289e", "学籍管理员");
			map.put("409891a89c2e4e0ca12381e207e3d9bb", "教材管理员");
			map.put("97e31c2c70a442208443751fdeede0ff", "毕业管理员");
			map.put("a7b3b1ddc82043e78df9ccdd49cad393", "运营管理员");
			map.put("be60d336bc1946a5a24f88d5ae594b17", "辅导教师");
			map.put("699f6f83acf54548bfae7794915a3cf3", "督导教师");
			map.put("9a6f05b3e24d456fb84435dd75e934c2", "学支管理员");
			map.put("06141fff2ca84575bd9bc9fd55803b57", "招生点主任");

			map.remove(roleId);
		}

		return map;
	}

	/**
	 * 用来除去本身的角色类型通知 key=roleId;value=InfoType的Code；用于自己发送自己角色对应的通知类型
	 */
	public final static Map<String, String> benshenMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("7c0d7c8a39f315d610377458894050ae", "2");// "教务通知"
			put("03799e0b9fa7d6fe56c548df0cc3b150", "14");// "考务通知"
			put("449c71cbac1082b468a5941fbba4e5d6", "15");// "教学通知"
			put("3f5f7ca336a64c42bc5d3a4c1986289e", "16");// "学籍通知"
			put("409891a89c2e4e0ca12381e207e3d9bb", "17");// "教材通知"
			put("97e31c2c70a442208443751fdeede0ff", "18");// "毕业通知"
			put("7ab10371c2f040df8289b09cde4b9510", "19");// "学习中心通知"
			put("9a6f05b3e24d456fb84435dd75e934c2", "20");// "学支通知"
			put("a7b3b1ddc82043e78df9ccdd49cad393", "22");// "运营通知"
			put("bc2d0037e4d44f64b01f7f0d3ad47eb9", "23");// "院校通知"

			put("a06d56ed7fe44b30811475968708703f", "19");// "学习中心通知"
			put("3701c78f920c4cd79165e12621267bc7", "19");// "学习中心通知"
			put("06141fff2ca84575bd9bc9fd55803b57", "19");// "学习中心通知"
			put("a87f2558fb814b20949a402b03c7ea4c", "19");// "学习中心通知"
			put("a91c6d77661840ada0be7b82096c575d", "19");// "学习中心通知"

		}
	};

	/**
	 * 院校管理员已经系统管理员；用于通知类型里面的特殊角色
	 */
	public final static Map<String, String> guanliyuanMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("d2b2aa26c02f7bf10d2dda23a91522ab", "系统管理员");
			put("2fbd4bd4099e48309aea815fe2a1dde2", "院校管理员");
			put("d4b27a66c0a87b010120da231915c223", "院长");
		}
	};
}
