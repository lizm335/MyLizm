package com.gzedu.xlims.service.transaction;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtExemptExamInstall;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月22日
 * @version 2.5
 */
public interface GjtExemptExamInstallService {

	Page<GjtExemptExamInstall> queryAll(String xxId, Map<String, Object> searchParams, PageRequest pageRequst);

	void insert(GjtExemptExamInstall install);

	GjtExemptExamInstall findByInstallId(String installId);

	Boolean saveEntity(GjtExemptExamInstall install);

	int deleteInstall(String installId);
	
	List<GjtExemptExamInstall> findByXxIdAndIsDeletedAndStatus(String xxId, String booleanNo,String status);

	List<GjtExemptExamInstall> findByXxIdAndCourseIdAndIsDeleted(String xxId, String courseId, String booleanNo);

}
