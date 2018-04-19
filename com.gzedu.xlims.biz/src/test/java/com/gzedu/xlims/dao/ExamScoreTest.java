
package com.gzedu.xlims.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gzedu.xlims.serviceImpl.edumanage.GjtRecResultServiceImpl;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.cn
 * @Date 2017年1月12日
 * @version 1.0
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class ExamScoreTest {

	@Autowired
	GjtRecResultServiceImpl gjtRecResultServiceImpl;

	@org.junit.Test
	public void queryAll() {
		// 查询到一个教师信息
		String orgId = "2f5bfcce71fa462b8e1f65bcd0f4c632";
		Map<String, Object> searchParams = new HashMap<String, Object>();
		PageRequest pageRequest = new PageRequest(0, 20);
		gjtRecResultServiceImpl.queryAllBySql(orgId, searchParams, pageRequest);

	}

	@org.junit.Test
	public void queryAll2() {
		Map<String, Object> map = gjtRecResultServiceImpl.queryStudent("0e41090e289c42cdbd289766061f4ce8");
		System.out.println(map);
	}

	@org.junit.Test
	public void queryAll4() {
		List<Map<String, String>> list = gjtRecResultServiceImpl.queryTerm("6049a02926e64267af9028dc37a69d99");

		List<List<Map<String, String>>> listResult = new ArrayList<List<Map<String, String>>>();

		for (Map<String, String> map : list) {
			List<Map<String, String>> list1 = gjtRecResultServiceImpl.queryStudentSourceDetail("6049a02926e64267af9028dc37a69d99", map.get("TERM_TYPE_CODE"));
			listResult.add(list1);
		}
		for (List<Map<String, String>> list2 : listResult) {// 总共多少个学期
			for (Map<String, String> map : list2) {// 每个学期的内容
				System.out.println(map);
			}
		}

	}
}
