package com.gzedu.xlims.service.transaction;

import java.util.List;

import com.gzedu.xlims.pojo.GjtExemptExamProve;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月29日
 * @version 2.5
 */
public interface GjtExemptExamProveService {

	List<GjtExemptExamProve> findByExemptExamId(String exemptExamId);

	List<GjtExemptExamProve> findByExemptExamIdAndStudentId(String exemptExamId, String studentId);

}
