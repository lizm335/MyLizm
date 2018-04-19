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
<body>
<div class="box no-border no-shadow">
	<div class="box-header with-border">
      <h3 class="box-title">添加论文指导老师</h3>
    </div>
    <div class="box-body scroll-box">

		<div class="distribut-teacher table-block full-width no-margin no-padding">
			<div class="table-cell-block" style="width:40%;">
				<div class="panel panel-default">
				  <!-- Default panel contents -->
				  <div class="panel-heading">
				  	<div class="form-horizontal offset-margin-tb-15">
			          <div class="form-group margin-bottom-none">
			            <div class="col-xs-4 pad-t5"><b>可选老师</b></div>
			            <div class="col-xs-8">
			              <div class="input-group input-group-sm panel-search-group">
			                <input type="text" class="form-control" placeholder="输入姓名或账号查询">
			                <div class="input-group-addon"><i class="fa fa-search"></i></div>
			              </div>
			            </div>
			          </div>
			      	</div>
				  </div>
				  <div class="panel-body">
					<ul class="list-unstyled distribut-teacher-list" data-role="all-person" style="height:180px;">
						<c:forEach items="${guideTeachers}" var="guideTeacher">
							<li>
								<div class="checkbox">
									<label>
										<input type="checkbox" data-json='{
											"id":"${guideTeacher.employeeId}",
											"name":"${guideTeacher.xm}",
											"username":"${guideTeacher.gjtUserAccount.loginAccount}",
											"zp":"${guideTeacher.zp}"
										}'>
										<span class="name">${guideTeacher.xm}</span>
										<small class="gray9">[${guideTeacher.gjtUserAccount.loginAccount}]</small>
									</label>
								</div>
							</li>
						</c:forEach>
					</ul>

				  </div>
				  <div class="panel-footer">
			        <label class="text-no-bold"><input type="checkbox" data-role="select-all"> 全选</label>
			      </div>
				</div>
			</div>
			<div class="table-cell-block vertical-mid text-center">
				<div class="form-group">
					<button class="btn btn-primary" data-role="add"><span class="pad">添加&gt;</span></button>
				</div>
				<div class="form-group margin-bottom-none">
					<button class="btn btn-danger" data-role="remove"><span class="pad">&lt;移除</span></button>
				</div>
			</div>
			<div class="table-cell-block" style="width:40%;">
				<div class="panel panel-default">
				  <!-- Default panel contents -->
				  <div class="panel-heading">
				  	<div class="form-horizontal offset-margin-tb-15">
			          <div class="form-group margin-bottom-none">
			            <div class="col-xs-4 pad-t5"><b>已选老师</b></div>
			            <div class="col-xs-8">
			              <div class="input-group input-group-sm panel-search-group">
			                <input type="text" class="form-control" placeholder="输入姓名或账号查询">
			                <div class="input-group-addon"><i class="fa fa-search"></i></div>
			              </div>
			            </div>
			          </div>
			      	</div>
				  </div>
				  <div class="panel-body">
					<ul class="list-unstyled distribut-teacher-list" data-role='receive-person' style="height:180px;">
						
					</ul>
					
				  </div>
				  <div class="panel-footer">
			        <label class="text-no-bold"><input type="checkbox" data-role="select-all"> 全选</label>
			      </div>
				</div>
			</div>
		</div>

		<c:if test="${not empty list}">
			<div class="alert text-orange margin_t10 margin-bottom-none" style="background:#FFFFCC;border:#FFCC33 1px solid;">
					注意：请添加上期有未通过论文学员的指导老师（系统将默认分配上期未通过论文学员到原指导老师）：<br>
					<c:forEach items="${list}" var="map" varStatus="status">
						${map.teacherName}（${map.studentNum}学员未通过）<c:if test="${!status.last}">、</c:if>
					</c:forEach>
	        </div>
        </c:if>
    </div>
</div>
<div class="pop-btn-box pad text-right">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="cancel">取消</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="sure">确认分配</button>
</div>

<script>
$('.scroll-box').height($(window).height()-126);

$(".distribut-teacher")
//全选/全不选教师
.on("click","input[data-role='select-all']",function(){
  var $ckCollectors=$(this).closest(".panel").find(".distribut-teacher-list :checkbox");
  if($(this).is(":checked")){
    $ckCollectors.prop("checked",true);
  }
  else{
    $ckCollectors.prop("checked",false);
  }
})
//添加教师
.on("click",".btn[data-role='add']",function(){
	var $container=$(this).closest(".table-cell-block");
	var $containerLeft=$container.prev();
	var $containerRightUl=$container.next().find(".distribut-teacher-list");
	var $ckCollectors=$containerLeft.find(".distribut-teacher-list :checkbox");
	$ckCollectors.each(function(i,e){
		if($(this).is(":checked")){
			$containerRightUl.prepend($(e).prop("checked",false).closest("li"));
		}
	});
})
//移除教师
.on("click",".btn[data-role='remove']",function(){
	var $container=$(this).closest(".table-cell-block");
	var $containerLeftUl=$container.prev().find(".distribut-teacher-list");
	var $containerRight=$container.next();
	var $ckCollectors=$containerRight.find(".distribut-teacher-list :checkbox");
	$ckCollectors.each(function(i,e){
		if($(this).is(":checked")){
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
var arrangeId = '${param.arrangeId}';
$('[data-role="sure"]').click(function(event) {
  var $liCollectors=$("ul[data-role='receive-person']").children('li');
  var $container=parent.$('[data-box="'+frameElement.data.container+'"]');
  var arr=[];
  var htmlTemp=$('#temp').html();
  $liCollectors.each(function(index, el) {
  	var json=$(this).find(':checkbox').data('json');
    var tmp=htmlTemp;
    
    $.ajax({  
        type : "get",  
        url : "${ctx}/thesisArrange/findGuideNum",  
        dataType:'json',
        data : {arrangeId:arrangeId,teacherId:json.id},  
        async : false,  
        success : function(data){  
        	tmp=tmp.format(json.id, json.name, json.zp, data.num1, data.num2);
        }
	});
    
    arr.push(tmp);
    
  });

  $container.append(arr.join(''));
  parent.resetNum();
  
  parent.$.closeDialog(frameElement.api);
});

</script>

<!--添加老师 的模板-->
<script type="text/template" id="temp">
	<div class="alert col-sm-4 col-xs-6 pad-l5 no-pad-top no-pad-bottom pad-r5 margin_b10 fade in">
		<div class="box box-border no-shadow pad no-margin">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
            <div class="media no-margin">
            	<div class="media-left text-center">
            		<img src="{2}" class="img-circle" width="50" height="50">
            		<div class="margin_t5">{1}</div>
            	</div>
            	<div class="media-body media-middle">
            		共已分配指导 {3} 人
            		<div class="margin_t10">
            			本专业分配指导：
            			
            			<input type="text" name="adviserNums" value="{4}" class="form-control input-xs inline-block" style="width:45px;" 
							onkeyup="changeValue(this)"  onafterpaste="changeValue(this)">
            			
            			人
						<input type="hidden" name="advisers1" value="{0}">
            		</div>
            	</div>
            </div>
        </div>
	</div>
</script>
</body>
</html>


					

