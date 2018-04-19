/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.student;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.JoinType;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.Objects;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.ouchgzee.headTeacher.dao.account.GjtUserAccountDao;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.dao.signup.GjtSignupDao;
import com.ouchgzee.headTeacher.dao.signup.GjtSignupDataDao;
import com.ouchgzee.headTeacher.dao.student.GjtClassInfoDao;
import com.ouchgzee.headTeacher.dao.student.GjtGradeDao;
import com.ouchgzee.headTeacher.dao.student.GjtSpecialtyDao;
import com.ouchgzee.headTeacher.dao.student.GjtStudentEnteringSchoolDao;
import com.ouchgzee.headTeacher.dao.student.GjtStudentInfoDao;
import com.ouchgzee.headTeacher.daoImpl.BzrGjtRecResultDaoImpl;
import com.ouchgzee.headTeacher.daoImpl.StudentInfoDaoImpl;
import com.ouchgzee.headTeacher.dto.CountLoginDto;
import com.ouchgzee.headTeacher.dto.StudentClockingInDto;
import com.ouchgzee.headTeacher.dto.StudentPaymentDto;
import com.ouchgzee.headTeacher.dto.StudentSignupInfoDto;
import com.ouchgzee.headTeacher.dto.StudentStateDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtGrade;
import com.ouchgzee.headTeacher.pojo.BzrGjtSignup;
import com.ouchgzee.headTeacher.pojo.BzrGjtSpecialty;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentEnteringSchool;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.pojo.BzrTblPriLoginLog;
import com.ouchgzee.headTeacher.pojo.status.SignupAuditStateEnum;
import com.ouchgzee.headTeacher.service.BzrCacheService;
import com.ouchgzee.headTeacher.service.remote.BillRemoteService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;

import sun.misc.BASE64Encoder;

/**
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年4月26日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated
@Service("bzrGjtStudentServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtStudentServiceImpl extends BaseServiceImpl<BzrGjtStudentInfo> implements BzrGjtStudentService {

	private static Logger log = LoggerFactory.getLogger(GjtStudentServiceImpl.class);

	@Autowired
	private BillRemoteService billRemoteService;

	@Autowired
	private BzrCacheService cacheService;

	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;

	@Autowired
	private StudentInfoDaoImpl studentInfoDao;

	@Autowired
	private GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	private GjtClassInfoDao gjtClassInfoDao;

	@Autowired
	private BzrGjtRecResultDaoImpl recResultDao;

	@Autowired
	private GjtGradeDao gjtGradeDao;

	@Autowired
	private GjtSpecialtyDao gjtSpecialtyDao;

	@Autowired
	private GjtSignupDao gjtSignupDao;

	@Autowired
	private GjtSignupDataDao gjtSignupDataDao;

	@Autowired
	private GjtStudentEnteringSchoolDao gjtStudentEnteringSchoolDao;

	@Autowired
	private CommonDao commonDao;

	@Override
	protected BaseDao<BzrGjtStudentInfo, String> getBaseDao() {
		return gjtStudentInfoDao;
	}

	@Override
	public String queryXmById(String studentId) {
		return gjtStudentInfoDao.findXmById(studentId);
	}

	public boolean update(BzrGjtStudentInfo studentInfo) {
		if (StringUtils.isNoneBlank(studentInfo.getStudentId())) {
			studentInfo.setUpdatedDt(new Date());
			BzrGjtStudentInfo info = gjtStudentInfoDao.save(studentInfo);
			return info != null;
		}
		return false;
	}

	@Override
	public boolean updateStudentResetPwd(String studentId, String updateBy) {
		BzrGjtStudentInfo gjtStudentInfo = gjtStudentInfoDao.findOne(studentId);
		if (gjtStudentInfo != null) {
			gjtUserAccountDao.updatePwd(gjtStudentInfo.getGjtUserAccount().getId(),
					Md5Util.encode(Constants.STUDENT_ACCOUNT_PWD_DEFAULT), Constants.STUDENT_ACCOUNT_PWD_DEFAULT,
					updateBy, new Date());
			return true;
		}
		return false;
	}

	@Override
	public boolean updateStudentResetPwdNew(String studentId, String updateBy) {

		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GSI.STUDENT_ID,");
		sql.append("  	GSI.XH,");
		sql.append("  	GSI.XM,");
		sql.append("  	GSI.SFZH,");
		sql.append("  	GSI.SJH,");
		sql.append("  	GO.CODE");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN GJT_ORG GO ON");
		sql.append("  	GSI.XX_ID = GO.ID");
		sql.append("  	AND GO.IS_DELETED = 'N'");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID ");

		params.put("STUDENT_ID", studentId);

		Map map = commonDao.queryObjectToMapNative(sql.toString(), params);

		String id_card = null;
		String account = null;
		String password = "888888";
		String base64 = null;
		String base64Code = null;
		String code = null;
		String url = null;
		BASE64Encoder encoder = new BASE64Encoder();
		if (EmptyUtils.isNotEmpty(map)) {
			id_card = ObjectUtils.toString(map.get("SFZH"));
			account = ObjectUtils.toString(map.get("XH"));
			base64 = ObjectUtils.toString(map.get("SFZH")) + "|" + (System.currentTimeMillis()) / 1000 + "|"
					+ Md5Util.encodeLower(ObjectUtils.toString(map.get("SFZH")) + "oucnet");
			base64Code = encoder.encode(base64.getBytes());
			code = base64Code;

			Map result = new HashMap();
			url = AppConfig.getProperty("resetPwd.url");
			result.put("id_card", id_card);
			result.put("account", account);
			result.put("password", password);
			result.put("code", code);

			log.info("传入参数：" + result.toString());
			String rspXML = HttpClientUtils.doHttpPost(url, result, 3000, "UTF-8");
			log.info("输出结果：" + rspXML.toString());
		}

		BzrGjtStudentInfo gjtStudentInfo = gjtStudentInfoDao.findOne(studentId);
		if (gjtStudentInfo != null) {
			gjtUserAccountDao.updatePwd(gjtStudentInfo.getGjtUserAccount().getId(),
					Md5Util.encode(Constants.STUDENT_ACCOUNT_PWD_DEFAULT), Constants.STUDENT_ACCOUNT_PWD_DEFAULT,
					updateBy, new Date());
			return true;
		}
		return false;
	}

	@Override
	public boolean updateStudentEnteringSchool(BzrGjtStudentEnteringSchool info, String bzrId) {
		int resultNum = gjtStudentInfoDao.enteringSchool(info.getStudentId(), bzrId);
		if (resultNum == 1) {
			info.setCreatedBy(bzrId);
			gjtStudentEnteringSchoolDao.save(info);
			return true;
		}
		return false;
	}

	@Override
	public Page<BzrGjtStudentInfo> queryStudentInfoByClassIdPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<BzrGjtStudentInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.classId", classId, true));
		spec.addAll(Restrictions.parse(searchParams));

		Page<BzrGjtStudentInfo> result = gjtStudentInfoDao.findAll(spec, pageRequest);
		return result;
	}

	@Override
	public List<BzrGjtStudentInfo> queryStudentInfoByClassId(String classId, Map<String, Object> searchParams,
			Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<BzrGjtStudentInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.classId", classId, true));
		spec.addAll(Restrictions.parse(searchParams));

		List<BzrGjtStudentInfo> result = gjtStudentInfoDao.findAll(spec, sort);
		return result;
	}

	@Override
	public Page<BzrGjtStudentInfo> queryStudentInfoSpecialtyByClassIdPage(String classId,
			Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<BzrGjtStudentInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtUserAccount.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtSpecialty.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtSignup.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.classId", classId, true));

		String auditState = (String) searchParams.get("EQ_signupAuditState");
		if (StringUtils.isNotEmpty(auditState)) {
			if ((SignupAuditStateEnum.待审核.getValue() + "").equals(auditState)) {
				List<String> auditStateList = new ArrayList<String>(3);
				auditStateList.add(SignupAuditStateEnum.重提交.getValue() + "");
				auditStateList.add(SignupAuditStateEnum.待审核.getValue() + "");
				auditStateList.add(SignupAuditStateEnum.未提交.getValue() + "");
				spec.add(Restrictions.in("gjtSignup.auditState", auditStateList, true));
			} else {
				spec.add(Restrictions.eq("gjtSignup.auditState", auditState, true));
			}
			searchParams.remove("EQ_signupAuditState");
		}
		spec.addAll(Restrictions.parse(searchParams));
		searchParams.put("EQ_signupAuditState", auditState);

		Page<BzrGjtStudentInfo> result = gjtStudentInfoDao.findAll(spec, pageRequest);
		return result;
	}

	@Override
	public HSSFWorkbook exportStudentInfoSpecialtyToExcel(String classId, Map<String, Object> searchParams, Sort sort) {
		List<Map> list = studentInfoDao.findStudentSignupInfoBy(searchParams, sort);

		BzrGjtClassInfo classInfo = gjtClassInfoDao.findOne(classId);
		Map<String, String> sexMap = cacheService.getCachedDictionaryMap(BzrCacheService.DictionaryKey.SEX);
		Map<String, String> pyccMap = cacheService.getCachedDictionaryMap(BzrCacheService.DictionaryKey.TRAININGLEVEL);
		try {
			HSSFWorkbook book = null;
			InputStream in = this.getClass().getResourceAsStream("/template/Export_StudentInfo.xls");
			book = new HSSFWorkbook(in);
			HSSFSheet sheet = book.getSheetAt(0);
			HSSFRow row = null;

			int rowIdx = 1, idx = 1;
			int colIdx = 0;

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			for (Iterator<Map> iter = list.iterator(); iter.hasNext();) {
				Map info = iter.next();
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				row.createCell(colIdx++).setCellValue(idx++);
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("XH")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("XM")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(sexMap.get((String) info.get("XBM"))));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("SFZH")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("GRADE_NAME")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("RULE_CODE")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("ZYMC")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(pyccMap.get((String) info.get("PYCC"))));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("ROLL_REGISTER_DT")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("LXDH")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("SJH")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("SC_CO")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("DZXX")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("RECEIVER")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("MOBILE")));
				String address = "";
				String provinceCode = (String) info.get("PROVINCE_CODE");
				if (provinceCode != null) {
					address += Objects.toString(
							cacheService.getCachedDictionaryName(BzrCacheService.DictionaryKey.PROVINCE, provinceCode),
							"");
					String cityCode = (String) info.get("CITY_CODE");
					if (cityCode != null) {
						address += Objects.toString(cacheService.getCachedDictionaryName(
								BzrCacheService.DictionaryKey.CITY.replace("${Province}", provinceCode), cityCode), "");
						address += Objects.toString(cacheService.getCachedDictionaryName(
								BzrCacheService.DictionaryKey.AREA.replace("${Province}", provinceCode)
										.replace("${City}", cityCode),
								(String) info.get("AREA_CODE")), "");
					}
				}
				address += " " + Objects.toString(info.get("ADDRESS"), "");
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(address));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(classInfo.getBjmc()));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("HEADTEACHER")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("XXMC")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("ORG_NAME")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(
						Constants.BOOLEAN_YES.equals((String) info.get("SYNC_STATUS")) ? "已同步" : "未同步"));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(
						Constants.BOOLEAN_YES.equals((String) info.get("EESYNC")) ? "已同步" : "未同步"));
			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Page<StudentStateDto> queryStudentStateByClassIdPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "t.createdDt"));
		}

		Page<StudentStateDto> result = studentInfoDao.findStudentStateByPage(classId, searchParams, pageRequest);

		for (Iterator<StudentStateDto> iter = result.iterator(); iter.hasNext();) {
			StudentStateDto info = iter.next();

			Map tempMap = this.getAmount(info.getSfzh());
			if (tempMap.size() > 0) {
				double orderTotalAmt = NumberUtils.toDouble(tempMap.get("ORDER_TOTAL_AMT") + "");
				double orderAmt = NumberUtils.toDouble(tempMap.get("ORDER_AMT") + "");
				info.setGkxlPaymentTpye((String) tempMap.get("GKXL_PAYMENT_TPYE"));
				info.setFeeStatus(orderTotalAmt == orderAmt ? 1 : 0);
			}
		}
		return result;
	}

	@Override
	public long countNotLearningStudentByClassId(String classId) {
		return studentInfoDao.countNotLearningStudentByClassId(classId);
	}

	@Override
	public long countNotPerfectStudentByClassId(String classId) {
		return studentInfoDao.countNotPerfectStudentByClassId(classId);
	}

	@Override
	public long countGraduateStudentByClassId(String classId) {
		Criteria<BzrGjtStudentInfo> spec = new Criteria();
		// 设置左连接
		spec.setJoinType("gjtGraduationStu", JoinType.LEFT);

		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtClassStudentList.gjtClassInfo.classId", classId, true));
		spec.add(Restrictions.or(Restrictions.isNull("gjtGraduationStu.receiveStatus"),
				Restrictions.eq("gjtGraduationStu.receiveStatus", "0", true)));

		List<BzrGjtStudentInfo> result = gjtStudentInfoDao.findAll(spec, new Sort(Sort.Direction.DESC, "createdDt"));

		int count = 0;
		for (Iterator<BzrGjtStudentInfo> iter = result.iterator(); iter.hasNext();) {
			BzrGjtStudentInfo info = iter.next();
			// 学分
			Map minAndSum = studentInfoDao.getMinAndSum(info.getXh(), info.getGjtSpecialty().getSpecialtyId());
			int zdxf = minAndSum.get("ZDXF") != null ? ((BigDecimal) minAndSum.get("ZDXF")).intValue() : 0;
			int yxxf = minAndSum.get("YXXF") != null ? ((BigDecimal) minAndSum.get("YXXF")).intValue() : 0;
			if (yxxf >= zdxf) {
				count++;
			}
		}
		return count;
	}

	@Override
	public HSSFWorkbook exportStudentStateToExcel(String classId, Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "t.createdDt");
		}
		List<StudentStateDto> result = studentInfoDao.findAllStudentState(classId, searchParams, sort);

		Map<String, String> xjztMap = cacheService
				.getCachedDictionaryMap(BzrCacheService.DictionaryKey.STUDENTNUMBERSTATUS);
		try {
			HSSFWorkbook book = null;
			book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			HSSFRow row = sheet.createRow(0);

			int rowIdx = 1, idx = 1;
			int colIdx = 0;

			row.createCell(colIdx++).setCellValue("序号");
			row.createCell(colIdx++).setCellValue("学员姓名");
			row.createCell(colIdx++).setCellValue("报读时间");
			row.createCell(colIdx++).setCellValue("报读产品");
			row.createCell(colIdx++).setCellValue("年级");
			row.createCell(colIdx++).setCellValue("学习状态");
			row.createCell(colIdx++).setCellValue("资料完善");
			row.createCell(colIdx++).setCellValue("缴费状态");
			row.createCell(colIdx++).setCellValue("学籍状态");
			row.createCell(colIdx++).setCellValue("毕业状态");
			row.createCell(colIdx++).setCellValue("学位证书");

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			for (Iterator<StudentStateDto> iter = result.iterator(); iter.hasNext();) {
				StudentStateDto info = iter.next();

				Map tempMap = this.getAmount(info.getSfzh());
				if (tempMap.size() > 0) {
					double orderTotalAmt = NumberUtils.toDouble(tempMap.get("ORDER_TOTAL_AMT") + "");
					double orderAmt = NumberUtils.toDouble(tempMap.get("ORDER_AMT") + "");
					info.setGkxlPaymentTpye((String) tempMap.get("GKXL_PAYMENT_TPYE"));
					info.setFeeStatus(orderTotalAmt == orderAmt ? 1 : 0);
				}
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				row.createCell(colIdx++).setCellValue(idx++);
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(info.getXm()));
				row.createCell(colIdx++).setCellValue(
						info.getSignupDt() != null ? DateFormatUtils.ISO_DATE_FORMAT.format(info.getSignupDt()) : "");
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(info.getZymc()));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(info.getGradeName()));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString("0".equals(info.getLearningState()) ? "未激活" : "已激活"));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString("1".equals(info.getDataState()) ? "已完善" : "待完善"));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString("1".equals(info.getFeeStatus()) ? "已缴费" : "未缴费"));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(xjztMap.get(info.getXjzt())));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(
						("1".equals(info.getReceiveStatus()) || "2".equals(info.getReceiveStatus())) ? "已毕业" : "未毕业"));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(info.getCertificateNum() + ""));
			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Deprecated
	@Override
	public Page<StudentClockingInDto> queryStudentClockingInByClassIdPage(String classId,
			Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "t.created_dt"));
		}
		Page<Map> result = studentInfoDao.findStudentClockingInByPage(classId, searchParams, pageRequest);

		List<StudentClockingInDto> clockList = new ArrayList();
		for (Iterator<Map> iter = result.iterator(); iter.hasNext();) {
			Map info = iter.next();

			StudentClockingInDto clockDto = new StudentClockingInDto();
			clockDto.setStudentId((String) info.get("STUDENT_ID"));
			clockDto.setXm((String) info.get("XM"));
			clockDto.setXh(ObjectUtils.toString(info.get("XH")));
			clockDto.setSjh(ObjectUtils.toString(info.get("SJH")));
			clockDto.setPyccName(ObjectUtils.toString(info.get("PYCC_NAME")));
			clockDto.setYearName(ObjectUtils.toString(info.get("NAME")));
			clockDto.setGradeName(ObjectUtils.toString(info.get("GRADE_NAME")));
			clockDto.setZymc(ObjectUtils.toString(info.get("ZYMC")));
			clockDto.setBjmc(ObjectUtils.toString(info.get("BJMC")));
			clockDto.setIsOnline(ObjectUtils.toString(info.get("IS_ONLINE")));
			clockDto.setLoginType(ObjectUtils.toString(info.get("LOGIN_TYPE")));
			clockDto.setFirstLogin((Date) info.get("FIRSTLOGIN"));
			clockDto.setLastLogin((Date) info.get("LASTLOGIN"));
			clockDto.setCountLogin((BigDecimal) info.get("COUNTLOGIN"));
			clockDto.setTotalMinute((BigDecimal) info.get("TOTALMINUTE"));
			clockDto.setNoLoginDays((BigDecimal) info.get("NOLOGINDAYS"));
			clockDto.setClassId(ObjectUtils.toString(info.get("CLASS_ID")));
			clockDto.setXxId(ObjectUtils.toString(info.get("XX_ID")));

			clockList.add(clockDto);
		}
		return new PageImpl(clockList, pageRequest, result.getTotalElements());
	}

	/**
	 * 统计学员的考勤
	 *
	 * @param username
	 * @return
	 */
	@Override
	public StudentClockingInDto countStudentClockingInSituation(String username) {
		return null;
	}

	@Override
	public StudentClockingInDto countStudentClockingInSituation(String username, String classId) {
		Map info = studentInfoDao.countStudentClockingInSituation(username, classId);

		StudentClockingInDto clockDto = new StudentClockingInDto();
		if (info != null) {
			clockDto.setStudentId((String) info.get("STUDENT_ID"));
			clockDto.setXm((String) info.get("XM"));
			clockDto.setXh(ObjectUtils.toString(info.get("XH")));
			clockDto.setSjh(ObjectUtils.toString(info.get("SJH")));
			clockDto.setPyccName(ObjectUtils.toString(info.get("PYCC_NAME")));
			clockDto.setYearName(ObjectUtils.toString(info.get("NAME")));
			clockDto.setGradeName(ObjectUtils.toString(info.get("GRADE_NAME")));
			clockDto.setZymc(ObjectUtils.toString(info.get("ZYMC")));
			clockDto.setBjmc(ObjectUtils.toString(info.get("BJMC")));
			clockDto.setIsOnline(ObjectUtils.toString(info.get("IS_ONLINE")));
			clockDto.setLoginType(ObjectUtils.toString(info.get("LOGIN_TYPE")));
			clockDto.setFirstLogin((Date) info.get("FIRSTLOGIN"));
			clockDto.setLastLogin((Date) info.get("LASTLOGIN"));
			clockDto.setCountLogin((BigDecimal) info.get("COUNTLOGIN"));
			clockDto.setTotalMinute((BigDecimal) info.get("TOTALMINUTE"));
			clockDto.setNoLoginDays((BigDecimal) info.get("NOLOGINDAYS"));
		}
		return clockDto;
	}

	@Override
	public Map countStudentClockingInSituationByClass(String classId, Map<String, Object> queryParams) {
		return studentInfoDao.countClockInSituationByClass(classId, queryParams);
	}

	@Override
	public Map countStudySituationByClass(String classId, Map<String, Object> queryParams) {
		return studentInfoDao.countStudySituationByClass(classId, queryParams);
	}

	@Override
	public Map countClockInSituationByCourseClass(String classId, String courseId) {
		return studentInfoDao.countClockInSituationByCourseClass(classId, courseId);
	}

	@Override
	public Map countLearnSituationByCourseClass(String classId, String courseId, Map<String, Object> queryParams) {
		return studentInfoDao.countLearnSituationByCourseClass(classId, courseId, queryParams);
	}

	@Override
	public List<CountLoginDto> countTwoWeeksClockInSituation(String classId) {
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();

		// 得到上周
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.DATE, -7);
		// 再得到上周的周一
		Date monday = DateUtils.getMondayOfThisWeek(c.getTime());
		List<CountLoginDto> result = studentInfoDao.countClockInSituationAfterDateByClass(classId, monday);

		// 填充空的
		List<CountLoginDto> addList = new ArrayList();
		while (monday.before(now)) {
			String d = DateFormatUtils.ISO_DATE_FORMAT.format(monday);
			boolean flag = false; // 是否存在result里
			for (CountLoginDto info : result) {
				if (info.equals(d)) {
					flag = true;
					break;
				}
			}
			// 不存在则新增，设置登录人数默认值0
			if (!flag) {
				CountLoginDto countLoginDto = new CountLoginDto();
				countLoginDto.setDAYNAME(d);
				countLoginDto.setSTUDENTLOGINNUM(new BigDecimal(0));
				addList.add(countLoginDto);
			}
			// +一天，再循环
			monday.setTime(monday.getTime() + 86400000);
		}
		result.addAll(addList);
		Collections.sort(result, new Comparator<CountLoginDto>() {
			@Override
			public int compare(CountLoginDto o1, CountLoginDto o2) {
				try {
					Date thisDate = org.apache.commons.lang3.time.DateUtils.parseDate(o1.getDayName(), "yyyy-MM-dd");
					Date oDate = org.apache.commons.lang3.time.DateUtils.parseDate(o2.getDayName(), "yyyy-MM-dd");
					return thisDate.after(oDate) ? 1 : thisDate.before(oDate) ? -1 : 0;
				} catch (ParseException e) {

				}
				return 0;
			}
		});
		return result;
	}

	@Override
	public List<CountLoginDto> countThisYearClockInSituation(String classId) {
		// 得到当年第一天
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();

		// 得到当年第一天
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Date monday = c.getTime();
		List<CountLoginDto> result = studentInfoDao.countClockInSituationAfterDateByClass(classId, monday);

		// 填充空的
		List<CountLoginDto> addList = new ArrayList();
		while (monday.before(now)) {
			String d = DateFormatUtils.ISO_DATE_FORMAT.format(monday);
			boolean flag = false; // 是否存在result里
			for (CountLoginDto info : result) {
				if (info.equals(d)) {
					flag = true;
					break;
				}
			}
			// 不存在则新增，设置登录人数默认值0
			if (!flag) {
				CountLoginDto countLoginDto = new CountLoginDto();
				countLoginDto.setDAYNAME(d);
				countLoginDto.setSTUDENTLOGINNUM(new BigDecimal(0));
				addList.add(countLoginDto);
			}
			// +一天，再循环
			monday.setTime(monday.getTime() + 86400000);
		}
		result.addAll(addList);
		Collections.sort(result);
		return result;
	}

	@Override
	public Map countClassStudentExamSituation(String teachClassId, String classId) {
		return recResultDao.countClassStudentExamSituation(teachClassId, classId);
	}

	@Override
	public HSSFWorkbook exportStudentClockingInToExcel(String classId, Map<String, Object> searchParams, Sort sort) {
		// if(sort == null) {
		// sort = new Sort(Sort.Direction.DESC, "t.created_dt");
		// }
		List<Map> result = studentInfoDao.findAllStudentClockingIn(classId, searchParams, sort);

		try {
			HSSFWorkbook book = null;
			book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			HSSFRow row = sheet.createRow(0);

			int rowIdx = 1, idx = 1;
			int colIdx = 0;

			row.createCell(colIdx++).setCellValue("序号");
			row.createCell(colIdx++).setCellValue("学员姓名");
			row.createCell(colIdx++).setCellValue("首次登录");
			row.createCell(colIdx++).setCellValue("登录总次数");
			row.createCell(colIdx++).setCellValue("登录总时长");
			row.createCell(colIdx++).setCellValue("最后一次登录");
			row.createCell(colIdx++).setCellValue("未登录天数");

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行
			// 生成一个样式
			HSSFCellStyle style = book.createCellStyle();
			// 生成一个字体
			HSSFFont font = book.createFont();
			font.setColor(HSSFColor.RED.index);
			// 把字体应用到当前的样式
			style.setFont(font);

			for (Iterator<Map> iter = result.iterator(); iter.hasNext();) {
				Map info = iter.next();
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				row.createCell(colIdx++).setCellValue(idx++);
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.get("XM")));
				row.createCell(colIdx++).setCellValue(info.get("FIRSTLOGIN") != null
						? DateFormatUtils.ISO_DATETIME_FORMAT.format(info.get("FIRSTLOGIN")) : "");
				row.createCell(colIdx++).setCellValue(
						new HSSFRichTextString(info.get("COUNTLOGIN") != null ? info.get("COUNTLOGIN") + "" : null));
				row.createCell(colIdx++).setCellValue(
						new HSSFRichTextString(info.get("TOTALMINUTE") != null ? info.get("TOTALMINUTE") + "" : null));
				row.createCell(colIdx++).setCellValue(info.get("LASTLOGIN") != null
						? DateFormatUtils.ISO_DATETIME_FORMAT.format(info.get("LASTLOGIN")) : "");
				if (info.get("NOLOGINDAYS") != null && NumberUtils.toInt(info.get("NOLOGINDAYS").toString()) >= 7) {
					HSSFCell cell = row.createCell(colIdx++);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(info.get("NOLOGINDAYS") + "");
					cell.setCellValue(text);
				} else {
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString(
							info.get("NOLOGINDAYS") != null ? info.get("NOLOGINDAYS") + "" : null));
				}
			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Page<StudentPaymentDto> queryPaymentSituationByClassIdPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "t.created_dt"));
		}
		Page<Map> result = studentInfoDao.findPaymentSituationByPage(classId, searchParams, pageRequest);

		List<StudentPaymentDto> stuPayDtoList = this.encapsulationPaymentDto(result.getContent());
		return new PageImpl(stuPayDtoList, pageRequest, result.getTotalElements());
	}

	/**
	 * 共用方法 - 封装发票信息
	 * 
	 * @param result
	 * @return
	 */
	private List<StudentPaymentDto> encapsulationPaymentDto(List<Map> result) {
		List<StudentPaymentDto> stuPayDtoList = new ArrayList();
		for (Iterator<Map> iter = result.iterator(); iter.hasNext();) {
			Map info = iter.next();

			StudentPaymentDto stuPayDto = new StudentPaymentDto();
			stuPayDto.setStudentId((String) info.get("STUDENT_ID"));
			stuPayDto.setLoginAccount((String) info.get("LOGIN_ACCOUNT"));
			stuPayDto.setXm((String) info.get("XM"));
			stuPayDto.setSfzh((String) info.get("SFZH"));
			stuPayDto.setSjh((String) info.get("SJH"));
			stuPayDto.setLearningState((String) info.get("VALUE_"));
			Map paymentDetail = this.getAmount((String) info.get("SFZH"));
			int count = 0;
			double money = 0.0;
			boolean cflag = false;
			Map currentRecord = null; // 当前还款期

			Date now = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				List<Map> orders = (List) ((Map) paymentDetail.get("ORDER_PAY_RECORD_LIST")).get("PAY_RECORD");
				for (Map map : orders) {
					if (Constants.BOOLEAN_NO.equals(map.get("PAY_STATUS"))
							&& Constants.BOOLEAN_YES.equals(map.get("PAY_OVERDUE_SIGN"))) {
						count++;
						money += Double.parseDouble((String) map.get("REC_AMT"));
					}
					// 判断当前时间是否应付款时间之前或者等于应付款时间
					if (now.after(df.parse((String) map.get("REC_DATE")))) {
						cflag = false;
					} else {
						cflag = true;
					}
					// 如果是且只取第一个，即是当前还款期
					if (cflag && currentRecord == null) {
						currentRecord = map;
					}
				}
			} catch (Exception e) {
				count = 0;
				money = 0.0;
			}
			stuPayDto.setPaymentDetail(paymentDetail);
			stuPayDto.setOverdueCount(count);
			stuPayDto.setDebtAmount(money);
			if (currentRecord != null) {
				stuPayDto.setCurrentRecAmt(Double.parseDouble((String) currentRecord.get("REC_AMT")));
				try {
					stuPayDto.setCurrentRecDate(df.parse((String) currentRecord.get("REC_DATE")));
				} catch (ParseException e) {

				}
				if (Constants.BOOLEAN_YES.equals(currentRecord.get("PAY_STATUS"))) {
					stuPayDto.setCurrentStatus(1);
				} else if (Constants.BOOLEAN_NO.equals(currentRecord.get("PAY_STATUS"))
						&& Constants.BOOLEAN_NO.equals(currentRecord.get("PAY_OVERDUE_SIGN"))) {
					stuPayDto.setCurrentStatus(2);
				} else {
					stuPayDto.setCurrentStatus(3);
				}
			}

			stuPayDtoList.add(stuPayDto);
		}
		return stuPayDtoList;
	}

	@Override
	public HSSFWorkbook exportPaymentSituationToExcel(String classId, Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "t.created_dt");
		}
		List<Map> result = studentInfoDao.findAllPaymentSituation(classId, searchParams, sort);

		List<StudentPaymentDto> stuPayDtoList = this.encapsulationPaymentDto(result);
		try {
			HSSFWorkbook book = null;
			book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			HSSFRow row = sheet.createRow(0);

			int rowIdx = 1, idx = 1;
			int colIdx = 0;

			row.createCell(colIdx++).setCellValue("序号");
			row.createCell(colIdx++).setCellValue("学员姓名");
			row.createCell(colIdx++).setCellValue("缴费类型");
			row.createCell(colIdx++).setCellValue("优惠政策");
			row.createCell(colIdx++).setCellValue("缴费金额");
			row.createCell(colIdx++).setCellValue("当前应缴金额");
			row.createCell(colIdx++).setCellValue("最迟缴费时间");
			row.createCell(colIdx++).setCellValue("当前缴费状态");
			row.createCell(colIdx++).setCellValue("学习状态");

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			for (StudentPaymentDto info : stuPayDtoList) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				row.createCell(colIdx++).setCellValue(idx++);
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getXm())));
				String gkxlPaymentTpye = null;
				String GKXL_PAYMENT_TPYE = (String) info.getPaymentDetail().get("GKXL_PAYMENT_TPYE");
				if ("A".equals(GKXL_PAYMENT_TPYE))
					gkxlPaymentTpye = "全额缴费";
				else if ("B".equals(GKXL_PAYMENT_TPYE))
					gkxlPaymentTpye = "首年缴费";
				else if ("C".equals(GKXL_PAYMENT_TPYE))
					gkxlPaymentTpye = "分期付款";
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(gkxlPaymentTpye)));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(null)));
				String money = String.format("总金额：%s\n优惠/补贴：0\n应收金额：%s\n实收金额：%s",
						ObjectUtils.toString(info.getPaymentDetail().get("ORDER_TOTAL_AMT")),
						ObjectUtils.toString(info.getPaymentDetail().get("ORDER_TOTAL_AMT")),
						ObjectUtils.toString(info.getPaymentDetail().get("ORDER_AMT")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(money)));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getCurrentRecAmt())));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(info.getCurrentRecDate() != null
						? DateFormatUtils.ISO_DATE_FORMAT.format(info.getCurrentRecDate()) : ""));
				String currentRecStatus = null;
				if (info.getCurrentStatus() != null) {
					if (info.getCurrentStatus() == 1)
						currentRecStatus = "已缴费";
					else if (info.getCurrentStatus() == 2)
						currentRecStatus = "未开始";
					else if (info.getCurrentStatus() == 3)
						currentRecStatus = "欠费";
				}
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(currentRecStatus)));
				String stuLearningState = null;
				if (info.getLearningState() == null || Constants.BOOLEAN_1.equals(info.getLearningState()))
					stuLearningState = "正常学习";
				else if (Constants.BOOLEAN_0.equals(info.getLearningState()))
					stuLearningState = "停止学习";
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString(stuLearningState)));
			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map queryPaymentSituationByStudentId(String studentId) {
		BzrGjtStudentInfo studentInfo = queryById(studentId);
		return this.getAmount(studentInfo.getSfzh());
	}

	@Override
	public boolean updateChangePaymentState(String studentId, int payState) {
		studentInfoDao.changePaymentState(studentId, payState);
		return true;
	}

	@Override
	public boolean signupSibmit(String studentId, String bzrId) {
		gjtSignupDao.signupSibmit(studentId, bzrId);
		return true;
	}

	@Override
	public boolean updateSignupCopyData(String studentId, String fileType, String url) {
		BzrGjtSignup gjtSignup = gjtSignupDao.findByStudentId(studentId);
		gjtSignupDataDao.updateSignupCopyData(studentId, gjtSignup.getSignupId(), gjtSignup.getIdcard(), fileType, url);
		return true;
	}

	@Override
	public boolean updateSignupCopyData(String studentId, Map<String, String> copyData) {
		BzrGjtSignup gjtSignup = gjtSignupDao.findByStudentId(studentId);
		for (Map.Entry<String, String> item : copyData.entrySet()) {
			if (StringUtils.isNotBlank(item.getValue())) {
				String fileType = item.getKey();
				String url = item.getValue();
				gjtSignupDataDao.updateSignupCopyData(studentId, gjtSignup.getSignupId(), gjtSignup.getIdcard(),
						fileType, url);
			}
		}
		return true;
	}

	/**
	 * 获取学员已修学分情况
	 * 
	 * @param studentXh
	 * @param specialtyId
	 * @return XF-学分 ZDXF-最低学分 YXXF-已修学分
	 */
	@Override
	public Map getMinAndSum(String studentXh, String specialtyId) {
		return studentInfoDao.getMinAndSum(studentXh, specialtyId);
	}

	/**
	 * 远程获取用户订单费用缴纳明细
	 * 
	 * @param sfzh
	 * @return
	 */
	private Map getAmount(String sfzh) {
		List<Map> list = billRemoteService.getBillList(sfzh, BillRemoteService.BillType.XF.name());
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return Collections.emptyMap();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ouchgzee.headTeacher.service.student.GjtStudentService#queryGradeBy(
	 * java.lang.String)
	 */
	@Override
	public List<BzrGjtGrade> queryGradeBy(String xxId) {
		return gjtGradeDao.findByXxId(xxId);
	}

	@Override
	public BzrGjtGrade queryGradeById(String termId) {
		return gjtGradeDao.findOne(termId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ouchgzee.headTeacher.service.student.GjtStudentService#
	 * querySpecialtyBy( java.lang.String)
	 */
	@Override
	public List<BzrGjtSpecialty> querySpecialtyBy(String xxId) {
		return gjtSpecialtyDao.findByXxId(xxId);
	}

	@Override
	public Map<String, String> getSignupCopyData(String studentId) {
		Map<String, String> dataMap = new HashMap<String, String>();
		List<Object[]> dataList = gjtSignupDataDao.querySignupDataByStudentId(studentId);
		if (dataList != null && dataList.size() > 0) {
			for (Object[] data : dataList) {
				String url = null;
				if (data[1] instanceof Clob) {
					Clob urlNew = (Clob) data[1];
					try {
						url = urlNew != null ? urlNew.getSubString(1, (int) urlNew.length()) : null;
					} catch (SQLException e) {
						log.error("类型转化异常", e);
					}
				} else {
					url = (String) data[1];
				}
				dataMap.put((String) data[0], url);
			}
		}
		return dataMap;
	}

	/**
	 * 处理学员登录详情明细
	 *
	 * @param infos
	 * @return
	 */
	@Override
	public HSSFWorkbook exportInfoDetails(List<BzrTblPriLoginLog> infos) {

		try {
			HSSFWorkbook book = null;
			book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			HSSFRow row = sheet.createRow(0);

			int rowIdx = 1, idx = 1;
			int colIdx = 0;

			row.createCell(colIdx++).setCellValue("序号");
			row.createCell(colIdx++).setCellValue("IP地址");
			row.createCell(colIdx++).setCellValue("所在区域");
			row.createCell(colIdx++).setCellValue("学习终端");
			row.createCell(colIdx++).setCellValue("浏览器");
			row.createCell(colIdx++).setCellValue("登入平台时间");
			row.createCell(colIdx++).setCellValue("登出平台时间");
			row.createCell(colIdx++).setCellValue("登录时长");

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			if (EmptyUtils.isNotEmpty(infos)) {// 存在考勤数据
				for (BzrTblPriLoginLog info : infos) {
					row = sheet.createRow(rowIdx++);
					colIdx = 0;
					row.createCell(colIdx++).setCellValue(idx++);
					row.createCell(colIdx++)
							.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getLoginIp())));
					row.createCell(colIdx++).setCellValue(new HSSFRichTextString(ObjectUtils.toString("")));
					row.createCell(colIdx++)
							.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getLoginType())));
					row.createCell(colIdx++)
							.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getBrowser())));
					row.createCell(colIdx++)
							.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getCreatedDt())));
					row.createCell(colIdx++)
							.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getUpdatedDt())));
					row.createCell(colIdx++)
							.setCellValue(new HSSFRichTextString(ObjectUtils.toString(info.getLoginTime())));
				}
			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Page<StudentSignupInfoDto> queryStudentSignupInfoByPage(Map<String, Object> searchParams,
			PageRequest pageRequest) {
		Page<Map> result = studentInfoDao.findStudentSignupInfoByPage(searchParams, pageRequest);

		List<StudentSignupInfoDto> infoList = new ArrayList();
		for (Iterator<Map> iter = result.iterator(); iter.hasNext();) {
			Map info = iter.next();

			StudentSignupInfoDto infoDto = new StudentSignupInfoDto();
			infoDto.setStudentId((String) info.get("STUDENT_ID"));
			infoDto.setAccountId((String) info.get("ACCOUNT_ID"));
			infoDto.setXm((String) info.get("XM"));
			infoDto.setXh((String) info.get("XH"));
			infoDto.setXbm((String) info.get("XBM"));
			infoDto.setSfzh((String) info.get("SFZH"));
			infoDto.setSjh((String) info.get("SJH"));
			infoDto.setPycc((String) info.get("PYCC"));
			infoDto.setYearName((String) info.get("YEAR_NAME"));
			infoDto.setGradeName((String) info.get("GRADE_NAME"));
			infoDto.setSpecialtyName((String) info.get("ZYMC"));
			BigDecimal perfectStatus = (BigDecimal) info.get("PERFECT_STATUS");
			infoDto.setPerfectStatus(perfectStatus != null ? perfectStatus.intValue() : null);
			infoDto.setSignupAuditState(((Character) info.get("AUDIT_STATE")) + "");
			BigDecimal flowAuditOperatorRole = (BigDecimal) info.get("FLOW_AUDIT_OPERATOR_ROLE");
			infoDto.setFlowAuditOperatorRole(flowAuditOperatorRole != null ? flowAuditOperatorRole.intValue() : null);
			BigDecimal flowAuditState = (BigDecimal) info.get("FLOW_AUDIT_STATE");
			infoDto.setFlowAuditState(flowAuditState != null ? flowAuditState.intValue() : null);

			infoDto.setAvatar((String) info.get("AVATAR"));
			infoDto.setXjzt((String) info.get("XJZT"));
			infoDto.setXxzxName((String) info.get("ORG_NAME"));
			infoDto.setUserType((String) info.get("USER_TYPE"));
			Character isEnteringSchool = (Character) info.get("IS_ENTERING_SCHOOL");
			infoDto.setIsEnteringSchool(isEnteringSchool != null ? isEnteringSchool.toString() : null);
			infoDto.setScCo((String) info.get("SC_CO"));

			/*
			 * String base64 = ObjectUtils.toString(info.get("SFZH")) + "|" +
			 * (System.currentTimeMillis()) / 1000 + "|" +
			 * Md5Util.encodeLower(ObjectUtils.toString(info.get("SFZH")) +
			 * "oucnet"); BASE64Encoder encoder = new BASE64Encoder(); String
			 * base64Code = encoder.encode(base64.getBytes());
			 * infoDto.setBase64Code(base64Code);
			 */
			infoDto.setSynUrl(AppConfig.getProperty("synlogin.url"));
			infoDto.setXxCode((String) info.get("ORGCODE"));

			infoList.add(infoDto);
		}
		return new PageImpl(infoList, pageRequest, result.getTotalElements());
	}

	@Override
	public Map<String, BigDecimal> countGroupbyAuditState(Map<String, Object> searchParams) {
		searchParams.remove("EQ_signupAuditState");
		searchParams.remove("EQ_perfectStatus");
		searchParams.remove("EQ_isEnteringSchool");
		List<Map> countList = studentInfoDao.countGroupbyAuditState(searchParams);
		Map<String, BigDecimal> countMap = new HashMap<String, BigDecimal>();
		BigDecimal sum = new BigDecimal(0);
		for (Map arr : countList) {
			BigDecimal num = (BigDecimal) arr.get("STUDENT_NUM");
			countMap.put(arr.get("AUDIT_STATE").toString(), num);
			sum = sum.add(num);
		}
		countMap.put("", sum);
		return countMap;
	}

	@Override
	public Map<String, BigDecimal> countGroupbyPerfectStatus(Map<String, Object> searchParams) {
		searchParams.remove("EQ_signupAuditState");
		searchParams.remove("EQ_perfectStatus");
		searchParams.remove("EQ_isEnteringSchool");
		List<Map> countList = studentInfoDao.countGroupbyPerfectStatus(searchParams);
		Map<String, BigDecimal> countMap = new HashMap<String, BigDecimal>();
		BigDecimal sum = new BigDecimal(0);
		for (Map arr : countList) {
			BigDecimal num = (BigDecimal) arr.get("STUDENT_NUM");
			countMap.put(arr.get("PERFECT_STATUS").toString(), num);
			sum = sum.add(num);
		}
		countMap.put("", sum);
		return countMap;
	}

	@Override
	public Map<String, BigDecimal> countGroupbyIsEnteringSchool(Map<String, Object> searchParams) {
		searchParams.remove("EQ_signupAuditState");
		searchParams.remove("EQ_perfectStatus");
		searchParams.remove("EQ_isEnteringSchool");
		List<Map> countList = studentInfoDao.countGroupbyIsEnteringSchool(searchParams);
		Map<String, BigDecimal> countMap = new HashMap<String, BigDecimal>();
		BigDecimal sum = new BigDecimal(0);
		for (Map arr : countList) {
			BigDecimal num = (BigDecimal) arr.get("STUDENT_NUM");
			countMap.put(arr.get("IS_ENTERING_SCHOOL").toString(), num);
			sum = sum.add(num);
		}
		countMap.put("", sum);
		return countMap;
	}

	@Override
	public Map<String, BigDecimal> countGroupbyXjzt(Map<String, Object> searchParams) {
		searchParams.remove("EQ_xjzt");
		List<Map> countList = studentInfoDao.countGroupbyXjzt(searchParams);
		Map<String, BigDecimal> countMap = new HashMap<String, BigDecimal>();
		BigDecimal sum = new BigDecimal(0);
		for (Map arr : countList) {
			BigDecimal num = (BigDecimal) arr.get("STUDENT_NUM");
			countMap.put(arr.get("XJZT").toString(), num);
			sum = sum.add(num);
		}
		countMap.put("", sum);
		return countMap;
	}

	/**
	 * 获取学员的课程考勤统计
	 * 
	 * @param searchParams
	 * @return
	 */
	@Override
	public Map getStudentLoginDetail(Map searchParams) {
		Map resultMap = new HashMap();
		PageRequest pageRequest = new PageRequest(0, 1, null);
		Page studentInfo = recResultDao.getStudentLoginList(searchParams, pageRequest);// 个人信息，学习比重等
		Map studentMap = new HashMap();
		if (EmptyUtils.isNotEmpty(studentInfo)) {// 学生头部信息
			if (EmptyUtils.isNotEmpty(studentInfo.getContent())) {
				studentMap.putAll((Map) studentInfo.getContent().get(0));
			}
		}
		List resultList = studentInfoDao.getStudentLoginDetail(searchParams);// 课程考勤明细

		resultMap.put("studentMap", studentMap);
		resultMap.put("resultList", resultList);
		return resultMap;
	}

	@Override
	public BzrGjtClassInfo queryTeachClassInfoByStudetnId(String studentId) {

		return gjtClassInfoDao.queryTeachByStudetnId(studentId);
	}

	/**
	 * 导出学员考勤统计表
	 * 
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadAttendanceListXls(Map searchParams) {
		List<Map<String, Object>> resultList = recResultDao.getStudentLoginList(searchParams);
		try {
			HSSFWorkbook book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			if (EmptyUtils.isNotEmpty(resultList)) {
				HSSFRow row;
				HSSFCell cell;
				int rowIndex = 0;
				int cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				// 标题栏
				cell = row.createCell(cellIndex++);
				cell.setCellValue("姓名");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("学号");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("手机");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("专业层次");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("入学年级");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("入学学期");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("报读专业");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("教务班");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("登录次数");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("登录时长（小时）");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("主要应用终端");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("PC端登陆次数");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("APP登录次数");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("APP端登录占比");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("最近学习日期");
				cell = row.createCell(cellIndex++);
				cell.setCellValue("当前学习状态");

				for (Map e : resultList) {
					cellIndex = 0;
					row = sheet.createRow(rowIndex++);
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("XM")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("XH")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SJH")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("PYCC_NAME")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("YEAR_NAME")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("ZYMC")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("BJMC")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_COUNT")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_TIME")));
					cell = row.createCell(cellIndex++);
					int app_count = Integer.valueOf(ObjectUtils.toString(e.get("APP_ONLINE_COUNT"), "0"));
					int pc_count = Integer.valueOf(ObjectUtils.toString(e.get("PC_ONLINE_COUNT"), "0"));
					if (app_count > pc_count) {
						cell.setCellValue(ObjectUtils.toString("APP"));
					} else if (app_count <= pc_count && pc_count != 0) {
						cell.setCellValue(ObjectUtils.toString("PC"));
					} else {
						cell.setCellValue("");
					}
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("PC_ONLINE_COUNT")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("APP_ONLINE_COUNT")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("APP_ONLINE_PERCENT"), "0") + "%");
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("LAST_LOGIN_TIME")));
					cell = row.createCell(cellIndex++);
					String isOnline = ObjectUtils.toString(e.get("IS_ONLINE"), "");
					if ("0".equals(ObjectUtils.toString(e.get("LOGIN_COUNT")))) {
						cell.setCellValue("从未学习");
					} else {
						if ("".equals(isOnline)) {
							cell.setCellValue("从未学习");
						} else if ("N".equals(isOnline)) {
							cell.setCellValue("离线");
						} else if ("Y".equals(isOnline)) {
							cell.setCellValue("在线");
						}
					}
				}
			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 导出学员学习记录明细表
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook downLoadAttendanceDetailXls(Map searchParams) {
		Map resultMap = getStudentLoginDetail(searchParams);
		Map studentInfo = new HashMap();
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		if (EmptyUtils.isNotEmpty(resultMap)) {
			studentInfo = (Map) resultMap.get("studentMap");
			resultList = (List<Map<String, String>>) resultMap.get("resultList");
		}

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(resultList)) {
			HSSFRow row;
			HSSFCell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("姓名");
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
			cell.setCellValue("课程");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程班");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习时长（小时）");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("主要应用终端");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("最近学习日期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("最近学习状态");

			for (Map e : resultList) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(studentInfo.get("XM")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(studentInfo.get("XH")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(studentInfo.get("SJH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(studentInfo.get("PYCC_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("YEAR_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(studentInfo.get("ZYMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(studentInfo.get("BJMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_COUNT")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_TIME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("--")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LAST_DATE")));
				cell = row.createCell(cellIndex++);
				String isOnline = ObjectUtils.toString(e.get("IS_ONLINE"), "");
				if ("".equals(isOnline)) {
					cell.setCellValue("从未学习");
				} else if ("1".equals(isOnline)) {
					cell.setCellValue("离线");
				} else if ("0".equals(isOnline)) {
					cell.setCellValue("在线");
				}
			}
		}
		return wb;
	}

	@Override
	public List<BzrGjtGrade> findCurrentTermAndBeforeTerm(String xxId) {
		return gjtGradeDao.findCurrentTermAndBeforeTerm(xxId);
	}

	@Override
	public List<BzrGjtGrade> findCurrentTermAndBeforeTerm(String xxId, String termId) {
		List<BzrGjtGrade> list = new ArrayList<BzrGjtGrade>();
		List<BzrGjtGrade> result = gjtGradeDao.findCurrentTermAndBeforeTerm(xxId, termId);
		for (int i = 0; i < result.size() && i < 5; i++) {
			list.add(result.get(i));
		}
		return list;
	}

	@Override
	public List<Map> queryStudyRankingTopTenScheduleByCourseClass(String teachClassId, String courseId,
			Map<String, Object> queryParams) {
		return studentInfoDao.queryStudyRankingTopTenScheduleByCourseClass(teachClassId, courseId, queryParams);
	}

	@Override
	public List<Map> queryStudyRankingTopTenStudyNumByCourseClass(String teachClassId, String courseId,
			Map<String, Object> queryParams) {
		return studentInfoDao.queryStudyRankingTopTenStudyNumByCourseClass(teachClassId, courseId, queryParams);
	}

	@Override
	public List<Map> queryStudyRankingTopTenStudyHourByCourseClass(String teachClassId, String courseId,
			Map<String, Object> queryParams) {
		return studentInfoDao.queryStudyRankingTopTenStudyHourByCourseClass(teachClassId, courseId, queryParams);
	}

	@Override
	public List<Object[]> queryAtidByIds(final String... ids) {
		List<Object[]> list = this.gjtStudentInfoDao.queryAtidByIds(Lists.newArrayList(ids));
		return list;
	}

	@Override
	public BzrGjtStudentEnteringSchool queryStudentEnteringSchoolByStudentId(String studentId) {
		return gjtStudentEnteringSchoolDao.findOne(studentId);
	}

	/**
	 * 学习管理--》考勤分析--》课程考勤列表
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@Override
	public Page<Map<String, Object>> courseAttendanceList(Map<String, Object> searchParams, PageRequest pageRequst) {

		return recResultDao.courseAttendanceList(searchParams, pageRequst);
	}

	/**
	 * 课程考勤详情页
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@Override
	public Page<Map<String, Object>> courseAttendanceDetails(Map<String, Object> searchParams, PageRequest pageRequst) {

		return recResultDao.courseAttendanceDetails(searchParams, pageRequst);
	}

	/**
	 * 学员考勤列表
	 *
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@Override
	public Page getStudentLoginList(Map searchParams, PageRequest pageRequst) {
		return recResultDao.getStudentLoginList(searchParams, pageRequst);
	}

	/**
	 * 学员考勤列表统计项
	 * 
	 * @param searchParams
	 * @return
	 */
	@Override
	public int getStudentLoginCount(Map searchParams) {
		return recResultDao.getStudentLoginCount(searchParams);
	}

	/**
	 * 课程考勤列表下载
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook courseAttendanceList(Map searchParams) {

		List<Map<String, Object>> result = recResultDao.courseAttendanceList(searchParams);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(result)) {
			HSSFRow row;
			HSSFCell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("开课状态");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("选课人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习总次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习总时长");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习时长");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("7天以上未学习");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("3天以上未学习");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("3天以内未学习");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("从未学习人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("当前在学人数");

			for (Map e : result) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));

				cell = row.createCell(cellIndex++);
				String TIME_FLG = ObjectUtils.toString(e.get("TIME_FLG"));
				if ("1".equals(TIME_FLG)) {
					cell.setCellValue("开课中");
				} else if ("2".equals(TIME_FLG)) {
					cell.setCellValue("待开课");
				} else if ("3".equals(TIME_FLG)) {
					cell.setCellValue("已结束");
				} else {
					cell.setCellValue("已结束");
				}

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("CHOOOSE_COUNT")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_COUNT")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("AVG_LOGIN_COUNT")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_TIME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("AVG_LOGIN_TIME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("DAY7_LOGIN")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("DAY3_7_LOGIN")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("DAY3_LOGIN")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("NO_DAY_LOGIN")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ONLINE_STUDENT_COUNT")));
			}
		}
		return wb;
	}

	/**
	 * 课程考勤详情列表下载
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook courseAttendanceDetails(Map searchParams) {

		List<Map<String, Object>> result = recResultDao.courseAttendanceDetails(searchParams);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(result)) {
			HSSFRow row;
			HSSFCell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("姓名");
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
			cell.setCellValue("课程");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程班");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习时长（小时）");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("主要应用终端");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("最近学习日期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("当前学习状态");

			for (Map e : result) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XM")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XH")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SJH")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("PYCC_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("YEAR_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ZYMC")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("BJMC")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_COUNT")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_TIME")));

				cell = row.createCell(cellIndex++);
				int pcOnlinePercnet = Integer.parseInt(ObjectUtils.toString(e.get("PC_ONLINE_PERCENT")));
				int appOnlinePercent = Integer.parseInt(ObjectUtils.toString(e.get("APP_ONLINE_PERCENT")));
				int loginCount = Integer.parseInt(ObjectUtils.toString(e.get("LOGIN_COUNT"), "0"));
				if ("0".equals(loginCount) || loginCount == 0) {
					cell.setCellValue("");
				} else {
					if (pcOnlinePercnet >= appOnlinePercent && pcOnlinePercnet != 0) {
						cell.setCellValue("PC");
					} else {
						cell.setCellValue("APP");
					}
				}
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LAST_DATE")));

				cell = row.createCell(cellIndex++);
				String isOnline = ObjectUtils.toString(e.get("IS_ONLINE"));
				if ("1".equals(isOnline)) {
					if ("0".equals(ObjectUtils.toString(e.get("LOGIN_COUNT")))) {
						cell.setCellValue("从未学习");
					} else {
						cell.setCellValue("离线");
					}
				} else if ("0".equals(isOnline)) {
					cell.setCellValue("在线");
				} else {
					cell.setCellValue("离线");
				}
			}
		}
		return wb;
	}

	/**
	 * 课程学情列表下载
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook getCourseList(Map searchParams) {
		List<Map<String, Object>> resultList = recResultDao.getCourseList(searchParams);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(resultList)) {
			HSSFRow row;
			HSSFCell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("开课学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程名称");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("开课状态");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("选课人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习进度");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习时长");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均学习成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均考试成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("平均总成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("已通过人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("未通过人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习中人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("未学习人数");

			if (EmptyUtils.isNotEmpty(resultList)) {
				for (Map e : resultList) {
					cellIndex = 0;
					row = sheet.createRow(rowIndex++);
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));
					cell = row.createCell(cellIndex++);
					String time_flg = ObjectUtils.toString(e.get("TIME_FLG"));// 开课状态
					if ("1".equals(time_flg)) {
						cell.setCellValue("开课中");
					} else if ("2".equals(time_flg)) {
						cell.setCellValue("待开课");
					} else if ("3".equals(time_flg)) {
						cell.setCellValue("已结束");
					} else {
						cell.setCellValue("已结束");
					}
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("REC_COUNT")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("AVG_SCHEDULE")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("AVG_LOGIN_COUNT")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("AVG_LOGIN_TIME")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("AVG_STUDY_SCORE")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("AVG_EXAM_SCORE")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("AVG_TOTAL_SCORE")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SUM_PASS_COUNT")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(Integer.parseInt(ObjectUtils.toString(e.get("REC_COUNT"), "0"))
							- Integer.parseInt(ObjectUtils.toString(e.get("SUM_PASS_COUNT"), "0"))
							- Integer.parseInt(ObjectUtils.toString(e.get("SUM_STUDY_IMG"), "0"))
							- Integer.parseInt(ObjectUtils.toString(e.get("SUM_NEVER_STUDY"), "0")));// entity.REC_COUNT
																										// -
																										// entity.SUM_PASS_COUNT

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SUM_STUDY_IMG")));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("SUM_NEVER_STUDY")));
				}
			}
		}
		return wb;
	}

	/**
	 * 课程学情详情下载
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook courseLearnConditionDetails(Map searchParams) {

		List<Map<String, Object>> result = recResultDao.courseLearnConditionDetails(searchParams);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(result)) {
			HSSFRow row;
			HSSFCell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("姓名");
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
			cell.setCellValue("课程");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程班");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习进度");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习成绩");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习次数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习时长");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习状态");

			for (Map e : result) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XM")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XH")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SJH")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("PYCC_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("YEAR_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ZYMC")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("BJMC")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SCHEDULE"), "0") + "%");

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("EXAM_SCORE")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_COUNT")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("LOGIN_TIME")));

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("EXAM_STATE"), ""));
			}
		}
		return wb;
	}

	/**
	 * 学支平台--首页批量下载班级学员信息
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Workbook getClassStudInfo(Map<String, Object> searchParams) {
		List<Map<String, Object>> result = recResultDao.getClassStudInfo(searchParams);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		if (EmptyUtils.isNotEmpty(result)) {
			HSSFRow row;
			HSSFCell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("姓名");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("身份证");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("手机号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("年级");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("层次");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("专业");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("班级名称");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("班主任");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习中心");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("所在单位");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("邮箱");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("教材收货人");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("教材收货人手机");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("教材邮寄地址");

			for (Map e : result) {
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);

				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XM")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("XH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SFZH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SJH")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("PYCC_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ZYMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("BJMC")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("TEACHER_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SC_NAME")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("SC_CO")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("DZXX")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("RECEIVER")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("MOBILE")));
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(e.get("ADDRESS")));
			}
		}
		return wb;
	}
}
