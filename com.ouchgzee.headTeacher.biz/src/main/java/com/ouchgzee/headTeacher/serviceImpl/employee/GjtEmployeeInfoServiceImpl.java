/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.employee;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.gzedu.SyncInfoUtil;
import com.ouchgzee.headTeacher.dao.account.GjtUserAccountDao;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.employee.GjtEmployeeInfoDao;
import com.ouchgzee.headTeacher.dao.study.GjtTeachPlanDao;
import com.ouchgzee.headTeacher.daoImpl.BzrGjtEmployeeInfoDaoImpl;
import com.ouchgzee.headTeacher.dto.CourseTeacherDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.service.employee.BzrGjtEmployeeInfoService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;

/**
 * 班主任个人信息操作接口<br>
 * 功能说明：
 * 
 * @author 李建华 lijianhua@gzedu.net
 * @Date 2016年5月10日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated
@Service("bzrGjtEmployeeInfoServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtEmployeeInfoServiceImpl extends BaseServiceImpl<BzrGjtEmployeeInfo>
		implements BzrGjtEmployeeInfoService {

	@Autowired
	private GjtEmployeeInfoDao gjtEmployeeInfoDao;

	@Autowired
	private GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	private GjtTeachPlanDao gjtTeachPlanDao;

	@Autowired
	private BzrGjtEmployeeInfoDaoImpl employeeInfoDaoImpl;

	@Override
	protected BaseDao<BzrGjtEmployeeInfo, String> getBaseDao() {
		return gjtEmployeeInfoDao;
	}

	@Override
	public BzrGjtEmployeeInfo queryByLoginAccount(String loginAccount) {
		return gjtEmployeeInfoDao.findByLoginAccount(loginAccount);
	}

	@Override
	public List<Object[]> queryTachPlanClassByEmpId(String employeeId) {
		return gjtTeachPlanDao.findTachPlanClassByEmpId(employeeId);
	}

	@Override
	public List<CourseTeacherDto> queryCourseTeacherByTeachClassId(String classId) {
		return employeeInfoDaoImpl.findCourseTeacherByTeachClassId(classId);
	}

	@Override
	public boolean update(BzrGjtEmployeeInfo gjtEmployeeInfo) {
		if (StringUtils.isNoneBlank(gjtEmployeeInfo.getEmployeeId())) {
			gjtEmployeeInfo.setUpdatedDt(new Date());
			BzrGjtEmployeeInfo em = gjtEmployeeInfoDao.save(gjtEmployeeInfo);

			BzrGjtUserAccount gjtUserAccount = em.getGjtUserAccount();
			gjtUserAccount.setSjh(em.getSjh());
			gjtUserAccountDao.save(gjtUserAccount);
			if (em != null) {
				Map<String, Object> tMap = new HashMap<String, Object>();
				tMap.put("EMPLOYEE_ID", em.getEmployeeId());
				tMap.put("XM", em.getXm());
				tMap.put("LOGIN_ACCOUNT", em.getGjtUserAccount().getLoginAccount());
				tMap.put("PASSWORD", em.getGjtUserAccount().getPassword());
				tMap.put("ACCOUNT_ID", em.getGjtUserAccount().getId());
				tMap.put("SJH", em.getSjh());
				tMap.put("DZXX", em.getDzxx());
				tMap.put("EENO", em.getEeno());
				tMap.put("ZP", em.getZp());
				tMap.put("XBM", em.getXbm());
				// 数据同步
				SyncInfoUtil.synchTeacherInfo(tMap);
			}
			return true;
		}
		return false;
	}

	@Override
	public int updatePassword(String accountId, String password, String oldPassword, String updatedBy) {
		if (StringUtils.isNoneBlank(accountId) && StringUtils.isNoneBlank(password)
				&& StringUtils.isNoneBlank(oldPassword)) {
			BzrGjtUserAccount gjtUserAccount = gjtUserAccountDao.findOne(accountId);
			if (oldPassword.equals(gjtUserAccount.getPassword2())) {
				return gjtUserAccountDao.updatePwd(accountId, Md5Util.encode(password), password, updatedBy,
						new Date());
			}
			return -1;
		}
		return 0;
	}

	@Override
	public boolean updatePhoto(String employeeId, String photoUrl, String updatedBy) {
		if (StringUtils.isNoneBlank(employeeId) && StringUtils.isNoneBlank(photoUrl)) {
			gjtEmployeeInfoDao.updatePhoto(employeeId, photoUrl, updatedBy, new Date());
			BzrGjtEmployeeInfo em = gjtEmployeeInfoDao.findOne(employeeId);
			if (em != null) {
				Map tMap = new HashMap();
				tMap.put("EMPLOYEE_ID", em.getEmployeeId());
				tMap.put("XM", em.getXm());
				tMap.put("LOGIN_ACCOUNT", em.getGjtUserAccount().getLoginAccount());
				tMap.put("PASSWORD", em.getGjtUserAccount().getPassword());
				tMap.put("ACCOUNT_ID", em.getGjtUserAccount().getId());
				tMap.put("SJH", em.getSjh());
				tMap.put("DZXX", em.getDzxx());
				tMap.put("EENO", em.getEeno());
				tMap.put("ZP", photoUrl);
				tMap.put("XBM", em.getXbm());
				// 数据同步
				SyncInfoUtil.synchTeacherInfo(tMap);
			}
			return true;
		}
		return false;
	}

	@Override
	public List<BzrGjtEmployeeInfo> findListByType(int type, int subType, String orgId) {
		return gjtEmployeeInfoDao.findListByType(String.valueOf(type), subType, orgId);
	}
}
