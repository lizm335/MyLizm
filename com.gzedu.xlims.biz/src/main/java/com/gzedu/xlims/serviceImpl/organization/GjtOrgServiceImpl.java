/**
* Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.exception.ServiceException;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.organization.GjtSchoolInfoDao;
import com.gzedu.xlims.dao.organization.GjtSetOrgCopyrightDao;
import com.gzedu.xlims.dao.organization.GjtStudyCenterDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSetOrgCopyright;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

/**
 * 
 * 功能说明：院校管理
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月7日
 * @version 2.5
 *
 */
@Service
public class GjtOrgServiceImpl extends BaseServiceImpl<GjtOrg> implements GjtOrgService {

	private static final Logger log = LoggerFactory.getLogger(GjtOrgServiceImpl.class);

	@Autowired
	GjtOrgDao gjtOrgDao;

	@Autowired
	GjtSchoolInfoDao gjtSchoolInfoDao;

	@Autowired
	GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	private PriRoleInfoService priRoleInfoService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	private GjtSetOrgCopyrightDao gjtSetOrgCopyrightDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	GjtStudyCenterDao gjtStudyCenterDao;

	@Override
	protected BaseDao<GjtOrg, String> getBaseDao() {
		return this.gjtOrgDao;
	}

	@Override
	public Page<GjtOrg> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtOrg> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtOrg.class);
		return gjtOrgDao.findAll(spec, pageRequst);
	}

	@Override
	public Page<GjtOrg> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Criteria<GjtOrg> spec = new Criteria();

		if (StringUtils.isNotBlank(orgId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			spec.add(Restrictions.eq("gjtSchoolInfo.id", orgId, true));
		}

		spec.addAll(Restrictions.parse(searchParams));

		return gjtOrgDao.findAll(spec, pageRequst);
	}

	@Override
	public long queryAllCount(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtOrg> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtOrg.class);
		return gjtOrgDao.count(spec);
	}

	@Override
	public Page<GjtOrg> queryAllByParentId(String userId, boolean isChild, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		log.info("searchParams:" + searchParams + "userId:" + userId + "isChild:" + isChild);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		GjtUserAccount user = gjtUserAccountDao.findOne(userId);
		if (user.getGjtOrg() != null) {
			if (isChild) {
				filters.put("parentGjtOrg.id",
						new SearchFilter("parentGjtOrg.id", Operator.EQ, user.getGjtOrg().getId()));
			} else {
				filters.put("id", new SearchFilter("id", Operator.EQ, user.getGjtOrg().getId()));
			}
		} else {
			filters.put("id", new SearchFilter("id", Operator.EQ, null));
		}
		// filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ,
		// "N"));

		Specification<GjtOrg> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtOrg.class);
		return gjtOrgDao.findAll(spec, pageRequst);
	}

	@Override
	public List<GjtOrg> queryAll() {
		return gjtOrgDao.findAll();
	}

	@Override
	public GjtOrg queryById(String id) {
		return gjtOrgDao.findOne(id);
	}

	@Override
	@Transactional
	public boolean insert(GjtOrg entity) {
		// 当前用户创建院校
		String xx_id = UUIDUtils.random();
		entity.setId(xx_id);
		entity.setIsDeleted("N");
		entity.setIsEnabled("1");
		entity.setCreatedDt(new Date());
		if (StringUtils.isEmpty(entity.getCreatedBy())) {
			entity.setCreatedBy("admin");
		}
		entity.setOrgType("1");
		entity.setExtrCode(entity.getCode());
		entity.setTreeCode(entity.getCode());
		entity.getGjtSchoolInfo().setAppid(entity.getCode());// 用院校编号代表appid（确认过）
		entity.getGjtSchoolInfo().setXxdm(entity.getCode());
		entity.getGjtSchoolInfo().setId(entity.getId());
		entity.getGjtSchoolInfo().setXxmc(entity.getOrgName());
		entity.getGjtSchoolInfo().setIsDeleted("N");
		entity.getGjtSchoolInfo().setIsEnabled("1");
		entity.getGjtSchoolInfo().setCreatedDt(new Date());
		if (StringUtils.isEmpty(entity.getCreatedBy())) {
			entity.getGjtSchoolInfo().setCreatedBy("admin");
		} else {

		}
		entity.getGjtSchoolInfo().setCreatedBy(entity.getCreatedBy());

		if (entity.getParentGjtOrg() == null) {
			GjtOrg parent = gjtOrgDao.findOne("fccafe375dba41608688bc28f5e0c91f"); // 根节点
			entity.setParentGjtOrg(parent);
		}

		gjtOrgDao.save(entity);
		gjtSchoolInfoDao.save(entity.getGjtSchoolInfo());

		// 初始化版权
		List<GjtSetOrgCopyright> list = new ArrayList<GjtSetOrgCopyright>();
		GjtSetOrgCopyright item1 = new GjtSetOrgCopyright(xx_id, PlatfromTypeEnum.COACHTEACHERPLATFORM.getNum());
		list.add(item1);
		GjtSetOrgCopyright item2 = new GjtSetOrgCopyright(xx_id, PlatfromTypeEnum.HEADTEACHERPLATFORM.getNum());
		list.add(item2);
		GjtSetOrgCopyright item3 = new GjtSetOrgCopyright(xx_id, PlatfromTypeEnum.MANAGEPLATFORM.getNum());
		list.add(item3);
		GjtSetOrgCopyright item4 = new GjtSetOrgCopyright(xx_id, PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		list.add(item4);
		GjtSetOrgCopyright item5 = new GjtSetOrgCopyright(xx_id, PlatfromTypeEnum.SUPERVISORTEACHERPLATFORM.getNum());
		list.add(item5);
		gjtSetOrgCopyrightDao.save(list);

		// 初始化年级学期信息
		gjtGradeService.initYearAndGrade(entity.getId());
		return true;
	}

	@Override
	@Transactional
	public void insert(GjtOrg entity, Map<Integer, String> domainMap) {
		// 插入院校
		insert(entity);
		// 设置版权信息-院校域名
		if (domainMap != null && domainMap.size() > 0) {
			for (Entry<Integer, String> entry : domainMap.entrySet()) {
				PlatfromTypeEnum platFromType = PlatfromTypeEnum.getItem(entry.getKey());
				if (platFromType != null) {
					String domain = entry.getValue();
					if (StringUtils.isNotEmpty(domain)) {
						GjtSetOrgCopyright copyright = gjtSetOrgCopyrightDao.findByXxIdAndPlatfromType(entity.getId(),
								String.valueOf(platFromType.getNum()));
						copyright.setSchoolRealmName(domain);
						gjtSetOrgCopyrightDao.save(copyright);
					}
				}
			}
		}
	}

	@Override
	@Transactional
	public void insertChildOrg(String userId, GjtOrg entity) {
		GjtOrg parentOrg;
		if (StringUtils.isEmpty(userId)) {
			parentOrg = gjtOrgDao.findOne(entity.getParentGjtOrg().getId());
		} else {
			// 当前用户创建院校
			GjtUserAccount userAccount = gjtUserAccountDao.findOne(userId);

			if ("0".equals(userAccount.getGrantType())) {// 超级管理员的话上级院校可以选择
				parentOrg = gjtOrgDao.findOne(entity.getParentGjtOrg().getId());
			} else {
				parentOrg = userAccount.getGjtOrg();
			}
		}

		entity.setId(UUIDUtils.random());
		entity.setIsDeleted("N");
		entity.setCreatedDt(new Date());

		entity.setExtrCode(parentOrg.getCode() + entity.getCode());
		entity.setTreeCode(parentOrg.getCode() + entity.getCode());

		entity.setIsEnabled("1");
		entity.setOrgType("2");
		entity.getGjtSchoolInfo().setId(entity.getId());
		entity.getGjtSchoolInfo().setAppid(entity.getCode());// 用院校代码代替appid（确认过）
		entity.getGjtSchoolInfo().setXxdm(entity.getCode());
		entity.getGjtSchoolInfo().setXxmc(entity.getOrgName());
		entity.getGjtSchoolInfo().setIsDeleted("N");
		entity.getGjtSchoolInfo().setIsEnabled("1");
		entity.getGjtSchoolInfo().setCreatedDt(new Date());
		entity.getGjtSchoolInfo().setCreatedBy(entity.getCreatedBy());
		entity.setParentGjtOrg(parentOrg);
		entity.setSchoolModel(parentOrg.getSchoolModel()); // 办学模式和父院校相同
		gjtOrgDao.save(entity);
		gjtSchoolInfoDao.save(entity.getGjtSchoolInfo());
	}

	@Override
	@Transactional
	public void update(GjtOrg entity) {
		entity.setUpdatedDt(DateUtils.getNowTime());
		gjtOrgDao.save(entity);
		entity.getGjtSchoolInfo().setUpdatedDt(DateUtils.getNowTime());
		gjtSchoolInfoDao.save(entity.getGjtSchoolInfo());
	}

	@Override
	@Transactional
	public void updateStudyCenter(GjtOrg entity) {
		gjtOrgDao.save(entity);
		gjtStudyCenterDao.save(entity.getGjtStudyCenter());
	}

	@Override
	@Transactional
	public void delete(List<String> ids) {
		for (String id : ids) {
			GjtOrg org = gjtOrgDao.findOne(id);
			if (org.getGjtUserAccounts().size() > 1) {
				throw new ServiceException("存在用户数量:" + org.getGjtUserAccounts().size());
			}
			if (org.getGjtEmployeeInfos().size() > 0) {
				throw new ServiceException("存在职员数量:" + org.getGjtEmployeeInfos().size());
			}
			if (org.getGjtStudentInfos().size() > 0) {
				throw new ServiceException("存在学生数量:" + org.getGjtStudentInfos().size());
			}
			if (org.getChildGjtOrgs().size() > 0) {
				throw new ServiceException("存在分院数量:" + org.getChildGjtOrgs().size());
			}
			org.setIsDeleted("Y");
			org.setUpdatedDt(new Date());
			if (org.getGjtSchoolInfo() != null) {

				org.getGjtSchoolInfo().setUpdatedDt(new Date());
				org.getGjtSchoolInfo().setIsDeleted("Y");
			}
			gjtOrgDao.save(org);
			gjtSchoolInfoDao.save(org.getGjtSchoolInfo());
		}
	}

	@Override
	public Page<GjtUserAccount> queryUserManagers(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		List<PriRoleInfo> orgMagagers = priRoleInfoService.queryOrgMagagerRoles();
		filters.put("priRoleInfo", new SearchFilter("priRoleInfo", Operator.IN, orgMagagers));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));

		Specification<GjtUserAccount> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtUserAccount.class);
		return gjtUserAccountDao.findAll(spec, pageRequst);
	}

	@Override
	public Page<GjtUserAccount> queryUserManagers(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		List<PriRoleInfo> orgMagagers = priRoleInfoService.queryOrgMagagerRoles();
		filters.put("priRoleInfo", new SearchFilter("priRoleInfo", Operator.IN, orgMagagers));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("gjtOrg.id", new SearchFilter("gjtOrg.id", Operator.EQ, orgId));

		Specification<GjtUserAccount> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtUserAccount.class);
		return gjtUserAccountDao.findAll(spec, pageRequst);
	}

	@Override
	public long queryOrgSetNum(GjtSetOrgCopyright entity, String xxId) {
		log.info("xx_id:" + xxId + "type:" + entity.getPlatfromType());
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, xxId));
		filters.put("platfromType", new SearchFilter("platfromType", Operator.EQ, entity.getPlatfromType()));
		Specification<GjtSetOrgCopyright> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtSetOrgCopyright.class);
		return gjtSetOrgCopyrightDao.count(spec);
	}

	@Override
	@Transactional
	public boolean insertSetOrgCopyright(GjtSetOrgCopyright entity, String xxId, String userId) {
		log.info("xx_id:" + xxId + "platfromType:" + entity.getPlatfromType() + "userId:" + userId);
		boolean result = true;
		try {
			String id = UUIDUtils.random();
			GjtUserAccount userAccount = gjtUserAccountDao.findOne(userId);
			if (entity.getPlatfromType().equals("1")) {// 管理平台
				entity.setId(id);
				entity.setXxId(xxId);// 院校ID
				entity.setLoginHeadLogo(entity.getLoginHeadLogo());// 登录页头部LOGO
				entity.setLoginBackground(entity.getLoginBackground());// 登录背景
				entity.setSchoolRealmName(entity.getSchoolRealmName());// 院校域名
				entity.setLoginFooterCopyright(entity.getLoginFooterCopyright());// 登录页底部版权
				entity.setPlatfromType(entity.getPlatfromType());// 平台类型
			} else {
				entity.setId(id);
				entity.setXxId(xxId);// 院校ID
				entity.setLoginHeadLogo(entity.getLoginHeadLogo());// 登录页头部LOGO
				entity.setLoginBackground(entity.getLoginBackground());// 登录背景
				entity.setSchoolRealmName(entity.getSchoolRealmName());// 院校域名
				entity.setLoginTitle(entity.getLoginTitle());// 登录标题
				entity.setLoginFooterCopyright(entity.getLoginFooterCopyright());// 登录页底部版权
				entity.setHomeHeadLogo(entity.getHomeHeadLogo());// 首页头部LOGO
				entity.setHomeFooterCopyright(entity.getHomeFooterCopyright());// 首页底部版权
				entity.setPlatfromType(entity.getPlatfromType());// 平台类型
				if (entity.getPlatfromType().equals("5")) { // 个人中心
					entity.setSchoolModel(entity.getSchoolModel()); // 个人中心院校模式
				}
				entity.setQcodePic(entity.getQcodePic());// 下载个人中心的APP二维码地址
			}
			entity.setCreatedDt(new Date());
			entity.setCreatedBy(userAccount.getRealName());
			entity.setIsDeleted("N");
			gjtSetOrgCopyrightDao.save(entity);
		} catch (Exception e) {
			log.info("出现异常，开始回滚：" + e);
			result = false;
		}
		return result;
	}

	@Override
	@Transactional
	public boolean updateSetOrgCopyright(GjtSetOrgCopyright entity, String xxId, String userId) {
		log.info("xx_id:" + xxId + "platfromType:" + entity.getPlatfromType() + "userId:" + userId);
		boolean result = true;
		try {
			GjtUserAccount userAccount = gjtUserAccountDao.findOne(userId);
			GjtSetOrgCopyright gjtSetOrgCopyright = gjtSetOrgCopyrightDao.findByXxIdAndPlatfromType(xxId,
					entity.getPlatfromType());
			if (entity.getPlatfromType().equals("1")) {
				gjtSetOrgCopyright.setLoginHeadLogo(entity.getLoginHeadLogo());// 登录页头部LOGO
				gjtSetOrgCopyright.setLoginBackground(entity.getLoginBackground());// 登录背景
				gjtSetOrgCopyright.setSchoolRealmName(entity.getSchoolRealmName());// 院校域名
				gjtSetOrgCopyright.setPlatformName(entity.getPlatformName());// 平台名称
				gjtSetOrgCopyright.setLoginFooterCopyright(entity.getLoginFooterCopyright());// 登录页底部版权
				gjtSetOrgCopyright.setHomeFooterCopyright(entity.getHomeFooterCopyright());// 首页底部版权
			} else {
				gjtSetOrgCopyright.setLoginHeadLogo(entity.getLoginHeadLogo());// 登录页头部LOGO
				gjtSetOrgCopyright.setLoginBackground(entity.getLoginBackground());// 登录背景
				gjtSetOrgCopyright.setSchoolRealmName(entity.getSchoolRealmName());// 院校域名
				gjtSetOrgCopyright.setLoginTitle(entity.getLoginTitle());// 登录标题
				gjtSetOrgCopyright.setLoginFooterCopyright(entity.getLoginFooterCopyright());// 登录页底部版权
				gjtSetOrgCopyright.setHomeHeadLogo(entity.getHomeHeadLogo());// 首页头部LOGO
				gjtSetOrgCopyright.setHomeFooterCopyright(entity.getHomeFooterCopyright());// 首页底部版权
				if (entity.getPlatfromType().equals("5")) { // 个人中心
					gjtSetOrgCopyright.setSchoolModel(entity.getSchoolModel()); // 个人中心院校模式
					gjtSetOrgCopyright.setQcodePic(entity.getQcodePic());
				}
			}
			gjtSetOrgCopyright.setUpdatedDt(new Date());
			gjtSetOrgCopyright.setUpdatedBy(userAccount.getRealName());
			GjtSetOrgCopyright item = gjtSetOrgCopyrightDao.save(gjtSetOrgCopyright);
			result = item != null ? true : false;
		} catch (Exception e) {
			log.info("出现异常，开始回滚：" + e);
			result = false;
		}
		return result;
	}

	/**
	 * 启用停用机构
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@Override
	public boolean updateStatus(String id, String status) {
		log.info("启用停用机构参数：id={},status={};传0启用，传1停用", id, status);
		boolean result = true;
		try {
			GjtOrg org = gjtOrgDao.findOne(id);
			if ("1".equals(status)) {
				org.setIsEnabled("0");
				if (org.getGjtSchoolInfo() != null) {
					org.getGjtSchoolInfo().setIsEnabled("0");
				}
				if (org.getGjtStudyCenter() != null) {
					org.getGjtStudyCenter().setIsEnabled("0");
				}
			} else {
				org.setIsEnabled("1");
				if (org.getGjtSchoolInfo() != null) {
					org.getGjtSchoolInfo().setIsEnabled("1");
				}
				if (org.getGjtStudyCenter() != null) {
					org.getGjtStudyCenter().setIsEnabled("1");
				}
			}
			gjtOrgDao.save(org);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = false;
		}
		return result;
	}

	@Override
	public GjtOrg queryByschoolIdAndScName(String schoolId, String scName) {
		List<GjtOrg> list = gjtOrgDao.findByParentGjtOrgAndOrgName(schoolId, scName);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 唯一性校验，校验院校编码
	 *
	 * @param code
	 * @return
	 */
	@Override
	public boolean queryByCode(String code, String oldCode) {
		GjtOrg queryByCode = null;
		boolean bool = true;
		try {
			queryByCode = gjtOrgDao.queryByCode(code);
		} catch (Exception e) {
			bool = false;// 可能重复引起了，查询SQL报错，返回多条
		}
		if (queryByCode != null) {
			bool = false;
		}
		return bool;
	}

	@Override
	public List<String> queryByParentId(String parentId) {
		List<String> orgList = gjtOrgDao.queryChildsByParentId(parentId);
		return orgList;
	}

	@Override
	public List<String> queryByParentId(String parentId, String orgType) {
		List<String> orgList = gjtOrgDao.queryChildsByParentId(parentId, orgType);
		return orgList;
	}

	@Override
	public String queryChildsByParentIdAndCode(String parentId, String code) {
		return gjtOrgDao.queryChildsByParentIdAndCode(parentId, code);
	}

	/**
	 * 唯一性校验，校验院校编码
	 *
	 * @param code
	 * @return
	 */
	@Override
	public GjtOrg queryByCode(String code) {
		return gjtOrgDao.queryByCode(code);
	}

	/**
	 * 查询院校下的用户数
	 */
	@Override
	public List<Map<String, String>> queryUserAcountNum(String id) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		sql.append("  select *");
		sql.append("  from gjt_user_account go");
		sql.append("  where go.is_Deleted = 'N'");
		sql.append("  and go.user_type = '0'");
		sql.append("  and go.role_id in (select code");
		sql.append("  from tbl_sys_data tsd");
		sql.append("  where tsd.type_code = 'AdminRole'");
		sql.append("  and go.role_id = tsd.code)");
		sql.append("  and go.org_id IN (SELECT org.id");
		sql.append("  FROM GJT_ORG org");
		sql.append("  WHERE org.IS_DELETED = 'N'");
		sql.append("  START WITH org.ID =:orgId");
		sql.append("  CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID)");
		map.put("orgId", id);
		List<Map<String, String>> list = commonDao.queryForMapListNative(sql.toString(), map);
		return list;
		// Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		// filters.put("gjtOrg.id", new SearchFilter("gjtOrg.id", Operator.EQ,
		// id));
		// Specification<GjtUserAccount> spec =
		// DynamicSpecifications.bySearchFilter(filters.values(),
		// GjtUserAccount.class);

	}

	@Override
	public GjtOrg queryParentBySonId(String sonId) {
		return gjtOrgDao.queryParentBySonId(sonId);
	}

	@Override
	public List<String> queryParents(String childrenId) {
		return gjtOrgDao.queryParents(childrenId);
	}

	@Override
	public String getSystemAdministrativeOrganizationByOrgId(String orgId) {
		return gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
	}

	@Override
	public List<GjtOrg> queryStudyBySchoolId(String schoolId) {
		return gjtOrgDao.queryStudyBySchoolId(schoolId);
	}

	@Override
	public long queryOrgCount(Map<String, Object> searchParams) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select * from gjt_org  c where c.is_deleted='N' and c.id != :id ");

		String orgType = (String) searchParams.get("orgType");
		if (StringUtils.isNotBlank(orgType)) {
			sql.append(" and c.org_type=:orgType ");
			params.put("orgType", orgType);
		}

		String belongsTo = (String) searchParams.get("belongsTo");
		if (StringUtils.isNotBlank(belongsTo)) {
			sql.append(" and c.belongs_To=:belongsTo ");
			params.put("belongsTo", belongsTo);
		}

		sql.append(" start with c.id = :id connect by prior   c.id=c.perent_id");
		params.put("id", searchParams.get("id"));
		return commonDao.queryForCountNative(sql.toString(), params);
	}

	// 创建除了orgType =1；调用的方法
	@Override
	public GjtOrg saveSchool(GjtOrg entity) {
		String xx_id = UUIDUtils.random();
		entity.setId(xx_id);
		entity.setIsDeleted("N");
		entity.setIsEnabled("1");
		entity.setCreatedDt(DateUtils.getNowTime());
		entity.setCreatedBy("admin");
		entity.setOrgType(entity.getOrgType());
		entity.setExtrCode(entity.getCode());
		entity.setTreeCode(entity.getCode());
		entity.getGjtSchoolInfo().setAppid(entity.getCode());// 用院校编号代表appid（确认过）
		entity.getGjtSchoolInfo().setXxdm(entity.getCode());
		entity.getGjtSchoolInfo().setId(entity.getId());
		entity.getGjtSchoolInfo().setXxmc(entity.getOrgName());
		entity.getGjtSchoolInfo().setIsDeleted("N");
		entity.getGjtSchoolInfo().setIsEnabled("1");
		entity.getGjtSchoolInfo().setCreatedDt(DateUtils.getNowTime());
		entity.getGjtSchoolInfo().setCreatedBy("admin");
		entity.getGjtSchoolInfo().setCreatedBy(entity.getCreatedBy());

		entity.setParentGjtOrg(entity.getParentGjtOrg());

		GjtOrg save = gjtOrgDao.save(entity);
		gjtSchoolInfoDao.save(entity.getGjtSchoolInfo());
		return save;
	}

	@Override
	public List<Map<String, String>> queryOrgTree(String parentId) {
		StringBuilder sql = new StringBuilder();
		sql.append("  select a.id,");
		sql.append("  case");
		sql.append("  when a.org_type = '10' then");
		sql.append("  a.org_name");
		sql.append("  when a.org_type = '1' then");
		sql.append("  '——' || a.org_name");
		sql.append("  else");
		sql.append("  a.org_name");
		sql.append("  end name,");
		sql.append("  level");
		sql.append("  from gjt_org a");
		sql.append("  where (a.org_type = '10' or a.org_type = '1')");
		sql.append("  and a.is_deleted = 'N'");
		sql.append("  and a.is_enabled = '1'");
		sql.append("  START WITH a.id = :id ");
		sql.append("  CONNECT BY PRIOR a.id = a.PERENT_ID ");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", parentId);
		List<Map<String, String>> list = commonDao.queryForMapListNative(sql.toString(), params);
		return list;
	}

	@Override
	public GjtOrg saveStudyCenter(GjtOrg entity) {
		// 新增学习中心
		String id = UUIDUtils.random();
		// 新增组织机构表中间表关系
		entity.setId(id);
		entity.setIsDeleted("N");
		entity.setIsEnabled("0");
		entity.setVersion(BigDecimal.ONE);
		entity.setCreatedDt(DateUtils.getNowTime());
		GjtOrg parentOrg = gjtOrgDao.findOne(entity.getParentGjtOrg().getId());
		entity.setParentGjtOrg(parentOrg);
		entity.setSchoolModel(parentOrg.getSchoolModel()); // 办学模式和父机构相同
		if ("3".equals(entity.getOrgType())) {// 直属分部或者 属于分校
			entity.setBelongsTo(parentOrg.getOrgType());
		}

		entity.getGjtStudyCenter().setId(id);
		entity.getGjtStudyCenter().setScCode(entity.getCode());
		entity.getGjtStudyCenter().setScName(entity.getOrgName());
		entity.getGjtStudyCenter().setAuditStatus("0");
		entity.getGjtStudyCenter().setIsDeleted("N");
		entity.getGjtStudyCenter().setIsEnabled("0");
		entity.getGjtStudyCenter().setCenterType(entity.getOrgType());
		entity.getGjtStudyCenter().setCreatedBy(entity.getCreatedBy());
		entity.getGjtStudyCenter().setCreatedDt(DateUtils.getNowTime());
		GjtOrg save = gjtOrgDao.save(entity);

		return save;
	}

	@Override
	public Page<Map<String, Object>> queryAllStudyCenter(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("查询学习中心参数：{},分页参数:{}", searchParams, pageRequst);
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();

		sql.append("  SELECT org.id \"id\",org.org_name \"orgName\",b.org_name \"parentOrgName\",");
		sql.append("  org.code \"code\",");
		// sql.append(" (select count(*)");//突然说不要用户数
		// sql.append(" from gjt_user_account u");
		// sql.append(" where u.org_id = org.id");
		// sql.append(" and u.user_type = '0') \"userCount\",");
		sql.append("  (select count(*)");
		sql.append("  from gjt_org a");
		sql.append("  where a.is_deleted = 'N'");
		sql.append("  and a.org_type = '6'");
		sql.append("  start with a.id = org.id");
		sql.append("  connect by prior a.id = a.perent_id) \"enrollStudentCount\",");
		sql.append("  s.link_tel \"linkTel\" ,s.linkman \"linkman\",s.district ,s.service_area,");
		sql.append("  s.office_Addr,s.audit_Status  \"auditStatus\",s.is_Enabled  \"isEnabled\"");
		sql.append("  from gjt_org org");
		sql.append("  left join gjt_study_center s");
		sql.append("  on org.id = s.id");
		sql.append("  and s.is_deleted = 'N'");
		sql.append("  left join gjt_org b");
		sql.append("  on org.perent_id = b.id");
		sql.append("  where org.is_deleted = 'N'");

		String parentOrgName = (String) searchParams.get("LIKE_parentGjtOrg.orgName");
		if (StringUtils.isNotBlank(parentOrgName)) {
			sql.append("  and b.org_name like :parentOrgName ");
			params.put("parentOrgName", "%" + parentOrgName + "%");
		}

		String code = (String) searchParams.get("LIKE_code");
		if (StringUtils.isNotBlank(code)) {
			sql.append("  and org.code like :code ");
			params.put("code", "%" + code + "%");
		}

		String orgName = (String) searchParams.get("LIKE_orgName");
		if (StringUtils.isNotBlank(orgName)) {
			sql.append("  and org.org_name like :orgName ");
			params.put("orgName", "%" + orgName + "%");
		}

		String isEnabled = (String) searchParams.get("EQ_isEnabled");
		if (StringUtils.isNotBlank(isEnabled)) {
			sql.append("  and s.is_Enabled = :isEnabled ");
			params.put("isEnabled", isEnabled);
		}

		String auditStatus = (String) searchParams.get("EQ_auditStatus");
		if (StringUtils.isNotBlank(auditStatus)) {
			sql.append("  and s.audit_Status = :auditStatus ");
			params.put("auditStatus", auditStatus);
		}

		sql.append("  and org.org_type = '3'");
		sql.append("  start with org.perent_id = :orgId");
		sql.append("  connect by prior org.id = org.perent_id");

		params.put("orgId", searchParams.get("orgId"));

		Page<Map<String, Object>> page = commonDao.queryForPageNative(sql.toString(), params, pageRequst);
		return page;
	}

	@Override
	public long queryAllStudyCenterCount(Map<String, Object> searchParams) {
		log.info("查询学习中心参数：{},分页参数:{}", searchParams);
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();

		sql.append("  select * from gjt_org org ");
		sql.append("  left join gjt_study_center s");
		sql.append("  on org.id = s.id");
		sql.append("  and s.is_deleted = 'N'");
		sql.append("  where org.is_deleted = 'N'");

		String code = (String) searchParams.get("LIKE_code");
		if (StringUtils.isNotBlank(code)) {
			sql.append("  and org.code like :code ");
			params.put("code", "%" + code + "%");
		}

		String orgName = (String) searchParams.get("LIKE_orgName");
		if (StringUtils.isNotBlank(orgName)) {
			sql.append("  and org.org_name like :orgName ");
			params.put("orgName", "%" + orgName + "%");
		}

		String isEnabled = (String) searchParams.get("EQ_isEnabled");
		if (StringUtils.isNotBlank(isEnabled)) {
			sql.append("  and s.is_Enabled = :isEnabled ");
			params.put("isEnabled", isEnabled);
		}

		String auditStatus = (String) searchParams.get("EQ_auditStatus");
		if (StringUtils.isNotBlank(auditStatus)) {
			sql.append("  and s.audit_Status = :auditStatus ");
			params.put("auditStatus", auditStatus);
		}

		sql.append("  and org.org_type = :orgType");
		sql.append("  start with org.perent_id = :orgId");
		sql.append("  connect by prior org.id = org.perent_id");

		params.put("orgId", searchParams.get("orgId"));
		params.put("orgType", searchParams.get("orgType"));

		return commonDao.queryForCountNative(sql.toString(), params);
	}

	@Override
	public Page<Map<String, Object>> queryAllEnrollStudent(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("查询学习中心参数：{},分页参数:{}", searchParams, pageRequst);
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();

		sql.append("  SELECT org.id \"id\",");
		sql.append("  org.org_name \"orgName\",");
		sql.append("  b.org_name \"parentOrgName\",");
		sql.append("  org.code \"code\",");
		sql.append("  s.link_tel \"linkTel\",");
		sql.append("  s.linkman \"linkman\",");
		sql.append("  s.district,");
		sql.append("  s.service_area,");
		sql.append("  s.office_Addr,");
		sql.append("  s.audit_Status \"auditStatus\", ");
		sql.append("  s.is_Enabled \"isEnabled\"");
		sql.append("  from gjt_org org");
		sql.append("  left join gjt_study_center s");
		sql.append("  on org.id = s.id");
		sql.append("  and s.is_deleted = 'N'");
		sql.append("  left join gjt_org b");
		sql.append("  on org.perent_id = b.id");
		sql.append("  where org.is_deleted = 'N'");

		String parentOrgName = (String) searchParams.get("LIKE_parentGjtOrg.orgName");
		if (StringUtils.isNotBlank(parentOrgName)) {
			sql.append("  and b.org_name like :parentOrgName ");
			params.put("parentOrgName", "%" + parentOrgName + "%");
		}

		String code = (String) searchParams.get("LIKE_code");
		if (StringUtils.isNotBlank(code)) {
			sql.append("  and org.code like :code ");
			params.put("code", "%" + code + "%");
		}

		String orgName = (String) searchParams.get("LIKE_orgName");
		if (StringUtils.isNotBlank(orgName)) {
			sql.append("  and org.org_name like :orgName ");
			params.put("orgName", "%" + orgName + "%");
		}

		String isEnabled = (String) searchParams.get("EQ_isEnabled");
		if (StringUtils.isNotBlank(isEnabled)) {
			sql.append("  and s.is_Enabled = :isEnabled ");
			params.put("isEnabled", isEnabled);
		}

		String auditStatus = (String) searchParams.get("EQ_auditStatus");
		if (StringUtils.isNotBlank(auditStatus)) {
			sql.append("  and s.audit_Status = :auditStatus ");
			params.put("auditStatus", auditStatus);
		}

		sql.append("  and org.org_type = '6'");
		sql.append("  start with org.perent_id = :orgId");
		sql.append("  connect by prior org.id = org.perent_id");

		params.put("orgId", searchParams.get("orgId"));

		Page<Map<String, Object>> page = commonDao.queryForPageNative(sql.toString(), params, pageRequst);
		return page;
	}

	@Override
	public List<Map<String, String>> queryOrgByOrgType(String orgType, String orgId) {
		log.info("下拉父级机构参数：{},分页参数:{}", orgType, orgId);
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();

		sql.append("  select org.id ID,org.org_name NAME");
		sql.append("  from gjt_org org");
		sql.append("  where org.is_deleted = 'N'");
		sql.append("  and org.is_Enabled = '1'");
		sql.append("  and org.org_type = :orgType ");
		sql.append("  start with org.id = :orgId");
		sql.append("  connect by prior org.id = org.perent_id");

		params.put("orgId", orgId);
		params.put("orgType", orgType);
		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	@Override
	public GjtOrg queryStudyCenterInfo(String xxzxId, String orgType) {
		return gjtOrgDao.queryStudyCenterInfo(xxzxId, "3");
	}

	@Override
	public GjtOrg queryByName(String name) {
		return gjtOrgDao.findByOrgNameAndIsDeleted(name, Constants.BOOLEAN_NO);
	}

}
