/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.common;

import com.gzedu.xlims.common.AppConfig;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 远程文件上传回调控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月19日
 * @version 2.5
 */
@Controller
@RequestMapping("/upload")
public class UploadController extends BaseController {

	@Autowired
	private BzrGjtStudentService gjtStudentService;

	/**
	 * 头像
     * @return
     */
	@RequestMapping(value = "headimgtransfer", method = RequestMethod.GET)
	public String updateHeadPortraitForm(Model model, HttpServletRequest request,
										 HttpSession session) {
		getPicPath(model, request);
		return "new/common/headimgtransfer";
	}

	/**
	 * 班级活动图片
     * @return
     */
	@RequestMapping(value = "headimgactivity", method = RequestMethod.GET)
	public String updateActivityForm(Model model, HttpServletRequest request,
									 HttpSession session) {
		getPicPath(model, request);
		return "new/common/headimgactivity";
	}

	/**
	 * 报读资料 上传服务器回调本项目的地址
	 BMB_Z:报名表正面
	 BMB_F:报名表反面
	 SFZ_Z:身份证正面
	 SFZ_F:身份证反面
	 BYZ_Z:毕业证正面
	 BYZ_F:毕业证反面
	 ZP:个人照片
	 XLZ:学历证
	 */
	@RequestMapping(value = "imgtransfer_BMB_Z", method = RequestMethod.GET)
	public String imgtransfer_BMB_Z(Model model, HttpServletRequest request,
									HttpSession session) {
		String picPath = getPicPath(model, request);
		String studentId = ObjectUtils.toString(request.getParameter("oldrtid")); // 标记为学员ID
		boolean flag = gjtStudentService.updateSignupCopyData(studentId, "bmb-z", picPath);
		if (flag) {
			return "new/common/imgtransfer_BMB_Z";
		} else {
			return null;
		}
	}
	@RequestMapping(value = "imgtransfer_BMB_F", method = RequestMethod.GET)
	public String imgtransfer_BMB_F(Model model, HttpServletRequest request,
									HttpSession session) {
		String picPath = getPicPath(model, request);
		String studentId = ObjectUtils.toString(request.getParameter("oldrtid")); // 标记为学员ID
		boolean flag = gjtStudentService.updateSignupCopyData(studentId, "bmb-f", picPath);
		if (flag) {
			return "new/common/imgtransfer_BMB_F";
		} else {
			return null;
		}
	}
	@RequestMapping(value = "imgtransfer_ZP", method = RequestMethod.GET)
	public String imgtransfer_ZP(Model model, HttpServletRequest request,
									HttpSession session) {
		String picPath = getPicPath(model, request);
		String studentId = ObjectUtils.toString(request.getParameter("oldrtid")); // 标记为学员ID
		boolean flag = gjtStudentService.updateSignupCopyData(studentId, "zp", picPath);
		if (flag) {
			return "new/common/imgtransfer_ZP";
		} else {
			return null;
		}
	}
	@RequestMapping(value = "imgtransfer_SFZ_Z", method = RequestMethod.GET)
	public String imgtransfer_SFZ_Z(Model model, HttpServletRequest request,
									HttpSession session) {
		String picPath = getPicPath(model, request);
		String studentId = ObjectUtils.toString(request.getParameter("oldrtid")); // 标记为学员ID
		boolean flag = gjtStudentService.updateSignupCopyData(studentId, "sfz-z", picPath);
		if (flag) {
			return "new/common/imgtransfer_SFZ_Z";
		} else {
			return null;
		}
	}
	@RequestMapping(value = "imgtransfer_SFZ_F", method = RequestMethod.GET)
	public String imgtransfer_SFZ_F(Model model, HttpServletRequest request,
									HttpSession session) {
		String picPath = getPicPath(model, request);
		String studentId = ObjectUtils.toString(request.getParameter("oldrtid")); // 标记为学员ID
		boolean flag = gjtStudentService.updateSignupCopyData(studentId, "sfz-f", picPath);
		if (flag) {
			return "new/common/imgtransfer_SFZ_F";
		} else {
			return null;
		}
	}
	@RequestMapping(value = "imgtransfer_BYZ_Z", method = RequestMethod.GET)
	public String imgtransfer_BYZ_Z(Model model, HttpServletRequest request,
									HttpSession session) {
		String picPath = getPicPath(model, request);
		String studentId = ObjectUtils.toString(request.getParameter("oldrtid")); // 标记为学员ID
		boolean flag = gjtStudentService.updateSignupCopyData(studentId, "byz-z", picPath);
		if (flag) {
			return "new/common/imgtransfer_BYZ_Z";
		} else {
			return null;
		}
	}
	@RequestMapping(value = "imgtransfer_BYZ_F", method = RequestMethod.GET)
	public String imgtransfer_BYZ_F(Model model, HttpServletRequest request,
									HttpSession session) {
		String picPath = getPicPath(model, request);
		String studentId = ObjectUtils.toString(request.getParameter("oldrtid")); // 标记为学员ID
		boolean flag = gjtStudentService.updateSignupCopyData(studentId, "byz-f", picPath);
		if (flag) {
			return "new/common/imgtransfer_BYZ_F";
		} else {
			return null;
		}
	}
	@RequestMapping(value = "imgtransfer_XLZ", method = RequestMethod.GET)
	public String imgtransfer_XLZ(Model model, HttpServletRequest request,
								 HttpSession session) {
		String picPath = getPicPath(model, request);
		String studentId = ObjectUtils.toString(request.getParameter("oldrtid")); // 标记为学员ID
		boolean flag = gjtStudentService.updateSignupCopyData(studentId, "xlz", picPath);
		if (flag) {
			return "new/common/imgtransfer_XLZ";
		} else {
			return null;
		}
	}

	/**
	 * 公共的获取图片路径
	 * @param model
	 * @param request
     * @return
     */
	private String getPicPath(Model model, HttpServletRequest request) {
		String eforgeServer = AppConfig.getProperty("file.upload.url.server");
		String rtpath = ObjectUtils.toString(request.getParameter("rtpath"));
		String rtid = ObjectUtils.toString(request.getParameter("rtid"));
		String opentype = ObjectUtils.toString(request.getParameter("opentype"));
//		String studentId = ObjectUtils.toString(request.getParameter("oldrtid")); // 标记为学员ID
		String fileName = ObjectUtils.toString(request.getParameter("fileName"));
		String picPath = eforgeServer + rtpath;

		model.addAttribute("rtpath", rtpath);
		model.addAttribute("rtid", rtid);
		model.addAttribute("fileName", fileName);
		model.addAttribute("picPath", picPath);
		return picPath;
	}

}
