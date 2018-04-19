package com.gzedu.xlims.serviceImpl.exam;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.ptg.IntPtg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.ExcelReader;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.GjtCourseDao;
import com.gzedu.xlims.dao.exam.GjtExamSubjectCourseDao;
import com.gzedu.xlims.dao.exam.GjtExamSubjectNewDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.exam.GjtExamSubjectNewService;

import net.sf.jxls.transformer.XLSTransformer;

@Service
public class GjtExamSubjectNewServiceImpl implements GjtExamSubjectNewService {

	private static final Log log = LogFactory.getLog(GjtExamSubjectNewServiceImpl.class);

	@Autowired
	private CodeGeneratorService codeGeneratorService;
	@Autowired
	private GjtExamSubjectNewDao gjtExamSubjectNewDao;
	@Autowired
	private GjtCourseDao gjtCourseDao;
	
	
	public String getSubjectCodeByName(String name) {
		int type = 0;
		if("网考".equals(type)){
			type=1;
		}else if ("笔试".equals(type)){
			type=2;
		}else if ("机考".equals(type)){
			type=3;
		}else if ("形考".equals(type)){
			type=4;
		}else if ("省网考".equals(type)){
			type=5;
		}else if ("全国统考".equals(type)){
			type=6;
		}else if ("省统考".equals(type)){
			type=7;
		}else if ("论文".equals(type)){
			type=8;
		}else if ("报告".equals(type)){
			type=9;
		}else if ("大作业".equals(type)){
			type=10;
		}else if ("论文报告".equals(type)){
			type=11;
		}
		String code = getSubjectCode(type);
		return code;
	}

	/**
	 * 新增
	 */
	@Override
	public GjtExamSubjectNew insert(GjtExamSubjectNew entity) {
		entity.setSubjectId(UUIDUtils.random());
		if (StringUtils.isEmpty(entity.getSubjectCode())) {
			entity.setSubjectCode(this.getSubjectCode(entity.getType()));
		}
		try {
			entity = gjtExamSubjectNewDao.save(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			entity = null;
		}
		return entity;
	}

	/**
	 * 查询列表
	 */
	@Override
	public Page<GjtExamSubjectNew> queryAll(String schoolId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, schoolId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, 0));
//		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		Specification<GjtExamSubjectNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExamSubjectNew.class);
		return gjtExamSubjectNewDao.findAll(spec, pageRequst);
	}

	/**
	 * 删除 PS: 未制作可以删除、修改，制作中和已发布不允许做任何操作
	 */
	@Override
	public int delete(List<String> ids, String xxid) {
		return gjtExamSubjectNewDao.deleteGjtExamSubjectNew(ids, xxid);
	}

	/**
	 * 查询id
	 */
	@Override
	public GjtExamSubjectNew queryBy(String id) {
		return gjtExamSubjectNewDao.queryBy(id);
	}

	/**
	 * 更新
	 */
	@Override
	public GjtExamSubjectNew update(GjtExamSubjectNew entity) {
		entity.setUpdatedDt(new Date());
		try {
			entity = gjtExamSubjectNewDao.save(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			entity = null;
		}
		return entity;
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
	 * 未创建考试科目的课程列表
	 */
	@Override
	public List<GjtCourse> noSubjectCourseList(String xxid) {
		return gjtExamSubjectNewDao.noSubjectCourseList(xxid);
	}

	/**
	 * 导出未创建考试科目的课程列表excel
	 */
	@Override
	public HSSFWorkbook exportNoSubjectCourseList(List<GjtCourse> list) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row;
		HSSFCell cell;
		int rowIndex = 0;
		int cellIndex = 0;

		// 说明行
		// row = sheet.createRow(rowIndex++);
		// cell = row.createCell(0);
		// cell.setCellValue("导入需知: 1.x 2.如果是笔试、机考必须设置试卷号");
		//
		// HSSFCellStyle style = workbook.createCellStyle();
		// style.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
		// CellRangeAddress cellRangeAddress =new CellRangeAddress(0, 0, 0, 5);
		// sheet.addMergedRegion(cellRangeAddress);
		// cellIndex = 0;

		// 标题行
		row = sheet.createRow(rowIndex++);
		cell = row.createCell(cellIndex++);
		cell.setCellValue("课程号(必填, 请勿修改)");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("课程名称(必填, 请勿修改)");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("科目名称(必填, 可随意修改)");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("试卷号(必填)");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("标签说明");

		for (GjtCourse course : list) {
			if (StringUtils.isBlank(course.getKch())) {// 开发环境部分数据的课程号是空格字符串...为避免线上环境有同样情况,
														// 过滤一下
				continue;
			}
			cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(course.getKch());
			cell = row.createCell(cellIndex++);
			cell.setCellValue(course.getKcmc());
			cell = row.createCell(cellIndex++);
			cell.setCellValue(course.getKcmc() + "考试");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("");
		}

		return workbook;
	}

	/**
	 * 导入新建考试科目
	 * 
	 * @param examType
	 *            考试方式:1-网考;2-笔试;3-机考; PS: 不支持更新
	 */
	// @Override
	// public HSSFWorkbook importGjtExamSubjectNew(String path, int examType,
	// GjtUserAccount user) {
	// HSSFWorkbook resultWb = null;// 直接将结果放在excel 返回
	// resultWb = new HSSFWorkbook();
	// HSSFSheet sheet = resultWb.createSheet();
	// HSSFRow row;
	// HSSFCell cell;
	// int rowIndex = 0;
	// int cellIndex = 0;
	//
	// String[] titles = { "kch", "courseName", "subjectName", "examNo", "memo"
	// };// kch
	// // for
	// // 课程号,
	// // 课程业务唯一编码
	// File file = new File(path);
	// ExcelReader reader = new ExcelReader(file, titles, 1);
	// List<Object> list = reader.readAsList();
	// Map<String, Object> map;
	// Map<String, String> courseCodeMap = new HashMap<String, String>();
	// Object kch = null;
	// for (Object object : list) {
	// map = (Map<String, Object>) object;
	// kch = map.get("kch");
	// if (null!=kch && !courseCodeMap.containsKey((String) kch)) {
	// courseCodeMap.put((String) kch, "0");
	// }
	// }
	// List<String> kchList = new ArrayList<String>(courseCodeMap.keySet());
	// Map<String, GjtExamSubjectNew> existMap =
	// gjtExamSubjectNewDao.existList(kchList, examType);// 已有考试科目记录
	// // 线上数据,最多课程的院校才384条课程记录,
	// // 不会有性能隐患.
	// // 假如数据过多再考虑转成缓存
	// List<GjtCourse> courseList =
	// this.findCourseByXxid(user.getGjtOrg().getId());
	// for (GjtCourse gjtCourse : courseList) {
	// if (courseCodeMap.containsKey(gjtCourse.getKch())) {
	// courseCodeMap.put(gjtCourse.getKch(), gjtCourse.getCourseId());
	// }
	// }
	//
	// Date now = new Date();
	// boolean examNoFlag = examType > 1 ? true : false;
	// GjtExamSubjectNew subject;
	// String courseid;
	// List<GjtExamSubjectNew> insertList = new ArrayList<GjtExamSubjectNew>();
	// Map<String, Object> courseMap = new HashMap<String, Object>();
	// Map<Integer, Integer> errorMap = new LinkedHashMap<Integer, Integer>();
	// Integer indexCount = 1;// 第一行标题行忽略
	// int errorCode = 0;
	// for (Object object : list) {
	// indexCount++;
	// map = (Map<String, Object>) object;
	// errorCode = this.importDataValid(map, examNoFlag);
	// if (errorCode == 0) {
	// kch = map.get("kch");
	// courseid = courseCodeMap.get((String) kch);//根据课程号获取课程id
	// if(!"0".equals(courseid)) {
	// if (existMap.containsKey(courseid) &&
	// existMap.get(courseid).getIsDeleted() == 0
	// && existMap.get(courseid).getStatus() != WebConstants.EXAM_SUBJECT_NEW) {
	// // 数据库已有课程记录, 且该记录未删除, 且处于未同步的数据才能修改, 否则不能修改
	// errorMap.put(indexCount, 3);// 该课程当前考试类型已设置考试科目, 不能重复设置
	// } else {
	// if (courseMap.containsKey((String) kch)) {
	// errorMap.put(indexCount, 3);// 该课程当前考试类型已设置考试科目, 不能重复设置
	// } else {
	// courseMap.put((String) kch, indexCount);
	// subject = new GjtExamSubjectNew();
	// if (existMap.containsKey(courseid)) {// 旧数据修改
	// subject.setSubjectId(existMap.get(courseid).getSubjectId());
	// subject.setSubjectCode(existMap.get(courseid).getSubjectCode());
	// } else {// 新增
	// subject.setSubjectId(UUIDUtils.random());
	// subject.setSubjectCode(this.getSubjectCode(examType));
	// subject.setCreatedBy(user.getId());
	// subject.setCreatedDt(now);
	// }
	//
	// subject.setXxId(user.getGjtOrg().getId());
	// subject.setUpdatedBy(user.getId());
	// subject.setUpdatedDt(now);
	// subject.setIsDeleted(0);
	//
	// subject.setCourseId(courseid);
	// subject.setName((String) map.get("subjectName"));
	// subject.setType(examType);
	// if (examNoFlag) {// 只有笔试或机考才设置试卷号
	// subject.setExamNo((String) map.get("examNo"));
	// }
	// subject.setMemo((String) map.get("memo"));
	//
	// insertList.add(subject);
	// }
	// }
	// } else {
	// errorMap.put(indexCount, 1);
	// }
	// } else {
	// errorMap.put(indexCount, errorCode);
	// }
	// }
	//
	// StringBuilder sbuilder = new StringBuilder();
	// int rs = this.insertBatch(insertList);
	// if (rs > 0) {
	// sbuilder.append("成功导入" + rs + "条数据.");
	// }
	//
	// // write error date to resultWb
	// if (errorMap.size() > 0) {
	// sbuilder.append(" 失败" + errorMap.size() + "条. 错误行数如下: ");
	// row = sheet.createRow(rowIndex++);
	// cell = row.createCell(cellIndex++);
	// cell.setCellValue(sbuilder.toString());
	//
	// Set<Entry<Integer, Integer>> set = errorMap.entrySet();
	// Iterator<Entry<Integer, Integer>> enties = set.iterator();
	// Entry<Integer, Integer> entity;
	// while (enties.hasNext()) {
	// entity = enties.next();
	// cellIndex = 0;
	// row = sheet.createRow(rowIndex++);
	// cell = row.createCell(cellIndex++);
	// cell.setCellValue("第" + entity.getKey() + "行");
	// cell = row.createCell(cellIndex++);
	// cell.setCellValue("原因:"+this.getErrStr(entity.getValue()));
	// }
	// } else {
	// row = sheet.createRow(rowIndex++);
	// cell = row.createCell(cellIndex++);
	// cell.setCellValue(sbuilder.toString());
	// }
	//
	// return resultWb;
	// }
	// 2016-10-25 数据结构改变...
	@Override
	public HSSFWorkbook importGjtExamSubjectNew(String path, int examType, GjtUserAccount user) {
		HSSFWorkbook resultWb = null;// 直接将结果放在excel 返回
		resultWb = new HSSFWorkbook();
		HSSFSheet sheet = resultWb.createSheet();
		HSSFRow row;
		HSSFCell cell;
		int rowIndex = 0;
		int cellIndex = 0;

		String[] titles = { "kch", "courseName", "subjectName", "examNo", "memo" };
		File file = new File(path);
		ExcelReader reader = new ExcelReader(file, titles, 1);
		List<Object> list = reader.readAsList();
		Map<String, Object> map;
		Map<String, String> courseCodeMap = new HashMap<String, String>();
		Object kch = null;
		for (Object object : list) {
			map = (Map<String, Object>) object;
			kch = map.get("kch");
			if (null != kch && !courseCodeMap.containsKey((String) kch)) {
				courseCodeMap.put((String) kch, "0");
			}
		}
		List<String> examNoList = gjtExamSubjectNewDao.existExamNoList(user.getGjtOrg().getId(), examType);
		List<GjtCourse> courseList = gjtCourseDao.findByXxIdAndIsDeleted(user.getGjtOrg().getId(), "N");
		for (GjtCourse gjtCourse : courseList) {
			if (courseCodeMap.containsKey(gjtCourse.getKch())) {
				courseCodeMap.put(gjtCourse.getKch(), gjtCourse.getCourseId());
			}
		}

		Date now = new Date();
		GjtExamSubjectNew subject;
		String courseid;
		List<GjtExamSubjectNew> insertList = new ArrayList<GjtExamSubjectNew>();
		Map<Integer, Integer> errorMap = new LinkedHashMap<Integer, Integer>();
		Integer indexCount = 1;// 第一行标题行忽略
		int errorCode = 0;
		for (Object object : list) {
			indexCount++;
			map = (Map<String, Object>) object;
			errorCode = this.importDataValid(map);
			if (errorCode == 0) {
				kch = map.get("kch");
				courseid = courseCodeMap.get((String) kch);// 根据课程号获取课程id
				if (!"0".equals(courseid)) {
					String examNo = map.get("examNo").toString();
					if (examNoList.contains(examNo)) {
						errorMap.put(indexCount, 3);// 试卷号不能重复
					} else {
						examNoList.add(examNo);
						subject = new GjtExamSubjectNew();
						// 新增
						subject.setSubjectId(UUIDUtils.random());
						subject.setSubjectCode(this.getSubjectCode(examType));
						subject.setCreatedBy(user.getId());
						subject.setCreatedDt(now);
						
						if (examType != WebConstants.EXAM_WK_TYPE 
								&& examType != WebConstants.EXAM_XK_TYPE
								&& examType != WebConstants.EXAM_WKS_TYPE) {
							subject.setStatus(3);
						} else {
							subject.setStatus(1);
						}

						//subject.setKch((String) kch);
						subject.setXxId(user.getGjtOrg().getId());
						subject.setUpdatedBy(user.getId());
						subject.setUpdatedDt(now);
						subject.setIsDeleted(0);
//						subject.setIsDeleted("N");

						subject.setCourseId(courseid);
						subject.setName((String) map.get("subjectName"));
						subject.setType(examType);
						subject.setExamNo(examNo);
						subject.setMemo((String) map.get("memo"));

						insertList.add(subject);
					}
				} else {
					errorMap.put(indexCount, 1);
				}
			} else {
				errorMap.put(indexCount, errorCode);
			}
		}

		StringBuilder sbuilder = new StringBuilder();
		int rs = this.insertBatch(insertList);
		if (rs > 0) {
			sbuilder.append("成功导入" + rs + "条数据.");
		}

		// write error date to resultWb
		if (errorMap.size() > 0) {
			sbuilder.append(" 失败" + errorMap.size() + "条. 错误行数如下: ");
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

	/**
	 * 导入数据校验: 1. 课程号错误 2. 课程名称不能为空 3. 该课程当前考试类型已设置考试科目, 且不能更改状态(该科目已安排考试或已发布.)
	 * 4. 试卷号不能为空
	 * 
	 * @param data
	 * @param examNoFlag
	 *            网考无试卷号, 笔试机考必须有试卷号
	 * @return
	 */
	private int importDataValid(Map<String, Object> data) {
		if (StringUtils.isBlank(String.valueOf(data.get("kch")))) {
			return 1;
		}
		if (StringUtils.isBlank(String.valueOf(data.get("subjectName")))) {
			return 2;
		}
		if (StringUtils.isBlank(String.valueOf(data.get("examNo")))) {
			return 4;
		}
		return 0;
	}

	private String getErrStr(int type) {
		switch (type) {
		case 1:
			return "课程号错误";
		case 2:
			return "课程名称不能为空";
		case 3:
			//return "该课程当前考试类型已设置考试科目, 且不能更改状态(该科目已安排考试或已发布.)";
			return "试卷号不能重复";
		case 4:
			//return "如果是笔试或者机考, 试卷号不能为空";
			return "试卷号不能为空";
		default:
			return "未知错误,错误代码: " + type;
		}
	}

	@Override
	public int insertBatch(List<GjtExamSubjectNew> list) {
		int rs = 0;
		try {
			list = gjtExamSubjectNewDao.save(list);
			rs = list.size();
		} catch (Exception e) {
			log.error("plans insert batch error.");
			log.error(e.getMessage(), e);
		}
		return rs;
	}

	@Override
	public Map<String, String> plansCountBySubject(List<String> subjectids) {
		return gjtExamSubjectNewDao.plansCountBySubject(subjectids);
	}

	@Override
	public List<GjtCourse> findCourseByXxid(String xxid, String ksfs) {
		return gjtExamSubjectNewDao.findCourseByXxid(xxid, ksfs);
	}

	@Override
	public List<Map<String, String>> findTeachPlanByXxid(String xxid, String ksfs) {
		return gjtExamSubjectNewDao.findTeachPlanByXxid(xxid, ksfs);
	}

	@Override
	public List<Map<String, String>> noSubjectPlanList(String xxid, String ksfs) {
		return gjtExamSubjectNewDao.noSubjectPlanList(xxid, ksfs);
	}

	@Override
	public HSSFWorkbook exportNoSubjectPlanList(List<Map<String, String>> list) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row;
		HSSFCell cell;
		int rowIndex = 0;
		int cellIndex = 0;

		// 说明行
		// row = sheet.createRow(rowIndex++);
		// cell = row.createCell(0);
		// cell.setCellValue("导入需知: 1.x 2.如果是笔试、机考必须设置试卷号");
		//
		// HSSFCellStyle style = workbook.createCellStyle();
		// style.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
		// CellRangeAddress cellRangeAddress =new CellRangeAddress(0, 0, 0, 5);
		// sheet.addMergedRegion(cellRangeAddress);
		// cellIndex = 0;

		// 标题行
		row = sheet.createRow(rowIndex++);
		cell = row.createCell(cellIndex++);
		cell.setCellValue("课程号(必填, 请勿修改)");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("课程名称(必填, 请勿修改)");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("科目名称(必填, 可随意修改)");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("试卷号(如果是笔试、机考必须设置试卷号)");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("标签说明");

		for (Map<String, String> planMap : list) {
			cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(planMap.get("COURSE_CODE"));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(planMap.get("KCMC"));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(planMap.get("KCMC") + "考试");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("");
		}

		return workbook;
	}

	@Override
	public GjtExamSubjectNew isExamSubjectExist(GjtExamSubjectNew entity) {
		return gjtExamSubjectNewDao.isExamSubjectExist(entity);
	}

	@Override
	public List<GjtExamSubjectNew> findByTypeAndXxIdAndIsDeleted(int type, String xxId, int isDeleted) {
		return gjtExamSubjectNewDao.findByTypeAndXxIdAndIsDeleted(type, xxId, isDeleted);
	}
//	public List<GjtExamSubjectNew> findByTypeAndXxIdAndIsDeleted(int type, String xxId, String isDeleted) {
//		return gjtExamSubjectNewDao.findByTypeAndXxIdAndIsDeleted(type, xxId, isDeleted);
//	}

	@Override
	public Map<String, String> queryTeachPlanBySubject(List<String> ids) {
		return gjtExamSubjectNewDao.queryTeachPlanBySubject(ids);
	}

	/**
	 * 导出未创建考试科目的课程
	 * @return
	 */
	public String expCourseSubject(Map formMap, HttpServletRequest request, HttpServletResponse response) {
		String fileName = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/temp/未创建考试科目的课程目录表"+new Date().getTime()+".xls";
		try {
			List<GjtCourse> courseList = gjtExamSubjectNewDao.noSubjectCourseList(ObjectUtils.toString(formMap.get("XX_ID")));
			if (EmptyUtils.isNotEmpty(courseList)) {
				Map beans = new HashMap();
			    beans.put("dataList", courseList);
			    XLSTransformer transformer = new XLSTransformer();
	            transformer.transformXLS(GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/exam/未创建考试科目的课程目录表.xls", beans, fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	/**
	 * 导入考试科目
	 * @return
	 */
	public Map importCourseSubject(String filePaths, Map formMap, HttpServletRequest request, HttpServletResponse response) {
		Map resultMap = new HashMap();
		List rightList = new ArrayList();
		List errorList = new ArrayList();
		String errorMessage = "";
		try {
			String userId = ObjectUtils.toString(formMap.get("USER_ID"));
			int examType = Integer.parseInt(ObjectUtils.toString(formMap.get("EXAM_TYPE")));
			String xxId = ObjectUtils.toString(formMap.get("XX_ID"));
			String[] titles = { "kch", "courseName", "subjectName", "examNo", "memo" };
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
					tempMap.put("examType", examType);
					String kch = ObjectUtils.toString(tempMap.get("kch")).trim();
					String courseName = ObjectUtils.toString(tempMap.get("courseName")).trim();
					String subjectName = ObjectUtils.toString(tempMap.get("subjectName")).trim();
					String examNo = ObjectUtils.toString(tempMap.get("examNo")).trim();
					String memo = ObjectUtils.toString(tempMap.get("memo")).trim();
					String courseid = "";
					
					if (EmptyUtils.isNotEmpty(kch) || EmptyUtils.isNotEmpty(courseName)) {
						if (EmptyUtils.isEmpty(kch)) {
							errorstr += "课程号不能为空，";
	                        errorNum++;
						}
						
						if (EmptyUtils.isEmpty(courseName)) {
							errorstr += "课程名称不能为空，";
	                        errorNum++;
						}
						
						if (EmptyUtils.isEmpty(subjectName)) {
							errorstr += "科目名称不能为空，";
	                        errorNum++;
						}
						
						if (EmptyUtils.isEmpty(examNo)) {
							errorstr += "试卷号不能为空，";
	                        errorNum++;
						}
						
						if (memo.length()>200) {
							errorstr += "标签说明不能大于100字，";
	                        errorNum++;
						}
						
						List subList = gjtExamSubjectNewDao.getCourseExamNo(tempMap);
						if (EmptyUtils.isNotEmpty(subList)) {
							Map subMap = (Map)subList.get(0);
							courseid = ObjectUtils.toString(subMap.get("COURSE_ID"));
							int subject_count = Integer.parseInt(ObjectUtils.toString(subMap.get("SUBJECT_COUNT")));
							String subject_code = ObjectUtils.toString(subMap.get("SUBJECT_CODE"));
							if (subject_count>0) {
								errorstr += "该课程已经创建考试科目，";
		                        errorNum++;
							} else if (EmptyUtils.isNotEmpty(subject_code)) {
								errorstr += "试卷号已经关联其它科目，";
		                        errorNum++;
							}
						} else {
							errorstr += "该课程不存在，";
	                        errorNum++;
						}
						
						if (EmptyUtils.isEmpty(errorstr)) {
							Date now = new Date();
							List<GjtExamSubjectNew> insertList = new ArrayList<GjtExamSubjectNew>();
							GjtExamSubjectNew subject = new GjtExamSubjectNew();
							// 新增
							subject.setSubjectId(UUIDUtils.random());
							subject.setSubjectCode(this.getSubjectCode(examType));
							subject.setCreatedBy(userId);
							subject.setCreatedDt(now);
							
							if (examType != WebConstants.EXAM_WK_TYPE 
									&& examType != WebConstants.EXAM_XK_TYPE
									&& examType != WebConstants.EXAM_WKS_TYPE) {
								subject.setStatus(3);
							} else {
								subject.setStatus(1);
							}
							subject.setXxId(xxId);
							subject.setUpdatedBy(userId);
							subject.setUpdatedDt(now);
							subject.setIsDeleted(0);
							subject.setCourseId(courseid);
							subject.setName(subjectName);
							subject.setType(examType);
							subject.setExamNo(examNo);
							subject.setMemo(memo);
							subject.setKch(kch);
							insertList.add(subject);
							int rs = this.insertBatch(insertList);
							if (rs>0) {
								rightList.add(tempMap);
							} else {
								tempMap.put("errorstr", "导入异常");
								errorList.add(tempMap);
							}
						} else {
							tempMap.put("errorstr", errorstr);
							errorList.add(tempMap);
						}
					}
					
				}
			}
			
			String timeStr = ObjectUtils.toString(new Date().getTime());
			
			// 导出成功记录
			String tempPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/exam/未创建考试科目的课程目录成功表.xls";
			String downPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/temp/未创建考试科目的课程目录成功表"+timeStr+".xls";
			Map beans = new HashMap();
		    beans.put("dataList", rightList);
		    XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS(tempPath, beans, downPath);
            resultMap.put("RIGHT_DOWNPATH", downPath);
			
            // 导出失败记录
            tempPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/exam/未创建考试科目的课程目录失败表.xls";
			downPath = GjtExamSubjectNewServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/temp/未创建考试科目的课程目录失败表"+timeStr+".xls";
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
		resultMap.put("allNum", errorList.size()+rightList.size());
		return resultMap;
	}

	
}
