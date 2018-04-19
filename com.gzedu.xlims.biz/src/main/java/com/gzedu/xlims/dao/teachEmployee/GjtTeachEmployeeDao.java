package com.gzedu.xlims.dao.teachEmployee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtEmployeeInfo;

/**
 * Created by llx on 2017/2/17. 教务管理-教职人员dao
 */
public interface GjtTeachEmployeeDao
		extends JpaRepository<GjtEmployeeInfo, String>, JpaSpecificationExecutor<GjtEmployeeInfo> {
}
