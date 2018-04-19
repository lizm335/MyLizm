/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.serviceManager;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.serviceManager.GjtServiceInfoDao;
import com.ouchgzee.headTeacher.dao.serviceManager.GjtServiceRecordDao;
import com.ouchgzee.headTeacher.dao.serviceManager.GjtServiceStudentDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtServiceInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtServiceRecord;
import com.ouchgzee.headTeacher.pojo.BzrGjtServiceStudent;
import com.ouchgzee.headTeacher.service.serviceManager.BzrGjtServiceManagerService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 功能说明：服务记录管理业务逻辑处理
 * 
 * @author 李建华 lijianhua@gzedu.net
 * @Date 2016年5月09日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Service("bzrGjtServiceManagerServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtServiceManagerServiceImpl extends BaseServiceImpl<BzrGjtServiceInfo> implements BzrGjtServiceManagerService {

	@Autowired
	private GjtServiceInfoDao gjtServiceinfoDao;

	@Autowired
	private GjtServiceRecordDao gjtServiceRecordDao;

	@Autowired
	private GjtServiceStudentDao gjtServiceStudentDao;

	@Override
	protected BaseDao<BzrGjtServiceInfo, String> getBaseDao() {
		return gjtServiceinfoDao;
	}

	/**
	 * 根据班级id及其他查询条件分页查询服务主题信息
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<BzrGjtServiceInfo> queryServiceInfoByClassIdPage(String classId, Map<String, Object> searchParams,
																 PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(new Sort.Order(Sort.Direction.DESC, "createdDt")));
		}
		Criteria<BzrGjtServiceInfo> spec = new Criteria();
		spec.distinct(true);
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfoList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfoList.gjtClassStudentList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfoList.gjtClassStudentList.gjtClassInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfoList.gjtClassStudentList.gjtClassInfo.classId", classId, true));
		spec.addAll(Restrictions.parse(searchParams));

		Page<BzrGjtServiceInfo> result = this.gjtServiceinfoDao.findAll(spec, pageRequest);

		for (Iterator<BzrGjtServiceInfo> iter = result.iterator(); iter.hasNext();) {
			BzrGjtServiceInfo info = iter.next();

			Criteria<BzrGjtServiceRecord> gsrSpec = new Criteria();
			gsrSpec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
			gsrSpec.add(Restrictions.eq("gjtServiceInfo.serviceid", info.getServiceid(), true));
			info.setColRecordNum(gjtServiceRecordDao.count(gsrSpec));
		}
		return result;
	}

	@Override
	public long countUnoverNum(String classId) {
		Criteria<BzrGjtServiceInfo> spec = new Criteria();
		spec.distinct(true);
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfoList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfoList.gjtClassStudentList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfoList.gjtClassStudentList.gjtClassInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfoList.gjtClassStudentList.gjtClassInfo.classId", classId, true));
		spec.add(Restrictions.eq("status", Constants.ServiceStatus_0, true));

		return gjtServiceinfoDao.count(spec);
	}

	@Override
	public long countOverNum(String classId) {
		Criteria<BzrGjtServiceInfo> spec = new Criteria();
		spec.distinct(true);
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfoList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfoList.gjtClassStudentList.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfoList.gjtClassStudentList.gjtClassInfo.isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfoList.gjtClassStudentList.gjtClassInfo.classId", classId, true));
		spec.add(Restrictions.eq("status", Constants.ServiceStatus_1, true));

		return gjtServiceinfoDao.count(spec);
	}
	
	/**
	 * 新增一条服务主题信息
	 * 
	 * @param gjtServiceInfo
	 * @param gjtServiceRecord
	 * @param studentIds
	 * @param openUser 操作人
	 * @return 0:新增失败，1新增成功，-1参数有误
	 */
	@Override
	public String addServiceInfo(BzrGjtServiceInfo gjtServiceInfo, BzrGjtServiceRecord gjtServiceRecord, String studentIds, String openUser) {
		String flag = "0";
	    if (checkAddParam(gjtServiceInfo, gjtServiceRecord, studentIds)) {
			String serviceid = UUIDUtils.create();
			gjtServiceInfo.setServiceid(serviceid);
			gjtServiceInfo.setStarttime(gjtServiceRecord.getStarttime());

			if (Constants.ServiceStatus_1.equals(gjtServiceInfo.getStatus())){
				gjtServiceInfo.setEndtime(gjtServiceRecord.getEndtime());
			}

			gjtServiceRecord.setServiceRecordId(UUIDUtils.create());
			int taketime = (int) ((gjtServiceRecord.getEndtime().getTime() - gjtServiceRecord.getStarttime().getTime()) / 1000);
			gjtServiceRecord.setTaketime(taketime);
			gjtServiceInfo.setTotaltime(taketime);

			gjtServiceRecord.setCreatedBy(openUser);
			gjtServiceInfo.setGjtServiceRecordList(new ArrayList<BzrGjtServiceRecord>());
			gjtServiceInfo.addGjtServiceRecordList(gjtServiceRecord);

			gjtServiceInfo.setCreatedBy(openUser);
			gjtServiceinfoDao.save(gjtServiceInfo);

			List gjtServiceStudentList = new ArrayList();
			String[] studentIdArr = studentIds.split(",");
			for (String studentId : studentIdArr) {
			  BzrGjtServiceStudent gjtServiceStudent = new BzrGjtServiceStudent();
			  gjtServiceStudent.setServiceStudentId(UUIDUtils.create());
			  gjtServiceStudent.setServiceid(serviceid);
			  gjtServiceStudent.setStudentId(studentId);
			  gjtServiceStudent.setCreatedBy(openUser);
			  gjtServiceStudentList.add(gjtServiceStudent);
			}
			gjtServiceStudentDao.save(gjtServiceStudentList);

			flag = "1";
	    } else {
	      flag = "-1";
	    }
	    return flag;
	}
	
	private boolean checkAddParam(BzrGjtServiceInfo gjtServiceInfo, BzrGjtServiceRecord gjtServiceRecord, String studentIds) {
	    boolean flag = true;
	    String title = gjtServiceInfo.getTitle();
	    String content = gjtServiceRecord.getContent();
	    String way = gjtServiceRecord.getWay();
	    Date starttime = gjtServiceRecord.getStarttime();
	    Date endtime = gjtServiceRecord.getEndtime();
	    if ((StringUtils.isEmpty(title)) || (StringUtils.isEmpty(content)) || (StringUtils.isEmpty(way)) || (starttime == null) || (endtime == null) || starttime.getTime()>=endtime.getTime() || (StringUtils.isEmpty(studentIds))){
	      flag = false;
	    }
	    return flag;
	}
	

	/**
	 * 在服务主题下新增一条服务记录
	 * 
	 * @param gjtServiceRecord
	 * @param openUser 操作人
	 * @return 0:新增失败，1新增成功，-1参数有误
	 */
	public String addServiceRecord(BzrGjtServiceRecord gjtServiceRecord, String openUser){
		String flag = "0";
		String content = gjtServiceRecord.getContent();
		String way = gjtServiceRecord.getWay();
		String serviceid = gjtServiceRecord.getGjtServiceInfo().getServiceid();
		Date starttime = gjtServiceRecord.getStarttime();
		Date endtime = gjtServiceRecord.getEndtime();
		
		if(StringUtils.isEmpty(content) || StringUtils.isEmpty(way) || StringUtils.isEmpty(serviceid) || null==starttime || null==endtime || starttime.getTime()>=endtime.getTime()){
			flag="-1";
		}else{
			BzrGjtServiceInfo gjtServiceInfo = gjtServiceinfoDao.findOne(serviceid);
			if(gjtServiceInfo != null){
				gjtServiceRecord.setServiceRecordId(UUIDUtils.create());
				
				int taketime = (int)((endtime.getTime() - starttime.getTime()) / 1000);
				gjtServiceRecord.setTaketime(taketime);
				
				int totaltime = gjtServiceInfo.getTotaltime() + taketime;
				gjtServiceInfo.setTotaltime(totaltime);
				gjtServiceInfo.setUpdatedBy(openUser);
				gjtServiceInfo.setUpdatedDt(new Date());

				gjtServiceRecord.setCreatedBy(openUser);
				gjtServiceRecordDao.save(gjtServiceRecord);
				gjtServiceinfoDao.save(gjtServiceInfo);
				flag="1";
			}
		}
		return flag;
	}

	@Override
	public boolean over(String serviceid, String openUser) {
		BzrGjtServiceInfo gjtServiceInfo = gjtServiceinfoDao.findOne(serviceid);
		if(gjtServiceInfo != null) {
			if(Constants.ServiceStatus_0.equals(gjtServiceInfo.getStatus())) {
				// 获取最近的一条服务记录
				Criteria<BzrGjtServiceRecord> gsrSpec = new Criteria();
				gsrSpec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
				gsrSpec.add(Restrictions.eq("gjtServiceInfo.serviceid", serviceid, true));
				Page<BzrGjtServiceRecord> recordList = gjtServiceRecordDao.findAll(gsrSpec, new PageRequest(0, 1, new Sort(new Sort.Order(Sort.Direction.DESC, "createdDt"))));

				gjtServiceInfo.setStatus(Constants.ServiceStatus_1);
				gjtServiceInfo.setEndtime(recordList.getContent().get(0).getEndtime());
				gjtServiceInfo.setUpdatedBy(openUser);
				gjtServiceInfo.setUpdatedDt(new Date());
				gjtServiceinfoDao.save(gjtServiceInfo);
				return true;
			}
			return true;
		}
		return false;
	}

}
