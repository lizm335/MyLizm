/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.usermanage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.usermanage.GjtEmployeeInfoDao;
import com.gzedu.xlims.dao.usermanage.GjtEmployeePositionDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.daoImpl.EmployeeInfoDaoImpl;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtEmployeePosition;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.id.GjtEmployeePositionPK;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;

/**
 * 
 * 功能说明：教职工管理 实现接口
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月27日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class GjtEmployeeInfoServiceImpl implements GjtEmployeeInfoService {

	@Autowired
	private GjtEmployeeInfoDao gjtEmployeeInfoDao;

	@Autowired
	private GjtEmployeePositionDao gjtEmployeePositionDao;

	@Autowired
	private GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	private EmployeeInfoDaoImpl employeeInfoDao;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private CommonDao commonDao;

	/**
	 * 添加教职员
	 * 
	 * @param employeeInfo
	 */
	@Override
	public Boolean saveEntity(GjtEmployeeInfo employeeInfo) {
		GjtEmployeeInfo save = gjtEmployeeInfoDao.save(employeeInfo);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public GjtEmployeeInfo saveEntity(GjtEmployeeInfo item, GjtUserAccount gjtUserAccount, String employeeType) {
		return saveEntity(item, gjtUserAccount, employeeType, null);
	}

	@Override
	public GjtEmployeeInfo saveEntity(GjtEmployeeInfo item, GjtUserAccount gjtUserAccount, String employeeType,
			String[] positions) {
		String employeeId = UUIDUtils.random();
		item.setEmployeeId(employeeId);// id
		if (positions != null && positions.length > 0) {
			item.setGjtEmployeePositionList(new ArrayList<GjtEmployeePosition>());
			for (int i = 0; i < positions.length; i++) {
				GjtEmployeePosition position = new GjtEmployeePosition();
				GjtEmployeePositionPK id = new GjtEmployeePositionPK(employeeId, NumberUtils.toInt(positions[i]));
				position.setId(id);
				item.addGjtEmployeePositionList(position);
			}
		}
		// gjtEmployeeInfo.setYxsh("1");// 院系所部中心号
		// gjtEmployeeInfo.setKsh("学历教学部"); // 地址
		// gjtEmployeeInfo.setCym("*");// 曾用名
		// gjtEmployeeInfo.setZgh("qwlrgzhangyoubei");// 职工号
		// gjtEmployeeInfo.setXmpy("missLiu");// 姓名拼音
		// item.setXm(item.getXm());// 姓名
		// gjtEmployeeInfo.setSfzh("441481199008087012");// 身份证号
		// gjtEmployeeInfo.setCsrq("19891011");// 出生日期
		// item.setXbm(item.getXbm());// 性别码
		// gjtEmployeeInfo.setMzm("01");// 名族码
		// gjtEmployeeInfo.setJkzkm("1");// 健康状态
		// gjtEmployeeInfo.setHyzkm("0");// 婚姻状态
		// gjtEmployeeInfo.setJgm("gd");// 籍贯码
		// gjtEmployeeInfo.setHkxzm("1");// 户口哦性质码
		// gjtEmployeeInfo.setWhcdm("0");// 文化程度
		// gjtEmployeeInfo.setBzlbm("11");// 编制类别码
		// gjtEmployeeInfo.setJzglbm("10");// 教职工类别码
		// gjtEmployeeInfo.setRkzkm("11");// 任课状况码
		// gjtEmployeeInfo.setZp("http://baidu.com");// 头像地址
		// item.setLxdh(item.getLxdh());// 联系电话
		// item.setSjh(item.getSjh());// 手机号
		// item.setDzxx(item.getDzxx());// 电子邮箱
		// item.setWorkTime(item.getWorkTime());// 办公时间
		// item.setWorkAddr(item.getWorkAddr());// 办公地址
		// item.setGjtSchoolInfo(gjtSchoolInfo); // 所在院校
		// item.setGjtOrg(gjtOrg); // 学习机构
		// item.setGjtStudyCenter(gjtStudyCenter);// 学习中心
		item.setGjtUserAccount(gjtUserAccount);// 帐号表
		// gjtEmployeeInfo.setEeno("1280800");// ee帐号，感觉多余在用户表也存在
		// item.setOrgCode(gjtOrg.getTreeCode());// 机构编码
		item.setEmployeeType(employeeType);// 帐号类型
		item.setCreatedDt(DateUtils.getDate());// 创建日期
		item.setUpdatedDt(DateUtils.getDate());// 修改日期
		item.setIsDeleted("N");
		item.setIsEnabled("1");
		item.setVersion(new BigDecimal(1));
		item.setSyncStatus("N");// 同步状态
		this.saveEntity(item);
		return item;
	}

	/**
	 * 修改学员信息
	 */
	@Override
	public Boolean updateEntity(GjtEmployeeInfo employeeInfo) {
		GjtEmployeeInfo save = gjtEmployeeInfoDao.save(employeeInfo);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据ID查询
	 */
	@Override
	public GjtEmployeeInfo queryById(String id) {
		return gjtEmployeeInfoDao.findOne(id);
	}

	/**
	 * 重置密码
	 */
	@Override
	public Boolean updatePwd(String id) {
		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoDao.findOne(id);
		if (employeeInfo.getGjtUserAccount() != null) {
			int i = gjtUserAccountDao.updatePwd(employeeInfo.getGjtUserAccount().getId(), Md5Util.encode("888888"),
					"888888");
			return i == 1 ? true : false;
		}
		return false;
	}

	/**
	 * 假删除
	 */
	@Override
	public Boolean deleteById(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				gjtEmployeeInfoDao.deleteById(id, "Y");
			}
		}
		return true;
	}

	@Override
	public Boolean deleteById(String ids) {
		int i = gjtEmployeeInfoDao.deleteById(ids, "Y");
		return i == 0 ? false : true;
	}

	/**
	 * 真删除
	 */
	@Override
	public void delete(String id) {
		gjtEmployeeInfoDao.delete(id);

	}

	/**
	 * str 1启用 0停用
	 */
	@Override
	public Boolean updateIsEnabled(String id, String str) {
		int i = gjtEmployeeInfoDao.updateIsEnabled(id, str);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Page<GjtEmployeeInfo> queryAll(String schoolId, Map<String, Object> map, PageRequest pageRequest,
			Integer employeeType) {
		Map<String, SearchFilter> filters = SearchFilter.parse(map);

		Map<String, String> orgIds = commonMapService.getOrgMapByOrgId(schoolId);
		// filters.put("xxId", new SearchFilter("xxId", Operator.IN,
		// orgIds.keySet()));

		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("employeeType", new SearchFilter("employeeType", Operator.EQ, employeeType));
		Specification<GjtEmployeeInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtEmployeeInfo.class);
		return gjtEmployeeInfoDao.findAll(spec, pageRequest);
	}

	@Override
	public List<GjtEmployeeInfo> queryAll(Integer employeeType) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("employeeType", new SearchFilter("employeeType", Operator.EQ, employeeType));
		Specification<GjtEmployeeInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtEmployeeInfo.class);
		return gjtEmployeeInfoDao.findAll(spec);
	}

	@Override
	public List<GjtEmployeeInfo> queryAllNotClassEmployee(Integer employeeType, String classId, String xxId) {
		StringBuffer sql = new StringBuffer();
		sql.append("  select s.*");
		sql.append("  from gjt_employee_info s");
		sql.append("  where s.employee_type = :employeeType");
		sql.append("  and s.xx_id = :xxId");
		sql.append("  and s.employee_id not in");
		sql.append("  (select distinct employee_id");
		sql.append("  from gjt_class_teacher");
		sql.append("  where employee_type = :employeeType");
		sql.append("  and class_id = :classId)");
		sql.append("  and s.is_deleted = 'N'");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("employeeType", employeeType);
		params.put("classId", classId);
		params.put("xxId", xxId);

		List<GjtEmployeeInfo> queryForList = commonDao.querySqlForList(sql.toString(), params, GjtEmployeeInfo.class);
		return queryForList;
	}

	@Override
	public List<Map<String, String>> queryAllEmployeeByOrgId(String orgId, Integer employeeType) {
		StringBuffer sql = new StringBuffer();
		sql.append("  select employee_id id, xm name, zgh");
		sql.append("  from gjt_employee_info");
		sql.append("  where employee_type = :employeeType");
		sql.append("  and is_deleted = 'N'");
		sql.append("  and (xx_id = :xxId or");// 增加教辅云，院校模式，班主任xxzx_id为空查不到数据的问题
		sql.append("  xxzx_id in (select org.id");
		sql.append("  from gjt_org org");
		sql.append("  where org.is_deleted = 'N'");
		sql.append("  start with org.id = :xxId");
		sql.append("  connect by prior org.id = org.perent_id))");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("xxId", orgId);
		params.put("employeeType", employeeType);
		List<Map<String, String>> resultList = commonDao.queryForMapListNative(sql.toString(), params);
		return resultList;
	}

	@Override
	public List<GjtEmployeeInfo> findListByType(int type, int subType, String orgId) {
		return gjtEmployeeInfoDao.findListByType(String.valueOf(type), subType, orgId);
	}

	@Override
	public List<GjtEmployeeInfo> findListByType(String type, String orgId) {
		return gjtEmployeeInfoDao.findListByType(type, orgId);
	}

	@Override
	public GjtEmployeeInfo findOneByAccountId(String accountId) {
		return gjtEmployeeInfoDao.findOneByAccountId(accountId);
	}

	@Override
	public boolean updateLunwenTeacherAddPermissions(String accountId, String createdBy) {
		Date now = new Date();
		String oldIds = "386e5494c038487cb9f94a4b28b26fa7,c67f8a24943b436bb07ceb18575b679d,d1ca68b1b36b4e0fb868be9ad2152d48,4450a92273364f289618290f4b3723ec"
				+ ",aff3434bdbd54256947ecb6ecb4d6607,e0ef68211a144ed9a457222e9b25b979,dd9ef301c221484fbc35d150919a0a8f,aff8d8e3c0cd4439bee7169a507956d6"
				+ ",43a23b8a8df344b2856f547812dab021,3835c0e799ba46b79a56d7698453dbb1,ce7345e28e434be6adb1026784b93e19,9fed0be1b7d841eaa92d3b86fefeaa28"
				+ ",db905a54d11745f8a5607e998080ef0f";
		String[] oldIdArr = oldIds.split(",");

		employeeInfoDao.deleteUserMenuOpt(accountId);
		for (int i = 0; i < oldIdArr.length; i++) {
			Map<String, Object> menuOpt = new HashMap<String, Object>();
			menuOpt.put("userOptId", UUIDUtils.random());
			menuOpt.put("menuOptId", oldIdArr[i]);
			menuOpt.put("mgrId", accountId);
			menuOpt.put("status", "0");
			menuOpt.put("remark", "");
			menuOpt.put("createdBy", createdBy);
			menuOpt.put("createdDt", now);
			menuOpt.put("updatedDt", "");
			menuOpt.put("updatedBy", "");
			menuOpt.put("isDeleted", Constants.BOOLEAN_NO);
			menuOpt.put("version", "1");

			employeeInfoDao.assginSeletorUserMenu(menuOpt);
		}
		return true;
	}

	/**
	 * 根据班级id 查询班级的班主任
	 *
	 * @param classId
	 * @return
	 */
	@Override
	public Map<String, String> queryByClassId(String classId) {

		Map<String, String> employeeinfo = employeeInfoDao.queryByClassId(classId);

		return employeeinfo;
	}

	@Override
	public Map<String, Object> queryHeadTeacherInfo(String studentId) {
		Map<String, Object> headTeacherInfoMap = employeeInfoDao.queryHeadTeacherInfo(studentId);
		Map<String, Object> resultMap = headTeacherInfoMap == null ? new HashMap<String, Object>() : headTeacherInfoMap;
		return resultMap;
	}

	@Override
	public GjtEmployeeInfo queryByAccountId(String accountId) {
		// TODO Auto-generated method stub
		return employeeInfoDao.queryByAccountId(accountId);
	}

	@Override
	public void deletePositionByEmployeeId(String employeeId) {
		gjtEmployeePositionDao.deletePosition(employeeId);
	}

	@Override
	public void addPosition(String employeeId, String type) {
		GjtEmployeePosition position = new GjtEmployeePosition();
		GjtEmployeePositionPK pkId = new GjtEmployeePositionPK(employeeId, NumberUtils.toInt(type));
		position.setId(pkId);
		gjtEmployeePositionDao.save(position);
	}

}
