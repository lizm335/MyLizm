package com.gzedu.xlims.web.controller.exam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.ToolUtil;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamRoomNew;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.exam.GjtExamRoomNewService;
import com.gzedu.xlims.service.exam.GjtRoomExamService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.common.vo.CommonSelect;

@Controller
@RequestMapping("/exam/new/room")
public class GjtExamRoomNewController {

	private static final Log log = LogFactory.getLog(GjtExamRoomNewController.class);

	@Autowired
	private GjtExamRoomNewService gjtExamRoomNewService;
	
	@Autowired
	private GjtRoomExamService gjtRoomExamService;

	@Autowired
	private CommonMapService commonMapService;

//	@RequestMapping(value = "list", method = RequestMethod.GET)
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
		Order[] orders = new Order[] { new Order(Direction.DESC, "examPointId"), new Order(Direction.ASC, "orderNo") };
		Sort sort = new Sort(orders);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, sort);

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtExamRoomNew> pageInfo = gjtExamRoomNewService.queryAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		
		//统计项
		//查询已启用总数
		searchParams.put("EQ_status", "1");
		long openOperation = gjtExamRoomNewService.queryAll(user.getGjtOrg().getId(), searchParams,pageRequst).getTotalElements();
		model.addAttribute("openOperation",openOperation);
		//查询已停用总数
		searchParams.put("EQ_status", "0");
		long closeOperation = gjtExamRoomNewService.queryAll(user.getGjtOrg().getId(), searchParams,pageRequst).getTotalElements();
		model.addAttribute("closeOperation",closeOperation);
		
		model.addAttribute("sumRoomData",openOperation+closeOperation);

		Map<String, String> pointMap = new HashMap<String, String>();
		if (searchParams.get("EQ_examPonitNew.examBatchCode") != null
				&& searchParams.get("EQ_examPonitNew.examBatchCode") != "") {
			String examBatchCode = searchParams.get("EQ_examPonitNew.examBatchCode").toString();
			pointMap = commonMapService.getExamPointMapByBatchCode(examBatchCode);
		}
		model.addAttribute("pointMap", pointMap);

		Map<String, String> batchMap = commonMapService.getGjtExamBatchNewIdNameMap(user.getGjtOrg().getId());
		model.addAttribute("batchMap", batchMap);

		model.addAttribute("user", user);
		
		return "edumanage/exam/exam_room_list";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(HttpServletRequest request, @ModelAttribute GjtExamRoomNew roomEntity) {
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
		Date now = new Date();
		roomEntity.setCreatedDt(now);
		roomEntity.setCreatedBy(user.getId());
		roomEntity.setUpdatedDt(now);
		roomEntity.setUpdatedBy(user.getId());
		roomEntity.setXxId(user.getGjtOrg().getId());
		roomEntity.setStatus(true);
		roomEntity.setIsDeleted(0);

		// TODO: @micarol 各种非空字段判断
		roomEntity = gjtExamRoomNewService.insert(roomEntity);
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
				// TODO: @micarol 使用中且没有进行排考前可以修改或删除,使用中且已经排考不允许做任何操作,已过期可以允许删除
				int rs = gjtExamRoomNewService.delete(Arrays.asList(ids.split(",")), user.getGjtOrg().getId());
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

	/**
	 * 单条考场表单, 新建 更新 查看均是本方法跳转到页面, 根据op 标识
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
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
		GjtExamRoomNew roomEntity = new GjtExamRoomNew();
		if (!"create".equals(op)) {
			roomEntity = gjtExamRoomNewService.queryBy(id);
			model.addAttribute("entity", roomEntity);
		}

		Map<String, String> batchMap = commonMapService.getGjtExamBatchNewIdNameMap(user.getGjtOrg().getId());
		model.addAttribute("batchMap", batchMap);
		
		Map<String, String> pointMap = commonMapService.getExamPointMap(user.getGjtOrg().getId());
		model.addAttribute("pointMap", pointMap);
		model.addAttribute("entity", roomEntity);
		model.addAttribute("action", op);
		return "edumanage/exam/exam_room_form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(@ModelAttribute GjtExamRoomNew entity, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
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
		GjtExamRoomNew roomEntity = gjtExamRoomNewService.queryBy(entity.getExamRoomId());
		roomEntity.setUpdatedBy(user.getId());
		roomEntity.setName(entity.getName());
		roomEntity.setSeats(entity.getSeats());
		roomEntity.setOrderNo(entity.getOrderNo());
		roomEntity.setUpdatedBy(user.getId());
		if (null == gjtExamRoomNewService.update(roomEntity)) {
			feedback.setSuccessful(false);
			feedback.setMessage("更新失败");
		}
		return feedback;
	}

	@RequestMapping(value = "getExamRoomsByPointId", method = RequestMethod.GET)
	@ResponseBody
	public List<CommonSelect> getExamRoomsByPointId(String pointId) {
		List<CommonSelect> list = new ArrayList<CommonSelect>();
		if (StringUtils.isNotBlank(pointId)) {
			List<GjtExamRoomNew> examRooms = gjtExamRoomNewService.queryGjtExamRoomNewByExamPoint(pointId);
			for (GjtExamRoomNew gjtExamRoomNew : examRooms) {
				list.add(new CommonSelect(gjtExamRoomNew.getExamRoomId(), gjtExamRoomNew.getName()));
			}
		}
		return list;
	}

	@RequestMapping(value = "getExamRoomsById", method = RequestMethod.GET)
	@ResponseBody
	public GjtExamRoomNew getExamRoomsById(String id) {
		if (StringUtils.isNotBlank(id)) {
			GjtExamRoomNew queryBy = gjtExamRoomNewService.queryBy(id);
			return queryBy;
		}
		return null;
	}
	
	//导出考场信息页面
	@RequestMapping(value = "exportRoomData",method = RequestMethod.GET)
	public String exportRoomData(Model model,HttpServletRequest request,HttpServletResponse response){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> batchMap = commonMapService.getAllExamBatchs(user.getGjtOrg().getId());
		model.addAttribute("batchMap", batchMap);
		return "edumanage/exam/mydialog/exam_room_export";
	}
	
	//导入考场信息页面
	@RequestMapping(value = "importRoomData",method = RequestMethod.GET)
	public String importRoomData(Model model,HttpServletRequest request,HttpServletResponse response){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> batchMap = commonMapService.getAllExamBatchs(user.getGjtOrg().getId());
		model.addAttribute("batchMap",batchMap);
		return "edumanage/exam/mydialog/exam_room_import";
	}
	
	@RequestMapping(value = "exportDownLoad",method = RequestMethod.POST)
	public void exportDownLoad(Model model,HttpServletRequest request,HttpServletResponse response){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("xxid", user.getGjtOrg().getId());
		String execleFileName = gjtExamRoomNewService.exportDownLoad(searchParams, request, response);
//		String file = request.getServletContext().getRealPath("/static/upload/data/examroom/考场信息导出表_")+ execleFileName;
//		String file = "E:\\EducationProject\\gjt_xlims_eclipse\\com.gzedu.xlims\\com.gzedu.xlims.web\\target\\classes"+"/excel/exam/temp/考场信息导出表_"+ execleFileName;
		String file = GjtExamRoomNewController.class.getClassLoader().getResource("").getPath()+"/excel/exam/temp/考场信息导出表_"+ execleFileName;
		
		ToolUtil.download(file, response);
		
	}
	
	@RequestMapping(value = "downloadXLS",method = RequestMethod.GET)
	public void downloadXLS(Model molde,HttpServletRequest request,HttpServletResponse response){
		String file  = GjtExamRoomNewController.class.getClassLoader().getResource("").getPath()+"/excel/exam/import_examroom_template.xls";
		ToolUtil.download(file, response);
	}
	
	public String importRoomDataXLS(Model model,HttpServletRequest request,HttpServletResponse response){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("xxid", user.getGjtOrg().getId());
		searchParams.put("CREATED_BY", user.getId());
		searchParams.put("UPDATED_BY", user.getId());
		return  null;
	}
	
	/**
	 * 考场管理列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String getExamRoomList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		
		// 默认选择当前期(批次)
        if(EmptyUtils.isEmpty(searchParams)|| StringUtils.isBlank((String) searchParams.get("EXAM_BATCH_ID"))){
            String id = commonMapService.getCurrentGjtExamBatchNewId(user.getGjtOrg().getId());
            searchParams.put("EXAM_BATCH_ID", id);
            model.addAttribute("examBatchId",id);
        }else if(EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_ID")) ){
            model.addAttribute("examBatchId", ObjectUtils.toString(searchParams.get("EXAM_BATCH_ID")));
        }
        
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = gjtExamRoomNewService.getExamRoomList(searchParams, pageRequest);
		
		model.addAttribute("pageInfo", pageInfo);
		
		Map<String, Object> batchMap = gjtExamRoomNewService.getExamBatchInfo(searchParams);
		model.addAttribute("batchMap", batchMap.get("BATCHLIST"));
		
		model.addAttribute("user", user);
		
		return "edumanage/exam/exam_room_listInfo";

	}
	
	/**
	 * 查询考点信息
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value="/queryExamPoint")
	@ResponseBody
	public Map<String, Object> queryExamPoint(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		Map<String, Object> resultMap = gjtExamRoomNewService.getExamPointList(searchParams);
		
		return resultMap;
	}
	
	/**
	 * 删除考场
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value="/truncExamRoom",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> truncExamRoom(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		
		Map<String, Object> resultMap = gjtRoomExamService.truncExamRoom(searchParams);
		
		return resultMap;
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/examRoomStatus",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> examRoomStatus(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		
		if("0".equals(ObjectUtils.toString(searchParams.get("STATUS")))){
			searchParams.put("STATUS", "1");
		}else if("1".equals(ObjectUtils.toString(searchParams.get("STATUS")))){
			searchParams.put("STATUS", "0");
		}
		
		Map<String, Object> resultMap = gjtRoomExamService.examRoomStatus(searchParams);
		
		return resultMap;
		
	}
	
	/**
	 * 根据搜索条件导出考场
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "exportRoom")
	@ResponseBody
	public void exportRoom(HttpServletRequest request, HttpServletResponse response) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequest = Servlets.buildPageRequest(1, 10000, "createdDt", "DESC");// 单次最多导出10000条
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		Page pageInfo = gjtExamRoomNewService.getExamRoomList(searchParams, pageRequest);
		
		HSSFWorkbook workbook = gjtExamRoomNewService.exportRoom(pageInfo.getContent());
		String fileName = "考场表.xls";
		response.setContentType("application/x-msdownload;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
		workbook.write(response.getOutputStream());
	}
	
	/**
	 * 导入考场页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "importRoom", method = RequestMethod.GET)
	public String importRoom(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		return "edumanage/exam/exam_room_import";
	}
	
	/**
	 * 导入考场
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "importRoomInfo")
	@ResponseBody
	public ImportFeedback importRoomInfo(@RequestParam("file") MultipartFile file, 
			HttpServletRequest request, HttpServletResponse response) {
		try {
			
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "考试计划名称", "考试计划编号", "考点名称", "考点编号", "考场名称", "顺序号", "座位数", "导入结果"};
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}
			
			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据
					
					Map tempMap = new HashMap();
					tempMap.put("EXAM_BATCH_NAME", ObjectUtils.toString(datas[0]).trim());
					tempMap.put("EXAM_BATCH_CODE", ObjectUtils.toString(datas[1]).trim());
					tempMap.put("POINT_NAME", ObjectUtils.toString(datas[2]).trim());
					tempMap.put("POINT_CODE", ObjectUtils.toString(datas[3]).trim());
					tempMap.put("ROOM_NAME", ObjectUtils.toString(datas[4]).trim());
					tempMap.put("ORDER_NO", ObjectUtils.toString(datas[5]).trim());
					tempMap.put("SEATS", ObjectUtils.toString(datas[6]).trim());
					tempMap.put("XX_ID", user.getGjtOrg().getId());
					
					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}
					
					if (EmptyUtils.isEmpty(datas[1])) {
						result[heads.length - 1] = "考试计划编号不能为空";
						failedList.add(result);
						continue;
					}

					if (EmptyUtils.isEmpty(datas[3])) {
						result[heads.length - 1] = "考点编号不能为空";
						failedList.add(result);
						continue;
					}
					
					if (EmptyUtils.isEmpty(datas[4])) {
						result[heads.length - 1] = "考场名称不能为空";
						failedList.add(result);
						continue;
					}
					
					if (EmptyUtils.isEmpty(datas[5])) {
						result[heads.length - 1] = "顺序号不能为空";
						failedList.add(result);
						continue;
					}
					
					if (EmptyUtils.isEmpty(datas[6])) {
						result[heads.length - 1] = "座位数不能为空";
						failedList.add(result);
						continue;
					}
					
					Map roomMap = gjtExamRoomNewService.getRoomInfo(tempMap);
					if (EmptyUtils.isNotEmpty(roomMap)) {
						String exam_point_id = ObjectUtils.toString(roomMap.get("EXAM_POINT_ID"));
						String exam_room_id = ObjectUtils.toString(roomMap.get("EXAM_ROOM_ID"));
						
						tempMap.put("EXAM_POINT_ID", exam_point_id);
						tempMap.put("EXAM_ROOM_ID", exam_room_id);
						tempMap.put("NAME", ObjectUtils.toString(tempMap.get("ROOM_NAME")));
						
						
						if (EmptyUtils.isNotEmpty(exam_point_id)) {
							if (EmptyUtils.isNotEmpty(exam_room_id)) {
								int num = gjtExamRoomNewService.updRoomData(tempMap);
								if (num>0) {
									result[heads.length - 1] = "修改成功";
									successList.add(result);
								} else {
									result[heads.length - 1] = "修改失败";
									failedList.add(result);
								}
							} else {
								tempMap.put("STATUS", "1");
								tempMap.put("EXAM_ROOM_ID", SequenceUUID.getSequence());
								int num = gjtExamRoomNewService.saveRoomData(tempMap);
								if (num>0) {
									result[heads.length - 1] = "新增成功";
									successList.add(result);
								} else {
									result[heads.length - 1] = "新增失败";
									failedList.add(result);
								}
							}
						} else {
							result[heads.length - 1] = "考点编号不存在";
							failedList.add(result);
							continue;
						}
					} else {
						result[heads.length - 1] = "考试计划编号不存在";
						failedList.add(result);
						continue;
					}
				}
			}
			
			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "examRoom_success_" + currentTimeMillis + ".xls";
			String failedFileName = "examRoom_failed_" + currentTimeMillis + ".xls";
			
			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "批量导入考场成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "批量导入考场失败记录");
			
			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "examPlan" + File.separator;
			File f = new File(filePath);
			if (!f.exists()) {
				f.mkdirs();
			}
			
			File successFile = new File(filePath, successFileName);
			successFile.createNewFile();
			ExcelUtil.writeWorkbook(workbook1, successFile);

			File failedFile = new File(filePath, failedFileName);
			failedFile.createNewFile();
			ExcelUtil.writeWorkbook(workbook2, failedFile);

			return new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(), successFileName, failedFileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, "系统异常！");
		}

	}
}
