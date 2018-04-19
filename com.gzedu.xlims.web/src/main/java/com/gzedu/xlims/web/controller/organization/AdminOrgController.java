/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.organization;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtSetOrgCopyright;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSetOrgCopyrightService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明：学院管理
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月23日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/admin/organization/org")
public class AdminOrgController {

	private static final Log log = LogFactory.getLog(AdminOrgController.class);

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtSetOrgCopyrightService gjtSetOrgCopyrightService;

	@Autowired
	CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "createdDt", "DESC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_isDeleted", "N");
		searchParams.put("EQ_orgType", "1");
		Page<GjtOrg> pageInfo = gjtOrgService.queryAll(searchParams, pageRequst);

		Map<String, String> orgMap = commonMapService.getOrgMapByType("1");

		model.addAttribute("orgMap", orgMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("isAdmin", true);

		return "organization/org/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", gjtOrgService.queryById(id));
		model.addAttribute("action", "view");
		return "organization/org/form";
	}

	@RequestMapping(value = "toCreate", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("entity", new GjtOrg());
		model.addAttribute("action", "create");
		return "organization/org/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtOrg entity, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			gjtOrgService.insert(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/organization/org/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", gjtOrgService.queryById(id));
		model.addAttribute("action", "update");
		return "organization/org/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity") GjtOrg entity, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");

		GjtOrg modifyInfo = gjtOrgService.queryById(entity.getId()); // 院校编码
		GjtSchoolInfo schoolInfo = modifyInfo.getGjtSchoolInfo();
		if (schoolInfo == null) {
			modifyInfo.setGjtSchoolInfo(new GjtSchoolInfo(entity.getId()));
		}
		modifyInfo.setCode(entity.getCode()); // 院校名称
		modifyInfo.setOrgName(entity.getOrgName());
		modifyInfo.getGjtSchoolInfo().setXxmc(entity.getOrgName()); // 联系电话
		modifyInfo.getGjtSchoolInfo().setLinkTel(entity.getGjtSchoolInfo().getLinkTel()); // 联系人
		modifyInfo.getGjtSchoolInfo().setLinkMan(entity.getGjtSchoolInfo().getLinkMan()); // 电子邮箱
		modifyInfo.getGjtSchoolInfo().setLinkMail(entity.getGjtSchoolInfo().getLinkMail()); // 院校地址
		modifyInfo.getGjtSchoolInfo().setXxdz(entity.getGjtSchoolInfo().getXxdz()); // 描述
		modifyInfo.getGjtSchoolInfo().setMemo(entity.getGjtSchoolInfo().getMemo()); // APPId
		modifyInfo.getGjtSchoolInfo().setAppid(entity.getCode());// 确认过用院校代码作appid
		modifyInfo.getGjtSchoolInfo().setXxqhm(entity.getGjtSchoolInfo().getXxqhm());

		try {
			gjtOrgService.update(modifyInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/organization/org/list";
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, ServletResponse response) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			try {
				gjtOrgService.delete(Arrays.asList(ids.split(",")));
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return new Feedback(false, "删除失败，原因:" + e.getMessage());
			}
		}
		return new Feedback(false, "删除失败");
	}

	/**
	 * 删除总校
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteOrg/{id}", method = RequestMethod.POST)
	public Feedback deleteOrg(@PathVariable("id") String id) {

		if (StringUtils.isNotBlank(id)) {
			try {
				gjtOrgService.delete(Arrays.asList(id.split(",")));
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return new Feedback(false, "删除失败 " + e.getMessage());
			}
		}
		return new Feedback(false, "删除失败");
	}

	/**
	 * 跳转至院校设置页面
	 * 
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "orgSet/{id}", method = RequestMethod.GET)
	public String orgSetList(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		// GjtUserAccount user = (GjtUserAccount)
		// request.getSession().getAttribute(WebConstants.CURRENT_USER);
		// 查询当前所属院校
		GjtOrg gjtOrg = gjtOrgService.queryById(id);
		// 查询管理平台数据
		GjtSetOrgCopyright xlimsGzeduMap = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(id,
				PlatfromTypeEnum.MANAGEPLATFORM.getNum());
		// 查询辅导教师平台数据
		GjtSetOrgCopyright fudaoTeacherMap = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(id,
				PlatfromTypeEnum.COACHTEACHERPLATFORM.getNum());
		// 查询班主任平台数据
		GjtSetOrgCopyright bzrTeacherMap = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(id,
				PlatfromTypeEnum.HEADTEACHERPLATFORM.getNum());
		// 查询督导教师平台数据
		GjtSetOrgCopyright dudaoTeacherMap = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(id,
				PlatfromTypeEnum.SUPERVISORTEACHERPLATFORM.getNum());
		// 查询个人中心平台数据
		GjtSetOrgCopyright studyCenterMap = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(id,
				PlatfromTypeEnum.PERCENTPLATFORM.getNum());

		model.addAttribute("entity", gjtOrg);
		model.addAttribute("xlimsGzeduMap", xlimsGzeduMap);
		model.addAttribute("fudaoTeacherMap", fudaoTeacherMap);
		model.addAttribute("bzrTeacherMap", bzrTeacherMap);
		model.addAttribute("dudaoTeacherMap", dudaoTeacherMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		return "organization/org/org_set_list";
	}

	/**
	 * 版权设置后保存或更新
	 * 
	 * @param entity
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "updateOrInsert")
	@ResponseBody
	public Feedback updateFrom(@ModelAttribute GjtSetOrgCopyright entity, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "更新成功");
		try {
			String xxId = request.getParameter("id");
			// 根据xx_id和平台类型platfromType查询是否存在数据，如果不存在，则新增，否则更新
			long orgSetNum = gjtOrgService.queryOrgSetNum(entity, xxId);
			if (orgSetNum < 1) {// 新增
				boolean bool = gjtOrgService.insertSetOrgCopyright(entity, xxId, user.getId());
				if (!bool) {
					feedback = new Feedback(false, "新增失败");
				}
			} else {// 修改
				boolean bool = gjtOrgService.updateSetOrgCopyright(entity, xxId, user.getId());
				if (!bool) {
					feedback = new Feedback(false, "更新失败");
				}
			}
		} catch (Exception e) {
			feedback = new Feedback(true, "更新失败");
			System.out.println("出现异常:" + e);
		}

		// redirectAttributes.addFlashAttribute("feedback", feedback);
		return feedback;
	}

	@RequestMapping(value = "checkCode", method = { RequestMethod.POST })
	@ResponseBody
	public Feedback checkCode(String code, String oldCode) {

		boolean flage = gjtOrgService.queryByCode(code, oldCode);
		Feedback fe = new Feedback(flage, "");
		fe.setSuccessful(flage);

		return fe;
	}

}
