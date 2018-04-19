/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.classActivity;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.classActivity.GjtActivityDao;
import com.gzedu.xlims.pojo.GjtActivity;
import com.gzedu.xlims.service.classActivity.GjtActivityService;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月28日
 * @version 2.5
 *
 */
@Service
public class GjtActivityServiceImpl implements GjtActivityService {
	@Autowired
	private GjtActivityDao gjtActivityDao;

	@Override
	public Page<GjtActivity> queryActivityInfoPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<GjtActivity> spec = new Criteria();
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
	public String addActivity(GjtActivity gjtActivity) {
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
	public String updateActivity(GjtActivity gjtActivity) {
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
	public String deleteActivity(GjtActivity gjtActivity) {
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
	 * @param classId
	 * @return
	 */
	@Override
	public long countUnoverNum(Map<String, Object> searchParams) {
		Criteria<GjtActivity> spec = new Criteria();
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
		Criteria<GjtActivity> spec = new Criteria();
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
	public long countWaitActivityNum() {
		Criteria<GjtActivity> spec = new Criteria();
		spec.add(Restrictions.eq("auditStatus", "0", true));
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		return gjtActivityDao.count(spec);
	}

	@Override
	public GjtActivity queryById(String id) {
		return gjtActivityDao.findOne(id);
	}

	@Override
	public Boolean insert(GjtActivity entity) {
		GjtActivity activity = gjtActivityDao.save(entity);
		if (activity != null) {
			return true;
		}
		return false;
	}
}
