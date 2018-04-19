<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统-年级管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">
	<section class="content-header clearfix">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="homepage.html">
					<i class="fa fa-home"></i> 首页
				</a>
			</li>
			<li>
				<a href="#">教务管理</a>
			</li>
			<li>
				<a href="#">年级管理</a>
			</li>
			<li class="active">新建年级</li>
		</ol>
	</section>
	<section class="content">
		<div class="box no-margin">
			<div class="box-body pad20">
				<div class="form-horizontal reset-form-horizontal">
					<form id="inputForm">
						<div class="form-group">
							<label class="col-sm-3 control-label">
								<small class="text-red">*</small>选择年级
							</label>
							<div class="col-sm-6">
								<select class="form-control" name="gradeId" id="gradeId">
									<option value="">请选择</option>
									<c:forEach items="${years}" var="item">
										<option value="${item.gradeId}">${item.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">年级编号</label>
							<div class="col-sm-6">
								<p class="form-control-static" id="yearCode"></p>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" >入学年份</label>
							<div class="col-sm-6">
								<p class="form-control-static" id="startYear"></p>
							</div>
						</div>
					</form>
				</div>
				<div class="row">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-success min-width-90px margin_r15" data-role="save">保存</button>
						<button class="btn btn-default min-width-90px" data-role="back-off">取消</button>
					</div>
				</div>
			</div>
		</div>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
<script type="text/javascript">
/*日期控件*/
$('[data-role="timer"]').datepicker({
	  language:'zh-CN',
	  format:'yyyy-mm-dd'
});

$('#inputForm').bootstrapValidator({
	excluded: [':disabled'],//验证下拉必需加
	fields: {
	    gradeId: {
	        validators: {
	            notEmpty: "required"
	         }
	    }
	}
});

$('#gradeId').change(function(){
    if($.trim($(this).val())==''){
		return;
	}
    $.get('${ctx}/edumanage/year/getGjtYear',{
	    	gradeId:$(this).val()
	    },
	    function(data){
			$('#yearCode').text(data.code);
			$('#startYear').text(data.startYear);
		},'json');
});

//保存
$('[data-role="save"]').click(function(event) {
    $("#inputForm").data('bootstrapValidator').validate();  
	var valid= $("#inputForm").data('bootstrapValidator').isValid()
	if(!valid) return;
	var postIngIframe=$.mydialog({
	  id:'dialog-1',
	  width:150,
	  height:50,
	  backdrop:false,
	  fade:true,
	  showCloseIco:false,
	  zIndex:11000,
	  content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
	});

	$.post('${ctx}/edumanage/year/update',{
			gradeId:$('#gradeId').val(),
			isEnabled:1
		},function(data){
			if(data.successful){
			    postIngIframe.find(".text-center.pad-t15").html('数据提交成功...<i class="icon fa fa-check-circle"></i>');
				location.href='${ctx}/edumanage/year/list';
			}else{
				alert('系统异常');
				 $.closeDialog(postIngIframe);
			}
	},'json');

});


</script>
</html>