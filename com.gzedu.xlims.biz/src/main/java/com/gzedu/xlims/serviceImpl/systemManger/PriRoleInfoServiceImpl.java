/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.systemManger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.dictionary.TblSysDataDao;
import com.gzedu.xlims.dao.model.PriRoleInfoDao;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.pojo.PriRoleInfoRun;
import com.gzedu.xlims.pojo.TblSysData;
import com.gzedu.xlims.pojo.status.RoleType;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月18日
 * @version 2.5
 *
 */
@Service
public class PriRoleInfoServiceImpl implements PriRoleInfoService {

	@Autowired
	PriRoleInfoDao priRoleInfoDao;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	CommonDao commonDao;

	@Autowired
	TblSysDataDao tblSysDataDao;

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Object> hashOps;

	@Override
	public List<PriRoleInfo> queryOrgMagagerRoles() {
		List<String> roleCodes = new ArrayList<String>();
		roleCodes.add(RoleType.院校管理员.getCode());
		roleCodes.add(RoleType.分校管理员.getCode());
		roleCodes.add(RoleType.教务管理员.getCode());
		roleCodes.add(RoleType.考务管理员.getCode());
		roleCodes.add(RoleType.教学点管理员.getCode());

		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("roleCode", new SearchFilter("roleCode", Operator.IN, roleCodes));
		Specification<PriRoleInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), PriRoleInfo.class);
		return priRoleInfoDao.findAll(spec);
	}

	@Override
	public Page<PriRoleInfo> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		searchParams.put("EQ_isdeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<PriRoleInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), PriRoleInfo.class);
		return priRoleInfoDao.findAll(spec, pageRequst);
	}

	@Override
	public PriRoleInfo queryById(String id) {
		return priRoleInfoDao.findOne(id);
	}

	@Override
	public void insert(PriRoleInfo entity) {
		entity.setRoleId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsdeleted("N");
		priRoleInfoDao.save(entity);

		// 更新字典表以及缓存
		if (entity.getIsMng()) {
			TblSysData data = new TblSysData();
			data.setId(UUIDUtils.random());
			data.setTypeCode("AdminRole");
			data.setTypeName("管理员角色");
			data.setName(entity.getRoleName());
			data.setCode(entity.getRoleId());
			data.setDescription(entity.getRoleName());
			data.setIsDeleted("N");

			List<TblSysData> tblSysDatas = tblSysDataDao.findByIsDeletedAndTypeCodeOrderByOrdNoAsc("N", "AdminRole");
			if (tblSysDatas != null && tblSysDatas.size() > 0) {
				TblSysData data2 = tblSysDatas.get(tblSysDatas.size() - 1);
				data.setOrdNo(data2.getOrdNo().add(new BigDecimal(1)));
			} else {
				data.setOrdNo(new BigDecimal(1));
			}

			tblSysDataDao.save(data);
			hashOps.delete("Dictionary", "AdminRole");
		}
	}

	@Override
	public void delete(String id) {
		priRoleInfoDao.delete(id);
	}

	@Override
	public void delete(Iterable<String> ids) {
		// priRoleInfoDao.delete(priRoleInfoDao.findAll(ids));
		Iterable<PriRoleInfo> priRoleInfos = priRoleInfoDao.findAll(ids);
		if (priRoleInfos != null) {
			Iterator<PriRoleInfo> iterator = priRoleInfos.iterator();
			while (iterator.hasNext()) {
				PriRoleInfo priRoleInfo = iterator.next();
				priRoleInfo.setIsdeleted("Y");
			}

			priRoleInfoDao.save(priRoleInfos);
		}

		List<TblSysData> sysDatas = tblSysDataDao.findByIsDeletedAndTypeCodeAndCodeIn("N", "AdminRole", ids);
		if (sysDatas != null) {
			tblSysDataDao.delete(sysDatas);
			hashOps.delete("Dictionary", "AdminRole");
		}
	}

	@Override
	public void update(PriRoleInfo entity) {
		entity.setUpdatedDt(new Date());
		priRoleInfoDao.save(entity);

		// 更新字典表以及缓存
		TblSysData tblSysData = tblSysDataDao.findByIsDeletedAndTypeCodeAndCode("N", "AdminRole", entity.getRoleId());
		if (entity.getIsMng()) {
			if (tblSysData == null) {
				TblSysData data = new TblSysData();
				data.setId(UUIDUtils.random());
				data.setTypeCode("AdminRole");
				data.setTypeName("管理员角色");
				data.setName(entity.getRoleName());
				data.setCode(entity.getRoleId());
				data.setDescription(entity.getRoleName());
				data.setIsDeleted("N");

				List<TblSysData> tblSysDatas = tblSysDataDao.findByIsDeletedAndTypeCodeOrderByOrdNoAsc("N",
						"AdminRole");
				if (tblSysDatas != null && tblSysDatas.size() > 0) {
					TblSysData data2 = tblSysDatas.get(tblSysDatas.size() - 1);
					data.setOrdNo(data2.getOrdNo().add(new BigDecimal(1)));
				} else {
					data.setOrdNo(new BigDecimal(1));
				}

				tblSysDataDao.save(data);
				hashOps.delete("Dictionary", "AdminRole");
			} else {
				tblSysData.setName(entity.getRoleName());
				tblSysData.setDescription(entity.getRoleName());
				tblSysDataDao.save(tblSysData);
				hashOps.delete("Dictionary", "AdminRole");
			}
		} else {
			if (tblSysData != null) {
				tblSysDataDao.delete(tblSysData);
				hashOps.delete("Dictionary", "AdminRole");
			}
		}
	}

	@Override
	public List<PriRoleInfo> queryAll() {
		return priRoleInfoDao.findAlls();
	}

	@Override
	public List<PriRoleInfo> queryAll(Iterable<String> ids) {
		return (List<PriRoleInfo>) priRoleInfoDao.findAll(ids);
	}

	@Override
	public Map<String, String> queryRoleExcludeAdmin() {
		String sql = "select role_id as id,role_name as name from pri_role_info where role_code != '1' and isdeleted='N' order by role_code";
		return commonMapService.getMap(sql);
	}

	@Override
	public PriRoleInfo queryByName(String roleName) {
		return priRoleInfoDao.findByRoleNameAndIsdeleted(roleName, Constants.BOOLEAN_NO);
	}

	@Override
	public PriRoleInfo queryByCode(String roleCode) {
		return priRoleInfoDao.findByRoleCodeAndIsdeleted(roleCode, Constants.BOOLEAN_NO);
	}

	@Override
	public Page<Map<String, Object>> queryUserRoleClassCourse(Map<String, Object> searchMap, PageRequest pageRequst) {
		Map<String, Object> params = new HashMap<String, Object>();

		String studentId = (String) searchMap.get("studentId");
		String userName = (String) searchMap.get("userName");
		String className = (String) searchMap.get("className");

		StringBuffer sql = new StringBuffer();
		sql.append("  select a.role_id,c.xm real_name,");
		sql.append("  a.login_account,");
		sql.append("  b.role_name,");
		sql.append("  a.id,");
		sql.append("  c.employee_id,");
		sql.append("  e.bjmc,");
		sql.append("  d.kcmc,");
		sql.append("  e.termcourse_id,");
		sql.append("  e.class_id");
		sql.append("  from gjt_user_account a");
		sql.append("  inner join pri_role_info b");
		sql.append("  on a.role_id = b.role_id");
		sql.append("  inner join gjt_employee_info c");
		sql.append("  on a.id = c.account_id");
		sql.append("  inner join gjt_class_info e");
		sql.append("  on c.employee_id = e.bzr_id");
		sql.append("  inner join gjt_class_student f");
		sql.append("  on e.class_id = f.class_id");
		sql.append("  left join gjt_course d");
		sql.append("  on e.course_id = d.course_id");
		sql.append("  and d.is_deleted = 'N'");
		sql.append("  where a.is_deleted = 'N'");
		sql.append("  and c.is_deleted = 'N'");
		sql.append("  and e.is_deleted = 'N'");
		sql.append("  and f.is_deleted = 'N'");
		if (StringUtils.isNotBlank(studentId)) {
			sql.append("  and f.student_id = :studentId");
			params.put("studentId", studentId);
		}
		sql.append("  and e.class_type = 'course'");

		sql.append("  and b.role_id in(:roleId)");
		params.put("roleId", searchMap.get("roleId"));

		if (StringUtils.isNotBlank(userName)) {
			sql.append("  and c.xm like :userName");
			params.put("userName", "%" + userName + "%");
		}

		if (StringUtils.isNotBlank(className)) {
			sql.append("  and e.bjmc like :className");
			params.put("className", "%" + className + "%");
		}

		sql.append("  union");
		sql.append("  select a.role_id,c.xm real_name,");
		sql.append("  a.login_account,");
		sql.append("  b.role_name,");
		sql.append("  a.id,");
		sql.append("  c.employee_id,");
		sql.append("  e.bjmc,");
		sql.append("  d.kcmc,");
		sql.append("  e.termcourse_id,");
		sql.append("  e.class_id");
		sql.append("  from gjt_user_account a");
		sql.append("  inner join pri_role_info b");
		sql.append("  on a.role_id = b.role_id");
		sql.append("  inner join gjt_employee_info c");
		sql.append("  on a.id = c.account_id");
		sql.append("  inner join gjt_class_info e");
		sql.append("  on c.employee_id = e.supervisor_id");
		sql.append("  inner join gjt_class_student f");
		sql.append("  on e.class_id = f.class_id");
		sql.append("  left join gjt_course d");
		sql.append("  on e.course_id = d.course_id");
		sql.append("  and d.is_deleted = 'N'");
		sql.append("  where a.is_deleted = 'N'");
		sql.append("  and c.is_deleted = 'N'");
		sql.append("  and e.is_deleted = 'N'");
		sql.append("  and f.is_deleted = 'N'");

		if (StringUtils.isNotBlank(studentId)) {
			sql.append("  and f.student_id = :studentId");
			params.put("studentId", studentId);
		}
		sql.append("  and e.class_type = 'course'");

		sql.append("  and b.role_id in(:roleId)");
		params.put("roleId", searchMap.get("roleId"));

		if (StringUtils.isNotBlank(userName)) {
			sql.append("  and c.xm like :userName");
			params.put("userName", "%" + userName + "%");
		}

		if (StringUtils.isNotBlank(className)) {
			sql.append("  and e.bjmc like :className");
			params.put("className", "%" + className + "%");
		}
		sql.append("  union");
		sql.append("  select a.role_id,a.real_name,");
		sql.append("  a.login_account,");
		sql.append("  b.role_name,");
		sql.append("  a.id,");
		sql.append("  null,");
		sql.append("  null,");
		sql.append("  null,");
		sql.append("  null,");
		sql.append("  null");
		sql.append("  from gjt_user_account a");
		sql.append("  inner join pri_role_info b");
		sql.append("  on a.role_id = b.role_id");
		sql.append("  where a.is_deleted = 'N'");
		sql.append("  and a.org_id=:orgId");
		// sql.append(" and a.org_id in (select ID from GJT_ORG start with ID =
		// :orgId ");
		// sql.append(" connect by prior ID = PERENT_ID AND PERENT_ID != ID )
		// ");
		sql.append(" and b.role_id not in(");
		sql.append(" 'be60d336bc1946a5a24f88d5ae594b17',");// 辅导教师，上面SQL已经存在
		sql.append(" '699f6f83acf54548bfae7794915a3cf3')");// 督导教师，上面SQL已经存在

		sql.append(" and a.id !=:benshen");// 自身角色
		params.put("benshen", searchMap.get("benshen"));

		sql.append("  and b.role_id in(:roleId)");
		params.put("roleId", searchMap.get("roleId"));

		if (StringUtils.isNotBlank(userName)) {
			sql.append("  and a.real_name like :userName");
			params.put("userName", "%" + userName + "%");
		}

		params.put("orgId", searchMap.get("orgId"));
		Page<Map<String, Object>> page = commonDao.queryForPageNative(sql.toString(), params, pageRequst);
		return page;
	}

	@Override
	public Map<String, String> queryRoles(String grantType, String roleId) {
		Map<String, String> roles = new LinkedHashMap<String, String>();
		if ("0".equals(grantType)) {// 超级管理员全部角色
			List<PriRoleInfo> roleList = this.queryAll();
			for (PriRoleInfo priRoleInfo : roleList) {
				roles.put(priRoleInfo.getRoleId(), priRoleInfo.getRoleName());
			}
		} else {
			PriRoleInfo roleInfo = this.queryById(roleId);
			List<PriRoleInfoRun> priRoleInfoList = roleInfo.getPriRoleInfoList();
			if (priRoleInfoList != null && priRoleInfoList.size() > 0) {
				for (PriRoleInfoRun priRoleInfoRun : priRoleInfoList) {
					PriRoleInfo priRoleInfo = priRoleInfoRun.getPriRoleInfo();
					if (priRoleInfo != null && "N".equals(priRoleInfo.getIsdeleted())) {
						roles.put(priRoleInfo.getRoleId(), priRoleInfo.getRoleName());
					}
				}
			} else {// 如果为空，取自己对应的角色
				roles.put(roleInfo.getRoleId(), roleInfo.getRoleName());
			}
		}
		return roles;
	}

}
