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
<section class="content">
	<div class="box-header with-border">
		<h3 class="box-title">选择课程</h3>
	</div>
	<form id="listForm" class="form-horizontal">
		<div class="box-body">
			<div class="box-border">
				<div class="pad-t15 clearfix">
					<div class="col-xs-6">
		            <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">课程</label>
		              <div class="col-xs-9">
		                <input class="form-control" type="text" name="COURSE_NAME" value="${param.COURSE_NAME }"/>
		              </div>
		            </div>
		          </div>
		          <div class="col-xs-6">
		            <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">课程类型</label>
		              <div class="col-xs-9">
		                <select class="form-control" name="COURSE_TYPE" id="course_type" data-size="5" data-live-search="true">
		                  <option value="">请选择</option>
		                  <c:forEach items="${courseTypeMap}" var="map">
								<option value="${map.key}">${map.value}</option>
						  </c:forEach>
		                </select>
		              </div>
		            </div>
		          </div>
		          <div class="col-xs-6">
		            <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">培养层次</label>
		              <div class="col-xs-9">
		                <select class="form-control" id="pycc_id" name="PYCC" data-size="5" data-live-search="true">
		                  <option value="">请选择</option>
		                  <c:forEach items="${pyccMap}" var="map">
								<option value="${map.key}" <c:if test="${map.key==param.PYCC}">selected='selected'</c:if>>${map.value}</option>
						  </c:forEach>
		                </select>
		              </div>
		            </div>
		          </div>
		          <div class="col-xs-6">
		            <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">主讲教师</label>
		              <div class="col-xs-9">
		                <input type="text" class="form-control" placeholder="主讲教师" name="EMPLOYEE_NAME" value="${param.EMPLOYEE_NAME }"/>
		              </div>
		            </div>
		          </div>
				</div>
			</div>
		</div>
		<div class="box-footer text-right">
			<div class="pull-right">
				<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
			</div>
			<div class="pull-right margin_r15">
				<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
			</div>
		</div>
		<div class="box-body">
			<div class="margin_b10 clearfix">
			<h3 class="cnt-box-title f16">课程列表</h3>
			<div class="pull-right text-orange">
				注：选不到课程？先去 <a href="${ctx}/edumanage/course/create" data-role='single-page'><u>创建课程</u></a> 吧！
			</div>
			</div>
			
			<div class="table-responsive">
				<div class="slim-Scroll" style="height:190px;overflow:hidden;">
					<table class="batch-teacher table table-bordered table-hover table-striped vertical-mid text-center table-font margin-bottom-none">
						<thead>
							<tr>
								<th>选择</th>
								<th>课程</th>
								<th>课程类型</th>
								<th>培养层次</th>
								<th>主讲教师</th>
								<th>学时</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${pageInfo.content }" var="entity">
							<tr>
								<td>
									<i class="fa fa-square-o"></i>
									<input type="hidden" class="course_id" value="${entity.COURSE_ID }"/>
								</td>
								<td>
									<div class="name">${entity.COURSE_NAME }</div>
									<div class="gray9">（<span class="course-order">${entity.KCH }</span>）</div>
									<input type="hidden" class="kch" value="${entity.KCH }"/>
								</td>
								<td>${entity.COURSE_TYPE_NAME }</td>
								<td>${entity.PYCC_NAME }</td>
								<td>${entity.EMPLOYEE_NAME }</td>
								<td>${entity.HOUR }</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
					<tags:pagination page="${pageInfo}" paginationSize="5" />
				</div>
			</div>
		</div>		
	</form>
</section>

<div class="text-right pop-btn-box pad">
<!-- 
	<span class="pull-left pad-t5">
			已选择课程：<b class="text-light-blue inline-block selected-course" width></b>
	</span>
-->
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确定</button>
</div>

<input type="hidden" value="" data-role="save-container">
<script type="text/javascript">

$(function(){
	
	//缓存操作结果
	var $saveContainer=$('[data-role="save-container"]');
	
	$("#pycc_id").selectpicker();
	$("#course_type").selectpicker();
	
	$('.slim-Scroll').slimScroll({
	    height: 190,
	    size: '5px'
	});

	//确定选择
	$("[data-role='save-data']").click(function(event) {
	  event.preventDefault();
	  if(self!==parent){
		  var $container=parent.$(".select-container-ul.on");
		  var html=[
		    '<li class="select2-selection__choice">',
		      '<span class="select2-selection__choice__remove" title="删除" data-toggle="tooltip" data-container="body" data-role="delete">×</span>',
		      '<span class="select2-name" title="#name#" data-toggle="tooltip" data-container="body" data-order="#order#">#name#</span>',
		      '<input type="hidden" class="course_ids" name="COURSE_${subjectCode}" value="#courseId#"/>',
		      '<input type="hidden" class="kch_ids" name="kch_${subjectCode}" value="#kch#"/>',
		    '</li>'
		  ];
		  $container.empty();
		  $(".batch-teacher tr.on").each(function(index, el) {
		    var tmp=html.join("");
		    debugger;
		    var courseId = $.trim($(this).find("input").val());
		    var name=$.trim($(this).find(".name").text());
		    var order=$.trim($(this).find(".course-order").text());
		    tmp=tmp.replace(/#courseId#/g,courseId);
		    tmp=tmp.replace(/#kch#/g,order);
		    tmp=tmp.replace(/#name#/g,name);
		    tmp=tmp.replace(/#order#/g,order);
		    $container.append(tmp);
		  });
		}
	 	parent.$.closeDialog(frameElement.api);
	});

	// 选中
	$(".batch-teacher").on('click', 'tr', function(event) {
		/* event.preventDefault();
		if($(this).hasClass('on')){
			$(this).removeClass('on');
			$(".selected-course").text("");
		}
		else{
			$(this).addClass('on');
			$(".selected-course").text($(this).find(".name").parent().text());
		}
		$(this).siblings('tr').removeClass('on'); */
		event.preventDefault();
		var $c=$(this).find(".name");
		var vList=$saveContainer.val();
		var arrSave=vList==''?[]:vList.split(',');
		var name=$.trim($c.text());
		var uid=$(this).find("input").val();

		if($(this).hasClass('on')){
			$(this).removeClass('on');
			arrSave=$.map( arrSave, function(n){
				if( (n.split('|'))[1]==uid ){
					return null;
				}
				else{
					return n;
				}
			});
		}
		else{
			$(this).addClass('on');
			arrSave.push(name+'|'+uid);
		}

		$saveContainer.val(arrSave.join(','))

	});

    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
    	parent.$.closeDialog(frameElement.api);
    });

})
</script>
</body>
</html>

