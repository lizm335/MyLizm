/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.web.controller.graduation;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Strings;
import com.gzedu.xlims.common.DateUtil;
import com.gzedu.xlims.common.Encodes;
import com.gzedu.xlims.dao.dictionary.GjtDistrictDao;
import com.gzedu.xlims.pojo.GjtDistrict;
import com.gzedu.xlims.pojo.graduation.GjtPhotographData;
import com.gzedu.xlims.service.graduation.GjtPhotographDataService;
import com.ouchgzee.study.web.vo.graduation.GjtPhotographDataVO;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.BeanConvertUtils;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtPhotographAddress;
import com.gzedu.xlims.pojo.graduation.GjtPhotographUser;
import com.gzedu.xlims.service.dictionary.GjtDistrictService;
import com.gzedu.xlims.service.graduation.GjtPhotographAddressService;
import com.gzedu.xlims.service.graduation.GjtPhotographUserService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.ouchgzee.study.web.common.Servlets;
import com.ouchgzee.study.web.vo.graduation.PhotographAddressVO;

/**
 * 功能说明：
 *
 * @author zhangweiwen
 * @version 1.0
 * @Date 2018年3月28日
 */
@Controller
@RequestMapping("/pcenter/photograph")
public class PhotographAddressController {
    private final static Logger log = LoggerFactory.getLogger(PhotographAddressController.class);

    @Autowired
    private GjtPhotographUserService gjtPhotographUserService;

    @Autowired
    private GjtPhotographAddressService gjtPhotographAddressService;

    @Autowired
    private GjtDistrictService gjtDistrictService;

    @Autowired
    private GjtOrgService gjtOrgService;

    @Autowired
    private GjtPhotographDataService gjtPhotographDataService;


    // 查看学员是否预约.是的话，如果是电大的 还要显示时间，如果是新华社不理
    @RequestMapping(value = "queryIsAppointment", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryIsAppointment(HttpServletRequest request) throws CommonException {
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        GjtPhotographUser item = gjtPhotographUserService.findByUserId(user.getId());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        PhotographAddressVO vo = null;
        long day = 0;
        if (item != null) {
            resultMap.put("isAppointment", "1");
            resultMap.put("isOvertime", "0");
            resultMap.put("photographDate", "");
            GjtPhotographAddress entity = item.getGjtPhotographAddress();

            if (entity.getType().equals("1")) {
                if( new Date().getTime() > entity.getPhotographEndDate().getTime() ){
                    resultMap.put("isOvertime", "1");
                }else{
                    day = DateUtil.bettweenDateByTime(new Date(), entity.getPhotographEndDate());
//                        (entity.getPhotographEndDate().getTime() - DateUtils.getDate().getTime()) * 1000 * 60 * 60 * 24;
                }
            }else{
                resultMap.put("photographDate", entity.getPhotographDate());
            }
            vo = BeanConvertUtils.convert(entity, PhotographAddressVO.class);
            vo.setDistrict(gjtDistrictService.queryAllNameById(entity.getDistrict()));

        } else {
            resultMap.put("isAppointment", "0");
        }

        //拍摄现场填写资料
        GjtOrg org = gjtOrgService.queryParentBySonId(user.getGjtOrg().getId());
        GjtPhotographDataVO gjtPhotographDataVo = null;
        if (org != null) {
            GjtPhotographData gjtPhotographData = gjtPhotographDataService.findByXxId(org.getId());
            if (gjtPhotographData != null) {
                gjtPhotographDataVo = BeanConvertUtils.convert(gjtPhotographData, GjtPhotographDataVO.class);
                gjtPhotographDataVo.setXh(user.getGjtStudentInfo().getXh());
                gjtPhotographDataVo.setXxmc(user.getGjtStudentInfo().getGjtSchoolInfo().getXxmc());
                gjtPhotographDataVo.setZymc(user.getGjtStudentInfo().getGjtSpecialty().getZymc());
                resultMap.put("gjtPhotographData", gjtPhotographDataVo);
            }
        } else {
            resultMap.put("gjtPhotographData", gjtPhotographDataVo);
        }
//        resultMap.put("xh",user);
        resultMap.put("differ", day);
        resultMap.put("info", vo);
        return resultMap;
    }

    // 考点列表
    @RequestMapping(value = "applyList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> applyList(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        GjtOrg org = gjtOrgService.queryParentBySonId(user.getGjtOrg().getId());
        searchParams.put("EQ_orgId", org.getId());
        String type = request.getParameter("type");
        if (StringUtils.isNotBlank(type)) {
            searchParams.put("EQ_type", type);
        }
        searchParams.put("EQ_isDeleted", "N");
        searchParams.put("EQ_isEnabled", "1");


        if (StringUtils.isNotBlank(request.getParameter("province"))) {
            String provinces = URLDecoder.decode(ObjectUtils.toString(request.getParameter("province")), "UTF-8");
            GjtDistrict gjtDistrict = gjtDistrictService.queryByName("", provinces);
            if (gjtDistrict != null) {
                searchParams.put("EQ_province", provinces);
            }
        }
        if (StringUtils.isNotBlank(request.getParameter("city"))) {
            String city = URLDecoder.decode(ObjectUtils.toString(request.getParameter("city")), "UTF-8");
            GjtDistrict gjtDistrict = gjtDistrictService.queryByName("", city);
            if (gjtDistrict != null) {
                searchParams.put("EQ_city", city);
            }
        }


        log.info("查询参数：{}", searchParams);
        Page<GjtPhotographAddress> page = gjtPhotographAddressService.queryPageInfo(searchParams, pageRequst);

        List<PhotographAddressVO> list = new ArrayList<PhotographAddressVO>();
        for (GjtPhotographAddress item : page) {
            PhotographAddressVO vo = BeanConvertUtils.convert(item, PhotographAddressVO.class);
            String allName = gjtDistrictService.queryAllNameById(item.getDistrict());
            vo.setDistrict(allName);


            GjtPhotographUser photographUser = gjtPhotographUserService.findByUserId(user.getId());
            if (null != photographUser && photographUser.getPhotographId().equals(vo.getId())) {//是否预约
                vo.setIsAppointment("1");
            } else {
                vo.setIsAppointment("0");
            }

            list.add(vo);
        }
        Page<PhotographAddressVO> pageInfo = new PageImpl<PhotographAddressVO>(list, pageRequst,
                page.getTotalElements());
        resultMap.put("pageInfo", pageInfo);
        return resultMap;
    }

    // 预约报名点
    @RequestMapping(value = "applyAdd", method = RequestMethod.POST)
    @ResponseBody
    public void applyAdd(HttpServletRequest request, String id) throws CommonException {
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

        if (StringUtils.isBlank(id)) {
            throw new CommonException(MessageCode.BAD_REQUEST, "id 为空");
        }
//        GjtPhotographUser item = gjtPhotographUserService.findByUserId(user.getId());
        GjtPhotographUser item = gjtPhotographUserService.findByUserIdAndAddressId(user.getId(), id);
        if (item != null) {
            throw new CommonException(MessageCode.BIZ_ERROR, "已经预约成功，请勿重复操作");
        }

        GjtPhotographUser originalItem = gjtPhotographUserService.findByUserId(user.getId());//原有数据
        if (null != originalItem) {
            gjtPhotographUserService.delete(originalItem);
        }

        GjtPhotographUser entity = new GjtPhotographUser();
        entity.setId(UUIDUtils.random());
        entity.setUserId(user.getId());
        entity.setPhotographId(id);
        gjtPhotographUserService.save(entity);
    }
}