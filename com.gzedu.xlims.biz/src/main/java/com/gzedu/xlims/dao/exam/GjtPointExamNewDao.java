package com.gzedu.xlims.dao.exam;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;

@Repository
public class GjtPointExamNewDao {

	@PersistenceContext(unitName = "entityManagerFactory")
	EntityManager em;
	
	@Autowired
	CommonDao commonDao;
	
	/**
	 * 考点管理
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page<Map<String, Object>> getExamPointList(Map<String, Object> searchParams,PageRequest pageRequest){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	TAB.EXAM_BATCH_ID,");
		sql.append("  	TAB.EXAM_BATCH_NAME,");
		sql.append("  	TAB.EXAM_BATCH_CODE,");
		sql.append("  	TAB.EXAM_POINT_ID,");
		sql.append("  	TAB.EXAM_POINT_NAME,");
		sql.append("  	TAB.CODE,");
		sql.append("  	TAB.AREA_ID,");
		sql.append("  	TAB.EXAM_TYPE,");
		sql.append("  	TAB.PROVINCE_NAME,");
		sql.append("  	TAB.CITY_NAME,");
		sql.append("  	TAB.DISTRICT_NAME,");
		sql.append("  	TAB.ADDRESS,");
		sql.append("  	TAB.JION_COUNT,");
		sql.append("  	TAB.ROOM_COUNT,");
		sql.append("  	TAB.SEATS_COUNT,");
		sql.append("  	TAB.IS_ENABLED,");
		sql.append("  	TAB.CREATED_DT");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			DISTINCT GEB.EXAM_BATCH_ID,");
		sql.append("  			GEB.NAME AS EXAM_BATCH_NAME,");
		sql.append("  			GEB.EXAM_BATCH_CODE,");
		sql.append("  			GEP.EXAM_POINT_ID,");
		sql.append("  			GEP.NAME AS EXAM_POINT_NAME,");
		sql.append("  			GEP.CODE,");
		sql.append("  			GEP.AREA_ID,");
		sql.append("  			GEP.EXAM_TYPE,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					MAX( GDD.NAME )");
		sql.append("  				FROM");
		sql.append("  					GJT_DISTRICT GDD");
		sql.append("  				WHERE");
		sql.append("  					GDD.ID = SUBSTR( GEP.AREA_ID, 1, 2 )|| '0000'");
		sql.append("  			) PROVINCE_NAME,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					MAX( GDD.NAME )");
		sql.append("  				FROM");
		sql.append("  					GJT_DISTRICT GDD");
		sql.append("  				WHERE");
		sql.append("  					GDD.ID = SUBSTR( GEP.AREA_ID, 1, 4 )|| '00'");
		sql.append("  			) CITY_NAME,");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					MAX( GDD.NAME )");
		sql.append("  				FROM");
		sql.append("  					GJT_DISTRICT GDD");
		sql.append("  				WHERE");
		sql.append("  					GDD.ID = GEP.AREA_ID");
		sql.append("  			) DISTRICT_NAME,");
		sql.append("  			GEP.ADDRESS,");
		sql.append("			(SELECT COUNT(DISTINCT GEPA.STUDENT_ID) FROM GJT_EXAM_POINT_APPOINTMENT_NEW GEPA WHERE GEPA.IS_DELETED = 0 AND GEPA.EXAM_POINT_ID = GEP.EXAM_POINT_ID) JION_COUNT, ");
		sql.append("			NVL(( SELECT COUNT( DISTINCT GER.EXAM_ROOM_ID ) FROM GJT_EXAM_POINT_NEW GEP1, GJT_EXAM_ROOM_NEW GER WHERE GEP1.IS_DELETED = 'N' AND GER.IS_DELETED = 0 AND GEP1.EXAM_POINT_ID = GER.EXAM_POINT_ID AND GEP1.EXAM_POINT_ID = GEP.EXAM_POINT_ID ), 0 ) ROOM_COUNT, ");
		sql.append("			NVL(( SELECT SUM( GER1.SEATS ) FROM GJT_EXAM_ROOM_NEW GER1 WHERE GER1.IS_DELETED = 0 AND GER1.EXAM_POINT_ID = GEP.EXAM_POINT_ID ), 0 ) SEATS_COUNT, ");
		sql.append("  			GEP.IS_ENABLED,");
		sql.append("  			GEP.CREATED_DT");
		sql.append("  		FROM");
		sql.append("  			GJT_EXAM_POINT_NEW GEP");
		sql.append("  		LEFT JOIN GJT_EXAM_BATCH_NEW GEB ON GEB.IS_DELETED = 0 AND GEP.EXAM_BATCH_ID = GEB.EXAM_BATCH_ID");
		sql.append("  		LEFT JOIN GJT_EXAM_ROOM_NEW GER ON");
		sql.append("  			GER.IS_DELETED = 0");
		sql.append("  			AND GER.EXAM_POINT_ID = GEP.EXAM_POINT_ID");
		sql.append("  		WHERE");
		sql.append("  			GEP.IS_DELETED = 'N'");
		sql.append("  			AND GEP.XX_ID = :XX_ID ");
		sql.append("  	) TAB");
		sql.append("  WHERE");
		sql.append("  	1 = 1");
		
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_TYPE"))){
			sql.append("  	AND TAB.EXAM_TYPE = :EXAM_TYPE ");
			params.put("EXAM_TYPE", ObjectUtils.toString(searchParams.get("EXAM_TYPE")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_ID"))) {
			if ("null".equals(searchParams.get("EXAM_BATCH_ID"))) {
				sql.append("  	AND TAB.EXAM_BATCH_ID IS NULL ");// 考点（基础数据）
			} else {
				sql.append("  	AND TAB.EXAM_BATCH_ID = :EXAM_BATCH_ID ");
				params.put("EXAM_BATCH_ID", ObjectUtils.toString(searchParams.get("EXAM_BATCH_ID")));
			}
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_POINT_ID"))){
			sql.append("  	AND TAB.EXAM_POINT_ID = :EXAM_POINT_ID ");
			params.put("EXAM_POINT_ID", ObjectUtils.toString(searchParams.get("EXAM_POINT_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("NAME"))){
			sql.append("  	AND TAB.EXAM_POINT_NAME = :NAME ");
			params.put("NAME", ObjectUtils.toString(searchParams.get("NAME")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("PROVINCE_NAME"))){
			sql.append("  	AND TAB.PROVINCE_NAME LIKE :PROVINCE_NAME ");
			params.put("PROVINCE_NAME", ObjectUtils.toString(searchParams.get("PROVINCE_NAME") + "%"));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("CITY_NAME"))){
			sql.append("  	AND TAB.CITY_NAME LIKE :CITY_NAME ");
			params.put("CITY_NAME", ObjectUtils.toString(searchParams.get("CITY_NAME") + "%"));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("CITYNAME"))){
			sql.append("  	AND TAB.DISTRICT_NAME LIKE :DISTRICT_NAME ");
			params.put("DISTRICT_NAME", ObjectUtils.toString(searchParams.get("CITYNAME") + "%"));
		}
		sql.append("  ORDER BY");
		sql.append("  	TAB.CREATED_DT DESC");
		
		return commonDao.queryForPageNative(sql.toString(), params, pageRequest);
		
	}
	
	/**
	 * 启用/停用考点
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int examPointStatus(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_EXAM_POINT_NEW GEP");
		sql.append("  SET");
		sql.append("  	GEP.IS_ENABLED = :IS_ENABLED ");
		sql.append("  WHERE");
		sql.append("  	GEP.XX_ID = :XX_ID ");
		sql.append("  	GEP.EXAM_POINT_ID = :EXAM_POINT_ID ");
		
		params.put("IS_ENABLED", Integer.parseInt(ObjectUtils.toString(searchParams.get("IS_ENABLED"))));
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("EXAM_POINT_ID", ObjectUtils.toString(searchParams.get("EXAM_POINT_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), params);
		
	}
	
	/**
	 * 删除考点
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int delExamPoint(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_EXAM_POINT_NEW GEP");
		sql.append("  SET");
		sql.append("  	GEP.IS_DELETED = 'Y'");
		sql.append("  WHERE");
		sql.append("  	GEP.XX_ID = :XX_ID ");
		sql.append("  	AND GEP.EXAM_POINT_ID = :EXAM_POINT_ID ");
		
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("EXAM_POINT_ID", ObjectUtils.toString(searchParams.get("EXAM_POINT_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), params);
		
	}
	
	
	
}
