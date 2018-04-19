
package com.gzedu.xlims.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.serviceImpl.edumanage.GjtRecResultServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtRecResultTest {

	@Autowired
	GjtRecResultServiceImpl gjtRecResultServiceImpl;

	@org.junit.Test
	public void queryAll() {
		String orgId = "2f5bfcce71fa462b8e1f65bcd0f4c632";
		Map<String, Object> searchParams = new HashMap<String, Object>();
		PageRequest pageRequest = new PageRequest(0, 20);
		Page<GjtRecResult> queryAll = gjtRecResultServiceImpl.queryAll(orgId, searchParams, pageRequest);
		
		for (GjtRecResult gjtRecResult : queryAll.getContent()) {
			System.out.println(gjtRecResult.getGjtStudentInfo().getXm());
			System.out.println(gjtRecResult.getGjtStudentInfo().getGjtGrade().getGradeName());
			System.out.println(gjtRecResult.getGjtStudentInfo().getGjtSpecialty().getZymc());
		}
		

	}
}
