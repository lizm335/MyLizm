package com.gzedu.xlims.dao.thesis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.thesis.repository.GjtThesisDefencePlanRepository;
import com.gzedu.xlims.pojo.thesis.GjtThesisDefencePlan;

@Repository
public class GjtThesisDefencePlanDao {
	
	@Autowired
	private GjtThesisDefencePlanRepository gjtThesisDefencePlanRepository;
	
	public Page<GjtThesisDefencePlan> findAll(Specification<GjtThesisDefencePlan> spec, PageRequest pageRequst) {
		return gjtThesisDefencePlanRepository.findAll(spec, pageRequst);
	}
	
	public GjtThesisDefencePlan save(GjtThesisDefencePlan entity) {
		return gjtThesisDefencePlanRepository.save(entity);
	}
	
	public GjtThesisDefencePlan findOne(String id) {
		return gjtThesisDefencePlanRepository.findOne(id);
	}

}
