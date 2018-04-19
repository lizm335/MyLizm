package com.gzedu.xlims.serviceImpl.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.gzedu.AnalyXmlUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.json.JSONArray;
import com.gzedu.xlims.common.json.JSONObject;
import com.gzedu.xlims.dao.api.ApiDao;
import com.gzedu.xlims.service.api.ApiService;

@Service
public class ApiServiceImpl implements ApiService {

	@Autowired
	ApiDao apiDao;
	
	@Override
	@Transactional
	public Map addFeesRecord(HttpServletRequest request) {
		Map resultMap = new HashMap();
		try {
			String jsonStr = AnalyXmlUtil.getXmlString(request);
			JSONArray jsonArray = new JSONArray(jsonStr);
			if (EmptyUtils.isNotEmpty(jsonArray)) {
				for (int i=0;i<jsonArray.length(); i++) {
					
					JSONObject feesObj = jsonArray.getJSONObject(i);
					Map feesMap = new HashMap();
					feesMap.put("FEES_ID", SequenceUUID.getSequence());
					feesMap.put("ATID", ObjectUtils.toString(feesObj.get("ATID")));
					feesMap.put("REGISTRATION_TIME", ObjectUtils.toString(feesObj.get("REGISTRATION_TIME")));
					feesMap.put("FULL_TUITION", ObjectUtils.toString(feesObj.get("FULL_TUITION")));
					feesMap.put("DISCOUNT_TUITION", ObjectUtils.toString(feesObj.get("DISCOUNT_TUITION")));
					feesMap.put("PAYABLE_TUITION", ObjectUtils.toString(feesObj.get("PAYABLE_TUITION")));
					feesMap.put("PAID_TUITION", ObjectUtils.toString(feesObj.get("PAID_TUITION")));
					feesMap.put("SUM_TERM", ObjectUtils.toString(feesObj.get("SUM_TERM")));
					feesMap.put("PAID_TERM", ObjectUtils.toString(feesObj.get("PAID_TERM")));
					feesMap.put("PAY_FEES_TYPE", ObjectUtils.toString(feesObj.get("PAY_FEES_TYPE")));
					feesMap.put("PAY_FEES_STATE", ObjectUtils.toString(feesObj.get("PAY_FEES_STATE")));
					feesMap.put("DISCOUNT_POLICY", ObjectUtils.toString(feesObj.get("DISCOUNT_POLICY")));
					
					apiDao.delFeesRecord(feesMap);
					apiDao.addFeesRecord(feesMap);
					
					JSONArray feesDetail = feesObj.getJSONArray("FEES_DETAIL_LIST");
					if (EmptyUtils.isNotEmpty(feesDetail)) {
						apiDao.delFeesDetail(feesMap);
						for (int j=0; j<feesDetail.length(); j++) {
							
							JSONObject feesDetailObj = feesDetail.getJSONObject(j);
							
							Map detailMap = new HashMap();
							detailMap.put("FEES_DETAIL_ID", SequenceUUID.getSequence());
							detailMap.put("ATID", ObjectUtils.toString(feesObj.get("ATID")));
							detailMap.put("PAY_FEES_MONTHLY", ObjectUtils.toString(feesDetailObj.get("PAY_FEES_MONTHLY")));
							detailMap.put("PAYABLE_TUITION", ObjectUtils.toString(feesDetailObj.get("PAYABLE_TUITION")));
							detailMap.put("PAID_TUITION", ObjectUtils.toString(feesDetailObj.get("PAID_TUITION")));
							detailMap.put("PAY_FEES_STATE", ObjectUtils.toString(feesDetailObj.get("PAY_FEES_STATE")));
							detailMap.put("PAY_FEES_TYPE", ObjectUtils.toString(feesDetailObj.get("PAY_FEES_TYPE")));
							detailMap.put("PAY_FEES_TIME", ObjectUtils.toString(feesDetailObj.get("PAY_FEES_TIME")));
							apiDao.addFeesDetail(detailMap);
						}
					}
				}
			}
			resultMap.put("result", "success");
			resultMap.put("message", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", "failure");
			resultMap.put("message", "失败");
		}
		return resultMap;
	}
	
}
