package com.ouchgzee.headTeacher.dao.graduation;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationApply;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Deprecated @Repository("bzrGjtGraduationApplyDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtGraduationApplyDao extends BaseDao<BzrGjtGraduationApply, String> {
	
	@Query("SELECT a FROM BzrGjtGraduationApply a where a.isDeleted='N' and a.gjtGraduationBatch.batchId = ?1 and a.gjtStudentInfo.gjtSpecialty.specialtyId = ?2")
	public List<BzrGjtGraduationApply> queryListBySpecialty(String batchId, String specialtyId);
	
	@Query("SELECT a FROM BzrGjtGraduationApply a where a.isDeleted='N' and a.gjtGraduationBatch.batchId = ?1 and a.gjtStudentInfo.studentId = ?2 and a.applyType = ?3")
	public BzrGjtGraduationApply queryOneByStudent(String batchId, String studentId, int applyType);

	@Query("SELECT a FROM BzrGjtGraduationApply a where a.isDeleted='N' and a.gjtGraduationBatch.batchId = ?1 and a.gjtStudentInfo.gjtSpecialty.specialtyId = ?2 and a.applyType = ?3 and a.needDefence = ?4 and a.status in (?5)")
	public List<BzrGjtGraduationApply> queryList(String batchId, String specialtyId, int applyType, int defenceType, Set<Integer> status);

}
