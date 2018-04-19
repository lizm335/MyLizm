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
		<li><a href="#">订购详情</a></li>
		<li class="active">课程订购详情</li>
	</ol>
</section>
<section class="content">
	<div class="box">
		<div class="box-body">
          	<table class="table table-bordered text-center vertical-mid table-font">
				<thead class="with-bg-gray">
	              <tr>
	                <th>书号</th>
	                <th width="12%">教材名称</th>
	                <th>教材类型</th>
	                <th width="12%">发放课程</th>
	                <th>发放人数</th>
	                <th>教材发放量</th>
	                <th>剩余库存量</th>
	                <th>需订购数量</th>
	                <th>订购总价</th>
	                <th>状态</th>
	              </tr>
	            </thead>
	            <tbody>
	            	<c:forEach items="${detailLSummary.content}" var="entity">
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
		            			${entity.courseName}
		            			<div class="gray9">（${entity.courseCode}）</div>
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
		            	</tr>
	            	</c:forEach>
	            </tbody>
			</table>
		</div>
	</div>
	
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	
	<form id="listForm" class="form-horizontal">
	<input type="hidden" name="orderId" value="${param.orderId}">
	<input type="hidden" name="textbookId" value="${param.textbookId}">
	<div class="box margin-bottom-none">
		<div class="box-body">
			<c:if test="${(order.status == 0 || order.status == 2) && isDistribute}">
				<div class="pull-right">
					<button type="button" class="btn btn-warning btn-sm" data-role="unissue">批量设置不发放</button>
					<button type="button" class="btn btn-primary btn-sm margin_l10" data-role="issue">批量设置发放</button>
				</div>
			</c:if>
			<div class="filter-tabs clearfix">
				<ul class="list-unstyled">
					<li <c:if test="${empty param['search_EQ_needDistribute']}">class="actived"</c:if>>全部(${need + notNeed})</li>
					<li <c:if test="${param['search_EQ_needDistribute'] == 1 }">class="actived"</c:if>>需发放(${need})</li>
					<li <c:if test="${param['search_EQ_needDistribute'] == 0 }">class="actived"</c:if>>不发放(${notNeed})</li>
				</ul>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered table-striped text-center vertical-mid table-font">
					<thead>
		              <tr>
		              	<c:if test="${isDistribute}">
		              	<th width="40"><input type="checkbox" class="select-all no-margin"></th>
		              	</c:if>
		                <th>姓名</th>		                
		                <th>学号</th>
		                <th width="30%">报读信息</th>
		                <th>状态</th>
		                <c:if test="${isDistribute}">
		                <td>操作</td>
		                </c:if>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
									<c:if test="${not empty entity}">
						            	<tr>
						            		<c:if test="${isDistribute}">
						            		<td><input type="checkbox" name="orderDetailIds" value="${entity.detailId}"></td>
						            		</c:if>
						            		<td>${entity.gjtStudentInfo.xm}</td>
						            		<td>${entity.gjtStudentInfo.xh}</td>
						            		<td>
						            			<div class="text-left">
							            			学期：${entity.gjtStudentInfo.gjtGrade.gradeName} <br>
							            			层次：${pyccMap[entity.gjtStudentInfo.pycc]} <br>
							            			专业：${entity.gjtStudentInfo.gjtSpecialty.zymc} <br>
							            			学习中心：${entity.gjtStudentInfo.gjtStudyCenter.scName}
						            			</div>
						            		</td>
						            		<c:choose>
						            			<c:when test="${entity.needDistribute == 1}">
						            				<td>
								            			<span class="text-green">需发放</span>
								            		</td>
								            		<c:if test="${isDistribute}">
								            		<td>
								            			<c:choose>
								            				<c:when test="${order.status == 0 || order.status == 2}">
								            					<span class="text-blue setNotNeedDistribute" role="button" id="${entity.detailId}">设置不发放</span>
								            				</c:when>
								            				<c:otherwise>--</c:otherwise>
								            			</c:choose>
								            		</td>
								            		</c:if>
						            			</c:when>
						            			<c:otherwise>
						            				<td>
								            			<span class="text-red">不发放</span>
								            		</td>
								            		<c:if test="${isDistribute}">
								            		<td>
								            			<c:choose>
								            				<c:when test="${order.status == 0 || order.status == 2}">
								            					<span class="text-blue setNeedDistribute" role="button" id="${entity.detailId}">设置发放</span>
								            				</c:when>
								            				<c:otherwise>--</c:otherwise>
								            			</c:choose>
								            		</td>
								            		</c:if>
						            			</c:otherwise>
						            		</c:choose>
						            	</tr>
						            </c:if>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="6">暂无数据</td>
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
$(".filter-tabs li").click(function(event) {
	var $li = $(this);
	$(".filter-tabs li").each(function(index, el) {
		if (el == $li.context && index == 0) {
			window.location.href = "viewDetail?orderId=${param.orderId}&textbookId=${param.textbookId}";
		} else if (el == $li.context && index == 1) {
			window.location.href = "viewDetail?orderId=${param.orderId}&textbookId=${param.textbookId}&search_EQ_needDistribute=1";
		} else if (el == $li.context && index == 2) {
			window.location.href = "viewDetail?orderId=${param.orderId}&textbookId=${param.textbookId}&search_EQ_needDistribute=0";
		}
	});
});

$(".setNeedDistribute").click(function() {
	var id=$(this).attr('id');
	$.confirm({
        title: '提示',
        content: '确认设置发放？',
        confirmButton: '确认',
        icon: 'fa fa-warning',
        cancelButton: '取消',  
        confirmButtonClass: 'btn-primary',
        closeIcon: true,
        closeIconClass: 'fa fa-close',
        confirm: function () { 
        	 $.post("setNeedDistribute", {orderDetailIds:id, needDistribute:1}, function(data){
        		 if(data.successful) {
        			 window.location.reload();
        		 } else {
        			 alert(data.message);
        		 }
        		},"json"); 
        } 
    });
});

$(".setNotNeedDistribute").click(function() {
	var id=$(this).attr('id');
	$.confirm({
        title: '提示',
        content: '确认设置不发放？',
        confirmButton: '确认',
        icon: 'fa fa-warning',
        cancelButton: '取消',  
        confirmButtonClass: 'btn-primary',
        closeIcon: true,
        closeIconClass: 'fa fa-close',
        confirm: function () { 
        	 $.post("setNeedDistribute", {orderDetailIds:id, needDistribute:0}, function(data){
        		 if(data.successful) {
        			 window.location.reload();
        		 } else {
        			 alert(data.message);
        		 }
        		},"json"); 
        } 
    });
});

//全选
$(".table").on("click",".select-all",function(){
var $tbl=$(this).closest(".table");
if($(this).is(":checked")){
  $tbl.find(':checkbox').prop("checked",true);
}
else{
  $tbl.find(':checkbox').prop("checked",false);
}
});

//批量设置不发放
$('[data-role="unissue"]').click(function(event) {
	var $checkedIds = $(".table tbody input:checked");
	if($checkedIds.length<=0) {
        var postIng=$.formOperTipsDialog({text:'请选择学员'});
		setTimeout(function(){
	        $.closeDialog(postIng);
	    },1500);
        return;
    } else{
    	$.alertDialog({
			id:'delete',
	        width:400,
	        height:280,
	        zIndex:11000,
	        ok:function(){//“确定”按钮的回调方法
	        	//这里 this 指向弹窗对象
	        	$.closeDialog(this);
	        	var orderDetailIds = "";
	        	$checkedIds.each(function(index, el) {
	        		orderDetailIds += $(this).val();
	        		if ($checkedIds.length != index + 1) {
	        			orderDetailIds += ","
	        		}
	        	});
	        	$.post("setNeedDistribute",{orderDetailIds:orderDetailIds, needDistribute:0},function(data){
             		if(data.successful){ 
             			window.location.reload();
               		}else{
               			alert(data.message);
               		}
             	},"json");
	        },
	        content:[
				'<div class="text-center">',
		    		'<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
		            '<span class="f16 inline-block vertical-mid text-left">',
		              '是否确认批量设置不发放？',
		            '</span>',
		        '</div>'].join('')
		});
    }
});

//批量设置发放
$('[data-role="issue"]').click(function(event) {
	var $checkedIds = $(".table tbody input:checked");
	if($checkedIds.length<=0) {
      var postIng=$.formOperTipsDialog({text:'请选择学员'});
		setTimeout(function(){
	        $.closeDialog(postIng);
	    },1500);
      return;
  } else{
  	$.alertDialog({
			id:'delete',
	        width:400,
	        height:280,
	        zIndex:11000,
	        ok:function(){//“确定”按钮的回调方法
	        	//这里 this 指向弹窗对象
	        	$.closeDialog(this);
	        	var orderDetailIds = "";
	        	$checkedIds.each(function(index, el) {
	        		orderDetailIds += $(this).val();
	        		if ($checkedIds.length != index + 1) {
	        			orderDetailIds += ","
	        		}
	        	});
	        	$.post("setNeedDistribute",{orderDetailIds:orderDetailIds, needDistribute:1},function(data){
           		if(data.successful){ 
           			window.location.reload();
           		}else{
           			alert(data.message);
           		}
           	},"json");
	        },
	        content:[
				'<div class="text-center">',
		    		'<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
		            '<span class="f16 inline-block vertical-mid text-left">',
		              '是否确认批量设置发放？',
		            '</span>',
		        '</div>'].join('')
		});
  }
});
</script>
</body>
</html>
