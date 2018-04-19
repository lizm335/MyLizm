<%--
  Created by IntelliJ IDEA.
  User: Min
  Date: 2017/5/26
  Time: 14:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>课程班考情--课程班考勤详情页</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
    <button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="#">学习管理</a></li>
        <li><a href="#">考勤分析</a></li>
        <li class="active">课程班考勤明细</li>
    </ol>
</section>
<section class="content">
    <form id="listForm" class="form-horizontal">
        <div class="box">
            <div class="box-body">
                <div class="row pad-t15">
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">开课学期</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="gradeId" id="gradeId" data-size="5" data-live-search="true">
                                    <option value="all" selected="">全部学期</option>
                                    <c:forEach items="${gradeMap}" var="map">
                                        <c:if test="${empty grade_id}">
                                            <option value="${map.key}" <c:if test="${currentGradeId eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                        </c:if>
                                        <c:if test="${not empty grade_id}">
                                            <option value="${map.key}" <c:if test="${grade_id eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">姓名</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="XM" value="${param.XM }">
                                <input type="hidden" name="MAIN_DEVICE" value="${param.MAIN_DEVICE }" id="MAIN_DEVICE">
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">学号</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="XH" value="${param.XH }">
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">专业层次</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="PYCC" id="pycc" data-size="5" data-live-search="true">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${pyccMap}" var="map">
                                        <option value="${map.key}" <c:if test="${param.PYCC eq map.key}">selected='selected'</c:if>>
                                                ${map.value}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">报读专业</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="SPECIALTY_ID" id="SPECIALTY_ID" data-size="5" data-live-search="true">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${specialtyMap}" var="map">
                                        <option value="${map.key}" <c:if test="${param.SPECIALTY_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">入学学期</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="startGrade" id="startGrade" data-size="5" data-live-search="true">
                                    <option value="">全部学期</option>
                                    <c:forEach items="${gradeMap}" var="map">
                                        <option value="${map.key}" <c:if test="${param.startGrade eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">课程班</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="classId" id="class_id" data-size="5" data-live-search="true">
                                    <option value="">请选择</option>
                                    <c:forEach items="${courseClass}" var="map">
                                        <option value="${map.key}" <c:if test="${param.classId eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">学习状态</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="STUDY_STATUS" id="STUDY_STATUS" data-size="5" data-live-search="true">
                                    <option value="">全部状态</option>
                                    <option value="0" <c:if test="${param.STUDY_STATUS eq '0' }">selected="selected"</c:if>>在线</option>
                                    <option value="1" <c:if test="${param.STUDY_STATUS eq '1' }">selected="selected"</c:if>>7天以上未学习</option>
                                    <option value="2" <c:if test="${param.STUDY_STATUS eq '2' }">selected="selected"</c:if>>3天以上未学习</option>
                                    <option value="3" <c:if test="${param.STUDY_STATUS eq '3' }">selected="selected"</c:if>>3天内未学习</option>
                                    <option value="4" <c:if test="${param.STUDY_STATUS eq '4' }">selected="selected"</c:if>>从未学习</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- /.box-body -->
            <div class="box-footer text-right">
                <button type="submit" class="btn min-width-90px btn-primary margin_r15" id="sumit_buttion">搜索</button>
                <button type="button" class="btn min-width-90px btn-default">重置</button>
            </div><!-- /.box-footer-->
        </div>
        <div class="box margin-bottom-none">
            <div class="box-header with-border">
                <div class="filter-tabs clearfix pull-left no-margin">
                    <ul class="list-unstyled">
                        <li <c:if test="${empty param.MAIN_DEVICE}">class="actived"</c:if> id="main_device_all">全部(${ALL_MAIN_DEVICE})</li>
                        <li <c:if test="${param.MAIN_DEVICE eq 'APP'}">class="actived"</c:if> id="main_device_app">APP(${APP_MAIN_DEVICE})</li>
                        <li <c:if test="${param.MAIN_DEVICE eq 'PC'}">class="actived"</c:if> id="main_device_pc">PC(${PC_MAIN_DEVICE})</li>
                    </ul>
                </div>
                <div class="pull-right no-margin">
                    <a href="${ctx}/studymanage/courseClockingDetailExport/${pageInfo.getTotalElements()}" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出课程考勤明细表</a>
                </div>
            </div>
            <div class="box-body">
                <div class="table-responsive">
                    <table class="table table-bordered table-striped vertical-mid text-center table-font">
                        <thead>
                        <tr>
                            <th style="width: 100px">头像</th>
                            <th style="width: 15%">个人信息</th>
                            <th style="width: 16%">报读信息</th>
                            <th style="width: 20%">开课信息</th>
                            <th>学习次数</th>
                            <th>学习时长<br>（小时）</th>
                            <th>主要应用终端</th>
                            <th>最近学习日期</th>
                            <th>当前学习状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty pageInfo.content}">
                                <c:forEach items="${pageInfo.content}" var="item">
                                    <tr>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty item.ZP}">
                                                    <img src="${item.ZP}" class="img-circle" width="50" height="50">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${ctx }/static/images/headImg04.png" class="img-circle" width="50" height="50">
                                                </c:otherwise>
                                            </c:choose>
                                            <a href="#" class="btn btn-xs btn-default bg-white no-shadow btn-block margin_t5">
                                                <i class="fa fa-ee-online f24 vertical-middle text-green position-relative" style="top: -2px;"></i>
                                                <span class="gray9">交流</span>
                                            </a>
                                        </td>
                                        <td>
                                            <div class="text-left">
                                                姓名：${item.XM}<br>
                                                学号：${item.XH}<br>
                                                <shiro:hasPermission name="/personal/index$privacyJurisdiction">
                                                手机：${item.SJH}
                                                </shiro:hasPermission>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="text-left">
                                                专业层次：${item.PYCC_NAME}<br>
                                                入学年级：${item.RUXUE_YEAR}<br>
                                                入学学期：${item.RUXUE_TERM}<br>
                                                报读专业：${item.ZYMC}
                                            </div>
                                        </td>
                                        <td>
                                            <div class="text-left">
                                                开课学期：${item.GRADE_NAME}<br>
                                                课程名称：${item.KCMC}<br>
                                                课程代码：${item.KCH}<br>
                                                课程班级：${item.BJMC}<br>
                                                开课状态：<c:choose>
                                                <c:when test="${item.TIME_FLG eq '1'.charAt(0) }">
                                                    <span class="text-green">开课中</span>
                                                </c:when>
                                                <c:when test="${item.TIME_FLG eq '2'.charAt(0) }">
                                                    <span class="text-orange">待开课</span>
                                                </c:when>
                                                <c:when test="${item.TIME_FLG eq '3'.charAt(0) }">
                                                    <span class="gray9">已结束</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="gray9">已结束</span>
                                                </c:otherwise>
                                            </c:choose>
                                            </div>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${empty item.LOGIN_COUNT}">--</c:when>
                                                <c:otherwise>${item.LOGIN_COUNT}</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${empty item.LOGIN_TIME}">--</c:when>
                                                <c:otherwise>${item.LOGIN_TIME}</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item.LOGIN_COUNT ne 0}">
                                                    <c:if test="${item.PC_ONLINE_PERCENT gt item.APP_ONLINE_PERCENT}">PC<div class="gray9">（${item.PC_ONLINE_PERCENT}%）</div></c:if>
                                                    <c:if test="${item.PC_ONLINE_PERCENT lt item.APP_ONLINE_PERCENT}">APP<div class="gray9">（${item.APP_ONLINE_PERCENT}%）</div></c:if>
                                                </c:when>
                                                <c:otherwise>--</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${item.LAST_DATE}<%--<div class="gray9">（PC登录）</div>--%></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item.LOGIN_COUNT eq '0' or empty item.LOGIN_COUNT}">
                                                    <div class="gray9">离线<br>(从未学习)</div>
                                                </c:when>
                                                <c:when test="${item.IS_ONLINE eq 'N'}"><div class="gray9">离线<br>
                                                    <c:if test="${not empty item.LEFT_DAY and item.LEFT_DAY>0}">(${item.LEFT_DAY}天未学习)</c:if></div>
                                                </c:when>
                                                <c:when test="${item.IS_ONLINE eq 'Y'}"><div class="text-green">在线<c:if test="${not empty item.DEVICE}"><br><span class="text-green">(${item.DEVICE}在线)</span></c:if></div></c:when>
                                                <c:when test="${item.IS_ONLINE ne 'N' and item.IS_ONLINE ne '1' and item.LOGIN_COUNT eq '0'}"><div class="gray9">离线<br>(从未学习)</div></c:when>
                                                <c:otherwise><div class="gray9">离线(从未学习)</div></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><a href="${ctx}/studymanage/courseCondition/${item.TERMCOURSE_ID}/${item.STUDENT_ID}/${item.COURSE_ID}" class="operion-item" data-toggle="tooltip" title="查看课程学情明细"><i class="fa fa-fw fa-view-more"></i></a></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise></c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                    <tags:pagination page="${pageInfo}" paginationSize="5" />
                </div>
            </div>
        </div>
    </form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
    // filter tabs
    $(".filter-tabs li").click(function(event) {
        if($(this).hasClass('actived')){
            $(this).removeClass('actived');
        }
        else{
            $(this).addClass('actived');
        }
    });
    // 导出
    $('[data-role="export"]').click(function(event) {
        event.preventDefault();
        var self=this;
        $.mydialog({
            id:'export',
            width:600,
            height:415,
            zIndex:11000,
            content: 'url:'+$(this).attr('href')
        });
    });

    /**
     * 切换app，pc选项卡的结果
     */
    $("#main_device_all,#main_device_app,#main_device_pc").click(function () {
        var id = $(this).attr("id");
        switch (id){
            case "main_device_all":
                $("#MAIN_DEVICE").val("");
                break;
            case "main_device_app":
                $("#MAIN_DEVICE").val("APP");
                break;
            case "main_device_pc":
                $("#MAIN_DEVICE").val("PC");
                break;
        }
        $("#sumit_buttion").click();
    });

</script>
</body>
</html>

