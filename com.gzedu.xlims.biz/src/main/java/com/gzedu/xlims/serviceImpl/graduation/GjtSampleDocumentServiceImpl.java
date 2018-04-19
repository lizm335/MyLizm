package com.gzedu.xlims.serviceImpl.graduation;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.graduation.GjtSampleDocumentDao;
import com.gzedu.xlims.pojo.graduation.GjtSampleDocument;
import com.gzedu.xlims.pojo.status.DocumentTypeEnum;
import com.gzedu.xlims.service.graduation.GjtSampleDocumentService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月11日
 * @version 2.5
 * @since JDK 1.7
 */
@Service
public class GjtSampleDocumentServiceImpl extends BaseServiceImpl<GjtSampleDocument> implements GjtSampleDocumentService {

    @Autowired
    private GjtSampleDocumentDao gjtSampleDocumentDao;

    @Override
    protected BaseDao<GjtSampleDocument, String> getBaseDao() {
        return gjtSampleDocumentDao;
    }

    @Override
    public String updateRandomGraduationDoc(String xxId, String specialtyId) {
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("EQ_documentType", DocumentTypeEnum.DOCUMENT_TYPE01.getCode());
        searchParams.put("EQ_orgId", xxId);
        searchParams.put("EQ_gjtSpecialty.specialtyId", specialtyId);
        int count = (int) super.count(searchParams);
        if(count > 0) {
            // 随机获取一个示例
            int page = (new Random()).nextInt(count);
            PageRequest pageRequest = new PageRequest(page, 1);
            Page<GjtSampleDocument> infos = super.queryByPage(searchParams, pageRequest);
            GjtSampleDocument info = infos.getContent().get(0);
            // 下载次数+1
            info.setDownloadNum(info.getDownloadNum() + 1);
            gjtSampleDocumentDao.save(info);
            return info.getDocumentUrl();
        }
        return null;
    }

    @Override
    public boolean deleteInBatch(String[] ids) {
        if (ids != null) {
            for (String id : ids) {
                gjtSampleDocumentDao.delete(id);
            }
        }
        return true;
    }

}
