/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.systemManage;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtMenu;

/**
 * 功能说明：
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月7日
 * @version 2.5
 *
 */
public interface GjtMenuService {

	// 根据条件查询数据源
	public Page<GjtMenu> querySource(GjtMenu entity, PageRequest pageRequest);

	// 根据条件查询数据源
	public Page<GjtMenu> queryMainModel(Map<String, Object> searchParams, PageRequest pageRequst);

	// 添加个人中心菜单
	public Boolean saveEntity(GjtMenu entity);

	// 删除个人中心菜单
	public void delete(String id);

	// 查询个人中心菜单信息
	public GjtMenu queryById(String id);

	// 修改个人中心菜单
	public Boolean updateEntity(GjtMenu entity);

	// 查询全部数据
	public List<GjtMenu> queryAll();

	public List<GjtMenu> getGjtMenuTreeList();

	public List<GjtMenu> recursivGjtMenuTreeList(List<GjtMenu> allGjtMenuList, GjtMenu p, List<GjtMenu> temp);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月17日 下午6:02:50
	 * @param name
	 * @param xxId
	 * @return
	 */
	GjtMenu queryByNameAndXxId(String name, String xxId);

}
