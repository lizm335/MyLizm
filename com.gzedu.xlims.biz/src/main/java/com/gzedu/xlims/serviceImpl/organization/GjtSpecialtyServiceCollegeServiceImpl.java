/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.organization.GjtSpecialtyCollegeDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.college.GjtSpecialtyCollege;
import com.gzedu.xlims.pojo.status.CourseCategory;
import com.gzedu.xlims.pojo.status.ExamUnit;
import com.gzedu.xlims.service.BaseUserService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.edumanage.GjtSpecialtyPlanService;
import com.gzedu.xlims.service.organization.GjtSpecialtyCollegeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * 功能说明：专业--院校模式
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年5月20日
 * @version 3.0
 *
 */

@Service
public class GjtSpecialtyServiceCollegeServiceImpl extends BaseUserService implements GjtSpecialtyCollegeService {

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtSpecialtyCollegeDao gjtSpecialtyCollegeDao;

	@Autowired
	private GjtCourseService gjtCourseService;

	@Autowired
	private GjtSpecialtyPlanService gjtSpecialtyPlanService;

	@Override
	public Page<GjtSpecialtyCollege> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Map<String, String> orgIds = commonMapService.getOrgMapByOrgId(orgId);
		filters.put("orgId", new SearchFilter("orgId", Operator.IN, orgIds.keySet()));
		Specification<GjtSpecialtyCollege> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtSpecialtyCollege.class);
		return gjtSpecialtyCollegeDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtSpecialtyCollege existsSpecialtyByRuleCode(String ruleCode, String orgId) {
		GjtSpecialtyCollege specialtyCollege = gjtSpecialtyCollegeDao.getByRuleCodeAndOrgId(ruleCode, orgId);
		return specialtyCollege;
	}

	@Override
	public GjtSpecialtyCollege save(GjtSpecialtyCollege entity) {
		return gjtSpecialtyCollegeDao.save(entity);
	}

	@Override
	public GjtSpecialtyCollege findOne(String id) {
		return gjtSpecialtyCollegeDao.findOne(id);
	}

	@Transactional
	@Override
	public void saveImportData(List<String[]> datas, GjtUserAccount user) {
		// TODO Auto-generated method stub
		Date now = new Date();
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		Map<String, String> courseTypeMap = commonMapService.getDates("CourseType");// 课程模块
		for (String[] data : datas) {
			String ruleCode = data[0];// 专业规则号
			String name = data[1];// 专业名称
			String level = null;// 专业层次
			for (String key : pyccMap.keySet()) {
				if (data[2].equals(pyccMap.get(key))) {
					level = key;
					break;
				}
			}
			GjtSpecialtyCollege gjtSpecialtyCollege = existsSpecialtyByRuleCode(ruleCode,
					user.getGjtOrg().getId());
			if (gjtSpecialtyCollege == null) {
				gjtSpecialtyCollege = new GjtSpecialtyCollege();
				gjtSpecialtyCollege.setSpecialtyId(UUIDUtils.random());
				gjtSpecialtyCollege.setRuleCode(ruleCode);
				gjtSpecialtyCollege.setName(name);
				gjtSpecialtyCollege.setSpecialtyLevel(level);
				gjtSpecialtyCollege.setCreatedBy(user.getId());
				gjtSpecialtyCollege.setCreatedDt(now);
				gjtSpecialtyCollege.setOrgId(user.getGjtOrg().getId());
				save(gjtSpecialtyCollege);
			}

			String courseCode = data[4];// 课程代码
			String courseName = data[5];// 课程名称
			int courseCategory = CourseCategory.valueOf(data[6]).getNum();
			int courseType = "统设".equals(data[7]) ? 0 : 1;// 课程类型,0统设，1非统设
			int hours = Integer.parseInt(data[8]);// 学时
			// 先在国开实验学院中查询课程，不存在 再从本学院中查询
			List<GjtCourse> courses = gjtCourseService.findByKchAndXxId(courseCode, WebConstants.GK_ORG_ID);
			if (CollectionUtils.isEmpty(courses))
				courses = gjtCourseService.findByKchAndXxId(courseCode, user.getGjtOrg().getId());
			GjtCourse course = null;
			if (CollectionUtils.isNotEmpty(courses)) {
				course = courses.get(0);
			}
			if (course == null) {// 不存在就新增课程
				course = new GjtCourse();
				if ("1".equals(user.getGjtOrg().getOrgType()) || user.getGjtOrg().getParentGjtOrg() == null) {
					course.setXxId(user.getGjtOrg().getId());
				} else {
					course.setXxId(user.getGjtOrg().getParentGjtOrg().getId());
				}
				course.setKch(courseCode);
				course.setPycc(level);
				course.setKcmc(courseName);
				course.setHour(hours);
				course.setYxId(user.getGjtOrg().getId());
				course.setGjtOrg(user.getGjtOrg());
				course.setOrgCode(user.getGjtOrg().getCode());
				course.setCreatedBy(user.getId());
				gjtCourseService.insert(course);
			}


			int termCode = Integer.valueOf(data[9]);// 学期
			String courseModel = null;// 课程模块
			for (String key : courseTypeMap.keySet()) {
				if (data[3].equals(courseTypeMap.get(key))) {
					courseModel = key;
					break;
				}
			}
			int examUnit = ExamUnit.getCodeByName(data[10]);// 考试单位
			String examPaperNum = data[11];// 试卷号
			GjtSpecialtyPlan plan = new GjtSpecialtyPlan();
			plan.setXxId(user.getGjtOrg().getId());
			plan.setCreatedBy(user.getId());
			plan.setCreatedDt(now);
			plan.setSpecialtyId(gjtSpecialtyCollege.getSpecialtyId());
			plan.setCourseCategory(courseCategory);
			plan.setCoursetype(courseType);
			plan.setCourseId(course.getCourseId());
			plan.setCourseTypeId(courseModel);
			plan.setExamUnit(examUnit);
			plan.setHours(hours);
			plan.setTermTypeCode(termCode);
			plan.setExamPaperNum(examPaperNum);
			gjtSpecialtyPlanService.insert(plan);
		}
	}

}
