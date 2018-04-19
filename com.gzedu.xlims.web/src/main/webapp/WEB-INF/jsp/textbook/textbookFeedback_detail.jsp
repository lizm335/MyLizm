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
		<li><a href="#">学员反馈</a></li>
		<li class="active">反馈详情</li>
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
	        	<c:choose>
	        		<c:when test="${not empty entity.gjtStudentInfo.avatar}">
	        			<img src="${entity.gjtStudentInfo.avatar}" class="img-circle" alt="User Image" width="112" height="112">
	        		</c:when>
	        		<c:otherwise>
	        			<img src="${ctx }/static/images/headImg04.png" class="img-circle" alt="User Image" width="112" height="112">
	        		</c:otherwise>
	        	</c:choose>
	        </div>
	        <div class="media-body">
	          	<h3 class="margin_t10">${entity.gjtStudentInfo.xm}</h3>
	          	<table class="full-width per-tbl">
	          		<tr>
	          			<th width="40">学号:</th>
	          			<td>${entity.gjtStudentInfo.xh}</td>

	          			<th width="40">层次:</th>
	          			<td>${trainingLevelMap[entity.gjtStudentInfo.pycc]}</td>

	          			<th width="70">专业:</th>
	          			<td>${entity.gjtStudentInfo.gjtSpecialty.zymc}</td>
	          		</tr>
	          		<tr>
	          			<th width="40">手机:</th>
	          			<td>
	          			
	          				<shiro:hasPermission name="/personal/index$privacyJurisdiction">
								${entity.gjtStudentInfo.sjh}
							</shiro:hasPermission>
							<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
								<c:if test="${not empty entity.gjtStudentInfo.sjh }">
								${fn:substring(entity.gjtStudentInfo.sjh,0, 3)}******${fn:substring(entity.gjtStudentInfo.sjh,8, (entity.gjtStudentInfo.sjh).length())}
								</c:if>
							</shiro:lacksPermission>
	          			
	          			</td>

	          			<th width="40">年级:</th>
	          			<td>${entity.gjtStudentInfo.gjtGrade.gradeName}</td>

	          			<th>收货地址:</th>
	          			<td>
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
	        <h3 class="box-title text-bold">反馈信息</h3>
	    </div>
	    <div class="box-body">
	    	<div class="table-responsive">
	    		<table class="table no-border margin_t10 margin_b10 full-width">
	    			<tbody>
	    				<tr>
	    					<th width="75" class="no-pad-right">反馈类型:</th>
	    					<td>
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
	    					<th width="75" class="no-pad-right">教材名称:</th>
	    					<td>
	    						<c:forEach items="${entity.gjtTextbookFeedbackDetails}" var="detail" varStatus="status">
	    							<c:choose>
					          			<c:when test="${status.last}">${detail.gjtTextbook.textbookName}</c:when>
					          			<c:otherwise>${detail.gjtTextbook.textbookName}, </c:otherwise>
					          		</c:choose>
	    						</c:forEach>
	    					</td>
	    					<th width="75" class="no-pad-right">反馈时间:</th>
	    					<td><fmt:formatDate value="${entity.createdDt}" pattern="yyyy-MM-dd HH:mm"/></td>
	    				</tr>
	    				<tr>
	    					<th class="no-pad-right">反馈原因:</th>
	    					<td colspan="5">${entity.reason}</td>
	    				</tr>
	    			</tbody>
	    		</table>
	    	</div>
	    </div>
	</div>
	
	<div class="box">
		<div class="box-header with-border">
	        <h3 class="box-title text-bold">物流信息</h3>
	    </div>
	    <div class="box-body">
	    	<div class="table-responsive">
	    		<table class="table table-bordered table-striped margin_t10 margin_b10 vertical-mid text-center table-font">
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

	<c:if test="${isBtnDeal}">
	<div class="box margin-bottom-none">
		<div class="box-header with-border">
	        <h3 class="box-title text-bold">反馈处理</h3>
	        <div class="pull-right">
	        	<button class="btn btn-primary btn-sm min-width-90px margin-r-5" data-role="dial-tel">拨打电话</button>
	        	<button class="btn btn-default btn-sm min-width-90px">ee联系</button>
	        </div>
	    </div>
	    <div class="box-body">
	    	<form class="form-horizontal" id="replyForm" name="replyForm" action="${ctx}/textbookFeedback/deal" method="post">
    			<input type="hidden" name="feedbackId" value="${entity.feedbackId}">
    			<div class="form-group pad-t10">
    				<label class="control-label col-sm-2 text-nowrap no-pad-top">处理状态</label>
    				<div class="col-sm-7">
		                <div class="full-width">
							<c:choose>
								<c:when test="${entity.status == 1}">未处理</c:when>
								<c:otherwise>已处理</c:otherwise>
							</c:choose>
						</div>
		            </div>
    			</div>
    			<div class="form-group">
    				<label class="control-label col-sm-2 text-nowrap">处理回复</label>
    				<div class="col-sm-7">
		                <c:choose>
							<c:when test="${entity.status == 1}">
								<textarea id="reply" name="reply" class="form-control" rows="5" placeholder="请输入回复内容！"></textarea>
							</c:when>
							<c:otherwise>${entity.reply}</c:otherwise>
						</c:choose>
		            </div>
    			</div>
    			<c:if test="${entity.status == 1}">
	    			<div class="form-group">
	    				<div class="col-sm-7 col-sm-offset-2">
							<button class="btn btn-success min-width-90px margin_r15 btn-save-edit" data-role="btn-save-edit">保存</button>
							<button type="button" class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">取消</button>
						</div>
	    			</div>
    			</c:if>
	    	</form>
	    </div>
	</div>
	</c:if>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

$(function(){
	
	var xm = '${entity.gjtStudentInfo.xm}'
	var sjh = '${entity.gjtStudentInfo.sjh}';
	
	$("[data-role='btn-save-edit']").click(function(event) {
		var reply = $("#reply").val();
	    if (reply == "") {
		   alert("请输入回复内容！");
		   return false;
	    }
		   
		$("#replyForm").submit();
	});
	
	//拨打电话
	$("[data-role='dial-tel']").click(function(event) {
		$.mydialog({
		  id:'confirm',
		  width:600,
		  height:371,
		  zIndex:11000,
		  content: [
		  	'<div class="box no-border no-shadow margin-bottom-none" style="height:371px;">',
		  		'<div class="box-header with-border">',
					'<h3 class="box-title">拨打电话</h3>',
				'</div>',
				'<div class="box-body process-cnt-box">',
					'<div class="table-block full-width" style="height:100%;">',
						'<div class="table-cell-block vertical-mid text-center">',
							'<i class="fa fa-fw fa-exclamation-circle vertical-mid" style="color:#f39c12;font-size:54px;margin-top:-5px;"></i>',
							'<span>在处理反馈前，你可以拨打' + xm + '同学的电话了解具体情况</span>',
							'<div class="text-light-blue margin_t20" style="font-size:36px;">' + sjh + '</div>',
						'</div>',
					'</div>',
				'</div>',
				'<div class="pop-btn-box pad text-right" style="position:absolute;">',
					'<button type="button" class="btn btn-default min-width-90px" data-role="close">取消</button>',
					'<button type="button" class="btn btn-primary min-width-90px margin_l15" data-role="sure-btn">确定</button>',
				'</div>',
		  	'</div>'
		  ].join(''),
		  onLoaded:function(){
		  	var $box=$("[data-id='confirm']");
		  	//取消
		  	$('[data-role="close"]',$box).click(function(event) {
		  		$.closeDialog($box);
		  	});
		  	//确定
		  	$('[data-role="sure-btn"]',$box).click(function(event) {
		  		$.closeDialog($box);
		  	});
		  }
		});
	});
})
</script>
</body>
</html>
