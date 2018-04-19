/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.biz.serviceManager;

import com.gzedu.xlims.common.Constants;
import com.ouchgzee.headTeacher.pojo.BzrGjtServiceInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtServiceRecord;
import com.ouchgzee.headTeacher.service.serviceManager.BzrGjtServiceManagerService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 功能说明：
 * 
 * @author 李建华 lijianhua@gzedu.net
 * @Date 2016年5月09日
 * @version 2.5
 * @since JDK 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtServiceManagerServiceTest {

	@Autowired
	private BzrGjtServiceManagerService gjtServiceManagerService;
	
	@Autowired
	private BzrGjtStudentService gjtStudentService;
	
	
	public void findServiceInfoByIdTest(){
		String serviceid="09ee6fa015af11e6891ad249ad036f36";
		BzrGjtServiceInfo gjtServiceInfo = gjtServiceManagerService.queryById(serviceid);
		//System.err.println("id:"+gjtServiceInfo.getServiceid()+";serviceInfoTitle:"+gjtServiceInfo.getTitle()+";recordSize:"+gjtServiceInfo.getGjtServiceRecordList().size()+";studentSize:"+gjtServiceInfo.getGjtStudentInfoList().size());
		Assert.isTrue("09ee6fa015af11e6891ad249ad036f36".equals(gjtServiceInfo.getServiceid()));
	}

	@Test
	public void tt() {
		gjtStudentService.queryAtidByIds("388c3ef9ccbe457eb28082439fb1b023");
	}
	public void queryServiceInfoByClassIdPageTest(){
		PageRequest paramPageRequest = new PageRequest(0,10);
		Map<String, Object> searchParams = new HashMap();
		searchParams.put("LIKE_title", "测试");
		searchParams.put("EQ_status", Constants.ServiceStatus_0);
		
		String classId = "59e3a89436a4449baa3e77a3c1f1eeb6";
		Page result = gjtServiceManagerService.queryServiceInfoByClassIdPage(classId, searchParams, paramPageRequest);
		if(result!=null && !result.getContent().isEmpty()){
			List<BzrGjtServiceInfo> serverinfoList = result.getContent();
			for(BzrGjtServiceInfo gjtServiceInfo:serverinfoList){
				//System.err.println("id:"+gjtServiceInfo.getServiceid()+";serviceInfoTitle:"+gjtServiceInfo.getTitle()+";recordSize:"+gjtServiceInfo.getGjtServiceRecordList().size()+";studentSize:"+gjtServiceInfo.getGjtStudentInfoList().size());
				System.err.println("id:"+gjtServiceInfo.getServiceid()+";serviceInfoTitle:"+gjtServiceInfo.getTitle());
			}
		}

		Assert.notEmpty(result.getContent());
	}

	public void queryServiceInfoByPageTest() {
		Map<String, Object> searchParams = new HashMap();
		PageRequest paramPageRequest = new PageRequest(0,10);
		Page result = gjtServiceManagerService.queryByPage(searchParams, paramPageRequest);
		
		if(result!=null && !result.getContent().isEmpty()){
			List<BzrGjtServiceInfo> serverinfoList = result.getContent();
			for(BzrGjtServiceInfo gjtServiceInfo:serverinfoList){
				//System.err.println("id:"+gjtServiceInfo.getServiceid()+";serviceInfoTitle:"+gjtServiceInfo.getTitle()+";recordSize:"+gjtServiceInfo.getGjtServiceRecordList().size()+";studentSize:"+gjtServiceInfo.getGjtStudentInfoList().size());
				//System.out.println("id:"+gjtServiceInfo.getServiceid()+";serviceInfoTitle:"+gjtServiceInfo.getTitle());
			}
		}
		
		Assert.isTrue(!result.getContent().isEmpty());
	}

	/**
	 * 添加服务
	 */
	public void addServiceinfoTest() {
		BzrGjtServiceInfo paramGjtServiceInfo = new BzrGjtServiceInfo();
		BzrGjtServiceRecord paramGjtServiceRecord= new BzrGjtServiceRecord();
		paramGjtServiceInfo.setTitle("单元测试服务记录标题333");
		paramGjtServiceInfo.setStatus("1");
		
		paramGjtServiceRecord.setContent("单元测试服务记录内容333--111");
		String startdateStr = "2016-05-09 12:00:40";
		String enddateStr = "2016-05-09 12:36:40";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		try {
			paramGjtServiceRecord.setStarttime(sdf.parse(startdateStr));
			paramGjtServiceRecord.setEndtime(sdf.parse(enddateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		paramGjtServiceRecord.setWay("2"); 
		
		String paramStudentIds = "4e7ed0967f00000122d555b2bc70df9d,4e7ed09b7f00000122d555b201c4589f";
		String result="1";
		result=gjtServiceManagerService.addServiceInfo(paramGjtServiceInfo, paramGjtServiceRecord, paramStudentIds, "");
		Assert.isTrue("1".equals(result));
	}

	/**
	 * 添加记录
	 */
	public void addServiceRecord(){
		BzrGjtServiceRecord paramGjtServiceRecord= new BzrGjtServiceRecord();
		
		
		paramGjtServiceRecord.setContent("单元测试服务记录内容333-111");
		String startdateStr = "2016-05-09 13:30:40";
		String enddateStr = "2016-05-09 13:33:40";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		try {
			paramGjtServiceRecord.setStarttime(sdf.parse(startdateStr));
			paramGjtServiceRecord.setEndtime(sdf.parse(enddateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		paramGjtServiceRecord.setWay("2"); 
		paramGjtServiceRecord.setGjtServiceInfo(new BzrGjtServiceInfo("5bf88460165a11e6a13dcf50f496724d"));
		String result="0";
		result=gjtServiceManagerService.addServiceRecord(paramGjtServiceRecord, "");
		Assert.isTrue("1".equals(result));
	}

	/**
	 * 结束服务
	 */
	public void over(){
		boolean flag = gjtServiceManagerService.over("5bf88460165a11e6a13dcf50f496724d", "");
		Assert.isTrue(flag);
	}

}
