package com.gzedu.xlims.serviceImpl.exam;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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

import com.google.common.collect.Lists;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.ExcelReader;
import com.gzedu.xlims.common.ExcelService;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.ToolUtil;
import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.exam.GjtExamPlanNewDao;
import com.gzedu.xlims.dao.exam.GjtExamPlanNewSpecialtyDao;
import com.gzedu.xlims.dao.exam.GjtExamSubjectNewDao;
import com.gzedu.xlims.dao.exam.repository.GjtExamPlanNewRepository;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.exam.GjtExamBatchNewService;
import com.gzedu.xlims.service.exam.GjtExamPlanNewService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

import net.sf.jxls.transformer.XLSTransformer;

@Service
public class GjtExamPlanNewServiceImpl extends BaseServiceImpl<GjtExamPlanNew> implements GjtExamPlanNewService {

	private static final Log log = LogFactory.getLog(GjtExamPlanNewServiceImpl.class);

	@Autowired
	private GjtExamPlanNewDao gjtExamPlanNewDao;

	@Autowired
	private GjtExamPlanNewRepository gjtExamPlanNewRepository;

	@Autowired
	private GjtExamSubjectNewDao gjtExamSubjectNewDao;

	@Autowired
	private CodeGeneratorService codeGeneratorService;

	@Autowired
	private GjtExamBatchNewService gjtExamBatchNewService;

	@Autowired
	private GjtExamPlanNewSpecialtyDao gjtExamPlanNewSpecialtyDao;

	@Autowired
	private GjtCourseService gjtCourseService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private CacheService cacheService;

	@Override
	protected BaseDao<GjtExamPlanNew, String> getBaseDao() {
		return this.gjtExamPlanNewRepository;
	}

	@Override
	public boolean insert(GjtExamPlanNew entity) {
		entity.setExamPlanId(SequenceUUID.getSequence());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted(0);
		entity = gjtExamPlanNewDao.save(entity);

		insertExamPlanToTongyongSpecialty(entity.getExamPlanId());
		return entity != null;
	}

	@Override
	public boolean insertExamPlanToTongyongSpecialty(String examPlanId) {
		// 如果没有指定专业，则插入一条-1的记录
		if (!gjtExamPlanNewDao.existsExamPlanSpecialty(examPlanId)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("EXAM_PLAN_ID", examPlanId);
			params.put("SPECIALTY_ID", "-1");
			gjtExamPlanNewDao.insertExamPlanSpecialty(params);
		}
		return true;
	}

	@Override
	public Page<GjtExamPlanNew> queryAll(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, 0));
		Specification<GjtExamPlanNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExamPlanNew.class);
		return gjtExamPlanNewDao.findAll(spec, pageRequest);
	}

	@Override
	public void delete(List<String> ids) {
		for (String id : ids) {
			gjtExamPlanNewDao.deleteGjtExamPlanNew(id);
		}
	}

	@Override
	public GjtExamPlanNew queryBy(String id) {
		return gjtExamPlanNewDao.queryBy(id);
	}

	@Override
	public GjtExamPlanNew update(GjtExamPlanNew entity) {
		entity.setUpdatedDt(new Date());
		entity = gjtExamPlanNewDao.save(entity);

		insertExamPlanToTongyongSpecialty(entity.getExamPlanId());
		return entity;
	}

	@Override
	public Map<String, Object> dataFilter(List<GjtExamPlanNew> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<GjtExamPlanNew> updateList = new ArrayList<GjtExamPlanNew>();
		List<GjtExamPlanNew> illegalList = new ArrayList<GjtExamPlanNew>();
		for (GjtExamPlanNew gjtExamPlanNew : list) {
			if (planValid(gjtExamPlanNew) == 0) {
				updateList.add(gjtExamPlanNew);
			} else {
				illegalList.add(gjtExamPlanNew);
			}
		}
		map.put("updateList", updateList);
		map.put("illegalList", illegalList);
		return map;
	}

	/**
	 * 此方法仅支持新增, 要update 请调用update 方法 TODO: @micarol 分批插入
	 * 
	 * @return null-插入异常
	 */
	@Override
	public int insertBatch(List<GjtExamPlanNew> list) {
		int rs = 0;
		try {
			list = gjtExamPlanNewDao.save(list);
			rs = list.size();
		} catch (Exception e) {
			log.error("plans insert batch error.");
			log.error(e.getMessage(), e);
		}
		return rs;
	}

	/**
	 * 此方法仅支持新增 TODO: @micarol 分批插入
	 *
	 * @return null-插入异常
	 */
	@Override
	public int insertBatchSubject(List<GjtExamSubjectNew> list) {
		int rs = 0;
		try {
			list = gjtExamSubjectNewDao.save(list);
			rs = list.size();
		} catch (Exception e) {
			log.error("Subject insert batch error.");
			log.error(e.getMessage(), e);
		}
		return rs;
	}

	/**
	 * 批量更新, 主键必须有值. 注：设置考试安排，只有状态为未设置、待预约的时候可以设置，开始预约后将不能进行任何操作
	 */
	@Override
	public int updateBatch(List<GjtExamPlanNew> list) {
		int rs = 0;
		try {
			Date now = new Date();
			for (GjtExamPlanNew gjtExamPlanNew : list) {
				gjtExamPlanNew.setUpdatedDt(now);
			}
			list = gjtExamPlanNewDao.save(list);
			rs = list.size();
		} catch (Exception e) {
			log.error("plans update batch error.");
			log.error(e.getMessage(), e);
		}
		return rs;
	}

	/**
	 * 导入验证说明： 1、导入根据批次编号和考试科目标号进行修改, 即批次和科目不能空, 且联合唯一 2、考试结束时间不能小于考试开始时间
	 * 3、考试结束时间与考试开始时间最少相差30分钟 4、考试时间不能超出考试批次设置的考试开始时间和结束时间
	 * 5、只允许设置未设置、待预约状态的考试时间 6、如果是笔试、机考必须设置试卷号 7、考试时间不能空
	 * 
	 * @return 0-没有错误. 其余值代表错误编码
	 */
	@Override
	public int planValid(GjtExamPlanNew entity) {
		if (null == entity.getExamBatchNew() || null == entity.getSubjectCode()
				|| null == entity.getExamBatchNew().getExamBatchCode()) {
			return 1;
		}

		if (null == entity.getExamSt() || null == entity.getExamEnd()) {
			return 7;
		}
		if (entity.getExamEnd().before(entity.getExamSt())) {
			return 2;
		}
		if (((entity.getExamEnd().getTime() - entity.getExamSt().getTime()) / 1000 / 60) >= 30) {
			return 3;
		}

		Date now = new Date();
		if (now.before(entity.getExamBatchNew().getBookSt())) {
			return 5;
		}

		Date start;
		Date end;
		if (WebConstants.EXAM_WK_TYPE.equals(entity.getType())) {
			start = entity.getExamBatchNew().getOnlineSt();
			end = entity.getExamBatchNew().getOnlineEnd();
		} else {
			start = entity.getExamBatchNew().getOfflineSt();
			end = entity.getExamBatchNew().getOfflineEnd();
		}
		if (null == start || null == end || entity.getExamSt().before(start) || entity.getExamEnd().after(end)) {
			return 4;
		}

		if (WebConstants.EXAM_BS_TYPE.equals(entity.getType()) || WebConstants.EXAM_JK_TYPE.equals(entity.getType())) {
			// if (StringUtils.isEmpty(entity.getExamSubjectNew().getExamNo()))
			// {
			// return 6;
			// }

			if (StringUtils.isEmpty(entity.getExamNo())) {
				return 6;
			}
		}
		return 0;
	}

	/**
	 * subjectCode 不在维护 exampland 由subjectCode与batchCode 拼接组成,
	 * 导入计划时是根据这两个字段做insert 或 update
	 */
	@Override
	public String examPlanIdGenerator(String subjectCode, String batchCode) {
		return subjectCode + batchCode;
	}

	/**
	 * 批次创建完成以后，根据批次中的学年度，自动生成考试计划数据 PS: 预约时间默认关联批次的预约时间, 考试时间不设置
	 */
	@Override
	public Map<String, Object> autoCreatePlans(GjtExamBatchNew batch) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, batch.getXxId()));
		Specification<GjtExamSubjectNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExamSubjectNew.class);
		List<GjtExamSubjectNew> subjectList = gjtExamSubjectNewDao.findAll(spec);
		List<GjtExamPlanNew> plans = new ArrayList<GjtExamPlanNew>();
		GjtExamPlanNew plan;
		for (GjtExamSubjectNew subject : subjectList) {
			plan = new GjtExamPlanNew();
			// plan self about
			plan.setExamPlanId(this.examPlanIdGenerator(subject.getSubjectCode(), batch.getExamBatchCode()));
			plan.setCreatedBy(batch.getCreatedBy());
			plan.setCreatedDt(batch.getCreatedDt());
			plan.setUpdatedBy(batch.getCreatedBy());
			plan.setUpdatedDt(batch.getCreatedDt());

			// batch about
			// plan.setExamBatchId(batch.getExamBatchId());
			plan.setExamBatchCode(batch.getExamBatchCode());
			plan.setExamBatchNew(batch);
			plan.setBookSt(batch.getBookSt());
			plan.setBookEnd(batch.getBookEnd());
			plan.setStudyYearId(batch.getStudyYearId());
			plan.setXxId(batch.getXxId());
			// subject about
			// plan.setSubjectId(subject.getSubjectId());
			plan.setSubjectCode(subject.getSubjectCode());
			// plan.setExamSubjectNew(subject);
			plan.setType(subject.getType());

			plans.add(plan);
		}

		int rs = this.insertBatch(plans);
		if (rs > 0) {
			map.put("successful", true);
			log.info("plans auto create success. size: " + rs);
		} else {
			map.put("message", "插入数据库异常");
		}

		return map;
	}

	@Override
	public List<GjtExamPlanNew> findByExamBatchCode(String batchCode) {
		return gjtExamPlanNewDao.findByExamBatchCode(batchCode);
	}

	/**
	 * 导出考试计划
	 * 
	 * @param
	 * @return
	 */
	@Override
	public Map exportByList(Map searchParams, PageRequest pageRequst, HttpServletResponse response) {
		Map resultMap = new HashMap();
		try {
			Page pageList = gjtExamPlanNewDao.getExamPlanList(searchParams, pageRequst);
			List dataList = pageList.getContent();

			Map beans = new HashMap();
			beans.put("dataList", dataList);

			String timeStr = ObjectUtils.toString(new Date().getTime());
			String tempPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()
					+ "/excel/exam/考试计划导出表.xls";
			String downPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()
					+ "/excel/temp/考试计划" + timeStr + ".xls";

			XLSTransformer transformer = new XLSTransformer();
			transformer.transformXLS(tempPath, beans, downPath);
			ToolUtil.download(downPath, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	private String getDateStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return null != date ? sdf.format(date) : "";
	}

	/**
	 * 验证考试方式是否符合规定
	 * 
	 * @param type
	 * @return
	 */
	public boolean validatorExamType(String type) {
		boolean flag = true;
		if ("网考".equals(type) || "笔试".equals(type) || "机考".equals(type) || "形考".equals(type) || "省网考".equals(type)
				|| "全国统考".equals(type) || "省统考".equals(type) || "论文".equals(type) || "报告".equals(type)
				|| "大作业".equals(type) || "论文报告".equals(type)) {
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * 考试方式:1-网考;2-笔试;3-机考;
	 * 
	 * @param type
	 * @return
	 */
	public static String getTypeStr(int type) {
		switch (type) {
			case 1:
				return "网考";
			case 2:
				return "笔试";
			case 3:
				return "机考";
			case 4:
				return "形考";
			case 5:
				return "省网考";
			case 6:
				return "全国统考";
			case 7:
				return "省统考";
			case 8:
				return "论文";
			case 9:
				return "报告";
			case 10:
				return "大作业";
			case 11:
				return "论文报告";
			default:
				return "机考";
		}
	}

	public static String getStatus(Date bookSt, Date bookEnd, Date examSt, Date examEnd) {
		Date now = new Date();

		if (examSt == null || examEnd == null) {
			return "未设置";
		}

		if (bookSt != null && now.compareTo(bookSt) < 0) {
			return "未开始";
		}

		if (bookEnd != null && now.compareTo(bookEnd) < 0) {
			return "预约中";
		}

		if (examSt != null && now.compareTo(examSt) < 0) {
			return "待考试";
		}

		if (examEnd != null && now.compareTo(examEnd) < 0) {
			return "考试中";
		}

		return "已结束";
	}

	@Override
	public HSSFWorkbook importPlansSetting(String path, GjtUserAccount user) {
		HSSFWorkbook resultWb = null;// 直接将结果放在excel 返回
		resultWb = new HSSFWorkbook();
		HSSFSheet sheet = resultWb.createSheet();
		HSSFRow row;
		HSSFCell cell;
		int rowIndex = 0;
		int cellIndex = 0;

		// 学年度名称, 考试批次名称, 批次编码, 考试科目名称, 科目编码, 课程号，考试方式, 预约开始时间, 预约结束时间, 考试开始时间,
		// 考试结束时间, 状态
		// String[] titles = { "studyyearName", "batchName", "batchCode",
		// "subjectName", "subjectCode", "courseCode",
		// "type", "examNo", "bookSt", "bookEnd", "examSt", "examEnd", "status"
		// };
		// 学年度名称, 考试批次名称, 批次编码, 考试课程, 课程号, 试卷号，考试方式, 形考比例, 预约开始时间, 预约结束时间,
		// 考试开始时间, 考试结束时间, 状态
		String[] titles = { "studyyearName", "batchName", "batchCode", "courseName", "courseCode", "examNo", "type",
				"xkPercent", "bookSt", "bookEnd", "examSt", "examEnd", "status" };
		File file = new File(path);
		ExcelReader reader = new ExcelReader(file, titles, 1);
		List<Object> list = reader.readAsList();
		Map<String, Object> map;
		Map<Integer, Integer> errorMap = new LinkedHashMap<Integer, Integer>();
		Integer indexCount = 1;// 第一行标题行忽略
		int errorCode = 0;
		List<GjtExamPlanNew> updateList = new ArrayList<GjtExamPlanNew>();
		int rowCount = 0;

		Map<String, String> idMap = new HashMap<String, String>();
		for (Object object : list) {
			map = (Map<String, Object>) object;
			// idMap.put(
			// this.examPlanIdGenerator(String.valueOf(map.get("subjectCode")),
			// String.valueOf(map.get("batchCode"))),
			// this.examPlanIdGenerator(String.valueOf(map.get("subjectCode")),
			// String.valueOf(map.get("batchCode"))));
			if (++rowCount > 1000)
				break;// 单次导入最多1000, 超过1000 的数据忽略
		}
		rowCount = 0;
		List<String> planidList = Lists.newArrayList(idMap.values());
		Map<String, GjtExamPlanNew> planMap = gjtExamPlanNewDao.updatePlanMapByIds(planidList);// 系统已有记录
		if (planMap.size() > 0) {
			Date now = new Date();
			GjtExamPlanNew plan;
			GjtExamPlanNew planCopy;
			for (Object object : list) {
				indexCount++;
				map = (Map<String, Object>) object;
				errorCode = this.importDataValid(map);
				if (errorCode == 0) {
					// 判断是否存在改考试计划 根据 科目,考试方式跟批次判断是否存在
					gjtExamPlanNewDao.findByExamBatchCodeAndCourseIdAndExamType(
							ObjectUtils.toString(map.get("batchCode")), ObjectUtils.toString(map.get("")),
							ObjectUtils.toString(map.get("")));
					String planid = this.examPlanIdGenerator(String.valueOf(map.get("subjectCode")),
							String.valueOf(map.get("batchCode")));
					if (planMap.containsKey(planid)) {
						plan = planMap.get(planid);
						try {
							planCopy = plan.clone();// 这里如果不用copy, jpa
													// 会自动更新planMap 里的所有plan
													// 对象... 没搞明白造成这个bug 的原因...
							planCopy.setBookSt(DateUtils.getDateToString((String) map.get("bookSt")));
							planCopy.setBookEnd(DateUtils.getDateToString((String) map.get("bookEnd")));
							try {
								planCopy.setXkPercent(Integer.parseInt(ObjectUtils.toString(map.get("xkPercent"), "")));
								planCopy.setExamSt(DateUtils.getStrToDate((String) map.get("examSt")));
								planCopy.setExamEnd(DateUtils.getStrToDate((String) map.get("examEnd")));
								// planCopy.setExamSt(DateUtils.getDateToString((String)
								// map.get("examSt")));
								// planCopy.setExamEnd(DateUtils.getDateToString((String)
								// map.get("examEnd")));
								planCopy.setUpdatedBy(user.getId());
								planCopy.setUpdatedDt(now);
								errorCode = this.validAgain(planCopy);
								if (errorCode == 0) {
									updateList.add(planCopy);
								} else {
									errorMap.put(indexCount, errorCode);
								}
							} catch (Exception e) {
								errorMap.put(indexCount, 5);
							}
						} catch (Exception e) {
							errorMap.put(indexCount, 4);
						}
					} else {
						errorMap.put(indexCount, 1);// 1. 该计划不存在
					}
				} else {
					errorMap.put(indexCount, errorCode);
				}
				if (++rowCount > 1000)
					break;// 单次导入最多1000, 超过1000 的数据忽略
			}
		} else {
			errorMap.put(indexCount, 1);// 该计划不存在
		}

		StringBuilder sbuilder = new StringBuilder();
		int rs = this.insertBatch(updateList);
		if (rs > 0) {
			sbuilder.append("成功设置" + rs + "条数据.");
		}

		// write error date to resultWb
		if (errorMap.size() > 0) {
			sbuilder.append(" 失败" + errorMap.size() + "条. 错误数据如下: ");
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(sbuilder.toString());

			Set<Entry<Integer, Integer>> set = errorMap.entrySet();
			Iterator<Entry<Integer, Integer>> enties = set.iterator();
			Entry<Integer, Integer> entity;
			while (enties.hasNext()) {
				entity = enties.next();
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue("第" + entity.getKey() + "行");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("原因: " + this.getErrStr(entity.getValue()));
			}
		} else {
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(sbuilder.toString());
		}
		return resultWb;
	}

	/**
	 * 数据校验 1. 该计划不存在.(导入只能更新不能新建, 因此要校验数据是否已存在) 2. 批次编码错误 3. 科目编码错误 4. 预约时间错误
	 * 5. 考试时间错误
	 * 
	 * @param data
	 * @return
	 */
	private int importDataValid(Map<String, Object> data) {
		if (null == data.get("batchCode")) {
			return 2;
		}
		if (null == data.get("subjectCode")) {
			return 3;
		}
		if (null == data.get("bookSt") || null == data.get("bookEnd")) {
			return 4;
		}
		if (null == data.get("examSt") || null == data.get("examEnd")) {
			return 5;
		}
		return 0;
	}

	/**
	 * 导入验证说明： 6. 找不到对应的考试批次或考试科目 7. 考试结束时间不能小于考试开始时间 8. 考试结束时间与考试开始时间最少相差30分钟
	 * 9. 考试时间不能超出考试批次设置的考试开始时间和结束时间
	 * 
	 * @return 0-没有错误. 其余值代表错误编码
	 */
	public int validAgain(GjtExamPlanNew entity) {
		if (null == entity.getExamSubjectNew() || null == entity.getExamBatchNew()) {
			return 6;
		}
		if (entity.getExamEnd().before(entity.getExamSt())) {
			return 7;
		}
		if (((entity.getExamEnd().getTime() - entity.getExamSt().getTime()) / 1000 / 60) < 30) {
			return 8;
		}
		Date start;
		Date end;
		if (entity.getType() == WebConstants.EXAM_WK_TYPE || entity.getType() == WebConstants.EXAM_XK_TYPE) {
			start = entity.getExamBatchNew().getOnlineSt();
			end = entity.getExamBatchNew().getOnlineEnd();
		} else {
			start = entity.getExamBatchNew().getOfflineSt();
			end = entity.getExamBatchNew().getOfflineEnd();
		}
		if ((null != start && null != end) && (entity.getExamSt().before(start) || entity.getExamEnd().after(end))) {
			return 9;
		}
		return 0;
	}

	private String getErrStr(int type) {
		switch (type) {
			case 1:
				return "试卷号不能为空";
			case 2:
				return "考试课程不能为空";
			case 3:
				return "课程代码不能为空";
			case 4:
				return "考试类型错误";
			case 5:
				return "形考比例只能是数字并且在100范围内";
			case 6:
				return "时间格式不正确";
			case 7:
				return "时间格式不正确";
			case 8:
				return "时间格式不正确";
			case 9:
				return "时间格式不正确";
			case 10:
				return "课程号对应的课程名不对";
			case 101:
				return "课程号不存在或存在多个相同的课程号";
			case 12:
				return "不存在此课程号与试卷号对应的考试科目";
			case 121:
				return "不存在此课程号与试卷号对应的考试科目或者存在多个";
			case 13:
				return "课程不存在";
			default:
				return "未知错误: 错误代码" + type;
		}
	}

	@Override
	public GjtExamPlanNew isPlanExist(String examPlanid) {
		return gjtExamPlanNewDao.isPlanExist(examPlanid);
	}

	/**
	 * 查询考试计划列表页
	 */
	@Override
	public Page getExamPlanList(Map searchParams, PageRequest pageRequst) {
		return gjtExamPlanNewDao.getExamPlanList(searchParams, pageRequst);
	}

	/**
	 * 查询考试计划列表页(统计项)
	 */
	@Override
	public int getExamPlanCount(Map searchParams) {
		return gjtExamPlanNewDao.getExamPlanCount(searchParams);
	}

	/**
	 * 导出未设置考试时间的考试科目表
	 * 
	 * @return
	 */
	public String expExamPlanTime(Map formMap, HttpServletRequest request, HttpServletResponse response,
			PageRequest pageRequst) {
		try {
			formMap.put("PSTATUS", "1");
			Page pageList = gjtExamPlanNewDao.getExamPlanList(formMap, pageRequst);
			List dataList = pageList.getContent();

			Map beans = new HashMap();
			beans.put("dataList", dataList);

			String timeStr = ObjectUtils.toString(new Date().getTime());
			String tempPath = GjtExamPlanNewServiceImpl.class.getClassLoader().getResource("").getPath()
					+ "/excel/exam/未设置考试时间的考试科目表.xls";
			String downPath = GjtExamPlanNewServiceImpl.class.getClassLoader().getResource("").getPath()
					+ "/excel/temp/未设置考试时间的考试科目表" + timeStr + ".xls";

			XLSTransformer transformer = new XLSTransformer();
			transformer.transformXLS(tempPath, beans, downPath);
			ToolUtil.download(downPath, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 导入考试计划
	 * 
	 * @param filePaths
	 * @param formMap
	 * @param request
	 * @param response
	 * @return
	 */
	public Map importExamPlan(String filePaths, Map formMap, HttpServletRequest request, HttpServletResponse response) {
		Map resultMap = new HashMap();
		List rightList = new ArrayList();
		List errorList = new ArrayList();
		String errorMessage = "";
		try {
			String userId = ObjectUtils.toString(formMap.get("USER_ID"));
			String xxId = ObjectUtils.toString(formMap.get("XX_ID"));
			String[] titles = { "studyyearName", "batchName", "batchCode", "subjectName", "subjectCode", "courseCode",
					"type", "examNo", "bookSt", "bookEnd", "examSt", "examEnd", "status" };
			File file = new File(filePaths);
			ExcelReader reader = new ExcelReader(file, titles, 3);
			List list = reader.readAsList();
			if (EmptyUtils.isNotEmpty(list)) {
				for (Object object : list) {
					Map errorInfo = new HashMap();
					String errorstr = "";
					int errorNum = 1;

					Map tempMap = (Map<String, Object>) object;
					tempMap.put("xxId", xxId);

					String studyyearName = ObjectUtils.toString(tempMap.get("studyyearName")).trim();
					String batchName = ObjectUtils.toString(tempMap.get("batchName")).trim();
					String batchCode = ObjectUtils.toString(tempMap.get("batchCode")).trim();
					String subjectName = ObjectUtils.toString(tempMap.get("subjectName")).trim();
					String subjectCode = ObjectUtils.toString(tempMap.get("subjectCode")).trim();
					String courseCode = ObjectUtils.toString(tempMap.get("courseCode")).trim();
					String type = ObjectUtils.toString(tempMap.get("type")).trim();
					String examNo = ObjectUtils.toString(tempMap.get("examNo")).trim();
					String bookSt = ObjectUtils.toString(tempMap.get("bookSt")).trim();
					String bookEnd = ObjectUtils.toString(tempMap.get("bookEnd")).trim();
					String examSt = ObjectUtils.toString(tempMap.get("examSt")).trim();
					String examEnd = ObjectUtils.toString(tempMap.get("examEnd")).trim();
					String status = ObjectUtils.toString(tempMap.get("status")).trim();

					if (EmptyUtils.isNotEmpty(studyyearName) || EmptyUtils.isNotEmpty(batchName)) {
						if (EmptyUtils.isEmpty(studyyearName)) {
							errorstr += "学年度不能为空，";
							errorNum++;
						}

						if (EmptyUtils.isEmpty(batchName)) {
							errorstr += "考试批次不能为空，";
							errorNum++;
						}

						if (EmptyUtils.isEmpty(batchCode)) {
							errorstr += "考试批次编号不能为空，";
							errorNum++;
						}

						if (EmptyUtils.isEmpty(subjectName)) {
							errorstr += "考试科目不能为空，";
							errorNum++;
						}

						if (EmptyUtils.isEmpty(subjectCode)) {
							errorstr += "考试科目编号不能为空，";
							errorNum++;
						}

						if (EmptyUtils.isEmpty(courseCode)) {
							errorstr += "课程号不能为空，";
							errorNum++;
						}

						if (EmptyUtils.isEmpty(type)) {
							errorstr += "考试方式不能为空，";
							errorNum++;
						}

						if (EmptyUtils.isEmpty(examNo)) {
							errorstr += "试卷号不能为空，";
							errorNum++;
						}

						if (EmptyUtils.isEmpty(bookSt)) {
							errorstr += "预约考试开始时间不能为空，";
							errorNum++;
						}

						if (EmptyUtils.isEmpty(bookEnd)) {
							errorstr += "预约考试结束时间不能为空，";
							errorNum++;
						}

						if (EmptyUtils.isEmpty(examSt)) {
							errorstr += "考试开始时间不能为空，";
							errorNum++;
						} else if (!DateUtils.isValidDateFormat(examSt, "yyyy-MM-dd HH:mm")) {
							errorstr += "考试开始时间格式错误，";
							errorNum++;
						}

						if (EmptyUtils.isEmpty(examEnd)) {
							errorstr += "考试结束时间不能为空，";
							errorNum++;
						} else if (!DateUtils.isValidDateFormat(examEnd, "yyyy-MM-dd HH:mm")) {
							errorstr += "考试结束时间格式错误，";
							errorNum++;
						}

						String examType = "";
						if ("网考".equals(type)) {
							examType = "1";
						} else if ("笔考".equals(type)) {
							examType = "2";
						} else if ("机考".equals(type)) {
							examType = "3";
						} else if ("形考".equals(type)) {
							examType = "4";
						} else if ("网考（省）".equals(type)) {
							examType = "5";
						}

						if (EmptyUtils.isEmpty(errorstr)) {
							Map pMap = new HashMap();
							pMap.put("TYPE", examType);
							pMap.put("STUDY_YEAR_NAME", studyyearName);
							pMap.put("EXAM_BATCH_CODE", batchCode);
							pMap.put("SUBJECT_CODE", subjectCode);
							pMap.put("EXAM_ST", examSt);
							pMap.put("EXAM_END", examEnd);
							List subList = gjtExamPlanNewDao.getPlanListByCode(pMap);
							if (EmptyUtils.isNotEmpty(subList)) {
								Map subMap = (Map) subList.get(0);
								String exam_plan_id = ObjectUtils.toString(subMap.get("EXAM_PLAN_ID"));
								String time_error1 = ObjectUtils.toString(subMap.get("TIME_ERROR1"));
								String time_error2 = ObjectUtils.toString(subMap.get("TIME_ERROR2"));
								String time_error3 = ObjectUtils.toString(subMap.get("TIME_ERROR3"));
								if ("1".equals(time_error1)) {
									errorstr += "考试开始时间应该大于考试预约结束时间，";
									errorNum++;
								} else if ("1".equals(time_error2)) {
									errorstr += "考试开始时间应该小于考试结束时间，";
									errorNum++;
								} else if ("1".equals(time_error3)) {
									errorstr += "考试开始时间、考试结束时间之间相差应该大于30分钟，";
									errorNum++;
								} else {
									pMap.put("EXAM_PLAN_ID", exam_plan_id);
									int num = gjtExamPlanNewDao.updPlanExamTime(pMap);
									if (num <= 0) {
										errorstr += "计划设置失败，";
										errorNum++;
									}
								}
							} else {
								errorstr += "该计划不存在，";
								errorNum++;
							}
						}

						if (EmptyUtils.isEmpty(errorstr)) {
							rightList.add(tempMap);
						} else {
							tempMap.put("errorstr", errorstr);
							errorList.add(tempMap);
						}
					}

				}
			}

			String timeStr = ObjectUtils.toString(new Date().getTime());

			// 导出成功记录
			String tempPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()
					+ "/excel/exam/未设置考试时间的考试科目成功表.xls";
			String downPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()
					+ "/excel/temp/未设置考试时间的考试科目成功表" + timeStr + ".xls";
			Map beans = new HashMap();
			beans.put("dataList", rightList);
			XLSTransformer transformer = new XLSTransformer();
			transformer.transformXLS(tempPath, beans, downPath);
			resultMap.put("RIGHT_DOWNPATH", downPath);

			// 导出失败记录
			tempPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()
					+ "/excel/exam/未设置考试时间的考试科目失败表.xls";
			downPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()
					+ "/excel/temp/未设置考试时间的考试科目失败表" + timeStr + ".xls";
			beans = new HashMap();
			beans.put("dataList", errorList);
			transformer = new XLSTransformer();
			transformer.transformXLS(tempPath, beans, downPath);
			resultMap.put("ERROR_DOWNPATH", downPath);

		} catch (Exception e) {
			errorMessage = "excel格式不符合要求，请下载正确的模板，填写后再进行导入！";
			e.printStackTrace();
		}
		resultMap.put("errorMessage", errorMessage);
		resultMap.put("errorNum", errorList.size());
		resultMap.put("rightNum", rightList.size());
		resultMap.put("allNum", errorList.size() + rightList.size());
		return resultMap;
	}

	/**
	 * 考试科目编码规则:默认在前面加上考试类型缩写
	 * 
	 * @param type
	 * @return
	 */
	@Override
	public String getSubjectCode(int type) {
		String prefix = "BS";
		switch (type) {
			case 1:
				prefix = "WK";
				break;
			case 2:
				prefix = "BS";
				break;
			case 3:
				prefix = "JK";
				break;
			case 4:
				prefix = "XK";
				break;
			case 5:
				prefix = "WK";
				break;
			case 6:
				prefix = "GK";
				break;
			case 7:
				prefix = "SK";
				break;
			case 8:
				prefix = "LW";
				break;
			case 9:
				prefix = "BG";
				break;
			case 10:
				prefix = "ZY";
				break;
			case 11:
				prefix = "LG";
				break;
			default:
				break;
		}
		return prefix + codeGeneratorService.codeGenerator(CacheConstants.SUBJECT_CODE);
	}

	/**
	 * 考试科目编码规则:默认在前面加上考试类型缩写
	 *
	 * @param name
	 * @return
	 */
	@Override
	public String getSubjectCodeByName(String name) {
		int type = 0;
		if ("网考".equals(type)) {
			type = 1;
		} else if ("笔试".equals(type)) {
			type = 2;
		} else if ("机考".equals(type)) {
			type = 3;
		} else if ("形考".equals(type)) {
			type = 4;
		} else if ("省网考".equals(type)) {
			type = 5;
		} else if ("全国统考".equals(type)) {
			type = 6;
		} else if ("省统考".equals(type)) {
			type = 7;
		} else if ("论文".equals(type)) {
			type = 8;
		} else if ("报告".equals(type)) {
			type = 9;
		} else if ("大作业".equals(type)) {
			type = 10;
		} else if ("论文报告".equals(type)) {
			type = 11;
		}
		String code = getSubjectCode(type);
		return code;
	}

	/**
	 * 考试科目编码规则:默认在前面加上考试类型缩写
	 *
	 * @param name
	 * @return
	 */
	@Override
	public int getExamTypeByName(String name) {
		int type = 0;
		if ("网考".equals(name)) {
			type = 1;
		} else if ("笔试".equals(name)) {
			type = 2;
		} else if ("机考".equals(name)) {
			type = 3;
		} else if ("形考".equals(name)) {
			type = 4;
		} else if ("省网考".equals(name)) {
			type = 5;
		} else if ("全国统考".equals(name)) {
			type = 6;
		} else if ("省统考".equals(name)) {
			type = 7;
		} else if ("论文".equals(name)) {
			type = 8;
		} else if ("报告".equals(name)) {
			type = 9;
		} else if ("大作业".equals(name)) {
			type = 10;
		} else if ("论文报告".equals(name)) {
			type = 11;
		}
		return type;
	}

	public Page<Map> queryExamPlan(Map formMap, PageRequest pageRequst) {
		return gjtExamPlanNewDao.queryExamPlan(formMap, pageRequst);
	}

	public List queryByCourseIdAndBatchCodeAndExamType(String courseId, String batchCode, String examTYpe) {
		return gjtExamPlanNewDao.findByExamBatchCodeAndCourseIdAndExamType(batchCode, courseId, examTYpe);
	}

	/**
	 * 导出考试计划
	 * 
	 * @param list
	 * @return
	 */
	@Override
	public HSSFWorkbook exportByList(List<GjtExamPlanNew> list) {

		Map<String, String> examTypes = commonMapService.getExamTypeMapNew();
		Map<String, String> examStyleMap = commonMapService.getDates("EXAM_STYLE");
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row;
		HSSFCell cell;
		int rowIndex = 0;
		int cellIndex = 0;
		// 标题行
		row = sheet.createRow(rowIndex++);

		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试计划");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("开考科目名称");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("试卷号");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试课程");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("课程代码");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试形式");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试方式");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("形考比例（%）");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试专业");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("培养层次");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("专业代码");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试开始时间");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试结束时间");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试预约方式");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试预约最低分数限制（分）");

		cell = row.createCell(cellIndex++);
		cell.setCellValue("状态");

		for (GjtExamPlanNew plan : list) {
			cellIndex = 0;
			row = sheet.createRow(rowIndex++);

			cell = row.createCell(cellIndex++);
			cell.setCellValue(plan.getExamBatchNew().getName() + "（" + plan.getExamBatchNew().getExamBatchCode() + "）");

			cell = row.createCell(cellIndex++);
			cell.setCellValue(plan.getExamPlanName());

			cell = row.createCell(cellIndex++);
			cell.setCellValue(plan.getExamNo());

			Set<String> courseNames = new HashSet<String>();
			Set<String> courseCodes = new HashSet<String>();
			List<GjtCourse> gjtCourseList = plan.getGjtCourseList();
			if (gjtCourseList != null && gjtCourseList.size() > 0) {
				for (int i = 0; i < gjtCourseList.size(); i++) {
					if (EmptyUtils.isNotEmpty(gjtCourseList.get(i).getKcmc())) {
						String courseName = ObjectUtils.toString(gjtCourseList.get(i).getKcmc(), "--");
						String courseCode = ObjectUtils.toString(gjtCourseList.get(i).getKch(), "--");
						courseNames.add(courseName);
						courseCodes.add(courseCode);
					}
				}
			}

			cell = row.createCell(cellIndex++);
			cell.setCellValue(com.gzedu.xlims.common.StringUtils.arrayToString(courseNames.toArray()));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(com.gzedu.xlims.common.StringUtils.arrayToString(courseCodes.toArray()));

			cell = row.createCell(cellIndex++);
			cell.setCellValue(plan.getType() != null ? examTypes.get(new BigDecimal(plan.getType())) : "");

			cell = row.createCell(cellIndex++);
			cell.setCellValue(examStyleMap.get(plan.getExamStyle()));

			cell = row.createCell(cellIndex++);
			cell.setCellValue(ObjectUtils.toString(plan.getXkPercent(), ""));

			String pyccName = "";
			Set<String> specialtyNames = new HashSet<String>();
			Set<String> specialtyCodes = new HashSet<String>();
			List<GjtSpecialty> gjtSpecialtyList = plan.getGjtSpecialtyList();
			if (gjtSpecialtyList != null && gjtSpecialtyList.size() > 0) {
				pyccName = cacheService.getCachedDictionaryName("TrainingLevel", gjtSpecialtyList.get(0).getPycc());
				for (int i = 0; i < gjtSpecialtyList.size(); i++) {
					specialtyNames.add(gjtSpecialtyList.get(i).getGjtSpecialtyBase().getSpecialtyName());
					specialtyCodes.add(gjtSpecialtyList.get(i).getGjtSpecialtyBase().getSpecialtyCode());
				}
			}

			cell = row.createCell(cellIndex++);
			cell.setCellValue(com.gzedu.xlims.common.StringUtils.arrayToString(specialtyNames.toArray()));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(pyccName);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(com.gzedu.xlims.common.StringUtils.arrayToString(specialtyCodes.toArray()));

			cell = row.createCell(cellIndex++);
			cell.setCellValue(this.getDateStr(plan.getExamSt()));

			cell = row.createCell(cellIndex++);
			cell.setCellValue(this.getDateStr(plan.getExamEnd()));

			cell = row.createCell(cellIndex++);
			if ("1".equals(plan.getExamPlanOrder())) {
				cell.setCellValue("个人预约");
			} else {
				cell.setCellValue("集体预约");
			}

			cell = row.createCell(cellIndex++);
			if ("0".equals(plan.getExamPlanLimit())) {
				cell.setCellValue("无分数限制");
			} else {
				cell.setCellValue("大于等于" + plan.getExamPlanLimit() + "分");
			}

			cell = row.createCell(cellIndex++);
			if (plan.getStatus() == 1) {
				cell.setCellValue("未开始");
			} else if (plan.getStatus() == 2) {
				cell.setCellValue("预约中");
			} else if (plan.getStatus() == 3) {
				cell.setCellValue("待考试");
			} else if (plan.getStatus() == 4) {
				cell.setCellValue("考试中");
			} else if (plan.getStatus() == 5) {
				cell.setCellValue("已结束");
			} else {
				cell.setCellValue("--");
			}
		}

		return workbook;
	}

	@Override
	public String importUpdatePlans(String path, GjtUserAccount user, String savePath) {
		String reFile = null;

		Set<String> examNoSet = new HashSet();// 已有试卷号唯一集合
		List<GjtExamPlanNew> allExamPlan = gjtExamPlanNewDao.findByIsDeletedAndExamNoIsNotNull(0);
		for (GjtExamPlanNew plan : allExamPlan) {
			examNoSet.add(plan.getExamNo());
		}

		String[] dataNames = new String[] { "examPlanId", "studyyearName", "batchName", "batchCode", "courseName",
				"courseCode", "examNo", "type", "xkPercent", "bookSt", "bookEnd", "examSt", "examEnd", "status",
				"msg" };
		String[] fields = new String[] { "考试计划id", "学年度名称", "考试批次名称", "批次编码", "考试课程", "课程号", "试卷号", "考试方式", "形考比例",
				"预约开始时间", "预约结束时间", "考试开始时间", "考试结束时间", "状态", "更新结果" };
		String suffix = path.substring(path.lastIndexOf("."), path.length());// 后缀检验
		if (suffix.isEmpty() && !suffix.contains("xls")) {
			suffix = ExcelService.TYPE_03;
		}
		List<String[]> datas = ExcelService.getDateFormExcl(new File(path), (short) (dataNames.length - 1), 1, suffix);// 获取数据
		List<Map> results = new ArrayList<Map>();
		for (String[] data : datas) {
			Map<String, String> temp = new HashMap();// 返回表格的单行记录
			for (int i = 0; i < dataNames.length - 1; i++) {
				temp.put(dataNames[i], data[i]);
			}
			GjtExamPlanNew plan = new GjtExamPlanNew();
			String msg = null;// 操作结果
			String examPlanId = data[0].trim();
			String examNo = data[6].trim();
			String xkPercent = data[8];
			String bookSt = data[9];
			String bookEnd = data[10];
			String examSt = data[11];
			String examEnd = data[12];
			// 检验数据有效性
			if (EmptyUtils.isNotEmpty(examPlanId)) {
				plan = gjtExamPlanNewDao.queryBy(examPlanId);
				if (EmptyUtils.isEmpty(plan)) {
					msg = "考试计划不存在 请下载重新下载正确表格进行填写" + examPlanId;
				}
			}
			if (EmptyUtils.isNotEmpty(examNo)) {
				// 校验试卷号唯一
				if (examNoSet.contains(examNo)) {
					msg = "试卷号已存在 " + examNo;
				} else {
					plan.setExamNo(examNo);
				}
			}
			if (EmptyUtils.isEmpty(bookSt) || EmptyUtils.isEmpty(bookEnd)) {
				msg = "预约时间不能为空";
			}
			if (EmptyUtils.isEmpty(examSt) || EmptyUtils.isEmpty(examEnd)) {
				msg = "考试时间不能为空";
			}
			if (EmptyUtils.isNotEmpty(xkPercent)) {// 形考比例不为空 可以修改
				try {
					plan.setXkPercent(Integer.parseInt(xkPercent));
				} catch (ClassCastException e) {
					System.out.println(this.getClass() + "  类型转换异常 '" + xkPercent + "' \t" + e);
				}
			}
			try {
				plan.setBookSt(DateUtils.getStrToDate(bookEnd));
				plan.setBookEnd(DateUtils.getStrToDate(bookEnd));
				plan.setExamSt(DateUtils.getStrToDate(examSt));
				plan.setExamEnd(DateUtils.getStrToDate(examEnd));
			} catch (ParseException e) {
				msg = "日期格式应为 yyyy-MM-dd HH:mm:ss";
			}

			if (EmptyUtils.isEmpty(msg)) {
				plan = gjtExamPlanNewDao.save(plan);
				plan.setUpdatedDt(new Date());
				plan.setUpdatedBy(user.getId());
				if (EmptyUtils.isNotEmpty(plan))
					msg = "更新成功";
				else
					msg = "更新失败";
			}
			temp.put("msg", msg);
			results.add(temp);
		}

		// 渲染结果数据
		reFile = ExcelService.renderExcel(results, fields, dataNames, "考试安排调整反馈", savePath);
		return savePath + reFile;
	}

	/**
	 * 导入新的开考科目
	 * 
	 * @param path
	 * @param user
	 * @param pcId
	 *            考试批次id
	 * @return
	 */
	@Override
	public HSSFWorkbook importBlankPlans(String path, GjtUserAccount user, String pcId) {
		HSSFWorkbook resultWb = null;// 直接将结果放在excel 返回
		resultWb = new HSSFWorkbook();
		HSSFSheet sheet = resultWb.createSheet();
		HSSFRow row;
		HSSFCell cell;
		int rowIndex = 0;
		int cellIndex = 0;

		// 试卷号 考试课程 课程号 考试方式 形考比例 预约开始时间 预约结束时间 考试开始时间 考试结束时间
		String[] titles = { "examNo", "courseName", "courseCode", "type", "xkPercent", "bookSt", "bookEnd", "examSt",
				"examEnd" };
		File file = new File(path);
		ExcelReader reader = new ExcelReader(file, titles, 3);
		List<Object> list = reader.readAsList();
		Map<String, Object> map;
		Map<Integer, Integer> errorMap = new LinkedHashMap<Integer, Integer>();
		Integer indexCount = 3;// 0 1 2 题行忽略
		int errorCode = 0;
		List<GjtExamPlanNew> creatList = new ArrayList<GjtExamPlanNew>();
		int rowCount = 0;

		GjtExamBatchNew gjtExamBatchNew = gjtExamBatchNewService.queryByExamBatchCode(pcId);//

		Map examTypes = commonMapService.getExamTypeMap();// 考试方式集合

		if (EmptyUtils.isNotEmpty(list) && !"".equals(((Map) list.get(0)).get("examNo"))) {// 判断是否有数据可以导入
			for (Object m : list) {
				indexCount++;
				boolean flag = true;// 验证格式判断的
				String examNo = ObjectUtils.toString(((Map) m).get("examNo"), "").trim();// 试卷号
				String courseName = ObjectUtils.toString(((Map) m).get("courseName"), "").trim();// 考试课程
				String courseCode = ObjectUtils.toString(((Map) m).get("courseCode"), "").trim();// 课程代码（注意这个是课程代码，不是考试科目编码，你懂的）
				String type = ObjectUtils.toString(((Map) m).get("type"), "").trim();// 考试方式
				String xkPercent = ObjectUtils.toString(((Map) m).get("xkPercent"), "").trim();// 形考比例
				String bookSt = ObjectUtils.toString(((Map) m).get("bookSt"), "").trim();// 预约开始时间
				String bookEnd = ObjectUtils.toString(((Map) m).get("bookEnd"), "").trim();// 预约结束时间
				String examSt = ObjectUtils.toString(((Map) m).get("examSt"), "").trim();// 考试开始时间
				String examEnd = ObjectUtils.toString(((Map) m).get("examEnd"), "").trim();// 考试结束时间

				if (EmptyUtils.isEmpty(examNo) && EmptyUtils.isEmpty(courseName) && EmptyUtils.isEmpty(courseCode)
						&& EmptyUtils.isEmpty(type) && EmptyUtils.isEmpty(bookSt) && EmptyUtils.isEmpty(bookEnd)) {
					continue;
				}

				Date bookStartTime = null;
				Date bookEndTime = null;
				Date examStartTime = null;
				Date examEndTime = null;
				GjtCourse gjtCourse = null;
				if (EmptyUtils.isEmpty(examNo)) {
					flag = false;
					errorMap.put(indexCount, 1);
				}
				if (EmptyUtils.isEmpty(courseName)) {
					flag = false;
					errorMap.put(indexCount, 2);
				}
				if (EmptyUtils.isEmpty(courseCode)) {
					flag = false;
					errorMap.put(indexCount, 3);
				} else {
					gjtCourse = gjtCourseService.queryByKch(courseCode, "N");
					if (EmptyUtils.isNotEmpty(gjtCourse)) {
						if (!gjtCourse.getKcmc().equals(courseName)) {
							flag = false;
							errorMap.put(indexCount, 10);// 课程名称不对
						}
					} else {
						flag = false;
						errorMap.put(indexCount, 13);// 课程不存在
					}
				}
				if (EmptyUtils.isEmpty(type) || !validatorExamType(type)) {
					flag = false;
					errorMap.put(indexCount, 4);
				}
				if (EmptyUtils.isEmpty(xkPercent) || !NumberUtils.isNumber(xkPercent)) {
					flag = false;
					errorMap.put(indexCount, 5);
				}

				if (EmptyUtils.isEmpty(bookSt)) {
					flag = false;
					errorMap.put(indexCount, 6);
				} else {
					try {
						bookStartTime = DateUtils.getStrToDate(bookSt);
					} catch (Exception e) {
						flag = false;
						e.printStackTrace();
						errorMap.put(indexCount, 6);
					}
				}
				if (EmptyUtils.isEmpty(bookEnd)) {
					flag = false;
					errorMap.put(indexCount, 7);
				} else {
					try {
						bookEndTime = DateUtils.getStrToDate(bookEnd);
					} catch (Exception e) {
						flag = false;
						e.printStackTrace();
						errorMap.put(indexCount, 7);
					}
				}

				// if(EmptyUtils.isEmpty(examSt)){
				// flag = false;
				// errorMap.put(indexCount,8);
				// }else {
				// try {
				// examStartTime = DateUtils.getStrToDate(examSt);
				// } catch (ParseException e) {
				// flag = false;
				// e.printStackTrace();
				// errorMap.put(indexCount,8);
				// }
				// }

				// if(EmptyUtils.isEmpty(examEnd)){
				// flag = false;
				// errorMap.put(indexCount,9);
				// }else {
				// try {
				// examEndTime = DateUtils.getStrToDate(examEnd);
				// } catch (ParseException e) {
				// flag = false;
				// e.printStackTrace();
				// errorMap.put(indexCount,9);
				// }
				// }

				String examPlanId = SequenceUUID.getSequence();

				if (EmptyUtils.isEmpty(examPlanId)) {
					flag = false;
					errorMap.put(indexCount, 11);
				}
				// TODO 检验课程号 试卷号跟批次号是否存在

				if (!flag) {
					++errorCode;
					continue;
				}

				GjtExamPlanNew gjtExamPlanNew = new GjtExamPlanNew();

				gjtExamPlanNew.setExamPlanId(examPlanId);
				gjtExamPlanNew.setStudyYearId(gjtExamBatchNew.getStudyYearId());

				Iterator<Map.Entry<BigDecimal, String>> entries = examTypes.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry<BigDecimal, String> entry = entries.next();
					if (type.equals(entry.getValue())) {
						gjtExamPlanNew.setType(entry.getKey().intValue());
					}
				}

				gjtExamPlanNew.setBookSt(bookStartTime);
				gjtExamPlanNew.setBookEnd(bookEndTime);
				// gjtExamPlanNew.setExamSt(examStartTime);
				// gjtExamPlanNew.setExamEnd(examEndTime);
				gjtExamPlanNew.setXxId(gjtExamBatchNew.getXxId());
				gjtExamPlanNew.setCreatedBy(user.getId());
				gjtExamPlanNew.setCreatedDt(new Date());
				gjtExamPlanNew.setExamBatchCode(gjtExamBatchNew.getExamBatchCode());
				gjtExamPlanNew.setCourseID(gjtCourse.getCourseId());
				gjtExamPlanNew.setExamBatchId(gjtExamBatchNew.getExamBatchId());
				gjtExamPlanNew.setIsDeleted(0);
				gjtExamPlanNew.setXkPercent(Integer.parseInt(xkPercent));
				gjtExamPlanNew.setExamNo(examNo);
				// gjtExamSubjectNew.setSubjectCode(subjectCode);
				// gjtExamSubjectNew.setCourseId(gjtCourse.getCourseId());
				// gjtExamSubjectNew.setName(subjectName);
				// gjtExamSubjectNew.setType(getExamTypeByName(type));
				// gjtExamSubjectNew.setExamNo(examNo);
				// gjtExamSubjectNew.setMemo("来自导入...");
				// gjtExamSubjectNew.setXxId(gjtExamBatchNew.getXxId());
				// gjtExamSubjectNew.setCreatedBy(user.getId());
				// gjtExamSubjectNew.setCreatedDt(new Date());
				// gjtExamSubjectNew.setKch(courseCode);
				// createExamSubjectNews.add(gjtExamSubjectNew);

				creatList.add(gjtExamPlanNew);
			}
		} else {

		}

		StringBuilder sbuilder = new StringBuilder();
		int rs = this.insertBatch(creatList);
		if (rs > 0) {
			sbuilder.append("成功设置" + rs + "条数据.");
		}

		// write error date to resultWb
		if (errorMap.size() > 0) {
			sbuilder.append(" 失败" + errorMap.size() + "条. 错误数据如下:");
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(sbuilder.toString());

			Set<Entry<Integer, Integer>> set = errorMap.entrySet();
			Iterator<Entry<Integer, Integer>> enties = set.iterator();
			Entry<Integer, Integer> entity;
			while (enties.hasNext()) {
				entity = enties.next();
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue("第" + entity.getKey() + "行");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("原因:" + this.getErrStr(entity.getValue()));
			}
		} else {
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(sbuilder.toString());
		}
		return resultWb;
	}

	@Override
	public GjtExamPlanNew findByExamBatchCodeAndExamNoAndType(String examBatchCode, String examNo, int type) {
		return gjtExamPlanNewDao.findByExamBatchCodeAndExamNoAndTypeAndIsDeleted(examBatchCode, examNo, type, 0);
	}

	@Deprecated
	@Override
	public GjtExamPlanNew findByExamBatchCodeAndExamNoAndTypeAndGjtCourseListCourseId(String examBatchCode,
			String examNo, int type, String courseId) {
		return gjtExamPlanNewDao.findByExamBatchCodeAndExamNoAndTypeAndGjtCourseListCourseIdAndIsDeleted(examBatchCode,
				examNo, type, courseId, 0);
	}

	@Override
	public int insertExamPlanSpecialty(Map<String, Object> params) {
		return gjtExamPlanNewDao.insertExamPlanSpecialty(params);
	}

	@Override
	public Page getExamManagmentList(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:【xx_id=" + searchParams.get("XX_ID") + "】");
		return gjtExamPlanNewDao.getExamManagmentList(searchParams, pageRequst);
	}

	@Override
	public Page<Map<String, Object>> getArrangeExamRecordList(Map<String, Object> searchParams,
			PageRequest pageRequst) {
		log.info("searchParams:【xx_id=" + searchParams.get("XX_ID") + ";exam_plan_id="
				+ searchParams.get("EXAM_PLAN_ID") + ";exam_batch_code=" + searchParams.get("EXAM_BATCH_CODE") + "】");
		return gjtExamPlanNewDao.getArrangeExamRecordList(searchParams, pageRequst);
	}

	@Override
	public List<Map<String, String>> getStudentAdmissionTicket(Map<String, Object> searchParams) {
		log.info("searchParams:【student_id=" + searchParams.get("STUDENT_ID") + ";exam_plan_id="
				+ searchParams.get("EXAM_PLAN_ID") + "】");
		return gjtExamPlanNewDao.getStudentAdmissionTicket(searchParams);
	}

	@Override
	public Map getExamBatchList(Map<String, Object> searchParams) {
		log.info(searchParams.toString());
		Map resultMap = new LinkedHashMap();
		try {
			List examBatchList = new ArrayList();
			List list = gjtExamPlanNewDao.getExamBatchList(searchParams);
			if (EmptyUtils.isNotEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);

					Map result = new LinkedHashMap();
					result.put("EXAM_BATCH_CODE", ObjectUtils.toString(map.get("EXAM_BATCH_CODE")));
					result.put("EXAM_BATCH_NAME", ObjectUtils.toString(map.get("EXAM_BATCH_NAME")));
					result.put("GRADE_ID", ObjectUtils.toString(map.get("GRADE_ID")));
					result.put("GRADE_NAME", ObjectUtils.toString(map.get("GRADE_NAME")));
					result.put("BOOK_ST", ObjectUtils.toString(map.get("BOOK_ST")));
					result.put("BOOK_END", ObjectUtils.toString(map.get("BOOK_END")));
					result.put("ARRANGE_ST", ObjectUtils.toString(map.get("ARRANGE_ST")));
					result.put("ARRANGE_END", ObjectUtils.toString(map.get("ARRANGE_END")));
					result.put("ONLINE_ST", ObjectUtils.toString(map.get("ONLINE_ST")));
					result.put("ONLINE_END", ObjectUtils.toString(map.get("ONLINE_END")));
					result.put("PROVINCE_ONLINE_ST", ObjectUtils.toString(map.get("PROVINCE_ONLINE_ST")));
					result.put("PROVINCE_ONLINE_END", ObjectUtils.toString(map.get("PROVINCE_ONLINE_END")));
					result.put("PAPER_ST", ObjectUtils.toString(map.get("PAPER_ST")));
					result.put("PAPER_END", ObjectUtils.toString(map.get("PAPER_END")));
					result.put("MACHINE_ST", ObjectUtils.toString(map.get("MACHINE_ST")));
					result.put("MACHINE_END", ObjectUtils.toString(map.get("MACHINE_END")));
					result.put("SHAPE_END", ObjectUtils.toString(map.get("SHAPE_END")));
					result.put("THESIS_END", ObjectUtils.toString(map.get("THESIS_END")));
					result.put("REPORT_END", ObjectUtils.toString(map.get("REPORT_END")));
					result.put("RECORD_ST", ObjectUtils.toString(map.get("RECORD_ST")));
					result.put("RECORD_END", ObjectUtils.toString(map.get("RECORD_END")));

					examBatchList.add(result);
					resultMap.put("examBatchList", examBatchList);

				}
			} else {
				resultMap.put("examBatchList", new ArrayList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public boolean isUpdate(String examBatchCode) {
		Long countUpdateExamPlan = gjtExamPlanNewRepository.countUpdateExamPlan(examBatchCode);
		return countUpdateExamPlan > 0;
	}

	@Override
	public boolean initAutoExamPlan(String xxId, String examBatchCode) {
		GjtExamBatchNew examBatchNew = gjtExamBatchNewService.queryByExamBatchCode(examBatchCode);
		List<Map<String, Object>> examPlans = gjtExamPlanNewDao.getExamPlanByTeachPlan(xxId);
		try {
			// 删除所有开考科目
			gjtExamPlanNewRepository.deleteByExamBatchCode(examBatchCode);
			for (Map<String, Object> info : examPlans) {
				String examNo = (String) info.get("EXAM_NO");
				Object COURSE_ID_OBJECT = info.get("COURSE_ID");
				String replaceExamNo = (String) info.get("REPLACE_EXAM_NO");
				String courseIds = null;
				if (COURSE_ID_OBJECT instanceof Clob) {
					Clob COURSE_ID = (Clob) COURSE_ID_OBJECT;
					courseIds = COURSE_ID != null ? COURSE_ID.getSubString(1, (int) COURSE_ID.length()) : null;
				} else {
					courseIds = (String) COURSE_ID_OBJECT;
				}
				// 20180411 生成中央课程全部为通用的
				String[] courseIdArr = courseIds.split(",");
				Set<String> cids = new HashSet<String>(); // ID去重
				List<GjtCourse> gjtCourseList = new ArrayList<GjtCourse>();
				for (int i = 0; i < courseIdArr.length; i++) {
					cids.add(courseIdArr[i]);
				}
				for (String cid : cids) {
					gjtCourseList.add(new GjtCourse(cid));
				}
				List<GjtSpecialty> gjtSpecialtyList = new ArrayList<GjtSpecialty>();
				GjtExamPlanNew examPlanNew = new GjtExamPlanNew();
				examPlanNew.setSubjectType(1); // 中央课程科目
				examPlanNew.setExamNo(examNo);
				if (cids.size() == 1) {
					examPlanNew.setExamPlanName(gjtCourseService.queryById(cids.iterator().next()).getKcmc());
				}
				examPlanNew.setType(-1); // 默认设为未分配
				examPlanNew.setExamPlanOrder("1");
				examPlanNew.setExamPlanLimit("0");

				examPlanNew.setExamBatchId(examBatchNew.getExamBatchId());
				examPlanNew.setExamBatchCode(examBatchCode);
				examPlanNew.setGradeId(examBatchNew.getGradeId());
				examPlanNew.setXxId(xxId);
				examPlanNew.setCreatedBy("自动生成开考科目");
				examPlanNew.setCreatedDt(new Date());

				examPlanNew.setGjtCourseList(gjtCourseList);
				examPlanNew.setGjtSpecialtyList(gjtSpecialtyList);

				examPlanNew.setStudyYearId(examBatchNew.getStudyYearId());
				this.insert(examPlanNew);
				insertExamPlanToTongyongSpecialty(examPlanNew.getExamPlanId());
			}
		} catch (SQLException e) {
			return false;
		}

		List<Map<String, Object>> examPlansReplace = gjtExamPlanNewDao.getExamPlanByTeachPlanReplace(xxId);
		try {
			for (Map<String, Object> info : examPlansReplace) {
				Object courseId = info.get("COURSE_ID");
				String replaceExamNo = (String) info.get("REPLACE_EXAM_NO");
				String courseIds = null;
				if (courseId instanceof Clob) {
					Clob COURSE_ID = (Clob) courseId;
					courseIds = COURSE_ID != null ? COURSE_ID.getSubString(1, (int) COURSE_ID.length()) : null;
				} else {
					courseIds = (String) courseId;
				}
				// 20180411 生成
				String[] courseIdArr = courseIds.split(",");
				Set<String> cids = new HashSet<String>(); // ID去重
				List<GjtCourse> gjtCourseList = new ArrayList<GjtCourse>();
				for (int i = 0; i < courseIdArr.length; i++) {
					cids.add(courseIdArr[i]);
				}
				for (String cid : cids) {
					gjtCourseList.add(new GjtCourse(cid));
				}
				List<GjtSpecialty> gjtSpecialtyList = new ArrayList<GjtSpecialty>();
				GjtExamPlanNew examPlanNew = new GjtExamPlanNew();
				examPlanNew.setSubjectType(2); // 替换课程科目
				examPlanNew.setExamNo(replaceExamNo);
				if (cids.size() == 1) {
					examPlanNew.setExamPlanName(gjtCourseService.queryById(cids.iterator().next()).getKcmc());
				}
				examPlanNew.setType(-1); // 默认设为未分配
				examPlanNew.setExamPlanOrder("1");
				examPlanNew.setExamPlanLimit("0");

				examPlanNew.setExamBatchId(examBatchNew.getExamBatchId());
				examPlanNew.setExamBatchCode(examBatchCode);
				examPlanNew.setGradeId(examBatchNew.getGradeId());
				examPlanNew.setXxId(xxId);
				examPlanNew.setCreatedBy("自动生成开考科目");
				examPlanNew.setCreatedDt(new Date());

				examPlanNew.setGjtCourseList(gjtCourseList);
				examPlanNew.setGjtSpecialtyList(gjtSpecialtyList);

				examPlanNew.setStudyYearId(examBatchNew.getStudyYearId());
				this.insert(examPlanNew);
				insertExamPlanToTongyongSpecialty(examPlanNew.getExamPlanId());
			}
		} catch (SQLException e) {
			return false;
		}

		return true;
	}
}
