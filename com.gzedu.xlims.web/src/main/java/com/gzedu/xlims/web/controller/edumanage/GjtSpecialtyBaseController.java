package com.gzedu.xlims.web.controller.edumanage;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtSpecialtyBase;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtSpecialtyBaseService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/edumanage/specialty/base/")
public class GjtSpecialtyBaseController {
	
	private static final Logger log = LoggerFactory.getLogger(GjtSpecialtyController.class);

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtSpecialtyBaseService gjtSpecialtyBaseService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Sort sort = new Sort(Direction.DESC, "createdDt");

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, sort);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		/*List<GjtSpecialtyOwnership> gjtSpecialtyOwnershipList = gjtSpecialtyShareService
				.findByOrgCode(user.getGjtOrg().getId());

		List<String> specialtyIds = new ArrayList<String>();
		if (gjtSpecialtyOwnershipList.size() != 0) {
			for (GjtSpecialtyOwnership gjtSpecialtyOwnership : gjtSpecialtyOwnershipList) {
				if (gjtSpecialtyOwnership != null) {
					specialtyIds.add(gjtSpecialtyOwnership.getSpecialtyId());
				}
			}

		}*/
		Page<GjtSpecialtyBase> page = gjtSpecialtyBaseService.queryAll(user.getGjtOrg().getId(),searchParams,pageRequst);
		model.addAttribute("pageInfo", page);


		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 层次

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnCreate", subject.isPermitted("/edumanage/specialty/base/list$create"));
		model.addAttribute("isBtnUpdate", subject.isPermitted("/edumanage/specialty/base/list$update"));
		model.addAttribute("isBtnDelete", subject.isPermitted("/edumanage/specialty/base/list$delete"));
		model.addAttribute("isBtnView", subject.isPermitted("/edumanage/specialty/base/list$view"));
		model.addAttribute("isBtnStop", subject.isPermitted("/edumanage/specialty/base/list$stop"));
		model.addAttribute("isBtnEnable", subject.isPermitted("/edumanage/specialty/base/list$enable"));
		model.addAttribute("isBtnCreateRule", subject.isPermitted("/edumanage/specialty/list$create"));

		return "edumanage/specialty/base/list";
	}
	
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		model.addAttribute("item", new GjtSpecialtyBase());
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("action", "create");

		return "edumanage/specialty/base/form";
	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(GjtSpecialtyBase item, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			// 1.参数校验
			// 1.1  专业规则号/专业代码
			if(StringUtils.isNotEmpty(item.getSpecialtyCode()) && item.getSpecialtyLayer() != null) {
				if(item.getSpecialtyCode().length() != 8) {
					feedback = new Feedback(false, "专业代码规定为8位!");
					return feedback;
				}
				GjtSpecialtyBase exist = gjtSpecialtyBaseService.findByCodeAndLayer(user.getGjtOrg().getId(),item.getSpecialtyCode(),item.getSpecialtyLayer());
				if(exist != null ) {
					feedback = new Feedback(false, "专业代码已存在!");
					return feedback;
				}
			} else {
				feedback = new Feedback(false, "专业代码或层次不能为NULL!");
				return feedback;
			}
			// 1.2 专业名称
			if(StringUtils.isNotEmpty(item.getSpecialtyName())) {
				if(item.getSpecialtyName().contains("（") || item.getSpecialtyName().contains("）")) {
					feedback = new Feedback(false, "专业名称不能包含中文括号,请使用英文括号!");
					return feedback;
				}
			} else {
				feedback = new Feedback(false, "专业名称不能为空!");
				return feedback;
			}
			item.setSpecialtyBaseId(UUIDUtils.random());
			// 2.设置院校ID
			if ("1".equals(user.getGjtOrg().getOrgType())) {
				item.setXxId(user.getGjtOrg().getId());
			} else {
				if (user.getGjtOrg().getParentGjtOrg() != null) {
					item.setXxId(user.getGjtOrg().getParentGjtOrg().getId());
				} else {
					item.setXxId(user.getGjtOrg().getId());
				}
			}

			item.setCreatedDt(DateUtils.getNowTime());
			item.setCreatedBy(user.getId());
			gjtSpecialtyBaseService.save(item);
			feedback = new Feedback(true, item.getSpecialtyBaseId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, e.getMessage());
		}
		
		return feedback;
	}
	
	@RequestMapping(value = "{[update]|[view]}/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("[update]|[view]") String action, @PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtSpecialtyBase gjtSpecialtyBase = gjtSpecialtyBaseService.queryById(id);
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("item", gjtSpecialtyBase);
		if("update".equals(action)) {
			model.addAttribute("action", action);
			return "edumanage/specialty/base/form";
		} else if("view".equals(action)) {
			return "edumanage/specialty/base/view";
		} else {
			model.addAttribute("action", action);
			return "edumanage/specialty/base/form";
		}
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(GjtSpecialtyBase item, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			// 1.按院校校验规则号
			/*if(StringUtils.isNotEmpty(item.getSpecialtyCode()) && item.getSpecialtyLayer() != null) {
				if(item.getSpecialtyCode().length() != 8) {
					throw new Exception("专业规则号规定为8位!");
				}
				GjtSpecialtyBase exist = gjtSpecialtyBaseService.findByCodeAndLayer(user.getGjtOrg().getId(),item.getSpecialtyCode(),item.getSpecialtyLayer());
				if(exist != null && !item.getSpecialtyBaseId().equals(exist.getSpecialtyBaseId())) {
					throw new Exception("专业规则号已存在!");
				}
			} else {
				throw new Exception("专业规则号或层次不能为NULL!");
			}*/
			GjtSpecialtyBase entity = gjtSpecialtyBaseService.queryById(item.getSpecialtyBaseId());
			entity.setSpecialtyName(item.getSpecialtyName());
			entity.setSpecialtyImgUrl(item.getSpecialtyImgUrl());
			entity.setSpecialtyDetails(item.getSpecialtyDetails());

			entity.setTeacher(item.getTeacher());
			entity.setTeacherDetails(item.getTeacherDetails());
			entity.setTeacherImgUrl(item.getTeacherImgUrl());

			entity.setUpdatedBy(user.getId());
			entity.setUpdatedDt(new Date());
			gjtSpecialtyBaseService.save(entity);
			feedback = new Feedback(true, entity.getSpecialtyBaseId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, e.getMessage());
		}
		return feedback;
	}

	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids) throws IOException {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtSpecialtyBaseService.deleteById(selectedIds); // 删除专业
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				fb = new Feedback(false, "删除失败");
			}
		}
		return fb;
	}
	
	@RequestMapping(value = "changeStatus")
	public String changeStatus(String id, boolean b, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback fb = new Feedback(true, "操作成功");
		int status = b?1:0;
		try {
			gjtSpecialtyBaseService.updateStatus(id,status);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, "操作失败");
		}
		
		redirectAttributes.addFlashAttribute("feedback", fb);
		return "redirect:/edumanage/specialty/base/list";
	}
	
	@RequestMapping(value = "getLayerById")
	@ResponseBody
	public Integer getLayerById(String specialtyBaseId) {
		GjtSpecialtyBase specialtyBase = gjtSpecialtyBaseService.queryById(specialtyBaseId);
		return specialtyBase.getSpecialtyLayer();
	}
}
