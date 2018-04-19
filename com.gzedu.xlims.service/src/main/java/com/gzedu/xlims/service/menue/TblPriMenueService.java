/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.menue;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.TblPriMenue;

/**
 * 
 * 功能说明：菜单接口
 * 
 * @author liming
 * @Date 2016年4月27日
 * @version 2.5
 *
 */
public interface TblPriMenueService {
	/**
	 * 分页查询
	 * 
	 * @param searchParams
	 *            查询条件
	 * @param pageRequest
	 *            分页对象
	 * @return
	 */
	Page<TblPriMenue> queryAll(TblPriMenue menue, PageRequest pageRequest);

	/**
	 * 
	 * @param menue
	 * @return
	 */
	TblPriMenue insertMenue(TblPriMenue menue) throws RuntimeException;

	List<TblPriMenue> queryMenueByIsLeaf();

}
