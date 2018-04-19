<%@page import="java.util.Date"%>
<%@page import="javax.xml.crypto.Data"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<style type="text/css">
.inline-block2{display: inline-block;}
</style>

</head>
<body class="inner-page-body">

	<section class="content-header clearfix">
		<button class="btn btn-default btn-sm pull-right min-width-60px" onclick="history.back()">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="homepage.html">
					<i class="fa fa-home"></i> 首页
				</a>
			</li>
			<li href="javascript:;">授课管理</li>
			<li class="active">编辑</li>
		</ol>
	</section>

	<section class="content">

		<form id="theform" method="post" action="${ctx}/edumanage/openclass/saveLesson">

			<input type="hidden" id="search_number" name="search_id" value="${param.id}"> 
			<input type="hidden" id="search_termCourseIds" name="search_termCourseIds" value="${termCourseIds}">


			<div class="box no-margin school-set-box">
				<div class="box-body">
					<div class="form-horizontal reset-form-horizontal margin_t10">
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label">
								<small class="text-red">*</small>直播名称
							</label>
							<div class="col-md-8 col-sm-10">
								<div class="position-relative">
									<input type="text" class="form-control" placeholder="请填写直播名称" datatype="*" nullmsg="请填写直播名称" errormsg="请填写直播名称" id="search_onlineName" name="search_onlineName" value="${onlineLesson.onlinetutorName}">
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label">
								<small class="text-red">*</small>直播简介
							</label>
							<div class="col-md-8 col-sm-10">
								<div class="position-relative">
									<textarea class="form-control" placeholder="请填写直播简介" datatype="*" nullmsg="请填写直播简介" errormsg="请填写直播简介" id="search_onlineDesc" name="search_onlinetutorDesc" rows="5" cols="">${onlineLesson.onlinetutorDesc}</textarea>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label">
								<small class="text-red">*</small>直播封面
							</label>
							<div class="col-md-8 col-sm-10">
								<div class="position-relative" data-role="valid" data-id="valid-img">
									<div class="${empty(onlineLesson.imgUrl)?'inline-block2':'hide'} add-img margin_r10" role="button" id="uploadBtn">
										<i class="fa fa-fw fa-plus"></i>上传封面
									</div>
									<div class="${empty(onlineLesson.imgUrl)?'hide':'inline-block2'} img-box vertical-middle">
										<ul class="img-list clearfix">
											<li>
												<img src="${onlineLesson.imgUrl}"  id="imgId" class="user-image" alt="User Image">
												<button type="button" class="btn flat btn-default btn-sm btn-block">删除</button>
											</li>
										</ul>
									</div>
								</div>
								<input type="hidden" id="imgPath" name="search_imgUrl" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label">
								<small class="text-red">*</small>学期
							</label>
							<div class="col-md-8 col-sm-10">
								<div class="position-relative">
									<select class="form-control select2" multiple="multiple" data-placeholder="请选择学期" style="width: 100%;" datatype="*" nullmsg="请选择学期" errormsg="请选择学期" id="termIds" name="termIds">
										<c:forEach items="${grades}" var="map">
											<option value="${map.key}" <c:if test="${fn:contains(termIds,map.key)}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label">
								<small class="text-red">*</small>课程
							</label>
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
									<tbody data-id="course-box" id="course_list">
										<c:forEach items="${courseInfo}" var="entity">
											<tr>
												<td>${entity.GRADE_NAME}</td>
												<td>
													<div>${entity.KCMC}</div> (<small class='gray9'>${entity.KCH}</small>)
												</td>
												<td>${entity.CHOOSE_STU}</td>
												<td>${entity.NEEDLESSON_STU}</td>
												<td><a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove">
														<i class="fa fa-trash-o text-red"></i>
													</a> <input type="hidden" class="termCourseId" name="termCourseIds" value="${entity.TERMCOURSE_ID}" /></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label">
								<small class="text-red">*</small>直播时间
							</label>
							<div class="col-md-8 col-sm-10">
								<div class="input-group-custom" data-role="daterangetime-group">
									<div class="form-control-box position-relative">
										<input type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写起始时间" errormsg="日期格式不对" data-role="date-start" value="<fmt:formatDate value='${onlineLesson.onlinetutorStart}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" id="search_startDt" name="search_startDt">
									</div>
									<p class="input-group-addon">~</p>
									<div class="form-control-box position-relative">
										<input type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写结束时间" errormsg="日期格式不对" data-role="date-end" value="<fmt:formatDate value='${onlineLesson.onlinetutorFinish}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>" id="search_endDt" name="search_endDt">
									</div>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"></label>
							<div class="col-md-8 col-sm-10">
								<button type="submit" class="btn btn-success min-width-90px margin_r15" id="btn-update"></button>
								<a href="${ctx}/edumanage/openclass/schoolTeach" class="btn btn-default min-width-90px">取消</a>
							</div>
						</div>
					</div>
				</div>
				<!-- /.box-body -->
			</div>
		</form>


	</section>

	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<jsp:include page="/eefileupload/upload.jsp"/>
	<script type="text/javascript">
	$(function() {

	    $('.select2').select2();

	    initParam();

	    /*日期控件*/
	    /*日期控件*/
	    ;(function(){
	      var $container=$('[data-role="daterangetime-group"]');
	      var startDate=$('[data-role="date-start"]',$container);
	      var endDate=$('[data-role="date-end"]',$container);
	      var config=$.extend({},daterangepickerOpt,
	        {
	          format: "YYYY-MM-DD HH:mm:ss",
	          timePicker: true,
	          timePicker12Hour: false,
	          timePickerIncrement:5,
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

	        //结束时间只能是当天
	        endDaterangepicker.maxDate=moment(picker.startDate.format('YYYY-MM-DD 23:59:59'));

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


	    ;
	    (function() {
		var $theform = $("#theform");

		var htmlTemp = '<div class="tooltip top" role="tooltip">' + '<div class="tooltip-arrow"></div>' + '<div class="tooltip-inner"></div>' + '</div>';
		$theform.find(".position-relative").each(function(index, el) {
		    $(this).append(htmlTemp);
		});

		$.Tipmsg.r = '';
		var postIngIframe;
		var postForm = $theform.Validform({
		    //showAllError:true,
		    ignoreHidden : true,//是否忽略验证不可以见的表单元素
		    tiptype : function(msg, o, cssctl) {
			//msg：提示信息;
			//o:{obj:*,type:*,curform:*},
			//obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
			//type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态,
			//curform为当前form对象;
			//cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
			if (!o.obj.is("form")) {
			    var msgBox = o.obj.closest('.position-relative').find('.tooltip');

			    msgBox.css({
				'z-index' : 2,
				bottom : "100%",
				'margin-bottom' : -5
			    }).children('.tooltip-inner').text(msg);

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
			var $sTips = $('[data-role="select-course"]').next('.tooltip');
			if ($('[data-id="course-box"]').children().length <= 0) {
			    $sTips.addClass('in').css({
				'z-index' : 2,
				bottom : "100%",
				'margin-bottom' : -5
			    }).children('.tooltip-inner').text('请选择课程');

			    return false;
			} else {
			    $sTips.removeClass('in');
			}

			postIngIframe = $.formOperTipsDialog({
			    text : '数据提交中...',
			    iconClass : 'fa-refresh fa-spin'
			});

			// 重新统计期课程
			var tcids = new Array();
			$(".termCourseId").each(function() {
			    tcids.push($(this).val());
			});
			$("#search_termCourseIds").val(tcids);

			$.post(ctx + "/edumanage/openclass/saveLesson", curform.serialize(), function(data) {
			    if (data.successful) {
					postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
					postIngIframe.find('[data-role="tips-icon"]').attr('class', 'fa fa-check-circle');
					window.location.href = "${ctx}/edumanage/openclass/schoolTeach";
			    } else {
					postIngIframe.find('[data-role="tips-text"]').html('数据提交失败');
					postIngIframe.find('[data-role="tips-icon"]').attr('class', 'fa fa-exclamation-circle');
					setTimeout(function(){
					    $.closeDialog(postIngIframe);
						},2000);
			    }

			}, "json");

			return false;//阻止表单自动提交

		    },
		    callback : function(data) {

		    }
		});

	    })();

	    //选择课程
	    $('[data-role="select-course"]').click(function(event) {
		var gradeIds = $("#termIds").val();
		if ($.trim(gradeIds) == '') {
		    $('#termIds').closest('.position-relative').find('.tooltip').addClass('in').css({
			'z-index' : 2,
			bottom : "100%",
			'margin-bottom' : -5
		    }).children('.tooltip-inner').text('请选择学期');
		    return;
		}
		var termCourseIds = [];
		$('.termCourseId').each(function() {
		    termCourseIds.push(this.value);
		});
		termCourseIds = termCourseIds.join(',');
		gradeIds = gradeIds.join(',');
		var url = '${ctx}/edumanage/coachdata/chooseCourse?gradeIds=' + gradeIds + '&termCourseIds=' + termCourseIds;
		$.mydialog({
		    id : 'select-course',
		    width : 800,
		    height : 500,
		    zIndex : 11000,
		    content : 'url:' + url
		});
	    });

	    //删除课程
	    $('body').on('click', '[data-role="remove"]', function(event) {
		event.preventDefault();
		$(this).closest('tr').remove();
	    });

	});

	function initParam() {
	    var tcids = new Array();
	    $('#data-termcourseId').each(function() {
		tcids.push(entitys[i].TERMCOURSE_ID);
	    });
	    $("#search_termCourseIds").val(tcids);// 赋值期课程

	    var btn_type = "${param.number}";// 赋值按钮名
	    if (btn_type == null || btn_type.length < 10)
		$("#btn-update").text("发布");
	    else
		$("#btn-update").text("更新");
	}

	$('.img-box').on('click', '.btn', function(event) {
		event.preventDefault();
		$('.img-box').removeClass('inline-block2').addClass('hide');
		$('#uploadBtn').removeClass('hide').addClass('inline-block2');
		$('#imgPath').val('');
	});

	$('#uploadBtn').click(function(){
	    uploadImage('imgId','imgPath',null,null,uploadCallback);
	});

	var uploadCallback=function (){
	    $('.img-box').removeClass('hide').addClass('inline-block2');
	    $('#uploadBtn').removeClass('inline-block2').addClass('hide');
	}
    </script>
</body>
</html>