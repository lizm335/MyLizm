/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.usermanage;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.ExcelService;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.exception.ServiceException;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.usermanage.GjtClassStudentDao;
import com.gzedu.xlims.dao.usermanage.GjtClassStudentNativeDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtClassStudent;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.signup.SignupDataAddService;

import net.sf.json.JSONObject;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月25日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class GjtClassStudentServiceImpl implements GjtClassStudentService {
	public static final Logger logger = LoggerFactory.getLogger(GjtClassStudentServiceImpl.class);
	@Autowired
	private GjtClassStudentDao gjtClassStudentDao;

	@Autowired
	private GjtClassStudentNativeDao gjtClassStudentNativeDao;
	
	@Autowired
	private SignupDataAddService signupDataAddService;
	
	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;
	
	@Autowired
	private GjtClassInfoService gjtClassInfoService;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	@Autowired
	private CommonDao commonDao;

	@Value("#{configProperties['eeChatInterface']}")
	String eeServer;
	
//	@Value("#{configProperties['syncCourseRecInterface']}")
//	String courseRecServer;
	
	@Autowired
	GjtOrgDao gjtOrgDao;

	/**
	 * 查询班级学员，可以根据班级ID查询
	 */
	@Override
	public Page<GjtClassStudent> queryAll(String orgId, Map<String, Object> map, PageRequest pageRequest) {
		Map<String, SearchFilter> filters = SearchFilter.parse(map);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		if (StringUtils.isBlank((String) map.get("EQ_gjtClassInfo.classId"))) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			filters.put("gjtStudentInfo.gjtStudyCenter.id",
					new SearchFilter("gjtStudentInfo.gjtStudyCenter.id", SearchFilter.Operator.IN, orgList));
		}

		Specification<GjtClassStudent> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtClassStudent.class);

		return gjtClassStudentDao.findAll(spec, pageRequest);
	}

	@Override
	public List<GjtClassStudent> queryAll(String orgId, Map<String, Object> map) {
		Map<String, SearchFilter> filters = SearchFilter.parse(map);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("gjtSchoolInfo.id", new SearchFilter("gjtSchoolInfo.id", Operator.EQ, orgId));
		Specification<GjtClassStudent> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtClassStudent.class);
		return gjtClassStudentDao.findAll(spec);
	}

	@Override
	public long findAllCount(Map<String, Object> map) {
		Map<String, SearchFilter> filters = SearchFilter.parse(map);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		Specification<GjtClassStudent> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtClassStudent.class);
		return gjtClassStudentDao.count(spec);
	}

	@Override
	public GjtClassStudent queryById(String id) {
		return gjtClassStudentDao.findOne(id);
	}

	@Override
	public List<GjtClassStudent> queryByClassId(String classId) {
		return gjtClassStudentDao.queryByClassId(classId);
	}

	@Override
	public List<GjtClassStudent> queryAll() {
		return gjtClassStudentDao.findAll();
	}

	@Override
	public Boolean deleteById(String id) {
		int i = gjtClassStudentDao.deleteById(id, "Y");
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean deleteById(String[] ids) {
		for (String id : ids) {
			gjtClassStudentDao.deleteById(id, "Y");
		}
		return true;
	}

	@Override
	public void delete(String id) {
		gjtClassStudentDao.delete(id);
	}

	@Override
	public Boolean save(GjtClassStudent gjtClassStudent) {
		GjtClassStudent classStudent = gjtClassStudentDao.save(gjtClassStudent);
		if (classStudent == null) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean save(List<GjtClassStudent> gjtClassStudentList) {
		List<GjtClassStudent> classStudentList = gjtClassStudentDao.save(gjtClassStudentList);
		if (classStudentList == null) {
			return false;
		}
		return true;
	}

	@Override
	public void insertClassStudent(String studentids, String classId) {
		if (StringUtils.isNotBlank(studentids)) {
			String[] selectedIds = studentids.split(",");
			GjtClassInfo gc = gjtClassInfoService.queryById(classId);
			GjtGrade gjtGrade = gc.getGjtGrade();
			GjtOrg gjtOrg = gc.getGjtOrg();
			GjtSchoolInfo gjtSchoolInfo = gc.getGjtSchoolInfo();
			GjtStudyCenter gjtStudyCenter = gc.getGjtStudyCenter();
			for (String studentid : selectedIds) {
				GjtClassStudent item = new GjtClassStudent();
				item.setClassStudentId(UUIDUtils.random());
				item.setGjtClassInfo(gc);
				item.setGjtStudentInfo(new GjtStudentInfo(studentid));
				item.setCreatedDt(DateUtils.getNowTime());
				item.setIsDeleted("N");
				item.setGjtGrade(gjtGrade);
				item.setGjtOrg(gjtOrg);
				item.setGjtSchoolInfo(gjtSchoolInfo);
				item.setGjtStudyCenter(gjtStudyCenter);
				item.setIsEnabled("1");
				item.setVersion(BigDecimal.valueOf(2.5));
				item.setOrgCode(gjtOrg.getCode());
				save(item);
			}
		}
	}

	@Override
	public Page<Map<String, Object>> queryClassStudentInfo(Map<String, Object> searchParams, PageRequest pageRequst) {
		return gjtClassStudentNativeDao.queryClassStudentInfo(searchParams, pageRequst);
	}

	@Override
	public GjtClassInfo queryTeachClassInfoByStudetnId(String studentId) {
		return gjtClassInfoService.queryTeachClassInfo(studentId);
	}

	@Override
	@Transactional
	public boolean updateClassStudent(List<GjtClassStudent> classStudents, GjtClassInfo gjtClassInfo, String appId) {
		List<String> users = new ArrayList<String>();
		Date now = new Date();
		for (GjtClassStudent classStudent : classStudents) {
			classStudent.setGjtClassInfo(gjtClassInfo);
			classStudent.setUpdatedDt(now);
			users.add(classStudent.getGjtStudentInfo().getStudentId());
		}
		gjtClassStudentDao.save(classStudents);
		if(gjtClassInfo.getEegroup()==null){
			for(int i=0;i<users.size();i++){				
				signupDataAddService.createGroupNoAndAddSingleStudent(users.get(i));
				return true;
			}			
		}else{
			JSONObject json = new JSONObject();
			json.put("USER_ID", users);
			json.put("APP_ID", appId);
			json.put("GROUP_ID", gjtClassInfo.getClassId());// EE群号
			json.put("OWNER_ID", gjtClassInfo.getGjtBzr().getEeno());
			String url = eeServer + "/contactGroup/management/addGroupMember.do";
			Map<String, String> params = new HashMap<String, String>();
			params.put("data", json.toString());
			String result = HttpClientUtils.doHttpPost(url, params, 3000, "utf-8");
			if (StringUtils.isNotEmpty(result)) {
				json = JSONObject.fromObject(result);
				if ("1".equals(json.get("STATUS"))) {
					return true;
				}
			}	
		}
		throw new ServiceException("调班失败");
	}

	@Override
	public Map getStudentDataInfo(Map<String, Object> searchParams) {

		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GSI.STUDENT_ID,");
		sql.append("  	GSI.XH,");
		sql.append("  	GSI.XM,");
		sql.append("  	GSI.SFZH,");
		sql.append("  	GSI.SJH,");
		sql.append("  	GO.CODE");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN GJT_ORG GO ON");
		sql.append("  	GSI.XX_ID = GO.ID");
		sql.append("  	AND GO.IS_DELETED = 'N'");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID ");

		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("studentId")));


		Map<String,Object> map = commonDao.queryObjectToMapNative(sql.toString(),params);
		return map;
	}

	@Override
	public Page<Map<String, Object>> queryTermCourseStudentList(Map<String, Object> searchParams,
			PageRequest pageRequst) {
		return gjtClassStudentNativeDao.queryTermCourseStudentList(searchParams, pageRequst);
	}

	@Override
	@Transactional
	public Boolean deleteByStudentId(String studentId,String classId,String memo) {
		if(studentId!=null){
			gjtClassStudentDao.deleteByStudentId(studentId, memo,classId,"Y");	
		}		
		return true;
	}
	/**
	 * 添加学员至教学班中
	 */
	@Override
	@Transactional
	public Boolean addStudentToClassStudent(GjtClassInfo gjtClassInfo, GjtStudentInfo gjtStudentInfo,GjtClassInfo oldGjtClassInfo) {
		GjtClassStudent item=new GjtClassStudent();
		item.setClassStudentId(UUIDUtils.random());
		item.setGjtClassInfo(gjtClassInfo);
		item.setGjtStudentInfo(gjtStudentInfo);
		item.setCreatedDt(DateUtils.getNowTime());
		item.setIsDeleted("N");
		item.setGjtGrade(gjtClassInfo.getGjtGrade());
		item.setGjtOrg(gjtClassInfo.getGjtOrg());
		item.setGjtSchoolInfo(gjtClassInfo.getGjtSchoolInfo());
		item.setGjtStudyCenter(gjtClassInfo.getGjtStudyCenter());
		item.setIsEnabled("1");
		item.setVersion(BigDecimal.valueOf(2.5));
		item.setOrgCode(gjtClassInfo.getGjtOrg().getCode());
		gjtClassStudentDao.save(item);
		
		//解除学员原来的EE群关系
		if(oldGjtClassInfo!=null){
			String eeUrl = eeServer+ "/contactGroup/management/deleteGroupMember.do";
			Map<String, Object> parmsMap = new HashMap<String, Object>();
			parmsMap.put("USERID", gjtStudentInfo.getStudentId());
			parmsMap.put("EENO", gjtStudentInfo.getEeno());
			parmsMap.put("GROUP_ID", oldGjtClassInfo.getClassId());
			parmsMap.put("GROUP_EEIM_NO",oldGjtClassInfo.getEegroup());
			parmsMap.put("APP_ID", OrgUtil.getEEChatAppId(gjtStudentInfo.getGjtSchoolInfo().getGjtOrg().getCode()));
			JSONObject jsonObject = JSONObject.fromObject(parmsMap);
			parmsMap.clear();
			parmsMap.put("data", jsonObject.toString());
			String eeResult = HttpClientUtils.doHttpPost(eeUrl, parmsMap, 6000, "utf-8");
			if (StringUtils.isNotEmpty(eeResult)) {
				JSONObject jsonResult = JSONObject.fromObject(eeResult);			
				String status = jsonResult.getString("Status");
				logger.info("删除群关系返回结果：" + jsonResult);
				logger.info("删除群关系返回结果状态：" + status);
			}
		}	
		//同步学员至新的EE群
		Boolean reult=signupDataAddService.createGroupNoAndAddSingleStudent(gjtStudentInfo.getStudentId());
		if(!reult){
			return false;
		}
		return true;
	}
	/**
	 * 对学员进行调班
	 */
	@Override
	public Map importStuClassInfo(File targetFile, String path) {
		Map result=new HashMap();
		String [] heads = new String[]{"学号","姓名","身份证号","原教务班级","新教务班级","所属学习中心"};
		String [] dbNames = new String[]{"xh","xm","sfzh","oldTeachClass","newTeachClass","orgName"};
		String [] errortitle = new String[]{"学号","姓名","身份证号","原教务班级","新教务班级","所属学习中心","失败原因"};
		String [] errorNames = new String[]{"xh","xm","sfzh","oldTeachClass","newTeachClass","orgName","msg"};
		List<Map> successList = new ArrayList<Map>();//成功表格
		List<Map> errorList = new ArrayList<Map>();//失败表格
		List<String[]> datas = null;
		try {
			datas = ExcelUtil.readAsStringList(targetFile,2,heads.length);
			//校验标题
			String[] dataTitles = datas.remove(0);
			for(int i=0;i<heads.length;i++){
				if(!dataTitles[i].trim().equals(heads[i])){
					result.put("exception", "请下载正确表格模版填写");
					return result;
				}
			}
		} catch (Exception e) {
			result.put("exception", "请下载正确表格模版填写");
			return result;
		}
		try {
			//校验数据
			for(String data[]:datas){
				if(EmptyUtils.isEmpty(data[0])){
					errorList.add(conversionMap(data,"学号不能为空"));
					continue;
				}
				if(EmptyUtils.isEmpty(data[1])){
					errorList.add(conversionMap(data,"姓名不能为空"));
					continue;
				}
				if(EmptyUtils.isEmpty(data[2])){
					errorList.add(conversionMap(data,"身份证号不能为空"));
					continue;
				}
				if(EmptyUtils.isEmpty(data[3])){
					errorList.add(conversionMap(data,"原教务班级不能为空"));
					continue;
				}
				if(EmptyUtils.isEmpty(data[4])){
					errorList.add(conversionMap(data,"新教务班级不能为空"));
					continue;
				}	
				if(EmptyUtils.isEmpty(data[5])){
					errorList.add(conversionMap(data,"所属学习中心不能为空"));
					continue;
				}
				Map<String,String> param = conversionMap(data,"");
				//查询是否存在学习中心
				GjtOrg gjtOrg=gjtOrgDao.findByOrgNameAndIsDeleted(param.get("orgName"),Constants.BOOLEAN_NO);
				//查询学员信息
				GjtStudentInfo gjtStudentInfo=gjtStudentInfoDao.findByXhAndXmAndIsDeleted(param.get("xh"), param.get("xm"),Constants.BOOLEAN_NO);
				GjtClassInfo oldGjtClassInfo=null;
				GjtClassInfo newGjtClassInfo=null;
				Boolean flag=true;
				if(gjtOrg==null){
					errorList.add(conversionMap(data,"学习中心未找到,请核对此项信息的正确性。"));
					flag=false;
					continue;
				}else{					
					//查询是否存在旧的教务班级
				    oldGjtClassInfo=gjtClassInfoService.queryTeachClassInfo(gjtStudentInfo.getStudentId());
					if(oldGjtClassInfo==null){
						errorList.add(conversionMap(data,"旧的教务班级未找到,请核对此项信息的正确性。"));
						flag=false;
						continue;
					}
					//查询是否存在新的教务班级
					newGjtClassInfo=gjtClassInfoService.queryByBjmcAndXxzxId(param.get("newTeachClass"), gjtOrg.getId());
					if(newGjtClassInfo==null){
						errorList.add(conversionMap(data,"新的教务班级未找到,请核对此项信息的正确性。"));
						flag=false;
						continue;
					}
				}
				//调班
				if(flag){
					//删除学员以前所在的教学班
					gjtClassStudentDao.deleteByStudentId(gjtStudentInfo.getStudentId(), "学员调班",oldGjtClassInfo.getClassId(),Constants.BOOLEAN_YES);
					if(oldGjtClassInfo.getGradeId().equals(newGjtClassInfo.getGradeId())){
						Boolean status=addStudentToClassStudent(newGjtClassInfo,gjtStudentInfo,oldGjtClassInfo);
						if(status){
							successList.add(param);
						}else{
							param.put("msg","EE群同步失败，调班失败！");
							errorList.add(param);
						}	
					}else{
						param.put("msg","旧教务班级和新教务班级不属于相同学期，调班失败！");
						errorList.add(param);
					}				
				}		
			}
			//保存失败/成功列表
			String successFile = ExcelService.renderExcelHadTitle(successList, Arrays.asList(heads), Arrays.asList(dbNames) ,"导入成功列表",path);
			String errorFile = ExcelService.renderExcelHadTitle(errorList, Arrays.asList(errortitle), Arrays.asList(errorNames) ,"导入失败列表",path);
			result.put("all_num",successList.size()+errorList.size());
			result.put("success_num",successList.size());
			result.put("error_num",errorList.size());
			result.put("success_file",successFile);
			result.put("error_file",errorFile);
			result.put("msg","success");
		} catch (Exception e) {
			result.put("msg","error");
			result.put("exception",e.getMessage());
		}
		return result;
	}
	/**自定义数组转MAP*/
	private Map<String,String> conversionMap(String[] data,String msg){
		Map<String,String>  re = new HashMap();
		if (EmptyUtils.isNotEmpty(msg)){
			re.put("msg",msg);
		}
		re.put("xh",data[0]);
		re.put("xm",data[1]);
		re.put("sfzh",data[2]);
		re.put("oldTeachClass",data[3]);
		re.put("newTeachClass",data[4]);
		re.put("orgName",data[5]);
		return  re;
	}
}
