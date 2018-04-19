<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>设置教学辅导机构</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
	<div class="box no-border no-shadow margin-bottom-none">
		<div class="box-header with-border">
			<h3 class="box-title">设置教学辅导机构</h3>
		</div>
		<div class="box-body">
			<div class="box-border pad">
				<div class="form-inline">
					<form id="theform" class="theform" role="form" action="${ctx}/textbookPlan/updateOrderPlan" method="post">
						<input type="hidden" value="${entity.gradeId }" name="gradeId"/>
						<div class="form-group">
							<label class="col-sm-3 control-label text-no-bold">
								<i class="fa  fa-question-circle" data-toggle="tooltip" title="学院订购时间设置，是为了区分学院订购以及非学院订购（书商订购）订单教材；
教材管理员设定本学期学院订购时间后，系统会默认将订购时间范围内的学院订单归
纳到学院订购。非学院订购时间，则归纳为书商订购"></i>设置学院订购时间
							</label>
							<div class="col-sm-6">
								<div class="input-group-custom" data-role="daterangetime-group">
									<div class="form-control-box position-relative">
										<input name="textbookStartDate" value="${entity.textbookStartDate}" type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly>
									</div>
									<p class="input-group-addon">~</p>
									<div class="form-control-box position-relative">
										<input name="textbookEndDate" value="${entity.textbookEndDate}" type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div class="text-right pop-btn-box pad">
		<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">关闭</button>
		<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确认设置</button>
	</div>
	<script type="text/javascript">
	//关闭 弹窗
	$("button[data-role='close-pop']").click(function(event) {
	    parent.$.closeDialog(frameElement.api);
	});

	/*日期控件*/
	$('[data-role="daterangetime-group"]').each(function(i, e) {
	    var startDate = $('[data-role="date-start"]', e);
	    var endDate = $('[data-role="date-end"]', e);
	    //开始时间			
	    startDate.datepicker({
		language : 'zh-CN',
		format : 'yyyy-mm-dd',
		todayHighlight : true,
	    //orientation:'top'		  
	    //autoclose:true
	    }).on('changeDate', function(e) {
		var add = increaseOnedate(e.target.value);
		endDate.datepicker('setStartDate', add);
	    });
	    //结束时间
	    endDate.datepicker({
		language : 'zh-CN',
		format : 'yyyy-mm-dd',
		todayHighlight : true,
	    //orientation:'top'		  
	    //autoclose:true
	    }).on('changeDate', function(e) {
		var d = decreaseOnedate(e.target.value);
		startDate.datepicker('setEndDate', d);
	    }).on('focus', function() {
		if (this.value == "" && startDate.val() == "") {
		    startDate.focus();
		    endDate.datepicker('hide');
		}
	    });
	});

	$('[data-role="save-data"]').click(function(){
	    $(".theform").submit();
	});

	//表单验证
	;
	(function() {
	    var $theform = $(".theform");

	    var htmlTemp = '<div class="tooltip top" role="tooltip">' + '<div class="tooltip-arrow"></div>' + '<div class="tooltip-inner"></div>' + '</div>';
	    $theform.find(".position-relative").each(function(index, el) {
	            $(this).append(htmlTemp);
	        });

	    $.Tipmsg.r = '';
	    var postForm = $theform.Validform({
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
		    postIngIframe=$.formOperTipsDialog({
	                    text:'数据提交中...',
	                    iconClass:'fa-refresh fa-spin'
	                });
		},
		callback : function(data) {
		    if (data.successful) {
				postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
	            postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
				parent.location.reload();
				
		    } else {
				alert(data.message);
				 $.closeDialog(postIngIframe);
		    }
		}
	    });

	})();
    </script>
</body>
</html>