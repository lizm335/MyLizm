/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.recruitmanage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtEnrollBatchNew;
import com.gzedu.xlims.pojo.GjtStudyEnrollNum;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.recruitmanage.GjtEnrollBatchNewService;
import com.gzedu.xlims.service.recruitmanage.GjtEnrollBatchService;
import com.gzedu.xlims.service.recruitmanage.GjtStudyEnrollNumService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;


@Controller
@RequestMapping("/recruitmanage/recruitstudent")
public class GjtEnrollBatchNewController {

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtEnrollBatchService gjtEnrollBatchService;
	
	@Autowired
	GjtEnrollBatchNewService gjtEnrollBatchNewService;
	
	@Autowired
	GjtStudyEnrollNumService gjtStudyEnrollNumService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		String symbol=request.getParameter("year");//招生完成度符号
		String yearDegree=request.getParameter("yearDegree");//招生完成度
		List enrollBatchNewList=null;
		if(symbol!=null&&symbol!=""&&yearDegree!=null&&yearDegree!=""){
			enrollBatchNewList=gjtEnrollBatchNewService.queryEnrollBatchNew(user.getGjtOrg().getId(),yearDegree,symbol);
		}
		Page<GjtEnrollBatchNew> page = gjtEnrollBatchNewService.queryAll(user.getGjtOrg().getId(),enrollBatchNewList,format.format(new Date()),searchParams, pageRequst);
		Map<String,String> specialtyMap=commonMapService.getSpecialtyMap(user.getGjtOrg().getId());//专业列表
		Map<String,String> pyccMap=commonMapService.getPyccMap();//培养层次
		Map<String,String> studyYearMap=commonMapService.getStudyYearMap(user.getGjtOrg().getId());//学年度
		long hasRelease=gjtEnrollBatchNewService.queryStatusTotalNum(user.getGjtOrg().getId(), "2");//状态为【已发布】的数量
		long edit=gjtEnrollBatchNewService.queryStatusTotalNum(user.getGjtOrg().getId(), "3");//状态为【编辑中】的数量
		//状态为【已结束】的总数量
		
		long endTotalNum=gjtEnrollBatchNewService.queryDateTotalNum(user.getGjtOrg().getId(), format.format(new Date()));
		model.addAttribute("studyYearMap", studyYearMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("hasRelease", hasRelease);
		model.addAttribute("edit", edit);
		model.addAttribute("endTotalNum", endTotalNum);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("pageInfo", page);				
		return "recruitmanage/recruitstudent/list";
	}
	/**
	 * 查看详情
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtEnrollBatchNew gjtEnrollBatchNew = gjtEnrollBatchNewService.queryById(id);
		Map<String,String> specialtyMap=commonMapService.getSpecialtyMap(user.getGjtOrg().getId());//专业列表
		Map<String,String> pyccMap=commonMapService.getPyccMap();//培养层次
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 院校下的学习中心
		Map<String,String> studyYearMap=commonMapService.getStudyYearMap(user.getGjtOrg().getId());//学年度
		List<GjtStudyEnrollNum> studyEnrollNumList=gjtEnrollBatchNew.getGjtStudyEnrollNums();//查询各学习中心招生人数总和
		int kpiNums=0;
		for(int i=0;i<studyEnrollNumList.size();i++){
			GjtStudyEnrollNum  GjtStudyEnrollNum=studyEnrollNumList.get(i);
			String kpiNum=GjtStudyEnrollNum.getEnrollKpiNum();
			kpiNums=kpiNums+Integer.parseInt(kpiNum);
		}
		model.addAttribute("kpiNums", kpiNums);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("studyYearMap", studyYearMap);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("item", gjtEnrollBatchNew);
		model.addAttribute("action", "view");
		return "recruitmanage/recruitstudent/form";
	}
	/**
	 * 跳转至新增招生计划页面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("item", new GjtEnrollBatchNew());//招生批次实体对象
		model.addAttribute("studyEnrollNum", new GjtStudyEnrollNum());//学习中心下的招生人数对象
		Map<String,String> specialtyMap=commonMapService.getSpecialtyMap(user.getGjtOrg().getId());//专业列表
		Map<String,String> pyccMap=commonMapService.getPyccMap();//培养层次
		Map<String,String> studyYearMap=commonMapService.getStudyYearMap(user.getGjtOrg().getId());//学年度
//		Map<String,String> studyCenterMap=commonMapService.getStudyCenterMap(user.getGjtOrg().getId());//院校下的学习中心
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 院校下的学习中心
		
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("studyYearMap", studyYearMap);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("action", "create");
		return "recruitmanage/recruitstudent/form";
	}
	/**
	 * 获取院校下的学习中心
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "queryStudyCenter",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryStudyCenter(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 院校下的学习中心
		map.put("schoolInfoMap", schoolInfoMap);
		return map;
	}
	/**
	 * 新增
	 * @param item
	 * @param redirectAttributes
	 * @param request
	 * @param studyEnrollNum
	 * @return
	 */
	@RequestMapping(value = "create")
	public String create(@ModelAttribute GjtEnrollBatchNew item, RedirectAttributes redirectAttributes, HttpServletRequest request,
			@ModelAttribute GjtStudyEnrollNum studyEnrollNum) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback();
		try {
			String[] orgIds = request.getParameterValues("orgId");//学习中心ID
			String[] enrollNum=request.getParameterValues("kpiNum");//学习中心招标人数
			String sdate = request.getParameter("sdate");
			String edate = request.getParameter("edate");		
			if(orgIds!=null&&enrollNum!=null){
				String enrollBatchId=UUIDUtils.random();//招生批次ID
				item.setEnrollBatchId(enrollBatchId);
				if (StringUtils.isNotBlank(sdate)) {
					item.setEnrollSdate(DateUtils.getNowTime(sdate));
				}
				if (StringUtils.isNotBlank(edate)) {
					item.setEnrollEdate(DateUtils.getNowTime(edate));
				}
				item.setCreatedBy(user.getRealName());
				item.setCreatedDt(DateUtils.getNowTime());
				item.setOrgId(user.getGjtOrg().getId());
				item.setIsDeleted("N");
				item.setStatus("3");//1、已上架；2、已发布；3、编辑中；4、已结束
				gjtEnrollBatchNewService.insert(item);//插入批次表
				for(int i=0;i<orgIds.length;i++){
					String xxzxId=orgIds[i];//学习中心ID	
					String kpiNum=enrollNum[i];//招生指标
					studyEnrollNum.setId(UUIDUtils.random());//自增ID
					studyEnrollNum.setEnrollBatchId(enrollBatchId);
					studyEnrollNum.setXxzxId(xxzxId);
					studyEnrollNum.setEnrollKpiNum(kpiNum);
					studyEnrollNum.setCreatedBy(user.getRealName());
					studyEnrollNum.setCreatedDt(DateUtils.getNowTime());
					studyEnrollNum.setIsDeleted("N");;
					gjtStudyEnrollNumService.insert(studyEnrollNum);			
				}
				feedback = new Feedback(true, "新增成功");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "新增失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/recruitmanage/recruitstudent/list";
	}
	/**
	 * 跳转到编辑页面
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtEnrollBatchNew gjtEnrollBatchNew = gjtEnrollBatchNewService.queryById(id);
		Map<String,String> specialtyMap=commonMapService.getSpecialtyMap(user.getGjtOrg().getId());//专业列表
		Map<String,String> pyccMap=commonMapService.getPyccMap();//培养层次
		Map<String,String> studyYearMap=commonMapService.getStudyYearMap(user.getGjtOrg().getId());//学年度
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 院校下的学习中心
		List<GjtStudyEnrollNum> studyEnrollNumList=gjtEnrollBatchNew.getGjtStudyEnrollNums();//查询各学习中心招生人数总和
		int kpiNums=0;
		for(int i=0;i<studyEnrollNumList.size();i++){
			GjtStudyEnrollNum  GjtStudyEnrollNum=studyEnrollNumList.get(i);
			String kpiNum=GjtStudyEnrollNum.getEnrollKpiNum();
			kpiNums=kpiNums+Integer.parseInt(kpiNum);
		}
		model.addAttribute("kpiNums", kpiNums);
		model.addAttribute("item", gjtEnrollBatchNew);//招生批次实体对象
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("studyYearMap", studyYearMap);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("action", "update");
		return "recruitmanage/recruitstudent/form";
	}
	/**
	 * 编辑
	 * @param item
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(GjtEnrollBatchNew item,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);		 
		GjtEnrollBatchNew gjtEnrollBatchNew = gjtEnrollBatchNewService.queryById(item.getEnrollBatchId());
		try {
			String sdate = request.getParameter("sdate");
			String edate = request.getParameter("edate");
			gjtEnrollBatchNew.setUpdatedBy(user.getRealName());
			gjtEnrollBatchNew.setUpdatedDt(DateUtils.getNowTime());
			gjtEnrollBatchNew.setEnrollTotalNum(item.getEnrollTotalNum());
			gjtEnrollBatchNew.setUrl(item.getUrl());
			gjtEnrollBatchNew.setFileName(item.getFileName());
			if (StringUtils.isNotBlank(sdate)) {//招生开始时间
				gjtEnrollBatchNew.setEnrollSdate(DateUtils.getNowTime(sdate));
			}
			if (StringUtils.isNotBlank(edate)) {//招生结束时间
				gjtEnrollBatchNew.setEnrollEdate(DateUtils.getNowTime(edate));
			}
			int status=gjtEnrollBatchNewService.update(gjtEnrollBatchNew);//更新招生批次表
			String[] orgIds = request.getParameterValues("orgId");//学习中心ID
			String[] kpiNum=request.getParameterValues("kpiNum");//学习中心招标人数;
			if(status>0){
				for(int i=0;i<orgIds.length;i++){
				  //查询同一招生批次下的学习学习中心的招生数量
				  GjtStudyEnrollNum studyEnrollNum=gjtStudyEnrollNumService.queryByIdOrEnrId(item.getEnrollBatchId(),orgIds[i]);
				  if(studyEnrollNum!=null){
					  studyEnrollNum.setUpdatedDt(DateUtils.getNowTime());
					  studyEnrollNum.setUpdatedBy(user.getRealName());
					  studyEnrollNum.setEnrollKpiNum(kpiNum[i]);
					  gjtStudyEnrollNumService.update(studyEnrollNum);  
				  }else{
					  GjtStudyEnrollNum studyNum=new GjtStudyEnrollNum();
					  studyNum.setId(UUIDUtils.random());
					  studyNum.setEnrollBatchId(item.getEnrollBatchId());
					  studyNum.setXxzxId(orgIds[i]);
					  studyNum.setEnrollKpiNum(kpiNum[i]);
					  studyNum.setCreatedBy(user.getRealName());
					  studyNum.setCreatedDt(DateUtils.getNowTime());
					  studyNum.setIsDeleted("N");;
					  gjtStudyEnrollNumService.insert(studyNum);
				  }				  
				}
			feedback = new Feedback(false, "更新成功");
			}			
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/recruitmanage/recruitstudent/list";
	}
	/**
	 * 删除招生计划
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids) throws IOException {
//		if (StringUtils.isNotBlank(ids)) {
//			String[] selectedIds = ids.split(",");
			try {
				//1、先删除招生计划表中的数据
				gjtEnrollBatchNewService.delete(ids);
				//2、再删除对应的学习中心招生人数的数据
				gjtStudyEnrollNumService.delete(ids);
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
//		}
		return new Feedback(true, "删除成功");
	}
	/**
	 * 发布招生计划
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "updateStatus")
	@ResponseBody
	public Feedback updateStatus(String ids){		
		try {
			gjtEnrollBatchNewService.updateStatus(ids);
		} catch (Exception e) {
			return new Feedback(false, "发布失败");
		}
		return new Feedback(true, "发布成功");		
	}
	/**
	 * 删除附件
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "deleteFile")
	@ResponseBody
	public Feedback deleteFile(String enrollBatchId){		
		try {
			gjtEnrollBatchNewService.deleteFile(enrollBatchId);
		} catch (Exception e) {
			return new Feedback(false, "删除附件失败");
		}
		return new Feedback(true, "删除附件成功");		
	}
}
