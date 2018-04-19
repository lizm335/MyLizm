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
			<li><a href="${ctx}/textbookStock/list" target="_self">库存管理</a></li>
			<li class="active"><a href="${ctx}/textbookStockOperaBatch/list" target="_self">库存操作申请</a></li>
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
			              <label class="control-label col-sm-3 text-nowrap">操作类型</label>
			              <div class="col-sm-9">
			                <select name="search_EQ_operaType" class="selectpicker form-control">
			                  <option value="">全部</option>
							  <option value="1" <c:if test="${param['search_EQ_operaType'] == 1}">selected="selected"</c:if> >教材入库</option>
							  <option value="2" <c:if test="${param['search_EQ_operaType'] == 2}">selected="selected"</c:if> >库存损耗</option>
							  <option value="3" <c:if test="${param['search_EQ_operaType'] == 3}">selected="selected"</c:if> >清理库存</option>
							  <option value="4" <c:if test="${param['search_EQ_operaType'] == 4}">selected="selected"</c:if> >教材配送</option>
							  <option value="5" <c:if test="${param['search_EQ_operaType'] == 5}">selected="selected"</c:if> >退单/退货</option>
			                </select>
			              </div>
			            </div>
			          </div>

			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">状态</label>
			              <div class="col-sm-9">
			                <select name="search_EQ_status" class="selectpicker form-control">
			                  <option value="">全部</option>
							  <option value="1" <c:if test="${param['search_EQ_status'] == 1}">selected="selected"</c:if> >待审核</option>
							  <option value="2" <c:if test="${param['search_EQ_status'] == 2}">selected="selected"</c:if> >已入库</option>
							  <option value="3" <c:if test="${param['search_EQ_status'] == 3}">selected="selected"</c:if> >审核不通过</option>
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
						<h3 class="box-title pad-t5">库存操作申请列表</h3>
					</div>
					<div class="box-body">
						<div class="filter-tabs filter-tabs2 clearfix">
							<ul class="list-unstyled">
								<li lang=":input[name='search_EQ_status']" <c:if test="${empty param['search_EQ_status']}">class="actived"</c:if>>全部(${pending + notPassed + passed})</li>
								<li value="1" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 1}">class="actived"</c:if>>待审核(${pending})</li>
								<li value="2" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 2}">class="actived"</c:if>>已入库(${passed})</li>
								<li value="3" role=":input[name='search_EQ_status']" <c:if test="${param['search_EQ_status'] == 3}">class="actived"</c:if>>审核未通过(${notPassed})</li>
							</ul>
						</div>
						<div class="table-responsive">
							<table class="table table-bordered table-striped vertical-mid text-center table-font">
								<thead>
					              <tr>
					                <th>批次号</th>
					                <th>操作类型</th>
					                <th>操作明细</th>
					                <th>操作时间</th>
					                <th>总申请出/入库量</th>
					                <th>当前库存状态</th>
					                <th>审核状态</th>
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
									            			${entity.batchCode}
									            		</td>
									            		<td>
									            			<c:choose>
									            				<c:when test="${entity.operaType == 1}">教材入库</c:when>
									            				<c:when test="${entity.operaType == 2}">库存损耗</c:when>
									            				<c:when test="${entity.operaType == 3}">清理库存</c:when>
									            				<c:when test="${entity.operaType == 4}">教材配送</c:when>
									            				<c:when test="${entity.operaType == 5}">退单/退货</c:when>
									            				<c:otherwise>--</c:otherwise>
									            			</c:choose>
									            		</td>
									            		<td>
									            			${entity.description}
									            		</td>
									            		<td>
									            			<fmt:formatDate value="${entity.createdDt}" pattern="yyyy-MM-dd HH:mm"/>
									            		</td>
									            		<td>
									            			${entity.quantity}
									            		</td>
									            		<td>
									            			<c:choose>
									            				<c:when test="${entity.stockStatus}">
									            					<span class="text-green">
									            						库存充足
									            					</span>
									            				</c:when>
									            				<c:otherwise>
									            					<span class="text-red">
									            						库存不足
									            					</span>
									            				</c:otherwise>
									            			</c:choose>
									            		</td>
									            		<td>
									            			<c:choose>
									            				<c:when test="${entity.status == 1}">
									            					<span class="text-yellow">
									            						待审核
									            					</span>
									            				</c:when>
									            				<c:when test="${entity.status == 2}">
									            					<span class="text-muted">
									            						已入库
									            					</span>
									            				</c:when>
									            				<c:when test="${entity.status == 3}">
									            					<span class="text-red">
									            						审核不通过
									            					</span>
									            				</c:when>
									            			</c:choose>
									            		</td>
									            		<td>
									            			<a href="view/${entity.batchId}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
									            		</td>
									            	</tr>
									            </c:if>
									        </c:forEach>
									    </c:when>
										<c:otherwise>
											<tr>
												<td align="center" colspan="8">暂无数据</td>
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
	// filter tabs
	/* $(".filter-tabs li").click(function(event) {
		var $li = $(this);
		$(".filter-tabs li").each(function(index, el) {
			if (el == $li.context && index == 0) {
				window.location.href = "list";
			} else if (el == $li.context && index == 1) {
				window.location.href = "list?search_EQ_status=1";
			} else if (el == $li.context && index == 2) {
				window.location.href = "list?search_EQ_status=2";
			} else if (el == $li.context && index == 3) {
				window.location.href = "list?search_EQ_status=3";
			}
		});
	}); */
	
})
</script>
</body>
</html>
