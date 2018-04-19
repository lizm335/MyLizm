/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.graduation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtSpecialtyBase;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.DegreeCollegeSpecialtyDto;
import com.gzedu.xlims.pojo.graduation.GjtDegreeCollege;
import com.gzedu.xlims.pojo.graduation.GjtDegreeRequirement;
import com.gzedu.xlims.pojo.graduation.GjtSpecialtyDegreeCollege;
import com.gzedu.xlims.pojo.status.DegreeReqOperatorEnum;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.graduation.GjtDegreeCollegeService;
import com.gzedu.xlims.service.organization.GjtSpecialtyBaseService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

import net.sf.json.JSONObject;

/**
 * 学位院校管理控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @CreateDate 2016年11月21日
 * @EditDate 2016年11月21日
 * @version 2.5
 */
@Controller
@RequestMapping("/graduation/degreeCollege")
public class GjtDegreeCollegeController extends BaseController {

	@Autowired
	private GjtDegreeCollegeService gjtDegreeCollegeService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtSpecialtyBaseService gjtSpecialtyBaseService;

	/**
	 * 学位院校列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		Page<GjtDegreeCollege> list = gjtDegreeCollegeService.queryByPage(searchParams, pageRequst);
		model.addAttribute("pageInfo", list);
		searchParams.put("EQ_status", 0);
		model.addAttribute("enable", gjtDegreeCollegeService.count(searchParams));
		searchParams.put("EQ_status", 1);
		model.addAttribute("disable", gjtDegreeCollegeService.count(searchParams));
		return "graduation/degree/college_list";
	}

	@ResponseBody
	@RequestMapping(value = "changeStatus", method = RequestMethod.GET)
	public Feedback changeStatus(String collegeId, int status, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, null);
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtDegreeCollege gjtDegreeCollege = gjtDegreeCollegeService.queryById(collegeId);
			if (gjtDegreeCollege != null) {
				gjtDegreeCollege.setStatus(status);
				gjtDegreeCollege.setUpdatedBy(user.getId());
				gjtDegreeCollege.setUpdatedDt(new Date());
				gjtDegreeCollegeService.save(gjtDegreeCollege);
			}
		} catch (Exception e) {
			feedback.setSuccessful(false);
			feedback.setMessage("系统异常");
		}
		return feedback;
	}

	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public Feedback delete(String collegeId, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, null);
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtDegreeCollege gjtDegreeCollege = gjtDegreeCollegeService.queryById(collegeId);
			if (gjtDegreeCollege != null) {
				gjtDegreeCollege.setIsDeleted("Y");
				gjtDegreeCollege.setUpdatedBy(user.getId());
				gjtDegreeCollege.setUpdatedDt(new Date());
				gjtDegreeCollegeService.save(gjtDegreeCollege);
			}
		} catch (Exception e) {
			feedback.setSuccessful(false);
			feedback.setMessage("系统异常");
		}
		return feedback;
	}

	/**
	 * 查看学位院校详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId()); // 专业

		GjtDegreeCollege info = gjtDegreeCollegeService.queryById(id);

		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("info", info);
		model.addAttribute("action", "view");
		return "graduation/degree/college_form";
	}

	/**
	 * 加载新增学位院校
	 * 
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		model.addAttribute("info", new GjtDegreeCollege());
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("action", "create");
		return "graduation/degree/college_form";
	}

	/**
	 * 新增学位院校
	 * 
	 * @param info
	 * @return
	 */
	@SysLog("新增学位院校")
	@ResponseBody
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public Feedback create(@Valid GjtDegreeCollege info, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {

			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			info.setCollegeId(UUIDUtils.random());
			info.setOrgId(user.getGjtOrg().getId());
			info.setCreatedBy(user.getId());
			info.setCreatedDt(new Date());
			gjtDegreeCollegeService.insert(info);
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		return feedback;
	}

	/**
	 * 加载编辑学位条件基础信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "update/{baseId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("baseId") String baseId, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtDegreeCollege info = gjtDegreeCollegeService.queryById(baseId);
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId()); // 专业

		model.addAttribute("info", info);
		model.addAttribute("action", "update");
		return "graduation/degree/college_form";
	}

	/**
	 * 修改学位条件基础信息
	 * 
	 * @param info
	 * @return
	 */
	@SysLog("修改学位院校")
	@ResponseBody
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public Feedback update(@Valid GjtDegreeCollege info, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "修改成功");
		try {

			GjtDegreeCollege modifyInfo = gjtDegreeCollegeService.queryById(info.getCollegeId());
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			modifyInfo.setCollegeName(info.getCollegeName());
			modifyInfo.setCover(info.getCover());
			modifyInfo.setUpdatedBy(user.getId());
			modifyInfo.setUpdatedDt(new Date());
			gjtDegreeCollegeService.save(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "修改失败");
		}
		return feedback;
	}

	// /**
	// * 删除学位院校
	// * @param ids
	// * @return
	// */
	// @RequestMapping(value = "delete")
	// @ResponseBody
	// public Feedback delete(String ids) {
	// if (StringUtils.isNotBlank(ids)) {
	// String[] selectedIds = ids.split(",");
	// try {
	// gjtDegreeCollegeService.deleteInBatch(selectedIds);
	// return new Feedback(true, "删除成功");
	// } catch (Exception e) {
	// return new Feedback(false, "删除失败");
	// }
	// }
	// return new Feedback(false, "删除失败");
	// }

	// --------------------------------------- 学位院校/学位专业 华丽丽的分割线 牛不牛掰
	// ---------------------------------------//

	/**
	 * 学位专业列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "specialtyList", method = RequestMethod.GET)
	public String specialtyList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<DegreeCollegeSpecialtyDto> list = gjtDegreeCollegeService.queryDegreeCollegeSpecialtyByPage(user.getGjtOrg().getId(), searchParams,
				pageRequst);

		Map<String, String> specialtyMap = commonMapService.getSpecialtyBaseMap(user.getGjtOrg().getId()); // 专业

		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pageInfo", list);
		searchParams.remove("isEnabled");
		searchParams.put("notSet", "1");
		model.addAttribute("notSet", gjtDegreeCollegeService.countDegreeCollegeSpecialty(user.getGjtOrg().getId(), searchParams));
		searchParams.remove("notSet");

		searchParams.put("isEnabled", 0);
		model.addAttribute("enable", gjtDegreeCollegeService.countDegreeCollegeSpecialty(user.getGjtOrg().getId(), searchParams));

		searchParams.put("isEnabled", 1);
		model.addAttribute("disable", gjtDegreeCollegeService.countDegreeCollegeSpecialty(user.getGjtOrg().getId(), searchParams));
		return "graduation/degree/college_specialty_list";
	}

	/**
	 * 查看学位条件详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "viewDegreeReq/{collegeSpecialtyId}", method = RequestMethod.GET)
	public String viewDegreeReq(@PathVariable("collegeSpecialtyId") String collegeSpecialtyId, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtSpecialtyDegreeCollege specialytCollge = gjtDegreeCollegeService.querySpecialtyDegreeCollegeById(collegeSpecialtyId);
		List<GjtDegreeRequirement> requirements = gjtDegreeCollegeService.queryReqByCollegeSpecialtyId(collegeSpecialtyId);

		model.addAttribute("info", specialytCollge);
		model.addAttribute("baseTypeMap", EnumUtil.getDegreeRequirementTypeMap());
		model.addAttribute("operatorEnum", DegreeReqOperatorEnum.values());
		model.addAttribute("requirements", requirements);
		model.addAttribute("gradeMap", commonMapService.getGradeMap(user.getGjtOrg().getId()));// 年级
		model.addAttribute("action", "view");
		return "graduation/degree/college_specialty_form";
	}

	/**
	 * 加载编辑学位条件
	 * 
	 * @param collegeId
	 * @param specialtyId
	 * @return
	 */
	@RequestMapping(value = "updateDegreeReq/{collegeSpecialtyId}", method = RequestMethod.GET)
	public String updateDegreeReq(@PathVariable("collegeSpecialtyId") String collegeSpecialtyId, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtSpecialtyDegreeCollege specialytCollge = gjtDegreeCollegeService.querySpecialtyDegreeCollegeById(collegeSpecialtyId);
		List<GjtDegreeRequirement> requirements = gjtDegreeCollegeService.queryReqByCollegeSpecialtyId(collegeSpecialtyId);

		model.addAttribute("info", specialytCollge);
		model.addAttribute("baseTypeMap", EnumUtil.getDegreeRequirementTypeMap());
		model.addAttribute("operatorEnum", DegreeReqOperatorEnum.values());
		model.addAttribute("requirements", requirements);
		model.addAttribute("gradeMap", commonMapService.getGradeMap(user.getGjtOrg().getId()));// 年级
		model.addAttribute("action", "update");
		return "graduation/degree/college_specialty_form";
	}

	/**
	 * 修改学位条件
	 * 
	 * @param collegeId
	 * @param specialtyId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateDegreeReq", method = RequestMethod.POST)
	public Feedback updateDegreeReq(@RequestParam() String collegeSpecialtyId, String gradeId, String degreeName,
			String reqName, @RequestParam("requirements[]") List<String> requirements, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "修改成功");
		try {
			Date now = new Date();
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			List<GjtDegreeRequirement> entityList = new ArrayList<GjtDegreeRequirement>();
			GjtSpecialtyDegreeCollege specialytCollge = gjtDegreeCollegeService.querySpecialtyDegreeCollegeById(collegeSpecialtyId);
			specialytCollge.setGradeId(gradeId);
			specialytCollge.setDegreeName(degreeName);
			gjtDegreeCollegeService.saveCollegeSpecialty(specialytCollge);
			for (String r : requirements) {
				JSONObject json = JSONObject.fromObject(r);
				GjtDegreeRequirement entity = null;
				if (null != json.get("reqId")) {
					entity = gjtDegreeCollegeService.queryGjtDegreeRequirementById(json.getString("reqId"));
				} else {
					entity = new GjtDegreeRequirement();
					entity.setRequirementId(UUIDUtils.random());
					entity.setCollegeId(specialytCollge.getGjtDegreeCollege().getCollegeId());
					entity.setSpecialtyId(specialytCollge.getGjtSpecialty().getSpecialtyBaseId());
					entity.setCollegeSpecialtyId(specialytCollge.getId());
					entity.setCreatedBy(user.getId());
					entity.setCreatedDt(now);
				}
				if (json.get("operator") != null) {
					entity.setOperator(json.getInt("operator"));
					entity.setRequirementParam(Float.valueOf(json.getString("reqParam")));
				}
				entity.setRequirementName(json.getString("reqName"));
				entity.setRequirementType(json.getInt("reqType"));
				entity.setRequirementDesc(json.getString("reqDesc"));
				entityList.add(entity);
			}

			gjtDegreeCollegeService.updateGjtDegreeRequirement(entityList);
		} catch (Exception e) {
			e.printStackTrace();
			feedback = new Feedback(false, "修改失败");
		}
		return feedback;
	}

	/**
	 * 删除学位院校专业关系
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "deleteSpecialtyDegreeCollege")
	@ResponseBody
	public Feedback deleteSpecialtyDegreeCollege(String ids) {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtDegreeCollegeService.deleteGjtSpecialtyDegreeCollegeReqInBatch(selectedIds);
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	@RequestMapping(value = "addSpecialty", method = RequestMethod.GET)
	public String addSpecialty(String collegeId, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtDegreeCollege college = gjtDegreeCollegeService.queryById(collegeId);
		model.addAttribute("college", college);
		model.addAttribute("gradeMap", commonMapService.getGradeMap(user.getGjtOrg().getId()));// 年级
		model.addAttribute("baseTypeMap", EnumUtil.getDegreeRequirementTypeMap());
		model.addAttribute("operatorEnum", DegreeReqOperatorEnum.values());
		return "graduation/degree/add_specialty_form";
	}

	/**
	 * 批量添加学位和条件
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月16日 下午2:24:55
	 * @param collegeId
	 * @param gradeId
	 * @param degreeName
	 * @param specialtyIds
	 * @param requirements
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "createSpecialtyDegreeCollege", method = RequestMethod.POST)
	public Feedback createSpecialtyDegreeCollege(String collegeId, String gradeId, String degreeName,
			@RequestParam("specialtyIds[]") List<String> specialtyIds, @RequestParam("requirements[]") List<String> requirements,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, null);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<GjtDegreeRequirement> requirementList = new ArrayList<GjtDegreeRequirement>();
		try {
			for (String r : requirements) {
				JSONObject json = JSONObject.fromObject(r);
				GjtDegreeRequirement requirement = new GjtDegreeRequirement();
				requirement.setRequirementId(UUIDUtils.random());
				requirement.setCollegeId(collegeId);
				requirement.setCreatedBy(user.getId());
				requirement.setRequirementDesc((String) json.get("reqDesc"));
				requirement.setRequirementName((String) json.get("reqName"));
				requirement.setRequirementType(json.getInt("reqType"));
				if (json.get("operator") != null) {
					requirement.setOperator(json.getInt("operator"));
				}
				if (json.get("reqParam") != null) {
					requirement.setRequirementParam(Float.valueOf(json.getString("reqParam")));
				}
				requirementList.add(requirement);
			}
			gjtDegreeCollegeService.CreateSpecialtyDegreeCollege(collegeId, gradeId, degreeName, specialtyIds, requirementList,
					user.getGjtOrg().getId());
		} catch (Exception e) {
			feedback.setSuccessful(false);
			feedback.setMessage("系统异常");
		}
		return feedback;
	}

	@ResponseBody
	@RequestMapping(value = "changeCollegeSpecialtyStatus", method = RequestMethod.GET)
	public Feedback changeCollegeSpecialtyStatus(String collegeSpecialtyId, int isEnabled, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, null);
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtSpecialtyDegreeCollege specialytCollge = gjtDegreeCollegeService.querySpecialtyDegreeCollegeById(collegeSpecialtyId);
			if (specialytCollge != null) {
				specialytCollge.setIsEnabled(isEnabled);
				gjtDegreeCollegeService.saveCollegeSpecialty(specialytCollge);
			}
		} catch (Exception e) {
			feedback.setSuccessful(false);
			feedback.setMessage("系统异常");
		}
		return feedback;
	}

	@RequestMapping(value = "chooseSpecialtyList", method = RequestMethod.GET)
	public String chooseSpecialtyList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Sort sort = new Sort(Direction.DESC, "createdDt");

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, sort);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<GjtSpecialtyBase> page = gjtSpecialtyBaseService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);
		model.addAttribute("pageInfo", page);
		return "graduation/degree/choose_specialty_list";
	}

	@RequestMapping(value = "copyDegree", method = RequestMethod.GET)
	public String copyDegree(String collegeId, String collegeSpecialtyId, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<GjtSpecialtyDegreeCollege> specialtyColleges = gjtDegreeCollegeService.queryCollegeSpecialtyByOrgId(user.getGjtOrg().getId());
		model.addAttribute("specialtyColleges", specialtyColleges);
		if (StringUtils.isNoneBlank(collegeSpecialtyId)) {
			List<GjtDegreeRequirement> requirements = gjtDegreeCollegeService.queryReqByCollegeSpecialtyId(collegeSpecialtyId);
			model.addAttribute("requirements", requirements);
		}
		model.addAttribute("typeMap", EnumUtil.getDegreeRequirementTypeMap());
		return "graduation/degree/copy_degree";
	}

}
