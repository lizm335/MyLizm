package com.gzedu.xlims.serviceImpl.systemManger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.IPUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.systemManage.TblPriLoginLogDao;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.pojo.TblPriLoginLog;
import com.gzedu.xlims.service.systemManage.TblPriLoginLogService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

@Service
public class TblPriLoginLogServiceImpl extends BaseServiceImpl<TblPriLoginLog> implements TblPriLoginLogService {

	private final static Logger log = LoggerFactory.getLogger(TblPriLoginLogServiceImpl.class);

	@Autowired
	TblPriLoginLogDao tblPriLoginLogDao;

	@Override
	public boolean updateEntity(TblPriLoginLog tblPriLoginLog) {
		tblPriLoginLogDao.save(tblPriLoginLog);
		return true;
	}

	@Override
	protected BaseDao<TblPriLoginLog, String> getBaseDao() {
		return this.tblPriLoginLogDao;
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
	public boolean updateBySessionId(String sessionId, long min) {
		int i = tblPriLoginLogDao.updateBySessionId(sessionId, new BigDecimal(min));
		return i == 1 ? true : false;
	}

	@Override
	public boolean updateNewSessionByOldSession(String oldSessionId, String newSessionId) {
		int i = tblPriLoginLogDao.updateNewSessionByOldSession(oldSessionId, newSessionId);
		return i == 1 ? true : false;
	}

	@Override
	public boolean save(GjtUserAccount user, HttpServletRequest request, String sessionId) {
		TblPriLoginLog item = new TblPriLoginLog();
		item.setUsername(user.getRealName());
		String ip = IPUtils.getIpAddr(request);
		item.setLoginIp(ip != null ? ip.split(",")[ip.split(",").length - 1] : null);// 登陆IP
		item.setLoginAddress(IPUtils.getAddress(item.getLoginIp())); // 登录城市
		item.setOs(HttpClientUtils.getOperationSystem(request));// 系统
		item.setBrowser(HttpClientUtils.getBrowser(request));// 浏览器
		item.setScreen("屏幕尺寸");// 屏幕分辨率旧的是有参数的，我只能写死了
		item.setLoginType("1");// 登陆渠道 1PC,2手机，3，pad
		item.setCreatedDt(DateUtils.getNowTime());
		GjtStudentInfo studentInfo = user.getGjtStudentInfo();
		if (studentInfo != null) {
			item.setCreatedBy(studentInfo.getStudentId());// 如果是学生，记录studentId兼容以前
		} else {
			item.setCreatedBy(user.getId());
		}
		item.setLoginId(UUIDUtils.random().toString());
		item.setLoginSession(sessionId);
		item.setIsDeleted("N");
		item.setVersion(new BigDecimal("3.0"));
		item.setLoginTime(new BigDecimal("0"));
		PriRoleInfo priRoleInfo = user.getPriRoleInfo();
		if (priRoleInfo != null) {// 为null则是学员
			item.setRoleId(priRoleInfo.getRoleId());
		}
		item.setUsername(user.getLoginAccount());
		TblPriLoginLog save = tblPriLoginLogDao.save(item);
		return save != null ? true : false;
	}
	
	
	@Override
	public Page<TblPriLoginLog> queryLoginLogByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<TblPriLoginLog> spec = new Criteria<TblPriLoginLog>();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return tblPriLoginLogDao.findAll(spec, pageRequest);
	}
	
	
	@Override
	public List<TblPriLoginLog> queryAllLoginLog(Map<String, Object> searchParams) {

		Criteria<TblPriLoginLog> spec = new Criteria<TblPriLoginLog>();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		List<TblPriLoginLog> list = tblPriLoginLogDao.findAll(spec, new Sort(Sort.Direction.DESC, "createdDt"));
		return list;
	}
	
	
}
