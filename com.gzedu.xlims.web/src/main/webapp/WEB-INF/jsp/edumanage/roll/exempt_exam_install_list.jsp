<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统-免修免考</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li>学籍管理</li>
		<li>免修免考</li>
		<li class="active">免修免考设置</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs nav-tabs-lg">
			<!-- <li class="active"><a data-toggle="tab" href="#tab_top_1">学员申请</a></li>
			<li><a href="#tab_top_2" data-toggle="tab">免修免考课程设置</a></li> -->
			<li <c:if test="${exemptExamType==1}">class="active"</c:if>><a href="${ctx}/edumanage/exemptExamInfo/list.html">学员申请</a></li>
			<li <c:if test="${exemptExamType==2}">class="active"</c:if>><a href="${ctx}/edumanage/exemptExamInstall/list.html">免修免考课程设置</a></li>
		</ul>
		<div class="tab-content">
		
		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			
			<div class="tab-pane active" id="tab_top_2">
				<div class="box box-border margin-bottom-none">
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">信息列表</h3>
						<div class="pull-right">
							<a href="toAdd" class="btn btn-default btn-sm btn-import"><i class="fa fa-fw fa-plus"></i> 新增免修免考</a>
						</div>
					</div>
					<div class="box-body">
						<div class="table-responsive">
					        <table class="table table-bordered table-striped vertical-mid table-font text-center">
			                  <thead>
			                    <tr>
			                      <th>课程名称</th>
			                      <th width="27%">适用学期</th>
			                      <th width="14%">材料证明清单数</th>
			                      <th width="14%">状态</th>
			                      <th width="14%" class="text-nowrap">操作</th>
			                    </tr>
			                  </thead>
			                  <tbody>
			                  <c:forEach items="${pageInfo.content }" var="install">
			                  <input type="hidden" id="installId" value="${install.installId}"/>
			                    <tr>
			                      <td>
			                       	 ${install.gjtCourse.kcmc}
			                      </td>
			                      <c:if test="${not empty install.gradeId}">
			                      <td>			                       
			                        ${install.gradeName}
			                      </td>
			                      </c:if>
			                       <c:if test="${empty install.gradeId}">
			                      <td>			                       
			                        	通用
			                      </td>
			                      </c:if>
			                      <td>
			                        ${install.material}
			                      </td>
			                      <td>
			                      <c:choose>
				                      <c:when test="${install.status==0}">
				                        	<div class="text-green">已停用</div>
				                       </c:when>
				                       <c:otherwise>
				                       		<div class="text-green">已启用</div>
				                       </c:otherwise>
			                       </c:choose>
			                      </td>
			                     <c:choose>
			                      	<c:when test="${install.status==0}">
			                      	<td>
			                      	  <a href="${ctx}/edumanage/exemptExamInstall/setExemptExamStatus/${install.installId}/1"  class="operion-item" data-toggle="tooltip" title="启用" data-role="oper" data-type="start"><i class="fa fa-play-circle-o"></i></a>			                          
			                          <a href="${ctx}/edumanage/exemptExamInstall/toViewAndUpdate/${install.installId}/update" class="operion-item" data-toggle="tooltip" title="编辑"><i class="fa fa-fw fa-edit"></i></a>
			                      	 <%-- <a href="${ctx}/edumanage/exemptExamInstall/delete/${install.installId}" class="operion-item" data-role="remove" data-type="removeInfo" data-toggle="tooltip" title="删除"><i class="fa fa-fw fa-trash"></i></a> --%>
			                      	  <a href="${ctx}/edumanage/exemptExamInstall/delete/${install.installId}" class="operion-item" data-toggle="tooltip" title="删除" data-role='remove-item'><i class="fa fa-trash-o text-red"></i></a>
			                      	  
			                      	</td>
			                      	</c:when>
			                      	<c:otherwise>
			                      	<td>
			                      	  <a href="${ctx}/edumanage/exemptExamInstall/setExemptExamStatus/${install.installId}/0"  class="operion-item" data-toggle="tooltip" title="停用" data-role="oper" data-type="stop"><i class="fa fa-minus-circle"></i></a>
			                      	  <a href="${ctx}/edumanage/exemptExamInstall/toViewAndUpdate/${install.installId}/view" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
			                         </td> 
			                      	</c:otherwise>			                      	
			                      </c:choose>			                     			                        
			                    </tr>
			                   </c:forEach>			                    			                    
			                  </tbody>			                 
			                </table>
					    </div>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
//select2
$(".select2").select2();
// filter tabs
$(".filter-tabs li").click(function(event) {
	if($(this).hasClass('actived')){
		$(this).removeClass('actived');
	}
	else{
		$(this).addClass('actived');
	}
});

//确认提醒
$(document).confirmation({
  selector: '[data-role="oper"]',
  html:true,
  placement:'top',
  content:'<div class="f12 gray9 margin_b10 text-center"><i class="f16 fa fa-fw fa-exclamation-circle text-red"></i>{0}</div>',
  title:false,
  btnOkClass    : 'btn btn-xs btn-primary',
  btnOkLabel    : '确认',
  btnOkIcon     : '',
  btnCancelClass  : 'btn btn-xs btn-default margin_l10',
  btnCancelLabel  : '取消',
  btnCancelIcon   : '',
  popContentWidth:190,
  onShow:function(event,element){
  	var type=$(element).data('type');
  	if(type=='stop'){
  		this.content=this.content.format('是否停用？');
  	}
  	else if(type=='start'){
  		this.content=this.content.format('是否启用？');
  		
  	}
  },
  onConfirm:function(event,element){	  	  
	  var url=$(element).attr("href");
	  $.ajax({
		  type:"POST",
		  url:url,
		  dataType:"JSON",
		  async: true,
		  success: function(datas){
			if(datas.successful){
				alert(datas.message);
				window.location.reload();
			}else{
				alert(datas.message);
				window.location.reload();
			}	
		  }
	  });
  },
  onCancel:function(event, element){	 
   }
});
//删除
$("body").confirmation({
  selector: "[data-role='remove-item']",
  html:true,
  placement:'top',
  content:'<div class="margin_b10 text-center"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>确定要删除该免修免考信息？</div><div class="f12 gray9 margin_b10 text-center">删除后将无法找回，请慎重选择</div>',
  title:false,
  btnOkClass    : 'btn btn-xs btn-primary',
  btnOkLabel    : '确认',
  btnOkIcon     : '',
  btnCancelClass  : 'btn btn-xs btn-default margin_l10',
  btnCancelLabel  : '取消',
  btnCancelIcon   : '',
  popContentWidth:250,
  onShow:function(event,element){
    
  },
  onConfirm:function(event,element){
	  var url=$(element).attr("href");
		 $.ajax({
			  type:"POST",
			  url:url,
			  dataType:"JSON",
			  async: true,
			  success: function(datas){
				if(datas.successful){
					alert(datas.message);
					window.location.reload();
				}else{
					alert(datas.message);
					window.location.reload();
				}	
			  }
		  }); 
  },
  onCancel:function(event, element){	 
  }
});

</script>
</body>
</html>
