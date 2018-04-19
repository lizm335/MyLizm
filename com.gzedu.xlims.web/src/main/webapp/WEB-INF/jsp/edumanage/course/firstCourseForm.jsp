<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业规则</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<style type="text/css">
.img-list li {float: none;margin-bottom: 5px;width:150px}
.img-list li img{height:auto;}
</style>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#">
					<i class="fa fa-home"></i> 首页
				</a>
			</li>
			<li>
				<a href="#">教学管理</a>
			</li>
			<li>
				<a href="#">教学指引</a>
			</li>
			<li class="active">新增开学第一堂课</li>
		</ol>
	</section>
	<section class="content">
		<form id="theform">
			<div class="box margin-bottom-none">
				<div class="box-header with-border">
					<h3 class="box-title">新增专业</h3>
				</div>
				<div class="box-body">
					<input type="hidden" id="videoId" value="${item.id}"/>
					<table class="table-gray-th">
						<tr>
							<th width="20%" class="text-right">标题</th>
							<td>
								<div class="position-relative">
									<input class="form-control" type="text" id="title" value="${item.title}"  datatype="*" nullmsg="请填写标题" errormsg="请填写标题">
								</div>
							</td>
						</tr>
						<tr>
							<th width="20%" class="text-right">内容说明</th>
							<td>
								<div class="position-relative">
									<textarea class="form-control" id="content" rows="6" datatype="*" nullmsg="请填写内容说明" errormsg="请填内容说明">${item.content}</textarea>
								</div>
							</td>
						</tr>
						<tr>
							<th width="20%" class="text-right">视频</th>
							<td>
								<div class="position-relative" style="min-height: 100px;">
									<div class="clearfix pad5 no-pad-top row-offset-3px">
										<div class="inline-block" data-role="video-box">
											<c:forEach items="${item.gjtSpecialtyVideoList}" var="v">
												<ul class="img-list clearfix inline-block vertical-middle">
													<li>
														<c:if test="${empty(v.coverUrl)}">
															<img alt="视频图片" src="${ctx }/static/dist/img/images/video-img.jpg" class="user-image coverUrl" />
														</c:if>
														<c:if test="${not empty(v.coverUrl)}">
															<img alt="视频图片" src="${item.coverUrl }" class="user-image coverUrl" />
														</c:if>
													</li>
													<li>
														<div class="position-relative">
															<input class="form-control title" type="text" value="${v.title}"  datatype="*" nullmsg="请填写标题" errormsg="请填写标题">
															<input type="hidden" class="videoUrl" value="${v.videoUrl}" />
															<input type="hidden" class="videoId" value="${v.videoId}" />
														</div>
														<a href="#" class="operion-item operion-view"  data-toggle="tooltip" title="删除" data-role="del-btn" data-original-title="删除">
															<i class="fa fa-trash-o text-red"></i>
														</a>
													</li>
												</ul>
											</c:forEach>
										</div>
										<div class="inline-block add-img margin_r10" role="button" onclick="uploadFile('videlName','videlPath','mp4|avi|mpg',100,uploadCallback);"><i class="fa fa-fw fa-plus"></i>上传</div>
										<input type="hidden" id="videlPath" />
										<input type="hidden" id="videlName" />
									</div>
									<div class="tooltip top" id="videoTip" role="tooltip" style="bottom: 100%; margin-bottom: -5px;"><div class="tooltip-arrow"></div><div class="tooltip-inner">请添加视频</div></div>
								</div>
							</td>
						</tr>
						<tr>
							<th width="15%" class="text-right">专业名称

								<button type="button" data-toggle="tooltip" title="添加专业" class="btn btn-default btn-xs pull-right bg-white margin_l10" data-role="add-major">
									<i class="fa fa-fw fa-plus"></i>
								</button>

							</th>
							<td width="35%">
								<div class="box-border position-relative" style="min-height: 34px;">
									<div class="clearfix pad5 no-pad-top row-offset-3px" data-id="major-box" >
										<c:forEach items="${item.gjtSpecialtyBaseList}" var="s">
											<div class="alert col-sm-6 pad-l5 pad-r5 no-pad-top no-pad-bottom margin_t5 fade in margin-bottom-none">
											  <div class="box no-border bg-light-blue no-margin pad">
											      <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
											      <div class="text-center oh text-overflow text-nowrap" data-toggle="tooltip" title="${s.specialtyName}" data-container="body">${s.specialtyName} (${s.specialtyCode })</div>
												  <input type="hidden" class="sid" value="${s.specialtyBaseId}" />
											  </div>
											</div>
										</c:forEach>
										<div id="allSpecialty" style="height:30px;line-height:30px;color:#aaa;margin-top:5px">通用</div>
									</div>
									<div class="tooltip top" id="scpeicaltyTip" role="tooltip" style="bottom: 100%; margin-bottom: -5px;"><div class="tooltip-arrow"></div><div class="tooltip-inner">请添加专业</div></div>
								</div>
							</td>
						</tr>
					</table>
				</div>
				<div class="box-footer text-right">
					<button type="button" class="btn btn-default min-width-90px margin_r10">取消</button>
					<button type="submit" class="btn btn-primary min-width-90px" data-role="sure">保存</button>
				</div>
			</div>
		</form>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<jsp:include page="/eefileupload/upload.jsp"/>
	<script type="text/javascript">
//选择专业
$('[data-role="add-major"]').click(function(event) {
  $.mydialog({
    id:'add-major',
    width:800,
    height:550,
    zIndex:9999,
    content: 'url:${ctx}/edumanage/course/chooseSpecialty'
  });
});

//选择专业
$('[data-role="copy"]').click(function(event) {
  $.mydialog({
    id:'copy',
    width:700,
    height:600,
    zIndex:9999,
    content: 'url:${ctx}/graduation/degreeCollege/copyDegree?collegeId=${param.collegeId}'
  });
});

var uploadCallback =function(){
    var html=$('#videoTemplate').html().format($('#videlPath').val());
	$('[data-role="video-box"]').append(html);
    /* $.get('${ctx}/edumanage/course/queryVideoImage',{
		'videoUrl':$('#videlPath').val()
	},function(data){
		if(data){
		    $('[data-id="video-box] img:last').attr('src',data);
		}
	},'text'); */
}

$('body').on('click','[data-role="del-btn"]',function(){
    $(this).closest('ul').remove();
});





	//表单验证
	;
	(function() {
	    var $theform = $("#theform");
	    var htmlTemp = '<div class="tooltip top" role="tooltip">' + '<div class="tooltip-arrow"></div>' + '<div class="tooltip-inner"></div>' + '</div>';
	    $theform.find(":input[datatype]").each(function(index, el) {
		$(this).closest('.position-relative').append(htmlTemp);
	    });

	    $.Tipmsg.r = '';
	    var postForm = $theform.Validform({
			ignoreHidden : true,
			showAllError : false,
			ajaxPost : true,
			tiptype : function(msg, o, cssctl) {
			    if (!o.obj.is("form")) {
				var msgBox = o.obj.closest('.position-relative').find('.tooltip');
	
				msgBox.css({
				    bottom : "100%",
				    'margin-bottom' : -5
				})
				msgBox.children('.tooltip-inner').text(msg);
	
				switch (o.type) {
				case 3:
				    msgBox.addClass('in');
				    break;
				default:
				    msgBox.removeClass('in');
				    break;
				}
			    }
			},
			beforeSubmit : function(curform) {
				if($('[data-role="video-box"] ul').length==0){
				    $('#videoTip').addClass('in');
					return false;
				}
				
			   
			    var postIngIframe = $.formOperTipsDialog({
					text : '数据提交中...',
					iconClass : 'fa-refresh fa-spin'
			    });
			    var specialtyIds = [];
			    $('[data-id="major-box"] .sid').each(function() {
					specialtyIds.push($(this).val());
			    });
				var videos=[];
				$('[data-role="video-box"] ul').each(function(){
					var video={
						id:$(this).find('.videoId').val(),
						title:$(this).find('.title').val(),
						coverUrl:$(this).find('.coverUrl').attr('src'),
						videoUrl:$(this).find('.videoUrl').val()
					}
					videos.push(JSON.stringify(video))
				});
			   
			    $.post('${ctx}/edumanage/course/saveFirstCourse', {
				    id:$.trim($('#videoId').val()),
					title : $('#title').val(),
					content:$('#content').val(),
					specialtyIds : specialtyIds,
					videos:videos
			    }, function(data) {
					if(data.successful){
					    postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
						postIngIframe.find('[data-role="tips-icon"]').attr('class', 'fa fa-check-circle');
						location.href='${ctx}/edumanage/course/queryFirstCoursesList';
					}else{
						alert(data.message);
					    $.closeDialog(postIngIframe);
					}
			    }, 'json');
			    return false;
			}
	    });
	})();
    </script>
 	<script type="text/tpmplate" id="videoTemplate">
	<ul class="img-list clearfix inline-block vertical-middle">
		<li>
			<img alt="视频图片" src="${ctx}/static/dist/img/images/video-img.jpg" class="user-image coverUrl" />
		</li>
		<li>
			<div class="position-relative">
				<input class="form-control title" type="text" placeholder="请输入视频标题"  datatype="*" nullmsg="请填写标题" errormsg="请填写标题">
				<input type="hidden" class="videoUrl" value="{1}" />
			</div>
			<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="del-btn" data-original-title="删除">
				<i class="fa fa-trash-o text-red"></i>
			</a>
		</li>
	</ul>
	</script>
</body>
</html>