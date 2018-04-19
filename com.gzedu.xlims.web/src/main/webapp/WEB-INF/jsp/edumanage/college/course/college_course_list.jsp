<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<ol class="breadcrumb oh">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教学管理</a></li>
		<li class="active">课程管理</li>
	</ol>
</section>
<section class="content">
<form id="listForm"  class="form-horizontal">
	<div class="box">
	    <div class="box-body">
	      	<input type="hidden" name="college_model_tag" value="college" />
	        <div class="row pad-t15">
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">课程名称</label>
	              <div class="col-sm-9">
	                <input class="form-control" type="text" name="search_LIKE_kcmc" value="${param.search_LIKE_kcmc}">
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">教学方式</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_wsjxzk" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${wsjxzkMap}" var="s">
							<option value="${s.key}"  <c:if test="${s.key==param['search_EQ_wsjxzk']}">selected='selected'</c:if>>${s.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">课程性质</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_courseNature" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${courseNatureMap}" var="s">
							<option value="${s.key}"  <c:if test="${s.key==param['search_EQ_courseNature']}">selected='selected'</c:if>>${s.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">状态</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_isEnabled" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="">请选择</option>
						<option value="1" <c:if test="${param.search_EQ_isEnabled=='1'}">selected='selected'</c:if>>已启用</option>
						<option value="0" <c:if test="${param.search_EQ_isEnabled=='0'}">selected='selected'</c:if>>未启用</option>
					</select>
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">适用行业</label>
	              <div class="col-sm-9">
					<select name="search_EQ_syhy" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${syhyMap}" var="s">
							<option value="${s.key}"  <c:if test="${s.key==param['search_EQ_syhy']}">selected='selected'</c:if>>${s.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	          </div>
	          <div class="col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">适用专业</label>
	              <div class="col-sm-9">
					<select name="search_EQ_syzy" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${syzyMap}" var="s">
							<option value="${s.key}"  <c:if test="${s.key==param['search_EQ_syzy']}">selected='selected'</c:if>>${s.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	          </div>
	        </div>
	      
	    </div><!-- /.box-body -->
	    <div class="box-footer">
	      <div class="btn-wrap">
				<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
			</div>
			<div class="btn-wrap">
				<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
			</div>
	    </div><!-- /.box-footer-->
	</div>
	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="box">
		<div class="box-header with-border">
			<h3 class="box-title pad-t5">课程列表</h3>			
		</div>
		<div class="box-body">
			<div class="filter-tabs filter-tabs2 clearfix">
				<ul class="list-unstyled">
					<li lang=":input[name='search_EQ_isEnabled']" <c:if test="${empty param['search_EQ_isEnabled']}">class="actived"</c:if>>全部(${isEnabledNum + isNotEnabledNum})</li>
					<li value="1" role=":input[name='search_EQ_isEnabled']" <c:if test="${param['search_EQ_isEnabled'] == 1 }">class="actived"</c:if>>已启用(${isEnabledNum})</li>
					<li value="0" role=":input[name='search_EQ_isEnabled']" <c:if test="${param['search_EQ_isEnabled'] == '0' }">class="actived"</c:if>>未启用(${isNotEnabledNum})</li>
				</ul>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered table-striped vertical-mid text-center table-font">
					<thead>
		              <tr>
		                <th>课程代码</th>
		                <th>课程名称</th>
		                <th>课程属性</th>
		                <th>适用行业</th>
		                <th>适用专业</th>
		                <th>学时</th>
		                <th>状态</th>
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
						            			${entity.kch}
						            		</td>
						            		<td>
						            			${entity.kcmc}
						            		</td>
						            		<td style="text-align: left;">

												教学方式：${wsjxzkMap[entity.wsjxzk]}<br>
												
												课程性质：${courseNatureMap[entity.courseNature]}<br>
												
												课程层次：${pyccMap[entity.pycc]}<br>
												
						            			
						            		</td>
						            		<td>
						            			${syhyMap[entity.syhy]}
						            		</td>
						            		<td>${syzyMap[entity.syzy]}</td>
						            		<td>${entity.hour}</td>
						            		<c:if test="${entity.isEnabled==1}"><td class="text-green">
						            		已启用</td></c:if>
						            			<c:if test="${entity.isEnabled!=1}"><td class="text-red">未启用</td></c:if>
						            			
						            		<td>
													<a href="${ctx}/edumanage/courseCollege/view/${entity.courseId}" 
														class="operion-item" title="查看">
														<i class="fa fa-fw fa-view-more"></i></a>
						            			
						            		</td>
						            	</tr>	
						       		</c:if>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="9">暂无数据</td>
								</tr>
							</c:otherwise>
						</c:choose>          	
		            </tbody>
				</table>
				<tags:pagination page="${pageInfo}" paginationSize="5" />				
			</div>
		</div>		
	</div>
	</form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
function choice(flag){
	$("#changetype").val(flag);
	$("#listForm").submit();
}

//共享课程
$("[data-role='share']").click(function(event) {
	event.preventDefault();
	$.mydialog({
	  id:'import',
	  width:600,
	  height:415,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')
	});
});
</script>
</body>
</html>
