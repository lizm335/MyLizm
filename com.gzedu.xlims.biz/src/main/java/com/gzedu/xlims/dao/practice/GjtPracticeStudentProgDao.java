package com.gzedu.xlims.dao.practice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.practice.repository.GjtPracticeStudentProgRepository;
import com.gzedu.xlims.pojo.practice.GjtPracticeStudentProg;

@Repository
public class GjtPracticeStudentProgDao {
	
	@Autowired
	private GjtPracticeStudentProgRepository gjtPracticeStudentProgRepository;
	
	public Page<GjtPracticeStudentProg> findAll(Specification<GjtPracticeStudentProg> spec, PageRequest pageRequst) {
		return gjtPracticeStudentProgRepository.findAll(spec, pageRequst);
	}
	
	public GjtPracticeStudentProg save(GjtPracticeStudentProg entity) {
		return gjtPracticeStudentProgRepository.save(entity);
	}
	
	public void save(List<GjtPracticeStudentProg> entities) {
		gjtPracticeStudentProgRepository.save(entities);
	}
	
	public GjtPracticeStudentProg findOne(String id) {
		return gjtPracticeStudentProgRepository.findOne(id);
	}
	
	public void deleteByPracticePlanIdAndStudentId(String practicePlanId, String studentId) {
		gjtPracticeStudentProgRepository.deleteByPracticePlanIdAndStudentId(practicePlanId, studentId);
	}
	
	public List<GjtPracticeStudentProg> findByPracticePlanIdAndStudentId(String practicePlanId, String studentId) {
		return gjtPracticeStudentProgRepository.findByPracticePlanIdAndStudentIdOrderByCreatedDtAsc(practicePlanId, studentId);
	}

}
