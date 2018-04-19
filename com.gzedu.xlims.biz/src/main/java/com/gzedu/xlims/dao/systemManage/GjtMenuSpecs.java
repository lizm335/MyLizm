package com.gzedu.xlims.dao.systemManage;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.gzedu.xlims.pojo.GjtMenu;



/**
 * 功能说明：菜单查询条件
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月7日
 * @version 2.5
 *
 */
public class GjtMenuSpecs {

	public static Specification<GjtMenu> findAllByMenue(final GjtMenu entity) {
		return new Specification<GjtMenu>() {
			@Override
			public Predicate toPredicate(Root<GjtMenu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				String name = entity.getName();
				String xxid = entity.getXxid();
				if (StringUtils.isNotBlank(name)) {
					expressions.add(cb.equal(root.get("name"), name));
				}
				if (StringUtils.isNotBlank(xxid)) {
					expressions.add(cb.equal(root.get("xxid"), xxid));
				}
				
				query.where(cb.and(predicate));
				return query.getRestriction();

			}
		};
	}

	public static Specification<GjtMenu> isPrentMenuId(final String prentMenuId) {
		return new Specification<GjtMenu>() {
			@Override
			public Predicate toPredicate(Root<GjtMenu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				expressions.add(cb.equal(root.join("GjtMenu").get("pid"), prentMenuId));

				return predicate;
			}
		};
	}

	public static Specification<GjtMenu> findChildMenueList(final String pid) {
		return new Specification<GjtMenu>() {
			@Override
			public Predicate toPredicate(Root<GjtMenu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				expressions.add(cb.equal(root.get("pid"), pid));

				return predicate;
			}
		};
	}

}
