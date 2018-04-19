/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.biz.employee;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.service.employee.BzrGjtEmployeeInfoService;

/**
 * 
 * 功能说明：
 * @author 李建华 lijianhua@gzedu.net
 * @Date 2016年5月10日
 * @version 2.5
 * @since JDK 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })

public class GjtEmployeeInfoServiceTest {

	@Autowired
	private BzrGjtEmployeeInfoService gjtEmployeeInfoService;
	
	
	public void updateTest(){
		BzrGjtEmployeeInfo gjtEmployeeInfo = gjtEmployeeInfoService.queryById("000b26161ecc43c498ac1a4b61ed14fb");
		gjtEmployeeInfo.setXm("管理学院-刘凤绮1");
		gjtEmployeeInfo.setSjh("13800138000");
		boolean result = gjtEmployeeInfoService.update(gjtEmployeeInfo);
		Assert.isTrue(result);
	}
	
	
	public void updatePassword(){
		String password="123456";
		String accountId="311f08193ad14024878c4d54cdefb58b";
		int result = gjtEmployeeInfoService.updatePassword(accountId, password, "888888", "admin");
		Assert.isTrue(result==1);
	}
	
	@Test
	public void updatePhoto(){
		String employeeId="000b26161ecc43c498ac1a4b61ed14fb";
		String photoUrl="http://eforge.gzedu.com/upload/gjtcenter/201303/879f4ea4ac1086a759465946a1031858.jpg";
		boolean result = gjtEmployeeInfoService.updatePhoto(employeeId, photoUrl, "admin");
		Assert.isTrue(result);
	}
}
