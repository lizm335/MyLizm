/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.graduation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtDegreeRequirementBase;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.service.graduation.GjtDegreeCollegeService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 学位条件基础信息管理控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @CreateDate 2016年11月21日
 * @EditDate 2016年11月21日
 * @version 2.5
 */
@Controller
@RequestMapping("/graduation/degreeReqBase")
public class GjtDegreeRequirementBaseController extends BaseController {

	@Autowired
	private GjtDegreeCollegeService gjtDegreeCollegeService;

	/**
	 * 学位条件基础信息列表
     * @return
     */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		List<GjtDegreeRequirementBase> list = gjtDegreeCollegeService.queryGjtDegreeRequirementBaseAll();

		model.addAttribute("infos", list);
		model.addAttribute("baseTypeMap", EnumUtil.getBaseDegreeRequirementTypeMap());
		return "graduation/degree/req_base_list";
	}

	/**
	 * 加载新增学位条件基础信息
     * @return
     */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		model.addAttribute("info", new GjtDegreeRequirementBase());
		model.addAttribute("action", "create");
		model.addAttribute("baseTypeMap", EnumUtil.getBaseDegreeRequirementTypeMap());
		return "graduation/degree/req_base_form";
	}

	/**
	 * 新增学位条件基础信息
	 * @param info
     * @return
     */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtDegreeRequirementBase info,
						 HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			info.setBaseId(UUIDUtils.random());
			info.setCreatedBy(user.getId());
			gjtDegreeCollegeService.insertGjtDegreeRequirementBase(info);
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/graduation/degreeReqBase/list";
	}

	/**
	 * 加载编辑学位条件基础信息
	 * @return
	 */
	@RequestMapping(value = "update/{baseId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("baseId") String baseId, Model model, HttpServletRequest request) {
		GjtDegreeRequirementBase info = gjtDegreeCollegeService.queryGjtDegreeRequirementBaseById(baseId);
		model.addAttribute("info", info);
		model.addAttribute("action", "update");
		model.addAttribute("baseTypeMap", EnumUtil.getBaseDegreeRequirementTypeMap());
		return "graduation/degree/req_base_form";
	}

	/**
	 * 修改学位条件基础信息
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid GjtDegreeRequirementBase info,
						 HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "修改成功");
		try {
			GjtDegreeRequirementBase modifyInfo = gjtDegreeCollegeService.queryGjtDegreeRequirementBaseById(info.getBaseId());
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			modifyInfo.setBaseName(info.getBaseName());
			modifyInfo.setBaseDesc(info.getBaseDesc());
			modifyInfo.setUpdatedBy(user.getId());
			gjtDegreeCollegeService.updateGjtDegreeRequirementBase(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "修改失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/graduation/degreeReqBase/list";
	}

	/**
	 * 删除学位条件基础信息
	 * @param ids
	 * @return
     */
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids) {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtDegreeCollegeService.deleteGjtDegreeRequirementBaseInBatch(selectedIds);
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

}
