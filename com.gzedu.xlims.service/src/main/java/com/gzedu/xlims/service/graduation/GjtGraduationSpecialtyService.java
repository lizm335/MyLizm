package com.gzedu.xlims.service.graduation;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.graduation.GjtGraduationSpecialty;

public interface GjtGraduationSpecialtyService {
	
	public Page<Map<String, Object>> queryGraduationSpecialtyList(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public GjtGraduationSpecialty queryOneBySpecialty(String batchId, String specialtyId, String trainingLevel);
	
	public void insert(GjtGraduationSpecialty entity);
	
	public void update(GjtGraduationSpecialty entity);
	
	public GjtGraduationSpecialty queryById(String id);

}
