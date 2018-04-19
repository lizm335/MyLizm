<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>选择指定分班规则的学习中心</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>

<div class="box no-border">
	<div class="box-header with-border">
		<h3 class="box-title">选择指定分班规则的学习中心</h3>
	</div>
	<form class="form-horizontal" id="listForm" action="addStudyCenter.html">
	<div class="box-body cn-container">
	  <div class="row">
		<div class="col-sm-4 col-xs-6">
		  <div class="form-group">
			<label class="control-label col-sm-3 text-nowrap">学习中心代码</label>
			<div class="col-sm-5">
			 <input class="form-control" type="text" name="search_LIKE_code" value="${param.search_LIKE_code}">
			</div>
		  </div>
		</div>
		<div class="col-sm-4 col-xs-6">
		  <div class="form-group">
			<label class="control-label col-sm-3 text-nowrap">学习中心名称</label>
			<div class="col-sm-5">
			 	<input class="form-control" type="text" name="search_LIKE_orgName" value="${param.search_LIKE_orgName}">
		 	  	<input class="form-control" type="hidden" id="isEnabled" name="search_EQ_isEnabled" value="${param.search_EQ_isEnabled}">
              	<input class="form-control" type="hidden" id="auditStatus" name="search_EQ_auditStatus" value="${param['search_EQ_auditStatus']}">
			</div>
		  </div>
		</div>				
	  </div>
	  <div class="box-body">
	  	<div class="margin_b10 text-right">
			<button type="button" class="btn btn-default min-width-90px  btn-reset">重置</button>
			<button type="submit" class="btn btn-success min-width-90px margin_l15 search">搜索</button>
		</div>
		<table class="table table-bordered table-striped margin-bottom-none text-center vertical-middle f12">
		  <thead>
		    <tr>
		      <th width="40"><input type="checkbox" data-role="select-all"></th>
		      <th width="16%">学习中心代码</th>
		      <th>学习中心名称</th>
		      <th width="23%">上级单位</th>
		      <th width="23%">服务项</th>
		      <th width="9%">状态</th>
		    </tr>
		  </thead>
		  <tbody>
		  <c:choose>
		   <c:when test="${not empty pageInfo.content}">
		    <c:forEach items="${pageInfo.content}" var="entity">
		     <c:if test="${not empty entity}">
		    <tr>
		      <td><input type="checkbox" class="chk-item" data-json='{
		      	"centerNum":"${entity.code}",
		      	"centerName":"${entity.orgName}",
		      	"studyCenterId":"${entity.id}"
		      }'></td>
		      <td>${entity.code}</td>
		      <td>${entity.orgName}</td>
		      <td>${entity.parentOrgName}</td>
		      <td>
		      	<div class="inline-block">
			      	<ul class="list-unstyled text-left">
			      		<c:if test="${not empty entity.serviceList }">
				  			<c:forEach items="${entity.serviceList }" var="service" varStatus="status">
				  				<li>${ status.index + 1}、${service}</li>
				  			</c:forEach>
					  	</c:if>
			      	</ul>
		      	</div>
		      </td>
		      <td>
		      	<c:if test="${entity.isEnabled eq '1'}">已启用</c:if>
		      	<c:if test="${entity.isEnabled eq '0'}">已停用</c:if>
		      </td>
		    </tr>
		    </c:if>
			</c:forEach>
		    </c:when>
		    <c:otherwise>
			  <tr><td align="center" colspan="9">暂无数据</td></tr>
			</c:otherwise>
		    </c:choose>
		  </tbody>
		</table>
		<tags:pagination page="${pageInfo}" paginationSize="10"/>
	</div>
   </div>
  </form>
</div>

<div class="text-right pop-btn-box pad">
	<span class="pull-left pad-t5">
		已选择 <b class="text-light-blue select-total">0</b> 个学习中心
	</span>
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">关闭</button>
	<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确认选择</button>
</div>
<!--html模板-->
<script type="text/template" id="temp">	
	<tr id="tr">
		<td>{0}</td>
		<input type="hidden" id="studyCenterId" name="xxzxId" value={2}>
		<td id="td">{1}</td>		
		<td>
			<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove"><i class="fa fa-trash-o text-red"></i></a>
		</td>	
	</tr>

</script>

<script type="text/javascript">
//确定选择
$("[data-role='save-data']").click(function(event) {
	var $container=parent.$('[data-id="center-box"]');
	var temp=$('#temp').html(), arr=[];
	$(".chk-item:checked").each(function(index, el) {
		var json=$(this).data('json');
		var html=temp;		
		html=html.format(
			json.centerNum,
			json.centerName,
			json.studyCenterId
		);
		arr.push(html);
	});
	if(arr.length>0){
		$container.append(arr.join(''));
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

$(".cn-container").slimScroll({
	height:$(frameElement).height()-106
});
</script>
</body>
</html>
