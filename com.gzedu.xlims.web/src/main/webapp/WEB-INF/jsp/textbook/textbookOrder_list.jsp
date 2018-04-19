<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">
<!-- 数据来源与《发放编排-设置教材发放安排》 -->
<section class="content-header clearfix">
	<ol class="breadcrumb oh">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教材管理</a></li>
		<li class="active">教材订购</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<input type="hidden" name="search_EQ_status" value="">
	<div class="box">
	    <div class="box-body">
	        <div class="row pad-t15">
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">学期</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
						<option value="" selected="selected">请选择</option>
						<c:forEach items="${termMap}" var="map">
							<option value="${map.key}"<c:if test="${map.key==param['search_EQ_gradeId']}">selected='selected'</c:if> >${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">教材计划</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_planId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
						<option value="" selected="selected">请选择</option>
						<c:forEach items="${textbookPlanMap}" var="map">
							<option value="${map.key}"<c:if test="${map.key==param['search_EQ_planId']}">selected='selected'</c:if> >${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	          </div>
	        </div>
	    </div><!-- /.box-body -->
	    <div class="box-footer">
	      <div class="pull-right"><button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button></div>
	      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
	    </div><!-- /.box-footer-->
	</div>
	
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	
	<div class="box">
		<div class="box-header with-border">
			<h3 class="box-title pad-t5">订购列表</h3>
		</div>
		<div class="box-body">
			<div class="filter-tabs filter-tabs2 clearfix">
				<ul class="list-unstyled">
					<li lang=":input[name='search_EQ_status']" <c:if test="${empty param['search_EQ_status']}">class="actived"</c:if>>全部(${preSubmit + preAudit + auditNotPass + auditPass})</li>
					<li value="0" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == '0' }">class="actived"</c:if>>待提交(${preSubmit})</li>
					<li value="1" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 1 }">class="actived"</c:if>>待审核(${preAudit})</li>
					<li value="2" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 2 }">class="actived"</c:if>>审核不通过(${auditNotPass})</li>
					<li value="3" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 3 }">class="actived"</c:if>>审核通过(${auditPass})</li>
				</ul>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered table-striped text-center vertical-mid table-font">
					<thead>
		              <tr>
		                <th>学期</th>
		                <th width="18%">教材计划</th>
		                <th>发放课程总数</th>
		                <th>发放总人数</th>
		                <th>教材发放总量</th>
		                <th>需订购总数量</th>
		                <th>订购总价</th>
		                <th>状态</th>
		                <th>操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
									<c:if test="${not empty entity}">
						            	<tr>
						            		<td>${entity.gradeName}</td>
						            		<td>
						            			${entity.planName}
						            			<div class="gray9">（${entity.planCode}）</div>
						            		</td>
						            		<td>${entity.courseNum}</td>
						            		<td>${entity.studentNum}</td>
						            		<c:choose>
						            			<c:when test="${entity.status == 0 || entity.status == 2}">
						            				<td>${entity.distributeNum2}</td>
								            		<td>${entity.orderNum2}</td>
								            		<td>￥${entity.orderPrice2}</td>
						            			</c:when>
						            			<c:otherwise>
						            				<td>${entity.distributeNum1}</td>
								            		<td>${entity.orderNum1}</td>
								            		<td>￥${entity.orderPrice1}</td>
						            			</c:otherwise>
						            		</c:choose>
						            		<td>
						            			<c:choose>
						            				<c:when test="${entity.status == 0}">
						            					<span class="gray9">待提交</span>
						            				</c:when>
						            				<c:when test="${entity.status == 1}">
						            					<span class="text-orange">待审核</span>
						            				</c:when>
						            				<c:when test="${entity.status == 2}">
						            					<span class="text-red">审核不通过</span>
						            				</c:when>
						            				<c:when test="${entity.status == 3}">
						            					<span class="text-green">审核通过</span>
						            				</c:when>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<c:if test="${(entity.status == 0 || entity.status == 2) && canSubmitApproval}">
						            				<a href="javascript:void(0);" lang="${entity.orderId}" class="operion-item submit-approval" data-toggle="tooltip" title="提交审核"><i class="fa fa-fw fa-shxxjl"></i></a>
						            			</c:if>
						            			<c:if test="${entity.status == 1 && canApproval}">
						            				<a href="javascript:void(0);" lang="${entity.orderId}" class="operion-item approval" data-toggle="tooltip" title="审核"><i class="fa fa-fw fa-shxxjl"></i></a>
						            			</c:if>
						            			<c:if test="${isBtnView}">
						            				<a href="detailList?orderId=${entity.orderId}" class="operion-item" data-toggle="tooltip" title="详情"><i class="fa fa-fw fa-view-more"></i></a>
						            			</c:if>
						            		</td>
						            	</tr>
						            </c:if>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="9">暂无数据</td>
								</tr>
							</c:otherwise>
						</c:choose>
		            </tbody>
				</table>
			</div>
			<tags:pagination page="${pageInfo}" paginationSize="5" />
		</div>
	</div>
	</form>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

//filter tabs
/* $(".filter-tabs li").click(function(event) {
	var $li = $(this);
	$(".filter-tabs li").each(function(index, el) {
		if (el == $li.context && index == 0) {
			window.location.href = "list";
		} else if (el == $li.context && index == 1) {
			window.location.href = "list?search_EQ_status=0";
		} else if (el == $li.context && index == 2) {
			window.location.href = "list?search_EQ_status=1";
		} else if (el == $li.context && index == 3) {
			window.location.href = "list?search_EQ_status=2";
		} else if (el == $li.context && index == 4) {
			window.location.href = "list?search_EQ_status=3";
		}
	});
}); */

$(".submit-approval").click(function() {
	var id=$(this).attr('lang');
	$.confirm({
        title: '提示',
        content: '确认提交审核？',
        confirmButton: '确认',
        icon: 'fa fa-warning',
        cancelButton: '取消',  
        confirmButtonClass: 'btn-primary',
        closeIcon: true,
        closeIconClass: 'fa fa-close',
        confirm: function () { 
        	 $.post("submitApproval", {orderId:id}, function(data){
        		 if(data.successful) {
        			 window.location.reload();
        		 } else {
        			 alert(data.message);
        		 }
        		},"json"); 
        } 
    });
});

$("body").confirmation({
	  selector: ".approval",
	  html:true,
	  placement:'top',
	  content:'',
	  title:false,
	  btnOkClass    : 'btn btn-xs btn-warning',
	  btnOkLabel    : '审核不通过',
	  btnOkIcon     : '',
	  btnCancelClass  : 'btn btn-xs btn-success margin_l10',
	  btnCancelLabel  : '审核通过',
	  btnCancelIcon   : '',
	  popContentWidth:220,
	  onShow:function(event,element){
		  
	  },
	  onConfirm:function(event,element){
		  var id = $(element).attr('lang');
		  $.mydialog({
			  id:'result',
			  width:600,
			  height:408,
			  zIndex:11000,
			  content: 'url:approvalForm?orderId='+id
			});
	  },
	  onCancel:function(event, element){
		  var id = $(element).attr('lang');
		  $.post("approval", {orderId:id, status:3}, function(data){
       		 if(data.successful) {
       			 window.location.reload();
       		 } else {
       			 alert(data.message);
       		 }
       	  },"json"); 
	  }
	});

</script>
</body>
</html>
