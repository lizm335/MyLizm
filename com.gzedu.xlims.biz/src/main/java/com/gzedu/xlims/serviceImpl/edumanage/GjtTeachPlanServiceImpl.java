package com.gzedu.xlims.serviceImpl.edumanage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.exception.ServiceException;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.edumanage.GjtGradeSpecialtyDao;
import com.gzedu.xlims.dao.edumanage.GjtTeachPlanDao;
import com.gzedu.xlims.dao.edumanage.GjtTermCourseinfoDao;
import com.gzedu.xlims.dao.edumanage.TeachPlanDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.organization.GjtSpecialtyDao;
import com.gzedu.xlims.dao.organization.GjtSpecialtyPlanDao;
import com.gzedu.xlims.dao.textbook.repository.GjtTextbookRepository;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.GjtTermCourseinfo;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.organization.GjtGradeService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * 功能说明：年级专业 实施教学计划
 *
 * @author 李明 liming@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 *
 */
@Service
public class GjtTeachPlanServiceImpl implements GjtTeachPlanService {
	@Autowired
	private GjtTeachPlanDao gjtTeachPlanDao;
	@Autowired
	private GjtSpecialtyPlanDao gjtSpecialtyPlanDao;
	@Autowired
	private GjtSpecialtyDao gjtSpecialtyDao;
	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private TeachPlanDao teachPlanDao;

	@Autowired
	public GjtOrgDao gjtOrgDao;

	@Autowired
	private GjtTermCourseinfoDao gjtTermCourseinfoDao;

	@Autowired
	private GjtGradeSpecialtyDao gjtGradeSpecialtyDao;

	@Autowired
	private GjtTextbookRepository gjtTextbookRepository;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	private CacheService cacheService;

	private static final Logger log = LoggerFactory.getLogger(GjtTeachPlanServiceImpl.class);

	@Override
	public List<GjtTeachPlan> queryGjtTeachPlan(String gradeId, String gjtSpecialtyId) {
		return gjtTeachPlanDao.findByGradeIdAndKkzy(gradeId, gjtSpecialtyId);
	}

	@Override
	public long countGjtTeachPlan(String gsId) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("gradeSpecialtyId", new SearchFilter("gradeSpecialtyId", Operator.EQ, gsId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("gjtCourse.isDeleted", new SearchFilter("gjtCourse.isDeleted", Operator.EQ, "N"));
		Specification<GjtTeachPlan> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTeachPlan.class);
		return gjtTeachPlanDao.count(spec);
	}

	@Override
	public Page<GjtTeachPlan> queryAll(String xxId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, xxId));

		Specification<GjtTeachPlan> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTeachPlan.class);

		return gjtTeachPlanDao.findAll(spec, pageRequst);
	}

	@Override
	public List<GjtTeachPlan> queryAll(Collection<String> orgIds, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.IN, orgIds));

		Specification<GjtTeachPlan> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTeachPlan.class);

		return gjtTeachPlanDao.findAll(spec);
	}

	@Override
	public GjtTeachPlan findOne(String teachPlanId) {
		return gjtTeachPlanDao.findOne(teachPlanId);
	}

	@Override
	public void insert(GjtTeachPlan entity) {
		entity.setTeachPlanId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		gjtTeachPlanDao.save(entity);
	}

	@Override
	public void update(GjtTeachPlan entity) {
		entity.setUpdatedDt(new Date());
		gjtTeachPlanDao.save(entity);
	}

	@Override
	public boolean createTeachPlan(GjtGradeSpecialty ggs) throws Exception {
		GjtGrade grade = ggs.getGjtGrade();
		GjtSpecialty specialty = gjtSpecialtyDao.findOne(ggs.getGjtSpecialty().getSpecialtyId());
		List<GjtSpecialtyPlan> specialtyPlans = specialty.getGjtSpecialtyPlans();
		List<GjtTeachPlan> gjtTeachPlans = new ArrayList<GjtTeachPlan>();
		List<GjtGrade> grades = gjtGradeService.findGradeBySize(grade, WebConstants.MAX_TEARM);
		for (GjtSpecialtyPlan gjtSpecialtyPlan : specialtyPlans) {
			if (gjtSpecialtyPlan.getTermTypeCode() > grades.size()) {
				log.error("专业规则开课学期：" + gjtSpecialtyPlan.getTermTypeCode() + " 已开设学期数量：" + grades.size());
				throw new ServiceException("请完善未来三年学期信息后再操作");
			}
			String actualGradeId = grades.get(gjtSpecialtyPlan.getTermTypeCode() - 1).getGradeId();
			GjtTeachPlan gjtTeachPlan = this.createPlan(gjtSpecialtyPlan, grade.getGradeId(), actualGradeId,
					specialty.getPycc(), ggs.getId());
			gjtTeachPlans.add(gjtTeachPlan);
		}
		gjtTeachPlanDao.save(gjtTeachPlans);
		return true;
	}

	@Override
	public void createTeachPlan(GjtGradeSpecialty ggs, String gradeSpecialtyId) throws Exception {
		GjtGrade grade = ggs.getGjtGrade();
		List<GjtTeachPlan> teachPlans = gjtTeachPlanDao.findByGradeSpecialtyId(gradeSpecialtyId);

		if (CollectionUtils.isEmpty(teachPlans)) {// 不存在开设专业的时候，从专业规则中复制
			createTeachPlan(ggs);
			return;
		}
		List<GjtTeachPlan> gjtTeachPlans = new ArrayList<GjtTeachPlan>();
		List<GjtGrade> grades = gjtGradeService.findGradeBySize(grade, WebConstants.MAX_TEARM);
		for (GjtTeachPlan plan : teachPlans) {
			if (plan.getKkxq() > grades.size()) {
				log.error("复制教学计划出错；专业开设学期：" + plan.getKkxq() + " 已开设学期数量：" + grades.size());
				throw new RuntimeException("请完善未来三年学期信息后再操作");
			}
			String actualGradeId = grades.get(plan.getKkxq() - 1).getGradeId();
			GjtTeachPlan gjtTeachPlan = new GjtTeachPlan();
			BeanUtils.copyProperties(plan, gjtTeachPlan, "serialVersionUID", "passScore", "teachPlanId");
			gjtTeachPlan.setTeachPlanId(UUIDUtils.random());
			gjtTeachPlan.setGradeId(grade.getGradeId());
			gjtTeachPlan.setActualGradeId(actualGradeId);
			gjtTeachPlan.setPassScore(plan.getPassScore());
			gjtTeachPlan.setGradeSpecialtyId(ggs.getId());
			gjtTeachPlan.setCreatedDt(new Date());
			gjtTeachPlan.setCreatedBy(ggs.getUpdatedBy());
			gjtTeachPlans.add(gjtTeachPlan);
		}
		gjtTeachPlanDao.save(gjtTeachPlans);

	}

	private GjtTeachPlan createPlan(GjtSpecialtyPlan gjtSpecialtyPlan, String gradeId, String actualGradeId,
			String pycc, String ggsId) {

		GjtTeachPlan gjtTeachPlan = new GjtTeachPlan();
		gjtTeachPlan.setTeachPlanId(UUIDUtils.random());
		gjtTeachPlan.setKkxq(gjtSpecialtyPlan.getTermTypeCode());
		gjtTeachPlan.setActualGradeId(actualGradeId);
		gjtTeachPlan.setGradeId(gradeId);
		gjtTeachPlan.setKcsx(gjtSpecialtyPlan.getCourseCategory() + "");
		gjtTeachPlan.setCourseId(gjtSpecialtyPlan.getCourseId());
		gjtTeachPlan.setKclbm(gjtSpecialtyPlan.getCourseTypeId());
		gjtTeachPlan.setCreatedBy(gjtSpecialtyPlan.getCreatedBy());
		gjtTeachPlan.setCreatedDt(new Date());
		gjtTeachPlan.setKcksbz(gjtSpecialtyPlan.getExamRatio());
		gjtTeachPlan.setKcxxbz(gjtSpecialtyPlan.getStudyRatio());
		gjtTeachPlan.setKsfs(gjtSpecialtyPlan.getExamType() + "");
		gjtTeachPlan.setXf(gjtSpecialtyPlan.getScore());
		gjtTeachPlan.setXs(gjtSpecialtyPlan.getHours());
		gjtTeachPlan.setKkzy(gjtSpecialtyPlan.getSpecialtyId());
		gjtTeachPlan.setXxId(gjtSpecialtyPlan.getXxId());
		gjtTeachPlan.setPycc(pycc);
		gjtTeachPlan.setKsdw(String.valueOf(gjtSpecialtyPlan.getExamUnit()));
		gjtTeachPlan.setCourseType(String.valueOf(gjtSpecialtyPlan.getCoursetype()));
		gjtTeachPlan.setGradeSpecialtyId(ggsId);

		gjtTeachPlan.setIsDeleted("N");
		gjtTeachPlan.setVersion(new BigDecimal(3));
		return gjtTeachPlan;
	}

	@Override
	public void delete(String planId) {
		gjtTeachPlanDao.delete(planId);
	}

	@Override
	public void delete(GjtTeachPlan entity) {
		gjtTeachPlanDao.delete(entity);
	}

	public List<GjtTeachPlan> findByActualGradeIdAndCourseId(String actualGradeId, String courseId) {
		return gjtTeachPlanDao.findByActualGradeIdAndCourseIdAndIsDeleted(actualGradeId, courseId, "N");
	}

	@Override
	public List<GjtTeachPlan> batchAddPlan(String gradeSpecialtyId, List<String> courseIds, int term) {
		GjtGradeSpecialty ggs = gjtGradeSpecialtyDao.findOne(gradeSpecialtyId);

		GjtGrade grade = ggs.getGjtGrade();
		GjtSpecialty specialty = ggs.getGjtSpecialty();
		List<GjtSpecialtyPlan> specialtyPlans = gjtSpecialtyPlanDao
				.findBySpecialtyIdAndCourseIdIn(specialty.getSpecialtyId(), courseIds);
		List<GjtTeachPlan> gjtTeachPlans = new ArrayList<GjtTeachPlan>();
		List<GjtGrade> grades = gjtGradeService.findGradeBySize(grade, WebConstants.MAX_TEARM);
		for (GjtSpecialtyPlan gjtSpecialtyPlan : specialtyPlans) {
			if (term > grades.size()) {
				throw new ServiceException("请完善未来三年学期信息后再操作");
			}
			String actualGradeId = grades.get(term - 1).getGradeId();
			GjtTeachPlan gjtTeachPlan = this.createPlan(gjtSpecialtyPlan, grade.getGradeId(), actualGradeId,
					specialty.getPycc(), ggs.getId());
			gjtTeachPlan.setKkxq(term);
			gjtTeachPlans.add(gjtTeachPlan);
		}
		gjtTeachPlanDao.save(gjtTeachPlans);
		return gjtTeachPlans;
	}

	@Override
	public GjtTeachPlan findByGradeIdAndKkzyAndCourseId(String gradeId, String kkzy, String courseId) {
		return gjtTeachPlanDao.findByGradeIdAndKkzyAndIsDeletedAndCourseId(gradeId, kkzy, "N", courseId);
	}

	@Override
	public void copyTeachPlan(GjtTeachPlan gjtTeachPlan, GjtSpecialtyPlan gjtSpecialtyPlan) {
		// TODO Auto-generated method stub
		gjtTeachPlan.setKcsx(gjtSpecialtyPlan.getCourseCategory() + "");
		gjtTeachPlan.setCourseId(gjtSpecialtyPlan.getCourseId());
		gjtTeachPlan.setKclbm(gjtSpecialtyPlan.getCourseTypeId());
		gjtTeachPlan.setKcksbz(gjtSpecialtyPlan.getExamRatio());
		gjtTeachPlan.setKcxxbz(gjtSpecialtyPlan.getStudyRatio());
		gjtTeachPlan.setKsfs(gjtSpecialtyPlan.getExamType() + "");
		gjtTeachPlan.setXf(gjtSpecialtyPlan.getScore());
		gjtTeachPlan.setXs(gjtSpecialtyPlan.getHours());
		gjtTeachPlan.setKkzy(gjtSpecialtyPlan.getSpecialtyId());
		gjtTeachPlan.setXxId(gjtSpecialtyPlan.getXxId());
		gjtTeachPlan.setKsdw(String.valueOf(gjtSpecialtyPlan.getExamUnit()));
		gjtTeachPlan.setCourseType(String.valueOf(gjtSpecialtyPlan.getCoursetype()));
		gjtTeachPlan.setIsDeleted("N");
		gjtTeachPlan.setVersion(new BigDecimal(3));
		update(gjtTeachPlan);
	}

	@Override
	public void deleteById(String id) {
		gjtTeachPlanDao.deleteById(id, "Y");
	}

	@Override
	public GjtTermCourseinfo queryTermCourseinfoByOrgIdAndTermIdAndCourseId(String orgId, String termId,
			String courseId) {
		// 院校获取总校ID
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		return gjtTermCourseinfoDao.findByXxIdAndTermIdAndCourseId(xxId, termId, courseId);
	}

	public List<GjtTeachPlan> queryGjtTeachPlan(String gradeSpecialtyId) {
		return gjtTeachPlanDao.findByGradeSpecialtyId(gradeSpecialtyId);
	}

	@Override
	@Transactional
	public void updateTextbook(String planId, List<String> textbookIds) {
		GjtTeachPlan teachPlan = gjtTeachPlanDao.findOne(planId);
		teachPlan.setGjtTextbookList(null);
		gjtTeachPlanDao.save(teachPlan);
		gjtTeachPlanDao.flush();
		if (CollectionUtils.isEmpty(textbookIds))
			return;
		for (String textbookId : textbookIds) {
			gjtTextbookRepository.insert(UUIDUtils.random(), teachPlan.getTeachPlanId(), teachPlan.getCourseId(),
					textbookId);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void updateTeachPlan(String id, List<JSONObject> plans) {
		GjtGradeSpecialty gjtGradeSpecialty = gjtGradeSpecialtyDao.findOne(id);
		List<String> teachPlanIds = gjtTeachPlanDao.findTeachPlanIdByGradeSpecialtyId(id);
		List<GjtGrade> grades = gjtGradeService.findGradeBySize(gjtGradeSpecialty.getGjtGrade(),
				WebConstants.MAX_TEARM);
		for (JSONObject plan : plans) {

			String planId = (String) plan.get("planId");
			if (teachPlanIds.contains(planId)) {// 移除页面传过来的数据，剩下就是已删除的数据
				teachPlanIds.remove(planId);
			}
			// boolean isUpdate = plan.getBoolean("update");
			// if (!isUpdate) {
			// continue;
			// }
			String courseId = (String) plan.get("courseId");
			String replaceCourseId = (String) plan.get("replaceCourseId");
			int term = plan.getInt("term");
			// int grantData = plan.getInt("grantData");
			String exanNo = plan.getString("examNo");
			String replaceExamNo = plan.getString("replaceExamNo");
			// int courseCategory = plan.getInt("courseCategory");
			JSONArray textbookIds = plan.getJSONArray("textbookIds");
			GjtTeachPlan teachPlan = null;

			if (term > grades.size()) {
				log.error("专业规则开课学期：" + term + " 已开设学期数量：" + grades.size());
				throw new ServiceException("请完善未来三年学期信息后再操作");
			}
			String actualGradeId = grades.get(term - 1).getGradeId();
			if (StringUtils.isNotEmpty(planId)) {
				teachPlan = gjtTeachPlanDao.findOne(planId);

			} else {
				GjtSpecialtyPlan gjtSpecialtyPlan = gjtSpecialtyPlanDao
						.findBySpecialtyIdAndCourseId(gjtGradeSpecialty.getGjtSpecialty().getSpecialtyId(), courseId);

				teachPlan = this.createPlan(gjtSpecialtyPlan, gjtGradeSpecialty.getGjtGrade().getGradeId(),
						actualGradeId, gjtGradeSpecialty.getGjtSpecialty().getPycc(), id);

			}
			teachPlan.setReplaceCourseId(replaceCourseId);
			teachPlan.setKkxq(term);
			// teachPlan.setGrantData(grantData);
			teachPlan.setExamNo(exanNo);
			teachPlan.setReplaceExamNo(replaceExamNo);

			teachPlan.setLearningStyle(plan.getInt("learningStyle"));
			teachPlan.setActualGradeId(actualGradeId);
			// teachPlan.setCourseCategory(courseCategory);
			teachPlan.setGjtTextbookList(null);
			gjtTeachPlanDao.saveAndFlush(teachPlan);
			for (Object tbId : textbookIds) {
				gjtTextbookRepository.insert(UUIDUtils.random(), teachPlan.getTeachPlanId(), teachPlan.getCourseId(),
						tbId.toString());
			}
		}
		for (String pid : teachPlanIds) {
			gjtTeachPlanDao.deleteById(pid, "Y");
		}
	}

	@Override
	public List<GjtTeachPlan> findView(String gradeSpecialtyId, String courseId) {
		return teachPlanDao.findView(gradeSpecialtyId, courseId);
	}

	@Override
	public List<Map<String, Object>> getGjtTeachPlanInfo(String gradeSpecialtyId) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("  select gtp.kkxq, gtp.actual_grade_id");
		sql.append("  from gjt_teach_plan gtp");
		sql.append("  where gtp.grade_specialty_id =:grade_specialty_id");
		sql.append("  and gtp.is_deleted = 'N'");
		sql.append("  group by gtp.kkxq, gtp.actual_grade_id");
		sql.append("  order by gtp.kkxq");
		params.put("grade_specialty_id", gradeSpecialtyId);
		return commonDao.queryForMapList(sql.toString(), params);
	}

	@Override
	public Workbook expCoursePlan(Map<String, Object> formMap, HttpServletRequest request,
			HttpServletResponse response) {
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			HSSFSheet sheet = wb.createSheet();
			HSSFRow row;
			HSSFCell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("层次");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("年级");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("专业规则号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("专业");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程模块");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程代码");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程名称");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("试卷号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("替换课程代码");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("替换课程");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("试卷号(替)");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程性质");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程类型");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("考试单位");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学分");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学时");

			List<Map<String, Object>> resultlist = this.getExpPlanList(formMap);
			if (EmptyUtils.isNotEmpty(resultlist) && resultlist.size() > 0) {
				log.info("教学计划导出集合数量={}", resultlist.size());
				for (int i = 0; i < resultlist.size(); i++) {
					Map<String, Object> e = (Map<String, Object>) resultlist.get(i);
					cellIndex = 0;
					row = sheet.createRow(rowIndex++);

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("PYCC")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("RULE_CODE")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("ZYMC")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("KKXQ")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("MKMC")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SOURCE_KCH")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SOURCE_KCMC")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("EXAM_NO")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("KCH")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("REPLACE_EXAM_NO")));

					String KCSX = ObjectUtils.toString(e.get("KCSX"));
					cell = row.createCell(cellIndex++);
					if ("0".equals(KCSX)) {
						cell.setCellValue("必修");
					} else if ("1".equals(KCSX)) {
						cell.setCellValue("选修");
					} else {
						cell.setCellValue("补修");
					}

					String course_category = ObjectUtils.toString(e.get("COURSE_CATEGORY"));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(cacheService
							.getCachedDictionaryName(CacheService.DictionaryKey.COURSE_CATEGORY_INFO, course_category));

					String exam_unit = ObjectUtils.toString(e.get("KSDW"));
					cell = row.createCell(cellIndex++);
					if ("1".equals(exam_unit)) {
						cell.setCellValue("省");
					} else if ("2".equals(exam_unit)) {
						cell.setCellValue("中央");
					} else {
						cell.setCellValue("分校");
					}

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("XF")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("XS")));

				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return wb;
	}

	private List<Map<String, Object>> getExpPlanList(Map<String, Object> formMap) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select (select t.name from tbl_sys_data ");
		sql.append(" t where t.is_deleted='N' and t.type_code='TrainingLevel' and t.code=gs.pycc) pycc,");
		sql.append(" gg.grade_name,gs.rule_code,gs.zymc,");
		sql.append(" (select t.name from tbl_sys_data t ");
		sql.append(" where t.is_deleted='N' and t.type_code='CourseType' and t.code=vtp.kclbm) mkmc,");
		sql.append(" (case when vtp.source_course_id = vtp.course_id then '' else vtp.kch end) kch,");
		sql.append(" (case when vtp.source_course_id = vtp.course_id then '' else vtp.kcmc end) kcmc,");
		sql.append("  vtp.kkxq,vtp.source_kch,vtp.source_kcmc,vtp.kcsx,");
		sql.append("  gc.course_category,");
		sql.append(" vtp.ksdw,vtp.xf,vtp.xs,vtp.exam_no,vtp.replace_exam_no");
		sql.append("  from gjt_grade_specialty ggs");
		sql.append(" inner join gjt_grade gg on ggs.grade_id = gg.grade_id and gg.is_deleted='N'");
		sql.append(" inner join gjt_specialty gs on ggs.specialty_id = gs.specialty_id and gs.is_deleted='N'");
		sql.append(" inner join view_teach_plan vtp on ggs.id = vtp.grade_specialty_id");
		sql.append(" inner join gjt_course gc on gc.course_id = vtp.course_id and gc.is_deleted='N'");
		sql.append(" left join gjt_gs_study_center ggsc on ggsc.grade_specialty_id = ggs.id");
		sql.append("  where ggs.is_deleted = 'N'");

		String xxId = (String) formMap.get("xxId");
		if (EmptyUtils.isNotEmpty(xxId)) {
			Map<String, String> orgId = commonMapService.getOrgMapByOrgId(xxId);
			sql.append("  and gg.xx_id in :xxId");
			params.put("xxId", orgId.keySet());
		}

		if (EmptyUtils.isNotEmpty(formMap.get("EQ_gjtGrade.gradeId"))) {
			sql.append("  and gg.grade_id = :gradeId");
			params.put("gradeId", formMap.get("EQ_gjtGrade.gradeId"));
		}
		if (EmptyUtils.isNotEmpty(formMap.get("LIKE_gjtSpecialty.ruleCode"))) {
			sql.append("  and gs.rule_code like '%" + formMap.get("LIKE_gjtSpecialty.ruleCode") + "%'");
		}
		if (EmptyUtils.isNotEmpty(formMap.get("LIKE_gjtSpecialty.zymc"))) {
			sql.append("  and gs.zymc like '%" + formMap.get("LIKE_gjtSpecialty.zymc") + "%'");
		}
		if (EmptyUtils.isNotEmpty(formMap.get("EQ_gjtSpecialty.pycc"))) {
			sql.append("  and gtp.pycc = :pycc");
			params.put("pycc", formMap.get("EQ_gjtSpecialty.pycc"));
		}
		if (EmptyUtils.isNotEmpty(formMap.get("EQ_gjtSpecialty.subject"))) {
			sql.append("  and gs.subject = :subject");
			params.put("subject", formMap.get("EQ_gjtSpecialty.subject"));
		}
		if (EmptyUtils.isNotEmpty(formMap.get("EQ_gjtStudyCenters.id"))) {
			sql.append("  and ggsc.study_center_id = :studyCenter");
			params.put("studyCenter", formMap.get("EQ_gjtStudyCenters.id"));
		}

		sql.append("  order by gs.rule_code,vtp.kkxq ");
		log.info("教学计划导出Sql={},params={}", sql.toString(), params);
		return commonDao.queryForMapList(sql.toString(), params);
	}

	public List<GjtTeachPlan> findByGradeSpecialtyIdAndKkxq(String gradeSpecialtyId, int kkxq) {
		return gjtTeachPlanDao.findByGradeSpecialtyIdAndKkxq(gradeSpecialtyId, kkxq);
	}
}
