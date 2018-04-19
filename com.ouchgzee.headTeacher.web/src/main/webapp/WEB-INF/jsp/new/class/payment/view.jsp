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
        学员缴费详情
    </h1>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="${ctx}/home/class/payment/list">缴费管理</a></li>
        <li class="active">学员缴费详情</li>
    </ol>
</section>
<section class="content">
    <div class="box">
        <div class="box-body">
            <div class="media pad">
                <div class="media-left" style="padding-right:25px;">
                    <c:choose>
                        <c:when test="${not empty info.avatar}">
                            <img id ="headImgId" src="${info.avatar}" class="img-circle" alt="User Image" style="width: 128px; height: 128px;" onerror="this.src='${ctx }/static/images/headImg04.png'">
                        </c:when>
                        <c:otherwise>
                            <img id ="headImgId" src="${ctx }/static/images/headImg04.png" class="img-circle" alt="User Image" style="width: 128px; height: 128px;">
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="media-body">
                    <h3 class="margin_t10">${info.xm}</h3>
                    <div class="row">
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>手机:</b> <span>${info.sjh}</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>缴费类型:</b>
                            <c:if test="${stuPaymentInfo.paymentDetail.GKXL_PAYMENT_TPYE=='A'}"><span>全额缴费</span></c:if>
                            <c:if test="${stuPaymentInfo.paymentDetail.GKXL_PAYMENT_TPYE=='B'}"><span>首年缴费</span></c:if>
                            <c:if test="${stuPaymentInfo.paymentDetail.GKXL_PAYMENT_TPYE=='C'}"><span>分期付款</span></c:if>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>实收金额:</b> <span><fmt:formatNumber value="${stuPaymentInfo.paymentDetail.ORDER_AMT}" type="currency" />元</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>邮箱:</b> <span>${info.dzxx}</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>优惠补贴:</b>
                            <span>无</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>当前应收金额:</b> <span><fmt:formatNumber value="${stuPaymentInfo.currentRecAmt}" type="currency" />元</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>报读产品:</b>
                            <span>${info.gjtSpecialty.zymc}（<dic:getLabel typeCode="TrainingLevel" code="${info.pycc}" />）</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>当前状态:</b>
                            <c:choose>
                                <c:when test="${currentStatus==1}">
                                    <span class="text-green">正常</span>
                                </c:when>
                                <c:when test="${currentStatus==2}">
                                    <span class="">未开始</span>
                                </c:when>
                                <c:when test="${currentStatus==3}">
                                    <span class="text-red">欠费</span>
                                </c:when>
                            </c:choose>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>总金额:</b>
                            <span><fmt:formatNumber value="${stuPaymentInfo.paymentDetail.ORDER_TOTAL_AMT}" type="currency" />元</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="box margin-bottom-none">
        <div class="box-body">
            <table id="dtable" class="table table-bordered table-striped table-container">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>期数</th>
                    <th>缴费金额/应缴金额</th>
                    <th>缴费时间/最迟缴费时间</th>
                    <th>状态</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty stuPaymentInfo.paymentDetail && fn:length(stuPaymentInfo.paymentDetail.ORDER_PAY_RECORD_LIST) > 0}">
                        <c:forEach var="item" items="${stuPaymentInfo.paymentDetail.ORDER_PAY_RECORD_LIST}" varStatus="s">
                            <tr>
                                <td>${s.index+1}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.PAY_RECORD_TYPE_CODE=='A'}">全额</c:when>
                                        <c:when test="${item.PAY_RECORD_TYPE_CODE=='B'}">第一年学费</c:when>
                                        <c:when test="${item.PAY_RECORD_TYPE_CODE=='C'}">第二年学费</c:when>
                                        <c:when test="${item.PAY_RECORD_TYPE_CODE=='D'}">第三年学费</c:when>
                                        <c:when test="${item.PAY_RECORD_TYPE_CODE=='E'}">首付</c:when>
                                        <c:when test="${item.PAY_RECORD_TYPE_CODE=='F'}">分期</c:when>
                                        <c:when test="${item.PAY_RECORD_TYPE_CODE=='G'}">入学测试费</c:when>
                                        <c:when test="${item.PAY_RECORD_TYPE_CODE=='H'}">第二年教材费</c:when>
                                        <c:when test="${item.PAY_RECORD_TYPE_CODE=='I'}">第三年教材费</c:when>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:set var="alreadyMoney" value="0" />
                                    <c:if test="${item.PAY_STATUS=='Y'}">
                                        <c:set var="alreadyMoney" value="${item.REC_AMT}" />
                                    </c:if>
                                    <fmt:formatNumber value="${alreadyMoney}" type="currency" minFractionDigits="2" />/<fmt:formatNumber value="${item.REC_AMT}" type="currency" minFractionDigits="2" /></td>
                                <td>--/<fmt:formatDate value="${item.REC_DATE}" type="date" /></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.PAY_STATUS=='Y'}">
                                            <span class="text-green">已缴费</span>
                                        </c:when>
                                        <c:when test="${item.PAY_STATUS=='N'&&item.PAY_OVERDUE_SIGN=='N'}">
                                            <span class="">未开始</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-red">欠费</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
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
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
</body>
</html>
