package com.gzedu.xlims.serviceImpl.teachEmployee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.teachEmployee.GjtTeachEmployeeDao;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.service.teachemployee.GjtTeachEmployeeService;

/**
 * Created by llx on 2017/2/17. 教务管理-教职人员service实现
 */
@Service
public class GjtTeachEmployeeServieImpl implements GjtTeachEmployeeService {

	@Autowired
	private GjtTeachEmployeeDao gjtTeachEmployeeDao;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Autowired
	private CommonDao commonDao;

	/**
	 * 根据当前用户类型，还有参数查询：教职人员
	 * 
	 * @param orgId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@Override
	public Page<GjtEmployeeInfo> queryAll(Map<String, Object> map, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Criteria<GjtEmployeeInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));

		String orgId = (String) map.get("orgId");
		String defaultOrgId = (String) map.get("defaultOrgId");

		if (StringUtils.isNotBlank(orgId)) {
			spec.add(Restrictions.eq("xxzxId", orgId, true));
		} else {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(defaultOrgId);
			spec.add(Restrictions.or(Restrictions.eq("gjtSchoolInfo.id", defaultOrgId, true),
					Restrictions.in("xxzxId", orgList, true)));
		}

		spec.addAll(Restrictions.parse(searchParams));

		PageRequest PageRequestOrder = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
				new Sort(Direction.DESC, "createdDt"));

		Page<GjtEmployeeInfo> employeeInfos = gjtTeachEmployeeDao.findAll(spec, PageRequestOrder);

		return employeeInfos;
	}

	/**
	 * 新增教职人员
	 *
	 * @param item
	 * @return
	 */
	@Override
	public boolean saveEmployee(GjtEmployeeInfo item) {

		GjtEmployeeInfo employeeInfo = gjtTeachEmployeeDao.save(item);
		if (employeeInfo != null) {
			return true;
		}
		return false;
	}

	@Override
	public List<Map<String, String>> queryHeardTeacher(String orgId, int employeeType) {
		StringBuilder sql = new StringBuilder();
		sql.append("  select a.zgh, a.xm, a.xbm, a.sjh, a.dzxx, c.sc_name, b.xxmc");
		sql.append("  from gjt_employee_info a");
		sql.append("  left join gjt_school_info b");
		sql.append("  on a.xx_id = b.id");
		sql.append("  left join gjt_study_center c");
		sql.append("  on a.xxzx_id = c.id");
		sql.append("  where employee_type = :employeeType ");
		sql.append("  and a.is_deleted = 'N'");
		sql.append("  AND (a.XX_ID =:orgId OR");
		sql.append("  a.xxzx_id in");
		sql.append("  (select org.id");
		sql.append("  from gjt_org org");
		sql.append("  where org.is_deleted = 'N'");
		sql.append("  start with org.id = :orgId ");
		sql.append("  connect by prior org.id = org.perent_id))");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", orgId);
		params.put("employeeType", employeeType);
		return commonDao.queryForMapListNative(sql.toString(), params);

	}

}
