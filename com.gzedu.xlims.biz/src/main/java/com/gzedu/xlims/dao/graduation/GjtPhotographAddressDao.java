/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.graduation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.graduation.GjtPhotographAddress;

import java.util.List;

/**
 * 功能说明：拍摄点地址信息
 *
 * @author 张伟文 zhangeweiwen@eenet.com
 * @version 3.0
 * @Date 2018年3月28日
 */
public interface GjtPhotographAddressDao
        extends JpaRepository<GjtPhotographAddress, String>, JpaSpecificationExecutor<GjtPhotographAddress> {

    @Modifying
    @Transactional
    @Query("update GjtPhotographAddress g set g.isDeleted='Y' where g.id=:id ")
    public int deleteById(@Param("id") String id);

    @Modifying
    @Transactional
    @Query("update GjtPhotographAddress g set g.isEnabled=:isEnabled where g.id=:id ")
    public int updateEnabled(@Param("id") String id, @Param("isEnabled") String isEnabled);

    @Query(nativeQuery = true, value = "SELECT D.ID FROM GJT_DISTRICT D WHERE D.PID IN(SELECT c.ID FROM GJT_DISTRICT c WHERE c.PID =(SELECT ID FROM GJT_DISTRICT WHERE NAME =:EQ_province))")
    public List<String> getIdByProvince(@Param("EQ_province") String EQ_province);

    @Query(nativeQuery = true, value = "SELECT D.ID FROM GJT_DISTRICT D WHERE D.PID IN(SELECT d.PID FROM GJT_DISTRICT d WHERE d.PID =(SELECT ID FROM GJT_DISTRICT WHERE NAME =:EQ_city))")
    public List<String> getIdByCity(@Param("EQ_city") String EQ_city);
}
