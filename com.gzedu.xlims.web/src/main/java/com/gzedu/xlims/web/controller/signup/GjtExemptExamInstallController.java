package com.gzedu.xlims.web.controller.signup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtExemptExamInstall;
import com.gzedu.xlims.pojo.GjtExemptExamMaterial;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.transaction.GjtExemptExamInstallService;
import com.gzedu.xlims.service.transaction.GjtExemptExamMaterialService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 功能说明：免修免考设置
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月22日
 * @version 2.5
 */
@Controller
@RequestMapping("/edumanage/exemptExamInstall")
public class GjtExemptExamInstallController {
	private static final Logger log = LoggerFactory.getLogger(GjtExemptExamInstallController.class);
	
	@Autowired
	private GjtExemptExamInstallService gjtExemptExamInstallService;
	
	@Autowired
	private GjtExemptExamMaterialService gjtExemptExamMaterialService;
	
	@Autowired
	private GjtCourseService gjtCourseService;
	
	@Autowired
	private CommonMapService commonMapService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		Page<GjtExemptExamInstall> page = gjtExemptExamInstallService.queryAll(user.getGjtOrg().getId(), searchParams,pageRequst);
		model.addAttribute("pageInfo", page);
		model.addAttribute("exemptExamType", "2");
		return "edumanage/roll/exempt_exam_install_list";		
	}
	/**
	 * 跳转到新增免修免考页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "toAdd", method = RequestMethod.GET)
	public String toAddExemptExamInstall(HttpServletRequest request,Model model){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());//学期
		//查询当前院校下课程性质为正式课程中课程类型为统考课程的信息
//		Map<String, String> courseMap=gjtCourseService.findByXxIdAndCourseNatureAndCourseCategoryAndIsDeleted(user.getGjtOrg().getId(),"1",4,Constants.BOOLEAN_NO);
		List<GjtCourse> courseMap=gjtCourseService.findByXxIdAndCourseNatureAndCourseCategoryAndIsDeleted(user.getGjtOrg().getId(),"1",4,Constants.BOOLEAN_NO);
		Map<String, String> resultMap=null;
		if(courseMap!=null&courseMap.size()>0){
			resultMap = new LinkedHashMap<String, String>();
			for (GjtCourse gjtCourse : courseMap) {
				resultMap.put(gjtCourse.getCourseId(), gjtCourse.getKcmc());
			}
		}
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("courseMap", resultMap);
		return "edumanage/roll/add_exempt_exam";
	}
	/**
	 * 保存免修免考信息
	 * @param item
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public Feedback create(@ModelAttribute GjtExemptExamMaterial item, RedirectAttributes redirectAttributes, HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback();
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		try {						
			String[] materialName = request.getParameterValues("materialName");//材料证明清单名称
			String[] memo=request.getParameterValues("memo");//备注
			String[] isOnlineExam=request.getParameterValues("isOnlineExam");//是否需要参加线上考试
			String[] nj=request.getParameterValues("gradeId");
			String courseId=searchParams.get("EQ_courseId").toString();
			List<String> gradeList=new ArrayList<String>();
			//先判断新增的课程是否已经设置过免修免考，并且适应学期是否相同，如果相同则不允许新增
			List<GjtExemptExamInstall> installList=gjtExemptExamInstallService.findByXxIdAndCourseIdAndIsDeleted(user.getGjtOrg().getId(),courseId,Constants.BOOLEAN_NO);
			if(installList!=null&&installList.size()>0){
				for (GjtExemptExamInstall gjtExemptExamInstall : installList) {
					String gradeId=gjtExemptExamInstall.getGradeId();
					if(EmptyUtils.isEmpty(gradeId)&&EmptyUtils.isEmpty(nj)){						
						feedback = new Feedback(false, "该课程已设置过免修免考，请重新选择！");
						return feedback;
					}else if((EmptyUtils.isEmpty(nj)&&EmptyUtils.isNotEmpty(gradeId))||
							(EmptyUtils.isEmpty(gradeId)&&EmptyUtils.isNotEmpty(nj))){						
						feedback = new Feedback(false, "该课程已设置过免修免考，请重新选择！");
						return feedback;
					}else{
						String[] strGrade=gradeId.split(",");
						for(int i=0;i<strGrade.length;i++){
							gradeList.add(strGrade[i]);
						}
						for(int k=0;k<nj.length;k++){
							String[] strNj=nj[k].split(",");
							for (String str : strNj) {								
								String[] pp=str.split("_");								
								if(gradeList.contains(pp[0])){
									feedback = new Feedback(false, "该课程已设置过免修免考，请重新选择！");
									return feedback;
								}
							}
						}
					}					
				}				
			}
			int result=gjtExemptExamMaterialService.insertMaterial(item,nj,courseId,user,materialName,memo,isOnlineExam);						
			if(result==1){
				feedback = new Feedback(true, "新增成功");
			}else{
				feedback = new Feedback(false, "新增免修免考失败");
			}								
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "数据异常，新增免修免考失败");
		}
		return feedback;
	}
	/**
	 * 设置免修免考状态
	 * @param installId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "setExemptExamStatus/{installId}/{status}", method = RequestMethod.POST)
	public Feedback setExemptExamStatus(@PathVariable("installId") String installId,@PathVariable("status") String status,HttpServletRequest request){
		Feedback feedback = new Feedback();
		try {
			GjtExemptExamInstall install=gjtExemptExamInstallService.findByInstallId(installId);
			install.setStatus(status);
			gjtExemptExamInstallService.saveEntity(install);
			feedback = new Feedback(true, "设置成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "设置失败");
		}						
		return feedback;
	}
	
	/**
	 * 跳转至免修免考详情页面
	 * @param installId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "toViewAndUpdate/{installId}/{action}", method = RequestMethod.GET)
	public String toView(@PathVariable("installId") String installId,@PathVariable("action") String action,Model model,HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<GjtCourse> courseMap=gjtCourseService.findByXxIdAndCourseNatureAndCourseCategoryAndIsDeleted(user.getGjtOrg().getId(),"1",4,Constants.BOOLEAN_NO);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());//学期
		GjtExemptExamInstall install=gjtExemptExamInstallService.findByInstallId(installId);
		List<GjtExemptExamMaterial> materialList=gjtExemptExamMaterialService.findByInstallIdAndIsDeleted(installId,Constants.BOOLEAN_NO);
		Map<String, String> resultMap=null;
		Map<String, String> map=null;
		if(courseMap!=null&courseMap.size()>0){
			resultMap = new LinkedHashMap<String, String>();
			for (GjtCourse gjtCourse : courseMap) {
				resultMap.put(gjtCourse.getCourseId(), gjtCourse.getKcmc());
			}
		}
		String grade=install.getGradeId();
		if(StringUtils.isNotEmpty(grade)){
			String[] gradeId=grade.split(",");
			map=new LinkedHashMap<String, String>();
			for(int i=0;i<gradeId.length;i++){								
				map.put(gradeId[i], gradeId[i]);
			}
		}	
		model.addAttribute("resultMap", map);
		model.addAttribute("install", install);
		model.addAttribute("material", materialList);
		model.addAttribute("courseMap", resultMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("action", action);
		return "edumanage/roll/exempt_exam_install_form";		
	}
	/**
	 * 更新免修免考
	 * @param item
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public Feedback update(@ModelAttribute GjtExemptExamMaterial item,HttpServletRequest request){
		Feedback feedback = new Feedback();
		try {
			Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
			String installId=searchParams.get("EQ_installId").toString();
			//先删除旧的证明材料清单数据
			gjtExemptExamMaterialService.deletedMaterial(installId);
			GjtExemptExamInstall install=gjtExemptExamInstallService.findByInstallId(installId);			
			//新增新的证明清单数据
			String[] materialName = request.getParameterValues("materialName");//材料证明清单名称
			String[] memo=request.getParameterValues("memo");//备注
			String[] isOnlineExam=request.getParameterValues("isOnlineExam");//是否需要参加线上考试
			if(materialName!=null){
				int result=gjtExemptExamMaterialService.insert(item,installId,materialName,memo,isOnlineExam);	
				if(result==1){
					install.setMaterial(String.valueOf(materialName.length));
					gjtExemptExamInstallService.saveEntity(install);
					feedback = new Feedback(true, "更新成功");
				}else{
					feedback = new Feedback(false, "更新失败");
				}
			}else{
				feedback = new Feedback(false, "请填写材料证明清单！");
				return feedback;
			}			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "数据异常，更新免修免考失败");
		}		
		return feedback;
	}
	
	/**
	 * 删除免修免考信息
	 * @param installId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delete/{installId}", method = RequestMethod.POST)
	public Feedback delete(@PathVariable("installId") String installId,HttpServletRequest request){
		Feedback feedback = new Feedback();
		try {
			int result=gjtExemptExamInstallService.deleteInstall(installId);
			if(result==1){
				feedback = new Feedback(true, "删除成功");
			}else{
				feedback = new Feedback(false, "删除失败");
			}			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "数据异常");
		}						
		return feedback;
	} 
}
