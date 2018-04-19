package com.gzedu.xlims.webservice.controller.pri.role;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.gzedu.xlims.webservice.common.Servlets;
import com.gzedu.xlims.webservice.controller.BaseController;
import com.gzedu.xlims.webservice.response.ResponseResult;
import com.gzedu.xlims.webservice.response.ResponseStatus;
/**
 * 角色管理接口
 * @author lyj
 * @time 2017年5月18日 
 * TODO
 */
@Controller
@RequestMapping("/interface/priRole")
public class GjtRoleController extends BaseController{
	
	@Autowired
	PriRoleInfoService roleInfoService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseResult list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,HttpServletRequest request) {
		Map<String,Object> resultMap = Maps.newHashMap();
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "roleCode", "ASC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		ResponseResult result = null;
		try {
			List<PriRoleVo> list = Lists.newArrayList();
			Page<PriRoleInfo> pageInfo = roleInfoService.queryAll(searchParams, pageRequst);
			for(PriRoleInfo role : pageInfo.getContent()) {
				list.add(new PriRoleVo(role));
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
}
