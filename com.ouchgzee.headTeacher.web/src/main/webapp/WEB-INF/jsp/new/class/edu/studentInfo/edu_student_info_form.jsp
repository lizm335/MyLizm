<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 学员管理</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
    <!-- 学籍资料相关样式 -->
    <link rel="stylesheet" href="${ctx}/static/dist/css/signup-info.css">
</head>
<body class="inner-page-body">
    <section class="content-header">
        <button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
        <div class="pull-left">
            您所在位置：
        </div>
        <ol class="breadcrumb">
            <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
            <li><a href="#">教务管理</a></li>
            <li><a href="${ctx}/home/class/studentInfo/list">学员管理</a></li>
            <li class="active">资料详情</li>
        </ol>
    </section>

    <section class="content">
        <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
        <div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

        <div class="box">
            <div class="box-body">
                <div class="media pad">
                    <div class="media-left" style="padding-right:25px;">
                        <img id ="headImgId" src="${not empty item.avatar ? item.avatar : ctx.concat('/static/images/headImg04.png')}" class="img-circle" style="width:112px;height:112px;" alt="User Image" onerror="this.src='${ctx }/static/images/headImg04.png'">
                    </div>
                    <div class="media-body">
                        <h3 class="margin_t10">
                            ${item.xm}
                            <small class="f14">(<dic:getLabel typeCode="Sex" code="${item.xbm }" />)</small>
                        </h3>
                        <div class="row">
                            <div class="col-xs-6 col-sm-4 pad-b5">
                                <b>学号:</b> <span>${item.xh}</span>
                            </div>
                            <div class="col-xs-6 col-sm-4 pad-b5">
                                <b>手机:</b>
                                <span>${item.sjh}</span>
                            </div>
                            <div class="col-xs-6 col-sm-4 pad-b5">
                                <b>邮箱:</b> <span>${item.dzxx}</span>
                            </div>
                            <div class="col-xs-6 col-sm-4 pad-b5">
                                <b>层次:</b> <span><dic:getLabel typeCode="TrainingLevel" code="${item.pycc }" /></span>
                            </div>
                            <div class="col-xs-6 col-sm-4 pad-b5">
                                <b>专业:</b> <span>${item.gjtSpecialty.zymc}</span>
                            </div>
                            <div class="col-xs-6 col-sm-4 pad-b5">
                                <b>学习中心:</b> <span>${item.gjtStudyCenter.scName}</span>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <div class="box-footer">
                <div class="row stu-info-status">
                    <div class="col-sm-3 col-xs-6">
                        <div class="f24 text-center">${item.isEnteringSchool=='1'?'已确认':'<span class="text-red">未确认</span>'}</div>
                        <div class="text-center gray6">入学确认</div>
                    </div>
                    <div class="col-sm-3 col-xs-6">
                        ${item.perfectStatus==1?'<div class="text-green f24 text-center">已完善</div>':'<div class="text-red f24 text-center">未完善</div>' }
                        <div class="text-center gray6">资料状态</div>
                    </div>
                    <div class="col-sm-3 col-xs-6">
                        <c:choose>
                            <c:when test="${item.gjtSignup.auditState=='0'}"><div class="text-red f24 text-center">审核不通过</div></c:when>
                            <c:when test="${item.gjtSignup.auditState=='1'}"><div class="text-green f24 text-center">审核通过</div></c:when>
                            <c:otherwise><div class="text-orange f24 text-center">待审核</div></c:otherwise>
                        </c:choose>
                        <div class="text-center gray6">资料审批</div>
                    </div>
                    <div class="col-sm-3 col-xs-6">
                        <div class="text-center table-block full-width" style="height:64px;">
                            <div class="table-cell-block vertical-middle">
                                <c:if test="${item.isEnteringSchool!='1'}">
                                    <button type="button" class="btn btn-default btn-md" data-role="sure-btn-1" val="${item.studentId}">
                                        <i class="fa fa-confirm-stu f20 vertical-middle"></i>
                                        确认入学
                                    </button>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="box no-border margin-bottom-none">
            <form id="inputForm" class="theform" role="form" action="${ctx }/edumanage/schoolRollInfo/audit.html" method="post">
                <input type="hidden" name="action" value="${action}" />
                <input type="hidden" name="studentId" value="${item.studentId}" />
                <input type="hidden" name="auditState" value="" />

                <div class="nav-tabs-custom no-margin">
                <ul class="nav nav-tabs nav-tabs-lg">
                    <li class="active"><a href="#tab_notice_1" data-toggle="tab">报名资料<small class="label bg-red reset-label margin_l5">未完善</small></a></li>
                    <li><a href="#tab_notice_2" data-toggle="tab">证件资料<small class="label bg-red reset-label margin_l5">未完善</small></a></li>
                    <li><a href="#tab_notice_3" data-toggle="tab">资料审批
                        <c:choose>
                            <c:when test="${item.gjtSignup.auditState=='0'}"><small class="label bg-red reset-label margin_l5">审核不通过</small></c:when>
                            <c:when test="${item.gjtSignup.auditState=='1'}"><small class="label bg-green reset-label margin_l5">审核通过</small></c:when>
                            <c:otherwise><small class="label bg-orange reset-label margin_l5">待审核</small></c:otherwise>
                        </c:choose>
                    </a></li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane active" id="tab_notice_1">
                        <div class="clearfix">
                            <%--<button class="btn btn-default fr margin_l10"><i class="fa fa-fw fa-download"></i> 下载资料</button>
                            <button class="btn btn-default fr"><i class="fa fa-fw fa-print"></i> 打印资料</button>--%>
                        </div>
                        <div class="panel panel-default margin_t10">
                            <div class="panel-heading">
                                <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-1" role="button">
                                    <small class="label bg-yellow reset-label fr">待完善</small>
                                    <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
                                    <span class="margin-r-5">基础信息</span>
                                </h3>
                            </div>
                            <div id="info-box-1" class="collapse in">
                                <form class="theform">
                                    <div class="panel-body content-group">
                                        <div class="cnt-box-body no-padding">
                                            <table class="table-gray-th">
                                                <tr>
                                                    <th>
                                                        姓名
                                                    </th>
                                                    <td>
                                                        ${item.xm}
                                                    </td>
                                                    <th>
                                                        学号
                                                    </th>
                                                    <td>
                                                        ${item.xh}
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        性别
                                                    </th>
                                                    <td>
                                                        <dic:getLabel typeCode="Sex" code="${item.xbm }" />
                                                    </td>
                                                    <th>
                                                        证件类型
                                                    </th>
                                                    <td>
                                                        ${not empty item.certificatetype?item.certificatetype:'身份证'}
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        证件号
                                                    </th>
                                                    <td>
                                                        ${item.sfzh}
                                                    </td>
                                                    <th>
                                                        籍贯
                                                    </th>
                                                    <td required>
                                                        ${item.nativeplace}
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        民族
                                                    </th>
                                                    <td required>
                                                        <dic:getLabel typeCode="NationalityCode" code="${item.mzm}"/>
                                                    </td>
                                                    <th>
                                                        政治面貌
                                                    </th>
                                                    <td required>
                                                        ${item.politicsstatus}
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        婚姻状态
                                                    </th>
                                                    <td required>
                                                        <dic:getLabel typeCode="MaritalStatusCode" code="${item.hyzkm}" />
                                                    </td>
                                                    <th>
                                                        户口性质
                                                    </th>
                                                    <td required>
                                                        <dic:getLabel typeCode="AccountsNatureCode" code="${item.hkxzm}"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        出生日期
                                                    </th>
                                                    <td required>
                                                        ${item.csrq}
                                                    </td>
                                                    <th>
                                                        在职状况
                                                    </th>
                                                    <td required>
                                                        <dic:getLabel typeCode="OccupationStatus" code="${item.isonjob}"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        户籍所在地
                                                    </th>
                                                    <td colspan="3" required>
                                                        ${item.hkszd}
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="panel panel-default margin_t10">
                            <div class="panel-heading">
                                <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-2" role="button">
                                    <small class="label bg-yellow reset-label fr">待完善</small>
                                    <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
                                    <span class="margin-r-5">通讯信息</span>
                                </h3>
                            </div>
                            <div id="info-box-2" class="collapse in">
                                <form class="theform">
                                    <div class="panel-body content-group overlay-wrapper position-relative">
                                        <div class="cnt-box-body no-padding">
                                            <div class="table-responsive margin-bottom-none">
                                                <table class="table-gray-th">
                                                    <tr>
                                                        <th>
                                                            手机号码
                                                        </th>
                                                        <td>
                                                            ${item.sjh}
                                                        </td>
                                                        <th>
                                                            固定电话
                                                        </th>
                                                        <td>
                                                            ${item.lxdh}
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>
                                                            邮箱
                                                        </th>
                                                        <td required>
                                                            ${item.dzxx}
                                                        </td>
                                                        <th>
                                                            通信地址
                                                        </th>
                                                        <td required>
                                                            <dic:getLabel typeCode="Province" code="${item.province}" /><dic:getLabel typeCode="${item.province}_City" code="${item.city}" /><dic:getLabel typeCode="${item.province}_${item.city}_Area" code="${item.area}" />${item.txdz}
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>
                                                            邮编
                                                        </th>
                                                        <td required>
                                                            ${item.yzbm}
                                                        </td>
                                                        <%--<th>
                                                            第二联系人
                                                        </th>
                                                        <td>
                                                            ${item.scName}
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>
                                                            第二联系人电话
                                                        </th>
                                                        <td>
                                                            ${item.scPhone}
                                                        </td>--%>
                                                        <th>
                                                            所在单位
                                                        </th>
                                                        <td required>
                                                            ${item.scCo}
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>
                                                            单位地址
                                                        </th>
                                                        <td required>
                                                            ${item.scCoAddr}
                                                        </td>
                                                        <th>岗位职务</th>
                                                        <td required>
                                                            ${item.gjtSignup.jobPost}
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="panel panel-default margin_t10">
                            <div class="panel-heading">
                                <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-3" role="button">
                                    <small class="label bg-green reset-label fr">已完善</small>
                                    <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
                                    <span class="margin-r-5">学籍信息</span>
                                </h3>
                            </div>
                            <div id="info-box-3" class="collapse in">
                                <form class="theform">
                                    <div class="panel-body content-group">
                                        <div class="cnt-box-body no-padding">
                                            <table class="table-gray-th">
                                                <tr>
                                                    <th>
                                                        年级
                                                    </th>
                                                    <td>
                                                        ${item.gjtGrade.gradeName}
                                                    </td>
                                                    <th>
                                                        专业
                                                    </th>
                                                    <td>
                                                        ${item.gjtSpecialty.zymc}
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        层次
                                                    </th>
                                                    <td>
                                                        <dic:getLabel typeCode="TrainingLevel" code="${item.pycc }" />
                                                    </td>
                                                    <th>
                                                        学习方式
                                                    </th>
                                                    <td>
                                                        2.5年<%--${item.academic}--%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        报读院校
                                                    </th>
                                                    <td>
                                                        ${item.gjtSchoolInfo.xxmc}
                                                    </td>
                                                    <th>
                                                        班级
                                                    </th>
                                                    <td>
                                                        ${item.userclass}
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>
                                                        学习中心
                                                    </th>
                                                    <td>
                                                        ${item.gjtStudyCenter.scName}
                                                    </td>
                                                    <th>学籍状态</th>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${item.xjzt=='2'}">
                                                                <span class="text-green"><dic:getLabel typeCode="StudentNumberStatus" code="${item.xjzt }" /><c:if test="${not empty item.rollRegisterDt}">（${item.rollRegisterDt}）</c:if></span>
                                                            </c:when>
                                                            <c:when test="${item.xjzt=='8'}">
                                                                <span class="text-green"><dic:getLabel typeCode="StudentNumberStatus" code="${item.xjzt }" /></span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="text-red"><dic:getLabel typeCode="StudentNumberStatus" code="${item.xjzt }" /></span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="panel panel-default margin_t10">
                            <div class="panel-heading">
                                <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-4" role="button">
                                    <small class="label bg-yellow reset-label fr">待完善</small>
                                    <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
                                    <span class="margin-r-5">原最高学历</span>
                                </h3>
                            </div>
                            <div id="info-box-4" class="collapse in">
                                <form class="theform">
                                    <div class="panel-body content-group overlay-wrapper position-relative">
                                        <div class="cnt-box-body no-padding">
                                            <div class="table-responsive margin-bottom-none">
                                                <table class="table-gray-th">
                                                    <tr>
                                                        <th>
                                                            原学历层次
                                                        </th>
                                                        <td required>
                                                            ${item.exedulevel}
                                                        </td>
                                                        <th>
                                                            原毕业学校
                                                        </th>
                                                        <td required>
                                                            ${item.exschool}
                                                        </td>
                                                    </tr>
                                                    <%-- 电大续读生，以下选项不需要填写 --%>
                                                    <c:if test="${item.userType!='42' }">
                                                    <c:choose>
                                                        <c:when test="${isUndergraduateCourse }">
                                                            <tr>
                                                                <th>
                                                                    毕业时间
                                                                </th>
                                                                <td required>
                                                                        ${item.exgraduatedtime}
                                                                </td>
                                                                <th>
                                                                    原学科
                                                                </th>
                                                                <td required>
                                                                        ${item.exsubject}
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <th>
                                                                    原学科门类
                                                                </th>
                                                                <td required>
                                                                        ${item.exsubjectkind}
                                                                </td>
                                                                <th>
                                                                    原学历学习类型
                                                                </th>
                                                                <td required>
                                                                        ${item.exedubaktype}
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <th>
                                                                    原学历所学专业
                                                                </th>
                                                                <td required>
                                                                        ${item.exedumajor}
                                                                </td>
                                                                <th>
                                                                    原学历毕业证书编号
                                                                    <i class="text-red fa fa-fw fa-exclamation-circle" data-toggle="tooltip" title="<div class='text-left'>原学历毕业证书编号：可查看原学历证书上的毕业证书编号</div>" data-html="true" data-container="body"></i>
                                                                </th>
                                                                <td required>
                                                                        ${item.excertificatenum}
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <th>
                                                                    原学历证明材料
                                                                </th>
                                                                <td required>
                                                                        ${item.excertificateprove}
                                                                </td>
                                                                <th>
                                                                    原学历证明材料编号
                                                                    <i class="text-red fa fa-fw fa-exclamation-circle" data-toggle="tooltip" title="<div class='text-left'>二种查询方式：<br>1.学信网(www.chsi.com.cn)<br>2.中国高等教育学生信息网(http://www.chsi.com.cn/xlcx/rhsq.jsp)</div>" data-html="true" data-container="body"></i>
                                                                </th>
                                                                <td required>
                                                                        ${item.excertificateprovenum}
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <th>
                                                                    原学历姓名
                                                                </th>
                                                                <td required>
                                                                        ${item.exeduname}
                                                                </td>
                                                                <th>
                                                                    原学历证件类型
                                                                </th>
                                                                <td>
                                                                    身份证
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <th>
                                                                    原学历证件号码
                                                                </th>
                                                                <td required>
                                                                        ${item.exedunum}
                                                                </td>
                                                                <th>
                                                                    是否电大毕业
                                                                </th>
                                                                <td required>
                                                                        ${item.isgraduatebytv}
                                                                </td>
                                                            </tr>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <tr>
                                                                <th>
                                                                    毕业时间
                                                                </th>
                                                                <td required>
                                                                        ${item.exgraduatedtime}
                                                                </td>
                                                                <th>
                                                                    是否电大毕业
                                                                </th>
                                                                <td required>
                                                                        ${item.isgraduatebytv}
                                                                </td>
                                                            </tr>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    </c:if>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="panel panel-default margin_t10">
                            <div class="panel-heading">
                                <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-5" role="button">
                                    <c:choose>
                                        <c:when test="${not empty signupCopyData['sign'] }">
                                            <small class="label bg-green reset-label fr">已完善</small>
                                        </c:when>
                                        <c:otherwise>
                                            <small class="label bg-yellow reset-label fr">待完善</small>
                                        </c:otherwise>
                                    </c:choose>
                                    <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
                                    <span class="margin-r-5">签名</span>
                                </h3>
                            </div>
                            <div id="info-box-5" class="collapse in">
                                <div class="panel-body text-center">
                                    <img src="${signupCopyData['sign'] }" style="max-width: 750px;" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab_notice_2">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title text-bold">
                                    <span class="margin-r-5">电子版免冠一寸蓝底证件照</span>
                                    <c:choose>
                                        <c:when test="${not empty signupCopyData['zp'] }">
                                            <small class="label bg-green reset-label fr">已完善</small>
                                        </c:when>
                                        <c:otherwise>
                                            <small class="label bg-yellow reset-label fr">待完善</small>
                                        </c:otherwise>
                                    </c:choose>
                                </h3>
                            </div>
                            <div class="panel-body">
                                <div class="row margin_t15">
                                    <div class="col-sm-6 margin_b15">
                                        <div class="cert-wrap">
                                            <div style="width:160px;">
                                                <div class="cert-box has-upload cert-box-3">
                                                    <a href="javascript:void(0);" class="info-img-box">
                                                        <img class="info-img" key="zp" src="${signupCopyData['zp'] }" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
                                                        <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                "title":"电子版免冠一寸蓝底证件照",
                                                                "sampleImg":[]
                                                            }'><i></i><span>点击上传</span>
                                                        </div> -->
                                                    </a>
                                                    <a href="${not empty signupCopyData['zp'] ? signupCopyData['zp'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                    <input type="hidden" class="img-val" name="zp" value="${signupCopyData['zp'] }">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title text-bold">
                                    <span class="margin-r-5">身份证原件正反面扫描件</span>
                                    <c:choose>
                                        <c:when test="${not empty signupCopyData['sfz-z'] && not empty signupCopyData['sfz-f'] }">
                                            <small class="label bg-green reset-label fr">已完善</small>
                                        </c:when>
                                        <c:otherwise>
                                            <small class="label bg-yellow reset-label fr">待完善</small>
                                        </c:otherwise>
                                    </c:choose>
                                </h3>
                            </div>
                            <div class="panel-body">
                                <div class="signupType">
                                    <input type="radio"  name="signupSfzType" value="0" class="flat-red" <c:if test="${item.gjtSignup.signupSfzType==0}">checked="checked"</c:if>> 中国居民身份证
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="radio"  name="signupSfzType" value="1" class="flat-red" <c:if test="${item.gjtSignup.signupSfzType==1}">checked="checked"</c:if>> 其他身份证件（中国居民临时身份证、港澳台、外籍人士身份证）
                                </div>
                                <div class="row margin_t15 signupSfzType" style="display: none;">
                                    <div class="col-sm-6 margin_b15">
                                        <div class="cert-wrap">
                                            <h4 class="cert-title text-center f16">身份证正面</h4>
                                            <div class="cert-box has-upload cert-box-4">
                                                <a href="javascript:void(0);" class="info-img-box">
                                                    <img class="info-img" key="sfzz" src="${signupCopyData['sfz-z']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                                                    <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                            "title":"身份证正面",
                                                            "sampleImg":[]
                                                        }'><i></i><span>点击上传</span>
                                                    </div> -->
                                                </a>
                                                <a href="${not empty signupCopyData['sfz-z'] ? signupCopyData['sfz-z'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                <input type="hidden" class="img-val" name="sfzz" value="${signupCopyData['sfz-z'] }" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-6 margin_b15">
                                        <div class="cert-wrap">
                                            <h4 class="cert-title text-center f16">身份证反面</h4>
                                            <div class="cert-box has-upload cert-box-5">
                                                <a href="javascript:void(0);" class="info-img-box">
                                                    <img class="info-img" key="sfzf" src="${signupCopyData['sfz-f']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                                                    <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                            "title":"身份证反面",
                                                            "sampleImg":[]
                                                        }'><i></i><span>点击上传</span>
                                                    </div> -->
                                                </a>
                                                <a href="${not empty signupCopyData['sfz-f'] ? signupCopyData['sfz-f'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                <input type="hidden" class="img-val" name="sfzf" value="${signupCopyData['sfz-f'] }" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <c:choose>
                            <%-- 21 31 测试环境使用 --%>
                            <c:when test="${item.userType=='11'||item.userType=='12'||item.userType=='21'||item.userType=='31' }">
                                <c:if test="${isUndergraduateCourse }">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h3 class="panel-title text-bold">
                                                <span class="margin-r-5">毕业信息</span>
                                                <c:choose>
                                                    <c:when test="${((empty item.gjtSignup.signupByzType||item.gjtSignup.signupByzType==0) && not empty signupCopyData['byz-z'] && not empty signupCopyData['xlz']) || (item.gjtSignup.signupByzType==1 && not empty signupCopyData['dzzch'] && not empty signupCopyData['xsz']) }">
                                                        <small class="label bg-green reset-label fr">已完善</small>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <small class="label bg-yellow reset-label fr">待完善</small>
                                                    </c:otherwise>
                                                </c:choose>
                                            </h3>
                                        </div>
                                        <div class="panel-body">
                                            <div class="signupType">
                                                <input type="radio"  name="signupByzType" value="0" class="flat-red" <c:if test="${item.gjtSignup.signupByzType==0}">checked="checked"</c:if>> 毕业证原件+《中国高等教育学历认证报告》或《教 育部学历证书电子注册备案表》原件
                                                &nbsp;&nbsp;&nbsp;&nbsp;
                                                <input type="radio"  name="signupByzType" value="1" class="flat-red" <c:if test="${item.gjtSignup.signupByzType==1}">checked="checked"</c:if>> 毕业电子注册号证明+学生证原件
                                            </div>
                                            <div class="row margin_t15 signupByzType" style="display: none;">
                                                <div class="col-sm-6 margin_b15">
                                                    <div class="cert-wrap">
                                                        <h4 class="cert-title text-center f16">毕业证原件</h4>
                                                        <div class="cert-box has-upload cert-box-6">
                                                            <a href="javascript:void(0);" class="info-img-box">
                                                                <img class="info-img" key="byzz" src="${signupCopyData['byz-z']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                                                                <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                    "title":"毕业证原件",
                                                                    "sampleImg":[]
                                                                }'><i></i><span>点击上传</span>
                                                                </div> -->
                                                            </a>
                                                            <a href="${not empty signupCopyData['byz-z'] ? signupCopyData['byz-z'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                            <input type="hidden" class="img-val" name="byzz" value="${signupCopyData['byz-z'] }" />
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-6 margin_b15">
                                                    <div class="cert-wrap">
                                                        <div style="width:160px;">
                                                            <h4 class="cert-title text-center f16">《中国高等教育学历认证报告》或《教育部学历证书电子注册备案表》</h4>
                                                            <div class="cert-box has-upload cert-box-8">
                                                                <a href="javascript:void(0);" class="info-img-box">
                                                                    <img class="info-img" key="xlz" src="${signupCopyData['xlz']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
                                                                    <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                        "title":"《中国高等教育学历认证报告》或《教育部学历证书电子注册备案表》",
                                                                        "sampleImg":[]
                                                                    }'><i></i><span>点击上传</span>
                                                                    </div> -->
                                                                </a>
                                                                <a href="${not empty signupCopyData['xlz'] ? signupCopyData['xlz'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                                <input type="hidden" class="img-val" name="xlz" value="${signupCopyData['xlz'] }" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row margin_t15 signupByzType" style="display: none;">
                                                <div class="col-sm-6 margin_b15">
                                                    <div class="cert-wrap">
                                                        <div style="width:160px;">
                                                            <h4 class="cert-title text-center f16">毕业电子注册号证明</h4>
                                                            <div class="cert-box has-upload cert-box-6">
                                                                <a href="javascript:void(0);" class="info-img-box">
                                                                    <img class="info-img" key="dzzch" src="${signupCopyData['dzzch']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
                                                                    <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                        "title":"毕业电子注册号证明",
                                                                        "sampleImg":[]
                                                                    }'><i></i><span>点击上传</span>
                                                                    </div> -->
                                                                </a>
                                                                <a href="${not empty signupCopyData['dzzch'] ? signupCopyData['dzzch'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                                <input type="hidden" class="img-val" name="dzzch" value="${signupCopyData['dzzch'] }" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-6 margin_b15">
                                                    <div class="cert-wrap">
                                                        <h4 class="cert-title text-center f16">国家开放大学或广州电大学生证原件</h4>
                                                        <div class="cert-box has-upload cert-box-6">
                                                            <a href="javascript:void(0);" class="info-img-box">
                                                                <img class="info-img" key="xsz" src="${signupCopyData['xsz']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                                                                <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                    "title":"国家开放大学或广州电大学生证原件",
                                                                    "sampleImg":[]
                                                                }'><i></i><span>点击上传</span>
                                                                </div> -->
                                                            </a>
                                                            <a href="${not empty signupCopyData['xsz'] ? signupCopyData['xsz'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                            <input type="hidden" class="img-val" name="xsz" value="${signupCopyData['xsz'] }" />
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${!isUndergraduateCourse }">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h3 class="panel-title text-bold">
                                                <span class="margin-r-5">最高学历证明</span>
                                                <c:choose>
                                                    <c:when test="${((empty item.gjtSignup.zgxlRadioType||item.gjtSignup.zgxlRadioType==0) && not empty signupCopyData['byz-z']) || (item.gjtSignup.zgxlRadioType==1 && not empty signupCopyData['xlzm'] && not empty signupCopyData['cns']) }">
                                                        <small class="label bg-green reset-label fr">已完善</small>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <small class="label bg-yellow reset-label fr">待完善</small>
                                                    </c:otherwise>
                                                </c:choose>
                                            </h3>
                                        </div>
                                        <div class="panel-body">
                                            <div style="padding-bottom: 10px !important; color: #ff7200;"> * 高中或职高、中专、技校等同等学历的毕业证扫描件（一份）或【学历证明（广州市公司盖章）原件（一份）＋专科承诺书（一份）】</div>
                                            <div class="signupType">
                                                <input type="radio"  name="zgxlRadioType" value="0" class="flat-red" <c:if test="${item.gjtSignup.zgxlRadioType==0}">checked="checked"</c:if>> 高中或职高、中专、技校等同等学历的毕业证扫描件
                                                &nbsp;&nbsp;&nbsp;&nbsp;
                                                <input type="radio"  name="zgxlRadioType" value="1" class="flat-red" <c:if test="${item.gjtSignup.zgxlRadioType==1}">checked="checked"</c:if>> 学历证明（广州市公司盖章）原件＋专科承诺书
                                            </div>
                                            <div class="row margin_t15 zgxlRadioType" style="display: none;">
                                                <div class="col-sm-6 margin_b15">
                                                    <div class="cert-wrap">
                                                        <h4 class="cert-title text-center f16">高中或职高、中专、技校等同等学历的毕业证扫描件</h4>
                                                        <div class="cert-box has-upload cert-box-6">
                                                            <a href="javascript:void(0);" class="info-img-box">
                                                                <img class="info-img" key="byzz" src="${signupCopyData['byz-z']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                                                                <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                    "title":"高中或职高、中专、技校等同等学历的毕业证扫描件",
                                                                    "sampleImg":[]
                                                                }'><i></i><span>点击上传</span>
                                                                </div> -->
                                                            </a>
                                                            <a href="${not empty signupCopyData['byz-z'] ? signupCopyData['byz-z'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                            <input type="hidden" class="img-val" name="byzz" value="${signupCopyData['byz-z'] }" />
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row margin_t15 zgxlRadioType" style="display: none;">
                                                <div class="col-sm-6 margin_b15">
                                                    <div class="cert-wrap">
                                                        <div style="width: 160px;">
                                                            <h4 class="cert-title text-center f16">学历证明（广州市公司盖章）原件</h4>
                                                            <div class="cert-box has-upload cert-box-8">
                                                                <a href="javascript:void(0);" class="info-img-box">
                                                                    <img class="info-img" key="xlzm" src="${signupCopyData['xlzm']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
                                                                    <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                        "title":"学历证明（广州市公司盖章）原件",
                                                                        "sampleImg":[]
                                                                    }'><i></i><span>点击上传</span>
                                                                    </div> -->
                                                                </a>
                                                                <a href="${not empty signupCopyData['xlzm'] ? signupCopyData['xlzm'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                                <input type="hidden" class="img-val" name="xlzm" value="${signupCopyData['xlzm'] }" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-6 margin_b15">
                                                    <div class="cert-wrap">
                                                        <div style="width: 160px;">
                                                            <h4 class="cert-title text-center f16">专科承诺书</h4>
                                                            <div class="cert-box has-upload cert-box-8">
                                                                <a href="javascript:void(0);" class="info-img-box">
                                                                    <img class="info-img" key="cns" src="${signupCopyData['cns']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
                                                                    <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                        "title":"专科承诺书",
                                                                        "sampleImg":[]
                                                                    }'><i></i><span>点击上传</span>
                                                                    </div> -->
                                                                </a>
                                                                <a href="${not empty signupCopyData['cns'] ? signupCopyData['cns'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                                <input type="hidden" class="img-val" name="cns" value="${signupCopyData['cns'] }" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </c:when>
                            <c:when test="${item.userType=='42' }">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title text-bold">
                                            <span class="margin-r-5">国家开放大学或广州电大学生证原件</span>
                                            <c:choose>
                                                <c:when test="${not empty signupCopyData['xsz'] }">
                                                    <small class="label bg-green reset-label fr">已完善</small>
                                                </c:when>
                                                <c:otherwise>
                                                    <small class="label bg-yellow reset-label fr">待完善</small>
                                                </c:otherwise>
                                            </c:choose>
                                        </h3>
                                    </div>
                                    <div class="panel-body">
                                        <div class="row margin_t15">
                                            <div class="col-sm-6 margin_b15">
                                                <div class="cert-wrap">
                                                    <h4 class="cert-title text-center f16">学生证</h4>
                                                    <div class="cert-box has-upload cert-box-6">
                                                        <a href="javascript:void(0);" class="info-img-box">
                                                            <img class="info-img" key="xsz" src="${signupCopyData['xsz']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                                                            <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                "title":"学生证",
                                                                "sampleImg":[]
                                                            }'><i></i><span>点击上传</span>
                                                            </div> -->
                                                        </a>
                                                        <a href="${not empty signupCopyData['xsz'] ? signupCopyData['xsz'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                        <input type="hidden" class="img-val" name="xsz" value="${signupCopyData['xsz'] }" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title text-bold">
                                            <span class="margin-r-5">成绩单</span>
                                            <c:choose>
                                                <c:when test="${not empty signupCopyData['cjd'] }">
                                                    <small class="label bg-green reset-label fr">已完善</small>
                                                </c:when>
                                                <c:otherwise>
                                                    <small class="label bg-yellow reset-label fr">待完善</small>
                                                </c:otherwise>
                                            </c:choose>
                                        </h3>
                                    </div>
                                    <div class="panel-body">
                                        <div class="row margin_t15">
                                            <div class="col-sm-6 margin_b15">
                                                <div class="cert-wrap">
                                                    <h4 class="cert-title text-center f16">成绩单</h4>
                                                    <div class="cert-box has-upload cert-box-6">
                                                        <a href="javascript:void(0);" class="info-img-box">
                                                            <img class="info-img" key="cjd" src="${signupCopyData['cjd']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                                                            <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                "title":"成绩单",
                                                                "sampleImg":[]
                                                            }'><i></i><span>点击上传</span>
                                                            </div> -->
                                                        </a>
                                                        <a href="${not empty signupCopyData['cjd'] ? signupCopyData['cjd'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                        <input type="hidden" class="img-val" name="cjd" value="${signupCopyData['cjd'] }" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                            <c:when test="${item.userType=='51' }">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title text-bold">
                                            <span class="margin-r-5">成绩单</span>
                                            <c:choose>
                                                <c:when test="${not empty signupCopyData['cjd'] }">
                                                    <small class="label bg-green reset-label fr">已完善</small>
                                                </c:when>
                                                <c:otherwise>
                                                    <small class="label bg-yellow reset-label fr">待完善</small>
                                                </c:otherwise>
                                            </c:choose>
                                        </h3>
                                    </div>
                                    <div class="panel-body">
                                        <div class="row margin_t15">
                                            <div class="col-sm-6 margin_b15">
                                                <div class="cert-wrap">
                                                    <h4 class="cert-title text-center f16">成绩单</h4>
                                                    <div class="cert-box has-upload cert-box-6">
                                                        <a href="javascript:void(0);" class="info-img-box">
                                                            <img class="info-img" key="cjd" src="${signupCopyData['cjd']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                                                            <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                "title":"成绩单",
                                                                "sampleImg":[]
                                                            }'><i></i><span>点击上传</span>
                                                            </div> -->
                                                        </a>
                                                        <a href="${not empty signupCopyData['cjd'] ? signupCopyData['cjd'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                        <input type="hidden" class="img-val" name="cjd" value="${signupCopyData['cjd'] }" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title text-bold">
                                            <span class="margin-r-5">录取名册或入学通知书</span>
                                            <c:choose>
                                                <c:when test="${not empty signupCopyData['lqmc'] }">
                                                    <small class="label bg-green reset-label fr">已完善</small>
                                                </c:when>
                                                <c:otherwise>
                                                    <small class="label bg-yellow reset-label fr">待完善</small>
                                                </c:otherwise>
                                            </c:choose>
                                        </h3>
                                    </div>
                                    <div class="panel-body">
                                        <div class="row margin_t15">
                                            <div class="col-sm-6 margin_b15">
                                                <div class="cert-wrap">
                                                    <h4 class="cert-title text-center f16">录取名册或入学通知书</h4>
                                                    <div class="cert-box has-upload cert-box-6">
                                                        <a href="javascript:void(0);" class="info-img-box">
                                                            <img class="info-img" key="lqmc" src="${signupCopyData['lqmc']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                                                            <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                "title":"录取名册或入学通知书",
                                                                "sampleImg":[]
                                                            }'><i></i><span>点击上传</span>
                                                            </div> -->
                                                        </a>
                                                        <a href="${not empty signupCopyData['lqmc'] ? signupCopyData['lqmc'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                        <input type="hidden" class="img-val" name="lqmc" value="${signupCopyData['lqmc'] }" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title text-bold">
                                            <span class="margin-r-5">应届毕业生证明</span>
                                            <c:choose>
                                                <c:when test="${not empty signupCopyData['yjbyszm'] }">
                                                    <small class="label bg-green reset-label fr">已完善</small>
                                                </c:when>
                                                <c:otherwise>
                                                    <small class="label bg-yellow reset-label fr">待完善</small>
                                                </c:otherwise>
                                            </c:choose>
                                        </h3>
                                    </div>
                                    <div class="panel-body">
                                        <div class="row margin_t15">
                                            <div class="col-sm-6 margin_b15">
                                                <div class="cert-wrap">
                                                    <div style="width:160px;">
                                                        <h4 class="cert-title text-center f16">普通高等学校应届毕业生证明</h4>
                                                        <div class="cert-box has-upload cert-box-6">
                                                            <a href="javascript:void(0);" class="info-img-box">
                                                                <img class="info-img" key="yjbyszm" src="${signupCopyData['yjbyszm']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
                                                                <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                    "title":"普通高等学校应届毕业生证明",
                                                                    "sampleImg":[]
                                                                }'><i></i><span>点击上传</span>
                                                                </div> -->
                                                            </a>
                                                            <a href="${not empty signupCopyData['yjbyszm'] ? signupCopyData['yjbyszm'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                            <input type="hidden" class="img-val" name="yjbyszm" value="${signupCopyData['yjbyszm'] }" />
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h3 class="panel-title text-bold">
                                            <span class="margin-r-5">承诺书</span>
                                            <c:choose>
                                                <c:when test="${not empty signupCopyData['ykcns'] }">
                                                    <small class="label bg-green reset-label fr">已完善</small>
                                                </c:when>
                                                <c:otherwise>
                                                    <small class="label bg-yellow reset-label fr">待完善</small>
                                                </c:otherwise>
                                            </c:choose>
                                        </h3>
                                    </div>
                                    <div class="panel-body">
                                        <div class="row margin_t15">
                                            <div class="col-sm-6 margin_b15">
                                                <div class="cert-wrap">
                                                    <div style="width:160px;">
                                                        <h4 class="cert-title text-center f16">国家开放大学（广州）开放教育预科生信息表（承诺书）</h4>
                                                        <div class="cert-box has-upload cert-box-6">
                                                            <a href="javascript:void(0);" class="info-img-box">
                                                                <img class="info-img" key="ykcns" src="${signupCopyData['ykcns']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage150x210.png'">
                                                                <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                                    "title":"国家开放大学（广州）开放教育预科生信息表（承诺书）",
                                                                    "sampleImg":[]
                                                                }'><i></i><span>点击上传</span>
                                                                </div> -->
                                                            </a>
                                                            <a href="${not empty signupCopyData['ykcns'] ? signupCopyData['ykcns'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                            <input type="hidden" class="img-val" name="ykcns" value="${signupCopyData['ykcns'] }" />
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                        </c:choose>
                        <c:if test="${isOffsite==1}">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title text-bold">
                                        <span class="margin-r-5">区域证明</span>
                                        <c:choose>
                                            <c:when test="${((empty item.gjtSignup.signupJzzType||item.gjtSignup.signupJzzType==0) && not empty signupCopyData['jzz'] && not empty signupCopyData['jzzf']) || (item.gjtSignup.signupJzzType==1 && not empty signupCopyData['ygzm']) }">
                                                <small class="label bg-green reset-label fr">已完善</small>
                                            </c:when>
                                            <c:otherwise>
                                                <small class="label bg-yellow reset-label fr">待完善</small>
                                            </c:otherwise>
                                        </c:choose>
                                    </h3>
                                </div>
                                <div class="panel-body">
                                    <div style="padding-bottom: 10px !important; color: #ff7200;"> * 身份证前两位非44开头非广东省学员，请提供区域证明（在读年级证明）</div>
                                    <div style="padding-bottom: 10px !important; color: #ff7200;"> 区域证明：广州市居住证、社保卡、医保卡、劳动合同、租房协议、个人所得税完税证明、户口本、房产证、社保缴纳证明（以上任一扫描件，并注意：证明真实有效，公章所在区域与本人居住区域务必一致。）</div>
                                    <div style="padding-bottom: 10px !important; color: #ff7200;"> 在读年级证明：向在读学校申请开具在读年级证明，盖有学校公章后，扫描原件上传。</div>
                                    <div class="signupType">
                                        <input type="radio"  name="signupJzzType" value="0" class="flat-red" <c:if test="${item.gjtSignup.signupJzzType==0}">checked="checked"</c:if>> 区域证明
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <input type="radio"  name="signupJzzType" value="1" class="flat-red" <c:if test="${item.gjtSignup.signupJzzType==1}">checked="checked"</c:if>> 在读年级证明
                                    </div>
                                    <div class="row margin_t15 signupJzzType" style="display: none;">
                                        <div class="col-sm-6 margin_b15">
                                            <div class="cert-wrap">
                                                <h4 class="cert-title text-center f16">区域证明正面</h4>
                                                <div class="cert-box has-upload cert-box-6">
                                                    <a href="javascript:void(0);" class="info-img-box">
                                                        <img class="info-img" key="jzz" src="${signupCopyData['jzz']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                                                        <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                            "title":"区域证明正面",
                                                            "sampleImg":[]
                                                        }'><i></i><span>点击上传</span>
                                                        </div> -->
                                                    </a>
                                                    <a href="${not empty signupCopyData['jzz'] ? signupCopyData['jzz'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                    <input type="hidden" class="img-val" name="jzz" value="${signupCopyData['jzz'] }" />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-sm-6 margin_b15">
                                            <div class="cert-wrap">
                                                <h4 class="cert-title text-center f16">区域证明反面</h4>
                                                <div class="cert-box has-upload cert-box-6">
                                                    <a href="javascript:void(0);" class="info-img-box">
                                                        <img class="info-img" key="jzzf" src="${signupCopyData['jzzf']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                                                        <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                            "title":"区域证明反面",
                                                            "sampleImg":[]
                                                        }'><i></i><span>点击上传</span>
                                                        </div> -->
                                                    </a>
                                                    <a href="${not empty signupCopyData['jzzf'] ? signupCopyData['jzzf'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                    <input type="hidden" class="img-val" name="jzzf" value="${signupCopyData['jzzf'] }" />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row margin_t15 signupJzzType" style="display: none;">
                                        <div class="col-sm-6 margin_b15">
                                            <div class="cert-wrap">
                                                <h4 class="cert-title text-center f16">在读年级证明</h4>
                                                <div class="cert-box has-upload cert-box-6">
                                                    <a href="javascript:void(0);" class="info-img-box">
                                                        <img class="info-img" key="ygzm" src="${signupCopyData['ygzm']}" alt="No image" onerror="this.src='${ctx }/static/images/noimage.png'">
                                                        <!-- <div class="upload-btn" data-role="upload-img" data-object='{
                                                            "title":"在读年级证明",
                                                            "sampleImg":[]
                                                        }'><i></i><span>点击上传</span>
                                                        </div> -->
                                                    </a>
                                                    <a href="${not empty signupCopyData['ygzm'] ? signupCopyData['ygzm'] : '#" style="display: none' }" class="light-box" data-role="img-fancybox">点击放大</a>
                                                    <input type="hidden" class="img-val" name="ygzm" value="${signupCopyData['ygzm'] }" />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    <div class="tab-pane" id="tab_notice_3">

                        <div class="approval-list clearfix">
                            <c:forEach var="record" items="${flowRecordList}" varStatus="s">
                                <c:if test="${record.auditOperatorRole==1}">
                                    <dl class="approval-item">
                                        <dt class="clearfix">
                                            <b class="a-tit gray6">${s.index==0?'学员提交资料':'学员重交资料'}</b>
                                            <span class="gray9 text-no-bold f12"><fmt:formatDate value="${record.auditDt}" type="both"/></span>
                                            <span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
                                        </dt>
                                    </dl>
                                </c:if>
                                <c:if test="${record.auditOperatorRole!=1}">
                                    <dl class="approval-item" <c:if test="${s.index==(fn:length(flowRecordList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
                                        <dt class="clearfix">
                                            <b class="a-tit gray6">${record.auditOperatorRole==2?'班主任初审':record.auditOperatorRole==3?'招生办复审':record.auditOperatorRole==4?'学籍科终审':''}</b>
                                            <c:if test="${record.auditState==0}">
                                                <span class="fa fa-fw fa-dot-circle-o text-orange"></span>
                                                <label class="state-lb text-orange">待审核</label>
                                            </c:if>
                                            <c:if test="${record.auditState==1}">
                                                <span class="gray9 text-no-bold f12"><fmt:formatDate value="${record.auditDt}" type="both"/></span>
                                                <span class="fa fa-fw fa-check-circle text-green"></span>
                                                <label class="state-lb text-green">审核通过</label>
                                            </c:if>
                                            <c:if test="${record.auditState==2}">
                                                <span class="gray9 text-no-bold f12"><fmt:formatDate value="${record.auditDt}" type="both"/></span>
                                                <span class="fa fa-fw fa-times-circle text-red"></span>
                                                <label class="state-lb text-red">审核不通过</label>
                                            </c:if>
                                        </dt>
                                        <c:if test="${record.auditState==0}">
                                            <c:if test="${record.auditOperatorRole==4}">
                                                <!--<dd>
                                                    <div class="col-xs-12 no-padding position-relative">
                                                        <textarea name="auditContent" class="form-control" rows="3" placeholder="请输入资料审核备注，例如该学员的资料确认无误" datatype="*1-200" nullmsg="请输入内容！" errormsg="字数不能超过200"></textarea>
                                                    </div>
                                                    <div>
                                                        <button type="button" class="btn min-width-90px btn-warning margin_r10 margin_t10 btn-audit" val="2" data-form-id="5">审核不通过</button>
                                                        <button type="button" class="btn min-width-90px btn-success margin_r10 margin_t10 btn-audit" val="1" data-form-id="5">审核通过</button>
                                                    </div>
                                                </dd>-->
                                            </c:if>
                                        </c:if>
                                        <c:if test="${record.auditState!=0}">
                                            <dd>
                                                <div class="txt">
                                                    <p>${record.auditContent}</p>
                                                    <div class="gray9 text-right">审核人：${record.auditOperator}</div>
                                                    <i class="arrow-top"></i>
                                                </div>
                                            </dd>
                                        </c:if>
                                    </dl>
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            </form>
        </div>
    </section>

    <%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

    <%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
    <script type="text/javascript" src="${ctx}/static/js/edu/studentInfo/edu_student_info_form.js"></script>
    <script type="text/javascript">
        (function($) {
            //Flat red color scheme for iCheck
            $('input[type="radio"].flat-red').iCheck({
                checkboxClass: 'icheckbox_flat-green',
                radioClass: 'iradio_flat-green'
            }).on("ifChecked",function(e){
                $(e.target).attr('checked',true);
                loadSignupType(e.target);
            }).on("ifUnchecked",function(e){
                $(e.target).attr('checked',false);
            });
            $(".iCheck-helper").off("click"); // 禁止点击

            $('input[type="radio"].flat-red:checked').each(function(i, element) {
                loadSignupType(element);
            });

            function loadSignupType(ele) {
                var name = $(ele).attr("name");
                var value = $(ele).val();
                var index = 0;
                $(':input[name="'+name+'"]').each(function(i, element) {
                    if(value == element.value) {
                        index = i;
                    }
                });
                $('.' + name).hide();
                if($('.' + name).length == 1) {
                    $('.' + name).show();
                } else {
                    $('.' + name + ':eq('+index+')').show();
                }
            }

            var action = $('form#inputForm :input[name="action"]').val();
            if(action == 'audit') {
                $('ul.nav-tabs li:last a').trigger('click');
            }
        })(jQuery);
    </script>
</body>
</html>
