package com.ouchgzee.headTeacher.web.controller.home;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.GjtWorkOrder;
import com.gzedu.xlims.pojo.GjtWorkOrderAssignPerson;
import com.gzedu.xlims.pojo.GjtWorkOrderComment;
import com.gzedu.xlims.pojo.GjtWorkOrderUser;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.home.GjtWorkOrderAssignPersonService;
import com.gzedu.xlims.service.home.GjtWorkOrderCommentService;
import com.gzedu.xlims.service.home.GjtWorkOrderService;
import com.gzedu.xlims.service.home.GjtWorkOrderUserService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 
 * 功能说明：工单管理
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年9月6日
 * @version 3.0
 *
 */

@Controller
@RequestMapping("/home/class/workOrder")
public class BzrWorkOrderController extends BaseController {

	private final static Logger log = LoggerFactory.getLogger(BzrWorkOrderController.class);

	@Autowired
	GjtWorkOrderService gjtWorkOrderService;

	@Autowired
	GjtWorkOrderCommentService gjtWorkOrderCommentService;

	@Autowired
	GjtWorkOrderUserService gjtWorkOrderUserService;

	@Autowired
	GjtWorkOrderAssignPersonService gjtWorkOrderAssignPersonService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	PriRoleInfoService priRoleInfoService;

	// 我收到的工单
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, String createDtBegin, String createDtEnd) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();

		searchParams.put("EQ_gjtWorkOrder.isDeleted", "N");

		searchParams.put("EQ_userId", user.getId());

		if (StringUtils.isNotBlank(createDtBegin)) {
			searchParams.put("GTE_gjtWorkOrder.createdDt", createDtBegin);
		}
		if (StringUtils.isNotBlank(createDtEnd)) {
			searchParams.put("LT_gjtWorkOrder.createdDt", createDtEnd);
		}

		log.info("工单管理-我收到的工单-查询参数：{}", searchParams);
		Page<GjtWorkOrderAssignPerson> pageInfo = gjtWorkOrderAssignPersonService.queryPageInfo(searchParams,
				pageRequst);

		// 已完成
		searchParams.put("EQ_gjtWorkOrder.isState", "1");
		long finishNum = gjtWorkOrderAssignPersonService.queryAllCount(searchParams);

		// 待完成
		searchParams.put("EQ_gjtWorkOrder.isState", "0");
		long waitNum = gjtWorkOrderAssignPersonService.queryAllCount(searchParams);

		// 关闭
		searchParams.put("EQ_gjtWorkOrder.isState", "2");
		long closeNum = gjtWorkOrderAssignPersonService.queryAllCount(searchParams);

		Map<String, String> workTypeMap = commonMapService.getDates("WorkOrderType");
		model.addAttribute("workTypeMap", workTypeMap);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("finishNum", finishNum);
		model.addAttribute("waitNum", waitNum);
		model.addAttribute("closeNum", closeNum);
		model.addAttribute("createDtBegin", createDtBegin);
		model.addAttribute("createDtEnd", createDtEnd);

		return "new/class/workOrder/work_order_get_list";

	}

	// 我发布的工单
	@RequestMapping(value = "putList", method = RequestMethod.GET)
	public String putList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, String createDtBegin, String createDtEnd) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();

		searchParams.put("EQ_isDeleted", "N");

		searchParams.put("EQ_createdBy", user.getId());

		if (StringUtils.isNotBlank(createDtBegin)) {
			searchParams.put("GTE_createdDt", createDtBegin);
		}
		if (StringUtils.isNotBlank(createDtEnd)) {
			searchParams.put("LT_createdDt", createDtEnd);
		}

		log.info("工单管理-我发布的工单-查询参数：{}", searchParams);
		Page<GjtWorkOrder> pageInfo = gjtWorkOrderService.queryPageInfo(searchParams, pageRequst);

		// 已完成
		searchParams.put("EQ_isState", "1");
		long finishNum = gjtWorkOrderService.queryAllCount(searchParams);

		// 待完成
		searchParams.put("EQ_isState", "0");
		long waitNum = gjtWorkOrderService.queryAllCount(searchParams);

		// 关闭
		searchParams.put("EQ_isState", "2");
		long closeNum = gjtWorkOrderService.queryAllCount(searchParams);

		Map<String, String> workTypeMap = commonMapService.getDates("WorkOrderType");
		model.addAttribute("workTypeMap", workTypeMap);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("finishNum", finishNum);
		model.addAttribute("waitNum", waitNum);
		model.addAttribute("closeNum", closeNum);
		model.addAttribute("createDtBegin", createDtBegin);
		model.addAttribute("createDtEnd", createDtEnd);
		return "new/class/workOrder/work_order_put_list";

	}

	// 抄送给我的
	@RequestMapping(value = "copyTolist", method = RequestMethod.GET)
	public String copyTolist(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, String createDtBegin, String createDtEnd) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();

		searchParams.put("EQ_gjtWorkOrder.isDeleted", "N");
		searchParams.put("EQ_userId", user.getId());

		if (StringUtils.isNotBlank(createDtBegin)) {
			searchParams.put("GTE_gjtWorkOrder.createdDt", createDtBegin);
		}
		if (StringUtils.isNotBlank(createDtEnd)) {
			searchParams.put("LT_gjtWorkOrder.createdDt", createDtEnd);
		}

		log.info("工单管理-抄送给我的-查询参数：{}", searchParams);
		Page<GjtWorkOrderUser> pageInfo = gjtWorkOrderUserService.queryPageInfo(searchParams, pageRequst);

		// 已完成
		searchParams.put("EQ_gjtWorkOrder.isState", "1");
		long finishNum = gjtWorkOrderUserService.queryAllCount(searchParams);

		// 待完成
		searchParams.put("EQ_gjtWorkOrder.isState", "0");
		long waitNum = gjtWorkOrderUserService.queryAllCount(searchParams);

		// 关闭
		searchParams.put("EQ_gjtWorkOrder.isState", "2");
		long closeNum = gjtWorkOrderUserService.queryAllCount(searchParams);

		Map<String, String> workTypeMap = commonMapService.getDates("WorkOrderType");
		model.addAttribute("workTypeMap", workTypeMap);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("finishNum", finishNum);
		model.addAttribute("waitNum", waitNum);
		model.addAttribute("closeNum", closeNum);
		model.addAttribute("createDtBegin", createDtBegin);
		model.addAttribute("createDtEnd", createDtEnd);
		return "new/class/workOrder/work_order_copyto_list";

	}

	// 删除评论
	@SysLog("我的工单-删除工单")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Feedback delete(HttpServletRequest request, String ids) {
		Feedback fb = new Feedback(true, "删除成功！");
		try {
			boolean i = gjtWorkOrderService.delete(ids);
			if (!i) {
				fb = new Feedback(true, "删除失败！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(true, "系统异常,删除失败！");
		}

		return fb;
	}

	// 发布任务
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(HttpServletRequest request, Model model) {

		Map<String, String> workTypeMap = commonMapService.getDates("WorkOrderType");

		model.addAttribute("workTypeMap", workTypeMap);
		model.addAttribute("entity", new GjtWorkOrder());
		model.addAttribute("action", "create");
		return "new/class/workOrder/work_order_form";
	}

	@SysLog("我的工单-发布任务")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(@ModelAttribute GjtWorkOrder entity, Model model, HttpServletRequest request,
			String demandDates) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();

		String[] values = request.getParameterValues("users");// 抄送对象
		String[] assignPersons = request.getParameterValues("assignPersons");// 指定对象

		if (EmptyUtils.isEmpty(assignPersons)) {
			return new Feedback(false, "新增失败，指派人不能为空！");
		}

		Feedback feedback = new Feedback(true, "新增成功");
		try {
			entity.setId(UUIDUtils.random().toString());
			entity.setCreatedDt(DateUtils.getNowTime());
			entity.setDemandDate(DateUtils.getYMDToString(demandDates));
			entity.setCreateUser(new GjtUserAccount(user.getId()));
			entity.setIsDeleted("N");
			entity.setIsState("0");
			entity.setVersion(new BigDecimal("1"));
			GjtWorkOrder gjtWorkOrder = gjtWorkOrderService.save(entity);
			if (gjtWorkOrder != null) {
				if (values != null && values.length > 0) {
					Set<String> copyToSet = new HashSet<String>(Arrays.asList(values));// 去重
					Set<String> assignPersonSet = new HashSet<String>(Arrays.asList(assignPersons));// 去重

					for (String assignPersonId : assignPersonSet) {
						GjtWorkOrderAssignPerson item = new GjtWorkOrderAssignPerson();
						item.setId(UUIDUtils.random().toString());
						item.setCreatedBy(user.getId());
						item.setCreatedDt(DateUtils.getNowTime());
						item.setIsDeleted("N");
						item.setAssignPerson(new GjtUserAccount(assignPersonId));
						item.setGjtWorkOrder(gjtWorkOrder);
						gjtWorkOrderAssignPersonService.save(item);
					}

					for (String str : copyToSet) {// 防止既是指定人也是抄送人
						for (String assPer : assignPersonSet) {
							if (str.equals(assPer)) {
								copyToSet.remove(str);
							}
						}
					}

					for (String userId : copyToSet) {
						GjtWorkOrderUser item = new GjtWorkOrderUser();
						item.setId(UUIDUtils.random().toString());
						item.setCreatedBy(user.getId());
						item.setCreatedDt(DateUtils.getNowTime());
						item.setIsDeleted("N");
						item.setGjtUserAccount(new GjtUserAccount(userId));
						item.setGjtWorkOrder(gjtWorkOrder);
						gjtWorkOrderUserService.save(item);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "新增失败");
		}
		return feedback;
	}

	// 修改任务
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(HttpServletRequest request, Model model, String id) {
		GjtWorkOrder entity = gjtWorkOrderService.queryById(id);
		Map<String, String> workTypeMap = commonMapService.getDates("WorkOrderType");

		model.addAttribute("workTypeMap", workTypeMap);
		model.addAttribute("entity", entity);
		model.addAttribute("action", "update");
		return "new/class/workOrder/work_order_form";
	}

	@SysLog("我的工单-修改任务")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(HttpServletRequest request, Model model, @ModelAttribute GjtWorkOrder entity,
			String demandDates) {
		Feedback feedback = new Feedback(true, "修改成功");
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();

		GjtWorkOrder item = gjtWorkOrderService.queryById(entity.getId());
		try {
			item.setTitle(entity.getTitle());
			item.setContent(entity.getContent());
			item.setPriority(entity.getPriority());
			item.setWorkOrderType(entity.getWorkOrderType());
			item.setDemandDate(DateUtils.getYMDToString(demandDates));
			item.setFileName(entity.getFileName());
			item.setFileUrl(entity.getFileUrl());
			item.setUpdatedBy(user.getId());
			item.setUpdatedDt(DateUtils.getNowTime());
			gjtWorkOrderService.update(item);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "修改失败");
		}
		return feedback;
	}

	@SysLog("我的工单-修改任务进度")
	@RequestMapping(value = "updateState", method = RequestMethod.GET)
	@ResponseBody
	public Feedback updateState(HttpServletRequest request, Model model, String id, String state) {
		Feedback feedback = new Feedback(true, "操作成功");
		try {
			log.info("工单修改任务进度状态参数：id={},state={}", id, state);
			gjtWorkOrderService.updateIsState(id, state);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "操作失败");
		}
		return feedback;
	}

	// 查询角色用户列表
	@RequestMapping(value = "findRole", method = { RequestMethod.GET })
	public String findRole(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, HttpServletRequest request,
			Model model, String type) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);

		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		List<PriRoleInfo> queryAll = priRoleInfoService.queryAll();

		Map<String, String> rolesMap = new HashMap<String, String>();
		for (PriRoleInfo priRoleInfo : queryAll) {
			String roleId = priRoleInfo.getRoleId();
			// 以下角色不显示在下拉
			boolean bool = !"d2b2aa26c02f7bf10d2dda23a91522ab".equals(roleId)// 系统管理员
					|| !"be60d336bc1946a5a24f88d5ae594b17".equals(roleId)// 辅导
					|| !"699f6f83acf54548bfae7794915a3cf3".equals(roleId)// 督导
					|| !"04479d3652aa40ea9d99dc6e161ffcc0".equals(roleId)// 班主任（无考试院校模式）
					|| !"5e2ed936304f40bd99d6e5d6c09b47ba".equals(roleId)// 班主任（有考试院校模式）
					|| !"e272f056e6a242179b2414eac69814fa".equals(roleId);// 教育项目管理员（演示）
			if (bool) {
				rolesMap.put(roleId, priRoleInfo.getRoleName());
			}
		}

		if (EmptyUtils.isEmpty(searchParams.get("EQ_priRoleInfo.roleId"))) {
			searchParams.put("IN_priRoleInfo.roleId", rolesMap.keySet());
		}

		Page<Map<String, Object>> pageInfo = gjtUserAccountService.queryPage(user.getGjtOrg().getId(), searchParams,
				pageRequst);

		model.addAttribute("rolesMap", rolesMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("type", type);
		if ("1".equals(type)) {
			return "new/class/workOrder/add_object";
		} else {
			return "new/class/workOrder/add_objects";
		}
	}

	// 详情
	@RequestMapping(value = "view", method = { RequestMethod.GET })
	public String view(HttpServletRequest request, Model model, String id, String type) {
		GjtWorkOrder info = gjtWorkOrderService.queryById(id);
		List<GjtWorkOrderUser> orderUserList = info.getGjtWorkOrderUserList();
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();

		StringBuffer users = new StringBuffer();

		if (orderUserList != null && orderUserList.size() > 0) {
			for (int i = 0; i < orderUserList.size(); i++) {
				GjtWorkOrderUser orderUser = orderUserList.get(i);
				GjtUserAccount account = orderUser.getGjtUserAccount();
				users.append(account.getRealName());
				users.append("（");
				users.append(account.getPriRoleInfo().getRoleName());
				users.append("）");
				if (i < (orderUserList.size() - 1)) {
					users.append(",");
				}
			}
		}

		List<GjtWorkOrderAssignPerson> assignPersonList = info.getGjtWorkOrderAssignPersonList();
		StringBuffer assignPersons = new StringBuffer();
		if (assignPersonList != null && assignPersonList.size() > 0) {
			for (int i = 0; i < assignPersonList.size(); i++) {
				GjtWorkOrderAssignPerson assignPerson = assignPersonList.get(i);
				GjtUserAccount account = assignPerson.getAssignPerson();
				assignPersons.append(account.getRealName());
				assignPersons.append("（");
				assignPersons.append(account.getPriRoleInfo().getRoleName());
				assignPersons.append("）");
				if (i < (assignPersonList.size() - 1)) {
					assignPersons.append(",");
				}
			}
		}

		List<GjtWorkOrderComment> commentList = info.getGjtWorkOrderCommentList();

		Map<String, String> workTypeMap = commonMapService.getDates("WorkOrderType");
		model.addAttribute("workTypeMap", workTypeMap);

		model.addAttribute("assignPersons", assignPersons);
		model.addAttribute("users", users);
		model.addAttribute("info", info);
		model.addAttribute("commentList", commentList);
		model.addAttribute("commentCount", commentList.size());
		model.addAttribute("type", type);
		model.addAttribute("currentUser", user);

		return "new/class/workOrder/work_order_view";
	}

	// 发表评论
	@RequestMapping(value = "createComment", method = RequestMethod.GET)
	@ResponseBody
	public Feedback createComment(HttpServletRequest request, Model model, String content, String id) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();
		Feedback feedback = new Feedback(true, "操作成功");
		GjtWorkOrderComment item = new GjtWorkOrderComment();
		GjtWorkOrder workOrder = gjtWorkOrderService.queryById(id);
		try {
			item.setGjtWorkOrder(workOrder);
			item.setContent(content);
			item.setGjtUserAccount(new GjtUserAccount(user.getId()));
			item.setCreatedBy(user.getId());
			item.setCreatedDt(DateUtils.getNowTime());
			item.setId(UUIDUtils.random().toString());
			item.setVersion(new BigDecimal(1));
			item.setIsDeleted("N");
			gjtWorkOrderCommentService.save(item);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "操作失败");
		}
		return feedback;
	}

	// 删除评论
	@RequestMapping(value = "deleteComment", method = RequestMethod.GET)
	@ResponseBody
	public Feedback deleteComment(HttpServletRequest request, Model model, String id) {
		Feedback feedback = new Feedback(true, "删除成功");
		try {
			gjtWorkOrderCommentService.delete(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "删除失败");
		}
		return feedback;
	}

}
