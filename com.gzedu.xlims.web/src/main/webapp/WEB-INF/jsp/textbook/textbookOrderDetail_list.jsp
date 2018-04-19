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

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教材管理</a></li>
		<li><a href="#">教材订购</a></li>
		<li class="active">订购详情</li>
	</ol>
</section>
<section class="content">
	<div class="box">
		<div class="box-body">
			<c:choose>
				<c:when test="${order.status == 2 && canSubmitApproval}">
					<div class="custom-alert ">
						<div class="pull-right margin_r15"><button type="button" class="btn min-width-90px btn-warning submit-approval" lang="${param.orderId}">重新提交审核</button></div>
		
			            <div class="media no-padding no-margin oh">
			              <div class="media-left">
			                <i class="fa fa-exclamation-circle pad-t5" style="font-size:30px;margin-top:-5px;"></i>
			              </div>
			              <div class="media-body media-middle">
			                	<div>教材审核人 <fmt:formatDate value="${order.updatedDt}" pattern="yyyy-MM-dd"/> 审核不通过！</div>
			                	<div class="margin_t10">${order.reason}</div>
			              </div>
			            </div>
			      	</div>
				</c:when>
				<c:otherwise>
					<div class="text-right">
						<c:if test="${order.status == 0 && canSubmitApproval}">
							<button type="button" class="btn min-width-90px btn-primary submit-approval" lang="${param.orderId}">提交审核</button>
						</c:if>
						<c:if test="${order.status == 1 && canApproval}">
							<button type="button" class="btn min-width-90px btn-warning margin_r10" data-role="unpass" lang="${param.orderId}">审核不通过</button>
			            	<button type="button" class="btn min-width-90px btn-success" data-role="pass" lang="${param.orderId}">审核通过</button>
						</c:if>
		          	</div>
				</c:otherwise>
			</c:choose>
          	<table class="table table-bordered text-center vertical-mid table-font margin_t20">
				<thead class="with-bg-gray">
	              <tr>
	                <th>学期</th>
	                <th width="18%">教材计划</th>
	                <th>发放课程总数</th>
	                <th>发放总人数</th>
	                <th>教材发放总量</th>
	                <th>需订购总数量</th>
	                <th>订购总价</th>
	                <th>状态</th>
	              </tr>
	            </thead>
	            <tbody>
	            	<tr>
	            		<td>${order.gradeName}</td>
	            		<td>
	            			${order.planName}
	            			<div class="gray9">（${order.planCode}）</div>
	            		</td>
	            		<td>${order.courseNum}</td>
	            		<td>${order.studentNum}</td>
	            		<c:choose>
	            			<c:when test="${order.status == 0 || order.status == 2}">
	            				<td>${order.distributeNum2}</td>
			            		<td>${order.orderNum2}</td>
			            		<td>￥${order.orderPrice2}</td>
	            			</c:when>
	            			<c:otherwise>
	            				<td>${order.distributeNum1}</td>
			            		<td>${order.orderNum1}</td>
			            		<td>￥${order.orderPrice1}</td>
	            			</c:otherwise>
	            		</c:choose>
	            		<td>
	            			<c:choose>
	            				<c:when test="${order.status == 0}">
	            					<span class="gray9">待提交</span>
	            				</c:when>
	            				<c:when test="${order.status == 1}">
	            					<span class="text-orange">待审核</span>
	            				</c:when>
	            				<c:when test="${order.status == 2}">
	            					<span class="text-red">审核不通过</span>
	            				</c:when>
	            				<c:when test="${order.status == 3}">
	            					<span class="text-green">审核通过</span>
	            				</c:when>
	            			</c:choose>
	            		</td>
	            	</tr>
	            </tbody>
			</table>
		</div>
	</div>
	
	<form id="listForm" class="form-horizontal">
	<input type="hidden" name="orderId" value="${param.orderId}">
	<input type="hidden" name="search_EQ_stockStatus" value="">
	<div class="box">
	    <div class="box-body">
	        <div class="row pad-t15">
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">书号</label>
	              <div class="col-sm-9">
	                <input type="text" name="search_LIKE_textbookCode" class="form-control" value="${param['search_LIKE_textbookCode']}">
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">教材名称</label>
	              <div class="col-sm-9">
	                <input type="text" name="search_LIKE_textbookName" class="form-control" value="${param['search_LIKE_textbookName']}">
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">教材类型</label>
	              <div class="col-sm-9">
	                 <select name="search_EQ_textbookType" class="selectpicker form-control">
						<option value="">全部</option>
						<option value="1" <c:if test="${param['search_EQ_textbookType'] == 1}">selected="selected"</c:if> >主教材</option>
						<option value="2" <c:if test="${param['search_EQ_textbookType'] == 2}">selected="selected"</c:if> >复习资料</option>
					</select>
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">发放课程</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_courseId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
						<option value="" selected="selected">请选择</option>
						<c:forEach items="${courseMap}" var="map">
							<option value="${map.key}"<c:if test="${map.key==param['search_EQ_courseId']}">selected='selected'</c:if> >${map.value}</option>
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
			<h3 class="box-title pad-t5">订购详情</h3>
			<div class="pull-right no-margin">
				<c:if test="${(order.status == 0 || order.status == 2) && isBtnImport}">
					<button type="button" class="btn btn-default btn-sm btn-import">批量导入修改发放名单</button>
				</c:if>
				<c:if test="${isBtnExport }">
				<button type="button" class="btn btn-default btn-sm left10 btn-export">批量导出发放名单</button>
				</c:if>
			</div>
		</div>
		<div class="box-body">
			<div class="filter-tabs filter-tabs2 clearfix">
				<ul class="list-unstyled">
					<li lang=":input[name='search_EQ_stockStatus']" <c:if test="${empty param['search_EQ_stockStatus']}">class="actived"</c:if>>全部(${notEnough + enough})</li>
					<li value="0" role=":input[name='search_EQ_stockStatus']" <c:if test="${param['search_EQ_stockStatus'] == '0' }">class="actived"</c:if>>需订购(${notEnough})</li>
					<li value="1" role=":input[name='search_EQ_stockStatus']" <c:if test="${param['search_EQ_stockStatus'] == 1 }">class="actived"</c:if>>库存充足(${enough})</li>
				</ul>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered table-striped text-center vertical-mid table-font">
					<thead>
		              <tr>
		                <th>书号</th>
		                <th width="18%">教材名称</th>
		                <th>教材类型</th>
		                <th width="18%">发放课程</th>
		                <th>发放人数</th>
		                <th>教材发放量</th>
		                <th>剩余库存量</th>
		                <th>需订购数量</th>
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
						            		<td>${entity.textbookCode}</td>
						            		<td>${entity.textbookName}</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${entity.textbookType == 1}">主教材</c:when>
						            				<c:otherwise>复习资料</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<ul class="list-unstyled">
						            				<c:if test="${not empty entity.courseList}">
														<c:forEach  items="${entity.courseList}" var="item">
															<li>
																${item.kcmc}
																<span class="gray9">（${item.kch}）</span>
															</li>
														</c:forEach>
													</c:if>
						            			</ul>
						            		</td>
						            		<td>${entity.studentNum}</td>
						            		<td>${entity.distributeNum}</td>
						            		<td>${entity.stockNum}</td>
						            		<c:choose>
						            			<c:when test="${entity.distributeNum > entity.stockNum}">
						            				<td>${entity.distributeNum - entity.stockNum}</td>
						            				<td>￥${(entity.distributeNum - entity.stockNum) * entity.price}</td>
						            				<td><span class="text-orange">需订购</span></td>
						            			</c:when>
						            			<c:otherwise>
						            				<td>0</td>
						            				<td>￥0</td>
						            				<td><span class="text-green">库存充足</span></td>
						            			</c:otherwise>
						            		</c:choose>
						            		<td>
						            			<a href="viewDetail?orderId=${param.orderId}&textbookId=${entity.textbookId}" class="operion-item" data-toggle="tooltip" title="详情"><i class="fa fa-fw fa-view-more"></i></a>
						            		</td>
						            	</tr>
						            </c:if>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="11">暂无数据</td>
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
			window.location.href = "detailList?orderId=${param.orderId}";
		} else if (el == $li.context && index == 1) {
			window.location.href = "detailList?orderId=${param.orderId}&search_EQ_stockStatus=0";
		} else if (el == $li.context && index == 2) {
			window.location.href = "detailList?orderId=${param.orderId}&search_EQ_stockStatus=1";
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

$('[data-role="pass"]').click(function(event) {
	var id=$(this).attr('lang');
	$.alertDialog({
		id:'pass',
        width:400,
        height:280,
        zIndex:11000,
        ok:function(){//“确定”按钮的回调方法
        	//这里 this 指向弹窗对象
        	$.closeDialog(this);
        	$.post("approval", {orderId:id, status:3}, function(data){
       		 if(data.successful) {
       			 window.location.reload();
       		 } else {
       			 alert(data.message);
       		 }
       		},"json"); 
        },
        content:[
			'<div class="text-center">',
	    		'<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
	            '<span class="f16 inline-block vertical-mid text-left">',
	              '是否确认审核通过？',
	            '</span>',
	        '</div>'].join('')
	});
});

$('[data-role="unpass"]').click(function(event) {
	event.preventDefault();
	var id=$(this).attr('lang');
	var self=this;
	$.mydialog({
	  id:'result',
	  width:600,
	  height:408,
	  zIndex:11000,
	  content: 'url:approvalForm?orderId='+id
	});
});

$(".btn-export").click(function(event) {
	window.location.href=ctx+'/textbookOrder/exportDetail?orderId=${param.orderId}';
});

$(".btn-import").click(function(event) {
	var actionName = ctx+"/textbookOrder/import";
	var downloadFileUrl = ctx+"/excelImport/downloadModel?name=发放名单导入表.xls";
	var content1 = "为了方便你的工作，我们已经准备好了《发放名单导入表》的标准模板<br>你可以点击下面的下载按钮，下载标准模板。"
	var content2 = "请选择你要导入的《发放名单导入表》";
	excelImport(actionName, "file", "textbookOrderDetail", downloadFileUrl, null, "批量导入修改发放名单", null, null, null, content1, content2);
});
</script>
</body>
</html>
