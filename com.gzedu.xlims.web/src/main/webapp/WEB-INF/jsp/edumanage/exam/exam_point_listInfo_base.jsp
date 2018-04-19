<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>教学教务组织平台</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">考试管理</a></li>
		<li class="active">考点管理</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal" action="${ctx}/exam/new/point/baseList" method="post">
	<div class="nav-tabs-custom no-margin">
	 <ul class="nav nav-tabs nav-tabs-lg">
		<li><a href="list?EXAM_TYPE=8" target="_self">笔试</a></li>
		<li><a href="list?EXAM_TYPE=11" target="_self">机考</a></li>
		 <li class="active"><a href="baseList" target="_self">考点列表</a></li>
	 </ul>
	<div class="tab-content"> 
	 <div class="box box-border">
	    <div class="box-body">
	      
	        <div class="row pad-t15">

	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">考试形式</label>
	              <div class="col-sm-9">
	                <select class="form-control select2" id="EXAM_TYPE" name="EXAM_TYPE" data-size="5" data-live-search="true">
	                  <option value="" selected="selected">请选择</option>
						<option value="8" <c:if test="${EXAM_TYPE=='8'}">selected='selected'</c:if>>笔试</option>
						<option value="11" <c:if test="${EXAM_TYPE=='11'}">selected='selected'</c:if>>机试</option>
	                </select>
	              </div>
	            </div>
	          </div>			          

	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">考点名称</label>
	              <div class="col-sm-9">
	                <input class="form-control" id="NAME" name="NAME" value="${param['NAME']}" />
	              </div>
	            </div>
	          </div>
	          
	          <div class="col-sm-4">
	          	<div class="form-group">
	          		<label class="control-label col-sm-3 text-nowrap">所在区域</label>
	          		<div class="col-sm-9">
	          			<div class="select-box"> <i class="select-ico"> <span class="caret"></span> </i>
	          				<div class="select-in">
	          					<ins data-seltype="Pro"> <span>省份</span>
	          						<input type="hidden" name="PROVINCE_NAME">
	          					</ins>
	          					<ins data-seltype="City"> <span>城市</span>
	          						<input type="hidden" name="CITY_NAME">
	          					</ins>
	          					<ins data-seltype="District"> <span>区/县</span>
	          						<input type="hidden" name="CITYNAME">
	          					</ins> 
	          				</div>
	          				<ul class="itemSelBox dropdown-menu" style="display: none;"></ul>
	          			</div>
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

		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

	<div class="box margin-bottom-none">
		<div class="box-header with-border">
			<div class="fr">
				<div class="btn-wrap fl">
					<shiro:hasPermission name="/exam/new/point/list$create">
						<a href="create" class="btn btn-default btn-sm"> <i class="fa fa-fw fa-plus"></i> 新增</a>
					</shiro:hasPermission>
				</div>
			</div>
		</div>
		<div class="box-body">
			<div class="table-responsive">
				<table class="table table-bordered table-striped vertical-mid text-center table-font">
					<thead>
		              <tr>
		              	<th>考试形式</th>
		                <th>考点名称</th>
		                <th>所在区域	</th>
						<th>详细地址</th>
						<th>适用范围</th>
		                <th>使用状态</th>
		                <th>操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
		            			<c:forEach items="${pageInfo.content }" var="entity">
		            				<tr>
		            					<td>
											<c:if test="${entity.EXAM_TYPE=='8'}">笔试</c:if>
											<c:if test="${entity.EXAM_TYPE=='11'}">机试</c:if>
		            					</td>
		            					<td>
				            				${entity.EXAM_POINT_NAME }<br>
				            				<span class="gray9">${entity.CODE }</span>
				            			</td>
				            			<td>
				            				${entity.PROVINCE_NAME }-${entity.CITY_NAME }-${entity.DISTRICT_NAME }
				            			</td>
				            			<td>
				            				${entity.ADDRESS }
				            			</td>
										<td>
											<c:choose>
												<c:when test="${fn:length(entity.gjtStudyCenters) == 0}">
													通用
												</c:when>
												<c:otherwise>
													<c:forEach items="${entity.gjtStudyCenters}" var="s">
														${s.scName}<br>
													</c:forEach>
												</c:otherwise>
											</c:choose>
										</td>
				            			<td> 
											<c:if test="${entity.IS_ENABLED eq 1}">在用 </c:if>
											<c:if test="${entity.IS_ENABLED eq 0}">停用 </c:if>
										</td>
										<td>
											<div class="data-operion"> 
												<shiro:hasPermission name="/exam/new/point/list$update">
												 	<a href="update/${entity.EXAM_POINT_ID}" class="operion-item operion-edit" title="编辑" data-original-title="编辑">
														<i class="fa fa-fw fa-edit"></i>
													</a>
												</shiro:hasPermission> 
												<shiro:hasPermission name="/exam/new/point/list$delete">
													<a href="javascript:void(0);" val="${entity.EXAM_POINT_ID}" class="operion-item operion-del del-one"><i class="fa fa-fw fa-trash-o text-red"  data-toggle="tooltip" title="删除"></i></a>
												</shiro:hasPermission> 
											</div>
											<!-- 
											<c:if test="${entity.IS_ENABLED eq 1 }">
												<a href="javascript:;" id="${ctx}/exam/new/room/examPointStatus?EXAM_POINT_ID=${entity.EXAM_POINT_ID}&IS_ENABLED=${entity.IS_ENABLED}" onclick="changeStatus($(this))" class="operion-item" data-toggle="tooltip" title="停用" data-original-title="停用">
                                                    <i class="fa fa-power-off"></i>
                                                </a>
											</c:if>
											<c:if test="${entity.IS_ENABLED eq 0 }">
												<a href="javascript:;" id="${ctx}/exam/new/room/examPointStatus?EXAM_POINT_ID=${entity.EXAM_POINT_ID}&IS_ENABLED=${entity.IS_ENABLED}" onclick="changeStatus($(this))" class="operion-item" data-toggle="tooltip" title="启用" data-original-title="启用">
                                                    <i class="fa fa-play-circle-o"></i>
                                                </a>
                                                <div class="data-operion"> 
												 	<a href="update/${entity.EXAM_POINT_ID}" class="operion-item operion-edit" title="编辑" data-original-title="编辑">
														<i class="fa fa-fw fa-edit"></i>
													</a> 
												</div>
												<a href="#" id="${entity.EXAM_POINT_ID}" class="operion-item" data-toggle="tooltip" title="删除" data-role="remove" data-content='<div class="margin_b10"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i><span class="inline-block vertical-mid">确定要删除该考场？</span></div><div class="f12 gray9 margin_b10">删除后将无法找回，请慎重选择</div>'>
                                                    <i class="fa fa-fw fa-trash-o text-red"></i>
                                                </a>
											</c:if>
											 -->
											
										</td>  
		            				</tr>
		            			</c:forEach>
		            		</c:when>
		            		<c:otherwise>
								<tr>
								 	<td align="center" colspan="12">暂无数据</td>
								</tr>
					 		</c:otherwise>
		            	</c:choose>
		            </tbody>
				</table>
				<tags:pagination page="${pageInfo}" paginationSize="5" />
			</div>
		</div>
	 </div>
	</div>
   </div>
  </form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<!-- 省市区联动查询 --> 
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/cityselect/jsonData.js"></script> 
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/cityselect/citySelect.js"></script>
<!-- AdminLTE App --> 
<script src="${css}/ouchgzee_com/platform/xllms_css/dist/js/app.js"></script>

<script type="text/javascript">

//filter tabs
$(".filter-tabs li").click(function(event) {
	if($(this).hasClass('actived')){
		$(this).removeClass('actived');
	}
	else{
		$(this).addClass('actived');
	}
});

//省市区联动查询
$(".select-box").citySelect();

$("body").confirmation({
	selector: "[data-role='remove']",
	html:true,
	placement:'top',
	content:'<div class="margin_b10"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i><span class="inline-block vertical-mid">确定要该考点？</span></div><div class="f12 gray9 margin_b10">删除后将无法找回，请慎重选择</div>',
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
			url:"${ctx}/exam/new/point/delExamPoint?EXAM_POINT_ID="+id,
			type:"post",
			dataType:"json",
			success:function(data){
				if(data!=null){
					  if(data.result == '1'){
						  window.location.href = "${ctx}/exam/new/point/list";
					  }else{
						  alert("删除考点失败！");
					  }
				  }
			}
		});
	
	},
	onCancel:function(event, element){
 
	}
});

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

//批量设置
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

//启用停用
function changeStatus(obj){
	var url = obj.attr("id");
	$.ajax({
		type: "POST",
		dataType: "json",
		url: url,
		success:function(data){
			 window.location.href = "${ctx}/exam/new/point/list";
		},
		complete: function (request,status) {

        }
	});
}

$(".btn-reset").click(function(){
	//重置除隐藏域的input 
	$("form").find(':text').val('');
	$("select[name='EXAM_TYPE']").each(function(){
		$("#select2-exam_batch_id-container").html("请选择");
		$(this).val('');
	});
});

</script>
</body>
</html>
