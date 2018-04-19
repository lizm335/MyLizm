package com.gzedu.xlims.serviceImpl.studentClass;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.studentClass.GjtStudentClassInfoDao;
import com.gzedu.xlims.service.studentClass.GjtStudentClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GjtStudentClassInfoServiceImpl implements GjtStudentClassInfoService{

    @Autowired
    private GjtStudentClassInfoDao gjtStudentClassInfoDao;


    /**
     * 教学班查询
     * @param searchParams
     * @param pageRequest
     * @return
     */
    @Override
    public Page getTeachClassInfo(Map<String, Object> searchParams, PageRequest pageRequest) {
        return gjtStudentClassInfoDao.getTeachClassInfo(searchParams,pageRequest);
    }

    /**
     * 获取教学班学员信息
     * @param searchParams
     * @param pageRequest
     * @return
     */
    @Override
    public Page listTeachClassStudentInfo(Map<String, Object> searchParams, PageRequest pageRequest) {
        return gjtStudentClassInfoDao.listTeachClassStudentInfo(searchParams,pageRequest);
    }

    /**
     * 获取教学班信息
     * @param searchParams
     * @return
     */
    @Override
    public Map getTeachClassInfo(Map<String, Object> searchParams) {
        Map resultMap = new LinkedHashMap();
        try {
            List list = gjtStudentClassInfoDao.getTeachClassInfo(searchParams);
            if (EmptyUtils.isNotEmpty(list)){
                resultMap.put("data",list);
            }else {
                resultMap.put("data",new ArrayList());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

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
    @Override
    public int isPerfect(Map<String, Object> searchParams) {
        try {
            Map<String, Object> info = gjtStudentClassInfoDao.isPerfect(searchParams);
            return ((BigDecimal) info.get("PERFECT_STATUS")).intValue();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 批量获取学员学籍资料
     * @param searchParams
     * @return
     */
    @Override
    public List<Map<String, Object>> queryStudentSignupInfoByAtIds(Map<String, Object> searchParams) {
        List list = gjtStudentClassInfoDao.queryStudentSignupInfoByAtIds(searchParams);
        return list;
    }


    /**
     * 批量获取学员学籍资料(带分页)
     * @param searchParams
     * @param pageRequest
     * @return
     */
    @Override
    public Page<Map<String, Object>> queryStudentSignupInfoByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
        return gjtStudentClassInfoDao.queryStudentSignupInfoByPage(searchParams,pageRequest);
    }


    /**
     * 查询学员是否存在
     * @param searchParams
     * @return
     */
    @Override
    public Map queryStudentInfo(Map<String, Object> searchParams) {
        Map resultMap = new LinkedHashMap();
        try {
            List list = gjtStudentClassInfoDao.queryStudentInfo(searchParams);
            if (EmptyUtils.isNotEmpty(list)){
                resultMap = (Map) list.get(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public List<Map<String,Object>> countStudentSignupNum(Map<String, Object> searchParams) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(gjtStudentClassInfoDao.countStudentSignupNum(searchParams));
        return list;
    }

    @Override
    public List<Map<String, Object>> countStudentSignupNumAll(Map<String, Object> searchParams) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        searchParams.put("FLOW_AUDIT_OPERATOR_ROLE", 2);
        list.add(gjtStudentClassInfoDao.countStudentSignupNum(searchParams));
        searchParams.put("FLOW_AUDIT_OPERATOR_ROLE", 3);
        list.add(gjtStudentClassInfoDao.countStudentSignupNum(searchParams));
        searchParams.put("FLOW_AUDIT_OPERATOR_ROLE", 4);
        list.add(gjtStudentClassInfoDao.countStudentSignupNum(searchParams));
        return list;
    }


    @Override
    public List<Map<String, Object>> getOrgByCodes(String[] codes) {
        return gjtStudentClassInfoDao.getOrgByCodes(codes);
    }

    @Override
    public List<Map<String, Object>> queryGradeSpecialt(Map<String, Object> searchParams) {
        return gjtStudentClassInfoDao.queryGradeSpecialt(searchParams);
    }

    @Override
    public List<Map<String, Object>> getOrgAll() {
        return gjtStudentClassInfoDao.getOrgAll();
    }

    @Override
    public Map getOrgByCollegeCode(String collegeCode) {
        return gjtStudentClassInfoDao.getOrgByCollegeCode(collegeCode);
    }

    @Override
    public List<Map<String, Object>> queryGradeList(String xxId) {
        return gjtStudentClassInfoDao.queryGradeList(xxId);
    }
}
