/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.student;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.dao.student.GjtClassInfoDao;
import com.ouchgzee.headTeacher.dto.CourseClassInfoDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import com.ouchgzee.headTeacher.service.student.BzrGjtClassService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;

/**
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月11日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated
@Service("bzrGjtClassServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtClassServiceImpl extends BaseServiceImpl<BzrGjtClassInfo> implements BzrGjtClassService {

	@Autowired
	private GjtClassInfoDao gjtClassInfoDao;

	@Autowired
	private CommonDao commonDao;

	@Override
	protected BaseDao<BzrGjtClassInfo, String> getBaseDao() {
		return gjtClassInfoDao;
	}

	@Override
	public Long countClassByBzr(String employeeId) {
		return gjtClassInfoDao.countClassByBzr(employeeId);
	}

	@Override
	public boolean update(BzrGjtClassInfo classInfo) {
		if (StringUtils.isNoneBlank(classInfo.getClassId())) {
			classInfo.setUpdatedDt(new Date());
			BzrGjtClassInfo info = gjtClassInfoDao.save(classInfo);
			return info != null;
		}
		return false;
	}

	@Override
	public boolean updateCloseClass(String classId, String updatedBy) {
		gjtClassInfoDao.updateEnabled(classId, Constants.BOOLEAN_0, updatedBy, new Date());
		return true;
	}

	@Override
	public boolean updateCloseClass(String[] classIds, String updatedBy) {
		if (classIds != null && classIds.length > 0) {
			for (String classId : classIds) {
				this.updateCloseClass(classId, updatedBy);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean updateReopenClass(String classId, String updatedBy) {
		gjtClassInfoDao.updateEnabled(classId, Constants.BOOLEAN_1, updatedBy, new Date());
		return true;
	}

	@Override
	public boolean updateReopenClass(String[] classIds, String updatedBy) {
		if (classIds != null && classIds.length > 0) {
			for (String classId : classIds) {
				this.updateReopenClass(classId, updatedBy);
			}
			return true;
		}
		return false;
	}

	@Override
	public Page<BzrGjtClassInfo> queryClassInfoByPage(String bzrId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Direction.DESC, "createdDt"));
		}
		Criteria<BzrGjtClassInfo> spec = new Criteria();
		// 设置连接方式，如果是内连接可省略掉
		// spec.setJoinType("gjtGrade", JoinType.INNER);

		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtGrade.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("classType", "teach", true));
		spec.add(Restrictions.eq("bzrId", bzrId, true));
		spec.addAll(Restrictions.parse(searchParams));

		Page<BzrGjtClassInfo> result = gjtClassInfoDao.findAll(spec, pageRequest);

		for (Iterator<BzrGjtClassInfo> iterator = result.iterator(); iterator.hasNext();) {
			BzrGjtClassInfo info = iterator.next();
			Long studentNum = gjtClassInfoDao.countStudent(info.getClassId());
			info.setColStudentNum(studentNum);
			// 如果班级容量为null，则默认为班级学员数
			if (info.getBjrn() == null)
				info.setBjrn(BigDecimal.valueOf(info.getColStudentNum()));
		}
		return result;
	}

	@Override
	public List<BzrGjtClassInfo> queryClassInfoBy(String bzrId, Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Direction.DESC, "createdDt");
		}
		Criteria<BzrGjtClassInfo> spec = new Criteria();
		// 设置连接方式，如果是内连接可省略掉
		// spec.setJoinType("gjtGrade", JoinType.INNER);

		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtGrade.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("classType", "teach", true));
		spec.add(Restrictions.eq("bzrId", bzrId, true));
		spec.addAll(Restrictions.parse(searchParams));

		return gjtClassInfoDao.findAll(spec, sort);
	}

	@Override
	public List<CourseClassInfoDto> queryCourseClassInfoByTeachClassId(String classId) {
		List<CourseClassInfoDto> cciDtoList = new ArrayList<CourseClassInfoDto>();
		List<Object[]> list = gjtClassInfoDao.queryCourseClassInfoByTeachClassId(classId);
		for (Object[] arrs : list) {
			CourseClassInfoDto dto = new CourseClassInfoDto();
			// dto.setClassId((String) arrs[0]);
			dto.setCourseId((String) arrs[0]);
			dto.setKcmc((String) arrs[1]);
			cciDtoList.add(dto);
		}
		return cciDtoList;
	}

	@Override
	public List<CourseClassInfoDto> queryCourseClassInfoByTeachClassIdAndTermId(String classId, String termId) {
		List<CourseClassInfoDto> cciDtoList = new ArrayList<CourseClassInfoDto>();
		List<Object[]> list = gjtClassInfoDao.queryCourseClassInfoByTeachClassIdAndTermId(classId, termId);
		for (Object[] arrs : list) {
			CourseClassInfoDto dto = new CourseClassInfoDto();
			// dto.setClassId((String) arrs[0]);
			dto.setCourseId((String) arrs[0]);
			dto.setKcmc((String) arrs[1]);
			cciDtoList.add(dto);
		}
		return cciDtoList;
	}

	@Override
	public boolean isClassToTeacher(String classId, String employeeId) {
		long count = gjtClassInfoDao.countClassToBzrManage(classId, employeeId);
		return count == 1;
	}

	@Override
	public long countOpenClassNum(String bzrId) {
		Criteria<BzrGjtClassInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("classType", "teach", true));
		spec.add(Restrictions.eq("bzrId", bzrId, true));
		spec.add(Restrictions.eq("isEnabled", Constants.BOOLEAN_1, true));

		return gjtClassInfoDao.count(spec);
	}

	@Override
	public long countCloseClassNum(String bzrId) {
		Criteria<BzrGjtClassInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("classType", "teach", true));
		spec.add(Restrictions.eq("bzrId", bzrId, true));
		spec.add(Restrictions.eq("isEnabled", Constants.BOOLEAN_0, true));

		return gjtClassInfoDao.count(spec);
	}

	/**
	 * 获取学员学情，最低分，学分，已修学分
	 */
	@Override
	public List<Object[]> getStudentSpecialty(String specialtyId, List<String> ids) {
		List<Object[]> resultList = gjtClassInfoDao.getStudentSpecialty(specialtyId, ids);
		return resultList;
	}

	/**
	 * 获取班级学员的studentId
	 */
	@Override
	public List<String> queryTeacherClassStudent(String classId) {
		List<String> resultList = gjtClassInfoDao.queryTeacherClassStudent(classId);
		return resultList;
	}

	/**
	 * 获取班级学员 姓名，头像，班级名称
	 */
	@Override
	public List<Map<String, Object>> queryStudentByClassId(String classId, String searchName) {
		StringBuffer sb = new StringBuffer("select a.account_id id,a.xm,a.avatar,c.bjmc,a.atid,a.sfzh"
				+ " from gjt_student_info a "
				+ " inner join gjt_class_student b on a.student_id=b.student_id "
				+ " inner join gjt_class_info c on b.class_id=c.class_id "
				+ " where a.is_deleted='N' and b.is_deleted='N' and c.is_deleted='N' "
				+ " and c.class_type='teach' and c.class_id=:classId ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("classId", classId);
		if (StringUtils.isNotBlank(searchName)) {
			sb.append(" and a.xm like :xm");
			params.put("xm", "%" + searchName + "%");
		}
		List<Map<String, Object>> list = commonDao.queryForMapListNative(sb.toString(), params);
		return list;
	}

	@Override
	public List<Map<String, Object>> queryClassInfoByTeachId(String bzrId) {
		StringBuffer sb = new StringBuffer("select count(*)classCount,a.class_id,a.bjmc from gjt_class_info a "
				+ " inner join gjt_class_student b on a.class_id=b.class_id "
				+ " where a.is_deleted='N' and b.is_deleted='N' and a.class_type='teach' "
				+ " and a.bzr_id=:bzrId group by a.class_id,a.bjmc");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bzrId", bzrId);
		List<Map<String, Object>> list = commonDao.queryForMapListNative(sb.toString(), params);
		return list;
	}

}
