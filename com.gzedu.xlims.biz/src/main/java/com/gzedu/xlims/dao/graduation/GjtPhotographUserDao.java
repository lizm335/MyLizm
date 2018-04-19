/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.graduation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.graduation.GjtPhotographUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 功能说明：学员预约的拍摄点
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月28日
 * @version 3.0
 *
 */
public interface GjtPhotographUserDao
		extends JpaRepository<GjtPhotographUser, String>, JpaSpecificationExecutor<GjtPhotographUser> {

	GjtPhotographUser findByUserId(String userId);

	@Query(nativeQuery = true, value = "SELECT * FROM GJT_PHOTOGRAPH_USER WHERE USER_ID =:userId AND PHOTOGRAPH_ID =:photographAddressId")
	public GjtPhotographUser findByUserIdAndAddressId(@Param("userId") String userId,@Param("photographAddressId")String photographAddressId);


}
