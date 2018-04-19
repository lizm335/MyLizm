package com.gzedu.xlims.web.controller.graduation;

/**
 * 毕业申请控制层<br/>
 * 逻辑有误，弃用 迁移到com.gzedu.xlims.web.controller.graduation.GjtGraduationApplyCertifController
 * @author 黄一飞 huangyifei@eenet.com
 * @CreateDate 2017年08月29日
 * @EditDate 2017年09月16日
 * @version 3.0
 */
//@Controller
//@RequestMapping("/graduation/applyFor")
//public class GjtGraduationApplyForController extends BaseController {
//
//	private static final Log log = LogFactory.getLog(GjtGraduationApplyForController.class);
//
//	@Autowired
//	private GjtGraduationApplyService gjtGraduationApplyService;
//
//	@Autowired
//	private CommonMapService commonMapService;
//
//	/**
//	 * 查询毕业申请列表
//	 * @param pageNumber
//	 * @param pageSize
//	 * @return
//	 */
//	@RequestMapping(value = "list")
//	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
//			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
//			Model model, HttpServletRequest request) {
//		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
//		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
//		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
//		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
//		Page<GjtGraduationApply> pageInfo = gjtGraduationApplyService.queryGraduationApplyByPage(searchParams, pageRequst);
//
//		Map<String, String> graduationPlanMap = commonMapService.getGraduationPlanMap(user.getGjtOrg().getId());// 毕业计划
//		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心
//		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
//		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 学期
//		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());
//
//		model.addAttribute("pageInfo", pageInfo);
//		model.addAttribute("graduationPlanMap", graduationPlanMap);
//		model.addAttribute("studyCenterMap", studyCenterMap);
//		model.addAttribute("specialtyMap", specialtyMap);
//		model.addAttribute("gradeMap", gradeMap);
//		model.addAttribute("pyccMap", pyccMap);
//		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
//		return "graduation/applyFor/graduation_applyFor_list";
//	}
//
//}
