package com.gzedu.xlims.serviceImpl.exam;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.ExcelUtil;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.exam.GjtExamRoomNewDao;
import com.gzedu.xlims.pojo.exam.GjtExamRoomNew;
import com.gzedu.xlims.service.exam.GjtExamRoomNewService;

import net.sf.jxls.transformer.XLSTransformer;

@Service
public class GjtExamRoomNewServiceImpl implements GjtExamRoomNewService {

	private static final Log log = LogFactory.getLog(GjtExamRoomNewServiceImpl.class);

	@Autowired
	private GjtExamRoomNewDao gjtExamRoomNewDao;

	@Override
	public GjtExamRoomNew insert(GjtExamRoomNew entity) {
		entity.setExamRoomId(UUIDUtils.random());
		try {
			entity = gjtExamRoomNewDao.insert(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			entity = null;
		}
		return entity;
	}

	@Override
	public Page<GjtExamRoomNew> queryAll(String schoolId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, schoolId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, 0));
//		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, Constants.NODELETED));
		Specification<GjtExamRoomNew> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExamRoomNew.class);
		return gjtExamRoomNewDao.findAll(spec, pageRequst);
	}

	/**
	 * 根据id 删除考场 PS: 该方法不做任何业务上的数据校验
	 */
	@Override
	public int delete(List<String> ids, String xxid) {
		return gjtExamRoomNewDao.deleteGjtExamRoomNew(ids, xxid);
	}

	@Override
	public GjtExamRoomNew queryBy(String id) {
		return gjtExamRoomNewDao.queryBy(id);
	}

	@Override
	public GjtExamRoomNew update(GjtExamRoomNew entity) {
		entity.setUpdatedDt(new Date());
		try {
			entity = gjtExamRoomNewDao.save(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			entity = null;
		}
		return entity;
	}

	/**
	 * 将考场设置为禁用
	 */
	@Override
	public GjtExamRoomNew closeRoom(GjtExamRoomNew entity) {
		// TODO: @micarol 业务逻辑判断
		return this.update(entity);
	}

	@Override
	public Map<String, List<GjtExamRoomNew>> examPointIdRoomMap(String xxid) {
		return gjtExamRoomNewDao.examPointIdRoomMap(xxid);
	}

	@Override
	public List<GjtExamRoomNew> queryGjtExamRoomNewByExamPoint(String examPointId) {
		return gjtExamRoomNewDao.roomsByPointid(examPointId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String exportDownLoad(Map searchParams,HttpServletRequest request,HttpServletResponse response){
		String fileName = "";
		try{
			List list = gjtExamRoomNewDao.exportPointInfo(searchParams);
			Map beans = new HashMap();
			if(EmptyUtils.isNotEmpty(list)){
				Map map = (Map) list.get(0);
				beans.putAll(map);
			}
			List datas = gjtExamRoomNewDao.exportRoomInfo(searchParams);
			if(EmptyUtils.isNotEmpty(datas)){
				beans.put("dataList", datas);
			}
			
			XLSTransformer transformer = new XLSTransformer();
			fileName = new Date().getTime() + ".xls";
			//File file = ResourceUtils.getFile("classpath:"+"/excel/exam/expExamRoomTemplat.xls");
//			transformer.transformXLS("E:\\EducationProject\\gjt_xlims_eclipse\\com.gzedu.xlims\\com.gzedu.xlims.web\\target\\classes" + "/excel/exam/expExamRoomTemplat.xls", beans, "E:\\EducationProject\\gjt_xlims_eclipse\\com.gzedu.xlims\\com.gzedu.xlims.web\\target\\classes"
//                    + "/excel/exam/temp/考场信息导出表_" + fileName);
			System.out.println();
			transformer.transformXLS(GjtExamRoomNewServiceImpl.class.getClassLoader().getResource("").getPath()+ "/excel/exam/expExamRoomTemplat.xls", beans, GjtExamRoomNewServiceImpl.class.getClassLoader().getResource("").getPath()
                  + "/excel/exam/temp/考场信息导出表_" + fileName);
		}catch(Exception e){
			e.printStackTrace();
		}
		return fileName;
	}
	
	public Map importExamRoomXls(File filename,Map<String, String> searchParams){
		Map map = new HashMap();
		HSSFWorkbook wb = null;
		List rightList = new ArrayList();
		List errorList = new ArrayList();
		String errorMessage = "";
		try{
			wb = new HSSFWorkbook(new FileInputStream(filename));
			HSSFSheet sheetAt = wb.getSheetAt(0);
			HSSFRow titleRow = sheetAt.getRow(2);
			
			String titleRow0 = ObjectUtils.toString(ExcelUtil.getStringCellValue(titleRow.getCell((short)0)),"").trim();
			String titleRow1 = ObjectUtils.toString(ExcelUtil.getStringCellValue(titleRow.getCell((short)1)),"").trim();
	        String titleRow2 = ObjectUtils.toString(ExcelUtil.getStringCellValue(titleRow.getCell((short)2)),"").trim();
	        String titleRow3 = ObjectUtils.toString(ExcelUtil.getStringCellValue(titleRow.getCell((short)3)),"").trim();
	        String titleRow4 = ObjectUtils.toString(ExcelUtil.getStringCellValue(titleRow.getCell((short)4)),"").trim();
	        
	        String[] filds = new String[] {"考点编号","考点名称","考场名称","顺序号","座位数"};
	        if(filds[0].equals(titleRow0) && filds[1].equals(titleRow1) && filds[2].equals(titleRow2) && filds[3].equals(titleRow3) && filds[4].equals(titleRow4)){
	        	int lastRowNum = sheetAt.getLastRowNum();
	        	
	        	if(lastRowNum > 1003){
	        		 errorMessage = "导入的考场信息数量不能大于1000条！";
	        	}else{
	        		for(int i=3;i<=lastRowNum;i++){
	        			HSSFRow row = sheetAt.getRow(i);
	        			if(EmptyUtils.isNotEmpty(row)){
	        				Map tempMap = new HashMap();
	        				Map errorInfo = new HashMap();
	        				StringBuffer error_str = new StringBuffer();
	        				int errorNum = 0;
	        				String exampoint_code  = ObjectUtils.toString(ExcelUtil.getStringCellValue(row.getCell((short) 0))).trim();
                            String exampoint_name  = ObjectUtils.toString(ExcelUtil.getStringCellValue(row.getCell((short) 1))).trim();
                            String examroom_name = ObjectUtils.toString(ExcelUtil.getStringCellValue(row.getCell((short) 2))).trim();
                            String examroom_no = ObjectUtils.toString(ExcelUtil.getStringCellValue(row.getCell((short) 3))).trim();
                            String examroom_seat = ObjectUtils.toString(ExcelUtil.getStringCellValue(row.getCell((short) 4))).trim();
                            
                            tempMap.put("NUM", ObjectUtils.toString(i));
                            tempMap.put("EXAM_ROOM_ID", SequenceUUID.getSequence());
                            tempMap.put("EXAMPOINT_CODE", exampoint_code);
                            tempMap.put("EXAMPOINT_NAME", exampoint_name);
                            tempMap.put("EXAM_ROOM_NAME", examroom_name);
                            tempMap.put("ORDER_NO", examroom_no);
                            tempMap.put("SEATS", examroom_seat);
                            tempMap.put("STATUS", 1);
                            tempMap.put("CREATED_BY", ObjectUtils.toString(searchParams.get("CREATED_BY")));
                            tempMap.put("UPDATED_BY", ObjectUtils.toString(searchParams.get("UPDATED_BY")));
                            tempMap.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
                            
                            if(EmptyUtils.isNotEmpty(exampoint_code)){
                            	error_str.append("考点编号不能为空;");
                            	errorNum++;
                            }else{
                            	List list = gjtExamRoomNewDao.queryExamPiontByCode(tempMap);
                            	if(EmptyUtils.isNotEmpty(list)){
                            		tempMap.put("EXAM_POINT_ID", ((Map)list.get(0)).get("EXAM_POINT_ID"));
                            	}else{
                            		error_str.append("考点编号不存在，请注意核对;");
                            		errorNum++;
                            	}
                            }
                            if(EmptyUtils.isNotEmpty(exampoint_name)){
                            	error_str.append("考点名称不能为空;");
                                errorNum++;
                            }
                            if(EmptyUtils.isEmpty(examroom_name)){
                                error_str.append("考场名称不能为空;");
                                errorNum++;
                            }else if(EmptyUtils.isNotEmpty(examroom_name)&&examroom_name.length()>200){
                                error_str.append("考场名称不能大于100个字;");
                                errorNum++;
                            }
                            if(EmptyUtils.isNotEmpty(examroom_no)){
                            	 error_str.append("顺序号不能为空;");
                                 errorNum++;
                            }
                            if(!NumberUtils.isNumber(examroom_seat)){
                                error_str.append("座位数必须为数字;");
                            }
                            if(error_str.length()>0){//存在错误信息
                                error_str.insert(0,"第"+(i+1)+"行错误信息:");
                             }
                            
                            if (EmptyUtils.isNotEmpty(error_str.toString())) {
                                tempMap.put("ERRORSTR", error_str.toString());
                                errorList.add(tempMap);
                            } else {
                                rightList.add(tempMap);
                            }
	        			}
	        		}
	        	}
	        	
	        	for(int i=0;i<rightList.size();i++){
	        		Map tempMap = (Map) rightList.get(i);
	        		gjtExamRoomNewDao.saveRoomData(tempMap);
	        	}
	        }
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "excel格式不符合要求，请下载正确的模板，填写后再进行导入！";
		}
		map.put("errorMessage", errorMessage);
		map.put("errorNum", errorList.size());
		map.put("rightNum", rightList.size());
		map.put("allNum", errorList.size()+rightList.size());
		return map;
	}

	/**
	 * 考场管理列表
	 */
	@SuppressWarnings("rawtypes")
	public Page getExamRoomList(Map<String, Object> searchParams, PageRequest pageRequest) {
		return gjtExamRoomNewDao.getExamRoomList(searchParams, pageRequest);
	}

	/**
	 * 查询考试批次
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getExamBatchInfo(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			List list = gjtExamRoomNewDao.getExamBatchInfo(searchParams);
			if(EmptyUtils.isNotEmpty(list)){
				resultMap.put("BATCHLIST", list);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 查询考点信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getExamPointList(Map<String, Object> searchParams) {
		Map resultMap = new HashMap();
		try{
			List list = gjtExamRoomNewDao.getExamPointList(searchParams);
			if(EmptyUtils.isNotEmpty(list)){
				resultMap.put("POLINTLIST", list);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 导出考场
	 */
	public HSSFWorkbook exportRoom(List list) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet();
			HSSFRow row;
			HSSFCell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			
			// 标题行
			row = sheet.createRow(rowIndex++);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue("考试计划");
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue("考点名称");
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue("考场名称");
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue("序列号");
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue("座位数");
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue("考场状态");
			
			for (int i=0; i<list.size(); i++) {
				Map tempMap = (Map)list.get(i);
				
				cellIndex = 0;
				row = sheet.createRow(rowIndex++);
				
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(tempMap.get("EXAM_BATCH_NAME")));
				
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(tempMap.get("EXAM_POINT_NAME")));
				
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(tempMap.get("EXAM_ROOM_NAME")));
				
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(tempMap.get("ORDER_NO")));
				
				cell = row.createCell(cellIndex++);
				cell.setCellValue(ObjectUtils.toString(tempMap.get("SEATS")));
				
				cell = row.createCell(cellIndex++);
				if ("1".equals(ObjectUtils.toString(tempMap.get("STATUS")))) {
					cell.setCellValue("已启用");
				} else {
					cell.setCellValue("已停用");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}
	
	/**
	 * 查询考场信息
	 */
	public Map getRoomInfo(Map searchParams) {
		Map resultMap = new HashMap();
		try{
			List list = gjtExamRoomNewDao.getRoomInfo(searchParams);
			if(EmptyUtils.isNotEmpty(list)){
				resultMap = (Map)list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 新增考场
	 */
	public int saveRoomData(Map searchParams) {
		return gjtExamRoomNewDao.saveRoomData(searchParams);
	}
	
	/**
	 * 修改考场
	 */
	public int updRoomData(Map searchParams) {
		return gjtExamRoomNewDao.updRoomData(searchParams);
	}
}
