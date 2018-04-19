<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>学年度管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li class="active">学期计划</li>
	</ol>
</section>

<section class="content">
<form class="form-horizontal" id="listForm" action="list.html">
	<div class="box">
	    <div class="box-body">
	        <div class="row pad-t15">
	          <div class="col-sm-4 col-xs-6">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">编号</label>
	              <div class="col-sm-9">
	                <input type="text" class="form-control" name="search_LIKE_gradeCode" 	value="${param.search_LIKE_gradeCode}"  >
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4 col-xs-6">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">学期名称</label>
	              <div class="col-sm-9">
	                <input type="text" class="form-control" name="search_LIKE_gradeName" 	value="${param.search_LIKE_gradeName}">
	              </div>
	            </div>
	          </div>
	        </div>
	    </div><!-- /.box-body -->
	    <div class="box-footer">
	      <div class="pull-right"><button type="button" class="btn min-width-90px btn-default btn-reset">重置</button></div>
	      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
	    </div><!-- /.box-footer-->
	</div>
	
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	
	<div class="box margin-bottom-none">
		<div class="box-header with-border">
			<h3 class="box-title pad-t5">计划列表</h3>
		</div>
		<div class="box-body">
			<div class="table-responsive">
				<table class="table table-bordered table-striped vertical-mid text-center table-font">
					<thead>
		              <tr>
		                <th>编号</th>
		                <th>学期名称</th>
		                <th>学期时间</th>
		                <th colspan="2">学期计划</th>
		                <th>学期详细计划</th>
		                <th>操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            <c:choose>
						<c:when test="${not empty pageInfo.content}">
						<c:forEach items="${pageInfo.getContent() }" var="item">
			            	<tr>
			            		<td rowspan="3">${item.gradeCode}</td>
			            		<td rowspan="3">${item.gradeName}</td>
			            		<td rowspan="3">${item.startDate}~${item.endDate}</td>
			            		<td>老生缴费日期</td>
			            		<td>${item.payStartDate}~${item.payEndDate}</td>
			            		<td rowspan="3">
			            			<c:if test="${item.countDate==0}">
			            				<span style="color:#9999B6">
			            				未设置
			            			</c:if>
			            			<c:if test="${item.countDate>0 && item.countDate<6}">
			            				<span style="color:#FF6600">
			            				设置中
			            			</c:if>
			            			<c:if test="${item.countDate==6}">
			            				<span style="color:#008070">
			            				已设置
			            			</c:if>
			            				${item.countDate}/6
			            			</span>
			            		</td>
			            		<td rowspan="3">
			            			<c:if test="${item.countDate<6}">
			            				<shiro:hasPermission name="/edumanage/studyyear/list$update">
			            					<a href="update/${item.gradeId}" class="operion-item" data-toggle="tooltip" title="编辑"><i class="fa fa-edit"></i></a>
			            				</shiro:hasPermission>
			            			</c:if>
			            			<c:if test="${item.countDate==6}">
			            				<a href="view/${item.gradeId}" class="operion-item operion-view" title="查看"><i class="fa fa-fw fa-eye"></i></a>
									</c:if>
			            		</td>
			            	</tr>		 
			            	<tr> 
			                  <td>老生开学日期</td>
			                  <td>${item.oldStudentEnterDate}</td>
			                </tr>
			                <tr> 
			                  <td>新生开学日期</td>
			                  <td>${item.newStudentEnterDate}</td>
			                </tr>
			            	
		            	</c:forEach> 
						</c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="8">暂无数据</td>
								</tr>
							</c:otherwise>
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

//删除
$("body").confirmation({
  selector: "[data-role='sure-btn-1']",
  html:true,
  placement:'top',
  content:'<div class="margin_b10"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>确定要删除该学年度？</div><div class="f12 gray9 margin_b10 text-center">学年度：{0}</div>',
  title:false,
  btnOkClass    : 'btn btn-xs btn-primary',
  btnOkLabel    : '确认',
  btnOkIcon     : '',
  btnCancelClass  : 'btn btn-xs btn-default margin_l10',
  btnCancelLabel  : '取消',
  btnCancelIcon   : '',
  popContentWidth:220,
  onShow:function(event,element){
	  var delName = $(element).attr('delName');
      this.content = this.content.replace(/\{0\}/g, delName); 
  },
  onConfirm:function(event,element){
	 window.location.href=element.context.href;
  },
  onCancel:function(event, element){
  }
});
</script>
</body>
</html>