package com.gzedu.xlims.dao.openclass;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.openClass.GjtOnlineLessonFile;

public interface GjtOnlineLessonFileDao extends JpaRepository<GjtOnlineLessonFile, String>, JpaSpecificationExecutor<GjtOnlineLessonFile> {

	@Query("from GjtOnlineLessonFile where onlinetutorId=?1 and isDeleted='N'")
	public List<GjtOnlineLessonFile> findLessonFileByOnlinetutorId(String onlinetutorId);
}
