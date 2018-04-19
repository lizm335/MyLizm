package com.ouchgzee.study.web.controller.learningGuide;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.dao.Collections3;
import com.gzedu.xlims.pojo.GjtArticle;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.article.GjtArticleService;
import com.ouchgzee.study.web.vo.LearningGuideVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/pcenter/learningGuide")
public class LearningGuideController {

	private final static int LEARN_CENTER_MENU_ID = 5000;

	private final static int MIANXIUMIANKAO_MENU_ID = 3410;
	@Autowired
	private GjtArticleService gjtArticleService;
	
	@RequestMapping(value = "info", method = RequestMethod.GET)
	@ResponseBody
	public List<LearningGuideVo> info(Model model, HttpServletRequest request,HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Maps.newHashMap();
		//String menuId = request.getParameter("menuId"); 
		List<LearningGuideVo> list = Lists.newArrayList();
		try{
			searchParams.put("EQ_xxId", user.getGjtStudentInfo().getXxId());
			searchParams.put("EQ_menuId", LEARN_CENTER_MENU_ID);
			GjtStudentInfo student = user.getGjtStudentInfo();
			if(student.getGjtSpecialty() != null) {
				String specialtyId = student.getGjtSpecialty().getSpecialtyId();// 专业ID
				searchParams.put("EQ_specialtyId", specialtyId);
			} 
			if(!Strings.isNullOrEmpty(student.getNj())) {
				String gradeId = student.getGjtGrade().getGradeId(); // 年级ID
				searchParams.put("EQ_gradeId", gradeId);
			}
			if(!Strings.isNullOrEmpty(student.getPycc())) {
				String pycc = student.getPycc(); // 层次
				searchParams.put("EQ_pycc", pycc);
			}
			List<GjtArticle> gjtArticleList = gjtArticleService.queryGjtArticle(searchParams);
			if(Collections3.isEmpty(gjtArticleList)) {
				searchParams.remove("EQ_specialtyId");
				searchParams.remove("EQ_gradeId");
				searchParams.remove("EQ_pycc");
				searchParams.put("EQ_ownerType", 0);
				gjtArticleList = gjtArticleService.queryGjtArticle(searchParams);
			}
			
			for(int i = 0; i<gjtArticleList.size(); i++) {
				GjtArticle gjtArticle = gjtArticleList.get(i);
				LearningGuideVo vo = new LearningGuideVo(gjtArticle);
				vo.setNo(i);
				list.add(vo);
			}
        }catch(Exception e){
        	e.printStackTrace();
        }
        return list;
	}

	@RequestMapping(value = "mianxiumiankao", method = RequestMethod.GET)
	@ResponseBody
	public LearningGuideVo mianxiumiankao(Model model, HttpServletRequest request,HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Maps.newHashMap();
		//String menuId = request.getParameter("menuId");
		LearningGuideVo vo = null;
		try{
			searchParams.put("EQ_xxId", user.getGjtStudentInfo().getXxId());
			searchParams.put("EQ_menuId", MIANXIUMIANKAO_MENU_ID);
			GjtStudentInfo student = user.getGjtStudentInfo();
			if(student.getGjtSpecialty() != null) {
				String specialtyId = student.getGjtSpecialty().getSpecialtyId();// 专业ID
				searchParams.put("EQ_specialtyId", specialtyId);
			}
			if(!Strings.isNullOrEmpty(student.getNj())) {
				String gradeId = student.getGjtGrade().getGradeId(); // 年级ID
				searchParams.put("EQ_gradeId", gradeId);
			}
			if(!Strings.isNullOrEmpty(student.getPycc())) {
				String pycc = student.getPycc(); // 层次
				searchParams.put("EQ_pycc", pycc);
			}
			List<GjtArticle> gjtArticleList = gjtArticleService.queryGjtArticle(searchParams);
			if(Collections3.isEmpty(gjtArticleList)) {
				searchParams.remove("EQ_specialtyId");
				searchParams.remove("EQ_gradeId");
				searchParams.remove("EQ_pycc");
				searchParams.put("EQ_ownerType", 0);
				gjtArticleList = gjtArticleService.queryGjtArticle(searchParams);
			}

			if(Collections3.isNotEmpty(gjtArticleList)) {
				GjtArticle gjtArticle = gjtArticleList.get(0);
				vo = new LearningGuideVo(gjtArticle);
				vo.setNo(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return vo;
	}
}
