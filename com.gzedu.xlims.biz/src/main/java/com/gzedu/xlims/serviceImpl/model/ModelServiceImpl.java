/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.model.PriModelInfoDao;
import com.gzedu.xlims.dao.model.PriModelOperateDao;
import com.gzedu.xlims.pojo.PriModelInfo;
import com.gzedu.xlims.pojo.PriModelOperate;
import com.gzedu.xlims.pojo.PriOperateInfo;
import com.gzedu.xlims.pojo.TreeModel;
import com.gzedu.xlims.service.model.ModelService;

/**
 * 功能说明：
 * 
 * @author limin import com.gzedu.xlims.service.model.ModelService;g
 * @Date 2016年4月27日
 * @version 1.0
 *
 */
@Service
public class ModelServiceImpl implements ModelService {

	@Autowired
	PriModelInfoDao modelInfoDao;

	@Autowired
	PriModelOperateDao modelOperateDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gzedu.xlims.service.model.ModelService#queryMainModel()
	 */
	@Override
	public List<PriModelInfo> queryMainModel(String systemName) {
		return modelInfoDao.findByParentModelModelNameOrderByOrderNoAsc(systemName);
	}

	@Override
	public List<PriModelInfo> queryMainModelIn(List<String> systemNames) {
		return modelInfoDao.findByParentModelModelNameInOrderByOrderNoAsc(systemNames);
	}

	@Override
	public List<PriModelInfo> queryTopModel() {
		return modelInfoDao.findByIsdeletedAndParentModelIsNullOrderByOrderNoAsc("N");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gzedu.xlims.service.model.ModelService#queryMainModel(java.lang.
	 * String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<PriModelInfo> queryMainModel(String systemName, Pageable pageable) {

		return modelInfoDao.findByParentModelModelName(systemName, pageable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gzedu.xlims.service.model.ModelService#queryMainModel(java.lang.
	 * String, java.util.Map, org.springframework.data.domain.PageRequest)
	 */
	@Override
	public Page<PriModelInfo> queryMainModel(String systemName, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		searchParams.put("EQ_isdeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("parentModel.modelName", new SearchFilter("parentModel.modelName", Operator.EQ, systemName));
		Specification<PriModelInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), PriModelInfo.class);

		return modelInfoDao.findAll(spec, pageRequst);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.model.ModelService#getModelInfo(java.lang.String)
	 */
	@Override
	public PriModelInfo getModelInfo(String id) {
		return modelInfoDao.findOne(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.model.ModelService#getModelInfoByName(java.lang.
	 * String)
	 */
	@Override
	public PriModelInfo getModelInfoByName(String modelName) {
		return modelInfoDao.findByModelName(modelName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gzedu.xlims.service.model.ModelService#queryAll()
	 */
	@Override
	public List<PriModelInfo> queryAll() {
		return modelInfoDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.model.ModelService#createPriModelInfo(com.gzedu.
	 * xlims.pojo.PriModelInfo)
	 */
	@Override
	public PriModelInfo createPriModelInfo(PriModelInfo info) {
		info.setModelId(UUIDUtils.random());
		info.setCreatedDt(new Date());
		info.setIsdeleted("N");

		PriModelInfo parentModel = modelInfoDao.findOne(info.getParentModel().getModelId());
		info.setParentModel(parentModel);
		if (parentModel == null) {
			info.setModelRank(0);
		} else {
			info.setModelRank(parentModel.getModelRank() + 1);
		}

		return modelInfoDao.save(info);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.model.ModelService#updatePriModelInfo(com.gzedu.
	 * xlims.pojo.PriModelInfo)
	 */
	@Override
	public PriModelInfo updatePriModelInfo(PriModelInfo info) {
		info.setUpdatedDt(new Date());
		return modelInfoDao.save(info);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.model.ModelService#deletePriModelInfo(java.lang.
	 * String)
	 */
	@Override
	public boolean deletePriModelInfo(String id) {
		PriModelInfo modelInfo = modelInfoDao.findOne(id);

		if (modelInfo != null) {
			if (modelInfo.getChildModelList() == null || modelInfo.getChildModelList().size() == 0) {
				//modelInfoDao.delete(modelInfo);
				modelInfo.setIsdeleted("Y");
				modelInfoDao.save(modelInfo);
				return true;
			}
		}

		return false;
	}

	@Override
	public PriModelInfo getOne(String id) {
		return modelInfoDao.findOne(id);
	}

	@Override
	public void addOperateInfo(PriModelInfo modelInfo, PriOperateInfo operateInfo) {
		PriModelOperate modelOperate = new PriModelOperate();
		modelOperate.setModelOperateId(UUIDUtils.random());
		modelOperate.setPriModelInfo(modelInfo);
		modelOperate.setPriOperateInfo(operateInfo);
		modelOperateDao.save(modelOperate);
	}

	@Override
	public List<PriModelInfo> queryAll(Iterable<String> ids) {
		return (List<PriModelInfo>) modelInfoDao.findAll(ids);
	}

	@Override
	public List<TreeModel> queryModelTree() {
		List<PriModelInfo> infos = modelInfoDao.findByIsdeletedAndParentModelIsNullOrderByOrderNoAsc("N");
		List<TreeModel> tree = new ArrayList<TreeModel>();
		walk(infos, tree);
		return tree;
	}

	private void walk(List<PriModelInfo> infos, List<TreeModel> tree) {

		for (PriModelInfo info : infos) {
			TreeModel treeModel = new TreeModel();
			treeModel.setId(info.getModelId());
			treeModel.setText(info.getModelName() + "-------" + info.getModelAddress());

			if (info.getChildModelList() != null && info.getChildModelList().size() > 0) {
				treeModel.setNodes(new ArrayList<TreeModel>());
				walk(info.getChildModelList(), treeModel.getNodes());
			}
			tree.add(treeModel);
		}
	}

	@Override
	public List<TreeModel> queryModelAndOperateTree() {
		List<PriModelInfo> infos = modelInfoDao.findByIsdeletedAndParentModelIsNullOrderByOrderNoAsc("N");
		List<TreeModel> tree = new ArrayList<TreeModel>();
		walkModelAndOperate(infos, tree);
		return tree;
	}

	private void walkModelAndOperate(List<PriModelInfo> infos, List<TreeModel> tree) {

		for (PriModelInfo info : infos) {
			TreeModel treeModel = new TreeModel();
			treeModel.setId(info.getModelId());
			treeModel.setText(info.getModelName() + "-------" + info.getModelAddress());

			if (info.getChildModelList() != null && info.getChildModelList().size() > 0) {
				treeModel.setNodes(new ArrayList<TreeModel>());
				walkModelAndOperate(info.getChildModelList(), treeModel.getNodes());
			} else if (info.getIsLeaf()) {
				List<PriOperateInfo> modelOperates = info.getPriOperateInfos();
				if (modelOperates != null && modelOperates.size() > 0) {
					List<TreeModel> operateTrees = new ArrayList<TreeModel>();
					for (PriOperateInfo modelOperate : modelOperates) {
						TreeModel operateTree = new TreeModel();
						operateTree.setId(info.getModelId() + ":" + modelOperate.getOperateId());
						operateTree.setText(modelOperate.getOperateName());
						operateTree.setHref("##");
						
						operateTrees.add(operateTree);
					}
					
					treeModel.setNodes(operateTrees);
				}
			}
			tree.add(treeModel);
		}
	}

}
