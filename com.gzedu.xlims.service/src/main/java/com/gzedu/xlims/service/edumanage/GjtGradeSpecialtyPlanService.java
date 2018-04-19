package com.gzedu.xlims.service.edumanage;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtGradeSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtSpecialty;

/**
 * 
 * 功能说明：教学计划
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 *
 */
public interface GjtGradeSpecialtyPlanService {

	public List<GjtGradeSpecialtyPlan> queryGradeSpecialtyPlan(String gradeId, String specialtyId);

	public boolean createGradeSpecialtyPlan(GjtGrade grade, GjtSpecialty specialty);

	public void removeGradeSpecialtyPlan(String gradeId, String specialtyId);

	public Page<GjtGradeSpecialtyPlan> queryAll(final String schoolId, Map<String, Object> searchParams,
			PageRequest pageRequst);

	public GjtGradeSpecialtyPlan queryBy(String id);

	public void delete(Iterable<String> ids);

	public void delete(String id);

	public void insert(GjtGradeSpecialtyPlan entity);

	public void update(GjtGradeSpecialtyPlan entity);
}
