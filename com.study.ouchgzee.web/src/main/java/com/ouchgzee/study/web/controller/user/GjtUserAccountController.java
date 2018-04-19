package com.ouchgzee.study.web.controller.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtClassStudent;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.dictionary.GjtDistrictService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.ouchgzee.study.web.vo.GjtStudentVo;


@Controller
@RequestMapping("/pcenter/gjtUser")
public class GjtUserAccountController {
	
	@Autowired
	private GjtUserAccountService gjtUserAccountService;
	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;
	@Autowired
	private GjtClassInfoService gjtClassInfoService;
	
	@Autowired
	private CacheService cacheService;
	
	
	/*@RequestMapping(value="/test", method=RequestMethod.GET)
	public String test() {
		return "test";
	}*/
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	@ResponseBody
	public GjtStudentVo edit(HttpServletRequest request) throws CommonException {
		GjtUserAccount sessionUser = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if(null == sessionUser) {
			throw new CommonException(MessageCode.TOKEN_INVALID);
		}
		GjtStudentVo vo = null;
		GjtUserAccount user = gjtUserAccountService.findOne(sessionUser.getId());
		GjtStudentInfo student = user.getGjtStudentInfo();
		boolean schoolModel = new Boolean(request.getParameter("schoolModel"));
		if(schoolModel) {
			String className = null;// 教务班级
			GjtClassInfo clazInfo = gjtClassInfoService.queryTeachClassInfo(student.getStudentId());
			if(clazInfo != null) {
				className = clazInfo.getBjmc();
			}
			String orgName = ""; // 所属院校
			if(student.getGjtSchoolInfo() != null) {
				orgName = student.getGjtSchoolInfo().getGjtOrg().getOrgName();
			}
			vo = new GjtStudentVo(student, student.getPycc(), className, orgName);
		} else {
			vo = new GjtStudentVo(student);
			if (StringUtils.isNotEmpty(vo.getGender())) {
				vo.setGender(cacheService.getCachedDictionaryName("Sex", vo.getGender()));
			}
			if (StringUtils.isNotEmpty(vo.getNation())) {// 民族
				vo.setNation(cacheService.getCachedDictionaryName("NationalityCode", vo.getNation()));
			}
			if (StringUtils.isNotEmpty(vo.getMarryStatus())) {
				vo.setMarryStatus(cacheService.getCachedDictionaryName("MaritalStatusCode", vo.getMarryStatus()));
			}
			if(StringUtils.isNotEmpty(vo.getHouseholdNature())) {
				vo.setHouseholdNature(cacheService.getCachedDictionaryName("AccountsNatureCode", vo.getHouseholdNature()));
			}
		}
		if(StringUtils.isNotEmpty(vo.getPycc())) {
			String pyccName = cacheService.getCachedDictionaryName("TrainingLevel", student.getPycc()); // 专业层次
			vo.setPyccName(pyccName);
		}
		
		return vo;
	}
	
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public void update(GjtStudentVo vo) {
		String studentId = vo.getStudentId();
		GjtStudentInfo si = gjtStudentInfoService.queryById(studentId);
		if(StringUtils.isNotEmpty(vo.getPictureURL())) {
			si.setAvatar(vo.getPictureURL());
		}
		if (StringUtils.isNotEmpty(vo.getMobile())) {
			// 手机号码
			si.setSjh(vo.getMobile());
		}
		if (StringUtils.isNotEmpty(vo.getTelephone())) {
			// 固定电话
			si.setLxdh(vo.getTelephone());
		}
		if (StringUtils.isNotEmpty(vo.getEmail())) {
			// 邮箱
			si.setDzxx(vo.getEmail());
		}
		if (StringUtils.isNotEmpty(vo.getPostalAddress())) {
			// 通讯地址
			si.setTxdz(vo.getPostalAddress());
		}
		if (StringUtils.isNotEmpty(vo.getPostcode())) {
			// 邮编
			si.setYzbm(vo.getPostcode());
		}
		if (StringUtils.isNotEmpty(vo.getCompany())) {
			// 所在单位
			si.setScCo(vo.getCompany());
		}
		if (StringUtils.isNotEmpty(vo.getCompanyAddress())) {
			// 单位地址
			si.setScCoAddr(vo.getCompanyAddress());
		}
		if (StringUtils.isNotEmpty(vo.getJobPost())) {
			// 岗位职务
			if(si.getGjtSignup() != null) {
				si.getGjtSignup().setJobPost(vo.getJobPost());
			}
		}
		
		// 通信地址
		/*String province = studentVo.getProvince();
		String city = studentVo.getCity();
		String area = studentVo.getArea();
		String provinceCode = gjtDistrictService.queryByName(province).getId();
		String cityCode = gjtDistrictService.queryByName(city).getId();
		String areaCode = gjtDistrictService.queryByName(area).getId();
		info.setProvince(provinceCode);
		info.setCity(cityCode);
		info.setArea(areaCode);*/
		gjtStudentInfoService.updateEntityAndFlushCache(si);
	}
	
	@RequestMapping(value="/restPassword", method=RequestMethod.POST)
	@ResponseBody
	public String restPwd(HttpServletRequest request) {
		String result;
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		GjtUserAccount sessionUser = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtUserAccount user = gjtUserAccountService.findOne(sessionUser.getId());
		if(user.getPassword().equals(Md5Util.encode(oldPassword))) {
			try {
				user.setPassword(Md5Util.encode(newPassword));
				user.setPassword2(newPassword);
				gjtUserAccountService.update(user);
				result = "修改密码成功.";
			} catch (Exception e) {
				result = "更新密码时,发生异常.";
				e.printStackTrace();
			}
		} else {
			result = "密码错误.";
		}
		return result;
	}
}
