package com.gzedu.xlims.serviceImpl.flow;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.ResultFeedback;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.flow.GjtFlowRecordDao;
import com.gzedu.xlims.dao.signup.GjtSignupDao;
import com.gzedu.xlims.dao.signup.GjtSignupDataDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.flow.GjtFlowRecord;
import com.gzedu.xlims.pojo.status.SignupAuditStateEnum;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

/**
 * 审核流程逻辑类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年02月11日
 * @version 2.5
 * @since JDK 1.7
 */
@Service
public class GjtFlowServiceImpl extends BaseServiceImpl<GjtFlowRecord> implements GjtFlowService {

    @Autowired
    private GjtFlowRecordDao gjtFlowRecordDao;

    @Autowired
    private GjtSignupDao gjtSignupDao;

    @Autowired
    private GjtSignupDataDao gjtSignupDataDao;

    @Autowired
    private GjtUserAccountDao gjtUserAccountDao;

    @Autowired
    private GjtStudentInfoDao gjtStudentInfoDao;

    @Override
    protected BaseDao<GjtFlowRecord, String> getBaseDao() {
        return this.gjtFlowRecordDao;
    }

	@Override
	public List<GjtFlowRecord> queryFlowRecordByStudentId(String studentId) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_studentId", studentId);
		return super.queryBy(searchParams, new Sort("auditDt"));
	}

	@Override
	public boolean initAuditSignupInfo(String studentId) {
		GjtStudentInfo studentInfo = gjtStudentInfoDao.findOne(studentId);
		if(studentInfo != null
				&& (studentInfo.getPerfectStatus() != null && studentInfo.getPerfectStatus() == 1)
				&& !Constants.BOOLEAN_1.equals(studentInfo.getGjtSignup().getAuditState())) {
			List<GjtFlowRecord> list = queryFlowRecordByStudentId(studentId);
			// 没有审核记录或者审核不通过则初始化审核
			if(list == null || list.size() == 0 || list.get(list.size() - 1).getAuditState().intValue() == 2) {
				Date now = new Date();
				
				// 审核状态改为待审核
				studentInfo.getGjtSignup().setAuditState("3");
				gjtStudentInfoDao.save(studentInfo);
	
				GjtFlowRecord entity = new GjtFlowRecord();
				entity.setFlowRecordId(UUIDUtils.random());
				entity.setStudentId(studentId);
				entity.setAuditState(new BigDecimal(1)); // 默认审核通过
				entity.setAuditDt(now);
				// 审核角色：自己
				entity.setAuditOperatorRole(new BigDecimal(1));
				gjtFlowRecordDao.save(entity);
				
				GjtFlowRecord entity2 = new GjtFlowRecord();
				entity2.setFlowRecordId(UUIDUtils.random());
				entity2.setStudentId(studentId);
				entity2.setAuditState(new BigDecimal(0)); // 默认待审核
				// 下一个审核角色：班主任
				entity2.setAuditOperatorRole(new BigDecimal(2));
				gjtFlowRecordDao.save(entity2);

				if(list == null || list.size() == 0) {
					try {
						// 发送短信通知学籍资料提交成功
						SMSUtil.sendMessage(studentInfo.getSjh(),
								String.format("%1$s学员你好，你的学籍资料提交成功，需要通过审核后才能注册学籍和开始学习，你可以前往%2$s官网（http://www.oucgz.cn）登录个人学习平台，查看资料审核结果！",
										studentInfo.getXm(),
										"国家开放大学（广州）实验学院"),
								"gk");
					} catch (Exception e) {
					}
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public ResultFeedback auditSingupInfo(String studentId, String auditState, String auditContent, int operatorRole, String operatorRealName) {
		ResultFeedback feedback = new ResultFeedback(false, null);
		boolean pass = false;
		if((pass = "1".equals(auditState)) || "2".equals(auditState)) {
			if (operatorRole == 2 || operatorRole == 3) {
				Date now = new Date();
				List<GjtFlowRecord> list = queryFlowRecordByStudentId(studentId);
				if (list != null && list.size() > 0 ) {
					GjtFlowRecord gjtFlowRecord = list.get(list.size() - 1);
					if (gjtFlowRecord.getAuditOperatorRole().intValue() == operatorRole) {
						gjtFlowRecord.setAuditState(new BigDecimal(auditState));
						gjtFlowRecord.setAuditContent(auditContent);
						gjtFlowRecord.setAuditOperator(operatorRealName);
						gjtFlowRecord.setAuditDt(now);
						GjtFlowRecord result = gjtFlowRecordDao.save(gjtFlowRecord);
						if (result != null) {
							GjtStudentInfo studentInfo = gjtStudentInfoDao.findOne(studentId);
							String smsType = null;
							// 这个逻辑来源于旧的版本
							String treeCode = studentInfo.getGjtSchoolInfo().getGjtOrg() != null ? studentInfo.getGjtSchoolInfo().getGjtOrg().getTreeCode() : null;
							if(StringUtils.contains(treeCode, "00010011")) {
								smsType = "lgzz";
							} else if(StringUtils.contains(treeCode, "00010007")) {
								smsType = "gk";
							}
							// 如果审核通过，则增加一条下一个审核记录
							if (pass) {
								GjtFlowRecord entity = new GjtFlowRecord();
								entity.setFlowRecordId(UUIDUtils.random());
								entity.setStudentId(studentId);
								entity.setAuditState(new BigDecimal(0)); // 默认待审核
								switch (operatorRole) {
									case 2:
										// 下一个审核角色：招生办
										entity.setAuditOperatorRole(new BigDecimal(3));
										break;
									case 3:
										// 下一个审核角色：学籍科
										entity.setAuditOperatorRole(new BigDecimal(4));
										break;
								}
								gjtFlowRecordDao.save(entity);
							}
							// 如果审核不通过，则更新gjt_signup的最终审核状态audit_state 发送短信通知审核结果
							else {
								gjtSignupDao.auditSignupData(studentId, SignupAuditStateEnum.不通过.getValue() + "", auditContent, "招生调接口", now);

								try {
									// 发送短信通知审核结果
									SMSUtil.sendMessage(studentInfo.getSjh(),
											String.format("%1$s学员你好，你提交的学籍资料未通过审核，为了不影响你的学籍正常注册，请立即前往%2$s官网（http://www.oucgz.cn）登录个人学习平台，查看审核不通过原因，并按要求重新提交资料！",
													studentInfo.getXm(),
													studentInfo.getGjtSchoolInfo().getGjtOrg() != null ? studentInfo.getGjtSchoolInfo().getGjtOrg().getOrgName() : null),
											smsType);
								} catch (Exception e) {
								}
							}
							feedback.setSuccessful(true);
							return feedback;
						}
					} else {
						feedback.setMessage("当前审核角色不匹配");
					}
				} else {
					feedback.setMessage("学员待提交资料");
				}
			} else {
				feedback.setMessage("审核角色参数错误");
			}
		} else {
			feedback.setMessage("操作异常");
		}
		return feedback;
	}

    @Override
    public boolean auditSignupData(String studentId, String auditState, String auditContent, String operatorId, String operatorRealName) {
        boolean pass = false;
        if((pass = "1".equals(auditState)) || "2".equals(auditState)) {
            Date now = new Date();
			List<GjtFlowRecord> list = queryFlowRecordByStudentId(studentId);
			if (list != null && list.size() > 0 ) {
				GjtFlowRecord gjtFlowRecord = list.get(list.size() - 1);
				if (gjtFlowRecord.getAuditOperatorRole().intValue() == 4) {
					gjtFlowRecord.setAuditState(new BigDecimal(auditState));
					gjtFlowRecord.setAuditContent(auditContent);
					gjtFlowRecord.setAuditOperator(operatorRealName);
					gjtFlowRecord.setAuditDt(now);
					GjtFlowRecord result = gjtFlowRecordDao.save(gjtFlowRecord);
					if (result != null) {
						GjtStudentInfo studentInfo = gjtStudentInfoDao.findOne(studentId);
						String smsType = null;
						// 这个逻辑来源于旧的版本
						String treeCode = studentInfo.getGjtSchoolInfo().getGjtOrg() != null ? studentInfo.getGjtSchoolInfo().getGjtOrg().getTreeCode() : null;
						if(StringUtils.contains(treeCode, "00010011")) {
							smsType = "lgzz";
						} else if(StringUtils.contains(treeCode, "00010007")) {
							smsType = "gk";
						}
						if(pass) {
							gjtSignupDao.auditSignupData(studentId, auditState, auditContent, operatorId, now);
							gjtSignupDataDao.auditSignupData(studentId, auditState, auditContent, operatorId, now);

							try {
								// 发送短信通知审核结果
								SMSUtil.sendMessage(studentInfo.getSjh(),
										String.format("%1$s学员你好，你提交的学籍资料已通过审核，学籍正在注册中，你可以前往%2$s官网（http://www.oucgz.cn）登录个人学习平台，查询学籍注册状态和开始学习！",
												studentInfo.getXm(),
												studentInfo.getGjtSchoolInfo().getGjtOrg() != null ? studentInfo.getGjtSchoolInfo().getGjtOrg().getOrgName() : null),
										smsType);
							} catch (Exception e) {}
						} else {
							gjtSignupDao.auditSignupData(studentId, SignupAuditStateEnum.不通过.getValue() + "", auditContent, operatorId, now);

							try {
								// 发送短信通知审核结果
								SMSUtil.sendMessage(studentInfo.getSjh(),
										String.format("%1$s学员你好，你提交的学籍资料未通过审核，为了不影响你的学籍正常注册，请立即前往%2$s官网（http://www.oucgz.cn）登录个人学习平台，查看审核不通过原因，并按要求重新提交资料！",
												studentInfo.getXm(),
												studentInfo.getGjtSchoolInfo().getGjtOrg() != null ? studentInfo.getGjtSchoolInfo().getGjtOrg().getOrgName() : null),
										smsType);
							} catch (Exception e) {}
						}
						return true;
					}
				} else {
//					feedback.setMessage("当前审核角色不匹配");
				}
			} else {
//				feedback.setMessage("学员待提交资料");
			}
        } else {
//			feedback.setMessage("操作异常");
		}
        return false;
    }
    
}
