package com.gzedu.xlims.dao.graduation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.graduation.GjtGraduationStudentProg;

/**
 * 学员个人进度
 * @author eenet09
 *
 */
public interface GjtGraduationStudentProgDao extends JpaRepository<GjtGraduationStudentProg, String>, JpaSpecificationExecutor<GjtGraduationStudentProg> {
	
	@Query("SELECT p FROM GjtGraduationStudentProg p where p.gjtGraduationBatch.batchId = ?1 and p.studentId = ?2 and p.progressType = ?3 and p.progressCode = ?4")
	public GjtGraduationStudentProg queryOneByCode(String batchId, String studentId, int progressType, String progressCode);

	@Query("SELECT p FROM GjtGraduationStudentProg p where p.gjtGraduationBatch.batchId = ?1 and p.studentId = ?2 and p.progressType = ?3 order by p.createdDt")
	public List<GjtGraduationStudentProg> queryListByStudent(String batchId, String studentId, int progressType);

}
