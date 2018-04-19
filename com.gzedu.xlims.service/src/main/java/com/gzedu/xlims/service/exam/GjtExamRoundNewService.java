package com.gzedu.xlims.service.exam;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamRoundNew;

public interface GjtExamRoundNewService {

	public Page<GjtExamRoundNew> queryAll(final String schoolId, Map<String, Object> searchParams,
			PageRequest pageRequst);

	public GjtExamRoundNew queryBy(String id);

	public int delete(List<String> ids, String xxid);

	public int createExamRounds(GjtExamBatchNew batch, String roomids, GjtUserAccount user);

	/**
	 * 根据批次和考点查询已有的考场记录
	 * 
	 * @param batchCode
	 *            批次ＣＯＤＥ
	 * @param pointId
	 *            考点ＩＤ
	 * @return
	 */
	public List<GjtExamRoundNew> queryGjtExamRoundNewsBy(String batchCode, String pointId);

	public void insert(GjtExamRoundNew examRoundNew);
}
