<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>定制课程</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<style type="text/css">
html,body{
	overflow: hidden;
	height: 100%;
}
</style>
</head>
<body>

<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">上传成绩</h3>
		<input type="hidden" id="customCourseId" name="customCourseId" value="${customCourse.customCourseId}" />
	</div>
	<div class="box-body scroll-box">
		<%-- <div class="clearfix margin_b10">
			<form id="excelFileForm" class="form-horizontal" action="" method="post">
			<div class="col-md-8 col-sm-10">
			<input type="hidden" id="customCourseId" name="customCourseId" value="${customCourse.customCourseId}" />
			<input type="hidden" id="fileUrl" name="fileUrl" >
			</div>
        	</form>
        	<c:if test="${customCourse.scoreStatus != 2 }">
				<a href="/edumanage/custom/course/upload?type=score&id=${customCourse.customCourseId }" data-role="import" 
					 class="btn btn-primary btn-sm pull-right margin_l10">上传成绩</a>
			</c:if>
			<div class="margin_t5 oh">
        		<h3 class="cnt-box-title f14 text-bold">${teachPlan.gjtReplaceCourse.kcmc }</h3>
        	</div>
      	</div> --%>
      	<%-- <table class="table-gray-th text-center f12">
          <thead>
            <tr>
              <th>学生姓名</th>
              <th>正式学号</th>
              <th>平时成绩</th>
              <th>期末成绩</th>
            </tr>
          </thead>
          <tbody>
	      	<c:if test="${not empty customCourse.scores }">
          	<c:forEach items="${customCourse.scores }" var="score">
            <tr>
              <td>${score.studentName }</td>
              <td>${score.studentNo }</td>
              <td>${score.usualScore }</td>
              <td>${score.terminalScore }</td>
            </tr>
            </c:forEach>
			</c:if>
          </tbody>
        </table> --%>
		
      	<div class="clearfix margin_t10">
			<c:if test="${customCourse.scoreStatus != 2 }">
				<button class="btn btn-primary btn-sm pull-right margin_l10 btn-add-img-addon">添加扫描件</button>
			</c:if>
			<div class="margin_t5 oh">
        		<h3 class="cnt-box-title f14 text-bold">成绩扫描件</h3>
        	</div>
      	</div>
		<form id="imageForm" class="form-horizontal"  method="post">
		<ul class="list-inline img-gallery img-gallery-md upload-box" style="margin-right:-5px;">
			<c:forEach items="${customCourse.images }" var="image">
				<li>
					<img src="${image }">
					<span class="glyphicon glyphicon-remove-sign" data-toggle="tooltip" data-html="true" title="" data-original-title="<div style='width:40px;'>删除</div>"></span>
					<a href="#" class="img-fancybox f12" data-large-img="${image }">点击放大</a>
				</li>
			</c:forEach>
		</ul>
		</form>
	</div>
	
</div>
<c:if test="${customCourse.scoreStatus != 2 }">
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="export">提交</button>
</div>
</c:if>
<jsp:include page="/eefileupload/upload.jsp" />
<script type="text/javascript">

$('body').on('click','.btn-add-img-addon',function() {
	var $container=$(this).parent().next().find('.upload-box');
	var formControlName = 'images';
	uploadImageNew($container, formControlName);
});

function uploadScore() {
	uploadFile('fileName','fileUrl','xls',null,function(){
		$.ajax({
			url:"/edumanage/custom/course/uploadScore",
			async:false,
			data:$("#excelFileForm").serialize(),
			dataType:"text",
			type:"post",
			success:function(data, textStatus, jqXHR) {
				alert("上传成功！");
				location.reload();
			},
			error:function(XMLHttpRequest, textStatus, errorThrown) {
				alert("上传失败！");
			}
		});
		
	});
}
//上传授课凭证
$("[data-role='import']").click(function(event) {
    event.preventDefault();
    $.mydialog({
        id:'import',
        width:600,
        height:415,
        zIndex:11000,
        content: 'url:'+$(this).attr("href")
    });
});
////////////////////////////////////////////////////////////////
$(".box-body").height($(window).height()-126);
//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

//图片放大
;(function(){
	//加载图片放大的相关皮肤
	$('<link/>',{
		rel:"stylesheet",
		type:"text/css",
		href:'http://css.gzedu.com/common/js/fancybox/jquery.fancybox.css',
		'data-id':'require-css'
	}).appendTo($('head'));
	$.getScript('http://css.gzedu.com/common/js/fancybox/jquery.fancybox.pack.js',function(){

		$(".img-gallery").on('click', '.img-fancybox', function(event) {
			event.preventDefault();
			var $img=$(this).closest('.img-gallery').find('.img-fancybox');
	        var index=$img.index(this);
	        var imgUrls=$.map($img, function(item, index) {
	          return $(item).attr('data-large-img');
	        });

	        $.fancybox(imgUrls, {
	          'transitionIn'    : 'none',
	          'transitionOut'   : 'none',
	          'type'            : 'image',
	          'index'           : index,
	          'scrollOutside'	:false,
	          'changeFade'      : 0,
	          'loop'			:false,
	          beforeShow		:function(){
            	var $p=this.wrap.parent();
            	if(!$p.is('body')){
            		$p.css('overflow', 'hidden');
            	}
	          }
	        });
		});
	});
})();

/*删除图片*/
$(".img-gallery").on('click', '.glyphicon-remove-sign', function(event) {
  event.preventDefault();
  $(this).closest('li').remove();
})

//确定
$('[data-role="export"]').click(function(event) {
	var postIngIframe=$.formOperTipsDialog({
		text:'数据提交中...',
		iconClass:'fa-refresh fa-spin'
	});
	if($.find('input[name=images]') != undefined && $.find('input[name=images]') instanceof Array 
			&& $.find('input[name=images]').length > 0) {
		$.ajax({
			url:"/edumanage/custom/course/uploadScoreImage",
			async:false,
			data:$("#imageForm").serialize()+"&customCourseId="+$("#customCourseId").val(),
			dataType:"text",
			type:"post",
			success:function(data, textStatus, jqXHR) {
				successTip(postIngIframe);
			},
			error:function(XMLHttpRequest, textStatus, errorThrown) {
				errorTip(postIngIframe);
			}
		});
	} else {
		successTip(postIngIframe);
	}
	
});


function errorTip(postIngIframe) {
	setTimeout(function(){
		$.closeDialog(postIngIframe);
		/*失败状态*/
		$.when(
			$.resultDialog(
				{
					type:2,
					msg:'操作失败！',
					timer:1500,
					width:150,
					height:50
				}
			)
		)
		.done(function(){
            parent.$.closeDialog(frameElement.api);
            parent.location.reload();
        });
	},2000);
}

function successTip(postIngIframe) {
	setTimeout(function(){
		$.closeDialog(postIngIframe);
		/*成功状态*/
		$.when(
			$.resultDialog(
				{
					type:1,
					msg:'操作成功！',
					timer:1500,
					width:150,
					height:50
				}
			)
		)
		.done(function(){
            parent.$.closeDialog(frameElement.api);
            parent.location.reload();
        });
		
		/*服务器繁忙状态
		$.resultDialog(
			{
				type:0,
				msg:'服务器繁忙，请稍后再试！',
				timer:1500,
				width:265,
				height:50
			}
		);
		*/
	},2000);
}
</script>
</body>
</html>
