/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager.student;

import com.ouchgzee.headTeacher.dto.StudentInvoiceDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.invoice.BzrGjtInvoiceService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.common.UploadTmpFile;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 发票管理控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月20日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/class/feeInvoice")
public class FeeInvoiceController extends BaseController {

    @Autowired
    private BzrGjtInvoiceService gjtInvoiceService;

    @Autowired
    private BzrGjtStudentService gjtStudentService;

    @Autowired
    private BzrCommonMapService commonMapService;

    private static String INVOICE = "Invoice_${StudentId}";

    /**
     * 发票信息列表
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "page.size", defaultValue = "10") int pageSize,
                       Model model, ServletRequest request, HttpSession session) {
        PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        String classId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID)) ? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);
        Page<StudentInvoiceDto> infos = gjtInvoiceService.queryRemoteFeeInvoiceByClassIdPage(classId, searchParams, pageRequst);

        for (Iterator<StudentInvoiceDto> iter = infos.iterator(); iter.hasNext();) {
            StudentInvoiceDto info = iter.next();
            
            session.setAttribute(INVOICE.replace("${StudentId}", info.getStudentId()), info);
        }
        BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
        Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(employeeInfo.getXxId());
        Map<String, String> gradeMap = commonMapService.getGradeMap(employeeInfo.getXxId()); //年级
        Map<String, String> pyccMap = commonMapService.getPyccMap(); //层次

        model.addAttribute("infos", infos);
        model.addAttribute("specialtyMap", specialtyMap);
        model.addAttribute("gradeMap",gradeMap);
        model.addAttribute("pyccMap", pyccMap);
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
        return "new/class/feeInvoice/list";
    }

    /**
     * 浏览发票信息
     * @param studentId
     * @return
     */
    @RequestMapping(value = "view/{studentId}", method = RequestMethod.GET)
    public String viewForm(@PathVariable("studentId") String studentId, Model model,
                           ServletRequest request, HttpSession session) {
        BzrGjtStudentInfo studentInfo = gjtStudentService.queryById(studentId);
        // 发票详情
        StudentInvoiceDto stuInvoiceInfo = (StudentInvoiceDto) session.getAttribute(INVOICE.replace("${StudentId}", studentId));
        BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
        Map<String, String> gradeMap = commonMapService.getGradeMap(employeeInfo.getXxId()); //年级
        Map<String, String> pyccMap = commonMapService.getPyccMap(); //层次

        model.addAttribute("gradeMap",gradeMap);
        model.addAttribute("pyccMap", pyccMap);
        
        model.addAttribute("info", studentInfo);
        model.addAttribute("stuInvoiceInfo", stuInvoiceInfo);
        model.addAttribute("action", "view");
        return "new/class/feeInvoice/view";
    }

    /**
     * 导入发票申请
     * @return
     */
    @RequestMapping(value = "importInvoice", method = RequestMethod.POST)
    public Feedback importInvoice(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        Feedback feedback = new Feedback(true, "导入成功");

        BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
        try {
            List<String> filePaths = UploadTmpFile.uploadSimple(request);
            if(filePaths != null && filePaths.size() > 0) {
                // 单个文件
                Map<String, Object> result = gjtInvoiceService.updateBatchImportFeeInvoice(filePaths.get(0), employeeInfo.getGjtUserAccount().getId());
                feedback = new Feedback(((List)result.get("failList")).size()==0, "导入结果：成功" + ((List)result.get("succList")).size() + "条，失败" + ((List)result.get("failList")).size() + "条");
            } else {
                feedback = new Feedback(false, "没有找到文件");
            }
        } catch (Exception e) {
            feedback = new Feedback(false, "导入失败");
        }
        super.outputJsonData(response, feedback);
        return null;
    }

    /**
     * 导出发票信息模板功能
     */
    @RequestMapping(value = "downloadTemplate", method = RequestMethod.GET)
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String outputUrl = "发票信息模板.xlt";

        HSSFWorkbook workbook = gjtInvoiceService.exportFeeInvoiceTemplate();

        super.downloadExcelFile(response, workbook, outputUrl);
    }

    /**
     * 发放发票
     * @param invoiceNo
     * @param studentId
     * @return
     */
    @RequestMapping(value = "issue", method = RequestMethod.GET)
    public String issue(@RequestParam String invoiceNo, @RequestParam String studentId,
                          HttpSession session, RedirectAttributes redirectAttributes) {
        Feedback feedback = new Feedback(true, "发放成功");

        BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
        try {
            String[] recIds = invoiceNo.split(",");
            //gjtRecResultService.updateRecExamCourse(studentId, recIds, employeeInfo.getGjtUserAccount().getId());
        } catch (Exception e) {
            feedback = new Feedback(false, "发放失败");
        }
        redirectAttributes.addFlashAttribute("feedback", feedback);
        return "redirect:/home/class/feeInvoice/view/" + studentId;
    }

    /**
     * 导出发票信息功能
     */
    @RequestMapping(value = "exportInfo", method = RequestMethod.GET)
    public void exportInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String outputUrl = "发票信息_" + Calendar.getInstance().getTimeInMillis() + ".xls";

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        String classId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID)) ? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);
        HSSFWorkbook workbook = gjtInvoiceService.exportFeeInvoiceToExcel(classId, searchParams, null);

        super.downloadExcelFile(response, workbook, outputUrl);
    }

}
