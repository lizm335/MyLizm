package com.gzedu.xlims.web.controller.exam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.exam.GjtExamAppointmentNewDao;
import com.gzedu.xlims.dao.exam.GjtExamBatchNewDao;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamPointNew;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtAreaService;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.exam.GjtExamPointNewService;
import com.gzedu.xlims.service.exam.GjtExamRoomNewService;
import com.gzedu.xlims.service.exam.GjtPointExamNewService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/exam/new/point")
public class GjtExamPointNewController {

	@Autowired
	private GjtExamPointNewService gjtExamPointNewService;

	@Autowired
	GjtExamBatchNewDao gjtExamBatchNewDao;

	@Autowired
	CodeGeneratorService codeGeneratorService;
	@Autowired
	GjtExamAppointmentNewDao gjtExamAppointmentNewDao;

	@Autowired
	GjtAreaService gjtAreaService;
	
	@Autowired
	GjtPointExamNewService gjtPointExamNewService;
	
	@Autowired
	GjtExamRoomNewService gjtExamRoomNewService;

	@Autowired
	private CommonMapService commonMapService;

	/**
	 * 考点列表（基础数据）
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="baseList")
	public String baseList(@RequestParam(value="page",defaultValue = "1") int pageNumber,
						   @RequestParam(value = "page.size", defaultValue = "100") int pageSize,
						   Model model,HttpServletRequest request,HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");

		searchParams.put("EXAM_BATCH_ID", "null");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page<Map<String, Object>> pageInfo = gjtPointExamNewService.getExamPointList(searchParams, pageRequest);
		for (Iterator<Map<String, Object>> iter = pageInfo.iterator(); iter.hasNext();) {
			Map<String, Object> info = iter.next();
			GjtExamPointNew examPoint = gjtPointExamNewService.queryById((String) info.get("EXAM_POINT_ID"));
			info.put("gjtStudyCenters", examPoint.getGjtStudyCenters());
		}

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("EXAM_TYPE", searchParams.get("EXAM_TYPE"));
		return "edumanage/exam/exam_point_listInfo_base";
	}

	/**
	 * 考点管理
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="list")
	public String getExamPointList(@RequestParam(value="page",defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "100") int pageSize, 
			Model model,HttpServletRequest request,HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");

		// 默认选择当前期(批次)
		if(EmptyUtils.isEmpty(searchParams)|| StringUtils.isBlank((String) searchParams.get("EXAM_BATCH_ID"))){
			String id = commonMapService.getCurrentGjtExamBatchNewId(user.getGjtOrg().getId());
			searchParams.put("EXAM_BATCH_ID", id);
			model.addAttribute("examBatchId",id);
		}else if(EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_ID")) ){
			model.addAttribute("examBatchId", ObjectUtils.toString(searchParams.get("EXAM_BATCH_ID")));
		}
		if(EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EXAM_TYPE"))){
			searchParams.put("EXAM_TYPE", 8);
			model.addAttribute("EXAM_TYPE", 8);
		}
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page<Map<String, Object>> pageInfo = gjtPointExamNewService.getExamPointList(searchParams, pageRequest);
		for (Iterator<Map<String, Object>> iter = pageInfo.iterator(); iter.hasNext();) {
			Map<String, Object> info = iter.next();
			GjtExamPointNew examPoint = gjtPointExamNewService.queryById((String) info.get("EXAM_POINT_ID"));
			info.put("gjtStudyCenters", examPoint.getGjtStudyCenters());
		}

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("EXAM_TYPE", searchParams.get("EXAM_TYPE"));
		Map<String, Object> batchMap = gjtExamRoomNewService.getExamBatchInfo(searchParams);
		model.addAttribute("batchMap", batchMap.get("BATCHLIST"));

		return "edumanage/exam/exam_point_listInfo";

	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtExamPointNew gjtExamPointNew = new GjtExamPointNew();
		gjtExamPointNew.setCode("KD" + codeGeneratorService.codeGenerator(CacheConstants.EXAM_POINT_CODE));
		model.addAttribute("entity", gjtExamPointNew);
		model.addAttribute("studyCenterMap", commonMapService.getStudyCenterMap(user.getGjtOrg().getId())); // 学习中心
		model.addAttribute("action", "create");
		return "edumanage/exam/exam_point_form";
	}

	@RequestMapping(value = "existsCode")
	public @ResponseBody Feedback existsCode(String code, ServletResponse response) throws IOException {
		if (StringUtils.isNotBlank(code)) {
			try {
				boolean exists = gjtExamPointNewService.existsCode(code);
				if (exists) {
					return new Feedback(exists, "已存在");
				} else {
					return new Feedback(exists, "不存在");
				}
			} catch (Exception e) {
				return new Feedback(false, "服务器正忙");
			}
		}
		return new Feedback(false, "服务器正忙");
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtExamPointNew entity, String district, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "创建成功");
		String areaId = district;
		entity.setAreaId(areaId);
		entity.setXxId(user.getGjtOrg().getId());

		try {
			String[] studyCenterIds = request.getParameterValues("studyCenterIds");
			if (studyCenterIds != null && studyCenterIds.length > 0) {
				List<GjtStudyCenter> list = new ArrayList<GjtStudyCenter>();
				for (int i = 0; i < studyCenterIds.length; i++) {
					list.add(new GjtStudyCenter(studyCenterIds[i]));
				}
				entity.setGjtStudyCenters(list);
			} else {
				entity.setGjtStudyCenters(null);
			}
			
			gjtExamPointNewService.save(entity);
			gjtExamPointNewService.insertExamPointStudyCenter(entity.getExamPointId());
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/exam/new/point/baseList";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtExamPointNew gjtExamPointNew = gjtExamPointNewService.queryById(id);

		model.addAttribute("entity", gjtExamPointNew);
		model.addAttribute("studyCenterMap", commonMapService.getStudyCenterMap(user.getGjtOrg().getId())); // 学习中心
		model.addAttribute("action", "update");
		return "edumanage/exam/exam_point_form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid GjtExamPointNew entity, String district, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "更新成功");
		String areaId = district;
		entity.setAreaId(areaId);

		GjtExamPointNew examPoint = gjtExamPointNewService.queryById(entity.getExamPointId());
		try {
			if(StringUtils.isNotBlank(entity.getExamType())) examPoint.setExamType(entity.getExamType());
			examPoint.setExamBatchId(entity.getExamBatchId());
			examPoint.setName(entity.getName());
			examPoint.setAreaId(entity.getAreaId());
			examPoint.setAddress(entity.getAddress());
			examPoint.setLinkMan(entity.getLinkMan());
			examPoint.setLinkTel(entity.getLinkTel());
			examPoint.setUpdatedBy(user.getId());
			
			String[] studyCenterIds = request.getParameterValues("studyCenterIds");
			if (studyCenterIds != null && studyCenterIds.length > 0) {
				List<GjtStudyCenter> list = new ArrayList<GjtStudyCenter>();
				for (int i = 0; i < studyCenterIds.length; i++) {
					list.add(new GjtStudyCenter(studyCenterIds[i]));
				}
				examPoint.setGjtStudyCenters(list);
			} else {
				examPoint.setGjtStudyCenters(null);
			}

			gjtExamPointNewService.save(examPoint);
			gjtExamPointNewService.insertExamPointStudyCenter(examPoint.getExamPointId());
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return StringUtils.isNotBlank(entity.getExamBatchId()) ? "redirect:/exam/new/point/list?EXAM_TYPE="+examPoint.getExamType()+"" : "redirect:/exam/new/point/baseList";
	}

	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids, ServletResponse response) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			try {
				gjtExamPointNewService.delete(Arrays.asList(ids.split(",")));
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}
	
	/**
	 * 启用/停用考点
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/examPointStatus",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> examPointStatus(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		
		if("1".equals(ObjectUtils.toString(searchParams.get("IS_ENABLED")))){
			searchParams.put("IS_ENABLED", "0");
		}else if("0".equals(ObjectUtils.toString(searchParams.get("IS_ENABLED")))){
			searchParams.put("IS_ENABLED", "1");
		}
		Map<String, Object> resultMap = gjtPointExamNewService.examPointStatus(searchParams);
		
		return resultMap;
	}
	
	/**
	 * 删除考点
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/delExamPoint",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delExamPoint(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		Map<String, Object> resultMap = gjtPointExamNewService.delExamPoint(searchParams);
		
		return resultMap;
	}

	/**
	 * 考点列表（基础数据）【多选】
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "choiceMultiPointList")
	public String choiceMultiPointList(@RequestParam(value="page",defaultValue = "1") int pageNumber,
			   @RequestParam(value = "page.size", defaultValue = "100") int pageSize,
			   @RequestParam("examBatchId") String examBatchId,
			   Model model,HttpServletRequest request,HttpSession session) throws Exception{
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		
		searchParams.put("EXAM_BATCH_ID", "null");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page<Map<String, Object>> pageInfo = gjtPointExamNewService.getExamPointList(searchParams, pageRequest);
		for (Iterator<Map<String, Object>> iter = pageInfo.iterator(); iter.hasNext();) {
		Map<String, Object> info = iter.next();
		GjtExamPointNew examPoint = gjtPointExamNewService.queryById((String) info.get("EXAM_POINT_ID"));
		info.put("gjtStudyCenters", examPoint.getGjtStudyCenters());
		}
		
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("EXAM_TYPE", searchParams.get("EXAM_TYPE"));
		Map<String, Object> batchMap = gjtExamRoomNewService.getExamBatchInfo(searchParams);
		model.addAttribute("batchMap", batchMap.get("BATCHLIST"));
		return "edumanage/exam/exam_point_listInfo_choice";
	}

	/**
	 * 批量新增考点
	 * @param examBatchId
	 * @param pointIds
	 * @return
	 */
	@RequestMapping(value = "batchAddPoint")
	@ResponseBody
	public Feedback batchAddPoint(@RequestParam("examBatchId") String examBatchId,
			@RequestParam("pointIds[]") List<String> pointIds, ServletResponse response) {
		try {
			boolean flag = gjtExamPointNewService.batchAddPoint(examBatchId, pointIds);
			if(flag) {
				return new Feedback(true, "新增成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Feedback(false, "新增失败");
	}
	
}
