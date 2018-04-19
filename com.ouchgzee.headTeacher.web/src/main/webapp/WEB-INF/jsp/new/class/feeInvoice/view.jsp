<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 发票管理</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
<button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
    <h1>
        学员发票详情
    </h1>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="${ctx}/home/class/feeInvoice/list">发票管理</a></li>
        <li class="active">学员发票详情</li>
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
                    	<div class="col-xs-6 col-sm-4 pad-b5">
			              <b>学号:</b> <span>${info.xh}</span>
			            </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>手机:</b> <span>${info.sjh}</span>
                        </div>
                        <div class="col-xs-6 col-sm-4 pad-b5">
			              <b>邮箱:</b>
			              <span>${info.dzxx }</span>
			            </div>
                        <div class="col-xs-6 col-sm-4 pad-b5">
			              <b>层次:</b> <span>${pyccMap[info.pycc]}</span>
			            </div>
                        <div class="col-xs-6 col-sm-4 pad-b5">
			              <b>年级:</b>
			              <span>${gradeMap[info.nj]}</span>
			            </div>
                        
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>专业:</b>
                            <span>${info.gjtSpecialty.zymc}（<dic:getLabel typeCode="TrainingLevel" code="${info.pycc}" />）</span>
                        </div>
                        <!--  
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>报读产品:</b>
                            <span>${info.gjtSpecialty.zymc}（<dic:getLabel typeCode="TrainingLevel" code="${info.pycc}" />）</span>
                        </div>
                        -->
                        <!-- 
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>当前状态:</b>
                            <c:if test="${stuInvoiceInfo.state=='A'}"><span class="text-red">待发放</span></c:if>
                            <c:if test="${stuInvoiceInfo.state=='B'}"><span>已开具</span></c:if>
                            <c:if test="${stuInvoiceInfo.state=='C'}"><span>已发放</span></c:if>
                            <c:if test="${stuInvoiceInfo.state=='D'}"><span class="text-green">已领取</span></c:if>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>邮箱:</b> <span>${info.dzxx}</span>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>缴费类型:</b>
                            <c:if test="${stuInvoiceInfo.gkxlPaymentTpye=='A'}"><span>全额缴费</span></c:if>
                            <c:if test="${stuInvoiceInfo.gkxlPaymentTpye=='B'}"><span>首年缴费</span></c:if>
                            <c:if test="${stuInvoiceInfo.gkxlPaymentTpye=='C'}"><span>分期付款</span></c:if>
                        </div>
                        <div class="col-xs-6 col-md-4 pad-b5">
                            <b>发放次数/申请次数:</b>
                            <span>${stuInvoiceInfo.issueNum}/${stuInvoiceInfo.totalApplyNum}</span>
                        </div>
                         -->
                    </div>
                </div>
            </div>
        </div>
        <div class="box-footer">
	      <div class="row stu-info-status">
	        <div class="col-xs-4">
	          <div class="f24 text-center">
	          		<c:if test="${stuInvoiceInfo.gkxlPaymentTpye=='A'}"><span>全额缴费</span></c:if>
                    <c:if test="${stuInvoiceInfo.gkxlPaymentTpye=='B'}"><span>首年缴费</span></c:if>
                    <c:if test="${stuInvoiceInfo.gkxlPaymentTpye=='C'}"><span>分期付款</span></c:if>
	          </div>
	          <div class="text-center gray6">缴费方式</div>
	        </div>
	        <div class="col-xs-4">
	          <div class="f24 text-center text-red">
	          		<span>${stuInvoiceInfo.issueNum}/${stuInvoiceInfo.totalApplyNum}</span>
	          </div>
	          <div class="text-center gray6">发放次数/申请次数</div>
	        </div>
	        <div class="col-xs-4">
	          <div class="f24 text-center text-orange">
	          	<c:if test="${stuInvoiceInfo.state=='A'}"><span class="text-red">待发放</span></c:if>
                <c:if test="${stuInvoiceInfo.state=='B'}"><span>已开具</span></c:if>
                <c:if test="${stuInvoiceInfo.state=='C'}"><span>已发放</span></c:if>
                <c:if test="${stuInvoiceInfo.state=='D'}"><span class="text-green">已领取</span></c:if>
	          </div>
	          <div class="text-center gray6">当前状态</div>
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
                    <th>发票抬头</th>
                    <th>发票金额(元)</th>
                    <th>发票申请时间</th>
                    <th>确认发放时间</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty stuInvoiceInfo && fn:length(stuInvoiceInfo.invoiceList) > 0}">
                        <c:forEach var="item" items="${stuInvoiceInfo.invoiceList}" varStatus="s">
                            <tr>
                                <td>${s.index+1}</td>
                                <td>${item.INVOICE_NAME}</td>
                                <td><fmt:formatNumber value="${item.INVOICE_AMOUNT}" type="number" minFractionDigits="2" /></td>
                                <td></td>
                                <td><fmt:formatDate value="${item.INVOICE_TIME}" type="date" /></td>
                                <td>
                                    <c:if test="${item.STATE=='A'}"><span class="text-red">待发放</span></c:if>
                                    <c:if test="${item.STATE=='B'}">已开具</c:if>
                                    <c:if test="${item.STATE=='C'}">已发放</c:if>
                                    <c:if test="${item.STATE=='D'}"><span class="text-green">已领取</span></c:if>
                                </td>
                                <td>
                                    <c:if test="${item.STATE=='A'||item.STATE=='B'}">
                                        <a href="${ctx}/home/class/feeInvoice/issue?invoiceNo=${item.INVOICE_NO}&studentId=${studentId}" class="operion-item confirmed-pay">确认发放</a>
                                    </c:if>
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
<script type="application/javascript">
    var flag;
    // 确认发放发票
    $("body").confirmation({
        selector: ".confirmed-pay:not([disabled])",
        html:true,
        placement:'top',
        content:'<div class="margin_b15"><span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span><span class="f12">确认已发放该发票给学员？</span></div>',
        title:'确认',
        btnOkClass    : 'btn btn-xs btn-primary',
        btnOkLabel    : '确认',
        btnOkIcon     : '',
        btnCancelClass  : 'btn btn-xs btn-default margin_l10',
        btnCancelLabel  : '取消',
        btnCancelIcon   : '',
        popContentWidth : 200,
        onShow:function(event,element){
            flag = true;
        },
        onConfirm:function(event,element){
            if(flag) {
                flag = false;
                window.location.href = $(element).attr('href');
            }
        },
        onCancel:function(event, element){

        }
    });
</script>
</body>
</html>
