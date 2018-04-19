package com.gzedu.xlims.serviceImpl.graduation;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.ResultFeedback;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.graduation.GjtGraPlanFlowRecordDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationPlanDao;
import com.gzedu.xlims.pojo.graduation.GjtGraPlanFlowRecord;
import com.gzedu.xlims.pojo.graduation.GjtGraduationPlan;
import com.gzedu.xlims.service.graduation.GjtGraduationPlanService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

/**
 * 毕业计划业务逻辑<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月31日
 * @version 3.0
 * @since JDK 1.7
 */
@Service
public class GjtGraduationPlanServiceImpl extends BaseServiceImpl<GjtGraduationPlan> implements GjtGraduationPlanService {

    @Autowired
    private GjtGraduationPlanDao gjtGraduationPlanDao;

    @Autowired
    private GjtGraPlanFlowRecordDao gjtGraPlanFlowRecordDao;

    @Override
    protected BaseDao<GjtGraduationPlan, String> getBaseDao() {
        return this.gjtGraduationPlanDao;
    }

    @Override
    public Page<GjtGraduationPlan> queryGraduationPlanListByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
        String nowYmd = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
        Date now = null;
        try {
            now = DateUtils.getYMDToString(nowYmd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (pageRequest.getSort() == null) {
            pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
                    new Sort(Sort.Direction.DESC, "createdDt"));
        }

        Page<GjtGraduationPlan> pageInfos = gjtGraduationPlanDao.findAll(getSpecification(searchParams), pageRequest);
        for(Iterator<GjtGraduationPlan> iter = pageInfos.iterator(); iter.hasNext(); ) {
            GjtGraduationPlan plan = iter.next();
            if(plan.getAuditState().intValue() == 1) {
                if (now.before(plan.getGraApplyBeginDt())) {
                    plan.setStatus(3);
                } else if(now.before(plan.getDegreeCertReceiveEndDt()) || now.equals(plan.getDegreeCertReceiveEndDt())) {
                    plan.setStatus(4);
                } else {
                    plan.setStatus(5);
                }
            } else {
                plan.setStatus(plan.getAuditState().intValue());
            }
        }
        return pageInfos;
    }

    @Override
    public Map<String, Long> countGroupbyAuditState(Map<String, Object> searchParams) {
        Map<String, Long> result = new HashMap<String, Long>();
        Map<String, Object> searchParamsNew = new HashMap<String, Object>();
        searchParamsNew.putAll(searchParams);
        int status = NumberUtils.toInt((String) searchParamsNew.remove("EQ_status"), -1);
        result.put("", gjtGraduationPlanDao.count(getSpecification(searchParamsNew)));
        searchParamsNew.put("EQ_status", 0);
        result.put("0", gjtGraduationPlanDao.count(getSpecification(searchParamsNew)));
        searchParamsNew.put("EQ_status", 2);
        result.put("2", gjtGraduationPlanDao.count(getSpecification(searchParamsNew)));
        searchParamsNew.put("EQ_status", 3);
        result.put("3", gjtGraduationPlanDao.count(getSpecification(searchParamsNew)));
        searchParamsNew.put("EQ_status", 4);
        result.put("4", gjtGraduationPlanDao.count(getSpecification(searchParamsNew)));
        searchParamsNew.put("EQ_status", 5);
        result.put("5", gjtGraduationPlanDao.count(getSpecification(searchParamsNew)));
        return result;
    }

    /**
     * 公共条件拼接
     * @param searchParams
     * @return
     */
    private Criteria<GjtGraduationPlan> getSpecification(Map<String, Object> searchParams) {
        String nowYmdhms = DateFormatUtils.ISO_DATE_FORMAT.format(new Date()) + " 00:00:00";
        Criteria<GjtGraduationPlan> spec = new Criteria();
        spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
        Map<String, Object> searchParamsNew = new HashMap<String, Object>();
        searchParamsNew.putAll(searchParams);
        int status = NumberUtils.toInt(searchParamsNew.remove("EQ_status") + "", -1);
        if(status == -1) {

        } else if(status == 3) {
            spec.add(Restrictions.gt("graApplyBeginDt", nowYmdhms, true));
        } else if(status == 4) {
            spec.add(Restrictions.lte("graApplyBeginDt", nowYmdhms, true));
            spec.add(Restrictions.gte("degreeCertReceiveEndDt", nowYmdhms, true));
        } else if(status == 5) {
            spec.add(Restrictions.lt("degreeCertReceiveEndDt", nowYmdhms, true));
        } else {
            spec.add(Restrictions.eq("auditState", status, true));
        }
        spec.addAll(Restrictions.parse(searchParamsNew));
        return spec;
    }

    @Override
    public boolean updateGraduationPlan(GjtGraduationPlan entity) {
        if(entity.getId() != null) {
            entity.setUpdatedDt(new Date());
            gjtGraduationPlanDao.save(entity);
            return true;
        }
        return false;
    }

    @Override
    public GjtGraduationPlan findByTermId(String termId, String xxId) {
        return gjtGraduationPlanDao.findByGjtGradeGradeIdAndXxIdAndIsDeleted(termId, xxId, Constants.BOOLEAN_NO);
    }

    @Override
    public boolean delete(String id, String updatedBy) {
        GjtGraduationPlan info = gjtGraduationPlanDao.findOne(id);
        info.setIsDeleted(Constants.BOOLEAN_YES);
        info.setUpdatedBy(updatedBy);
        info.setUpdatedDt(new Date());
        gjtGraduationPlanDao.save(info);
        return true;
    }

    @Override
    public List<GjtGraPlanFlowRecord> queryGraPlanFlowRecordByPlanId(String planId) {
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("EQ_graduationPlanId", planId);

        Criteria<GjtGraPlanFlowRecord> spec = new Criteria();
        spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
        spec.addAll(Restrictions.parse(searchParams));
        return gjtGraPlanFlowRecordDao.findAll(spec, new Sort("auditDt"));
    }

    @Override
    public boolean initAuditGraduationPlan(String planId) {
        GjtGraduationPlan info = gjtGraduationPlanDao.findOne(planId);
        if(info != null) {
            List<GjtGraPlanFlowRecord> list = queryGraPlanFlowRecordByPlanId(planId);
            // 没有审核记录或者审核不通过则初始化审核
            if(list == null || list.size() == 0 || list.get(list.size() - 1).getAuditState().intValue() == 2) {
                Date now = new Date();
                
                if(list.size() > 0 && list.get(list.size() - 1).getAuditState().intValue() == 2) {
	                // 审核状态改为待审核
	                info.setAuditState(0);
	                gjtGraduationPlanDao.save(info);
                }

                GjtGraPlanFlowRecord entity = new GjtGraPlanFlowRecord();
                entity.setFlowRecordId(UUIDUtils.random());
                entity.setGraduationPlanId(planId);
                entity.setAuditState(1); // 默认审核通过
                entity.setAuditDt(now);
                // 审核角色：自己
                entity.setAuditOperatorRole(1);
                gjtGraPlanFlowRecordDao.save(entity);

                GjtGraPlanFlowRecord entity2 = new GjtGraPlanFlowRecord();
                entity2.setFlowRecordId(UUIDUtils.random());
                entity2.setGraduationPlanId(planId);
                entity2.setAuditState(0); // 默认待审核
                // 下一个审核角色：教务管理员
                entity2.setAuditOperatorRole(5);
                gjtGraPlanFlowRecordDao.save(entity2);
                return true;
            }
        }
        return false;
    }

    @Override
    public ResultFeedback auditGraduationPlan(String planId, Integer auditState, String auditContent, int operatorRole, String operatorRealName) {
        ResultFeedback feedback = new ResultFeedback(false, null);
        boolean pass = false;
        if((pass = "1".equals(auditState)) || "2".equals(auditState)) {
            if (operatorRole == 5) {
                Date now = new Date();
                List<GjtGraPlanFlowRecord> list = queryGraPlanFlowRecordByPlanId(planId);
                if (list != null && list.size() > 0 ) {
                    GjtGraPlanFlowRecord gjtFlowRecord = list.get(list.size() - 1);
                    if (gjtFlowRecord.getAuditOperatorRole().intValue() == operatorRole) {
                        gjtFlowRecord.setAuditState(auditState);
                        gjtFlowRecord.setAuditContent(auditContent);
                        gjtFlowRecord.setAuditOperator(operatorRealName);
                        gjtFlowRecord.setAuditDt(now);
                        GjtGraPlanFlowRecord result = gjtGraPlanFlowRecordDao.save(gjtFlowRecord);
                        if (result != null) {
                            // 如果审核通过，则增加一条下一个审核记录
                            if (pass) {
                                gjtGraduationPlanDao.auditGraduationPlan(planId, new BigDecimal(auditState), operatorRealName, now);
                            }
                            // 如果审核不通过，则更新最终审核状态audit_state
                            else {
                                gjtGraduationPlanDao.auditGraduationPlan(planId, new BigDecimal(auditState), operatorRealName, now);
                            }
                            feedback.setSuccessful(true);
                            return feedback;
                        }
                    } else {
                        feedback.setMessage("当前审核角色不匹配");
                    }
                } else {
                    feedback.setMessage("待提交");
                }
            } else {
                feedback.setMessage("审核角色参数错误");
            }
        } else {
            feedback.setMessage("操作异常");
        }
        return feedback;
    }

	@Override
	public GjtGraduationPlan queryByTermId(String currentGradeId) {
		return gjtGraduationPlanDao.findByTermId(currentGradeId);		
	}

}
