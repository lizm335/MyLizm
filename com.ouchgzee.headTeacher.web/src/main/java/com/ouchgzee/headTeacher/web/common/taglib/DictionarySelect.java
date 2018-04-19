/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.common.taglib;

import com.ouchgzee.headTeacher.service.BzrCacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;

/**
 * 数据字典下拉列表控件<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月7日
 * @version 2.5
 * @since JDK 1.7
 */
public class DictionarySelect extends TagSupport {

    private String typeCode;//999

    private String code;//选中该code的值

    private String name;// html element name

    private String otherAttrs;// html element other attribute

    @Override
    public int doStartTag() throws JspException {
        // Spring 上下文
        ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(super.pageContext.getServletContext());

        BzrCacheService cacheService = appContext.getBean(BzrCacheService.class);
        List<BzrCacheService.Value> valueList = cacheService.getCachedDictionary(typeCode);

        try {
            JspWriter out = super.pageContext.getOut();
            out.write("<select name='"+name+"' " + StringUtils.defaultString(otherAttrs) +">");
            out.write("<option value=''>- 请选择 -</option>");
            for (BzrCacheService.Value v : valueList) {
                out.write("<option value='"+v.getCode()+"' " + (v.getCode().equals(code)?"selected":"") + ">"+v.getName()+"</option>");
            }
            out.write("</select>");
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

    public void setName(String name) {
        this.name = name;
    }

    public void setOtherAttrs(String otherAttrs) {
        this.otherAttrs = otherAttrs;
    }
}
