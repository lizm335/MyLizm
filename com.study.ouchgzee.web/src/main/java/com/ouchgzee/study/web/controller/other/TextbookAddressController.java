package com.ouchgzee.study.web.controller.other;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.FormSubmitUtil;
import com.gzedu.xlims.common.Objects;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.pojo.GjtStudentAddress;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.textbook.GjtStudentAddressService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.TextbookAddressComponent;

@Controller
@RequestMapping("/static/tba")
public class TextbookAddressController extends BaseController {

    @Autowired
    private TextbookAddressComponent textbookAddressComponent;

    @Autowired
    private GjtStudentInfoService gjtStudentInfoService;
    
    @Autowired
    private GjtStudentAddressService gjtStudentAddressService;
    
	@Autowired
	private CacheService cacheService;

    @RequestMapping(value = "/batchImport", method = RequestMethod.GET)
    public void batchImport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.outputHtml(
                response,
                FormSubmitUtil.buildRequest("batchImport", Collections.EMPTY_MAP, RequestMethod.POST.name(), "上传", "file")
        );
    }

    /**
     * 批量更新教材地址确认名单，可以使用Postman请求<br/>
     * 请求头配置：Content-Type: multipart/form-data 和 Cookie<br/>
     * 请求体配置：file: 文件<br/>
     * @param file csv/txt格式文件 格式：学号,...
     * @throws Exception
     */
    @RequestMapping(value = "/batchImport", method = RequestMethod.POST)
    public void batchImport(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(file.getInputStream(), Constants.CHARSET));
        String outputFileName = "导入结果.csv";
        OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), Constants.CHARSET);
        BufferedWriter bw = new BufferedWriter(osw);

        String fileName = com.gzedu.xlims.common.StringUtils.getBrowserStr(request, outputFileName); // 解决下载文件名乱码问题（兼容性）
        response.setContentType("application/x-msdownload;charset=utf-8");
        // 解决IE下载文件名乱码问题
        // response.setHeader("Content-Disposition", "attachment; filename=" + new String(outputFileName.getBytes("GB2312"), "ISO8859-1");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        String str = null;
        while ((str = bf.readLine()) != null) {
            try {
                String[] strs = str.split(",");
                String studentId = gjtStudentInfoService.queryStudentIdByXh(strs[0]);
                if(studentId == null) {
                    bw.write(str + ",学员不存在");
                    bw.newLine();
                    continue;
                }

                textbookAddressComponent.setCached(studentId, false); // 设置未确认
                bw.write(str + ",已设置为未确认");
                bw.newLine();
            } catch (Exception e) {
                bw.write(str + "," + e.getMessage());
                bw.newLine();
            }
            bw.flush();
        }

        bf.close();
        bw.close();
        osw.close();
    }

    /**
     * 导出教材地址确认情况<br/>
     * @throws Exception
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String outputFileName = "教材地址确认情况.csv";
        String fileName = com.gzedu.xlims.common.StringUtils.getBrowserStr(request, outputFileName); // 解决下载文件名乱码问题（兼容性）
        response.setContentType("application/x-msdownload;charset=utf-8");
        // 解决IE下载文件名乱码问题
        // response.setHeader("Content-Disposition", "attachment; filename=" + new String(outputFileName.getBytes("GB2312"), "ISO8859-1");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), Constants.CHARSET);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write("姓名,学号,身份证号,手机号,收货地址,是否确认");
        bw.newLine();
        bw.flush();
        
        Map<String, Object> data = textbookAddressComponent.getAll();
        for (Map.Entry<String, Object> e : data.entrySet()) {
//            if((Boolean) e.getValue()) {
                GjtStudentInfo info = gjtStudentInfoService.queryById(e.getKey());
        		List<GjtStudentAddress> addressList = gjtStudentAddressService.findByStudentId(info.getStudentId());
        		String address = "";
        		for (GjtStudentAddress a : addressList) {
        			if(a.getIsDefault() == 1) {
	        			// 教材邮寄地址
	    				String provinceCode = a.getProvinceCode();
	    				if (provinceCode != null) {
	    					address += Objects.toString(
	    							cacheService.getCachedDictionaryName(CacheService.DictionaryKey.PROVINCE, provinceCode),
	    							"");
	    					String cityCode = a.getCityCode();
	    					if (cityCode != null) {
	    						address += Objects.toString(
	    								cacheService.getCachedDictionaryName(
	    										CacheService.DictionaryKey.CITY.replace("${Province}", provinceCode), cityCode),
	    								"");
	    						address += Objects.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.AREA
	    								.replace("${Province}", provinceCode).replace("${City}", cityCode),
	    								a.getAreaCode()), "");
	    					}
	    				}
	    				address += Objects.toString(a.getAddress(), "");
	    				address = StringUtils.replaceBlank(address).replace(",", "，");
	        			break;
        			}
        		}
                bw.write(info.getXm() + "," + info.getXh() + "," + info.getSfzh() + "," + info.getSjh() + "," + address + "," + ((Boolean) e.getValue() ? "已确认" : "未确认"));
                bw.newLine();
                bw.flush();
//            }
        }

        bw.close();
        osw.close();
    }

}