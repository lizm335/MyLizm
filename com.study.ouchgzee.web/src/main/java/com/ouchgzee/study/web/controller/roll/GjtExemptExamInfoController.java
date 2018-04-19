package com.ouchgzee.study.web.controller.roll;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.eefile.EEFileUploadUtil;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.transaction.GjtExemptExamInfoDao;
import com.gzedu.xlims.pojo.GjtExemptExamInfo;
import com.gzedu.xlims.pojo.GjtExemptExamInfoAudit;
import com.gzedu.xlims.pojo.GjtExemptExamInstall;
import com.gzedu.xlims.pojo.GjtExemptExamMaterial;
import com.gzedu.xlims.pojo.GjtExemptExamProve;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.transaction.GjtExemptExamInfoAuditService;
import com.gzedu.xlims.service.transaction.GjtExemptExamInfoService;
import com.gzedu.xlims.service.transaction.GjtExemptExamInstallService;
import com.gzedu.xlims.service.transaction.GjtExemptExamMaterialService;
import com.gzedu.xlims.service.transaction.GjtExemptExamProveService;
import com.ouchgzee.study.web.controller.roll.vo.RollTransactionAudtVO;

/**
 * 功能说明：学员申请免修免考接口
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月27日
 * @version 2.5
 */
@RestController
@RequestMapping("/pcenter/edumanage/exemptExamInfo")
public class GjtExemptExamInfoController {
	private static final Logger log = LoggerFactory.getLogger(GjtExemptExamInfoController.class);
	
	@Autowired
	private GjtExemptExamInstallService gjtExemptExamInstallService;
	
	@Autowired
	private GjtExemptExamMaterialService  gjtExemptExamMaterialService;
	
	@Autowired
	private GjtExemptExamInfoService gjtExemptExamInfoService;
	
	@Autowired
	private GjtExemptExamProveService gjtExemptExamProveService;
	
	@Autowired
	private GjtExemptExamInfoAuditService gjtExemptExamInfoAuditService;
	
	@Autowired
	private GjtExemptExamInfoDao gjtExemptExamInfoDao;
	
	/**
	 * 学员申请免修免考
	 * @param request
	 * @throws CommonException 
	 */
	@RequestMapping(value = "applyExemptExamInfo", method = RequestMethod.POST)
	public void applyExemptExamInfo(HttpServletRequest request) throws CommonException{
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> applyExemptExamMap = new HashMap<String, Object>();
		String courseId=request.getParameter("courseId");//课程ID
		String sign = request.getParameter("sign"); //学员 签名
		String[] materialId=request.getParameterValues("materialId[]");//材料证明清单ID
		String[] awardDate=request.getParameterValues("awardDate[]");//材料证明颁发时间
		String[] awardUnit=request.getParameterValues("awardUnit[]");//材料证明颁发单位
		String[] url=request.getParameterValues("url[]");//材料证明地址
		if(StringUtils.isNotBlank(sign)) {
			if(sign.contains("base64,")) {
				String tmpFolderPath = request.getSession().getServletContext().getRealPath("") + File.separator + "tmp" + File.separator
						+ DateFormatUtils.ISO_DATE_FORMAT.format(Calendar.getInstance());
				String signPath = EEFileUploadUtil.uploadImageBase64ToUrl(sign, tmpFolderPath);
				if(StringUtils.isNoneEmpty(signPath)){
					applyExemptExamMap.put("sign", signPath); // 签名
				}else{
					throw new CommonException(600, "内部异常-签名上传失败!");
				}
			} else {
				applyExemptExamMap.put("sign", sign); // 签名
			}
		}
		applyExemptExamMap.put("courseId", courseId);
		applyExemptExamMap.put("studentId", user.getGjtStudentInfo().getStudentId());
		try {
			boolean result= gjtExemptExamInfoService.insertExemptExamInfo(applyExemptExamMap,materialId,awardDate,awardUnit,url);
			if(!result){
				throw new CommonException(600, "内部异常-免修免考申请失败!");	
			}
		} catch (Exception e) {
			throw new CommonException(600, "数据异常");
		}
	}
	/**
	 * 查询学员申请免修免考的课程
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getExemptExamCourse", method = RequestMethod.GET)
	public Map<String,Object> getExemptExamMaterialInfo(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER); 
		Map<String,Object> installMap=null;//免修免考课程信息
		Map<String,Object> resultMap=new HashMap<String, Object>();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		List<String> gradeList=new ArrayList<String>();
		String nj=user.getGjtStudentInfo().getNj();		
		//查询免修免考的设置信息
		List<GjtExemptExamInstall> installList = gjtExemptExamInstallService.findByXxIdAndIsDeletedAndStatus(user.getGjtStudentInfo().getXxId(),Constants.BOOLEAN_NO,"1");
		if(installList!=null && installList.size()>0){
			for(int i=0;i<installList.size();i++){				
				GjtExemptExamInstall installInfo=installList.get(i);
				String grade=installInfo.getGradeId();
				if(EmptyUtils.isNotEmpty(grade)){
					String[] gradeId=grade.split(",");
					for(int k=0;k<gradeId.length;k++){
						gradeList.add(gradeId[k]);
					}
				}
				//判断该学员的学期是在免修免考的信息中
				if(gradeList.contains(nj)||EmptyUtils.isEmpty(grade)){
					installMap=new HashMap<String, Object>();
					installMap.put("installId", installInfo.getInstallId());
					installMap.put("courseId", installInfo.getCourseId());//课程ID
					installMap.put("courseName", installInfo.getGjtCourse().getKcmc());//课程名称
					list.add(installMap);
				}				
			}			
			resultMap.put("installList", list);
		}
		return resultMap;
	}
	
	/**
	 * 查询学员申请免修免考的材料证明清单
	 * @param request
	 * @return
	 * @throws CommonException 
	 */
	@RequestMapping(value = "getExemptExamMaterialList", method = RequestMethod.POST)
	public Map<String,Object> getExemptExamMaterialList(HttpServletRequest request) throws CommonException{
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String,Object> materialMap=new HashMap<String, Object>();
		Map<String,Object> resultMap=new HashMap<String, Object>();
		List<Map<String,Object>> list=null;
		String installId=request.getParameter("installId");//免修免考设置ID
		String courseId=request.getParameter("courseId");//课程ID
		try {
			List<GjtExemptExamMaterial> materialList=gjtExemptExamMaterialService.findByInstallIdAndIsDeleted(installId, Constants.BOOLEAN_NO);
			if(materialList!=null && materialList.size()>0){
				list=new ArrayList<Map<String,Object>>();
				for (GjtExemptExamMaterial material : materialList) {
					materialMap=new HashMap<String, Object>();
					materialMap.put("materialId", material.getId());
					materialMap.put("materialName", material.getMaterialName());//材料证明清单
					materialMap.put("memo", material.getMemo());//备注
					materialMap.put("isOnlineExam", material.getIsOnlineExam());//是否需要在线考试
					list.add(materialMap);									
				}
			}
			resultMap.put("materialList", list);
			List<GjtExemptExamInfo> infoList=gjtExemptExamInfoService.findByStudentIdAndCourseIdOrderByCreatedDtDesc(user.getGjtStudentInfo().getStudentId(),courseId);
			if(infoList!=null && infoList.size()>0){
				GjtExemptExamInfo item=infoList.get(0);
				if("1".equals(item.getIsApplay())){
					resultMap.put("isApplyFor", "1");//已申请过免修免考
				}else{
					resultMap.put("isApplyFor", "0");//未申请过免修免考
				}				
			}else{
				resultMap.put("isApplyFor", "0");//未申请过免修免考
			}			
		} catch (Exception e) {
			throw new CommonException(600, "数据异常！");
		}		
		return resultMap;
	}
	/**
	 * 查看免修免考详情
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getExemptExamInfoAudit", method = RequestMethod.POST)
	public Map<String,Object> getExemptExamInfoAudit(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		SimpleDateFormat dataformat=new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> resultMap = null;
		Map<String, Object> infoMap = new HashMap<String, Object>();
		String courseId=request.getParameter("courseId");//课程ID
		List<GjtExemptExamInfo> infoList=gjtExemptExamInfoService.findByStudentIdAndCourseIdOrderByCreatedDtDesc(user.getGjtStudentInfo().getStudentId(),courseId);
		List<GjtExemptExamProve> proveList=null;
		List<Map<String,Object>> list=null;
		if(infoList!=null && infoList.size()>0){
			GjtExemptExamInfo gjtExemptExamInfo=infoList.get(0);
			list=new ArrayList<Map<String,Object>>();
			proveList=gjtExemptExamProveService.findByExemptExamIdAndStudentId(gjtExemptExamInfo.getExemptExamId(),user.getGjtStudentInfo().getStudentId());
			for (GjtExemptExamProve gjtExemptExamProve : proveList) {
				resultMap = new HashMap<String, Object>();
				resultMap.put("materialName", gjtExemptExamProve.getGjtExemptExamMaterial().getMaterialName());
				resultMap.put("memo", gjtExemptExamProve.getGjtExemptExamMaterial().getMemo());
				resultMap.put("awardDate", gjtExemptExamProve.getAwardDate());//颁发时间
				resultMap.put("awardUnit", gjtExemptExamProve.getAwardUnit());//颁发单位
				resultMap.put("url",gjtExemptExamProve.getUrl());
				resultMap.put("isOnlineExam",gjtExemptExamProve.getGjtExemptExamMaterial().getIsOnlineExam());
				list.add(resultMap);
			}
			
			infoMap.put("createdDt", dataformat.format(gjtExemptExamInfo.getCreatedDt()));
			infoMap.put("sign", gjtExemptExamInfo.getSign());
			infoMap.put("auditStatus", gjtExemptExamInfo.getAuditStatus());
			infoMap.put("materialList", list);
			//查询审核记录
			List<GjtExemptExamInfoAudit> auditList=gjtExemptExamInfoAuditService.findByExemptExamIdOrderByAuditDtAsc(gjtExemptExamInfo.getExemptExamId());
			if("2".equals(auditList.get(auditList.size()-1).getAuditState())){
				infoMap.put("auditIdea", auditList.get(auditList.size()-1).getAuditContent());//审核意见
			}
			List<RollTransactionAudtVO> result = new ArrayList<RollTransactionAudtVO>();			
			if(auditList!=null&&auditList.size()>0){
				for(int i=0;i<auditList.size();i++){
					GjtExemptExamInfoAudit audit=auditList.get(i);
					RollTransactionAudtVO audtVo=new RollTransactionAudtVO();
					audtVo.setAuditState(audit.getAuditState());
				    String role=audit.getAuditOperatorRole();
				    if(role!=null){
				    	audtVo.setAuditOperatoRole(role.toString());
					}else{
						audtVo.setAuditOperatoRole(new String());
					}
				    audtVo.setAuditContent(audit.getAuditContent()); 
				    audtVo.setAuditOperator(audit.getAuditOperator());
				    if(audit.getAuditDt()!=null){
						dataformat=new SimpleDateFormat("yyyy-MM-dd");			
						String date=dataformat.format(audit.getAuditDt());
						audtVo.setAuditDt(date);//审核时间
					}else{
						audtVo.setAuditDt(dataformat.format(new Date()));
					}
				    result.add(audtVo);
				}
			}						
			infoMap.put("auditList", result);
		}
		return infoMap;
	}
	
	/**
	 * 重新申请免修免考，更改IsApplay状态为 0
	 * @param request
	 */
	@RequestMapping(value = "againSubmitExemptExam", method = RequestMethod.POST)
	public void againSubmitExemptExam(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String courseId=request.getParameter("courseId");//课程ID
		List<GjtExemptExamInfo> infoList=gjtExemptExamInfoService.findByStudentIdAndCourseIdOrderByCreatedDtDesc(user.getGjtStudentInfo().getStudentId(),courseId);
		if(infoList!=null && infoList.size()>0){
			GjtExemptExamInfo item=infoList.get(0);
			item.setIsApplay("0");
			gjtExemptExamInfoDao.save(item);
		}
	}
}
