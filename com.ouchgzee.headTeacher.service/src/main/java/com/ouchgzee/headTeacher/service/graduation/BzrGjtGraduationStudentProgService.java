package com.ouchgzee.headTeacher.service.graduation;

import java.util.List;

import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationStudentProg;

/**
 * 学员个人进度
 * @author eenet09
 *
 */
@Deprecated public interface BzrGjtGraduationStudentProgService {
	
	public BzrGjtGraduationStudentProg queryOneByCode(String batchId, String studentId, int progressType, String progressCode);
	
	public void insert(BzrGjtGraduationStudentProg entity);

	public void insert(List<BzrGjtGraduationStudentProg> entityList);
	
	public List<BzrGjtGraduationStudentProg> queryListByStudent(String batchId, String studentId, int progressType);
	
	public void delete(List<BzrGjtGraduationStudentProg> entityList);

}
