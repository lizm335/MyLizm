/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.exam.wk;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.dao.exam.GjtExamAppointmentNewDao;
import com.gzedu.xlims.dao.exam.GjtExamBatchNewDao;
import com.gzedu.xlims.dao.exam.GjtExamRoomNewDao;
import com.gzedu.xlims.dao.exam.GjtExamRoundNewDao;
import com.gzedu.xlims.dao.exam.GjtExamStudentRoomNewDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.service.exam.GjtExamAppointmentNewService;
import com.gzedu.xlims.serviceImpl.exam.ExportExamStudentRoomService;
import com.gzedu.xlims.serviceImpl.exam.wk.dto.ExamProjectTeam;
import com.gzedu.xlims.serviceImpl.exam.wk.dto.ExamRoom;
import com.gzedu.xlims.serviceImpl.exam.wk.dto.ExamRoomDTO;
import com.gzedu.xlims.serviceImpl.exam.wk.dto.ExamRoomDistribute;
import com.gzedu.xlims.serviceImpl.exam.wk.dto.ExamStudentChoose;
import com.gzedu.xlims.serviceImpl.exam.wk.dto.ExamStudentInfo;
import com.gzedu.xlims.serviceImpl.exam.wk.dto.ExamStudentInfoDTO;
import com.gzedu.xlims.serviceImpl.exam.wk.dto.ExamTeam;
import com.gzedu.xlims.serviceImpl.exam.wk.dto.ExamTeamDTO;
import com.thoughtworks.xstream.XStream;

import net.sf.json.JSONObject;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年12月13日
 * @version 2.5
 *
 */
@Service
public class GjtExamWkService {

	@Autowired
	GjtExamRoomNewDao gjtExamRoomNewDao;
	@Autowired
	ExportExamStudentRoomService exportExamStudentRoomService;

	@Autowired
	GjtExamAppointmentNewDao gjtExamAppointmentNewDao;
	@Autowired
	GjtExamRoundNewDao gjtExamRoundNewDao;

	@Autowired
	GjtExamAppointmentNewService gjtExamAppointmentNewService;
	@Autowired
	GjtExamStudentRoomNewDao gjtExamStudentRoomNewDao;
	@Autowired
	GjtStudentInfoDao gjtStudentInfoDao;
	@Autowired
	GjtExamBatchNewDao gjtExamBatchNewDao;

	//private static final String examServer = "http://exam.chinaeenet.com";
	
	//private static final String examServer = "http://localhost:9530/base_exam";
	
	private String getRemoteUrl(){
		String url = AppConfig.getProperty("examServer");
		return url;
	}
	
	
	

	public void initWk(String examNo, String batchCode, String userId, String userName, String chooseId)
			throws RuntimeException {
		// 第一步，获得网考资源ID
		String projectId = wk1(examNo);
		if (projectId == "") {
			throw new RuntimeException("同步失败:没有网考资源;examNo:" + examNo);
		}
		// 第二步，同步考试安排
		wk2(batchCode, projectId);
		// 第三步，同步考场、
		wk3(batchCode, projectId);
		// 第四步，同步学员和报考信息
		wk4(batchCode, projectId, userId, userName, chooseId);
	}

	// 第一步，获得网考资源ID
	/**
	 * 
	 * @param examNo
	 *            考试号
	 * @return 考试资源ＩＤ
	 */
	public String wk1(String examNo) {
		// 第一步，获得网考资源ID
		String url = getRemoteUrl() + "/exam/getProjectInfo.do";
		Map<String, String> m = new HashMap<String, String>();
		m.put("formMap.EXAM_NO", examNo);
		String result = HttpClientUtils.doHttpPost(url, m, 60 * 1000, "UTF-8");
		System.out.println(result);
		if (result != "") {
			// RESULT = 0 返回成功，PROJECT_ID：返回成功时数据
			Map<String, Object> jsonToMap = jsonToMap(result);
			int code = Integer.valueOf(jsonToMap.get("RESULT").toString());
			if (code == 0) {
				Object projectId = jsonToMap.get("PROJECT_ID");
				return projectId.toString();
			}
		}
		return "";
	}

	public void wk2(String batchCode, String projectId) throws RuntimeException {
		// String projectId = "2b56535bba0840128d32c5d9fc2ca386";
		// 第二步，同步考试安排
		String appId = "APP014";
		ExamTeam examTeam = new ExamTeam(batchCode, batchCode);
		ExamProjectTeam examProjectTeam = new ExamProjectTeam(batchCode + projectId, projectId, batchCode);
		ExamTeamDTO data = new ExamTeamDTO(appId, examTeam, examProjectTeam);

		XStream tranceData = new XStream();
		tranceData.alias("tranceData", ExamTeamDTO.class);
		String xmlString = tranceData.toXML(data).replace("__", "_");
		System.out.println(xmlString);

		String url = getRemoteUrl() + "/exam/synProjectAndTerm.do";
		System.out.println(url);
		String result = HttpClientUtils.doHttpPostXml2(url, xmlString, 60 * 1000, "UTF-8");
		System.out.println("result:" + result);
		if (!"1".equals(result)) {
			System.out.println("同步失败");
			throw new RuntimeException("同步失败:同步考试期和考试安排");
		} else {
			System.out.println("同步成功");
		}
	}

	/**
	 * 同步考场
	 * 
	 * @param batchCode
	 *            批次ｃｏｄｅ
	 * @param projectId
	 *            考试资源ＩＤ
	 */
	public void wk3(String batchCode, String projectId) throws RuntimeException {
		// 第三步，同步考场、
		String distributeId = batchCode + projectId;

		ExamRoom examRoom = new ExamRoom(batchCode, batchCode);
		ExamRoomDistribute examRoomDistribute = new ExamRoomDistribute(distributeId, batchCode, distributeId);
		ExamRoomDTO data = new ExamRoomDTO(projectId, examRoom, examRoomDistribute);

		XStream tranceData = new XStream();
		tranceData.alias("tranceData", ExamRoomDTO.class);
		String xmlString = tranceData.toXML(data).replace("__", "_");
		System.out.println(xmlString);

		String url = getRemoteUrl() + "/exam/synExamRoomAndExamRoomdistribute.do";
		System.out.println(url);
		String result = HttpClientUtils.doHttpPostXml2(url, xmlString, 60 * 1000, "UTF-8");
		System.out.println("result:" + result);
		if (!"1".equals(result)) {
			System.out.println("同步失败");
			throw new RuntimeException("同步失败:同步考试期和考试安排");
		} else {
			System.out.println("同步成功");
		}
	}

	public void wk4(String batchCode, String projectId, String userId, String userName, String chooseId)
			throws RuntimeException {
		// 第四步，同步学员和报考信息
		String appId = "APP014";

		/*
		 * String userId = "112233"; String userName = "测试明"; String chooseId =
		 * "chooseId001";
		 */
		String distributeId = batchCode + projectId;

		ExamStudentInfo examStudentInfo = new ExamStudentInfo(userId, userName);
		ExamStudentChoose examStudentChoose = new ExamStudentChoose(chooseId, userId, distributeId, distributeId);
		ExamStudentInfoDTO data = new ExamStudentInfoDTO(appId, examStudentInfo, examStudentChoose);

		XStream tranceData = new XStream();
		tranceData.alias("tranceData", ExamStudentInfoDTO.class);
		String xmlString = tranceData.toXML(data).replace("__", "_");
		System.out.println(xmlString);

		String url = getRemoteUrl() + "/exam/synExamUserInfoAndExamUserChoose.do";
		System.out.println(url);
		String result = HttpClientUtils.doHttpPostXml2(url, xmlString, 60 * 1000, "UTF-8");
		System.out.println("result:" + result);
		if (!"1".equals(result)) {
			System.out.println("同步失败");
			throw new RuntimeException("同步失败:同步考试期和考试安排");
		} else {
			System.out.println("同步成功");
		}
	}

	// http://172.16.165.162:8081/exam/examIndex/login.do?formMap.CHOOSE_ID=chooseId001&formMap.BACK_URI=

	/**
	 * json string 转换为 map 对象
	 * 
	 * @param jsonObj
	 * @return
	 */
	public static Map<String, Object> jsonToMap(Object jsonObj) {
		JSONObject jsonObject = JSONObject.fromObject(jsonObj);
		Map<String, Object> map = (Map) jsonObject;
		return map;
	}

	/**
	 * json string 转换为 对象
	 * 
	 * @param jsonObj
	 * @param type
	 * @return
	 */
	public static <T> T jsonToBean(Object jsonObj, Class<T> type) {
		JSONObject jsonObject = JSONObject.fromObject(jsonObj);
		T obj = (T) JSONObject.toBean(jsonObject, type);
		return obj;
	}
}
