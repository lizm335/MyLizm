package com.ouchgzee.study.web.controller.index;

import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.ouchgzee.study.service.index.IndexService;
import com.ouchgzee.study.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习首页
 */
@Controller
@RequestMapping("/pcenter/index")
public class IndexController extends BaseController {
	
	@Autowired
	IndexService indexService;
	
	@RequestMapping(value = "/getHeadTeacher")
	@ResponseBody
	public Map<String, Object> getHeadTeacher(HttpServletRequest request,HttpSession session) throws CommonException{
		Map resultMap = new HashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map param = new HashMap();
			param.put("CLASS_ID", super.getTeachClassId(session));
			param.put("STUDENT_ID",user.getGjtStudentInfo().getStudentId());
			param.put("USER_ID",user.getId());
			resultMap = indexService.getHeadTeacher(param);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(MessageCode.BIZ_ERROR);
		}
		return resultMap;
	}


	/**
	 * 获取在线学员
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/onlineStudent")
	@ResponseBody
	public List<Map<String,String>> onlineStudent(HttpServletRequest request,HttpSession session) throws CommonException {
		List<Map<String,String>> result = null;
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map<String,Object> param = new HashMap<String, Object>();

			param.put("classId", super.getTeachClassId(session));
			param.put("studentId",user.getGjtStudentInfo().getStudentId());
			result = indexService.getOnlineStudent(param);
			if(EmptyUtils.isNotEmpty(result)){

			}
		}catch (Exception e){
			throw new CommonException(MessageCode.BIZ_ERROR);
		}
		return result;
	}

	/**
	 * 获取班级的学员动态
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/studentDynamic")
	@ResponseBody
	public List<Map<String,String>> studentDynamic(HttpSession session) throws CommonException {
		List<Map<String,String>> result = null;
		try {
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("classId", super.getTeachClassId(session));
			result = indexService.getStudentDynamic(param);
		}catch (Exception e){
			throw new CommonException(MessageCode.BIZ_ERROR);
		}


		return result;
	}

}
