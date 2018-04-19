package com.gzedu.xlims.webservice.controller.gjt.org;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.webservice.common.Servlets;
import com.gzedu.xlims.webservice.controller.BaseController;
import com.gzedu.xlims.webservice.response.ResponseResult;
import com.gzedu.xlims.webservice.response.ResponseStatus;
/**
 * 院校管理接口
 * @author lyj
 * @time 2017年5月18日 
 * TODO
 */
@Controller
@RequestMapping("/interface/gjtOrg")
public class GjtOrgController extends BaseController{
	
	@Autowired
	private GjtOrgService gjtOrgService;
	
	/**
	 * 机构列表查询接口(分页)
	 * @param pageNumber
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseResult list(	@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, HttpServletRequest request) {
		Map<String,Object> resultMap = Maps.newHashMap();
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber,pageSize, "createdDt", "DESC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		ResponseResult result = null;
		try {
			searchParams.put("EQ_isDeleted","N");
			Page<GjtOrg> pageInfo = gjtOrgService.queryAll(searchParams, pageRequst);
			List<GjtOrgVo> list = Lists.newArrayList();
			for(GjtOrg org : pageInfo.getContent() ) {
				list.add(new GjtOrgVo(org));
			}
			resultMap.put("pageInfo", list);
			resultMap.put("total", pageInfo.getTotalElements());
			result = new ResponseResult(ResponseStatus.SUCCESS, resultMap);
		} catch (Exception e) {
			result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "query", method = RequestMethod.GET)
	@ResponseBody
	public ResponseResult query(HttpServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		String code = (String) searchParams.get("EQ_code");
		
		ResponseResult result = null;
		try {
			GjtOrg org = gjtOrgService.queryByCode(code);
			GjtOrgVo vo = new GjtOrgVo(org);
			if(org.getChildGjtOrgs() != null) {
				List<GjtOrgVo> childs = getChilds(org.getChildGjtOrgs());
				vo.setChilds(childs);
			}
			result = new ResponseResult(ResponseStatus.SUCCESS, vo);
		} catch (Exception e) {
			result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	private List<GjtOrgVo> getChilds(List<GjtOrg> list) {
		List<GjtOrgVo> voList = Lists.newArrayList();
		GjtOrgVo vo = null;
		for(GjtOrg org : list) {
			vo = new GjtOrgVo(org);
			if("2".equals(org.getOrgType()) || "3".equals(org.getOrgType())) {
				vo.setChilds(getChilds(org.getChildGjtOrgs()));
			}
			voList.add(vo);
		}
		return voList;
	}
	
	/**
	 * 机构创建/更新接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult saveOrUpdate(HttpServletRequest request) {
		ResponseResult result = null;
		try {
			String orgType = request.getParameter("orgType");
			if(StringUtils.isEmpty(orgType)) {
				result = new ResponseResult(ResponseStatus.PARAM_ERROR, "机构类型不能为空!");
			} else {
				// 1：院校，2：分校
				GjtOrg org = builderOrg4Request(request);
				if(checkOrg(org)) {
					if(orgType.equals("1")) {
						toPersistOrg(org, getDomainMap(request));
					}
					if(orgType.equals("2")) {
						toPersistChildOrg(org);
					}
	                result = new ResponseResult(ResponseStatus.SUCCESS, org.getId());
				} else {
					result = new ResponseResult(ResponseStatus.PARAM_ERROR, "院校/分校信息不完整,请检查参数!");
				}
			}
		} catch (Exception e) {
			result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 启用停用
	 * @param id
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changeStatus",method = RequestMethod.POST)
	public ResponseResult changeStatus(String id,String status){

		ResponseResult result;
		try {
			boolean b = gjtOrgService.updateStatus(id,status);
			if(b){
				result= new ResponseResult(ResponseStatus.SUCCESS, "修改状态成功!");
			}else {
				result= new ResponseResult(ResponseStatus.FAIL, "修改状态失败!");
			}
		} catch (Exception e) {
			result= new ResponseResult(ResponseStatus.FAIL, e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	private GjtOrg builderOrg4Request(HttpServletRequest request) {
		GjtOrg org = new GjtOrg();
		org.setId(request.getParameter("orgId"));
		org.setOrgType(request.getParameter("orgType"));// 机构类型
		org.setCode(request.getParameter("orgCode")); // 机构编码
		org.setOrgName(request.getParameter("orgName")); // 机构名称
		org.setSchoolModel(request.getParameter("schoolModel")); // 办学模式(1-学历办学 2-中职院校 3-院校模式)
		
		org.setGjtSchoolInfo(new GjtSchoolInfo());
		org.getGjtSchoolInfo().setLinkTel(request.getParameter("gjtSchoolInfo.linkTel")); // 联系电话
		org.getGjtSchoolInfo().setLinkMan(request.getParameter("gjtSchoolInfo.linkMan")); // 联系人
		org.getGjtSchoolInfo().setLinkMail(request.getParameter("gjtSchoolInfo.linkMail")); // email
		org.getGjtSchoolInfo().setXxdz(request.getParameter("gjtSchoolInfo.xxdz")); // 院校地址
		org.getGjtSchoolInfo().setXxqhm(request.getParameter("gjtSchoolInfo.xxqhm")); // 所在地行政区
		org.getGjtSchoolInfo().setMemo(request.getParameter("gjtSchoolInfo.memo")); // 描述
		
		org.setParentGjtOrg(new GjtOrg());
		org.getParentGjtOrg().setId(request.getParameter("parentGjtOrg.id")); // orgType为2时，分校所属的院校ID
		
		if(StringUtils.isNotEmpty(org.getId())) {
			org.setUpdatedBy(request.getRequestURL().toString());
			org.getGjtSchoolInfo().setUpdatedBy(request.getRequestURL().toString());
		} else {
			org.setCreatedBy(request.getRequestURL().toString());
			org.getGjtSchoolInfo().setCreatedBy(request.getRequestURL().toString());
		}
		return org;
	}
	
	private void toPersistOrg(GjtOrg org,Map<Integer,String> domainMap) throws Exception {
		// TODO 新增/更新
		if(StringUtils.isEmpty(org.getId())) {
			if(gjtOrgService.queryByCode(org.getCode()) != null ) {
				throw new Exception("机构编码已存在!");
			};
			gjtOrgService.insert(org,domainMap);
		} else {
			GjtOrg modifyInfo = gjtOrgService.queryById(org.getId());
			// modifyInfo.setCode(org.getCode());//机构编码
			modifyInfo.setOrgName(org.getOrgName());
			modifyInfo.getGjtSchoolInfo().setXxmc(org.getOrgName());// 机构名称
			//modifyInfo.setSchoolModel(org.getSchoolModel());// 办学模式(1-学历办学 2-中职院校 3-院校模式)
			modifyInfo.getGjtSchoolInfo().setLinkTel(org.getGjtSchoolInfo().getLinkTel());// 联系电话
			modifyInfo.getGjtSchoolInfo().setLinkMan(org.getGjtSchoolInfo().getLinkMan());// 联系人
			modifyInfo.getGjtSchoolInfo().setLinkMail(org.getGjtSchoolInfo().getLinkMail());// 电子邮箱
			modifyInfo.getGjtSchoolInfo().setXxdz(org.getGjtSchoolInfo().getXxdz());// 院校地址
			modifyInfo.getGjtSchoolInfo().setMemo(org.getGjtSchoolInfo().getMemo());// 描述
			/*modifyInfo.getGjtSchoolInfo()
					.setAppid(org.getGjtSchoolInfo().getAppid());// APPId*/
			try {
				gjtOrgService.update(modifyInfo);
			} catch (Exception e) {
				throw new Exception("更新失败:"+e.getMessage());
			}
		}
	}

	private void toPersistChildOrg(GjtOrg org) throws Exception {
		// TODO 新增/更新
		if(StringUtils.isEmpty(org.getId())) {
			if(gjtOrgService.queryByCode(org.getCode()) != null ) {
				throw new Exception("机构编码已存在!");
			};
			gjtOrgService.insertChildOrg(null,org);
		} else {
			//String tempParentId = org.getParentGjtOrg().getId();//更新parentid
			GjtOrg modifyInfo = gjtOrgService.queryById(org.getId());
			//modifyInfo.setCode(org.getCode());// 院校编码
			modifyInfo.setOrgName(org.getOrgName());
			modifyInfo.getGjtSchoolInfo().setXxmc(org.getOrgName());// 院校名称
			modifyInfo.getGjtSchoolInfo().setLinkTel(org.getGjtSchoolInfo().getLinkTel());// 联系电话
			modifyInfo.getGjtSchoolInfo().setLinkMan(org.getGjtSchoolInfo().getLinkMan());	// 联系人
			modifyInfo.getGjtSchoolInfo().setLinkMail(org.getGjtSchoolInfo().getLinkMail());// 电子邮箱
			modifyInfo.getGjtSchoolInfo().setXxdz(org.getGjtSchoolInfo().getXxdz());// 院校地址
			modifyInfo.getGjtSchoolInfo().setMemo(org.getGjtSchoolInfo().getMemo());// 描述
			//modifyInfo.getGjtSchoolInfo().setAppid(org.getGjtSchoolInfo().getAppid());	// APPId
			//GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			/*if("0".equals(user.getGrantType())) {//超级管理员
				GjtOrg gjtOrg = gjtOrgService.queryById(tempParentId);
				modifyInfo.setParentGjtOrg(gjtOrg);
			}*/
			try {
				gjtOrgService.update(modifyInfo);
			} catch (Exception e) {
				throw new Exception("更新失败:"+e.getMessage());
			}
		}
	}
	
	/**
	 * 获取院校域名信息
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	private Map<Integer, String> getDomainMap(HttpServletRequest request) throws Exception {
		Map<Integer, String> domainMap = Maps.newHashMap();
		// 班主任平台-院校域名
		String headTeacherDomain = request.getParameter("headTeacherDomain");
		if(StringUtils.isNotEmpty(headTeacherDomain)) {
			domainMap.put(PlatfromTypeEnum.HEADTEACHERPLATFORM.getNum(), headTeacherDomain);
		}
		// 管理平台-院校域名
		String manageDomain = request.getParameter("manageDomain");
		if (StringUtils.isNotEmpty(manageDomain)) {
			domainMap.put(PlatfromTypeEnum.MANAGEPLATFORM.getNum(), manageDomain);
		}
		// 个人中心平台-院校域名
		String percentDomain = request.getParameter("percentDomain");
		if (StringUtils.isNotEmpty(percentDomain)) {
			domainMap.put(PlatfromTypeEnum.PERCENTPLATFORM.getNum(), percentDomain);
		}
		/*if(domainMap.size() < 3) {
			throw new Exception("请输入院校域名信息!");
		}*/
		return domainMap;
	}
	/**
	 * 院校/分校信息校验
	 * @param org
	 * @return
	 */
	private boolean checkOrg(GjtOrg org) {
		boolean b = StringUtils.isNotEmpty(org.getCode())
				&& StringUtils.isNotEmpty(org.getOrgName())
				&& StringUtils.isNotEmpty(org.getOrgType()) 
				&& StringUtils.isNotEmpty(org.getGjtSchoolInfo().getLinkTel())
				&& StringUtils.isNotEmpty(org.getGjtSchoolInfo().getLinkMan())
				&& StringUtils.isNotEmpty(org.getGjtSchoolInfo().getLinkMail())
				&& StringUtils.isNotEmpty(org.getGjtSchoolInfo().getXxdz());
		// 院校需要输入办学模式字段
		return (b && org.getOrgType().equals("1")) ? StringUtils.isNotEmpty(org.getSchoolModel()) : b;
	}
}
