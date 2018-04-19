/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common.web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.gzedu.SSOUtil;
import com.gzedu.xlims.common.json.JsonUtils;

/**
 * 功能说明：生成各个平台的模拟登陆地址类
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年6月23日
 * @version 2.5
 *
 */
@Deprecated
public class SimulationUtils {
	
	private static Logger log = LoggerFactory.getLogger(SimulationUtils.class);
	// 域名映射json文件
	private static String MAPPING_URL_FILE = "domain-mapping.json";
	// 域名匹配正则表达式
	private static String DOMAIN_REGEX = "^(?:http:+)?/*((?:\\w+\\.?)+)(?:\\:\\d{1,4})?/*(?:\\w+/*\\.?\\w*)*";

	/**
	 * 学员模拟登陆URL生产
	 * 
	 * @param studentId
	 * @param xh
	 * @return
	 */
	public String studentSimulation(String studentId, String xh,String requestUrl) {
		String p = SSOUtil.getSignOnParam(xh);
		String confServerUrl = AppConfig.getProperty("pcenterServer");
		String url = getRedirectDomain(requestUrl, confServerUrl) + AppConfig.getProperty("sso.login.url");
		url += "?p=" + p + "&pdmn=pd&s=" + studentId;
		return url;
	}

	/**
	 * 辅导老师模拟登陆
	 * 
	 * @param employeeId
	 * @return
	 */
	public String coachTeacherSimulation(String employeeId) {
		String teacherId = EncryptUtils.encrypt(employeeId);
		String url = AppConfig.getProperty("oclassHost") + "/book/index/teacher/urlLogin.do?formMap.USER_INFO="
				+ teacherId;
		return url;
	}

	/**
	 * 督导老师模拟登陆
	 * 
	 * @param teachPlanId
	 * @param classId
	 * @param employeeId
	 * @return
	 */
	public String supervisorTeacherSimulation(String teachPlanId, String classId, String employeeId) {
		String teacherParam = EncryptUtils.encrypt(teachPlanId + "," + classId + "," + employeeId);
		String url = AppConfig.getProperty("oclassHost") + "/book/index/ddteacher/urlLogin.do?formMap.USER_INFO="
				+ teacherParam;
		return url;
	}

	/**
	 * 班主任模拟登陆
	 * 
	 * @param loginAccount
	 * @param employeeId
	 * @return
	 */
	public String headTeacherSimulation(String loginAccount, String employeeId,String requestUrl) {
		String teacherParam = EncryptUtils
				.encrypt(loginAccount + "," + employeeId + "," + DateUtils.getDate().getTime());
		String confServerUrl = AppConfig.getProperty("pcenterEmployeeServer");
		String url = getRedirectDomain(requestUrl, confServerUrl) + AppConfig.getProperty("employee.sso.login.url");
		url += "?userInfo=" + teacherParam;
		return url;
	}
	
	private InputStreamReader getFileStream() throws IOException {
		InputStreamReader in = null;
		try {
			PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
			Resource[] posterResources = patternResolver.getResources(MAPPING_URL_FILE);
			if (posterResources != null && posterResources.length > 0) {
				in = new InputStreamReader(posterResources[0].getInputStream());
			} else {
				throw new IOException("获取资源失败!");
			}
		} catch (IOException e) {
			throw e;
		} finally {
		}
		return in;
	}
	
	@SuppressWarnings("unchecked")
	public String getRedirectDomain(String requestUrl,String confServerUrl)  {
		String redirectDomain = confServerUrl;
		//String requestUrl = "http://lms.oucnet.com/";
		//String confServerUrl = "http://xzlms.gzedu.com/";//AppConfig.getProperty("pcenterEmployeeServer");
		try {
			StringBuilder jsonText = new StringBuilder();
			// 1. 获取json输入流对象
			try {
				InputStreamReader in = getFileStream(); 
				char[] buffer = new char[1000]; 
				while (in.read(buffer) != -1) { 
				    jsonText.append(new String(buffer)); 
				} 
				in.close();
			} catch (IOException e) {
				log.error("读取域名映射json文件{}出错:{}",MAPPING_URL_FILE,e.getMessage());
				e.printStackTrace();
			}
			// 2.从文本解析成list列表对象
			if(jsonText.length() > 0) {
				String requestDomain = getDomain(requestUrl);
				String confServerDomain = getDomain(confServerUrl);
				List<LinkedHashMap<String, Object>> list =  JsonUtils.toObject(jsonText.toString(), ArrayList.class);
				OUTER:for(LinkedHashMap<String, Object> map : list) {
					List<String> domainList = (ArrayList<String>) map.get("domains");
					for(String domain : domainList) {
						if(requestDomain.equals(getDomain(domain))) {
							LinkedHashMap<String,String> mappingMap = (LinkedHashMap<String, String>) map.get("mapping");
							for(String keyUrl : mappingMap.keySet()) {
								if(confServerDomain.equals(getDomain(keyUrl))) {
									redirectDomain = mappingMap.get(keyUrl);
									break OUTER;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("解析域名出现异常:{}",e.getMessage());
			e.printStackTrace();
		}
        return redirectDomain;
        
	}
	
	private String getDomain(String url) {
		String host=null;
		Pattern pattern = Pattern.compile(DOMAIN_REGEX);
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			host = matcher.group(1);
		}
		return host;
	}

	public static void main(String[] args) {
		StringBuilder jsonText = new StringBuilder();
		System.out.println(jsonText.length());
		List<LinkedHashMap<String, Object>> list =  JsonUtils.toObject(jsonText.toString(), ArrayList.class);
		System.out.println(list);
	}
}
