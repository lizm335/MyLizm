<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<!-- <!DOCTYPE html> -->
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>定制课程</title>
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
		<h3 class="box-title">授课凭证</h3>
	</div>
	<div class="box-body scroll-box">
		<div class="clearfix margin_b10">
			<div class="margin_t5 oh">
        		<h3 class="cnt-box-title f14 text-bold">${plan.courseTheme }</h3>
        	</div>
			
      	</div>
      	<table class="table-gray-th text-center f12">
      		<thead>
      			<tr>
      				<th>姓名</th>
      				<th>学号</th>
      				<th>签到状态</th>
      			</tr>
      		</thead>
      		<tbody>
      			<c:forEach items="${plan.certificates }" var="p">
      			<tr>
      				<td>${p.studentName }</td>
      				<td>${p.studentNo }</td>
      				<td>
      					<c:if test="${p.signStatus==1 }">已签到</c:if>
      					<c:if test="${p.signStatus==0 }">未签到</c:if>
      				</td>
      			</tr>
      			</c:forEach>
      		</tbody>
      	</table>

		<div class="clearfix margin_t10">
			<div class="margin_t5 oh">
				<h3 class="cnt-box-title f14 text-bold">授课凭证扫描件</h3>
			</div>
		</div>
		<form id="imageForm" class="form-horizontal"  method="post">
		<ul class="list-inline img-gallery img-gallery-md upload-box" style="margin-right:-5px;">
			<c:forEach items="${plan.images }" var="image">
				<li>
					<img src="${image }">
					<span class="glyphicon " data-toggle="tooltip" data-html="true" title="" data-original-title="<div style='width:40px;'>删除</div>"></span>
					<a href="#" class="img-fancybox f12" data-large-img="${image }">点击放大</a>
				</li>
			</c:forEach>
		</ul>
		</form>
	</div>
</div>
<jsp:include page="/eefileupload/upload.jsp" />

<script type="text/javascript">

$('body').on('click','.btn-add-img-addon',function() {
	var $container=$(this).parent().next().find('.upload-box');
	var formControlName = 'images';
	uploadImageNew($container, formControlName);
});

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
 
function uploadCertificate() {
	uploadFile('fileName','fileUrl','xls',null,function(){
		$.ajax({
			url:"/edumanage/custom/course/uploadCertificate",
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
			url:"/edumanage/custom/course/uploadCertificateImage",
			async:false,
			data:$("#imageForm").serialize()+"&grantCoursePlanId="+$("#grantCoursePlanId").val(),
			dataType:"text",
			type:"post",
			success:function(data ) {
				successTip(postIngIframe);
			},
			error:function(XMLHttpRequest, textStatus, errorThrown) {
				errorTip(postIngIframe);
				
			}
		});
	}  else {
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
