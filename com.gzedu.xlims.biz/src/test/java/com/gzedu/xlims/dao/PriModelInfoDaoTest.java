/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.menu.SystemName;
import com.gzedu.xlims.dao.model.PriModelInfoDao;
import com.gzedu.xlims.dao.model.PriModelOperateDao;
import com.gzedu.xlims.dao.model.PriOperateInfoDao;
import com.gzedu.xlims.dao.model.PriRoleInfoDao;
import com.gzedu.xlims.dao.model.PriRoleOperateDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriModelInfo;
import com.gzedu.xlims.pojo.PriModelOperate;
import com.gzedu.xlims.pojo.PriOperateInfo;
import com.gzedu.xlims.pojo.PriRoleInfo;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月6日
 * @version 2.5
 *
 */
@Transactional
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

	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager em;

	@org.junit.Test
	public void query() {
		Page<PriModelInfo> page = modelInfoDao.findByParentModelModelName(SystemName.办学组织管理平台.name(),
				new PageRequest(0, 10));
		// List<PriModelInfo> page =
		// modelInfoDao.findByParentModelModelName(SystemName.办学组织管理平台.name());
		for (PriModelInfo priModelInfo : page) {
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

	private void next(PriModelInfo info, String space) {
		List<PriModelInfo> childModelList = info.getChildModelList();
		if (childModelList != null) {
			space += "-";
			for (PriModelInfo element : childModelList) {
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

		PriModelInfo parent = modelInfoDao.findOne("2c17bd6b81d2ba71eb51f4718d158e0b");

		// 创建一个子菜单
		{
			PriModelInfo modelInfo = new PriModelInfo();
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
		// PriRoleInfo role =
		// roleDao.findOne("d2b2aa26c02f7bf10d2dda23a91522ab");
		// Assert.assertEquals("管理员", role.getRoleName());

		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		// "search_EQ_priModelInfos.priOperateInfos.operateId"
		filters.put("priModelInfos.priOperateInfos.operateId", new SearchFilter(
				"priModelInfos.priOperateInfos.operateId", Operator.EQ, "34818da4ac10a5730194b85fe66f1922"));

		filters.put("priModelInfos.modelId", new SearchFilter("priModelInfos.modelId", SearchFilter.Operator.EQ,
				"5b5bf1b9a82971c378127ecdebe1db17"));
		filters.put("priModelInfos.priOperateInfos.operateId",
				new SearchFilter("priModelInfos.priOperateInfos.operateId", SearchFilter.Operator.EQ,
						"34818da4ac10a5730194b85fe66f1922"));

		Specification<PriRoleInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), PriRoleInfo.class);

		List<PriRoleInfo> findAll = roleDao.findAll(spec);

		System.out.println(findAll.size());
		// List<PriModelInfo> roleModels =
		// modelInfoDao.findByPriRoleInfos(role);
		//
		// for (PriModelInfo priModelInfo : roleModels) {
		// System.out.println(priModelInfo.getModelName());
		// }

		// Assert.assertEquals(1, role.getPriRoleOperateList().size());
	}

	@Test
	public void addRoleOp() {
		// 给管理员添加 项目管理－新增 权限
		PriRoleInfo role = roleDao.findOne("d2b2aa26c02f7bf10d2dda23a91522ab");
		Assert.assertEquals("管理员", role.getRoleName());

		PriModelOperate modelOperate = modelOperateDao.findOne("c554c5593a771311ea4f8143092c2b93");
		Assert.assertEquals("项目管理", modelOperate.getPriModelInfo().getModelName());
		Assert.assertEquals("新增", modelOperate.getPriOperateInfo().getOperateName());

		// role.addRoleOperate(modelOperate);
		roleDao.save(role);
	}

	@Test
	public void addRoleOp1111() {
		PriModelInfo model = modelInfoDao.findOne("fb4cd506ff876ffa98176e6307a5c72e");

		PriOperateInfo operate = operateInfoDao.findOne("34803b6aac10a5730194b85f743cc1b3");

		model.getPriOperateInfos().add(operate);

		modelInfoDao.save(model);

		PriModelInfo findOne = modelInfoDao.findOne("fb4cd506ff876ffa98176e6307a5c72e");
		System.out.println(findOne.getPriOperateInfos().size());
	}

	@Test
	public void addRoleToUser() {
		// 给ADMIN添加一个角色
		PriRoleInfo role = roleDao.findOne("d2b2aa26c02f7bf10d2dda23a91522ab");
		Assert.assertEquals("管理员", role.getRoleName());

		GjtUserAccount user = userAccountDao.findByLoginAccount("admin");
		Assert.assertNotNull(user);

		user.setPriRoleInfo(role);

		userAccountDao.updateUserRole(role.getRoleId(), user.getId());

		System.out.println(user.getPriRoleInfo().getRoleName());
	}

	@Test
	public void initModel() {
		modelInfoDao.deleteAll();

		// 初始化后台菜单
		List<PriModelInfo> modelInfos = new ArrayList<PriModelInfo>();
		{
			PriModelInfo modelInfo = new PriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("系统管理");
			modelInfo.setModelCode("SYSTEM");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(1);
			modelInfo.setChildModelList(new ArrayList<PriModelInfo>());
			modelInfos.add(modelInfo);
			{
				PriModelInfo childModel = new PriModelInfo();
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
				PriModelInfo childModel = new PriModelInfo();
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
			PriModelInfo modelInfo = new PriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("机构管理");
			modelInfo.setModelCode("ORG");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(2);
			modelInfo.setChildModelList(new ArrayList<PriModelInfo>());
			modelInfos.add(modelInfo);
			{
				PriModelInfo childModel = new PriModelInfo();
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
				PriModelInfo childModel = new PriModelInfo();
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
			PriModelInfo modelInfo = new PriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("用户管理");
			modelInfo.setModelCode("USER");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(3);
			modelInfo.setChildModelList(new ArrayList<PriModelInfo>());
			modelInfos.add(modelInfo);
			{
				PriModelInfo childModel = new PriModelInfo();
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
				PriModelInfo childModel = new PriModelInfo();
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
				PriModelInfo childModel = new PriModelInfo();
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
				PriModelInfo childModel = new PriModelInfo();
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
				PriModelInfo childModel = new PriModelInfo();
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
			PriModelInfo modelInfo = new PriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("教学教务管理");
			modelInfo.setModelCode("EXAMINATION");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(4);
			modelInfo.setChildModelList(new ArrayList<PriModelInfo>());
			modelInfos.add(modelInfo);
		}
		{
			PriModelInfo modelInfo = new PriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("考务管理");
			modelInfo.setModelCode("EDUCATION");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(5);
			modelInfo.setChildModelList(new ArrayList<PriModelInfo>());
			modelInfos.add(modelInfo);
		}
		{
			PriModelInfo modelInfo = new PriModelInfo();
			modelInfo.setModelId(UUIDUtils.random());
			modelInfo.setCreatedDt(new Date());
			modelInfo.setCreatedBy("MM");
			modelInfo.setIsdeleted("N");
			modelInfo.setModelName("统计管理");
			modelInfo.setModelCode("STATISTIC");
			modelInfo.setIsLeaf(false);
			modelInfo.setOrderNo(6);

			modelInfo.setChildModelList(new ArrayList<PriModelInfo>());
			modelInfos.add(modelInfo);
		}

		// 初始化系统
		{
			PriModelInfo systemModel = new PriModelInfo();
			systemModel.setModelId(UUIDUtils.random());
			systemModel.setCreatedDt(new Date());
			systemModel.setCreatedBy("MM");
			systemModel.setIsdeleted("N");
			systemModel.setModelName(SystemName.办学组织管理平台.name());
			systemModel.setModelCode("XLIMS");
			systemModel.setIsLeaf(false);
			systemModel.setOrderNo(1);
			for (PriModelInfo priModelInfo : modelInfos) {
				priModelInfo.setParentModel(systemModel);
			}
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
