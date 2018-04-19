package com.gzedu.xlims.service.teachemployee;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtEmployeeInfo;

/**
 * Created by llx on 2017/2/17. 教务管理-教职人员service接口
 */
public interface GjtTeachEmployeeService {

	/**
	 * 根据当前用户类型，还有参数查询：教职人员
	 * 
	 * @param orgId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<GjtEmployeeInfo> queryAll(Map<String, Object> map, Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 新增教职人员
	 * 
	 * @param item
	 * @return
	 */
	boolean saveEmployee(GjtEmployeeInfo item);

	public List<Map<String, String>> queryHeardTeacher(String orgId, int employeeType);

}
