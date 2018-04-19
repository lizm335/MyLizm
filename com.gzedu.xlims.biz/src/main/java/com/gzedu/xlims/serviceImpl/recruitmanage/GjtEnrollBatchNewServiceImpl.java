package com.gzedu.xlims.serviceImpl.recruitmanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.recruitmanage.GjtEnrollBatchNewDao;
import com.gzedu.xlims.pojo.GjtEnrollBatchNew;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.recruitmanage.GjtEnrollBatchNewService;

@Service
public class GjtEnrollBatchNewServiceImpl implements GjtEnrollBatchNewService {
	
	private static final Log log = LogFactory.getLog(GjtEnrollBatchNewServiceImpl.class);
	@Autowired
	CommonMapService commonMapService;
	
	@Autowired
	GjtEnrollBatchNewDao gjtEnrollBatchNewDao;
	
	@Autowired
	private CommonDao commonDao;
	
	@Override
	public Page<GjtEnrollBatchNew> queryAll(String schoolId, List enrollBatchNewList,String date,Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:"+searchParams+"schoolId:"+schoolId);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("orgId", new SearchFilter("orgId", Operator.IN, schoolId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		if(searchParams.size()>0){
			if(searchParams.get("EQ_status").equals("4")){
				filters.remove("EQ_status");
				filters.put("enrollEdate", new SearchFilter("enrollEdate", Operator.LT, date));
			}
		}
		if(enrollBatchNewList!=null){
			if(enrollBatchNewList.size()==0){
				filters.put("enrollBatchId", new SearchFilter("enrollBatchId", Operator.EQ, ""));
			}else{
				filters.put("enrollBatchId", new SearchFilter("enrollBatchId", Operator.IN, enrollBatchNewList));
			}
		}
		Specification<GjtEnrollBatchNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtEnrollBatchNew.class);
		return gjtEnrollBatchNewDao.findAll(spec, pageRequst);
	}
	/**
	 * 假删除(多个)
	 */
	@Override
	public void delete(Iterable<String> ids) {
		if (ids != null) {
			for (String id : ids) {
				gjtEnrollBatchNewDao.deleteById(id, "Y");
			}
		}

	}

	/**
	 * 假删除(单个)
	 */
	@Override
	public void delete(String id) {
		gjtEnrollBatchNewDao.deleteById(id, "Y");
		
	}

	@Override
	public void insert(GjtEnrollBatchNew entity) {
		gjtEnrollBatchNewDao.save(entity);
		
	}

	@Override
	public int update(GjtEnrollBatchNew entity) {
		gjtEnrollBatchNewDao.save(entity);
		return 1;		
	}

	@Override
	public void updateStatus(String id) {
		log.info("【参数】:id=:"+id);
		gjtEnrollBatchNewDao.updateStatus(id);		
	}

	@Override
	public GjtEnrollBatchNew queryById(String id) {
		log.info("【参数】:id=:"+id);
		return gjtEnrollBatchNewDao.findOne(id);
	}

	@Override
	public long queryStatusTotalNum(String orgId, String status) {
		log.info("status:"+status+"schoolId:"+orgId);
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("orgId", new SearchFilter("orgId", Operator.EQ, orgId));
		filters.put("status", new SearchFilter("status", Operator.EQ, status));
		Specification<GjtEnrollBatchNew> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtEnrollBatchNew.class);
		return gjtEnrollBatchNewDao.count(spec);
	}

	@Override
	public long queryDateTotalNum(String orgId, String date) {
		log.info("orgId:"+orgId+"date:"+date);
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("orgId", new SearchFilter("orgId", Operator.EQ, orgId));
		filters.put("enrollEdate", new SearchFilter("enrollEdate", Operator.LT, date));
		Specification<GjtEnrollBatchNew> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtEnrollBatchNew.class);
		return gjtEnrollBatchNewDao.count(spec);
	}
	@Override
	public List queryEnrollBatchNew(String id, String yearDegree, String symbol) {
		log.info("id:"+id+"yearDegree:"+yearDegree+"symbol:"+symbol);
		Map<String, Object> parameters = new HashMap<String, Object>();
		String sql="select a.enrollBatchId from ( "
				  +"select gebn.enroll_batch_id enrollBatchId ,gebn.enroll_total_num enrollNum,gebn.org_id orgId, "
				  + "(select count(*) from gjt_signup gs where gs.enroll_batch_id in "
				  + "(select gebn.enroll_batch_id from  gjt_enroll_batch_new gebn where gebn.is_deleted='N'))totalNum "
				  + "from gjt_enroll_batch_new gebn where gebn.is_deleted='N')a "
				  + "where a.orgId='"+id+"' ";
				  if(symbol.equals("1")){
					 sql=sql+"and (a.totalNum/a.enrollNum)*100= :yearDegree ";  
				  }else if(symbol.equals("2")){
					  sql=sql+"and (a.totalNum/a.enrollNum)*100> :yearDegree ";  
				  }else if(symbol.equals("3")){
					  sql=sql+"and (a.totalNum/a.enrollNum)*100< :yearDegree ";  
				  }else if(symbol.equals("4")){
					  sql=sql+"and (a.totalNum/a.enrollNum)*100>= :yearDegree ";  
				  }else{
					  sql=sql+"and (a.totalNum/a.enrollNum)*100<= :yearDegree ";   
				  }				  
				  parameters.put("yearDegree", yearDegree);
		log.info("sql==:"+sql);
//		return commonDao.queryForMapListNative(sql, parameters);//queryForObjectListNative
		return commonDao.queryForObjectListNative(sql, parameters);
	}
	@Override
	public void deleteFile(String enrollBatchId) {
		gjtEnrollBatchNewDao.deleteFileById(enrollBatchId, "");	
	}
}
