package com.gzedu.xlims.serviceImpl.textbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.textbook.GjtStudentTextbookOrderDao;
import com.gzedu.xlims.pojo.textbook.GjtStudentTextbookOrder;
import com.gzedu.xlims.service.textbook.GjtStudentTextbookOrderService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

@Service
public class GjtStudentTextbookOrderServiceImpl extends BaseServiceImpl<GjtStudentTextbookOrder> implements GjtStudentTextbookOrderService
{

	private static final Log log = LogFactory.getLog(GjtStudentTextbookOrderServiceImpl.class);

	@Autowired
	private GjtStudentTextbookOrderDao gjtStudentTextbookOrderDao;

	@Override
	protected BaseDao<GjtStudentTextbookOrder, String> getBaseDao() {
		return gjtStudentTextbookOrderDao;
	}

	@Override
	public GjtStudentTextbookOrder getByStudentIdAndGradeId(String studentId, String gradeId) {
		return gjtStudentTextbookOrderDao.getByStudentIdAndGradeId(studentId, gradeId);
	}

	@Override
	public GjtStudentTextbookOrder getByStudentIdAndGradeIdAndIsDeleted(String studentId, String gradeId, String isDeleted) {
		return gjtStudentTextbookOrderDao.getByStudentIdAndGradeIdAndIsDeleted(studentId, gradeId, isDeleted);
	}

}
