package com.gzedu.xlims.dao.menue;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.TblPriMenue;

/**
 * 
 * 功能说明：菜单
 * 
 * @author liming
 * @Date 2016年5月4日
 * @version 1.0
 *
 */
public interface TblPriMenueDao extends JpaRepository<TblPriMenue, String>, JpaSpecificationExecutor<TblPriMenue> {

	List<TblPriMenue> findByIsDeleted(String isDeleted);
}