package com.gzedu.xlims.service.exam;

import com.gzedu.xlims.pojo.exam.GjtExamPointAppointmentNew;
import com.gzedu.xlims.service.base.BaseService;

public interface GjtExamPointAppointmentNewService extends BaseService<GjtExamPointAppointmentNew> {

	/**
	 * 按照考试方式查询学员所约考点记录
	 * @param examBatchCode
	 * @param studentId
	 * @param examType
     * @return
     */
	GjtExamPointAppointmentNew findByExamBatchCodeAndStudentIdAndGjtExamPointNewExamType(String examBatchCode, String studentId, String examType);
	
	GjtExamPointAppointmentNew update(GjtExamPointAppointmentNew entity);

}
