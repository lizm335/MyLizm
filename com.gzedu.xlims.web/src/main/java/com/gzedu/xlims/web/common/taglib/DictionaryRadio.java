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
import java.util.List;

/**
 * 数据字典单选按钮控件<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月7日
 * @version 2.5
 * @since JDK 1.7
 */
public class DictionaryRadio extends TagSupport {

    private String typeCode;//999

    private String code;//选中该code的值

    private String name;// html element name

    private String defaultCode;// 默认的code值

    private String excludeCode;// 排除的code值

    private String otherAttrs;// html element other attribute

    @Override
    public int doStartTag() throws JspException {
        // Spring 上下文
        ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(super.pageContext.getServletContext());

        CacheService cacheService = appContext.getBean(CacheService.class);
        List<CacheService.Value> valueList = cacheService.getCachedDictionary(typeCode);

        try {
            boolean fristFlag = true;
            JspWriter out = super.pageContext.getOut();
            out.write("<div class='full-width'>");
            for (CacheService.Value v : valueList) {
                if(v.getCode().equals(excludeCode)) continue;
                if(v.getCode().equals(code)) {
                    // code优先于defaultCode
                    defaultCode = null;
                }
                if(fristFlag) {
                    out.write("<label class='text-no-bold'>");
                    fristFlag = false;
                } else {
                    out.write("<label class='text-no-bold left10'>");
                }
                out.write("<input type='radio' name='"+name+"' value='"+v.getCode()+"' " + (v.getCode().equals(code)||v.getCode().equals(defaultCode)?"checked":"") + " " + StringUtils.defaultString(otherAttrs)+" /> "+v.getName());
                out.write("</label>");
            }
            out.write("</div>");
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

    public void setDefaultCode(String defaultCode) {
        this.defaultCode = defaultCode;
    }

    public void setExcludeCode(String excludeCode) {
        this.excludeCode = excludeCode;
    }

    public void setOtherAttrs(String otherAttrs) {
        this.otherAttrs = otherAttrs;
    }
}
