package org.com.gzedu.xlims.third.weixin;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.third.weixin.IMessageSender;
import com.gzedu.xlims.third.weixin.MessageTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 发送微信公众号消息
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:weixin.xml" })
public class WxMsgTest {

	@Resource
	private IMessageSender messageSender;

	/**
	 * test
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testSendCustomMsg() throws UnsupportedEncodingException {
		boolean flag = messageSender.sendCustomMsg("25", "o9I6xsywTcHsLo1mrC1AN9tj8U2g", MessageTemplate.TEST);
		Assert.assertTrue(flag);
	}

	/**
	 * 发送一条带链接的消息
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testSendCustomMsg2() throws UnsupportedEncodingException {
		// 第一个参数可以从配置文件中拿取 AppConfig.getProperty("wx.publicAccounts.student")
		boolean flag = messageSender.sendCustomMsg("25", "o9I6xsywTcHsLo1mrC1AN9tj8U2g", MessageTemplate.FEEDBACK_REPLY,
				"http://test.study.tt.gzedu.com/wx/studentOauth/to?orgCode=041&url=/wx/weixin/faq/transfer.html?path="
						+ URLEncoder.encode("/wx/weixin/faq/student/faq_list.html?orgCode=041&tid=1", Constants.CHARSET));
		Assert.assertTrue(flag);
	}
}
