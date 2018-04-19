package com.gzedu.xlims.service.thesis;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.thesis.GjtThesisStudentProg;

public interface GjtThesisStudentProgService {
	
	public Page<GjtThesisStudentProg> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public GjtThesisStudentProg insert(GjtThesisStudentProg entity);

	public GjtThesisStudentProg findOne(String id);
	
	public void deleteByThesisPlanIdAndStudentId(String thesisPlanId, String studentId);
	
	public List<GjtThesisStudentProg> findByThesisPlanIdAndStudentId(String thesisPlanId, String studentId);

}
