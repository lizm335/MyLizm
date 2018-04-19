package com.ouchgzee.headTeacher.dao.model;

import com.ouchgzee.headTeacher.pojo.BzrPriModelInfo;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

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

	public static Specification<BzrPriModelInfo> findAll() {
		return new Specification<BzrPriModelInfo>() {
			@Override
			public Predicate toPredicate(Root<BzrPriModelInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				expressions.add(cb.isNull(root.get("parentModel")));

				Join<BzrPriModelInfo, BzrPriModelInfo> childs = root.join("childModelList", JoinType.LEFT);

				expressions.add(cb.equal(root.get("isdeleted"), "N"));
				expressions.add(cb.equal(childs.get("isdeleted"), "N"));

				query.distinct(true);
				query.where(predicate);
				return query.getRestriction();
			}
		};
	}
}
