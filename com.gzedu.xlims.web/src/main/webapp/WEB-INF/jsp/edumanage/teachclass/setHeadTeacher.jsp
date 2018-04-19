<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>AdminLTE 2 | Dashboard</title>
<%@ include file="/WEB-INF/jsp/common/jslibs_new.jsp"%>
</head>
<body>
	<div class="box no-border no-shadow">
		<div class="box-header with-border">
			<h3 class="box-title">分配班主任</h3>
		</div>
		<div class="box-body">
			<form action="${ctx}/edumanage/teachclass/saveHeadTeacher.html" method="post" id="dialogForm">
				<input type="hidden" name="classId" value="${classId}">
				<div class="distribut-teacher table-block full-width no-margin">
					<div class="table-cell-block" style="width: 40%;">
						<div class="panel panel-default">
							<!-- Default panel contents -->
							<div class="panel-heading">老师列表</div>
							<div class="panel-body">
								<div class="input-group input-group-sm margin panel-search-group">
									<input type="text" class="form-control" placeholder="输入姓名搜索">
									<div class="input-group-addon">
										<i class="fa fa-search"></i>
									</div>
								</div>

								<ul class="list-unstyled distribut-teacher-list" data-role="all-person">
									<c:if test="${not empty headTeacherList}">
										<c:forEach items="${headTeacherList}" var="map">
											<li>
												<div class="radio">
													<label>
														<input type="radio" checked name="employeeId" value="${map.ID}">
													 	<span class="name">${map.NAME}</span>
													 	<small 	class="gray9">[${map.ZGH}]</small>
													 </label>
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
							<button type="button" class="btn btn-primary" data-role="add">
								<span class="pad">添加&gt;</span>
							</button>
						</div>
						<div class="form-group margin-bottom-none">
							<button type="button" class="btn btn-danger" data-role="remove">
								<span class="pad">&lt;移除</span>
							</button>
						</div>
					</div>
					<div class="table-cell-block" style="width: 40%;">
						<div class="panel panel-default">
							<!-- Default panel contents -->
							<div class="panel-heading">已选老师</div>
							<div class="panel-body">
								<div class="input-group input-group-sm margin panel-search-group">
									<input type="text" class="form-control" placeholder="输入姓名搜索">
									<div class="input-group-addon">
										<i class="fa fa-search"></i>
									</div>
								</div>

								<ul class="list-unstyled distribut-teacher-list select-person" data-role='receive-person'>
									<c:if test="${not empty employee}">
										<c:forEach items="${employee}" var="map">
											<li>
												<div class="radio">
													<label><input type="radio" checked name="employeeId" value="${map.key}"> <span class="name">${map.value}</span><small
														class="gray9">[班主任]</small></label>
												</div>
											</li>
										</c:forEach>
									</c:if>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<div class="form-group text-right margin">
					<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="cancel">取消</button>
					<button type="button" class="btn btn-success min-width-90px" data-role="sure">保存</button>
				</div>
			</form>
		</div>
	</div>

	<script>
    $(".distribut-teacher")
    //添加教师
        .on("click",".btn[data-role='add']",function(){
            var html = $("ul[data-role='receive-person']").html();
            if($.trim(html)!='')return
            var $container=$(this).closest(".table-cell-block");
            var $containerLeft=$container.prev();
            var $containerRightUl=$container.next().find(".distribut-teacher-list");
            var $ckCollectors=$containerLeft.find(".distribut-teacher-list :radio");
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
            var $ckCollectors=$containerRight.find(".distribut-teacher-list :radio");
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

  //双击操作
    $('[data-role="all-person"]').on('dblclick', 'li', function(event) {
    	event.preventDefault();
    	var $r=$('[data-role="receive-person"]').children();
    	if($r.length>0){
    		$('[data-role="all-person"]').append($r.attr('name','r1'))
    	}
    	$('[data-role="receive-person"]').html($(this).attr('name','r2'));
    });
    $('[data-role="receive-person"]').on('dblclick', 'li', function(event) {
    	event.preventDefault();
    	$('[data-role="all-person"]').prepend($(this).attr('name','r1'));
    });

    
    /*取消*/
    $('[data-role="cancel"]').click(function(event) {
        parent.$.closeDialog(frameElement.api);
    });

    /*确认*/
    $('[data-role="sure"]').click(function(event) {
        /*插入业务逻辑*/
	
        var $liCollectors=$("ul[data-role='receive-person']").children('li');
        var $container=parent.$('.stu-list-box.on');
        var html='<li>'
            +'<span>{name}</span>'
            +'<i class="fa fa-fw fa-remove" data-toggle="tooltip" title="删除"></i>'
            +'</li>';
        $liCollectors.each(function(index, el) {
            var tmp=html;
            tmp=tmp.replace('{name}',$(this).find(".name").text());
            $container.append(tmp);
        });
        if( $liCollectors.find('input[name="employeeId"]').length==0){
        	alert('请选择班主任');
        	return;
        }
        //$liCollectors.find('input[name="employeeId"]').prop('checked',true); 
        $.post('${ctx}/edumanage/teachclass/saveHeadTeacher',{classId:'${classId}',employeeId:$liCollectors.find('input[name="employeeId"]').val()},function(data){
        	if(data.successful){
        		parent.location.reload();
        	}else{
        		alert(data.message);
        	}
        },'json');
        //parent.$.closeDialog(frameElement.api);
        
    });

</script>
</body>
</html>





