<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<title>班主任平台 - 督促提醒</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
	<h1>
		督促提醒
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
		<li class="active">督促提醒</li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

	
	<div class="nav-tabs-custom reset-nav-tabs-custom margin-bottom-none" id="notice-tabs">
		<div class="pull-right margin_r10 margin_t10">
			<a role="button" class="btn btn-default btn-add" href="${ctx}/home/class/remind/create"><i class="fa fa-fw fa-plus"></i> 新增提醒</a>
		</div>
		<ul class="nav nav-tabs nav-tabs-lg">
			<li class="active"><a href="${ctx}/home/class/remind/eeList" data-toggle="tab">EE提醒</a></li>
			<li><a href="${ctx}/home/class/remind/smsList">手机短信</a></li>
			
		</ul>
		<div class="tab-content">
			<form class="form-horizontal">
				<div class="box box-border no-shadow">
					<div class="box-body">
						<div class="row pad-t15">
							<div class="col-sm-5">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">标题</label>
									<div class="col-sm-9">
										<input type="text" name="search_LIKE_infoContent" value="${param.search_LIKE_infoContent}" class="form-control">
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
											<div class="media">
												<div class="media-body">
													<p class="news-tit">
														<a href="view/${info.messageId}?source=1">
																${info.infoContent}
														</a>
													</p>
													<div class="news-other">
														我 <fmt:formatDate value="${info.createdDt}" type="date" /> 发送
													</div>
													<div class="clearfix gray9 f12 more-per-wrap">
														<div class="pull-left">接收人：</div>
														<div class="oh">
															<span class="stu_container" data-list="${info.memo}"></span>
															  <span class="omit_txt">
																  等<em></em>人
															  </span>
															<a href="javascript:void(0)" class="extra_oper">展开&gt;</a>
														</div>
													</div>
												</div>
												<div class="media-right media-middle min-width-90px text-right">
													<a href="javascript:void(0);" val="${info.messageId}" class="operion-item operion-resend"><i class="fa fa-fw fa-resend" data-toggle="tooltip" title="重新发送"></i></a>
													<a href="javascript:void(0);" val="${info.messageId}" class="operion-item operion-del del-one"><i class="fa fa-fw fa-trash-o text-red"  data-toggle="tooltip" title="删除"></i></a>
												</div>
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
	var objId;
	// 重新发送
	$('body').confirmation({
		selector: ".operion-resend",
		html:true,
		placement:'top',
		content:'',
		title:'<span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span><span class="f12">确认重新发送？</span>',
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

				$.post("resend",{id:id, isSendEE:'1'},function(data){
					if(data.successful){
						showSueccess(data);

					}else{
						$.alert({
							title: '失败',
							icon: 'fa fa-exclamation-circle',
							confirmButtonClass: 'btn-primary',
							content: '发送失败！',
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

	// 删除督促提醒
	$('.news-list').confirmation({
		selector: ".operion-del",
		html:true,
		placement:'top',
		content:'',
		title:'<span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span><span class="f12">确认删除该信息？</span>',
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

						$(element).closest('.news-item').remove();
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

	//“接收人”操作
	$(".stu_container").each(function(index,ele){
		var container=$(this),
				stu_list=(container.attr("data-list")).split(",");
		if(stu_list.length>0){
			var strPrev3=stu_list.slice(0,3).join("、");
			container.html(strPrev3);
			container.siblings(".omit_txt").children("em").html(stu_list.length);

			if(stu_list.length<=3){
				container.siblings().hide();
			}
			else{
				container.siblings(".extra_oper").click(function(event) {
					if(!$(this).hasClass('on')){
						container.html(stu_list.join("、"));
						$(this).html("收起");
						$(this).addClass('on');
						$(this).siblings(".omit_txt").hide();
					}
					else{
						container.html(strPrev3);
						$(this).html("展开>");
						$(this).removeClass('on');
						$(this).siblings(".omit_txt").show();
					}
				});
			}
		}
	});
</script>
</body>
</html>
