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

import net.sf.jxls.transformer.XLSTransformer;

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
import com.gzedu.xlims.dao.edumanage.GjtChooseManageDao;
import com.gzedu.xlims.dao.edumanage.GjtTeachCourseDao;
import com.gzedu.xlims.service.api.ApiOucSyncService;
import com.gzedu.xlims.service.edumanage.GjtTeachCourseService;

/**
 * 任教管理
 */
@Service
public class GjtTeachCourseServiceImpl implements GjtTeachCourseService {
	
	@Autowired
	GjtTeachCourseDao gjtTeachCourseDao;
	
	@Autowired
	GjtChooseManageDao gjtChooseManageDao;
	
	@Autowired
	ApiOucSyncService apiOucSyncService;
	
	/**
	 * 任教管理列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page getTeachCourseList(Map searchParams,PageRequest pageRequst) {
		return gjtTeachCourseDao.getTeachCourseList(searchParams, pageRequst);
	}
	
	/**
	 * 任教管理列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public List getTeachCourseList(Map searchParams) {
		return gjtTeachCourseDao.getTeachCourseList(searchParams);
	}
	
	/**
	 * 任教管理列表（统计）
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int getTeachCourseCount(Map searchParams) {
		int num = 0;
		List chooseList = gjtTeachCourseDao.getTeachCourseCount(searchParams);
		if (EmptyUtils.isNotEmpty(chooseList)) {
			Map chooseMap = (Map)chooseList.get(0);
			num = Integer.parseInt(ObjectUtils.toString(chooseMap.get("STATE_COUNT")));
		}
		return num;
	}
	
	/**
	 * 删除任教信息
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int delTeacherInfo(Map searchParams) {
		return gjtTeachCourseDao.delTeacherInfo(searchParams);
	}
	
	/**
	 * 更新任教信息
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int uptTeacherInfo(Map searchParams) {
		return gjtTeachCourseDao.uptTeacherInfo(searchParams);
	}
	
	/**
	 * 新增选课
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int addCourseTeacher(Map searchParams) {
		int num = 0;
		try{
			String[] ids = (String[]) searchParams.get("COURSE_IDS");
			for(int i = 0; i < ids.length;i++){
				searchParams.put("COURSE_ID", ids[i]);
				if(this.getTeachCourseCount(searchParams) == 0){
					searchParams.put("COURSE_TEACHER_ID", SequenceUUID.getSequence());
					num = num + gjtTeachCourseDao.addCourseTeacher(searchParams);
				}
			}
		}catch(Exception e){
			searchParams.put("COURSE_ID", searchParams.get("COURSE_IDS"));
			if(this.getTeachCourseCount(searchParams) == 0){
				searchParams.put("COURSE_TEACHER_ID", SequenceUUID.getSequence());
				num = num + gjtTeachCourseDao.addCourseTeacher(searchParams);
			}
		}
		return num;
	}
	

	@Override
	public Page getTeacherList(Map searchParams, PageRequest pageRequst) {
		return gjtTeachCourseDao.getTeacherList(searchParams, pageRequst);
	}
	
	/**
	 * 同步任教信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map getSyncResult(Map searchParams, String basePath) {
		Map resultMap = new HashMap();
		List rightList = new ArrayList();
		List errorList = new ArrayList();
		String errorMessage = "";
		try {
			searchParams.put("IS_SYNCHRO", "N");
			List data = gjtTeachCourseDao.getTeachCourseList(searchParams);
			
			// 选课每次最多只能传20条选课，分段处理
			int perCount=20,index=0;
	        int times=data.size()/perCount;
	        do {
	            List<Map> listTemp=null;
	            // listTemp是分段处理逻辑的参数 
	            if(data.size()>=perCount){
	                listTemp=data.subList(0, perCount);
	            } else {
	                listTemp=data.subList(0, data.size());
	            }
	            Map userMap = new HashMap();
	            List userList = new ArrayList();
	            for (int i= 0; i<listTemp.size(); i++) {
	            	Map tempMap = (Map)listTemp.get(i);
	                
	            	Map studMap = new HashMap();
	            	userMap.put(ObjectUtils.toString(tempMap.get("ZGH"))+"_"+ObjectUtils.toString(tempMap.get("KCH")), ObjectUtils.toString(tempMap.get("COURSE_TEACHER_ID")));
	            	studMap.put("UserName", ObjectUtils.toString(tempMap.get("ZGH")));
	            	studMap.put("CourseCode", ObjectUtils.toString(tempMap.get("KCH")));
	            	studMap.put("TeacherType", ObjectUtils.toString(tempMap.get("TEACHER_TYPE")));
	            	studMap.put("OrgCode", ObjectUtils.toString(searchParams.get("OrgCode")));
	            	userList.add(studMap);
	            }
	            	
	            if (EmptyUtils.isNotEmpty(userList)) {
	        		Map dataMap = new HashMap();
	        		dataMap.put("operatingUserName", ObjectUtils.toString(searchParams.get("operatingUserName")));
	        		dataMap.put("functionType", "SynchTeacherCourse");
	        		dataMap.put("synchDATA", userList);
	        		Map msgMap =apiOucSyncService.addAPPDataSynch(dataMap);
	        		String status = ObjectUtils.toString(msgMap.get("code"));
	        		String message = ObjectUtils.toString(msgMap.get("message"));
	        		if ("1".equals(status)) {
	        			for (int i= 0; i<listTemp.size(); i++) {
	    	            	Map tempMap = (Map)listTemp.get(i);
	    	            	tempMap.put("IS_SYNCHRO", "Y");
							gjtTeachCourseDao.uptTeacherInfo(tempMap);
							String TeacherType = ObjectUtils.toString(tempMap.get("TEACHER_TYPE"));
							tempMap.put("TEACHER_TYPE", TeacherType.equals("3")?"辅导老师":TeacherType.equals("2")?"责任老师":"主持老师");
	    	            	tempMap.put("OrgCode", searchParams.get("OrgCode"));
	    	            	rightList.add(tempMap);
	    	            }
	        		} else {
	        			List dataList = (List)JSON.parse(ObjectUtils.toString(msgMap.get("data")));
	        			if (EmptyUtils.isNotEmpty(dataList) && dataList.size() > 0) {
	    					for (int i=0;i<dataList.size(); i++) {
	    						Map dataMaps = (Map)dataList.get(i);
	    						String UserName = ObjectUtils.toString(dataMaps.get("UserName"));
	    						String CourseCode = ObjectUtils.toString(dataMaps.get("CourseCode"));
	    						String TeacherType = ObjectUtils.toString(dataMaps.get("TeacherType"));
	    						String ErrorCcode = ObjectUtils.toString(dataMaps.get("ErrorCode"));
	    						String ErrorMessage = ObjectUtils.toString(dataMaps.get("ErrorMessage"));
	    						String id = ObjectUtils.toString(userMap.get(UserName+"_"+CourseCode));
	    						Map tempMap = new HashMap();
	    						tempMap.put("COURSE_TEACHER_ID", id);
	    						if ("0".equals(ErrorCcode)) {
	    							if(ErrorMessage.indexOf("该任教关系已设置") > -1){
	    								tempMap.put("IS_SYNCHRO", "Y");
		    							gjtTeachCourseDao.uptTeacherInfo(tempMap);
		    							dataMaps.put("ZGH", UserName);
		    							dataMaps.put("KCH", CourseCode);
		    							dataMaps.put("TEACHER_TYPE", TeacherType.equals("3")?"辅导老师":TeacherType.equals("2")?"责任老师":"主持老师");
		    							dataMaps.put("OrgCode", searchParams.get("OrgCode"));
		    							dataMaps.put("XXW_SYNC_MSG", ErrorMessage);
		    	    	            	dataMaps.putAll(tempMap);
		    	    	            	rightList.add(dataMaps);
	    							}else{
	    								dataMaps.put("ZGH", UserName);
		    							dataMaps.put("KCH", CourseCode);
		    							dataMaps.put("TEACHER_TYPE", TeacherType.equals("3")?"辅导老师":TeacherType.equals("2")?"责任老师":"主持老师");
		    							dataMaps.put("OrgCode", searchParams.get("OrgCode"));
		    							dataMaps.put("XXW_SYNC_MSG", ErrorMessage);
		    	    	            	errorList.add(dataMaps);
	    							}
	    						} else {
	    							tempMap.put("IS_SYNCHRO", "Y");
	    							gjtTeachCourseDao.uptTeacherInfo(tempMap);
	    							dataMaps.put("ZGH", UserName);
	    							dataMaps.put("KCH", CourseCode);
	    							dataMaps.put("TEACHER_TYPE", TeacherType.equals("3")?"辅导老师":TeacherType.equals("2")?"责任老师":"主持老师");
	    							dataMaps.put("OrgCode", searchParams.get("OrgCode"));
	    							dataMaps.put("XXW_SYNC_MSG", ErrorMessage);
	    	    	            	dataMaps.putAll(tempMap);
	    	    	            	rightList.add(dataMaps);
	    						}
	    					}
	    				}else{
	    					for (int i= 0; i<listTemp.size(); i++) {
		    	            	Map tempMap = (Map)listTemp.get(i);
		    	            	tempMap.put("XXW_SYNC_MSG", message);
		    	            	tempMap.put("OrgCode", searchParams.get("OrgCode"));
		    	            	errorList.add(tempMap);
		    	            }
	    				}
	        		}
	            }
	            data.removeAll(listTemp);
	            index++;
	        }
	        while (index<=times);
	        
	        String timeStr = ObjectUtils.toString(new Date().getTime());
			String root = basePath;
			
			File folder=new File(root+"/tmp");
			if(!folder.exists()){
				folder.mkdirs();
			}
			
			// 导出成功记录
			String tempPath = GjtTeachCourseServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/choose/任教信息同步成功表.xls";
			String downPath = "/tmp/任教信息同步成功表"+timeStr+".xls";
			Map beans = new HashMap();  
		    beans.put("dataList", rightList);
		    XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS(tempPath, beans, downPath);
            resultMap.put("RIGHT_DOWNPATH", downPath);
			
            // 导出失败记录
            tempPath = GjtTeachCourseServiceImpl.class.getClassLoader().getResource("").getPath()+"/excel/choose/任教信息同步失败表.xls";
			downPath = "/tmp/任教信息同步失败表"+timeStr+".xls";
            beans = new HashMap();
            beans.put("dataList", errorList);
            transformer = new XLSTransformer();
            transformer.transformXLS(tempPath, beans, downPath);
            resultMap.put("ERROR_DOWNPATH", downPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("errorMessage", errorMessage);
		resultMap.put("errorNum", errorList.size());
		resultMap.put("rightNum", rightList.size());
		resultMap.put("allNum", errorList.size()+rightList.size());
		return resultMap;
	}

	@Override
	public int updClassXxwSyncStatus(Map searchParams) {
		return gjtChooseManageDao.updClassXxwSyncStatus(searchParams);
	}
	
	@Override
	public int updStudClassXxwSyncStatus(Map searchParams) {
		return gjtChooseManageDao.updStudClassXxwSyncStatus(searchParams);
	}

	@Override
	public List getClassInfo(Map searchParams) {
		return gjtTeachCourseDao.getClassInfoById(searchParams);
	}

	@Override
	public int updClassStudent(Map searchParams) {
		return gjtTeachCourseDao.updClassStudent(searchParams);
	}
	
	@Override
	public List getClassStudent(Map searchParams) {
		return gjtTeachCourseDao.getClassStudent(searchParams);
	}
	
	/**
	 * 导出选课记录
	 */
	/*public String expRecordRecResult(Map formMap) {
		//csv表头
		String[] header = new String[] {"姓名","学号","身份证","手机号","专业层次", "入学学期", "报读专业",
				"开课学期","课程代码","课程名称","选课类型","课程班级","辅导老师","同步状态"};
		
		//下面 data里的key，可以说是数据库字段了
		String[] key = new String[] {"XM","XH","SFZH","SJH","PYCC_NAME","GRADE_NAME","ZYMC",
				"OPEN_GRADE_NAME","KCH","KCMC","REBUILD_STATE","BJMC","TEACH_NAME","SYNC_STATUS"};
		try {
			List data = gjtTeachCourseDao.getChooseManageList(formMap);
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
	}*/
}
