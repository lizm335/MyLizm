/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web;

import com.gzedu.xlims.common.UUIDUtils;

/**
 * 功能说明：生成数据字典SQL
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月22日
 * @version 2.5
 *
 */
public class InsertDictionary {

	public static void main(String[] args) {
		inserWorkOrderType();
	}

	public static void insertEnroll() {
		String str = "辅导教师，督导教师，总校管理员，学位英语";
		String Type_Code = "CourseType";
		String Description = "课程类型";

		String[] list = str.split("，");

		for (int i = 0; i < list.length; i++) {
			String sql = "insert into TBL_SYS_DATA(id,Type_Code,Type_Name,Name,code,Description,Ord_No,Is_Deleted,Created_Dt,Version)"
					+ "values('" + UUIDUtils.random().toString() + "','" + Type_Code + "','" + (i) + "','" + list[i]
					+ "','" + (i) + "','" + Description + "','" + (10 + i) + "','N',sysdate,'1');";
			System.out.println(sql);
		}
	}

	public static void inserWorkOrderType() {
		String str = "学员，辅导员，教务，教学，学生事务，院长";
		String Type_Code = "GetScopeObject";
		String Description = "接受范围对象";

		String[] list = str.split("，");

		for (int i = 0; i < list.length; i++) {
			String sql = "insert into TBL_SYS_DATA(id,Type_Code,Type_Name,Name,code,Description,Ord_No,Is_Deleted,Created_Dt,Version)";
			System.out.println(sql);
			String sql2 = "values('" + UUIDUtils.random().toString() + "','" + Type_Code + "','" + Description + "','"
					+ list[i] + "','" + (1 + i) + "','" + Description + "','" + (1 + i) + "','N',sysdate,'1');";
			System.out.println(sql2);
		}
	}

	public static void insertWorkOrderRole() {
		String str = "院长，学支管理员，招生管理员，学籍管理员，教学管理员，教材管理员，考务管理员，毕业管理员，运营管理员，学习中心管理员，招生服务站管理员，班主任";
		String Type_Code = "AnswerTranspondRole";
		String Description = "答疑转发角色";

		String[] list = str.split("，");

		for (int i = 0; i < list.length; i++) {
			String sql = "insert into TBL_SYS_DATA(id,Type_Code,Type_Name,Name,code,Description,Ord_No,Is_Deleted,Created_Dt,Version)";
			System.out.println(sql);
			String sql2 = "values('" + UUIDUtils.random().toString() + "','" + Type_Code + "','" + i + "','" + list[i]
					+ "','" + i + "','" + Description + "','" + (10 + i) + "','N',sysdate,'1');";
			System.out.println(sql2);
		}
	}

	public static void insertStudyService() {
		String str = "招生服务，学习支持服务，课程定制服务，课程教学辅导服务，教材服务，双元教学服务";
		String Type_Code = "StudyServiceInfo";
		String Description = "学习中心服务项";

		String[] list = str.split("，");

		for (int i = 0; i < list.length; i++) {
			String sql = "insert into TBL_SYS_DATA(id,Type_Code,Type_Name,Name,code,Description,Ord_No,Is_Deleted,Created_Dt,Version)";
			System.out.println(sql);
			String sql2 = "values('" + UUIDUtils.random().toString() + "','" + Type_Code + "','" + (i + 1) + "','"
					+ list[i] + "','" + (i + 1) + "','" + Description + "','" + (10 + i) + "','N',sysdate,'1');";
			System.out.println(sql2);

		}
	}

}
