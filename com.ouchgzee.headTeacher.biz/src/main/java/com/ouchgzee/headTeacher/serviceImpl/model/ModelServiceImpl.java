/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.serviceImpl.model;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.ouchgzee.headTeacher.dao.model.PriModelInfoDao;
import com.ouchgzee.headTeacher.dao.model.PriModelOperateDao;
import com.ouchgzee.headTeacher.pojo.BzrPriModelInfo;
import com.ouchgzee.headTeacher.pojo.BzrPriModelOperate;
import com.ouchgzee.headTeacher.pojo.BzrPriOperateInfo;
import com.ouchgzee.headTeacher.pojo.BzrTreeModel;
import com.ouchgzee.headTeacher.service.model.BzrModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：
 * 
 * @author limin import com.gzedu.xlims.service.model.ModelService;g
 * @Date 2016年4月27日
 * @version 1.0
 *
 */
@Deprecated @Service("bzrModelServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class ModelServiceImpl implements BzrModelService {

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
	public List<BzrPriModelInfo> queryMainModel(String systemName) {
		return modelInfoDao.findByParentModelModelNameOrderByOrderNoAsc(systemName);
	}

	@Override
	public List<BzrPriModelInfo> queryMainModelIn(List<String> systemNames) {
		return modelInfoDao.findByParentModelModelNameInOrderByOrderNoAsc(systemNames);
	}

	@Override
	public List<BzrPriModelInfo> queryTopModel() {
		return modelInfoDao.findByIsdeletedAndParentModelIsNullOrderByOrderNoAsc("N");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gzedu.xlims.service.model.ModelService#queryMainModel(java.lang.
	 * String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<BzrPriModelInfo> queryMainModel(String systemName, Pageable pageable) {

		return modelInfoDao.findByParentModelModelName(systemName, pageable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gzedu.xlims.service.model.ModelService#queryMainModel(java.lang.
	 * String, java.util.Map, org.springframework.data.domain.PageRequest)
	 */
	@Override
	public Page<BzrPriModelInfo> queryMainModel(String systemName, Map<String, Object> searchParams,
												PageRequest pageRequst) {
		searchParams.put("EQ_isdeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("parentModel.modelName", new SearchFilter("parentModel.modelName", Operator.EQ, systemName));
		Specification<BzrPriModelInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), BzrPriModelInfo.class);

		return modelInfoDao.findAll(spec, pageRequst);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gzedu.xlims.service.model.ModelService#getModelInfo(java.lang.String)
	 */
	@Override
	public BzrPriModelInfo getModelInfo(String id) {
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
	public BzrPriModelInfo getModelInfoByName(String modelName) {
		return modelInfoDao.findByModelName(modelName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gzedu.xlims.service.model.ModelService#queryAll()
	 */
	@Override
	public List<BzrPriModelInfo> queryAll() {
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
	public BzrPriModelInfo createPriModelInfo(BzrPriModelInfo info) {
		info.setModelId(UUIDUtils.random());
		info.setCreatedDt(new Date());
		info.setIsdeleted("N");

		BzrPriModelInfo parentModel = modelInfoDao.findOne(info.getParentModel().getModelId());
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
	public BzrPriModelInfo updatePriModelInfo(BzrPriModelInfo info) {
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
		BzrPriModelInfo modelInfo = modelInfoDao.findOne(id);

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
	public BzrPriModelInfo getOne(String id) {
		return modelInfoDao.findOne(id);
	}

	@Override
	public void addOperateInfo(BzrPriModelInfo modelInfo, BzrPriOperateInfo operateInfo) {
		BzrPriModelOperate modelOperate = new BzrPriModelOperate();
		modelOperate.setModelOperateId(UUIDUtils.random());
		modelOperate.setPriModelInfo(modelInfo);
		modelOperate.setPriOperateInfo(operateInfo);
		modelOperateDao.save(modelOperate);
	}

	@Override
	public List<BzrPriModelInfo> queryAll(Iterable<String> ids) {
		return (List<BzrPriModelInfo>) modelInfoDao.findAll(ids);
	}

	@Override
	public List<BzrTreeModel> queryModelTree() {
		List<BzrPriModelInfo> infos = modelInfoDao.findByIsdeletedAndParentModelIsNullOrderByOrderNoAsc("N");
		List<BzrTreeModel> tree = new ArrayList<BzrTreeModel>();
		walk(infos, tree);
		return tree;
	}

	private void walk(List<BzrPriModelInfo> infos, List<BzrTreeModel> tree) {

		for (BzrPriModelInfo info : infos) {
			BzrTreeModel treeModel = new BzrTreeModel();
			treeModel.setId(info.getModelId());
			treeModel.setText(info.getModelName() + "-------" + info.getModelAddress());

			if (info.getChildModelList() != null && info.getChildModelList().size() > 0) {
				treeModel.setNodes(new ArrayList<BzrTreeModel>());
				walk(info.getChildModelList(), treeModel.getNodes());
			}
			tree.add(treeModel);
		}
	}

	@Override
	public List<BzrTreeModel> queryModelAndOperateTree() {
		List<BzrPriModelInfo> infos = modelInfoDao.findByIsdeletedAndParentModelIsNullOrderByOrderNoAsc("N");
		List<BzrTreeModel> tree = new ArrayList<BzrTreeModel>();
		walkModelAndOperate(infos, tree);
		return tree;
	}

	private void walkModelAndOperate(List<BzrPriModelInfo> infos, List<BzrTreeModel> tree) {

		for (BzrPriModelInfo info : infos) {
			BzrTreeModel treeModel = new BzrTreeModel();
			treeModel.setId(info.getModelId());
			treeModel.setText(info.getModelName() + "-------" + info.getModelAddress());

			if (info.getChildModelList() != null && info.getChildModelList().size() > 0) {
				treeModel.setNodes(new ArrayList<BzrTreeModel>());
				walkModelAndOperate(info.getChildModelList(), treeModel.getNodes());
			} else if (info.getIsLeaf()) {
				List<BzrPriOperateInfo> modelOperates = info.getPriOperateInfos();
				if (modelOperates != null && modelOperates.size() > 0) {
					List<BzrTreeModel> operateTrees = new ArrayList<BzrTreeModel>();
					for (BzrPriOperateInfo modelOperate : modelOperates) {
						BzrTreeModel operateTree = new BzrTreeModel();
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
