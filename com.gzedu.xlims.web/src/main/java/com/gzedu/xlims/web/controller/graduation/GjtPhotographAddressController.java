/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.graduation;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtPhotographAddress;
import com.gzedu.xlims.pojo.graduation.GjtPhotographData;
import com.gzedu.xlims.service.dictionary.GjtDistrictService;
import com.gzedu.xlims.service.graduation.GjtPhotographAddressService;
import com.gzedu.xlims.service.graduation.GjtPhotographDataService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月28日
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/graduation/photograph")
public class GjtPhotographAddressController {

	private final static Logger log = LoggerFactory.getLogger(GjtPhotographAddressController.class);

	@Autowired
	GjtPhotographAddressService gjtPhotographAddressService;

	@Autowired
	GjtDistrictService gjtDistrictService;

	@Autowired
	GjtPhotographDataService gjtPhotographDataService;

	@Autowired
	GjtOrgService gjtOrgService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, String type) {
		type = StringUtils.isBlank(type) ? "1" : type;// 默认学院
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		searchParams.put("EQ_type", type);
		searchParams.put("EQ_isDeleted", "N");
		log.info("查询参数：{}", searchParams);
		Page<GjtPhotographAddress> pageInfo = gjtPhotographAddressService.queryPageInfo(searchParams, pageRequst);
		for (GjtPhotographAddress item : pageInfo) {
			String allName = gjtDistrictService.queryAllNameById(item.getDistrict());
			item.setRemark(allName);
		}
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("type", type);
		return "graduation/photograph/photograph_list";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(HttpServletRequest request, Model model, String type) {
		model.addAttribute("entity", new GjtPhotographAddress());
		model.addAttribute("type", type);
		model.addAttribute("action", "create");
		return "graduation/photograph/photograph_from";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(@ModelAttribute GjtPhotographAddress entity, Model model, HttpServletRequest request,
			String photographEndDates, String photographSrartDates) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "新增成功");
		try {
			entity.setId(UUIDUtils.random().toString());
			entity.setCreatedDt(DateUtils.getNowTime());
			entity.setIsDeleted("N");
			entity.setIsEnabled("1");
			entity.setOrgId(user.getGjtOrg().getId());
			if ("1".equals(entity.getType())) {
				entity.setPhotographEndDate(DateUtils.getDateToString(photographEndDates + ":00"));
				entity.setPhotographSrartDate(DateUtils.getDateToString(photographSrartDates + ":00"));
			}
			entity.setCoordinate(entity.getCoordinate());
			entity.setExpensiveStu(entity.getExpensiveStu());
			entity.setCreatedBy(user.getId());
			gjtPhotographAddressService.save(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "新增失败");
		}
		return feedback;
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String update(HttpServletRequest request, Model model, @PathVariable String id) {
		GjtPhotographAddress entity = gjtPhotographAddressService.queryById(id);
		model.addAttribute("type", entity.getType());
		model.addAttribute("entity", entity);
		model.addAttribute("action", "update");
		return "graduation/photograph/photograph_from";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(HttpServletRequest request, Model model, @ModelAttribute GjtPhotographAddress entity,
			String photographEndDates, String photographSrartDates) {
		Feedback feedback = new Feedback(true, "修改成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtPhotographAddress item = gjtPhotographAddressService.queryById(entity.getId());
		try {
			item.setDistrict(entity.getDistrict());
			item.setNotesRemark(entity.getNotesRemark());
			item.setPhotographAddress(entity.getPhotographAddress());
			item.setPhotographName(entity.getPhotographName());
			if ("1".equals(entity.getType())) {
				item.setPhotographEndDate(DateUtils.getDateToString(photographEndDates + ":00"));
				item.setPhotographSrartDate(DateUtils.getDateToString(photographSrartDates + ":00"));
			}
			item.setTelePhone(entity.getTelePhone());
			item.setUpdateBy(user.getId());
			item.setUpdateDt(DateUtils.getNowTime());
			item.setCoordinate(entity.getCoordinate());
			item.setExpensiveStu(entity.getExpensiveStu());
			gjtPhotographAddressService.update(item);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "修改失败");
		}
		return feedback;
	}

	// 删除评论
	@RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Feedback delete(HttpServletRequest request, Model model, @PathVariable("id") String id) {
		Feedback feedback = new Feedback(true, "删除成功");
		try {
			gjtPhotographAddressService.delete(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "删除失败");
		}
		return feedback;
	}

	@RequestMapping(value = "photographDataView", method = RequestMethod.GET)
	public String photographDataView(HttpServletRequest request, Model model, String type) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String xxId = user.getGjtOrg().getGjtSchoolInfo().getId();
		GjtPhotographData info = gjtPhotographDataService.findByXxId(xxId);
		model.addAttribute("info", info);
		model.addAttribute("type", type);
		return "graduation/photograph/photographData_view";
	}

	@RequestMapping(value = "photographDataCreate", method = RequestMethod.POST)
	@ResponseBody
	public Feedback photographDataCreate(@ModelAttribute GjtPhotographData entity, HttpServletRequest request) {
		Feedback fb = new Feedback(true, "保存成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String id = entity.getId();
		try {
			if (StringUtils.isNotBlank(id)) {
				entity.setUpdateBy(user.getId());
				entity.setUpdateDt(DateUtils.getNowTime());
				gjtPhotographDataService.update(entity);
			} else {
				GjtOrg gjtOrg = user.getGjtOrg();
				String xxId = "";
				if ("1".equals(gjtOrg.getOrgType())) {
					xxId = gjtOrg.getId();
				} else {
					GjtOrg org = gjtOrgService.queryParentBySonId(gjtOrg.getId());
					xxId = org.getId();
				}
				entity.setId(UUIDUtils.random());
				entity.setXxId(xxId);
				entity.setCreatedBy(user.getId());
				entity.setCreatedDt(DateUtils.getNowTime());
				gjtPhotographDataService.save(entity);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			fb = new Feedback(false, "保存失败！" + e.getMessage());
		}

		return fb;
	}

	@ResponseBody
	@RequestMapping(value = "changeStatus/{id}/{status}", method = RequestMethod.POST)
	public Feedback changeStatus(@PathVariable("id") String id, @PathVariable("status") String status) {
		boolean result = gjtPhotographAddressService.updateEnabled(id, status);
		Feedback feedback = new Feedback(result, "");
		return feedback;
	}

}
