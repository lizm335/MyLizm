/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.useremp;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtEmployeePosition;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.id.GjtEmployeePositionPK;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.pojo.status.UserTypeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import java.util.Map;

/**
 * 教职工管理控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年09月20日
 * @version 2.5
 */
@Controller
@RequestMapping("/usermanage/teacher")
public class OtherTeacherController extends BaseController {

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	GjtStudyCenterService gjtStudyCenterService;

	@Autowired
	GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtOrgService gjtOrgService;

	/**
	 * 教职工信息列表
	 * @param employeeType 论文指导教师(11), 论文答辩教师(12), 社会实践教师(13)
	 * @param pageNumber
	 * @param pageSize
     * @return
     */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "employeeType") int employeeType,
					   @RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtEmployeeInfo> list = gjtEmployeeInfoService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst, employeeType);

		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 学校
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心

		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("pageInfo", list);
		model.addAttribute("employeeType", employeeType);
		
		
		return "usermanage/teacher/list";
	}

	/**
	 * 查看教职工信息详情
	 * @param id
     * @return
     */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id,
						   Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 学校
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心

		GjtEmployeeInfo info = gjtEmployeeInfoService.queryById(id);

		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("item", info);
		model.addAttribute("employeeType", info.getEmployeeType());
		model.addAttribute("action", "view");
		return "usermanage/teacher/form";
	}

	/**
	 * 加载教职工新增信息
	 * @param employeeType 论文指导教师(11), 论文答辩教师(12), 社会实践教师(13)
     * @return
     */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(@RequestParam(value = "employeeType") int employeeType,
							 Model model, HttpServletRequest request) {
		model.addAttribute("item", new GjtEmployeeInfo());
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 学校
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心

		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("employeeType", employeeType);
		model.addAttribute("action", "create");
		return "usermanage/teacher/form";
	}

	/**
	 * 新增教职工
	 * @param employeeType 论文指导教师(11), 论文答辩教师(12), 社会实践教师(13)
	 * @param item
	 * @param loginAccount
     * @return
     */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@RequestParam(value = "employeeType") int employeeType,
						 @Valid GjtEmployeeInfo item,
						 HttpServletRequest request, RedirectAttributes redirectAttributes, String loginAccount) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			GjtOrg org = gjtOrgService.queryById(item.getXxzxId());
			item.setOrgId(org.getId());
			item.setOrgCode(org.getCode());
			if(employeeType == 10) {
				String[] positions = request.getParameterValues("position");
				if (positions != null && positions.length > 0) {
					GjtUserAccount gjtUserAccount = gjtUserAccountService.saveEntity(item.getXm(), loginAccount, item.getOrgId(),
							UserTypeEnum.管理员.getNum());
					gjtEmployeeInfoService.saveEntity(item, gjtUserAccount, employeeType + "", positions);
					GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
					gjtEmployeeInfoService.updateLunwenTeacherAddPermissions(gjtUserAccount.getId(), user.getId());
				} else {
					feedback = new Feedback(false, "创建失败，教师类别为空");
				}
			} else {
				GjtUserAccount gjtUserAccount = gjtUserAccountService.saveEntity(item.getXm(), loginAccount, item.getOrgId(),
						UserTypeEnum.教师.getNum());
				gjtEmployeeInfoService.saveEntity(item, gjtUserAccount, employeeType + "");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/usermanage/teacher/list?employeeType="+employeeType;
	}

	/**
	 * 加载教职工修改信息
	 * @param id
     * @return
     */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 学校
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心

		GjtEmployeeInfo info = gjtEmployeeInfoService.queryById(id);

		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("item", info);
		model.addAttribute("employeeType", info.getEmployeeType());
		model.addAttribute("action", "update");
		return "usermanage/teacher/form";
	}

	/**
	 * 修改教职工信息
	 * @param item
     * @return
     */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(GjtEmployeeInfo item,
						 HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtEmployeeInfo modifyInfo = gjtEmployeeInfoService.queryById(item.getEmployeeId());

		modifyInfo.setXbm(item.getXbm());
		modifyInfo.setWorkAddr(item.getWorkAddr());
		modifyInfo.setWorkTime(item.getWorkTime());
		modifyInfo.setSjh(item.getSjh());
		modifyInfo.setLxdh(item.getLxdh());
		modifyInfo.setDzxx(item.getDzxx());
		modifyInfo.setUpdatedDt(DateUtils.getNowTime());
		modifyInfo.setXm(item.getXm());
		modifyInfo.getGjtUserAccount().setRealName(item.getXm());
		modifyInfo.setQq(item.getQq());
		modifyInfo.setIndividualitySign(item.getIndividualitySign());
		if((EmployeeTypeEnum.论文教师.getNum()+"").equals(modifyInfo.getEmployeeType())) {
			String[] positions = request.getParameterValues("position");
			if (positions != null && positions.length > 0) {
				modifyInfo.removeGjtEmployeePositionListAll();
				for (int i = 0; i < positions.length; i++) {
					GjtEmployeePosition position = new GjtEmployeePosition();
					GjtEmployeePositionPK id = new GjtEmployeePositionPK(modifyInfo.getEmployeeId(), NumberUtils.toInt(positions[i]));
					position.setId(id);
					modifyInfo.addGjtEmployeePositionList(position);
				}
			}
		}
		// 判断头像是否更换
		if(StringUtils.isNotBlank(item.getZp())) {
			modifyInfo.setZp(item.getZp());
		}

		try {
			gjtEmployeeInfoService.updateEntity(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/usermanage/teacher/list?employeeType="+modifyInfo.getEmployeeType();
	}

	/**
	 * 删除教职工
	 * @param ids
	 * @return
	 * @throws IOException
     */
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtEmployeeInfoService.deleteById(selectedIds);
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	/**
	 * 检测登录账户是否存在
	 * @param loginAccount
	 * @return
	 * @throws IOException
     */
	@RequestMapping(value = "checkLogin")
	@ResponseBody
	public Feedback checkLogin(String loginAccount) throws IOException {
		Boolean boolean1 = gjtUserAccountService.existsByLoginAccount(loginAccount);
		Feedback fe = new Feedback(boolean1, "");
		fe.setSuccessful(boolean1);
		return fe;
	}

}
