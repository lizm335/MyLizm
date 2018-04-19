package com.gzedu.xlims.dao.exam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;

public interface GjtExamSubjectNewRepository
		extends JpaRepository<GjtExamSubjectNew, String>, JpaSpecificationExecutor<GjtExamSubjectNew> {

	@Query("select gc from GjtCourse gc where gc.isDeleted = 'N' and gc.xxId = :XXID and gc.courseId not in ("
			+ "select s.courseId from GjtExamSubjectNew s where s.isDeleted = 0 and s.xxId = :XXID)")
	public List<GjtCourse> noSubjectCourseList(@Param("XXID") String xxid);

	// none of use, only a sample
	@Query(nativeQuery = true, value = "select " + "		gc.id " + "	 from "
			+ "		GJT_COURSE gc left join gjt_exam_subject_new gesn on gc.course_id=gesn.course_id " + "	 where "
			+ "		gc.xx_id=:XXID and gc.is_deleted='N' and gesn.subject_id is null")
	public List<String> noSubjectCourseIdList(@Param("XXID") String xxid);

	public GjtExamSubjectNew findByNameAndKch(String name, String kch);
	
	public List<GjtExamSubjectNew> findByTypeAndXxIdAndIsDeleted(int type, String xxId, int isDeleted);
//	public List<GjtExamSubjectNew> findByTypeAndXxIdAndIsDeleted(int type, String xxId, String isDeleted);
	
	GjtExamSubjectNew findByXxIdAndKchAndType(String xxid, String kch, int type);

}
