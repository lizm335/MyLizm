/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpUtil;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.organization.GjtSchoolInfoDao;
import com.gzedu.xlims.dao.organization.GjtStudyCenterDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.remote.StudyCenter;
import com.gzedu.xlims.service.GjtAreaService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 功能说明：学习中心 实现接口
 * 
 * @author 卢林林 lulinlin@eenet.com
 * @Date 2016年12月28日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class GjtStudyCenterServiceImpl extends BaseDaoImpl implements GjtStudyCenterService {

	private static final Logger log = LoggerFactory.getLogger(GjtStudyCenterServiceImpl.class);

	@Autowired
	private GjtStudyCenterDao gjtStudyCenterDao;

	@Autowired
	private GjtSchoolInfoDao gjtSchoolInfoDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	protected EntityManager em;

	@Autowired
	GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	GjtOrgDao gjtOrgDao;

	@Autowired
	GjtAreaService gjtAreaService;

	@Resource(name = "transactionManager")
	private JpaTransactionManager tm;

	/**
	 * 查询全部适用于下拉
	 */
	@Override
	public List<GjtStudyCenter> queryAll() {
		return gjtStudyCenterDao.findAll();
	}

	@Override
	public GjtStudyCenter queryById(String id) {
		return gjtStudyCenterDao.findOne(id);
	}

	@Override
	public Page<GjtStudyCenter> queryAll(GjtUserAccount user, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		if (!"0".equals(user.getGrantType())) {// 超级管理员可以看到全部
			// filters.put("gjtOrg.treeCode", new
			// SearchFilter("gjtOrg.treeCode", Operator.LIKE,
			// MatchMode.START.toMatchString(user.getGjtOrg().getTreeCode())));
			String xxId = user.getGjtOrg().getId();
			if (StringUtils.isNotBlank(xxId)) {
				List<String> orgList = gjtOrgDao.queryChildsByParentId(xxId);
				filters.put("gjtStudyCenter.id", new SearchFilter("gjtOrg.id", Operator.IN, orgList));
			}
		}
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("gjtOrg.orgType", new SearchFilter("gjtOrg.orgType", Operator.EQ, "3"));
		Specification<GjtStudyCenter> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtStudyCenter.class);
		Page<GjtStudyCenter> page = gjtStudyCenterDao.findAll(spec, pageRequst);
		if (EmptyUtils.isNotEmpty(page)) {
			for (GjtStudyCenter studyCenter : page) {
				String code = studyCenter.getDistrict();
				if (EmptyUtils.isNotEmpty(code)) {
					studyCenter.setDistrictName(gjtAreaService.queryAreaName(code));
				}
			}
		}

		// List<GjtStudyCenter> studyCenters = page.getContent();
		//
		// Map<String, Object> studyCenterStudentMap =
		// queryStudyCenterStudentSize(studyCenters);
		// for (GjtStudyCenter gjtStudyCenter : studyCenters) {
		// if (studyCenterStudentMap.get(gjtStudyCenter.getId()) != null) {
		// gjtStudyCenter.setGjtStudentInfosSize(
		// Integer.valueOf(studyCenterStudentMap.get(gjtStudyCenter.getId()).toString()));
		// }
		// }
		return page;
	}

	/* 废弃 */
	@Deprecated
	public Map<String, StudyCenter> getStudyCenterMap() {
		String jsonStr = HttpUtil.postData("http://eomp.oucnet.cn/interface/learncenter/getLearncenter.do", "", "GBK");
		JSONObject result = JSONObject.fromObject(jsonStr);
		Map<String, StudyCenter> studyCenters = new HashMap<String, StudyCenter>();

		if (result.get("result").equals("1")) {

			JSONArray jsonArray = JSONArray.fromObject(result.get("data"));
			for (Object obj1 : jsonArray) {
				StudyCenter sc = (StudyCenter) JSONObject.toBean((JSONObject) obj1, StudyCenter.class);
				studyCenters.put(sc.getLEARNCENTER_ID(), sc);
			}

		}
		return studyCenters;
	}

	/*
	 * @Override public void setSchoolInfo(GjtStudyCenter gjtStudyCenter, GjtOrg
	 * gjtSchoolInfo) { if (gjtStudyCenter.getGjtSchoolInfos() != null) {
	 * gjtStudyCenter.getGjtSchoolInfos().add(gjtSchoolInfo); } else {
	 * List<GjtOrg> gjtSchoolInfos = new ArrayList<GjtOrg>();
	 * gjtSchoolInfos.add(gjtSchoolInfo);
	 * gjtStudyCenter.setGjtSchoolInfos(gjtSchoolInfos); }
	 * gjtStudyCenterDao.save(gjtStudyCenter); }
	 */

	@Override
	public void setSchoolInfo(GjtStudyCenter gjtStudyCenter, GjtSchoolInfo gjtSchoolInfo) {
		gjtStudyCenterDao.save(gjtStudyCenter);
	}

	@Override
	public Map<String, Object> queryStudyCenterStudentSize(List<GjtStudyCenter> studyCenters) {
		String ids = "";
		for (GjtStudyCenter gjtStudyCenter : studyCenters) {
			ids += "'" + gjtStudyCenter.getId() + "',";
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (ids.length() > 0) {
			ids = ids.substring(0, ids.length() - 1);
			String sql = "select xxzx_id id,count(1) count from GJT_STUDENT_INFO where xxzx_id in (" + ids
					+ ") group by xxzx_id";
			Query query = em.createNativeQuery(sql);
			query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List<Object> rows = query.getResultList();
			for (Object obj : rows) {
				Map<String, Object> m = (Map<String, Object>) obj;
				resultMap.put((String) m.get("ID"), m.get("COUNT"));
			}
		}
		return resultMap;
	}

	@Override
	public GjtSchoolInfo queryByOrgId(String id) {
		return gjtSchoolInfoDao.findOne(id);
	}

	@Override
	public boolean insertStudyCenter(GjtUserAccount user, GjtStudyCenter entity, String suoShuXxId,
			String suoShuXxzxId) {
		boolean result = true;
		try {

			// 新增学习中心
			String id = UUIDUtils.random();
			// 新增组织机构表中间表关系
			GjtOrg org = new GjtOrg();
			org.setId(id);
			org.setIsDeleted("N");
			org.setIsEnabled("1");
			org.setVersion(BigDecimal.ONE);
			org.setCreatedDt(new Date());
			org.setOrgType(entity.getCenterType());
			org.setOrgName(entity.getScName());
			org.setCode(entity.getScCode());
			GjtOrg parentOrg = null;
			if ("6".equals(entity.getCenterType())) {// 招生点父节点是学习中心
				parentOrg = gjtOrgDao.findOne(suoShuXxzxId);
				org.setParentGjtOrg(parentOrg);
			}

			if ("3".equals(entity.getCenterType())) {// 学习中心父节点是院校
				parentOrg = gjtOrgDao.findOne(suoShuXxId);
				org.setParentGjtOrg(parentOrg);
			}

			org.setSchoolModel(parentOrg.getSchoolModel()); // 办学模式和父机构相同
			entity.setId(id);
			entity.setScCode(entity.getScCode());
			entity.setScName(entity.getScName());
			entity.setDistrict(entity.getDistrict());
			entity.setOfficeAddr(entity.getOfficeAddr());
			entity.setLinkTel(entity.getLinkTel());
			entity.setLinkman(entity.getLinkman());
			entity.setMemo(entity.getMemo());
			entity.setIsDeleted("N");
			entity.setIsEnabled("0");
			entity.setAuditStatus("0");
			entity.setCreatedDt(new Date());
			if (user != null) {
				entity.setCreatedBy(user.getRealName());
			}
			entity.setGjtOrg(org);

			tm.setNestedTransactionAllowed(true);
			tm.setRollbackOnCommitFailure(true);
			GjtStudyCenter save = gjtStudyCenterDao.save(entity);
			// em.flush();
			// gjtStudyCenterDao.save(entity);

			// if (save != null) {
			// entity.setId(id);
			// entity.setScCode(entity.getScCode());
			// entity.setScName(entity.getScName());
			// entity.setDistrict(entity.getDistrict());
			// entity.setOfficeAddr(entity.getOfficeAddr());
			// entity.setLinkTel(entity.getLinkTel());
			// entity.setLinkman(entity.getLinkman());
			// entity.setMemo(entity.getMemo());
			// entity.setIsDeleted("N");
			// entity.setIsEnabled("1");
			// entity.setCreatedDt(new Date());
			// entity.setCreatedBy(user.getRealName());
			// gjtStudyCenterDao.save(entity);
			// }
			/*
			 * Map<String,Object> param1 = new HashMap<String, Object>();
			 * StringBuffer sql1 = new StringBuffer(); sql1.append(
			 * " INSERT INTO GJT_ORG (ID, CODE, ORG_NAME, TREE_CODE, PERENT_ID, ORG_TYPE, ORG_RESPONSIBILITY, MEMO, CREATED_DT, CREATED_BY, IS_DELETED,"
			 * ); sql1.append(" VERSION, IS_ENABLED, SOURCE_, ID_, EXTR_CODE)");
			 * sql1.append(
			 * " VALUES (:ID, :CODE, :ORG_NAME, :TREE_CODE, :PERENT_ID, :ORG_TYPE, :ORG_RESPONSIBILITY, :MEMO,:CREATED_DT, :CREATED_BY,:IS_DELETED,:VERSION,:IS_ENABLED,:SOURCE_, :ID_, :EXTR_CODE)"
			 * );
			 * 
			 * param1.put("ID",org.getId()); param1.put("CODE",org.getCode());
			 * param1.put("ORG_NAME",org.getOrgName());
			 * param1.put("TREE_CODE",org.getTreeCode());
			 * param1.put("PERENT_ID",org.getParentGjtOrg().getId());
			 * param1.put("ORG_TYPE",org.getOrgType());
			 * param1.put("ORG_RESPONSIBILITY",org.getOrgResponsibility());
			 * param1.put("MEMO",org.getMemo()); param1.put("CREATED_DT",new
			 * Date()); param1.put("CREATED_BY",user.getId());
			 * param1.put("IS_DELETED",org.getIsDeleted());
			 * param1.put("VERSION",org.getVersion());
			 * param1.put("IS_ENABLED",org.getIsEnabled());
			 * param1.put("SOURCE_",org.getSource());
			 * param1.put("ID_",org.getId_());
			 * param1.put("EXTR_CODE",org.getExtrCode());
			 * commonDao.insertForMapNative(sql1.toString(),param1);
			 * 
			 * Map<String,Object> param2 = new HashMap<String, Object>();
			 * StringBuffer sql2 = new StringBuffer(); sql2.append(
			 * " INSERT INTO GJT_STUDY_CENTER (ID, SC_CODE, SC_NAME, IS_VALID, OFFICE_ADDR, OFFICE_TEL, LEADER, LINKMAN, LINK_TEL,"
			 * ); sql2.append(
			 * " DISTRICT, MEMO, IS_DELETED, CREATED_BY, CREATED_DT, VERSION, AUDIT_STATUS, AUDIT_OPINION, IS_ENABLED, SERVICE_AREA, CENTER_TYPE)"
			 * ); sql2.append(
			 * " VALUES (:ID,:SC_CODE,:SC_NAME,:IS_VALID,:OFFICE_ADDR, :OFFICE_TEL, :LEADER, :LINKMAN, :LINK_TEL,"
			 * ); sql2.append(
			 * " :DISTRICT, :MEMO, :IS_DELETED, :CREATED_BY, :CREATED_DT, :VERSION, :AUDIT_STATUS, :AUDIT_OPINION, :IS_ENABLED, :SERVICE_AREA, :CENTER_TYPE)"
			 * );
			 * 
			 * param2.put("ID",entity.getId());
			 * param2.put("SC_CODE",entity.getScCode());
			 * param2.put("IS_VALID",entity.getIsValid());
			 * param2.put("OFFICE_ADDR",entity.getOfficeAddr());
			 * param2.put("OFFICE_TEL",entity.getOfficeTel());
			 * param2.put("LEADER",entity.getLeader());
			 * param2.put("LINKMAN",entity.getLinkman());
			 * param2.put("LINK_TEL",entity.getLinkTel());
			 * param2.put("DISTRICT",entity.getDistrict());
			 * param2.put("MEMO",entity.getMemo());
			 * param2.put("IS_DELETED",entity.getIsDeleted());
			 * param2.put("CREATED_BY",user.getId());
			 * param2.put("CREATED_DT",new Date());
			 * param2.put("VERSION",entity.getVersion());
			 * param2.put("AUDIT_STATUS",entity.getAuditStatus());
			 * param2.put("AUDIT_OPINION",entity.getAuditOpinion());
			 * param2.put("IS_ENABLED",entity.getIsEnabled());
			 * param2.put("SERVICE_AREA",entity.getServiceArea());
			 * param2.put("CENTER_TYPE",entity.getCenterType());
			 * commonDao.insertForMapNative(sql2.toString(),param2);
			 */
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	@Override
	public Page<GjtStudyCenter> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtStudyCenter> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtStudyCenter.class);
		return gjtStudyCenterDao.findAll(spec, pageRequst);
	}

	@Override
	public boolean update(GjtUserAccount user, GjtStudyCenter entity, String suoShuXxId, String suoShuXxzxId) {
		boolean result = true;
		try {

			GjtStudyCenter modifyStudyCenter = gjtStudyCenterDao.findOne(entity.getId());
			// 修改学习中心信息
			modifyStudyCenter.setScCode(entity.getScCode());
			modifyStudyCenter.setScName(entity.getScName());
			modifyStudyCenter.setDistrict(entity.getDistrict());
			modifyStudyCenter.setOfficeAddr(entity.getOfficeAddr());
			modifyStudyCenter.setLinkTel(entity.getLinkTel());
			modifyStudyCenter.setLinkman(entity.getLinkman());
			modifyStudyCenter.setMemo(entity.getMemo());
			modifyStudyCenter.setServiceArea(entity.getServiceArea());
			modifyStudyCenter.setCenterType(entity.getCenterType());
			// 更新ORG表中学习中心的数据
			GjtOrg gjtOrg = modifyStudyCenter.getGjtOrg();// gjtOrgDao.findOne(entity.getId());
			gjtOrg.setCode(entity.getScCode());
			gjtOrg.setTreeCode(entity.getScCode());
			gjtOrg.setExtrCode(entity.getScCode());
			gjtOrg.setOrgName(entity.getScName());
			gjtOrg.setOrgType(entity.getCenterType());

			if ("6".equals(entity.getCenterType())) {// 招生点更改所属学习中心
				gjtOrg.setParentGjtOrg(gjtOrgDao.findOne(suoShuXxzxId));
			}

			// 招生点升级为学习中心，需要把parentId更改为院校Id
			if ("3".equals(entity.getCenterType()) && !gjtOrg.equals(entity.getCenterType())) {
				gjtOrg.setParentGjtOrg(gjtOrgDao.findOne(suoShuXxId));
			}

			gjtStudyCenterDao.save(modifyStudyCenter);
			// gjtOrgDao.save(gjtOrg);
		} catch (Exception e) {
			log.info("出现异常，开始回滚：" + e);
			result = false;
		}
		return result;
	}

	@Override
	public boolean update(GjtStudyCenter entity) {
		return gjtStudyCenterDao.save(entity) != null;
	}

	@Override
	public void delete(String id) {
		// 删除学习中心表里的数据
		gjtStudyCenterDao.deleteStudyCenterById(id, "Y");
		// 删除院校表中学习中心数据
		gjtOrgDao.deleteGjtOrgById(id, "Y");
	}

	@Override
	public GjtStudyCenter queryByScName(String scName) {
		List<GjtStudyCenter> list = gjtStudyCenterDao.findByScName(scName);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Page<Map> queryAllOrg(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("查询学习中心参数：{},分页参数:{}", searchParams, pageRequst);
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("  select  a.org_type,a.id,b.audit_status,");
		sql.append("  b.sc_code,");
		sql.append("  case");
		sql.append("  when a.org_type = '3' then");
		sql.append("  a.org_name");
		sql.append("  when a.org_type = '6' then");
		sql.append("  '——' || a.org_name");
		sql.append("  else");
		sql.append("  a.org_name");
		sql.append("  end name,");
		sql.append("  b.service_area,");
		sql.append("  (select (select max(gaa.name)");
		sql.append("  from gjt_district gaa");
		sql.append("  where gaa.id like");
		sql.append("  '%' || substr(ga.id, 1, 2) || '0000%') || '-' ||");
		sql.append("  (select max(gaa.name)");
		sql.append("  from gjt_district gaa");
		sql.append("  where gaa.id like");
		sql.append("  '%' || substr(ga.id, 1, 4) || '00%') || '-' ||");
		sql.append("  ga.name area_name");
		sql.append("  from gjt_district ga");
		sql.append("  where b.district = ga.id) districtName,");
		sql.append("  b.link_Tel,");
		sql.append("  b.linkman,");
		sql.append("  (select c.org_name from gjt_org c where org_type='1' ");
		sql.append("  start with c.id = b.id connect by prior c.perent_id = c.id ) schoolName,");
		sql.append("  b.is_enabled,    ");
		sql.append("  level");
		sql.append("  from gjt_org a");
		sql.append("  left join gjt_study_center b");
		sql.append("  on a.id = b.id");

		String sc_code = (String) searchParams.get("LIKE_scCode");
		if (StringUtils.isNotBlank(sc_code)) {
			sql.append("  and b.sc_code like :sc_code ");
			params.put("sc_code", "%" + sc_code + "%");
		}

		String sc_name = (String) searchParams.get("LIKE_scName");
		if (StringUtils.isNotBlank(sc_name)) {
			sql.append("  and b.sc_name like :sc_name");
			params.put("sc_name", "%" + sc_name + "%");
		}

		String isEnabled = (String) searchParams.get("EQ_isEnabled");
		if (StringUtils.isNotBlank(isEnabled)) {
			sql.append("  and b.is_enabled = :isEnabled");
			params.put("isEnabled", isEnabled);
		}

		String auditStatus = (String) searchParams.get("EQ_auditStatus");
		if (StringUtils.isNotBlank(auditStatus)) {
			sql.append("  and b.audit_status = :auditStatus");
			params.put("auditStatus", auditStatus);
		}

		sql.append("  where a.is_deleted = 'N'  and b.is_deleted='N' ");
		sql.append("  start with a.perent_id = :orgId");

		params.put("orgId", searchParams.get("orgId"));

		sql.append("  connect by prior a.id = a.perent_id");
		sql.append(
				" ORDER BY (select REVERSE(to_char(wm_concat(x.id))) from GJT_ORG x START WITH x.id=a.id CONNECT BY PRIOR x.PERENT_ID=x.ID)"); // 层级排序
		Page<Map> page = super.findByPageToMap(sql, params, pageRequst);
		return page;
	}

	@Override
	public long queryOrgCount(Map<String, Object> searchParams) {
		log.info("学习中心列表统计查询参数：{}", searchParams);
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("  select count(*)");
		sql.append("  from gjt_org a");
		sql.append("  left join gjt_study_center b");
		sql.append("  on a.id = b.id");

		String sc_code = (String) searchParams.get("LIKE_scCode");
		if (StringUtils.isNotBlank(sc_code)) {
			sql.append("  and b.sc_code like :sc_code ");
			params.put("sc_code", "%" + sc_code + "%");
		}

		String sc_name = (String) searchParams.get("LIKE_scName");
		if (StringUtils.isNotBlank(sc_name)) {
			sql.append("  and b.sc_name like :sc_name");
			params.put("sc_name", "%" + sc_name + "%");
		}

		String isEnabled = (String) searchParams.get("EQ_isEnabled");
		if (StringUtils.isNotBlank(isEnabled)) {
			sql.append("  and b.is_enabled = :isEnabled");
			params.put("isEnabled", isEnabled);
		}

		String auditStatus = (String) searchParams.get("EQ_auditStatus");
		if (StringUtils.isNotBlank(auditStatus)) {
			sql.append("  and b.audit_status = :auditStatus");
			params.put("auditStatus", auditStatus);
		}

		sql.append("  where a.is_deleted = 'N'");
		sql.append("  and b.is_deleted = 'N'");
		sql.append("  start with a.perent_id = :orgId");
		sql.append("  connect by prior a.id = a.perent_id");
		params.put("orgId", searchParams.get("orgId"));

		return super.countBySqlQuery(sql, params);
	}

	@Override
	public boolean updateAudit(String id, String audit) {
		log.info("学习中心审核修改参数：id={},audit={}", id, audit);
		boolean result = true;
		try {
			GjtStudyCenter modifyStudyCenter = gjtStudyCenterDao.findOne(id);
			modifyStudyCenter.setAuditStatus(audit);
			GjtStudyCenter save = gjtStudyCenterDao.save(modifyStudyCenter);
			if (save == null) {
				result = false;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = false;
		}
		return result;
	}

	@Override
	public boolean insert(GjtStudyCenter entity) {

		entity.setAuditStatus("1");// 审核通过
		entity.setIsEnabled("1");// 启用
		entity.setIsDeleted("N");
		entity.setVersion(new BigDecimal(1.0));
		entity.setCreatedDt(DateUtils.getNowTime());
		log.info("接口新增学习中心Id={},Code={},orgType={}", entity.getId(), entity.getScCode(),
				entity.getGjtOrg().getOrgType());

		// 新增组织机构表中间表关系
		GjtOrg org = new GjtOrg();
		org.setId(entity.getId());
		org.setIsDeleted("N");
		org.setIsEnabled("1");
		org.setVersion(BigDecimal.ONE);
		org.setOrgType(entity.getGjtOrg().getOrgType());
		org.setOrgName(entity.getScName());
		org.setCode(entity.getScCode());
		org.setParentGjtOrg(entity.getGjtOrg().getParentGjtOrg());
		org.setSchoolModel(entity.getGjtOrg().getParentGjtOrg().getSchoolModel()); // 办学模式和父机构相同
		org.setCreatedDt(DateUtils.getNowTime());
		org.setCreatedBy(entity.getCreatedBy());
		org.setSource("api");
		entity.setGjtOrg(org);
		GjtStudyCenter save = gjtStudyCenterDao.save(entity);
		return save == null ? false : true;
	}

	@Override
	public boolean updateEntity(GjtStudyCenter entity) {
		log.info("接口新增学习中心Id={},Code={},orgType={}", entity.getId(), entity.getScCode(),
				entity.getGjtOrg().getOrgType());
		GjtStudyCenter save = gjtStudyCenterDao.save(entity);
		return save == null ? false : true;
	}

}