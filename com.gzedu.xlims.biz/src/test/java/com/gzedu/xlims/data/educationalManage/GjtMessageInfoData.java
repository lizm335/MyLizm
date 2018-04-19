/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.data.educationalManage;

import java.math.BigDecimal;

import com.gzedu.xlims.pojo.GjtMenu;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.message.GjtMessageInfo;

public class GjtMessageInfoData {

	public static GjtMessageInfo setGjtMessageInfo() {
		GjtMessageInfo entity = new GjtMessageInfo();

		entity.setInfoTheme("消息标题1");
		entity.setInfoType("1");//1-系统消息 2-教务通知 11-班级公告 12-考试通知 13-学习提醒 具体查看数据字典
		//接收者身份(1学生 2学习中心管理员  3院系管理员 具体参考角色定义表)
		entity.setGetUserRole("1");
		//接收者类型(6:'全院', 3:'专业', 4:'年级', 5:'专业+年级',7:'层次' )
		entity.setGetUserMethod("6");
		//接收者(3:'专业', 4:'年级', 5:'专业+年级',6:'学院id',7:'层次')
		entity.setGetUser("3");
		entity.setPutUser("zhy");// 发送者
		
		GjtSchoolInfo gjtSchoolInfo = new GjtSchoolInfo();
		gjtSchoolInfo.setId("28949de42f0244b9a70d9c8e06c6cfd4");
		//entity.setGjtSchoolInfo(gjtSchoolInfo);// 院校
		entity.setCreatedBy("zhy");//创建人
		entity.setIsEnabled("1");//是否有效
		entity.setInfoContent("消息测试");//内容
		
		entity.setFileName("考试");//获得附件名称
		entity.setFileUrl("http://www.baidu.com");//// 获得上传路径
		

		return entity;
	}
}
