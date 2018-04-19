<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业管理－课程列表</title>

<%-- <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%> --%>
<!-- Bootstrap 3.3.5 -->
<link rel="stylesheet" href="${ctx}/static/bootstrap/css/bootstrap.min.css">
<link href="${ctx}/static/bootstrap/css/bootstrap-select.min.css"  rel="stylesheet">
<link href="${ctx}/static/bootstrap/css/bootstrap-table.css"  rel="stylesheet">
<!-- Font Awesome -->
<link rel="stylesheet" href="${ctx}/static/font-awesome/4.4.0/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="${ctx}/static/ionicons/2.0.1/css/ionicons.min.css">

<!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet" href="${ctx}/static/dist/css/skins/skin-blue.min.css">
<!-- Morris chart -->
<link rel="stylesheet" href="${ctx}/static/plugins/morris/morris.css">
<!-- jvectormap -->
<link rel="stylesheet" href="${ctx}/static/plugins/jvectormap/jquery-jvectormap-1.2.2.css">
<!-- Daterange picker -->
<link rel="stylesheet" href="${ctx}/static/plugins/daterangepicker/daterangepicker-bs3.css">
 <!-- Select2 -->
<link rel="stylesheet" href="${ctx}/static/plugins/select2/select2.min.css">
<!-- iCheck for checkboxes and radio inputs -->
<link rel="stylesheet" href="${ctx}/static/plugins/iCheck/all.css">
<!-- bootstrap wysihtml5 - text editor -->
<link rel="stylesheet" href="${ctx}/static/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="${ctx}/static/dist/css/AdminLTE.min.css">
<!-- jquery-ui -->
<link rel="stylesheet" href="${ctx}/static/plugins/jQueryUI/css/jquery-ui.css">
<link rel="stylesheet" href="${ctx}/static/bootstrap/css/jquery-confirm.css">
<link rel="stylesheet" href="${ctx}/static/dist/css/reset.css">
<script type="text/javascript">
</script>

</head>
<body class="inner-page-body">
	<section class="content">
		<div class="box no-margin">
			<div class="box-header with-border">
				<h3 class="box-title">${gjtGrade.gradeName } -- ${gjtSpecialty.zymc }</h3>
				<input id="specialtyId" type="hidden" value="${gjtSpecialty.specialtyId }">
				<input id="gradeId" type="hidden" value="${gjtGrade.gradeId }">
			</div>
			<div class="box-body">
				<div class="row clearfix">
					<div class="col-md-8">
						<ul class="list-inline score-list">
							<li>总学分：<strong class="f16"><span class="text-green" id="score_total">0</span>/<span id="score_base">${gjtSpecialty.zxf}</span></strong></li>
							<li>必修学分：<strong class="f16"><span
									class="text-green" id="score_total0">0</span>/<span id="score_base0">${gjtSpecialty.bxxf}</span></strong></li>
							<li>选修学分：<strong class="f16"><span class="text-green" id="score_total1">0</span>/<span id="score_base1">${gjtSpecialty.xxxf}</span></strong></li>
						</ul>
					</div>
				</div>
				<div class="margin_t10 table-responsive">
					<table class="table table-bordered plan-table">
						<thead>
							<tr>
								<th width="100" class="text-center">学期</th>
								<th width="50" class="text-center">序号</th>
								<th>
									<ul class="list-inline ul-label-list">
										<li><span class="text">课程名称</span></li>
										<li><span class="text">辅导教师</span></li>
										<li><span class="text">课程属性</span></li>
										<li><span class="text">课程类别</span></li>
										<li><span class="text">学分</span></li>
										<li><span class="text">学时</span></li>
										<li><span class="text">考试方式</span></li>
										<li><span class="text">学习占比(%)</span></li>
										<li><span class="text">考试占比(%)</span></li>
										<!-- <li><span class="text">学习时间</span></li>
										<li><span class="text">考试时间</span></li> -->
										<li><span class="text">操作</span></li>
									</ul>
								</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${planMap}" var="planMap" varStatus="vs">
								<tr>
									<td>
										<input name="termTypeCode" type="hidden" value="${planMap.key}">
										第${planMap.key}学期
										${studyYearMap.get(planMap.key) }
									</td>
								<td></td>
								<td>
									<ul class="todo-list reset-todo-list">
										<c:forEach items="${planMap.value}" var="plan">
												<li class="todo-item">
													<div class="handle">
														<div>
															<i class="fa fa-ellipsis-v"></i> <i
																class="fa fa-ellipsis-v"></i>
														</div>
													</div>

														<div class="pad clearfix">
															<ul class="list-inline ul-text-list">
																<li><span class="text">${courseMap[plan.courseId]}&nbsp;</span>
																	<span class="text_value" hidden="true">${plan.courseId }</span>
																	<div class="form-group">
																		<label class="sr-only">课程名称</label>
																		 <select class="selectpicker show-tick form-control" data-size="5" data-live-search="true"  data-container="body">
																		 	<c:forEach items="${courseMap }" var="course" >
																		 		<option value="${course.key }" >${course.value }</option>
																			</c:forEach>
																		</select> 
																	</div></li>
																<li><span class="text">${employeeMap[plan.counselorId]}&nbsp;</span>
																	<span class="text_value" hidden="true">${plan.counselorId }</span>
																	<div class="form-group">
																		<label class="sr-only">辅导教师</label> 
																		<select class="selectpicker show-tick form-control" data-size="5" data-live-search="true"  data-container="body">
																		 	<c:forEach items="${employeeMap }" var="employeeMap" >
																		 		<option value="${employeeMap.key }" >${employeeMap.value }</option>
																			</c:forEach>
																		</select> 
																	</div></li>
																<li><span class="text">${courseCategory[plan.courseCategory] }</span>
																	<span class="text_value courseCategory" hidden="true" >${plan.courseCategory }</span>
																	<div class="form-group">
																		<label class="sr-only">课程属性</label> <select
																			class="form-control">
																			<option value="0">必修</option>
																			<option value="1">选修</option>
																		</select>
																	</div></li>
																<li><span class="text">${courseTypeMap[plan.courseTypeId] }&nbsp;</span>
																	<span class="text_value" hidden="true">${plan.courseTypeId }</span>
																	<div class="form-group">
																		<label class="sr-only">课程类型</label>
																		 <select class="form-control" data-size="5" data-live-search="true"  data-container="body">
																		 	<c:forEach items="${courseTypeMap }" var="courseType" >
																		 		<option value="${courseType.key }" >${courseType.value }</option>
																			</c:forEach>
																		</select> 
																	</div></li>
																<li><span class="text score">${plan.score}</span>
																	<div class="form-group">
																		<label class="sr-only">学分</label> <input type="number"
																			class="form-control" placeholder="学分">
																	</div></li>
																<li><span class="text">${plan.hours }</span>
																	<div class="form-group">
																		<label class="sr-only">学时</label> <input type="number"
																			class="form-control" placeholder="学时">
																	</div></li>
																<li><span class="text">${examType[plan.examType]}</span>
																	<span class="text_value" hidden="true">${plan.examType }</span>
																	<div class="form-group">
																		<label class="sr-only">考试方式</label> <select
																			class="form-control">
																			<option value="0">网考</option>
																			<option value="1">场考</option>
																		</select>
																	</div></li>
																<li><span class="text">${plan.studyRatio }</span>
																	<div class="form-group">
																		<label class="sr-only">学习占比</label> <input type="number"
																			class="form-control studyRatio" placeholder="学习占比">
																	</div></li>
																<li><span class="text">${plan.examRatio }</span>
																	<div class="form-group">
																		<label class="sr-only">考试占比</label> <input type="number"
																			class="form-control examRatio" placeholder="考试占比">
																	</div></li>
																<li>
																	<div class="tools">
																		<i class="fa fa-edit"></i> <i class="fa fa-trash-o" planId="${plan.id}"></i>
																	</div>
																	<div class="form-group">
																		<label class="sr-only">操作</label>
																		<button class="btn btn-success btn-sure-edit"
																			type="button" data-loading-text="提交中"
																			data-complete-text="更新成功" autocomplete="off" planId="${plan.id}"  termTypeCode="${planMap.key}">确定</button>
																		<button class="btn btn-danger btn-sure-cancel" planId="${plan.id}">取消</button>
																	</div>
																</li>
															</ul>
														</div>
												</li>
										</c:forEach>
									</ul>
									<button
										class="btn btn-block text-green btn-add-course nobg margin_t5">
										<i class="fa fa-fw fa-plus"></i>添加课程
									</button>
								</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
					<button
						class="btn btn-block text-green btn-add-term nobg margin_t5">
						<i class="fa fa-fw fa-plus"></i>添加学期
					</button>
				</div>

				<div class="text-right">
					<button id="btn-back" class="btn btn-primary" onclick="history.back()">返回</button>
					<button class="left10 btn btn-success btn-pad-horizontal save-all">设置教学计划</button>
				</div>
			</div>
		</div>
	</section>

<script type="text/template" id="temp1">
<div class="alert (0) fade in tips-box" role="alert">   
	<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
	<div class="no-margin f16"><i class="icon fa (2)"></i> (1)</div>
</div>
</script>

<script type="text/template" id="temp2">
<li class="todo-item">
<div class="handle">
	<div>
		<i class="fa fa-ellipsis-v"></i> <i
			class="fa fa-ellipsis-v"></i>
	</div>
</div>

<div class="pad clearfix">
	<ul class="list-inline ul-text-list add-data">
		<li><span class="text">${courseMap['246bbc92ac1082af46a19cc570bd2bd5'] }</span>
		<span class="text_value" hidden="true">246bbc92ac1082af46a19cc570bd2bd5</span>
		<div class="form-group">
			<label class="sr-only">课程名称</label>
		 	<select name="course" class="selectpicker form-control" data-size="7" data-live-search="true"  data-container="body">
			 	<c:forEach items="${courseMap }" var="course" >
			 		<option value="${course.key }" >${course.value }</option>
				</c:forEach>
			</select> 
		</div></li>
	<li><span class="text">${employeeMap[plan.counselorId]}&nbsp;</span>
		<span class="text_value" hidden="true">${plan.counselorId }</span>
		<div class="form-group">
			<label class="sr-only">辅导教师</label> 
			<select class="selectpicker show-tick form-control" data-size="5" data-live-search="true"  data-container="body">
			 	<c:forEach items="${employeeMap }" var="employeeMap" >
			 		<option value="${employeeMap.key }" >${employeeMap.value }</option>
				</c:forEach>
			</select> 
		</div></li>
	<li><span class="text">必修</span>
		<span class="text_value courseCategory" hidden="true">0</span>
		<div class="form-group">
			<label class="sr-only">课程属性</label> <select
				class="form-control">
				<option value="0">必修</option>
				<option value="1">选修</option>
			</select>
		</div></li>
	<li><span class="text">${courseTypeMap[plan.courseTypeId] }&nbsp;</span>
		<span class="text_value" hidden="true">${plan.courseTypeId }</span>
		<div class="form-group">
			<label class="sr-only">课程类型</label>
			 <select class="form-control" data-size="5" data-live-search="true"  data-container="body">
			 	<c:forEach items="${courseTypeMap }" var="courseType" >
			 		<option value="${courseType.key }" >${courseType.value }</option>
				</c:forEach>
			</select> 
		</div></li>
	<li><span class="text score">0</span>
		<div class="form-group">
			<label class="sr-only">学分</label> <input type="number"
				class="form-control" placeholder="学分" value="0">
		</div></li>
	<li><span class="text">0</span>
		<div class="form-group">
			<label class="sr-only">学时</label> <input type="number"
				class="form-control" placeholder="学时" value="0">
		</div></li>
	<li><span class="text">网考</span>
		<span class="text_value" hidden="true">0</span>
		<div class="form-group">
			<label class="sr-only">考试方式</label> <select
				class="form-control">
				<option value="0">网考</option>
				<option value="1">场考</option>
			</select>
		</div></li>
	<li><span class="text">40%</span>
		<div class="form-group">
			<label class="sr-only">学习占比</label> <input type="number"
				class="form-control studyRatio" placeholder="学习占比" value="50">
		</div></li>
	<li><span class="text">60%</span>
		<div class="form-group">
			<label class="sr-only">考试占比</label> <input type="number"
				class="form-control examRatio" placeholder="考试占比" value="50">
		</div></li>
	<li>
		<div class="tools">
			<i class="fa fa-edit"></i> <i class="fa fa-trash-o" planId=""></i>
		</div>
		<div class="form-group">
			<label class="sr-only">操作</label>
			<button class="btn btn-success btn-sure-edit"
				type="button" data-loading-text="提交中"
				data-complete-text="更新成功" autocomplete="off" planId="" termTypeCode="">确定</button>
			<button class="btn btn-danger btn-sure-cancel" planId="">取消</button>
				</div>
			</li>
		</ul>
	</div>
</li>
</script>
<script src="${ctx}/static/plugins/jQuery/jQuery-2.1.4.min.js"></script> 
<script src="${ctx}/static/plugins/jQueryUI/jquery-ui.min.js"></script> 
<script src="${ctx}/static/bootstrap/js/jquery-confirm.js"></script>

<script src="${ctx}/static/bootstrap/js/bootstrap.min.js"></script> 
<script src="${ctx}/static/bootstrap/js/bootstrap-select.js"></script> 
<script src="${ctx}/static/bootstrap/js/bootstrap-table.js"></script> 
<script src="${ctx}/static/bootstrap/js/bootstrap-table-zh-CN.js"></script> 
<script src="${ctx}/static/bootstrap/js/defaults-zh_CN.js"></script> 

<!-- Select2 --> 
<script src="${ctx}/static/plugins/select2/select2.full.min.js"></script> 
<!-- SlimScroll 1.3.0 --> 
<script src="${ctx}/static/plugins/slimScroll/jquery.slimscroll.min.js"></script> 
<!-- ckeditor --> 
<script src="${ctx}/static/plugins/ckeditor/ckeditor.js"></script> 
<!-- AdminLTE App --> 
<script src="${ctx}/static/dist/js/app.min.js"></script> 
<!-- common js --> 
<script src="${ctx}/static/dist/js/common.js"></script> 
<script src="${ctx}/static/js/page/list.js"></script> 
<script>
$(function() {
	
	  $(".studyRatio").change(function(){
		  var value = $(this).val();
		  if(value>100){
			  $(this).val(100);
		  }else if(value<0 || value == 0){ 
			  $(this).val(0);
		  }
		  $(this).closest(".todo-item").find('.examRatio').val(100 - $(this).val());
	  });
	  
	  $(".examRatio").change(function(){
		  var value = $(this).val();
		  if(value>100){
			  $(this).val(100);
		  }else if(value<0 || value == 0){
			  $(this).val(0);
		  }
		  $(this).closest(".todo-item").find('.studyRatio').val(100 - $(this).val());
	  });
	  resetScore();
});

function resetScore(){
	var score_total = 0;
 	var score_total0 = 0;
 	var score_total1 = 0;
	
	$(".courseCategory").each(function(i,e){
		var score = parseInt($(this).closest(".todo-item").find('.score').text());
		if($(this).text() == 0){
			score_total0 = score_total0 + score;
		}else{
			score_total1 = score_total1 + score;
		}
		score_total = score_total + score;
	});
	$('#score_total').text(score_total);
	$('#score_total0').text(score_total0);
	$('#score_total1').text(score_total1);
	
	if(parseInt($('#score_base').text()) < score_total){
		$('#score_total').removeClass("text-green").addClass("text-red");
	}else{
		$('#score_total').removeClass("text-red").addClass("text-green");
	}
	if(parseInt($('#score_base0').text()) < score_total0){
		$('#score_total0').removeClass("text-green").addClass("text-red");
	}else{
		$('#score_total0').removeClass("text-red").addClass("text-green");
	}
	if(parseInt($('#score_base1').text()) < score_total1){
		$('#score_total1').removeClass("text-green").addClass("text-red");
	}else{
		$('#score_total1').removeClass("text-red").addClass("text-green");
	}
}

	  //jQuery UI sortable for the todo list
	  /* $(".todo-list").sortable({
		placeholder: "sort-highlight",
		connectWith: ".todo-list",
		handle: ".handle",
		forcePlaceholderSize: true,
		zIndex: 999999
	  }).disableSelection(); */
	  
	  $(".plan-table").on("click",".fa-edit",function(){//编辑
		var $container=$(this).closest(".ul-text-list");
		var $liCollector=$container.children("li:not(:last)");
		$liCollector.each(function(i,e){
			var $formControl=$(e).find(".form-control");
			if(i==0 || i==1){
				$(e).find('.selectpicker').selectpicker('val', $(e).find(".text_value").text());
			}else{
				if($formControl.is("input")){
					$formControl.attr("value",$(this).children(".text").text())
				}
				else if($formControl.is("select")){
					$formControl.val($(this).children(".text_value").text());
				}
			}
		});
		$container.addClass("add-data");
	  }).on("click",".btn-sure-edit",function(){//确定编辑，并提交
		var $this = $(this);
		var $container=$(this).closest(".ul-text-list");
		var $liCollector=$container.children("li:not(:last)");
		var $btn = $(this).button('loading');
		
		// 按钮的父节点的父节点是tr。  
        var tr = $(this).closest("tr");  
        var td1 = tr.find("td:first [name='termTypeCode']");
        var termTypeCode = td1.val(); 
        //var termTypeCode  = $(this).attr("termTypeCode");
        var planId  = $(this).attr("planId");
        
		//post 请求
		var vs = new Array();
		$liCollector.each(function(i,e){
			var $formControl=$(this).find(".form-control");
			if($formControl.val() == ""){
				vs[i] = 0;
			}else{
				vs[i] = $formControl.val();
			}
		});
		
		$.post("${ctx}/edumanage/gradespecialty/savePlan",
		  {
			'specialtyId':$('#specialtyId').val(),
			'gradeId':$('#gradeId').val(),
			'planId':planId,
			'termTypeCode':termTypeCode,
			'vs':vs.toString()
		  },function(data,status){
			  if(data.successful){ 
				  var planId = data.obj.id;
				  $this.attr("planId",planId);
				  $this.closest(".todo-item").find('.btn-sure-cancel').attr("planId",planId);//取消按钮
				  $this.closest(".todo-item").find('.fa-trash-o').attr("planId",planId);//删除按钮
				  
				  $liCollector.each(function(i,e){
					  var $formControl=$(e).find(".form-control");
					    if(i==0 || i==1){
							var value = $(e).find('.selectpicker').selectpicker('val');
							var text = $(e).find('.selectpicker :selected').text();
							if(text == ''){
								$(this).children(".text").text("空");
							}else{
								$(this).children(".text").text(text);
							}
							$(this).children(".text_value").text(value);
						}else{
							if($formControl.is("input")){
								if($formControl.val() === ''){
									$(this).children(".text").text("0");
								}else{
									$(this).children(".text").text($formControl.val());
								}
							}
							else if($formControl.is("select")){
								var text = $(this).find(".form-control :selected").text();
								$(this).children(".text").text(text);
								$(this).children(".text_value").text($formControl.val());
							}
						}
							
					});
				  
				  	resetScore();
						
					$btn.button('reset');
					$container.removeClass("add-data");
					$btn.html("确定").attr("data-complete-text","更新成功");
			  }else{
				  $btn.html("确定").attr("data-complete-text","更新失败");
			  }
			  showMessage(data);
		  },"json"); 
		
	  }).on("click",".btn-sure-cancel",function(){//取消
		  var id = $(this).attr("planId");
	  	  if(id == ""){
	  		$(this).closest(".todo-item").remove();
	  	  }else{
	  		var $container=$(this).closest(".ul-text-list");
			 $container.removeClass("add-data");
	  	  }
	  }).on("click",".btn-add-course",function(){//添加课程
		  
		  $(this).siblings(".todo-list").append($("#temp2").html());
		  $('.selectpicker').selectpicker('refresh'); 
		  
	  }).on("click",".fa-trash-o",function(){//删除课程
		  var planId = $(this).attr("planId");
		  $this = $(this);
		  $.confirm({
              title: '提示',
              content: '确认删除？',
              confirmButton: '确认',
              icon: 'fa fa-warning',
              cancelButton: '取消',  
              confirmButtonClass: 'btn-primary',
              closeIcon: true,
              closeIconClass: 'fa fa-close',
              confirm: function () {
            	  $.post("${ctx}/edumanage/gradespecialty/removePlan",
        				  {'planId':planId},
        				  function(data,status){
        					  if(data.successful){ 
        						  $this.closest(".todo-item").remove();
        						  resetScore();
        					  }
        					  showMessage(data);
        				  },"json"); 
              }
          });
	  });
	  
	  
	  $(".btn-add-term").click(function(){//添加学期
		  	var trLength = $(".plan-table tr").length-1;
		   //找到上一行的学期code
		  	var termTypeCode = $(".plan-table tr:eq("+trLength+")").find("td:first [name='termTypeCode']").val();
		  	if(typeof(termTypeCode) == "undefined"){
		    	termTypeCode=0;
		    }
	        var term = parseInt(termTypeCode) + 1;
	        
	        var ss = "<tr>"+
			        "<td>"+
			    	"<input type='hidden' name='termTypeCode' value='"+term+"'>"+
			    	"第"+term+"学期"+
				    "</td>"+
				    "<td></td>"+
				    "<td>"+
				    "<ul class='todo-list reset-todo-list'>"+
				    "</ul>"+
				   "<button class='btn btn-block text-green btn-add-course nobg margin_t5'><i class='fa fa-fw fa-plus'></i>添加课程</button>"+
				    "</td>"+
				   "</tr>";
			$(".plan-table").append(ss);
		  });
	  
	  
	  //提交并保存
	  $(".save-all").click(function(){
		  
		  $.post("${ctx}/edumanage/gradespecialty/createClass",
				  {
			  		'specialtyId':$('#specialtyId').val(),
					'gradeId':$('#gradeId').val()
				  },function(data,status){
					   var tmp=$("#temp1").html();
					  if(data.successful){ 
						  tmp=tmp.replace("(0)","alert-success").replace("(1)","保存成功！").replace("(2)","fa-check");
					  }else{
						  tmp=tmp.replace("(0)","alert-danger").replace("(1)","保存失败！").replace("(2)","fa-warning");
					  }
					  tmp=$(tmp);
					  $("body").append(tmp);
				  		tmp.delay(2000).queue(function(){
				  			tmp.find(".close").click();
				  		});
				  },"json"); 
	  });
</script>

</body>
</html>