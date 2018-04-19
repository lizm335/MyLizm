package com.ouchgzee.headTeacher.dao;

import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;

import java.util.List;

/**
 * Created by paul on 2017/2/14.
 */
public interface EmployeeWebServiceDao {

    List classList(BzrGjtEmployeeInfo employeeInfo);

    List stuList(String classId );
}
