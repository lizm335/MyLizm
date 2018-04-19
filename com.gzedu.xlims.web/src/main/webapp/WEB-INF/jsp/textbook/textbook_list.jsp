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
	              <label class="control-label col-sm-3 text-nowrap">书号</label>
	              <div class="col-sm-9">
	                <input class="form-control" type="text" name="search_LIKE_textbookCode" value="${param.search_LIKE_textbookCode}">
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">教材名称</label>
	              <div class="col-sm-9">
	                <input class="form-control" type="text" name="search_LIKE_textbookName" value="${param.search_LIKE_textbookName}">
	              </div>
	            </div>
	          </div>

	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">作者</label>
	              <div class="col-sm-9">
	                <input class="form-control" type="text" name="search_LIKE_author" value="${param.search_LIKE_author}">
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">版次</label>
	              <div class="col-sm-9">
	                <input class="form-control" type="text" name="search_LIKE_revision" value="${param.search_LIKE_revision}">
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">ISBN</label>
	              <div class="col-sm-9">
	                <input class="form-control" type="text" name="search_LIKE_isbn" value="${param.search_LIKE_isbn}">
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
	
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	
	<div class="box margin-bottom-none">
		<div class="box-header with-border">
			<h3 class="box-title pad-t5">教材列表</h3>
			<div class="pull-right no-margin">
				<c:if test="${isBtnExport }">
					<a href="exportTextbook" class="btn btn-default btn-sm"><i class="fa fa-fw fa-sign-out"></i> 批量导出教材</a>
				</c:if>
				<c:if test="${isBtnImport }">
					<button type="button" class="btn btn-default btn-sm left10 btn-import"><i class="fa fa-fw fa-sign-in"></i> 批量导入教材</button>
				</c:if>
				<c:if test="${isBtnCreate }">
					<a href="create" class="btn btn-default btn-sm left10 btn-add"><i class="fa fa-fw fa-plus"></i> 新增教材</a>
				</c:if>
			</div>
		</div>
		<div class="box-body">
			<div class="filter-tabs filter-tabs2 clearfix">
				<ul class="list-unstyled">
					<li lang=":input[name='search_EQ_textbookType'], :input[name='search_EQ_status']" <c:if test="${empty param['search_EQ_textbookType'] && empty param['search_EQ_status']}">class="actived"</c:if>>全部(${primaryTextbook + reviewTextbook})</li>
					<li value="1" role=":input[name='search_EQ_textbookType']" lang=":input[name='search_EQ_status']"  <c:if test="${param['search_EQ_textbookType'] == 1 }">class="actived"</c:if>>主教材(${primaryTextbook})</li>
					<li value="2" role=":input[name='search_EQ_textbookType']" lang=":input[name='search_EQ_status']"  <c:if test="${param['search_EQ_textbookType'] == 2 }">class="actived"</c:if>>复习资料(${reviewTextbook})</li>
					<li value="1" role=":input[name='search_EQ_status']" lang=":input[name='search_EQ_textbookType']"  <c:if test="${param['search_EQ_status'] == 1 }">class="actived"</c:if>>已启用(${enabled})</li>
					<li value="0" role=":input[name='search_EQ_status']" lang=":input[name='search_EQ_textbookType']"  <c:if test="${param['search_EQ_status'] == '0' }">class="actived"</c:if>>已停用(${disabled})</li>
				</ul>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered table-striped text-center vertical-mid table-font">
					<thead>
		              <tr>
		              	<th>封面</th>
		                <th>书号</th>
		                <th>ISBN</th>
						<th>货位号</th>
		                <th>教材名称</th>
		                <th width="20%">使用课程</th>
		                <th>作者</th>
		                <th>定价</th>
		                <th>状态</th>
		                <th>操作</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
									<c:if test="${not empty entity}">
						            	<tr>
						            		<td><img alt="" src="${entity.cover}" width="100px" height="100px" /></td>
						            		<td>
						            			${entity.textbookCode}
						            		</td>
						            		<td>
						            			${empty(entity.isbn)?'--':entity.isbn}
						            		</td>
											<td>
												${entity.positionNo}
											</td>
						            		<td>
						            			${entity.textbookName}
						            		</td>
						            		<td>
						            			<c:if test="${empty entity.gjtCourseList}">--</c:if>
						            			<c:if test="${not empty entity.gjtCourseList}">
													<c:forEach  items="${entity.gjtCourseList}" var="item" varStatus="status">
														${item.kcmc}
														<c:if test="${!status.last}">, </c:if>
													</c:forEach>
												</c:if>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${not empty entity.author}">
						            					${entity.author}
						            				</c:when>
						            				<c:otherwise>--</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${not empty entity.discountPrice}">
						            					￥${entity.discountPrice}
						            				</c:when>
						            				<c:otherwise>--</c:otherwise>
						            			</c:choose>
						            		</td>
					            			<c:choose>
					            				<c:when test="${entity.status == 1}">
					            					<td class="text-green">已启用</td>
					            				</c:when>
					            				<c:otherwise>
					            					<td class="text-red">已停用</td>
					            				</c:otherwise>
					            			</c:choose>
						            		<td>
						            			<c:if test="${isBtnUpdate }">
						            				<a href="update/${entity.textbookId}" class="operion-item" data-toggle="tooltip" title="编辑"><i class="fa fa-edit"></i></a>
						            			</c:if>
						            			<c:if test="${isBtnView }">
						            			<a href="view/${entity.textbookId}" class="operion-item" data-toggle="tooltip" title="详情"><i class="fa fa-view-more"></i></a>
						            			</c:if>
						            			<c:if test="${isBtnViewStock }">
						            			<a href="javascript:viewStock('${entity.textbookCode}', '${entity.textbookName}', '${entity.gjtTextbookStock.stockNum}', '${entity.gjtTextbookStock.inStockNum}', '${entity.gjtTextbookStock.outStockNum}', '${entity.gjtTextbookStock.planOutStockNum}')" 
						            				class="operion-item" data-toggle="tooltip" title="查看库存"><i class="fa fa-fw fa-eye"></i></a>
						            			</c:if>
						            		</td>
						            	</tr>
						            </c:if>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="11">暂无数据</td>
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

<div id="viewStockDiv" class="box no-border no-shadow" style="display: none;">
	<div class="box-body slim-Scroll">
		<div class="panel panel-default">
	      <div class="panel-heading">
	        <h3 class="panel-title">库存信息</h3>
	      </div>
	      <div class="panel-body">
	        <div class="table-responsive margin-bottom-none">
                <table class="table-gray-th table-font">
                  <tbody>
	                  <tr>
	                    <th class="text-right">
	                      	书号
	                    </th>
	                    <td id="textbookCode">
	                      	
	                    </td>
	                  </tr>
	                  <tr>
	                    <th class="text-right">
	                      	教材名称
	                    </th>
	                    <td id="textbookName">
	                      	
	                    </td>
	                  </tr>
	                  <tr>
	                    <th class="text-right">
	                      	库存数量
	                    </th>
	                    <td id="stockNum">
	                      	
	                    </td>
	                  </tr>
	                  <tr>
	                    <th class="text-right">
	                      	总入库数量
	                    </th>
	                    <td id="inStockNum">
	                      	
	                    </td>
	                  </tr>
	                  <tr>
	                    <th class="text-right">
	                      	总出库数量
	                    </th>
	                    <td id="outStockNum">
	                      	
	                    </td>
	                  </tr>
	                  <tr>
	                    <th class="text-right">
	                      	待出库数量
	                    </th>
	                    <td id="planOutStockNum">
	                      	
	                    </td>
	                  </tr>
                	</tbody>
                </table>
              </div>
	      </div>
	    </div>
	</div>
</div>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
$(".btn-import").click(function(event) {
	/* $.mydialog({
	  id:'import',
	  width:600,
	  height:415,
	  zIndex:11000,
	  content: 'url:importForm'
	}); */
	var actionName = ctx+"/textbook/import";
	var downloadFileUrl = ctx+"/excelImport/downloadModel?name=教材导入表.xls";
	var content1 = "为了方便你的工作，我们已经准备好了《教材导入表》的标准模板<br>你可以点击下面的下载按钮，下载标准模板。"
	var content2 = "请选择你要导入的《教材导入表》";
	excelImport(actionName, "file", "textbook", downloadFileUrl, null, "批量导入教材", null, null, null, content1, content2);
});

// filter tabs
/* $(".filter-tabs li").click(function(event) {
	var $li = $(this);
	$(".filter-tabs li").each(function(index, el) {
		if (el == $li.context && index == 0) {
			window.location.href = "list";
		} else if (el == $li.context && index == 1) {
			window.location.href = "list?search_EQ_textbookType=1";
		} else if (el == $li.context && index == 2) {
			window.location.href = "list?search_EQ_textbookType=2";
		} else if (el == $li.context && index == 3) {
			window.location.href = "list?search_EQ_status=1";
		} else if (el == $li.context && index == 4) {
			window.location.href = "list?search_EQ_status=0";
		}
	});
}); */

function viewStock(textbookCode, textbookName, stockNum, inStockNum, outStockNum, planOutStockNum) {
	$("#textbookCode").html(textbookCode);
	$("#textbookName").html(textbookName);
	$("#stockNum").html(stockNum);
	$("#inStockNum").html(inStockNum);
	$("#outStockNum").html(outStockNum);
	$("#planOutStockNum").html(planOutStockNum);
	$.mydialog({
		id:'viewStock',
		width:300,
		height:350,
		zIndex:11000,
		content: $("#viewStockDiv").html()
	});
}

</script>
</body>
</html>
