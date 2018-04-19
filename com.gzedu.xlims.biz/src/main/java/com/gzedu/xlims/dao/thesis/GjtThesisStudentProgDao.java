package com.gzedu.xlims.dao.thesis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.thesis.repository.GjtThesisStudentProgRepository;
import com.gzedu.xlims.pojo.thesis.GjtThesisStudentProg;

@Repository
public class GjtThesisStudentProgDao {

	@Autowired
	private GjtThesisStudentProgRepository gjtThesisStudentProgRepository;
	
	public Page<GjtThesisStudentProg> findAll(Specification<GjtThesisStudentProg> spec, PageRequest pageRequst) {
		return gjtThesisStudentProgRepository.findAll(spec, pageRequst);
	}
	
	public GjtThesisStudentProg save(GjtThesisStudentProg entity) {
		return gjtThesisStudentProgRepository.save(entity);
	}
	
	public void save(List<GjtThesisStudentProg> entities) {
		gjtThesisStudentProgRepository.save(entities);
	}
	
	public GjtThesisStudentProg findOne(String id) {
		return gjtThesisStudentProgRepository.findOne(id);
	}
	
	public void deleteByThesisPlanIdAndStudentId(String thesisPlanId, String studentId) {
		gjtThesisStudentProgRepository.deleteByThesisPlanIdAndStudentId(thesisPlanId, studentId);
	}
	
	public List<GjtThesisStudentProg> findByThesisPlanIdAndStudentId(String thesisPlanId, String studentId) {
		return gjtThesisStudentProgRepository.findByThesisPlanIdAndStudentIdOrderByCreatedDtAsc(thesisPlanId, studentId);
	}

}
