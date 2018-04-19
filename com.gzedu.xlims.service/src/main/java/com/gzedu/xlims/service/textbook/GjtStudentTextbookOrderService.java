package com.gzedu.xlims.service.textbook;

import com.gzedu.xlims.pojo.textbook.GjtStudentTextbookOrder;
import com.gzedu.xlims.service.base.BaseService;

public interface GjtStudentTextbookOrderService extends BaseService<GjtStudentTextbookOrder> {

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年3月1日 下午1:59:50
	 * @param studentId
	 * @param gradeId
	 * @return
	 */
	GjtStudentTextbookOrder getByStudentIdAndGradeId(String studentId, String gradeId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年3月2日 上午9:57:06
	 * @param studentId
	 * @param gradeId
	 * @param isDeleted
	 * @return
	 */
	GjtStudentTextbookOrder getByStudentIdAndGradeIdAndIsDeleted(String studentId, String gradeId, String isDeleted);
}
