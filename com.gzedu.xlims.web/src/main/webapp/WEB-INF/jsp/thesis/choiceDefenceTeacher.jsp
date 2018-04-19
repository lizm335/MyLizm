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
      <h3 class="box-title">添加论文答辩老师</h3>
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
					<ul class="list-unstyled distribut-teacher-list" data-role="all-person" style="height:250px;">
						<c:forEach items="${defenceTeachers}" var="defenceTeacher">
							<li>
								<div class="checkbox">
									<label>
										<input type="checkbox" data-json='{
											"id":"${defenceTeacher.employeeId}",
											"name":"${defenceTeacher.xm}",
											"username":"${defenceTeacher.gjtUserAccount.loginAccount}"
										}'>
										<span class="name">${defenceTeacher.xm}</span>
										<small class="gray9">[${defenceTeacher.gjtUserAccount.loginAccount}]</small>
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
					<ul class="list-unstyled distribut-teacher-list" data-role='receive-person' style="height:250px;">
						
					</ul>
					
				  </div>
				  <div class="panel-footer">
			        <label class="text-no-bold"><input type="checkbox" data-role="select-all"> 全选</label>
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
$('[data-role="sure"]').click(function(event) {
  var $liCollectors=$("ul[data-role='receive-person']").children('li');
  var $container=parent.$('[data-box="'+frameElement.data.container+'"]');
  var arr=[];
  var htmlTemp=$('#temp').html();
  $liCollectors.each(function(index, el) {
  	var json=$(this).find(':checkbox').data('json');
    var tmp=htmlTemp;
    tmp=tmp.format(json.name, frameElement.data.container, json.id);
    arr.push(tmp);
    
  });

  $container.append(arr.join(''));
  
  parent.$.closeDialog(frameElement.api);
});

</script>
<!--添加老师 的模板-->
<script type="text/template" id="temp">
	<div class="alert col-sm-2 col-xs-4 pad-l5 pad-r5 no-pad-top no-pad-bottom margin_b10 fade in">
		<div class="box no-border bg-light-blue no-margin pad">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
            <div class="text-center oh text-overflow text-nowrap">{0}</div>
			<input type="hidden" name="advisers{1}" value="{2}">
        </div>
	</div>
</script>
</body>
</html>


					

