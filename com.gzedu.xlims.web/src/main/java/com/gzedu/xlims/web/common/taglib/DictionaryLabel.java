/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.common.taglib;

import com.gzedu.xlims.service.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 数据字典文本控件<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月7日
 * @version 2.5
 * @since JDK 1.7
 */
public class DictionaryLabel extends TagSupport {

    private String typeCode;//999

    private String code;//选中该code的值

    @Override
    public int doStartTag() throws JspException {
        if (StringUtils.isBlank(code)) return super.SKIP_BODY;
        // Spring 上下文
        ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(super.pageContext.getServletContext());

        CacheService cacheService = appContext.getBean(CacheService.class);

        try {
            JspWriter out = super.pageContext.getOut();
            out.write(cacheService.getCachedDictionaryName(typeCode, code));
            out.flush();
        } catch (Exception e) {

        }
        return super.SKIP_BODY;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
