<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-开设课程</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" onclick="history.back()">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教务管理</a></li>
		<li><a href="${ctx}/edumanage/openclassCollege/list">开课管理</a></li>
		<li class="active">开设课程</li>
	</ol>
</section>

<section class="content">
	<div class="row">
		<div class="col-md-12">
			<div class="box box-primary">
				<div class="box-header with-border">
					<h3 class="box-title">开设课程</h3>
				</div>
				<form id="theform" class="theform" role="form" action="${ctx}/edumanage/openclass/saveOpenCourse" method="post">
					<input id="action" type="hidden" name="action" value="${action }"/>
					<input type="hidden" name="courseIds" value=""/>

					<div class="box-body" id="openclass">
						<div class="form-group">
							<label class="col-sm-2 control-label" style="width: 10%"><small class="text-red">*</small>开课学期</label>
							<div class="col-sm-4">
								<select id="termId" name="termId"
										class="selectpicker show-tick form-control" data-size="8" <c:if test="${action!='create'}">disabled="disabled"</c:if>>
									<c:forEach items="${termMap}" var="map">
										<c:if test="${action=='create'}">
											<option value="${map.key}" <c:if test="${map.key==defaultGradeId}">selected='selected'</c:if>>${map.value}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
						</div>
						<br><br>
						<div class="form-group">
							<label class="col-sm-2 control-label no-pad-top" style="width: 10%">开设课程</label>
							<div class="col-sm-10">
								<table class="table table-bordered table-striped vertical-mid text-center table-font">
									<thead>
										<tr>
											<th><input type="checkbox" class="select-all"></th>
											<th>课程代码</th>
											<th>课程名称</th>
											<th>选课人数</th>
											<th>教学方式</th>
											<th>课程性质</th>
											<th>课程学时</th>
											<th>状态</th>
										</tr>
									</thead>
									<tbody id="courseTbody">
										<tr>
											<td>
												<input type="checkbox" name="ids" data-id="" data-name="check-id" value="">
											</td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="box-footer text-right">
						<button type="button" class="btn btn-default min-width-90px margin_r10" data-role="cancel" onclick="history.back()">返回</button>
						<button type="submit" class="btn btn-primary min-width-90px" data-role="sure">开设课程</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</section>  
<script type="text/javascript">
	
	//表单验证
	;(function(){
		var $theform=$(".theform");
	
		var htmlTemp='<div class="tooltip top" role="tooltip">'
		      +'<div class="tooltip-arrow"></div>'
		      +'<div class="tooltip-inner"></div>'
		      +'</div>';
		$theform.find(":input[datatype]").each(function(index, el) {
			$(this).after(htmlTemp);
		});
	
		$.Tipmsg.r='';
		var postForm=$theform.Validform({
		  showAllError:false,
		  ajaxPost:true,
		  tiptype:function(msg,o,cssctl){
		    if(!o.obj.is("form")){
		    	//msg：提示信息;
			    //o:{obj:*,type:*,curform:*},
			    //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
			    //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, 
			    //curform为当前form对象;
			    //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
	
			    var msgBox=o.obj.closest('.position-relative').find('.tooltip');
	
			    msgBox.css({
			        bottom:"100%",
			        'margin-bottom':-5
			    })
			    msgBox.children('.tooltip-inner').text(msg);
	
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
			  
			if($(':input[name="ids"]:checked').length==0) {
	            alert('请选择课程');
	            return false;
	        }
	        var ids = '';
	        $(':input[name="ids"]:checked').each(function(i, v) {
	        	ids = ids + v.value + ',';
	        });
	        $(':input[name="courseIds"]').val(ids.substring(0, ids.length - 1));  
			
		    window.postIngIframe=$.formOperTipsDialog({
				text:'数据提交中...',
				iconClass:'fa-refresh fa-spin'
			});
		  },
		  callback:function(data){
		    //这里执行回调操作;
		    setTimeout(function(){//此句模拟交互，程序时请去掉
		      	postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
		  		postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
	
		      /*关闭弹窗*/
		      setTimeout(function(){
		        $.closeDialog(postIngIframe);
		        window.location.href="${ctx}/edumanage/openclass/list";
		      },1000)
		    },1000);//此句模拟交互，程序时请去掉
		  }
		});
	
		//确认发布
		/* $('[data-role="sure"]').click(function(event) {
			postForm.ajaxPost();
		}); */
	})();
	
	$(function() {

	    // 选择学期
	    $("#termId").on('change', function(){
	        var termId = $(this).val();
	        if($.trim(termId) == '') {
	            $("form#inputForm :input[name='termId']").focus();
	            return;
	        }
	        
	        $("#openclass").addClass("overlay-wrapper");
			$("#openclass").append('<div class="overlay"><i class="fa fa-refresh fa-spin" style="top:50px !important"></i></div>');
	        $('#courseTbody').empty();
	        $.getJSON(ctx+'/edumanage/openclass/getCourseList', {termId:termId}, function (data) {
	            $.each(data.obj, function(i,item) {
	            	var trHtml = '<tr>';
	            	//1-全部启用，0-暂无资源，2-建设中，3-待验收，4-验收不通过，5-部分启用 
	            	//更改新的状态，0不开课，1可以开课
	            	if ((item.isOpen!='1') || item.wsjxzk!='1') {
		            	trHtml += '<td></td>';
	            	} else {
		            	trHtml += '<td><input type="checkbox" name="ids" data-id="'+item.courseId+'" data-name="check-id" value="'+item.courseId+'"></td>';
	            	}
	            	trHtml += '<td>'+item.kch+'</td>';
	            	trHtml += '<td>'+item.kcmc+'</td>';
	            	trHtml += '<td id="course_'+item.courseId+'">0</td>';
	            	trHtml += '<td>'+item.wsjxzkName+'</td>';
	            	trHtml += '<td>'+item.courseNature+'</td>';
	            	trHtml += '<td>'+(item.hour!=null?item.hour:'')+'</td>';
	            	if (item.isOpen=='1') {
	            		trHtml += '<td><span class="text-green">已启用</span></td>';
	            	} else if (item.isOpen=='0') {
	            		trHtml += '<td><span class="text-red">暂无资源</span></td>';
	            	}
	            	trHtml += '</tr>';
	                $('#courseTbody').append(trHtml);
	            });
	            $("#openclass").removeClass("overlay-wrapper");
	            $("#openclass").find(".overlay").remove();
	            
	            $.getJSON(ctx+'/edumanage/openclass/getCourseChooseCount?TERM_ID='+termId, {}, function (courseData) {
	            	for (var j in courseData) {
	            		var course = courseData[j];
	            		$("#course_"+course.COURSE_ID).html(course.CHOOSE_COUNT);
	            	}
	            });
	        });
	    }).trigger('change');

	    // 确认
	    /*$("#btn-submit").click(function(){
	        if($(':input[name="ids"]:checked').length==0) {
	            alert('请选择课程');
	            return false;
	        }
	        var ids = '';
	        $(':input[name="ids"]:checked').each(function(i, v) {
	            ids = ids + v.value + ',';
	        });
	        $(':input[name="courseIds"]').val(ids.substring(0, ids.length - 1));
	        return true;
	    });*/
	})
</script>

</body>
</html>