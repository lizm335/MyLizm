package com.gzedu.xlims.serviceImpl.graduation;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gzedu.xlims.dao.graduation.*;
import com.gzedu.xlims.pojo.graduation.GjtApplyDegreeCertif;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.gzedu.xlims.common.*;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.graduation.*;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.graduation.GjtApplyDegreeCertif;
import com.gzedu.xlims.pojo.graduation.GjtGraApplyFlowRecord;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyCertif;
import com.gzedu.xlims.service.graduation.GjtApplyDegreeCertifService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyCertifService;
import com.gzedu.xlims.service.graduation.GjtGraduationRegisterService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class GjtGraduationApplyCertifServiceImpl extends BaseServiceImpl<GjtGraduationApplyCertif>
		implements GjtGraduationApplyCertifService {

	private static final Logger log = LoggerFactory.getLogger(GjtGraduationApplyCertifServiceImpl.class);

	@Autowired
	private GjtGraduationApplyCertifDao gjtGraduationApplyCertifDao;

	@Autowired
	private GjtApplyDegreeCertifDao gjtApplyDegreeCertifDao;

	@Autowired
	private GjtGraduationNativeDao gjtGraduationNativeDao;

	@Autowired
	private GjtGraApplyFlowRecordDao gjtGraApplyFlowRecordDao;

	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;

	@Autowired
	private GjtGraduationRegisterService gjtGraduationRegisterService;

	@Autowired
	public GjtOrgDao gjtOrgDao;

	@Autowired
	private GjtGraduationDao gjtGraduationDao;

	@Autowired
	private GjtGraduationPlanDao gjtGraduationPlanDao;

	@Autowired
	private GjtGraduationRegisterDao gjtGraduationRegisterDao;

	@Override
	protected BaseDao<GjtGraduationApplyCertif, String> getBaseDao() {
		return this.gjtGraduationApplyCertifDao;
	}

	@Override
	public Page<GjtGraduationApplyCertif> queryPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequest + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtGraduationApplyCertif> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtGraduationApplyCertif.class);
		return gjtGraduationApplyCertifDao.findAll(spec, pageRequest);
	}

	@Override
	public long queryPageCount(Map<String, Object> searchParams) {
		log.info("searchParams：{}", searchParams);
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtGraduationApplyCertif> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtGraduationApplyCertif.class);
		return gjtGraduationApplyCertifDao.count(spec);
	}

	@Override
	public Page<GjtGraduationApplyCertif> queryGraduationApplyCardByPage(Map<String, Object> searchParams,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Map<String, Object> searchParamsNew = new HashMap<String, Object>();
		searchParamsNew.putAll(searchParams);
		Criteria<GjtGraduationApplyCertif> spec = new Criteria();
		// 设置连接方式，如果是内连接可省略掉
		// spec.setJoinType("gjtStudyCenter", JoinType.LEFT);
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
		// 机构ID
		String orgId = (String) searchParamsNew.remove("EQ_orgId");
		if (StringUtils.isNotBlank(orgId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		}
		// int status =
		// NumberUtils.toInt(searchParamsNew.remove("EQ_gjtGraduationRegister.expressSignState")
		// + "", -1);
		// if (status> -1){
		// List<String> studentIds =
		// gjtGraduationRegisterDao.queryStudentIdByStatus(String.valueOf(status));
		// spec.add(Restrictions.in("gjtStudentInfo.studentId", studentIds,
		// true));
		// }
		spec.addAll(Restrictions.parse(searchParamsNew));

		Page<GjtGraduationApplyCertif> pageInfos = gjtGraduationApplyCertifDao.findAll(spec, pageRequest);
		for (Iterator<GjtGraduationApplyCertif> iter = pageInfos.iterator(); iter.hasNext();) {
			GjtGraduationApplyCertif info = iter.next();
			info.setGjtGraduationRegister(gjtGraduationRegisterService
					.queryByStudentId(info.getGjtGraduationPlan().getId(), info.getGjtStudentInfo().getStudentId()));
		}
		return pageInfos;
	}

	@Override
	public Map importGraduationAudit(File targetFile, String path, String userId, String planId) {
		Map result = new HashMap();
		String[] heads = new String[] { "姓名", "学号", "分部审核结果", "分部审核备注", "总部审核结果", "总部审核备注", "电子注册号" };
		String[] dbNames = new String[] { "xm", "xh", "fbnAuditResult", "fbnAuditRemarks", "zbnAuditResult",
				"zbnAuditRemarks", "registrationNumber" };
		String[] etitle = new String[] { "姓名", "学号", "分部审核结果", "分部审核备注", "总部审核结果", "总部审核备注", "失败原因" };
		String[] edbNames = new String[] { "xm", "xh", "fbnAuditResult", "fbnAuditRemarks", "zbnAuditResult",
				"zbnAuditRemarks", "registrationNumber", "msg" };

		List<Map> successList = new ArrayList<Map>();
		List<Map> errorList = new ArrayList<Map>();
		List<String[]> datas = null;
		try {
			datas = ExcelUtil.readAsStringList(targetFile, 2, heads.length);
			String[] dataTitles = datas.remove(0);// 标题校验
			for (int i = 0; i < heads.length; i++) {
				if (!dataTitles[i].trim().equals(heads[i])) {
					result.put("exception", "请下载正确表格模版填写");
					return result;
				}
			}
		} catch (Exception e) {
			result.put("exception", "请下载正确表格模版填写");
			return result;
		}
		try {
			// 数据校验
			for (String[] data : datas) {

				if (EmptyUtils.isEmpty(data[0])) {
					errorList.add(addMap(data, "姓名不能为空"));
					continue;
				}
				if (EmptyUtils.isEmpty(data[1])) {
					errorList.add(addMap(data, "学号不能为空"));
					continue;
				}

				Map<String, String> param = addMap(data, "");
				int i = updateAudit(param, userId, planId);
				if (i == 0) {
					successList.add(param);
				} else if (i == 1){
					errorList.add(addMap(data, "省校未审核，中央无法审核"));
					continue;
				}else if (i == 2) {
					errorList.add(addMap(data, "学生未提交申请，中央无法审核"));
					continue;
				}else{
					param.put("msg", "学员不存在 请核对必填项");
					errorList.add(param);

				}

			}

			// 保存失败/成功列表
			String successFile = ExcelService.renderExcelHadTitle(successList, Arrays.asList(heads),
					Arrays.asList(dbNames), "导入成功列表", path);
			String errorFile = ExcelService.renderExcelHadTitle(errorList, Arrays.asList(etitle),
					Arrays.asList(edbNames), "导入失败列表", path);
			result.put("all_num", successList.size() + errorList.size());
			result.put("success_num", successList.size());
			result.put("error_num", errorList.size());
			result.put("success_file", successFile);
			result.put("error_file", errorFile);
			result.put("msg", "success");
		} catch (Exception e) {
			result.put("msg", "error");
			result.put("exception", e.getMessage());
		}
		return result;
	}

	public int updateAudit(Map<String, String> data, String userId, String planId) {
		try {
			GjtStudentInfo info = gjtStudentInfoDao.findByXhAndIsDeleted(data.get("xh"));
			if (info == null || !data.get("xm").equals(info.getXm())) {
				return 1;
			}
			GjtGraduationApplyCertif entity = gjtGraduationApplyCertifDao.queryByStudentIdAndPlanId(info.getStudentId(),
					planId);

			if ("".equals(data.get("fbnAuditResult")) && !"".equals(data.get("zbnAuditResult")) && entity!= null && entity.getAuditState() ==0 ){
				return 1;
			}
			if (entity != null && entity.getAuditState() <6 && !"".equals(data.get("zbnAuditResult"))) {
				return 2;
			}
			if (entity == null  && !"".equals(data.get("zbnAuditResult"))) {
				return 2;
			}

			if (entity != null) {
				entity.setUpdatedBy(userId);
				entity.setEleRegistrationNumber(data.get("registrationNumber"));
				createRecordByType(entity, data,1);
				tranformCertif(entity, data);
				update(entity);
			} else {
				entity = createCertif(info, data, userId, planId);
				gjtGraduationApplyCertifDao.save(entity);
				createRecordByType(entity, data,0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 记录审核记录
	 * @param entity
	 * @param data
	 * @param type 0-创建 1-修改
	 */
	private void createRecordByType(GjtGraduationApplyCertif entity, Map<String, String> data, int type){
		int fbnAuditResult = Strings.isNullOrEmpty(data.get("fbnAuditResult")) ? 0 : ("通过".equals(data.get("fbnAuditResult")) ? 1 : 2);
		int zbnAuditResult = Strings.isNullOrEmpty(data.get("zbnAuditResult")) ? 0 : ("通过".equals(data.get("zbnAuditResult")) ? 1 : 2);

		if(type ==0){
			if(fbnAuditResult != 0){createRecord(entity,fbnAuditResult, 3,  data.get("fbnAuditRemarks"));}
		}
		if(type ==1){
			if( fbnAuditResult !=0 && fbnAuditResult != entity.getAuditState()){createRecord(entity,fbnAuditResult, 3, data.get("fbnAuditRemarks"));}
			if( zbnAuditResult !=0 && zbnAuditResult+10 != entity.getAuditState()){createRecord(entity,zbnAuditResult, 4, data.get("zbnAuditRemarks"));}
		}
		/*if(type ==1 && entity.getAuditState() <6  ){
			if( fbnAuditResult !=0 && fbnAuditResult != entity.getAuditState()){createRecord(entity,fbnAuditResult, 3);}
		}
		if(type ==1 && entity.getAuditState() >6){
			if(zbnAuditResult != 0 && zbnAuditResult+10 != entity.getAuditState()){createRecord(entity,zbnAuditResult, 4);}
		}*/
	}

	/**
	 * 创建单挑审核记录
	 * @param entity
	 * @param auditState
	 */
	private void createRecord(GjtGraduationApplyCertif entity, int auditState, int operatorRole, String auditRemarks){
		GjtGraApplyFlowRecord record = new GjtGraApplyFlowRecord();
		record.setFlowRecordId(UUIDUtils.random());
		record.setApplyId(entity.getApplyId());
		record.setAuditState(auditState);
		record.setAuditDt(new Date());
		record.setAuditOperator(entity.getCreatedBy());
		record.setAuditContent(auditRemarks);
		//int operatorRole = entity.getAuditState() > 6 ? 4 : 3;
		record.setAuditOperatorRole(operatorRole);
		gjtGraApplyFlowRecordDao.save(record);
	}

	/**
	 * 构建毕业申请
	 * @param info
	 * @param data
	 * @param userId
	 * @param planId
	 * @return
	 */
	private GjtGraduationApplyCertif createCertif(GjtStudentInfo info, Map<String, String> data, String userId,
			String planId) {
		GjtGraduationApplyCertif entity = new GjtGraduationApplyCertif();
		entity.setApplyId(UUIDUtils.random());
		entity.setCreatedBy(userId);
		entity.setGjtGraduationPlan(gjtGraduationPlanDao.findById(planId));
		entity.setGjtStudentInfo(info);
		entity.setEleRegistrationNumber(data.get("registrationNumber"));
		entity.setCreatedDt(new Date());

		entity.setRemark(data.get("fbnAuditRemarks"));
		String fbnAuditResult = data.get("fbnAuditResult");
		entity.setAuditState(Strings.isNullOrEmpty(fbnAuditResult) ? 0 : ("通过".equals(fbnAuditResult) ? 1 : 2));
		entity.setGraduationCondition("通过".equals(fbnAuditResult) ? 1 : 0);
		//TODO
		entity.setApplyDegree(0);
		entity.setIsReceive(0);
		entity.setGraduationState(0);
		log.info("entity:[" + entity + "]");
		return entity;
	}

	/**
	 * 更新毕业申请
	 * @param one
	 * @param data
	 */
	private GjtGraduationApplyCertif tranformCertif(GjtGraduationApplyCertif one, Map<String, String> data) {
		// 当学生已经提交审核之后，分部不允许在修改审核状态
		if (one.getAuditState() < 6) {
			one.setRemark(data.get("fbnAuditRemarks"));
			String fbnAuditResult = data.get("fbnAuditResult");
			one.setAuditState(Strings.isNullOrEmpty(fbnAuditResult) ? 0 : ("通过".equals(fbnAuditResult) ? 1 : 2));
			// one.setAuditState("通过".equals(fbnAuditResult) ? 1 : 2);
			one.setGraduationCondition("通过".equals(fbnAuditResult) ? 1 : 0);
			one.setGraduationState(0);
		} else {
			one.setRemark(data.get("zbnAuditRemarks"));
			String zbnAuditResult = data.get("zbnAuditResult");
			if ("通过".equals(zbnAuditResult)) {
				gjtStudentInfoDao.updateStuInfoStudentNumberStatus("8", one.getGjtStudentInfo().getStudentId() );
				one.setGraduationState(2);
				one.setAuditState(11);
			} else if ("不通过".equals(zbnAuditResult)) {
				one.setAuditState(12);
			}
		}
		return one;
	}

	/**
	 * 自定义数组转MAP
	 */
	private Map<String, String> addMap(String[] data, String msg) {
		Map<String, String> re = new HashMap();
		if (EmptyUtils.isNotEmpty(msg)) {
			re.put("msg", msg);
		}
		re.put("xm", data[0]);
		re.put("xh", data[1]);
		re.put("fbnAuditResult", data[2]);
		re.put("fbnAuditRemarks", data[3]);
		re.put("zbnAuditResult", data[4]);
		re.put("zbnAuditRemarks", data[5]);
		re.put("registrationNumber", data[6]);
		return re;
	}

	@Override
	public void update(GjtGraduationApplyCertif entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtGraduationApplyCertifDao.save(entity);
	}

	@Override
	public List<Object[]> queryModuleScore(String studentId) {
		log.info("studentId:" + studentId);
		return gjtGraduationNativeDao.queryModuleScore(studentId);
	}

	@Override
	public float queryCompulsorySumScore(String studentId) {
		log.info("studentId:" + studentId);
		return gjtGraduationNativeDao.queryCompulsorySumScore(studentId);
	}

	@Override
	public float queryOtherSumScore(String studentId) {
		log.info("studentId:" + studentId);
		return gjtGraduationNativeDao.queryOtherSumScore(studentId);
	}

	@Override
	public float queryDegreeScore(String studentId) {
		log.info("studentId:" + studentId);
		return gjtGraduationNativeDao.queryDegreeScore(studentId);
	}

	@Override
	public float queryDesignScore(String studentId) {
		log.info("studentId:" + studentId);
		return gjtGraduationNativeDao.queryDesignScore(studentId);
	}

	@Override
	public GjtGraduationApplyCertif queryByStudentIdAndStatus(String studentId, int status) {
		log.info("studentId:" + studentId + ", status:" + status);
		return gjtGraduationApplyCertifDao.queryByStudentIdAndStatus(studentId, status);
	}

	@Override
	public GjtGraduationApplyCertif queryByStudentId(String studentId) {
		return gjtGraduationApplyCertifDao.queryByStudentId(studentId);
	}

	@Override
	public Map<String, Object> queryStudentRegisterMsg(String studentId) {
		log.info("studentId:" + studentId);
		return gjtGraduationNativeDao.queryStudentRegisterMsg(studentId);
	}

	@Override
	public List<GjtGraApplyFlowRecord> queryGraApplyFlowRecordByApplyId(String applyId) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_applyId", applyId);

		Criteria<GjtGraApplyFlowRecord> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtGraApplyFlowRecordDao.findAll(spec, new Sort("auditDt"));
	}

	@Override
	public boolean initAuditGraduationApply(String applyId) {
		GjtGraduationApplyCertif info = gjtGraduationApplyCertifDao.findOne(applyId);
		if (info != null) {
			List<GjtGraApplyFlowRecord> list = queryGraApplyFlowRecordByApplyId(applyId);
			// 没有审核记录或者审核不通过则初始化审核
			if (list == null || list.size() == 0 || list.get(list.size() - 1).getAuditState().intValue() == 2) {
				Date now = new Date();

				// 审核状态改为待审核
				info.setAuditState(0);
				gjtGraduationApplyCertifDao.save(info);

				GjtGraApplyFlowRecord entity = new GjtGraApplyFlowRecord();
				entity.setFlowRecordId(UUIDUtils.random());
				entity.setApplyId(applyId);
				entity.setAuditState(1); // 默认审核通过
				entity.setAuditDt(now);
				// 审核角色：自己
				entity.setAuditOperatorRole(1);
				gjtGraApplyFlowRecordDao.save(entity);

				GjtGraApplyFlowRecord entity2 = new GjtGraApplyFlowRecord();
				entity2.setFlowRecordId(UUIDUtils.random());
				entity2.setApplyId(applyId);
				entity2.setAuditState(0); // 默认待审核
				// 下一个审核角色：教务管理员
				entity2.setAuditOperatorRole(5);
				gjtGraApplyFlowRecordDao.save(entity2);
				return true;
			}
		}
		return false;
	}

	@Override
	public ResultFeedback auditGraduationApply(String applyId, Integer auditState, String auditContent,
			int operatorRole, String operatorRealName) {
		ResultFeedback feedback = new ResultFeedback(false, null);
		boolean pass = false;
		if ((pass = "1".equals(auditState)) || "2".equals(auditState)) {
			if (operatorRole == 2 || operatorRole == 3 || operatorRole == 4) {
				Date now = new Date();
				List<GjtGraApplyFlowRecord> list = queryGraApplyFlowRecordByApplyId(applyId);
				if (list != null && list.size() > 0) {
					GjtGraApplyFlowRecord gjtFlowRecord = list.get(list.size() - 1);
					if (gjtFlowRecord.getAuditOperatorRole().intValue() == operatorRole) {
						gjtFlowRecord.setAuditState(auditState);
						gjtFlowRecord.setAuditContent(auditContent);
						gjtFlowRecord.setAuditOperator(operatorRealName);
						gjtFlowRecord.setAuditDt(now);
						GjtGraApplyFlowRecord result = gjtGraApplyFlowRecordDao.save(gjtFlowRecord);
						if (result != null) {
							// 如果审核通过，则增加一条下一个审核记录
							if (pass) {
								GjtGraApplyFlowRecord entity = new GjtGraApplyFlowRecord();
								entity.setFlowRecordId(UUIDUtils.random());
								entity.setApplyId(applyId);
								entity.setAuditState(0); // 默认待审核
								switch (operatorRole) {
									case 2: // 学习中心
										// 下一个审核角色：分部
										entity.setAuditOperatorRole(3);
										gjtGraApplyFlowRecordDao.save(entity);
										break;
									case 3:
										// 下一个审核角色：总部
										entity.setAuditOperatorRole(4);
										gjtGraApplyFlowRecordDao.save(entity);
										break;
									case 4:
										GjtGraduationApplyCertif info = gjtGraduationApplyCertifDao.findOne(applyId);
										info.setAuditState(auditState);
										info.setUpdatedBy(operatorRealName);
										info.setUpdatedDt(now);
										gjtGraduationApplyCertifDao.save(info);
										break;
								}
							}
							// 如果审核不通过，则更新最终审核状态audit_state
							else {
								GjtGraduationApplyCertif info = gjtGraduationApplyCertifDao.findOne(applyId);
								info.setAuditState(auditState);
								info.setUpdatedBy(operatorRealName);
								info.setUpdatedDt(now);
								gjtGraduationApplyCertifDao.save(info);
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
	public Map<String, Object> countStudentApplyCertifSituation(Map<String, Object> searchParams) {
		return gjtGraduationDao.countStudentApplyCertifSituation(searchParams);
	}

	@Override
	public Map<String, Long> countGroupbyAuditState(Map<String, Object> searchParams) {

		Map<String, Long> result = new HashMap<String, Long>();
		Map<String, Object> searchParamsNew = new HashMap<String, Object>();
		searchParamsNew.putAll(searchParams);

		// int status = NumberUtils.toInt((String)
		// searchParamsNew.remove("EQ_auditState"), -1);
		result.put("", gjtGraduationApplyCertifDao.count(getSpecification(searchParamsNew)));

		searchParamsNew.put("EQ_auditState", 0);
		result.put("0", gjtGraduationApplyCertifDao.count(getSpecification(searchParamsNew)));
		searchParamsNew.put("EQ_auditState", 1);
		result.put("1", gjtGraduationApplyCertifDao.count(getSpecification(searchParamsNew)));
		searchParamsNew.put("EQ_auditState", 6);
		result.put("6", gjtGraduationApplyCertifDao.count(getSpecification(searchParamsNew)));
		searchParamsNew.put("EQ_auditState", 11);
		result.put("11", gjtGraduationApplyCertifDao.count(getSpecification(searchParamsNew)));
		searchParamsNew.put("EQ_auditState", 12);
		result.put("12", gjtGraduationApplyCertifDao.count(getSpecification(searchParamsNew)));
		return result;
	}

	/**
	 * 公共条件拼接
	 *
	 * @param searchParams
	 * @return
	 */
	private Criteria<GjtGraduationApplyCertif> getSpecification(Map<String, Object> searchParams) {
		Criteria<GjtGraduationApplyCertif> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
		Map<String, Object> searchParamsNew = new HashMap<String, Object>();
		searchParamsNew.putAll(searchParams);
		int status = NumberUtils.toInt(searchParamsNew.remove("EQ_auditState") + "", -1);
		if (status > -1) {
			spec.add(Restrictions.eq("auditState", status, true));
		}

		// 机构ID
		String orgId = (String) searchParamsNew.remove("EQ_orgId");
		if (StringUtils.isNotBlank(orgId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		}
		spec.addAll(Restrictions.parse(searchParamsNew));
		return spec;
	}

	@Override
	public int updatePhotoUrl(String studentId, String photoUrl) {
		return gjtGraduationApplyCertifDao.updatePhotoUrl(studentId, photoUrl);
	}

	@Override
	public Page<GjtApplyDegreeCertif> queryGraduationApplyDegreeByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		{
			if (pageRequest.getSort() == null) {
				pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
						new Sort(Sort.Direction.DESC, "createdDt"));
			}
			Map<String, Object> searchParamsNew = new HashMap<String, Object>();
			searchParamsNew.putAll(searchParams);
			Criteria<GjtApplyDegreeCertif> spec = new Criteria();
			// 设置连接方式，如果是内连接可省略掉
			// spec.setJoinType("gjtStudyCenter", JoinType.LEFT);
			spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
			spec.add(Restrictions.eq("gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
			// 机构ID
			String orgId = (String) searchParamsNew.remove("EQ_orgId");
			if (StringUtils.isNotBlank(orgId)) {
				List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
				spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
			}
			spec.addAll(Restrictions.parse(searchParamsNew));

			Page<GjtApplyDegreeCertif> pageInfos = gjtApplyDegreeCertifDao.findAll(spec, pageRequest);
			return pageInfos;
		}
	}

	@Override
	public GjtGraduationApplyCertif queryByStudentIdAndPlanId(String studentId, String planId) {
		return gjtGraduationApplyCertifDao.queryByStudentIdAndPlanId(studentId, planId);
	}

	@Override
	public Workbook exportGraduationApply(Map formMap, HttpServletRequest request, HttpServletResponse response) {
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			HSSFSheet sheet = wb.createSheet();
			HSSFRow row;
			HSSFCell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("姓名");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("层次");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("专业");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习中心");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("是否满足毕业申请条件");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("电子注册号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("是否申请毕业");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("毕业状态");

			List<GjtGraduationApplyCertif> recordlist = queryGraduationApplyList(formMap);
			if (EmptyUtils.isNotEmpty(recordlist)) {
				for (int i = 0; i < recordlist.size(); i++) {
					GjtGraduationApplyCertif gjtGraduationApplyCertif = recordlist.get(i);

					cellIndex = 0;
					row = sheet.createRow(rowIndex++);

					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtGraduationApplyCertif.getGjtStudentInfo().getXm());
					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtGraduationApplyCertif.getGjtStudentInfo().getXh());
					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtGraduationApplyCertif.getGjtStudentInfo().getPycc());
					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtGraduationApplyCertif.getGjtStudentInfo().getGjtGrade().getGradeName());
					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtGraduationApplyCertif.getGjtStudentInfo().getGjtSpecialty().getZymc());
					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtGraduationApplyCertif.getGjtStudentInfo().getGjtStudyCenter().getScName());
					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtGraduationApplyCertif.getGraduationCondition() == 1 ? "已满足" : "未满足");
					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtGraduationApplyCertif.getEleRegistrationNumber() == null ? "--" : gjtGraduationApplyCertif.getEleRegistrationNumber());

					cell = row.createCell(cellIndex++);
					if (gjtGraduationApplyCertif.getAuditState() == 6) {
						cell.setCellValue("已申请");
					} else {
						cell.setCellValue("未申请");
					}

					cell = row.createCell(cellIndex++);
					if (gjtGraduationApplyCertif.getGraduationState() == 0) {
						cell.setCellValue("未毕业");
					} else if (gjtGraduationApplyCertif.getGraduationState() == 1) {
						cell.setCellValue("延迟毕业");
					} else {
						cell.setCellValue("已毕业");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}

	@Override
	public void studentApply(GjtGraduationApplyCertif apply,String userId) {
		apply.setAuditState(6);
		update(apply);

		GjtGraApplyFlowRecord record = new GjtGraApplyFlowRecord();
		record.setFlowRecordId(UUIDUtils.random());
		record.setApplyId(apply.getApplyId());
		record.setAuditState(0);
		record.setAuditDt(new Date());
		record.setAuditOperator(userId);
		record.setAuditOperatorRole(1);
		gjtGraApplyFlowRecordDao.save(record);
	}


	public List<GjtGraduationApplyCertif> queryGraduationApplyList(Map searchParams) {
		Map<String, Object> searchParamsNew = new HashMap<String, Object>();
		searchParamsNew.putAll(searchParams);
		Criteria<GjtGraduationApplyCertif> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
		// 机构ID
		String orgId = (String) searchParamsNew.remove("EQ_orgId");
		if (StringUtils.isNotBlank(orgId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		}
		spec.addAll(Restrictions.parse(searchParamsNew));

		List<GjtGraduationApplyCertif> gjtGraduationApplyCertifList = gjtGraduationApplyCertifDao.findAll(spec);

		return gjtGraduationApplyCertifList;
	}
}
