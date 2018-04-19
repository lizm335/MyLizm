/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.systemManage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.pojo.GjtMenu;
import com.gzedu.xlims.service.systemManage.GjtMenuService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 功能说明：
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月7日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/module/menu")
public class MenuActionController {

	private Log log = LogFactory.getLog(MenuActionController.class);

	@Autowired
	private GjtMenuService gjtMenuService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request) {

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtMenu> infos = gjtMenuService.queryMainModel(searchParams, pageRequst);

		model.addAttribute("infos", infos);
		model.addAttribute("list", gjtMenuService.getGjtMenuTreeList());
		
		return "systemManage/menu/list";

	}

	@RequestMapping(value = "getMenu", method = RequestMethod.POST)
	@ResponseBody
	public String getMenu(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		BufferedReader bufr;
		try {
			bufr = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

			StringBuilder sBuilder = new StringBuilder("");
			String temp = "";
			while ((temp = bufr.readLine()) != null) {
				sBuilder.append(temp);
			}
			bufr.close();
			String json = sBuilder.toString();
			JSONObject json1 = JSONObject.fromObject(json);
			int pageindex = Integer.valueOf(request.getParameter("offset"));
			int offset = Integer.valueOf(json1.getString("offset"));
			int limit = Integer.valueOf(json1.getString("limit"));

			if (offset != 0) {
				pageindex = offset / limit;
			}

			JSONArray jsonData = new JSONArray();
			Page<GjtMenu> pageMenu = gjtMenuService.querySource(null, new PageRequest(0, 20));

			request.setAttribute("RsCount", pageMenu.getSize());
			request.setAttribute("gblst", pageMenu);
			request.setAttribute("count_per_page", 9);
			request.setAttribute("pageindex", 2);

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("rows", jsonData);
			jsonObject.put("total", pageMenu.getSize());

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(ModelMap model, HttpServletRequest request) {
		model.addAttribute("list", gjtMenuService.getGjtMenuTreeList());
		return "systemManage/menu/add";

	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public Feedback save(GjtMenu entity, ModelMap model) {
		entity.setName(entity.getName().trim());
		if (entity.getName().length() == 0) {
			return new Feedback(false, "操作失败!");
		}
		String id = UUIDUtils.random();
		entity.setId(id);
		if (entity.getParentId() != null) {
			GjtMenu ey = new GjtMenu();
			ey.setId(entity.getParentId());
			entity.setGjtMenu(ey);
		}
		boolean bl;
		try {
			bl = gjtMenuService.saveEntity(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			return new Feedback(false, "新增个人中心菜单失败!");
		}
		return new Feedback(bl, "新增个人中心菜单成功!");
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(String id, ModelMap model) {

		GjtMenu entity = gjtMenuService.queryById(id);
		model.addAttribute("list", gjtMenuService.getGjtMenuTreeList());
		model.addAttribute("entity", entity);
		return "systemManage/menu/edit";

	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(GjtMenu entity) {

		String id = entity.getId();

		if (StringUtils.isBlank(id)) {
			return new Feedback(false, "操作失败!");
		}
		GjtMenu gm = gjtMenuService.queryById(id);
		gm.setName(entity.getName());
		gm.setNameEn(entity.getNameEn());
		gm.setOrderNo(entity.getOrderNo());
		gm.setUrl(entity.getUrl());
		gm.setVisible(entity.getVisible());
		gm.setXxid(entity.getXxid());

		GjtMenu gu = new GjtMenu();
		gu.setId(entity.getParentId());
		gm.setGjtMenu(gu);

		boolean bl = true;
		try {
			bl = gjtMenuService.updateEntity(gm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			return new Feedback(bl, "更新个人中心菜单失败!");
		}
		return new Feedback(bl, "更新个人中心菜单成功!");

	}

	// 先检查是否有子资源, 有的话就不删除, 没有的话就删除.
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Feedback delete(String id) {

		if (StringUtils.isBlank(id)) {
			return new Feedback(false, "id为空");
		}
		GjtMenu gjtMenu = gjtMenuService.queryById(id);

		if (gjtMenu == null) {
			return new Feedback(false, "没找到个人中心菜单!");
		}
		List localSet1 = gjtMenu.getChildMenueList();
		if ((localSet1 != null) && (!localSet1.isEmpty())) {
			return new Feedback(false, "不是末级分类,您不能删除!!");
		}
		this.gjtMenuService.delete(id);
		return new Feedback(true, "删除成功!");

	}

}
