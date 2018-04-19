package com.ouchgzee.headTeacher.dao.graduation;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationStudentProg;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学员个人进度
 * @author eenet09
 *
 */
@Deprecated @Repository("bzrGjtGraduationStudentProgDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtGraduationStudentProgDao extends BaseDao<BzrGjtGraduationStudentProg, String> {
	
	@Query("SELECT p FROM BzrGjtGraduationStudentProg p where p.gjtGraduationBatch.batchId = ?1 and p.studentId = ?2 and p.progressType = ?3 and p.progressCode = ?4")
	public BzrGjtGraduationStudentProg queryOneByCode(String batchId, String studentId, int progressType, String progressCode);

	@Query("SELECT p FROM BzrGjtGraduationStudentProg p where p.gjtGraduationBatch.batchId = ?1 and p.studentId = ?2 and p.progressType = ?3 order by p.createdDt")
	public List<BzrGjtGraduationStudentProg> queryListByStudent(String batchId, String studentId, int progressType);

}
