package com.gzedu.xlims.serviceImpl.feedback;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.dao.feedback.LcmsMutualReplyDao;
import com.gzedu.xlims.dao.feedback.LcmsMutualSubjectDao;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualReply;
import com.gzedu.xlims.pojo.LcmsMutualSubject;
import com.gzedu.xlims.service.feedback.LcmsMutualReplyService;

@Service
public class LcmsMutualReplyServiceImpl implements LcmsMutualReplyService {

	@Autowired
	LcmsMutualReplyDao lcmsMutualReplyDao;

	@Autowired
	LcmsMutualSubjectDao lcmsMutualSubjectDao;

	@Override
	public Page<LcmsMutualReply> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		PageRequest page = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
				new Sort(Sort.Direction.DESC, "createdDt"));
		Specification<LcmsMutualReply> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				LcmsMutualReply.class);
		return lcmsMutualReplyDao.findAll(spec, page);
	}

	@Override
	public long queryAllCount(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<LcmsMutualReply> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				LcmsMutualReply.class);
		return lcmsMutualReplyDao.count(spec);
	}

	@Override
	public LcmsMutualReply save(LcmsMutualReply item) {
		return lcmsMutualReplyDao.save(item);
	}

	@Override
	public LcmsMutualReply queryById(String id) {
		return lcmsMutualReplyDao.findOne(id);
	}

	@Override
	public boolean update(LcmsMutualReply item) {
		LcmsMutualReply entity = lcmsMutualReplyDao.save(item);
		return entity == null ? false : true;
	}

	@Override
	public boolean deleteBySubjectId(String id) {
		int i = lcmsMutualReplyDao.deleteBySubjectId(id);
		return i == 1 ? true : false;
	}

	@Override
	public LcmsMutualReply queryByPid(String pid) {
		return lcmsMutualReplyDao.findBySubjectIdAndParentIdIsNull(pid);
	}

	@Override
	public LcmsMutualReply queryPidBySubjectId(String pid) {
		LcmsMutualReply item = null;
		List<LcmsMutualReply> list = lcmsMutualReplyDao.findBySubjectIdAndParentIdIsNotNull(pid);
		if (list != null && list.size() > 0) {
			item = list.get(0);
		}
		return item;
	}

	@Override
	public LcmsMutualReply save(String[] imgUrls, String pid, GjtUserAccount user, String content) {
		StringBuffer imgUrl = new StringBuffer("");
		if (imgUrls != null && imgUrls.length > 0) {
			for (int i = 0; i < imgUrls.length; i++) {
				imgUrl.append(imgUrls[i]);
				if (i < imgUrls.length - 1) {
					imgUrl.append(",");
				}
			}
		}

		LcmsMutualReply reply = this.queryByPid(pid);
		// 老师是否回复了
		if (reply == null) {
			return null;
		}

		List<LcmsMutualReply> list = reply.getLcmsMutualSubject().getGjtLcmsMutualReply();
		if ("stud".equals(list.get(list.size() - 1).getUserType())) {
			return null;
		}

		LcmsMutualSubject feedback = reply.getLcmsMutualSubject();
		LcmsMutualReply item = new LcmsMutualReply();
		item.setReplyId(UUIDUtils.random().toString());
		item.setCreatedUser(user);// 因为旧的没用用这个字段，所以这个可以当作存储userAccountId
		item.setReplyContent(content);
		item.setLcmsMutualSubject(feedback);
		item.setParentId(reply.getReplyId());
		item.setUserId(user.getGjtStudentInfo().getStudentId());// 兼容旧的userId字段，取studentId
		item.setCreatedDt(DateUtils.getNowTime());
		item.setReplyTitle(feedback.getSubjectTitle());
		item.setUserType("stud");
		item.setResPath(imgUrl.toString());
		item.setIsdeleted("N");
		item.setVersion(new BigDecimal("1"));
		LcmsMutualReply save = this.save(item);
		if (save != null) {
			// 追问以后把问题改成未解决
			BigDecimal replyCount = feedback.getReplyCount();
			int addNum = 0;
			if (replyCount != null) {
				addNum = replyCount.intValue() + 1;
			}
			feedback.setReplyCount(new BigDecimal(addNum));
			feedback.setSubjectStatus("N");
			feedback.setUpdatedDt(DateUtils.getNowTime());
			lcmsMutualSubjectDao.save(feedback);
		}
		return save;
	}

	@Override
	public LcmsMutualReply teachSave(String[] imgUrls, String pid, GjtUserAccount user, String content) {
		StringBuffer imgUrl = new StringBuffer("");
		if (imgUrls != null && imgUrls.length > 0) {
			for (int i = 0; i < imgUrls.length; i++) {
				imgUrl.append(imgUrls[i]);
				if (i < imgUrls.length - 1) {
					imgUrl.append(",");
				}
			}
		}

		LcmsMutualReply reply = this.queryByPid(pid);
		// 老师是否回复了
		if (reply == null) {
			return null;
		}

		List<LcmsMutualReply> list = reply.getLcmsMutualSubject().getGjtLcmsMutualReply();
		if ("stud".equals(list.get(list.size() - 1).getUserType())) {
			return null;
		}

		LcmsMutualSubject feedback = reply.getLcmsMutualSubject();
		LcmsMutualReply item = new LcmsMutualReply();
		item.setReplyId(UUIDUtils.random().toString());
		item.setCreatedUser(user);// 因为旧的没用用这个字段，所以这个可以当作存储userAccountId
		item.setReplyContent(content);
		item.setLcmsMutualSubject(feedback);
		item.setParentId(reply.getReplyId());
		item.setUserId(user.getId());// 老师提的问题给后台管理员
		item.setCreatedDt(DateUtils.getNowTime());
		item.setReplyTitle(feedback.getSubjectTitle());
		item.setUserType("stud");
		item.setResPath(imgUrl.toString());
		item.setIsdeleted("N");
		item.setVersion(new BigDecimal("1"));
		LcmsMutualReply save = this.save(item);
		if (save != null) {
			// 追问以后把问题改成未解决
			BigDecimal replyCount = feedback.getReplyCount();
			int addNum = 0;
			if (replyCount != null) {
				addNum = replyCount.intValue() + 1;
			}
			feedback.setReplyCount(new BigDecimal(addNum));
			feedback.setSubjectStatus("N");
			feedback.setUpdatedDt(DateUtils.getNowTime());
			lcmsMutualSubjectDao.save(feedback);
		}
		return save;
	}

	@Override
	public LcmsMutualReply saveOftenReply(String[] imgUrls2, LcmsMutualSubject subject, GjtUserAccount user,
			String content2, String title) {
		StringBuffer imgUrl2 = new StringBuffer("");
		if (imgUrls2 != null && imgUrls2.length > 0) {
			for (int i = 0; i < imgUrls2.length; i++) {
				imgUrl2.append(imgUrls2[i]);
				if (i < imgUrls2.length - 1) {
					imgUrl2.append(",");
				}
			}
		}

		LcmsMutualReply entity = new LcmsMutualReply();
		entity.setCreatedUser(user);
		entity.setCreatedDt(DateUtils.getNowTime());
		entity.setIsdeleted("N");
		entity.setLcmsMutualSubject(subject);
		entity.setParentId(subject.getSubjectId());
		entity.setRemark("自问自答");
		entity.setReplyContent(content2);
		entity.setReplyId(UUIDUtils.random().toString());
		entity.setReplyTitle(title);
		entity.setResPath(imgUrl2.toString());
		entity.setUserId("");
		entity.setUserType("tchr");
		entity.setVersion(new BigDecimal("1"));
		return this.save(entity);
	}

	@Override
	public LcmsMutualReply teachReplyStudentSave(String[] imgs, String pid, LcmsMutualSubject entity,
			GjtUserAccount user, String content, String is_comm, String oftenType, String isCommendType) {
		// 追问以后把问题改成未解决
		BigDecimal replyCount = entity.getReplyCount();
		int addNum = 0;
		if (replyCount != null) {
			addNum = replyCount.intValue() + 1;
		}

		LcmsMutualReply reply = this.queryPidBySubjectId(pid);
		LcmsMutualReply item = new LcmsMutualReply();
		item.setReplyId(UUIDUtils.random().toString());
		item.setCreatedUser(user);// 因为旧的没用用这个字段，所以这个可以当作存储userAccountId
		item.setReplyContent(content);
		item.setLcmsMutualSubject(entity);
		if (reply != null) {// 学生回复过，或者本身回复，取第一条的RID为PID，后面追加的回复的PID都是第一条的RID
			item.setParentId(reply.getParentId());
		}
		item.setUserId("");// 兼容旧的userId字段，取employeeId
		item.setCreatedDt(DateUtils.getNowTime());
		item.setReplyTitle(entity.getSubjectTitle());
		item.setIsdeleted("N");
		item.setVersion(new BigDecimal("1"));
		item.setUserType("tchr");

		StringBuffer imgUrls = new StringBuffer();
		if (imgs != null) {
			for (int i = 0; i < imgs.length; i++) {
				imgUrls.append(imgs[i]);
				if (i < imgs.length - 1) {
					imgUrls.append(",");
				}
			}
		}
		item.setResPath(imgUrls.toString());
		LcmsMutualReply save = this.save(item);
		if (save != null) {
			// 追问以后把问题改成未解决
			entity.setReplyCount(new BigDecimal(addNum));
			entity.setSubjectStatus("Y");
			if (StringUtils.isNotBlank(is_comm)) {// 是否常见问题
				entity.setIsCommend(is_comm);
				entity.setOftenType(oftenType);// 根据角色ID获取数据字典的常见问题类型
				entity.setIsCommendType(isCommendType);
			}
			lcmsMutualSubjectDao.save(entity);
		}
		return save;
	}

	@Override
	public LcmsMutualReply updateOftenReply(String replyId, String content2, String[] imgUrls2) {
		LcmsMutualReply modify = lcmsMutualReplyDao.findOne(replyId);
		StringBuffer imgUrl2 = new StringBuffer("");
		if (imgUrls2 != null && imgUrls2.length > 0) {
			for (int i = 0; i < imgUrls2.length; i++) {
				imgUrl2.append(imgUrls2[i]);
				if (i < imgUrls2.length - 1) {
					imgUrl2.append(",");
				}
			}
		}
		modify.setReplyContent(content2);
		modify.setResPath(imgUrl2.toString());
		modify.setUpdatedDt(DateUtils.getNowTime());
		return lcmsMutualReplyDao.save(modify);
	}
}