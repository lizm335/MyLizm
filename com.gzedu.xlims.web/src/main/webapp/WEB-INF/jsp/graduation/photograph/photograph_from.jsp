<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>工单管理</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<script type="text/javascript">
$(function() {
	//下拉渲染

$('#province').select_district($('#city'), $("#district"), $("#district").val());

})
</script>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="#">我的工单</a>
			</li>
			<li class="active">新建工单</li>
		</ol>
	</section>
	<section class="content">
		<form id="theform" action="${ctx }/graduation/photograph/${action }" method="post">
			<input type="hidden" name="id" value="${entity.id }"> 
			<input type="hidden" name="action" value="${action }">
			<input type="hidden" name="type" value="${type }"> 
			<div class="box no-margin">
				<div class="box-body pad-t15">
					<div class="form-horizontal reset-form-horizontal margin_t10">
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>拍摄点名称</label>
							<div class="col-sm-6 position-relative">
								<input type="text" name="photographName" value="${entity.photographName }" class="form-control" placeholder="拍摄点名称" datatype="*"
									nullmsg="请填写拍摄点名称" errormsg="请填写拍摄点名称">
							</div>
						</div>


						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>所在区域</label>
							<div class="col-sm-6">
								<div class="row row-offset-3px">
									<div class="col-sm-4">
										<select id="province" class="selectpicker show-tick form-control" 
											data-size="8" data-live-search="true">
											<option value=""></option>
										</select>
									</div>
									<div class="col-sm-4">
										<select id="city" class="selectpicker show-tick form-control" 
											data-size="8" data-live-search="true">
											<option value=""></option>
										</select>
									</div>
									<div class="col-sm-4">
										<select id="district" name="district" class="selectpicker show-tick form-control" 
											data-size="8" data-live-search="true">
											<option value="${entity.district}"></option>
										</select>
									</div>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>拍摄地址</label>
							<div class="col-sm-6 position-relative">
								<input type="text" class="form-control" value="${entity.photographAddress}" name="photographAddress" placeholder="拍摄地址" datatype="*" nullmsg="请填写拍摄地址" errormsg="请填写拍摄地址">
							</div>
						</div>
						<c:if test="${type eq '1' }">
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>拍摄时间</label>
							<div class="col-md-6 col-sm-10">
								<div class="input-group-custom" data-role="daterangetime-group">
									<div class="form-control-box position-relative">
										<input type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2})$/i" placeholder="起始时间"
											nullmsg="请填写起始时间" errormsg="日期格式不对" data-role="date-start" readonly=""
											value="<fmt:formatDate value="${entity.photographSrartDate}" pattern="yyyy-MM-dd HH:mm" />" name="photographSrartDates">
											
									</div>
									<p class="input-group-addon">~</p>
									<div class="form-control-box position-relative">
										<input type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2}) (\d{1,2}):(\d{1,2})$/i" placeholder="结束时间"
											nullmsg="请填写结束时间" errormsg="日期格式不对" data-role="date-end" readonly=""
											value="<fmt:formatDate value="${entity.photographEndDate}" pattern="yyyy-MM-dd HH:mm"/>" name="photographEndDates">
									</div>
								</div>
							</div>
						</div>
					</c:if>
                    <c:if test="${type ne '1' }">
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>拍摄时间</label>
							<div class="col-sm-6 position-relative">
								<input type="text" class="form-control" value="${entity.photographDate }" name="photographDate" placeholder="拍摄时间" datatype="*" nullmsg="请填写拍摄时间" errormsg="请填写拍摄时间">
							</div>
						</div>
					</c:if>
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>联系电话</label>
							<div class="col-sm-6 position-relative">
								<input type="text" class="form-control" value="${entity.telePhone }" name="telePhone" placeholder="联系电话" datatype="*" nullmsg="请填写联系电话" errormsg="请填写联系电话">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>注意事项</label>
							<div class="col-sm-6 position-relative">
								<textarea class="form-control" rows="5" name="notesRemark" placeholder="注意事项" datatype="*" nullmsg="请填写注意事项" errormsg="请填写注意事项">${entity.notesRemark }</textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>拍摄地址坐标值</label>
							<div class="col-sm-6 position-relative">
								<input type="text" class="form-control" value="${entity.coordinate }" name="coordinate" placeholder="拍摄地址坐标经讳度值" datatype="*" nullmsg="请填写拍摄地址坐标经讳度值" errormsg="请填写拍摄地址坐标经讳度值">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label"><small class="text-red">*</small>较贵状态</label>
							<div class="col-md-8 col-sm-10">
								<div class="radio">
									<label class="margin_r10">
										<input type="radio" name="expensiveStu" value="0" data-id="radio" <c:if test="${entity.expensiveStu eq 0 ||empty entity.expensiveStu}">checked</c:if>>
										正常
									</label>

									<label>
										<input type="radio" name="expensiveStu" value="1" data-id="radio" <c:if test="${entity.expensiveStu eq 1}">checked</c:if>>
										较贵
									</label>
								</div>
							</div>
						</div>

					</div>
				</div>
				<div class="box-footer text-center">
					<button type="submit" class="btn btn-success min-width-90px margin_r15" data-role="submit">
						<c:if test="${action eq 'create' }">发布</c:if>
						<c:if test="${action eq 'update' }">修改</c:if>
					</button>
					<button type="button" class="btn btn-default min-width-90px" data-role="cancel" onclick="history.back()">取消</button>
				</div>
			</div>
		</form>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
	
	
	/*日期控件*/
	;(function(){
	  var $container=$('[data-role="daterangetime-group"]');
	  var startDate=$('[data-role="date-start"]',$container);
	  var endDate=$('[data-role="date-end"]',$container);
	  var config=$.extend({},daterangepickerOpt,
	    {
	      format: "YYYY-MM-DD HH:mm",
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

	//确认
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
	      showAllError:true,
	      ignoreHidden:true,//是否忽略验证不可以见的表单元素
	      ajaxPost:true,
	      tiptype:function(msg,o,cssctl){
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

	        postIngIframe=$.formOperTipsDialog({
	          text:'数据提交中...',
	          iconClass:'fa-refresh fa-spin'
	        });
	      },
	      callback:function(data){
	        	postIngIframe.find('[data-role="tips-text"]').html(data.message);
				postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
				setTimeout(function(){
					$.closeDialog(postIngIframe);
						if(data.successful==true){
							window.location.href='${ctx}/graduation/photograph/list?type=${type}';
						}
				},2000); 
	      }
	    });

	})();
	</script>
</body>
</html>
