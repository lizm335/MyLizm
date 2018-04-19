package com.gzedu.xlims.third.weixin;

import java.util.Map;

/**
 * 微信公众号消息发送<br/>
 */
public interface IMessageSender {

    /**
     * 发送消息模板<br/>
     * 接口文档见：http://api.saas.workeredu.com/index.php?act=api&tag=15#info_api_f4b9ec30ad9f68f89b29639786cb62ef<br/>
     * @param publicAccounts 公众号编号
     * @param openid 接收人openid
     * @param tplid 微擎的消息模板ID
     * @param msgTemplateParams 微擎的消息模板参数
     * @return
     */
    boolean sendTemplete(String publicAccounts, String openid, String tplid, Map<String, Object> msgTemplateParams);

    /**
     * 发送客服消息<br/>
     * 接口文档见：http://api.saas.workeredu.com/index.php?act=api&tag=15#info_api_5878a7ab84fb43402106c575658472fa<br/>
     * @param publicAccounts 公众号编号
     * @param openid 接收人openid
     * @param msgTemplate 自己定义的消息模板 见：com.gzedu.xlims.third.weixin.MessageTemplate
     * @param msgTemplateParams 自己定义的消息模板参数
     * @return
     */
    boolean sendCustomMsg(String publicAccounts, String openid, MessageTemplate msgTemplate, Object... msgTemplateParams);

}
