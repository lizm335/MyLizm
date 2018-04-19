package com.gzedu.xlims.dao.model;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.gzedu.xlims.pojo.PriModelInfo;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月6日
 * @version 2.5
 *
 */
public class PriModelInfoSpecs {

	public static Specification<PriModelInfo> findAll() {
		return new Specification<PriModelInfo>() {
			@Override
			public Predicate toPredicate(Root<PriModelInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				expressions.add(cb.isNull(root.get("parentModel")));

				Join<PriModelInfo, PriModelInfo> childs = root.join("childModelList", JoinType.LEFT);

				expressions.add(cb.equal(root.get("isdeleted"), "N"));
				expressions.add(cb.equal(childs.get("isdeleted"), "N"));

				query.distinct(true);
				query.where(predicate);
				return query.getRestriction();
			}
		};
	}
}
