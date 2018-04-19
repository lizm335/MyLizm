/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dto;

import java.util.List;
import java.util.Map;

/**
 * 学员学情信息DTO<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月27日
 * @version 2.5
 * @since JDK 1.7
 */
public class StudentLearnDto {

    private String studentId;

    private String xm;

    private String sfzh;

    /**
     * 课程学习详情
     */
    private List<Map> learnCourseList;

    /**
     * @return the studentId
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * @param studentId
     *            the studentId to set
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * @return the xm
     */
    public String getXm() {
        return xm;
    }

    /**
     * @param xm
     *            the xm to set
     */
    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public List<Map> getLearnCourseList() {
        return learnCourseList;
    }

    public void setLearnCourseList(List<Map> learnCourseList) {
        this.learnCourseList = learnCourseList;
    }

}
