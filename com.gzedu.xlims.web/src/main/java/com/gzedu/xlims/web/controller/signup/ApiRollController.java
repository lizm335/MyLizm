package com.gzedu.xlims.web.controller.signup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.Objects;
import com.gzedu.xlims.common.ValidateUtil;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtSyncLogService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年1月2日
 * @version 2.5
 */
@Controller
@RequestMapping("/api/edumanage/roll")
public class ApiRollController  extends BaseController {
	
	public static final Logger log = LoggerFactory.getLogger(RollController.class);

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtClassStudentService gjtClassStudentService;

	@Autowired
	GjtGradeSpecialtyService gjtGradeSpecialtyService;

	@Autowired
	GjtClassInfoService gjtClassInfoService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtRecResultService gjtRecResultService;
	
	@Value("#{configProperties['yunyingCenterServer']}")
	private String YUNYING_CENTER_SERVER;
	
	@Autowired
	private GjtSyncLogService gjtSyncLogService;
	
	@Autowired
	GjtStudentInfoDao gjtStudentInfoDao;
	/**
	 * 更新学员的学期
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "forwordStudentGrade", method = RequestMethod.POST)
	public int forwordStudentGrade(HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String classId = request.getParameter("classId");// 班级ID
		String newSpecialty = request.getParameter("newSpecialty");// 新专业
		String newGradeId = request.getParameter("newGradeId");// 学期
		String studentId = request.getParameter("studentId");// 学生ID
		String gradeSpecialtyId = request.getParameter("gradeSpecialtyId");//产品ID
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(studentId);// 学员基本信息
		int bh=1;
		// 查询学学员所属的学习中心
		GjtOrg gjtStudyCenter = gjtOrgService.queryStudyCenterInfo(gjtStudentInfo.getXxzxId(), "3");
		// 查询新的专业和学期下的产品状态是否开设
//		GjtGradeSpecialty gjtGradeSpecialty = gjtGradeSpecialtyService.findByGradeIdAndSpecialtyIdAndIsDeletedNew(newGradeId,newSpecialty);
//		GjtGradeSpecialty gjtGradeSpecialty = gjtGradeSpecialtyService.findByGradeIdAndSpecialtyIdAndIsDeleted(newGradeId,newSpecialty);
		GjtGradeSpecialty gjtGradeSpecialty =gjtGradeSpecialtyService.findOne(gradeSpecialtyId);
		if(gjtGradeSpecialty!=null){
			if(gjtGradeSpecialty.getStatus()==1){
				//只需要删除教务班级,不需要删除选课和课程班
				gjtClassStudentService.deleteByStudentId(studentId, classId,"学员转学期");
				//创建新的教学班和分班
				try {
					boolean result=gjtClassInfoService.createTeachClassAndRec(gjtStudentInfo,gjtStudyCenter,gjtGradeSpecialty,newSpecialty,newGradeId,bh);
					if(!result){
						return 1;//分班出错
					}					 
				} catch (Exception e) {
					e.printStackTrace();
					 return 1;//分班出错
				}
			}else{
				log.error("学期ID：" + newGradeId + ";新专业ID" + newSpecialty);
				return 2;//新专业未启动
			}
		}else{
			log.error("新学期ID：" + newGradeId + ";新专业ID" + newSpecialty);
			return 3;//新专业未找到
		}		
		return 0;//更改学期成功
	}
	
	/**
	 * 更新学员的专业
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "forwordMajor")
	public Map<String,Object> forwordMajor(HttpServletRequest request){
		Map<String, Object> reslutMap=new HashMap<String, Object>();
		String newSpecialty = request.getParameter("newSpecialty");// 新专业
		String gradeId = request.getParameter("gradeId");// 学期
		String studentId = request.getParameter("studentId");// 学生ID
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(studentId);// 学员基本信息
		GjtClassInfo gjtClassInfo = gjtClassInfoService.queryTeachClassInfo(studentId);
		String classId = gjtClassInfo.getClassId();// 班级ID
		int bh = 1;// 班号默认为1;
		// 查询学学员所属的学习中心
		GjtOrg gjtOrg = gjtOrgService.queryStudyCenterInfo(gjtStudentInfo.getXxzxId(), "3");
		// 查询新的专业下的产品状态是否开设
		GjtGradeSpecialty gjtGradeSpecialty = gjtGradeSpecialtyService.queryByGradeIdAndSpecialtyId(gradeId,newSpecialty);
		if (gjtGradeSpecialty != null) {
			if (gjtGradeSpecialty.getStatus() == 1) {
				//只需要删除教务班级,不需要删除选课和课程班
				gjtClassStudentService.deleteByStudentId(studentId, classId,"学员转学期");
				//创建新的教学班和分班
				try {
					boolean result=gjtClassInfoService.createTeachClassAndRec(gjtStudentInfo,gjtOrg,gjtGradeSpecialty,newSpecialty,gradeId,bh);
					if(!result){
						reslutMap.put("1", "内部异常-分配教学班失败");
					}					 
				} catch (Exception e) {
					e.printStackTrace();
					reslutMap.put("2", "内部异常-分配教学班失败");
				}
			}else{
				reslutMap.put("3", "新专业暂未启用");
			}			
		}else{
			reslutMap.put("4", "该学期暂未开设本专业");
		}
		reslutMap.put("5", "学员转专业成功");
		return reslutMap;
	}
	/**
	 * 同步广州国开学员至运营部门(临时使用)
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "syncStudentYunYingCenter")
	public Map<String,Object> syncStudentYunYingCenter(HttpServletRequest request){
		Map<String, Object> reslutMap=new HashMap<String, Object>();
		//List<GjtStudentInfo> studentList=gjtStudentInfoService.findByXxIdAndIsDeleted("2f5bfcce71fa462b8e1f65bcd0f4c632",Constants.BOOLEAN_NO);
		List<Map<String, Object>> list=gjtStudentInfoService.queryStudentInfo();
		//GjtStudentInfo info = gjtStudentInfoDao.findOne("27d700737b60421397bb92d8d4e9a011");
		Map<String, Object> resultSync = new HashMap<String, Object>();
		resultSync.put("successful", true);
		if(list!=null && list.size()>0){
			for (Map studentMap : list) {				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("name", studentMap.get("XM"));
				params.put("id_card", studentMap.get("SFZH"));
				params.put("phone", com.gzedu.xlims.common.Objects.toString(studentMap.get("SJH"), ""));
				params.put("account", studentMap.get("LOGIN_ACCOUNT"));
				params.put("student_id", studentMap.get("STUDENT_ID"));
				params.put("atid", studentMap.get("ATID"));
				String mail = com.gzedu.xlims.common.Objects.toString(studentMap.get("DZXX"), "");
				params.put("email", ValidateUtil.isEmail(mail) ? mail : "");
				params.put("birthday", com.gzedu.xlims.common.Objects.toString(studentMap.get("CSRQ"), ""));
				params.put("sex", com.gzedu.xlims.common.Objects.toString(studentMap.get("XBM"), ""));
				params.put("address", com.gzedu.xlims.common.Objects.toString(studentMap.get("TXDZ"), ""));
				params.put("product_id", com.gzedu.xlims.common.Objects.toString(studentMap.get("GRADE_SPECIALTY_ID"), ""));
				params.put("order_no", com.gzedu.xlims.common.Objects.toString(studentMap.get("ORDER_SN"), ""));
				params.put("source", "education"); // 平台来源，enrollment 为招生平台，education 教学平台
				params.put("sync_type", "1"); // 学员类型  1、学历  2、职业
				
				try {
					String result = HttpClientUtils.doHttpPost(YUNYING_CENTER_SERVER + "/receive/studentAgain", params, 6000, Constants.CHARSET);
					Map<String, Object> resultMap = GsonUtils.toBean(result, Map.class);
					if(resultMap != null) {
						int code = (int) NumberUtils.toDouble(Objects.toString(resultMap.get("code"), ""));
						if (code == 200) {
							//info.getGjtUserAccount().setIsSync(true);
							//gjtUserAccountService.update(info.getGjtUserAccount());
							//info.setYunyingSync(Constants.BOOLEAN_YES);
							//gjtStudentInfoDao.save(info);
							// 成功也记录下
							//gjtSyncLogService.insert(new GjtSyncLog(studentMap.get("XM").toString(), studentMap.get("XH").toString(), Constants.RSBIZ_CODE_B0002, GsonUtils.toJson(params), "success" + result));
							//return resultSync;
						} else {
							// 记录同步失败日志
							//gjtSyncLogService.insert(new GjtSyncLog(studentMap.get("XM").toString(), studentMap.get("XH").toString(), Constants.RSBIZ_CODE_B0002, GsonUtils.toJson(params), result));
							resultSync.put("type", 3);
							resultSync.put("message", "请求同步结果-" + result);
						}
					} else {
						// 记录同步失败日志
						//gjtSyncLogService.insert(new GjtSyncLog(studentMap.get("XM").toString(), studentMap.get("XH").toString(), Constants.RSBIZ_CODE_B0002, GsonUtils.toJson(params), result));
						resultSync.put("type", 3);
						resultSync.put("message", "请求同步结果-" + result);
					}
				} catch (Exception e) {
					String objJson = GsonUtils.toJson(params);
					log.error("syncYunYingCenter fail ======== params:" + objJson);
					// 记录同步失败日志
					//gjtSyncLogService.insert(new GjtSyncLog(studentMap.get("XM").toString(), studentMap.get("XH").toString(), Constants.RSBIZ_CODE_B0002, objJson, e.toString().length() > 500 ? e.toString().substring(0, 500) : e.toString()));
					e.printStackTrace();
					resultSync.put("type", 2);
					resultSync.put("message", "内部异常-" + e.toString());
				}
				resultSync.put("successful", false);
//				return resultSync;
			}
		}else{
			resultSync.put("false", "未找到该院校的学员！");
		}
		return resultSync;
	}
}
