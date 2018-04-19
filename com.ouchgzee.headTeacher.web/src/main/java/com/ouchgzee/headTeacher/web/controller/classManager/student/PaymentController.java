/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager.student;

import com.ouchgzee.headTeacher.dto.StudentPaymentDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
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
import java.util.Map;

/**
 * 缴费管理控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月24日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/class/payment")
public class PaymentController extends BaseController {

    @Autowired
    private BzrGjtStudentService gjtStudentService;

    private static String PAYMENT = "Payment_${StudentId}";

    /**
     * 缴费信息列表
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
        Page<StudentPaymentDto> infos = gjtStudentService.queryPaymentSituationByClassIdPage(classId, searchParams, pageRequst);

        for (Iterator<StudentPaymentDto> iter = infos.iterator(); iter.hasNext();) {
            StudentPaymentDto info = iter.next();

            session.setAttribute(PAYMENT.replace("${StudentId}", info.getStudentId()), info);
        }
        model.addAttribute("infos", infos);
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
        return "new/class/payment/list";
    }

    /**
     * 浏览缴费信息
     * @param studentId
     * @return
     */
    @RequestMapping(value = "view/{studentId}", method = RequestMethod.GET)
    public String viewForm(@PathVariable("studentId") String studentId, Model model,
                           ServletRequest request, HttpSession session) {
        BzrGjtStudentInfo studentInfo = gjtStudentService.queryById(studentId);
        // 缴费详情
        StudentPaymentDto stuPaymentInfo = (StudentPaymentDto) session.getAttribute(PAYMENT.replace("${StudentId}", studentId));

        model.addAttribute("info", studentInfo);
        model.addAttribute("stuPaymentInfo", stuPaymentInfo);
        model.addAttribute("action", "view");
        return "new/class/payment/view";
    }

    /**
     * 变更学员学习状态
     * @param studentId
     * @param learningState
     * @return
     */
    @RequestMapping(value = "changeLearningState", method = RequestMethod.GET)
    public String issue(@RequestParam String studentId, @RequestParam Integer learningState,
                          HttpSession session, RedirectAttributes redirectAttributes) {
        Feedback feedback = new Feedback(true, "变更成功");

        // GjtEmployeeInfo employeeInfo = super.getUser(session);
        try {
            gjtStudentService.updateChangePaymentState(studentId, learningState);
        } catch (Exception e) {
            feedback = new Feedback(false, "变更失败");
        }
        redirectAttributes.addFlashAttribute("feedback", feedback);
        return "redirect:/home/class/payment/list";
    }

    /**
     * 导出缴费信息功能
     */
    @RequestMapping(value = "exportInfo", method = RequestMethod.GET)
    public void exportInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String outputUrl = "缴费信息_" + Calendar.getInstance().getTimeInMillis() + ".xls";

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        String classId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID)) ? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);
        HSSFWorkbook workbook = gjtStudentService.exportPaymentSituationToExcel(classId, searchParams, null);

        super.downloadExcelFile(response, workbook, outputUrl);
    }

}
