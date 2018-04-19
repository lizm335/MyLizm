package com.gzedu.xlims.serviceImpl.textbook;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.dao.textbook.GjtTextbookDistributeDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookDistributeDetailDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookOrderDetailDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookStockApprovalDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookStockDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookStockOperaBatchDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookStockOperaDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistribute;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistributeDetail;
import com.gzedu.xlims.pojo.textbook.GjtTextbookOrderDetail;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStock;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockApproval;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOpera;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOperaBatch;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.textbook.GjtTextbookStockOperaBatchService;

@Service
public class GjtTextbookStockOperaBatchServiceImpl implements GjtTextbookStockOperaBatchService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookStockOperaBatchServiceImpl.class);

	@Autowired
	private GjtTextbookStockOperaBatchDao gjtTextbookStockOperaBatchDao;

	@Autowired
	private GjtTextbookStockOperaDao gjtTextbookStockOperaDao;

	@Autowired
	private GjtTextbookStockApprovalDao gjtTextbookStockApprovalDao;

	@Autowired
	private GjtTextbookDistributeDao gjtTextbookDistributeDao;

	@Autowired
	private GjtTextbookDistributeDetailDao gjtTextbookDistributeDetailDao;

	@Autowired
	private GjtTextbookStockDao gjtTextbookStockDao;

	@Autowired
	private GjtTextbookOrderDetailDao gjtTextbookOrderDetailDao;

	@Autowired
	private CodeGeneratorService codeGeneratorService;

	@Override
	public Page<GjtTextbookStockOperaBatch> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		return gjtTextbookStockOperaBatchDao.findAll(searchParams, pageRequst);
	}

	@Override
	@Transactional
	public GjtTextbookStockOperaBatch insert(GjtTextbookStockOperaBatch entity) {
		log.info("entity:[" + entity + "]");
		
		String id = UUIDUtils.random();
		entity.setBatchId(id);
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		GjtTextbookStockOperaBatch operaBatch = gjtTextbookStockOperaBatchDao.save(entity);
		
		//明细
		List<GjtTextbookStockOpera> operas = entity.getGjtTextbookStockOperas();
		if (operas != null && operas.size() > 0) {
			for (GjtTextbookStockOpera opera : operas) {
				opera.setOperaId(UUIDUtils.random());
				opera.setCreatedDt(new Date());
				opera.setIsDeleted("N");
				opera.setGjtTextbookStockOperaBatch(entity);
			}
			gjtTextbookStockOperaDao.save(operas);
		}
		
		//审批
		List<GjtTextbookStockApproval> approvals = entity.getGjtTextbookStockApprovals();
		if (approvals != null && approvals.size() > 0) {
			for (GjtTextbookStockApproval approval : approvals) {
				approval.setApprovalId(UUIDUtils.random());
				approval.setCreatedDt(new Date());
				approval.setIsDeleted("N");
				approval.setGjtTextbookStockOperaBatch(entity);
			}
			gjtTextbookStockApprovalDao.save(approvals);
		}
		
		//教材发放订单
		List<GjtTextbookDistribute> textbookDistributes = entity.getGjtTextbookDistributes();
		if (textbookDistributes != null && textbookDistributes.size() > 0) {
			for (GjtTextbookDistribute textbookDistribute : textbookDistributes) {
				textbookDistribute.setDistributeId(UUIDUtils.random());
				textbookDistribute.setCreatedDt(new Date());
				textbookDistribute.setIsDeleted("N");
				textbookDistribute.setGjtTextbookStockOperaBatch(entity);
			}
			gjtTextbookDistributeDao.save(textbookDistributes);
			
			//教材发放明细
			for (GjtTextbookDistribute textbookDistribute : textbookDistributes) {
				List<GjtTextbookDistributeDetail> details = textbookDistribute.getGjtTextbookDistributeDetails();
				if (details != null && details.size() > 0) {
					for (GjtTextbookDistributeDetail detail : details) {
						detail.setDetailId(UUIDUtils.random());
						detail.setGjtTextbookDistribute(textbookDistribute);
					}
					gjtTextbookDistributeDetailDao.save(details);
				}
			}
		}
		
		return operaBatch;
	}

	@Override
	public GjtTextbookStockOperaBatch findOne(String id) {
		return gjtTextbookStockOperaBatchDao.findOne(id);
	}

	@Override
	@Transactional
	public void approval(String id, int operaType, String description, String userId) {
		GjtTextbookStockOperaBatch operaBatch = gjtTextbookStockOperaBatchDao.findOne(id);
		operaBatch.setStatus(operaType); //设置审批状态
		operaBatch.setUpdatedBy(userId);
		operaBatch.setUpdatedDt(new Date());
		if (gjtTextbookStockOperaBatchDao.save(operaBatch) != null) {
			 //同时更新明细的状态
			List<GjtTextbookStockOpera> operas = operaBatch.getGjtTextbookStockOperas();
			for (GjtTextbookStockOpera opera : operas) {
				opera.setStatus(operaType); //设置审批状态
				opera.setUpdatedBy(userId);
				opera.setUpdatedDt(new Date());
			}
			gjtTextbookStockOperaDao.save(operas);
			
			//插入审批记录
			GjtTextbookStockApproval approval = new GjtTextbookStockApproval();
			approval.setApprovalId(UUIDUtils.random());
			approval.setOperaRole(2);
			approval.setOperaType(operaType);
			approval.setDescription(description);
			approval.setGjtTextbookStockOperaBatch(operaBatch);
			approval.setCreatedBy(userId);
			approval.setCreatedDt(new Date());
			approval.setIsDeleted("N");
			gjtTextbookStockApprovalDao.save(approval);
			
			if (operaType == 2) { //如果是审批通过
				//更新教材发放状态为“待配送”
				List<GjtTextbookDistribute> distributes = operaBatch.getGjtTextbookDistributes();
				if (distributes != null && distributes.size() > 0) {
					for (GjtTextbookDistribute distribute : distributes) {
						distribute.setOrderCode(codeGeneratorService.codeGenerator(CacheConstants.TEXTBOOK_DISTRIBUTE_ORDER_CODE));
						distribute.setStatus(1);
						distribute.setUpdatedBy(userId);
						distribute.setUpdatedDt(new Date());
						
						//更新教材发放明细状态以及订购明细状态为“待配送”
						List<GjtTextbookOrderDetail> orderDetails = new ArrayList<GjtTextbookOrderDetail>();
						List<GjtTextbookDistributeDetail> distributeDetails = distribute.getGjtTextbookDistributeDetails();
						for (GjtTextbookDistributeDetail distributeDetail : distributeDetails) {
							distributeDetail.setStatus(1);
							
							List<GjtTextbookOrderDetail> orderDetailList = gjtTextbookOrderDetailDao.findByStudentIdAndTextbookIdAndStatus(distribute.getStudentId(), distributeDetail.getTextbookId(), 4);
							if (orderDetailList != null && orderDetailList.size() > 0) {
								for (GjtTextbookOrderDetail orderDetail : orderDetailList) {
									orderDetail.setStatus(5);
									orderDetail.setUpdatedBy(userId);
									orderDetail.setUpdatedDt(new Date());
								}
								orderDetails.addAll(orderDetailList);
							}
						}
						gjtTextbookDistributeDetailDao.save(distributeDetails);
						gjtTextbookOrderDetailDao.save(orderDetails);
					}
				}
				gjtTextbookDistributeDao.save(distributes);
				
				//更新库存数量和库存操作的实际入库数量
				for (GjtTextbookStockOpera opera : operas) {
					GjtTextbookStock textbookStock = opera.getGjtTextbook().getGjtTextbookStock();
					if (opera.getOperaType() == 1) {  //教材入库操作
						textbookStock.setStockNum(textbookStock.getStockNum() + opera.getApplyQuantity());  //库存数量增加
						textbookStock.setInStockNum(textbookStock.getInStockNum() + opera.getApplyQuantity());  //总入库数量增加
					} else if (opera.getOperaType() == 2 || opera.getOperaType() == 3 || opera.getOperaType() == 4) {  //教材出库操作
						textbookStock.setStockNum(textbookStock.getStockNum() + opera.getApplyQuantity()); //库存数量减少
						textbookStock.setOutStockNum(textbookStock.getOutStockNum() - opera.getApplyQuantity()); //总出库数量增加
						textbookStock.setPlanOutStockNum(textbookStock.getPlanOutStockNum() + opera.getApplyQuantity());  //计划出库数量减少
					}
					gjtTextbookStockDao.save(textbookStock);
					
					//设置库存操作的实际入库数量
					opera.setActualQuantity(opera.getApplyQuantity());
					opera.setStockQuantity(textbookStock.getStockNum());
				}
				gjtTextbookStockOperaDao.save(operas);
			} else {  //如果是审批不通过
				//删除教材发放信息
				List<GjtTextbookDistribute> distributes = operaBatch.getGjtTextbookDistributes();
				if (distributes != null && distributes.size() > 0) {
					for (GjtTextbookDistribute distribute : distributes) {
						distribute.setIsDeleted("Y");
						distribute.setDeletedBy(userId);
						distribute.setDeletedDt(new Date());
						
						//更新订购明细状态
						List<GjtTextbookOrderDetail> orderDetails = new ArrayList<GjtTextbookOrderDetail>();
						List<GjtTextbookDistributeDetail> distributeDetails = distribute.getGjtTextbookDistributeDetails();
						for (GjtTextbookDistributeDetail distributeDetail : distributeDetails) {
							List<GjtTextbookOrderDetail> orderDetailList = gjtTextbookOrderDetailDao.findByStudentIdAndTextbookIdAndStatus(distribute.getStudentId(), distributeDetail.getTextbookId(), 4);
							if (orderDetailList != null && orderDetailList.size() > 0) {
								for (GjtTextbookOrderDetail orderDetail : orderDetailList) {
									orderDetail.setStatus(3);
									orderDetail.setUpdatedBy(userId);
									orderDetail.setUpdatedDt(new Date());
								}
								orderDetails.addAll(orderDetailList);
							}
						}
						gjtTextbookOrderDetailDao.save(orderDetails);
					}
				}
				gjtTextbookDistributeDao.save(distributes);
				
				//更新库存数量和库存操作的实际入库数量
				for (GjtTextbookStockOpera opera : operas) {
					GjtTextbookStock textbookStock = opera.getGjtTextbook().getGjtTextbookStock();
					if (opera.getOperaType() == 1) {  //教材入库操作
						
					} else if (opera.getOperaType() == 2 || opera.getOperaType() == 3 || opera.getOperaType() == 4) {  //教材出库操作
						textbookStock.setPlanOutStockNum(textbookStock.getPlanOutStockNum() + opera.getApplyQuantity());  //计划出库数量减少
					}
					gjtTextbookStockDao.save(textbookStock);
					
					//设置库存操作的实际入库数量
					opera.setActualQuantity(0);
					opera.setStockQuantity(textbookStock.getStockNum());
				}
				gjtTextbookStockOperaDao.save(operas);
			}
		}
		
	}

}
