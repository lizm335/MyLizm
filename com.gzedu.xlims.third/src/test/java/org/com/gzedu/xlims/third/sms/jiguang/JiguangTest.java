package org.com.gzedu.xlims.third.sms.jiguang;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.gzedu.xlims.common.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gzedu.xlims.third.sms.SmsSenderManager;
import com.gzedu.xlims.third.sms.SmsSenderType;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:sender.xml" })
public class JiguangTest {

	@Resource
	private SmsSenderManager smsSenderManager;
	
	@Test
	public void testJPust() {
		// 推送
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", "d1214257b43f46a4b53291924f5431e3");
		params.put("title", "学习督促提醒2");// 标题
		String alert = "<p>你的学习进度已落后,请联系班主任！</p>";
		alert = alert.replaceAll("<.?\\w+\\s*[\\w+='?\"?\\w+'?\"?\\s*]*>", "");
		params.put("alert", alert);// 内容
		params.put("time", DateUtils.getStringToDate(new Date()));
		params.put("type", "1");// 1-系统消息 2-教务通知 11-班级公告 12-考试通知 13-学习提醒
								// 具体查看数据字典
		// TODO 按院校推送,一次推送最多 20 个
		// String schoolId = "adba13982e654eb7813d746387ccd20e";
		// schoolId = "3db287e417624fb9b6926e6d69ce6872";
		// params.put("tag", schoolId);

		// 按学员推送,一次推送最多 1000 个。
		List<String> atidList = Lists.newArrayList();
		// atidList.add("4e7ed0927f00000122d555b296672cfc");
		// atidList.add("58db48cc7f00000122d555b20300555c");
		// atidList.add("58db48cc7f00000122d555b20300555c");

		atidList.add("B01CBAC355CE4260B3481E513B518899");
		atidList.add("8BA1062C74B5461485121391FF3836A0");
		atidList.add("06DDBB7A26F0410A89DC30E19843666B");
		atidList.add("B4E99185AC964C29866C17E5B44DA3E2");

		params.put("alias", atidList);
		smsSenderManager.smsSend(SmsSenderType.guangzhoushiyanxueyuan, params);
	}
}
