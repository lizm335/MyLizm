package com.ouchgzee.headTeacher.service;

import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by paul on 2017/2/14.
 *  班主任对外接口服务
 */
@Deprecated public interface BzrEmployeeWebService {

    List classList(BzrGjtEmployeeInfo employeeInfo);

    Map stuList(String classId ,String oldStus);

}
