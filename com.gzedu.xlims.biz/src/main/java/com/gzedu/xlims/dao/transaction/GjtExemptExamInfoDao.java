package com.gzedu.xlims.dao.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtExemptExamInfo;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月28日
 * @version 2.5
 */
public interface GjtExemptExamInfoDao extends JpaRepository<GjtExemptExamInfo, String>, JpaSpecificationExecutor<GjtExemptExamInfo>{

	List<GjtExemptExamInfo> findByStudentIdAndCourseIdOrderByCreatedDtDesc(String studentId, String courseId);

}
