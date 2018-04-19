<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>班主任平台</title>

<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
	<div class="pull-left">
    	您所在位置：
  	</div>
	<ol class="breadcrumb">
		<li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教务管理</a></li>
		<li><a href="#">教材管理</a></li>
		<li class="active">反馈详情</li>
	</ol>
</section>
<section class="content">
	<div class="box">
	    <div class="box-body">
	      <div class="media pad">
	        <div class="media-left" style="padding-right:25px;">
	          <img src="${entity.gjtStudentInfo.avatar}" class="img-circle" style="width:112px;height:112px;" alt="User Image">
	        </div>
	        <div class="media-body">
	          <h3 class="margin_t10">
	            	${entity.gjtStudentInfo.xm}
	          </h3>
	          <div class="row">
	            <div class="col-xs-6 col-md-4 pad-b5">
	              <b>学号:</b> <span>${entity.gjtStudentInfo.xh}</span>
	            </div>
	            <div class="col-xs-6 col-md-4 pad-b5">
	              <b>手机:</b>
	              <span>${entity.gjtStudentInfo.sjh}</span>
	            </div>
	            <div class="col-xs-6 col-md-4 pad-b5">
	              <b>邮箱:</b> <span>${entity.gjtStudentInfo.dzxx}</span>
	            </div>
	            <div class="col-xs-6 col-md-4 pad-b5">
	              <b>已发教材:</b> <span>0</span>
	            </div>
	            <div class="col-xs-6 col-md-4 pad-b5">
	              <b>剩余待发:</b> <span class="text-orange">0</span>
	            </div>
	            <div class="col-xs-6 col-md-4 pad-b5">
	              <b>当前待发:</b> <span>0</span>
	            </div>
	          </div>
	        </div>
	      </div>
	    </div>
	</div>
	
	<div class="box">
	  	<div class="box-header with-border">
	        <h3 class="box-title">个人信息</h3>
	    </div>
	    <div class="box-body">
	    	<div class="cnt-box-body no-padding">
	          <div class="table-responsive margin-bottom-none">
	            <table class="table-block table-col-4">
	              <tr class="table-row">
	                <td class="table-cell-block">
	                  	姓名
	                </td>
	                <td class="table-cell-block even">
	                  	${entity.gjtStudentInfo.xm}
	                </td>
	                <td class="table-cell-block">
	                  	学号
	                </td>
	                <td class="table-cell-block even">
	                  	${entity.gjtStudentInfo.xh}
	                </td>
	              </tr>
	              
	              <tr class="table-row">
	                <td class="table-cell-block">
	                  	手机
	                </td>
	                <td class="table-cell-block even">
	                  	${entity.gjtStudentInfo.sjh}
	                </td>
	                <td class="table-cell-block">
	                  	年级
	                </td>
	                <td class="table-cell-block even">
	                  	${entity.gjtStudentInfo.gjtGrade.gradeName}
	                </td>
	              </tr>
	              <tr class="table-row">
	                <td class="table-cell-block">
	                  	层次
	                </td>
	                <td class="table-cell-block even">
	                  	${trainingLevelMap[entity.gjtStudentInfo.pycc]}
	                </td>
	                <td class="table-cell-block">
	                  	专业
	                </td>
	                <td class="table-cell-block even">
	                 	${entity.gjtStudentInfo.gjtSpecialty.zymc}
	                </td>
	              </tr>
	              <tr class="table-row">
	                <td class="table-cell-block">
	                  	收货地址
	                </td>
	                <td class="table-cell-block even" colspan="3">
	                  	${entity.gjtStudentInfo.txdz}
	                </td>
	              </tr>
	            </table>
	          </div>
	        </div>
	    </div>
	</div>
	
	<div class="box">
		<div class="box-header with-border">
	        <h3 class="box-title">反馈信息</h3>
	    </div>
	    <div class="box-body">
	    	<div class="cnt-box-body no-padding">
		    	<div class="table-responsive margin-bottom-none">
		    		<table class="table-block table-col-4">
	    				<tr class="table-row">
				            <td class="table-cell-block">
				              	反馈类型
				            </td>
				            <td class="table-cell-block even">
				             	<c:choose>
	    							<c:when test="${entity.feedbackType == 1}">
	    								退换教材
	    							</c:when>
	    							<c:when test="${entity.feedbackType == 2}">
	    								补发教材
	    							</c:when>
	    							<c:otherwise>
	    								其它
	    							</c:otherwise>
	    						</c:choose>
				            </td>
				            <td class="table-cell-block">
				              	教材名称
				            </td>
				            <td class="table-cell-block even">
				              	<c:forEach items="${entity.gjtTextbookFeedbackDetails}" var="detail" varStatus="status">
	    							<c:choose>
					          			<c:when test="${status.last}">${detail.gjtTextbook.textbookName}</c:when>
					          			<c:otherwise>${detail.gjtTextbook.textbookName}, </c:otherwise>
					          		</c:choose>
	    						</c:forEach>
				            </td>
				          </tr>
				          <tr class="table-row">
				            <td class="table-cell-block">
				              	反馈时间
				            </td>
				            <td class="table-cell-block even">
				              	<fmt:formatDate value="${entity.createdDt}" pattern="yyyy-MM-dd HH:mm"/>
				            </td>
				            <td class="table-cell-block">
				              	反馈原因
				            </td>
				            <td class="table-cell-block even">
				              	${entity.reason}
				            </td>
				        </tr>
		    		</table>
		    	</div>
		    </div>
	    </div>
	</div>
	
	<div class="box">
		<div class="box-header with-border">
	        <h3 class="box-title">物流信息</h3>
	    </div>
	    <div class="box-body">
	    	<div class="table-responsive">
	    		<table class="table table-bordered table-striped margin_t10 margin_b10 full-width table-cell-ver-mid">
			      <thead>
			        <tr>
			          <th>配送时间</th>
			          <th>订单号</th>
			          <th>运单号</th>
			          <th>快递费用</th>
			          <th>运单状态</th>
			          <th>物品明细</th>
			        </tr>
			      </thead>
			      <tbody>
	            	<c:choose>
	            		<c:when test="${not empty textbookDistributes}">
							<c:forEach items="${textbookDistributes}" var="entity">
								<c:if test="${not empty entity}">
							        <tr>
							          <td>
							          	<c:choose>
							          		<c:when test="${not empty entity.distributionDt}">
							          			<fmt:formatDate value="${entity.distributionDt}" pattern="yyyy-MM-dd HH:mm"/>
							          		</c:when>
							          		<c:otherwise>--</c:otherwise>
							          	</c:choose>
							          </td>
							          <td>${entity.orderCode}</td>
							          <td>
							          	<c:choose>
							          		<c:when test="${not empty entity.waybillCode}">${entity.waybillCode}</c:when>
							          		<c:otherwise>--</c:otherwise>
							          	</c:choose>
							          </td>
							          <td>
							          	<c:choose>
							          		<c:when test="${not empty entity.freight}">￥${entity.freight}</c:when>
							          		<c:otherwise>--</c:otherwise>
							          	</c:choose>
							          </td>
							          <td>
					          			<c:choose>
					          				<c:when test="${entity.status == 0}">
					          					<span class="gray9">未就绪</span>
					          				</c:when>
					          				<c:when test="${entity.status == 1}">
					          					<span class="text-orange">待配送</span>
					          				</c:when>
					          				<c:when test="${entity.status == 2}">
					          					<span class="text-light-blue">配送中</span>
					          				</c:when>
					          				<c:when test="${entity.status == 3}">
					          					<span class="text-green">已签收</span>
					          				</c:when>
					          				<c:otherwise>--</c:otherwise>
					          			</c:choose>
							          </td>
							          <td>
							          	<c:forEach items="${entity.gjtTextbookDistributeDetails}" var="detail" varStatus="status">
							          		<c:choose>
							          			<c:when test="${status.last}">${detail.gjtTextbook.textbookName}</c:when>
							          			<c:otherwise>${detail.gjtTextbook.textbookName}、</c:otherwise>
							          		</c:choose>
							          	</c:forEach>
							          </td>
							        </tr>
							    </c:if>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td align="center" colspan="6">暂无数据</td>
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
	        <h3 class="box-title">反馈处理</h3>
	    </div>
	    <div class="box-body">
	    	<table class="table table-bordered table-striped margin_t10 margin_b10 full-width text-center">
		      <thead>
		        <tr>
		          <th>处理状态</th>
		          <th>处理回复</th>
		        </tr>
		      </thead>
		      <tbody>
		        <tr>
		          <c:choose>
					<c:when test="${entity.status == 1}"><td class="text-orange">未处理</td></c:when>
					<c:otherwise><td class="text-green">已处理</td></c:otherwise>
				  </c:choose>
		          <td>
			          <c:choose>
						<c:when test="${entity.status == 1}">--</c:when>
						<c:otherwise>${entity.reply}</c:otherwise>
					  </c:choose>
		          </td>
		        </tr>
		      </tbody>
		    </table>
	    </div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

<script type="text/javascript">

$(function(){
	
})
</script>
</body>
</html>
