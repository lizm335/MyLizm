/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dto;

import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

/**
 * 每日登录信息DTO<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年7月1日
 * @version 2.5
 * @since JDK 1.7
 */
public class CountLoginDto implements Comparable<CountLoginDto> {

    /**
     * 日期
     */
    private String dayName;

    /**
     * 登录人数
     */
    private BigDecimal studentLoginNum;

    public String getDayName() {
        return dayName;
    }

    public void setDAYNAME(String dayName) {
        this.dayName = dayName;
    }

    public BigDecimal getStudentLoginNum() {
        return studentLoginNum;
    }

    public void setSTUDENTLOGINNUM(BigDecimal studentLoginNum) {
        this.studentLoginNum = studentLoginNum;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof String) {
            return obj.equals(this.dayName);
        }
        return super.equals(obj);
    }

    @Override
    public int compareTo(CountLoginDto o) {
        try {
            Date thisDate = DateUtils.parseDate(this.dayName, "yyyy-MM-dd");
            Date oDate = DateUtils.parseDate(o.getDayName(), "yyyy-MM-dd");
            return thisDate.after(oDate) ? 1 : thisDate.before(oDate) ? -1 : 0;
        } catch (ParseException e) {

        }
        return 0;
    }
}
