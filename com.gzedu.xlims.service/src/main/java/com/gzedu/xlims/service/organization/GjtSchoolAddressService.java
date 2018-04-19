package com.gzedu.xlims.service.organization;

import com.gzedu.xlims.pojo.GjtSchoolAddress;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 院校收货地址业务逻辑类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月15日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtSchoolAddressService extends BaseService<GjtSchoolAddress> {

	/**
	 * 查询院校收货地址信息
	 * @param xxId
	 * @param type
	 * @return
	 */
	GjtSchoolAddress queryByXxIdAndType(String xxId, int type);

	/**
     * 修改院校收货地址信息
	 * @param info
     * @return
     */
    boolean update(GjtSchoolAddress info);
}
