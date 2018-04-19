<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<form id="listForm" class="form-horizontal">
<input type="hidden" name="action" value="${param.action }" id="actionFlag"/>
<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">选择教材</h3>
	</div>
	<div class="box-body">
		<div class="box-border">
		        <div class="pad-t15 clearfix">
		          <div class="col-xs-6">
		            <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">教材名称</label>
		              <div class="col-xs-9">
		                 <input class="form-control" type="text" name="search_LIKE_textbookName" value="${param.search_LIKE_textbookName}">
		              </div>
		            </div>
		          </div>
		          <div class="col-xs-6">
		            <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">作者</label>
		              <div class="col-xs-9">
		               <input class="form-control" type="text" name="search_LIKE_author" value="${param.search_LIKE_author}">
		              </div>
		            </div>
		          </div>
		        </div>
	    </div>
	</div>
	<div class="box-footer">
      <div class="btn-wrap">
			<button type="button" class="btn btn-default btn-reset">重置</button>
		</div>
		<div class="btn-wrap">
			<button type="submit" class="btn btn-primary">搜索</button>
		</div>
    </div>
	<div class="box-body">
		<div class="margin_b10 clearfix">
			<h3 class="cnt-box-title f16">教材列表</h3>
			<!-- <div class="pull-right text-orange">
				注：选不到教材？先去 <a href="新增教材.html" data-role='single-page'><u>创建教材</u></a> 吧！
			</div> -->
		</div>
		
		<div class="table-responsive">
			<div class="slim-Scroll" style="height:190px;overflow:hidden;">
				<table class="batch-teacher table table-bordered table-hover table-striped vertical-mid text-center table-font margin-bottom-none">
					<thead>
						<tr>
							<th>选择</th>
							<th>书号</th>
							<th>教材名称</th>
							<th>教材类型</th>
							<th>作者</th>
						</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${not empty pageInfo.content}">
							<c:forEach items="${pageInfo.content}" var="entity">
								<c:if test="${not empty entity}">
									<tr>
										<td>
											<i class="fa fa-circle-o"></i>
										</td>
										<td class="order-num">
											${entity.textbookCode}
											<input type="hidden" class="textbookIds" value="${entity.textbookId}"/>
										</td>
										<td class="name">${entity.textbookName}</td>
										<td>
										<c:if test="${entity.textbookType == 1}">主教材</c:if>
										<c:if test="${entity.textbookType == 2}">复习资料</c:if>
										<td>${entity.author}</td>
									</tr>
									</c:if>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td align="center" colspan="10">
									  注：选不到教材？先去创建教材吧！
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
				<tags:pagination page="${pageInfo}" paginationSize="3" />
			</div>				
		</div>
	</div>
</div>
</form>
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确定</button>
</div>
<script type="text/javascript">

$(function(){
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
		      '<span class="select2-name" title="#name#" data-toggle="tooltip" data-container="body"  data-order="#order#">#name#</span>',
		      '<input type="hidden" name = "textbookIds" value="#textbookIds#"/>',			      
		      '</li>'
		  ];
		  $(".batch-teacher tr.on").each(function(index, el) {
		    var tmp=html.join("");
		    var name=$.trim($(this).find(".name").text());
		    var order=$.trim($(this).find(".order-num").text());
		    var textbookIds=$.trim($(this).find(".textbookIds").val());
		    tmp=tmp.replace(/#name#/g,name);
		    tmp=tmp.replace(/#order#/g,order);
		    tmp=tmp.replace(/#textbookIds#/g,textbookIds);
		    $container.append(tmp);
		  });
		}
	 	parent.$.closeDialog(frameElement.api);
	});

	// 选中
	$(".batch-teacher").on('click', 'tr', function(event) {
		event.preventDefault();
		if($(this).hasClass('on')){
			$(this).removeClass('on');
		}
		else{
			$(this).addClass('on');
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
