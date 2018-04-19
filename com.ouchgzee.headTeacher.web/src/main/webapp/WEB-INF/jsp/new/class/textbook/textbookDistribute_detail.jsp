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
		<li class="active">教材详情</li>
	</ol>
</section>
<section class="content">
	<div class="box">
		<div class="box-header with-border">
	        <h3 class="box-title text-bold">个人信息</h3>
	    </div>
	    <div class="box-body">
	      <div class="media pad">
	        <div class="media-left" style="padding-right:25px;">
	          	<img src="${studentInfo.avatar}" class="img-circle" alt="User Image">
	        </div>
	        <div class="media-body">
	          	<h3 class="margin_t10">${studentInfo.xm}</h3>
	          	<table class="full-width per-tbl">
	          		<tr>
	          			<th width="40">学号:</th>
	          			<td>${studentInfo.xh}</td>

	          			<th width="40">层次:</th>
	          			<td>${trainingLevelMap[studentInfo.pycc]}</td>

	          			<th width="70">专业:</th>
	          			<td>${studentInfo.gjtSpecialty.zymc}</td>
	          		</tr>
	          		<tr>
	          			<th width="40">手机:</th>
	          			<td>${studentInfo.sjh}</td>

	          			<th width="40">年级:</th>
	          			<td>${studentInfo.gjtGrade.gradeName}</td>

	          			<th>收货地址:</th>
	          			<td>
	          				${studentInfo.txdz}
	          			</td>
	          		</tr>
	          	</table>
	        </div>
	      </div>
	    </div>
	</div>
	
	<c:set var="hadDistributePrice" value="0"></c:set>
	<div class="box">
		<div class="box-header with-border">
	        <h3 class="box-title text-bold">教材配送明细</h3>
	    </div>
	    <div class="box-body">
	    	<c:forEach items="${gradeSpecialtyPlanMap}" var="map" varStatus="mapStatus">
	    		<c:set var="total" value="0"></c:set>
		    	<div class="table-responsive">
			    	<table class="table table-bordered table-striped margin_t10 margin_b10 full-width vertical-mid table-font">
			    	  <c:if test="${mapStatus.first}">
				    	  <thead>
					        <tr>
					          <th width="10%" class="text-center">学期</th>
					          <th width="30%">课程名称</th>
					          <th width="10%">教材类型</th>
					          <th width="30%">教材名称</th>
					          <th width="10%">教材价格</th>
					          <th width="10%">配送状态</th>
					        </tr>
					      </thead>
			    	  </c:if>
				      <tbody>
				      	  <c:set var="rowspan" value="0"></c:set>
				      	  <c:forEach items="${map.value}" var="gradeSpecialtyPlan">
				      		<c:choose>
				      			<c:when test="${not empty gradeSpecialtyPlan.gjtCourse.gjtTextbookList}">
					      			<c:forEach items="${gradeSpecialtyPlan.gjtCourse.gjtTextbookList}" var="book">
					      				<c:set var="rowspan" value="${rowspan + 1}"></c:set>
					      				<c:set var="total" value="${total + book.discountPrice}"></c:set>
					      			</c:forEach>
				      			</c:when>
				      			<c:otherwise>
				      				<c:set var="rowspan" value="${rowspan + 1}"></c:set>
				      			</c:otherwise>
				      		</c:choose>
			      		  </c:forEach>
			      		  
					      <c:forEach items="${map.value}" var="gradeSpecialtyPlan" varStatus="status">
				      		<c:choose>
				      			<c:when test="${not empty gradeSpecialtyPlan.gjtCourse.gjtTextbookList}">
					      			<c:forEach items="${gradeSpecialtyPlan.gjtCourse.gjtTextbookList}" var="book" varStatus="bookStatus">
					      				<c:choose>
					      					<c:when test="${status.first && bookStatus.first}">
						      					<tr>
										          <td width="10%" rowspan="${rowspan}" class="text-center">第${gradeSpecialtyPlan.kkxq}学期</td>
										          <td width="30%" rowspan="${fn:length(gradeSpecialtyPlan.gjtCourse.gjtTextbookList)}">${gradeSpecialtyPlan.gjtCourse.kcmc}</td>
										          <td width="10%">
										          	<c:choose>
										          		<c:when test="${book.textbookType == 1}">主教材</c:when>
										          		<c:otherwise>复习资料</c:otherwise>
										          	</c:choose>
										          </td>
										          <td width="30%">${book.textbookName}</td>
										          <td width="10%">￥${book.discountPrice}</td>
										          <td width="10%">
										          	<c:choose>
										          		<c:when test="${not empty statusMap[book.textbookId]}">
										          			<c:choose>
										          				<c:when test="${statusMap[book.textbookId] == 0}">
										          					<span class="gray9">未就绪</span>
										          				</c:when>
										          				<c:when test="${statusMap[book.textbookId] == 1}">
										          					<span class="text-orange">待配送</span>
										          				</c:when>
										          				<c:when test="${statusMap[book.textbookId] == 2}">
										          					<span class="text-light-blue">配送中</span>
										          					<c:set var="hadDistributePrice" value="${hadDistributePrice + book.discountPrice}"></c:set>
										          				</c:when>
										          				<c:when test="${statusMap[book.textbookId] == 3}">
										          					<span class="text-green">已签收</span>
										          					<c:set var="hadDistributePrice" value="${hadDistributePrice + book.discountPrice}"></c:set>
										          				</c:when>
										          				<c:otherwise>--</c:otherwise>
										          			</c:choose>
										          		</c:when>
										          		<c:otherwise>
										          			<span class="gray9">未就绪</span>
										          		</c:otherwise>
										          	</c:choose>
										          </td>
										        </tr>
					      					</c:when>
					      					<c:when test="${bookStatus.first}">
						      					<tr>
										          <td width="30%" rowspan="${fn:length(gradeSpecialtyPlan.gjtCourse.gjtTextbookList)}">${gradeSpecialtyPlan.gjtCourse.kcmc}</td>
										          <td width="10%">
										          	<c:choose>
										          		<c:when test="${book.textbookType == 1}">主教材</c:when>
										          		<c:otherwise>复习资料</c:otherwise>
										          	</c:choose>
										          </td>
										          <td width="30%">${book.textbookName}</td>
										          <td width="10%">￥${book.discountPrice}</td>
										          <td width="10%">
										          	<c:choose>
										          		<c:when test="${not empty statusMap[book.textbookId]}">
										          			<c:choose>
										          				<c:when test="${statusMap[book.textbookId] == 0}">
										          					<span class="gray9">未就绪</span>
										          				</c:when>
										          				<c:when test="${statusMap[book.textbookId] == 1}">
										          					<span class="text-orange">待配送</span>
										          				</c:when>
										          				<c:when test="${statusMap[book.textbookId] == 2}">
										          					<span class="text-light-blue">配送中</span>
										          					<c:set var="hadDistributePrice" value="${hadDistributePrice + book.discountPrice}"></c:set>
										          				</c:when>
										          				<c:when test="${statusMap[book.textbookId] == 3}">
										          					<span class="text-green">已签收</span>
										          					<c:set var="hadDistributePrice" value="${hadDistributePrice + book.discountPrice}"></c:set>
										          				</c:when>
										          				<c:otherwise>--</c:otherwise>
										          			</c:choose>
										          		</c:when>
										          		<c:otherwise>
										          			<span class="gray9">未就绪</span>
										          		</c:otherwise>
										          	</c:choose>
										          </td>
										        </tr>
					      					</c:when>
					      					<c:otherwise>
						      					<tr>
										          <td width="10%">
										          	<c:choose>
										          		<c:when test="${book.textbookType == 1}">主教材</c:when>
										          		<c:otherwise>复习资料</c:otherwise>
										          	</c:choose>
										          </td>
										          <td width="30%">${book.textbookName}</td>
										          <td width="10%">￥${book.discountPrice}</td>
										          <td width="10%">
										          	<c:choose>
										          		<c:when test="${not empty statusMap[book.textbookId]}">
										          			<c:choose>
										          				<c:when test="${statusMap[book.textbookId] == 0}">
										          					<span class="gray9">未就绪</span>
										          				</c:when>
										          				<c:when test="${statusMap[book.textbookId] == 1}">
										          					<span class="text-orange">待配送</span>
										          				</c:when>
										          				<c:when test="${statusMap[book.textbookId] == 2}">
										          					<span class="text-light-blue">配送中</span>
										          					<c:set var="hadDistributePrice" value="${hadDistributePrice + book.discountPrice}"></c:set>
										          				</c:when>
										          				<c:when test="${statusMap[book.textbookId] == 3}">
										          					<span class="text-green">已签收</span>
										          					<c:set var="hadDistributePrice" value="${hadDistributePrice + book.discountPrice}"></c:set>
										          				</c:when>
										          				<c:otherwise>--</c:otherwise>
										          			</c:choose>
										          		</c:when>
										          		<c:otherwise>
										          			<span class="gray9">未就绪</span>
										          		</c:otherwise>
										          	</c:choose>
										          </td>
										        </tr>
					      					</c:otherwise>
					      				</c:choose>
					      			</c:forEach>
				      			</c:when>
				      			<c:otherwise>
				      				<c:choose>
				      					<c:when test="${status.first}">
					      					<tr>
									          <td width="10%" rowspan="${rowspan}" class="text-center">第${gradeSpecialtyPlan.kkxq}学期</td>
									          <td width="30%">${gradeSpecialtyPlan.gjtCourse.kcmc}</td>
									          <td width="10%">--</td>
									          <td width="30%">--</td>
									          <td width="10%">--</td>
									          <td width="10%">--</td>
									        </tr>
				      					</c:when>
				      					<c:otherwise>
					      					<tr>
									          <td width="30%">${gradeSpecialtyPlan.gjtCourse.kcmc}</td>
									          <td width="10%">--</td>
									          <td width="30%">--</td>
									          <td width="10%">--</td>
									          <td width="10%">--</td>
									        </tr>
				      					</c:otherwise>
				      				</c:choose>
				      			</c:otherwise>
				      		</c:choose>
						</c:forEach>
				      </tbody>
				    </table>
			    </div>
			    <p> <b>合计：￥<fmt:formatNumber value="${total}" pattern="##.##" minFractionDigits="2" /></b> </p>
			</c:forEach>
			<c:if test="${empty gradeSpecialtyPlanMap}">暂无数据</c:if>
	    </div>
	</div>

	<c:set var="totalFreight" value="0"></c:set>
	<div class="box">
		<div class="box-header with-border">
	        <h3 class="box-title text-bold">物流信息</h3>
	    </div>
	    <div class="box-body">
	    	<div class="table-responsive">
	    		<table class="table table-bordered table-striped margin_t10 margin_b10 full-width vertical-mid table-font">
			      <thead>
			        <tr>
			          <th>配送时间</th>
			          <th>订单号</th>
			          <th>运单号</th>
			          <th>快递费用</th>
			          <th>运单状态</th>
			          <th>物品明细</th>
			          <th>操作</th>
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
							          <c:set var="totalFreight" value="${totalFreight + entity.freight}"></c:set>
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
							          <td>
					          			<c:choose>
					          				<c:when test="${entity.status == 2 || entity.status == 3}">
					          					<a href="javascript:queryLogistics('${entity.logisticsComp}', '${entity.waybillCode}')" class="operion-item" data-toggle="tooltip" title="查看物流"><i class="fa fa-fw fa-view-more"></i></a>
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
	        <h3 class="box-title text-bold">费用信息</h3>
	    </div>
	    <div class="box-body">
	    	<table class="table table-bordered table-striped margin_t10 margin_b10 full-width text-center f12">
		      <thead>
		        <tr>
		          <th>预交教材费</th>
		          <th>已配送教材</th>
		          <th>快递费用</th>
		          <th>退款</th>
		          <th>剩余</th>
		        </tr>
		      </thead>
		      <tbody>
		        <tr>
		          <td>￥${hasPayTextbookCost}</td>
		          <td>￥<fmt:formatNumber value="${hadDistributePrice}" pattern="##.##" minFractionDigits="2" /></td>
		          <td>￥<fmt:formatNumber value="${totalFreight}" pattern="##.##" minFractionDigits="2" /></td>
		          <td>￥0.00</td>
		          <td>￥<fmt:formatNumber value="${hasPayTextbookCost - hadDistributePrice - totalFreight}" pattern="##.##" minFractionDigits="2" /></td>
		        </tr>
		      </tbody>
		    </table>
	    </div>
	</div>
</section>

<div id="logisticsDialog" class="box no-border" style="display: none;">
  <div class="box-header with-border">
	  <h3 class="box-title">物流状态</h3>
  </div>
  <div class="pop-content">
	  <div class="padding15 f14 text-bold f_474747" style="background:#f5f5f5;">
	    <p>货运单号：<span class="express-num"></span></p>
	    <p>快递名称：<span class="express-name"></span></p>
	  </div>
	  <div class="logistics-box approval-list approval-list-2 clearfix" style="overflow-y: scroll; overflow-x: hidden; max-height: 460px; width:470px;"></div>
  </div>
</div>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

$(function(){
	
	
})

function queryLogistics(logisticsComp, waybillCode) {
	var logisticsCompValues = logisticsComp.split("-");
	var htmlTmp=[];
	
	$.ajax({
  	  type: 'GET',
  	  url: ctx+'/textbookDistribute/queryLogistics',
  	  data: {'logisticsComp' : logisticsCompValues[1], 'waybillCode' : waybillCode},
  	  dataType: 'json',
  	  cache: false,
  	  success: function(ret) {
  		    ret = eval('(' + ret + ')');
	  		if (ret && ret.status == '200') {
				$.each(ret.data, function(i, e) {
					var css = (i == 0 ? 'first' : (i == ret.data.length - 1 ? 'last' : ''));
				    var item=[
				      '<dl class="approval-item">',
				        '<dt class="clearfix">',
				        	'<div class="a-tit"><b>' + e.context + '</b></div>',
				        	'<span class="time state-lb">' + e.time + '</span>',
				        '</dt>',
				      '</dl>'
				    ];
				    htmlTmp.push(item.join(""));
				});
			} else {
				htmlTmp.push(ret.message);
			}
	  		
	  		var $model=$("<div/>").html($("#logisticsDialog").html());

	  		$model.find(".express-num").html(waybillCode);
	  		$model.find(".express-name").html(logisticsCompValues[0]);
	  		$model.find(".logistics-box").html(htmlTmp.join(""));
	  		
	  		$.mydialog({
	  			id:'dialog',
	  			width:480,
	  			height:600,
	  			zIndex:11000,
	  			content: $model.html()
	  		});
  	  },
  	  error: function() {
  		  alert("请求超时！");
  	  }
  	});
}
</script>
</body>
</html>
