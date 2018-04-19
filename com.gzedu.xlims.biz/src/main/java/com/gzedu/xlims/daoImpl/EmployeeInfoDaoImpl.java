/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.daoImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;

/**
 * 教职工信息操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月04日
 * @version 2.5
 * @since JDK 1.7
 */
@Repository
@Transactional(readOnly = true)
public class EmployeeInfoDaoImpl extends BaseDaoImpl {

	private static Logger log = LoggerFactory.getLogger(EmployeeInfoDaoImpl.class);

	@Autowired
	private CommonDao commonDao;

	/**
	 * 获取学校的某类型教职工，SQL语句
	 * 
	 * @param schoolId
	 * @param employeeType
	 * @return
	 */
	public List<Map> queryBySchoolId(String schoolId, Integer employeeType) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"select e.employee_id id,e.xm name from gjt_employee_info e where e.xx_id= :schoolId and e.is_deleted='N' and e.is_enabled='1' and e.employee_type= :employeeType");

		parameters.put("schoolId", schoolId);
		parameters.put("employeeType", employeeType);
		return super.findAllBySql(querySql, parameters, null, Map.class);
	}

	/**
	 * 删除原来的授权
	 * 
	 * @param accountId
	 * @return
	 */
	@Transactional
	public int deleteUserMenuOpt(String accountId) {
		StringBuilder updateSql = new StringBuilder("delete from tbl_pri_user_opt where mgr_id=:accountId");

		Query q = em.createNativeQuery(updateSql.toString());
		q.setParameter("accountId", accountId);
		return q.executeUpdate();
	}

	/**
	 * 新增授权
	 * 
	 * @param params
	 * @return
	 */
	@Transactional
	public int assginSeletorUserMenu(Map<String, Object> params) {
		StringBuilder updateSql = new StringBuilder(
				"insert into TBL_PRI_USER_OPT (USER_OPT_ID, MENU_OPT_ID, MGR_ID, STATUS, REMARK, UPDATED_DT, UPDATED_BY, CREATED_DT, CREATED_BY, IS_DELETED, VERSION)");
		updateSql.append(" values");
		updateSql.append(
				" (:userOptId, :menuOptId, :mgrId, :status, :remark, :updatedDt, :updatedBy, :createdDt, :createdBy, :isDeleted, :version)");

		Query q = em.createNativeQuery(updateSql.toString());
		for (Map.Entry<String, Object> e : params.entrySet()) {
			q.setParameter(e.getKey(), e.getValue());
		}
		return q.executeUpdate();
	}

	/**
	 * 根据班级id查询其班主任
	 * 
	 * @param classId
	 * @return
	 */
	public Map<String, String> queryByClassId(String classId) {

		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();

		sql.append(
				"SELECT gei.EMPLOYEE_ID,gei.XM FROM GJT_CLASS_INFO gci,GJT_EMPLOYEE_INFO gei WHERE gci.BZR_ID=gei.EMPLOYEE_ID AND gci.CLASS_ID=:classId");

		Query query = em.createNativeQuery(sql.toString());
		query.setParameter("classId", classId);
		List list = query.getResultList();
		if (EmptyUtils.isNotEmpty(list)) {
			Object[] objects = (Object[]) list.get(0);
			map.put(String.valueOf(objects[0]), String.valueOf(objects[1]));
		}
		return map;

	}

	/**
	 * 查询班主任信息
	 * 
	 * @param studentId
	 * @return
	 */
	public Map<String, Object> queryHeadTeacherInfo(String studentId) {// as
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder querySql = new StringBuilder();
		querySql.append("select gei.employee_id as \"employeeId\", ");
		querySql.append("gcs.class_id as \"classId\", ");
		querySql.append("gei.xm  as \"userName\",gei.zp as \"photoUrl\",gei.lxdh as \"telPhone\",");
		querySql.append("gei.sjh as \"mobilePhone\",gei.dzxx as \"email\",gei.eeno  as \"eeno\",");
		querySql.append("gei.work_time as \"workTime\", ");
		querySql.append("gei.work_addr as \"workAddr\", ");
		querySql.append("gsc.org_name as \"scName\" ,gei.qq  as \"QQ\",");
		querySql.append("gua.is_online as \"isOnline\" ");
		querySql.append("from gjt_class_info gci inner join gjt_class_student gcs on gci.class_id=gcs.class_id ");
		querySql.append("inner join gjt_employee_info gei on gci.bzr_id=gei.employee_id ");
		querySql.append("inner join gjt_user_account gua on gua.id=gei.account_id ");
		querySql.append("left join gjt_org gsc on gsc.id=gei.xxzx_id and gsc.is_deleted='N'");
		querySql.append("where gci.is_deleted='N' and gcs.is_deleted='N' and ");
		querySql.append("gei.is_deleted='N'  ");
		querySql.append("and gci.class_type='teach' and gcs.student_id=:studentId");
		map.put("studentId", studentId);
		// return commonDao.queryObjectNative(querySql.toString(), map);
		return commonDao.queryObjectToMapNative(querySql.toString(), map);
	}

	/**
	 * 查询教学班中所有辅导老师的ee号
	 * @param teachClassId
	 * @return
	 */
	public List<String> queryFudaoTeachEENosByTeachClass(String teachClassId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder querySql = new StringBuilder();
		querySql.append(" select distinct c.eeno from gjt_class_student a");
		querySql.append(" left join gjt_class_info b on a.class_id=b.class_id and b.is_deleted='N'");
		querySql.append(" inner join gjt_employee_info c on c.employee_id=b.BZR_ID and c.is_deleted='N'");
		querySql.append(" where a.is_deleted = 'N' and b.class_type='course'");
		querySql.append("  and a.student_id in (");
		querySql.append("   select b.student_id from gjt_student_info a");
		querySql.append("   left join gjt_class_student b on a.student_id = b.student_id and b.is_deleted = 'N'");
		querySql.append("   where a.is_deleted = 'N' and b.class_id=:teachClassId");
		querySql.append(" )");
		params.put("teachClassId", teachClassId);
		return commonDao.queryForStringListNative(querySql.toString(), params);
	}

	/**
	 * 查询教学班中所有督导老师的ee号
	 * @param teachClassId
	 * @return
	 */
	public List<String> queryDudaoTeachEENosByTeachClass(String teachClassId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder querySql = new StringBuilder();
		querySql.append(" select distinct c.eeno from gjt_class_student a");
		querySql.append(" left join gjt_class_info b on a.class_id=b.class_id and b.is_deleted='N'");
		querySql.append(" inner join gjt_employee_info c on c.employee_id=b.SUPERVISOR_ID and c.is_deleted='N'");
		querySql.append(" where a.is_deleted = 'N' and b.class_type='course'");
		querySql.append("  and a.student_id in (");
		querySql.append("   select b.student_id from gjt_student_info a");
		querySql.append("   left join gjt_class_student b on a.student_id = b.student_id and b.is_deleted = 'N'");
		querySql.append("   where a.is_deleted = 'N' and b.class_id=:teachClassId");
		querySql.append(" )");
		params.put("teachClassId", teachClassId);
		return commonDao.queryForStringListNative(querySql.toString(), params);
	}

	public static void main(String[] args) {
		StringBuilder querySql = new StringBuilder();
		querySql.append("select gei.employee_id as \"employeeId\", ");
		querySql.append("gcs.class_id as \"classId\", ");
		querySql.append("gei.xm  as \"userName\",gei.zp as \"photoUrl\",gei.lxdh as \"telPhone\",");
		querySql.append("gei.sjh as \"mobilePhone\",gei.dzxx as \"email\",gei.eeno  as \"eeno\",");
		querySql.append("gei.work_time as \"workTime\", ");
		querySql.append("gei.work_addr as \"workAddr\", ");
		querySql.append("gsc.org_name as \"scName\" ,");
		querySql.append("gua.is_online as \"isOnline\" ");
		querySql.append("from gjt_class_info gci inner join gjt_class_student gcs on gci.class_id=gcs.class_id ");
		querySql.append("inner join gjt_employee_info gei on gci.bzr_id=gei.employee_id ");
		querySql.append("inner join gjt_user_account gua on gua.id=gei.account_id ");
		querySql.append("left join gjt_org gsc on gsc.id=gei.xxzx_id and gsc.is_deleted='N'");
		querySql.append("where gci.is_deleted='N' and gcs.is_deleted='N' and ");
		querySql.append("gei.is_deleted='N'  ");
		querySql.append("and gci.class_type='teach' and gcs.student_id=:studentId");
		System.out.println(querySql);
	}

	public GjtEmployeeInfo queryByAccountId(String accountId) {
		StringBuilder sql = new StringBuilder(
				"select * from gjt_employee_info where is_deleted='N' and account_id='" + accountId + "' ");
		Query q = em.createNativeQuery(sql.toString(), GjtEmployeeInfo.class);
		return (GjtEmployeeInfo) q.getSingleResult();
	}
}
