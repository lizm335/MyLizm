package com.ouchgzee.study.serviceImpl.index;

import com.alibaba.fastjson.JSONObject;
import com.cybermkd.mongo.kit.MongoQuery;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.mogo.MongoClientFactoryBean;
import com.mongodb.MongoClient;
import com.ouchgzee.study.dao.index.IndexDao;
import com.ouchgzee.study.service.index.IndexService;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class IndexServiceImpl implements IndexService {

	@Autowired
	private IndexDao indexDao;

	@Autowired
	private MongoClientFactoryBean mongoClientFactoryBean;
	
	@Value("#{configProperties['eeChatInterface']}")
	String eeServer;
	
	@Override
	@SuppressWarnings("rawtypes")
	public Map getHeadTeacher(Map searchParams) {
		Map resultMap = new HashMap();
		try {
			List headList = indexDao.getHeadTeacher(searchParams);
			if (EmptyUtils.isNotEmpty(headList)) {
				Map headMap = (Map)headList.get(0);
				String employeeId = ObjectUtils.toString(headMap.get("EMPLOYEE_ID"));
				String eeUrl = "";
				if (StringUtils.isNotEmpty(employeeId)) {
					eeUrl = eeServer + "/openChat.do?data=" + EncryptUtils
							.encrypt("{\"USER_ID\":\"" + ObjectUtils.toString(searchParams.get("STUDENT_ID")) + "\",\"TO_ID\":\"" + employeeId + "\"}");
				}
				headMap.put("eeUrl", eeUrl);
				resultMap.putAll(headMap);
			}
			
			List msgList = indexDao.getMsgCount(searchParams);
			if (EmptyUtils.isNotEmpty(msgList)) {
				resultMap.putAll((Map)msgList.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 获取班级在线学员
	 *
	 * @param param
	 * @return
	 */
	@Override
	public List<Map<String,String>> getOnlineStudent(Map<String, Object> param) {
		String studentId = ObjectUtils.toString(param.get("studentId"));
		param.remove("studentId");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		MongoClient client = mongoClientFactoryBean.initMongoClient();
		try {
			List<Map<String,String>> result = indexDao.getOnlineStudent(param);
			if(EmptyUtils.isNotEmpty(result)){
				for (Map map :result){
					Map resultMap = new HashMap();
					resultMap.put("studentId", ObjectUtils.toString(map.get("STUDENT_ID"),""));
					resultMap.put("xm", ObjectUtils.toString(map.get("XM"),""));
					resultMap.put("xh", ObjectUtils.toString(map.get("XH"),""));
					resultMap.put("zp", ObjectUtils.toString(map.get("ZP"),""));
					resultMap.put("eeno", ObjectUtils.toString(map.get("EENO"),""));
					if(EmptyUtils.isNotEmpty(resultMap.get("studentId"))&&!ObjectUtils.toString(studentId,"").equals(ObjectUtils.toString(resultMap.get("studentId")))){
						String deurl = "{\"USER_ID\":\""+studentId+"\",\"TO_ID\":\""+resultMap.get("studentId")+"\"}";
						String eeurl = "http://eechat.gzedu.com/openChat.do?data=" +EncryptUtils.encrypt(deurl);
						resultMap.put("eeurl",eeurl);
					}else  if (EmptyUtils.isNotEmpty(resultMap.get("studentId"))&&ObjectUtils.toString(studentId,"").equals(ObjectUtils.toString(resultMap.get("studentId")))){//自己
						String deurl = "{\"USER_ID\":\""+studentId+"\",\"APP_ID\":\"\"}";
						String eeurl = "http://eechat.gzedu.com/urlLoginCheck.do?data=" +EncryptUtils.encrypt(deurl);
						resultMap.put("eeurl",eeurl);
					}else {
						resultMap.put("eeurl","");
					}


					List<Map<String,String>> choose = indexDao.getStudentChoose(ObjectUtils.toString(map.get("STUDENT_ID")));//查出来学员的全部选课
					List<String> args = new ArrayList<String>();
					Map<String,String> courseMap = new HashMap<String, String>();
					if(EmptyUtils.isNotEmpty(choose)){
						for (int i = 0; i <choose.size() ; i++) {
							String recId = ObjectUtils.toString(choose.get(i).get("REC_ID"));
							if(recId!=null&&!"".equals(recId)){
								args.add(recId);
								courseMap.put(recId,choose.get(i).get("KCMC"));//课程名称
							}
						}
					}
					MongoQuery query=new MongoQuery();//查询mongodb
					JSONObject object = query.use("LMS_USER_ONLINE").in("USER_ID",args).eq("IS_ONLINE","Y").findOne();
					if(EmptyUtils.isNotEmpty(object)){
						String chooseId = ObjectUtils.toString(object.get("USER_ID"),"");
						if(EmptyUtils.isNotEmpty(chooseId)){
							if(courseMap.containsKey(chooseId)){//用户在线的那个课程
								resultMap.put("status","正在学习"+ObjectUtils.toString(ObjectUtils.toString(courseMap.get(chooseId)),""));
							}else {
								resultMap.put("status","");
							}
						}
					}
					if(EmptyUtils.isEmpty(resultMap.get("status"))){//不存在Mongodb的记录
						if(EmptyUtils.isNotEmpty(ObjectUtils.toString(map.get("CURRENT_LOGIN_TIME"),""))){
							long status = DateUtil.howLong("m", DateUtils.getTodayTime(),ObjectUtils.toString(map.get("CURRENT_LOGIN_TIME"),""));
							if(status>0){
								resultMap.put("status",status+"分钟前登录");
							}else {
								resultMap.put("status","");
							}
						}
					}
					list.add(resultMap);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			client.close();
		}
		return list;
	}


	/**
	 * 获取班级学员的实时动态
	 *
	 * @param param
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStudentDynamic(Map<String, Object> param) {

		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		MongoClient client = mongoClientFactoryBean.initMongoClient();
		try {
			List<Map<String,String>> result = indexDao.getOnlineStudent(param);
			if(EmptyUtils.isNotEmpty(result)){
				for (Map map :result){
					Map<String,String> tmap = new HashMap<String, String>();
					MongoQuery query = new MongoQuery();

					List<Map<String,String>> choose = indexDao.getStudentChoose(ObjectUtils.toString(map.get("STUDENT_ID")));//查出来学员的全部选课
					List<String> args = new ArrayList<String>();
					Map<String,String> courseMap = new HashMap<String, String>();
					if(EmptyUtils.isNotEmpty(choose)){
						for (int i = 0; i <choose.size() ; i++) {
							String recId = ObjectUtils.toString(choose.get(i).get("REC_ID"));
							if(recId!=null&&!"".equals(recId)){
								args.add(recId);
								courseMap.put(recId,choose.get(i).get("KCMC"));//课程名称
							}
						}
					}
					JSONObject object = query.use("LMS_USER_ONLINE").in("USER_ID",args).eq("IS_ONLINE","Y").findOne();
					if(EmptyUtils.isNotEmpty(object)){
						String chooseId = ObjectUtils.toString(object.get("USER_ID"),"");
						if(EmptyUtils.isNotEmpty(chooseId)){
							if(courseMap.containsKey(chooseId)){//用户在线的那个课程
								tmap.put("xm",ObjectUtils.toString(map.get("XM"),""));
								tmap.put("dynamic","正在学习"+ObjectUtils.toString(ObjectUtils.toString(courseMap.get(chooseId)),""));
								list.add(tmap);
							}
						}
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			client.close();
		}
		return list;
	}
}
