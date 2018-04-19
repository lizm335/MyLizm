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
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">库存管理</a></li>
		<li class="active">库存详情</li>
	</ol>
</section>
<section class="content">
	<div class="box">
		<div class="box-header with-border">
	        <h3 class="box-title text-bold">教材信息</h3>
	    </div>
		<div class="box-body">
			<div class="row margin_t10 margin_b10">
				<div class="col-sm-4">
					<b>教材名称:</b>
					${textbook.textbookName}
				</div>
				<div class="col-sm-4">
					<b>教材类型:</b>
					<c:choose>
           				<c:when test="${textbook.textbookType == 1}">主教材</c:when>
           				<c:otherwise>复习资料</c:otherwise>
           			</c:choose>
				</div>
				<div class="col-sm-4">
					<b>教材版次:</b>
					${textbook.revision}
				</div>
			</div>
			<div class="row margin_t10 margin_b10">
				<div class="col-sm-4">
					<b>教材书号:</b>
					${textbook.textbookCode}
				</div>
				<div class="col-sm-4">
					<b>教材作者:</b>
					${textbook.author}
				</div>
				<div class="col-sm-4">
					<b>教材价格:</b>
					￥${textbook.price}
				</div>
			</div>
		</div>
	</div>
	<div class="box no-margin">
		<div class="box-header with-border">
	        <h3 class="box-title text-bold">库存操作明细</h3>
	    </div>
		<div class="box-body">
			<ul class="list-inline inline-data">
				<li>
					<span>库存状态：</span>
					<c:choose>
           				<c:when test="${textbook.gjtTextbookStock.stockNum != 0 && textbook.gjtTextbookStock.stockNum >= textbook.gjtTextbookStock.planOutStockNum}">
           					<b class="text-green">
	            				库存充足
	            			</b>
           				</c:when>
           				<c:when test="${textbook.gjtTextbookStock.stockNum < textbook.gjtTextbookStock.planOutStockNum}">
           					<b class="text-red">
	            				库存不足
	            			</b>
           				</c:when>
           				<c:otherwise>--</c:otherwise>
           			</c:choose>
				</li>
				<li>	
					<span>总入库：</span>
					<b>${textbook.gjtTextbookStock.inStockNum}</b>
				</li>
				<li>
					<span>总出库：</span>
					<b>${textbook.gjtTextbookStock.outStockNum}</b>
				</li>
				<li>
					<span>剩余库存：</span>
					<b>${textbook.gjtTextbookStock.stockNum}</b>
				</li>
				<li>
					<span>待出库：</span>
					<b>${textbook.gjtTextbookStock.planOutStockNum}</b>
				</li>
			</ul>
			<form id="listForm" class="form-horizontal" action="${ctx}/textbookStock/view/${textbook.textbookId}" method="post">
				<div class="row pad-t15">
					<div class="col-sm-6 col-md-3">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">操作类型</label>
							<div class="col-sm-9">
				                <select name="search_EQ_operaType" class="selectpicker form-control">
				                  <option value="">全部类型</option>
								  <option value="1" <c:if test="${param['search_EQ_operaType'] == 1}">selected="selected"</c:if> >教材入库</option>
								  <option value="2" <c:if test="${param['search_EQ_operaType'] == 2}">selected="selected"</c:if> >库存损耗</option>
								  <option value="3" <c:if test="${param['search_EQ_operaType'] == 3}">selected="selected"</c:if> >清理库存</option>
								  <option value="4" <c:if test="${param['search_EQ_operaType'] == 4}">selected="selected"</c:if> >教材配送</option>
								  <option value="5" <c:if test="${param['search_EQ_operaType'] == 5}">selected="selected"</c:if> >退单/退货</option>
				                </select>
				            </div>
						</div>
					</div>
					<div class="col-sm-6 col-md-3">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">状态</label>
							<div class="col-sm-9">
				                <select name="search_EQ_status" class="selectpicker form-control">
				                  <option value="">全部状态</option>
								  <option value="1" <c:if test="${param['search_EQ_status'] == 1}">selected="selected"</c:if> >待审核</option>
								  <option value="2" <c:if test="${param['search_EQ_status'] == 2}">selected="selected"</c:if> >已入库</option>
								  <option value="3" <c:if test="${param['search_EQ_status'] == 3}">selected="selected"</c:if> >审核不通过</option>
				                </select>
				            </div>
						</div>
					</div>
					<div class="col-sm-6 col-md-4">
						<div class="form-group">
							<label class="control-label col-sm-3 text-nowrap">时间</label>
							<div class="col-sm-9">
				                <div class="input-group">
				                	<input name="search_GTE_createdDt" type="text" class="form-control reservation" value="${param['search_GTE_createdDt']}" placeholder="开始时间">
				                	<div class="input-group-addon no-border">
					                    	至
					                </div>
					                <input name="search_LTE_createdDt" type="text" class="form-control reservation" value="${param['search_LTE_createdDt']}" placeholder="结束时间">
				                </div>
				            </div>
						</div>
					</div>
					<div class="col-sm-6 col-md-2">
						<div class="form-group">
							<div class="col-sm-9 col-sm-offset-1">
								<button type="submit" class="btn btn-primary">搜索</button>
							</div>
						</div>
					</div>
				</div>
			<div class="table-responsive">
				<table class="table table-bordered table-striped vertical-mid table-font text-center">
					<thead>
		              <tr>
		                <th>操作类型</th>
		                <th>操作明细</th>
		                <th>操作时间</th>
		                <th>申请出/入库量</th>
		                <th>实际出/入库量</th>
		                <th>剩余库存</th>
		                <th>状态</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
									<c:if test="${not empty entity}">
						            	<tr>
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
						            			${entity.applyQuantity}
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${entity.status == 1}">
						            					<span class="text-muted">
						            						待审核
						            					</span>
						            				</c:when>
						            				<c:otherwise>
						            					${entity.actualQuantity}
						            				</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${entity.status == 1}">
						            					${textbook.gjtTextbookStock.stockNum}
						            				</c:when>
						            				<c:otherwise>
						            					${entity.stockQuantity}
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
						            	</tr>
						            </c:if>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="7">暂无数据</td>
								</tr>
							</c:otherwise>
						</c:choose>
		            </tbody>
				</table>
			<tags:pagination page="${pageInfo}" paginationSize="5" />
			</div>
			</form>
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

$(function(){
	/*日期控件*/
	$('.reservation').datepicker({
		language:'zh-CN',
		todayHighlight:true,
		format:'yyyy-mm-dd'
	});

})
</script>
</body>
</html>
