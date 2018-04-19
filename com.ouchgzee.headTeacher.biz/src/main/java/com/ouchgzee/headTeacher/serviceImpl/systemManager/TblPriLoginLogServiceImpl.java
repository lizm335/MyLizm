package com.ouchgzee.headTeacher.serviceImpl.systemManager;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.IPUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.ouchgzee.headTeacher.dao.account.TblPriLoginLogDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.pojo.BzrTblPriLoginLog;
import com.ouchgzee.headTeacher.service.systemManage.BzrTblPriLoginLogService;

@Deprecated @Service("bzrTblPriLoginLogServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class TblPriLoginLogServiceImpl implements BzrTblPriLoginLogService {

	private final static Logger log = LoggerFactory.getLogger(TblPriLoginLogServiceImpl.class);

	@Autowired
	TblPriLoginLogDao tblPriLoginLogDao;

	@Override
	public Page<BzrTblPriLoginLog> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveEntity(BzrTblPriLoginLog tblPriLoginLog) {
		tblPriLoginLogDao.save(tblPriLoginLog);
	}

	@Override
	public void updateEntity(BzrTblPriLoginLog tblPriLoginLog) {
		tblPriLoginLogDao.save(tblPriLoginLog);
	}

	@Override
	public BzrTblPriLoginLog queryById(String id) {
		return tblPriLoginLogDao.findOne(id);
	}

	@Override
	public boolean delete(Iterable<String> ids) {
		Boolean bool = true;
		try {
			if (ids != null)
				for (String id : ids) {
					tblPriLoginLogDao.delete(id);
				}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			bool = false;
		}
		return bool;

	}

	@Override
	public boolean delete(String ids) {
		Boolean bool = true;
		try {
			tblPriLoginLogDao.delete(ids);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			bool = false;
		}
		return bool;
	}

	@Override
	public boolean updateBySessionId(String sessionId, long min) {
		int i = tblPriLoginLogDao.updateBySessionId(sessionId, new BigDecimal(min));
		return i == 1 ? true : false;
	}

	@Override
	public boolean save(BzrGjtUserAccount user, HttpServletRequest request, String sessionId) {
		BzrTblPriLoginLog item = new BzrTblPriLoginLog();
		item.setUsername(user.getRealName());
		item.setLoginIp(IPUtils.getIpAddr(request));// 登陆IP
		item.setOs(HttpClientUtils.getOperationSystem(request));// 系统
		item.setBrowser(HttpClientUtils.getBrowser(request));// 浏览器
		item.setScreen("屏幕尺寸");// 屏幕分辨率旧的是有参数的，我只能写死了
		item.setLoginType("1");// 登陆渠道 1PC,2手机，3，pad
		item.setCreatedDt(DateUtils.getNowTime());
		item.setCreatedBy(user.getId());
		item.setLoginId(UUIDUtils.random().toString());
		item.setLoginSession(sessionId);
		item.setIsDeleted("N");
		item.setVersion(new BigDecimal("3.0"));
		item.setLoginTime(new BigDecimal("0"));
		item.setRoleId(user.getPriRoleInfo().getRoleId());
		item.setUsername(user.getLoginAccount());
		BzrTblPriLoginLog save = tblPriLoginLogDao.save(item);
		return save != null ? true : false;
	}

	@Override
	public boolean updateNewSessionByOldSession(String oldSessionId, String newSessionId) {
		int i = tblPriLoginLogDao.updateNewSessionByOldSession(oldSessionId, newSessionId);
		return i == 1 ? true : false;
	}
}
