/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager.student;

import com.ouchgzee.headTeacher.dto.StudentStateDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtGraduateStandard;
import com.ouchgzee.headTeacher.pojo.BzrGjtGraduationStu;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.account.BzrGjtUserAccountService;
import com.ouchgzee.headTeacher.service.graduate.BzrGjtGraduateStandardService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.service.student.BzrGjtTermInfoService;
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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 学员状态控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月16日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/class/studentState")
public class StudentStateController extends BaseController {

    @Autowired
    private BzrGjtStudentService gjtStudentService;

    @Autowired
    private BzrGjtGraduateStandardService gjtGraduateStandardService;

    @Autowired
    private BzrGjtTermInfoService gjtTermInfoService;

    @Autowired
    private BzrGjtUserAccountService gjtUserAccountService;

    @Autowired
    private BzrCommonMapService commonMapService;

    /**
     * 学员状态列表
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
        Page<StudentStateDto> infos = gjtStudentService.queryStudentStateByClassIdPage(classId, searchParams, pageRequst);

        BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
        Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(employeeInfo.getXxId());

        model.addAttribute("infos", infos);
        model.addAttribute("specialtyMap", specialtyMap);
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
        return "new/class/studentState/list";
    }

    /**
     * 浏览学员状态
     * @param studentId
     * @return
     */
    @RequestMapping(value = "view/{studentId}", method = RequestMethod.GET)
    public String viewForm(@PathVariable("studentId") String studentId, Model model,
                           ServletRequest request, HttpSession session) {
        BzrGjtStudentInfo studentInfo = gjtStudentService.queryById(studentId);
        Date firstLogin = gjtUserAccountService.getFirstLoginByLoginAccount(studentInfo.getGjtUserAccount().getLoginAccount());
        // 缴费详情
        Map paymentDetail = gjtStudentService.queryPaymentSituationByStudentId(studentId);
        // 学分要求
        Map<String, Object> searchParams = new HashMap();
        searchParams.put("EQ_specialtyId", studentInfo.getGjtSpecialty().getSpecialtyId());
        searchParams.put("EQ_yxId", studentInfo.getGjtSpecialty().getYxId());
        List<BzrGjtGraduateStandard> graduateStandardList = gjtGraduateStandardService.queryBy(searchParams, null);
        // 毕业信息
        BzrGjtGraduationStu graduationStuInfo = gjtGraduateStandardService.queryGraduationStuByStuId(studentId);
        // 学分
        Map minAndSum = gjtStudentService.getMinAndSum(studentInfo.getXh(), studentInfo.getGjtSpecialty().getSpecialtyId());
        int yxxf = minAndSum.get("YXXF") != null ? ((BigDecimal) minAndSum.get("YXXF")).intValue() : 0;
        // 总学期/已学习
        Object[] termNums = gjtTermInfoService.countByStudentTerm(studentId);

        model.addAttribute("info", studentInfo);
        model.addAttribute("firstLogin", firstLogin);
        model.addAttribute("paymentDetail", paymentDetail);
        model.addAttribute("graduateStandard", (graduateStandardList != null && graduateStandardList.size() > 0) ? graduateStandardList.get(0) : null);
        model.addAttribute("graduationStuInfo", graduationStuInfo);
        model.addAttribute("yxxf", yxxf);
        model.addAttribute("termStudyNum", ((BigDecimal) termNums[1]).intValue());
        model.addAttribute("action", "view");
        return "new/class/studentState/view";
    }

    /**
     * 导出学员状态功能
     */
    @RequestMapping(value = "exportInfo", method = RequestMethod.GET)
    public void exportInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String outputUrl = "学员状态_" + Calendar.getInstance().getTimeInMillis() + ".xls";

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        String classId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID)) ? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);
        HSSFWorkbook workbook = gjtStudentService.exportStudentStateToExcel(classId, searchParams, null);

        super.downloadExcelFile(response, workbook, outputUrl);
    }

}
