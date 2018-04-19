package com.gzedu.xlims.common.gzedu;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gzedu.xlims.common.HttpClientUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gzedu.xlims.common.AppConfig;

public class SyncInfoUtil {

	private static final Log log = LogFactory.getLog(SyncInfoUtil.class);

	public static String getStudentProgress(Map formMap, List list) {
		if (list != null && !list.isEmpty()) {
			StringBuffer sb = new StringBuffer();
			sb.append("formMap.TERMCOURSECLASSIDS=");
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				String termCourseId = ObjectUtils.toString(map.get("TEACH_PLAN_ID"));
				String termClassId = ObjectUtils.toString(map.get("CLASS_ID"));
				sb.append(termCourseId + "||" + termClassId);
				if (i < list.size() - 1) {
					sb.append(",");
				}
			}
			sb.append("&formMap.USER_ID=" + ObjectUtils.toString(formMap.get("STUDENT_ID")));
			String params = sb.toString();
			String url = AppConfig.getProperty("xlHost") + "/opi/termcourse/getProgressAct.do";
			String result = HttpClientUtils.PostData(url, params, "utf-8");
			Map resultMap = AnalyXmlUtil.parserXml(result);
			if (resultMap != null && !resultMap.isEmpty()) {
				List resultList = AnalyXmlUtil.getList(resultMap.get("PROGRESS"));
				try {
					Gson gson = new GsonBuilder().create();
					gson.toJson(resultList);
					// return JSONUtil.serialize(resultList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	public static Map getClassStudy(Map formMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("formMap.TERMCOURSE_ID=");
		sb.append(ObjectUtils.toString(formMap.get("TERMCOURSE_ID")));
		sb.append("&formMap.CLASS_ID=");
		sb.append(ObjectUtils.toString(formMap.get("C_CLASS_ID")));
		sb.append("&formMap.XLCLASS_ID=");
		sb.append(ObjectUtils.toString(formMap.get("T_CLASS_ID")));
		String params = sb.toString();
		String url = AppConfig.getProperty("xlHost") + "/opi/termcourse/getClassAct.do";
		String result = HttpClientUtils.PostData(url, params, "utf-8");
		return AnalyXmlUtil.parserXml(result);
	}

	/**
	 * 获取班级学员学勤详细信息
	 * 
	 * @param formMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map getClassStudentStudy(Map formMap) throws UnsupportedEncodingException {
		Map map = new HashMap();
		map.put("formMap.CLASS_ID", formMap.get("CLASS_ID"));
		map.put("formMap.TERMCOURSE_ID", formMap.get("TEACH_PLAN_ID"));
		String url = AppConfig.getProperty("xlHost") + "/opi/student/getStudAnalysis.do";
		String result = HttpClientUtils.doHttpPost(url, map, 3000, "gbk");
		log.info("请求接口返回参数" + result);
		return AnalyXmlUtil.parserXml(result);
	}

	public static List getStudStudy(Map formMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("formMap.TERMCOURSE_ID=");
		sb.append(ObjectUtils.toString(formMap.get("TERMCOURSE_ID")));
		sb.append("&formMap.CLASS_ID=");
		sb.append(ObjectUtils.toString(formMap.get("C_CLASS_ID")));
		sb.append("&formMap.USER_IDS=");
		sb.append(ObjectUtils.toString(formMap.get("USER_ID")));
		String params = sb.toString();
		String url = AppConfig.getProperty("xlHost") + "/opi/termcourse/getUserLearnSituation.do";
		String result = HttpClientUtils.PostData(url, params, "utf-8");
		log.info("请求接口参数" + url + params);
		Map resultMap = AnalyXmlUtil.parserXml(result);
		log.info("请求接口返回参数" + result);
		if (resultMap != null && !resultMap.isEmpty()) {
			return AnalyXmlUtil.getList(resultMap.get("PROGRESS"));
		}
		return null;
	}

	public static List getUserLogin(Map formMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("formMap.TERMCOURSE_ID=");
		sb.append(ObjectUtils.toString(formMap.get("TERMCOURSE_ID")));
		sb.append("&formMap.CLASS_ID=");
		sb.append(ObjectUtils.toString(formMap.get("C_CLASS_ID")));
		sb.append("&formMap.USER_IDS=");
		sb.append(ObjectUtils.toString(formMap.get("USER_ID")));
		String params = sb.toString();
		String url = AppConfig.getProperty("xlHost") + "/opi/termcourse/getUserLogin.do";
		log.info("请求接口参数" + url + params);
		// String url =
		// "http://tcourse.gzedu.com/opi/termcourse/getUserLogin.do";
		String result = HttpClientUtils.PostData(url, params, "utf-8");
		log.info("请求接口返回参数" + result);
		Map resultMap = AnalyXmlUtil.parserXml(result);
		if (resultMap != null && !resultMap.isEmpty()) {
			return AnalyXmlUtil.getList(resultMap.get("USER_LOGIN"));
		}
		return null;
	}

	public static Map getClassLogin(Map formMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("formMap.TERMCOURSE_ID=");
		sb.append(ObjectUtils.toString(formMap.get("TERMCOURSE_ID")));
		sb.append("&formMap.CLASS_ID=");
		sb.append(ObjectUtils.toString(formMap.get("C_CLASS_ID")));
		sb.append("&formMap.XLCLASS_ID=");
		sb.append(ObjectUtils.toString(formMap.get("T_CLASS_ID")));
		String params = sb.toString();
		String url = AppConfig.getProperty("xlHost") + "/opi/termcourse/getClassLogin.do";
		log.error("url=" + url + ",params=" + params);
		String result = HttpClientUtils.PostData(url, params, "utf-8");
		log.error("result=" + result);
		return AnalyXmlUtil.parserXml(result);
	}

	public static List getClassProover(List list) {
		if (list != null && !list.isEmpty()) {
			StringBuffer sb = new StringBuffer();
			sb.append("formMap.TERMCOURSECLASSIDS=");
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				String termCourseId = ObjectUtils.toString(map.get("TEACH_PLAN_ID"));
				String termClassId = ObjectUtils.toString(map.get("CLASS_ID"));
				sb.append(termCourseId + "||" + termClassId);
				if (i < list.size() - 1) {
					sb.append(",");
				}
			}
			String params = sb.toString();
			String url = AppConfig.getProperty("xlHost") + "/opi/termcourse/getClassProover.do";
			String result = HttpClientUtils.PostData(url, params, "utf-8");
			Map resultMap = AnalyXmlUtil.parserXml(result);
			if (resultMap != null && !resultMap.isEmpty()) {
				return AnalyXmlUtil.getList(resultMap.get("PROGRESS"));
			}
		}
		return null;
	}

	public static String syncUpdateStudent(Map beanMap) throws UnsupportedEncodingException {
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<tranceData>");
		xml.append("<APP_ID>APP014</APP_ID>");
		xml.append("<STUDENT>");
		xml.append("<STUD_ID>" + ObjectUtils.toString(beanMap.get("STUDENT_ID") + "</STUD_ID>"));
		xml.append("<LMS_NO>" + ObjectUtils.toString(beanMap.get("ACCOUNT_ID") + "</LMS_NO>"));
		if (!"".equals(ObjectUtils.toString(beanMap.get("XM")))) {
			xml.append("<REALNAME>" + ObjectUtils.toString(beanMap.get("XM")) + "</REALNAME>");
		}
		if (!"".equals(ObjectUtils.toString(beanMap.get("SJH")))) {
			xml.append("<MOBILE_NO>" + ObjectUtils.toString(beanMap.get("SJH") + "</MOBILE_NO>"));
		}
		if (!"".equals(ObjectUtils.toString(beanMap.get("DZXX")))) {
			xml.append("<EMAIL>" + ObjectUtils.toString(beanMap.get("DZXX") + "</EMAIL>"));
		}
		if ("1".equals(ObjectUtils.toString(beanMap.get("SEX")))) {
			xml.append("<SEX>��</SEX>");
		} else if ("2".equals(ObjectUtils.toString(beanMap.get("SEX")))) {
			xml.append("<SEX>Ů</SEX>");
		}
		if (!"".equals(ObjectUtils.toString(beanMap.get("PHOTO")))) {
			xml.append("<IMG_PATH>" + ObjectUtils.toString(beanMap.get("PHOTO") + "</IMG_PATH>"));
		}
		xml.append("<EE_NO>" + ObjectUtils.toString(beanMap.get("LOGIN_ACCOUNT") + "</EE_NO>"));
		xml.append("<CREATED_BY>" + ObjectUtils.toString(beanMap.get("STUDENT_ID") + "</CREATED_BY>"));
		xml.append("</STUDENT>");
		xml.append("</tranceData>");
		String url = AppConfig.getProperty("xlHost") + "/opi/student/synchroStudent.do";
		String resultXml = HttpClientUtils.doHttpPostXml2(url, xml.toString(), 60 * 1000, "gbk");
		log.info(
				"==>ͬ��ѧ����Ϣ[����URL��" + url + ", \n\t����XML��" + xml.toString() + "�� ����XML��" + resultXml + "]");
		System.out.println("===> ͬ��ѧ����Ϣ��ѧϰƽ̨[url:" + url + ", \n\t����XML��" + xml.toString() + " \n\t,����XML��"
				+ resultXml + "]");
		Map map = AnalyXmlUtil.parserXml(resultXml);
		return ObjectUtils.toString(map.get("status"));
	}

	public static String synchTeacherInfo(Map<String, Object> tMap) {
		StringBuffer syInfoBuf = new StringBuffer();
		String synchTead = "<?xml version='1.0' encoding='UTF-8' ?>";
		String fieldFr = "<tranceData><APP_ID>APP014</APP_ID><TEACHER>";
		String fieldLa = "</TEACHER></tranceData>";
		syInfoBuf.append(synchTead);
		syInfoBuf.append(fieldFr);
		syInfoBuf.append("<MANAGER_ID>" + String.valueOf(tMap.get("EMPLOYEE_ID")) + "</MANAGER_ID>");
		syInfoBuf.append("<REALNAME>" + String.valueOf(tMap.get("XM")) + "</REALNAME>");
		syInfoBuf.append("<USERNAME>" + String.valueOf(tMap.get("LOGIN_ACCOUNT")) + "</USERNAME>");
		syInfoBuf.append("<USERPASS>" + String.valueOf(tMap.get("PASSWORD")) + "</USERPASS>");
		syInfoBuf.append("<LMS_NO>" + String.valueOf(tMap.get("ACCOUNT_ID")) + "</LMS_NO>");
		syInfoBuf.append("<MANAGER_ROLE>teacher</MANAGER_ROLE>");
		syInfoBuf.append("<MOBILE_NO>" + String.valueOf(tMap.get("SJH")) + "</MOBILE_NO>");
		syInfoBuf.append("<EMAIL>" + String.valueOf(tMap.get("DZXX")) + "</EMAIL>");
		syInfoBuf.append("<EE_NO>" + String.valueOf(tMap.get("EENO")) + "</EE_NO>");
		syInfoBuf.append("<HEAD_IMG>" + String.valueOf(tMap.get("ZP")) + "</HEAD_IMG>");
		if ("1".equals(String.valueOf(tMap.get("XBM")))) {
			syInfoBuf.append("<SEX>男</SEX>");
		} else if ("2".equals(String.valueOf(tMap.get("XBM")))) {
			syInfoBuf.append("<SEX>女</SEX>");
		}
		syInfoBuf.append("<CREATED_BY>" + String.valueOf(tMap.get("ACCOUNT_ID")) + "</CREATED_BY>");
		syInfoBuf.append(fieldLa);
		String url = AppConfig.getProperty("xlHost") + "/opi/teacher/synchroTeacher.do";
		String resultXml = HttpClientUtils.doHttpPostXml2(url, syInfoBuf.toString(), 60 * 1000, "gbk");
		Map map = AnalyXmlUtil.parserXml(resultXml);

		log.info("==>ͬ��ѧ����Ϣ[����URL��" + url + ", \n\t����XML��" + syInfoBuf.toString() + "�� ����XML��" + resultXml
				+ "]");
		System.out.println("===> ͬ����ʦ��Ϣ��ѧϰƽ̨[url:" + url + ", \n\t����XML��" + syInfoBuf.toString()
				+ " \n\t,����XML��" + resultXml + "]");

		return String.valueOf(map.get("status"));
	}

	/*
	 * public static void main(String []a)throws Exception{ String xml =
	 * "<?xml version='1.0' encoding='UTF-8'?><tranceData><APP_ID>APP001</APP_ID><TEACHER><MANAGER_ID>7510b4864a1d467abb90136a59cca0aa</MANAGER_ID><REALNAME>����</REALNAME><LMS_NO>af7147cdc2594965abc2965f94c1be25</LMS_NO><MANAGER_ROLE>teacher</MANAGER_ROLE><MOBILE_NO>13560381698</MOBILE_NO><EMAIL>wangguihua@gzedu.net</EMAIL><EE_NO>wangguihua</EE_NO><IMG_PATH></IMG_PATH><SEX>Ů</SEX><CREATED_BY>af7147cdc2594965abc2965f94c1be25</CREATED_BY></TEACHER></tranceData>"
	 * ; String url =
	 * "http://xlbooks.tt.gzedu.com/opi/teacher/synchroTeacher.do"; //String
	 * result = HttpUtil.doHttpPostXml2(url, xml, 60*1000, "gbk"); String result
	 * =
	 * "<?xml version='1.0' encoding='UTF-8'?><result><response><sms>14</sms><mms>0</mms><sum>1.26</sum><products>14</products><frozen>0</frozen></response></result>"
	 * ; Map map = AnalyXmlUtil.parserXml(result); System.out.println(map);
	 * System.out.println(ObjectUtils.toString(map.get("response")));
	 * System.out.println(ObjectUtils.toString(((Map)map.get("response")).get(
	 * "sms"))); }
	 */
	/*
	 * public static void main(String[] a) throws
	 * JSONException,UnsupportedEncodingException{ String url =
	 * "http://xlbooks.tt.gzedu.com/opi/student/synchroStudent.do"; String name
	 * = "ССС�˳�"; String sex = "��"; String xml =
	 * "<?xml version='1.0' encoding='UTF-8'?><tranceData><APP_ID>APP001</APP_ID><STUDENT><STUD_ID>4e7ed1167f00000122d555b2a0d3295e</STUD_ID><LMS_NO>4e17196c7f00000122d555b2e57dd64d</LMS_NO><REALNAME>"
	 * +name+"</REALNAME><EMAIL>fere@126.com</EMAIL><SEX>"+sex+
	 * "</SEX><CREATED_BY>4e7ed1167f00000122d555b2a0d3295e</CREATED_BY></STUDENT></tranceData>";
	 * String result = HttpUtil.doHttpPostXml2(url,xml.toString(),60*1000,
	 * "gbk"); System.out.println(result); }
	 */
}