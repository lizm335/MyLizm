<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>基础考点列表</title>
   <%@ include file="/WEB-INF/jsp/common/jslibs_new.jsp"%>
</head>
<body>
<form id="listForm" class="form-horizontal" action="${ctx}/exam/new/point/choiceMultiPointList" method="post">
	<input type="hidden" name="action" value="${param.action }" id="actionFlag"/>
	<div class="box no-border no-shadow">
		<div class="box-header with-border">
			<h3 class="box-title">选择考点</h3>
		</div>
		<div class="box-body">
			<div class="box-border">
			    <div class="pad-t15 clearfix">

		          <div class="col-sm-4">
		            <div class="form-group">
		              <label class="control-label col-sm-3 text-nowrap">考试计划</label>
		              <div class="col-sm-9">
		                <select class="form-control select2" id="examBatchId" name="examBatchId" data-size="5" data-live-search="true">
		                  <option value="" selected="selected">请选择</option>
		                  <c:forEach items="${batchMap }" var="map">
		                  	<option value="${map.EXAM_BATCH_ID }" <c:if test="${map.EXAM_BATCH_ID==param['examBatchId']}">selected='selected'</c:if>>${map.EXAM_BATCH_NAME }</option>
		                  </c:forEach>
		                </select>
		              </div>
		            </div>
		          </div>	
		        </div>
	        </div>
			<div class="box-border">
			    <div class="pad-t15 clearfix">
			    	 <div class="col-sm-4">
		            <div class="form-group">
		              <label class="control-label col-sm-3 text-nowrap">考试形式</label>
		              <div class="col-sm-9">
		                <select class="form-control select2" id="EXAM_TYPE" name="EXAM_TYPE" data-size="5" data-live-search="true">
		                  <option value="" selected="selected">请选择</option>
							<option value="8" <c:if test="${EXAM_TYPE=='8'}">selected='selected'</c:if>>笔试</option>
							<option value="11" <c:if test="${EXAM_TYPE=='11'}">selected='selected'</c:if>>机试</option>
		                </select>
		              </div>
		            </div>
		          </div>			          
	
		          <div class="col-sm-4">
		            <div class="form-group">
		              <label class="control-label col-sm-3 text-nowrap">考点名称</label>
		              <div class="col-sm-9">
		                <input class="form-control" id="NAME" name="NAME" value="${param['NAME']}" />
		              </div>
		            </div>
		          </div>
		          
		          <div class="col-sm-4">
		          	<div class="form-group">
		          		<label class="control-label col-sm-3 text-nowrap">所在区域</label>
		          		<div class="col-sm-9">
		          			<div class="select-box"> <i class="select-ico"> <span class="caret"></span> </i>
		          				<div class="select-in">
		          					<ins data-seltype="Pro"> <span>省份</span>
		          						<input type="hidden" name="PROVINCE_NAME">
		          					</ins>
		          					<ins data-seltype="City"> <span>城市</span>
		          						<input type="hidden" name="CITY_NAME">
		          					</ins>
		          					<ins data-seltype="District"> <span>区/县</span>
		          						<input type="hidden" name="CITYNAME">
		          					</ins> 
		          				</div>
		          				<ul class="itemSelBox dropdown-menu" style="display: none;"></ul>
		          			</div>
		          		</div>
		          	</div>
		          </div>
			    </div>
		    </div>
		</div>
		<div class="box-footer">
	      <div class="btn-wrap">
				<button type="button" class="btn btn-default btn-reset">重置</button>
			</div>
			<div class="btn-wrap">
				<button type="submit" class="btn btn-primary">搜索</button>
			</div>
	    </div>
		<div class="box-body">
			<div class="margin_b10 clearfix">
				<h3 class="cnt-box-title f16">考点列表</h3>
			</div>
			
			<div class="table-responsive">
				<div class="slim-Scroll" style="height:500px;overflow:hidden;">
					<table class="batch-teacher table table-bordered table-hover table-striped vertical-mid text-center table-font margin-bottom-none">
						<thead>
							<tr>
								<th>选择</th>
				              	<th>考试形式</th>
				                <th>考点名称</th>
				                <th>所在区域	</th>
								<th>详细地址</th>
								<th>适用范围</th>
				                <th>使用状态</th>
							</tr>
						</thead>
						<tbody>
						<c:choose>
							<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="entity">
									<c:if test="${not empty entity}">
										<tr>
											<td>
												<input type="checkbox" name="pointIds" class="flat-red" value="${entity.EXAM_POINT_ID }" text="${entity.EXAM_POINT_NAME }">
											</td>
											<td>
												<c:if test="${entity.EXAM_TYPE=='8'}">笔试</c:if>
												<c:if test="${entity.EXAM_TYPE=='11'}">机试</c:if>
			            					</td>
			            					<td>
					            				${entity.EXAM_POINT_NAME }<br>
					            				<span class="gray9">${entity.CODE }</span>
					            			</td>
					            			<td>
					            				${entity.PROVINCE_NAME }-${entity.CITY_NAME }-${entity.DISTRICT_NAME }
					            			</td>
					            			<td>
					            				${entity.ADDRESS }
					            			</td>
											<td>
												<c:choose>
													<c:when test="${fn:length(entity.gjtStudyCenters) == 0}">
														通用
													</c:when>
													<c:otherwise>
														<c:forEach items="${entity.gjtStudyCenters}" var="s">
															${s.scName}<br>
														</c:forEach>
													</c:otherwise>
												</c:choose>
											</td>
					            			<td> 
												<c:if test="${entity.IS_ENABLED eq 1}">在用 </c:if>
												<c:if test="${entity.IS_ENABLED eq 0}">停用 </c:if>
											</td>
										</tr>
									</c:if>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="10">
										  没有搜索到相关考点！
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
						</tbody>
					</table>
					<tags:pagination page="${pageInfo}" paginationSize="3" />			
				</div>			
			</div>
		</div>
	</div>
</form>
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确定</button>
</div>

<script type="text/javascript">
$(function(){
	$('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
		checkboxClass: 'icheckbox_flat-green',
		radioClass: 'iradio_flat-green'
	});
/* 	$("[data-role='single-page']").click(function(event) {
		 $.ajax({			    
			    url:"${ctx}/edumanage/course/create",			 
			 success:function(data){		  
			 // parent.$.closeDialog(frameElement.api);			  
			 }
		  });
	}); */
	$('.slim-Scroll').slimScroll({
	    height: 400,
	    size: '5px'
	});

	//确定选择
	$("[data-role='save-data']").click(function(event) {
	  event.preventDefault();
	  if(self!==parent){
		  var examBatchId = $(':input[name="examBatchId"]').val();
	      	if(examBatchId == ''){
	      	    alert('请先选择考试计划');
	      	    return;
	      	}
	      	if($(':input[name="pointIds"]:checked').length==0){
	      	    alert('请先选择考点');
	      	    return;
	      	}
	      	var pointIds = new Array();
	      	var names = new Array();
	      	$(':input[name="pointIds"]:checked').each(function(index, el) {
			    var pointId=$.trim($(this).val());
			    pointIds.push(pointId);

			    var name=$.trim($(this).attr("text"));
			    names.push(name);
			});
	      	
		    if(parent.choicePointCallback){
				parent.choicePointCallback(pointIds, names, examBatchId);
		    }
		}
	 	parent.$.closeDialog(frameElement.api);
	});

	// 选中
	$(".batch-teacher").on('click', 'tr', function(event) {
		event.preventDefault();
		if($(this).hasClass('on')){
			$(this).removeClass('on');
		}
		else{
			$(this).addClass('on');
		}

	});

    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
    	parent.$.closeDialog(frameElement.api);
    });

})
</script>
</body>
</html>
