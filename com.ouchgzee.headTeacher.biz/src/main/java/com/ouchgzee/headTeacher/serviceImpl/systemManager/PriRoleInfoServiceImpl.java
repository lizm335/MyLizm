/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.serviceImpl.systemManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.dao.model.PriRoleInfoDao;
import com.ouchgzee.headTeacher.pojo.BzrPriRoleInfo;
import com.ouchgzee.headTeacher.pojo.status.RoleType;
import com.ouchgzee.headTeacher.service.usermanage.BzrPriRoleInfoService;

/**
 *
 * 功能说明：
 *
 * @author 李明 liming@eenet.com
 * @Date 2016年5月18日
 * @version 2.5
 *
 */
@Deprecated @Service("bzrPriRoleInfoServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class PriRoleInfoServiceImpl implements BzrPriRoleInfoService {

	@Autowired
	PriRoleInfoDao priRoleInfoDao;

	@Autowired
	CommonDao commonDao;

	@Override
	public List<BzrPriRoleInfo> queryOrgMagagerRoles() {
		List<String> roleCodes = new ArrayList();
		roleCodes.add(RoleType.院校管理员.getCode());
		roleCodes.add(RoleType.分校管理员.getCode());
		roleCodes.add(RoleType.教务管理员.getCode());
		roleCodes.add(RoleType.考务管理员.getCode());
		roleCodes.add(RoleType.教学点管理员.getCode());

		Map<String, SearchFilter> filters = new HashMap();
		filters.put("roleCode", new SearchFilter("roleCode", Operator.IN, roleCodes));
		Specification<BzrPriRoleInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), BzrPriRoleInfo.class);
		return priRoleInfoDao.findAll(spec);
	}

	@Override
	public Page<BzrPriRoleInfo> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrPriRoleInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), BzrPriRoleInfo.class);
		return priRoleInfoDao.findAll(spec, pageRequst);
	}

	@Override
	public BzrPriRoleInfo getOne(String id) {
		return priRoleInfoDao.findOne(id);
	}

	@Override
	public void insert(BzrPriRoleInfo entity) {
		entity.setRoleId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		priRoleInfoDao.save(entity);
	}

	@Override
	public void delete(String id) {
		priRoleInfoDao.delete(id);
	}

	@Override
	public void delete(Iterable<String> ids) {
		priRoleInfoDao.delete(priRoleInfoDao.findAll(ids));
	}

	@Override
	public void update(BzrPriRoleInfo entity) {
		entity.setUpdatedDt(new Date());
		priRoleInfoDao.save(entity);
	}

	@Override
	public List<BzrPriRoleInfo> queryAll() {
		return priRoleInfoDao.findAll();
	}

	@Override
	public List<BzrPriRoleInfo> queryAll(Iterable<String> ids) {
		return (List<BzrPriRoleInfo>) priRoleInfoDao.findAll(ids);
	}

	@Override
	public Page<Map<String, Object>> queryUserRoleClassCourse(Map<String, Object> searchMap, PageRequest pageRequst) {
		Map<String, Object> params = new HashMap<String, Object>();

		String studentId = (String) searchMap.get("studentId");
		String roleId = (String) searchMap.get("roleId");
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
		sql.append("  e.teach_plan_id,");
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
		if (StringUtils.isNotBlank(roleId)) {
			sql.append("  and b.role_id = :roleId");
			params.put("roleId", roleId);
		}
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
		sql.append("  e.teach_plan_id,");
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
		if (StringUtils.isNotBlank(roleId)) {
			sql.append("  and b.role_id = :roleId");
			params.put("roleId", roleId);
		}
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
		sql.append("  and a.org_id = :orgId");
		sql.append("  and b.role_id not in");
		sql.append("  ('d4b27a66c0a87b010120da231915c223',");// 院长
		sql.append("  'd2b2aa26c02f7bf10d2dda23a91522ab',");// 系统管理员
		sql.append("  'fcf94a20da1c44a1a31f94eafaf4b707',");// 班主任//不查找本身的角色
		sql.append("  'be60d336bc1946a5a24f88d5ae594b17',");// 辅导教师，上面SQL已经存在
		sql.append(" '699f6f83acf54548bfae7794915a3cf3')");// 督导教师，上面SQL已经存在
		if (StringUtils.isNotBlank(roleId)) {
			sql.append("  and b.role_id = :roleId");
			params.put("roleId", roleId);
		}
		if (StringUtils.isNotBlank(userName)) {
			sql.append("  and a.real_name like :userName");
			params.put("userName", "%" + userName + "%");
		}

		params.put("orgId", searchMap.get("orgId"));
		Page<Map<String, Object>> page = commonDao.queryForPageNative(sql.toString(), params, pageRequst);
		return page;
	}

}
