package com.gzedu.xlims.dao.menue;

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

import com.gzedu.xlims.pojo.TblPriMenue;

/**
 * 
 * 功能说明：菜单查询
 * 
 * @author liming
 * @Date 2016年5月4日
 * @version 2.5
 *
 */
public class TblPriMenueSpecs {

	public static Specification<TblPriMenue> findAllByMenue(final TblPriMenue menue) {
		return new Specification<TblPriMenue>() {
			@Override
			public Predicate toPredicate(Root<TblPriMenue> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				if (StringUtils.isNotBlank(menue.getMenuName())) {
					expressions.add(cb.equal(root.get("menuName"), menue.getMenuName()));
				}
				if (menue.getMenuLevel() != null) {
					expressions.add(cb.equal(root.get("menuLevel"), menue.getMenuLevel()));
				}
				query.where(cb.and(predicate));
				return query.getRestriction();
			}
		};
	}

	public static Specification<TblPriMenue> isMenuId(final String menuId) {
		return new Specification<TblPriMenue>() {
			@Override
			public Predicate toPredicate(Root<TblPriMenue> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("menuId"), menuId);
			}
		};
	}

	public static Specification<TblPriMenue> isPrentMenuId(final String prentMenuId) {
		return new Specification<TblPriMenue>() {
			@Override
			public Predicate toPredicate(Root<TblPriMenue> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				expressions.add(cb.equal(root.join("TblPriMenue").get("menuId"), prentMenuId));

				return predicate;
			}
		};
	}

	public static Specification<TblPriMenue> findChildMenueList(final String menuId, final String isDeleted) {
		return new Specification<TblPriMenue>() {
			@Override
			public Predicate toPredicate(Root<TblPriMenue> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				Join<TblPriMenue, TblPriMenue> childs = root.join("childMenueList", JoinType.LEFT);
				// Join<Object, Object> join = childs.join("childM");

				expressions.add(cb.equal(root.get("menuId"), menuId));
				// expressions.add(cb.equal(childs.get("menuId"), menuId));

				expressions.add(cb.equal(childs.get("isDeleted"), isDeleted));

				// expressions.add(cb.equal(join.get("isDeleted"), isDeleted));

				return predicate;
			}
		};
	}

}
