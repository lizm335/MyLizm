/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.exam.BzrGjtRecResultService;
import com.ouchgzee.headTeacher.service.student.BzrGjtClassService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * 班级管理控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月14日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/class")
public class ClassManagerController extends BaseController {

    @Autowired
    private BzrGjtClassService gjtClassService;

    @Autowired
    private BzrGjtStudentService gjtStudentService;

    @Autowired
    private BzrCommonMapService commonMapService;

    @Autowired
    private BzrGjtRecResultService gjtRecResultService;

    /**
     * 当前班级列表
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "page.size", defaultValue = "10") int pageSize,
                       Model model, ServletRequest request, HttpSession session) {
        PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        // 班级状态 1-开启/0-关闭
        searchParams.put("EQ_isEnabled", Constants.BOOLEAN_1);
        BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
        Page<BzrGjtClassInfo> infos = gjtClassService.queryClassInfoByPage(employeeInfo.getEmployeeId(), searchParams, pageRequst);

        Map<String, String> gradeMap = commonMapService.getGradeMap(employeeInfo.getXxId());

        model.addAttribute("infos", infos);
        model.addAttribute("gradeMap", gradeMap);
        model.addAttribute("opCount", gjtClassService.countOpenClassNum(employeeInfo.getEmployeeId()));
        model.addAttribute("clCount", gjtClassService.countCloseClassNum(employeeInfo.getEmployeeId()));
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
        return "/new/class/list";
    }

    /**
     * 关闭班级列表
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "closeList", method = RequestMethod.GET)
    public String closeList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "page.size", defaultValue = "10") int pageSize,
                            Model model, ServletRequest request, HttpSession session) {
        PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        // 班级状态 1-开启/0-关闭
        searchParams.put("EQ_isEnabled", Constants.BOOLEAN_0);
        BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
        Page<BzrGjtClassInfo> infos = gjtClassService.queryClassInfoByPage(employeeInfo.getEmployeeId(), searchParams, pageRequst);

        Map<String, String> gradeMap = commonMapService.getGradeMap(employeeInfo.getXxId());

        model.addAttribute("infos", infos);
        model.addAttribute("gradeMap", gradeMap);
        model.addAttribute("opCount", gjtClassService.countOpenClassNum(employeeInfo.getEmployeeId()));
        model.addAttribute("clCount", gjtClassService.countCloseClassNum(employeeInfo.getEmployeeId()));
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
        return "/new/class/close_list";
    }

    /**
     * 进入某个班级管理
     * @param classId
     * @return
     */
    @RequestMapping(value = "go/{classId}", method = RequestMethod.GET)
    public String goClassManager(@PathVariable("classId") String classId,
                                 HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        // 加上判断，是否属于当前班主任所管理的班级
        BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
        boolean flag = gjtClassService.isClassToTeacher(classId, employeeInfo.getEmployeeId());
        if (flag) {
            super.setCurrentClass(session, gjtClassService.queryById(classId));
            super.outputJs(response, "parent.window.location.href='"+request.getContextPath()+"/home/main';");
            return null;
        }
        return "redirect:/home/class/list";
    }

    /**
     * 关闭班级
     * @param ids
     * @return
     */
    @RequestMapping(value = "close")
    @ResponseBody
    public Feedback close(String ids, ServletResponse response, HttpSession session) {
        if (StringUtils.isNotBlank(ids)) {
            try {
                String[] selectedIds = ids.split(",");
                BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
                gjtClassService.updateCloseClass(selectedIds, employeeInfo.getGjtUserAccount().getId());
                return new Feedback(true, "关闭成功");
            } catch (Exception e) {
                return new Feedback(false, "关闭失败");
            }
        }
        return new Feedback(false, "失败");
    }

    /**
     * 重开班级
     * @param ids
     * @return
     */
    @RequestMapping(value = "reopen")
    @ResponseBody
    public Feedback reopen(String ids, ServletResponse response, HttpSession session) {
        if (StringUtils.isNotBlank(ids)) {
            try {
                String[] selectedIds = ids.split(",");
                BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
                gjtClassService.updateReopenClass(selectedIds, employeeInfo.getGjtUserAccount().getId());
                return new Feedback(true, "重开成功");
            } catch (Exception e) {
                return new Feedback(false, "重开失败");
            }
        }
        return new Feedback(false, "失败");
    }

    /**
     * 学支平台--打包导出班主任所选择的班级学情的到处页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "classesConditionExportPage",method = {RequestMethod.GET,RequestMethod.POST})
    public String classesConditionExportPage(HttpServletRequest request,Model model){
        BzrGjtEmployeeInfo user = getUser(request.getSession());
        try {
            Map searchParams = Servlets.getParametersStartingWith(request,"");
            searchParams.put("BZR_ID",user.getEmployeeId());
            searchParams.put("XX_ID",user.getGjtOrg().getId());
            String phone = ObjectUtils.toString(user.getSjh());
            if(EmptyUtils.isNotEmpty(phone)){
                model.addAttribute("mobileNumber",phone.substring(phone.length()-4,phone.length()));
                request.getSession().setAttribute("downLoadClassConditonXls",searchParams);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "/new/class/export_classes_condition_page";
    }

    /**
     *  学支平台--首页批量导出班级课程学情列表下载
     */
    @RequestMapping(value = "downLoadClassConditonXls",method = {RequestMethod.POST})
    public void downLoadClassConditonXls(HttpServletRequest request, HttpServletResponse response){
        BzrGjtEmployeeInfo user = getUser(request.getSession());
        Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
        if(flag!=null&&flag==true){
            try {
                Map searchParams = (Map) request.getSession().getAttribute("downLoadClassConditonXls");
                List<File> wbList = gjtRecResultService.getClassConditons(searchParams);
                String strZipName = new String("班级学员学情数据.zip".getBytes("UTF-8"),"ISO8859-1");
                byte[] buffer = new byte[1024];
                File filePath = new File(strZipName);
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(filePath));
                out.setEncoding(System.getProperty("sun.jnu.encoding"));
                if(EmptyUtils.isNotEmpty(wbList)){
                    for (File file : wbList) {
                        FileInputStream fis = new FileInputStream(file);
                        out.putNextEntry(new ZipEntry(file.getName()));
                        int len;
                        //读入需要下载的文件的内容，打包到zip文件
                        while((len = fis.read(buffer))>0) {
                            out.write(buffer,0,len);
                        }
                        out.closeEntry();
                        fis.close();
                    }
                }
                out.close();
                FileInputStream fis = new FileInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(fis);
                response.setContentType("application/x-msdownload;charset=utf-8");
                response.setHeader("Content-Disposition","attachment; filename=" + strZipName);
                request.getSession().setAttribute(user.getSjh(),"");
                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
                byte[] buff = new byte[2048];
                while (-1 != (bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, buff.length);
                }
                fis.close();
                bis.close();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            throw new RuntimeException("您没有权限");
        }
    }

    /**
     * 学支平台--打包导出班主任所选择的班级学员信息页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "classesStudExportPage",method = {RequestMethod.GET,RequestMethod.POST})
    public  String classesStudExportPage(HttpServletRequest request,Model model){
        BzrGjtEmployeeInfo user = getUser(request.getSession());
        try {
            Map searchParams = Servlets.getParametersStartingWith(request,"");
            searchParams.put("BZR_ID",user.getEmployeeId());
            searchParams.put("XX_ID",user.getGjtOrg().getId());
            String phone = ObjectUtils.toString(user.getSjh());
            if(EmptyUtils.isNotEmpty(phone)){
                model.addAttribute("mobileNumber",phone.substring(phone.length()-4,phone.length()));
                request.getSession().setAttribute("downLoadClassStudXls",searchParams);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "/new/class/export_classes_stud_page";
    }


    /**
     *  学支平台--首页批量导出班级学员信息
     */
    @RequestMapping(value = "downLoadClassStudXls",method = {RequestMethod.POST})
    public void downLoadClassStudXls(HttpServletRequest request, HttpServletResponse response){
        BzrGjtEmployeeInfo user = getUser(request.getSession());
        Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
        if(flag!=null&&flag==true){
            try {
                Map searchParams = (Map) request.getSession().getAttribute("downLoadClassStudXls");
                List<File> wbList = gjtRecResultService.getClassStudInfo(searchParams);
                String strZipName = new String("班级学员信息数据.zip".getBytes("UTF-8"),"ISO8859-1");
                byte[] buffer = new byte[1024];
                File filePath = new File(strZipName);
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(filePath));
                out.setEncoding(System.getProperty("sun.jnu.encoding"));
                if(EmptyUtils.isNotEmpty(wbList)){
                    for (File file : wbList) {
                        FileInputStream fis = new FileInputStream(file);
                        out.putNextEntry(new ZipEntry(file.getName()));
                        int len;
                        //读入需要下载的文件的内容，打包到zip文件
                        while((len = fis.read(buffer))>0) {
                            out.write(buffer,0,len);
                        }
                        out.closeEntry();
                        fis.close();
                    }
                }
                out.close();
                FileInputStream fis = new FileInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(fis);
                response.setContentType("application/x-msdownload;charset=utf-8");
                response.setHeader("Content-Disposition","attachment; filename=" + strZipName);
                request.getSession().setAttribute(user.getSjh(),"");
                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
                byte[] buff = new byte[2048];
                while (-1 != (bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, buff.length);
                }
                fis.close();
                bis.close();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            throw new RuntimeException("您没有权限");
        }
    }





}


