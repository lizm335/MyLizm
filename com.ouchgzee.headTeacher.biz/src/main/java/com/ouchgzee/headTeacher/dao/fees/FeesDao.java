package com.ouchgzee.headTeacher.dao.fees;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.ouchgzee.headTeacher.dao.comm.CommonDao;

@Deprecated @Repository("bzrFeesDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class FeesDao {

	@Autowired
	private CommonDao commonDao;
	
	public Page getFeeslist(Map searchParams, PageRequest pageRequst) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GSI.STUDENT_ID,");
		sql.append("  GSI.ATID,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.SJH,");
		sql.append("  GSI.SFZH,");
		sql.append("  GSI.PYCC,");
		
		sql.append("  GSI.XBM,");
		sql.append("  GSI.ZP,");
		sql.append("  GSI.DZXX,");
		sql.append("  GSI.SC_CO,");
		sql.append("  GSI.TXDZ,");
		
		sql.append("  GSO.XXMC,");
		sql.append("  GSC.SC_NAME,");
		
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = GSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		
		sql.append("  GGE.GRADE_ID,");
		sql.append("  GGE.GRADE_NAME,");
		sql.append("  GSY.SPECIALTY_ID,");
		sql.append("  GSY.ZYMC,");
		sql.append("  TO_CHAR(GFI.REGISTRATION_TIME, 'yyyy-mm-dd') REGISTRATION_TIME,");
		sql.append("  GFI.FULL_TUITION,");
		sql.append("  GFI.DISCOUNT_TUITION,");
		sql.append("  GFI.PAYABLE_TUITION,");
		sql.append("  GFI.PAID_TUITION,");
		sql.append("  GFI.PAID_TERM,");
		sql.append("  GFI.SUM_TERM,");
		sql.append("  GFI.PAY_FEES_TYPE,");
		sql.append("  GFI.PAY_FEES_STATE,");
		sql.append("  ESP.VALUE_ AS LEARNING_STATE");
		sql.append("  FROM GJT_CLASS_STUDENT GCS,");
		sql.append("  GJT_CLASS_INFO    GCI,");
		sql.append("  GJT_GRADE         GGE,");
		sql.append("  GJT_SPECIALTY     GSY,");
		sql.append("  GJT_STUDENT_INFO  GSI");
		sql.append("  LEFT JOIN GJT_FEES_INFO GFI ON GSI.ATID = GFI.ATID");
		sql.append("  LEFT JOIN GJT_SCHOOL_INFO GSO ON GSI.XX_ID = GSO.ID");
		sql.append("  LEFT JOIN GJT_STUDY_CENTER GSC ON GSC.ID = GSI.XXZX_ID");
		sql.append("  LEFT JOIN GJT_STUDENT_PROPERTY ESP ON GSI.STUDENT_ID = ESP.STUDENT_ID AND ESP.KEY_ = 'PAYSTATE'");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GGE.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  AND GSI.STUDENT_ID = GCS.STUDENT_ID");
		sql.append("  AND GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  AND GSI.NJ = GGE.GRADE_ID");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			sql.append("  AND GCI.CLASS_ID = :CLASS_ID");
			param.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			param.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM")).trim()+"%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			param.put("XH", ObjectUtils.toString(searchParams.get("XH")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SFZH")))) {
			sql.append("  AND GSI.SFZH LIKE :SFZH");
			param.put("SFZH", "%"+ObjectUtils.toString(searchParams.get("SFZH")).trim()+"%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSY.SPECIALTY_ID = :SPECIALTY_ID");
			param.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GGE.GRADE_ID = :GRADE_ID");
			param.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			param.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDENT_ID")))) {
			sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID");
			param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ATID")))) {
			sql.append("  AND GSI.ATID = :ATID");
			param.put("ATID", ObjectUtils.toString(searchParams.get("ATID")).trim());
		}

		return (Page) commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}
	
}
