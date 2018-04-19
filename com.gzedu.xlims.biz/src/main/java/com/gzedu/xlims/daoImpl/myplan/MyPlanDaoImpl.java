package com.gzedu.xlims.daoImpl.myplan;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.myplan.MyPlanDao;

/**
 * Created by llx on 2017/8/17.
 */
@Repository
public class MyPlanDaoImpl implements MyPlanDao {

	@Autowired
	private CommonDao commonDao;

	/**
	 * 我的计划--计划列表查询
	 *
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	@Override
	public Page getPlanListPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  SELECT");
		sql.append(
				"  	gmp.PLAN_ID,gmp.USER_ID,gmp.PLAN_TITLE,gmp.PLAN_LEVEL,gmp.PLAN_STATUS,TO_CHAR(gmp.PLAN_FINISH_TIME,'yyyy-MM-dd') PLAN_FINISH_TIME,");
		sql.append(
				"  	TO_CHAR(gmp.START_DATE,'yyyy-MM-dd hh24:mi') START_DATE,TO_CHAR(gmp.END_DATE,'yyyy-MM-dd hh24:mi') END_DATE,F_DAYS2STR(gmp.END_DATE-gmp.START_DATE) USE_TIME");
		sql.append("  FROM");
		sql.append("  	GJT_MY_PLAN gmp");
		sql.append("  WHERE");
		sql.append("  	gmp.USER_ID = :USER_ID");
		sql.append("  	AND gmp.IS_DELETED = 'N'");

		params.put("USER_ID", ObjectUtils.toString(searchParams.get("USER_ID")));
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PLAN_TITLE")))) {
			sql.append("  	AND gmp.PLAN_TITLE LIKE :PLAN_TITLE");
			params.put("PLAN_TITLE", "%" + ObjectUtils.toString(searchParams.get("PLAN_TITLE")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("START_DATE")))) {
			sql.append("  	AND gmp.START_DATE= :START_DATE");
			params.put("START_DATE", ObjectUtils.toString(searchParams.get("START_DATE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("END_DATE")))) {
			sql.append("  	AND gmp.END_DATE= :END_DATE");
			params.put("END_DATE", ObjectUtils.toString(searchParams.get("END_DATE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PLAN_STATUS")))) {
			sql.append("  	AND gmp.PLAN_STATUS= :PLAN_STATUS");
			params.put("PLAN_STATUS", ObjectUtils.toString(searchParams.get("PLAN_STATUS")));
		}
		sql.append("  ORDER BY");
		sql.append("  	gmp.CREATED_DT");
		return commonDao.queryForPageNative(sql.toString(), params, pageRequest);
	}

	/**
	 * 我的计划--计划列表统计
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public int getPlanCount(Map<String, Object> searchParams) {

		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append(
				"SELECT COUNT(gmp.PLAN_ID) PLAN_ID FROM GJT_MY_PLAN gmp WHERE gmp.IS_DELETED='N' AND gmp.USER_ID=:USER_ID");

		params.put("USER_ID", ObjectUtils.toString(searchParams.get("USER_ID")));
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PLAN_TITLE")))) {
			sql.append("  	AND gmp.PLAN_TITLE LIKE :PLAN_TITLE");
			params.put("PLAN_TITLE", "%" + ObjectUtils.toString(searchParams.get("PLAN_TITLE")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("START_DATE")))) {
			sql.append("  	AND gmp.START_DATE >= to_date(:START_DATE,'yyyy-mm-dd') ");
			params.put("START_DATE", ObjectUtils.toString(searchParams.get("START_DATE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("END_DATE")))) {
			sql.append("  	AND gmp.END_DATE< to_date(:END_DATE,'yyyy-mm-dd')");
			params.put("END_DATE", ObjectUtils.toString(searchParams.get("END_DATE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PLAN_STATUS")))) {
			sql.append("    AND gmp.PLAN_STATUS=:PLAN_STATUS");
			params.put("PLAN_STATUS", ObjectUtils.toString(searchParams.get("PLAN_STATUS")));
		}
		BigDecimal num = (BigDecimal) commonDao.queryObjectNative(sql.toString(), params);
		return Integer.parseInt(ObjectUtils.toString(num));
	}

	/**
	 * 我的计划--新建计划
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public boolean createPlan(Map<String, Object> searchParams) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("  	INSERT INTO GJT_MY_PLAN(");
		sql.append("  			PLAN_ID,PLAN_TITLE,");
		sql.append("  			PLAN_LEVEL,PLAN_CONTENT,");
		sql.append("  			PLAN_FINISH_TIME,");
		sql.append("  			ATTACHMENT_URI,ATTACHMENT_NAME,USER_ID,");
		sql.append("  			CREATED_BY,CREATED_DT");
		sql.append("  		)");
		sql.append(
				"  	VALUES(:PLAN_ID,:PLAN_TITLE,:PLAN_LEVEL,:PLAN_CONTENT,TO_DATE(:PLAN_FINISH_TIME,'yyyy-mm-dd hh24:mi'),:ATTACHMENT_URI,:ATTACHMENT_NAME,:USER_ID,:CREATED_BY,SYSDATE)");

		params.put("PLAN_ID", UUIDUtils.random());
		params.put("PLAN_TITLE", ObjectUtils.toString(searchParams.get("PLAN_TITLE"), "").trim());
		params.put("PLAN_LEVEL", ObjectUtils.toString(searchParams.get("PLAN_LEVEL"), "").trim());
		params.put("PLAN_CONTENT", ObjectUtils.toString(searchParams.get("PLAN_CONTENT"), "").trim());
		params.put("PLAN_FINISH_TIME", ObjectUtils.toString(searchParams.get("PLAN_FINISH_TIME"), "").trim());
		params.put("ATTACHMENT_URI", ObjectUtils.toString(searchParams.get("ATTACHMENT_URI"), "").trim());
		params.put("ATTACHMENT_NAME", ObjectUtils.toString(searchParams.get("ATTACHMENT_NAME"), "").trim());
		params.put("USER_ID", ObjectUtils.toString(searchParams.get("USER_ID")));
		params.put("CREATED_BY", ObjectUtils.toString(searchParams.get("USER_ID")));
		int num = commonDao.insertForMapNative(sql.toString(), params);
		if (num > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 我的计划--查看计划
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Map<String, Object> getPlan(Map<String, Object> searchParams) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  SELECT");
		sql.append(
				"  	(SELECT gua.REAL_NAME FROM GJT_USER_ACCOUNT gua WHERE gua.IS_DELETED='N' AND gua.ID=gmp.USER_ID) REAL_NAME,");
		sql.append(
				"  	(SELECT pri.ROLE_NAME FROM GJT_USER_ACCOUNT gua LEFT JOIN PRI_ROLE_INFO pri ON gua.ROLE_ID=pri.ROLE_ID WHERE gua.IS_DELETED='N' AND pri.ISDELETED='N' AND gua.ID=gmp.USER_ID) ROLE_NAME,");
		sql.append(
				"  	gmp.PLAN_ID,gmp.PLAN_TITLE,gmp.PLAN_LEVEL,to_char(gmp.PLAN_CONTENT) PLAN_CONTENT,gmp.PLAN_STATUS,TO_CHAR(gmp.PLAN_FINISH_TIME,'yyyy-MM-dd') PLAN_FINISH_TIME,TO_CHAR(gmp.CREATED_DT,'yyyy-MM-dd hh24:mi') CREATED_DT,gmp.USER_ID,");
		sql.append("  	gmp.ATTACHMENT_NAME,gmp.ATTACHMENT_URI,");
		sql.append("  	TO_CHAR(gmp.START_DATE,'yyyy-MM-dd') START_DATE,TO_CHAR(gmp.START_DATE,'hh24:mi') START_TIME,");
		sql.append("    TO_CHAR(gmp.END_DATE,'yyyy-MM-dd') END_DATE,TO_CHAR(gmp.END_DATE,'hh24:mi') END_TIME,");
		sql.append("    F_DAYS2STR(gmp.END_DATE-gmp.START_DATE) USE_TIME");
		sql.append("  FROM");
		sql.append(
				"  	GJT_MY_PLAN gmp WHERE gmp.IS_DELETED = 'N' AND gmp.USER_ID= :USER_ID AND gmp.PLAN_ID = :PLAN_ID");

		params.put("USER_ID", ObjectUtils.toString(searchParams.get("USER_ID")));
		params.put("PLAN_ID", ObjectUtils.toString(searchParams.get("PLAN_ID")));
		return commonDao.queryObjectToMapNative(sql.toString(), params);
	}

	/**
	 * 我的计划--查看计划-备注列表
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public List<Map<String, String>> getPlanRemark(Map<String, Object> searchParams) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("SELECT ");
		sql.append("    gmpr.REMARK_ID,gmpr.REMARK_CONTENT,to_char(gmpr.CREATED_DT,'yyyy-MM-dd hh24:mi') CREATED_DT,");
		sql.append(
				"    gmpr.UPDATED_DT FROM GJT_MY_PLAN_REMARK gmpr WHERE gmpr.IS_DELETED='N' AND gmpr.PLAN_ID= :PLAN_ID ORDER BY gmpr.CREATED_DT");

		params.put("PLAN_ID", ObjectUtils.toString(searchParams.get("PLAN_ID")));
		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	/**
	 * 我的计划--开始计划
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public int startPlan(Map<String, Object> searchParams) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append(
				"UPDATE GJT_MY_PLAN gmp SET gmp.START_DATE=TO_DATE(:START_DATE,'yyyy-MM-dd hh24:mi'),gmp.PLAN_STATUS='1' WHERE  gmp.USER_ID=:USER_ID AND gmp.PLAN_ID=:PLAN_ID AND gmp.IS_DELETED='N'");
		params.put("START_DATE", ObjectUtils.toString(searchParams.get("START_DATE")));
		params.put("PLAN_ID", ObjectUtils.toString(searchParams.get("PLAN_ID")));
		params.put("USER_ID", ObjectUtils.toString(searchParams.get("USER_ID")));

		return commonDao.updateForMapNative(sql.toString(), params);
	}

	/**
	 * 我的计划-完成计划
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public int finishPlan(Map<String, Object> searchParams) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append(
				"UPDATE GJT_MY_PLAN gmp SET gmp.END_DATE=TO_DATE(:END_DATE,'yyyy-MM-dd hh24:mi'),gmp.PLAN_STATUS='2' WHERE gmp.USER_ID=:USER_ID AND gmp.PLAN_ID=:PLAN_ID AND gmp.IS_DELETED='N'");
		params.put("END_DATE", ObjectUtils.toString(searchParams.get("END_DATE")));
		params.put("PLAN_ID", ObjectUtils.toString(searchParams.get("PLAN_ID")));
		params.put("USER_ID", ObjectUtils.toString(searchParams.get("USER_ID")));

		return commonDao.updateForMapNative(sql.toString(), params);
	}

	/**
	 * 我的计划-增加备注
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public int addPlanRemark(Map<String, Object> searchParams) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append(
				"INSERT INTO GJT_MY_PLAN_REMARK(REMARK_ID,PLAN_ID,REMARK_CONTENT,USER_ID,CREATED_BY,CREATED_DT) VALUES(:REMARK_ID,:PLAN_ID,:REMARK_CONTENT,:USER_ID,:USER_ID,SYSDATE)");

		String remark_content = ObjectUtils.toString(searchParams.get("REMARK_CONTENT"), "").trim();
		if (EmptyUtils.isEmpty(remark_content)) {
			return 0;
		}
		params.put("REMARK_ID", UUIDUtils.random());
		params.put("PLAN_ID", ObjectUtils.toString(searchParams.get("PLAN_ID")));
		params.put("REMARK_CONTENT", remark_content);
		params.put("USER_ID", ObjectUtils.toString(searchParams.get("USER_ID")));

		return commonDao.updateForMapNative(sql.toString(), params);
	}

	/**
	 * 我的计划--删除计划
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public int deletePlan(Map<String, Object> searchParams) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append(
				"UPDATE GJT_MY_PLAN gmp SET gmp.IS_DELETED='Y',gmp.UPDATED_DT=SYSDATE,gmp.UPDATED_BY=:USER_ID WHERE gmp.PLAN_ID=:PLAN_ID AND gmp.USER_ID=:USER_ID");

		params.put("USER_ID", ObjectUtils.toString(searchParams.get("USER_ID")));
		params.put("PLAN_ID", ObjectUtils.toString(searchParams.get("PLAN_ID")));
		return commonDao.updateForMapNative(sql.toString(), params);
	}

	/**
	 * 我的计划--删除备注
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public int deletePlanRemark(Map<String, Object> searchParams) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append(
				"UPDATE GJT_MY_PLAN_REMARK gmpr SET gmpr.IS_DELETED='Y',gmpr.UPDATED_DT=SYSDATE,gmpr.UPDATED_BY=:USER_ID WHERE gmpr.USER_ID=:USER_ID");

		if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("PLAN_ID"), ""))
				&& EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("REMARK_ID"), ""))) {
			return 0;
		}
		params.put("USER_ID", ObjectUtils.toString(searchParams.get("USER_ID")));
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PLAN_ID")))) {
			sql.append("   AND gmpr.PLAN_ID=:PLAN_ID");
			params.put("PLAN_ID", ObjectUtils.toString(searchParams.get("PLAN_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("REMARK_ID")))) {
			sql.append("   AND gmpr.REMARK_ID=:REMARK_ID");
			params.put("REMARK_ID", ObjectUtils.toString(searchParams.get("REMARK_ID")));
		}
		return commonDao.updateForMapNative(sql.toString(), params);
	}

	/**
	 * 我的计划--修改备注
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public boolean editPlanRemark(Map<String, Object> searchParams) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("UPDATE GJT_MY_PLAN_REMARK gmpr SET gmpr.REMARK_CONTENT=:REMARK_CONTENT,gmpr.UPDATED_BY=:USER_ID,");
		sql.append(
				"    gmpr.UPDATED_DT=SYSDATE,gmpr.VERSION=gmpr.VERSION+1 WHERE gmpr.REMARK_ID=:REMARK_ID AND gmpr.USER_ID=:USER_ID AND gmpr.IS_DELETED='N'");

		params.put("REMARK_CONTENT", ObjectUtils.toString(searchParams.get("REMARK_CONTENT"), "").trim());
		params.put("REMARK_ID", ObjectUtils.toString(searchParams.get("REMARK_ID")));
		params.put("USER_ID", ObjectUtils.toString(searchParams.get("USER_ID")));
		int num = commonDao.updateForMapNative(sql.toString(), params);
		if (num > 0) {
			return true;
		} else {
			return false;
		}
	}
}
