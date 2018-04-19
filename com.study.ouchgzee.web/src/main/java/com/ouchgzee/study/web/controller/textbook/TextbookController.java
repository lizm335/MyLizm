package com.ouchgzee.study.web.controller.textbook;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.common.constants.CacheConstants.OrderType;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.common.gzedu.SignUtil;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtStudentAddress;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.ExpressEnum;
import com.gzedu.xlims.pojo.status.TermType;
import com.gzedu.xlims.pojo.textbook.GjtStudentTextbookOrder;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistribute;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistributeDetail;
import com.gzedu.xlims.pojo.textbook.GjtTextbookFeedback;
import com.gzedu.xlims.pojo.textbook.GjtTextbookFeedbackDetail;
import com.gzedu.xlims.pojo.textbook.GjtTextbookFreight;
import com.gzedu.xlims.pojo.textbook.GjtTextbookPlan;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.dictionary.GjtDistrictService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.service.textbook.GjtStudentAddressService;
import com.gzedu.xlims.service.textbook.GjtStudentTextbookOrderService;
import com.gzedu.xlims.service.textbook.GjtTextbookDistributeService;
import com.gzedu.xlims.service.textbook.GjtTextbookFeedbackService;
import com.gzedu.xlims.service.textbook.GjtTextbookFreightService;
import com.gzedu.xlims.service.textbook.GjtTextbookPlanService;
import com.gzedu.xlims.service.textbook.GjtTextbookService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.TextbookAddressComponent;
import com.ouchgzee.study.web.vo.GjtStudentAddressVO;
import com.ouchgzee.study.web.vo.GjtStudyCenterVO;
import com.ouchgzee.study.web.vo.GjtTextbookDistributeVO;
import com.ouchgzee.study.web.vo.TearmVO;
import com.ouchgzee.study.web.vo.TextBookPlanVO;
import com.ouchgzee.study.web.vo.TextbookVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/pcenter/textbook")
public class TextbookController extends BaseController {

	private static final Log log = LogFactory.getLog(TextbookController.class);
	@Autowired
	private GjtTextbookDistributeService gjtTextbookDistributeService;

	@Autowired
	private GjtTeachPlanService gjtTeachPlanService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtTextbookPlanService gjtTextbookPlanService;

	@Autowired
	private GjtTextbookFeedbackService gjtTextbookFeedbackService;

	@Autowired
	private GjtStudentAddressService gjtStudentAddressService;
	@Autowired
	private CacheService cacheService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtStudyCenterService gjtStudyCenterService;

	@Autowired
	private GjtDistrictService gjtDistrictService;

	@Autowired
	private GjtTextbookService gjtTextbookService;

	@Autowired
	private CodeGeneratorService codeGeneratorService;

	@Autowired
	private GjtTextbookFreightService gjtTextbookFreightService;


	@Autowired
	private GjtStudentTextbookOrderService gjtStudentTextbookOrderService;

	@Autowired
	private TextbookAddressComponent textbookAddressComponent;

	@Value("#{configProperties['pcenterStudyServer']}")
	private String pcenterStudyServer;


	/**
	 * 根据学员ID查询学员的每个学期的教材发放列表
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年3月30日 下午7:10:21
	 * @param request
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryTextbookDistributeList", method = RequestMethod.GET)
	public List<TearmVO> queryTextbookDistributeList(HttpServletRequest request, HttpSession session) {
		GjtStudentInfo studentInfo = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);

		// 查询学员的教学计划列表，并按照学期归并
		List<GjtTeachPlan> gjtTeachPlans = gjtTeachPlanService.queryGjtTeachPlan(studentInfo.getGradeSpecialtyId());
		if (CollectionUtils.isEmpty(gjtTeachPlans))
			return null;

		List<GjtTextbookDistribute> textbookDistributes = gjtTextbookDistributeService
				.findByStudentIdAndIsDeletedAndStatusIn(studentInfo.getStudentId(), "N",
						Arrays.asList(new Integer[] { 1, 2, 3 }));

		// 构建学生发放教材的id与发放状态对应map
		Map<String, GjtTextbookDistributeDetail> distributeMap = new HashMap<String, GjtTextbookDistributeDetail>();
		if (CollectionUtils.isNotEmpty(textbookDistributes)) {
			for (GjtTextbookDistribute textbookDistribute : textbookDistributes) {
				List<GjtTextbookDistributeDetail> textbookDistributeDetails = textbookDistribute
						.getGjtTextbookDistributeDetails();
				for (GjtTextbookDistributeDetail detail : textbookDistributeDetails) {
					distributeMap.put(detail.getTextbookId(), detail);
				}
			}
		}
		Collections.sort(gjtTeachPlans, new Comparator<GjtTeachPlan>() {
			@Override
			public int compare(GjtTeachPlan o1, GjtTeachPlan o2) {
				return Integer.valueOf(o1.getKkxq()).compareTo(o2.getKkxq());
			}
		});
		// 2017秋之前的学期全部默认为“已发放”状态
		List<GjtGrade> gradeList = gjtGradeService.findGradesBefore(studentInfo.getXxId(),
				"e8748bbe49c7442584f7a2c8347aef41");
		List<String> gradeIds = new ArrayList<String>();
		for (GjtGrade g : gradeList) {
			gradeIds.add(g.getGradeId());
		}
		Map<Integer, TearmVO> result = new LinkedHashMap<Integer, TearmVO>();
		TearmVO term = null;
		for (GjtTeachPlan plan : gjtTeachPlans) {
			if (result.containsKey(plan.getKkxq())) {
				term = result.get(plan.getKkxq());
			} else {
				term = new TearmVO();
				term.setTermName(TermType.getName(String.valueOf(plan.getKkxq())));
				result.put(plan.getKkxq(), term);
			}
			boolean isSign = gradeIds.contains(plan.getActualGradeId());
			term.addCourse(plan, distributeMap, isSign);
		}

		return new ArrayList<TearmVO>(result.values());
	}

	/**
	 * 查询学员的教材配送列表
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年3月30日 下午7:11:25
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "queryTextbookDeliveryList", method = RequestMethod.GET)
	public Map<String, Object> queryTextbookDeliveryList(HttpServletRequest request, HttpSession session,
			@RequestParam Integer status) throws Exception {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<GjtTextbookDistribute> textbookDistributes = gjtTextbookDistributeService
				.findByStudentIdAndIsDeletedAndStatus(student.getStudentId(), "N", status);
		if (CollectionUtils.isNotEmpty(textbookDistributes)) {
			List<GjtTextbookDistributeVO> list = new ArrayList<GjtTextbookDistributeVO>();
			for (GjtTextbookDistribute tbd : textbookDistributes) {
				list.add(new GjtTextbookDistributeVO(tbd));
			}
			resultMap.put("allowFeedback", false);
			resultMap.put("deliveryList", list);
		}

		GjtStudyCenter studyCenter = gjtStudyCenterService.queryById(student.getXxzxId());
		if (!"3".equals(studyCenter.getGjtOrg().getOrgType())) {
			studyCenter = gjtStudyCenterService.queryById(studyCenter.getGjtOrg().getParentGjtOrg().getId());
		}
		if (studyCenter != null && StringUtils.isNoneBlank(studyCenter.getServiceArea())) {
			String[] serviceArea = studyCenter.getServiceArea().split(",");
			if (ArrayUtils.contains(serviceArea, "5")) {
				GjtStudyCenterVO scVO = new GjtStudyCenterVO(studyCenter);
				String district=studyCenter.getDistrict();
				if(StringUtils.isNoneBlank(district)){
					district = gjtDistrictService.queryAllNameById(district);
				}
				scVO.setOfficeAddr(
						StringUtils.defaultString(district) + StringUtils.defaultString(studyCenter.getOfficeAddr()));
				resultMap.put("studyCenter", scVO);
			}
		}

		return resultMap;
	}

	/**
	 * 查询学员的教材配送明细
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年3月30日 下午7:12:02
	 * @param session
	 * @param distributeId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "queryTextbookDeliveryDetail", method = RequestMethod.GET)
	public GjtTextbookDistributeVO queryTextbookDeliveryDetail(HttpSession session, @RequestParam String distributeId)
			throws Exception {
		GjtStudentInfo studentInfo = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtTextbookDistribute tbd = gjtTextbookDistributeService.queryById(distributeId);
		if (tbd == null)
			return null;
		if (!tbd.getStudentId().equals(studentInfo.getStudentId())) {
			throw new CommonException(MessageCode.PERMISSION_DENIED);
		}
		return new GjtTextbookDistributeVO(tbd);
	}

	/**
	 * 学员确认收到教材
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年3月30日 下午7:09:07
	 * @param session
	 * @param distributeId
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "confirmOrder", method = RequestMethod.GET)
	public String confirmOrder(HttpSession session, @RequestParam String distributeId) throws Exception {
		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtTextbookDistribute tbd = gjtTextbookDistributeService.queryById(distributeId);

		// String textbookId =
		// tbd.getGjtTextbookDistributeDetails().get(0).getTextbookId();
		// List<GjtTextbookOrderDetail> orderDetails =
		// gjtTextbookOrderDetailService
		// .findByStudentIdAndTextbookIdAndStatus(student.getStudentId(),
		// textbookId, 3);
		// GjtTextbookPlan textbookPlan =
		// orderDetails.get(0).getGjtTextbookPlan();
		// if (textbookPlan == null) {
		// log.info("GjtTextbookPlan is null");
		// throw new CommonException(MessageCode.BIZ_ERROR.getMsgCode(),
		// "未设置教材计划");
		// }
		// Date startDate = textbookPlan.getOaddressConfirmSdate();
		// Date endDate = com.gzedu.xlims.common.DateUtil.dateAddDay(textbookPlan.getOaddressConfirmEdate(), 1); // 结束时间精确到23:59分，所以在日期的基础上+1
		// Date now = new Date();
		// if (now.getTime() < startDate.getTime() || now.getTime() >
		// endDate.getTime()) {
		// throw new CommonException(MessageCode.BIZ_ERROR.getMsgCode(),
		// "不在确认收货时间范围内");
		// }

		/*
		 * if (tbd == null) { log.info("GjtTextbookDistribute is null"); throw
		 * new CommonException(MessageCode.BIZ_ERROR); }
		 */
		if (!tbd.getStudentId().equals(student.getStudentId())) {
			throw new CommonException(MessageCode.PERMISSION_DENIED);
		}
		tbd.setUpdatedBy(user.getId());
		gjtTextbookDistributeService.updateTextbookDistributeAndOrderDetai(tbd);
		// TODO 返回结果处理
		return "";
	}

	/**
	 * 根据学员ID查询学员的收货地址(已弃用)
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年3月30日 下午7:09:43
	 * @param session
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping(value = "queryDeliveryAddress", method = RequestMethod.GET)
	public List<GjtStudentAddressVO> queryDeliveryAddress(HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		List<GjtStudentAddress> addressList = gjtStudentAddressService.findByStudentId(student.getStudentId());
		if (CollectionUtils.isEmpty(addressList)) {
			GjtStudentInfo info = gjtStudentInfoService.queryById(student.getStudentId());
			GjtStudentAddress gjtStudentAddress = new GjtStudentAddress();
			gjtStudentAddress.setAddressId(UUIDUtils.random());
			gjtStudentAddress.setStudentId(student.getStudentId());
			gjtStudentAddress.setAddress(info.getTxdz());
			gjtStudentAddress.setIsDefault(1);
			gjtStudentAddress.setMobile(info.getSjh());
			gjtStudentAddress.setReceiver(info.getXm());
			gjtStudentAddress.setCreatedBy(user.getId());
			gjtStudentAddress.setCreatedDt(DateUtil.getDate());
			gjtStudentAddress.setIsDeleted("N");
			gjtStudentAddress = gjtStudentAddressService.save(gjtStudentAddress);
			addressList = new ArrayList<GjtStudentAddress>();
			addressList.add(gjtStudentAddress);

		}
		List<GjtStudentAddressVO> result = new ArrayList<GjtStudentAddressVO>();
		for (GjtStudentAddress address : addressList) {
			GjtStudentAddressVO studentAddressVO = new GjtStudentAddressVO(address);
			String key = CacheService.DictionaryKey.PROVINCE;
			studentAddressVO.setProvince(cacheService.getCachedDictionaryName(key, address.getProvinceCode()));
			if (StringUtils.isNoneEmpty(address.getCityCode())) {
				key = CacheService.DictionaryKey.CITY.replace("${Province}", address.getProvinceCode());
				studentAddressVO.setCity(cacheService.getCachedDictionaryName(key, address.getCityCode()));
				if (StringUtils.isNoneEmpty(address.getAreaCode())) {
					key = CacheService.DictionaryKey.AREA.replace("${Province}", address.getProvinceCode());
					key = key.replace("${City}", address.getCityCode());
					studentAddressVO.setArea(cacheService.getCachedDictionaryName(key, address.getAreaCode()));
				}
			}
			result.add(studentAddressVO);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("addressList", result);
		List<GjtTextbookPlan> plans = gjtTextbookPlanService.findByOrgIdAndSysdate(student.getXxId(), student.getGjtGrade().getGradeId());
		boolean allowUpdate = false;
		for (GjtTextbookPlan p : plans) {
			Date startDate = p.getOaddressConfirmSdate(), endDate = com.gzedu.xlims.common.DateUtil.dateAddDay(p.getOaddressConfirmEdate(), 1); // 结束时间精确到23:59分，所以在日期的基础上+1
			long current = System.currentTimeMillis();
			if (current >= startDate.getTime() && current <= endDate.getTime()) {
				allowUpdate = true;
				break;
			}
		}
		resultMap.put("allowUpdate", allowUpdate);
		return result;
	}

	/**
	 * 根据学员ID查询学员的收货地址 PC版本
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月20日 下午3:38:57
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryDeliveryAddressPC", method = RequestMethod.GET)
	public Map<String, Object> queryDeliveryAddressPC(HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<GjtStudentAddress> addressList = gjtStudentAddressService.findByStudentId(student.getStudentId());
		resultMap.put("allowUpdate", true);
		List<GjtStudentAddressVO> result = new ArrayList<GjtStudentAddressVO>();
		if (CollectionUtils.isEmpty(addressList)) {
			resultMap.put("addressList", result);
			return resultMap;
		}

		for (GjtStudentAddress address : addressList) {
			GjtStudentAddressVO studentAddressVO = new GjtStudentAddressVO(address);
			String key = CacheService.DictionaryKey.PROVINCE;
			studentAddressVO.setProvince(cacheService.getCachedDictionaryName(key, address.getProvinceCode()));
			if (StringUtils.isNoneEmpty(address.getCityCode())) {
				key = CacheService.DictionaryKey.CITY.replace("${Province}", address.getProvinceCode());
				studentAddressVO.setCity(cacheService.getCachedDictionaryName(key, address.getCityCode()));
				if (StringUtils.isNoneEmpty(address.getAreaCode())) {
					key = CacheService.DictionaryKey.AREA.replace("${Province}", address.getProvinceCode());
					key = key.replace("${City}", address.getCityCode());
					studentAddressVO.setArea(cacheService.getCachedDictionaryName(key, address.getAreaCode()));
				}
			}
			result.add(studentAddressVO);
		}

		resultMap.put("addressList", result);
		return resultMap;
	}

	/**
	 * 修改学员的收货地址
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年3月30日 下午7:11:05
	 * @param session
	 * @param gjtStudentAddressVO
	 */
	@ResponseBody
	@RequestMapping(value = "updateDeliveryAddress", method = RequestMethod.POST)
	public String updateDeliveryAddress(@Valid GjtStudentAddressVO gjtStudentAddressVO, HttpSession session) {
		GjtUserAccount gjtUserAccount = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		GjtStudentAddress gjtStudentAddress = gjtStudentAddressService.findOne(gjtStudentAddressVO.getAddressId());
		gjtStudentAddress.setMobile(gjtStudentAddressVO.getMobile());
		gjtStudentAddress.setReceiver(gjtStudentAddressVO.getReceiver());
		gjtStudentAddress.setProvinceCode(gjtStudentAddressVO.getProvinceCode());
		gjtStudentAddress.setCityCode(gjtStudentAddressVO.getCityCode());
		gjtStudentAddress.setAreaCode(gjtStudentAddressVO.getAreaCode());
		gjtStudentAddress.setAddress(gjtStudentAddressVO.getAddress());
		gjtStudentAddress.setUpdatedBy(gjtUserAccount.getId());
		gjtStudentAddress.setUpdatedDt(DateUtil.getDate());
		gjtStudentAddressService.save(gjtStudentAddress);
		// TODO 返回结果处理
		return "";
	}

	/**
	 * 新增收货地址
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月25日 下午1:52:14
	 * @param gjtStudentAddress
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addDeliveryAddress", method = RequestMethod.POST)
	public String addDeliveryAddress(@Valid GjtStudentAddress gjtStudentAddress, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		gjtStudentAddress.setAddressId(UUIDUtils.random());
		gjtStudentAddress.setStudentId(student.getStudentId());
		gjtStudentAddress.setCreatedBy(user.getId());
		gjtStudentAddress.setCreatedDt(DateUtil.getDate());
		gjtStudentAddress.setIsDeleted("N");
		gjtStudentAddressService.save(gjtStudentAddress);
		// TODO 返回结果处理
		return "";
	}

	@ResponseBody
	@RequestMapping(value = "deleteDeliveryAddress", method = RequestMethod.GET)
	public String deleteDeliveryAddress(@RequestParam String addressId, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		GjtStudentAddress address = gjtStudentAddressService.findOne(addressId);
		address.setIsDeleted("Y");
		address.setUpdatedBy(user.getId());
		address.setUpdatedDt(DateUtils.getDate());
		gjtStudentAddressService.save(address);
		// TODO 返回结果处理
		return "";
	}

	@ResponseBody
	@RequestMapping(value = "setDefualtAddress", method = RequestMethod.GET)
	public String setDefualtAddress(@RequestParam String addressId, HttpSession session) throws CommonException {
		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtStudentAddress address = new GjtStudentAddress();
		address.setAddressId(addressId);
		address.setUpdatedBy(user.getId());
		address.setStudentId(student.getStudentId());
		gjtStudentAddressService.updateDefualtAddress(address);
		// TODO 返回结果处理
		return "";
	}

	@ResponseBody
	@RequestMapping(value = "queryArea", method = RequestMethod.GET)
	public List<com.gzedu.xlims.service.CacheService.Value> queryArea(@RequestParam String type, String code, HttpSession session)
			throws CommonException {
		String[] arr = { "province", "city", "area" };
		if (!ArrayUtils.contains(arr, type)) {
			throw new CommonException(MessageCode.BAD_REQUEST);
		}
		if ("province".equals(type)) {
			return cacheService.getCachedDictionary(CacheService.DictionaryKey.PROVINCE);
		}
		if (StringUtils.isBlank(code) && code.length() != 6) {
			throw new CommonException(MessageCode.BAD_REQUEST);
		}
		if ("city".equals(type)) {
			return cacheService.getCachedDictionary(CacheService.DictionaryKey.CITY.replace("${Province}", code));
		} else if ("area".equals(type)) {
			String p = code.substring(0, 2) + "0000";// 省编码
			String key = CacheService.DictionaryKey.AREA.replace("${Province}", p).replace("${City}", code);
			return cacheService.getCachedDictionary(key);
		}
		return null;
	}

	/**
	 * 查询物流详情
	 * @author ouguohao@eenet.com
	 * @Date 2017年3月31日 上午10:43:39
	 * @param distributeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryLogistics", method = RequestMethod.GET)
	public Object queryLogistics(@RequestParam String distributeId) {
		GjtTextbookDistribute distribute = gjtTextbookDistributeService.queryById(distributeId);
		String[] logistics = distribute.getLogisticsComp().split("-");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", logistics[1]);
		params.put("postid", distribute.getWaybillCode());
		String s = HttpClientUtils.doHttpPost("https://www.kuaidi100.com/query", params, 10000, "utf-8");
		JSONObject result = JSONObject.fromObject(s);
		List<JSONObject> list = new ArrayList<JSONObject>();
		if (result.get("status") != null && "200".equals(result.getString("status"))) {
			if (result != null && result.get("data") != null) {
				JSONArray arr = result.getJSONArray("data");
				for (int i = 0; i < arr.size(); i++) {
					JSONObject json = arr.getJSONObject(i);
					JSONObject obj = new JSONObject();
					obj.put("time", json.get("time"));
					obj.put("context", json.get("context"));
					list.add(obj);
				}
			}
		} else if(ExpressEnum.shunfeng.getValue().equals(logistics[1])) { // 由于快递100接口不支持顺丰快递查询
			JSONObject obj = new JSONObject();
			obj.put("context", "请前往 <a href=\"http://www.sf-express.com/cn/sc/dynamic_function/waybill/#search/bill-number/"+distribute.getWaybillCode()+"\" target=\"_blank\">顺丰快递官网</a> 查询");
			list.add(obj);
		}
		JSONObject json = new JSONObject();
		json.put("waybillCode", distribute.getWaybillCode());
		json.put("logisticsComp", logistics[0]);
		json.put("logisticsCode", logistics[1]);
		json.put("logisticsDetails", list);
		return json;
	}

	/**
	 * 当学员对教材发放有疑问时，提交反馈
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年3月31日 上午10:44:53
	 * @param distributeId
	 * @param feedbackType
	 * @param reason
	 * @return
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "addTextbookFeedback", method = RequestMethod.POST)
	public String addTextbookFeedback(HttpSession session, @RequestParam String distributeId,
			@RequestParam int feedbackType, @RequestParam String reason, String textbookId) throws CommonException {
		GjtUserAccount gjtUserAccount = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		// 当前学期
		// GjtGrade gjtGrade =
		// gjtGradeService.findCurrentGrade(student.getXxId());
		// gjtTextbookOrderDetailService.findByStudentIdAndTextbookIdAndStatus(student.getStudentId(),
		// textbookId, 3);
		// GjtTextbookPlan textbookPlan =
		// gjtTextbookPlanService.findByGradeIdAndOrgId(gjtGrade.getGradeId(),
		// student.getXxId());
		// if (textbookPlan == null) {
		// log.info("GjtTextbookPlan is null");
		// throw new CommonException(MessageCode.BIZ_ERROR, "未设置配送计划");
		// }
		// Date startDate = textbookPlan.getOfeedbackSdate();
		// Date endDate = textbookPlan.getOfeedbackEdate();
		// Date now = new Date();
		// if (now.getTime() < startDate.getTime() || now.getTime() >
		// endDate.getTime()) {
		// throw new CommonException(MessageCode.BIZ_ERROR.getMsgCode(),
		// "不在反馈时间范围内");
		// }
		GjtTextbookDistribute tbd = gjtTextbookDistributeService.queryById(distributeId);
		if (!tbd.getStudentId().equals(student.getStudentId())) {
			throw new CommonException(MessageCode.PERMISSION_DENIED);
		}
		if (tbd != null && CollectionUtils.isNotEmpty(tbd.getGjtTextbookDistributeDetails())) {
			GjtTextbookFeedback feedback = new GjtTextbookFeedback();
			feedback.setFeedbackType(feedbackType);
			feedback.setReason(reason);
			feedback.setStatus(1);
			feedback.setCreatedBy(gjtUserAccount.getId());
			feedback.setGjtStudentInfo(new GjtStudentInfo(student.getStudentId()));
			List<GjtTextbookFeedbackDetail> textbookFeedbackDetails = new ArrayList<GjtTextbookFeedbackDetail>();
			GjtTextbookFeedbackDetail feedbackDetail = new GjtTextbookFeedbackDetail();
			feedbackDetail.setTextbookId(textbookId);
			textbookFeedbackDetails.add(feedbackDetail);
			feedback.setGjtTextbookFeedbackDetails(textbookFeedbackDetails);
			gjtTextbookFeedbackService.insert(feedback);
		}
		// TODO 返回结果处理
		return "";
	}

	/**
	 * 查询教材计划
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月4日 上午10:16:30
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryTextbookPlan", method = RequestMethod.GET)
	public List<TextBookPlanVO> queryTextbookPlan(HttpSession session) {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);

		List<GjtTextbookPlan> plans = gjtTextbookPlanService.findByOrgIdAndSysdate(student.getXxId(), student.getGjtGrade().getGradeId());
		if (CollectionUtils.isEmpty(plans)) {
			log.info("GjtTextbookPlan is null");
			return null;
		}
		// 查询时间大于入学时间的所有年级
		List<String> gradeIds = gjtGradeService.findGradeIdByStartDate(student.getXxId(),
				student.getGjtGrade().getStartDate());

		List<TextBookPlanVO> planVOs = new ArrayList<TextBookPlanVO>();
		for (GjtTextbookPlan plan : plans) {
			TextBookPlanVO planVO = new TextBookPlanVO();
			int index = gradeIds.indexOf(plan.getGradeId());
			planVO.setPlanCode(plan.getPlanCode());
			planVO.setGradeName(plan.getGjtGrade().getGradeName());
			planVO.setTermName(TermType.getName(String.valueOf(index + 1)));
			planVO.setArrangeSdate(plan.getArrangeSdate());
			planVO.setArrangeEdate(plan.getArrangeEdate());
			planVO.setOrderSdate(plan.getOrderSdate());
			planVO.setOrderEdate(plan.getOrderEdate());
			planVO.setAddressConfirmSdate(plan.getOaddressConfirmSdate());
			planVO.setAddressConfirmEdate(plan.getOaddressConfirmEdate());
			planVO.setDistributeSdate(plan.getOdistributeSdate());
			planVO.setDistributeEdate(plan.getOdistributeEdate());
			planVO.setFeedbackSdate(plan.getOfeedbackSdate());
			planVO.setFeedbackEdate(plan.getOfeedbackEdate());
			planVO.setPlanName(plan.getPlanName());
			planVOs.add(planVO);
		}
		return planVOs;
	}

	// 2017春季的学期id
	private static final String GRADEID_2017C = "5a7eb15a82614101b55de86dad498041";

	/**
	 * 教材列表
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年2月27日 上午9:48:00
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryTextbookList", method = RequestMethod.GET)
	public Map<String, Object> queryTextbookList(HttpSession session) {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map<String, Object> result = new HashMap<String, Object>();
		String currentGradeId = gjtGradeService.getCurrentGradeId(student.getXxId());
		// 2017春季
		GjtGrade grade2017c = gjtGradeService.queryById(GRADEID_2017C);
		List<Map<String, Object>> grades = gjtGradeService.findByGradeSpecialtyId(student.getGradeSpecialtyId(), student.getStudentId());
		List<Map<String, Object>> textbooks=gjtTextbookService.findByGradeSpecialtyId(student.getGradeSpecialtyId());
		List<Map<String, Object>> termList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < grades.size(); i++) {
			Map<String, Object> termMap=new HashMap<String, Object>();
			termMap.put("termName", TermType.getName(String.valueOf(i+1)));
			termMap.put("termId", grades.get(i).get("gradeId"));
			List<Map<String, Object>> textbookList = new ArrayList<Map<String, Object>>();
			BigDecimal price = new BigDecimal(0);
			// 是否有指定教材
			GjtStudentTextbookOrder sto = gjtStudentTextbookOrderService.getByStudentIdAndGradeIdAndIsDeleted(student.getStudentId(),
					grades.get(i).get("gradeId").toString(), Constants.NODELETED);
			if(sto != null && StringUtils.isNotEmpty(sto.getTextbookCodes())) {
				String[] textbookCodeArray = sto.getTextbookCodes().split(",");
				for (String code : textbookCodeArray) {
					GjtTextbook textbook = gjtTextbookService.findByCode(code, student.getXxId());
					Map<String, Object> tb = new HashMap<String, Object>();
					tb.put("cover", textbook.getCover());
					tb.put("textbookId", textbook.getTextbookId());
					tb.put("textbookName", textbook.getTextbookName());
					tb.put("price", textbook.getPrice());
					textbookList.add(tb);
					price = price.add(new BigDecimal(textbook.getPrice()));
				}
			} else {
				for(Map<String, Object> textbookMap:textbooks){
					if (textbookMap.get("termId").equals(grades.get(i).get("gradeId"))) {
						Map<String, Object> tb = new HashMap<String, Object>();
						tb.put("cover", textbookMap.get("cover"));
						tb.put("textbookId", textbookMap.get("textbookId"));
						tb.put("textbookName", textbookMap.get("textbookName"));
						tb.put("courseName", textbookMap.get("courseName"));
						tb.put("price", textbookMap.get("price"));
						textbookList.add(tb);
						price = price.add((BigDecimal) (textbookMap.get("price") == null ? 0 : textbookMap.get("price")));
					}
				}
			}
			termMap.put("textbooks", textbookList);
			termMap.put("totalNumber", textbookList.size());
			termMap.put("totalPrice", price.setScale(2, BigDecimal.ROUND_HALF_UP));
			int status = 0;// 0暂未开放，1可购买 2未确认订单 3再次购买
			if (grades.get(i).get("status").toString().equals("0")) {// 不可购买
				status = 0;
			} else if (grades.get(i).get("distributeId") != null) {// 有购买记录，再次购买
				status = 3;
			} else {// 根据GjtStudentTextbookOrder已交费学期来判断
				if (sto != null) {// 已交费
					status = 2;
				} else {
					status = 1;
				}
			}
			termMap.put("status", status);
			termList.add(termMap);
		}
		result.put("termList", termList);
		Long orderCount = gjtTextbookDistributeService.countByStudentIdAndIsDeletedAndStatusIn(student.getStudentId(),
				Constants.NODELETED, Arrays.asList(new Integer[] { 1, 2 }));
		int unfinishedOrder = 0;
		if (orderCount > 0) {
			unfinishedOrder = 1;
		}
		result.put("unfinishedOrder", unfinishedOrder);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "queryTextbookListByTermId", method = RequestMethod.GET)
	public Map<String, Object> queryTextbookInfoByTermId(HttpSession session, @RequestParam String termId) {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> textbooks = gjtTextbookService.findByGradeId(termId, student.getGradeSpecialtyId());
		List<Map<String, Object>> textbookList = new ArrayList<Map<String, Object>>();
		BigDecimal price = new BigDecimal(0);
		for (Map<String, Object> textbookMap : textbooks) {
			Map<String, Object> tb = new HashMap<String, Object>();
			tb.put("cover", textbookMap.get("cover"));
			tb.put("textbookName", textbookMap.get("textbookName"));
			tb.put("courseName", textbookMap.get("courseName"));
			tb.put("textbookId", textbookMap.get("textbookId"));
			textbookList.add(tb);
			price = price.add((BigDecimal) (textbookMap.get("price") == null ? 0 : textbookMap.get("price")));
		}
		result.put("textbooks", textbookList);
		result.put("totalNumber", textbookList.size());
		result.put("totalPrice", price);
		return result;
	}

	/**
	 * 教材订单列表
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年2月27日 上午9:48:25
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryTextbookOrderList", method = RequestMethod.GET)
	public Map<String, Object> queryTextbookOrderList(HttpSession session) {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		List<GjtTextbookDistribute> distributes = gjtTextbookDistributeService.findByStudentIdAndStatus(student.getStudentId(),
				Constants.NODELETED, Arrays.asList(new Integer[] { 1, 2, 3 }));
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
		for (GjtTextbookDistribute distribute : distributes) {
			Map<String, Object> orderMap = new HashMap<String, Object>();
			orderMap.put("orderCode",distribute.getOrderCode());
			orderMap.put("distributeId", distribute.getDistributeId());
			orderMap.put("status", distribute.getStatus());
			List<TextbookVO> textbookVOs = new ArrayList<TextbookVO>();
			List<Map<String, Object>> textbooks = gjtTextbookService.findByDistributeId(distribute.getDistributeId(), student.getStudentId());
			BigDecimal totalPirce = new BigDecimal(0);
			for (Map<String, Object> tbMap : textbooks) {
				TextbookVO tbVO = new TextbookVO();
				tbVO.setCover((String) tbMap.get("cover"));
				tbVO.setTextbookName((String) tbMap.get("textbookName"));
				tbVO.setCourseName((String) tbMap.get("courseName"));
				tbVO.setPrice((BigDecimal) tbMap.get("price"));
				textbookVOs.add(tbVO);
				totalPirce = totalPirce.add((BigDecimal) tbMap.get("price"));
			}

			if (distribute.getPrice() != null && distribute.getPrice().intValue() != 0) {
				totalPirce = distribute.getPrice();
			}
			if (distribute.getFreight() != null) {
				totalPirce = totalPirce.add(distribute.getFreight());
			}

			orderMap.put("totalNumber", textbookVOs.size());
			orderMap.put("textbooks", textbookVOs);
			orderMap.put("totalPrice", totalPirce);
			orderMap.put("freight", distribute.getFreight() == null ? 0 : distribute.getFreight());
			orderList.add(orderMap);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("orderList", orderList);
		return result;
	}

	/**
	 * 提交订单
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年2月27日 上午9:48:37
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @param termId
	 * @param addressId
	 * @param textbookIds
	 * @return
	 * @throws IOException
	 */
	@SysLog("教材订购-提交订单")
	@ResponseBody
	@RequestMapping(value = "/createTextbookOrder")
	public String createTextbookOrder(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam String termId, String addressId, @RequestParam(value = "textbookIds[]", required = false) List<String> textbookIds)
			throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String url = null;
		String paySn = OrderType.TEXTBOOK_DISTRIBUTE.getCode() + codeGeneratorService.codeGenerator(CacheConstants.TEXTBOOK_DISTRIBUTE_ORDER_CODE, 14);
		// 生成补考费支付链接
		GjtStudentInfo student = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
		GjtGrade gjtGrade = gjtGradeService.queryById(termId);
		GjtStudentAddress gjtStudentAddress = gjtStudentAddressService.findOne(addressId);
		BigDecimal price = new BigDecimal(0);
		GjtTextbookDistribute distribute = new GjtTextbookDistribute();
		distribute.setDistributeId(UUIDUtils.random());
		distribute.setCreatedBy(user.getId());
		distribute.setCreatedDt(new Date());
		distribute.setOrderCode(paySn);
		distribute.setStudentId(student.getStudentId());
		distribute.setStatus(-1);
		distribute.setGradeId(termId);
		distribute.setMobile(gjtStudentAddress.getMobile());
		distribute.setReceiver(gjtStudentAddress.getReceiver());
		distribute.setIsDeleted(Constants.NODELETED);
		long startTime = gjtGrade.getTextbookStartDate().getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(gjtGrade.getTextbookEndDate());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.add(Calendar.DATE, 1);
		long endTime = calendar.getTimeInMillis();
		long currentTime = System.currentTimeMillis();
		if (currentTime >= startTime && currentTime < endTime) {// 学院订购
			distribute.setBelong(0);
		} else {
			distribute.setBelong(1);
		}
		String address = gjtDistrictService.queryAllNameById(gjtStudentAddress.getAreaCode())
				+ StringUtils.defaultString(gjtStudentAddress.getAddress());
		distribute.setAddress(address);
		List<GjtTextbookDistributeDetail> details = new ArrayList<GjtTextbookDistributeDetail>();
		if (CollectionUtils.isEmpty(textbookIds)) {
			List<Map<String, Object>> textbooks = gjtTextbookService.findByGradeId(termId, student.getGradeSpecialtyId());
			for (Map<String, Object> textbookMap : textbooks) {
				price = price.add((BigDecimal) textbookMap.get("price"));
				GjtTextbookDistributeDetail detail = new GjtTextbookDistributeDetail();
				detail.setDetailId(UUIDUtils.random());
				detail.setTextbookId((String) textbookMap.get("textbookId"));
				detail.setQuantity(1);
				detail.setPrice(Float.valueOf(textbookMap.get("price").toString()));
				detail.setStatus(0);
				detail.setGjtTextbookDistribute(distribute);
				details.add(detail);
			}
		} else {
			List<GjtTextbook> textbooks = gjtTextbookService.findAll(textbookIds);
			for (GjtTextbook textbook : textbooks) {
				price = price.add(new BigDecimal(textbook.getPrice()));
				GjtTextbookDistributeDetail detail = new GjtTextbookDistributeDetail();
				detail.setDetailId(UUIDUtils.random());
				detail.setTextbookId(textbook.getTextbookId());
				detail.setQuantity(1);
				detail.setPrice(textbook.getPrice());
				detail.setStatus(0);
				detail.setGjtTextbookDistribute(distribute);
				details.add(detail);
			}
		}
		distribute.setPrice(price);
		GjtTextbookFreight freight = gjtTextbookFreightService.queryByDistrictId(gjtStudentAddress.getAreaCode());
		BigDecimal feightPrice = price.multiply(freight.getFreightRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
		distribute.setFreight(feightPrice);
		BigDecimal payPrice = price.add(feightPrice);
		distribute.setPayPrice(payPrice);
		gjtTextbookDistributeService.saveTextbookDistributeAndDetail(distribute, details);
		Map<String, String> params = new HashMap<String, String>();
		params.put("order_sn", ObjectUtils.toString(student.getGjtSignup().getOrderSn())); // 订单号
		params.put("pay_sn", paySn); // 缴费单号,15位字符,由调用方生成
		params.put("title", URLEncoder.encode("教材费-" + gjtGrade.getGradeName(), Constants.CHARSET)); // 缴费标题
		params.put("price", String.format("%.2f", payPrice)); // 缴费金额
		params.put("return_", String.format("%s%s?data=%s", pcenterStudyServer, "/pay/textbookFeeReturn", distribute.getDistributeId())); // 同步回调地址
		params.put("notify_", String.format("%s%s?data=%s", pcenterStudyServer, "/pay/textbookFeeNotify", distribute.getDistributeId())); // 异步回调地址

		// TODO 测试订单号
		// params.put("order_sn", "201802084077880");
		// params.put("price", "0.01");
		url = getTextbookPayUrl(params, student.getGjtSchoolInfo().getGjtOrg().getCode());
		// session.setAttribute(EXAM_PAY_SN, paySn); // 缴费单号存储到session中
		// 2.跳转到支付
		return url;
	}

	/**
	 * 已交费的学员确定订单
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年3月2日 上午11:24:59
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @param termId
	 * @param addressId
	 * @param textbookIds
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/confirmTextbookOrder")
	public void confirmTextbookOrder(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam String termId,
			String addressId, @RequestParam(value = "textbookIds[]") List<String> textbookIds) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
		String paySn = null;
		GjtStudentTextbookOrder sto = gjtStudentTextbookOrderService.getByStudentIdAndGradeIdAndIsDeleted(student.getStudentId(), termId,
				Constants.NODELETED);
		if (sto == null) {
			throw new Exception("不符合确认订单条件");
		}
		paySn = sto.getOrderNo();
		
		Date now = new Date();
		GjtStudentAddress gjtStudentAddress = gjtStudentAddressService.findOne(addressId);
		BigDecimal price = new BigDecimal(0);
		GjtTextbookDistribute distribute = new GjtTextbookDistribute();
		distribute.setDistributeId(UUIDUtils.random());
		distribute.setCreatedBy(user.getId());
		distribute.setCreatedDt(now);
		distribute.setOrderCode(paySn);
		distribute.setStudentId(student.getStudentId());
		distribute.setStatus(1);// 所有确认订单都是已支付
		distribute.setGradeId(termId);
		distribute.setMobile(gjtStudentAddress.getMobile());
		distribute.setReceiver(gjtStudentAddress.getReceiver());
		distribute.setIsDeleted(Constants.NODELETED);
		distribute.setBelong(0);// 所有确认订单都是学院订购
		String address = gjtDistrictService.queryAllNameById(gjtStudentAddress.getAreaCode())
				+ StringUtils.defaultString(gjtStudentAddress.getAddress());
		distribute.setAddress(address);
		List<GjtTextbookDistributeDetail> details = new ArrayList<GjtTextbookDistributeDetail>();
		if (StringUtils.isNotEmpty(sto.getTextbookCodes())) {
			String[] textbookCodeArray = sto.getTextbookCodes().split(",");
			for (String code : textbookCodeArray) {
				GjtTextbook textbook = gjtTextbookService.findByCode(code, student.getXxId());
				price = price.add(new BigDecimal(textbook.getPrice()));
				GjtTextbookDistributeDetail detail = new GjtTextbookDistributeDetail();
				detail.setDetailId(UUIDUtils.random());
				detail.setTextbookId(textbook.getTextbookId());
				detail.setQuantity(1);
				detail.setPrice(textbook.getPrice());
				detail.setStatus(1);
				detail.setGjtTextbookDistribute(distribute);
				details.add(detail);
			}
		} else {
			List<GjtTextbook> textbooks = gjtTextbookService.findAll(textbookIds);
			for (GjtTextbook textbook : textbooks) {
				price = price.add(new BigDecimal(textbook.getPrice()));
				GjtTextbookDistributeDetail detail = new GjtTextbookDistributeDetail();
				detail.setDetailId(UUIDUtils.random());
				detail.setTextbookId(textbook.getTextbookId());
				detail.setQuantity(1);
				detail.setPrice(textbook.getPrice());
				detail.setStatus(1);
				detail.setGjtTextbookDistribute(distribute);
				details.add(detail);
			}
		}
		distribute.setPrice(price);
		GjtTextbookFreight freight = gjtTextbookFreightService.queryByDistrictId(gjtStudentAddress.getAreaCode());
		BigDecimal feightPrice = price.multiply(freight.getFreightRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
		distribute.setFreight(feightPrice);
		BigDecimal payPrice = price.add(feightPrice);
		distribute.setPayPrice(payPrice);
		gjtTextbookDistributeService.saveTextbookDistributeAndDetail(distribute, details);
		if (sto != null) {
			sto.setIsDeleted(Constants.ISDELETED);
			sto.setUpdatedBy(user.getId());
			sto.setUpdatedDt(now);
			gjtStudentTextbookOrderService.save(sto);
		}
		// 已确认地址
		if (textbookAddressComponent.hasCached(student.getStudentId())) {
			textbookAddressComponent.setCached(student.getStudentId(), true);
		}
	}

	private String getTextbookPayUrl(Map<String, String> params, String schoolCode) {
		// 接口编号 : 0000000229 缴费支付接口
		params.put("type", "7"); // 缴费类型 转专业差价-4 考试费-6 教材费-7

		// 额外参数;签名
		long time = DateUtils.getDate().getTime();
		if (StringUtils.isNotBlank(schoolCode)) {
			params.put("school_code", schoolCode); // 院校code，非必填
		}
		params.put("sign", SignUtil.formatUrlMap(params, time));
		params.put("appid", SignUtil.APPID);// APPID不需要参与加密
		params.put("time", String.valueOf(time));

		try {
			// 链接需要encode一次
			params.put("return_", URLEncoder.encode(ObjectUtils.toString(params.get("return_")), Constants.CHARSET)); // 同步回调地址
			params.put("notify_", URLEncoder.encode(ObjectUtils.toString(params.get("notify_")), Constants.CHARSET)); // 异步回调地址
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		StringBuffer paramsJoin = new StringBuffer();
		for (Map.Entry<String, String> item : params.entrySet()) {
			if (org.apache.commons.lang.StringUtils.isNotEmpty(item.getKey())) {
				String key = item.getKey();
				String val = item.getValue();
				paramsJoin.append(key).append("=").append(val).append("&");
			}
		}
		paramsJoin.setLength(paramsJoin.length() - 1);
		return String.format("%s?%s", ObjectUtils.toString(AppConfig.getProperty("payHost")), paramsJoin);
	}

	/**
	 * 查询运费
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年2月26日 上午10:42:51
	 * @param request
	 * @param response
	 * @param session
	 * @param addressId
	 * @param textbookIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryFreight")
	public Map<String, Object> queryFreight(HttpServletRequest request, HttpServletResponse response, HttpSession session, String addressId,
			@RequestParam(value = "textbookIds[]", required = false) List<String> textbookIds) {
		List<GjtTextbook> textbooks = gjtTextbookService.findAll(textbookIds);
		BigDecimal price = new BigDecimal(0);
		for (GjtTextbook textbook : textbooks) {
			price = price.add(new BigDecimal(textbook.getPrice()));
		}
		GjtStudentAddress gjtStudentAddress = gjtStudentAddressService.findOne(addressId);
		GjtTextbookFreight freight = gjtTextbookFreightService.queryByDistrictId(gjtStudentAddress.getAreaCode());
		BigDecimal feightPrice = price.multiply(freight.getFreightRate());
		Map<String, Object> resutl = new HashMap<String, Object>();
		resutl.put("feight", new BigDecimal(String.format("%.2f", feightPrice)));
		return resutl;
	}
}
