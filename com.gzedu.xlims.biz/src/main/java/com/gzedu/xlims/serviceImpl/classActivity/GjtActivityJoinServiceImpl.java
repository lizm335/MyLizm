/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.classActivity;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.classActivity.GjtActivityJoinDao;
import com.gzedu.xlims.daoImpl.classActivity.GjtActivityJoinDaoImpl;
import com.gzedu.xlims.pojo.GjtActivityJoin;
import com.gzedu.xlims.pojo.GjtActivityJoinPK;
import com.gzedu.xlims.pojo.dto.ActivityJoinDto;
import com.gzedu.xlims.pojo.dto.GjtActivityDto;
import com.gzedu.xlims.service.classActivity.GjtActivityJoinService;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月28日
 * @version 2.5
 *
 */
@Service
public class GjtActivityJoinServiceImpl implements GjtActivityJoinService {
	@Autowired
	private GjtActivityJoinDao gjtActivityJoinDao;
	@Autowired
	private GjtActivityJoinDaoImpl gjtActivityJoinImpl;

	/**
	 * 已结束的活动
	 * 
	 * @param auditStatus
	 * @return
	 */
	@Override
	public long countApplyNum(final String id, final String auditStatus) {
		Specification<GjtActivityJoin> spec = new Specification<GjtActivityJoin>() {

			@Override
			public Predicate toPredicate(Root<GjtActivityJoin> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				expressions.add(cb.equal(root.get("auditStatus"), auditStatus));
				expressions.add(cb.equal(root.get("id").get("activityId"), id));
				return predicate;
			}

		};
		return gjtActivityJoinDao.count(spec);
	}

	/**
	 * 导出活动呢人员
	 * 
	 * @param activityId
	 * @return
	 */
	@Override
	public HSSFWorkbook exportActivityNumberToExcel(String activityId) {
		// List<Map> infos = this.queryStudentRecResultDetail(studentId);
		List<ActivityJoinDto> passList = gjtActivityJoinImpl.getActivityStudentsInfo(activityId, "1");

		try {
			HSSFWorkbook book = null;
			book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			HSSFRow row = sheet.createRow(0);

			int rowIdx = 1, idx = 1;
			int colIdx = 0;

			row.createCell(colIdx++).setCellValue("序号");
			row.createCell(colIdx++).setCellValue("姓名");
			row.createCell(colIdx++).setCellValue("性别");
			row.createCell(colIdx++).setCellValue("报名时间");

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行
			// 生成一个样式
			HSSFCellStyle style = book.createCellStyle();
			// 生成一个字体
			HSSFFont font = book.createFont();
			font.setColor(HSSFColor.RED.index);
			// 把字体应用到当前的样式
			style.setFont(font);

			for (ActivityJoinDto activityJoinDto : passList) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				row.createCell(colIdx++).setCellValue(idx++);
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(activityJoinDto.getName()));
				row.createCell(colIdx++).setCellValue(new HSSFRichTextString(activityJoinDto.getSex()));
				row.createCell(colIdx++)
						.setCellValue(new HSSFRichTextString(ObjectUtils.toString(activityJoinDto.getJoinDt() != null
								? DateFormatUtils.ISO_DATETIME_FORMAT.format(activityJoinDto.getJoinDt()) : null)));

			}
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取参与学生信息
	 * 
	 * @param activityId
	 * @return
	 */
	public List<ActivityJoinDto> getActivityStudentsInfo(String activityId, String auditStatus) {
		return gjtActivityJoinImpl.getActivityStudentsInfo(activityId, auditStatus);
	}

	public Page<ActivityJoinDto> getActivityStudentsInfoPage(String activityId, PageRequest pageRequest) {
		return gjtActivityJoinImpl.getActivityStudentsInfoPage(activityId, pageRequest);
	}

	/**
	 * 学生报名的活动，返回详情，模糊查询
	 */
	public Page<GjtActivityDto> getActivityByStudent(Map<String, Object> map, PageRequest pageRequest) {
		return gjtActivityJoinImpl.getActivityByStudent(map, pageRequest);
	}

	/**
	 * 学生报名的活动，只返回ID
	 */
	public List<GjtActivityJoin> getActivityIdByStudentId(String studentId) {
		return gjtActivityJoinDao.findByIdStudentId(studentId);
	}

	/**
	 * 批量审核活动
	 * 
	 * @param activityId
	 * @return
	 */
	public void updateBatchAuditActivity(String activityId, String auditStatus) {
		gjtActivityJoinImpl.batchAuditActivity(activityId, auditStatus);
	}

	/**
	 * 单独审核学生的活动
	 * 
	 * @param activityId
	 * @return
	 */
	public void updateBatchAuditActivityBystudentId(String activityId, String auditStatus, String studentId) {
		gjtActivityJoinImpl.batchAuditActivityBystudentId(activityId, auditStatus, studentId);
	}

	/**
	 * 审核不通过的活动
	 * 
	 * @param activityId
	 * @return
	 */
	public void updateBatchAuditActivitytoUnpass(String activityId, String auditStatus, String studentId) {
		gjtActivityJoinImpl.batchAuditActivityBystudentIdtoUnpass(activityId, auditStatus, studentId);
	}

	@Override
	public GjtActivityJoin queryById(String studentId, String activityId) {
		// return gjtActivityJoinDao.findByIdStudentIdAndActivityId(studentId,
		// activityId);
		GjtActivityJoinPK pk = new GjtActivityJoinPK();
		pk.setActivityId(activityId);
		pk.setStudentId(studentId);
		return gjtActivityJoinDao.findById(pk);
	}

	@Override
	public Boolean insert(GjtActivityJoin entity) {
		GjtActivityJoin activityJoin = gjtActivityJoinDao.save(entity);
		if (activityJoin != null) {
			return true;
		}
		return false;
	}

}
