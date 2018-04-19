package com.gzedu.xlims.serviceImpl.textbook;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.edumanage.GjtRecResultDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookArrangeDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookOrderDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookOrderDetailDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.textbook.GjtTextbookArrange;
import com.gzedu.xlims.pojo.textbook.GjtTextbookGrade;
import com.gzedu.xlims.pojo.textbook.GjtTextbookOrder;
import com.gzedu.xlims.pojo.textbook.GjtTextbookOrderDetail;
import com.gzedu.xlims.service.textbook.GjtTextbookArrangeService;

@Service
public class GjtTextbookArrangeServiceImpl implements GjtTextbookArrangeService {

	private static final Log log = LogFactory.getLog(GjtTextbookArrangeServiceImpl.class);

	private static final String String = null;

	@Autowired
	private GjtTextbookArrangeDao gjtTextbookArrangeDao;

	@Autowired
	private GjtTextbookOrderDao gjtTextbookOrderDao;

	@Autowired
	private GjtTextbookOrderDetailDao gjtTextbookOrderDetailDao;

	@Autowired
	private GjtRecResultDao gjtRecResultDao;

	@Autowired
	private CommonDao commonDao;

	@Override
	public Page<GjtTextbookArrange> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtTextbookArrange> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtTextbookArrange.class);
		return gjtTextbookArrangeDao.findAll(spec, pageRequst);
	}

	@Override
	@Transactional
	public GjtTextbookArrange insert(GjtTextbookArrange entity) {
		log.info("entity:[" + entity + "]");
		entity.setArrangeId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		GjtTextbookArrange textbookArrange = gjtTextbookArrangeDao.save(entity);

		// 生成订购表和明细
		GjtTextbookOrder textbookOrder = gjtTextbookOrderDao
				.findByPlanIdAndStatus(entity.getGjtTextbookPlan().getPlanId(), 0);
		if (textbookOrder == null) {
			textbookOrder = new GjtTextbookOrder();
			textbookOrder.setOrderId(UUIDUtils.random());
			textbookOrder.setStatus(0);
			textbookOrder.setCreatedDt(new Date());
			textbookOrder.setCreatedBy(entity.getCreatedBy());
			textbookOrder.setPlanId(entity.getGjtTextbookPlan().getPlanId());

			gjtTextbookOrderDao.save(textbookOrder);
		}

		// 查询学生的选课记录，以此来作为发放教材的依据
		List<Map<String, Object>> recResults = null;
		List<GjtTextbookGrade> gjtTextbookGradeList = entity.getGjtTextbookPlan().getGjtTextbookGradeList();
		// 适用学期
		List<String> textbookGradeIds = new ArrayList<String>();
		for (GjtTextbookGrade item : gjtTextbookGradeList) {
			textbookGradeIds.add(item.getGradeId());
		}

		if (entity.getGjtTextbook().getTextbookType() == 1) {
			// 主教材的发放在教学计划里设置
			List<String> teachPlanIds = gjtTextbookArrangeDao.findTeachPlanIdsByGradeIdAndTextbookId(
					entity.getGjtTextbookPlan().getGradeId(), entity.getGjtTextbook().getTextbookId());

			if (teachPlanIds != null && teachPlanIds.size() > 0) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("teachPlanIds", teachPlanIds);
				params.put("textbookGradeIds", textbookGradeIds);

				// 匹配教学计划和学生表里面的入学学期 NJ字段
				recResults = gjtTextbookArrangeDao.findRecResults(params);
				// recResults = gjtRecResultDao.findByTeachPlanIds(teachPlanIds,
				// textbookGradeIds);
			}
		} else {
			// 复习资料直接关联要发放的课程
			List<GjtCourse> courseList = entity.getGjtCourseList();
			if (courseList != null && courseList.size() > 0) {
				List<String> courseIds = new ArrayList<String>();
				for (GjtCourse course : courseList) {
					courseIds.add(course.getCourseId());
				}

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("courseIds", courseIds);
				params.put("gradeId", entity.getGjtTextbookPlan().getGradeId());
				params.put("textbookGradeIds", textbookGradeIds);
				// 匹配复习资料对应的课程和学生表里面的入学学期 NJ字段
				recResults = gjtTextbookArrangeDao.findRecResultsToReview(params);

				// recResults =
				// gjtRecResultDao.findByGradeAndCourses(entity.getGjtTextbookPlan().getGradeId(),
				// courseIds);
			}
		}

		if (recResults != null && recResults.size() > 0) {
			List<GjtTextbookOrderDetail> textbookOrderDetails = new ArrayList<GjtTextbookOrderDetail>();
			for (Map<String, Object> recResult : recResults) {
				String studentId = (String) recResult.get("STUDENT_ID");
				String courseId = (String) recResult.get("COURSE_ID");
				// 判断学生在此学期下的此课程是否订购过教材
				long details = gjtTextbookOrderDetailDao.findByStudentIdAndCourseIdAndGradeIdAndTextbookId(studentId,
						courseId, entity.getGjtTextbookPlan().getGradeId(), entity.getTextbookId());
				if (details == 0) {
					GjtTextbookOrderDetail textbookOrderDetail = new GjtTextbookOrderDetail();
					textbookOrderDetail.setDetailId(UUIDUtils.random());
					textbookOrderDetail.setGjtTextbookOrder(textbookOrder);
					textbookOrderDetail.setGradeId(entity.getGjtTextbookPlan().getGradeId());
					textbookOrderDetail.setPlanId(entity.getGjtTextbookPlan().getPlanId());
					textbookOrderDetail.setGjtTextbookArrange(entity);
					textbookOrderDetail.setGjtTextbook(entity.getGjtTextbook());
					textbookOrderDetail.setCourseId(courseId);
					textbookOrderDetail.setStudentId(studentId);
					textbookOrderDetail.setNeedDistribute(1);
					textbookOrderDetail.setStatus(0);
					textbookOrderDetail.setCreatedDt(new Date());
					textbookOrderDetail.setCreatedBy(entity.getCreatedBy());

					textbookOrderDetails.add(textbookOrderDetail);
				}
			}

			if (textbookOrderDetails.size() > 0) {
				gjtTextbookOrderDetailDao.save(textbookOrderDetails);
			}

		}

		return textbookArrange;
	}

	@Override
	public void update(GjtTextbookArrange entity) {
		log.info("entity:[" + entity + "]");
		gjtTextbookArrangeDao.save(entity);
	}

	@Override
	@Transactional
	public void insert(List<GjtTextbookArrange> entities) {
		log.info("entities:[" + entities + "]");
		for (GjtTextbookArrange entity : entities) {
			entity.setArrangeId(UUIDUtils.random());
			entity.setCreatedDt(new Date());
		}

		gjtTextbookArrangeDao.save(entities);

		// 生成订购表和明细
		GjtTextbookOrder textbookOrder = gjtTextbookOrderDao
				.findByPlanIdAndStatus(entities.get(0).getGjtTextbookPlan().getPlanId(), 0);
		if (textbookOrder == null) {
			textbookOrder = new GjtTextbookOrder();
			textbookOrder.setOrderId(UUIDUtils.random());
			textbookOrder.setStatus(0);
			textbookOrder.setCreatedDt(new Date());
			textbookOrder.setCreatedBy(entities.get(0).getCreatedBy());
			textbookOrder.setPlanId(entities.get(0).getGjtTextbookPlan().getPlanId());

			gjtTextbookOrderDao.save(textbookOrder);
		}

		for (GjtTextbookArrange entity : entities) {
			// 查询学生的选课记录，以此来作为发放教材的依据
			List<Map<String, Object>> recResults = null;
			List<GjtTextbookGrade> gjtTextbookGradeList = entity.getGjtTextbookPlan().getGjtTextbookGradeList();
			// 适用学期
			List<String> textbookGradeIds = new ArrayList<String>();
			for (GjtTextbookGrade item : gjtTextbookGradeList) {
				textbookGradeIds.add(item.getGradeId());
			}

			if (entity.getGjtTextbook().getTextbookType() == 1) {
				// 主教材的发放在教学计划里设置
				List<String> teachPlanIds = gjtTextbookArrangeDao.findTeachPlanIdsByGradeIdAndTextbookId(
						entity.getGjtTextbookPlan().getGradeId(), entity.getGjtTextbook().getTextbookId());

				if (teachPlanIds != null && teachPlanIds.size() > 0) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("teachPlanIds", teachPlanIds);
					params.put("textbookGradeIds", textbookGradeIds);

					// 匹配教学计划和学生表里面的入学学期 NJ字段
					recResults = gjtTextbookArrangeDao.findRecResults(params);
					// recResults =
					// gjtRecResultDao.findByTeachPlanIds(teachPlanIds,
					// textbookGradeIds);
				}
			} else {
				// 复习资料直接关联要发放的课程
				List<GjtCourse> courseList = entity.getGjtCourseList();
				if (courseList != null && courseList.size() > 0) {
					List<String> courseIds = new ArrayList<String>();
					for (GjtCourse course : courseList) {
						courseIds.add(course.getCourseId());
					}

					Map<String, Object> params = new HashMap<String, Object>();
					params.put("courseIds", courseIds);
					params.put("gradeId", entity.getGjtTextbookPlan().getGradeId());
					params.put("textbookGradeIds", textbookGradeIds);
					// 匹配复习资料对应的课程和学生表里面的入学学期 NJ字段
					recResults = gjtTextbookArrangeDao.findRecResultsToReview(params);
					// recResults =
					// gjtRecResultDao.findByGradeAndCourses(entity.getGjtTextbookPlan().getGradeId(),
					// courseIds);
				}
			}

			if (recResults != null && recResults.size() > 0) {
				List<GjtTextbookOrderDetail> textbookOrderDetails = new ArrayList<GjtTextbookOrderDetail>();
				for (Map<String, Object> recResult : recResults) {
					String studentId = (String) recResult.get("STUDENT_ID");
					String courseId = (String) recResult.get("COURSE_ID");
					// 判断学生在此学期下的此课程是否订购过教材
					long details = gjtTextbookOrderDetailDao.findByStudentIdAndCourseIdAndGradeIdAndTextbookId(
							studentId, courseId, entity.getGjtTextbookPlan().getGradeId(), entity.getTextbookId());
					if (details == 0) {
						GjtTextbookOrderDetail textbookOrderDetail = new GjtTextbookOrderDetail();
						textbookOrderDetail.setDetailId(UUIDUtils.random());
						textbookOrderDetail.setGjtTextbookOrder(textbookOrder);
						textbookOrderDetail.setGradeId(entity.getGjtTextbookPlan().getGradeId());
						textbookOrderDetail.setPlanId(entity.getGjtTextbookPlan().getPlanId());
						textbookOrderDetail.setGjtTextbookArrange(entity);
						textbookOrderDetail.setGjtTextbook(entity.getGjtTextbook());
						textbookOrderDetail.setCourseId(courseId);
						textbookOrderDetail.setStudentId(studentId);
						textbookOrderDetail.setNeedDistribute(1);
						textbookOrderDetail.setStatus(0);
						textbookOrderDetail.setCreatedDt(new Date());
						textbookOrderDetail.setCreatedBy(entity.getCreatedBy());

						textbookOrderDetails.add(textbookOrderDetail);
					}
				}

				if (textbookOrderDetails.size() > 0) {
					gjtTextbookOrderDetailDao.save(textbookOrderDetails);
				}

			}
		}

	}

	@Override
	public GjtTextbookArrange findOne(String id) {
		return gjtTextbookArrangeDao.findOne(id);
	}

	@Override
	@Transactional
	public void delete(GjtTextbookArrange entity) {
		log.info("entity:[" + entity + "]");
		gjtTextbookOrderDetailDao.deleteByArrangeId(entity.getArrangeId());
		gjtTextbookArrangeDao.delete(entity);
	}

	@Override
	public GjtTextbookArrange findByPlanIdAndTextbookId(String planId, String textbookId) {
		return gjtTextbookArrangeDao.findByPlanIdAndTextbookId(planId, textbookId);
	}

	@Override
	public Page<Map<String, Object>> findTextbookArrangeList(Map<String, Object> searchParams, PageRequest pageRequst) {
		return gjtTextbookArrangeDao.findTextbookArrangeList(searchParams, pageRequst);
	}

	@Override
	public void saveArrangeTextbooks(java.lang.String gradeId, List<java.lang.String> textbookIds) {
		gjtTextbookArrangeDao.saveArrangeTextbook(gradeId, textbookIds);
	}

	@Override
	public void removeTextbook(String gradeId, String textbookId) {
		gjtTextbookArrangeDao.removeTextbook(gradeId, textbookId);

	}

}
