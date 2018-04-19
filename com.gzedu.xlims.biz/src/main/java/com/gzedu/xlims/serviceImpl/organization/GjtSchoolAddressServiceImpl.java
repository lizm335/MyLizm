package com.gzedu.xlims.serviceImpl.organization;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.organization.GjtSchoolAddressDao;
import com.gzedu.xlims.pojo.GjtSchoolAddress;
import com.gzedu.xlims.service.organization.GjtSchoolAddressService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 院校收货地址业务逻辑类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月15日
 * @version 2.5
 * @since JDK 1.7
 */
@Service
public class GjtSchoolAddressServiceImpl extends BaseServiceImpl<GjtSchoolAddress> implements GjtSchoolAddressService {

    @Autowired
    private GjtSchoolAddressDao gjtSchoolAddressDao;

    @Override
    protected BaseDao<GjtSchoolAddress, String> getBaseDao() {
        return this.gjtSchoolAddressDao;
    }

    @Override
    public GjtSchoolAddress queryByXxIdAndType(String xxId, int type) {
        return gjtSchoolAddressDao.findByXxIdAndTypeAndIsDeleted(xxId, type, Constants.BOOLEAN_NO);
    }

    @Override
    public boolean update(GjtSchoolAddress entity) {
        entity.setUpdatedDt(new Date());
        return gjtSchoolAddressDao.save(entity) != null;
    }
}
