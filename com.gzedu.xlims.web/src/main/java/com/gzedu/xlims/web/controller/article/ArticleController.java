/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.article;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtArticle;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtMenu;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.article.GjtArticleService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.systemManage.GjtMenuService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年8月6日
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/article")

public class ArticleController extends BaseController {
	@Autowired
	GjtArticleService gjtArticleService;
	@Autowired
	GjtSpecialtyService gjtSpecialtyService;
	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	CacheService cacheService;

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	GjtMenuService gjtMenuService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request,
			HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		searchParams.put("EQ_xxId", user.getGjtOrg().getId());
		Page<GjtArticle> lists = gjtArticleService.queryPageList(searchParams, pageRequst);

		for (Iterator<GjtArticle> iterator = lists.iterator(); iterator.hasNext();) {
			GjtArticle gjtArticle = iterator.next();
			if (StringUtils.isNotEmpty(gjtArticle.getSpecialtyId())) {
				GjtSpecialty specialty = gjtSpecialtyService.queryById(gjtArticle.getSpecialtyId());
				gjtArticle.setSpecialtyName(specialty == null ? null : specialty.getZymc());
			}
			if (StringUtils.isNotEmpty(gjtArticle.getGradeId())) {
				GjtGrade gjtGrade = gjtGradeService.queryById(gjtArticle.getGradeId());
				gjtArticle.setGradeName(gjtGrade == null ? null : gjtGrade.getGradeName());
			}
			gjtArticle.setPyccName(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL, gjtArticle.getPycc()));
		}
		model.addAttribute("infos", lists);
		return "/article/list";
	}

	@RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") String id, HttpServletRequest request, Model model) {
		model.addAttribute("gjtArticle", gjtArticleService.queryById(id));
		return "/article/detail";
	}

	/**
	 * 加载新增文章
	 * 
	 * @return
	 */
	@RequestMapping(value = "toCreateArticle", method = RequestMethod.GET)
	public String toCreateArticle(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		Map<String, String> articleCategoryMap = commonMapService.getCategoryDates();
		Map<String, String> menuMap = commonMapService.getMenuDates(user.getGjtOrg().getId());
		// 院校模式默认所属菜单为学习指引
		Subject subject = SecurityUtils.getSubject();
		if(subject.isPermitted("/article/list$schoolModel")) { // 院校模式
			menuMap = commonMapService.getMenuByName("学习指引");
		}
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("articleCategoryMap", articleCategoryMap);
		model.addAttribute("menuMap", menuMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("gjtArticle", new GjtArticle());
		model.addAttribute("action", "doCreateArticle");
		return "/article/form";
	}

	/**
	 * 创建文章
	 * 
	 * @return
	 */
	@RequestMapping(value = "doCreateArticle", method = RequestMethod.POST)
	public String doCreateArticle(ServletRequest request, RedirectAttributes redirectAttributes,
			@Valid GjtArticle gjtArticle) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) ((HttpServletRequest) request).getSession()
				.getAttribute(WebConstants.CURRENT_USER);
		gjtArticle.setId(UUIDUtils.create());
		gjtArticle.setXxId(user.getGjtOrg().getId());
		String effectiveStimeStr = request.getParameter("effectiveStimeStr");
		String effectiveEtimeStr = request.getParameter("effectiveEtimeStr");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (!"".equals(effectiveStimeStr)) {
				Date effectiveStime = sdf.parse(effectiveStimeStr);
				gjtArticle.setEffectiveStime(effectiveStime);
			}
			if (!"".equals(effectiveEtimeStr)) {
				Date effectiveEtime = sdf.parse(effectiveEtimeStr);
				gjtArticle.setEffectiveEtime(effectiveEtime);
			}
			gjtArticleService.saveArticle(gjtArticle);
		} catch (Exception e) {
			e.printStackTrace();
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/article/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		Map<String, String> articleCategoryMap = commonMapService.getCategoryDates();
		Map<String, String> menuMap = commonMapService.getMenuDates(user.getGjtOrg().getId());
		Subject subject = SecurityUtils.getSubject();
		if(subject.isPermitted("/article/list$schoolModel")) { // 院校模式
			menuMap = commonMapService.getMenuByName("学习指引");
		}
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("articleCategoryMap", articleCategoryMap);
		model.addAttribute("menuMap", menuMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("gjtArticle", gjtArticleService.queryById(id));
		model.addAttribute("action", "doUpdateArticle");
		return "/article/form";
	}

	@RequestMapping(value = "doUpdateArticle", method = RequestMethod.POST)
	public String doUpdateArticle(ServletRequest request, RedirectAttributes redirectAttributes,
			@Valid GjtArticle gjtArticle) {
		Feedback feedback = new Feedback(true, "修改成功");
		GjtArticle modifyEntity = gjtArticleService.queryById(gjtArticle.getId());
		modifyEntity.setArticleCategoryId(gjtArticle.getArticleCategoryId());
		modifyEntity.setMenuId(gjtArticle.getMenuId());
		modifyEntity.setTitle(gjtArticle.getTitle());
		modifyEntity.setSpecialtyId(gjtArticle.getSpecialtyId());
		modifyEntity.setGradeId(gjtArticle.getGradeId());
		modifyEntity.setPycc(gjtArticle.getPycc());
		modifyEntity.setContent(gjtArticle.getContent());
		modifyEntity.setFileName(gjtArticle.getFileName());
		if(StringUtils.isNotBlank(gjtArticle.getFileUrl())) modifyEntity.setFileUrl(gjtArticle.getFileUrl());
		String effectiveStimeStr = request.getParameter("effectiveStimeStr");
		String effectiveEtimeStr = request.getParameter("effectiveEtimeStr");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (!"".equals(effectiveStimeStr)) {
				Date effectiveStime = sdf.parse(effectiveStimeStr);
				modifyEntity.setEffectiveStime(effectiveStime);
			}
			if (!"".equals(effectiveEtimeStr)) {
				Date effectiveEtime = sdf.parse(effectiveEtimeStr);
				modifyEntity.setEffectiveEtime(effectiveEtime);
			}
			gjtArticleService.updateArticle(modifyEntity);
		} catch (Exception e) {
			feedback = new Feedback(false, "修改失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/article/list";
	}

	/**
	 * 删除文章
	 * 
	 * @return
	 */
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				for (String id : selectedIds) {
					GjtArticle gjtArticle = gjtArticleService.queryById(id);
					gjtArticle.setIsDeleted("Y");
					gjtArticleService.updateArticle(gjtArticle);
				}
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	@RequestMapping(value = "guideList", method = RequestMethod.GET)
	public String guideList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request, HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		searchParams.put("EQ_xxId", user.getGjtOrg().getId());
		GjtMenu menu = gjtMenuService.queryByNameAndXxId("学习指引", user.getGjtOrg().getId());
		if (menu != null) {
			searchParams.put("EQ_menuId", menu.getId());
		}
		Page<GjtArticle> lists = gjtArticleService.queryPageList(searchParams, pageRequst);

		for (Iterator<GjtArticle> iterator = lists.iterator(); iterator.hasNext();) {
			GjtArticle gjtArticle = iterator.next();
			if (StringUtils.isNotEmpty(gjtArticle.getSpecialtyId())) {
				GjtSpecialty specialty = gjtSpecialtyService.queryById(gjtArticle.getSpecialtyId());
				gjtArticle.setSpecialtyName(specialty == null ? null : specialty.getZymc());
			}
			if (StringUtils.isNotEmpty(gjtArticle.getGradeId())) {
				GjtGrade gjtGrade = gjtGradeService.queryById(gjtArticle.getGradeId());
				gjtArticle.setGradeName(gjtGrade == null ? null : gjtGrade.getGradeName());
			}
			gjtArticle.setPyccName(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL, gjtArticle.getPycc()));
		}
		model.addAttribute("infos", lists);
		return "/article/guide_list";
	}

	@RequestMapping(value = "toGuideForm", method = RequestMethod.GET)
	public String toGuideForm(String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		Map<String, String> articleCategoryMap = commonMapService.getCategoryDates();
		Map<String, String> menuMap = commonMapService.getMenuDates(user.getGjtOrg().getId());
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());
		GjtArticle gjtArticle;
		if(StringUtils.isNotBlank(id)){
			gjtArticle=gjtArticleService.queryById(id);
		}else{
			gjtArticle=new GjtArticle();
		}
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("articleCategoryMap", articleCategoryMap);
		model.addAttribute("menuMap", menuMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("gjtArticle", gjtArticle);
		model.addAttribute("action", "doUpdateArticle");
		return "/article/guide_form";
	}

	@ResponseBody
	@RequestMapping(value = "saveGuide", method = RequestMethod.POST)
	public Feedback saveGuide(Model model, HttpServletRequest request, @Valid GjtArticle gjtArticle) {
		Feedback feedback = new Feedback(true, "修改成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			GjtArticle modifyEntity;
			GjtMenu menu = gjtMenuService.queryByNameAndXxId("学习指引", user.getGjtOrg().getId());
			if (StringUtils.isNotBlank(gjtArticle.getId())) {
				modifyEntity = gjtArticleService.queryById(gjtArticle.getId());
				modifyEntity.setMenuId(gjtArticle.getMenuId());
				modifyEntity.setTitle(gjtArticle.getTitle());
				modifyEntity.setSpecialtyId(gjtArticle.getSpecialtyId());
				modifyEntity.setGradeId(gjtArticle.getGradeId());
				modifyEntity.setPycc(gjtArticle.getPycc());
				modifyEntity.setContent(gjtArticle.getContent());
				modifyEntity.setFileName(gjtArticle.getFileName());
				modifyEntity.setFileUrl(gjtArticle.getFileUrl());
				modifyEntity.setEffectiveStime(gjtArticle.getEffectiveStime());
				modifyEntity.setEffectiveEtime(gjtArticle.getEffectiveEtime());
				modifyEntity.setUpdatedBy(user.getId());
				modifyEntity.setUpdatedDt(new Date());

			} else {
				modifyEntity = gjtArticle;
				modifyEntity.setId(UUIDUtils.random());
				modifyEntity.setCreatedBy(user.getId());
				modifyEntity.setCreatedDt(new Date());
				modifyEntity.setArticleCategoryId("20000");
				modifyEntity.setXxId(user.getGjtOrg().getId());
			}
			modifyEntity.setMenuId(menu.getId());

			gjtArticleService.saveArticle(modifyEntity);
		} catch (Exception e) {
			feedback = new Feedback(false, "修改失败");
		}
		return feedback;
	}
}
