<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 学员考勤</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
    <button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
    <div class="pull-left">
        您所在位置：
    </div>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页 </a></li>
        <li ><a href="${ctx}/home/class/clock/list">学员考勤</a></li>
        <li class="active">学员考勤明细</li>
    </ol>
</section>
<section class="content">
    <%--
        <div class="box">
            <div class="box-body">
                <div class="media pad">
                    <div class="media-left" style="padding-right:25px;">
                        <c:choose>
                            <c:when test="${not empty info.avatar}">
                                <img id ="headImgId" src="${info.avatar}" class="img-circle" alt="User Image" style="width:112px;height:112px;" onerror="this.src='${ctx }/static/images/headImg04.png'">
                            </c:when>
                            <c:otherwise>
                                <img id ="headImgId" src="${ctx }/static/images/headImg04.png" class="img-circle" alt="User Image" style="width:112px;height:112px;">
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="media-body">
                        <h3 class="margin_t10">${info.xm}<small class="f14">(<c:if test="${info.xbm eq '1'}">男</c:if><c:if test="${info.xbm eq '2'}">女</c:if>)</small></h3>
                        <div class="row">
                            <div class="col-xs-6 col-sm-4 pad-b5">
                                <b>学号:</b> <span>${info.xh}</span>
                            </div>
                            <div class="col-xs-6 col-sm-4 pad-b5">
                                <b>手机:</b>
                                <span>${info.sjh}</span>
                            </div>
                            <div class="col-xs-6 col-sm-4 pad-b5">
                                <b>邮箱:</b> <span>${info.dzxx}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="box-footer">
                <div class="row stu-info-status">
                    <div class="col-sm-2_5 col-xs-4">
                        <div class="f24 text-center">${clockDto.countLogin}次</div>
                        <div class="text-center gray6">总登录次数</div>
                    </div>
                    <div class="col-sm-2_5 col-xs-4">
                        <div class="f24 text-center">
                            <c:choose>
                                <c:when test="${clockDto.totalMinute/60 >= 1}">
                                    <fmt:formatNumber value="${clockDto.totalMinute/60-0.49}" maxFractionDigits="0" />
                                </c:when>
                                <c:otherwise>
                                    0
                                </c:otherwise>
                            </c:choose>小时
                        </div>
                        <div class="text-center gray6">总登录时长</div>
                    </div>
                    <div class="col-sm-2_5 col-xs-4">
                        <c:if test="${clockDto.noLoginDays>=7}"><div class="f24 text-center text-red">${clockDto.noLoginDays}天</div></c:if>
                        <c:if test="${clockDto.noLoginDays<7}"><div class="f24 text-center">${clockDto.noLoginDays}天</div></c:if>
                        <div class="text-center gray6">未登录天数</div>
                    </div>
                    <div class="col-sm-2_5 col-xs-4">
                        <div class="f16 text-center line-height-1em"><fmt:formatDate value="${clockDto.firstLogin}" type="both" /></div>
                        <div class="text-center gray6">首次登录</div>
                    </div>
                    <div class="col-sm-2_5 col-xs-4">
                        <div class="f16 text-center line-height-1em"><fmt:formatDate value="${clockDto.lastLogin}" type="both" /></div>
                        <div class="text-center gray6">最后一次登录</div>
                    </div>
                </div>
            </div>
        </div>
    --%>
    <div class="nav-tabs-custom no-margin">
        <ul class="nav nav-tabs nav-tabs-lg">
            <li class="active"><a href="#tab_top_1" data-toggle="tab" onclick="changType('1')">登录记录明细</a></li>
            <li><a href="#tab_top_2"  data-toggle="tab" onclick="changType('2')">学习记录明细</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_top_1">
                <%--
                <div class="row">
                    <form class="form-horizontal">
                        <div class="col-sm-7">
                            <div class="input-group input-daterange full-width">
                                <span class="input-group-addon no-border text-left2">
                                  <b class="pad-r10">登录时间</b>
                                </span>
                                    <input type="text" name="search_GTE_createdDt" value="${param.search_GTE_createdDt}" class="form-control pull-right reservation">
                                    <span class="input-group-addon no-border">至</span>
                                    <input type="text" name="search_LTE_createdDt" value="${param.search_LTE_createdDt}" class="form-control pull-right reservation">
                                <span class="input-group-btn pad-l20">
                                  <button class="btn btn-primary f14 border-radius-3">搜索</button>
                                </span>
                            </div>
                        </div>
                    </form>
                    <div class="col-sm-5 text-right">
                        <a href="javascript:;" onclick="exportInfoDetails('${studentId}')" class="btn btn-default margin_r10">
                            <i class="fa fa-fw fa-sign-out"></i> 导出详情
                        </a>
                    </div>
                </div>
                --%>
                <div class="box box-border margin-bottom-none">
                    <div class="box-header with-border">
                        <div class="filter-tabs clearfix pull-left no-margin">
                            <ul class="list-unstyled">
                                <li <c:if test="${type eq '2' or type eq 'all'}">class="actived"</c:if> id="search_actived_0" data-num="2">APP(${appCount})</li>
                                <li <c:if test="${type eq '1'}">class="actived"</c:if> id="search_actived_1" data-num="1">PC(${pcCount})</li>
                            </ul>
                        </div>
                    </div>
                    <div class="box-body">
                        <div class="table-responsive margin_t10">
                            <table class="table table-bordered table-striped vertical-mid text-center table-font">
                                <thead>
                                <tr>
                                    <th width="13%">个人信息</th>
                                    <th width="21%">报读信息</th>
                                    <th width="22%">教务班</th>
                                    <th>登录时间</th>
                                    <th>登录时长（小时）</th>
                                    <th>应用终端</th>
                                    <th>IP地址</th>
                                    <%--
                                        <th>序号</th>
                                        <th>IP地址</th>
                                        <th>所在区域</th>
                                        <th>学习终端</th>
                                        <th>浏览器</th>
                                        <th>登入平台时间</th>
                                        <th>登出平台时间</th>
                                        <th>登录时长</th>
                                    --%>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${not empty infos && infos.numberOfElements > 0}">
                                        <c:forEach items="${infos.content}" var="item" varStatus="s">
                                            <tr>
                                                <td>
                                                    <div class="text-left">
                                                        姓名：${clockDto.xm}<br>
                                                        学号：${clockDto.xh}<br>
                                                        手机：${clockDto.sjh}
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="text-left">
                                                        层次：${clockDto.pyccName}<br>
                                                        年级：${clockDto.yearName}<br>
                                                        学期：${clockDto.gradeName}<br>
                                                        专业：${clockDto.zymc}
                                                    </div>
                                                </td>
                                                <td>${clockDto.bjmc}</td>
                                                <td><fmt:formatDate value="${item.createdDt}" type="both" /></td>
                                                <td>
                                                    <c:if test="${item.loginTime/60 >= 1}">
                                                        <fmt:formatNumber value="${item.loginTime/60-0.49}" maxFractionDigits="0" />小时
                                                    </c:if>
                                                    <fmt:formatNumber value="${item.loginTime%60}" maxFractionDigits="0" />分钟
                                                </td>
                                                <td>
                                                    <c:if test="${item.loginType eq '1'}">PC</c:if>
                                                    <c:if test="${not empty item.loginType and item.loginType ne '1'}">PC</c:if>
                                                </td>
                                                <td>${item.loginIp}</td>
                                                <%--<td>${item.os}</td>
                                                <td>${item.browser}</td>--%>
                                                <%--<td><fmt:formatDate value="${item.updatedDt}" type="both" /></td>--%>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td align="center" colspan="15">暂无数据</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                            <tags:pagination page="${infos}" paginationSize="10" />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
    function exportInfoDetails(student_id) {
        var url = '${ctx}/home/class/clock/exportInfoDetails';
        var form = $("<form>");//定义form表单
        form.attr("style","display:none");
        form.attr("target","");
        form.attr("method","post");
        form.attr("action",url);
        $("body").append(form);
        form.append("<input type='text' name='studentId' value='"+student_id+"' /> ");
        form.submit();//表单提交
    }
    // filter tabs
    $(".filter-tabs li").click(function(event) {
        if($(this).hasClass('actived')){
            $(this).removeClass('actived');
        }
        else{
            $(this).addClass('actived');
        }
    });


    //切换到学习记录明细
    function changType(type) {
        if (type=="1") {
            window.location.href = "${ctx}/home/class/clock/view/${studentId}?type=all";
        } else {
            window.location.href = "${ctx}/home/class/clock/studyView/${studentId}?type=all";
        }
    }

    /***切换app，pc*/
    $("#search_actived_0,#search_actived_1").click(function () {
        var data = Number($(this).attr("data-num"));
        // 1:PC  2:APP
        window.location.href = "${studentId}?type="+data;

    });
</script>
</body>
</html>
