/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.biz.dao;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.menu.SystemName;
import com.ouchgzee.headTeacher.dao.model.*;
import com.ouchgzee.headTeacher.dao.account.GjtUserAccountDao;
import com.ouchgzee.headTeacher.pojo.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月6日
 * @version 2.5
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class PriModelInfoDaoTest {
	@Autowired
	PriModelInfoDao modelInfoDao;

	@Autowired
	PriOperateInfoDao operateInfoDao;

	@Autowired
	PriModelOperateDao modelOperateDao;

	@Autowired
	PriRoleOperateDao priRoleOperateDao;

	@Autowired
	GjtUserAccountDao userAccountDao;

	@Autowired
	PriRoleInfoDao roleDao;

	@PersistenceContext(unitName = "entityManagerFactoryBzr")
	private EntityManager em;

	@org.junit.Test
	public void query() {
		Page<BzrPriModelInfo> page = modelInfoDao.findByParentModelModelName(SystemName.办学组织管理平台.name(),
				new PageRequest(0, 10));
		// List<PriModelInfo> page =
		// modelInfoDao.findByParentModelModelName(SystemName.办学组织管理平台.name());
		for (BzrPriModelInfo priModelInfo : page) {
			// System.out.println(priModelInfo.getModelName());
			// for (PriModelInfo child : priModelInfo.getChildModelList()) {
			// System.out.println("--" + child.getModelName());
			// }
			System.out.println(priModelInfo.getOrderNo());
			next(priModelInfo, "-");
		}
		//
		// PriModelInfo findOne =
		// modelInfoDao.findOne("9c8b6fb1ac10a57300768bdbce0527bb");
		// System.out.println(findOne.getPriModelOperateList().size());
	}

	private void next(BzrPriModelInfo info, String space) {
		List<BzrPriModelInfo> childModelList = info.getChildModelList();
		if (childModelList != null) {
			space += "-";
			for (BzrPriModelInfo element : childModelList) {
				System.out.println(space + element.getModelName());
				this.next(element, space);
			}
		}
	}

	@Ignore
	public void add() {
		// 创建一个主菜单
		{
			// PriModelInfo modelInfo = new PriModelInfo();
			// modelInfo.setModelId(UUIDUtils.random());
			// modelInfo.setCreatedDt(new Date());
			// modelInfo.setCreatedBy("liming");
			// modelInfo.setIsdeleted("N");
			// modelInfo.setModelName("TEST1");
			// modelInfoDao.save(modelInfo);
		}

		BzrPriModelInfo parent = modelInfoDao.findOne("2c17bd6b81d2ba71eb51f4718d158e0b");

		// 创建一个子菜单
		{
			BzrPriModelInfo modelInfo = new BzrPriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("liming");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("TEST1111111");
			modelInfo.setParentModel(parent);
			modelInfoDao.save(modelInfo);
		}
	}

	// @Test
	// public void addModelAndOperate() {
	// PriModelInfo model =
	// modelInfoDao.findOne("9c8b6fb1ac10a57300768bdbce0527bb");
	// Assert.assertEquals("项目管理", model.getModelName());
	// Assert.assertEquals(true, model.getIsLeaf());
	// // clean 他的所有操作
	// model.cleanOperateAll();
	// PriModelInfo noOperateModel = modelInfoDao.save(model);
	// Assert.assertEquals(0, noOperateModel.getPriOperateInfoSet().size());
	//
	// List<PriOperateInfo> operateList = operateInfoDao.findAll();
	// PriOperateInfo addOperate = new PriOperateInfo();
	//
	// for (PriOperateInfo operate : operateList) {
	// if (operate.getOperateName().equals("新增")) {
	// addOperate = operate;
	// }
	// }
	// // 给项目管理菜单添加 新增 操作
	// model.addOperate(addOperate);
	//
	// PriModelInfo model1 = modelInfoDao.save(model);
	// Assert.assertEquals(1, model1.getPriModelOperateList().size());
	// }

	@Test
	public void queryRole() {
		BzrPriRoleInfo role = roleDao.findOne("d2b2aa26c02f7bf10d2dda23a91522ab");
		Assert.assertEquals("管理员", role.getRoleName());

		// Assert.assertEquals(1, role.getPriRoleOperateList().size());
	}

	@Test
	public void addRoleOp() {
		// 给管理员添加 项目管理－新增 权限
		BzrPriRoleInfo role = roleDao.findOne("d2b2aa26c02f7bf10d2dda23a91522ab");
		Assert.assertEquals("管理员", role.getRoleName());

		BzrPriModelOperate modelOperate = modelOperateDao.findOne("c554c5593a771311ea4f8143092c2b93");
		Assert.assertEquals("项目管理", modelOperate.getPriModelInfo().getModelName());
		Assert.assertEquals("新增", modelOperate.getPriOperateInfo().getOperateName());

//		role.addRoleOperate(modelOperate);
//		roleDao.save(role);
	}

	@Test
	public void addRoleOp1111() {
		BzrPriModelInfo model = modelInfoDao.findOne("fb4cd506ff876ffa98176e6307a5c72e");

		BzrPriOperateInfo operate = operateInfoDao.findOne("34803b6aac10a5730194b85f743cc1b3");

		model.getPriOperateInfos().add(operate);

		modelInfoDao.save(model);

		BzrPriModelInfo findOne = modelInfoDao.findOne("fb4cd506ff876ffa98176e6307a5c72e");
		System.out.println(findOne.getPriOperateInfos().size());
	}

	@Test
	public void addRoleToUser() {
		// 给ADMIN添加一个角色
		BzrPriRoleInfo role = roleDao.findOne("d2b2aa26c02f7bf10d2dda23a91522ab");
		Assert.assertEquals("管理员", role.getRoleName());

		BzrGjtUserAccount user = userAccountDao.findByLoginAccount("admin");
		Assert.assertNotNull(user);

		user.setPriRoleInfo(role);

		userAccountDao.updateUserRole(role.getRoleId(), user.getId());

		System.out.println(user.getPriRoleInfo().getRoleName());
	}

	@Test
	public void initModel() {
		modelInfoDao.deleteAll();

		// 初始化后台菜单
		List<BzrPriModelInfo> modelInfos = new ArrayList();
		{
			BzrPriModelInfo modelInfo = new BzrPriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("系统管理");
			modelInfo.setModelCode("SYSTEM");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(1);
			modelInfo.setChildModelList(new ArrayList<BzrPriModelInfo>());
			modelInfos.add(modelInfo);
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("个人中心菜单");
				childModel.setModelCode("SYSTEM_PERSONAL");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(1);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("班主任菜单");
				childModel.setModelCode("SYSTEM_");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(2);
				modelInfo.getChildModelList().add(childModel);
			}
		}
		{
			BzrPriModelInfo modelInfo = new BzrPriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("机构管理");
			modelInfo.setModelCode("ORG");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(2);
			modelInfo.setChildModelList(new ArrayList<BzrPriModelInfo>());
			modelInfos.add(modelInfo);
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("院校管理");
				childModel.setModelCode("SYSTEM_");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(1);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("分校管理");
				childModel.setModelCode("SYSTEM_");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(2);
				modelInfo.getChildModelList().add(childModel);
			}
		}
		{
			BzrPriModelInfo modelInfo = new BzrPriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("用户管理");
			modelInfo.setModelCode("USER");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(3);
			modelInfo.setChildModelList(new ArrayList<BzrPriModelInfo>());
			modelInfos.add(modelInfo);
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("学员管理");
				childModel.setModelCode("SYSTEM_");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(1);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("班主任管理");
				childModel.setModelCode("SYSTEM_");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(2);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("辅导教师管理");
				childModel.setModelCode("SYSTEM_");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(3);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("督导管理");
				childModel.setModelCode("SYSTEM_");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(4);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("管理员管理");
				childModel.setModelCode("SYSTEM_");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(5);
				modelInfo.getChildModelList().add(childModel);
			}
		}
		{
			BzrPriModelInfo modelInfo = new BzrPriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("教学教务管理");
			modelInfo.setModelCode("EXAMINATION");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(4);
			modelInfo.setChildModelList(new ArrayList<BzrPriModelInfo>());
			modelInfos.add(modelInfo);
		}
		{
			BzrPriModelInfo modelInfo = new BzrPriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("考务管理");
			modelInfo.setModelCode("EDUCATION");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(5);
			modelInfo.setChildModelList(new ArrayList<BzrPriModelInfo>());
			modelInfos.add(modelInfo);
		}
		{
			BzrPriModelInfo modelInfo = new BzrPriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("统计管理");
			modelInfo.setModelCode("STATISTIC");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(6);

			modelInfo.setChildModelList(new ArrayList<BzrPriModelInfo>());
			modelInfos.add(modelInfo);
		}

		// 初始化系统
		{
			BzrPriModelInfo systemModel = new BzrPriModelInfo();
			systemModel.setModelId(UUIDUtils.random());
			systemModel.setCreatedDt(new Date());
			systemModel.setCreatedBy("MM");
			systemModel.setIsdeleted("N");
			systemModel.setModelName(SystemName.办学组织管理平台.name());
			systemModel.setModelCode("XLIMS");
			systemModel.setIsLeaf(false);
			systemModel.setOrderNo(1);
			for (BzrPriModelInfo priModelInfo : modelInfos) {
				priModelInfo.setParentModel(systemModel);
			}
		}
		modelInfoDao.save(modelInfos);
	}

	@Test
	public void initModelTeacher() {
//		modelInfoDao.deleteBySystemName(SystemName.班主任平台.name());

		// 初始化后台菜单
		List<BzrPriModelInfo> modelInfos = new ArrayList();
		{
			BzrPriModelInfo modelInfo = new BzrPriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("首页");
			modelInfo.setModelCode("MAIN");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(1);
			modelInfo.setChildModelList(new ArrayList<BzrPriModelInfo>());
			modelInfos.add(modelInfo);
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("首页");
				childModel.setModelCode("INDEX");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(1);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("班级管理");
				childModel.setModelCode("CLASS");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(2);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("我的个人管理");
				childModel.setModelCode("PERSONAL");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(3);
				modelInfo.getChildModelList().add(childModel);
			}
		}
		{
			BzrPriModelInfo modelInfo = new BzrPriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("班级管理");
			modelInfo.setModelCode("CLASS_MANAGE");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(2);
			modelInfo.setChildModelList(new ArrayList<BzrPriModelInfo>());
			modelInfos.add(modelInfo);
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("首页");
				childModel.setModelCode("CLASS_INDEX");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(1);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("通知公告");
				childModel.setModelCode("CLASS_MESSAGE");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(2);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("学员信息");
				childModel.setModelCode("CLASS_STUDENT");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(3);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("学员状态");
				childModel.setModelCode("CLASS_STATE");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(4);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("学员考勤");
				childModel.setModelCode("CLASS_CLOCK");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(5);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("学员学情");
				childModel.setModelCode("CLASS_LEARNING");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(6);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("答疑管理");
				childModel.setModelCode("CLASS_FEEDBACK");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(7);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("考试预约");
				childModel.setModelCode("CLASS_EXAMR");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(8);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("缴费管理");
				childModel.setModelCode("CLASS_PAYMENT");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(9);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("发票管理");
				childModel.setModelCode("CLASS_INVOICE");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(10);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("班级活动");
				childModel.setModelCode("CLASS_ACTIVITY");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(11);
				modelInfo.getChildModelList().add(childModel);
			}
			{
				BzrPriModelInfo childModel = new BzrPriModelInfo();
				childModel.setModelId(UUIDUtils.random());
				childModel.setCreatedDt(new Date());
				childModel.setCreatedBy("MM");
				childModel.setIsdeleted("N");
				childModel.setModelName("服务记录");
				childModel.setModelCode("CLASS_SERVICE");
				childModel.setIsLeaf(true);
				childModel.setOrderNo(12);
				modelInfo.getChildModelList().add(childModel);
			}
		}

		// 初始化系统
		{
			BzrPriModelInfo systemModel = new BzrPriModelInfo();
			systemModel.setModelId(UUIDUtils.random());
			systemModel.setCreatedDt(new Date());
			systemModel.setCreatedBy("MM");
			systemModel.setIsdeleted("N");
			systemModel.setModelName(SystemName.班主任平台.name());
			systemModel.setModelCode("HEADTEACHER");
			systemModel.setIsLeaf(false);
			systemModel.setOrderNo(1);
			for (BzrPriModelInfo priModelInfo : modelInfos) {
				priModelInfo.setParentModel(systemModel);
			}
		}

		modelInfoDao.save(modelInfos.get(0).getParentModel());
		for (BzrPriModelInfo priModelInfo : modelInfos) {
			modelInfoDao.save(priModelInfo.getChildModelList());
		}
		modelInfoDao.save(modelInfos);
	}

	public static void main(String[] args) {
	}

	@Test
	public void addModelOperate() {
		operateInfoDao.findAll();
	}

	@Test
	public void ssss() {
		// Query query = em.createNativeQuery("select t.model_id id,t.model_name
		// name from pri_model_info t");
		// query.unwrap(SQLQuery.class).setResultTransformer(new
		// AliasToBeanResultTransformer(Sssssss.class));
		// //
		// query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		// List<Sssssss> rows = query.getResultList();
		// for (Sssssss row : rows) {
		// System.out.println(row.getID());
		// System.out.println(row.getNAME());
		// }
	}

}