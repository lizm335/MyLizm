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
<body class="inner-page-body">

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li class="active">教材管理</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<input type="hidden" name="search_EQ_status" value="">
	<div class="box">
	    <div class="box-body">
	        <div class="row pad-t15">
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">学期</label>
	              <div class="col-sm-9">
		               <select name="search_gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
							<option value="" selected="selected">请选择</option>
							<c:forEach items="${termMap}" var="map">
								<option value="${map.key}"<c:if test="${map.key==param.search_gradeId}">selected='selected'</c:if> >${map.value}</option>
							</c:forEach>
						</select>
	              </div>
	            </div>
	          </div>
	        </div>
	    </div><!-- /.box-body -->
	    <div class="box-footer">
	      <div class="pull-right"><button type="reset" class="btn min-width-90px btn-default btn-reset">重置</button></div>
	      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
	    </div><!-- /.box-footer-->
	</div>
	
	
	<div class="box margin-bottom-none">
		<div class="box-header with-border">
			<h3 class="box-title pad-t5">教材列表</h3>
		</div>
		<div class="box-body">
			<div class="table-responsive">
				<table class="table table-bordered table-striped text-center vertical-mid table-font">
					<thead>
		              <tr>
		              	<th>学期</th>
		                <th>书号</th>
		                <th>教材名称</th>
		                <th>使用课程</th>
		                <th>操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
									<c:if test="${not empty entity}">
						            	<tr>
						            		<td>${grade }</td>
						            		<td>
						            			${entity.textbookCode}
						            		</td>
						            		<td>
						            			${entity.textbookName}
						            		</td>
						            		<td>
						            			<c:if test="${not empty entity.gjtCourseList}">
													<c:forEach  items="${entity.gjtCourseList}" var="item" varStatus="status">
														${item.kcmc}
														<c:if test="${!status.last}">, </c:if>
													</c:forEach>
												</c:if>
						            		</td>
						            		
						            		<td>
						            			<a href="javascript:void(0);" class="operion-item remove-tb" data-tid="${entity.textbookId }" data-gid="${param.search_gradeId}" title="删除" data-temptitle="删除">
													<i class="fa fa-fw fa-trash-o text-red"></i>
												</a>
						            		</td>
						            	</tr>
						            </c:if>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="5">暂无数据</td>
								</tr>
							</c:otherwise>
						</c:choose>
		            </tbody>
				</table>
			</div>
			<tags:pagination page="${pageInfo}" paginationSize="5" />
		</div>
	</div>
	</form>
</section>



<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
$(".remove-tb").click(function(event) {
    var gradeId=$(this).data('gid');
    var textbookId=$(this).data('tid');
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
            var postIngIframe = $.formOperTipsDialog({
				text : '数据提交中...',
				iconClass : 'fa-refresh fa-spin'
		    });
            $.get('${ctx}/textbookArrange/removeTextbook',{gradeId:gradeId,textbookId:textbookId},function(data){
				if(data.successful){
				    postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
					postIngIframe.find('[data-role="tips-icon"]').attr('class', 'fa fa-check-circle');
					location.reload();
				}else{
				    alert(data.message);
					$.closeDialog(postIngIframe);
				} 
			},'json');
        }
	});

});
</script>
</body>
</html>
