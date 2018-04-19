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

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li class="active">库存管理</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs nav-tabs-lg">
			<li class="active"><a href="${ctx}/textbookStock/list" target="_self">库存管理</a></li>
			<li><a href="${ctx}/textbookStockOperaBatch/list" target="_self">库存操作申请</a></li>
		</ul>
		<div class="tab-content">
			<div class="box box-border">
			    <div class="box-body">
			        <div class="row pad-t15">
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">教材类型</label>
			              <div class="col-sm-9">
			                <select name="search_EQ_textbookType" class="selectpicker form-control">
			                  <option value="">全部</option>
							  <option value="1" <c:if test="${param['search_EQ_textbookType'] == 1}">selected="selected"</c:if> >主教材</option>
							  <option value="2" <c:if test="${param['search_EQ_textbookType'] == 2}">selected="selected"</c:if> >复习资料</option>
			                </select>
			              </div>
			            </div>
			          </div>
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">书号</label>
			              <div class="col-sm-9">
			                <input type="text" class="form-control" name="search_LIKE_textbookCode" value="${param.search_LIKE_textbookCode}">
			              </div>
			            </div>
			          </div>
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">书名</label>
			              <div class="col-sm-9">
			                <input type="text" class="form-control" name="search_LIKE_textbookName" value="${param.search_LIKE_textbookName}">
			              </div>
			            </div>
			          </div>
			       </div>
			       <div class="row pad-t15">
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">库存状态</label>
			              <div class="col-sm-9">
			                <select name="search_EQ_stockStatus" class="selectpicker form-control">
			                  <option value="">全部</option>
							  <option value="1" <c:if test="${param['search_EQ_stockStatus'] == 1}">selected="selected"</c:if> >库存充足</option>
							  <option value="2" <c:if test="${param['search_EQ_stockStatus'] == 2}">selected="selected"</c:if> >库存不足</option>
			                </select>
			              </div>
			            </div>
			          </div>
			        </div>
			    </div><!-- /.box-body -->
			    <div class="box-footer">
			      <div class="pull-right"><button type="reset" class="btn min-width-90px btn-default">重置</button></div>
			      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
			    </div><!-- /.box-footer-->
			</div>
	
			<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
			<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	
			<div class="box box-border margin-bottom-none">
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">库存列表</h3>
						<div class="pull-right">
							<c:if test="${isBtnImport }">
								<a class="btn btn-default btn-sm btn-import"><i class="fa fa-fw fa-sign-in"></i> 批量操作库存</a>
							</c:if>
							<c:if test="${isBtnExport }">
								<a href="export" class="btn btn-default btn-sm"><i class="fa fa-fw fa-sign-out"></i> 批量导出库存</a>
							</c:if>
						</div>
					</div>
					<div class="box-body">
						<div class="filter-tabs filter-tabs2 clearfix">
							<ul class="list-unstyled">
								<li lang=":input[name='search_EQ_stockStatus']" <c:if test="${empty param['search_EQ_stockStatus']}">class="actived"</c:if>>全部(${all})</li>
								<li value="1" role=":input[name='search_EQ_stockStatus']" <c:if test="${param['search_EQ_stockStatus'] == 1}">class="actived"</c:if>>库存充足(${enoughStock})</li>
								<li value="2" role=":input[name='search_EQ_stockStatus']" <c:if test="${param['search_EQ_stockStatus'] == 2}">class="actived"</c:if>>库存不足(${notEnoughStock})</li>
							</ul>
						</div>
						<div class="table-responsive">
							<table class="table table-bordered table-striped vertical-mid text-center table-font">
								<thead>
					              <tr>
					                <th>书号</th>
					                <th>书名</th>
					                <th>教材类型</th>
					                <th>总入库</th>
					                <th>总出库</th>
					                <th>剩余库存</th>
					                <th>待出库</th>
					                <th>库存状态</th>
					                <th>操作</th>
					              </tr>
					            </thead>
					            <tbody>
					            	<c:choose>
					            		<c:when test="${not empty pageInfo.content}">
											<c:forEach items="${pageInfo.content}" var="entity">
												<c:if test="${not empty entity}">
									            	<tr>
									            		<td>
									            			${entity.textbookCode}
									            		</td>
									            		<td>
									            			${entity.textbookName}
									            		</td>
									            		<td>
									            			<c:choose>
									            				<c:when test="${entity.textbookType == 1}">主教材</c:when>
									            				<c:otherwise>复习资料</c:otherwise>
									            			</c:choose>
									            		</td>
									            		<td>
									            			${entity.gjtTextbookStock.inStockNum}
									            		</td>
									            		<td>
									            			${entity.gjtTextbookStock.outStockNum}
									            		</td>
									            		<td>
									            			${entity.gjtTextbookStock.stockNum}
									            		</td>
									            		<td>
									            			${entity.gjtTextbookStock.planOutStockNum}
									            		</td>
									            		<td>
									            			<c:choose>
									            				<c:when test="${entity.gjtTextbookStock.stockNum != 0 && entity.gjtTextbookStock.stockNum >= entity.gjtTextbookStock.planOutStockNum}">
									            					<span class="text-green">
											            				库存充足
											            			</span>
									            				</c:when>
									            				<c:when test="${entity.gjtTextbookStock.stockNum < entity.gjtTextbookStock.planOutStockNum}">
									            					<span class="text-red">
											            				库存不足
											            			</span>
									            				</c:when>
									            				<c:otherwise>--</c:otherwise>
									            			</c:choose>
									            		</td>
									            		<td>
									            			<c:if test="${isBtnView}">
									            			<a href="view/${entity.textbookId}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
									            			</c:if>
									            		</td>
									            	</tr>
									            </c:if>
									        </c:forEach>
									    </c:when>
										<c:otherwise>
											<tr>
												<td align="center" colspan="9">暂无数据</td>
											</tr>
										</c:otherwise>
									</c:choose>
					            </tbody>
							</table>
						</div>
					</div>
				</div>
			<tags:pagination page="${pageInfo}" paginationSize="5" />
		</div>
	</div>
	</form>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

$(function(){
	$(".btn-import").click(function(event) {
		$.mydialog({
		  id:'oper-kc',
		  width:600,
		  height:408,
		  zIndex:11000,
		  content: 'url:importForm'
		});
	});

	// filter tabs
	/* $(".filter-tabs li").click(function(event) {
		var $li = $(this);
		$(".filter-tabs li").each(function(index, el) {
			if (el == $li.context && index == 0) {
				window.location.href = "list";
			} else if (el == $li.context && index == 1) {
				window.location.href = "list?search_EQ_stockStatus=1";
			} else if (el == $li.context && index == 2) {
				window.location.href = "list?search_EQ_stockStatus=2";
			}
		});
	}); */
	
})
</script>
</body>
</html>
