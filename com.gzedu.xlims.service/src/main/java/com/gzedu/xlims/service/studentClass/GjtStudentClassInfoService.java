package com.gzedu.xlims.service.studentClass;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface GjtStudentClassInfoService {

    /**
     * 教学班查询-->对应接口平台api中旧接口：/api/clazz/search.do
     * @param searchParams
     * @param pageRequest
     * @return
     */
    Page getTeachClassInfo(Map<String,Object> searchParams, PageRequest pageRequest);

    /**
     * 获取教学班学员信息
     * @param searchParams
     * @param pageRequest
     * @return
     */
    Page listTeachClassStudentInfo(Map<String,Object> searchParams,PageRequest pageRequest);

    /**
     * 获取教学班信息
     * @param searchParams
     * @return
     */
    Map getTeachClassInfo(Map<String,Object> searchParams);


    /**
     * 获取学员是否已完善资料<br/>
     * 1-已完善
     * 0-未完善，进入第一步标识-确认个人信息
     * 2-进入第二步标识-确认通讯信息
     * 3-进入第三步标识-确认报读信息
     * 4-进入第四步标识-确认原最高学历
     * 5-进入第五步标识-确认证件信息
     * 6-进入第六步标识-确认签名
     * @param searchParams
     * @return
     */
    int isPerfect(Map<String, Object> searchParams);


    /**
     * 批量获取学员学籍资料
     * @param searchParams
     * @return
     */
    List<Map<String,Object>> queryStudentSignupInfoByAtIds(Map<String,Object> searchParams);


    /**
     * 批量获取学员学籍资料（带分页）
     * @param searchParams
     * @param pageRequest
     * @return
     */
    Page<Map<String,Object>> queryStudentSignupInfoByPage(Map<String,Object> searchParams,PageRequest pageRequest);

    /**
     * 获取学员学籍数量
     * @param searchParams
     * @return totalElements-总学籍数
     * 			waitCount-待审核学籍数
     *          passCount-已通过学籍数
     *          noPassCount-不通过学籍数
     *          currentRoleWaitCount-当前角色的待审核学籍数
     *          currentRolePassCount-当前角色的已通过学籍数
     *          currentRoleNoPassCount-当前角色的不通过学籍数
     *          noPerfectCount-资料未完善学籍数
     */
    List<Map<String,Object>> countStudentSignupNum(Map<String, Object> searchParams);

    /**
     * 获取所有角色的学员学籍数量
     * @param searchParams
     * @return totalElements-总学籍数
     * 			waitCount-待审核学籍数
     *          passCount-已通过学籍数
     *          noPassCount-不通过学籍数
     *          currentRoleWaitCount-当前角色的待审核学籍数
     *          currentRolePassCount-当前角色的已通过学籍数
     *          currentRoleNoPassCount-当前角色的不通过学籍数
     *          noPerfectCount-资料未完善学籍数
     */
    List<Map<String,Object>> countStudentSignupNumAll(Map<String, Object> searchParams);

    /**
     * 查询学员是否存在
     * @param searchParams
     * @return
     */
    Map queryStudentInfo(Map<String,Object> searchParams);

    List<Map<String, Object>> getOrgAll();

    List<Map<String,Object>> getOrgByCodes(String[] codes);

    List<Map<String, Object>> queryGradeSpecialt(Map<String, Object> searchParams);

    Map getOrgByCollegeCode(String collegeCode);

    List<Map<String, Object>> queryGradeList(String xxId);

}
