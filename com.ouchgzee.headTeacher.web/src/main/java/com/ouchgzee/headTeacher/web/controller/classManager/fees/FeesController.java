package com.ouchgzee.headTeacher.web.controller.classManager.fees;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.fees.BzrFeesService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 缴费管理
 */
@Controller
@RequestMapping("/home/class/fees")
public class FeesController extends BaseController {
	
	@Autowired
    BzrFeesService feesService;

	@Autowired
    private BzrCommonMapService commonMapService;
	
	/**
     * 费用管理列表查询页
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getFeeslist", method = RequestMethod.GET)
    public String getFeeslist(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "page.size", defaultValue = "10") int pageSize,
                       Model model, ServletRequest request, HttpSession session) {
    	
        PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
        String classId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID)) ? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);

        BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
        
        searchParams.put("CLASS_ID", classId);
		Page pageInfo = feesService.getFeeslist(searchParams, pageRequst);
		
		// 专业
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(employeeInfo.getXxId());
		
		// 年级
        Map<String, String> gradeMap = commonMapService.getGradeMap(employeeInfo.getXxId());
        
        model.addAttribute("infos", pageInfo);
        model.addAttribute("gradeMap", gradeMap);
        model.addAttribute("specialtyMap", specialtyMap);
        
        return "new/class/fees/getFeeslist";
    }
    
    /**
     * 费用管理详情
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getFeesView", method = RequestMethod.GET)
    public String getFeesView(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "page.size", defaultValue = "10") int pageSize,
                       Model model, ServletRequest request, HttpSession session) {
    	
        PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
        BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
        
		Page pageInfo = feesService.getFeeslist(searchParams, pageRequst);
		
		Map studentMap = new HashMap();
		if (EmptyUtils.isNotEmpty(pageInfo)) {
			List list = pageInfo.getContent();
			if (EmptyUtils.isNotEmpty(list)) {
				studentMap = (Map)list.get(0);
			}
		}
		
        model.addAttribute("studentMap", studentMap);
        return "new/class/fees/getFeesView";
    }
    
    /**
     * 费用统计
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getFeesStatistics", method = RequestMethod.GET)
    public String getFeesStatistics(Model model, ServletRequest request, HttpSession session) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");

        BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
     
		// 专业
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(employeeInfo.getXxId());
		
		// 年级
        Map<String, String> gradeMap = commonMapService.getGradeMap(employeeInfo.getXxId());
        
        model.addAttribute("gradeMap", gradeMap);
        model.addAttribute("specialtyMap", specialtyMap);
        
        return "new/class/fees/getFeesStatistics";
    }
    
    /**
     * 缴费状态统计
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getPayFeesType", method = RequestMethod.GET)
    public String getPayFeesType(Model model, ServletRequest request, HttpSession session) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
        return "new/class/fees/getPayFeesType";
    }
    
    /**
     * 缴费方式统计
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getPayFeesState", method = RequestMethod.GET)
    public String getPayFeesState(Model model, ServletRequest request, HttpSession session) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
        return "new/class/fees/getPayFeesState";
    }
}
