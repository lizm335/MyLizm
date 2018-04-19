
package com.gzedu.xlims.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * 功能说明：(废弃）
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月6日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/usermanage/adminteacher")
public class AdminTeacherController {
	//
	// @Autowired
	// GjtUserAccountService gjtUserAccountService;
	//
	// @Autowired
	// GjtSchoolInfoService gjtSchoolInfoService;
	//
	// @Autowired
	// GjtStudyCenterService gjtStudyCenterService;
	//
	// @Autowired
	// GjtEmployeeInfoService gjtEmployeeInfoService;
	//
	// @Autowired
	// GjtOrgService gjtOrgService;
	//
	// @Autowired
	// @Qualifier("adminCommonMapService")
	// CommonMapService CommonMapService;
	//
	// @RequestMapping(value = "list", method = RequestMethod.GET)
	// public String list(@RequestParam(value = "page", defaultValue = "1") int
	// pageNumber,
	// @RequestParam(value = "page.size", defaultValue = "10") int pageSize,
	// Model model, ServletRequest request,
	// HttpSession session) {
	// GjtUserAccount user =
	// gjtUserAccountService.findOne(session.getAttribute("userId").toString());
	//
	// // if (user == null) {
	// // // 退出登录
	// // }
	//
	// // 是否是超级管理员
	// Map<String, String> orgMap = new HashMap();
	// if (user.getIsSuperMgr()) {
	// orgMap = CommonMapService.getOrgMap();
	// } else {
	// // orgMap = user.getUserOrgMap();
	// }
	//
	// PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
	// Map<String, Object> searchParams =
	// Servlets.getParametersStartingWith(request, "search_");
	//
	// Page<GjtUserAccount> pageInfo =
	// gjtUserAccountService.queryAll(searchParams, pageRequst);
	//
	// model.addAttribute("pageInfo", pageInfo);
	// model.addAttribute("orgMap", orgMap);
	// 
	// model.addAttribute("searchParams",
	// Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
	// return "usermanage/adminteacher/list";
	// }
	//
	// @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	// public String viewForm(@PathVariable("id") String id, Model model) {
	// Map<String, String> orgMap = CommonMapService.getOrgMap();
	// GjtUserAccount gjtUserAccount = gjtUserAccountService.findOne(id);
	//
	// model.addAttribute("orgMap", orgMap);
	// model.addAttribute("item", gjtUserAccount);
	// model.addAttribute("action", "view");
	// return "usermanage/adminteacher/form";
	// }
	//
	// @RequestMapping(value = "create", method = RequestMethod.GET)
	// public String createForm(Model model, HttpSession session) {
	// GjtUserAccount user =
	// gjtUserAccountService.findOne(session.getAttribute("userId").toString());
	// Map<String, String> orgMap = new HashMap();
	// if (user.getIsSuperMgr()) {
	// orgMap = CommonMapService.getOrgMap();
	// } else {
	// // orgMap = user.getUserOrgMap();
	// }
	//
	// model.addAttribute("item", new GjtUserAccount());
	// model.addAttribute("orgMap", orgMap);
	// model.addAttribute("action", "create");
	// return "usermanage/adminteacher/form";
	// }
	//
	// @RequestMapping(value = "create", method = RequestMethod.POST)
	// public String create(@Valid GjtUserAccount item, RedirectAttributes
	// redirectAttributes, String gjtOrgId) {
	// GjtOrg gjtOrg = gjtOrgService.queryById(gjtOrgId);
	// Feedback feedback = new Feedback(true, "创建成功");
	// try {
	// item.setGjtOrg(gjtOrg);
	// item.setId(UUIDUtils.random());
	// item.setUserType(UserTypeEnum.管理员.getNum());
	// item.setPassword(Md5Util.encode(item.getPassword()));
	// item.setCreatedDt(DateUtils.getNowTime());
	// item.setIsDeleted("N");
	// item.setIsEnabled(true);
	// item.setOrgCode(gjtOrg.getCode());
	// gjtUserAccountService.saveEntity(item);
	// } catch (Exception e) {
	// feedback = new Feedback(false, "创建失败");
	// }
	// redirectAttributes.addFlashAttribute("feedback", feedback);
	// return "redirect:/usermanage/adminteacher/list";
	// }
	//
	// @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	// public String updateForm(@PathVariable("id") String id, Model model,
	// HttpSession session) {
	// GjtUserAccount user =
	// gjtUserAccountService.findOne(session.getAttribute("userId").toString());
	// Map<String, String> orgMap = new HashMap();
	// if (user.getIsSuperMgr()) {
	// orgMap = CommonMapService.getOrgMap();
	// } else {
	// // orgMap = user.getUserOrgMap();
	// }
	// GjtUserAccount gjtUserAccount = gjtUserAccountService.findOne(id);
	//
	// model.addAttribute("item", gjtUserAccount);
	// model.addAttribute("orgMap", orgMap);
	// model.addAttribute("action", "update");
	// return "usermanage/adminteacher/form";
	// }
	//
	// @RequestMapping(value = "update", method = RequestMethod.POST)
	// public String update(GjtUserAccount item, RedirectAttributes
	// redirectAttributes, String gjtOrgId) {
	// Feedback feedback = new Feedback(true, "更新成功");
	// GjtOrg gjtOrg = gjtOrgService.queryById(gjtOrgId);
	// GjtUserAccount gjtUserAccount =
	// gjtUserAccountService.findOne(item.getId());
	//
	// gjtUserAccount.setGjtOrg(gjtOrg);
	// gjtUserAccount.setLoginAccount(item.getLoginAccount());
	// gjtUserAccount.setRealName(item.getRealName());
	// gjtUserAccount.setGrantType(item.getGrantType());
	// gjtUserAccount.setPassword(Md5Util.encode(item.getPassword()));
	// gjtUserAccount.setPassword2(item.getPassword2());
	//
	// try {
	// gjtUserAccountService.update(item);
	// } catch (Exception e) {
	// feedback = new Feedback(false, "更新失败");
	// }
	// redirectAttributes.addFlashAttribute("feedback", feedback);
	// return "redirect:/usermanage/adminteacher/list";
	// }
	//
	// // 单个删除和多个删除
	// @RequestMapping(value = "delete")
	// public @ResponseBody Feedback delete(String ids) throws IOException {
	// if (StringUtils.isNotBlank(ids)) {
	// String[] selectedIds = ids.split(",");
	// try {
	// gjtUserAccountService.deleteById(selectedIds);
	// return new Feedback(true, "删除成功");
	// } catch (Exception e) {
	// return new Feedback(false, "删除失败");
	// }
	// }
	// return new Feedback(false, "删除失败");
	// }
	//
	// // 校验帐号是否存在
	// @RequestMapping(value = "checkLogin")
	// @ResponseBody
	// public Feedback checkLogin(String loginAccount) throws IOException {
	// Boolean boolean1 =
	// gjtUserAccountService.queryByLoginAccount(loginAccount);
	// Feedback fe = new Feedback(boolean1, "");
	// fe.setSuccessful(boolean1);
	// return fe;
	// }
}
