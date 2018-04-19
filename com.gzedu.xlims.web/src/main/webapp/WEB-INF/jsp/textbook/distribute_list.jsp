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
<!-- 数据来源与《发放编排-设置教材发放安排》 -->
<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教材管理</a></li>
		<li class="active">教材发放</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom no-margin">
		<div class="tab-content">
			<div class="tab-pane active" id="tab_top_1">
				<form id="listForm" class="form-horizontal">
				<div class="box box-border">
				    <div class="box-body">
				        <div class="row pad-t15">
				          
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">订单号</label>
				              <div class="col-sm-9">
				                <input type="text" class="form-control" name="search_orderCode" value="${param['search_orderCode']}">
				              </div>
				            </div>
				          </div>
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">学号</label>
				              <div class="col-sm-9">
								 <input type="text" class="form-control" name="search_studentCode" value="${param['search_studentCode']}">
				              </div>
				            </div>
				          </div>

				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">姓名</label>
				              <div class="col-sm-9">
									<input type="text" class="form-control" name="search_studentName" value="${param['search_studentName']}">
				              </div>
				            </div>
				          </div>
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">商品名称</label>
				              <div class="col-sm-9">
								  <input type="text" class="form-control" name="search_textbookName" value="${param['search_textbookName']}">
				              </div>
				            </div>
				          </div>
				        
				         
				        </div>
				    </div><!-- /.box-body -->
				    <div class="box-footer">
				      <div class="pull-right"><button type="button" class="btn min-width-90px btn-default btn-reset">重置</button></div>
				      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
				    </div><!-- /.box-footer-->
				</div>
	
	
				<div class="box box-border margin-bottom-none">
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">
							订单列表
						</h3>
						<div class="pull-right no-margin">
						<c:if test="${isBtnImport }">
							<button type="button" class="btn btn-default btn-sm btn-import margin-r-5"><i class="fa fa-fw fa-sign-in"></i> 批量导入运单号</button>
						</c:if>
						<c:if test="${isBtnExport }">
							<button type="button" class="btn btn-default btn-sm margin-r-5 btn-export"><i class="fa fa-fw fa-sign-out"></i> 导出订单信息</button>
						</c:if>
						</div>
					</div>
					<div class="box-body">
						<div class="filter-tabs filter-tabs2 clearfix">
							<ul class="list-unstyled">
								<li  <c:if test="${empty param['search_status']}">class="actived"</c:if>>全部(${all})</li>
								<li data-status="1" <c:if test="${param['search_status'] == 1 }">class="actived"</c:if>>待配送(${notDistribute})</li>
								<li data-status="2" <c:if test="${param['search_status'] == 2 }">class="actived"</c:if>>已发货(${distributing})</li>
								<li data-status="3" role=":input[name='search_status']" <c:if test="${param['search_status'] == 3 }">class="actived"</c:if>>已完成(${signed})</li>
							</ul>
							<input type="hidden" value="${param.search_status }" name="search_status" id="status"/>
						</div>
						<div class="table-responsive">
							<table class="table table-bordered table-striped vertical-mid table-font text-center">
								<colgroup>
									<col width="9%"></col>
									<col width=""></col>
									<col width="7%"></col>
									<col width="8%"></col>
									<col width="13%"></col>
									<col width="8%"></col>
									<col width="9%"></col>
									<col width="7%"></col>
									<col width="5%"></col>
								</colgroup>
								<thead>
					              <tr>
					                <th >订单号</th>
					                <th >商品</th>
					                <th >订单所属</th>
					                <th >总价</th>
					                <th>买家</th>
					                <th >运单号</th>
					                <th>下单时间</th>
					                <th >交易状态</th>
					                <th >操作</th>
					              </tr>
					            </thead>
					            <tbody>
					            	<c:choose>
					            		<c:when test="${not empty pageInfo.content}">
											<c:forEach items="${pageInfo.content}" var="entity">
												<c:if test="${not empty entity}">
									            	<tr>
									            		<td>
									            			${entity.ORDER_CODE}
									            		</td>
									            		<td >	
									            			<table width="100%">
									            				<tbody>
									            					<c:forEach items="${details}" var="item">
									            						<c:if test="${item.distributeId==entity.DISTRIBUTE_ID }">
									            						<tr>
										            						<td>${item.gjtTextbook.textbookName}</td>
										            						<td class="pad5 text-nowrap">数量：${item.quantity}</td>
										            						<td class="pad5 text-nowrap">单价：￥${item.price}</td>
										            					</tr>
											            				</c:if>
											            			</c:forEach>
									            				</tbody>
									            			</table>
									            		</td>
									            		<td>
									            			<c:if test="${entity.ORDER_TYPE==0}">学院订购</c:if>
									            			<c:if test="${entity.ORDER_TYPE==1}">书商订购</c:if>
									            		</td>
									            		<td>
									            			￥${entity.TOTAL_PRICE}<br/>
									            			<c:if test="${not empty(entity.FREIGHT) }">
									            				(含运费￥${entity.FREIGHT})
									            			</c:if>
									            		</td>
									            		<td>
							                              	<div class="text-left">
								                              	姓名：${entity.XM}<br>
								                              	学号：${entity.XH}
							                              	</div>
									            		</td>
									            		<td>
									            			<c:if test="${not empty(entity.WAYBILL_CODE) }">
									            				${entity.WAYBILL_CODE}
									            			</c:if>
									            			<c:if test="${empty(entity.WAYBILL_CODE) }">
									            				--
									            			</c:if>
									            		</td>
									            		<td>
									            			<fmt:formatDate value="${entity.CREATED_DT }" pattern="yyyy-MM-dd"/><br/>
									            			<fmt:formatDate value="${entity.CREATED_DT }" pattern="HH:mm:ss"/>
									            		</td>
									            		<td>
									            			<c:choose>
									            				<c:when test="${entity.STATUS == 1}">
									            					<span class="text-red">待配送</span>
									            				</c:when>
									            				<c:when test="${entity.STATUS == 2}">
									            					<span class="text-light-blue">已发货</span>
									            				</c:when>
									            				<c:when test="${entity.STATUS == 3}">
									            					<span class="text-green">已完成</span>
									            				</c:when>
									            				<c:otherwise>--</c:otherwise>
									            			</c:choose>
									            		</td>
									            		<td>
									            			<c:if test="${isBtnView }">
									            			<a href="view?studentId=${entity.STUDENT_ID}&distributeId=${entity.DISTRIBUTE_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
									            			</c:if>
									            		</td>
									            	</tr>
									            </c:if>
									        </c:forEach>
									    </c:when>
										<c:otherwise>
											<tr>
												<td align="center" colspan="10">暂无数据</td>
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
			</div>
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

$(function(){

	
	//批量导入
	$(".btn-import").click(function(event) {
		var actionName = ctx+"/textbookDistribute/importWaybillCode";
		var downloadFileUrl = ctx+"/textbookDistribute/exportCurrentDistributeList2";
		var content1 = "为了方便你的工作，我们已经准备好了待配送学员的《运单号导入表》<br>的标准模板，你可以点击下面的下载按钮，下载标准模板。"
		+"</br>必需是状态为《待配送》的才能导入；如何变成《待配送》，库存管理-库存操作申请-审核以后";
		var content2 = "请选择你要导入的《运单号导入表》";
		excelImport(actionName, "file", "textbookDistribute", downloadFileUrl, "下载待配送教材订单", "批量导入运单号", null, null, null, content1, content2);
	});
	
	$('.btn-export').click(function(){
		open('${ctx}/textbookDistribute/exportOrderList?'+$('#listForm').serialize());
	});
	
	// filter tabs
	 $(".filter-tabs li").click(function(event) {
		$('[name="search_status"]').val($.trim($(this).data('status')));
		$('#listForm').submit();
	}); 
})

</script>
</body>
</html>
