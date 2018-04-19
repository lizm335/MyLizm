/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.record.RecordDao;
import com.gzedu.xlims.service.record.RecordServer;
import com.ouchgzee.study.dao.exam.ExamNewDaoImpl;
import com.ouchgzee.study.service.exam.ExamServeService;

import net.spy.memcached.MemcachedClient;

/**
 * 记录统计
 */
@Service
public class RecordServerImpl implements RecordServer {
	
	private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 8);

	@Autowired
	RecordDao recordDao;
	
	@Autowired 
	private ExamServeService examServeService;
	
	@Autowired 
	private ExamNewDaoImpl examNewDaoImpl;
	
	@Autowired
	private MemcachedClient memcachedClient;
	
	/**
	 * 初始化考试预约数据
	 * @param formMap
	 */
	@Override
	public Map initRecordAppointment(Map formMap) {
		Map resultMap = new HashMap();
		try {
			// 查询每个院校最后的考试计划
			List batchList = recordDao.getLastExamBatchList(formMap);
            
			if (EmptyUtils.isNotEmpty(batchList)) {
				for (int i=0; i<batchList.size(); i++) {
					final Map batchMap = (Map)batchList.get(i);
					new Thread(new Runnable() {
						@Override
						public void run() {
							initRecordAppointmentRun(batchMap);
						}
					}).start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		return resultMap;
	}
	
	/**
	 * 线程执行
	 * @param batchMap
	 * @return
	 */
	private int initRecordAppointmentRun(final Map batchMap) {
		String exam_batch_code = ObjectUtils.toString(batchMap.get("EXAM_BATCH_CODE"));
		try {
			// 放入缓存，在1个小时内不重复调用
			if (EmptyUtils.isNotEmpty(ObjectUtils.toString(memcachedClient.get("RECORD_APPOINTMENT_" + exam_batch_code)))) {
				return 0;
			} else {
				memcachedClient.add("RECORD_APPOINTMENT_" + exam_batch_code, 60*60, exam_batch_code);
			}
			
			// 查询考试计划该参与的学员
			Map params = new HashMap();
			params.put("xxId", ObjectUtils.toString(batchMap.get("XX_ID")));
	        List<Map> studentInfos = examNewDaoImpl.getExamStudentList(ObjectUtils.toString(batchMap.get("EXAM_BATCH_CODE")), params);
            for (int i = 0; i < studentInfos.size(); i++) {
				final Map searchParams = (Map) studentInfos.get(i);
				// 每个学员的查询预约考试数据都放到线程池处理
				executorService.execute(new Runnable() {
                    @Override
                    public void run() {
						searchParams.put("EXAM_BATCH_CODE", ObjectUtils.toString(batchMap.get("EXAM_BATCH_CODE")));
						searchParams.put("XX_ID", ObjectUtils.toString(batchMap.get("XX_ID")));
		                searchParams.put("condition", "1");
		                searchParams.put("username", ObjectUtils.toString(searchParams.get("SFZH")));
		                searchParams.put("KKZY", ObjectUtils.toString(searchParams.get("MAJOR")));
		                
						// 查询学员的预约列表
						Map appointmentMap = examServeService.queryAppointmentExam(searchParams);
						if (EmptyUtils.isNotEmpty(appointmentMap)) {
							List list = (List)appointmentMap.get("LIST");
							if (EmptyUtils.isNotEmpty(list)) {
								for (int k=0; k<list.size(); k++) {
									Map courseMap = (Map)list.get(k);
									List appointmentList = (List)courseMap.get("APPOINTMENTLIST");
									if (EmptyUtils.isNotEmpty(appointmentList)) {
										for (int m=0; m<appointmentList.size(); m++) {
											Map appMap = (Map)appointmentList.get(m);
											searchParams.put("RECORD_APPOINTMENT_ID", SequenceUUID.getSequence());
											searchParams.put("BESPEAK_STATE", ObjectUtils.toString(appMap.get("BESPEAK_STATE"))); // 预约状态 0-未预约 1-已预约
											searchParams.put("EXAM_PLAN_LIMIT", ObjectUtils.toString(appMap.get("EXAM_PLAN_LIMIT")));
											searchParams.put("EXAM_PLAN_ID", ObjectUtils.toString(appMap.get("EXAM_PLAN_ID")));
											searchParams.put("REC_ID", ObjectUtils.toString(appMap.get("REC_ID")));
											
											if (EmptyUtils.isNotEmpty(recordDao.getRecordAppointment(searchParams))) {
												recordDao.updRecordAppointment(searchParams);
											} else {
												recordDao.addRecordAppointment(searchParams);
											}
										}
									}
								}
							}
						}
                    }
                });
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 让它自动失效
//			memcachedClient.delete("RECORD_APPOINTMENT_"+exam_batch_code);
		}
		return 0;
	}
}
