/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.serviceImpl.activity;

import java.util.Date;
import java.util.Map;

import com.ouchgzee.headTeacher.daoImpl.BzrGjtActivityJoinDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.ouchgzee.headTeacher.dao.activity.GjtActivityDao;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtActivity;
import com.ouchgzee.headTeacher.service.activity.BzrGjtActivityService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月12日
 * @version 1.0
 *
 */
@Deprecated @Service("bzrGjtActivityServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtActivityServiceImpl extends BaseServiceImpl<BzrGjtActivity> implements BzrGjtActivityService {

	@Autowired
	private GjtActivityDao gjtActivityDao;

	@Autowired
	private BzrGjtActivityJoinDaoImpl activityJoinDaoImpl;

	@Override
	protected BaseDao<BzrGjtActivity, String> getBaseDao() {
		// TODO Auto-generated method stub
		return gjtActivityDao;
	}

	@Override
	public Page<BzrGjtActivity> queryActivityInfoPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<BzrGjtActivity> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		// receiveType 1为null ;2为学校ID ;3为年级ID;4为班级ID)
		spec.add(Restrictions.eq("receiveType", "4", true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtActivityDao.findAll(spec, pageRequest);
	}

	/**
	 * 班级活动新增一条活动
	 * 
	 * @param gjtActivity
	 * @return 0:新增失败，1新增成功，-1参数有误
	 */
	@Override
	public String addActivity(BzrGjtActivity gjtActivity) {
		String flag = "0";
		try {
			Date dt1 = gjtActivity.getBeginTime();
			Date dt2 = gjtActivity.getEndTime();
			if (dt1.getTime() > dt2.getTime()) {
				System.out.println("dt1 在dt2后");
				flag = "-1";
			} else {
				gjtActivityDao.save(gjtActivity);
				flag = "1";
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return flag;
	}

	/**
	 * 班级活动修改一条活动
	 * 
	 * @param gjtActivity
	 * @return 0:修改失败，1修改成功，-1修改有误
	 */
	@Override
	public String updateActivity(BzrGjtActivity gjtActivity) {
		String flag = "0";
		try {
			Date dt1 = gjtActivity.getBeginTime();
			Date dt2 = gjtActivity.getEndTime();
			if (gjtActivity.getId() == null) {
				flag = "-2";
			} else {
				if (dt1 != null && dt2 != null && dt1.getTime() > dt2.getTime()) {
					flag = "-1";
				} else {
					gjtActivityDao.save(gjtActivity);
					flag = "1";
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return flag;
	}

	/**
	 * 班级活动删除一条活动
	 * 
	 * @param gjtActivity
	 * @return 0:删除失败，1删除成功，-1删除有误
	 */
	@Override
	public String deleteActivity(BzrGjtActivity gjtActivity) {
		String flag = "0";
		if (gjtActivity.getId() == null) {
			flag = "-1";
		} else {
			gjtActivity.setIsDeleted("Y");
			gjtActivityDao.save(gjtActivity);
			flag = "1";
		}
		return flag;
	}

	/**
	 * 进行中的活动
	 * 
	 * @param searchParams
	 * @return
	 */
	@Override
	public long countUnoverNum(Map<String, Object> searchParams) {
		Criteria<BzrGjtActivity> spec = new Criteria();
		spec.distinct(true);
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtActivityDao.count(spec);
	}

	/**
	 * 已结束的活动
	 * 
	 * @param classId
	 * @return
	 */
	@Override
	public long countOverNum(String endTimeStr, String classId) {
		Criteria<BzrGjtActivity> spec = new Criteria();
		spec.distinct(true);
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.lt("endTime", endTimeStr, true));
		spec.add(Restrictions.eq("receiveId", classId, true));
		return gjtActivityDao.count(spec);
	}

	/**
	 * 待审核活动数量 参数：无 返回的结果：活动数long
	 * 
	 * @return 活动数
	 */
	@Override
	public long countWaitActivityStudentNum(Map<String, Object> searchParams) {
		return activityJoinDaoImpl.countWaitAuditStudentNum(searchParams);
	}

}
