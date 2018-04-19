/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.signup;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.signup.GjtSignupDao;
import com.gzedu.xlims.dao.signup.GjtSignupDataDao;
import com.gzedu.xlims.dao.signup.GjtSignupNativeDao;
import com.gzedu.xlims.pojo.GjtSignup;
import com.gzedu.xlims.pojo.GjtSignupData;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年8月19日
 * @version 1.0
 *
 */
@Service
public class GjtSignupServiceImpl extends BaseServiceImpl<GjtSignup> implements GjtSignupService {
	private static final Log log = LogFactory.getLog(GjtSignupServiceImpl.class);
	@Autowired
	private GjtSignupDao gjtSignupDao;
	@Autowired
	private GjtSignupDataDao gjtSignupDataDao;
	@Autowired
	private CommonMapService commonMapService;
	@Autowired
	private GjtSignupNativeDao gjtSignupNativeDao;
	
	@Override
	protected BaseDao<GjtSignup, String> getBaseDao() {
		return this.gjtSignupDao;
	}
	
	@Override
	public Page<GjtSignup> queryPageList(String xxId,Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:"+"【"+searchParams+"】;"+"xxId:"+"【"+xxId+"】");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.IN, xxId));
		Specification<GjtSignup> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtSignup.class);
		return gjtSignupDao.findAll(spec, pageRequst);
	}

	@Override
	public HSSFWorkbook exportInfo(String xxId,Map<String, Object> searchParams, Sort sort) {
		log.info("searchParams:"+"【"+searchParams+"】;"+"xxId:"+"【"+xxId+"】");
//		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
//		filters.put("xxId", new SearchFilter("xxId", Operator.IN, xxId));
//		Specification<GjtSignup> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtSignup.class);
		List<Map<String, String>> result = gjtSignupNativeDao.queryGjtSignupList(xxId,searchParams);
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		Map<String, String> auditState = new HashMap<String, String>();
		auditState.put("0", "审核不通过");
		auditState.put("1", "审核通过");
		auditState.put("3", "待审核");
		auditState.put("4", "未提交");
		try {
			HSSFWorkbook book = null;
			book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell = row.createCell((short) 0);
			int rowIdx = 1, idx = 1;
			int colIdx = 0;
			row.createCell(colIdx++).setCellValue("序号");
			row.createCell(colIdx++).setCellValue("姓名");
			row.createCell(colIdx++).setCellValue("性别");
			row.createCell(colIdx++).setCellValue("学号");
			row.createCell(colIdx++).setCellValue("身份证");
			row.createCell(colIdx++).setCellValue("年级");
			row.createCell(colIdx++).setCellValue("层次");
			row.createCell(colIdx++).setCellValue("专业");
			row.createCell(colIdx++).setCellValue("报名时间");
			row.createCell(colIdx++).setCellValue("学生来源");
			row.createCell(colIdx++).setCellValue("资料提交");
			row.createCell(colIdx++).setCellValue("缴费状态");
			row.createCell(colIdx++).setCellValue("学习中心");

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行
			// 生成一个样式
			HSSFCellStyle style = book.createCellStyle();
			// 生成一个字体
			HSSFFont font = book.createFont();
			font.setColor(HSSFColor.RED.index);
			// 把字体应用到当前的样式
			style.setFont(font);

			for (Iterator<Map<String, String>> iter = result.iterator(); iter.hasNext();) {
				Map map = (Map) iter.next();
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				row.createCell(colIdx++).setCellValue(idx++);
//				row.createCell(colIdx++)
//						.setCellValue((info.getGjtStudentInfo() == null ? "" : info.getGjtStudentInfo().getXm()));
//				row.createCell(colIdx++).setCellValue((info.getGjtStudentInfo() == null ? ""
//						: ("1".equals(info.getGjtStudentInfo().getXbm()) ? "男" : "女")));
//				row.createCell(colIdx++)
//						.setCellValue((info.getGjtStudentInfo() == null ? "" : info.getGjtStudentInfo().getXh()));
//				row.createCell(colIdx++)
//						.setCellValue((info.getGjtStudentInfo() == null ? "" : info.getGjtStudentInfo().getSfzh()));
//				row.createCell(colIdx++).setCellValue((info.getGjtStudentInfo() == null ? ""
//						: info.getGjtStudentInfo().getGjtGrade().getGradeName()));
//				row.createCell(colIdx++).setCellValue(
//						(info.getGjtStudentInfo() == null ? "" : pyccMap.get(info.getGjtStudentInfo().getPycc())));
//				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) commonMapService
//						.getSpecialtyDates(info.getSignupSpecialtyId()).get(info.getSignupSpecialtyId())));
//				row.createCell(colIdx++).setCellValue(DateFormatUtils.ISO_DATETIME_FORMAT.format(info.getCreatedDt()));
//				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) info.getAuditSource()));
//				row.createCell(colIdx++)
//						.setCellValue(new HSSFRichTextString((String) auditState.get(info.getAuditState())));
//				row.createCell(colIdx++).setCellValue("1".equals(info.getCharge()) ? "已缴费" : "未缴费");
//				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(
//						(String) commonMapService.getXxzxmcDates(info.getXxzxId()).get(info.getXxzxId())));
				row.createCell(colIdx++)
				.setCellValue((map.get("XM").toString() == null ? "" : map.get("XM").toString()));
				row.createCell(colIdx++).setCellValue((map.get("XBM") == null ? ""
						: ("1".equals(map.get("XBM")) ? "男" : "女")));
				row.createCell(colIdx++)
				.setCellValue((map.get("XH").toString() == null ? "" : map.get("XH").toString()));
				row.createCell(colIdx++)
				.setCellValue((map.get("SFZH").toString() == null ? "" : map.get("SFZH").toString()));
				row.createCell(colIdx++).setCellValue((map.get("GRADE_NAME").toString() == null ? ""
						: map.get("GRADE_NAME").toString()));
				row.createCell(colIdx++).setCellValue(
						(map.get("PYCC").toString() == null ? "" : map.get("PYCC").toString()));
				row.createCell(colIdx++).setCellValue(
						(map.get("ZYMC").toString() == null ? "" : map.get("ZYMC").toString()));
				row.createCell(colIdx++).setCellValue(DateFormatUtils.ISO_DATETIME_FORMAT.format(map.get("CREATED_DT")));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString((String) map.get("AUDIT_SOURCE")));
				row.createCell(colIdx++)
				.setCellValue(map.get("AUDIT_STATE").toString());
				row.createCell(colIdx++).setCellValue("1".equals(map.get("CHARGE")) ? "已缴费" : "未缴费");
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(
						(String) commonMapService.getXxzxmcDates((String) map.get("XXZX_ID")).get(map.get("XXZX_ID"))));
			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, String> getSignupCopyData(String studentId) {
		Map<String, String> dataMap = new HashMap<String, String>();
		List<Object[]> dataList = gjtSignupDataDao.querySignupDataByStudentId(studentId);
		if(dataList != null && dataList.size() > 0) {
			for (Object[] data : dataList) {
				String url = null;
				if(data[1] instanceof Clob) {
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

	@Override
	public boolean auditSignupData(String studentId, String auditState, String auditOpinion, String updatedBy) {
		boolean pass = false;
		if((pass = "1".equals(auditState)) || "0".equals(auditState) || "2".equals(auditState)) {
			Date now = new Date();
			if(pass) {
				gjtSignupDao.auditSignupData(studentId, auditState, auditOpinion, updatedBy, now);
				gjtSignupDataDao.auditSignupData(studentId, auditState, auditOpinion, updatedBy, now);
			} else {
				gjtSignupDao.auditSignupData(studentId, auditState, auditOpinion, updatedBy, now);
			}
			return true;
		}
		return false;
	}

	@Override
	public long queryAuditStateTotalNum(String xxId, String auditState,Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, xxId));
		if(auditState!=""){
			filters.put("auditState", new SearchFilter("auditState", Operator.EQ, auditState));	
		}			
		Specification<GjtSignup> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtSignup.class);
		return gjtSignupDao.count(spec);
	}

	@Override
	public GjtSignup querySignupBySignupId(String xxId, String id) {
		log.info("xxId:【"+xxId+"】;【"+id+"】");
		return gjtSignupDao.findOne(id);
	}
	
	@Override
	public GjtSignup querySignupByStudentId(String studentId) {
		return gjtSignupDao.querySignupByStudentId(studentId);
	}

	@Override
	public boolean updateEveryType(String studentId, String signupSfzType, String zgxlRadioType, String signupByzType, String signupJzzType) {
		GjtSignup info = gjtSignupDao.querySignupByStudentId(studentId);
		if(StringUtils.isNotBlank(signupSfzType)) {
			info.setSignupSfzType(NumberUtils.toInt(signupSfzType));
		}
		if(StringUtils.isNotBlank(zgxlRadioType)) {
			info.setZgxlRadioType(NumberUtils.toInt(zgxlRadioType));
		}
		if(StringUtils.isNotBlank(signupByzType)) {
			info.setSignupByzType(NumberUtils.toInt(signupByzType));
		}
		if(StringUtils.isNotBlank(signupJzzType)) {
			info.setSignupJzzType(NumberUtils.toInt(signupJzzType));
		}
		gjtSignupDao.save(info);
		return true;
	}

	@Override
	public List<Map> querySignupNums(String id, Map<String, Object> searchParams) {
		searchParams.put("xxId", id);
		List<Map> result=gjtSignupNativeDao.querySignupNums(searchParams,null);
		return result;
	}

	@Override
	public List<Map> querySignupPayCostNum(String id, Map<String, Object> searchParams) {
		searchParams.put("xxId", id);
		List<Map> result=gjtSignupNativeDao.querySignupPayCostNum(searchParams,null);
		return result;
	}
	
	@Override
	public boolean updateSignupCopyData(String studentId, String fileType, String url) {
		GjtSignup info = gjtSignupDao.querySignupByStudentId(studentId);
		GjtSignupData entity = gjtSignupDataDao.findByStudentIdAndFileType(studentId, fileType);
		if(entity == null) {
			entity = new GjtSignupData();
			entity.setId(UUIDUtils.random());
			entity.setContentType("application/octet-stream");
			entity.setAuditState("0");
			entity.setSource("student"); // 证件资料来源 1.sysadm 2.student
		}
		entity.setStudentId(studentId);
		entity.setSignupId(info != null ? info.getSignupId() : null);
		entity.setIdNo(info != null ? info.getIdcard() : null);
		entity.setFileType(fileType);
		entity.setUrlNew(url);
		gjtSignupDataDao.save(entity);
		return true;
	}

	@Override
	public boolean updateSignupCopyData(String studentId, Map<String, String> signupCopyData) {
		for (Entry<String, String> item : signupCopyData.entrySet()) {
			if (StringUtils.isNotBlank(item.getValue())) {
				this.updateSignupCopyData(studentId, item.getKey(), item.getValue());
			}
		}
		return true;
	}

}
