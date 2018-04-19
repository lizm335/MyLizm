package com.gzedu.xlims.service.thesis;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.thesis.GjtThesisDefencePlan;

public interface GjtThesisDefencePlanService {
	
	public Page<GjtThesisDefencePlan> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public GjtThesisDefencePlan insert(GjtThesisDefencePlan entity);

	public void update(GjtThesisDefencePlan entity);
	
	public GjtThesisDefencePlan findOne(String id);

}
