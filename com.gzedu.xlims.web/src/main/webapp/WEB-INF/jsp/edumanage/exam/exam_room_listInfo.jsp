<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>教学教务组织平台</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">考试管理</a></li>
		<li class="active">考场管理</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<div class="box box-border">
	    <div class="box-body">
	      
	        <div class="row reset-form-horizontal clearbox">

	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">考试计划</label>
	              <div class="col-sm-9">
	                <select class="form-control select2" id="exam_batch_id" name="EXAM_BATCH_ID" data-size="5" data-live-search="true">
	                  <option value="" selected="selected">请选择</option>
	                  <c:forEach items="${batchMap }" var="map">
	                  	<option value="${map.EXAM_BATCH_ID }" <c:if test="${map.EXAM_BATCH_ID==examBatchId}">selected='selected'</c:if>>${map.EXAM_BATCH_NAME }</option>
	                  </c:forEach>
	                </select>
	              </div>
	            </div>
	          </div>			          

	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">考点</label>
	              <div class="col-sm-9">
	                <select class="form-control select2" id="examPointId" name="EXAM_POINT_ID" data-size="5" data-live-search="true">
	                	<option value="">请选择</option>
	                </select>
	              </div>
	            </div>
	          </div>

	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">考场名称</label>
	              <div class="col-sm-9">
	                <input type="text" class="form-control" name="EXAM_ROOM_NAME" id="exam_room_name" value="${param.EXAM_ROOM_NAME }">
	              </div>
	            </div>
	          </div>

	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">考场状态</label>
	              <div class="col-sm-9">
	                <select class="form-control" id="status" name="STATUS">
	                  <option value="">请选择</option>
	                  <option value="1">已启用</option>
	                  <option value="0">已停用</option>
	                </select>
	              </div>
	            </div>
	          </div>
	        </div>
	    </div><!-- /.box-body -->
	    <div class="box-footer text-right">
           <div class="btn-wrap">
				<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
			</div>
			<div class="btn-wrap">
				<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
			</div>
        </div><!-- /.box-footer-->
	</div>
	<div class="box box-border margin-bottom-none">
		<div class="box-header with-border">
			<div class="fr">
				<shiro:hasPermission name="/exam/new/room/list$export">
					<a href="javascript:void(0);" class="btn btn-sm btn-default btn-add btn-export">
						<i class="fa fa-fw fa-download"></i> 导出考场
					</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="/exam/new/room/list$import">
					<a href="javascript:void(0);" class="btn btn-sm btn-default btn-add btn-import margin_l10">
						<i class="fa fa-fw fa-upload"></i> 导入考场
					</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="/exam/new/room/list$create">
					<a href="create/0" class="btn btn-default btn-sm"> <i class="fa fa-fw fa-plus"></i> 新增</a>
				</shiro:hasPermission>
			</div>
		</div>
		<div class="box-body">
			<div class="table-responsive">
				<table class="table table-bordered table-striped vertical-mid text-center table-font">
					<thead>
		              <tr>
		              	<th>考试计划</th>
		                <th>考点</th>
		                <th>考场名称</th>
		                <th>顺序号</th>
		                <th>座位数</th>
		                <th>考场状态</th>
		                <th>操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
		            			<c:forEach items="${pageInfo.content }" var="entity">
				            		<tr>
				            			<td>
				            				${entity.EXAM_BATCH_NAME }<br>
				            				<span class="gray9">${entity.EXAM_BATCH_CODE }</span>
				            			</td>
				            			<td>
				            				${entity.EXAM_POINT_NAME }<br>
				            				<span class="gray9">${entity.CODE }</span>
				            			</td>
				            			<td>
				            				${entity.EXAM_ROOM_NAME }<br>
				            			</td>
				            			<td>${entity.ORDER_NO}</td>
				            			<td>
				            				${entity.SEATS }<br>
				            			</td>
				            			<td>
				            				<c:if test="${entity.STATUS eq '1' }">
				            					<span class="text-green">已启用</span>
				            				</c:if>
				            				<c:if test="${entity.STATUS eq '0' }">
				            					<span class="gray9">已停用</span>
				            				</c:if>
				            			</td>
				            			<td>
				            				<div class="data-operion">
					            					<c:if test="${entity.STATUS eq 1 }">
					            						<shiro:hasPermission name="/exam/new/room/list$update">
						            						<a href="update/${entity.EXAM_ROOM_ID }" class="operion-item operion-edit" title="编辑">
						            						<i class="fa fa-fw fa-edit"></i></a>
					            						</shiro:hasPermission>
					            						<!-- 
					            						<a href="javascript:;" id="${ctx}/exam/new/room/examRoomStatus?EXAM_ROOM_ID=${entity.EXAM_ROOM_ID}&STATUS=${entity.STATUS}" onclick="changeStatus($(this))" class="operion-item" data-toggle="tooltip" title="停用" data-original-title="停用">
					            							<i class="fa fa-fw fa-power-off"></i>
					            						</a> 
					            						 -->
					            					</c:if>
				            					
				            					<c:if test="${entity.STATUS eq 0 }">
				            						<!-- 
				            						<a href="javascript:;" id="${ctx}/exam/new/room/examRoomStatus?EXAM_ROOM_ID=${entity.EXAM_ROOM_ID}&STATUS=${entity.STATUS}" onclick="changeStatus($(this))" class="operion-item" data-toggle="tooltip" title="启用" data-original-title="启用">
				            							<i class="fa fa-fw fa-power-off"></i>
				            						</a> 
				            						 -->
				            						<!--  
				            						<a href="update/${entity.EXAM_ROOM_ID }" class="operion-item operion-edit" title="编辑">
				            						<i class="fa fa-fw fa-edit"></i></a>
				            						-->
				            						<shiro:hasPermission name="/exam/new/room/list$delete">
				            							<a href="javascript:void(0);" id="${entity.EXAM_ROOM_ID }" class="operion-item" data-toggle="tooltip" title="删除" data-role="remove"><i class="fa fa-fw fa-trash-o text-red"></i></a>
				            						</shiro:hasPermission>
				            					</c:if>
				            				</div>
				            			</td>
				            		</tr>
		            			</c:forEach>
		            		</c:when>
		            		<c:otherwise>
								<tr>
								 	<td align="center" colspan="7">暂无数据</td>
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

$(function() {
	$(".btn-export").click(function(event) {
		var url = ctx + "/exam/new/room/exportRoom?" + $("#listForm").serialize();
		window.open(url);
	});
	
	$(".btn-import").click(function(event) {
		$.mydialog({
		  id:'import',
		  width:600,
		  height:415,
		  zIndex:11000,
		  content: 'url:importRoom'
		});
	});
});

$('#exam_batch_id').change(function(){
	var exam_batch_id = $('#exam_batch_id').val();
	if(exam_batch_id!=''){
		$.ajax({
			type:"get",
			url:"${ctx}/exam/new/room/queryExamPoint?EXAM_BATCH_ID="+exam_batch_id,
			dataType:"json",
			data:"",
			success:function(datas){
				$('#examPointId').empty();
				$("#examPointId").append("<option value=''>请选择</option>");
				if(datas!=null && datas!=''){
					var data = datas.POLINTLIST;
					for(var i=0;i<data.length;i++){
						$("#examPointId").append("<option value='"+data[i].EXAM_POINT_ID+"'>"+data[i].EXAM_POINT_NAME+"（"+data[i].CODE+"）"+"</option>");
					}
				}
			}
		});
	}else{
		$('#examPointId').empty();
		$("#examPointId").append("<option value=''>请选择</option>");
		$("#examPointId").val("").trigger("change");
	}
});


//Initialize Select2 Elements
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

//删除
$("body").confirmation({
  selector: "[data-role='remove']",
  html:true,
  placement:'top',
  content:'<div class="margin_b10"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i><span class="inline-block vertical-mid">确定要该考场？</span></div><div class="f12 gray9 margin_b10">删除后将无法找回，请慎重选择</div>',
  title:false,
  btnOkClass    : 'btn btn-xs btn-primary',
  btnOkLabel    : '确认',
  btnOkIcon     : '',
  btnCancelClass  : 'btn btn-xs btn-default margin_l10',
  btnCancelLabel  : '取消',
  btnCancelIcon   : '',
  popContentWidth:200,
  onShow:function(event,element){
    
  },
  onConfirm:function(event,element){
	  var id = $(element).attr("id");
	  $.ajax({
		  url:"${ctx}/exam/new/room/truncExamRoom?EXAM_ROOM_ID="+id,
		  type:"post",
		  dataType:"json",
		  data:"",
		  success:function(data){
			  if(data!=null){
				  if(data.result == '1'){
					  window.location.href = "${ctx}/exam/new/room/list";
				  }else{
					  alert("删除考场失败！");
				  }
			  }
		  }
	  });
  },
  onCancel:function(event, element){
    
  }
});

function changeStatus(obj){
	var url = obj.attr("id");
	$.ajax({
		type:"POST",
		dataType:"json",
		url:url,
		data:"",
		success:function(data){
			window.location.href = "${ctx}/exam/new/room/list";
		}
	});
}


//导出
$("[data-role='export']").click(function(event) {
	event.preventDefault();
	$.mydialog({
	  id:'export',
	  width:540,
	  height:400,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')
	});
});

//批量导入
$("[data-role='set']").click(function(event) {
	event.preventDefault();
	$.mydialog({
	  id:'import',
	  width:600,
	  height:415,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')
	});
});

$(".btn-reset").click(function(){
	//重置除隐藏域的input 
	$("form").find(':text').val('');
	//下拉列表初始化
	$("form").find('select').val('').selectpicker('refresh');
	$("select[name='EXAM_BATCH_ID']").each(function(){
		$("#select2-exam_batch_id-container").html("请选择");
		$(this).val('');
	});
	$("select[name='EXAM_POINT_ID']").each(function(){
		$("#select2-examPointId-container").html("请选择");
		$(this).val('');
	});
	$("input[name='EXAM_ROOM_NAME']").each(function(){
		$(this).val('');
	});
});

</script>
</body>
</html>
