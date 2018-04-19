/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.account;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.gzedu.SSOUtil;
import com.ouchgzee.headTeacher.dao.account.GjtUserAccountDao;
import com.ouchgzee.headTeacher.dao.account.TblPriLoginLogDao;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.systemManager.GjtSetOrgCopyrightDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtSetOrgCopyright;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.pojo.BzrTblPriLoginLog;
import com.ouchgzee.headTeacher.service.account.BzrGjtUserAccountService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;

/**
 * 用户账号业务接口<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月4日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated
@Service("bzrGjtUserAccountServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtUserAccountServiceImpl extends BaseServiceImpl<BzrGjtUserAccount> implements BzrGjtUserAccountService {

	@Autowired
	private GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	private TblPriLoginLogDao tblPriLoginLogDao;
	@Autowired
	private GjtSetOrgCopyrightDao gjtSetOrgCopyrightDao;

	@Override
	protected BaseDao<BzrGjtUserAccount, String> getBaseDao() {
		return gjtUserAccountDao;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ouchgzee.headTeacher.service.student.GjtUserAccountService#findOne(
	 * java.lang.String)
	 */
	@Override
	public BzrGjtUserAccount findOne(String accountId) {
		return gjtUserAccountDao.findOne(accountId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ouchgzee.headTeacher.service.student.GjtUserAccountService#
	 * findByTeacherLogin(java.lang.String)
	 */
	@Override
	public BzrGjtUserAccount findByTeacherLogin(String username) {
		return gjtUserAccountDao.findByTeacherLoginAccount(username);
	}

	@Override
	public Page<BzrGjtUserAccount> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrGjtUserAccount> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				BzrGjtUserAccount.class);
		return gjtUserAccountDao.findAll(spec, pageRequst);
	}

	@Override
	public BzrGjtUserAccount queryByLoginAccount(String loginAccount) {
		BzrGjtUserAccount user = gjtUserAccountDao.findByLoginAccount(loginAccount);
		return user;
	}

	@Override
	public Date getFirstLoginByLoginAccount(String loginAccount) {
		return tblPriLoginLogDao.findFirstLoginByLoginAccount(loginAccount);
	}

	@Override
	public boolean insert(BzrGjtUserAccount entity) {
		entity.setId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		gjtUserAccountDao.save(entity);
		return true;
	}

	@Override
	public void update(BzrGjtUserAccount entity) {
		entity.setUpdatedDt(new Date());
		gjtUserAccountDao.save(entity);
	}

	@Override
	public void delete(List<String> ids) {
		gjtUserAccountDao.delete(gjtUserAccountDao.findAll(ids));
	}

	@Override
	public Page<BzrTblPriLoginLog> queryLoginLogByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<BzrTblPriLoginLog> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return tblPriLoginLogDao.findAll(spec, pageRequest);
	}

	/**
	 * 根据学员查他的全部考勤信息
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public List<BzrTblPriLoginLog> queryAllLoginLog(Map<String, Object> searchParams) {

		Criteria<BzrTblPriLoginLog> spec = new Criteria<BzrTblPriLoginLog>();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		List<BzrTblPriLoginLog> list = tblPriLoginLogDao.findAll(spec, new Sort(Sort.Direction.DESC, "createdDt"));
		return list;

	}

	@Override
	public boolean updateQuitState(String id) {
		int i = gjtUserAccountDao.updateQuitState(id);
		return i == 1 ? true : false;
	}

	@Override
	public boolean updateSessionId(String userId, BigDecimal loginCount, String sessionId) {
		// 登陆成功，记录用户是否在线,记录登陆次数
		int loginNum = 0;
		if (loginCount != null) {
			loginNum = loginCount.intValue() + 1;
		} else {
			loginNum = 1;
		}
		int i = gjtUserAccountDao.updateSessionId(userId, new BigDecimal(loginNum), sessionId);
		return i == 1 ? true : false;
	}

	@Override
	public boolean updateLoginState(String id, BigDecimal loginCount, String sessionId) {
		// 登陆成功，记录用户是否在线,记录登陆次数
		int loginNum = 0;
		if (loginCount != null) {
			loginNum = loginCount.intValue() + 1;
		} else {
			loginNum = 1;
		}
		int i = gjtUserAccountDao.updateLoginState(id, new BigDecimal(loginNum), sessionId);
		return i == 1 ? true : false;
	}

	@Override
	public String studentSimulation(String studentId, String xh) {
		String p = SSOUtil.getSignOnParam(xh);
		String domain = getRedirectDomain(xh, PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		domain = domain == null ? AppConfig.getProperty("pcenterServer") : domain;
		String url = domain + AppConfig.getProperty("sso.login.url");
		url += "?p=" + p + "&pdmn=pd&s=" + studentId;
		return url;
	}

	@Override
	public String studentSimulationNew(String appId, String studentId, String xh, String type) {
		String p = SSOUtil.encrypt(appId, studentId);
		String domain = getRedirectDomain(xh, PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		domain = domain == null ? AppConfig.getProperty("pcenterServer") : domain;
		String url = domain + AppConfig.getProperty("sso.login.url.new");
		url += "?sign=" + p + "&t=" + type;
		return url;
	}

	public String getRedirectDomain(String loginAccount, int platfromType) {
		BzrGjtSetOrgCopyright item = gjtSetOrgCopyrightDao.findByLoginAccountAndPlatfromType(loginAccount,
				String.valueOf(platfromType));
		if (item != null) {
			return item.getSchoolRealmName();
		} else {
			return null;
		}
	}
}
