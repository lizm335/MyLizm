package com.gzedu.xlims.serviceImpl.exam;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.exam.repository.GjtExamPointAppointmentNewRepository;
import com.gzedu.xlims.pojo.exam.GjtExamPointAppointmentNew;
import com.gzedu.xlims.service.exam.GjtExamPointAppointmentNewService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GjtExamPointAppointmentNewServiceImpl extends BaseServiceImpl<GjtExamPointAppointmentNew> implements GjtExamPointAppointmentNewService {

	@Autowired
	private GjtExamPointAppointmentNewRepository gjtExamPointAppointmentNewRepository;

	@Override
	public GjtExamPointAppointmentNew findByExamBatchCodeAndStudentIdAndGjtExamPointNewExamType(String examBatchCode, String studentId, String examType) {
		return gjtExamPointAppointmentNewRepository.findByExamBatchCodeAndStudentIdAndGjtExamPointNewExamTypeAndIsDeleted(examBatchCode, studentId, examType, 0);
	}

	@Override
	protected BaseDao<GjtExamPointAppointmentNew, String> getBaseDao() {
		return this.gjtExamPointAppointmentNewRepository;
	}

	@Override
	public boolean insert(GjtExamPointAppointmentNew entity) {
		entity.setId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted(0);
		return gjtExamPointAppointmentNewRepository.save(entity) != null;
	}

	@Override
	public GjtExamPointAppointmentNew update(GjtExamPointAppointmentNew entity) {
		entity.setUpdatedDt(new Date());
		return gjtExamPointAppointmentNewRepository.save(entity);
	}

}
