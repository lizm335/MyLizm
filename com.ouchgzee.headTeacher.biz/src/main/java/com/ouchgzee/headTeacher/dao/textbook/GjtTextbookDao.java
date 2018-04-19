package com.ouchgzee.headTeacher.dao.textbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.dao.textbook.repository.GjtTextbookRepository;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbook;

@Deprecated @Repository("bzrGjtTextbookDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookDao {

	@Autowired
	private GjtTextbookRepository gjtTextbookRepository;

	@Autowired
	private CommonDao commonDao;

	public Page<BzrGjtTextbook> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("		t.* ")
			.append("from ")
			.append("		GJT_TEXTBOOK t, ")
			.append("		GJT_TEXTBOOK_STOCK s ")
			.append("where ")
			.append("		t.IS_DELETED = 'N' ")
			.append("		and s.TEXTBOOK_ID = t.TEXTBOOK_ID ");
		
		Object orgId = searchParams.get("EQ_orgId");
		if (orgId != null) {
			sb.append(" and t.ORG_ID = :orgId ");
			map.put("orgId", orgId);
		}
		
		Object textbookCode = searchParams.get("LIKE_textbookCode");
		if (textbookCode != null && StringUtils.isNotBlank(textbookCode.toString())) {
			sb.append(" and t.TEXTBOOK_CODE like :textbookCode ");
			map.put("textbookCode", "%" + textbookCode + "%");
		}
		
		Object textbookName = searchParams.get("LIKE_textbookName");
		if (textbookName != null && StringUtils.isNotBlank(textbookName.toString())) {
			sb.append(" and t.TEXTBOOK_NAME like :textbookName ");
			map.put("textbookName", "%" + textbookName + "%");
		}
		
		Object courseName = searchParams.get("LIKE_courseName");
		if (courseName != null && StringUtils.isNotBlank(courseName.toString())) {
			sb.append(" and exists ( ")
				.append("			select ")
				.append("					1 ")
				.append("			from ")
				.append("					GJT_COURSE_TEXTBOOK ct, ")
				.append("					GJT_COURSE c ")
				.append("			where ")
				.append("					ct.TEXTBOOK_ID = t.TEXTBOOK_ID ")
				.append("					and ct.COURSE_ID = c.COURSE_ID ")
				.append("					and c.IS_DELETED = 'N' ")
				.append("					and c.KCMC like :courseName ")
				.append("			) ");
			map.put("courseName", "%" + courseName + "%");
		}
		
		Object textbookType = searchParams.get("EQ_textbookType");
		if (textbookType != null && StringUtils.isNotBlank(textbookType.toString())) {
			sb.append(" and t.TEXTBOOK_TYPE = :textbookType ");
			map.put("textbookType", textbookType);
		}
		
		Object author = searchParams.get("LIKE_author");
		if (author != null && StringUtils.isNotBlank(author.toString())) {
			sb.append(" and t.AUTHOR like :author ");
			map.put("author", "%" + author + "%");
		}
		
		Object revision = searchParams.get("LIKE_revision");
		if (revision != null && StringUtils.isNotBlank(revision.toString())) {
			sb.append(" and t.REVISION like :revision ");
			map.put("revision", "%" + author + "%");
		}
		
		Object stockStatus = searchParams.get("EQ_stockStatus");
		if (stockStatus != null && StringUtils.isNotBlank(stockStatus.toString())) {
			if ("1".equals(stockStatus.toString())) {
				sb.append(" and s.STOCK_NUM >= s.PLAN_OUT_STOCK_NUM ")
					.append(" and s.STOCK_NUM != 0 ");
			} else if ("2".equals(stockStatus.toString())) {
				sb.append(" and s.STOCK_NUM < s.PLAN_OUT_STOCK_NUM ");
			}
		}
		
		sb.append(" order by t.TEXTBOOK_CODE ");
		
		return (Page<BzrGjtTextbook>)commonDao.queryForPageNative(sb.toString(), map, pageRequst, BzrGjtTextbook.class);
	}

	public List<BzrGjtTextbook> findAll(Specification<BzrGjtTextbook> spec) {
		return gjtTextbookRepository.findAll(spec);
	}
	
	public BzrGjtTextbook findOne(Specification<BzrGjtTextbook> spec) {
		return gjtTextbookRepository.findOne(spec);
	}
	
	public BzrGjtTextbook findOne(String id) {
		return gjtTextbookRepository.findOne(id);
	}
	
	public BzrGjtTextbook findByCode(String textbookCode, String orgId) {
		return gjtTextbookRepository.findByCode(textbookCode, orgId);
	}
	
	public BzrGjtTextbook save(BzrGjtTextbook entity) {
		return gjtTextbookRepository.save(entity);
	}

}
