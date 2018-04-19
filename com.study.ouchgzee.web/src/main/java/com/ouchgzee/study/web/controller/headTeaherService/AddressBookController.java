package com.ouchgzee.study.web.controller.headTeaherService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.dto.AddressBookDto;
import com.gzedu.xlims.service.addressBookService.AddressBookService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.ouchgzee.study.web.common.Servlets;
import com.ouchgzee.study.web.vo.AddressBookVo;

/**
 * 
 * 功能说明：地址通讯录
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年3月16日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/pcenter/headTeacherService/addressBook")
public class AddressBookController {

	private static final Logger log = LoggerFactory.getLogger(AddressBookController.class);

	@Autowired
	AddressBookService addressBookService;
	@Autowired
	GjtClassStudentService gjtClassStudentService;

	@Value("#{configProperties['eeChatInterface']}")
	String eeServer;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) throws CommonException {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);

		GjtClassInfo classInfo = (GjtClassInfo) request.getSession().getAttribute(WebConstants.TEACH_CLASS);
		GjtStudentInfo student = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		String userName = request.getParameter("userName");

		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("classId", classInfo.getClassId());
		if (StringUtils.isNotBlank(userName)) {
			searchParams.put("userName", userName);
		}
		searchParams.put("studentId", student.getStudentId());

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String type = request.getParameter("type");
		log.info("班级通讯录参数:{}", searchParams);
		List<AddressBookVo> list = new ArrayList<AddressBookVo>();
		if ("student".equals(type)) {// 如果是班级同学就传"student"，否则默认为辅导老师通讯录
			Page<AddressBookDto> addressBookDtoPage = addressBookService.findByPageHql(searchParams, pageRequst);
			log.info("班级学员通讯录集合:{}" + addressBookDtoPage.getTotalElements());
			for (AddressBookDto addressBookDto : addressBookDtoPage) {
				AddressBookVo vo = new AddressBookVo(student.getStudentId(), addressBookDto, eeServer);
				list.add(vo);
			}
			Page<AddressBookVo> page = new PageImpl<AddressBookVo>(list, pageRequst,
					addressBookDtoPage.getTotalElements());
			resultMap.put("pageInfo", page);
		} else {
			Page<Map> classTeacherList = addressBookService.findCourseTeacher(searchParams, pageRequst);
			for (Map map : classTeacherList) {
				AddressBookVo vo = new AddressBookVo(student.getStudentId(), map, eeServer);
				list.add(vo);
			}
			Page<AddressBookVo> page = new PageImpl<AddressBookVo>(list, pageRequst,
					classTeacherList.getTotalElements());
			resultMap.put("pageInfo", page);
		}
		long studentCount = addressBookService.findAllCount(searchParams);
		long teacherCount = addressBookService.findCourseTeacherCount(searchParams);
		resultMap.put("studentCount", studentCount);
		resultMap.put("teacherCount", teacherCount);

		return resultMap;
	}
}
