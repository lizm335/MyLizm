<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 缴费管理</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
    <h1>
        缴费管理
    </h1>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li class="active">缴费管理</li>
    </ol>
</section>
<section class="content">
    <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
    <div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

    <form class="form-horizontal">
        <div class="box">
            <div class="box-body">
                <div class="row reset-form-horizontal pad-t15">
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3">学员账号</label>
                            <div class="col-sm-9">
                                <input class="form-control" type="text" name="search_loginAccount" value="${param['search_loginAccount']}">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3">学员姓名</label>
                            <div class="col-sm-9">
                                <input class="form-control" type="text" name="search_xm" value="${param['search_xm']}">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="box-footer">
                <div class="pull-right"><button type="reset" class="btn btn-default">重置</button></div>
                <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
            </div>
        </div>
    </form>

    <div class="box margin-bottom-none">
        <div class="box-header with-border">
            <div class="fr">
                <a href="exportInfo?sortType=${sortType}&${searchParams}" target="_blank" class="btn btn-success btn-outport"><i class="fa fa-fw fa-sign-out"></i> 导出缴费信息</a>
            </div>
        </div>
        <div class="box-body">
            <table id="dtable" class="table table-bordered table-striped table-container">
                <thead>
                <tr>
                    <th><input type="checkbox" class="select-all"></th>
                    <th>学员姓名</th>
                    <th>缴费类型</th>
                    <th>优惠政策</th>
                    <th>缴费金额</th>
                    <th>当前应缴金额</th>
                    <th>最迟缴费时间</th>
                    <th>当前缴费状态</th>
                    <th>学习状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty infos && infos.numberOfElements > 0}">
                        <c:forEach items="${infos.content}" var="info">
                            <c:if test="${not empty info}">
                                <tr>
                                    <td><input type="checkbox" name="ids"
                                               data-id="${info.studentId}" data-name="check-id"
                                               value="${info.studentId}"></td>
                                    <td>${info.xm}</td>
                                    <td>
                                        <c:if test="${info.paymentDetail.GKXL_PAYMENT_TPYE=='A'}">全额缴费</c:if>
                                        <c:if test="${info.paymentDetail.GKXL_PAYMENT_TPYE=='B'}">首年缴费</c:if>
                                        <c:if test="${info.paymentDetail.GKXL_PAYMENT_TPYE=='C'}">分期付款</c:if>
                                    </td>
                                    <td></td>
                                    <td>
                                        总金额：<fmt:formatNumber value="${info.paymentDetail.ORDER_TOTAL_AMT}" type="currency" minFractionDigits="2" />
                                        <br/>优惠/补贴：<fmt:formatNumber value="0" type="currency" minFractionDigits="2" />
                                        <br/>应收金额：<fmt:formatNumber value="${info.paymentDetail.ORDER_TOTAL_AMT}" type="currency" minFractionDigits="2" />
                                        <br/>实收金额：<fmt:formatNumber value="${info.paymentDetail.ORDER_AMT}" type="currency" minFractionDigits="2" />
                                    </td>
                                    <td><fmt:formatNumber value="${info.currentRecAmt}" type="currency" minFractionDigits="2" /></td>
                                    <td><fmt:formatDate value="${info.currentRecDate}" type="date" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${info.currentStatus==1}">
                                                <span class="text-green">已缴费</span>
                                            </c:when>
                                            <c:when test="${info.currentStatus==2}">
                                                <span class="">未开始</span>
                                            </c:when>
                                            <c:when test="${info.currentStatus==3}">
                                                <span class="text-red">欠费</span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${empty info.learningState || info.learningState=='1'}">正常学习</c:if>
                                        <c:if test="${info.learningState=='0'}">停止学习</c:if>
                                    </td>
                                    <td>
                                        <div class="data-operion">
                                            <a href="view/${info.studentId}?currentStatus=${info.currentStatus}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                                            <c:choose>
                                                <c:when test="${empty info.learningState || info.learningState=='1'}">
                                                    <a href="changeLearningState?studentId=${info.studentId}&learningState=0" xm="${info.xm}" class="operion-item stopstudy" data-toggle="tooltip" title="停止学习"><i class="fa fa-fw fa-stop"></i></a>
                                                </c:when>
                                                <c:when test="${not empty info.overdueCount || info.overdueCount<3}">
                                                    <a href="javascript:void(0);" xm="${info.xm}" class="operion-item resume-learning" data-toggle="tooltip" title="恢复学习" disabled><i class="fa fa-fw fa-reply"></i></a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="changeLearningState?studentId=${info.studentId}&learningState=1" xm="${info.xm}" class="operion-item resume-learning" data-toggle="tooltip" title="恢复学习"><i class="fa fa-fw fa-reply"></i></a>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
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
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="application/javascript">
    // 停止学习
    $('#dtable').on('click',".stopstudy",function() {
        if(!$(this).is("[disabled]")){
            var $this = $(this);
            var xm = $(this).attr('xm');
            $.confirm({
                title: '',
                content: '<div class="f20 row" style="margin-bottom:-15px;"><span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span>确认停止' + xm + '学员学习？</div>',
                confirm: function(){
                    //$.alert('Confirmed!');
                    window.location.href = $this.attr('href');
                },
                confirmButton:'确认',
                confirmButtonClass:'btn-primary',
                cancel: function(){
                    //$.alert('Canceled!')
                },
                cancelButton:'取消'
            });
        }
        return false;
    });

    // 恢复学习
    $('#dtable').on('click',".resume-learning",function() {
        if(!$(this).is("[disabled]")){
            var $this = $(this);
            var xm = $(this).attr('xm');
            $.confirm({
                title: '',
                content: '<div class="f20 row" style="margin-bottom:-15px;"><span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span>确认恢复' + xm + '学员学习？</div>',
                confirm: function(){
                    //$.alert('Confirmed!');
                    window.location.href = $this.attr('href');
                },
                confirmButton:'确认',
                confirmButtonClass:'btn-primary',
                cancel: function(){
                    //$.alert('Canceled!')
                },
                cancelButton:'取消'
            });
        }
        return false;
    });
</script>
</body>
</html>
