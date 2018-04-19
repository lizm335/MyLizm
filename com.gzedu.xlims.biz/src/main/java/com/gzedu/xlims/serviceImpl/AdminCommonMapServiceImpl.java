/**
 * @Package com.gzedu.xlims.serviceImpl 
 * @Project com.gzedu.xlims.biz
 * @File TestServiceImpl.java
 * @Date:2016年4月18日下午5:11:41
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.serviceImpl;

import org.springframework.stereotype.Service;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月31日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Service(value = "adminCommonMapService")
public class AdminCommonMapServiceImpl {
	//
	// // 常用的map 查询，注意：需要返回 id,name
	// public static final String SCHOOL_MAP = "SELECT ID,XXMC NAME FROM
	// GJT_SCHOOL_INFO where is_deleted = 'N'";
	// public static final String STUDYCENTER_MAP = "select ID,SC_NAME NAME from
	// GJT_STUDY_CENTER where is_deleted = 'N' and is_enabled = '1'";
	// public static final String SPECIALTY_MAP = "select SPECIALTY_ID ID,ZYMC
	// NAME from GJT_SPECIALTY where is_deleted = 'N' and is_enabled = '1'";
	// public static final String GRADE_MAP = "select Grade_Id ID,Grade_Name
	// NAME from gjt_grade where is_deleted = 'N' and is_enabled = '1'";
	// public static final String PYCC_MAP = "SELECT CODE ID,NAME FROM
	// TBL_SYS_DATA WHERE TBL_SYS_DATA.TYPE_CODE ='TrainingLevel' AND is_deleted
	// = 'N' ";
	// public static final String ROLL_TYPE_MAP = "SELECT CODE ID,NAME FROM
	// TBL_SYS_DATA WHERE TBL_SYS_DATA.TYPE_CODE ='StudentNumberStatus' AND
	// is_deleted = 'N' ";
	// public static final String HEADTEACHER_MAP = "SELECT EMPLOYEE_ID ID,XM
	// NAME FROM GJT_EMPLOYEE_INFO WHERE EMPLOYEE_TYPE="
	// + EmployeeTypeEnum.班主任.getNum() + " AND is_deleted = 'N' and is_enabled =
	// '1' ";
	// public static final String CLASS_MAP = "select class_id ID,bjmc NAME from
	// gjt_class_info WHERE is_deleted = 'N' and is_enabled = '1' ";
	//
	// @Override
	// public Map<String, String> getOrgMap() {
	// final String sql = "select id as id,org_name as name from gjt_org where
	// is_deleted = 'N' and is_enabled = '1'";
	// return getMap(sql);
	// }
	//
	// @Override
	// public Map<String, String> getSchoolInfoMap() {
	// return getMap(SCHOOL_MAP);
	// }
	//
	// @Override
	// public Map<String, String> getStudyCenterMap() {
	// return getMap(STUDYCENTER_MAP);
	// }
	//
	// @Override
	// public Map<String, String> getSpecialtyMap() {
	// return getMap(SPECIALTY_MAP);
	// }
	//
	// @Override
	// public Map<String, String> getGradeMap() {
	// return getMap(GRADE_MAP);
	// }
	//
	// @Override
	// public Map<String, String> getPyccMap() {
	// return getMap(PYCC_MAP);
	// }
	//
	// @Override
	// public Map<String, String> getRollTypeMap() {
	// return getMap(ROLL_TYPE_MAP);
	// }
	//
	// @Override
	// public Map<String, String> getHeadTeacherMap() {
	// return getMap(HEADTEACHER_MAP);
	// }
	//
	// @Override
	// public Map<String, String> getOrgMagagerRoleMap() {
	// List<String> roleCodes = new ArrayList();
	// roleCodes.add(RoleType.院校管理员.getCode());
	// roleCodes.add(RoleType.分校管理员.getCode());
	// roleCodes.add(RoleType.教务管理员.getCode());
	// roleCodes.add(RoleType.考务管理员.getCode());
	// roleCodes.add(RoleType.教学点管理员.getCode());
	// String codes = "";
	// for (String code : roleCodes) {
	// codes += code + ",";
	// }
	// codes.substring(0, codes.length() - 1);
	// String sql = "select role_id,role_name from pri_role_info where ROLE_CODE
	// IN (" + codes + ");";
	// return getMap(sql);
	// }
	//
	// @Override
	// public Map<String, String> getClassInfoMap() {
	// return getMap(CLASS_MAP);
	// }
	//
	// @Override
	// public Map<String, String> getOrgMapBy(boolean isChild) {
	// String sql;
	// if (!isChild) {
	// sql = "select a.id ,a.org_name as name from gjt_org a where (a.perent_id
	// is null or a.perent_id='fccafe375dba41608688bc28f5e0c91f') and
	// a.is_deleted = 'N'";
	// } else {
	// sql = "select a.id ,a.org_name as name from gjt_org a where a.perent_id
	// is not null and a.is_deleted = 'N'";
	// }
	// return getMap(sql);
	// }
	//
	// @Override
	// public Map<String, String> getEmployeeMap(EmployeeTypeEnum employeeType)
	// {
	// String sql = "SELECT EMPLOYEE_ID ID,XM NAME FROM GJT_EMPLOYEE_INFO WHERE
	// EMPLOYEE_TYPE=" + employeeType.getNum()
	// + " AND is_deleted = 'N'";
	// return getMap(sql);
	// }

}
