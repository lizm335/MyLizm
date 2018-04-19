package com.gzedu.xlims.serviceImpl.transaction;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.ExcelService;
import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.common.gzedu.SignUtil;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.transaction.GjtSchoolRollTranAuditDao;
import com.gzedu.xlims.dao.transaction.GjtSchoolRollTranDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtSchoolRollTran;
import com.gzedu.xlims.pojo.GjtSchoolRollTransAudit;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtSyncLog;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.GjtSyncLogService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranAuditService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranService;

import net.sf.json.JSONObject;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年9月7日
 * @version 2.5
 */
@Service
public class GjtSchoolRollTranServiceImpl implements GjtSchoolRollTranService{
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(GjtSchoolRollTranServiceImpl.class);
	
	private static final String PAY_NEWSERVER_DOMAIN = AppConfig.getProperty("pay.newServerDomain");
	
	@Autowired
	private GjtSchoolRollTranDao gjtSchoolRollTranDao;
	
	@Autowired
	private GjtSchoolRollTranAuditDao gjtSchoolRollTranAuditDao;
	
	@Autowired
	private GjtSchoolRollTranAuditService gjtSchoolRollTranAuditService;

	@Autowired
	private GjtSyncLogService gjtSyncLogService;
	
	@Autowired
	GjtUserAccountDao gjtUserAccountDao;
	
	@Autowired
	GjtOrgDao gjtOrgDao;
	
	@Autowired
	private CommonDao commonDao;

	@Override
	@Transactional
	public boolean insertGjtSchoolRollTran(Map<String, Object> studentMap,GjtStudentInfo gjtStudentInfo) {
		try{	
			log.info("初始化审核记录开始：studentId"+gjtStudentInfo.getStudentId());
				Date now = new Date();
				String transactionId=UUIDUtils.random();
				GjtSchoolRollTran gjtSchoolRollTran=new GjtSchoolRollTran();		
				gjtSchoolRollTran.setTransactionId(transactionId);
				gjtSchoolRollTran.setStudentId(gjtStudentInfo.getStudentId());
				gjtSchoolRollTran.setTransactionType(Integer.parseInt(studentMap.get("transactionType").toString()));//异动类型
				gjtSchoolRollTran.setAuditOperatorRole(5);//默认为审核人为学籍科
				//如果异动类型是信息更正(5)，则需要插入信息更正子类型
				if(Integer.parseInt(studentMap.get("transactionType").toString())==5){
					gjtSchoolRollTran.setTransactionPartStatus(Integer.parseInt(studentMap.get("messageType").toString()));//信息更正子类型
				}				
				gjtSchoolRollTran.setCreatedDt(now);//当前申请时间
				gjtSchoolRollTran.setTransactionStatus(new BigDecimal(0));//异动状态
				gjtSchoolRollTran.setIsApplyFor("1");//是否是重新申请 默认为1-否
				//把需要变更的内容转换为json格式，存入TRANSACTION_CONTENT字段中
				JSONObject jsonObject = JSONObject.fromObject(studentMap);
				gjtSchoolRollTran.setTransactionContent(jsonObject.toString());			
				GjtSchoolRollTran entity=gjtSchoolRollTranDao.save(gjtSchoolRollTran);				
				Boolean result=addschoolRollTransAudit(gjtStudentInfo.getStudentId(),transactionId,"5");
				if(entity!=null&&result){
					log.info("学员异动申请成功：studentId"+gjtStudentInfo.getStudentId());
					return true;
				}														
		}catch(Exception e){
			e.printStackTrace();
			log.error("学员异动申请失败："+e.getMessage());
			return false;
		}
		return false;
	}
	/**
	 * 新增休学、信息更正和复学的申请记录
	 * @param studentId
	 * @param transactionId
	 * @param operatorRole
	 * @return
	 */
	public Boolean addschoolRollTransAudit(String studentId, String transactionId, String operatorRole) {
		try {
			//为学员自己插入一条申请记录			
			GjtSchoolRollTransAudit schoolRollTransAudit=new GjtSchoolRollTransAudit();
			Date now = new Date();
			schoolRollTransAudit.setId(UUIDUtils.random());
			schoolRollTransAudit.setTransactionId(transactionId);
			schoolRollTransAudit.setStudentId(studentId);
			schoolRollTransAudit.setAuditDt(now);
			schoolRollTransAudit.setAuditState(new BigDecimal(1));//默认为审核通过
			schoolRollTransAudit.setAuditOperatorRole(new BigDecimal(1));//审核角色：学员
			GjtSchoolRollTransAudit entity1=gjtSchoolRollTranAuditDao.save(schoolRollTransAudit);
			// 下一个审核角色：学籍科
			GjtSchoolRollTransAudit schoolRollTransAudit2=new GjtSchoolRollTransAudit();
			schoolRollTransAudit2.setId(UUIDUtils.random());
			schoolRollTransAudit2.setTransactionId(transactionId);
			schoolRollTransAudit2.setStudentId(studentId);
			schoolRollTransAudit2.setAuditState(new BigDecimal(0));//默认为待审核
			schoolRollTransAudit2.setAuditOperatorRole(new BigDecimal(operatorRole));//审核角色：学籍科
			GjtSchoolRollTransAudit entity2=gjtSchoolRollTranAuditDao.save(schoolRollTransAudit2);
			if(entity1!=null&&entity2!=null){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;	
	}

	public Boolean insertschoolRollTransAudit(String studentId,String transactionId, String operatorRole) {
		try {
			//为学员自己插入一条申请记录			
			GjtSchoolRollTransAudit schoolRollTransAudit=new GjtSchoolRollTransAudit();
			Date now = new Date();
			schoolRollTransAudit.setId(UUIDUtils.random());
			schoolRollTransAudit.setTransactionId(transactionId);
			schoolRollTransAudit.setStudentId(studentId);
			schoolRollTransAudit.setAuditDt(now);
			schoolRollTransAudit.setAuditState(new BigDecimal(1));//默认为审核通过
			schoolRollTransAudit.setAuditOperatorRole(new BigDecimal(-1));//审核角色：学员
			GjtSchoolRollTransAudit entity1=gjtSchoolRollTranAuditDao.save(schoolRollTransAudit);
			// 下一个审核角色：学习中心管理员
			GjtSchoolRollTransAudit schoolRollTransAudit2=new GjtSchoolRollTransAudit();
			schoolRollTransAudit2.setId(UUIDUtils.random());
			schoolRollTransAudit2.setTransactionId(transactionId);
			schoolRollTransAudit2.setStudentId(studentId);
			schoolRollTransAudit2.setAuditState(new BigDecimal(3));//默认为劝学中
			schoolRollTransAudit2.setAuditOperatorRole(new BigDecimal(operatorRole));//审核角色：招生办
			GjtSchoolRollTransAudit entity2=gjtSchoolRollTranAuditDao.save(schoolRollTransAudit2);
			if(entity1!=null&&entity2!=null){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;	
	}

	@Override
	public Page<GjtSchoolRollTran> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("transactionType", new SearchFilter("transactionType", Operator.EQ, 5));
		List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
		filters.put("gjtStudentInfo.gjtStudyCenter.gjtOrg.id",
				new SearchFilter("gjtStudentInfo.gjtStudyCenter.gjtOrg.id", SearchFilter.Operator.IN, orgList));
		Specification<GjtSchoolRollTran> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtSchoolRollTran.class);
		return gjtSchoolRollTranDao.findAll(spec, pageRequst);
	}

	@Override
	public List<Map<String, String>> querySchoolRollRransAuditList(String studentId,String transactionId) {
		log.info("searchParams:【transactionId="+transactionId+";studentId="+studentId+"】");
		return gjtSchoolRollTranAuditService.querySchoolRollRransAuditList(studentId,transactionId);
	}
	/**
	 * 查询学员异动的内容
	 */
	@Override
	public List<GjtSchoolRollTran> findSchoolRollTran(int messageType, String studentId,int transactionType) {
		log.info("searchParams:【messageType="+messageType+";studentId="+studentId+"】");
		return gjtSchoolRollTranDao.findSchoolRollTran(messageType,studentId,transactionType);
	}
	@Override
	public List<GjtSchoolRollTran> findByStudentIdAndIsDeletedOrderByCreatedDt(String studentId, String booleanNo) {
		return gjtSchoolRollTranDao.findByStudentIdAndIsDeletedOrderByCreatedDtAsc(studentId,booleanNo);
	}

	@Override
	public GjtSchoolRollTran findById(String transactionId) {
		log.info("searchParams:【transactionId="+transactionId+"】");
		return gjtSchoolRollTranDao.findOne(transactionId);
	}

	@Override
	public List<Map<String, Object>> querySchoolRollTranList(Map searchParams,String orgId) {
		Map params = null;
		String sql = null;
		Map<String,Object> handlerMap=this.querySchoolRollTranMap(searchParams,orgId);
		if(EmptyUtils.isNotEmpty(handlerMap)){
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForMapList(sql,params);
	}

	public Map<String, Object> querySchoolRollTranMap(Map searchParams,String orgId) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();
		sql.append("  select gsi.xm,");
		sql.append("  gsi.xh,");
		sql.append("  gsi.sjh,");
		sql.append("  gsi.sfzh,");
		sql.append("  (select t1.name");
		sql.append("  from tbl_sys_data t1");
		sql.append("  where t1.is_deleted = 'N'");
		sql.append("  and t1.type_code = 'TrainingLevel'");
		sql.append("  and t1.code = gsi.pycc) pycc,");
		sql.append("  gy.name,");
		sql.append("  gg.grade_name,");
		sql.append("  gs.zymc,");
		sql.append("  go.org_name,");
		sql.append("  (select t1.name");
		sql.append("  from tbl_sys_data t1");
		sql.append("  where t1.is_deleted = 'N'");
		sql.append("  and t1.type_code = 'StudentNumberStatus'");
		sql.append("  and t1.code = gsi.xjzt) xjzt,");
		sql.append("  (select t1.name");
		sql.append("  from tbl_sys_data t1");
		sql.append("  where t1.is_deleted = 'N'");
		sql.append("  and t1.type_code = 'USER_TYPE'");
		sql.append("  and t1.code = gsi.user_type)user_type,");
		sql.append("  gsrt.transaction_type,");
		sql.append("  gsrt.transaction_status");
		sql.append("  from gjt_school_roll_trans gsrt");
		sql.append("  left join gjt_student_info gsi");
		sql.append("  on gsi.student_id = gsrt.student_id");
		sql.append("  and gsi.is_deleted = 'N'");
		sql.append("  left join view_student_info vsi");
		sql.append("  on gsi.student_id = vsi.STUDENT_ID");
		sql.append("  left join gjt_grade gg");
		sql.append("  on gg.grade_id = vsi.GRADE_ID");
		sql.append("  and gg.is_deleted = 'N'");
		sql.append("  left join gjt_specialty gs");
		sql.append("  on gs.specialty_id = vsi.MAJOR");
		sql.append("  and gs.is_deleted = 'N'");
		sql.append("  left join gjt_year gy");
		sql.append("  on gy.grade_id = gg.year_id");
		sql.append("  left join gjt_org go on go.id=gsi.xxzx_id");
		sql.append("  and go.is_deleted='N'");
		sql.append("  where gsrt.is_deleted='N' ");
		sql.append("  and gsi.xxzx_id in ");
		sql.append("  (SELECT org.ID");
		sql.append("  FROM GJT_ORG org");
		sql.append("  WHERE org.IS_DELETED = 'N'");
		sql.append("  START WITH org.ID ='"+orgId+"' ");
		sql.append("  CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID)");
		
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("LIKE_gjtStudentInfo.xm")))) {
			sql.append("  and gsi.xm like :xm");
			params.put("xm", "%"+ObjectUtils.toString(searchParams.get("LIKE_gjtStudentInfo.xm"))+"%");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.xh")))) {
			sql.append("  and gsi.xh =:xh");
			params.put("xh", ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.xh")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.gjtStudyCenter.gjtOrg.id")))) {
			sql.append("  and gsi.xxzx_id =:xxzx_id");
			params.put("xxzx_id", ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.gjtStudyCenter.gjtOrg.id")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.gjtSpecialty.specialtyId")))) {
			sql.append("  and gsi.major =:major");
			params.put("major", ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.gjtSpecialty.specialtyId")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.gjtGrade.gjtYear.gradeId")))) {
			sql.append("  and gy.grade_id =:year_grade_id");
			params.put("year_grade_id", ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.gjtGrade.gjtYear.gradeId")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.pycc")))) {
			sql.append("  and gsi.pycc =:pycc");
			params.put("pycc", ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.pycc")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_viewStudentInfo.gradeId")))) {
			sql.append("  and vsi.grade_id =:gradeId");
			params.put("gradeId", ObjectUtils.toString(searchParams.get("EQ_viewStudentInfo.gradeId")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.xjzt")))) {
			sql.append("  and gsi.xjzt =:xjzt");
			params.put("xjzt", ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.xjzt")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.userType")))) {
			sql.append("  and gsi.xjzt =:user_type");
			params.put("user_type", ObjectUtils.toString(searchParams.get("EQ_gjtStudentInfo.userType")));
		}
		Map<String,Object> Map = new HashMap<String, Object>();
		Map.put("sql",sql.toString());
		Map.put("params",params);
		return Map;
	}

	@Override
	public String downLoadTranactionList(List<Map<String, Object>> resultList,String fileName,String path) {
		String []  titles =new String[]{"姓名","学号","手机号","身份证号","层次","年级","学期","专业","学籍状态","学员类型","学习中心","异动类型","异动状态"};
		String []  dbNames =new String[]{"XM","XH","SJH","SFZH","PYCC","NAME","GRADE_NAME","ZYMC","XJZT","USER_TYPE","ORG_NAME", "TRANSACTION_TYPE","TRANSACTION_STATUS"}; 
		List datas = new ArrayList();
		for (Map <String,Object> map : resultList) {
			Object transactionType = map.get("TRANSACTION_TYPE");
			Object transactionStatus = map.get("TRANSACTION_STATUS");
			if(Integer.parseInt(transactionType.toString())==1){
				map.put("TRANSACTION_TYPE", "转专业");
			}else if(Integer.parseInt(transactionType.toString())==2){
				map.put("TRANSACTION_TYPE", "休学");
			}else if(Integer.parseInt(transactionType.toString())==3){
				map.put("TRANSACTION_TYPE", "复学");
			}else if(Integer.parseInt(transactionType.toString())==4){
				map.put("TRANSACTION_TYPE", "退学");
			}else if(Integer.parseInt(transactionType.toString())==5){
				map.put("TRANSACTION_TYPE", "信息更正");
			}else{
				map.put("TRANSACTION_TYPE", "转学");
			}
			
			if(Integer.parseInt(transactionStatus.toString())==0){
				map.put("TRANSACTION_STATUS", "待审核");
			}else if(Integer.parseInt(transactionStatus.toString())==1){
				map.put("TRANSACTION_STATUS", "审核通过");
			}else{
				map.put("TRANSACTION_STATUS", "审核不通过");
			}
			datas.add(map);
		}		
		String outFileName = ExcelService.renderExcel(datas, titles,dbNames ,"学籍异动申请明细表",path,fileName);
		return outFileName;
	}
	 private static HSSFFont getFont(HSSFWorkbook wb, short size, String fontType, short fine) {
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints(size);
        font.setFontName(fontType);
        font.setBoldweight(fine);
        return font;
	  }
 /**
  * 
  * @param wb
  * @return 单元格样式(居中 四边框)
  */
	 private static HSSFCellStyle getStyle(HSSFWorkbook wb) {
	     HSSFCellStyle style = wb.createCellStyle();
	     short size = 1;
	     // 边框
	     style.setBorderBottom(size);
	     style.setBorderLeft(size);
	     style.setBorderTop(size);
	     style.setBorderRight(size);
	     
	     style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直对齐
	     style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平对齐
	     
	     return style;
	 }

	@Override
	public List<GjtSchoolRollTran> findByStudentIdAndTransactionTypeIsDeleted(String studentId,int type,String booleanNo) {		
		return gjtSchoolRollTranDao.findByStudentIdAndTransactionTypeAndIsDeletedOrderByCreatedDtDesc(studentId,type,booleanNo);
	}

	@Override
	public boolean againSubmitGjtSchoolRollTran(GjtStudentInfo gjtStudentInfo, int transactionType, int transactionPartStatus) {
		List<GjtSchoolRollTran> gjtSchoolRollTran=gjtSchoolRollTranDao.findSchoolRollTran(transactionPartStatus,gjtStudentInfo.getStudentId(),transactionType);	
		if(gjtSchoolRollTran!=null&&gjtSchoolRollTran.size()>0){
			GjtSchoolRollTran rollTran=gjtSchoolRollTran.get(0);
			rollTran.setIsApplyFor("0");
			gjtSchoolRollTranDao.save(rollTran);			
		}			
		return true;
	}

	@Override
	public Boolean againSubmitGjtSchoolRollTranNew(GjtStudentInfo gjtStudentInfo, int transactionType) {
		List<GjtSchoolRollTran> gjtSchoolRollTran=gjtSchoolRollTranDao.findByStudentIdAndTransactionTypeAndIsDeletedOrderByCreatedDtDesc(gjtStudentInfo.getStudentId(),transactionType,Constants.BOOLEAN_NO);		
		Date now=new Date();
		if(gjtSchoolRollTran!=null&&gjtSchoolRollTran.size()>0){
			GjtSchoolRollTran rollTran=gjtSchoolRollTran.get(0);
			rollTran.setIsApplyFor("0");
			gjtSchoolRollTranDao.save(rollTran);
		}		
		return true;
	}

	@Override
	public void updateGjtSchoolRollTran(Map<String, Object> studentMap,  GjtSchoolRollTran rollTran) {
		try {
			Date date=new Date();
			//把需要变更的内容转换为json格式，存入TRANSACTION_CONTENT字段中
			JSONObject transactionContent = JSONObject.fromObject(studentMap);
			rollTran.setTransactionContent(transactionContent.toString());
			rollTran.setUpdatedDt(date);
			rollTran.setIsApplyFor("1");
			gjtSchoolRollTranDao.save(rollTran);
		} catch (Exception e) {
			log.error("更新异动申请表内容出错："+rollTran.getTransactionId());
		}
		
	}

	@Override
	public Boolean getCurrentDate(GjtGrade currentGrade) {
		log.info("当前学期计划的ID：【"+currentGrade.getGradeId()+"】");
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//获取当前学期计划的开学时间后三周时间
			Date startDate=currentGrade.getStartDate();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.DATE, 21);
			Date date = calendar.getTime();
			Date day = format.parse(format.format(date));
			//获取当前时间
			Date currentDate=new Date();
			if(day.getTime()>currentDate.getTime()){//当前时间小于当前学期计划的开学时间后三周时间
				return false;
			}
			if(day.getTime()<=currentDate.getTime()){//当前时间大于等于当前学期计划的开学时间后三周时间
				return true;
			}
		} catch (Exception e) {
			log.info("时间转换出错："+e.getMessage());
			e.printStackTrace();
		}		
		return false;
	}

	@Override
	public boolean inertDropOutStudy(Map<String, Object> outStudyMap, GjtStudentInfo gjtStudentInfo) {
		log.info("当前学员ID：【"+gjtStudentInfo.getStudentId()+"】");
		try {
			Date now=new Date();
			GjtSchoolRollTran gjtSchoolRollTran=new GjtSchoolRollTran();
			String transactionId=UUIDUtils.random();
			gjtSchoolRollTran.setTransactionId(transactionId);
			gjtSchoolRollTran.setIsApplyFor("1");
			gjtSchoolRollTran.setStudentId(gjtStudentInfo.getStudentId());
			gjtSchoolRollTran.setTransactionType(Integer.parseInt("4"));//异动类型
			gjtSchoolRollTran.setAuditOperatorRole(4);//默认为审核人为招生办		
			gjtSchoolRollTran.setCreatedDt(now);//当前申请时间
			gjtSchoolRollTran.setTransactionStatus(new BigDecimal(3));//异动状态
			//把需要变更的内容转换为json格式，存入TRANSACTION_CONTENT字段中
			JSONObject jsonObject = JSONObject.fromObject(outStudyMap);
			gjtSchoolRollTran.setTransactionContent(jsonObject.toString());			
			GjtSchoolRollTran entity=gjtSchoolRollTranDao.save(gjtSchoolRollTran);
			//插入审核记录
			insertschoolRollTransAudit(gjtStudentInfo.getStudentId(),transactionId,"4");
		} catch (Exception e) {
			log.info("学员退学失败："+e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	@Override
	public Page<GjtSchoolRollTran> queryRollTransAll(String id, int transType, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("transactionType", new SearchFilter("transactionType", Operator.EQ, transType));
		//List<String> orgList = gjtOrgDao.queryChildsByParentId(id);
		filters.put("gjtStudentInfo.gjtSchoolInfo.gjtOrg.id",
				new SearchFilter("gjtStudentInfo.gjtSchoolInfo.gjtOrg.id", SearchFilter.Operator.EQ, id));
		Specification<GjtSchoolRollTran> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtSchoolRollTran.class);
		return gjtSchoolRollTranDao.findAll(spec, pageRequst);
	}

	@Override
	public Page<GjtSchoolRollTran> queryGjtSchoolRollTransAll(String id, int i, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("gjtStudentInfo.gjtSchoolInfo.gjtOrg.id",
				new SearchFilter("gjtStudentInfo.gjtSchoolInfo.gjtOrg.id", SearchFilter.Operator.EQ, id));
		Specification<GjtSchoolRollTran> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtSchoolRollTran.class);
		return gjtSchoolRollTranDao.findAll(spec, pageRequest);
	}

	@Override
	public int syncDropOutStudy(Map<String, Object> outStudyMap,GjtStudentInfo info) {
		try {
			Map params = new HashMap();
			Map<String,String> map = new HashMap<String,String>();
			long time = System.currentTimeMillis();
			params.put("order_sn", info.getGjtSignup().getOrderSn());//订单编号
			params.put("school_code", info.getGjtSchoolInfo().getGjtOrg().getCode());//院校编码
			params.put("bank", outStudyMap.get("openBank"));//开户行
			params.put("bank_account", outStudyMap.get("bankCardNumber"));//开户账号
			params.put("reason", outStudyMap.get("cause"));//退学原因
			params.put("account_username", outStudyMap.get("accountName"));//开户人姓名
			params.put("sign", SignUtil.formatUrlMapTX(params,time));
			params.put("appid", SignUtil.TX_APPID);
			params.put("time", String.valueOf(time));
			log.error("招生平台退学接口返回数据 syncDropOutStudy params"+params);
			String result = HttpClientUtils.doHttpPost(PAY_NEWSERVER_DOMAIN + "/Refundapplyapi/apply.html", params, 10000, "UTF-8");
			log.error("招生平台退学接口返回数据 syncDropOutStudy result"+result);
			if (StringUtils.isNotEmpty(result)) {
				// 记录同步失败日志
				gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0007, GsonUtils.toJson(params), "success" + result));

				JSONObject json = JSONObject.fromObject(result);
				int status = json.getInt("status");
				return status;				
			} else {
				// 记录同步失败日志
				gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0007, GsonUtils.toJson(params), result));
			}
		} catch (Exception e) {
			log.error("招生平台退学接口发生异常！ syncDropOutStudy " + e.getMessage(), e);
		}
		return 0;
	}
	/**
	 * 学员待确认应扣、应退金额
	 */
	@Override
	public int studentConfirmCost(String transactionId,GjtStudentInfo gjtStudentInfo) {
		log.info("当前异动transactionId：【"+transactionId+"】");
		try {
			Date now=new Date();
			GjtSchoolRollTran gjtSchoolRollTran=gjtSchoolRollTranDao.findOne(transactionId);
			List<GjtSchoolRollTransAudit> auditList=gjtSchoolRollTranAuditDao.
					findByTransactionIdAndStudentIdAndIsDeletedOrderByAuditDtAsc(transactionId,gjtStudentInfo.getStudentId(),Constants.BOOLEAN_NO);
			gjtSchoolRollTran.setUpdatedDt(now);
			gjtSchoolRollTran.setAuditOperatorRole(0);//院长
			gjtSchoolRollTran.setTransactionStatus(new BigDecimal(8));//待确认
			String content=gjtSchoolRollTran.getTransactionContent();
			JSONObject jsonObject = JSONObject.fromObject(content);
			String revocation=jsonObject.getString("revocation");//是否显示撤销退学按钮
			if("0".equals(revocation)){
				revocation="1";
			}			
			jsonObject.put("revocation", revocation);
			gjtSchoolRollTran.setTransactionContent(jsonObject.toString());
			gjtSchoolRollTranDao.save(gjtSchoolRollTran);//更新异动申请表中的数据
			GjtSchoolRollTransAudit audit=auditList.get(auditList.size() - 1);
			audit.setAuditDt(now);
			audit.setAuditOperatorRole(new BigDecimal(1));
			audit.setAuditState(new BigDecimal(9));
			audit.setUpdatedDt(now);
			gjtSchoolRollTranAuditDao.save(audit);//更新异动审核表中的数据
			GjtSchoolRollTransAudit gjtSchoolRollTransAudit=new GjtSchoolRollTransAudit();
			gjtSchoolRollTransAudit.setId(UUIDUtils.random());
			gjtSchoolRollTransAudit.setTransactionId(transactionId);
			gjtSchoolRollTransAudit.setStudentId(gjtStudentInfo.getStudentId());
			gjtSchoolRollTransAudit.setAuditState(new BigDecimal(8));
			gjtSchoolRollTransAudit.setAuditOperatorRole(new BigDecimal(0));
			gjtSchoolRollTranAuditDao.save(gjtSchoolRollTransAudit);
			//发送短信通知下个审核人：院长
			GjtUserAccount account=gjtUserAccountDao.findByLoginAccount("gzyzadmin");
			try {					
				SMSUtil.sendMessage(account.getSjh(),String.format("%1$s学员已确认退学应扣、应退费用，请登录教务平台-学籍异动-退学栏目，进行确认退学处理！",gjtStudentInfo.getXm()),"gk");
			} catch (Exception e) {				
			}
			//同步至招生平台
			gjtSchoolRollTranAuditService.syncOutStudyRransAudit(gjtStudentInfo,"5");//院长待确认
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public long queryStatusTotalNum(String xxId,int OperatorRole, int transactionStatus,String transactionType) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("transactionStatus", new SearchFilter("transactionStatus", Operator.EQ, transactionStatus));//审核状态
		filters.put("transactionType", new SearchFilter("transactionType", Operator.EQ, Integer.parseInt(transactionType)));//异动状态
		if(OperatorRole!=-1){
			filters.put("auditOperatorRole", new SearchFilter("auditOperatorRole", Operator.EQ, OperatorRole));//审核人角色
		}		
		filters.put("gjtStudentInfo.gjtSchoolInfo.gjtOrg.id",
				new SearchFilter("gjtStudentInfo.gjtSchoolInfo.gjtOrg.id", SearchFilter.Operator.EQ, xxId));
		Specification<GjtSchoolRollTran> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtSchoolRollTran.class);
		return gjtSchoolRollTranDao.count(spec);
	}

	@Override
	public long queryTotalNum(String xxId,int transactionType) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("transactionType", new SearchFilter("transactionType", Operator.EQ, transactionType));//异动状态
		filters.put("gjtStudentInfo.gjtSchoolInfo.gjtOrg.id",
				new SearchFilter("gjtStudentInfo.gjtSchoolInfo.gjtOrg.id", SearchFilter.Operator.EQ, xxId));
		Specification<GjtSchoolRollTran> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtSchoolRollTran.class);
		return gjtSchoolRollTranDao.count(spec);
	}

	@Override
	public Map<String, Object> queryStudentApplicationMsg(String studentId) {
		log.info("studentId：【"+studentId+"】");
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select");
		sql.append("  gsi.xm as \"studentName\",");
		sql.append("  tsd2.name as \"sex\",");
		sql.append("  nvl(gsi.bh,' ')  as \"bh\",");
		sql.append("  gsi.xh as \"studentCode\",");
		sql.append("  gg.grade_name as \"gradeName\",");
		sql.append("  tsd3.name as \"userType\",");
		sql.append("  tsd1.name  as \"trainingLevel\",");
		sql.append("  gs.zymc as \"specialty\",");
		sql.append("  nvl(to_char(gsr.created_dt, 'YYYY\"年\"MM\"月\"DD\"日\"'), ' ') as \"createdDt\" ");
		sql.append("  from gjt_school_roll_trans gsr");
		sql.append("  inner join gjt_student_info gsi");
		sql.append("  on gsi.student_id = gsr.student_id");
		sql.append("  and gsi.is_deleted = 'N'");
		sql.append("  inner join view_student_info vsi");
		sql.append("  on vsi.student_id = gsi.student_id");
		sql.append("  inner join gjt_specialty gs");
		sql.append("  on gs.specialty_id = vsi.major");
		sql.append("  and gs.is_deleted = 'N'");
		sql.append("  inner join gjt_grade gg");
		sql.append("  on gg.grade_id = vsi.grade_id");
		sql.append("  and gg.is_deleted = 'N'");
		sql.append("  left join tbl_sys_data tsd1");
		sql.append("  on tsd1.type_code = 'TrainingLevel'");
		sql.append("  and tsd1.code = gsi.pycc");
		sql.append("  left join tbl_sys_data tsd2");
		sql.append("  on tsd2.type_code = 'Sex'");
		sql.append("  and tsd2.code = gsi.xbm");
		sql.append("  left join tbl_sys_data tsd3");
		sql.append("  on tsd3.type_code = 'USER_TYPE'");
		sql.append("  and tsd3.code = gsi.user_type");
		sql.append("  where gsi.student_id =:studentId");
		map.put("studentId", studentId);
		return commonDao.queryObjectToMapNative(sql.toString(), map);
	}

	@Override
	public int studentRevocation(String transactionId, GjtStudentInfo gjtStudentInfo) {
		log.info("当前异动transactionId：【"+transactionId+"】");
		try {
			Date now=new Date();
			GjtSchoolRollTran gjtSchoolRollTran=gjtSchoolRollTranDao.findOne(transactionId);
			List<GjtSchoolRollTransAudit> auditList=gjtSchoolRollTranAuditDao.
					findByTransactionIdAndStudentIdAndIsDeletedOrderByAuditDtAsc(transactionId,gjtStudentInfo.getStudentId(),Constants.BOOLEAN_NO);
			gjtSchoolRollTran.setUpdatedDt(now);
			gjtSchoolRollTran.setAuditOperatorRole(1);//学员
			gjtSchoolRollTran.setTransactionStatus(new BigDecimal(13));//撤销退学
			gjtSchoolRollTran.setIsApplyFor("0");
			gjtSchoolRollTranDao.save(gjtSchoolRollTran);//更新异动申请表中的数据
			//更新审核记录表中的信息
			GjtSchoolRollTransAudit audit=auditList.get(auditList.size() - 1);
			audit.setAuditDt(now);
			audit.setUpdatedDt(now);				
			gjtSchoolRollTranAuditDao.save(audit);
			GjtSchoolRollTransAudit gjtSchoolRollTransAudit=new GjtSchoolRollTransAudit();
			gjtSchoolRollTransAudit.setId(UUIDUtils.random());
			gjtSchoolRollTransAudit.setTransactionId(gjtSchoolRollTran.getTransactionId());
			gjtSchoolRollTransAudit.setStudentId(gjtStudentInfo.getStudentId());
			gjtSchoolRollTransAudit.setAuditOperatorRole(new BigDecimal(1));//学员
			gjtSchoolRollTransAudit.setAuditState(new BigDecimal(13));//撤销退学
			gjtSchoolRollTranAuditDao.save(gjtSchoolRollTransAudit);
			//同步至招生平台
			gjtSchoolRollTranAuditService.syncOutStudyRransAudit(gjtStudentInfo,"8");//学员撤销退学
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<GjtSchoolRollTran> findByDropOutStudyRollTran(String studentId, int transactionType) {
		return gjtSchoolRollTranDao.findByDropOutStudyRollTran(studentId,transactionType);
	}

	@Override
	public boolean insertChangeSpecialty(Map<String, Object> changeSpecialtyMap, GjtStudentInfo gjtStudentInfo) {
		log.info("当前学员ID：【"+gjtStudentInfo.getStudentId()+"】");
		try {
			Date now=new Date();
			GjtSchoolRollTran gjtSchoolRollTran=new GjtSchoolRollTran();
			String transactionId=UUIDUtils.random();
			gjtSchoolRollTran.setTransactionId(transactionId);
			gjtSchoolRollTran.setIsApplyFor("1");
			gjtSchoolRollTran.setStudentId(gjtStudentInfo.getStudentId());
			gjtSchoolRollTran.setTransactionType(Integer.parseInt("1"));//异动类型：转专业
			gjtSchoolRollTran.setAuditOperatorRole(5);//默认为审核人为学籍科		
			gjtSchoolRollTran.setCreatedDt(now);//当前申请时间
			gjtSchoolRollTran.setTransactionStatus(new BigDecimal(0));//异动状态:待审核
			//把需要变更的内容转换为json格式，存入TRANSACTION_CONTENT字段中
			JSONObject jsonObject = JSONObject.fromObject(changeSpecialtyMap);
			gjtSchoolRollTran.setTransactionContent(jsonObject.toString());			
			GjtSchoolRollTran entity=gjtSchoolRollTranDao.save(gjtSchoolRollTran);
			//插入审核记录
			insertChangeSpecialtyAudit(gjtStudentInfo.getStudentId(),transactionId,"5");			
		} catch (Exception e) {
			log.info("学员退学失败："+e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private Boolean insertChangeSpecialtyAudit(String studentId, String transactionId, String operatorRole) {
		try {
			//为学员自己插入一条申请记录			
			GjtSchoolRollTransAudit schoolRollTransAudit=new GjtSchoolRollTransAudit();
			Date now = new Date();
			schoolRollTransAudit.setId(UUIDUtils.random());
			schoolRollTransAudit.setTransactionId(transactionId);
			schoolRollTransAudit.setStudentId(studentId);
			schoolRollTransAudit.setAuditDt(now);
			schoolRollTransAudit.setAuditState(new BigDecimal(1));//默认为审核通过
			schoolRollTransAudit.setAuditOperatorRole(new BigDecimal(1));//审核角色：学员
			GjtSchoolRollTransAudit entity1=gjtSchoolRollTranAuditDao.save(schoolRollTransAudit);
			// 下一个审核角色：学籍科
			GjtSchoolRollTransAudit schoolRollTransAudit2=new GjtSchoolRollTransAudit();
			schoolRollTransAudit2.setId(UUIDUtils.random());
			schoolRollTransAudit2.setTransactionId(transactionId);
			schoolRollTransAudit2.setStudentId(studentId);
			schoolRollTransAudit2.setAuditState(new BigDecimal(0));//默认为待审核
			schoolRollTransAudit2.setAuditOperatorRole(new BigDecimal(operatorRole));//审核角色：学籍科
			GjtSchoolRollTransAudit entity2=gjtSchoolRollTranAuditDao.save(schoolRollTransAudit2);
			if(entity1!=null&&entity2!=null){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;			
	}
	@Override
	public List<GjtSchoolRollTran> findByStudentId(String studentId) {
		return gjtSchoolRollTranDao.findByStudentId(studentId);
	}	
}
