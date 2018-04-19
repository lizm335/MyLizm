/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.SerializeUtil;
import com.gzedu.xlims.common.StringCount;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.gzedu.EEUtils;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.ouchgzee.headTeacher.condition.GjtMessageInfoSpecs;
import com.ouchgzee.headTeacher.dao.account.GjtUserAccountDao;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.message.GjtMessageBoxDao;
import com.ouchgzee.headTeacher.dao.message.GjtMessageInfoDao;
import com.ouchgzee.headTeacher.dao.message.GjtMessageUserDao;
import com.ouchgzee.headTeacher.dao.student.GjtClassInfoDao;
import com.ouchgzee.headTeacher.dao.student.GjtStudentInfoDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageBox;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageUser;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.service.message.BzrGjtMessageService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;

/**
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月9日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated
@Service("bzrGjtMessageServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtMessageServiceImpl extends BaseServiceImpl<BzrGjtMessageInfo> implements BzrGjtMessageService {

	private static final Object String = null;

	private static Logger log = LoggerFactory.getLogger(GjtMessageServiceImpl.class);

	@Autowired
	private GjtMessageInfoDao gjtMessageInfoDao;

	@Autowired
	private GjtMessageBoxDao gjtMessageBoxDao;

	@Autowired
	private GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	private GjtClassInfoDao gjtClassInfoDao;

	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;

	@Autowired
	private GjtMessageUserDao gjtMessageUserDao;

	@Value("#{configProperties['eeChatInterface']}")
	String eeServer;

	@Override
	protected BaseDao<BzrGjtMessageInfo, String> getBaseDao() {
		return gjtMessageInfoDao;
	}

	@Override
	public boolean insert(BzrGjtMessageInfo messageInfo) {
		BzrGjtMessageInfo message = gjtMessageInfoDao.save(messageInfo);
		return message != null;
	}

	@Override
	@Transactional(value = "transactionManagerBzr")
	public boolean insert(BzrGjtMessageInfo messageInfo, List<BzrGjtMessageUser> gjtMessageUserList) {
		String messageId = UUIDUtils.random();
		messageInfo.setMessageId(messageId);
		if ("12".equals(messageInfo.getInfoType()) || "13".equals(messageInfo.getInfoType())) {
			// 学员ID
			String[] studentIds = messageInfo.getGetUser().split(",");

			for (String studentId : studentIds) {
				if (StringUtils.isBlank(studentId)) {
					return false;
				}
			}
		} else if ("11".equals(messageInfo.getInfoType())) {
			if (StringUtils.isBlank(messageInfo.getGetUser()))
				return false;
		}
		BzrGjtMessageInfo message = gjtMessageInfoDao.save(messageInfo);
		for (BzrGjtMessageUser gjtMessageUser : gjtMessageUserList) {
			gjtMessageUser.setMessageId(messageId);
		}
		gjtMessageUserDao.save(gjtMessageUserList);
		return message != null;

	}

	@Override
	public boolean update(BzrGjtMessageInfo messageInfo) {
		if (StringUtils.isNoneBlank(messageInfo.getMessageId())) {
			messageInfo.setUpdatedDt(new Date());
			BzrGjtMessageInfo message = gjtMessageInfoDao.save(messageInfo);
			return message != null;
		}
		return false;
	}

	@Override
	public boolean updateMessageRead(String messageId, String receiveUser) {
		Integer isRead = gjtMessageBoxDao.isMsgUserRead(messageId, receiveUser);
		if (isRead == 0) {
			BzrGjtMessageInfo messageInfo = gjtMessageInfoDao.findOne(messageId);
			BzrGjtUserAccount userAccount = gjtUserAccountDao.findByLoginAccount(receiveUser);

			// 教务通知
			if ("2".equals(messageInfo.getInfoType())) {
				BzrGjtMessageBox gjtMessageBox = new BzrGjtMessageBox();
				gjtMessageBox.setId(UUIDUtils.random());
				gjtMessageBox.setIsRead("1");
				gjtMessageBox.setXxId(messageInfo.getXxId());
				gjtMessageBox.setCreatedBy(userAccount.getId());
				gjtMessageBox.setReceiveUser(receiveUser);
				gjtMessageBox.setGjtMessageInfo(messageInfo);
				BzrGjtMessageBox mb = gjtMessageBoxDao.save(gjtMessageBox);
				return mb != null;
			}
		}
		return true;
	}

	@Override
	public boolean delete(String messageId, String updatedBy) {
		int i = gjtMessageInfoDao.updateDeleteStatus(messageId, updatedBy, DateUtils.getNowTime());
		return i > 0 ? true : false;
	}

	@Override
	public boolean delete(String[] messageIds, String updatedBy) {
		if (messageIds != null && messageIds.length > 0) {
			for (String messageId : messageIds) {
				this.delete(messageId, updatedBy);
			}
			return true;
		}
		return false;
	}

	@Override
	public Page<BzrGjtMessageInfo> queryHeadTeacherMessageInfoByPage(BzrGjtMessageInfo messageInfo,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Direction.DESC, "createdDt"));
		}
		Page<BzrGjtMessageInfo> result = gjtMessageInfoDao
				.findAll(GjtMessageInfoSpecs.findHeadTeacherMessageInfo(messageInfo), pageRequest);
		for (Iterator<BzrGjtMessageInfo> iterator = result.iterator(); iterator.hasNext();) {
			BzrGjtMessageInfo message = iterator.next();
			message.setColIsNew(gjtMessageBoxDao.isMsgUserRead(message.getMessageId(), messageInfo.getGetUser()) == 0);
		}
		return result;
	}

	@Override
	public long countHeadTeacherMessageInfo(BzrGjtMessageInfo messageInfo) {
		return gjtMessageInfoDao.count(GjtMessageInfoSpecs.findHeadTeacherMessageInfo(messageInfo));
	}

	@Override
	public Page<BzrGjtMessageInfo> queryClassMessageInfoByPage(BzrGjtMessageInfo messageInfo, String classId,
			String putUser, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Direction.DESC, "createdDt"));
		}
		Page<BzrGjtMessageInfo> result = gjtMessageInfoDao
				.findAll(GjtMessageInfoSpecs.findClassMessageInfoByClassId(messageInfo, classId, putUser), pageRequest);

		Long studentNum = gjtClassInfoDao.countStudent(classId);
		for (Iterator<BzrGjtMessageInfo> iterator = result.iterator(); iterator.hasNext();) {
			BzrGjtMessageInfo message = iterator.next();
			Long receiveUserNum = null;
			if ("1".equals(message.getGetUserMethod()))
				receiveUserNum = (long) StringCount.getArrayCount(message.getGetUser(), ",");
			else if ("2".equals(message.getGetUserMethod()))
				receiveUserNum = studentNum;
			message.setColReceiveUserNum(receiveUserNum);
			Long readUserNum = gjtMessageBoxDao.countRead(message.getMessageId());
			message.setColReadUserNum(readUserNum);
		}
		return result;
	}

	@Override
	public long countClassMessageInfo(BzrGjtMessageInfo messageInfo, String classId, String putUser) {
		return gjtMessageInfoDao
				.count(GjtMessageInfoSpecs.findClassMessageInfoByClassId(messageInfo, classId, putUser));
	}

	@Override
	public BzrGjtMessageInfo queryMessageReadSituationById(String messageId) {
		BzrGjtMessageInfo message = gjtMessageInfoDao.findOne(messageId);
		// 消息类型 1-系统消息 2-教务通知 11-班级公告 12-考试通知 13-学习提醒
		if ("11".equals(message.getInfoType()) || "12".equals(message.getInfoType())
				|| "13".equals(message.getInfoType())) {
			Long receiveUserNum = null;
			if ("1".equals(message.getGetUserMethod()))
				receiveUserNum = (long) StringCount.getArrayCount(message.getGetUser(), ",");
			else if ("2".equals(message.getGetUserMethod())) {
				receiveUserNum = gjtClassInfoDao.countStudent(message.getGetUser());
			}
			message.setColReceiveUserNum(receiveUserNum);
			Long readUserNum = gjtMessageBoxDao.countRead(message.getMessageId());
			message.setColReadUserNum(readUserNum);
		}
		return message;
	}

	// ---------------------------------------------------------- 华丽丽的分割线
	// -----------------------------------------------------//

	@Override
	public Page<BzrGjtMessageInfo> queryRemindMessageInfoByPage(Map<String, Object> searchParams,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<BzrGjtMessageInfo> spec = new Criteria<BzrGjtMessageInfo>();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("infoType", "21", true)); // 督促提醒
		spec.addAll(Restrictions.parse(searchParams));
		log.info("督促提醒查询参数：{}", searchParams);
		return getBaseDao().findAll(spec, pageRequest);
	}

	@Override
	public boolean insertRemind(BzrGjtMessageInfo messageInfo, String treeCode, boolean isSendEE, boolean isSendSMS) {
		// 学员ID
		List<String> studentIdList = new ArrayList<String>();
		String[] studentIds = messageInfo.getGetUser().split(",");

		for (String studentId : studentIds) {
			if (StringUtils.isBlank(studentId)) {
				return false;
			}
			studentIdList.add(studentId);
		}
		messageInfo.setInfoType("21"); // 督促提醒
		messageInfo.setGetUserMethod("1"); // 个人
		messageInfo.setGetUserRole("1"); // 学员

		List<Object[]> studentInfoList = gjtStudentInfoDao.findManyStudentInfoByIds(studentIdList);
		StringBuilder xms = new StringBuilder();
		StringBuilder phones = new StringBuilder();
		List<String> eenos = new ArrayList();
		for (Iterator<Object[]> iter = studentInfoList.iterator(); iter.hasNext();) {
			Object[] info = iter.next();
			xms.append(info[1]).append(",");

			phones.append(info[2]).append(",");

			eenos.add((String) info[3]);
		}
		if (xms.length() > 0)
			xms.setLength(xms.length() - 1);
		if (phones.length() > 0)
			phones.setLength(phones.length() - 1);
		messageInfo.setMemo(xms.toString());

		BzrGjtMessageInfo message = null;
		if (isSendEE) {
			String result = "0";
			messageInfo.setMessageId(UUIDUtils.random());
			messageInfo.setInfoTool("4"); // EE
			result = EEUtils.sendEEMessage(messageInfo.getEmployeeId(), messageInfo.getGetUser(),
					messageInfo.getInfoContent());
			if ("1".equals(result)) {
				message = gjtMessageInfoDao.save(messageInfo);
			}
		}
		if (isSendSMS) {
			int smsResult = -1;
			messageInfo.setMessageId(UUIDUtils.random());
			messageInfo.setInfoTool("3"); // 短信
			// 这个逻辑来源于旧的版本
			if (StringUtils.contains(treeCode, "00010011")) {
				smsResult = SMSUtil.sendMessage(phones.toString(), messageInfo.getInfoContent(), "lgzz");
			} else if (StringUtils.contains(treeCode, "00010007")) {
				smsResult = SMSUtil.sendMessage(phones.toString(), messageInfo.getInfoContent(), "gk");
			}
			if (smsResult == 1) {
				message = gjtMessageInfoDao.save(messageInfo);
			}
		}
		return message != null;
	}

	@Override
	public boolean insertRemind(String teaherEENo, BzrGjtMessageInfo messageInfo, List<String> userIdList,
			boolean isSendEE, boolean isSendSMS) {
		List<Object[]> studentInfoList = gjtStudentInfoDao.findManyStudentByuserIds(userIdList);
		BzrGjtMessageInfo message = null;
		StringBuilder xms = new StringBuilder();
		StringBuilder mobilePhones = new StringBuilder();
		List<String> phoneList = new ArrayList<String>();
		List<String> eenos = new ArrayList<String>();

		for (Object[] info : studentInfoList) {
			xms.append(info[1]).append(",");
			String phone = (String) info[2];
			phoneList.add(phone);
			mobilePhones.append(phone).append(",");
			eenos.add((String) info[3]);
			messageInfo.setMemo(xms.toString());

			if (isSendEE) {
				try {
					String result = "0";
					if (StringUtils.isBlank(messageInfo.getMessageId())) {
						messageInfo.setMessageId(UUIDUtils.random());
					} else {
						messageInfo.setUpdatedDt(DateUtils.getNowTime());
					}
					messageInfo.setInfoTool("4"); // EE
					result = EEUtils.sendEE(teaherEENo, (String) info[3], messageInfo.getInfoContent(),
							(String) info[4], eeServer);
					if ("1".equals(result)) {
						message = gjtMessageInfoDao.save(messageInfo);
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}

		// 短信可以发送多个手机号
		if (isSendSMS) {
			int smsResult = -1;
			if (StringUtils.isBlank(messageInfo.getMessageId())) {
				messageInfo.setMessageId(UUIDUtils.random());
			} else {
				messageInfo.setUpdatedDt(DateUtils.getNowTime());
			}
			messageInfo.setInfoTool("3"); // 短信
			int size = phoneList.size();
			if (size > 30) {
				int nThreads = 5;
				for (int i = 0; i < nThreads; i++) {
					int start = size / nThreads * i;
					int end = size / nThreads * (i + 1);
					if (!(i < nThreads - 1)) {
						end = size;// 最后一份有余数。直接取size最后一个下标
					}
					List<String> subList = phoneList.subList(start, end);
					StringBuilder manyPhones = new StringBuilder();
					for (String phone : subList) {
						manyPhones.append(phone).append(",");
					}
					smsResult = SMSUtil.sendMessage(manyPhones.toString(), messageInfo.getInfoContent(), "gk");
					log.info("督促提醒发送的手机号：" + mobilePhones.toString());
				}
			} else {
				smsResult = SMSUtil.sendMessage(mobilePhones.toString(), messageInfo.getInfoContent(), "gk");
				log.info("督促提醒发送的手机号：" + mobilePhones.toString());
			}

			try {
				message = gjtMessageInfoDao.save(messageInfo);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		return message != null;
	}

	@Override
	public boolean resend(String employeeId, String messageId, String updatedBy, String treeCode, boolean isSendEE,
			boolean isSendSMS) {
		BzrGjtMessageInfo messageInfo = gjtMessageInfoDao.findOne(messageId);

		BzrGjtMessageInfo info = SerializeUtil.unserialize(SerializeUtil.serialize(messageInfo));
		info.setMessageId(null);
		info.setGjtUserAccount(new BzrGjtUserAccount(updatedBy));
		info.setUpdatedBy(updatedBy);
		info.setUpdatedDt(new Date());
		info.setEmployeeId(employeeId);
		return insertRemind(info, treeCode, isSendEE, isSendSMS);
	}

	@Override
	public Page<BzrGjtMessageInfo> findAll(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "effectiveTime"));
		}
		Criteria<BzrGjtMessageInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		Page<BzrGjtMessageInfo> result = gjtMessageInfoDao.findAll(spec, pageRequest);

		for (Iterator<BzrGjtMessageInfo> iterator = result.iterator(); iterator.hasNext();) {
			BzrGjtMessageInfo message = iterator.next();
			Criteria<BzrGjtMessageUser> spec2 = new Criteria();
			spec2.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
			spec2.add(Restrictions.eq("messageId", message.getMessageId(), true));
			long receiveUserNum = gjtMessageUserDao.count(spec2);
			message.setColReceiveUserNum(receiveUserNum);
			spec2.add(Restrictions.eq("isEnabled", Constants.BOOLEAN_1, true));
			Long readUserNum = gjtMessageUserDao.count(spec2);
			message.setColReadUserNum(readUserNum);
		}
		return result;
	}

	@Override
	public long findAllCount(Map<String, Object> searchParams) {
		Criteria<BzrGjtMessageInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtMessageInfoDao.count(spec);
	}

}
