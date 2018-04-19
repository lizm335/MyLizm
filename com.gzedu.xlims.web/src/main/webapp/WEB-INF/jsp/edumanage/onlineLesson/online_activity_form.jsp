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

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">直播活动</a></li>
		<li class="active">新增直播</li>
	</ol>
</section>
<section class="content">
<form id="theform">
	<div class="box no-margin school-set-box">
		<div class="box-body">
			<div class="form-horizontal reset-form-horizontal margin_t10">
				<input type="hidden" value="${param.id}" id="id">
				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>直播名称</label>
					<div class="col-md-8 col-sm-10">
						<div class="position-relative">
							<input type="text" id="onlinetutorName" value="${lesson.onlinetutorName}" class="form-control" placeholder="请填写直播名称" datatype="*" nullmsg="请填写直播名称" errormsg="请填写直播名称">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>直播简介</label>
					<div class="col-md-8 col-sm-10">
						<div class="position-relative">
							<textarea class="form-control" id="onlinetutorDesc" rows="5" placeholder="请填写直播简介" datatype="*" nullmsg="请填写直播简介" errormsg="请填写直播简介">${lesson.onlinetutorDesc}</textarea>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label">直播标签</label>
					<div class="col-md-8 col-sm-10">
						<div class="position-relative">
							<input type="text" id="onlinetutorLabel" value="${lesson.onlinetutorLabel}" class="form-control" placeholder="请填写直播标签" >
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label">直播封面</label>
					<div class="col-md-8 col-sm-10">
              <button type="button" onclick="uploadImage('coverImg','imgUrl',null,null,showCoverBox)" class="btn min-width-90px btn-default">上传图片</button>
              <ul class="list-inline img-gallery margin-bottom-none">
                  <li id="coverBox" ${empty(lesson.imgUrl)?'style="display:none"':''}>
                      <img src="${lesson.imgUrl}" id="coverImg" />
                      <span class="glyphicon glyphicon-remove-sign" data-toggle="tooltip" data-html="true" title="<div style='width:40px;'>删除</div>"></span>
                      <input type="hidden" id="imgUrl" name="imgUrl" value="${lesson.imgUrl}" />
                  </li>
              </ul>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label">直播宣传</label>
					<div class="col-md-8 col-sm-10">
						<textarea class="form-control" id="activityContent" rows="5" placeholder="请填写活动内容" datatype="*" nullmsg="请填写活动内容" errormsg="请填写活动内容">${lesson.activityContent}</textarea>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>直播范围</label>
					<div class="col-md-8 col-sm-10">
						<div class="radio">
                <label class="margin_r10">
                  <input type="radio" name="r1" value="0" data-id="radio" <c:if test="${empty(chooseStu)}">checked</c:if>>
                 	 按范围选择
                </label>

                <label>
                  <input type="radio" name="r1" value="1" data-id="radio" <c:if test="${not empty(chooseStu)}">checked</c:if>>
                  指定学员
                </label>
            </div>

            <div class="margin_t10">
            	<div data-role="tab-cnt" <c:if test="${not empty(chooseStu)}">style="display:none;"</c:if>>
								<ul class="list-unstyled">
									<li class="margin_b10 position-relative">
										<select class="form-control select2 condition" datatype="*" id="gradeIds" name="gradeIds" multiple="multiple" data-placeholder="全部学期" nullmsg="请选择学期" errormsg="请选择学期" style="width: 100%;">
											<option value=''></option>
											<c:forEach items="${gradeMap}" var="item">
												<option ${fn:contains(objectIds,item.key)?'selected="selected"':'' } value='${item.key }'>${item.value}</option>
											</c:forEach>
										</select>
									</li>
									<li class="margin_b10">
										<select class="form-control select2 condition" id="pyccIds" name="pyccIds" multiple="multiple" data-placeholder="全部层次" style="width: 100%;">
											<option value=''></option>
											<c:forEach items="${pyccMap}" var="item">
												<c:set var="iscontain" value="false" /> 
												<c:forEach items="${objectIds}" var="obj">
													<c:if test="${obj==item.key }">
														<c:set var="iscontain" value="true" /> 
													</c:if>
												</c:forEach>
												<option ${iscontain?'selected="selected"':'' } value="${item.key }">${item.value}</option>
											</c:forEach>
										</select>
									</li>
									
									<li class="margin_b10">
										<select class="form-control select2 condition" id="specialtyIds" name="specialtyIds" multiple="multiple" data-placeholder="全部专业" style="width: 100%;">
											<option value=''></option>
											<c:forEach items="${specialtyMap}" var="item">
												<option ${fn:contains(objectIds,item.key)?'selected="selected"':'' } value='${item.key }'>${item.value}</option>
											</c:forEach>
										</select>
									</li>
									<li>
										<select class="form-control select2 condition" id="courseIds" name="courseIds" multiple="multiple" data-placeholder="全部课程" style="width: 100%;">
											<option value=''></option>
											<c:forEach items="${courseMap}" var="item">
												<option ${fn:contains(objectIds,item.key)?'selected="selected"':'' } value='${item.key }'>${item.value}</option>
											</c:forEach>
										</select>
									</li>
								</ul>
							</div>
							<div data-role="tab-cnt" <c:if test="${empty(chooseStu)}">style="display:none;"</c:if>>
								<button type="button" class="btn min-width-90px btn-default margin_r5" data-role="import">批量导入学员</button>
								<button type="button" class="btn min-width-90px btn-default" data-role="sel-online">在线选择学员</button>
							</div>
						</div>

						<div class="margin_t10">
							本次共选择 <u role="button" id="stunum" data-role="view-sel-stu">${stunum}</u> 个学员
						</div>
					</div>
				</div>

				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>直播时间</label>
					<div class="col-md-8 col-sm-10">
						<div class="input-group-custom" data-role="daterangetime-group">
              <div class="form-control-box position-relative">
                <input type="text" class="form-control bg-white" id="onlinetutorStart" value="<fmt:formatDate value="${lesson.onlinetutorStart }" pattern="yyyy-MM-dd HH:mm:ss"  />" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写起始时间" errormsg="日期格式不对" data-role="date-start" readonly="">
              </div>
              <p class="input-group-addon">~</p>
              <div class="form-control-box position-relative">
                <input type="text" class="form-control bg-white" id="onlinetutorFinish" value="<fmt:formatDate value="${lesson.onlinetutorFinish }" pattern="yyyy-MM-dd HH:mm:ss"  />" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写结束时间" errormsg="日期格式不对" data-role="date-end" readonly="">
              </div>
            </div>
					</div>
				</div>

				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label">附件资料</label>
					<div class="col-md-8 col-sm-10">
						<div class="table-block control-group">
      				<div class="table-cell-block">
                  <ul id="files" class="list-unstyled stu-list-box clearfix" data-id="assign">
                    <c:forEach items="${files }" var="file">
                    	<li>
	                      <span data-url="${file.fileUrl }">${file.fileName }</span>
	                      <i class="fa fa-fw fa-remove" data-toggle="tooltip" title="删除"></i>
	                    </li>
                    </c:forEach>
                  </ul>
      				</div>
      				<div class="table-cell-block pad-l10">
      					<button type="button" class="btn btn-block btn-default" onclick="uploadFile('filename','filepath','txt|rar|xls|xlsx|doc|docx|ppt|jpg|png',50,uploadCallback)" data-role="add-person-btn">选择文件</button>
      				</div>
      				<input type="hidden" id="filename"/>
      				<input type="hidden" id="filepath"/>
      			</div>
					</div>
				</div>

				<div class="form-group">
					<label class="col-md-2 col-sm-2 control-label"></label>
					<div class="col-md-8 col-sm-10">
						<button type="submit" class="btn btn-success min-width-90px margin_r15" data-role="submit">发布</button>
						<a href="${ctx}/edumanage/onlinelesson/queryOnlineActivityList" class="btn btn-default min-width-90px" data-role="cancel">取消</a>
					</div>
				</div>
			</div>
		</div><!-- /.box-body -->
	</div>
</form>
</section>

	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<jsp:include page="/eefileupload/upload.jsp"/>
	<script type="text/javascript">
	CKEDITOR.replace('activityContent');
	/*删除图片*/
	$(".img-gallery").on('click', '.glyphicon-remove-sign', function(event) {
	    event.preventDefault();
	    $(this).closest('li').hide();
	    $(this).siblings('[name="imgUrl"]').val('');
	});

	// select2
	$('.select2').select2();

	/* 日期控件 */
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


	// 表单验证
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
		// showAllError:true,
		ignoreHidden : true,// 是否忽略验证不可以见的表单元素
		tiptype : function(msg, o, cssctl) {
		    // msg：提示信息;
		    // o:{obj:*,type:*,curform:*},
		    // obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
		    // type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态,
		    // curform为当前form对象;
		    // cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
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
			if($('#stunum').text()=='0'){
				alert('请选择参与直播的学员');
				return false;
			}

			var params={
					id:$('#id').val(),
					onlinetutorName:$('#onlinetutorName').val(),
					onlinetutorDesc:$('#onlinetutorDesc').val(),
					onlinetutorLabel:$('#onlinetutorLabel').val(),
					description:$('#onlinetutorDesc').val(),
					imgUrl:$('#imgUrl').val(),
					activityContent:CKEDITOR.instances.activityContent.getData(),
					onlinetutorStart:$('#onlinetutorStart').val(),
					onlinetutorFinish:$('#onlinetutorFinish').val(),
					pyccIds:$('#pyccIds').select2('val'),
					gradeIds:$('#gradeIds').select2('val'),
					specialtyIds:$('#specialtyIds').select2('val'),
					courseIds:$('#courseIds').select2('val'),
					objType:$('[name="r1"]:checked').val()
				};
			var files=[];
			$('#files span').each(function(){
				files.push(JSON.stringify({
					name:$(this).text(),
					path:$(this).data('url')
				}));
			});
			params.files=files;
		    postIngIframe = $.formOperTipsDialog({
				text : '数据提交中...',
				iconClass : 'fa-refresh fa-spin'
		    });
 			$.post('${ctx}/edumanage/onlinelesson/saveOnlineActivity',params,function(data){
				if(data.successful){
				    postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
					postIngIframe.find('[data-role="tips-icon"]').attr('class', 'fa fa-check-circle');
					location.href='${ctx}/edumanage/onlinelesson/queryOnlineActivityList';
				}else{
					alert("系统异常");
					 $.closeDialog(postIngIframe);
				}
 			},'json');

		  

		    return false;// 阻止表单自动提交
		},
		callback : function(data) {

		}
	    });

	})();

	// 直播范围 类型选择
	$('[data-id="radio"]').on('click', function(event) {
	    var index = $('[data-id="radio"]').index(this);
	    $('[data-role="tab-cnt"]').eq(index).show().siblings().hide();
	    if(index==0){
			queryActivityStudent();
		}else if(index==1){
			$.get('${ctx}/edumanage/onlinelesson/queryActivityStudent',{},function(data){
			    $('#stunum').text(0);
			},'json');
		}
	});

	// 批量导入学员
	$('[data-role="import"]').click(function(event) {
	    event.preventDefault();
	    var actionName = ctx+"/edumanage/onlinelesson/importOnlineActivityStudent";
		var downloadFileUrl = ctx+"/excelImport/downloadModel?name=直播学员导入表.xls";
		var content1 = "为了方便你的工作，我们已经准备好了《直播学员导入表》的标准模板<br>你可以点击下面的下载按钮，下载标准模板。"
		var content2 = "请选择你要导入的《直播学员导入表》";
		excelImport(actionName, "file", "onlineStudent", downloadFileUrl, null, "批量导入直播学员", null, null, null, content1, content2,null,"importCallBack");
	});

	var importCallBack=function(data){
	    $('#stunum').text(data.result);
	};

	// 删除附件
	$(".stu-list-box").on("click", ".fa-remove", function() {
	    $(this).parent().remove();
	});

	var uploadCallback=function(){
	    var html=[
	    	'<li>',
              '<span data-url="{0}">{1}</span>',
              '<i class="fa fa-fw fa-remove" data-toggle="tooltip" title="删除"></i>',
            '</li>'
		];
		html=html.join('').format($('#filepath').val(),$('#filename').val());
		$('.stu-list-box').append(html);
	}

	// 直播 查看已选择的学员
	$('[data-role="view-sel-stu"]').click(function(event) {
	    $.mydialog({
		id : 'broadcast-view-student',
		width : 800,
		height : 600,
		zIndex : 11000,
		content : 'url:${ctx}/edumanage/onlinelesson/queryOnlineActivityStudent'
	    });
	});

	$('[data-role="sel-online"]').click(function(event) {
	    $.mydialog({
		id : 'sel-online-student',
		width : 800,
		height : 600,
		zIndex : 11000,
		content : 'url:${ctx}/edumanage/onlinelesson/onlineChooseStudent'
	    });
	});

	var showCoverBox=function(){
			$('#coverBox').show();
		}
	var showAppBox=function(){
	    	$('#appBox').show();
		}

	var queryActivityStudent=function(){
	    var msgBox = $('#gradeIds').closest('.position-relative').find('.tooltip');
		if($('#gradeIds').select2('val')==null){
		    msgBox.css({
			    'z-index' : 2,
			    bottom : "100%",
			    'margin-bottom' : -5
			}).children('.tooltip-inner').text('请先选择学期');
		    msgBox.addClass('in');
			return false;
		}else{
		    msgBox.removeClass('in');
		}
	    $.post('${ctx}/edumanage/onlinelesson/queryActivityStudent',{
			pyccIds:$('#pyccIds').select2('val'),
			gradeIds:$('#gradeIds').select2('val'),
			specialtyIds:$('#specialtyIds').select2('val'),
			courseIds:$('#courseIds').select2('val')
		},function(data){
			$('#stunum').text(data);
		},'json');
	}
	$('.condition').change(queryActivityStudent);
    </script>
</body>
</html>