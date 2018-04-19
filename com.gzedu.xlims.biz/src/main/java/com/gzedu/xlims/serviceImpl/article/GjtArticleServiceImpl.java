/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.article;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.article.GjtArticleDao;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtArticle;
import com.gzedu.xlims.service.article.GjtArticleService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年8月8日
 * @version 1.0
 *
 */
@Service
public class GjtArticleServiceImpl extends BaseServiceImpl<GjtArticle> implements GjtArticleService {

	@Autowired
	GjtArticleDao gjtArticleDao;

	@Override
	protected BaseDao<GjtArticle, String> getBaseDao() {
		return gjtArticleDao;
	}

	@Override
	public Page<GjtArticle> queryPageList(Map<String, Object> searchParams,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Criteria<GjtArticle> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtArticleDao.findAll(spec, pageRequest);
	}
	@Override
	public List<GjtArticle> queryGjtArticle(Map<String, Object> searchParams) {
		Sort sort =	new Sort(Sort.Direction.DESC, "createdDt");

		Criteria<GjtArticle> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtArticleDao.findAll(spec, sort);
	}

	@Override
	public GjtArticle queryById(String id) {
		GjtArticle gjtArticle = gjtArticleDao.findOne(id);
		return gjtArticle;
	}

	/**
	 * 添加文章
	 * 
	 * @param gjtArticle
	 */
	@Override
	public Boolean saveArticle(GjtArticle gjtArticle) {
		if(StringUtils.isEmpty(gjtArticle.getGradeId()) && StringUtils.isEmpty(gjtArticle.getSpecialtyId())){
			gjtArticle.setOwnerType(new BigDecimal(0));
		} else if(StringUtils.isNotEmpty(gjtArticle.getGradeId()) && StringUtils.isEmpty(gjtArticle.getSpecialtyId())){
			gjtArticle.setOwnerType(new BigDecimal(1));
		} else if(StringUtils.isEmpty(gjtArticle.getGradeId()) && StringUtils.isNotEmpty(gjtArticle.getSpecialtyId())){
			gjtArticle.setOwnerType(new BigDecimal(2));
		} else if(StringUtils.isNotEmpty(gjtArticle.getGradeId()) && StringUtils.isNotEmpty(gjtArticle.getSpecialtyId())){
			gjtArticle.setOwnerType(new BigDecimal(3));
		}
		GjtArticle save = gjtArticleDao.save(gjtArticle);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 修改文章
	 * 
	 * @param gjtArticle
	 */
	@Override
	public Boolean updateArticle(GjtArticle gjtArticle) {
		if(StringUtils.isEmpty(gjtArticle.getGradeId()) && StringUtils.isEmpty(gjtArticle.getSpecialtyId())){
			gjtArticle.setOwnerType(new BigDecimal(0));
		} else if(StringUtils.isNotEmpty(gjtArticle.getGradeId()) && StringUtils.isEmpty(gjtArticle.getSpecialtyId())){
			gjtArticle.setOwnerType(new BigDecimal(1));
		} else if(StringUtils.isEmpty(gjtArticle.getGradeId()) && StringUtils.isNotEmpty(gjtArticle.getSpecialtyId())){
			gjtArticle.setOwnerType(new BigDecimal(2));
		} else if(StringUtils.isNotEmpty(gjtArticle.getGradeId()) && StringUtils.isNotEmpty(gjtArticle.getSpecialtyId())){
			gjtArticle.setOwnerType(new BigDecimal(3));
		}
		gjtArticle.setUpdatedDt(new Date());
		GjtArticle save = gjtArticleDao.save(gjtArticle);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除文章 真删
	 * 
	 * @param id
	 */
	@Override
	public boolean delete(String id) {
		// TODO Auto-generated method stub
		gjtArticleDao.delete(id);
		return true;
	}
}
