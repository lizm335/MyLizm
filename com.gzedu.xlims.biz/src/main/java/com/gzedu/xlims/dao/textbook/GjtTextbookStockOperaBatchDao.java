package com.gzedu.xlims.dao.textbook;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.textbook.repository.GjtTextbookStockOperaBatchRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOperaBatch;

@Repository
public class GjtTextbookStockOperaBatchDao {

	@Autowired
	private GjtTextbookStockOperaBatchRepository gjtTextbookStockOperaBatchRepository;

	@Autowired
	private CommonDao commonDao;
	
	public Page<GjtTextbookStockOperaBatch> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("		b.* ")
			.append("from ")
			.append("		GJT_TEXTBOOK_STOCK_OPERA_BATCH b ")
			.append("where ")
			.append("		b.IS_DELETED = 'N' ");
		
		Object orgId = searchParams.get("EQ_orgId");
		if (orgId != null) {
			sb.append(" and exists ( ")
				.append("			select ")
				.append("					1 ")
				.append("			from ")
				.append("					GJT_TEXTBOOK t, ")
				.append("					GJT_TEXTBOOK_STOCK_OPERA o ")
				.append("			where ")
				.append("					t.TEXTBOOK_ID = o.TEXTBOOK_ID ")
				.append("					and o.BATCH_ID = b.BATCH_ID ")
				.append("					and t.ORG_ID = :orgId ")
				.append("			) ");
			map.put("orgId", orgId);
		}
		
		Object textbookCode = searchParams.get("LIKE_textbookCode");
		if (textbookCode != null && StringUtils.isNotBlank(textbookCode.toString())) {
			sb.append(" and exists ( ")
				.append("			select ")
				.append("					1 ")
				.append("			from ")
				.append("					GJT_TEXTBOOK t, ")
				.append("					GJT_TEXTBOOK_STOCK_OPERA o ")
				.append("			where ")
				.append("					t.TEXTBOOK_ID = o.TEXTBOOK_ID ")
				.append("					and o.BATCH_ID = b.BATCH_ID ")
				.append("					and t.TEXTBOOK_CODE like :textbookCode ")
				.append("			) ");
			map.put("textbookCode", "%" + textbookCode + "%");
		}
		
		Object textbookName = searchParams.get("LIKE_textbookName");
		if (textbookName != null && StringUtils.isNotBlank(textbookName.toString())) {
			sb.append(" and exists ( ")
				.append("			select ")
				.append("					1 ")
				.append("			from ")
				.append("					GJT_TEXTBOOK t, ")
				.append("					GJT_TEXTBOOK_STOCK_OPERA o ")
				.append("			where ")
				.append("					t.TEXTBOOK_ID = o.TEXTBOOK_ID ")
				.append("					and o.BATCH_ID = b.BATCH_ID ")
				.append("					and t.TEXTBOOK_NAME like :textbookName ")
				.append("			) ");
			map.put("textbookName", "%" + textbookName + "%");
		}
		
		Object textbookType = searchParams.get("EQ_textbookType");
		if (textbookType != null && StringUtils.isNotBlank(textbookType.toString())) {
			sb.append(" and exists ( ")
				.append("			select ")
				.append("					1 ")
				.append("			from ")
				.append("					GJT_TEXTBOOK t, ")
				.append("					GJT_TEXTBOOK_STOCK_OPERA o ")
				.append("			where ")
				.append("					t.TEXTBOOK_ID = o.TEXTBOOK_ID ")
				.append("					and o.BATCH_ID = b.BATCH_ID ")
				.append("					and t.TEXTBOOK_TYPE = :textbookType ")
				.append("			) ");
			map.put("textbookType", textbookType);
		}
		
		Object operaType = searchParams.get("EQ_operaType");
		if (operaType != null && StringUtils.isNotBlank(operaType.toString())) {
			sb.append(" and b.OPERA_TYPE = :operaType ");
			map.put("operaType", operaType);
		}
		
		Object status = searchParams.get("EQ_status");
		if (status != null && StringUtils.isNotBlank(status.toString())) {
			sb.append(" and b.STATUS = :status ");
			map.put("status", status);
		}
		
		sb.append(" order by b.CREATED_DT desc ");
		
		return (Page<GjtTextbookStockOperaBatch>)commonDao.queryForPageNative(sb.toString(), map, pageRequst, GjtTextbookStockOperaBatch.class);
	}
	
	public GjtTextbookStockOperaBatch save(GjtTextbookStockOperaBatch entity) {
		return gjtTextbookStockOperaBatchRepository.save(entity);
	}
	
	public GjtTextbookStockOperaBatch findOne(String id) {
		return gjtTextbookStockOperaBatchRepository.findOne(id);
	}

}
