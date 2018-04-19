<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>班主任管理系统-操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<div class="box no-border no-shadow">
	<div class="box-header with-border">
      <h3 class="box-title"><c:if test="${type=='4'}">配置督导教师</c:if>
      <c:if test="${type=='2'}">配置辅导教师</c:if></h3>
    </div>
    <div class="box-body">
    <input type="hidden" value="${classId }" id="classId">
    <input type="hidden" value="${type }" id="type">
    	<div class="distribut-teacher table-block full-width no-margin">
			<div class="table-cell-block" style="width:40%;">
				<div class="panel panel-default">
				  <!-- Default panel contents -->
				  <div class="panel-heading">
				  		<c:if test="${type=='4'}">督导教师列表</c:if>
     					<c:if test="${type=='2'}">辅导教师列表</c:if></div>
				  <div class="panel-body">
					<div class="input-group input-group-sm margin panel-search-group">
						<input type="text" class="form-control" placeholder="请输入搜索内容">
						<div class="input-group-addon"><i class="fa fa-search"></i></div>
					</div>
					
					<ul class="list-unstyled distribut-teacher-list" data-role="all-person">
					<c:if test="${not empty gjtEmployeeInfoList }">
						<c:forEach items="${gjtEmployeeInfoList }" var ="gjtEmployeeInfoItems">
						<li>
							<input type="hidden" value="${gjtEmployeeInfoItems.employeeId}">
							<div class="checkbox">
								<label><input type="checkbox"> <span class="name">${gjtEmployeeInfoItems.xm }</span><small class="gray9">[${gjtEmployeeInfoItems.zgh }]</small></label>
							</div>
						</li>	
						</c:forEach>
					</c:if>					
					</ul>

				  </div>
				</div>
			</div>
			<div class="table-cell-block vertical-mid text-center">
				<div class="form-group">
					<label class="sr-only">添加</label>
					<button class="btn btn-primary" data-role="add"><span class="pad">添加&gt;</span></button>
				</div>
				<div class="form-group margin-bottom-none">
					<label class="sr-only">移除</label>
					<button class="btn btn-danger" data-role="remove"><span class="pad">移除&gt;</span></button>
				</div>
			</div>
			<div class="table-cell-block" style="width:40%;">
				<div class="panel panel-default">
				  <!-- Default panel contents -->
				  <div class="panel-heading">
				  <c:if test="${type=='4'}">已选督导教师</c:if>
     			  <c:if test="${type=='2'}">已选辅导教师</c:if>
				  </div>
				  <div class="panel-body">
					<div class="input-group input-group-sm margin panel-search-group">
						<input type="text" class="form-control" placeholder="请输入搜索内容">
						<div class="input-group-addon"><i class="fa fa-search"></i></div>
					</div>
					
					<ul class="list-unstyled distribut-teacher-list" data-role='receive-person'>
					<c:if test="${not empty employeeInfo }">
						<li>
							<input type="hidden" value="${employeeInfo.employeeId}">
							<div class="checkbox">
								<label><input type="checkbox"> <span class="name">${employeeInfo.xm }</span><small class="gray9">[${employeeInfo.zgh }]</small></label>
							</div>
						</li>	
					</c:if>						
					</ul>
					
				  </div>
				</div>
			</div>
		</div>
    </div>
</div>
<div class="pop-btn-box pad text-right">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="cancel">取消</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="sure">确认分配</button>
</div>
<script>
$(".distribut-teacher")
//添加教师
.on("click",".btn[data-role='add']",function(){
	var $container=$(this).closest(".table-cell-block");
	var $containerLeft=$container.prev();
	var $containerRightUl=$container.next().find(".distribut-teacher-list");
	
	var $lis = $containerRightUl.find("li");
	if ($lis.length == 0) {
		var $ckCollectors=$containerLeft.find(".distribut-teacher-list :checkbox");
		$ckCollectors.each(function(i,e){
			if($(this).is(":checked")){
				$(e).attr("name","def");
				$containerRightUl.prepend($(e).prop("checked",false).closest("li"));
			}
		});
	}
	
})
//移除教师
.on("click",".btn[data-role='remove']",function(){
	var $container=$(this).closest(".table-cell-block");
	var $containerLeftUl=$container.prev().find(".distribut-teacher-list");
	var $containerRight=$container.next();
	var $ckCollectors=$containerRight.find(".distribut-teacher-list :checkbox");
	$ckCollectors.each(function(i,e){
		if($(this).is(":checked")){
			$(e).removeAttr("name");
			$containerLeftUl.prepend($(e).prop("checked",false).closest("li"));
		}
	});
})
//搜索教师
.on("keyup",".panel-search-group > input",function(){
	var $that=$(this);
	var $liCollectors=$(this).closest(".panel").find(".distribut-teacher-list > li");
	$liCollectors.each(function(i,e){
		var searchTxt=$(this).text();
		if(searchTxt.indexOf($that.val())==-1){
			$(this).hide();
		}
		else{
			$(this).show();
		}
	});
});

/*取消*/
$('[data-role="cancel"]').click(function(event) {
  	parent.$.closeDialog(frameElement.api);
});

/*确认*/
$('[data-role="sure"]').click(function(event) {
  /*插入业务逻辑*/
  var $liCollectors=$("ul[data-role='receive-person']").children('li');
  var employeeIdList=new Array();
  for(var i = 0;i<$liCollectors.children('input').length;i++){
	  employeeIdList.push($liCollectors.children('input')[i].defaultValue);
  }
  $.ajax({
	    type: 'post',
	    url:"${ctx }/edumanage/courseclass/createClassTeacher",
	 	data: { "employeeIdList": employeeIdList.join(","),"classId":$("#classId").val(), "type":$("#type").val()},
		success:function(data){	
			  //parent.$.closeDialog(frameElement.api);
			  var data = JSON.parse(data); 
			  if(data.successful) {
				  $.get("${ctx }/api/openclass/initCourseTeacher?CLASS_ID="+$("#classId").val(), function(result){});
				  parent.$.closeDialog(frameElement.api);
				  parent.location.reload();
			  } else {
				  alert(data.message);	
			  }
		 }
	});
});

</script>
</body>
</html>


					

