/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.signup;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.Objects;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.ValidateUtil;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.dao.organization.GjtClassInfoDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.signup.GjtRollPlanDao;
import com.gzedu.xlims.dao.signup.GjtSignupDao;
import com.gzedu.xlims.dao.usermanage.GjtClassStudentDao;
import com.gzedu.xlims.dao.usermanage.GjtEmployeeInfoDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.daoImpl.EmployeeInfoDaoImpl;
import com.gzedu.xlims.daoImpl.StudentInfoDaoImpl;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtClassStudent;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtRollPlan;
import com.gzedu.xlims.pojo.GjtSignup;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtSyncLog;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.opi.PoiDelStudentData;
import com.gzedu.xlims.pojo.opi.RSimpleData;
import com.gzedu.xlims.service.GjtSyncLogService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.pcourse.PCourseServer;
import com.gzedu.xlims.service.signup.SignupDataAddService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.third.eechat.data.EEIMGroup;
import com.gzedu.xlims.third.eechat.data.EEIMUserNew;
import com.gzedu.xlims.third.eechat.service.EEChatServiceImpl;

/**
 * 学籍资料同步业务
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月21日
 * @version 3.0
 * @since JDK1.7
 */

@Service
public class SignupDataAddServiceImpl implements SignupDataAddService {

	private static final Log log = LogFactory.getLog(SignupDataAddServiceImpl.class);

	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;

	@Autowired
	private StudentInfoDaoImpl studentInfoDao;

	@Autowired
	private GjtSignupDao gjtSignupDao;

	@Autowired
	private GjtClassInfoDao gjtClassInfoDao;

	@Autowired
	private GjtRollPlanDao gjtRollPlanDao;

	@Autowired
	public GjtOrgDao gjtOrgDao;

	@Autowired
	private GjtClassStudentDao gjtClassStudentDao;

	@Autowired
	private EmployeeInfoDaoImpl employeeInfoDao;

	@Autowired
	private GjtClassInfoService gjtClassInfoService;

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtEmployeeInfoDao gjtEmployeeInfoDao;

	@Autowired
	private GjtSyncLogService gjtSyncLogService;

	@Autowired
	private EEChatServiceImpl eeChatService;
	
	@Autowired
	private PCourseServer pCourseServer;

	@Value("#{configProperties['yunyingCenterServer']}")
	private String YUNYING_CENTER_SERVER;

	@Override
	public GjtStudentInfo queryExistsSignupDataByPycc(String xxId, String sfzh, String pycc) {
		Criteria<GjtStudentInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.ne("xjzt", "5", true));
		spec.add(Restrictions.ne("xjzt", "8", true));
		spec.add(Restrictions.eq("xxId", xxId, true));
		spec.add(Restrictions.eq("sfzh", sfzh, true));
		spec.add(Restrictions.eq("pycc", pycc, true));
		spec.add(Restrictions.ne("gjtSignup.charge", "2", true)); // 收费状态不能为待缴费 0-已全额缴费，1-已部分缴费，2-待缴费，3-已欠费
		List<GjtStudentInfo> studentInfos = gjtStudentInfoDao.findAll(spec, new Sort(Sort.Direction.DESC, "createdDt"));
		return studentInfos != null && studentInfos.size() > 0 ? studentInfos.get(0) : null;
	}

	@Override
	public boolean updateSignupDataByAwaitPayOrder(String xxId, String sfzh, String pycc, String studentId) {
		Criteria<GjtStudentInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.ne("xjzt", "5", true));
		spec.add(Restrictions.ne("xjzt", "8", true));
		spec.add(Restrictions.eq("sfzh", sfzh, true));
		spec.add(Restrictions.isNull("gjtUserAccount.id"));
		// 删除的与院校和培养层次无关
//		spec.add(Restrictions.eq("xxId", xxId, true));
//		spec.add(Restrictions.eq("pycc", pycc, true));
		spec.add(Restrictions.eq("gjtSignup.charge", "2", true)); // 收费状态为待缴费 0-已全额缴费，1-已部分缴费，2-待缴费，3-已欠费
		spec.add(Restrictions.ne("studentId", studentId, true)); // 去除已缴费的学员ID
		List<GjtStudentInfo> studentInfos = gjtStudentInfoDao.findAll(spec, new Sort(Sort.Direction.DESC, "createdDt"));
		if(studentInfos != null && studentInfos.size() > 0) {
			for (GjtStudentInfo info : studentInfos) {
				info.setIsDeleted(Constants.BOOLEAN_YES);
				info.setUpdatedBy("接口调用-报读缴费状态确认-删除预存学籍资料");
				info.setUpdatedDt(new Date());
				gjtStudentInfoDao.save(info);
			}
		}
		return true;
	}

	@Override
	public GjtClassInfo queryExistsClassName(String xxId, String className) {
		Criteria<GjtClassInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		List<String> orgList = gjtOrgDao.queryChildsByParentId(xxId);
		spec.add(Restrictions.or(Restrictions.eq("gjtSchoolInfo.id", xxId, true), Restrictions.in("xxzxId", orgList, true)));
		spec.add(Restrictions.eq("bjmc", className, true));
		List<GjtClassInfo> classInfos = gjtClassInfoDao.findAll(spec, new Sort(Sort.Direction.DESC, "createdDt"));
		return classInfos != null && classInfos.size() > 0 ? classInfos.get(0) : null;
	}

	/**
	 * 录入学籍
	 * @param entity
	 * @return successful-是否成功 type-错误类型 message-错误信息 obj-结果
	 */
	@Override
	public Map<String, Object> addSignupData(GjtStudentInfo entity) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("successful", true);
		if(entity.getStudentId() == null) {
			entity.setStudentId(UUIDUtils.random());
		}
		// 不存在则录入
		if(queryExistsSignupDataByPycc(entity.getGjtSchoolInfo().getId(), entity.getSfzh(), entity.getPycc()) == null
				&& gjtStudentInfoDao.findOne(entity.getStudentId()) == null) {
			Date now = new Date();
			// 则通过身份证获取性别  18位身份证号码：第17位代表性别，奇数为男，偶数为女。
			if(entity.getSfzh().length() == 18) {
				if(NumberUtils.toInt(entity.getXbm()) == 0) {
					String sexNum = entity.getSfzh().substring(16, 17);
					int mod = NumberUtils.toInt(sexNum) % 2;
					String sex = "0";
					if (mod == 1) {
						sex = "1";
					} else {
						sex = "2";
					}
					entity.setXbm(sex);
				}
				// 则通过身份证获取出生日期
				if(StringUtils.isBlank(entity.getCsrq())) {
					entity.setCsrq(entity.getSfzh().substring(6, 14));
				}
			}
			entity.getGjtSignup().setSignupId(UUIDUtils.random());
			entity.getGjtSignup().setSignupSpecialtyId(entity.getMajor());
			entity.getGjtSignup().setPycc(entity.getPycc());
			entity.getGjtSignup().setGjtStudentInfo(entity);
			entity.getGjtSignup().setName(entity.getXm());
			entity.getGjtSignup().setMobile(entity.getSjh());
			entity.getGjtSignup().setIdcard(entity.getSfzh());
			entity.getGjtSignup().setXxId(entity.getGjtSchoolInfo().getId());
			entity.getGjtSignup().setXxzxId(entity.getGjtStudyCenter().getId());
			entity.getGjtSignup().setPortty(Constants.BOOLEAN_YES); // 是否接口调用
			// 根据学籍计划判断学员类型
			if("11".equals(entity.getUserType())) {
				List<GjtRollPlan> rollPlanList = gjtRollPlanDao.findTermRollPlanList(entity.getGjtSchoolInfo().getId(), entity.getGjtGrade().getGradeId());
				if(rollPlanList != null && rollPlanList.size() > 0) {
					GjtRollPlan rollPlan = rollPlanList.get(0);
					if(rollPlan.getOfficialBeginDt() != null && rollPlan.getOfficialEndDt() != null
							&& now.after(rollPlan.getOfficialBeginDt())
//						&& now.before(DateUtil.parseDate(DateUtil.toString(rollPlan.getOfficialEndDt(), "yyyy-MM-dd") + " 23:59:59"))
							&& now.before(rollPlan.getOfficialEndDt())
							) {
						// 11
						entity.setUserType("11");
					} else if(rollPlan.getOfficialBeginDt2() != null && rollPlan.getOfficialEndDt2() != null
							&& now.after(rollPlan.getOfficialBeginDt2())
							&& now.before(rollPlan.getOfficialEndDt2())
							) {
						// 11
						entity.setUserType("11");
					} else if(rollPlan.getFollowBeginDt() != null && rollPlan.getFollowEndDt() != null
							&& now.after(rollPlan.getFollowBeginDt())
							&& now.before(rollPlan.getFollowEndDt())
							) {
						// 12 正式跟读生
						entity.setUserType("12");
					} else if(rollPlan.getFollowEndDt() != null
							&& now.after(rollPlan.getFollowEndDt())
							) {
						// 13 非正式跟读生
						entity.setUserType("13");
					}
				}
			}
			// “体验学员”，“测试学员”，非正式跟读生，课程预读生，本科预读生 默认完善资料，且审核通过
			if("21".equals(entity.getUserType()) || "31".equals(entity.getUserType())
					|| "13".equals(entity.getUserType()) || "41".equals(entity.getUserType()) || "61".equals(entity.getUserType())) {
				entity.setPerfectStatus(1);
				entity.getGjtSignup().setAuditState("1");
			} else {
				entity.setPerfectStatus(0);
				entity.getGjtSignup().setAuditState("3");
			}

			// 设置默认属性
			entity.setClassType("C"); // 班级类型：A：精英班；B：进取班；C: 默认班
			entity.setXjzt("3");
			entity.setStatus("0");
			entity.setCreatedDt(now);
			entity.setIsDeleted(Constants.BOOLEAN_NO);
			entity.setVersion(new BigDecimal(1));
			entity.setCreatedBy("新接口调用生成");
			GjtStudentInfo info = gjtStudentInfoDao.save(entity); // 添加
			// 添加报读信息
			entity.getGjtSignup().setGjtStudentInfo(info);
			GjtSignup signup = gjtSignupDao.save(entity.getGjtSignup());
			info.setGjtSignup(signup);
			gjtStudentInfoDao.save(info);

			result.put("obj", info.getStudentId());
		}
		return result;
	}

	@Override
	public Map<String, Object> addCreateUserAccountAndDivideIntoClasses(String studentId) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("successful", true);
		GjtStudentInfo entity = gjtStudentInfoDao.findOne(studentId);
		// 收费状态 0-已全额缴费，1-已部分缴费，2-待缴费，3-已欠费
		if(entity != null && ("0".equals(entity.getGjtSignup().getCharge()) || "1".equals(entity.getGjtSignup().getCharge()))) {
			GjtClassInfo teachClassInfo = null;
			try {
				// 没有指定教学班需要自动分配教学班
				int bh = 1; // 班号 暂时为1
				GjtOrg gjtStudyCenter=gjtOrgDao.queryStudyCenterInfo(entity.getXxzxId(),"3");
				teachClassInfo = gjtClassInfoService.createTeachClassInfo(entity.getGjtGrade().getGradeId(), entity.getMajor(),bh,gjtStudyCenter.getId(), entity.getGjtSchoolInfo().getId(), "新接口调用生成");
				entity.setUserclass(teachClassInfo.getBjmc());
			} catch (Exception e) {
				result.put("successful", false);
				result.put("type", 2);
				result.put("message", "内部异常-分配教学班失败");
				e.printStackTrace();
				return result;
			}

			// 添加账号信息
			GjtUserAccount userAccount = gjtUserAccountService.saveEntity(entity.getXm(), entity.getXh(), entity.getGjtSchoolInfo().getId(), 1);
			userAccount.setEmail(entity.getDzxx());
			userAccount.setSjh(entity.getSjh());
			gjtUserAccountService.update(userAccount);
			// 添加学员信息
			entity.setGjtUserAccount(userAccount);
			GjtStudentInfo info = gjtStudentInfoDao.save(entity); // 添加

			// 学员加入该班级
			GjtClassStudent item = new GjtClassStudent();
			item.setClassStudentId(UUIDUtils.random());
			item.setGjtClassInfo(teachClassInfo);
			item.setGjtStudentInfo(info);
			item.setGjtSchoolInfo(info.getGjtSchoolInfo());
			item.setGjtGrade(info.getGjtGrade());
			item.setCreatedBy("新接口调用生成");
			gjtClassStudentDao.save(item);
		} else {
			// 代缴费的也返回true，因为这个只是检测是否缴费然后生成账号等。
		}
		return result;
	}

	/**
	 * 注销学籍，或删除未缴费学员
	 * @param studentId
	 * @param force 是否强制注销
	 * @return successful-是否成功 type-错误类型 message-错误信息 obj-结果
	 */
	@Transactional
	@Override
	public Map<String, Object> revokedSignup(String studentId, boolean force) {
		GjtStudentInfo info = gjtStudentInfoDao.findOne(studentId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(StringUtils.equals(info.getXjzt(), "5")) {
			result.put("successful", false);
			result.put("message", "学籍资料已被注销，不能重复注销！");
			return result;
		}
		// 查看订单是否缴费，如果是未缴费则直接删除学员信息
		if("2".equals(info.getGjtSignup().getCharge())) {
			// 学籍状态更改为退学，再删除学员信息
			info.setXjzt("5");
			info.setIsDeleted(Constants.BOOLEAN_YES);
			Date now = new Date();
			info.setUpdatedBy(DateFormatUtils.ISO_DATE_FORMAT.format(now) + " 新接口调用-注销学籍");
			info.setUpdatedDt(now);
			gjtStudentInfoDao.save(info);
		} else {
			if(!force) {
				// 审核通过的学员不能注销
				// 根据需求2403 资料审核未通过前，学员退费，标识已退费无需审核状态，如学籍资料审核已通过，学员退费，需要走退学流程
				if(StringUtils.equals(info.getGjtSignup().getAuditState(), Constants.BOOLEAN_1)) {
					result.put("successful", false);
					result.put("message", "学籍资料已审核通过的学员，需要走退学流程！");
					return result;
				}
			}
			
			// 1.先备份账号信息数据，再物理删除（目的是为了防止删除再重新同步时账号唯一约束冲突）
			int num = studentInfoDao.copyAccountHistory(info.getGjtUserAccount().getId());
			gjtUserAccountService.delete(info.getGjtUserAccount().getId());
			info.setGjtUserAccount(null);
			
			// 2.学籍状态更改为退学
			info.setXjzt("5");
			Date now = new Date();
			info.setUpdatedBy(DateFormatUtils.ISO_DATE_FORMAT.format(now) + " 新接口调用-注销学籍");
			info.setUpdatedDt(now);
			gjtStudentInfoDao.save(info);
	
			// 3.逻辑删除学员所在教学班级的信息
			studentInfoDao.removeClassStudent(studentId);
			
			try {
				// 4.删除学习平台的数据
				PoiDelStudentData term = new PoiDelStudentData(studentId, "注销学籍");
				RSimpleData synchroCourse = pCourseServer.synchroStudDel(term);
				if(synchroCourse.getStatus() != 1) {
					log.error("function revokedSignup 删除学习平台的数据错误 ======== studentId:" + studentId + " " + synchroCourse.getStatus() + " " + synchroCourse.getMsg());
				}
			} catch (Exception e) {
				log.error("function revokedSignup 删除学习平台的数据错误 ======== studentId:" + studentId + " " + e.getMessage());
			}
		}
		
		result.put("successful", true);
		return result;
	}

	@Override
	public boolean signupDataToSms(final String studentId) {
		GjtStudentInfo info = gjtStudentInfoDao.findOne(studentId);
		// 收费状态 0-已全额缴费，1-已部分缴费，2-待缴费，3-已欠费
		if(info != null && ("0".equals(info.getGjtSignup().getCharge()) || "1".equals(info.getGjtSignup().getCharge()))) {
			final String sjh = info.getSjh();
			final String xm = info.getXm();
			final String xh = info.getXh();
			final String password = Constants.STUDENT_ACCOUNT_PWD_DEFAULT;
			final String xxCode = info.getGjtSchoolInfo().getGjtOrg().getCode();
			final String xxzxTel = info.getGjtStudyCenter().getOfficeTel();

			// 报读学籍已缴费时发短信通知学员、班主任已经分班，请老师确认
			String content = null;
			int smsResult = -1;
			if (StringUtils.equals(xxCode, OrgUtil.SZLG)) { // 深圳龙岗
				content = String.format("%s,%s,%s,%s", xm, "http://lgzz.oucnet.cn ", xh, password);
				smsResult = SMSUtil.sendMessageSignup(sjh, content, "lgzz");
			} else if (StringUtils.equals(xxCode, OrgUtil.GK_GZ)) { // 国开广州
				content = String.format("%s,%s,%s,%s,%s%s", xm, xh, password, "http://www.oucgz.cn ", StringUtils.isNotBlank(xxzxTel) ? "学习中心：" + xxzxTel + "，或" : "", "统一教育服务热线：020-969300，祝学习愉快。");
				smsResult = SMSUtil.sendMessageSignup(sjh, content, "gk");

				// 发短信通知班主任学员已经分班，请老师确认
				GjtClassInfo teachClassInfo = gjtClassInfoService.queryTeachClassInfo(studentId);
				if (teachClassInfo != null && teachClassInfo.getGjtBzr() != null && StringUtils.isNotBlank(teachClassInfo.getGjtBzr().getXm()) && StringUtils.isNotBlank(teachClassInfo.getGjtBzr().getSjh())) {
					SMSUtil.sendMessageHeardTeacher(teachClassInfo.getGjtBzr().getSjh(), String.format("%s,%s", teachClassInfo.getGjtBzr().getXm(), xm));
				}
			} else if (StringUtils.equals(xxCode, OrgUtil.GK_SY)) { // 国家开放大学实验学院
				content = String.format("%s,%s,%s,%s,%s%s", xm, xh, password, "http://shiyan.oucnet.cn ", StringUtils.isNotBlank(xxzxTel) ? "学习中心：" + xxzxTel + "，或" : "", "统一教育服务热线：020-969300，祝学习愉快。");
				smsResult = SMSUtil.sendMessageSignup(sjh, content, "gk");
			} else if (StringUtils.equals(xxCode, OrgUtil.GK_HZ)) { // 国开杭州
				content = String.format("%s,%s,%s,%s,%s%s", xm, xh, password, "http://hzsy.oucnet.cn ", StringUtils.isNotBlank(xxzxTel) ? "学习中心：" + xxzxTel + "，或" : "", "统一教育服务热线：020-969300，祝学习愉快。");
				smsResult = SMSUtil.sendMessageSignup(sjh, content, "gk");
			}
			// 记录短信日志发送记录
			gjtSyncLogService.insert(new GjtSyncLog(xm, xh, null, content, smsResult + ""));
		}
		return true;
	}

	/**
	 * 账号同步至运营平台
	 * @param studentId
	 * @return successful-是否成功 type-错误类型 message-错误信息 obj-结果
	 */
	@Override
	public Map<String, Object> syncYunYingCenter(String studentId) {
		Map<String, Object> resultSync = new HashMap<String, Object>();
		resultSync.put("successful", false);
		GjtStudentInfo info = gjtStudentInfoDao.findOne(studentId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", info.getXm());
		params.put("id_card", info.getSfzh());
		params.put("phone", com.gzedu.xlims.common.Objects.toString(info.getSjh(), ""));
		params.put("account", info.getGjtUserAccount().getLoginAccount());
		params.put("student_id", info.getStudentId());
		params.put("atid", info.getAtid());
		String mail = com.gzedu.xlims.common.Objects.toString(info.getDzxx(), "");
		params.put("email", ValidateUtil.isEmail(mail) ? mail : "");
		params.put("birthday", com.gzedu.xlims.common.Objects.toString(info.getCsrq(), ""));
		params.put("sex", com.gzedu.xlims.common.Objects.toString(info.getXbm(), ""));
		params.put("address", com.gzedu.xlims.common.Objects.toString(info.getTxdz(), ""));
		params.put("product_id", com.gzedu.xlims.common.Objects.toString(info.getGradeSpecialtyId(), ""));
		params.put("order_no", com.gzedu.xlims.common.Objects.toString(info.getGjtSignup().getOrderSn(), ""));
		params.put("source", "education"); // 平台来源，enrollment 为招生平台，education 教学平台
		params.put("sync_type", "1"); // 学员类型  1、学历  2、职业
		
		try {
//			String result = HttpClientUtils.doHttpPost(YUNYING_CENTER_SERVER + "/receive/studentNew", params, 6000, Constants.CHARSET);
			// 20180417暂用旧的
			String result = HttpClientUtils.doHttpPost(YUNYING_CENTER_SERVER + "/receive/student", params, 6000, Constants.CHARSET);
			Map<String, Object> resultMap = GsonUtils.toBean(result, Map.class);
			if(resultMap != null) {
				int code = (int) NumberUtils.toDouble(Objects.toString(resultMap.get("code"), ""));
				if (code == 200) {
					// 门户接口返回数据结构比较奇怪，data中有错误message也是同步失败的
					Map<String, Object> data = (Map<String, Object>) resultMap.get("data");
					String message = data != null ? (String) data.get("message") : null;
					if(StringUtils.isBlank(message)) {
						info.getGjtUserAccount().setIsSync(true);
						gjtUserAccountService.update(info.getGjtUserAccount());
						info.setYunyingSync(Constants.BOOLEAN_YES);
						gjtStudentInfoDao.save(info);
						// 成功也记录下
						gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0002, GsonUtils.toJson(params), "success" + result));
						resultSync.put("successful", true);
						return resultSync;
					} else {
						// 记录同步失败日志
						gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0002, GsonUtils.toJson(params), message));
						resultSync.put("type", 3);
						resultSync.put("message", "请求同步结果-" + message);
					}
				} else {
					// 记录同步失败日志
					gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0002, GsonUtils.toJson(params), result));
					resultSync.put("type", 3);
					resultSync.put("message", "请求同步结果-" + result);
				}
			} else {
				// 记录同步失败日志
				gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0002, GsonUtils.toJson(params), result));
				resultSync.put("type", 3);
				resultSync.put("message", "请求同步结果-" + result);
			}
		} catch (Exception e) {
			String objJson = GsonUtils.toJson(params);
			log.error("syncYunYingCenter fail ======== params:" + objJson);
			// 记录同步失败日志
			gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0002, objJson, e.toString().length() > 500 ? e.toString().substring(0, 500) : e.toString()));
			e.printStackTrace();
			resultSync.put("type", 2);
			resultSync.put("message", "内部异常-" + e.toString());
		}
		return resultSync;
	}

	/**
	 * 生成EE号
	 * @param studentId
	 * @return
     */
	@Override
	public boolean createEENo(String studentId) {
		EEIMUserNew eeimUser = null;
		GjtStudentInfo info = gjtStudentInfoDao.findOne(studentId);
		try {
			eeimUser = new EEIMUserNew(info.getStudentId(), info.getXm(), "3", info.getSfzh(), "");
			final String appId = OrgUtil.getEEChatAppId(info.getGjtSchoolInfo().getGjtOrg().getCode());
			String eeno = eeChatService.syncSingleStudents(appId, eeimUser);

			if(StringUtils.isNotEmpty(eeno)) {
				info.getGjtUserAccount().setEeno(eeno);
				gjtUserAccountService.update(info.getGjtUserAccount());
				info.setEeno(eeno);
				info.setEesync(Constants.BOOLEAN_YES);
				gjtStudentInfoDao.save(info);
				return true;
			}
		} catch (Exception e) {
			String objJson = GsonUtils.toJson(eeimUser);
			log.error("createEENo fail ======== params:" + objJson);
			// 记录EE失败日志
			gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_EE001, objJson, e.toString().length() > 500 ? e.toString().substring(0, 500) : e.toString()));
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 生成班主任EE号
	 * @param employeeId
	 * @return
	 */
	@Override
	public boolean createHeadTeacherEENo(String employeeId) {
		EEIMUserNew eeimUser = null;
		GjtEmployeeInfo info = gjtEmployeeInfoDao.findOne(employeeId);
		try {
			eeimUser = new EEIMUserNew(info.getEmployeeId(), info.getXm(), "2", info.getSfzh(), "");
			final String appId = OrgUtil.getEEChatAppId(info.getGjtSchoolInfo().getGjtOrg().getCode());
			String eeno = eeChatService.syncSingleStudents(appId, eeimUser);

			if(StringUtils.isNotEmpty(eeno)) {
				info.getGjtUserAccount().setEeno(eeno);
				gjtUserAccountService.update(info.getGjtUserAccount());
				info.setEeno(eeno);
				gjtEmployeeInfoDao.save(info);
				return true;
			}
		} catch (Exception e) {
			String objJson = GsonUtils.toJson(eeimUser);
			log.error("createHeadTeacherEENo fail ======== params:" + objJson);
			// 记录EE失败日志
			gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getSfzh(), Constants.RSBIZ_CODE_EE001, objJson, e.toString().length() > 500 ? e.toString().substring(0, 500) : e.toString()));
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 创建EE群（群以教学班ID为唯一标识），并加群成员，如果群不存在就创建再加群成员，如果群存在直接加群成员
	 * @param studentId
     * @return
     */
	public boolean createGroupNoAndAddSingleStudent(String studentId) {
		EEIMGroup eeimGroup = null;
		GjtClassInfo teachClassInfo = gjtClassInfoDao.findTeachClassByStudentId(studentId); // 获取教学班信息
		GjtStudentInfo info = gjtStudentInfoDao.findOne(studentId);
		try {
			String teacherEENo = teachClassInfo.getGjtBzr() != null ? teachClassInfo.getGjtBzr().getEeno() : null;
			if(StringUtils.isNotBlank(teacherEENo)) {
				String groupName = teachClassInfo.getBjmc();
				if (teachClassInfo.getBjmc() != null && teachClassInfo.getBjmc().length() > 10) {
					groupName = teachClassInfo.getBjmc().substring(0, 10);
				}
				String[] gROUP_USERS = new String[1];
				gROUP_USERS[0] = info.getEeno();
				eeimGroup = new EEIMGroup(teachClassInfo.getClassId(), teacherEENo, groupName, gROUP_USERS);
				// 辅导督导老师
				Set<String> teachEENos = new HashSet<String>();
				List<String> fudaoEENos = employeeInfoDao.queryFudaoTeachEENosByTeachClass(teachClassInfo.getClassId());
				List<String> dudaoEENos = employeeInfoDao.queryDudaoTeachEENosByTeachClass(teachClassInfo.getClassId());
				fudaoEENos.addAll(dudaoEENos);
				for (String eeno : fudaoEENos) {
					if (StringUtils.isNotBlank(eeno) && !StringUtils.equals(teacherEENo, eeno)) {
						teachEENos.add(eeno);
					}
				}
				eeimGroup.setTEACHERS(teachEENos.toArray(new String[0]));
				final String appId = OrgUtil.getEEChatAppId(info.getGjtSchoolInfo().getGjtOrg().getCode());
				String eeGroupNo = eeChatService.sysnCreateGroup(appId, eeimGroup);

				if(StringUtils.isNotEmpty(eeGroupNo)) {
					// 如果两个EE群号不同，以最新为准，比如：第一次同步，eegroup为null时则更新
					if(!StringUtils.equals(eeGroupNo, teachClassInfo.getEegroup())) {
						teachClassInfo.setEegroup(eeGroupNo);
						teachClassInfo.setSyncStatus(Constants.BOOLEAN_YES);
						gjtClassInfoDao.save(teachClassInfo);
					}
					return true;
				}
			}
		} catch (Exception e) {
			String objJson = GsonUtils.toJson(eeimGroup);
			log.error("createGroupNoAndAddSingleStudent fail ======== params:" + objJson);
			// 记录EE失败日志
			gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_EE002, objJson, e.toString().length() > 500 ? e.toString().substring(0, 500) : e.toString()));
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 创建EE群，并加入班级所有的成员，如果群不存在就创建再加群成员，如果群存在直接加群成员
	 * @param teachClassId
	 * @return
	 */
	@Override
	public boolean createGroupNoAndAddAllStudent(String teachClassId) {
		EEIMGroup eeimGroup = null;
		GjtClassInfo teachClassInfo = gjtClassInfoDao.findOne(teachClassId); // 获取教学班信息		
		try {
			List<String> eenoList = studentInfoDao.findStudentEENoListByTeachClassId(teachClassId);
			if(teachClassInfo.getGjtBzr() != null) {
				String teacherEENo = teachClassInfo.getGjtBzr().getEeno();
				if (StringUtils.isBlank(teacherEENo)) {
					this.createHeadTeacherEENo(teachClassInfo.getGjtBzr().getEmployeeId());
					teachClassInfo.setGjtBzr(gjtEmployeeInfoDao.findOne(teachClassInfo.getGjtBzr().getEmployeeId()));
					teacherEENo = teachClassInfo.getGjtBzr().getEeno();
				}
				String groupName = teachClassInfo.getBjmc();
				if (teachClassInfo.getBjmc() != null && teachClassInfo.getBjmc().length() > 10) {
					groupName = teachClassInfo.getBjmc().substring(0, 10);
				}
				Set<String> studentEENos = new HashSet<String>();
				for (String eeno : eenoList) {
					if (StringUtils.isNotBlank(eeno) && !StringUtils.equals(teacherEENo, eeno)) {
						studentEENos.add(eeno);
					}
				}
				eeimGroup = new EEIMGroup(teachClassInfo.getClassId(), teacherEENo, groupName, studentEENos.toArray(new String[0]));
				// 辅导督导老师
				Set<String> teachEENos = new HashSet<String>();
				List<String> fudaoEENos = employeeInfoDao.queryFudaoTeachEENosByTeachClass(teachClassInfo.getClassId());
//				List<String> dudaoEENos = employeeInfoDao.queryDudaoTeachEENosByTeachClass(teachClassInfo.getClassId());
//				fudaoEENos.addAll(dudaoEENos);
				for (String eeno : fudaoEENos) {
					if (StringUtils.isNotBlank(eeno) && !StringUtils.equals(teacherEENo, eeno)) {
						teachEENos.add(eeno);
					}
				}
				eeimGroup.setTEACHERS(teachEENos.toArray(new String[0]));
				final String appId = OrgUtil.getEEChatAppId(teachClassInfo.getGjtSchoolInfo().getGjtOrg().getCode());
				String eeGroupNo = eeChatService.sysnCreateGroup(appId, eeimGroup);

				if (StringUtils.isNotEmpty(eeGroupNo)) {
					// 如果两个EE群号不同，以最新为准，比如：第一次同步，eegroup为null时则更新
					if (!StringUtils.equals(eeGroupNo, teachClassInfo.getEegroup()))			 {
						teachClassInfo.setEegroup(eeGroupNo);
						teachClassInfo.setSyncStatus(Constants.BOOLEAN_YES);
						gjtClassInfoDao.save(teachClassInfo);
					}
					return true;
				}
			}
		} catch (Exception e) {
			String objJson = GsonUtils.toJson(eeimGroup);
			log.error("createGroupNoAndAddAllStudent fail ======== params:" + objJson);
			// 记录EE失败日志
			gjtSyncLogService.insert(new GjtSyncLog(teachClassInfo.getBjmc(), teachClassInfo.getClassId(), Constants.RSBIZ_CODE_EE003, objJson, e.toString().length() > 500 ? e.toString().substring(0, 500) : e.toString()));
			e.printStackTrace();
		}
		return false;
	}
}
