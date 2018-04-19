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
<section class="content-header">
  <button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
  <div class="pull-left">
    您所在位置：
  </div>
  <ol class="breadcrumb">
    <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
    <li><a href="#">考试管理</a></li>
    <li><a href="#">考试计划</a></li>
    <li class="active">计划详情</li>
  </ol>
</section>
<section class="content">
  <div class="box margin-bottom-none">
    <div class="box-body">
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-1" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">考试计划详情</span>
          </h3>
        </div>
        <div id="info-box-1" class="collapse in">
          <div class="panel-body">
              <div class="table-responsive margin-bottom-none">
                <table class="table-gray-th">
                	<tr>
                 		<th>
                 			计划编号：
                 		</th>
                 		<td>${batchInfo.EXAM_BATCH_CODE }</td>
                 	</tr>
                	<tr>
                 		<th>
                 			计划名称：
                 		</th>
                 		<td>${batchInfo.EXAM_BATCH_NAME }</td>
                 	</tr>
                	<tr>
                 		<th>
                 			学期：
                 		</th>
                 		<td>${batchInfo.GRADE_NAME }</td>
                 	</tr>
                	<tr>
                 		<th>
                 			预约考试时间：
                 		</th>
                 		<td> ${batchInfo.BOOK_ST }~${batchInfo.BOOK_END }</td>
                 	</tr>
                	<tr>
                 		<th>
                 			线下考试时间：
                 		</th>
                 		<td>  ${batchInfo.OFFLINE_ST }~${batchInfo.OFFLINE_END }</td>
                 	</tr>
                	<tr>
                 		<th>
							网考、大作业考试时间：
                 		</th>
                 		<td>  ${batchInfo.ONLINE_ST }~${batchInfo.ONLINE_END }</td>
                 	</tr>
                	<tr>
                 		<th>
                 			状态：
                 		</th>
                 		<td>
                 			<c:if test="${batchInfo.PLAN_STATUS eq '1' }">
								<span class="text-red">待审核</span>
							</c:if>
							<c:if test="${batchInfo.PLAN_STATUS eq '2' }">
								<span class="text-light-blue">审核不通过</span>
							</c:if>
							<c:if test="${batchInfo.PLAN_STATUS eq '3' }">
								<span class="text-light-blue">已发布</span>
							</c:if>
							<c:if test="${batchInfo.PLAN_STATUS eq '4' }">
								<span class="gray9">已过期</span>
							</c:if>
							<c:if test="${empty batchInfo.PLAN_STATUS }">
								<span class="gray9">--</span>
							</c:if>
                 		</td>
                 	</tr>
                  
                </table>
              </div>
          </div>
        </div>
      </div>
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-2" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">审核记录</span>
          </h3>
        </div>
        <div id="info-box-2" class="collapse in">
           	 <div class="approval-list clearfix">
           	 	<c:forEach items="${batchInfo.approvalList }" var="record" varStatus="s">
           	 		<c:if test="${record.AUDIT_STATE eq '0' and record.AUDIT_CONTENT eq '发布计划' }">
           	 			<dl class="approval-item">
           	 				<dt class="clearfix">
           	 					<b class="a-tit gray6">${record.AUDIT_OPERATOR }</b>
           	 					<span class="gray9 text-no-bold f12">${record.AUDIT_DT }</span>
           	 					<b class="a-tit gray6">发布</b>
           	 					<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
           	 					<label class="state-lb">发布计划</label>
           	 				</dt>
           	 			</dl>
           	 		</c:if>
           	 		<c:if test="${record.AUDIT_STATE eq '2'}">
           	 			<dl class="approval-item">
           	 				<dt class="clearfix">
           	 					<b class="a-tit gray6">${record.AUDIT_OPERATOR }</b>
           	 					<span class="gray9 text-no-bold f12">${record.AUDIT_DT }</span>
           	 					<span class="fa fa-fw fa-times-circle text-red"></span>
           	 					<label class="state-lb text-red">审核不通过</label>
           	 				</dt>
           	 				<dd>
           	 					<div class="txt">
           	 						<p>${record.AUDIT_CONTENT }</p>
           	 						<div class="gray9 text-right">审核人：${record.AUDIT_OPERATOR }</div>
           	 						<i class="arrow-top"></i>
           	 					</div>
           	 				</dd>
           	 			</dl>
           	 		</c:if>
           	 		<c:if test="${record.AUDIT_STATE eq '0' and record.AUDIT_CONTENT eq '重新发布计划' }">
           	 			<dl class="approval-item">
           	 				<dt class="clearfix">
           	 					<b class="a-tit gray6">${record.AUDIT_OPERATOR }</b>
           	 					<span class="gray9 text-no-bold f12">${record.AUDIT_DT }</span>
           	 					<b class="a-tit gray6">发布</b>
           	 					<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
           	 					
           	 				</dt>
           	 			</dl>
           	 		</c:if>
           	 		<c:if test="${record.AUDIT_STATE eq '1' }">
           	 			<dl class="approval-item">
           	 				<dt class="clearfix">
           	 					<b class="a-tit gray6">${record.AUDIT_OPERATOR }</b>
           	 					<span class="gray9 text-no-bold f12">${record.AUDIT_DT }</span>
           	 					<span class="fa fa-fw fa-check-circle text-green"></span>
           	 					<label class="state-lb text-green">审核通过</label>
           	 				</dt>
           	 			</dl>
           	 		</c:if>
           	 	</c:forEach>
           	 	<dl class="approval-item white-border">
           	 		<dt class="clearfix">
           	 			<b class="a-tit gray6">${record.AUDIT_OPERATOR }</b>
           	 			<span class="fa fa-fw fa-dot-circle-o text-yellow"></span>
           	 			<label class="state-lb pending-state">待审核</label>	
           	 		</dt>
           	 	<dd>
           	 		<form class="theform" action="${ctx}/exam/new/batch/saveApprovalData">
           	 			<input type="hidden" name="EXAM_BATCH_ID" value="${batchInfo.EXAM_BATCH_ID }">
           	 			<div class="col-xs-12 no-padding position-relative">
           	 				<textarea name="AUDIT_CONTENT" id="audit_content" class="form-control" rows="3" placeholder="请输入考试计划审核，确认无误" datatype="*1-500" nullmsg="请输入内容！" errormsg="字数不能超过500"></textarea>
           	 			</div>
           	 			<div>
           	 			<button type="button" class="btn min-width-90px btn-warning margin_r10 margin_t10 btn-save-edit" data-role="btn-nopass">审核不通过</button>
           	 			<button type="button" class="btn min-width-90px btn-success margin_r10 margin_t10" data-role="btn-pass">审核通过</button>
           	 			</div>
           	 		</form>
           	 	</dd>
           	 	</dl>
           	 </div>
        </div>
      </div>
    </div>
  </div>
</section>

<!-- 底部 -->
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
$('[data-role="btn-pass"]').click(function(event){
	 var dialog1=$.mydialog({
		  id:'dialog-1',
		  width:150,
		  height:50,
		  backdrop:false,
		  fade:true,
		  showCloseIco:false,
		  zIndex:11000,
		  content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
		});
	
	 var params = {
			 EXAM_BATCH_ID:'${batchInfo.EXAM_BATCH_ID}',
			 AUDIT_STATE:"1",
			 AUDIT_CONTENT:$("#audit_content").val()
	 };
	 $.ajax({
		 type:"post",
		 url:ctx+"/exam/new/batch/saveApprovalData",
		 dataType:"json",
		 data:params,
		 success:function(obj){
			 if(obj.successful == true){
				 window.location.href = ctx + '/exam/new/batch/list';
			 }else{
				 alert(obj.message);
			 }
		 }
	 });
});


$('[data-role="btn-nopass"]').click(function(event){
	var dialog1=$.mydialog({
		  id:'dialog-1',
		  width:150,
		  height:50,
		  backdrop:false,
		  fade:true,
		  showCloseIco:false,
		  zIndex:11000,
		  content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
		});
	
	 var params = {
			 EXAM_BATCH_ID:'${batchInfo.EXAM_BATCH_ID}',
			 AUDIT_STATE:"2",
			 AUDIT_CONTENT:$("#audit_content").val()
	 };
	 $.ajax({
		 type:"post",
		 url:ctx+"/exam/new/batch/saveApprovalData",
		 dataType:"json",
		 data:params,
		 success:function(obj){
			 if(obj.successful == true){
				 window.location.href = ctx + '/exam/new/batch/list';
			 }else{
				 alert(obj.message);
			 }
		 }
	 });
});

</script>

</body>
</html>

