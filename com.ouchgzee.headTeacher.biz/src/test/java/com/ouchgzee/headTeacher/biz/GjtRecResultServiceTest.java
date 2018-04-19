package com.ouchgzee.headTeacher.biz;

import com.ouchgzee.headTeacher.dto.StudentLearnDto;
import com.ouchgzee.headTeacher.dto.StudentRecResultDto;
import com.ouchgzee.headTeacher.service.exam.BzrGjtRecResultService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约选课考试
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtRecResultServiceTest {

    private final String CLASS_ID = "59e3a89436a4449baa3e77a3c1f1eeb6";

    @Autowired
    private BzrGjtRecResultService gjtRecResultService;

    /**
     * 预约选课考试列表页面
     */
    @Test
    @Transactional(value="transactionManagerBzr", readOnly = true)
    public void queryStudentRecResultByPage() {
        Map<String, Object> searchParams = new HashMap();
//        searchParams.put("xm", "张");
//        // 考点状态 1.已预约 2. 未预约
//        searchParams.put("examState", 2);

        Page<StudentRecResultDto> page = gjtRecResultService.queryStudentRecResultByPage(CLASS_ID, searchParams, new PageRequest(0, 10));

        System.err.println(page.getNumberOfElements());
        System.err.println(page.getTotalElements());
        System.err.println(page.getTotalPages());

        System.err.println("ID\t学员姓名\t年级\t报读产品\t可预约考试数\t已预约考试数\t已预约考点\t操作");
        for (StudentRecResultDto info : page.getContent()) {
            System.err.print(info.getStudentId() + "\t");
            System.err.print(info.getXm() + "\t");
            System.err.print(info.getGradeName() + "\t");
            System.err.print(info.getZymc() + "\t");
            System.err.print(info.getCanExamNum() + "\t");
            System.err.print(info.getAlreadyExamNum() + "\t");
            System.err.print(info.getExamPointName() + "\t");
            System.err.println();
        }
        Assert.notEmpty(page.getContent());
    }

    /**
     * 课程预约详情
     */
    @Test
    @Transactional(value="transactionManagerBzr", readOnly = true)
    public void queryStudentRecResultDetail() {
        List<Map> recCourseList = gjtRecResultService.queryStudentRecResultDetail("99ff74466afa4f979ef38ac403435bf4");
        for (Map m : recCourseList) {
            System.err.println(m);
        }
    }

    /**
     * 学员学情列表页面
     */
    @Test
    public void queryLearningSituation() {
        Map<String, Object> searchParams = new HashMap();
//		searchParams.put("EQ_gjtUserAccount.loginAccount", "20160930");
        searchParams.put("LIKE_xm", "溢");

        Page<StudentLearnDto> page = gjtRecResultService.queryLearningSituationByClassIdPage(CLASS_ID, searchParams, new PageRequest(0, 10));

        System.err.println(page.getNumberOfElements());
        System.err.println(page.getTotalElements());
        System.err.println(page.getTotalPages());

        System.err.println("ID\t学员姓名\t学习总次数\t学习总时长\t上次学习间隔天数\t已完成课程/当前应完成\t已通过考试/当前应通过\t操作");
        for (StudentLearnDto info : page.getContent()) {
            System.err.print(info.getStudentId() + "\t");
            System.err.print(info.getXm() + "\t");
            System.err.println();
        }
        Assert.notEmpty(page.getContent());
    }

    /**
     * 学员学情详情
     */
    @Test
    public void queryStudentRecResultLearningDetail() {
        List<Map> list = gjtRecResultService.queryStudentRecResultLearningDetail("99ff74466afa4f979ef38ac403435bf4");

        for (Map info : list) {
            System.err.println(info);
        }
        Assert.notEmpty(list);
    }

}
