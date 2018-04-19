<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>选择学位专业列表</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">添加专业</h3>
	</div>
	<form class="form-horizontal" id="listForm">
		<div class="box-body">
			<div class="box-border">
				<div class="pad-t15 clearfix">
					<div class="col-xs-6 col-sm-4">
						<div class="form-group">
							<label class="control-label col-xs-3 text-nowrap">专业代码</label>
							<div class="col-xs-9">
								<input class="form-control" type="text">
							</div>
						</div>
					</div>
					<div class="col-xs-6 col-sm-4">
						<div class="form-group">
							<label class="control-label col-xs-3 text-nowrap">专业名称</label>
							<div class="col-xs-9">
								<input class="form-control" type="text">
							</div>
						</div>
					</div>
					<div class="col-xs-6 col-sm-4">
						<button type="button" class="btn btn-primary">搜索</button>
					</div>
				</div>
			</div>
		</div>
		<div class="box-body">
			<div class="slim-Scroll" style="height: 190px; overflow: hidden;">
				<table class="batch-teacher table table-bordered table-hover table-striped vertical-mid text-center table-font margin-bottom-none">
					<thead>
						<tr>
							<th width="60"><input type="checkbox" data-role="select-all"></th>
							<th>专业代码</th>
							<th>专业名称</th>
							<th>层次</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${pageInfo.content}" var="item">
							<tr>
								<td><input type="checkbox" class="chk-item" data-json='{
									"id":"${item.specialtyBaseId}","name":"${item.specialtyName}"
								}'></td>
								<td>${item.specialtyCode}</td>
								<td>${item.specialtyName}</td>
								<td>
									<c:choose>
										<c:when test="${item.specialtyLayer == 0}">专科</c:when>
										<c:when test="${item.specialtyLayer == 2}">本科</c:when>
										<c:when test="${item.specialtyLayer == 4}">中专</c:when>
										<c:when test="${item.specialtyLayer == 6}">高起专_助力计划</c:when>
										<c:when test="${item.specialtyLayer == 8}">专升本_助力计划</c:when>
										<c:otherwise>--</c:otherwise>
									</c:choose>	
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<c:if test="${not empty(pageInfo.content)}">
					<tags:pagination page="${pageInfo}" paginationSize="10" />
				</c:if>
				<c:if test="${empty(pageInfo.content)}">
					<div class="box-border pad20 margin_t15 text-center text-orange">
						<i class="fa fa-exclamation-circle f24 vertical-middle"></i> <span class="inline-block vertical-middle margin_l5">没有搜索到相关专业！</span>
					</div>
				</c:if>

			</div>
		</div>
	</form>
</div>
<div class="text-right pop-btn-box pad">
	<span class="pull-left pad-t5"> 已选择 <b class="text-light-blue select-total">0</b> 个专业</span>
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确定</button>
</div>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/template" id="temp">
<div class="alert col-sm-6 pad-l5 pad-r5 no-pad-top no-pad-bottom margin_t5 fade in margin-bottom-none">
  <div class="box no-border bg-light-blue no-margin pad">
      <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
      <div class="text-center oh text-overflow text-nowrap" data-toggle="tooltip" title="{0}" data-container="body">{0}</div>
	  <input type="hidden" class="sid" value="{1}" />
  </div>
</div>
</script>
<script type="text/javascript">
$(function(){
	$('.slim-Scroll').slimScroll({
	    height: $(frameElement).height()-212,
	    size: '5px'
	});

	//确定选择
	$("[data-role='save-data']").click(function(event) {
	  event.preventDefault();
	  	if(self!==parent){
		  var $container=parent.$('[data-id="major-box"]');
		  var html=$('#temp').html();
		  var arr=[];
		  $(".batch-teacher tbody :checkbox:checked").each(function(index, el) {
		    var json=$(this).data('json');
		    var $item=$container.find('.sid[value="'+json.id+'"]');
		    if($item.length>0){
				return true;
			}
		    var tmp=html;
		    tmp=tmp.format(
		    	json.name,
		    	json.id
		    );
		    arr.push(tmp);
		  });

		  $container.append(arr.join(''));
		  parent.$('#scpeicaltyTip').removeClass('in');
		}
	 	parent.$.closeDialog(frameElement.api);
	});

	//全部选择
	$("[data-role='select-all']").change(function(event) {
	  var $ckCollector=$(".chk-item");
	  var $txt=$(".select-total");
	  if($(this).prop('checked')){
	    $ckCollector.prop('checked', true);
	    $txt.text($ckCollector.length);
	  }
	  else{
	    $ckCollector.prop('checked', false);
	    $txt.text(0);
	  }
	});

	$(".chk-item").click(function(event) {
	  var $txt=$(".select-total");
	  if($(this).prop('checked')){
	    $txt.text(parseInt($txt.text())+1);
	  }
	  else{
	    $txt.text(parseInt($txt.text())-1);
	  }
	});

    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
    	parent.$.closeDialog(frameElement.api);
    });

})
</script>
</body>
</html>