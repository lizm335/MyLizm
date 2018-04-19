/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.graduation;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtSampleDocument;
import com.gzedu.xlims.pojo.status.DocumentTypeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.graduation.GjtSampleDocumentService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * 示例文档管理控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @CreateDate 2016年11月11日
 * @EditDate 2016年11月11日
 * @version 2.5
 */
@Controller
@RequestMapping("/graduation/sampleDoc")
public class GjtSampleDocumentController extends BaseController {

	@Autowired
	private GjtSampleDocumentService gjtSampleDocumentService;

	@Autowired
	private CommonMapService commonMapService;

	/**
	 * 示例文档信息列表
	 * @param pageNumber
	 * @param pageSize
     * @return
     */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		Page<GjtSampleDocument> list = gjtSampleDocumentService.queryByPage(searchParams, pageRequst);

		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId()); // 专业

		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("documentTypes", DocumentTypeEnum.values());
		model.addAttribute("pageInfo", list);
		
		
		return "graduation/sampleDoc/list";
	}

	/**
	 * 查看示例文档信息详情
	 * @param id
     * @return
     */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id,
						   Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId()); // 专业

		GjtSampleDocument info = gjtSampleDocumentService.queryById(id);

		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("documentTypes", DocumentTypeEnum.values());
		model.addAttribute("info", info);
		model.addAttribute("action", "view");
		return "graduation/sampleDoc/form";
	}

	/**
	 * 加载示例文档新增信息
     * @return
     */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		model.addAttribute("info", new GjtSampleDocument());
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId()); // 专业

		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("documentTypes", DocumentTypeEnum.values());
		model.addAttribute("action", "create");
		return "graduation/sampleDoc/form";
	}

	/**
	 * 新增示例文档
	 * @param info
     * @return
     */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtSampleDocument info,
						 HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			info.setDocumentId(UUIDUtils.random());
			info.setOrgId(user.getGjtOrg().getId());
			info.setCreatedBy(user.getId());
			info.setCreatedDt(new Date());
			gjtSampleDocumentService.insert(info);
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/graduation/sampleDoc/list";
	}

	/**
	 * 删除示例文档
	 * @param ids
	 * @return
     */
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids) {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtSampleDocumentService.deleteInBatch(selectedIds);
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

}
