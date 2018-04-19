package com.gzedu.xlims.serviceImpl.exam;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.ExcelService;
import com.gzedu.xlims.common.Objects;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.exception.ServiceException;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.edumanage.GjtRecResultDao;
import com.gzedu.xlims.dao.edumanage.GjtTeachPlanDao;
import com.gzedu.xlims.dao.edumanage.TeachPlanDao;
import com.gzedu.xlims.dao.exam.GjtExamAppointmentNewDao;
import com.gzedu.xlims.dao.exam.GjtExamCostDao;
import com.gzedu.xlims.dao.exam.GjtExamPlanNewDao;
import com.gzedu.xlims.dao.exam.GjtExamSubjectNewDao;
import com.gzedu.xlims.dao.exam.ViewExamPlanScDao;
import com.gzedu.xlims.dao.exam.repository.GjtExamAppointmentNewRepository;
import com.gzedu.xlims.dao.exam.repository.GjtExamPointAppointmentNewRepository;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtStudyYearInfo;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointNew;
import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;
import com.gzedu.xlims.pojo.exam.ViewExamPlanSc;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.exam.GjtExamAppointmentNewService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

@Service
public class GjtExamAppointmentNewServiceImpl extends BaseServiceImpl<GjtExamAppointmentNew> implements GjtExamAppointmentNewService {

	private static final Log log = LogFactory.getLog(GjtExamAppointmentNewServiceImpl.class);

	@Autowired
	private GjtExamAppointmentNewDao gjtExamAppointmentNewDao;

	@Autowired
	private GjtExamAppointmentNewRepository gjtExamAppointmentNewRepository;
	@Autowired
	private GjtExamPointAppointmentNewRepository gjtExamPointAppointmentNewRepository;
	@Autowired
	private GjtExamSubjectNewDao gjtExamSubjectNewDao;
	@Autowired
	private GjtTeachPlanDao gjtTeachPlanDao;

	@Autowired
	private GjtExamPlanNewDao gjtExamPlanNewDao;
	
	@Autowired
	private GjtRecResultDao gjtRecResultDao;
	
	@Autowired
	private TeachPlanDao teachPlanDao;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Autowired
	private GjtExamCostDao gjtExamCostDao;

    @Autowired
    private CacheService cacheService;

	@Autowired
	private ViewExamPlanScDao viewExamPlanScDao;

	@Autowired
	private CommonMapService commonMapService;

	@Override
	protected BaseDao<GjtExamAppointmentNew, String> getBaseDao() {
		return this.gjtExamAppointmentNewRepository;
	}

	@Override
	public Page<GjtExamAppointmentNew> queryAll(String xxId, Map<String, Object> searchParams, PageRequest pageRequst) {
		/*Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, xxId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, 0));

		Specification<GjtExamAppointmentNew> spec = DynamicSpecifications.bySearchFilterLeft(filters.values(),
				GjtExamAppointmentNew.class);
		return gjtExamAppointmentNewDao.findAll(spec, pageRequst);*/
		
		searchParams.put("EQ_xxId", xxId);
		return gjtExamAppointmentNewDao.queryPage(searchParams, pageRequst);
	}

	@Override
	public Page<GjtExamAppointmentNew> queryByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<GjtExamAppointmentNew> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", 0, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamAppointmentNewRepository.findAll(spec, pageRequest);
	}

	@Override
	public List<GjtExamAppointmentNew> queryBy(Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<GjtExamAppointmentNew> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", 0, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamAppointmentNewRepository.findAll(spec, sort);
	}

	@Override
	public List<GjtExamPointAppointmentNew> queryExamPointAppointmentBy(Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<GjtExamPointAppointmentNew> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", 0, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamPointAppointmentNewRepository.findAll(spec, sort);
	}

	@Override
	public List<GjtExamPointAppointmentNew> queryExamPointAppointmentBy(String xxId, Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<GjtExamPointAppointmentNew> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", 0, true));
		List<String> orgList = gjtOrgDao.queryChildsByParentId(xxId);
		spec.add(Restrictions.in("gjtStudentInfo.xxzxId", orgList, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamPointAppointmentNewRepository.findAll(spec, sort);
	}

	@Override
	public Map<String, List<GjtExamAppointmentNew>> queryAllByBatchCodeAndStudents(String examBatchCode,
			Set<String> studentIds) {
		Map<String, List<GjtExamAppointmentNew>> m = new HashMap<String, List<GjtExamAppointmentNew>>();
		int count = studentIds.size() / 1000 + 1;
		Object[] list = (Object[]) studentIds.toArray();
		for (int i = 0; i < count; i++) {
			Set<String> querys = new HashSet<String>();
			for (int j = 1000 * i; j < 1000 + 1000 * i && j < list.length; j++) {
				querys.add(String.valueOf(list[j]));
			}
			Map<String, SearchFilter> filters = new LinkedHashMap<String, SearchFilter>();
			filters.put("studentId", new SearchFilter("studentId", Operator.IN, querys));
			filters.put("examPlanNew.examBatchCode",
					new SearchFilter("examPlanNew.examBatchCode", Operator.EQ, examBatchCode));
			filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, 0));
			Specification<GjtExamAppointmentNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
					GjtExamAppointmentNew.class);
			List<GjtExamAppointmentNew> infos = gjtExamAppointmentNewDao.findAll(spec);

			for (GjtExamAppointmentNew info : infos) {
				String studentId = info.getStudentId();
				if (m.get(studentId) == null) {
					List<GjtExamAppointmentNew> examAppointmentNews = new ArrayList<GjtExamAppointmentNew>();
					examAppointmentNews.add(info);
					m.put(studentId, examAppointmentNews);
				} else {
					m.get(studentId).add(info);
				}
			}
		}
		return m;
	}

	@Override
	public Map<String, GjtExamPointAppointmentNew> queryAllPointAppointmentByBatchCodeAndStudents(String studyYearId,
			Set<String> studentIds) {
		Map<String, GjtExamPointAppointmentNew> m = new HashMap<String, GjtExamPointAppointmentNew>();
		int count = studentIds.size() / 1000 + 1;
		Object[] list = (Object[]) studentIds.toArray();
		for (int i = 0; i < count; i++) {
			Set<String> querys = new HashSet<String>();
			for (int j = 1000 * i; j < 1000 + 1000 * i && j < list.length; j++) {
				querys.add(String.valueOf(list[j]));
			}
			Map<String, SearchFilter> filters = new LinkedHashMap<String, SearchFilter>();
			filters.put("studentId", new SearchFilter("studentId", Operator.IN, querys));
			filters.put("studyYearId", new SearchFilter("studyYearId", Operator.EQ, studyYearId));
			filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, 0));
			Specification<GjtExamPointAppointmentNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
					GjtExamPointAppointmentNew.class);
			List<GjtExamPointAppointmentNew> infos = gjtExamPointAppointmentNewRepository.findAll(spec);
			for (GjtExamPointAppointmentNew info : infos) {
				m.put(info.getStudentId(), info);
			}
		}
		return m;
	}

	@Override
	public long isNotNullRecordCount(String xxId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, xxId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, 0));
		filters.put("gjtExamStudentRoomNew", new SearchFilter("gjtExamStudentRoomNew", Operator.ISNOTNULL));
		Specification<GjtExamAppointmentNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExamAppointmentNew.class);
		return gjtExamAppointmentNewDao.count(spec);
	}

	@Override
	public long isNullRecordCount(String xxId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, xxId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, 0));
		filters.put("gjtExamStudentRoomNew", new SearchFilter("gjtExamStudentRoomNew", Operator.ISNULL));
		Specification<GjtExamAppointmentNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExamAppointmentNew.class);
		return gjtExamAppointmentNewDao.count(spec);
	}

	@Override
	public GjtExamAppointmentNew queryGjtExamAppointmentNew(String studentId, String teachPlanId)
			throws ServiceException {
		GjtTeachPlan teachPlan = gjtTeachPlanDao.findOne(teachPlanId);
		if (teachPlan == null) {
			throw new ServiceException("教学计划未找到;参数teachPlanId:" + teachPlanId);
		}
		String subjectId = teachPlan.getSubjectId();// 考试科目ＩＤ
		if (subjectId == null) {
			throw new ServiceException("该教学计划没考试科目；参数teachPlanId:" + teachPlanId);
		}
		GjtExamSubjectNew subjectNew = gjtExamSubjectNewDao.findOne(subjectId);
		if (subjectNew == null) {
			throw new ServiceException("考试科目未找到;参数subjectId:" + subjectId);
		}
		// 当前学年度
		GjtStudyYearInfo gjtStudyYearInfo = this.getCurrentStudyYear(subjectNew.getXxId());
		int newExamType = teachPlan.getNewExamType();
		try {
			GjtExamPlanNew examPlanNew = gjtExamPlanNewDao
					.findByExamBatchNewStudyYearIdAndTypeAndSubjectCodeAndIsDeleted(gjtStudyYearInfo.getId(),
							newExamType, subjectNew.getSubjectCode());
			GjtExamAppointmentNew gjtExamAppointmentNew = gjtExamAppointmentNewDao
					.findByStudentIdAndExamPlanId(studentId, examPlanNew.getExamPlanId());
			return gjtExamAppointmentNew;
		} catch (Exception e) {
			throw new ServiceException("数据异常：" + subjectId);
		}
	}

	@Override
	public List<GjtExamAppointmentNew> queryExamAppointments(String examBatchCode, int examType,
			List<String> studentIds) {
		// 该考点预约记录
		Map<String, Object> searchParams = new HashMap<String, Object>();
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("examPlanNew.examBatchNew.examBatchCode",
				new SearchFilter("examPlanNew.examBatchNew.examBatchCode", Operator.EQ, examBatchCode));
		filters.put("type", new SearchFilter("type", Operator.EQ, examType));
		filters.put("studentId", new SearchFilter("studentId", Operator.IN, studentIds));
		Specification<GjtExamAppointmentNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExamAppointmentNew.class);
		List<GjtExamAppointmentNew> examAppointments = gjtExamAppointmentNewDao.findAll(spec);
		return examAppointments;
	}

	public List<GjtExamAppointmentNew> queryExamAppointments(String examBatchCode, int examType, String examPointId) {
		// 该考点预约记录
		Map<String, Object> searchParams = new HashMap<String, Object>();
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("examPlanNew.examBatchNew.examBatchCode",
				new SearchFilter("examPlanNew.examBatchNew.examBatchCode", Operator.EQ, examBatchCode));
		filters.put("type", new SearchFilter("type", Operator.EQ, examType));
		Specification<GjtExamAppointmentNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExamAppointmentNew.class);
		List<GjtExamAppointmentNew> examAppointments = gjtExamAppointmentNewDao.findAll(spec);
		return examAppointments;
	}

	@Override
	public Map<String, Object> appointExamPlan(String studentid, String teachPlanid, int op) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int count = gjtExamAppointmentNewDao.countAppointMents(studentid);
		log.info("appointment count:" + count);
		try {
			if (count < WebConstants.EXAM_APPOINTMENT_LIMIT || op == 0) {
				GjtTeachPlan teachPlan = gjtTeachPlanDao.findOne(teachPlanid);

				if (null != teachPlan.getSubjectId()) {
					GjtExamSubjectNew examSubject = gjtExamSubjectNewDao.findOne(teachPlan.getSubjectId());
					int newExamType = teachPlan.getNewExamType();
					if (null != examSubject) {
						GjtStudyYearInfo gjtStudyYearInfo = gjtExamAppointmentNewDao
								.getCurrentStudyYear(examSubject.getXxId());

						GjtExamPlanNew plan = gjtExamPlanNewDao
								.findByExamBatchNewStudyYearIdAndTypeAndSubjectCodeAndIsDeleted(
										gjtStudyYearInfo.getId(), newExamType, examSubject.getSubjectCode());
						if (null != plan) {
							GjtExamAppointmentNew entity = this.getAppointmentByStuidAndPlanid(studentid,
									plan.getExamPlanId());
							Date now = new Date();
							if (op == 0) {// 取消预约
								if (null != entity) {
									entity.setUpdatedBy(studentid);
									entity.setUpdatedDt(now);
									entity.setIsDeleted(1);
//									entity.setIsDeleted("Y");
									gjtExamAppointmentNewDao.savePlan(entity);
								}
								resultMap.put("successful", true);
							} else {
								if (null == entity) {
									entity = new GjtExamAppointmentNew();
									entity.setAppointmentId(UUIDUtils.random());
									entity.setCreatedBy(studentid);
									entity.setCreatedDt(now);
									entity.setXxId(plan.getXxId());
									entity.setStudentId(studentid);
									entity.setExamPlanId(plan.getExamPlanId());
									entity.setType(plan.getType());
								}
								// roundid 和 seatno 等排考的时候初始化
								entity.setUpdatedBy(studentid);
								entity.setUpdatedDt(now);
								entity.setStatus(0);// 待排考
								entity.setIsDeleted(0);
//								entity.setIsDeleted("N");
								gjtExamAppointmentNewDao.savePlan(entity);
								resultMap.put("successful", true);
							}
						} else {
							resultMap.put("successful", false);
							resultMap.put("message", "该课程尚未创建考试计划, 无法预约");
							log.error("can't find teachPlan by teachPlanid:" + teachPlanid);
						}
					} else {
						resultMap.put("successful", false);
						resultMap.put("message", "该课程尚未创建考试科目, 无法预约");
						log.error("can't find examSubject by teachPlanid:" + teachPlanid);
					}
				} else {
					resultMap.put("successful", false);
					resultMap.put("message", "该教学计划无课程号, 无法预约");
					log.error("can't find kch by teachPlanid:" + teachPlanid);
				}
			} else {
				resultMap.put("successful", false);
				resultMap.put("message", "最多只能预约" + WebConstants.EXAM_APPOINTMENT_LIMIT + "门考试.");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public GjtExamAppointmentNew appointExamPlan(String studentid, GjtExamPlanNew plan, int op) {
		GjtExamAppointmentNew entity = gjtExamAppointmentNewDao.findByStudentIdAndExamPlanId(studentid,
				plan.getExamPlanId());
		Date now = new Date();
		if (op == 0) {// 取消预约
			if (null != entity) {
				entity.setUpdatedBy(studentid);
				entity.setUpdatedDt(now);
				entity.setIsDeleted(1);
//				entity.setIsDeleted("Y");
			}
		} else {
			if (null == entity) {
				entity = new GjtExamAppointmentNew();
				entity.setAppointmentId(UUIDUtils.random());
				entity.setCreatedBy(studentid);
				entity.setCreatedDt(now);
				entity.setXxId(plan.getXxId());
				entity.setStudentId(studentid);
				entity.setExamPlanId(plan.getExamPlanId());
				entity.setType(plan.getType());
			}
			// roundid 和 seatno 等排考的时候初始化
			entity.setUpdatedBy(studentid);
			entity.setUpdatedDt(now);
			entity.setStatus(0);// 待排考
			entity.setIsDeleted(0);
//			entity.setIsDeleted("N");
		}
		return entity;
	}

	@Override
	public Map<String, Object> appointExamPlanNew(String studentid, String subjectCode, int op) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int count = gjtExamAppointmentNewDao.countAppointMents(studentid);
		if (count < WebConstants.EXAM_APPOINTMENT_LIMIT) {
			// 通过课程id 获取对应的考试计划
			GjtExamPlanNew plan = gjtExamAppointmentNewDao.getExamPlanBySubjectCode(subjectCode);
			if (null != plan) {
				GjtExamAppointmentNew entity = this.getAppointmentByStuidAndPlanid(studentid, plan.getExamPlanId());
				Date now = new Date();
				if (op == 0) {// 取消预约
					if (null != entity) {
						entity.setUpdatedBy(studentid);
						entity.setUpdatedDt(now);
						entity.setIsDeleted(1);
//						entity.setIsDeleted("Y");
						gjtExamAppointmentNewDao.savePlan(entity);
					}
					resultMap.put("successful", true);
				} else {
					if (null == entity) {
						entity = new GjtExamAppointmentNew();
						entity.setAppointmentId(UUIDUtils.random());
						entity.setCreatedBy(studentid);
						entity.setCreatedDt(now);
						entity.setXxId(plan.getXxId());
						entity.setStudentId(studentid);
						entity.setExamPlanId(plan.getExamPlanId());
						entity.setType(plan.getType());
					}
					// roundid 和 seatno 等排考的时候初始化
					entity.setUpdatedBy(studentid);
					entity.setUpdatedDt(now);
					entity.setStatus(0);// 待排考
					entity.setIsDeleted(0);
//					entity.setIsDeleted("N");
					gjtExamAppointmentNewDao.savePlan(entity);
					resultMap.put("successful", true);
				}
			} else {
				resultMap.put("successful", false);
				resultMap.put("message", "该课程尚未创建考试计划, 无法预约");
			}
		} else {
			resultMap.put("successful", false);
			resultMap.put("message", "最多只能预约" + WebConstants.EXAM_APPOINTMENT_LIMIT + "门考试.");
		}
		return resultMap;
	}

	public GjtExamAppointmentNew getAppointmentByStuidAndPlanid(String studentid, String planid) {
		return gjtExamAppointmentNewDao.getAppointmentByStuidAndPlanid(studentid, planid);
	}

	@Override
	public Map<String, Object> appointExamPoint(String studentid, String name, String xxid, int op) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("successful", true);
		GjtExamPointNew point = gjtExamAppointmentNewDao.getExamPointByNameAndXxid(xxid, name);
		GjtStudyYearInfo gjtStudyYearInfo = gjtExamAppointmentNewDao.getCurrentStudyYear(xxid);
		if (null != point) {
			if (null != gjtStudyYearInfo) {
				Date now = new Date();
				if (op == 0) {// 取消预约
					// 查询已有记录
					GjtExamPointAppointmentNew pointAppointment = gjtExamAppointmentNewDao
							.getPointAppointment(studentid, point.getExamPointId(), gjtStudyYearInfo.getId());
					if (null != pointAppointment) {
						pointAppointment.setUpdatedDt(now);
						pointAppointment.setUpdatedBy(studentid);
						pointAppointment.setIsDeleted(1);
//						pointAppointment.setIsDeleted("Y");
						gjtExamAppointmentNewDao.savePoint(pointAppointment);
					} else {
						log.warn("can't find point appointment by params: studentid: " + studentid + " name: " + name
								+ " xxid: " + xxid);
					}
				} else {
					// 限制逻辑: 一个学生在一个学年度内只能预约一个考点
					// 先删除已有的数据
					List<GjtExamPointAppointmentNew> pointAppointmentList = gjtExamAppointmentNewDao
							.getPointAppointmentList(studentid, gjtStudyYearInfo.getId());
					if (pointAppointmentList != null && pointAppointmentList.size() > 0) {
						for (GjtExamPointAppointmentNew pointAppointment : pointAppointmentList) {
							pointAppointment.setUpdatedDt(now);
							pointAppointment.setUpdatedBy(studentid);
							pointAppointment.setIsDeleted(1);
//							pointAppointment.setIsDeleted("Y");
							gjtExamAppointmentNewDao.savePoint(pointAppointment);
						}
					}

					// 再新增
					GjtExamPointAppointmentNew pointAppointment = new GjtExamPointAppointmentNew();
					pointAppointment.setId(UUIDUtils.random());
					pointAppointment.setCreatedBy(studentid);
					pointAppointment.setCreatedDt(now);
					pointAppointment.setExamPointId(point.getExamPointId());
					pointAppointment.setStudyYearId(gjtStudyYearInfo.getId());
					pointAppointment.setStudentId(studentid);
					pointAppointment.setUpdatedDt(now);
					pointAppointment.setUpdatedBy(studentid);
					pointAppointment.setIsDeleted(0);
//					pointAppointment.setIsDeleted("N");
					gjtExamAppointmentNewDao.savePoint(pointAppointment);
				}

			} else {
				resultMap.put("successful", false);
				resultMap.put("message", "无法找到匹配的学年度, 请联系管理员.");
				log.error("can't find studyyear by params: studentid: " + studentid + " name: " + name + " xxid: "
						+ xxid);
			}
		} else {
			resultMap.put("successful", false);
			resultMap.put("message", "无法找到匹配的考点, 请联系管理员.");
			log.error("can't find point by params: studentid: " + studentid + " name: " + name + " xxid: " + xxid);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> appointExamPointNew(String studentid, String pointid, String xxid, int op,
			String studyyearid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("successful", true);
		Date now = new Date();
		if (op == 0) {// 取消预约
			// 查询已有记录
			GjtExamPointAppointmentNew pointAppointment = gjtExamAppointmentNewDao.getPointAppointment(studentid,
					pointid, studyyearid);
			if (null != pointAppointment) {
				pointAppointment.setUpdatedDt(now);
				pointAppointment.setUpdatedBy(studentid);
				pointAppointment.setIsDeleted(1);
//				pointAppointment.setIsDeleted("Y");
				gjtExamAppointmentNewDao.savePoint(pointAppointment);
			} else {
				log.warn("can't find point appointment by params: studentid: " + studentid + " pointid: " + pointid
						+ " xxid: " + xxid);
			}
		} else {
			// 限制逻辑: 一个学生在一个学年度内只能预约一个考点
			// 先删除已有的数据
			List<GjtExamPointAppointmentNew> pointAppointmentList = gjtExamAppointmentNewDao
					.getPointAppointmentList(studentid, studyyearid);
			if (pointAppointmentList != null && pointAppointmentList.size() > 0) {
				for (GjtExamPointAppointmentNew pointAppointment : pointAppointmentList) {
					pointAppointment.setUpdatedDt(now);
					pointAppointment.setUpdatedBy(studentid);
					pointAppointment.setIsDeleted(1);
//					pointAppointment.setIsDeleted("Y");
					gjtExamAppointmentNewDao.savePoint(pointAppointment);
				}
			}

			// 再新增
			GjtExamPointAppointmentNew pointAppointment = new GjtExamPointAppointmentNew();
			pointAppointment.setId(UUIDUtils.random());
			pointAppointment.setCreatedBy(studentid);
			pointAppointment.setCreatedDt(now);
			pointAppointment.setExamPointId(pointid);
			pointAppointment.setStudyYearId(studyyearid);
			pointAppointment.setStudentId(studentid);
			pointAppointment.setUpdatedDt(now);
			pointAppointment.setUpdatedBy(studentid);
			pointAppointment.setIsDeleted(0);
//			pointAppointment.setIsDeleted("N");
			gjtExamAppointmentNewDao.savePoint(pointAppointment);
		}
		return resultMap;
	}

	@Override
	public Map<String, Map<String, String>> studentViewList(List<String> studentids) {
		return gjtExamAppointmentNewDao.studentViewList(studentids);
	}

	@Override
	public Map<String, String> appointPointMap(List<String> ids) {
		return gjtExamAppointmentNewDao.appointPointMap(ids);
	}

	@Override
	public List<Map> appointPointMapBySearch(String examBatchCode, String orgId) {
		return gjtExamAppointmentNewDao.appointPointMapBySearch(examBatchCode, orgId);
	}

	@Override
	public HSSFWorkbook exportAppointPoint(List<Map> list) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row;
		HSSFCell cell;
		int rowIndex = 0;
		int cellIndex = 0;

		// 标题行
		row = sheet.createRow(rowIndex++);
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学号");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学生姓名");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学期");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("专业名称");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试形式");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考点编码");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考点名称");

		for (Map map : list) {
			cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(Objects.toString(map.get("XH"), ""));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(Objects.toString(map.get("STUDENT_NAME"), ""));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(Objects.toString(map.get("GRADE_NAME"), ""));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(Objects.toString(map.get("ZYMC"), ""));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(Objects.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.EXAMINATIONMODE, map.get("EXAM_TYPE") + ""), ""));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(Objects.toString(map.get("POINT_CODE"), ""));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(Objects.toString(map.get("POINT_NAME"), ""));
		}

		return workbook;
	}

	@Override
	public HSSFWorkbook exportExamPointAppointment(List<GjtExamPointAppointmentNew> list) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row;
		HSSFCell cell;
		int rowIndex = 0;
		int cellIndex = 0;

		// 标题行
		row = sheet.createRow(rowIndex++);
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学号");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学生姓名");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("专业名称");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学期");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考点名称");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考点编码");

		for (GjtExamPointAppointmentNew pointAppointment : list) {
			try {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				
				cell = row.createCell(cellIndex++);
				cell.setCellValue(pointAppointment.getGjtStudentInfo().getXh());
				
				cell = row.createCell(cellIndex++);
				cell.setCellValue(pointAppointment.getGjtStudentInfo().getXm());
				
				cell = row.createCell(cellIndex++);
				cell.setCellValue(pointAppointment.getGjtStudentInfo().getGjtSpecialty().getZymc());
				
				cell = row.createCell(cellIndex++);
				if (pointAppointment.getGjtStudentInfo().getGjtGrade() != null) {
					cell.setCellValue(pointAppointment.getGjtStudentInfo().getGjtGrade().getGradeName());
				} else {
					cell.setCellValue("");
				}
				
				cell = row.createCell(cellIndex++);
				if (pointAppointment.getGjtExamPointNew() != null) {
					cell.setCellValue(pointAppointment.getGjtExamPointNew().getName());
				} else {
					cell.setCellValue("");
				}
				
				cell = row.createCell(cellIndex++);
				if (pointAppointment.getGjtExamPointNew() != null) {
					cell.setCellValue(pointAppointment.getGjtExamPointNew().getCode());
				} else {
					cell.setCellValue("");
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		return workbook;
	}

	@Override
	public GjtStudyYearInfo getCurrentStudyYear(String xxid) {
		return gjtExamAppointmentNewDao.getCurrentStudyYear(xxid);
	}

	@Override
	public List<Map<String, Object>> appointmentListBySearch(Map<String, String> params) {
		return gjtExamAppointmentNewDao.appointmentListBySearch(params);
	}

	/**
	 * 导出考试预约数据
	 */
	@Override
	@Deprecated
	public HSSFWorkbook exportAppointPlan(List<Map> list) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row;
		HSSFCell cell;
		int rowIndex = 0;
		int cellIndex = 0;

		// 标题行
		row = sheet.createRow(rowIndex++);
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学生姓名");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学号");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("层次");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("年级");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学期");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("专业");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试计划");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试计划编码");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试科目");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("试卷号");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("课程ID");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("课程名称");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("替换课课程ID");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("替换课名称");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试形式");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("预约考点");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试预约时间");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("排考状态");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("是否补考");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("补考费用");

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Map<String, Object> map : list) {
			cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("XM")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("XH")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(GjtExamStudentArrangesServiceImpl.getPyccStr(String.valueOf(map.get("PYCC"))));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("YEAR_NAME")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("GRADE_NAME")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("ZYMC")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("BATCH_NAME")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("EXAM_BATCH_CODE")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("EXAM_PLAN_NAME")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("EXAM_NO")));
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("SOURCE_KCH")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("SOURCE_KCMC")));
			
			if (!ObjectUtils.toString(map.get("SOURCE_KCH")).equals(ObjectUtils.toString(map.get("KCH")))) {
				cell = row.createCell(cellIndex++);
				cell.setCellValue(String.valueOf(map.get("KCH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(String.valueOf(map.get("KCMC")));
			} else {
				cell = row.createCell(cellIndex++);
				cell.setCellValue("");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("");
			}
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("EXAM_TYPE_NAME")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("POINT_NAME")));
			cell = row.createCell(cellIndex++);
			if (map.get("UPDATED_DT") != null) {
				cell.setCellValue(df.format(map.get("UPDATED_DT")));
			} else {
				cell.setCellValue(df.format(map.get("CREATED_DT")));
			}
			cell = row.createCell(cellIndex++);
			cell.setCellValue(getStatusStr((BigDecimal) map.get("STATUS")));
			
			if (map.get("COURSE_COST") != null) {
				cell = row.createCell(cellIndex++);
				cell.setCellValue("是");
				cell = row.createCell(cellIndex++);
				cell.setCellValue(String.valueOf(map.get("COURSE_COST")));
			} else {
				cell = row.createCell(cellIndex++);
				cell.setCellValue("否");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("--");
			}
		}

		return workbook;
	}

	private String getStatusStr(BigDecimal status) {
		if (null != status) {
			if (status.intValue() == 0) {
				return "未排考";
			} else if (status.intValue() == 1) {
				return "已排考";
			} else {
				return "已过期";
			}
		}
		return "";
	}

	@Override
	public String exportAppointPlan(String orgId, Map searchParams, String path) {
		try {
			List<Map> dataList = gjtExamAppointmentNewDao.findExamAppointmentList(orgId, searchParams);
			String []  titles =new String[]{"学员姓名","学号","学期","专业","层次",
					"考试计划","考试计划编码","考试科目","试卷号", "原课程试卷号",
					"课程号", "课程名称", "替换课课程号", "替换课名称",
					"考试形式", "预约考点", "考试预约时间", "排考状态", "是否补考", "补考费用"};
			String []  dbNames =new String[]{"xm","xh","termName","zymc","pycc",
					"examBatchName","examBatchCode","examPlanName","examNo", "sourceCourseExamNo",
					"sourceCourseKch","sourceCourseKcmc","courseKch","courseKcmc",
					"type", "pointName","appointmentDt", "status", "isResit", "resitCost"};

			for (Map info : dataList) {
				int type = NumberUtils.toInt(info.get("type") + "");
				info.put("pycc", Objects.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL, (String) info.get("pycc")), ""));
				info.put("type", Objects.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.EXAMINATIONMODE, type + ""), ""));
				info.put("status", getStatusStr((BigDecimal) info.get("status")));

				// 有替换课程的需要查找原课程对应的试卷号
				String examBatchCode = (String) searchParams.get("EQ_examPlanNew.examBatchCode");
				String major = (String) info.get("major");
				String sourceCourseId = (String) info.get("sourceCourseId");
				if(!Objects.toString(info.get("sourceCourseKch"), "").equals(info.get("courseKch"))) {
					ViewExamPlanSc viewExamPlanSc = null;
					try {
						// 如果通用的科目找不到，则找原始课程的科目
						if (viewExamPlanSc == null) {
							viewExamPlanSc = viewExamPlanScDao.findByExamBatchCodeAndCourseIdAndSpecialtyIdAndType(
									examBatchCode, sourceCourseId, major, type);
						}
						if (viewExamPlanSc == null) {
							viewExamPlanSc = viewExamPlanScDao.findByExamBatchCodeAndCourseIdAndSpecialtyIdAndType(
									examBatchCode, sourceCourseId, "-1", type);
						}
					} catch (Exception e) {
						info.put("sourceCourseExamNo", "原课程号对应的多个开考科目，查询失败！");
					}
					if(viewExamPlanSc != null) {
						info.put("sourceCourseExamNo", gjtExamPlanNewDao.queryBy(viewExamPlanSc.getExamPlanId()).getExamNo());
					}
				}
			}
			String fileName = ExcelService.renderExcel(dataList, titles, dbNames ,"考试预约数据", path, "考试预约数据_" + Calendar.getInstance().getTimeInMillis());
			return  fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public HSSFWorkbook exportExamAppointment(List<GjtExamAppointmentNew> list) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row;
		HSSFCell cell;
		int rowIndex = 0;
		int cellIndex = 0;

		// 标题行
		row = sheet.createRow(rowIndex++);
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学生姓名");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学号");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("手机");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("层次");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("年级");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学期");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("专业");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试计划");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试计划编码");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试科目");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试科目编码");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试形式");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("试卷号");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("预约考点");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试预约时间");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("排考状态");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("是否补考");
		
		cell = row.createCell(cellIndex++);
		cell.setCellValue("补考费用");

		@SuppressWarnings("unchecked")
		Map<Integer, String> examTypeMap = commonMapService.getExamTypeIntMap();
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (GjtExamAppointmentNew examAppointment : list) {
			cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examAppointment.getStudent().getXm());
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examAppointment.getStudent().getXh());
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examAppointment.getStudent().getSjh());
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(pyccMap.get(examAppointment.getStudent().getPycc()));
			
			cell = row.createCell(cellIndex++);
			if (examAppointment.getStudent().getGjtGrade() != null) {
				cell.setCellValue(examAppointment.getStudent().getGjtGrade().getGjtYear().getName());
			} else {
				cell.setCellValue("");
			}
			
			cell = row.createCell(cellIndex++);
			if (examAppointment.getStudent().getGjtGrade() != null) {
				cell.setCellValue(examAppointment.getStudent().getGjtGrade().getGradeName());
			} else {
				cell.setCellValue("");
			}
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examAppointment.getStudent().getGjtSpecialty().getZymc());
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examAppointment.getExamPlanNew().getExamBatchNew().getName());
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examAppointment.getExamPlanNew().getExamBatchNew().getExamBatchCode());
			
			String courseNames = "";
			String courseCodes = "";
			List<GjtCourse> courseList = examAppointment.getExamPlanNew().getGjtCourseList();
			if (courseList != null) {
				for (int i = 0; i < courseList.size(); i++) {
					courseNames += courseList.get(i).getKcmc();
					courseCodes += courseList.get(i).getKch();
					
					if (i != courseList.size() - 1) {
						courseNames += ",";
						courseCodes += ",";
					}
				}
			}
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(courseNames);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(courseCodes);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examTypeMap.get(examAppointment.getExamPlanNew().getType()));
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examAppointment.getExamPlanNew().getExamNo());
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examAppointment.getExamPointName());
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(df.format(examAppointment.getUpdatedDt()));
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(getStatusStr(examAppointment.getStatus()));
			
			if (examAppointment.getIsResit()) {
				cell = row.createCell(cellIndex++);
				cell.setCellValue("是");
				
				cell = row.createCell(cellIndex++);
				cell.setCellValue(examAppointment.getResitCost());
			} else {
				cell = row.createCell(cellIndex++);
				cell.setCellValue("否");
				
				cell = row.createCell(cellIndex++);
				cell.setCellValue("--");
			}
		}

		return workbook;
	}

	private String getStatusStr(Integer status) {
		if (null != status) {
			if (status.intValue() == 0) {
				return "未排考";
			} else if (status.intValue() == 1) {
				return "已排考";
			} else {
				return "已过期";
			}
		}
		return "";
	}
	
	@Override
	public List<Map> queryList(Map searchParams) {
		return gjtExamAppointmentNewDao.queryList(searchParams);
	}

	@Override
	public Page<GjtExamAppointmentNew> queryPage(String xxId, Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<GjtExamAppointmentNew> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", 0, true));

		String schoolId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(xxId);
		if(xxId.equals(schoolId)) {
			spec.add(Restrictions.eq("student.xxId", xxId, true));
		} else {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(xxId);
			spec.add(Restrictions.in("student.xxzxId", orgList, true));
		}
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamAppointmentNewRepository.findAll(spec, pageRequest);
	}

	@Override
	public List<GjtExamAppointmentNew> queryBy(String xxId, Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<GjtExamAppointmentNew> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", 0, true));

		String schoolId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(xxId);
		if(xxId.equals(schoolId)) {
			spec.add(Restrictions.eq("student.xxId", xxId, true));
		} else {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(xxId);
			spec.add(Restrictions.in("student.xxzxId", orgList, true));
		}
		spec.addAll(Restrictions.parse(searchParams));
		return gjtExamAppointmentNewRepository.findAll(spec, sort);
	}

	@Override
	public GjtExamAppointmentNew findByStudentIdAndExamPlanId(String studentId, String examPlanId) {
		return gjtExamAppointmentNewDao.findByStudentIdAndExamPlanId(studentId, examPlanId);
	}

	@Override
	public boolean insert(GjtExamAppointmentNew entity) {
		entity.setAppointmentId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setStatus(0);
		entity.setIsDeleted(0);
	
		return gjtExamAppointmentNewDao.save(entity) != null;
	}

	@Override
	public GjtExamAppointmentNew update(GjtExamAppointmentNew entity) {
		entity.setUpdatedDt(new Date());
		
		return gjtExamAppointmentNewDao.save(entity);
	}

	@Override
	public GjtExamAppointmentNew queryOne(String id) {
		return gjtExamAppointmentNewDao.queryOne(id);
	}

	@Override
	public List<GjtExamAppointmentNew> queryList(Iterable<String> ids) {
		return gjtExamAppointmentNewDao.queryList(ids);
	}
	
	/**
     * 查询学员预约的考点
     * @return
     */
	@Override
    public List queryStudentPoint(Map formMap) {
		return gjtExamAppointmentNewDao.queryStudentPoint(formMap);
	}

	@Override
	public List<Map<String, Object>> queryExamAppointment(Map<String, Object> searchParams) {
		return gjtExamAppointmentNewDao.queryExamAppointment(searchParams);
	}
}
