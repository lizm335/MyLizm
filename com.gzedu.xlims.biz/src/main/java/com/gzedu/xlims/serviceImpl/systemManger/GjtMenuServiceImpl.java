/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.systemManger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.systemManage.GjtMenuDao;
import com.gzedu.xlims.dao.systemManage.GjtMenuSpecs;
import com.gzedu.xlims.pojo.GjtMenu;
import com.gzedu.xlims.service.systemManage.GjtMenuService;

/**
 * 功能说明：
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月7日
 * @version 2.5
 *
 */
@Service
public class GjtMenuServiceImpl implements GjtMenuService {

	@Autowired
	private GjtMenuDao gjtMenuDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.systemManage.GjtMenuService#querySource(com.gzedu
	 * .xlims.pojo.GjtMenu, org.springframework.data.domain.PageRequest)
	 */
	@Override
	public Page<GjtMenu> querySource(GjtMenu entity, PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return gjtMenuDao.findAll(GjtMenuSpecs.findAllByMenue(entity), pageRequest);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.systemManage.GjtMenuService#saveEntity(com.gzedu.
	 * xlims.pojo.GjtMenu)
	 */
	@Override
	public Boolean saveEntity(GjtMenu entity) {
		// TODO Auto-generated method stub
		GjtMenu gjtMenu = gjtMenuDao.save(entity);
		if (gjtMenu != null) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.systemManage.GjtMenuService#delete(java.lang.
	 * String)
	 */
	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		gjtMenuDao.delete(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.systemManage.GjtMenuService#queryById(java.lang.
	 * String)
	 */
	@Override
	public GjtMenu queryById(String id) {
		// TODO Auto-generated method stub
		return gjtMenuDao.findOne(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.systemManage.GjtMenuService#updateEntity(com.
	 * gzedu.xlims.pojo.GjtMenu)
	 */
	@Override
	public Boolean updateEntity(GjtMenu entity) {
		// TODO Auto-generated method stub
		GjtMenu gjtMenu = gjtMenuDao.save(entity);
		if (gjtMenu != null) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gzedu.xlims.service.systemManage.GjtMenuService#queryAll()
	 */
	@Override
	public List<GjtMenu> queryAll() {
		// TODO Auto-generated method stub
		return gjtMenuDao.findAll();
	}

	@Override
	public List<GjtMenu> getGjtMenuTreeList() {
		// TODO Auto-generated method stub
		List<GjtMenu> allGjtMenuList = gjtMenuDao.findAll();
		return recursivGjtMenuTreeList(allGjtMenuList, null, null);
	}

	@Override
	public List<GjtMenu> recursivGjtMenuTreeList(List<GjtMenu> allGjtMenuList, GjtMenu p, List<GjtMenu> temp) {
		// TODO Auto-generated method stub
		if (temp == null) {
			temp = new ArrayList<GjtMenu>();
		}
		for (GjtMenu gjtMenu : allGjtMenuList) {
			GjtMenu parent = gjtMenu.getGjtMenu();
			if (parent == null) {
				// 一级菜单
				gjtMenu.setMenu_level(new BigDecimal(0));
			} else if (parent.getGjtMenu() == null
					&& ((gjtMenu.getChildMenueList() != null) && (gjtMenu.getChildMenueList().size() > 0))) {
				// 二级菜单(祖父为空，父不为空，无子类)
				gjtMenu.setMenu_level(new BigDecimal(1));
			} else if (parent.getGjtMenu() != null
					&& ((gjtMenu.getChildMenueList().isEmpty()) && (gjtMenu.getChildMenueList().size() <= 0))) {
				// 二级菜单(祖父不为空，父不为空，无子类)
				gjtMenu.setMenu_level(new BigDecimal(2));
			}

			if (((p == null) && (parent == null)) || (p != null ? p.equals(parent) : p == parent)) {
				temp.add(gjtMenu);
				if ((gjtMenu.getChildMenueList() != null) && (gjtMenu.getChildMenueList().size() > 0)) {
					recursivGjtMenuTreeList(allGjtMenuList, gjtMenu, temp);
				}
			}
		}
		return temp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.systemManage.GjtMenuService#queryMainModel(java.
	 * util.Map, org.springframework.data.domain.PageRequest)
	 */
	@Override
	public Page<GjtMenu> queryMainModel(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtMenu> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtMenu.class);

		return gjtMenuDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtMenu queryByNameAndXxId(String name, String xxId) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("xxid", new SearchFilter("xxid", Operator.EQ, xxId));
		filters.put("name", new SearchFilter("name", Operator.EQ, name));
		Specification<GjtMenu> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtMenu.class);
		return gjtMenuDao.findOne(spec);

	}

}
