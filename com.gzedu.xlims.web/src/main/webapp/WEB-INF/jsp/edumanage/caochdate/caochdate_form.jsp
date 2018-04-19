<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>管理系统</title>

	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<jsp:include page="/eefileupload/upload.jsp" />
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">授课管理</a></li>
		<li class="active">新增资料</li>
	</ol>
</section>
<section class="content">
<form id="theform" action="${ctx}/edumanage/coachdata/create" method="post">
	<div class="box no-margin school-set-box">
		<div class="box-body">
			<div class="form-horizontal reset-form-horizontal margin_t10">
				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>资料名称</label>
					<div class="col-md-8 col-sm-10">
						<div class="position-relative">
							<input type="text" name="dataName" class="form-control" placeholder="请填写资料名称" datatype="*" nullmsg="请填写资料名称" errormsg="请填写资料名称">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label">资料标签</label>
					<div class="col-md-8 col-sm-10">
						<div class="position-relative">
							<input type="text" name="dataLabel" class="form-control" placeholder="请填写标签" >
						</div>
						<div class="gray9">多个标签请用分号“;”分隔开</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>学期</label>
					<div class="col-md-8 col-sm-10">
						<div class="position-relative">
							<select class="form-control select2" id="gradeId" multiple="multiple" data-placeholder="请选择学期" style="width: 100%;" datatype="*" nullmsg="请选择学期" errormsg="请选择学期">
								<option value=''></option>
								<c:forEach items="${termMap}" var="map">
									<option value="${map.key}">${map.value}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>共享课程</label>
					<div class="col-md-8 col-sm-10">
						<div class="position-relative">
							<button type="button" class="btn btn-default" data-role="select-course">选择课程</button>
						</div>
						<table class="table table-bordered vertical-mid text-center table-font margin_t10 margin-bottom-none">
							<thead class="with-bg-gray">
								<tr>
									<th>学期</th>
									<th>课程名称</th>
									<th>选课人数</th>
									<th>需辅导人数</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody data-id="course-box"></tbody>
						</table>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>上传资料</label>
					<div class="col-md-8 col-sm-10">
						<div class="position-relative">
							<div class="input-group">
								<input id="filename" class="form-control bg-white" datatype="*" nullmsg="请上传资料" errormsg="请上传资料" readonly />
								<input id="filepath" name="dataPath" type="hidden" />
								
								<div class="input-group-btn pad-l10">
									<input type="button" class="btn btn-default flat" value="选择文件" onclick="uploadFile('filename','filepath','ppt|pptx|doc|docx|xls|xlsx|txt|rar|zip',10)" >
								</div>
								<div class="input-group-addon f12 gray9 text-left2 no-border">
									文件大小要求：不超过10M<br>
									文件类型要求：ppt|pptx|doc|docx|xls|xlsx|txt|rar|zip
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label"></label>
					<div class="col-md-8 col-sm-10">
						<button type="submit" class="btn btn-success min-width-90px margin_r15" data-role="submit">发布</button>
						<a href="${ctx}/edumanage/coachdata/coachDateList" class="btn btn-default min-width-90px" data-role="cancel">取消</a>
					</div>
				</div>
			</div>
		</div><!-- /.box-body -->
	</div>
</form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
//select2
$('.select2').select2().change(function(){
	if($(this).select2('val')!=null){
		$(this).closest('.position-relative').find('.tooltip').removeClass('in')
		    .children('.tooltip-inner').empty();
	}
});;

/*日期控件*/
;(function(){
  var $container=$('[data-role="daterangetime-group"]');
  var startDate=$('[data-role="date-start"]',$container);
  var endDate=$('[data-role="date-end"]',$container);
  var config=$.extend(true,daterangepickerOpt,
    {
      format: "YYYY-MM-DD HH:mm",
      timePicker: true,
      timePicker12Hour: false,
      //drops:'up',
      buttonClasses:'btn btn-sm margin_l5 margin_r5'
    }
  );
  //开始时间      
  startDate.daterangepicker(config);

  startDate
  .on('show.daterangepicker', function(ev, picker) {
    picker.container.find('.ranges').css({
      width: 'auto',
      'text-align': 'center'
    });
  })
  .on('apply.daterangepicker', function(ev, picker) {
    $(ev.target).removeClass('Validform_error')
    .next('.tooltip').removeClass('in')
    .children('.tooltip-inner').empty();

    var endDaterangepicker=endDate.data('daterangepicker');
    endDaterangepicker.minDate=picker.startDate;
    endDaterangepicker.updateView();
    endDaterangepicker.updateCalendars();
  });

  //结束时间
  endDate.daterangepicker(config);

  endDate
  .on('show.daterangepicker', function(ev, picker) {
    picker.container.find('.ranges').css({
      width: 'auto',
      'text-align': 'center'
    });
  })
  .on('apply.daterangepicker',function(ev, picker) {
    $(ev.target).removeClass('Validform_error')
    .next('.tooltip').removeClass('in')
    .children('.tooltip-inner').empty();

    var startDaterangepicker=startDate.data('daterangepicker');
    startDaterangepicker.maxDate=picker.startDate;
    startDaterangepicker.updateView();
    startDaterangepicker.updateCalendars();
  });
})();

	/*失败状态
	$.resultDialog(
		{
			type:2,
			msg:'发布失败！',
			timer:1500,
			width:150,
			height:50
		}
	);
	*/
	/*成功状态
	$.resultDialog(
		{
			type:1,
			msg:'发布成功！',
			timer:1500,
			width:150,
			height:50
		}
	);
	*/
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

//表单验证
;(function(){
    var $theform=$("#theform");

    var htmlTemp='<div class="tooltip top" role="tooltip">'
          +'<div class="tooltip-arrow"></div>'
          +'<div class="tooltip-inner"></div>'
          +'</div>';
    $theform.find(".position-relative").each(function(index, el) {
        $(this).append(htmlTemp);
    });

    $.Tipmsg.r='';
    var postIngIframe;
    var postForm=$theform.Validform({
      //showAllError:true,
      ajaxPost:true,
      ignoreHidden:true,//是否忽略验证不可以见的表单元素
      tiptype:function(msg,o,cssctl){
        //msg：提示信息;
        //o:{obj:*,type:*,curform:*},
        //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
        //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, 
        //curform为当前form对象;
        //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
        if(!o.obj.is("form")){
            var msgBox=o.obj.closest('.position-relative').find('.tooltip');

            msgBox.css({
            	'z-index':2,
	            bottom:"100%",
	            'margin-bottom':-5
	        }).children('.tooltip-inner').text(msg);

            switch(o.type){
              case 3:
                msgBox.addClass('in');
                break;
              default:
                msgBox.removeClass('in');
                break;
            }
        }
      },
      beforeSubmit:function(curform){
      	var $sTips=$('[data-role="select-course"]').next('.tooltip');
      	if($('[data-id="course-box"]').children().length<=0){
      		$sTips.addClass('in').css({
            	'z-index':2,
	            bottom:"100%",
	            'margin-bottom':-5
	        }).children('.tooltip-inner').text('请选择课程');

	        return false;
      	}
      	else{
      		$sTips.removeClass('in');
      	}

        postIngIframe=$.formOperTipsDialog({
          text:'数据提交中...',
          iconClass:'fa-refresh fa-spin'
        });
      },
      callback:function(data){
	      if(data.successful){
			postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
			postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
			location.href='${ctx}/edumanage/coachdata/coachDateList';
		  }else{
			alert(data.message);
			$.closeDialog(postIngIframe);
		  }
      }
    });

})();

//选择课程
$('[data-role="select-course"]').click(function(event) {
   var gradeIds=$('#gradeId').select2("val");
   if($.trim(gradeIds)==''){
		$('#gradeId').closest('.position-relative').find('.tooltip').addClass('in').css({
	            	'z-index':2,
		            bottom:"100%",
		            'margin-bottom':-5
		        }).children('.tooltip-inner').text('请选择学期');
	    return;
   }
   var termCourseIds=[];
   $('.termCourseId').each(function(){
       termCourseIds.push(this.value);
   });
   termCourseIds=termCourseIds.join(',');
   gradeIds=gradeIds.join(',');
	var url='${ctx}/edumanage/coachdata/chooseCourse?gradeIds='+gradeIds+'&termCourseIds='+termCourseIds;
	$.mydialog({
	  id:'select-course',
	  width:800,
	  height:500,
	  zIndex:11000,
	  content: 'url:'+url
	});
});

//删除课程
$('body').on('click', '[data-role="remove"]', function(event) {
	event.preventDefault();
	$(this).closest('tr').remove();
});
</script>

</body>
</html>
