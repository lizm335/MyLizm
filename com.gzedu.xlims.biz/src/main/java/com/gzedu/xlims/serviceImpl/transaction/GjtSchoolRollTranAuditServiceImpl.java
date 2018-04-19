package com.gzedu.xlims.serviceImpl.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.common.gzedu.SignUtil;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.transaction.GjtSchoolRollTranAuditDao;
import com.gzedu.xlims.dao.transaction.GjtSchoolRollTranDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.pojo.GjtSchoolRollTran;
import com.gzedu.xlims.pojo.GjtSchoolRollTransAudit;
import com.gzedu.xlims.pojo.GjtSchoolRollTransCost;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.opi.OpiStudent;
import com.gzedu.xlims.pojo.opi.OpiStudentData;
import com.gzedu.xlims.pojo.opi.RSimpleData;
import com.gzedu.xlims.pojo.status.SchoolRollTransEnum;
import com.gzedu.xlims.pojo.status.SchoolRollTransTypeEnum;
import com.gzedu.xlims.pojo.status.StudentMessageMoveEnum;
import com.gzedu.xlims.service.pcourse.PCourseServer;
import com.gzedu.xlims.service.signup.SignupDataAddService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranAuditService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTransCostService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.third.eechat.data.EEIMUpdateInfo;
import com.gzedu.xlims.third.eechat.data.EEIMUpdateInfoReturnData;
import com.gzedu.xlims.third.eechat.util.EEIMService;

import net.sf.json.JSONObject;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年9月9日
 * @version 2.5
 */
@Service
public class GjtSchoolRollTranAuditServiceImpl implements GjtSchoolRollTranAuditService{
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(GjtSchoolRollTranAuditServiceImpl.class);
	
	private static final String PAY_NEWSERVER_DOMAIN = AppConfig.getProperty("pay.newServerDomain");
	
	@Autowired
	private GjtSchoolRollTranAuditDao gjtSchoolRollTranAuditDao;
	
	@Autowired
	private GjtSchoolRollTranDao gjtSchoolRollTranDao;
	
	@Autowired
	private GjtUserAccountDao gjtUserAccountDao;
	
	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;
	
	@Autowired
	private SignupDataAddService signupDataAddService;

	@Autowired
	private GjtSchoolRollTransCostService gjtSchoolRollTransCostService;
	
	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager em;
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private PCourseServer pCourseServer;
	
	@Autowired
	private EEIMService  eeIMService;
	
	/**
	 * 查询学员是否存在审核记录
	 */
	@Override
	public List<GjtSchoolRollTransAudit> queryGjtSchoolRollTranAudit(int messageType, String studentId,
			int transactionType) {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT GSTA.*");
		sql.append("  FROM GJT_SCHOOL_ROLL_TRANS GST");
		sql.append("  INNER JOIN GJT_SCHOOL_ROLL_TRANS_AUDIT GSTA");
		sql.append("  ON GST.STUDENT_ID = GSTA.STUDENT_ID");
		sql.append("  AND GST.TRANSACTION_ID = GSTA.TRANSACTION_ID");
		sql.append("  WHERE GST.IS_DELETED = 'N'");
		sql.append("  AND GSTA.IS_DELETED = 'N'");
		sql.append("  AND GST.TRANSACTION_TYPE = "+transactionType+" ");
		sql.append("  AND GST.TRANSACTION_PART_STATUS = "+messageType+" ");
		sql.append("  AND GST.STUDENT_ID='"+studentId+"'");
		sql.append("  ORDER BY GSTA.CREATED_DT,GSTA.AUDIT_OPERATOR_ROLE ");
		Query query = em.createNativeQuery(sql.toString(), GjtSchoolRollTransAudit.class);
		return query.getResultList();
	}

	/**
	 * 查询学员是否存在审核记录--学习空间接口
	 */
	@Override
	public List<Map<String, String>> querySchoolRollRransAuditList(String studentId,String transactionId) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  SELECT GSTA.* ");
		sql.append("  FROM GJT_SCHOOL_ROLL_TRANS GST");
		sql.append("  INNER JOIN GJT_SCHOOL_ROLL_TRANS_AUDIT GSTA");
		sql.append("  ON GST.STUDENT_ID = GSTA.STUDENT_ID");
		sql.append("  AND GST.TRANSACTION_ID = GSTA.TRANSACTION_ID");
		sql.append("  WHERE GST.IS_DELETED = 'N'");
		sql.append("  AND GSTA.IS_DELETED = 'N'");	
		if(EmptyUtils.isNotEmpty(studentId)){
			sql.append("  AND GST.STUDENT_ID =:STUDENT_ID ");
			params.put("STUDENT_ID", studentId);
		}
		if(EmptyUtils.isNotEmpty(transactionId)){
			sql.append("  AND GST.TRANSACTION_ID =:TRANSACTION_ID ");
			params.put("TRANSACTION_ID", transactionId);
		}	
		sql.append("  ORDER BY GSTA.AUDIT_DT, GSTA.AUDIT_OPERATOR_ROLE");		
		return commonDao.queryForMapListNative(sql.toString(), params);		
	}

	@Override
	public List<GjtSchoolRollTransAudit> queryTransAuditInfo(String transactionId, String studentId, String booleanNo) {		
		return gjtSchoolRollTranAuditDao.findByTransactionIdAndStudentIdAndIsDeletedOrderByAuditDtAsc(transactionId,studentId,booleanNo);
	}

	@Override
	public boolean updateSchoolRollTranAudit(Map<String, Object> data) {
		log.info("参数查询：【transactionId："+data.get("transactionId")+";auditState:"+data.get("auditState")+";studentId:"+data.get("studentId")+"】");
		try {
			GjtSchoolRollTran gjtSchoolRollTran=gjtSchoolRollTranDao.findOne(data.get("transactionId").toString());
			List<GjtSchoolRollTransAudit> auditList=gjtSchoolRollTranAuditDao.
					findByTransactionIdAndStudentIdAndIsDeletedOrderByAuditDtAsc(data.get("transactionId").toString(),data.get("studentId").toString(),Constants.BOOLEAN_NO);
			Date now=new Date();
			if("2".equals(data.get("auditState").toString())){//审核不通过,则不往下插入审核记录数据
				gjtSchoolRollTran.setUpdatedDt(now);;
				gjtSchoolRollTran.setAuditOperator(data.get("roleName").toString());
				gjtSchoolRollTran.setTransactionStatus(new BigDecimal(data.get("auditState").toString()));
				//更新申请记录表中的状态
				GjtSchoolRollTran result=gjtSchoolRollTranDao.save(gjtSchoolRollTran);
				GjtSchoolRollTransAudit audit=auditList.get(auditList.size() - 1);
				audit.setAuditDt(now);
				audit.setAuditOperator(data.get("roleName").toString());
				audit.setAuditContent((String) data.get("auditContent"));
				audit.setAuditState(new BigDecimal((String)data.get("auditState")));
				audit.setUpdatedDt(now);
				//更新审核记录表中的信息
				GjtSchoolRollTransAudit result2=gjtSchoolRollTranAuditDao.save(audit);
				if(result!=null&&result2!=null){
					return true;
				}				
			}else{////审核通过,则继续往下插入审核记录数据,并更新相关状态
				gjtSchoolRollTran.setUpdatedDt(now);;
				gjtSchoolRollTran.setAuditOperatorRole(5);
				gjtSchoolRollTran.setTransactionStatus(new BigDecimal(0));
				gjtSchoolRollTran.setAuditOperator(data.get("roleName").toString());
				gjtSchoolRollTranDao.save(gjtSchoolRollTran);
				GjtSchoolRollTransAudit audit=auditList.get(auditList.size() - 1);
				audit.setAuditDt(now);
				audit.setAuditContent((String) data.get("auditContent"));
				audit.setAuditState(new BigDecimal((String)data.get("auditState")));
				audit.setAuditOperator(data.get("roleName").toString());
				gjtSchoolRollTranAuditDao.save(audit);
				GjtSchoolRollTransAudit gjtSchoolRollTransAudit=new GjtSchoolRollTransAudit();
				gjtSchoolRollTransAudit.setId(UUIDUtils.random());
				gjtSchoolRollTransAudit.setTransactionId(gjtSchoolRollTran.getTransactionId());
				gjtSchoolRollTransAudit.setStudentId((String)data.get("studentId"));
				gjtSchoolRollTransAudit.setAuditState(new BigDecimal(0));
				gjtSchoolRollTransAudit.setAuditOperatorRole(new BigDecimal(5));
				GjtSchoolRollTransAudit entity=gjtSchoolRollTranAuditDao.save(gjtSchoolRollTransAudit);
				if(entity!=null){
					return true;
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
			log.error("学习中心管理员审核失败=="+e.getMessage());
			return false;
		}
		return false;
	}

	@Override
	@Transactional
	public boolean updateTransAudit(Map<String, Object> data) {
		try {
			GjtSchoolRollTran gjtSchoolRollTran=gjtSchoolRollTranDao.findOne(data.get("transactionId").toString());
			List<GjtSchoolRollTransAudit> auditList=gjtSchoolRollTranAuditDao.
					findByTransactionIdAndStudentIdAndIsDeletedOrderByAuditDtAsc(data.get("transactionId").toString(),data.get("studentId").toString(),Constants.BOOLEAN_NO);
			Date now=new Date();
			if("2".equals(data.get("auditState").toString())){//审核不通过
				gjtSchoolRollTran.setUpdatedDt(now);;
				gjtSchoolRollTran.setAuditOperator(data.get("roleName").toString());
				gjtSchoolRollTran.setAuditOperatorRole(5);
				gjtSchoolRollTran.setTransactionStatus(new BigDecimal(data.get("auditState").toString()));
				//更新申请记录表中的状态
				GjtSchoolRollTran result=gjtSchoolRollTranDao.save(gjtSchoolRollTran);
				GjtSchoolRollTransAudit audit=auditList.get(auditList.size() - 1);
				audit.setAuditDt(now);
				audit.setAuditOperator(data.get("roleName").toString());
				audit.setAuditOperatorRole(new BigDecimal(5));
				audit.setAuditContent((String) data.get("auditContent"));
				audit.setAuditState(new BigDecimal((String)data.get("auditState")));
				audit.setUpdatedDt(now);
				//更新审核记录表中的信息
				GjtSchoolRollTransAudit result2=gjtSchoolRollTranAuditDao.save(audit);
				if(result!=null&&result2!=null){
					return true;
				}				
			}else{//审核通过				
				gjtSchoolRollTran.setUpdatedDt(now);;
				gjtSchoolRollTran.setAuditOperatorRole(5);
				gjtSchoolRollTran.setTransactionStatus(new BigDecimal(data.get("transactionStatus").toString()));
				gjtSchoolRollTran.setAuditOperator(data.get("roleName").toString());				
				gjtSchoolRollTranDao.save(gjtSchoolRollTran);
				GjtSchoolRollTransAudit audit=auditList.get(auditList.size() - 1);
				audit.setAuditDt(now);
				audit.setAuditContent((String) data.get("auditContent"));
				audit.setAuditState(new BigDecimal(data.get("transactionStatus").toString()));
				audit.setAuditOperator(data.get("roleName").toString());
				audit.setAuditOperatorRole(new BigDecimal(5));
				if(StringUtils.isNotEmpty(ObjectUtils.toString(data.get("approvalBook")))){
					audit.setApprovalBook((String)data.get("approvalBook"));
				}
				if(StringUtils.isNotEmpty(ObjectUtils.toString(data.get("approvalBookName")))){
					audit.setApprovalBookName((String)data.get("approvalBookName"));
				}
				if(StringUtils.isNotEmpty(ObjectUtils.toString(data.get("auditVoucher")))){
					audit.setAuditVoucher((String)data.get("auditVoucher"));
				}
				gjtSchoolRollTranAuditDao.save(audit);
				//更新学生表里的信息
				GjtStudentInfo gjtStudentInfo=gjtStudentInfoService.queryById(data.get("studentId").toString());
				Map<String,Object> transactionContent=JsonUtils.toObject(gjtSchoolRollTran.getTransactionContent().toString(), HashMap.class);
				if(gjtSchoolRollTran.getTransactionType()==SchoolRollTransEnum.转专业.getValue()){
					
				}else if(gjtSchoolRollTran.getTransactionType()==SchoolRollTransEnum.休学.getValue()){
					gjtStudentInfo.setXjzt("4");
					gjtStudentInfoService.saveEntity(gjtStudentInfo);
				}else if(gjtSchoolRollTran.getTransactionType()==SchoolRollTransEnum.复学.getValue()){
					
				}else if(gjtSchoolRollTran.getTransactionType()==SchoolRollTransEnum.退学.getValue()){
					
				}else if(gjtSchoolRollTran.getTransactionType()==SchoolRollTransEnum.转学.getValue()){
					
				}else{
					OpiStudent student=new OpiStudent();
					EEIMUpdateInfo info =new EEIMUpdateInfo();
					student.setSTUD_ID(gjtStudentInfo.getStudentId());
					info.setUSER_ID(gjtStudentInfo.getStudentId());
					final String appId = OrgUtil.getEEChatAppId(gjtStudentInfo.getGjtSchoolInfo().getGjtOrg().getCode());
					if(gjtSchoolRollTran.getTransactionPartStatus()==StudentMessageMoveEnum.性别民族变更.getValue()){
						gjtStudentInfo.setXbm((String) transactionContent.get("xbm"));
						gjtStudentInfo.setMzm((String) transactionContent.get("mzm"));
						gjtStudentInfo.setNation((String) transactionContent.get("nation"));
						student.setSEX((String)transactionContent.get("sex"));
						info.setUSER_SEX((String) transactionContent.get("xbm"));
					}else if(gjtSchoolRollTran.getTransactionPartStatus()==StudentMessageMoveEnum.入学文化程度更变.getValue()){
						gjtStudentInfo.setExedulevel((String) transactionContent.get("exedulevel"));
					}else if(gjtSchoolRollTran.getTransactionPartStatus()==StudentMessageMoveEnum.姓名变更.getValue()){
						gjtStudentInfo.setXm((String) transactionContent.get("xm"));
						student.setREALNAME((String) transactionContent.get("xm"));
						info.setUSER_NAME((String) transactionContent.get("xm"));
					}else{//身份证号码变更
						String sfzh=(String)transactionContent.get("sfzh");
						gjtStudentInfo.setSfzh(sfzh);
						info.setUSER_IDCARD(sfzh);
					}
					//把更新后的信息同步至学习平台
					OpiStudentData opi = new OpiStudentData(appId, student);
					Boolean flag=true;
					RSimpleData synchroCourse = pCourseServer.synchroStudent(opi);
					if(EmptyUtils.isNotEmpty(synchroCourse) && synchroCourse.getStatus()!=1){
						flag=false;
						log.info("更新后的信息同步至学习平台失败："+gjtSchoolRollTran.getTransactionPartStatus());;
					}
					//把更新后的信息同步至EE平台
					List<EEIMUpdateInfo> studentInfos = new ArrayList<EEIMUpdateInfo>();
					studentInfos.add(info);
					EEIMUpdateInfoReturnData resultData=eeIMService.updateDataD2(studentInfos);
					if(!Constants.BOOLEAN_1.equals(resultData.getSTATUS())){
						flag=false;
						log.info("更新后的信息同步至EE平台失败："+gjtSchoolRollTran.getTransactionPartStatus());;
					}
					if(flag){
						//推送至招生平台
						int status=syncMessageInfo(gjtStudentInfo);
						if(status==1){
							gjtStudentInfoService.saveEntity(gjtStudentInfo);
						}else{
							log.error("招生平台退学接口发生异常！" +gjtStudentInfo.getStudentId());
							flag=false;
						}						
					}else{
						return false;
					}					
				}				
				return true;
			}			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("学籍科审核失败=="+e.getMessage());
			return false;
		}
		return false;
	}
	
	public int syncMessageInfo(GjtStudentInfo gjtStudentInfo) {		
		try {
			Map params = new HashMap();
			Map<String,String> map = new HashMap<String,String>();
			long time = System.currentTimeMillis();
			params.put("student_id", gjtStudentInfo.getStudentId());//学员ID
			params.put("school_code", gjtStudentInfo.getGjtSchoolInfo().getGjtOrg().getCode());//院校编码
			params.put("sign", SignUtil.formatUrlMap(params,time));
			params.put("appid", SignUtil.APPID);
			params.put("time", String.valueOf(time));
			String result = HttpClientUtils.doHttpPost(PAY_NEWSERVER_DOMAIN + "/userapi/updateUserInfo.html", params, 3000, "UTF-8");
			if (StringUtils.isNotEmpty(result)) {
				JSONObject json = JSONObject.fromObject(result);
				int status = json.getInt("status");
				return status;				
			}
		} catch (Exception e) {
			log.error("招生平台退学接口发生异常！" + e.getMessage(), e);
		}
		return 0;
	}

	@Override
	public void initSchoolRollTranAudit(int messageType, String studentId, int tranType, GjtSchoolRollTran rollTran) {
		List <GjtSchoolRollTransAudit> list=this.queryGjtSchoolRollTranAudit(messageType,studentId,tranType);
		if(list!=null&&list.size()>0&&list.get(list.size() - 1).getAuditState().intValue() == 2){
			//更新申请表中的审核状态及审核人角色
			rollTran.setTransactionStatus(new BigDecimal(0));
			rollTran.setAuditOperatorRole(3);
			rollTran.setAuditOperator("");
			gjtSchoolRollTranDao.save(rollTran);
			//初始化新的审核记录
			Boolean result=updateschoolRollTransAudit(studentId,rollTran.getTransactionId());
			if(result){
				log.info("学员初始化审核记录成功：messageType:"+messageType+";transactionId:"+rollTran.getTransactionId());
			}
		}
	}
	public Boolean updateschoolRollTransAudit(String studentId,String transactionId) {
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
			// 下一个审核角色：学习中心管理员
			GjtSchoolRollTransAudit schoolRollTransAudit2=new GjtSchoolRollTransAudit();
			schoolRollTransAudit2.setId(UUIDUtils.random());
			schoolRollTransAudit2.setTransactionId(transactionId);
			schoolRollTransAudit2.setStudentId(studentId);
			schoolRollTransAudit2.setAuditState(new BigDecimal(0));//默认为待审核
			schoolRollTransAudit2.setAuditOperatorRole(new BigDecimal(3));//审核角色：学习中心管理员
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
	public void initAudit(GjtSchoolRollTran rollTran, String studentId) {
		List <GjtSchoolRollTransAudit> list=
				gjtSchoolRollTranAuditDao.findByTransactionIdAndStudentIdAndIsDeletedOrderByAuditDtAsc(rollTran.getTransactionId(), studentId, Constants.BOOLEAN_NO);
		if(list!=null&&list.size()>0&&list.get(list.size() - 1).getAuditState().intValue() == 2){
			//更新申请表中的审核状态及审核人角色
			rollTran.setTransactionStatus(new BigDecimal(0));
			rollTran.setAuditOperatorRole(3);
			rollTran.setAuditOperator("");
			gjtSchoolRollTranDao.save(rollTran);
			//初始化新的审核记录
			Boolean result=updateschoolRollTransAudit(studentId,rollTran.getTransactionId());
			if(result){
				log.info("学员初始化审核记录成功：transactionId:"+rollTran.getTransactionId());
			}
		}		
	}

	@Override
	public boolean updateOutStudyRransAudit(String studentId, int transType,int transStatus, String operatorRoleName,int operatorRole,String auditContent,GjtSchoolRollTransCost transCost) {
		log.info("参数查询：【studentId："+studentId+";transType:"+transType+"】");
		try {
			List<GjtSchoolRollTran> gjtSchoolRollTran=gjtSchoolRollTranDao.findByDropOutStudyRollTran(studentId, transType);
			GjtStudentInfo gjtStudentInfo=gjtStudentInfoService.queryById(studentId);
			if(gjtSchoolRollTran!=null&&gjtSchoolRollTran.size()>0){
				for(GjtSchoolRollTran rollTran:gjtSchoolRollTran){
					List<GjtSchoolRollTransAudit> auditList=gjtSchoolRollTranAuditDao.
							findByTransactionIdAndStudentIdAndIsDeletedOrderByAuditDtAsc(rollTran.getTransactionId(),studentId,Constants.BOOLEAN_NO);
					Date now=new Date();
					if(operatorRole==4){//招生办学习中心
						if(transStatus==4){//劝学失败,则往下插入审核记录数据							
							rollTran.setUpdatedDt(now);
							rollTran.setAuditOperatorRole(2);//学生服务部(班主任)
							rollTran.setTransactionStatus(new BigDecimal(3));//劝学中
							rollTran.setAuditOperator(operatorRoleName);
							gjtSchoolRollTranDao.save(rollTran);
							GjtSchoolRollTransAudit audit=auditList.get(auditList.size() - 1);
							audit.setUpdatedDt(now);
							audit.setAuditDt(now);
							audit.setAuditContent(auditContent);
							audit.setAuditState(new BigDecimal(transStatus));
							audit.setAuditOperator(operatorRoleName);
							gjtSchoolRollTranAuditDao.save(audit);
							GjtSchoolRollTransAudit gjtSchoolRollTransAudit=new GjtSchoolRollTransAudit();
							gjtSchoolRollTransAudit.setId(UUIDUtils.random());
							gjtSchoolRollTransAudit.setTransactionId(rollTran.getTransactionId());
							gjtSchoolRollTransAudit.setStudentId(studentId);
							gjtSchoolRollTransAudit.setAuditState(new BigDecimal(3));
							gjtSchoolRollTransAudit.setAuditOperatorRole(new BigDecimal(2));
							gjtSchoolRollTranAuditDao.save(gjtSchoolRollTransAudit);
							//短信通知下个审核人：教务服务部(学支管理员)
							GjtUserAccount account=gjtUserAccountDao.findByLoginAccount("gzxzadmin001");
							String sjh=account.getSjh();
							try {					
								SMSUtil.sendMessage(sjh,String.format("学习中心对%1$s学员劝学失败，请登录教务平台-学籍异动-退学栏目，进行劝学处理！",gjtStudentInfo.getXm()),"gk");
							} catch (Exception e) {				
							}
						}else{//劝学成功,则不往下插入审核记录数据
							rollTran.setUpdatedDt(now);
							rollTran.setAuditOperator(operatorRoleName);
							rollTran.setTransactionStatus(new BigDecimal(13));
							//更新申请记录表中的状态
							GjtSchoolRollTran result=gjtSchoolRollTranDao.save(rollTran);
							GjtSchoolRollTransAudit audit=auditList.get(auditList.size() - 1);
							audit.setAuditDt(now);
							audit.setAuditOperator(operatorRoleName);
							audit.setAuditContent(auditContent);
							audit.setAuditState(new BigDecimal(transStatus));
							audit.setUpdatedDt(now);
							//更新审核记录表中的信息
							gjtSchoolRollTranAuditDao.save(audit);
							GjtSchoolRollTransAudit gjtSchoolRollTransAudit=new GjtSchoolRollTransAudit();
							gjtSchoolRollTransAudit.setId(UUIDUtils.random());
							gjtSchoolRollTransAudit.setTransactionId(rollTran.getTransactionId());
							gjtSchoolRollTransAudit.setStudentId(studentId);
							gjtSchoolRollTransAudit.setAuditState(new BigDecimal(13));
							gjtSchoolRollTranAuditDao.save(gjtSchoolRollTransAudit);
							//发送短信给学员
							try {					
								SMSUtil.sendMessage(gjtStudentInfo.getSjh(),String.format("%1$s学员，经过与你沟通，你的退学申请已被撤销，请继续努力学习，早日完成学业！",gjtStudentInfo.getXm()),"gk");
							} catch (Exception e) {				
							}
						}
					}
					if(operatorRole==6){//财务服务部
						if(transStatus==7){//已核算
							rollTran.setUpdatedDt(now);
							rollTran.setAuditOperatorRole(1);//学员
							rollTran.setTransactionStatus(new BigDecimal(8));//待确认
							gjtSchoolRollTranDao.save(rollTran);
							GjtSchoolRollTransAudit audit=auditList.get(auditList.size() - 1);
							audit.setUpdatedDt(now);
							audit.setAuditDt(now);
							audit.setAuditContent(auditContent);
							audit.setAuditState(new BigDecimal(transStatus));
							audit.setAuditOperator(operatorRoleName);
							gjtSchoolRollTranAuditDao.save(audit);
							GjtSchoolRollTransAudit gjtSchoolRollTransAudit=new GjtSchoolRollTransAudit();
							gjtSchoolRollTransAudit.setId(UUIDUtils.random());
							gjtSchoolRollTransAudit.setTransactionId(rollTran.getTransactionId());
							gjtSchoolRollTransAudit.setStudentId(studentId);
							gjtSchoolRollTransAudit.setAuditState(new BigDecimal(8));
							gjtSchoolRollTransAudit.setAuditOperatorRole(new BigDecimal(1));
							gjtSchoolRollTranAuditDao.save(gjtSchoolRollTransAudit);
							//插入学员费用信息
							transCost.setId(UUIDUtils.random());
							transCost.setTransactionId(rollTran.getTransactionId());
							gjtSchoolRollTransCostService.saveEntity(transCost);
							//短信通知学员进行应扣、应退费用确认
							try {					
								SMSUtil.sendMessage(gjtStudentInfo.getSjh(),String.format("%1$s学员，请及时登录学习空间确认你的退学申请中应扣、应退的费用！",gjtStudentInfo.getXm()),"gk");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					if(operatorRole==7){//财务部
						if(transStatus==11){//已登记
							rollTran.setUpdatedDt(now);
							rollTran.setAuditOperatorRole(5);//学员
							rollTran.setTransactionStatus(new BigDecimal(8));//待确认
							gjtSchoolRollTranDao.save(rollTran);
							GjtSchoolRollTransAudit audit=auditList.get(auditList.size() - 1);
							audit.setUpdatedDt(now);
							audit.setAuditDt(now);
							audit.setAuditContent(auditContent);
							audit.setAuditState(new BigDecimal(transStatus));
							audit.setAuditOperator(operatorRoleName);
							gjtSchoolRollTranAuditDao.save(audit);
							GjtSchoolRollTransAudit gjtSchoolRollTransAudit=new GjtSchoolRollTransAudit();
							gjtSchoolRollTransAudit.setId(UUIDUtils.random());
							gjtSchoolRollTransAudit.setTransactionId(rollTran.getTransactionId());
							gjtSchoolRollTransAudit.setStudentId(studentId);
							gjtSchoolRollTransAudit.setAuditState(new BigDecimal(8));
							gjtSchoolRollTransAudit.setAuditOperatorRole(new BigDecimal(5));
							gjtSchoolRollTranAuditDao.save(gjtSchoolRollTransAudit);
							GjtSchoolRollTransCost cost=gjtSchoolRollTransCostService.findByTransactionId(rollTran.getTransactionId());
							cost.setBackPriceVoucher(transCost.getBackPriceVoucher());
							cost.setRealBackPrice(transCost.getRealBackPrice());
							gjtSchoolRollTransCostService.saveEntity(cost);
							//短信通知下个审核人：学籍科
							GjtUserAccount account=gjtUserAccountDao.findByLoginAccount("gzxjadmin001");
							try {					
								SMSUtil.sendMessage(account.getSjh(),String.format("财务已登记%1$s学员退学的退费信息，请登录教务平台-学籍异动-退学栏目，进行确认退学处理！",gjtStudentInfo.getXm()),"gk");
							} catch (Exception e) {				
							}
							
						}						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 招生平台更新退学申请状态
	 * @param gjtStudentInfo
	 * @param applyStatus
	 * @return
	 */
	public int syncOutStudyRransAudit(GjtStudentInfo gjtStudentInfo, String applyStatus) {
		try {
			Map params = new HashMap();
			Map<String,String> map = new HashMap<String,String>();
			long time = System.currentTimeMillis();
			params.put("order_sn", gjtStudentInfo.getGjtSignup().getOrderSn());//订单编号
			params.put("school_code", gjtStudentInfo.getGjtSchoolInfo().getGjtOrg().getCode());//院校编码
			params.put("apply_status", applyStatus);//申请状态
			params.put("sign", SignUtil.formatUrlMap(params,time));
			params.put("appid", SignUtil.APPID);
			params.put("time", String.valueOf(time));
			String result = HttpClientUtils.doHttpPost(PAY_NEWSERVER_DOMAIN + "/Refundapplyapi/updateStatus.html", params, 3000, "UTF-8");
			if (StringUtils.isNotEmpty(result)) {
				JSONObject json = JSONObject.fromObject(result);
				int status = json.getInt("status");
				return status;				
			}
		} catch (Exception e) {
			log.error("招生平台更新学籍信息接口发生异常！" + e.getMessage(), e);
		}
		return 0;
	}
	
	@Transactional
	@Override
	public boolean insertOutStudyTransAudit(Map<String, Object> data) {
		log.info("transactionId:"+data.get("transactionId").toString()+";studentId:"+data.get("studentId").toString());
		try {
			GjtSchoolRollTran gjtSchoolRollTran=gjtSchoolRollTranDao.findOne(data.get("transactionId").toString());
			List<GjtSchoolRollTransAudit> auditList=gjtSchoolRollTranAuditDao.
					findByTransactionIdAndStudentIdAndIsDeletedOrderByAuditDtAsc(data.get("transactionId").toString(),data.get("studentId").toString(),Constants.BOOLEAN_NO);
			GjtStudentInfo gjtStudentInfo=gjtStudentInfoService.queryById(data.get("studentId").toString());
			Date now=new Date();
			//审核人的角色: 1-学员 2-班主任(学生服务部/学支管理员) 3-学习中心管理员 4-招生办 5-学籍科 0-院长 6-财务服务部 7-公司财务部 
			//异动状态:默认0 0-待审核 1-通过 2-不通过 3-劝学中 4-劝学失败 5-劝学成功 6-待核算 7-已核算 8-待确认 9-已确认 10-待登记 11-已登记 12-退学成功 13-撤销退学
			if("2".equals(ObjectUtils.toString(data.get("roleCode")))){//教务服务部、学支管理员
				if("4".equals(data.get("auditState"))){//劝学不成功,则往下插入审核记录数据										
					OutStudyTransAuditFail(5,SchoolRollTransTypeEnum.劝学中.getValue(),"2",data,gjtSchoolRollTran,gjtStudentInfo,auditList);
				}
				if("5".equals(data.get("auditState"))){//劝学成功										
					OutStudyTransAuditSuccess(gjtSchoolRollTran,gjtStudentInfo,auditList,data,SchoolRollTransTypeEnum.撤销退学.getValue(),"8");
				}
			}
			if("5".equals(ObjectUtils.toString(data.get("roleCode")))){//学籍科						
				if("4".equals(data.get("auditState"))){//劝学不成功,则往下插入审核记录数据					
					OutStudyTransAuditFail(6,SchoolRollTransTypeEnum.待核算.getValue(),"3",data,gjtSchoolRollTran,gjtStudentInfo,auditList);
					
				}
				if("5".equals(data.get("auditState"))){//劝学成功										
					OutStudyTransAuditSuccess(gjtSchoolRollTran,gjtStudentInfo,auditList,data,SchoolRollTransTypeEnum.撤销退学.getValue(),"8");
				}
				if("9".equals(data.get("auditState"))){//学籍科确认退学操作
					//更新申请记录表中的状态
					gjtSchoolRollTran.setUpdatedDt(now);
					gjtSchoolRollTran.setTransactionStatus(new BigDecimal(12));
					String content=gjtSchoolRollTran.getTransactionContent();
					JSONObject jsonObject = JSONObject.fromObject(content);
					jsonObject.put("dropSchoolPhoto", data.get("dropSchoolPhoto"));
					gjtSchoolRollTran.setTransactionContent(jsonObject.toString());
					GjtSchoolRollTran result=gjtSchoolRollTranDao.save(gjtSchoolRollTran);
					//更新审核记录表中的信息
					GjtSchoolRollTransAudit audit=auditList.get(auditList.size() - 1);
					audit.setAuditDt(now);
					audit.setAuditOperator(ObjectUtils.toString(data.get("roleName")));
					audit.setAuditContent((String) data.get("auditContent"));
					audit.setAuditState(new BigDecimal((String)data.get("auditState")));
					audit.setAuditVoucher(ObjectUtils.toString(data.get("auditVoucher")));
					audit.setUpdatedDt(now);				
					gjtSchoolRollTranAuditDao.save(audit);
					//插入一条新的数据
					GjtSchoolRollTransAudit gjtSchoolRollTransAudit=new GjtSchoolRollTransAudit();
					gjtSchoolRollTransAudit.setId(UUIDUtils.random());
					gjtSchoolRollTransAudit.setTransactionId(gjtSchoolRollTran.getTransactionId());
					gjtSchoolRollTransAudit.setStudentId(ObjectUtils.toString(data.get("studentId")));
					gjtSchoolRollTransAudit.setAuditState(new BigDecimal(12));
					gjtSchoolRollTransAudit.setAuditOperatorRole(new BigDecimal(1));//学员
					gjtSchoolRollTranAuditDao.save(gjtSchoolRollTransAudit);					
					//同步至招生平台
					int status=syncOutStudyRransAudit(gjtStudentInfo,"7");//已退学
					if(status==1){						
						//删除学员的账号和更改学籍状态
						signupDataAddService.revokedSignup(gjtStudentInfo.getStudentId(), true);
						//短信通知学员已退学完成
						try {					
							SMSUtil.sendMessage(gjtStudentInfo.getSjh(),String.format("%1$s学员，你的退学申请已完成，你已正式退学，你的学习帐号已注销，不能登录平台学习！",gjtStudentInfo.getXm()),"gk");
						} catch (Exception e) {				
						}
						
					}
				}
			}
			if("0".equals(ObjectUtils.toString(data.get("roleCode")))){//院长								
				OutStudyTransAuditFail(7,SchoolRollTransTypeEnum.待登记.getValue(),"6",data,gjtSchoolRollTran,gjtStudentInfo,auditList);				
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("学籍科劝学或确认学员退学发生异常！" + e.getMessage(), e);
			return false;
		}
		return true;
	}
	
	/**
	 * 劝学成功
	 * @param gjtSchoolRollTran
	 * @param gjtStudentInfo
	 * @param auditList
	 * @param data 
	 * @param transactionStatus 
	 * @param string
	 */
	private void OutStudyTransAuditSuccess(GjtSchoolRollTran gjtSchoolRollTran, GjtStudentInfo gjtStudentInfo,
			List<GjtSchoolRollTransAudit> auditList, Map<String, Object> data, int transactionStatus, String syncStatus) {
		try {
			Date now=new Date();
			gjtSchoolRollTran.setUpdatedDt(now);
			gjtSchoolRollTran.setAuditOperator(data.get("roleName").toString());
			gjtSchoolRollTran.setTransactionStatus(new BigDecimal(transactionStatus));
			gjtSchoolRollTran.setIsApplyFor("0");
			//更新申请记录表中的状态
			GjtSchoolRollTran result=gjtSchoolRollTranDao.save(gjtSchoolRollTran);
			GjtSchoolRollTransAudit audit=auditList.get(auditList.size() - 1);
			audit.setAuditDt(now);
			audit.setAuditOperator(ObjectUtils.toString(data.get("roleName")));
			audit.setAuditContent((String) data.get("auditContent"));
			audit.setAuditState(new BigDecimal((String)data.get("auditState")));
			audit.setUpdatedDt(now);
			//更新审核记录表中的信息
			gjtSchoolRollTranAuditDao.save(audit);
			//插入一条新的数据
			GjtSchoolRollTransAudit gjtSchoolRollTransAudit=new GjtSchoolRollTransAudit();
			gjtSchoolRollTransAudit.setId(UUIDUtils.random());
			gjtSchoolRollTransAudit.setTransactionId(gjtSchoolRollTran.getTransactionId());
			gjtSchoolRollTransAudit.setStudentId(ObjectUtils.toString(data.get("studentId")));
			gjtSchoolRollTransAudit.setAuditState(new BigDecimal(transactionStatus));
			gjtSchoolRollTranAuditDao.save(gjtSchoolRollTransAudit);
			//发送短信给学员
			try {					
				SMSUtil.sendMessage(gjtStudentInfo.getSjh(),String.format("%1$s学员，经过与你沟通，你的退学申请已被撤销，请继续努力学习，早日完成学业！",gjtStudentInfo.getXm()),"gk");
			} catch (Exception e) {				
			}
			//同步至招生平台
			int status=syncOutStudyRransAudit(gjtStudentInfo,syncStatus);
			if(status!=1){
				log.error("招生平台接口发生异常！同步流程为：" + syncStatus);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("劝学成功或确认学员退学发生异常！" + e.getMessage(), e);
		}		
	}
	/**
	 * 劝学失败
	 * @param OperatorRole
	 * @param transactionStatus
	 * @param syncStatus
	 * @param data
	 * @param rollTran
	 * @param gjtStudentInfo
	 * @param auditList
	 */
	private void OutStudyTransAuditFail(int OperatorRole, int transactionStatus, String syncStatus,Map<String, Object> data,
			GjtSchoolRollTran rollTran, GjtStudentInfo gjtStudentInfo, List<GjtSchoolRollTransAudit> auditList) {
		try {
			Date now=new Date();
			rollTran.setUpdatedDt(now);
			rollTran.setAuditOperatorRole(OperatorRole);
			rollTran.setTransactionStatus(new BigDecimal(transactionStatus));
			gjtSchoolRollTranDao.save(rollTran);//更新异动申请表中的数据
			GjtSchoolRollTransAudit audit=auditList.get(auditList.size() - 1);
			audit.setAuditDt(now);
			audit.setAuditOperator(data.get("roleName").toString());
			audit.setAuditOperatorRole(new BigDecimal(data.get("roleCode").toString()));
			audit.setAuditContent((String) data.get("auditContent"));
			audit.setAuditState(new BigDecimal((String)data.get("auditState")));
			audit.setUpdatedDt(now);
			gjtSchoolRollTranAuditDao.save(audit);//更新异动审核表中的数据
			GjtSchoolRollTransAudit gjtSchoolRollTransAudit=new GjtSchoolRollTransAudit();
			gjtSchoolRollTransAudit.setId(UUIDUtils.random());
			gjtSchoolRollTransAudit.setTransactionId(rollTran.getTransactionId());
			gjtSchoolRollTransAudit.setStudentId(ObjectUtils.toString(data.get("studentId")));
			gjtSchoolRollTransAudit.setAuditState(new BigDecimal(transactionStatus));
			gjtSchoolRollTransAudit.setAuditOperatorRole(new BigDecimal(OperatorRole));
			gjtSchoolRollTranAuditDao.save(gjtSchoolRollTransAudit);			
			if(OperatorRole==5){
				//发送短信通知下个审核人：学籍科
				GjtUserAccount account=gjtUserAccountDao.findByLoginAccount("gzxjadmin001");
				try {					
					SMSUtil.sendMessage(account.getSjh(),String.format("学支管理员对%1$s学员劝学失败，请登录教务平台-学籍异动-退学栏目，进行劝学处理！",gjtStudentInfo.getXm()),"gk");
				} catch (Exception e) {				
				}
			}
			//同步至招生平台
			int status=syncOutStudyRransAudit(gjtStudentInfo,syncStatus);
			if(status!=1){
				log.error("招生平台接口发生异常！同步流程为：" + syncStatus);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("劝学失败或确认学员退学发生异常！" + e.getMessage(), e);
		}		
	}

	@Override
	public List<GjtSchoolRollTransAudit> queryDropOutStudyRransAuditList(String transactionId, String studentId) {	
		return gjtSchoolRollTranAuditDao.findByTransactionIdAndStudentIdOrderByAuditDtAsc(transactionId, studentId);
	}
}
