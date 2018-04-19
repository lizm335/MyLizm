package com.gzedu.xlims.dao.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.exam.repository.GjtExamRoomNewRepository;
import com.gzedu.xlims.pojo.exam.GjtExamRoomNew;

@Repository
public class GjtExamRoomNewDao {
	
	@Autowired
	private CommonDao commonDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	@Autowired
	private GjtExamRoomNewRepository gjtExamRoomNewRepository;

	public GjtExamRoomNew insert(GjtExamRoomNew entity) {
		return gjtExamRoomNewRepository.save(entity);
	}

	public Page<GjtExamRoomNew> findAll(Specification<GjtExamRoomNew> spec, PageRequest pageRequst) {
		return gjtExamRoomNewRepository.findAll(spec, pageRequst);
	}

	public GjtExamRoomNew save(GjtExamRoomNew entity) {
		return gjtExamRoomNewRepository.save(entity);
	}

	public GjtExamRoomNew queryBy(String id) {
		return gjtExamRoomNewRepository.findOne(id);
	}

	@Transactional
	public int deleteGjtExamRoomNew(List<String> ids, String xxid) {
		int rs = 0;
		if (ids.size() > 0) {
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("update " + "				GJT_EXAM_ROOM_NEW " + "			set "
//					+ "				IS_DELETED=1" + "			where " + "				XX_ID='");
					+ "				IS_DELETED='N'" + "			where " + "				XX_ID='");
			sbuilder.append(xxid);
			sbuilder.append("' ");
			if (null != ids && ids.size() > 0) {
				sbuilder.append(" and EXAM_ROOM_ID in ('");
				sbuilder.append(ids.get(0));
				sbuilder.append("'");
				for (int i = 1; i < ids.size(); i++) {
					sbuilder.append(", '");
					sbuilder.append(ids.get(i));
					sbuilder.append("'");
				}
				sbuilder.append(")");
				Query query = em.createNativeQuery(sbuilder.toString());
				rs = query.executeUpdate();
			}
		}
		return rs;
	}

	/**
	 * 列出学院启用状态的考场, 用map 返回 map.key 考点id map.value 考点旗下的考场列表
	 * 
	 * @param xxid
	 *            所属学院id
	 * @return
	 */
	public Map<String, List<GjtExamRoomNew>> examPointIdRoomMap(String xxid) {
		Map<String, List<GjtExamRoomNew>> map = new HashMap<String, List<GjtExamRoomNew>>();
//		String sql = "select * from gjt_exam_room_new where is_deleted=0 and status=1 and xx_id='" + xxid
		String sql = "select * from gjt_exam_room_new where is_deleted='N' and status=1 and xx_id='" + xxid
				+ "' order by exam_point_id";
		Query query = em.createNativeQuery(sql, GjtExamRoomNew.class);
		List<GjtExamRoomNew> rooms = query.getResultList();
		List<GjtExamRoomNew> tempList = null;
		for (GjtExamRoomNew room : rooms) {
			if (map.containsKey(room.getExamPointId())) {
				map.get(room.getExamPointId()).add(room);
			} else {
				tempList = new ArrayList<GjtExamRoomNew>();
				tempList.add(room);
				map.put(room.getExamPointId(), tempList);
			}
		}
		return map;
	}

	public List<GjtExamRoomNew> roomsByPointid(String pointid) {
//		String sql = "select * from gjt_exam_room_new where is_deleted=0 and status=1 and exam_point_id='" + pointid
		String sql = "select * from gjt_exam_room_new where is_deleted='N' and status=1 and exam_point_id='" + pointid
				+ "'";
		Query query = em.createNativeQuery(sql, GjtExamRoomNew.class);
		List<GjtExamRoomNew> rooms = query.getResultList();
		return rooms;
	}

	public List<GjtExamRoomNew> queryAllByExamBatchCode(String examBatchCode) {
		String sql = "select a.* from gjt_exam_room_new a left join gjt_exam_point_new b on a.exam_point_id = b.exam_point_id "
//				+ " where a.is_deleted=0 and b.exam_batch_code = '" + examBatchCode + "'";
				+ " where a.is_deleted='N' and b.exam_batch_code = '" + examBatchCode + "'";
		Query query = em.createNativeQuery(sql, GjtExamRoomNew.class);
		List<GjtExamRoomNew> rooms = query.getResultList();
		return rooms;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List exportPointInfo(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT ");
		sql.append("	TAB.CODE, ");
		sql.append("	TAB.NAME, ");
		sql.append("	TAB.AREA_ID_1 || TAB.AREA_ID_2 || TAB.AREA_ID_3 AS AREA_ID, ");
		sql.append("	TAB.ADDRESS, ");
		sql.append("	TAB.LINK_MAN, ");
		sql.append("	TAB.LINK_TEL, ");
		sql.append("	TAB.EXAM_BATCH_ID, ");
		sql.append("	TAB.BATCH_NAME, ");
		sql.append("	TAB.ID, ");
		sql.append("	TAB.XXMC ");
		sql.append("FROM ");
		sql.append("	( ");
		sql.append("		SELECT ");
		sql.append("			GEP.CODE, ");
		sql.append("			GEP.NAME, ");
		sql.append("			( ");
		sql.append("				SELECT ");
		sql.append("					GD.NAME ");
		sql.append("				FROM ");
		sql.append("					GJT_DISTRICT GD ");
		sql.append("				WHERE ");
		sql.append("					GD.ID = SUBSTR( GEP.AREA_ID, 0, 2 )|| '0000' ");
		sql.append("			) AREA_ID_1, ");
		sql.append("			( ");
		sql.append("				SELECT ");
		sql.append("					GD.NAME ");
		sql.append("				FROM ");
		sql.append("					GJT_DISTRICT GD ");
		sql.append("				WHERE ");
		sql.append("					GD.ID = SUBSTR( GEP.AREA_ID, 0, 4 )|| '00' ");
		sql.append("			) AREA_ID_2, ");
		sql.append("			( ");
		sql.append("				SELECT ");
		sql.append("					GD.NAME ");
		sql.append("				FROM ");
		sql.append("					GJT_DISTRICT GD ");
		sql.append("				WHERE ");
		sql.append("					GD.ID = GEP.AREA_ID ");
		sql.append("			) AREA_ID_3, ");
		sql.append("			GEP.ADDRESS, ");
		sql.append("			GEP.LINK_MAN, ");
		sql.append("			GEP.LINK_TEL, ");
		sql.append("			GEP.EXAM_BATCH_ID, ");
		sql.append("			GEBN.NAME AS BATCH_NAME, ");
		sql.append("			GSI.ID, ");
		sql.append("			GSI.XXMC ");
		sql.append("		FROM ");
		sql.append("			GJT_EXAM_ROOM_NEW GER ");
		sql.append("		LEFT JOIN GJT_EXAM_POINT_NEW GEP ON ");
		sql.append("			GEP.IS_DELETED = 'N' ");
		sql.append("			AND GEP.EXAM_POINT_ID = GER.EXAM_POINT_ID");
		sql.append("		LEFT JOIN GJT_EXAM_BATCH_NEW GEBN ON ");
		sql.append("			GEBN.IS_DELETED = 'N' ");
		sql.append("		AND GEBN.EXAM_BATCH_ID = GEP.EXAM_BATCH_ID ");
		sql.append("		LEFT JOIN GJT_SCHOOL_INFO GSI ON ");
		sql.append("			GSI.IS_DELETED = 'N' ");
		sql.append("			AND GSI.ID = GER.XX_ID ");
		sql.append("		WHERE ");
		sql.append("			GER.IS_DELETED = 'N' ");
		sql.append("			AND ROWNUM = 1 ");
		sql.append("			AND GEP.XX_ID = :xxid ");
		params.put("xxid", ObjectUtils.toString(searchParams.get("xxid")));
		sql.append("			AND GEBN.EXAM_BATCH_ID = :examBatchID ");
		params.put("examBatchID", ObjectUtils.toString(searchParams.get("EQ_examBatchCode")));
		sql.append(") TAB ");
		
		return commonDao.queryForMapListNative(sql.toString(), params);
	}
	
	public List exportRoomInfo(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT ");
		sql.append("	GER.EXAM_ROOM_ID, ");
		sql.append("	GER.NAME AS ROOM_NAME, ");
		sql.append("	NVL( GER.SEATS, 0 ) AS SEATS, ");
		sql.append("	( ");
		sql.append("		CASE ");
		sql.append("			WHEN GER.STATUS = '0' THEN '已停用' ");
		sql.append("			WHEN GER.STATUS = '1' THEN '已启用' ");
		sql.append("			ELSE '--' ");
		sql.append("		END ");
		sql.append("	) STATUS ");
		sql.append("FROM ");
		sql.append("	GJT_EXAM_ROOM_NEW GER ");
		sql.append("LEFT JOIN GJT_EXAM_POINT_NEW GEP ON ");
		sql.append("	GEP.IS_DELETED = 'N' ");
		sql.append("	AND GEP.EXAM_POINT_ID = GER.EXAM_POINT_ID ");
		sql.append("LEFT JOIN GJT_EXAM_BATCH_NEW GEBN ON ");
		sql.append("	GEBN.IS_DELETED = 'N' ");
		sql.append("	AND GEBN.EXAM_BATCH_ID = GEP.EXAM_BATCH_ID ");
		sql.append("WHERE ");
		sql.append("	GER.IS_DELETED = 'N' ");
		sql.append("	AND GEBN.EXAM_BATCH_ID = :examBatchID ");
		params.put("examBatchID", ObjectUtils.toString(searchParams.get("EQ_examBatchCode")));
		sql.append("ORDER BY GER.CREATED_DT DESC ");
		
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 新增考场数据
	 * @param searchParams
	 * @return
	 */
	@Transactional
	public int saveRoomData(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("INSERT ");
		sql.append("	INTO ");
		sql.append("		GJT_EXAM_ROOM_NEW( ");
		sql.append("			EXAM_ROOM_ID, ");
		sql.append("			EXAM_POINT_ID,");
		sql.append("			NAME, ");
		sql.append("			SEATS, ");
		sql.append("			XX_ID, ");
		sql.append("			STATUS, ");
		sql.append("			CREATED_BY, ");
		sql.append("			CREATED_DT, ");
		sql.append("			UPDATED_BY, ");
		sql.append("			UPDATED_DT, ");
		sql.append("			ORDER_NO ");
		sql.append("		) ");
		sql.append("	VALUES( ");
		sql.append("		:EXAM_ROOM_ID, ");
		sql.append("		:EXAM_POINT_ID, ");
		sql.append("		:NAME, ");
		sql.append("		:SEATS, ");
		sql.append("		:XX_ID, ");
		sql.append("		:STATUS, ");
		sql.append("		:CREATED_BY, ");
		sql.append("		SYSDATE, ");
		sql.append("		:UPDATED_BY, ");
		sql.append("		SYSDATE, ");
		sql.append("		:ORDER_NO ");
		sql.append("	) ");
		
		params.put("EXAM_ROOM_ID", searchParams.get("EXAM_ROOM_ID"));
		params.put("EXAM_POINT_ID", searchParams.get("EXAM_POINT_ID"));
		params.put("NAME", searchParams.get("NAME"));
		params.put("SEATS", searchParams.get("SEATS"));
		params.put("XX_ID", searchParams.get("XX_ID"));
		params.put("STATUS", searchParams.get("STATUS"));
		params.put("CREATED_BY", searchParams.get("CREATED_BY"));
		params.put("UPDATED_BY", searchParams.get("UPDATED_BY"));
		params.put("ORDER_NO", searchParams.get("ORDER_NO"));
		
		return commonDao.updateForMapNative(sql.toString(), params);
	}
	
	/**
	 * 修改考场数据
	 * @param searchParams
	 * @return
	 */
	@Transactional
	public int updRoomData(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE GJT_EXAM_ROOM_NEW GER");
		sql.append("  SET GER.ORDER_NO = :ORDER_NO, GER.SEATS = :SEATS");
		sql.append("  WHERE GER.IS_DELETED = 0");
		sql.append("  AND GER.EXAM_ROOM_ID = :EXAM_ROOM_ID");
		
		params.put("ORDER_NO", searchParams.get("ORDER_NO"));
		params.put("SEATS", searchParams.get("SEATS"));
		params.put("EXAM_ROOM_ID", searchParams.get("EXAM_ROOM_ID"));
		
		return commonDao.updateForMapNative(sql.toString(), params);
	}
	
	/**
	 * 查询考点编号
	 * @param searchParams
	 * @return
	 */
	public List queryExamPiontByCode(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT ");
		sql.append("	GEP.EXAM_POINT_ID, ");
		sql.append("	GEP.CODE, ");
		sql.append("	GEP.NAME ");
		sql.append("FROM ");
		sql.append("	GJT_EXAM_POINT_NEW GEP ");
		sql.append("WHERE ");
		sql.append("	GEP.IS_DELETED = 'N' ");
		sql.append("	AND GEP.CODE = :CODE ");
		sql.append("	AND GEP.EXAM_BATCH_ID = :EXAM_BATCH_ID ");
		sql.append("	AND GEP.XX_ID = :XX_ID ");
		
		params.put("CODE", searchParams.get("CODE"));
		params.put("EXAM_BATCH_ID", searchParams.get("EXAM_BATCH_ID"));
		params.put("XX_ID", searchParams.get("XX_ID"));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
	}
	
	
	/**
	 * 考场管理
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page getExamRoomList(Map searchParams,PageRequest pageRequest){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEB.EXAM_BATCH_ID,");
		sql.append("  	GEB.EXAM_BATCH_CODE,");
		sql.append("  	GEB.NAME AS EXAM_BATCH_NAME,");
		sql.append("  	GEP.EXAM_POINT_ID,");
		sql.append("  	GEP.CODE,");
		sql.append("  	GEP.NAME AS EXAM_POINT_NAME,");
		sql.append("  	GER.EXAM_ROOM_ID,");
		sql.append("  	GER.NAME AS EXAM_ROOM_NAME,");
		sql.append("  	GER.ORDER_NO,");
		sql.append("  	GER.SEATS,");
		sql.append("  	GER.STATUS,");
		sql.append("  	GEB.XX_ID");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_ROOM_NEW GER,");
		sql.append("  	GJT_EXAM_POINT_NEW GEP,");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE");
		sql.append("  	GER.IS_DELETED = 0");
		sql.append("  	AND GEP.IS_DELETED = 'N'");
		sql.append("  	AND GEB.IS_DELETED = 0");
		sql.append("  	AND GER.EXAM_POINT_ID = GEP.EXAM_POINT_ID");
		sql.append("  	AND GEP.EXAM_BATCH_ID = GEB.EXAM_BATCH_ID");
		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND GEB.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_ID"))){
			sql.append("	AND GEB.EXAM_BATCH_ID = :EXAM_BATCH_ID ");
			params.put("EXAM_BATCH_ID", ObjectUtils.toString(searchParams.get("EXAM_BATCH_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_POINT_ID"))){
			sql.append("	AND GEP.EXAM_POINT_ID = :EXAM_POINT_ID ");
			params.put("EXAM_POINT_ID", ObjectUtils.toString(searchParams.get("EXAM_POINT_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_ROOM_NAME"))){
			sql.append("	AND GER.NAME LIKE :EXAM_ROOM_NAME ");
			params.put("EXAM_ROOM_NAME", "%"+ObjectUtils.toString(searchParams.get("EXAM_ROOM_NAME"))+"%");
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("STATUS"))){
			sql.append("	AND GER.STATUS = :STATUS ");
			params.put("STATUS", ObjectUtils.toString(searchParams.get("STATUS")));
		}

		sql.append("ORDER BY GER.CREATED_DT DESC ");
		
		return (Page)commonDao.queryForPageNative(sql.toString(), params, pageRequest);
		
	}
	
	/**
	 * 查询考试批次
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getExamBatchInfo(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEB.EXAM_BATCH_ID,");
		sql.append("  	GEB.EXAM_BATCH_CODE,");
		sql.append("  	GEB.NAME AS EXAM_BATCH_NAME");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE");
		sql.append("  	GEB.IS_DELETED = 0");
		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND GEB.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 查询考点信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getExamPointList(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEP.EXAM_POINT_ID,");
		sql.append("  	GEP.CODE,");
		sql.append("  	GEP.NAME AS EXAM_POINT_NAME");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_POINT_NEW GEP");
		sql.append("  WHERE");
		sql.append("  	GEP.IS_DELETED = 'N'");
		if(EmptyUtils.isNotEmpty(searchParams.get("XX_ID"))){
			sql.append("  	AND GEP.XX_ID = :XX_ID ");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_ID"))){
			sql.append("  	AND GEP.EXAM_BATCH_ID = :EXAM_BATCH_ID ");
			params.put("EXAM_BATCH_ID", ObjectUtils.toString(searchParams.get("EXAM_BATCH_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_TYPE"))){
			sql.append("  	AND GEP.EXAM_TYPE = :EXAM_TYPE ");
			params.put("EXAM_TYPE", ObjectUtils.toString(searchParams.get("EXAM_TYPE")));
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);
	}
	
	/**
	 * 删除考场
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int truncExamRoom(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_EXAM_ROOM_NEW GER");
		sql.append("  SET");
		sql.append("  	GER.IS_DELETED = 1");
		sql.append("  WHERE ");
		sql.append("	GER.XX_ID = :XX_ID ");
		sql.append("  	AND GER.EXAM_ROOM_ID = :EXAM_ROOM_ID ");
		
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("EXAM_ROOM_ID", ObjectUtils.toString(searchParams.get("EXAM_ROOM_ID")).trim());
		
		return commonDao.updateForMapNative(sql.toString(), params);
		
	}
	
	/**
	 * 启用/停用考场
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int examRoomStatus(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_EXAM_ROOM_NEW GER");
		sql.append("  SET");
		sql.append("  	GER.STATUS = :STATUS ");
		sql.append("  WHERE");
		sql.append("  	GER.XX_ID = :XX_ID ");
		sql.append("  	AND GER.EXAM_ROOM_ID = :EXAM_ROOM_ID ");
		
		params.put("STATUS", Integer.parseInt(ObjectUtils.toString(searchParams.get("STATUS"))));
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("EXAM_ROOM_ID", ObjectUtils.toString(searchParams.get("EXAM_ROOM_ID")).trim());

		return commonDao.updateForMapNative(sql.toString(), params);
		
	}
	
	/**
	 * 查询考场信息
	 * @param searchParams
	 * @return
	 */
	public List getRoomInfo(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GEB.EXAM_BATCH_ID,");
		sql.append("  GEB.EXAM_BATCH_CODE,");
		sql.append("  GEB.NAME EXAM_BATCH_NAME,");
		sql.append("  GEP.EXAM_POINT_ID,");
		sql.append("  (SELECT GER.EXAM_ROOM_ID");
		sql.append("  FROM GJT_EXAM_ROOM_NEW GER");
		sql.append("  WHERE GER.IS_DELETED = 0");
		sql.append("  AND GER.EXAM_POINT_ID = GEP.EXAM_POINT_ID");
		sql.append("  AND GER.NAME = :ROOM_NAME");
		sql.append("  AND ROWNUM = 1) EXAM_ROOM_ID");
		sql.append("  FROM GJT_EXAM_BATCH_NEW GEB");
		sql.append("  LEFT JOIN GJT_EXAM_POINT_NEW GEP ON GEB.EXAM_BATCH_CODE =");
		sql.append("  GEP.EXAM_BATCH_CODE");
		sql.append("  AND GEP.IS_DELETED = 'N'");
		sql.append("  AND GEP.CODE = :POINT_CODE");
		sql.append("  WHERE GEB.IS_DELETED = 0");
		sql.append("  AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");		
		
		params.put("ROOM_NAME", ObjectUtils.toString(searchParams.get("ROOM_NAME")));
		params.put("POINT_CODE", ObjectUtils.toString(searchParams.get("POINT_CODE")));
		params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
	}
	
}
