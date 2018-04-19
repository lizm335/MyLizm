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
		<li><a href="#">教材管理</a></li>
		<li><a href="#">订单管理</a></li>
		<li class="active">订单详情</li>
	</ol>
</section>
<section class="content">
	<div class="box no-margin">
		<div class="box-body pad">
			<div class="panel panel-default margin_t10">
		        <div class="panel-heading">
		          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-1" role="button"> 
		              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
		              <span class="margin-r-5">订单基本信息</span>
		          </h3>
		        </div>
		        <div id="info-box-1" class="collapse in">
		          <div class="panel-body">
		              	<table class="table-gray-th text-center">
			                <colgroup>
			                  <col width="13%">
			                  <col width="20%">

			                  <col width="13%">
			                  <col width="20%">

			                  <col width="13%">
			                  <col width="20%">
			                </colgroup>
			                <tbody>
			                  <tr>
			                    <th>订单号</th>
			                    <td>${textbookDistribute.orderCode }</td>

			                    <th>姓名</th>
			                    <td>${studentInfo.xm }
									<c:if test="${studentInfo.xbm =='1'}">（男）</c:if>
									<c:if test="${studentInfo.xbm =='2'}">（女）</c:if>
								</td>

			                    <th>学号</th>
			                    <td>${studentInfo.xh}</td>
			                  </tr>
			                  <tr>
			                    <th>联系电话</th>
			                    <td>
									<shiro:hasPermission name="/personal/index$privacyJurisdiction">
										${studentInfo.sjh}
									</shiro:hasPermission>
									<shiro:lacksPermission name="/personal/index$privacyJurisdiction">
										<c:if test="${not empty studentInfo.sjh }">
										${fn:substring(studentInfo.sjh,0, 3)}******${fn:substring(studentInfo.sjh,8, (studentInfo.sjh).length())}
										</c:if>
									</shiro:lacksPermission>
								</td>

			                    <th>收货地址</th>
			                    <td colspan="3">
			                    	${textbookDistribute.address}
			                    </td>
			                  </tr>
			                </tbody>
		              	</table>
		          </div>
		        </div>
		    </div>

		    <div class="panel panel-default margin_t10">
		        <div class="panel-heading">
		          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-2" role="button"> 
		              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
		              <span class="margin-r-5">商品信息</span>
		          </h3>
		        </div>
		        <div id="info-box-2" class="collapse in">
		          	<div class="panel-body">
		              	<table class="table-gray-th text-center">
		              		<colgroup>
		              			<col width="10%"></col>
		              			<col width="150"></col>
		              			<col></col>
		              			<col width="15%"></col>
		              			<col width="15%"></col>
		              		</colgroup>
			                <thead>
			                	<tr>
				                    <th>序号</th>
				                    <th>商品图片</th>

				                    <th>商品名称</th>
				                    <th>数量</th>

				                    <th>单价</th>
				                </tr>
			                </thead>
			                <tbody>
			                	<c:set var="quantityCount" value="0" />
			                	<c:set var="priceCount" value="0" />
			                	<c:forEach items="${textbookDistribute.gjtTextbookDistributeDetails}" var="item" varStatus="i">
				                	<tr>
					                  	<td>${i.count }</td>
					                  	<td>
					                  		<c:if test="${empty(item.gjtTextbook.cover) }">
					                  			<img src="${css }/common/v2/temp/demo_01.jpg" width="120" height="70">
					                  		</c:if>
					                  		<c:if test="${not empty(item.gjtTextbook.cover) }">
					                  			<img src="${item.gjtTextbook.cover}" width="120" height="70">
					                  		</c:if>
					                  	</td>
					                  	<td>
											${item.gjtTextbook.textbookName}
					                  	</td>
					                  	<td>${item.quantity}</td>
					                  	<td>￥${item.price}</td>
				                  	</tr>
			                	</c:forEach>
			                  

			                  	<tr>
				                  	<th colspan="3">
				                  		<div class="text-left">
				                  			订单编号：${textbookDistribute.orderCode}
				                  		</div>
				                  	</th>
				                  	<th>
				                  		共 ${fn:length(textbookDistribute.gjtTextbookDistributeDetails)} 件商品
				                  	</th>
				                  	<th>
				                  		实付款：￥${textbookDistribute.toTalPrice } 元
				                  		<c:if test="${textbookDistribute.freight>0 }">
				                  		(包含运费￥${textbookDistribute.freight}元)
				                  		</c:if>
				                  		
				                  	</th>
			                  	</tr>
			                </tbody>
		              	</table>
		          	</div>
		        </div>
		    </div>

		    <div class="panel panel-default margin_t10">
		        <div class="panel-heading">
		          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-3" role="button"> 
		              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
		              <span class="margin-r-5">快递信息</span>
		          </h3>
		        </div>
		        <div id="info-box-3" class="collapse in">
		          	<div class="panel-body">
		          		<div class="form-horizontal row border-bottom">
		          			<div class="col-sm-4 col-xs-6">
		          				<div class="form-group">
		          					<label class="control-label col-sm-3 text-nowrap text-no-bold">快递名称</label>
		          					<div class="col-sm-9">
		          						<select class="form-control logistics-input logisticsComp" disabled="disabled">
						                	<option>请选择</option>
						                	<c:forEach items="${expressMap }" var="map">
						                		<option <c:if test="${textbookDistribute.logisticsComp==map.value.concat('-').concat(map.key)}">selected="selected"</c:if> value="${map.value}-${map.key}">${map.value}-${map.key}</option>
						                	</c:forEach>
						              	</select>
		          					</div>
		          				</div>
		          			</div>
		          			<div class="col-sm-4 col-xs-6">
					          	<div class="form-group">
						            <label  class="control-label col-sm-3 text-nowrap text-no-bold">运单号</label>
						            <div class="col-sm-9">
						                <input class="form-control logistics-input waybillCode" disabled="disabled" value="${textbookDistribute.waybillCode }" placeholder="请输入您的快递单号">
						            </div>
					          	</div>
					        </div>
					        <div class="col-sm-4 col-xs-6">
					        	<button type="button" class="btn btn-primary margin_b15 save-logistics" style="display: none">保存</button>
					        	 <button type="button" class="btn btn-danger margin_l10 margin_b15 update-logistics">修改</button> 
					        </div>
		          		</div>

		              	<div class="logistics-box margin_t10">
				        	<div class="pad-l15 pad-t10 pad-b10" id="logistics-box">
							   
						    </div>
						</div>
		          	</div>
		        </div>
		    </div>

		    <div class="panel panel-default margin_t10">
		        <div class="panel-heading">
		          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-4" role="button"> 
		              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
		              <span class="margin-r-5">学员评价</span>
		          </h3>
		        </div>
		        <div id="info-box-4" class="collapse in">
		          	<div class="panel-body">
		          		<c:if test="${not empty(textbookDistribute.gjtTextbookComment)}">
		              	<table class="table-gray-th">
			                <colgroup>
			                  <col width="20%"/>
			                  <col/>
			                </colgroup>
			                <tbody>
			                  <tr>
			                    <th class="text-right">评价等级</th>
			                    <td>
			                    	<c:choose>
			                    		<c:when test="${textbookDistribute.gjtTextbookComment.commentType==0}">
			                    			好评
			                    		</c:when>
			                    		<c:when test="${textbookDistribute.gjtTextbookComment.commentType==1}">
			                    			中评
			                    		</c:when>
			                    		<c:when test="${textbookDistribute.gjtTextbookComment.commentType==2}">
			                    			差评
			                    		</c:when>
			                    	</c:choose>
			                    </td>
			                  </tr>
			                  <tr>
			                    <th class="text-right">评价内容</th>
			                    <td>
			                    	${textbookDistribute.gjtTextbookComment.commentContent }
			                    </td>
			                  </tr>
			                </tbody>
		              	</table>
		              	</c:if>
		              	<c:if test="${empty(textbookDistribute.gjtTextbookComment)}">
		              	<div class="text-center gray9 pad20">
		              		<i class="fa fa-fw fa-warning gray9" style="font-size: 80px;"></i>
		              		<div class="margin_t5 f20">
		              			暂无评价
		              		</div>
		              	</div>
		              	</c:if>
		          	</div>
		        </div>
		    </div>
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

$(function(){
    queryLogistics('${textbookDistribute.logisticsComp}','${textbookDistribute.waybillCode }');
	$('.update-logistics').click(function(){
	    $('.save-logistics').show();
	    $('.logistics-input').removeAttr('disabled');
	    $(this).hide();
	});
    
	$('.save-logistics').click(function(){
	   var postIngIframe=$.formOperTipsDialog({
                text:'数据提交中...',
                iconClass:'fa-refresh fa-spin'
            });
        var param={distributeId:'${param.distributeId}',waybillCode:$('.waybillCode').val(),logisticsComp:$('.logisticsComp').val()}
        $.get('${ctx}/textbookDistribute/updateDistributeLogistics',param,function(data){
            $.closeDialog(postIngIframe);
			if(data.successful){
			    postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
		            postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
					location.reload();
			}else{
				alert(data.message);
			}
        },'json');
	});;
})

function queryLogistics(logisticsComp, waybillCode) {
    if($.trim(logisticsComp)==''||$.trim(waybillCode)==''){
		return;
	 }
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
					var css = (i == 0) ? 'text-green' :'text';
				    var item=[
					'<dl>',
				    	'<dt class="'+css+'">' + e.context + '</dt>',
				        '<dd class="gray9">' + e.time + '</dd>',
				        '<dd class="icon-order"></dd>',
				    '</dl>'
				    ];
				    htmlTmp.push(item.join(""));
				});
			} else {
				htmlTmp.push(ret.message);
			}
	  		$('#logistics-box').html(htmlTmp);
  	  },
  	  error: function() {
  		  alert("请求超时！");
  	  }
  	});
}
</script>
</body>
</html>
