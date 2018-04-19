<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<title>我发布的通知公告</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
	
</head>
<body class="inner-page-body">
<section class="content-header">
	<h1>
		通知公告
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
		<li class="active">通知公告</li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

	
		
	<div class="nav-tabs-custom reset-nav-tabs-custom" id="notice-tabs" style="margin-bottom: 0px;">
		<ul class="nav nav-tabs nav-tabs-lg">
			<li><a href="${ctx}/home/class/message/list">我接收(${tmCount})</a></li>
			<li class="active"><a href="${ctx}/home/class/message/publishList" data-toggle="tab">我发布(${pmCount})</a></li>
			<li class="pull-right margin_r10 margin-bottom-none margin_t5">
				<button type="button" class="btn btn-default btn-add" onclick="window.location.href='${ctx}/home/class/message/create'"><i class="fa fa-fw fa-plus"></i> 新增通知</button>
			</li>
		</ul>
		<div class="tab-content">
			<form class="form-horizontal" id="listForm">
				<div class="box box-border no-shadow">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-sm-5">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">通知标题</label>
									<div class="col-sm-9">
										<input type="text" name="search_LIKE_infoTheme" value="${param.search_LIKE_infoTheme}" class="form-control">
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="control-label col-sm-2 text-nowrap">发布时间</label>
									<div class="col-sm-9">
										<div class="input-group">
											<input type="text" name="search_GTE_createdDt" value="${param.search_GTE_createdDt}" class="form-control pull-right reservation">
											<div class="input-group-addon no-border">
												至
											</div>
											<input type="text" name="search_LTE_createdDt" value="${param.search_LTE_createdDt}" class="form-control pull-right reservation">
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-5">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">公告类型</label>
									<div class="col-sm-9">
										<select name="search_EQ_infoType" class="form-control select2">
											<option value="">请选择</option>
											<option value="11" <c:if test="${param.search_EQ_infoType==11}">selected</c:if>>班级公告</option>
											<option value="12" <c:if test="${param.search_EQ_infoType==12}">selected</c:if>>考试通知</option>
											<option value="13" <c:if test="${param.search_EQ_infoType==13}">selected</c:if>>学习提醒</option>
										</select>
									</div>
								</div>
							</div>
						</div>
					</div><!-- /.box-body -->
					<div class="box-footer">
						<div class="pull-right"><button type="reset" class="btn min-width-90px btn-default">重置</button></div>
						<div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
					</div><!-- /.box-footer-->
				</div>
			</form>
			<div class="box box-border no-shadow margin-bottom-none">
				<div class="box-body">
					<ul class="list-unstyled news-list">
						<c:choose>
							<c:when test="${not empty infos && infos.numberOfElements > 0}">
								<c:forEach items="${infos.content}" var="info">
									<c:if test="${not empty info}">
										<li class="news-item">
											<p class="news-tit"><a href="view/${info.messageId}?source=2">${info.isStick==true?'【置顶】':'' }【${infoTypeMap[info.infoType]}】${info.infoTheme}</a></p>
											<p class="news-summary">${info.infoContent}</p>
											<div class="news-other">
												<span class="fr">
													<a href="update/${info.messageId }" class="operion-item operion-edit" title="编辑"> <i class="fa fa-fw fa-edit"></i></a>
													<a href="view/${info.messageId}?source=2" class="operion-item" data-toggle="tooltip" title="" data-original-title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
													<a href="#" val="${info.messageId }" class="operion-item operion-del text-red" data-toggle="tooltip" title="" data-content="<div class='gray margin_b10'>确认删除该通知公告？</div>" data-original-title="删除"><i class="fa fa-fw fa-trash-o"></i></a>
												</span>
												<cite>${info.colReadUserNum}/${info.colReceiveUserNum}</cite>
												<time><fmt:formatDate value="${info.createdDt}" type="both" /></time>
											</div>
										</li>
									</c:if>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<li class="news-item" style="text-align: center;">暂无数据</li>
							</c:otherwise>
						</c:choose>
					</ul>

					<div style="padding: 10px 0 10px 0;">
						<tags:pagination page="${infos}" paginationSize="10" />
					</div>
				</div>
			</div>
		</div>
	</div><!-- nav-tabs-custom -->
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>
<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

<script type="text/javascript">

	$('.select2').select2();

	var objId;
	// 删除通知公告
	$('.news-list').confirmation({
		selector: ".operion-del",
		html:true,
		placement:'top',
		content:'',
		title:'<span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span><span class="f12">确认删除该通知公告？</span>',
		btnOkClass    : 'btn btn-xs btn-primary',
		btnOkLabel    : '确认',
		btnOkIcon     : '',
		btnCancelClass  : 'btn btn-xs btn-default margin_l10',
		btnCancelLabel  : '取消',
		btnCancelIcon   : '',
		popContentWidth : 180,
		onShow:function(event,element){
			if($(element).attr('val') != null) {
				objId = $(element).attr('val');
			}
		},
		onConfirm:function(event,element){
			if(objId != null) {
				var id = objId;
				objId = null;
				$.post("delete",{ids:id},function(data){
					if(data.successful){
						showSueccess(data);
						setTimeout(function(){
							window.location.href=window.location.href;
				          },2500);
					}else{
						$.alert({
							title: '失败',
							icon: 'fa fa-exclamation-circle',
							confirmButtonClass: 'btn-primary',
							content: '数据删除失败！',
							confirmButton: '确认',
							confirm:function(){
								showFail(data);
							}
						});
					}
				},"json");
			}
		},
		onCancel:function(event, element){
		}
	});
</script>
</body>
</html>
