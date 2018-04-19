package com.gzedu.xlims.web.controller.exam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.MapKit;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamCost;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.exam.GjtExamCostService;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/exam/new/cost")
public class GjtExamCostController {
	
	@Autowired
	private GjtExamCostService gjtExamCostService;

	@Autowired
	private CommonMapService commonMapService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String schoolId= user.getGjtOrg().getId();
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        // 默认选择当前期(批次)
        if(EmptyUtils.isEmpty(searchParams)|| StringUtils.isBlank((String) searchParams.get("EQ_examBatchCode"))){
            String code = commonMapService.getCurrentGjtExamBatchNew(schoolId);
            searchParams.put("EQ_examBatchCode", code);
            model.addAttribute("examBatchCode",code);
        }else if(EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EQ_examBatchCode")) ){
            model.addAttribute("examBatchCode", ObjectUtils.toString(searchParams.get("EQ_examBatchCode")));
        }
        
        Page<GjtExamCost> pageInfo = gjtExamCostService.findAll(schoolId, searchParams, pageRequst);
        
        model.addAttribute("termMap", commonMapService.getGradeMap(user.getGjtOrg().getId()));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(schoolId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(schoolId));
		model.addAttribute("examTypeMap", MapKit.toIntAscMap(commonMapService.getExamTypeIntMap()));
		model.addAttribute("pageInfo", pageInfo);

		return "edumanage/exam/exam_cost_list";
	}

	/**
	 * 导出补考缴费记录
	 * 
	 * @param examBatchCode
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SysLog("补考缴费-导出补考缴费记录")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	@ResponseBody
	public void exportPlanAppoinments(@RequestParam String examBatchCode, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("EQ_examBatchCode", examBatchCode);
		
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		
		Page<GjtExamCost> pageInfo = gjtExamCostService.findAll(user.getGjtOrg().getId(), params, pageRequst);
		
		HSSFWorkbook workbook = this.getWorkbook(pageInfo.getContent());

		response.setContentType("application/x-msdownload;charset=utf-8");
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String("补考缴费记录.xls".getBytes("UTF-8"), "ISO8859-1"));
		workbook.write(response.getOutputStream());
	}
	
	private HSSFWorkbook getWorkbook(List<GjtExamCost> list) {
		Subject subject = SecurityUtils.getSubject();
		boolean isPrivacyJurisdiction= subject.isPermitted("/personal/index$privacyJurisdiction");
		
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
		if(isPrivacyJurisdiction) {
			cell = row.createCell(cellIndex++);
			cell.setCellValue("手机");
		}
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
		cell.setCellValue("考试形式");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("是否补考");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("补考费用");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("缴费状态");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("缴费时间");

		@SuppressWarnings("unchecked")
		Map<Integer, String> examTypeMap = commonMapService.getExamTypeIntMap();
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (GjtExamCost examCost : list) {
			cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examCost.getStudent().getXm());
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examCost.getStudent().getXh());
			if(isPrivacyJurisdiction) {
				cell = row.createCell(cellIndex++);
				cell.setCellValue(examCost.getStudent().getSjh());
			}
			cell = row.createCell(cellIndex++);
			cell.setCellValue(pyccMap.get(examCost.getStudent().getPycc()));
			cell = row.createCell(cellIndex++);
			if (examCost.getStudent().getGjtGrade() != null) {
				cell.setCellValue(examCost.getStudent().getGjtGrade().getGjtYear().getName());
			} else {
				cell.setCellValue("");
			}
			cell = row.createCell(cellIndex++);
			if (examCost.getStudent().getGjtGrade() != null) {
				cell.setCellValue(examCost.getStudent().getGjtGrade().getGradeName());
			} else {
				cell.setCellValue("");
			}
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examCost.getStudent().getGjtSpecialty().getZymc());
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examCost.getExamBatchNew().getName());
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examCost.getExamBatchNew().getExamBatchCode());
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examCost.getExamPlanNew().getExamPlanName());
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examTypeMap.get(examCost.getExamPlanNew().getType()));
			cell = row.createCell(cellIndex++);
			if ("1".equals(examCost.getMakeup())) {
				cell.setCellValue("是");
			} else {
				cell.setCellValue("否");
			}
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examCost.getCourseCost());
			cell = row.createCell(cellIndex++);
			if ("0".equals(examCost.getPayStatus())) {
				cell.setCellValue("已缴费");
			} else {
				cell.setCellValue("未缴费");
			}
			cell = row.createCell(cellIndex++);
			cell.setCellValue(examCost.getPayDate() != null ? df.format(examCost.getPayDate()) : "");
		}

		return workbook;
	}

}
