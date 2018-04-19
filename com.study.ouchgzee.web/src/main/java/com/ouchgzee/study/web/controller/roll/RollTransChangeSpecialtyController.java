/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.controller.roll;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.eefile.EEFileUploadUtil;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.signup.GjtRollPlanService;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranAuditService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTransCostService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.ouchgzee.study.web.common.BaseController;

/**
 * 功能说明：学籍异动--转专业
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年01月12日
 * @version 2.5
 */
@RestController
@RequestMapping("/pcenter/transaction/changeSpecialty")
public class RollTransChangeSpecialtyController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(RollTransChangeSpecialtyController.class);
	
	@Autowired
	GjtGradeSpecialtyService gjtGradeSpecialtyService;
	
	@Autowired
	private GjtRollPlanService gjtRollPlanService;
	
	@Autowired
	GjtRecResultService gjtRecResultService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtSignupService gjtSignupService;
	
	@Autowired
	GjtTeachPlanService gjtTeachPlanService;
	
	@Autowired
	private GjtSchoolRollTranService gjtSchoolRollTranService;
	
	@Autowired
	private GjtSchoolRollTranAuditService  gjtSchoolRollTranAuditService;
	
	@Autowired
	GjtSchoolRollTransCostService gjtSchoolRollTransCostService;

	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private GjtGradeService gjtGradeService;
			
	/**
	 * 查询学期下的专业
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getSpecialtyList", method = RequestMethod.POST)
	public Map<String,Object> getSpecialtyList(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> specialtyMap=new HashMap<String, Object>();
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());// 学员基本信息
		Map<String,Object> resultMap=null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		//查询学期下的专业
		Map<String,String> specialty=gjtGradeSpecialtyService.getSpecialtyMap(gjtStudentInfo.getXxId(),
								gjtStudentInfo.getViewStudentInfo().getGradeId(),gjtStudentInfo.getPycc());
		Set set = specialty.entrySet();
		for(Iterator iter = set.iterator(); iter.hasNext();){
			resultMap=new HashMap();
			Map.Entry entry = (Map.Entry)iter.next();
			 String key = (String)entry.getKey();
			 String value = (String)entry.getValue();
			 resultMap.put("majorId", key);
			 resultMap.put("majorName", value);
			 list.add(resultMap);
		}
		specialtyMap.put("specialtyMap", list);
		return specialtyMap;
	}
	
	/**
	 * 查询专业下的课程
	 * @param request
	 * @return
	 * @throws CommonException 
	 */
	@RequestMapping(value = "getRecResultlist", method = RequestMethod.POST)
	public List<Map<String,Object>> getRecResultlist(HttpServletRequest request) throws CommonException{
		GjtStudentInfo user = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		if (EmptyUtils.isEmpty(user)) {
			throw new CommonException(MessageCode.TOKEN_INVALID);
		}
		String studentId = ObjectUtils.toString(user.getStudentId(), "");
		if (EmptyUtils.isEmpty(studentId)) {
			throw new CommonException(MessageCode.TOKEN_INVALID);
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String newSpecialty=request.getParameter("newSpecialty");//新专业ID
		try {
			List<Map<String, String>> gradeList = gjtRecResultService.queryTeachTerm(studentId);// 获取学期信息
			if (EmptyUtils.isEmpty(gradeList)) {
				return resultList;
			}			
			GjtGradeSpecialty gjtGradeSpecialty = gjtGradeSpecialtyService.
					queryByGradeIdAndSpecialtyIdAndStudyCenterId(user.getViewStudentInfo().getGradeId(),newSpecialty,user.getXxzxId());
			if(gjtGradeSpecialty==null){
				gjtGradeSpecialty=gjtGradeSpecialtyService.queryByGradeIdAndSpecialtyId(user.getViewStudentInfo().getGradeId(), newSpecialty);
			}
			if(gjtGradeSpecialty!=null){
				for (Map map : gradeList) {
					List<GjtTeachPlan> gjtTeachPlans = gjtTeachPlanService.findByGradeSpecialtyIdAndKkxq(gjtGradeSpecialty.getId(),Integer.parseInt(ObjectUtils.toString(map.get("KKXQ"))));
					Map<String, Object> mapResult = new LinkedHashMap<String, Object>();
					List<Map<String, Object>> list= new LinkedList<Map<String, Object>>();
					List<Map<String, String>> list1 = gjtRecResultService.queryTeachStudentSourceDetail(studentId,ObjectUtils.toString(map.get("TERM_ID")));// 每个学期的明细
					List<Map<String, Object>> list2 = new LinkedList<Map<String, Object>>();// 筛选目前需要的字段
					if (EmptyUtils.isNotEmpty(list1)) {
						for (Map entity : list1) {
							Map<String, Object> columMap= new LinkedHashMap<String, Object>();
							columMap.put("KKXQ", ObjectUtils.toString(map.get("KKXQ")));//学期
							columMap.put("KCLBM_NAME", ObjectUtils.toString(entity.get("KCLBM_NAME")));// 课程模块名称
							columMap.put("KCMC", ObjectUtils.toString(entity.get("KCMC")));// 课程名称
							columMap.put("KCH", ObjectUtils.toString(entity.get("KCH")));// 课程号
							columMap.put("PROGRESS", ObjectUtils.toString(entity.get("PROGRESS")));//学习进度
							columMap.put("STUDY_SCORE", ObjectUtils.toString(entity.get("STUDY_SCORE")));// 学习成绩
							double examScore = NumberUtils.toDouble(ObjectUtils.toString(entity.get("EXAM_SCORE")));
							columMap.put("EXAM_SCORE", ObjectUtils.toString(entity.get("EXAM_SCORE")));// 考试成绩
							if (examScore < 0) {
								// 缺考等显示
								columMap.put("EXAM_SCORE", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.EXAM_SCORE, ((int) examScore)+""));// 考试成绩
							}
							columMap.put("SUM_SCORE", ObjectUtils.toString(entity.get("SUM_SCORE")));// 考试总成绩
							columMap.put("SCORE_STATE", ObjectUtils.toString(entity.get("SCORE_STATE")));// 状态
							List<String> courseList=new ArrayList<String>();
							for(int i=0;i<gjtTeachPlans.size();i++){
								GjtTeachPlan plan=gjtTeachPlans.get(i);
								courseList.add(plan.getCourseId());	
							}
							if(courseList.contains(ObjectUtils.toString(entity.get("COURSE_ID")))){
								columMap.put("isReserve", "1");//保留课程
							}else{
								columMap.put("isReserve", "0");//不可以保留课程
							}
							list2.add(columMap);
							mapResult.put("TERM_INFO", list2);
						}
					}
					resultList.add(mapResult);
				}			
			}else{
				throw new CommonException(600,"新专业暂未启动！");
			}			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CommonException(MessageCode.SYSTEM_ERROR);
		}
		return resultList;		
	}
	
	/**
	 * 学员转专业申请
	 * @param request
	 * @throws CommonException
	 */
	@RequestMapping(value = "changeSpecialty", method = RequestMethod.POST)
	public void changeSpecialty(HttpServletRequest request) throws CommonException{
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo gjtStudentInfo=user.getGjtStudentInfo();
		Map<String, Object> changeSpecialtyMap = new HashMap<String, Object>();
		String bankCardNumber=request.getParameter("bankCardNumber");//银行卡号
		String openBank=request.getParameter("openBank");//开户银行
		String causeType=request.getParameter("causeType");//转专业原因类型
//		String cause=request.getParameter("cause");//转专业原因
		String newSpecialtyId=request.getParameter("specialtyId");//专业ID
		String sign = request.getParameter("sign"); //学员 签名
		if(StringUtils.isNotBlank(sign)) {
			if(sign.contains("base64,")) {
				String tmpFolderPath = request.getSession().getServletContext().getRealPath("") + File.separator + "tmp" + File.separator
						+ DateFormatUtils.ISO_DATE_FORMAT.format(Calendar.getInstance());
				String signPath = EEFileUploadUtil.uploadImageBase64ToUrl(sign, tmpFolderPath);
				if(StringUtils.isNoneEmpty(signPath)){
					changeSpecialtyMap.put("sign", signPath); // 签名
				}else{
					throw new CommonException(600, "内部异常-转专业申请失败!");
				}				
			} else {
				changeSpecialtyMap.put("sign", sign); // 签名
			}
		}
		changeSpecialtyMap.put("bankCardNumber", bankCardNumber);
		changeSpecialtyMap.put("openBank", openBank);
		changeSpecialtyMap.put("causeType", causeType);
//		changeSpecialtyMap.put("cause", cause);
		changeSpecialtyMap.put("newSpecialtyId", newSpecialtyId);
		//推送至招生平台
		
		//本地新增转专业申请记录
		boolean result=gjtSchoolRollTranService.insertChangeSpecialty(changeSpecialtyMap,gjtStudentInfo);
		if(!result){
			throw new CommonException(600, "内部异常-转专业申请失败!");	
		}		
	}
}
