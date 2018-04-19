/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.educationalManage;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.data.educationalManage.GjtMessageInfoData;
import com.gzedu.xlims.data.systemManage.GjtMenuData;
import com.gzedu.xlims.pojo.GjtMenu;
import com.gzedu.xlims.pojo.message.GjtMessageInfo;
import com.gzedu.xlims.service.home.message.GjtMessageInfoService;

/**
 * 功能说明：
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月7日
 * @version 2.5
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtMessageInfoServiceTest {

	@Autowired
	private GjtMessageInfoService gjtMessageInfoService;
	
	// 根据条件查询数据源
	@Test
	public void querySource(){
		
		GjtMessageInfo entity = new GjtMessageInfo();
		entity.setInfoTheme("11000");
		entity.setInfoType("1");
		
		Page<GjtMessageInfo> page= gjtMessageInfoService.querySource(entity,new PageRequest(0,10,new Sort(Direction.DESC, "name")));
		
		for(GjtMessageInfo gu : page.getContent()){
			
			System.out.println("父菜单----->"+gu.getInfoTheme());
		}
	}

	// 添加通知公告
	@Test
	public void saveEntity(){
		
		GjtMessageInfo entity = GjtMessageInfoData.setGjtMessageInfo();
		String id = UUIDUtils.random();
		System.out.println("id--->"+id);
		entity.setMessageId(id);
		
		gjtMessageInfoService.saveEntity(entity);
	}

	// 删除通知公告
	@Test
	public void delete(){
		
		gjtMessageInfoService.delete("30038c138cf74b55f2e37c33b9b00983");
	}

	// 查询通知公告
	@Test
	public void queryById(){
		
		GjtMessageInfo gjtMessageInfo =gjtMessageInfoService.queryById("30038c138cf74b55f2e37c33b9b00983");
		
		System.out.println(gjtMessageInfo.getInfoTheme());
	}

	// 修改通知公告
	@Test
	public void updateEntity(){
		GjtMessageInfo gjtMessageInfo = new GjtMessageInfo();
		gjtMessageInfo.setMessageId("30038c138cf74b55f2e37c33b9b00983");
		gjtMessageInfo.setInfoTheme("不设置菜单");
		
		gjtMessageInfoService.updateEntity(gjtMessageInfo);
	}	
		
	// 查询全部数据
	@Test
	public void queryAll(){
		
		gjtMessageInfoService.queryAll();
	}
}
