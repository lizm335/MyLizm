/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.biz.dao;

import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.ouchgzee.headTeacher.dao.account.GjtUserAccountDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明： 角色用户测试用例
 * 
 * @author liming
 * @Date 2016年4月28日
 * @version 1.0
 *
 */
@Transactional(value="transactionManagerBzr")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class TblPriUserRoleTest {

	@Autowired
	private GjtUserAccountDao gjtUserAccountDao;

	@Test
	public void queryRole() {
		Criteria<BzrGjtUserAccount> spec = new Criteria();
		spec.setJoinType("priRoleInfo.priModelInfos", JoinType.LEFT);
		spec.setJoinType("priRoleInfo.priModelInfos.priOperateInfos", JoinType.LEFT);

		spec.add(Restrictions.eq("loginAccount", "admin", true));
		spec.add(Restrictions.eq("priRoleInfo.priModelInfos.modelId", "3ff42bb542a718c1c597d5a6cc488d91", true));

		List<String> operateList = new ArrayList<String>();
		operateList.add("34818da4ac10a5730194b85fe66f1922");
		operateList.add("34803b6aac10a5730194b85f743cc1b3");
		spec.add(Restrictions.in("priRoleInfo.priModelInfos.priOperateInfos.operateId", operateList, true));

		List<BzrGjtUserAccount> findAll = gjtUserAccountDao.findAll(spec);

		System.out.println(findAll.size());
	}

}
