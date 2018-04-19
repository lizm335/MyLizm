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
<title>专业规则-列表查询</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>


</head>
<body class="inner-page-body"> 
	<section class="content-header">
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">基础信息</a></li>
			<li class="active">专业规则</li>
		</ol>
	</section>


		<section class="content" data-id="0">
			<form class="form-horizontal" id="listForm" action="list.html">
			<input type="hidden" name="search_EQ_type" value="${search_EQ_type}">
			<input type="hidden" name="search_EQ_status" value="${search_EQ_status}">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
								<label class="control-label col-sm-3">规则号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_ruleCode" 	value="${param.search_LIKE_ruleCode}">
								</div>
						</div>
						<div class="col-md-4">
								<label class="control-label col-sm-3">所属专业</label>
								<div class="col-sm-9">
									<select name="search_EQ_specialtyBaseId" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
										<option value="">请选择</option>
										<c:forEach items="${specialtyBaseMap}" var="map">
											<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_specialtyBaseId}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
						</div>
						<%-- <div class="col-md-4">
							<label class="control-label col-sm-3">专业类型</label>
							<div class="col-sm-9">
								<select name="search_EQ_specialtyCategory" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${zyxzMap}" var="map">
									<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_specialtyCategory}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
								</select>
							</div>
						</div> --%>
						<div class="col-md-4">
							<label class="control-label col-sm-3">专业层次</label>
							<div class="col-sm-9">
								<select name="search_EQ_pycc" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${pyccMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_pycc}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
							<label class="control-label col-sm-3">学科</label>
							<div class="col-sm-9">
								<select id="subject" name="search_EQ_subject" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${subjectMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_subject}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-4">
							<label class="control-label col-sm-3">学科门类</label>
							<div class="col-sm-9">
								<select id="category" name="search_EQ_category" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
									<option value="">请先选择学科</option>
									<c:forEach items="${categoryMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_category}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<%-- <div class="col-md-4">
							<label class="control-label col-sm-3">适用行业</label>
							<div class="col-sm-9">
								<select name="search_EQ_syhy" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${syhyMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_syhy}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div> --%>
						<%-- <div class="col-md-4">
							<label class="control-label col-sm-3">所属院校</label>
							<div class="col-sm-9">
								<select name="search_EQ_xxId" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${orgMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_xxId}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>	 --%>					
					</div>
				</div>
				<div class="box-footer">
					<div class="btn-wrap">
						<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
					</div>
					<div class="btn-wrap">
						<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
					</div>
				</div>
			</div>
	

	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	
	<div class="box">
		<div class="box-header with-border">
			<h3 class="box-title pad-t5">专业列表</h3>
			<div class="fr">			
				<!-- <div class="btn-wrap fl">
						<a href="javascript:void(0);" class="btn btn-block btn-danger btn-del del-checked">
							<i class="fa fa-fw fa-trash-o"></i> 删除
						</a>
				</div> -->
				<div class="btn-wrap fl">
					<c:if test="${isBtnCreate }">
					<a href="create?specialtyBaseId=${param.search_EQ_specialtyBaseId}" class="btn btn-default btn-sm">
							<i class="fa fa-fw fa-plus"></i> 新增专业规则</a>
					</c:if>
				</div>
			</div>
		</div>
		<div class="box-body">
			<div class="filter-tabs filter-tabs2 clearfix">
				<ul class="list-unstyled">
					<li lang=":input[name='search_EQ_type'], :input[name='search_EQ_status']" <c:if test="${empty param['search_EQ_type'] && empty param['search_EQ_status']}">class="actived"</c:if>>全部(${all})</li>
					<li value="1" role=":input[name='search_EQ_type']" lang=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_type'] == 1 }">class="actived"</c:if>>正式专业(${isEnabledNum})</li>
					<li value="2" role=":input[name='search_EQ_type']" lang=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_type'] == 2 }">class="actived"</c:if>>体验专业(${isNotEnabledNum})</li>
					<li value="1" role=":input[name='search_EQ_status']" lang=":input[name='search_EQ_type']" <c:if test="${param['search_EQ_status'] == 1 }">class="actived"</c:if>>编辑中(${edit})</li>
					<li value="2" role=":input[name='search_EQ_status']" lang=":input[name='search_EQ_type']" <c:if test="${param['search_EQ_status'] == 2 }">class="actived"</c:if>>未发布(${notPublish})</li>
					<li value="3" role=":input[name='search_EQ_status']" lang=":input[name='search_EQ_type']" <c:if test="${param['search_EQ_status'] == 3 }">class="actived"</c:if>>已发布(${publish})</li>
					<li value="4" role=":input[name='search_EQ_status']" lang=":input[name='search_EQ_type']" <c:if test="${param['search_EQ_status'] == 4 }">class="actived"</c:if>>已停用(${stop})</li>
				</ul>
			</div>
			<div class="table-responsive">
				<table id="dtable"
					class="table table-bordered table-striped vertical-mid text-center table-font">
					<thead>
						<tr>
							<!-- <th><input type="checkbox" class="select-all" 	id="selectAll"></th> -->
							<th>专业规则号</th>
							<th>专业代码</th>
							<th>专业名称</th>
							<th>专业属性</th>
							<!-- <th>总学分</th> -->
							<th>最低毕业学分</th>
							<!-- <th>所属院校</th> -->
							<th>课程总数</th>
							<th>专业性质</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${not empty pageInfo.content}">
						<c:forEach items="${pageInfo.getContent() }" var="item">
							<tr>
								<%-- <td><input type="checkbox" value="${item.specialtyId }"	name="ids" class="checkbox"></td> --%>
								<td>${item.ruleCode}</td>
								<td>${item.gjtSpecialtyBase.specialtyCode}</td>
								<td>
									<span data-role="major-name">${item.gjtSpecialtyBase.specialtyName}</span><br>
									<%-- <c:if test="${not empty item.zyh}">
										<span class="gray9">${item.zyh}</span>
									</c:if> --%>
									<input type="hidden" name="specialtyId" value="${item.specialtyId}">
								</td>
								<td style="text-align: left;">
									<%-- 专业类型：${zyxzMap[item.specialtyCategory]}<br> --%>

									专业层次：${pyccMap[item.pycc]}<br>
									
									学科：${subjectMap[item.subject]}<br>
												
									学科门类：${subjectCategoryMap[item.subject][item.category]}<br>
									
									<%-- 适用行业：${syhyMap[item.syhy]} --%>
								</td>										
								<%-- <td>${item.zxf}</td> --%>
								<td>${item.zdbyxf}</td>
								<%-- <td>${item.gjtSchoolInfo.xxmc}</td> --%>
								<td>${fn:length(item.gjtSpecialtyPlans)}</td>
								<td>
									<c:choose>
										<c:when test="${item.type == 1}">正式专业</c:when>
										<c:when test="${item.type == 2}">体验专业</c:when>
										<c:otherwise>--</c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:choose>
										<c:when test="${item.status == 1}"><span class="gray9">编辑中</span></c:when>
										<c:when test="${item.status == 2}"><span class="text-orange">未发布</span></c:when>
										<c:when test="${item.status == 3}"><span class="text-green">已发布</span></c:when>
										<c:when test="${item.status == 4}"><span class="text-red">已停用</span></c:when>
									</c:choose>
								</td>
								<td>
									<div class="data-operion">
									<c:if test="${user.gjtOrg.id == item.gjtOrg.id }">
										<c:if test="${item.status != 3 && isBtnUpdate}">
											<a href="update/${item.specialtyId}"
												class="operion-item operion-edit" title="编辑">
												<i class="fa fa-fw fa-edit"></i></a> 
										</c:if>
										<c:if test="${item.status != 3 && item.status != 4 && isBtnDelete}">		
											<a href="javascript:void(0);"
												class="operion-item operion-del del-one" val="${item.specialtyId}"
												title="删除" data-tempTitle="删除">
												<i class="fa fa-fw fa-trash-o"></i></a>
										</c:if>
										<%-- <c:if test="${item.status == 2 || item.status == 4}">
											<a href="#" class="operion-item" data-toggle="tooltip" title="发布专业" data-role='issue'><i class="fa fa-article-manage"></i></a>
										</c:if> --%>
										<c:if test="${item.status == 3 && isBtnStop}">
											<a href="#" class="operion-item" data-toggle="tooltip" title="停用" data-role='stop'><i class="fa fa-minus-circle"></i></a>
										</c:if>
										<c:if test="${item.status == 3 && isBtnCopy}">
											<a href="#" class="operion-item" data-toggle="tooltip" title="复制" data-role='copy'><i class="fa fa-copy"></i></a>
										</c:if>
										<c:if test="${isBtnView}">
											<a href="view/${item.specialtyId}" 
												class="operion-item" title="查看详情">
												<i class="fa fa-fw fa-view-more"></i></a>
										</c:if>	
										<%-- <a href="share/${item.specialtyId}"
											class="operion-item" title="分享"
											data-toggle="tooltip" title="共享专业" data-role='share'>
											<i class="fa fa-share-alt"></i></a> 	 --%>												
										 <%-- <a href="plan/${item.specialtyId}" 
											class="operion-item" 
											 title="设置专业规则" data-tempTitle="设置专业规则">
											<i class="fa fa-gear"></i><!--设置专业教学计划 --></a> --%>
										</c:if>	
									</div>
								</td>
							</tr>
						</c:forEach> 
						</c:when>
										<c:otherwise>
											<tr>
												<td align="center" colspan="10">暂无数据</td>
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
	
	<div id="copyModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	    <div class="modal-dialog">
	        <form id="copyForm" name="copyForm" action="${ctx}/edumanage/specialty/copy" method="post">
	            <input type="hidden" id="specialtyId" name="specialtyId" value="">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
	                    <h4 class="modal-title">复制专业</h4>
	                </div>
	                <div class="modal-body">
	                
	                	<div>
	                		<p style="margin-left: 20px;">
	                		将复制专业<span id="specialtyName" class="text-red"></span>，请填写新的专业规则号：
		                    </p>    
							<div class="col-sm-9">
								<input type="text" id="ruleCode" name="ruleCode" value="">
							</div>
							
						</div>
	                </div>
	                <br>
	                <br>
	                <br>
	                <div class="modal-footer">
	                    <button type="button" class="btn btn-default pull-left" data-dismiss="modal">取消</button>
	                    <button type="button" class="btn btn-primary" id="sure">确认复制</button>
	                </div>
	            </div><!-- /.modal-content -->
	        </form>
	    </div><!-- /.modal-dialog -->
	</div>
	
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	
</body>

<script type="text/template" id="temp1">
<div class="alert (0) fade in tips-box" role="alert">   
	<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
	<div class="no-margin f16"><i class="icon fa (2)"></i> (1)</div>
</div>
</script>
<script>
$("[data-role='share']").click(function(event) {
	event.preventDefault();
	$.mydialog({
	  id:'import',
	  width:600,
	  height:415,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')
	});
});

//发布专业
$("html").confirmation({
  selector: "[data-role='issue']",
  html:true,
  placement:'top',
  content:'<div class="margin_b10 text-center"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>确定发布该专业？</div><div class="f12 gray9 margin_b10 text-center">专业：{0}<div class="margin_t5">发布专业后不能修改和删除专业</div></div>',
  title:false,
  btnOkClass    : 'btn btn-xs btn-primary',
  btnOkLabel    : '确认',
  btnOkIcon     : '',
  btnCancelClass  : 'btn btn-xs btn-default margin_l10',
  btnCancelLabel  : '取消',
  btnCancelIcon   : '',
  popContentWidth:250,
  onShow:function(event,element){
    var $el=$(element);
    if($el.is('[data-role="issue"]')){
    	this.content=this.content.format($el.closest('tr').find('[data-role="major-name"]').text());
    }
  },
  onConfirm:function(event,element){
	  var id = $(element).closest('tr').find('input[name="specialtyId"]').val()
	  window.location.href = ctx + "/edumanage/specialty/publish?id=" + id;
  },
  onCancel:function(event, element){
    
  }
});

//停用专业
$(".data-operion").confirmation({
	selector: "[data-role='stop']",
	html:true,
	placement:'top',
	content:'<div class="margin_b10 text-center"><i class="fa fa-fw fa-exclamation-circle text-red vertical-mid" style="font-size:24px;"></i>确定停用该专业？</div><div class="f12 gray9 margin_b10 text-center">专业：{0}<div class="margin_t5">停用专业后可重新编辑发布</div></div>',
	title:false,
	btnOkClass    : 'btn btn-xs btn-primary',
	btnOkLabel    : '确认',
	btnOkIcon     : '',
	btnCancelClass  : 'btn btn-xs btn-default margin_l10',
	btnCancelLabel  : '取消',
	btnCancelIcon   : '',
	popContentWidth:250,
	onShow:function(event,element){
	  var $el=$(element);
	  if($el.is('[data-role="stop"]')){
	  	this.content=this.content.format($el.closest('tr').find('[data-role="major-name"]').text());
	  }
	},
	onConfirm:function(event,element){
		  var id = $(element).closest('tr').find('input[name="specialtyId"]').val()
		  window.location.href = ctx + "/edumanage/specialty/stop?id=" + id;
	},
	onCancel:function(event, element){
  
	}
});

$("#subject").change(function(){
	var $category = $("#category");
	$category.empty();
	$category.append('<option value="">请选择</option>');
	var value = $(this).children('option:selected').val();
	if (value != "") {
		$.getJSON(ctx+'/edumanage/course/changeSubject', {subject:value}, function (data) {
			$.each(data.obj, function(i,item){
            	if (item.code == '${entity.category}') {
            		$category.append('<option value="'+item.code+'" selected="selected">'+item.name+'</option>');
            	} else {
            		$category.append('<option value="'+item.code+'">'+item.name+'</option>');
            	}
            });
			$category.selectpicker('refresh');
        });
	}
});

$("[data-role='copy']").click(function(event) {
	event.preventDefault();
	var id = $(this).closest('tr').find('input[name="specialtyId"]').val();
	var name = $(this).closest('tr').find('[data-role="major-name"]').text();
	$('#specialtyId').val(id);
	$('#specialtyName').text(name);
	$('#ruleCode').val("");
	$('#copyModal').modal('show');
});

$("#sure").click(function(event) {
	var ruleCode = $('#ruleCode').val();
	if(!ruleCode) {
		alert('请填写专业规则号');
		return;
	}
	var id = $('#specialtyId').val();
	$.post("copy",{specialtyId:id, ruleCode:ruleCode},function(data){
   		if(data.successful){
   			window.location.reload();
   		}else{
   			alert(data.message);
   		}
   },"json");
});

</script>
</html>