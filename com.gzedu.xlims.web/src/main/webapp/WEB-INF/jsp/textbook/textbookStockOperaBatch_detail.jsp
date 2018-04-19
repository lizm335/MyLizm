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
		<li class="active">查看库存申请详情</li>
	</ol>
</section>
<section class="content">
	<div class="box">
		<div class="box-header with-border">
	        <h3 class="box-title text-bold">库存操作申请明细</h3>
	    </div>
		<div class="box-body">
			<div class="inline-data no-pad-top pad-l15 pad-r15">
				<div class="row margin_t10 margin_b10">
					<div class="col-sm-4">
						<b>批次号:</b>
						${operaBatch.batchCode}
					</div>
					<div class="col-sm-4">
						<b>操作类型:</b>
						<c:choose>
            				<c:when test="${operaBatch.operaType == 1}">教材入库</c:when>
            				<c:when test="${operaBatch.operaType == 2}">库存损耗</c:when>
            				<c:when test="${operaBatch.operaType == 3}">清理库存</c:when>
            				<c:when test="${operaBatch.operaType == 4}">教材配送</c:when>
            				<c:when test="${operaBatch.operaType == 5}">退单/退货</c:when>
            				<c:otherwise>--</c:otherwise>
            			</c:choose>
					</div>
					<div class="col-sm-4">
						<b>操作时间:</b>
						<fmt:formatDate value="${operaBatch.createdDt}" pattern="yyyy-MM-dd HH:mm"/>
					</div>
				</div>
				<div class="row margin_t10 margin_b10">
					<div class="col-sm-4">
						<b>操作明细:</b>
						${operaBatch.description}
					</div>
					<div class="col-sm-4">
						<b>总申请出/入库量:</b>
						${operaBatch.quantity}
					</div>
					<div class="col-sm-4">
						<b>库存状态:</b>
						<c:choose>
            				<c:when test="${operaBatch.stockStatus}">
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
					</div>
				</div>
			</div>
			<div class="table-responsive margin_t20">
				<table class="table table-bordered table-striped vertical-mid text-center table-font">
					<thead>
		              <tr>
		                <th>书号</th>
		                <th>教材名称</th>
		                <th>申请出/入库量</th>
		                <th>当前库存</th>
		                <th>实际出/入库量</th>
		                <th>剩余库存</th>
		                <th>库存状态</th>
		              </tr>
		            </thead>
		            <tbody>
		            	<c:choose>
		            		<c:when test="${not empty operaBatch.gjtTextbookStockOperas}">
								<c:forEach items="${operaBatch.gjtTextbookStockOperas}" var="entity">
									<c:if test="${not empty entity}">
						            	<tr>
						            		<td>
						            			${entity.gjtTextbook.textbookCode}
						            		</td>
						            		<td>
						            			${entity.gjtTextbook.textbookName}
						            		</td>
						            		<td>
						            			${entity.applyQuantity}
						            		</td>
						            		<td>
						            			${entity.gjtTextbook.gjtTextbookStock.stockNum}
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${entity.status == 1}">
						            					<span class="text-muted">
						            						待审核，未出库
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
						            					<span class="text-muted">
								            				待审核，未出库
								            			</span>
						            				</c:when>
						            				<c:otherwise>
						            					${entity.stockQuantity}
						            				</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
												<c:choose>
							           				<c:when test="${entity.gjtTextbook.gjtTextbookStock.stockNum != 0 && entity.gjtTextbook.gjtTextbookStock.stockNum >= entity.gjtTextbook.gjtTextbookStock.planOutStockNum}">
							           					<b class="text-green">
								            				库存充足
								            			</b>
							           				</c:when>
							           				<c:when test="${entity.gjtTextbook.gjtTextbookStock.stockNum < entity.gjtTextbook.gjtTextbookStock.planOutStockNum}">
							           					<b class="text-red">
								            				库存不足
								            			</b>
							           				</c:when>
							           				<c:otherwise>--</c:otherwise>
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
			</div>
		</div>
	</div>
	<div class="box margin-bottom-none">
		<div class="box-header with-border">
	        <h3 class="box-title text-bold">审批明细</h3>
	    </div>
		<div class="box-body">
			<div class="approval-list clearfix">
				<c:forEach items="${operaBatch.gjtTextbookStockApprovals}" var="entity">
					<dl class="approval-item">
				        <dt class="clearfix">
				          <span class="pull-right gray9 text-no-bold"><fmt:formatDate value="${entity.createdDt}" pattern="yyyy-MM-dd HH:mm"/></span>
				          <b class="a-tit gray6">
				          	<c:choose>
				          		<c:when test="${entity.operaRole == 1}">
				          			教材专员
				          		</c:when>
				          		<c:otherwise>
				          			教材主任
				          		</c:otherwise>
				          	</c:choose>
				          	<c:choose>
				          		<c:when test="${entity.operaType == 1}">
				          			申请
				          		</c:when>
				          		<c:otherwise>
				          			审批
				          		</c:otherwise>
				          	</c:choose>
				          </b>
				          	<c:choose>
				          		<c:when test="${entity.operaType == 1}">
				          			<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
				          		</c:when>
				          		<c:when test="${entity.operaType == 2}">
				          			<span class="fa fa-fw fa-check-circle text-green"></span>
				          			<label class="state-lb text-green">审批通过</label>
				          		</c:when>
				          		<c:otherwise>
				          			<span class="fa fa-fw fa-times-circle text-red"></span>
				          			<label class="state-lb text-red">审批不通过</label>
				          		</c:otherwise>
				          	</c:choose>
				        </dt>
				        <c:if test="${not empty entity.description}">
					        <dd>
					        	<div class="txt">
						            ${entity.description}
						            <i class="arrow-top"></i>
						        </div>
					        </dd>
				        </c:if>
				    </dl>
			    </c:forEach>
			    
			    <c:if test="${operaBatch.status == 1}">
				    <dl class="approval-item">
				        <dt class="clearfix">
				          <b class="a-tit gray6">教材主任审批</b>
				          <span class="fa fa-fw fa-dot-circle-o text-yellow"></span>
				          <label class="state-lb pending-state">待审批</label>
				        </dt>
				        <dd>
				        	<form id="approvalForm" action="${ctx}/textbookStockOperaBatch/approval" method="post">
				        		<input type="hidden" name="operaBatchId" value="${operaBatch.batchId}">
				        		<input type="hidden" id="operaType" name="operaType" value="">
					        	<textarea id="description" name="description" class="form-control" rows="3" placeholder="请输入审批评语或跟进记录"></textarea>
					        	<div class="pad-t10">
					        		<button type="button" class="btn min-width-90px btn-success">通过</button>
					        		<button type="button" class="btn min-width-90px btn-danger left10">不通过</button>
					        	</div>
				        	</form>
				        </dd>
				    </dl>
			    </c:if>
			</div>
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

$(function(){
	
	$(".btn-success").click(function() {
		if ($("#description").val() == "") {
			alert("请输入审批评语或跟进记录！");
			return false;
		}
		$("#operaType").val(2);
		
		postIngIframe=$.mydialog({
		  id:'dialog-1',
		  width:150,
		  height:50,
		  backdrop:false,
		  fade:true,
		  showCloseIco:false,
		  zIndex:11000,
		  content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
		});
		
		$("#approvalForm").submit();
	})
	
	$(".btn-danger").click(function() {
		if ($("#description").val() == "") {
			alert("请输入审批评语或跟进记录！");
			return false;
		}
		$("#operaType").val(3);
		
		postIngIframe=$.mydialog({
		  id:'dialog-1',
		  width:150,
		  height:50,
		  backdrop:false,
		  fade:true,
		  showCloseIco:false,
		  zIndex:11000,
		  content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
		});
		
		$("#approvalForm").submit();
	})

})
</script>
</body>
</html>
