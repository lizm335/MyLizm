package com.gzedu.xlims.dao.organization;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtSchoolAddress;

/**
 * 院校收货地址操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月15日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtSchoolAddressDao extends BaseDao<GjtSchoolAddress, String> {

	GjtSchoolAddress findByXxIdAndTypeAndIsDeleted(String xxId, int type, String isDeleted);

}
