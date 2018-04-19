package com.ouchgzee.study.serviceImpl.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.gzedu.AnalyXmlUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;

@Service
public class CourseRemoteService {

	private static final Log log = LogFactory.getLog(CourseRemoteService.class);
	private static final String API_TERMCOURSE_SCORE = "api.termcourse.score";
	private static final String API_TERMCOURSE_STUDY_INFO = "api.termcourse.studyinfo";
	private static final String API_EXAM_PROJECT_INFO = "/exam/examapp/getProjectTestInfo.do";
	private static final String API_TERMCOURSE_ONLINE = "/opi/termcourse/getOnlineLearningCount.do";
	private static final String API_CHOOSEIDS_DYNA = "/opi/termcourse/getCourseScoreByNotExam.do";

	private String getRemoteUrl() {
		String url = AppConfig.getProperty("xlHost") + AppConfig.getProperty(API_TERMCOURSE_SCORE);
		return url;
	}
	
	/**
	 * 获取学员在线学习课程
	 * @param formMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getOnlineLearningCount(Map formMap){
		List list = new ArrayList();
		if(EmptyUtils.isEmpty(formMap)){
			return list;
		}
		
		String url = AppConfig.getProperty("oclassHost")+API_TERMCOURSE_ONLINE;
		//url = "http://oclass.dev.oucnet.cn:8080/opi/termcourse/getOnlineLearningCount.do";
		
		Map params = new HashMap();
		params.put("formMap.TERMCOURSE_IDS", formMap.get("TERMCOURSE_IDS"));
		params.put("formMap.CLASS_IDS", formMap.get("CLASS_IDS"));
		String rspXML = HttpClientUtils.doHttpGet(url, params, 3000, "UTF-8");
		log.info("获取学员在线学习课程[url:" + url + ", params: " + params + "]， 返回值为：" + rspXML);
		Map map = new HashMap();
		if(StringUtils.isNotBlank(rspXML) &&!"error".equals(rspXML)){
			try{
				Map<String,Object> tempMap = new Gson().fromJson(rspXML, new TypeToken<HashMap<String,Object>>(){}.getType());
				map.put("TEACH_LIST", tempMap.get("TEACH_LIST"));
				list.add(map);
			}catch(Exception e){
				log.error("===> 调用接口出错[获取学员在线学习课程]\n" + rspXML);
				e.printStackTrace();
			}
		}
		return list;
		
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getProjectTestInfo(Map<String, Object> searchParams){
		String examNo = ObjectUtils.toString(searchParams.get("EXAM_NO"));
		String ksfs = ObjectUtils.toString(searchParams.get("KSFS_FLAG"));
		
		List list = new ArrayList();
		
		if(EmptyUtils.isEmpty(examNo)){
			return list;
		}
		String url = AppConfig.getProperty("examServer")+API_EXAM_PROJECT_INFO;
		if("19".equals(ksfs.trim())){
			url = AppConfig.getProperty("examOucnet")+API_EXAM_PROJECT_INFO;
		}
		//url = "http://localhost:9530/base_exam/exam/examapp/getProjectTestInfo.do";
		
		Map params = new HashMap();
		params.put("formMap.EXAM_NO", examNo);
		params.put("formMap.EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		params.put("formMap.USER_NO", ObjectUtils.toString(searchParams.get("USER_NO")));
		String rspXML = HttpClientUtils.doHttpGet(url, params, 3000, "UTF-8");
		log.info("读取考试科目信息[url:" + url + ", params: " + params + "]， 返回值为：" + rspXML);
		Map map = new HashMap();
		if(StringUtils.isNotBlank(rspXML) && !"error".equals(rspXML)){
			try{
			    Map<String,Object> tempMap = new Gson().fromJson(rspXML, new TypeToken<HashMap<String,Object>>(){}.getType());
				map.put("TEST_TIME", ObjectUtils.toString(tempMap.get("TEST_TIME")));
				map.put("QUALIFIED_POINT", ObjectUtils.toString(tempMap.get("QUALIFIED_POINT")));
				map.put("TEST_POINT",ObjectUtils.toString(tempMap.get("TEST_POINT")));
				map.put("TEST_COUNT", ObjectUtils.toString(tempMap.get("TEST_COUNT")));
				list.add(map);
				log.info(tempMap);
			}catch(Exception e){
				log.error("===> 调用接口出错[读取考试科目信息]\n" + rspXML);
				e.printStackTrace();
			}
		}
		return list;
	} 

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getCourseScore(List chooseIds) {
		List list = null;
		if (EmptyUtils.isEmpty(chooseIds)) {
			return list;
		}
		String url = getRemoteUrl();
		String chooseIdsStr = StringUtils.join(chooseIds, ",");

		Map params = new HashMap();
		params.put("formMap.CHOOSE_IDS", chooseIdsStr);

		String rspXML = HttpClientUtils.doHttpGet(url, params, 3000, "UTF-8");
		log.info("获取学员课程成绩[url:" + url + ", params: " + params + "]， 返回值为：" + rspXML);
		if (StringUtils.isNotBlank(rspXML) && !"error".equals(rspXML)) {
			try {
				Map rsp = AnalyXmlUtil.parserXml(rspXML);
				Object score = rsp.get("SCORE");
				if (score instanceof List) {
					list = (List) rsp.get("SCORE");
				} else if (score instanceof Map) {
					list = new ArrayList();
					list.add(score);
				}
				log.info(rsp);
			} catch (Exception e) {
				log.error("===> 调用接口出错[读取学习进度]\n" + rspXML);
				e.printStackTrace();
			}
		}

		return list;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getCourseScore(String chooseId) {
		String url = getRemoteUrl();

		Map params = new HashMap();
		params.put("formMap.CHOOSE_IDS", chooseId);

		String rspXML = HttpClientUtils.doHttpGet(url, params, 3000, "UTF-8");
		log.info("获取学员课程成绩[url:" + url + ", params: " + params + "]， 返回值为：" + rspXML);
		Map map = null;
		if (StringUtils.isNotBlank(rspXML) && !"error".equals(rspXML)) {
			try {
				Map rsp = AnalyXmlUtil.parserXml(rspXML);
				map = (Map) rsp.get("SCORE");
				log.info(rsp);
			} catch (Exception e) {
				log.error("===> 调用接口出错[读取学习进度]\n" + rspXML);
				e.printStackTrace();
			}
		}

		return map;
	}

	/**
	 * 查询课程班级学员学习情况。
	 * 
	 * @param teachPlanId
	 * @param recId
	 * @param courseClassId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> getStudentStudySituation(Map recId) {
		String url = AppConfig.getProperty("xlHost") + "/opi/termcourse/getUserDynaByChooseId.do";
		StringBuffer sb = new StringBuffer();
		String params = AnalyXmlUtil.createXml(recId);
		String rspXML = HttpClientUtils.doHttpPostXml2(url, params, 3000, "GBK");
		log.info("获取登录时长和登录次数[url:" + url + ", params: " + params + "]， 返回值为：" + rspXML);
		Map<String, Object> map = null;
		if (StringUtils.isNotBlank(rspXML) && !"error".equals(rspXML)) {
			try {
				map = AnalyXmlUtil.parserXml(rspXML);
				log.info(map);
			} catch (Exception e) {
				log.error("===> 调用接口出错[获取登录时长和登录次数]\n" + rspXML);
				e.printStackTrace();
			}
		}

		return map;

	}

	public Double courseRemoteService(String string) {
		String url = AppConfig.getProperty("examServer") + "/exam/getPointByChooseIds.do";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("CHOOSE_IDS", string);
		String get = HttpClientUtils.doHttpGet(url, map, 3000, "GBK");
		Map xml = AnalyXmlUtil.parserXml(get);
		String point = "";
		Object CHOOSELISTObj = xml.get("CHOOSELIST");
		if (CHOOSELISTObj != null) {
			Map CHOOSELIST = (Map) CHOOSELISTObj;
			point = (String) CHOOSELIST.get("POINT");
		}
		if (StringUtils.isEmpty(point) || !NumberUtils.isNumber(point)) {
			return null;
		}
		return Double.parseDouble(point);
	}


	/**
	 * 无考试模式(课程学习成绩)
	 * @param chooseIds
	 * @return
	 */
	public List getCourseScoreByNotExam(List chooseIds){
		List list = new ArrayList();
		if (EmptyUtils.isEmpty(chooseIds)){
			return list;
		}

		String url = AppConfig.getProperty("oclassHost")+API_CHOOSEIDS_DYNA;
		//url = "http://oclass.dev.oucnet.cn:8080/opi/termcourse/getCourseScoreByNotExam.do";

		String chooseIdsStr = StringUtils.join(chooseIds, ",");
		Map params = new HashMap();
		params.put("formMap.CHOOSE_IDS",chooseIdsStr);

		String rspXML = HttpClientUtils.doHttpGet(url, params, 3000, "UTF-8");
		log.info("获取学员在线学习课程[url:" + url + ", params: " + params + "]， 返回值为：" + rspXML);
		Map map = new HashMap();
		if(StringUtils.isNotBlank(rspXML) &&!"error".equals(rspXML)){
			try{
				Map<String,Object> tempMap = new Gson().fromJson(rspXML, new TypeToken<HashMap<String,Object>>(){}.getType());
				map.put("CHOOSE_LIST", tempMap.get("CHOOSE_LIST"));
				list.add(map);
			}catch(Exception e){
				log.error("===> 调用接口出错[获取学员在线学习课程]\n" + rspXML);
				e.printStackTrace();
			}
		}
		return list;
	}
	

	public static void main(String[] args) {
		String url = "http://exam.chinaeenet.com/exam/getPointByChooseIds.do";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("CHOOSE_IDS", "eb5ad33f7f00000122d555b2d881b163");
		String get = HttpClientUtils.doHttpGet(url, map, 3000, "GBK");
		Map xml = AnalyXmlUtil.parserXml(get);
		String point = "";
		Object CHOOSELISTObj = xml.get("CHOOSELIST");
		if (CHOOSELISTObj != null) {
			Map CHOOSELIST = (Map) CHOOSELISTObj;
			point = (String) CHOOSELIST.get("POINT");
			System.out.println(point);
		}
	}

}
