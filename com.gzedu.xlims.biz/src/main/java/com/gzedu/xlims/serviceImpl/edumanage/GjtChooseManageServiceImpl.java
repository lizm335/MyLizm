/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.edumanage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Holder;

import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.gzedu.xlims.common.CSVUtils;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.json.JSONArray;
import com.gzedu.xlims.dao.edumanage.GjtChooseManageDao;
import com.gzedu.xlims.service.api.ApiOucSyncService;
import com.gzedu.xlims.service.edumanage.GjtChooseManageService;
import com.gzedu.xlims.serviceImpl.exam.GjtExamRecordNewServiceImpl;

import net.sf.jxls.transformer.XLSTransformer;
import net.spy.memcached.MemcachedClient;

/**
 * 选课管理
 */
@Service
public class GjtChooseManageServiceImpl implements GjtChooseManageService {
	
	@Autowired
	GjtChooseManageDao gjtChooseManageDao;
	
	@Autowired
	ApiOucSyncService apiOucSyncService;
	
	@Autowired
	MemcachedClient memcachedClient;
	
	/**
	 * 选课管理列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page getChooseManageList(Map searchParams,PageRequest pageRequst) {
		return gjtChooseManageDao.getChooseManageList(searchParams, pageRequst);
	}
	
	/**
	 * 选课管理列表（统计）
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int getChooseManageCount(Map searchParams) {
		int num = 0;
		List chooseList = gjtChooseManageDao.getChooseManageCount(searchParams);
		if (EmptyUtils.isNotEmpty(chooseList)) {
			Map chooseMap = (Map)chooseList.get(0);
			num = Integer.parseInt(ObjectUtils.toString(chooseMap.get("CHOOSE_COUNT")));
		}
		return num;
	}
	
	/**
	 * 删除选课
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int delRecResult(Map searchParams) {
		return gjtChooseManageDao.delRecResult(searchParams);
	}
	
	/**
	 * 新增选课
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int addRecResult(Map searchParams) {
		searchParams.put("REC_ID", SequenceUUID.getSequence());
		String param = ObjectUtils.toString(searchParams.get("ids"));
		if (EmptyUtils.isNotEmpty(param)) {
			String params[] = param.split(",");
			if (EmptyUtils.isNotEmpty(params) && params.length==5) {
				searchParams.put("STUDENT_ID", ObjectUtils.toString(params[0]));
				searchParams.put("TEACH_PLAN_ID", ObjectUtils.toString(params[1]));
				searchParams.put("TERMCOURSE_ID", ObjectUtils.toString(params[2]));
				searchParams.put("TERM_ID", ObjectUtils.toString(params[3]));
				searchParams.put("COURSE_ID", ObjectUtils.toString(params[4]));
			}
		}
		return gjtChooseManageDao.addRecResult(searchParams);
	}
	
	/**
	 * 查询未选课页面
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public List getNoChooseInfo(Map searchParams) {
		return gjtChooseManageDao.getNoChooseInfo(searchParams);
	}
	
	/**
	 * 导出选课记录
	 */
	public String expRecordRecResult(Map formMap) {
		//csv表头
		String[] header = new String[] {"姓名","学号","身份证","手机号","专业层次", "入学学期", "报读专业",
				"开课学期","课程代码","课程名称","选课类型","课程班级","辅导老师","同步状态"};
		
		//下面 data里的key，可以说是数据库字段了
		String[] key = new String[] {"XM","XH","SFZH","SJH","PYCC_NAME","GRADE_NAME","ZYMC",
				"OPEN_GRADE_NAME","KCH","KCMC","REBUILD_STATE","BJMC","TEACH_NAME","SYNC_STATUS"};
		try {
			List data = gjtChooseManageDao.getChooseManageList(formMap);
			List dataList = new ArrayList();
			for (int i=0;i<data.size(); i++) {
				Map dataMap = (Map)data.get(i);
				
				String rebuild_state = ObjectUtils.toString(dataMap.get("REBUILD_STATE"));
				if ("0".equals(rebuild_state)) {
					dataMap.put("REBUILD_STATE", "新增");
				} else {
					dataMap.put("REBUILD_STATE", "重修");
				}
				
				String sync_status = ObjectUtils.toString(dataMap.get("SYNC_STATUS"));
				if ("Y".equals(sync_status)) {
					dataMap.put("SYNC_STATUS", "已同步");
				} else {
					dataMap.put("SYNC_STATUS", "未同步");
				}
				
				dataList.add(dataMap);
			}
			
			String content =  CSVUtils.formatCsvData(dataList, header, key);
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			return CSVUtils.formatCsvData(new ArrayList(), header, key);
		}
	}
	
	/**
	 * 同步选课
	 */
	public Map getSyncRecList(Map formMap) {
		Map resultMap = new HashMap();
		try {
			List syncList = gjtChooseManageDao.getSyncRecList(formMap);
			if (EmptyUtils.isNotEmpty(syncList)) {
				resultMap = (Map)syncList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 同步选课结果
	 */
	public Map getSyncRecResult(Map formMap, String basePath) {
		Map resultMap = new HashMap();
		List rightList = new ArrayList();
		List errorList = new ArrayList();
		String errorMessage = "";
		int syncNum = 0;
		int sumNum = 0;
		try {
			String xx_id = ObjectUtils.toString(formMap.get("XX_ID"));
			// 放入缓存，在十分钟内不重复调用
			if (EmptyUtils.isNotEmpty(ObjectUtils.toString(memcachedClient.get("SYNCREC_"+xx_id)))) {
//				errorMessage = "选课还在同步中，请勿重复同步...";
//				resultMap.put("errorMessage", errorMessage);
//				return resultMap;
			} else {
				memcachedClient.add("SYNCREC_"+xx_id, 600, xx_id);
			}
			
			formMap.put("XXW_SYNC_STATUS", "0");
			List data = gjtChooseManageDao.getChooseManageList(formMap);
			sumNum = data.size();
			
			// 选课每次最多只能传20条选课，分段处理
			int perCount=20, index=0;  
	        int times=data.size()/perCount;  
	        do {
	            List<Map> listTemp=null;  
	            // listTemp是分段处理逻辑的参数  
	            if(data.size()>=perCount){  
	                listTemp=data.subList(0, perCount);
	            } else {  
	                listTemp=data.subList(0, data.size());
	            }
	            Map chooseMap = new HashMap();
	            List chooseList = new ArrayList();
	            for (int i= 0; i<listTemp.size(); i++) {
	            	Map tempMap = (Map)listTemp.get(i);
	            	chooseMap.put(ObjectUtils.toString(tempMap.get("XH"))+"_"+ObjectUtils.toString(tempMap.get("KCH")), ObjectUtils.toString(tempMap.get("REC_ID")));
	            	Map studMap = new HashMap();
	            	studMap.put("UserName", ObjectUtils.toString(tempMap.get("XH")));
	            	studMap.put("CourseCode", ObjectUtils.toString(tempMap.get("KCH")));
	            	studMap.put("OrgCode", ObjectUtils.toString(formMap.get("OrgCode")));
	            	chooseList.add(studMap);
	            	syncNum++;
	            	memcachedClient.delete("SYNCNUM_"+xx_id);
	            	memcachedClient.add("SYNCNUM_"+xx_id, 600, syncNum+"/"+sumNum);
	            }
	            
	            if (EmptyUtils.isNotEmpty(chooseList)) {
	        		Map dataMap = new HashMap();
	        		dataMap.put("operatingUserName", ObjectUtils.toString(formMap.get("operatingUserName")));
	        		dataMap.put("functionType", "SynchStudentCourse");
	        		dataMap.put("synchDATA", chooseList);
	        		Map msgMap =apiOucSyncService.addAPPDataSynch(dataMap);
	        		String status = ObjectUtils.toString(msgMap.get("code"));
	        		String message = ObjectUtils.toString(msgMap.get("message"));
	        		if (EmptyUtils.isEmpty(msgMap)) {
	        			for (int i= 0; i<listTemp.size(); i++) {
	    	            	Map tempMap = (Map)listTemp.get(i);
	    	            	tempMap.put("XXW_SYNC_STATUS", "2");
	    	            	tempMap.put("XXW_SYNC_MSG", "国开学习网服务器接口异常，请联系管理员！");
	    	            	errorList.add(tempMap);
	    	            }
	        		} else if ("1".equals(status)) {// 1、成功  0、失败
	        			for (int i= 0; i<listTemp.size(); i++) {
	    	            	Map tempMap = (Map)listTemp.get(i);
	    	            	tempMap.put("XXW_SYNC_STATUS", "1");
	    	            	tempMap.put("XXW_SYNC_MSG", "同步成功");
	    	            	rightList.add(tempMap);
	    	            	gjtChooseManageDao.updRecSyncStatus(tempMap);
	    	            }
	        		} else {
	        			if (EmptyUtils.isNotEmpty(ObjectUtils.toString(msgMap.get("data")))) {
	    					List dataList = (List)JSON.parse(ObjectUtils.toString(msgMap.get("data")));
	    					Map tempDataMap = new HashMap();
	    					for (int i=0; i<dataList.size(); i++) {
	    						Map dataMaps = (Map)dataList.get(i);
	    						String UserName = ObjectUtils.toString(dataMaps.get("StudentNo"));
	    						String CourseCode = ObjectUtils.toString(dataMaps.get("CourseCode"));
	    						String ErrorCode = ObjectUtils.toString(dataMaps.get("ErrorCode"));
	    						String ErrorMessage = ObjectUtils.toString(dataMaps.get("ErrorMessage"));
	    						if ("0".equals(ErrorCode)) {
	    							tempDataMap.put(UserName+"_"+CourseCode, "国开学习网返回错误信息："+ErrorMessage);
	    						}
	    					}
	    					for (int i= 0; i<listTemp.size(); i++) {
		    	            	Map tempMap = (Map)listTemp.get(i);
		    	            	String ErrorMessage = ObjectUtils.toString(tempDataMap.get(ObjectUtils.toString(tempMap.get("XH"))+"_"+ObjectUtils.toString(tempMap.get("KCH"))));
		    	            	if (EmptyUtils.isEmpty(ErrorMessage))  {
		    	            		tempMap.put("XXW_SYNC_STATUS", "1");
	    	    	            	tempMap.put("XXW_SYNC_MSG", "");
	    	    	            	gjtChooseManageDao.updRecSyncStatus(tempMap);
	    	    	            	rightList.add(tempMap);
		    	            	} else {
		    	            		if (ErrorMessage.indexOf("该课程已选课")>-1) {
		    	            			tempMap.put("XXW_SYNC_STATUS", "1");
		    	    	            	tempMap.put("XXW_SYNC_MSG", "");
		    	    	            	gjtChooseManageDao.updRecSyncStatus(tempMap);
		    	    	            	rightList.add(tempMap);
		    	            		} else {
		    	            			tempMap.put("XXW_SYNC_STATUS", "2");
		    	    	            	tempMap.put("XXW_SYNC_MSG", ErrorMessage);
		    	    	            	gjtChooseManageDao.updRecSyncStatus(tempMap);
		    	    	            	errorList.add(tempMap);
		    	            		}
		    	            	}
	    					}
	    				} else {
	    					for (int i= 0; i<listTemp.size(); i++) {
		    	            	Map tempMap = (Map)listTemp.get(i);
		    	            	tempMap.put("XXW_SYNC_STATUS", "2");
		    	            	tempMap.put("XXW_SYNC_MSG", "国开学习网返回错误信息："+message);
		    	            	errorList.add(tempMap);
		    	            	gjtChooseManageDao.updRecSyncStatus(tempMap);
		    	            }
	    				}
	        		}
	            }
	            data.removeAll(listTemp);  
	            index++;  
	        }  
	        while (index<=times);  
	        
	        // 必须是同步选课后才能同步班级，班级关系，必须按顺序
	     	synchClassStudentCourse(formMap);
	        
	        String timeStr = ObjectUtils.toString(new Date().getTime());
			String root = basePath;
			
			File folder=new File(root+"/tmp");
			if(!folder.exists()){
				folder.mkdirs();
			}
			
			// 导出成功记录
			String tempPath = GjtChooseManageServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/choose/studChooseRight.xls";
			String downPath = root+"/tmp/studChooseRight"+timeStr+".xls";
			Map beans = new HashMap();  
		    beans.put("dataList", rightList);
		    XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS(tempPath, beans, downPath);
            resultMap.put("RIGHT_DOWNPATH", downPath);
			
            // 导出失败记录
            tempPath = GjtChooseManageServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/choose/studChooseError.xls";
			downPath = root+"/tmp/studChooseError"+timeStr+".xls";
            beans = new HashMap();
            beans.put("dataList", errorList);
            transformer = new XLSTransformer();
            transformer.transformXLS(tempPath, beans, downPath);
            resultMap.put("ERROR_DOWNPATH", downPath);
            
            memcachedClient.delete("SYNCREC_"+xx_id);
            memcachedClient.delete("SYNCNUM_"+xx_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resultMap.put("errorMessage", errorMessage);
		resultMap.put("errorNum", errorList.size());
		resultMap.put("rightNum", rightList.size());
		resultMap.put("allNum", errorList.size()+rightList.size());
		return resultMap;
	}
	
	/**
	 * 同步班级数据到国开学习网
	 */
	public Map synchClassStudentCourse(Map formMap) {
		Map resultMap = new HashMap();
		try {
			// 没有设置过辅导老师的课程班设置辅导老师
			gjtChooseManageDao.updaClassTeacher(formMap);
			
			// 同步班级信息
			synchCourseClass(formMap);
			
			// 同步辅导老师和班级信息
			synchCourseClassTeacher(formMap);
			
			// 同步新学员和班级信息
			SynchCourseClassStudent(formMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 同步班级数据
	 */
	public Map synchCourseClass(Map formMap) {
		Map resultMap = new HashMap();
		try {
			List data = gjtChooseManageDao.getNoSyncClassList(formMap);
			
			// 选课每次最多只能传20条选课，分段处理
			int perCount=20,index=0;  
	        int times= data.size()/perCount;  
	        do {  
	            List<Map> listTemp=null;  
	            // listTemp是分段处理逻辑的参数  
	            if(data.size()>=perCount){  
	                listTemp=data.subList(0, perCount);
	            } else {  
	                listTemp=data.subList(0, data.size());
	            }
	            Map chooseMap = new HashMap();
	            List chooseList = new ArrayList();
	            for (int i= 0; i<listTemp.size(); i++) {
	            	Map tempMap = (Map)listTemp.get(i);
	            	chooseMap.put(ObjectUtils.toString(tempMap.get("BH")), ObjectUtils.toString(tempMap.get("CLASS_ID")));
	            	Map cMap = new HashMap();
	            	cMap.put("CourseClassCode", ObjectUtils.toString(tempMap.get("BH")));
	            	cMap.put("CourseClassName", ObjectUtils.toString(tempMap.get("BJMC")));
	            	cMap.put("CourseCode", ObjectUtils.toString(tempMap.get("KCH")));
	            	cMap.put("OrgCode", ObjectUtils.toString(formMap.get("OrgCode")));
	            	chooseList.add(cMap);
	            }
	            
	            if (EmptyUtils.isNotEmpty(chooseList)) {
	        		Map dataMap = new HashMap();
	        		dataMap.put("operatingUserName", ObjectUtils.toString(formMap.get("operatingUserName")));
	        		dataMap.put("functionType", "SynchCourseClass");
	        		dataMap.put("synchDATA", chooseList);
	        		Map msgMap =apiOucSyncService.addAPPDataSynch(dataMap);
	        		String status = ObjectUtils.toString(msgMap.get("code"));
	        		String message = ObjectUtils.toString(msgMap.get("message"));
	        		// 同步成功
	        		if ("1".equals(status)) {
	        			for (int i= 0; i<listTemp.size(); i++) {
	    	            	Map tempMap = (Map)listTemp.get(i);
	    	            	tempMap.put("XXW_SYNC_STATUS", "1");
	    	            	tempMap.put("XXW_SYNC_MSG", "成功");
	    	            	gjtChooseManageDao.updClassXxwSyncStatus(tempMap);
	    	            }
	        		} else {
	        			if (EmptyUtils.isNotEmpty(ObjectUtils.toString(msgMap.get("data")))) {
	        				List dataList = (List)JSON.parse(ObjectUtils.toString(msgMap.get("data")));
	    					Map tempDataMap = new HashMap();
	    					for (int i=0; i<dataList.size(); i++) {
	    						Map dataMaps = (Map)dataList.get(i);
	    						String bh = ObjectUtils.toString(dataMaps.get("CourseClassCode"));
	    						String ErrorCode = ObjectUtils.toString(dataMaps.get("ErrorCode"));
	    						String ErrorMessage = ObjectUtils.toString(dataMaps.get("ErrorMessage"));
	    						if ("0".equals(ErrorCode)) {
	    							tempDataMap.put(bh, "国开学习网返回错误信息："+ErrorMessage);
	    						}
	    					}
	    					for (int i= 0; i<listTemp.size(); i++) {
		    	            	Map tempMap = (Map)listTemp.get(i);
		    	            	String ErrorMessage = ObjectUtils.toString(tempDataMap.get(ObjectUtils.toString(tempMap.get("BH"))));
		    	            	if (EmptyUtils.isEmpty(ErrorMessage))  {
		    	            		tempMap.put("XXW_SYNC_STATUS", "1");
	    	    	            	tempMap.put("XXW_SYNC_MSG", message);
	    	    	            	gjtChooseManageDao.updClassXxwSyncStatus(tempMap);
		    	            	} else {
	    	    	            	if (ErrorMessage.indexOf("该课程班级已经存在")>-1) {
		    	            			tempMap.put("XXW_SYNC_STATUS", "1");
		    	    	            	tempMap.put("XXW_SYNC_MSG", message);
		    	    	            	gjtChooseManageDao.updClassXxwSyncStatus(tempMap);
		    	            		} else {
		    	            			tempMap.put("XXW_SYNC_STATUS", "2");
		    	    	            	tempMap.put("XXW_SYNC_MSG", message);
		    	    	            	gjtChooseManageDao.updClassXxwSyncStatus(tempMap);
		    	            		}
		    	            	}
	    					}
	    				} else {
	    					for (int i= 0; i<listTemp.size(); i++) {
		    	            	Map tempMap = (Map)listTemp.get(i);
		    	            	tempMap.put("XXW_SYNC_STATUS", "2");
		    	            	tempMap.put("XXW_SYNC_MSG", "国开学习网返回错误信息："+message);
		    	            	gjtChooseManageDao.updClassXxwSyncStatus(tempMap);
		    	            }
	    				}
	        		}
	            }
	            data.removeAll(listTemp);  
	            index++;  
	        }  
	        while (index<=times);  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 同步辅导老师班级数据
	 */
	public Map synchCourseClassTeacher(Map formMap) {
		Map resultMap = new HashMap();
		try {
			List data = gjtChooseManageDao.getNoSyncTchClassList(formMap);
			
			// 选课每次最多只能传20条选课，分段处理
			int perCount=20,index=0;  
	        int times= data.size()/perCount;  
	        do {  
	            List<Map> listTemp=null;  
	            // listTemp是分段处理逻辑的参数  
	            if(data.size()>=perCount){  
	                listTemp=data.subList(0, perCount);
	            } else {  
	                listTemp=data.subList(0, data.size());
	            }
	            Map chooseMap = new HashMap();
	            List chooseList = new ArrayList();
	            for (int i= 0; i<listTemp.size(); i++) {
	            	Map tempMap = (Map)listTemp.get(i);
	            	chooseMap.put(ObjectUtils.toString(tempMap.get("BH")), ObjectUtils.toString(tempMap.get("CLASS_ID")));
	            	Map cMap = new HashMap();
	            	cMap.put("UserName", ObjectUtils.toString(tempMap.get("LOGIN_ACCOUNT")));
	            	cMap.put("CourseClassCode", ObjectUtils.toString(tempMap.get("BH")));
	            	cMap.put("CourseCode", ObjectUtils.toString(tempMap.get("KCH")));
	            	cMap.put("OrgCode", ObjectUtils.toString(formMap.get("VirtualXxzxCode")));
	            	chooseList.add(cMap);
	            }
	            
	            if (EmptyUtils.isNotEmpty(chooseList)) {
	        		Map dataMap = new HashMap();
	        		dataMap.put("operatingUserName", ObjectUtils.toString(formMap.get("operatingUserName")));
	        		dataMap.put("functionType", "synchCourseClassTeacher");
	        		dataMap.put("synchDATA", chooseList);
	        		Map msgMap =apiOucSyncService.addAPPDataSynch(dataMap);
	        		String status = ObjectUtils.toString(msgMap.get("code"));
	        		String message = ObjectUtils.toString(msgMap.get("message"));
	        		// 同步成功
	        		if ("1".equals(status)) {
	        			for (int i= 0; i<listTemp.size(); i++) {
	    	            	Map tempMap = (Map)listTemp.get(i);
	    	            	tempMap.put("XXW_TCH_SYNC_STATUS", "1");
	    	            	tempMap.put("XXW_TCH_SYNC_MSG", message);
	    	            	gjtChooseManageDao.updClassXxwTchSyncStatus(tempMap);
	    	            }
	        		} else {
	        			if (EmptyUtils.isNotEmpty(ObjectUtils.toString(msgMap.get("data")))) {
	        				
	        				List dataList = (List)JSON.parse(ObjectUtils.toString(msgMap.get("data")));
	        				Map tempDataMap = new HashMap();
	    					for (int i=0; i<dataList.size(); i++) {
	    						Map dataMaps = (Map)dataList.get(i);
	    						String bh = ObjectUtils.toString(dataMaps.get("CourseClassCode"));
	    						String ErrorCode = ObjectUtils.toString(dataMaps.get("ErrorCode"));
	    						String ErrorMessage = ObjectUtils.toString(dataMaps.get("ErrorMessage"));
	    						if ("0".equals(ErrorCode)) {
	    							tempDataMap.put(bh, "国开学习网返回错误信息："+ErrorMessage);
	    						}
	    					}
	    					for (int i= 0; i<listTemp.size(); i++) {
		    	            	Map tempMap = (Map)listTemp.get(i);
		    	            	String ErrorMessage = ObjectUtils.toString(tempDataMap.get(ObjectUtils.toString(tempMap.get("BH"))));
		    	            	if (EmptyUtils.isEmpty(ErrorMessage))  {
		    	            		tempMap.put("XXW_TCH_SYNC_STATUS", "1");
	    	    	            	tempMap.put("XXW_TCH_SYNC_MSG", "成功");
	    	    	            	gjtChooseManageDao.updClassXxwTchSyncStatus(tempMap);
		    	            	} else {
		    	            		if (ErrorMessage.indexOf("已经分到此课程班级")>-1) {
		    	            			tempMap.put("XXW_TCH_SYNC_STATUS", "1");
		    	    	            	tempMap.put("XXW_TCH_SYNC_MSG", "成功");
		    	    	            	gjtChooseManageDao.updClassXxwTchSyncStatus(tempMap);
		    	            		} else {
		    	            			tempMap.put("XXW_TCH_SYNC_STATUS", "2");
		    	    	            	tempMap.put("XXW_TCH_SYNC_MSG", ErrorMessage);
		    	    	            	gjtChooseManageDao.updClassXxwTchSyncStatus(tempMap);
		    	            		}
		    	            	}
	    					}
	    				} else {
	    					for (int i= 0; i<listTemp.size(); i++) {
		    	            	Map tempMap = (Map)listTemp.get(i);
		    	            	tempMap.put("XXW_TCH_SYNC_STATUS", "2");
		    	            	tempMap.put("XXW_TCH_SYNC_MSG", "国开学习网返回错误信息："+message);
		    	            	gjtChooseManageDao.updClassXxwTchSyncStatus(tempMap);
		    	            }
	    				}
	        		}
	            }
	            data.removeAll(listTemp);  
	            index++;  
	        }  
	        while (index<=times);  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 同步学员与课程班关系
	 */
	public Map SynchCourseClassStudent(Map formMap) {
		Map resultMap = new HashMap();
		try {
			List data = gjtChooseManageDao.getNoSyncStudClassList(formMap);
			
			// 选课每次最多只能传20条选课，分段处理
			int perCount=20,index=0;  
	        int times= data.size()/perCount;  
	        do {  
	            List<Map> listTemp=null;  
	            // listTemp是分段处理逻辑的参数  
	            if(data.size()>=perCount){
	                listTemp=data.subList(0, perCount);
	            } else {  
	                listTemp=data.subList(0, data.size());
	            }
	            Map chooseMap = new HashMap();
	            List chooseList = new ArrayList();
	            for (int i= 0; i<listTemp.size(); i++) {
	            	Map tempMap = (Map)listTemp.get(i);
	            	chooseMap.put(ObjectUtils.toString(tempMap.get("BH"))+"_"+tempMap.get("XH"), ObjectUtils.toString(tempMap.get("CLASS_STUDENT_ID")));
	            	Map cMap = new HashMap();
	            	cMap.put("UserName", ObjectUtils.toString(tempMap.get("XH")));
	            	cMap.put("CourseClassCode", ObjectUtils.toString(tempMap.get("BH")));
	            	cMap.put("CourseCode", ObjectUtils.toString(tempMap.get("KCH")));
	            	cMap.put("OrgCode", ObjectUtils.toString(formMap.get("OrgCode")));
	            	chooseList.add(cMap);
	            }
	            
	            if (EmptyUtils.isNotEmpty(chooseList)) {
	        		Map dataMap = new HashMap();
	        		dataMap.put("operatingUserName", ObjectUtils.toString(formMap.get("operatingUserName")));
	        		dataMap.put("functionType", "SynchCourseClassStudent");
	        		dataMap.put("synchDATA", chooseList);
	        		Map msgMap =apiOucSyncService.addAPPDataSynch(dataMap);
	        		String status = ObjectUtils.toString(msgMap.get("code"));
	        		String message = ObjectUtils.toString(msgMap.get("message"));
	        		// 同步成功
	        		if ("1".equals(status)) {
	        			for (int i= 0; i<listTemp.size(); i++) {
	    	            	Map tempMap = (Map)listTemp.get(i);
	    	            	tempMap.put("XXW_SYNC_STATUS", "1");
	    	            	tempMap.put("XXW_SYNC_MSG", message);
	    	            	gjtChooseManageDao.updStudClassXxwSyncStatus(tempMap);
	    	            }
	        		} else {
	        			if (EmptyUtils.isNotEmpty(ObjectUtils.toString(msgMap.get("data")))) {
	        				List dataList = (List)JSON.parse(ObjectUtils.toString(msgMap.get("data")));
	    					Map tempDataMap = new HashMap();
	    					for (int i=0; i<dataList.size(); i++) {
	    						Map dataMaps = (Map)dataList.get(i);
	    						String bh = ObjectUtils.toString(dataMaps.get("CourseClassCode"));
	    						String xh = ObjectUtils.toString(dataMaps.get("UserName"));
	    						String ErrorCode = ObjectUtils.toString(dataMaps.get("ErrorCode"));
	    						String ErrorMessage = ObjectUtils.toString(dataMaps.get("ErrorMessage"));
	    						if ("0".equals(ErrorCode)) {
	    							tempDataMap.put(bh+"_"+xh, "国开学习网返回错误信息："+ErrorMessage);
	    						}
	    					}
	    					for (int i= 0; i<listTemp.size(); i++) {
		    	            	Map tempMap = (Map)listTemp.get(i);
		    	            	String ErrorMessage = ObjectUtils.toString(tempDataMap.get(ObjectUtils.toString(tempMap.get("BH"))+"_"+ObjectUtils.toString(tempMap.get("XH"))));
		    	            	if (EmptyUtils.isEmpty(ErrorMessage))  {
		    	            		tempMap.put("XXW_SYNC_STATUS", "1");
	    	    	            	tempMap.put("XXW_SYNC_MSG", "成功");
	    	    	            	gjtChooseManageDao.updStudClassXxwSyncStatus(tempMap);
		    	            	} else {
		    	            		if (ErrorMessage.indexOf("该学生的此课程已经分过班")>-1) {
		    	            			tempMap.put("XXW_SYNC_STATUS", "1");
		    	    	            	tempMap.put("XXW_SYNC_MSG", "成功");
		    	    	            	gjtChooseManageDao.updStudClassXxwSyncStatus(tempMap);
		    	            		} else {
		    	            			tempMap.put("XXW_SYNC_STATUS", "2");
		    	    	            	tempMap.put("XXW_SYNC_MSG", ErrorMessage);
		    	    	            	gjtChooseManageDao.updStudClassXxwSyncStatus(tempMap);
		    	            		}
		    	            	}
	    					}
	    				} else {
	    					for (int i= 0; i<listTemp.size(); i++) {
		    	            	Map tempMap = (Map)listTemp.get(i);
		    	            	tempMap.put("XXW_SYNC_STATUS", "2");
		    	            	tempMap.put("XXW_SYNC_MSG", "国开学习网返回错误信息："+message);
		    	            	gjtChooseManageDao.updStudClassXxwSyncStatus(tempMap);
		    	            }
	    				}
	        		}
	            }
	            data.removeAll(listTemp);  
	            index++;  
	        }  
	        while (index<=times);  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
}
