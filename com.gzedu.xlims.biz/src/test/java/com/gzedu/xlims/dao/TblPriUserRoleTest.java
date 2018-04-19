/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.menue.TblPriRoleDao;
import com.gzedu.xlims.dao.menue.TblPriRoleSpecs;
import com.gzedu.xlims.dao.model.PriModelInfoDao;
import com.gzedu.xlims.dao.model.PriRoleInfoDao;
import com.gzedu.xlims.dao.model.PriRoleModelDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.data.RandomTestData;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriModelInfo;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.pojo.PriRoleModel;
import com.gzedu.xlims.pojo.TblPriRole;

/**
 * 功能说明： 角色用户测试用例
 * 
 * @author liming
 * @Date 2016年4月28日
 * @version 1.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class TblPriUserRoleTest {
	@Autowired
	private TblPriRoleDao tblPriRoleDao;

	@Autowired
	PriRoleInfoDao roleInfoDao;

	@Autowired
	PriRoleModelDao roleModelDao;

	@Autowired
	private GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	private PriModelInfoDao modelInfoDao;

	@Test
	public void addRoleModel() {
		GjtUserAccount userAccount = gjtUserAccountDao.findByLoginAccount("admin");
		PriRoleInfo roleInfo = userAccount.getPriRoleInfo();
		List<PriModelInfo> allModel = modelInfoDao.findAll();
		roleModelDao.delete(roleModelDao.findByRoleId(roleInfo.getRoleId()));

		List<PriRoleModel> roleModels = new ArrayList<PriRoleModel>();
		// 给角色分配菜单权限
		for (PriModelInfo priModelInfo : allModel) {
			PriRoleModel roleModel = new PriRoleModel();
			roleModel.setRoleModelId(UUIDUtils.random());
			roleModel.setModelId(priModelInfo.getModelId());
			roleModel.setRoleId(roleInfo.getRoleId());
			roleModels.add(roleModel);
		}
		roleModelDao.save(roleModels);
	}

	@Test
	public void queryRole() {
		// GjtUserAccount userAccount =
		// gjtUserAccountDao.findByLoginAccount("admin");
		// PriRoleInfo roleInfo = userAccount.getPriRoleInfo();

		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("loginAccount", new SearchFilter("loginAccount", SearchFilter.Operator.EQ, "admin"));
		filters.put("priRoleInfo.priModelInfos.modelId", new SearchFilter("priRoleInfo.priModelInfos.modelId",
				SearchFilter.Operator.EQ, "3ff42bb542a718c1c597d5a6cc488d91"));
		filters.put("priRoleInfo.priModelInfos.priOperateInfos.operateId",
				new SearchFilter("priRoleInfo.priModelInfos.priOperateInfos.operateId", SearchFilter.Operator.EQ,
						"34818da4ac10a5730194b85fe66f1922"));

		Specification<GjtUserAccount> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtUserAccount.class);

		List<GjtUserAccount> findAll = gjtUserAccountDao.findAll(spec);

		System.out.println(findAll.size());
	}

	@Test
	public void createUserAndRole() {
		TblPriRole role = RandomTestData.TblPriRole();
		role.setRoleId(UUID.randomUUID().toString());
		role = tblPriRoleDao.save(role);

		GjtUserAccount user = RandomTestData.GjtUserAccount();
		user.setId(UUID.randomUUID().toString());
		user = gjtUserAccountDao.save(user);

		List<GjtUserAccount> users = role.getGjtUserAccountList();
		if (users == null) {
			users = new ArrayList<GjtUserAccount>();
		}
		users.add(user);
		tblPriRoleDao.save(role);
		System.out.println(role.getGjtUserAccountList().size());
	}

	@Test
	public void query() {
		List<TblPriRole> list = tblPriRoleDao.findAll();
		System.out.println(list.size());
		for (TblPriRole tblPriRole : list) {
			System.out.println(tblPriRole.getRoleName());
			System.out.println(tblPriRole.getGjtUserAccountList().get(0).getRealName());
		}
	}

	@Test
	public void query2() {
		GjtUserAccount user = new GjtUserAccount();
		user.setId("2ee0e229bf7144009f842dbb7ce14592");
		List<TblPriRole> list = tblPriRoleDao.findAll(TblPriRoleSpecs.findByUser(user));
		Assert.assertEquals(1, list.size());

		for (TblPriRole tblPriRole : list) {
			Assert.assertEquals("教学点管理员", tblPriRole.getRoleName());
		}
	}
}
