package com.gzedu.xlims.web.controller.exam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamRoomNew;
import com.gzedu.xlims.pojo.exam.GjtExamRoundNew;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.exam.GjtExamBatchNewService;
import com.gzedu.xlims.service.exam.GjtExamRoomNewService;
import com.gzedu.xlims.service.exam.GjtExamRoundNewService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.common.vo.CommonSelect;

@Controller
@RequestMapping("/exam/new/round")
public class GjtExamRoundNewController {

	private static final Log log = LogFactory.getLog(GjtExamRoundNewController.class);

	@Autowired
	private GjtExamRoundNewService gjtExamRoundNewService;
	@Autowired
	private GjtExamRoomNewService gjtExamRoomNewService;
	@Autowired
	private GjtExamBatchNewService gjtExamBatchNewService;
	@Autowired
	private CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		/** 过渡版本代码 start */
		if (null == user || null != request.getParameter("userid")) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end */
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtExamRoundNew> pageInfo = gjtExamRoundNewService.queryAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);

		Map<String, String> batchMap = commonMapService.getGjtExamBatchNewIdNameMap(user.getGjtOrg().getId());
		model.addAttribute("batchMap", batchMap);
		model.addAttribute("user", user);
		model.addAttribute("pageInfo", pageInfo);
		
		
		return "edumanage/exam/exam_round_list";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(GjtExamRoundNew entity, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		/** 过渡版本代码 start */
		if (null == user) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end */
		if (null != entity) {
			entity.setXxId(user.getGjtOrg().getId());
			gjtExamRoundNewService.insert(entity);
		} else {
			feedback.setSuccessful(false);
			feedback.setMessage("失败");
		}
		return feedback;
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(@RequestParam String ids, HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		/** 过渡版本代码 start */
		if (null == user) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end */
		Feedback feedback = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			try {
				// TODO: @micarol 删除限制
				int rs = gjtExamRoundNewService.delete(Arrays.asList(ids.split(",")), user.getGjtOrg().getId());
				if (rs == 0) {
					feedback.setSuccessful(false);
					feedback.setMessage("删除失败, 只能删除未同步数据.");
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				feedback.setSuccessful(false);
				feedback.setMessage("删除失败, 数据库异常");
			}
		}
		return feedback;
	}

	@RequestMapping(value = "{op}/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable String op, @PathVariable String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		/** 过渡版本代码 start */
		if (null == user) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end */
		GjtExamRoundNew entity = new GjtExamRoundNew();
		if (!"create".equals(op)) {
			entity = gjtExamRoundNewService.queryBy(id);
			model.addAttribute("entity", entity);
		} else {
			Map<String, List<GjtExamRoomNew>> roomMap = gjtExamRoomNewService
					.examPointIdRoomMap(user.getGjtOrg().getId());
			model.addAttribute("roomMap", roomMap);

			Map<String, String> pointMap = commonMapService.getExamPointMap(user.getGjtOrg().getId());
			model.addAttribute("pointMap", pointMap);

			Map<String, String> batchMap = commonMapService.getGjtExamBatchNewIdNameMap(user.getGjtOrg().getId());
			model.addAttribute("batchMap", batchMap);
		}

		model.addAttribute("entity", entity);
		model.addAttribute("action", op);
		return "edumanage/exam/exam_round_form";
	}

	@RequestMapping(value = "getExamRoomsByPointId", method = RequestMethod.GET)
	@ResponseBody
	public List<CommonSelect> getExamRoomsByPointId(String batchCode, String pointId) {
		List<CommonSelect> list = new ArrayList<CommonSelect>();
		if (StringUtils.isNotBlank(pointId)) {
			List<GjtExamRoundNew> examRoundNewsRecords = gjtExamRoundNewService.queryGjtExamRoundNewsBy(batchCode,
					pointId);
			List<GjtExamRoomNew> examRooms = gjtExamRoomNewService.queryGjtExamRoomNewByExamPoint(pointId);
			for (GjtExamRoomNew gjtExamRoomNew : examRooms) {
				if (!examRoundNewsRecords.contains(gjtExamRoomNew)) {// 过滤已存在的考场
					list.add(new CommonSelect(gjtExamRoomNew.getExamRoomId(), gjtExamRoomNew.getName()));
				}
			}
		}
		return list;
	}
}
