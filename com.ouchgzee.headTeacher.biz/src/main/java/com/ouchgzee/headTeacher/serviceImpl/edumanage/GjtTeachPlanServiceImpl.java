package com.ouchgzee.headTeacher.serviceImpl.edumanage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ouchgzee.headTeacher.dao.study.GjtTeachPlanDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtTeachPlan;
import com.ouchgzee.headTeacher.service.edumanage.BzrGjtTeachPlanService;

/**
 * 
 * 功能说明：年级专业 实施教学计划
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 *
 */
@Deprecated @Service("bzrGjtTeachPlanServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTeachPlanServiceImpl implements BzrGjtTeachPlanService {
	@Autowired
	private GjtTeachPlanDao gjtTeachPlanDao;

	@Override
	public List<BzrGjtTeachPlan> queryGjtTeachPlan(String gradeId, String gjtSpecialtyId) {
		return gjtTeachPlanDao.findByGradeIdAndKkzyAndIsDeletedOrderByCreatedDtAsc(gradeId, gjtSpecialtyId, "N");
	}

}
