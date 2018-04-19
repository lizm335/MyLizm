/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.menue;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.TblPriRole;

/**
 * 功能说明：
 * 
 * @author liming
 * @Date 2016年4月28日
 * @version 1.0
 *
 */
public class TblPriRoleSpecs {

	public static Specification<TblPriRole> findByUser(final GjtUserAccount user) {
		return new Specification<TblPriRole>() {
			@Override
			public Predicate toPredicate(Root<TblPriRole> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				Join<TblPriRole, GjtUserAccount> joinUser = root.join("gjtUserAccountList");

				expressions.add(cb.equal(joinUser.get("id"), user.getId()));

				return predicate;
			}
		};
	}
}
