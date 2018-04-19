/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.systemManage;


import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.data.systemManage.GjtMenuData;
import com.gzedu.xlims.pojo.GjtMenu;
import com.gzedu.xlims.service.systemManage.GjtMenuService;

/**
 * 功能说明：
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月7日
 * @version 2.5
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtMenuServiceTest {

	@Autowired
	private GjtMenuService gjtMenuService;
	
	// 根据条件查询数据源
	@Test
	public void querySource(){
		
		GjtMenu entity = new GjtMenu();
		entity.setId("11000");
		
		Page<GjtMenu> page= gjtMenuService.querySource(entity,new PageRequest(0,10,new Sort(Direction.DESC, "name")));
		
		for(GjtMenu gu : page.getContent()){
			
			System.out.println("父菜单----->"+gu.getName());
			for(GjtMenu g : gu.getChildMenueList()){
				System.out.println("子菜单--->"+g.getName());
			}
		}
	}

	// 添加个人中心菜单
	@Test
	public void saveEntity(){
		
		GjtMenu entity = GjtMenuData.setGjtMenu();
		String id = UUIDUtils.random();
		System.out.println("id--->"+id);
		entity.setId(id);
		
		gjtMenuService.saveEntity(entity);
	}

	// 删除个人中心菜单
	@Test
	public void delete(){
		
		gjtMenuService.delete("30038c138cf74b55f2e37c33b9b00983");
	}

	// 查询个人中心菜单信息
	@Test
	public void queryById(){
		
		GjtMenu gjtMenu =gjtMenuService.queryById("30038c138cf74b55f2e37c33b9b00983");
		
		System.out.println(gjtMenu.getName());
	}

	// 修改个人中心菜单
	@Test
	public void updateEntity(){
		GjtMenu gjtMenu = new GjtMenu();
		gjtMenu.setId("30038c138cf74b55f2e37c33b9b00983");
		gjtMenu.setName("不设置菜单");
		
		gjtMenuService.updateEntity(gjtMenu);
	}	
		
	// 查询全部数据
	@Test
	public void queryAll(){
		
		gjtMenuService.queryAll();
	}
}
