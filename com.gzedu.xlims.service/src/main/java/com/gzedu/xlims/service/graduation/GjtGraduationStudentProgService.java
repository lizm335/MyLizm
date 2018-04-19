package com.gzedu.xlims.service.graduation;

import java.util.List;

import com.gzedu.xlims.pojo.graduation.GjtGraduationStudentProg;

/**
 * 学员个人进度
 * @author eenet09
 *
 */
public interface GjtGraduationStudentProgService {
	
	public GjtGraduationStudentProg queryOneByCode(String batchId, String studentId, int progressType, String progressCode);
	
	public void insert(GjtGraduationStudentProg entity);

	public void insert(List<GjtGraduationStudentProg> entityList);
	
	public List<GjtGraduationStudentProg> queryListByStudent(String batchId, String studentId, int progressType);
	
	public void delete(List<GjtGraduationStudentProg> entityList);

}
