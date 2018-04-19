package com.gzedu.xlims.dao.home;

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
import com.gzedu.xlims.pojo.message.GjtMessageInfo;



/**
 * 功能说明：教务管理-通知公告查询条件
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月11日
 * @version 2.5
 *
 */
public class GjtMessageInfoSpecs {

	public static Specification<GjtMessageInfo> findAllByMessageInfo(final GjtMessageInfo entity) {
		return new Specification<GjtMessageInfo>() {
			@Override
			public Predicate toPredicate(Root<GjtMessageInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				
				//消息标题
				String infoTheme = entity.getInfoTheme();
				//消息类型
				String infoType = entity.getInfoType();
				if (StringUtils.isNotBlank(infoTheme)) {
					expressions.add(cb.equal(root.get("infoTheme"), infoTheme));
				}
				if (StringUtils.isNotBlank(infoType)) {
					expressions.add(cb.equal(root.get("infoType"), infoType));
				}
				
				query.where(cb.and(predicate));
				return query.getRestriction();

			}
		};
	}

}
