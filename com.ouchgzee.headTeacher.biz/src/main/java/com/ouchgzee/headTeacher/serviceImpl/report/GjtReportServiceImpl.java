/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.serviceImpl.report;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.report.GjtReportDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtReport;
import com.ouchgzee.headTeacher.service.report.BzrGjtReportService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;

/**
 * 日报实现类<br>
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月27日
 * @version 1.0
 *
 */
@Deprecated @Service("bzrGjtReportServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtReportServiceImpl extends BaseServiceImpl<BzrGjtReport>
		implements BzrGjtReportService {

	@Autowired
	private GjtReportDao gjtReportDao;

	@Override
	protected BaseDao<BzrGjtReport, String> getBaseDao() {
		// TODO Auto-generated method stub
		return gjtReportDao;
	}

	/**
	 * 查询日报
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	@Override
	public Page<BzrGjtReport> queryReportInfoByCreatedDtPage(
			Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(),
					pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<BzrGjtReport> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtReportDao.findAll(spec, pageRequest);
	}

	/**
	 * 增加日报
	 * 
	 * @param gjtReport
	 * @return 0:新增失败，1:新增成功
	 */
	@Override
	public String addReport(BzrGjtReport gjtReport) {
		String flag = "0";
		try {
			gjtReportDao.save(gjtReport);
			flag = "1";
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return flag;
	}

	/**
	 * 修改日报
	 * 
	 * @param gjtReport
	 * @return 0:修改失败，1:修改成功，-1:修改有误
	 */
	@Override
	public String updateReport(BzrGjtReport gjtReport) {
		String flag = "0";
		try {
			if (gjtReport.getId() == null) {
				flag = "-1";
			} else {
				gjtReportDao.save(gjtReport);
				flag = "1";
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return flag;
	}

}
