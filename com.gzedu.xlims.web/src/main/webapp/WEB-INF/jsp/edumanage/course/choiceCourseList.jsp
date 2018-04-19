<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业操作管理</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<form id="listForm" class="form-horizontal">
<input type="hidden" name="action" value="${param.action }" id="actionFlag"/>
<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">选择课程</h3>
	</div>
	<div class="box-body">
		<div class="box-border">
		    <div class="pad-t15 clearfix">
		    	 <div class="col-xs-4">
		            <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">课程代码</label>
		              <div class="col-xs-9">
		                <input class="form-control" type="text" name="search_LIKE_kch" value="${param.search_LIKE_kch}">
		              </div>
		            </div>
		          </div>
		          <div class="col-xs-4">
		            <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">课程名称</label>
		              <div class="col-xs-9">
		                <input class="form-control" type="text" name="search_LIKE_kcmc" value="${param.search_LIKE_kcmc}">
		              </div>
		            </div>
		          </div>
		           <div class="col-xs-4">
		            <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">课程层次</label>
		              <div class="col-xs-9">
		                	<select name="search_EQ_pycc" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="">请选择</option>
								<c:forEach items="${pyccMap}" var="map">
									<option value="${map.key}" <c:if test="${map.key==param.search_EQ_pycc}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
		              </div>
		            </div>
		          </div>
		          <%-- <div class="col-xs-4">
		          	<div class="form-group">
						<label class="control-label col-xs-3 text-nowrap">适用行业</label>
						<div class="col-xs-9">
							<select name="search_EQ_syhy" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="">请选择</option>
								<c:forEach items="${syhyMap}" var="map">
									<option value="${map.key}" <c:if test="${map.key==param.search_EQ_syhy}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				  </div> --%>
				  <div class="col-xs-4">
		          	<div class="form-group">
						<label class="control-label col-xs-3 text-nowrap">适用专业</label>
						<div class="col-xs-9">
							<select name="search_LIKE_syzy" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="">请选择</option>
								<c:forEach items="${syzyMap}" var="map">
									<option value="${map.key}" <c:if test="${map.key==param.search_LIKE_syzy}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
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
		<div class="table-responsive">
			<div class="slim-Scroll" style="height:500px;overflow:hidden;">
				<table class="batch-teacher  table-bordered table-hover table-striped vertical-mid text-center table-font margin-bottom-none">
					<thead>
						<tr>
							<th>选择</th>
							<th>课程代码</th>
							<th>课程名称</th>
							<th>课程属性</th>
							<!-- <th>适用行业</th> -->
							<th>适用专业</th>
							<th>学分</th>
							<th>学时</th>
							<!-- <th>所属院校</th> -->
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
											<i class="fa fa-circle-o"></i>
										</td>
										<td>
											<span class="code">${entity.kch}</span>
											<input type="hidden" class="courseId" value="${entity.courseId}"/>
											<input type="hidden" class="courseType" value="${entity.courseType}"/>
										</td>
										<td><span class="name">${entity.kcmc}</span></td>
					            		<td style="text-align: left;">
					            			<%-- 课程类型：<span class="courseTypeName">${courseTypeMap[entity.courseType]}</span><br> --%>

											教学方式：${wsjxzkMap[entity.wsjxzk]}<br>
											
											课程性质：${courseNatureMap[entity.courseNature]}<br>
											
											课程层次：${pyccMap[entity.pycc]}<br>
											
											<%-- 课程门类：${categoryMap[entity.category]}<br>
											
											课程学科：${subjectMap[entity.subject]} --%>
										            			
					            			课程类型：
					            			<%-- <c:choose>
					            				<c:when test="${entity.courseCategory == 0}">普通课程</c:when>
					            				<c:when test="${entity.courseCategory == 1}">社会实践</c:when>
					            				<c:when test="${entity.courseCategory == 2}">毕业论文</c:when>
					            			</c:choose> --%>
					            			
					            			${courseCategoryMap[entity.courseCategory]}
					            			
					            		</td>
										<%-- <td>${syhyMap[entity.syhy]}</td> --%>
										<td>
						                	<c:if test="${not empty entity.syzy}">
						                		<c:forEach items="${fn:split(entity.syzy, ',')}" var="syzy">
						                			${syzyMap[syzy]} <br>
						                		</c:forEach>
						                	</c:if>
										</td>
										<td><span class="credit">${entity.credit}</span></td>
										<td><span class="hour">${entity.hour}</span></td>
										<%-- <td>${entity.gjtOrg.orgName}</td> --%>
						            	<c:if test="${entity.isEnabled==1}"><td class="text-green">已启用</td></c:if>
					            		<c:if test="${entity.isEnabled==0}"><td class="text-red">暂无资源</td></c:if>
					            		<c:if test="${entity.isEnabled==2}"><td class="text-orange">建设中</td></c:if>
									</tr>
								</c:if>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td align="center" colspan="9">
									  没有搜索到相关课程！
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
				<tags:pagination page="${pageInfo}" paginationSize="10" />		
			</div>			
		</div>
	</div>
</div>
</form>
<div class="text-right pop-btn-box pad">
	<span class="pull-left pad-t5">
			已选择课程：<b class="text-light-blue inline-block selected-course" width></b>
	</span>
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确定</button>
</div>

<script type="text/javascript">

$(function(){
/* 	$("[data-role='single-page']").click(function(event) {
		 $.ajax({			    
			    url:"${ctx}/edumanage/course/create",			 
			 success:function(data){		  
			 // parent.$.closeDialog(frameElement.api);			  
			 }
		  });
	}); */
	$('.slim-Scroll').slimScroll({
	    height: 250,
	    size: '5px'
	});

	//确定选择
	$("[data-role='save-data']").click(function(event) {
	  event.preventDefault();
	  if(self!==parent){
	      	var name=$.trim($(".batch-teacher tr.on").find(".name").text());
		    var courseId=$.trim($(".batch-teacher tr.on").find(".courseId").val());
		    if(parent.choiceCourseCallback){
				parent.choiceCourseCallback(courseId,name);
		    }
		}
	 	//parent.$.closeDialog(frameElement.api);
	});

	// 选中
	$(".batch-teacher").on('click', 'tr', function(event) {
		event.preventDefault();
		if($(this).hasClass('on')){
			$(this).removeClass('on');
			$(".selected-course").text("");
		}
		else{
			$(this).addClass('on');
			$(".selected-course").text($(this).find(".name").parent().text());
		}
		$(this).siblings('tr').removeClass('on');


	});

    //关闭 弹窗
    $("button[data-role='close-pop']").click(function(event) {
    	parent.$.closeDialog(frameElement.api);
    });

})
</script>
</body>
</html>
