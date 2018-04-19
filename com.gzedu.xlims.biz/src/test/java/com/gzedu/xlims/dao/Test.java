
package com.gzedu.xlims.dao;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.usermanage.GjtEmployeeInfoDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class Test {

	public static void main(String[] args) {
		System.out.println("2");
		System.out.println(1);
		System.out.println("4");
		System.out.println("5");
		System.out.println("6");
	}

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;

	@Autowired
	private GjtEmployeeInfoDao gjtEmployeeInfoDao;

	@Autowired
	private GjtUserAccountDao userDao;

	@Transactional
	@org.junit.Test
	public void page() {

		GjtUserAccount user = gjtUserAccountService.queryByLoginAccount("admin");
		System.out.println(user.getLoginAccount());
	}

}
