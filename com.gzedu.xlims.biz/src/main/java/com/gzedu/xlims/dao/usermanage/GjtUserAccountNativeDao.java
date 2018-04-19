package com.gzedu.xlims.dao.usermanage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;

@Repository
public class GjtUserAccountNativeDao {

	private static Logger log = LoggerFactory.getLogger(GjtUserAccountNativeDao.class);

	@Autowired
	private CommonDao commonDao;

	public Page<Map<String, Object>> queryPage(String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select a.ID            as \"id\",");
		sql.append("  a.login_account as \"loginAccount\",");
		sql.append("  a.real_name     as \"realName\",");
		sql.append("  o.org_name      as \"orgName\",");
		sql.append("  r.role_name     as \"roleName\",");
		sql.append("  a.sjh           as \"sjh\",");
		sql.append("  a.is_enabled    as \"isEnabled\"");
		sql.append("  from gjt_org o, gjt_user_account a");
		sql.append("  left join pri_role_info r");
		sql.append("  on a.role_id = r.role_id");
		sql.append("  where a.is_deleted = 'N'");
		sql.append("  and a.org_id = o.id");

		String isAdmin = (String) searchParams.get("isAdmin");
		if (StringUtils.isBlank(isAdmin)) {
			sql.append("  and (a.org_id in (select id");
			sql.append("  from gjt_org");
			sql.append("  start with ID = :orgId");
			sql.append("  connect by prior id = perent_id");
			sql.append("  and perent_id != id) or a.org_id is null)");
			map.put("orgId", orgId);
		}

		sql.append("  and o.is_deleted = 'N'");

		String orgId2 = (String) searchParams.get("EQ_orgId");
		if (StringUtils.isNotBlank(orgId2)) {
			sql.append(" and a.ORG_ID = :orgId2 ");
			map.put("orgId2", orgId2);
		}

		Object loginAccount = searchParams.get("LIKE_loginAccount");
		if (loginAccount != null && StringUtils.isNotBlank((String) loginAccount)) {
			sql.append(" and a.LOGIN_ACCOUNT like :loginAccount ");
			map.put("loginAccount", "%" + loginAccount + "%");
		}

		Object realName = searchParams.get("LIKE_realName");
		if (realName != null && StringUtils.isNotBlank((String) realName)) {
			sql.append(" and a.REAL_NAME like :realName ");
			map.put("realName", "%" + realName + "%");
		}

		Object roleId = searchParams.get("EQ_priRoleInfo.roleId");
		if (roleId != null && StringUtils.isNotBlank((String) roleId)) {
			sql.append(" and a.ROLE_ID = :roleId ");
			map.put("roleId", roleId);
		}

		Object roleIds = searchParams.get("IN_priRoleInfo.roleId");
		if (roleIds != null) {
			sql.append(" and a.ROLE_ID in (:roleIds) ");
			map.put("roleIds", roleIds);
		}

		sql.append(" order by a.CREATED_DT desc ");

		return commonDao.queryForPageNative(sql.toString(), map, pageRequst);
	}

	public List<Map<String, Object>> queryUserRoleByOrg(String orgId, List<String> roleIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.ID as \"id\",");
		sql.append(" a.login_account as \"loginAccount\",");
		sql.append(" a.real_name as \"realName\",");
		sql.append(" o.org_name as \"orgName\",");
		sql.append(" r.role_name as \"roleName\",");
		sql.append(" r.role_id as \"roleId\",");
		sql.append(" a.sjh as \"sjh\",");
		sql.append(" a.is_enabled as \"isEnabled\"");
		sql.append(" from gjt_org o, gjt_user_account a");
		sql.append(" left join pri_role_info r");
		sql.append(" on a.role_id = r.role_id");
		sql.append(" where a.is_deleted = 'N'");
		sql.append(" and a.org_id = o.id");

		sql.append(" and (a.org_id in (select id");
		sql.append(" from gjt_org");
		sql.append(" start with ID = :orgId");
		sql.append(" connect by prior id = perent_id");
		sql.append(" and perent_id != id) or a.org_id is null)");
		params.put("orgId", orgId);

		sql.append(" and o.is_deleted = 'N'");

		sql.append(" and a.ROLE_ID in (:roleIds) ");
		params.put("roleIds", roleIds);

		sql.append(" order by a.CREATED_DT desc ");

		return commonDao.queryForMapList(sql.toString(), params);
	}

	/**
	 * 查询工作统计，带分页
	 * 
	 * @param orgId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryWorkStatisticsByPage(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		joinSql(orgId, searchParams, sql, params);
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 查询工作统计
	 * 
	 * @param orgId
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, Object>> queryWorkStatisticsBy(String orgId, Map<String, Object> searchParams) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		joinSql(orgId, searchParams, sql, params);
		return commonDao.queryForStringObjectMapListNative(sql.toString(), params);
	}

	/**
	 * COUNT工作统计
	 * 
	 * @param orgId
	 * @param searchParams
	 * @return
	 */
	public long countWorkStatisticsBy(String orgId, Map<String, Object> searchParams) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		joinSql(orgId, searchParams, sql, params);
		return commonDao.queryForCountNative(sql.toString(), params);
	}

	private void joinSql(String orgId, Map<String, Object> searchParams, StringBuffer sql, Map<String, Object> params) {
		sql.append("  select ");
		sql.append("  a.ID \"id\", ");
		sql.append("  a.LOGIN_ACCOUNT \"loginAccount\", ");
		sql.append("  a.REAL_NAME \"realName\", ");
		sql.append("  o.ORG_NAME \"orgName\", ");
		sql.append("  r.ROLE_NAME \"roleName\", ");
		sql.append(
				"  (select count(case when reply_count>0 then 1 else null end)/decode(count(*),0,1,count(*)) from LCMS_MUTUAL_SUBJECT x ");
		sql.append("    where x.isdeleted='N' and x.forward_account_id=a.id) \"reversionRate\",");
		sql.append(
				"  ( select count(case when (y.reply_dt-x.publish_dt)<=1 then 1 else null end)/decode(count(x.subject_id),0,1,count(x.subject_id)) from LCMS_MUTUAL_SUBJECT x ");
		sql.append(
				"    left join LCMS_MUTUAL_REPLY y on x.subject_id=y.subject_id and x.forward_account_id=y.created_by and y.isdeleted='N'");
		sql.append("    where x.isdeleted='N' and x.forward_account_id=a.id) \"reversionInTimeRate\",");
		sql.append("  a.SJH \"sjh\", ");
		sql.append("  a.IS_ENABLED \"isEnabled\",");
		sql.append("  NVL(a.LOGIN_COUNT,0) \"loginCount\",");
		sql.append("  a.LAST_LOGIN_TIME \"lastLoginTime\",");
		sql.append(
				"  NVL((select sum(LOGIN_TIME) from tbl_Pri_Login_Log where is_deleted='N' and created_by=A.ID),0) \"totalLoginTime\",");
		sql.append("  NVL(a.IS_ONLINE,'N') \"isOnline\",");
		sql.append("  FLOOR(SYSDATE - LAST_LOGIN_TIME) \"leftDay\"");
		sql.append("  from ");
		sql.append("  GJT_ORG o, ");
		sql.append("  GJT_USER_ACCOUNT a");
		sql.append("  left join ");
		sql.append("  PRI_ROLE_INFO r ");
		sql.append("  on ");
		sql.append("  a.ROLE_ID = r.ROLE_ID ");
		sql.append("  where ");
		sql.append("  a.ORG_ID = o.ID ");
		sql.append("  and a.IS_DELETED = 'N' ");
		sql.append("  and (a.USER_TYPE = 0 or a.USER_TYPE = 2)");
		sql.append("  and a.ORG_ID in (");
		sql.append(
				"  select ID from GJT_ORG start with ID = :orgId connect by prior ID = PERENT_ID AND PERENT_ID != ID ");
		sql.append("  )");
		sql.append("  and o.IS_DELETED='N'");

		params.put("orgId", orgId);
		Object id = searchParams.get("EQ_id");
		if (StringUtils.isNotBlank((String) id)) {
			sql.append(" and a.id=:id");
			params.put("id", id);
		}
		Object loginAccount = searchParams.get("LIKE_loginAccount");
		if (StringUtils.isNotBlank((String) loginAccount)) {
			sql.append(" and a.LOGIN_ACCOUNT like :loginAccount ");
			params.put("loginAccount", "%" + loginAccount + "%");
		}
		Object realName = searchParams.get("LIKE_realName");
		if (StringUtils.isNotBlank((String) realName)) {
			sql.append(" and a.REAL_NAME like :realName ");
			params.put("realName", "%" + realName + "%");
		}
		Object roleId = searchParams.get("EQ_priRoleInfo.roleId");
		if (StringUtils.isNotBlank((String) roleId)) {
			sql.append(" and a.ROLE_ID = :roleId ");
			params.put("roleId", roleId);
		}
		Object roleIds = searchParams.get("IN_priRoleInfo.roleId");
		if (roleIds != null) {
			sql.append(" and a.ROLE_ID in (:roleIds) ");
			params.put("roleIds", roleIds);
		}
		String studyStatus = ObjectUtils.toString(searchParams.get("STUDY_STATUS"));
		if (EmptyUtils.isNotEmpty(studyStatus)) {
			if ("0".equals(studyStatus)) {// 在线
				sql.append(" AND IS_ONLINE = 'Y'");
			} else if ("4".equals(studyStatus)) {// 从未登录
				sql.append(" AND NVL(a.IS_ONLINE,'N') = 'N' AND (LAST_LOGIN_TIME IS NULL)");
			} else if ("3".equals(studyStatus)) {// 离线（3天内未登录）
				sql.append(
						" AND NVL(a.IS_ONLINE,'N') = 'N' AND LAST_LOGIN_TIME <SYSDATE AND LAST_LOGIN_TIME >=SYSDATE-3");
			} else if ("2".equals(studyStatus)) {// 离线（3天以上未登录）
				sql.append(
						" AND NVL(a.IS_ONLINE,'N') = 'N' AND LAST_LOGIN_TIME <SYSDATE-3 AND LAST_LOGIN_TIME >=SYSDATE-7 ");
			} else if ("1".equals(studyStatus)) {// 离线（7天以上未登录）
				sql.append(" AND NVL(a.IS_ONLINE,'N') = 'N' AND LAST_LOGIN_TIME <SYSDATE-7");
			}
		}
		sql.append(" order by a.CREATED_DT desc ");
	}

	/**
	 * COUNT工作统计-答疑服务
	 * 
	 * @param accountId
	 * @return
	 */
	public Map<String, Object> countWorkStatisticsMutualSubjectByAccountId(String accountId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select count(case when subject_status='N' then 1 else null end) \"totalNotReversion\",");
		sql.append(
				"  count(case when subject_status='N' and ((reply_dt is null and (sysdate-publish_dt)>1) or (reply_dt is not null and (reply_dt-publish_dt)>1)) then 1 else null end) \"totalNotReversionInTimeOut\",");
		sql.append("  count(case when subject_status='Y' then 1 else null end) \"totalReversion\",");
		sql.append(
				"  count(case when subject_status='Y' and (reply_dt-publish_dt)>1 then 1 else null end) \"totalReversionInTimeOut\",");
		sql.append("  count(*) \"total\"");
		sql.append("  from (");
		sql.append("  select x.subject_id,x.subject_status,x.publish_dt,min(y.reply_dt) reply_dt");
		sql.append("  from LCMS_MUTUAL_SUBJECT x ");
		sql.append(
				"  left join LCMS_MUTUAL_REPLY y on x.subject_id=y.subject_id and x.forward_account_id=y.created_by and y.isdeleted='N'");
		sql.append("  where x.isdeleted='N' and x.forward_account_id=:accountId");
		sql.append("  group by x.subject_id,x.subject_status,x.publish_dt");
		sql.append("  )");
		sql.append("  temp");

		params.put("accountId", accountId);
		List<Map<String, Object>> list = commonDao.queryForStringObjectMapListNative(sql.toString(), params);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * COUNT工作统计-统计登录情况
	 * 
	 * @param accountId
	 * @return earliestLoginDt-最早一次登录 latestLogoutDt-最晚一次退出 totalLoginNum-总登录次数
	 *         totalLoginTime-总登录时长（单位：分） recentlyDt-最晚一次登录
	 */
	public Map<String, Object> countProLoginLogByAccountId(String accountId, Map<String, Object> searchParams) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select min(t.created_dt) \"earliestLoginDt\",max(t.updated_dt) \"latestLogoutDt\",");
		sql.append(" 	count(*) \"totalLoginNum\",NVL(sum(LOGIN_TIME),0) \"totalLoginTime\",");
		sql.append(" 	max(t.created_dt) \"recentlyDt\"");
		sql.append(" from tbl_Pri_Login_Log t");
		sql.append(" where t.is_deleted='N' and t.created_by=:accountId");
		params.put("accountId", accountId);

		// 日期格式yyyy-MM-dd
		Pattern pattern = Pattern.compile("^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])$");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String gteCreatedDt = (String) searchParams.get("GTE_createdDt");
		if (StringUtils.isNotBlank(gteCreatedDt)) {
			sql.append(" and t.created_dt>=:gteCreatedDt");
			try {
				Matcher matcher = pattern.matcher(String.valueOf(gteCreatedDt));
				if (matcher.matches()) {
					params.put("gteCreatedDt", sdf.parse(String.valueOf(gteCreatedDt) + " 00:00:00"));
				} else {
					params.put("gteCreatedDt", sdf.parse(String.valueOf(gteCreatedDt)));
				}
			} catch (ParseException e) {
				log.error("时间类型转化异常", e);
			}
		}
		String lteCreatedDt = (String) searchParams.get("LTE_createdDt");
		if (StringUtils.isNotBlank(lteCreatedDt)) {
			sql.append(" and t.created_dt<=:lteCreatedDt");
			try {
				Matcher matcher = pattern.matcher(String.valueOf(lteCreatedDt));
				if (matcher.matches()) {
					params.put("lteCreatedDt", sdf.parse(String.valueOf(lteCreatedDt) + " 23:59:59"));
				} else {
					params.put("lteCreatedDt", sdf.parse(String.valueOf(lteCreatedDt)));
				}
			} catch (ParseException e) {
				log.error("时间类型转化异常", e);
			}
		}

		List<Map<String, Object>> list = commonDao.queryForStringObjectMapListNative(sql.toString(), params);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

}
