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
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">论文管理</a></li>
		<li class="active">论文安排</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<div class="box">
	  <div class="box-body">
	      <div class="row pad-t15">
	        <div class="col-sm-4 col-xs-6">
	          <div class="form-group">
	            <label class="control-label col-sm-3 text-nowrap">论文计划</label>
	            <div class="col-sm-9">
	              <select name="search_EQ_thesisPlanId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${thesisPlanMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==thesisPlanId}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	            </div>
	          </div>
	        </div>
	        <div class="col-sm-4 col-xs-6">
	          <div class="form-group">
	            <label class="control-label col-sm-3 text-nowrap">专业</label>
	            <div class="col-sm-9">
	                <select name="search_EQ_gjtSpecialtyBase.specialtyBaseId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${specialtyMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtSpecialtyBase.specialtyBaseId']}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	            </div>
	          </div>
	        </div>
	        <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">层次</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_gjtSpecialtyBase.specialtyLayer" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${pyccMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtSpecialtyBase.specialtyLayer']}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	        </div>
	        <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">状态</label>
	              <div class="col-sm-9">
	                <select name="status" id="status" class="selectpicker form-control">
						<option value="0">全部</option>
						<option value="1" <c:if test="${param.status == 1}">selected="selected"</c:if> >未开始</option>
						<option value="2" <c:if test="${param.status == 2}">selected="selected"</c:if> >进行中</option>
						<option value="3" <c:if test="${param.status == 3}">selected="selected"</c:if> >已结束</option>
						<%-- <option value="4" <c:if test="${param.status == 4}">selected="selected"</c:if> >未设置论文指导老师</option>
						<option value="5" <c:if test="${param.status == 5}">selected="selected"</c:if> >未设置论文答辩老师</option> --%>
					</select>
	              </div>
	            </div>
	        </div>
	      </div>
	  </div><!-- /.box-body -->
	  <div class="box-footer">
	    <div class="pull-right"><button type="reset" class="btn min-width-90px btn-default">重置</button></div>
	    <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
	  </div><!-- /.box-footer-->
	</div>

	<div class="box margin-bottom-none">
		<div class="box-header with-border">
		  <h3 class="box-title pad-t5">论文安排列表</h3>
		  <div class="pull-right no-margin">
		  	<c:if test="${isBtnCreate }">
		    	<a href="create" class="btn btn-default btn-sm"><i class="fa fa-fw fa-plus"></i> 设置毕业论文安排</a>
		    </c:if>
		  </div>
		</div>
		<div class="box-body">
			<div class="filter-tabs filter-tabs2 clearfix">
				<ul class="list-unstyled">
					<li lang=":input[name='status']" <c:if test="${empty param['status']}">class="actived"</c:if>>全部（${notStart + starting + end}）</li>
					<li value="1" role=":input[name='status']" <c:if test="${param['status'] == 1 }">class="actived"</c:if>>未开始（${notStart}）</li>
					<li value="2" role=":input[name='status']" <c:if test="${param['status'] == 2 }">class="actived"</c:if>>进行中（${starting}）</li>
					<li value="3" role=":input[name='status']" <c:if test="${param['status'] == 3 }">class="actived"</c:if>>已结束（${end}）</li>
					<%-- <li value="4" role=":input[name='status']" <c:if test="${param['status'] == 4 }">class="actived"</c:if>>未设置论文指导老师（${notSetGuideTeacher}）</li>
					<li value="5" role=":input[name='status']" <c:if test="${param['status'] == 5 }">class="actived"</c:if>>未设置论文答辩老师（${notSetDefenceTeacher}）</li> --%>
				</ul>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered vertical-mid text-center table-font">
					<thead class="with-bg-gray">
		              <tr>
		              	<th>论文计划</th>
		                <th>专业信息</th>
		                <th>可申请人数</th>
		                <th>已申请人数</th>
		                <th>未申请人数</th>
		                <th>已通过人数</th>
		                <th>未通过人数</th>
		                <th>论文指导老师</th>
		                <th>论文答辩老师</th>
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
						            			${entity.gjtThesisPlan.thesisPlanName}<br>
						            			<div class="gray9">（${entity.gjtThesisPlan.thesisPlanCode}）</div>
						            		</td>
						            		<td>
						            			<div class="text-left">
						            				专业代码：${entity.gjtSpecialtyBase.specialtyCode}<br>
						            				专业名称：${entity.gjtSpecialtyBase.specialtyName}<br>
						            				<c:set var="pycc">${entity.gjtSpecialtyBase.specialtyLayer}</c:set>
						            				层次：${pyccMap[pycc]}
						            			</div>
						            		</td>
						            		<td><a href="listStudent?thesisPlanId=${entity.gjtThesisPlan.thesisPlanId}&specialtyBaseId=${entity.gjtSpecialtyBase.specialtyBaseId}">${entity.canApplyNum}</a></td>
						            		<td>
						            			${entity.applyNum}
						            			<div class="gray9">（<fmt:formatNumber type="number" value="${(entity.applyNum / entity.canApplyNum) * 100}" pattern="0" maxFractionDigits="0"/>%）</div>
						            		</td>
						            		<td>
						            			${entity.canApplyNum - entity.applyNum}
						            			<div class="gray9">（<fmt:formatNumber type="number" value="${((entity.canApplyNum - entity.applyNum) / entity.canApplyNum) * 100}" pattern="0" maxFractionDigits="0"/>%）</div>
						            		</td>
						            		<td>
						            			${entity.passNum}
						            			<div class="gray9">（
						            			<c:choose>
						            				<c:when test="${entity.applyNum == 0}">0%</c:when>
						            				<c:otherwise>
						            					<fmt:formatNumber type="number" value="${(entity.passNum / entity.applyNum) * 100}" pattern="0" maxFractionDigits="0"/>%
						            				</c:otherwise>
						            			</c:choose>
						            			）</div>
						            		</td>
						            		<td>
						            			${entity.applyNum - entity.passNum}
						            			<div class="gray9">（
						            			<c:choose>
						            				<c:when test="${entity.applyNum == 0}">0%</c:when>
						            				<c:otherwise>
						            					<fmt:formatNumber type="number" value="${((entity.applyNum - entity.passNum) / entity.applyNum) * 100}" pattern="0" maxFractionDigits="0"/>%
						            				</c:otherwise>
						            			</c:choose>
						            			）</div>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${not empty entity.gjtThesisAdvisers1}">
						            					${fn:length(entity.gjtThesisAdvisers1)}
						            				</c:when>
						            				<c:otherwise>
						            					<div class="text-orange">未设置</div>
						            				</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${not empty entity.gjtThesisAdvisers2 || not empty entity.gjtThesisAdvisers3}">
						            					${fn:length(entity.gjtThesisAdvisers2) + fn:length(entity.gjtThesisAdvisers3)}
						            				</c:when>
						            				<c:otherwise>
						            					<div class="text-orange">未设置</div>
						            				</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td class="text-orange">
						            			<c:if test="${entity.status == 1}">
													<span class="text-orange">未开始</span>
												</c:if>
												<c:if test="${entity.status == 2}">
													<span class="text-green">进行中</span>
												</c:if>
												<c:if test="${entity.status == 3}">
													<span class="gray9">已结束</span>
												</c:if>
						            		</td>
						            		<td>
						            			<c:if test="${entity.canUpdate && isBtnUpdate}">
						            				<a href="update/${entity.arrangeId}" class="operion-item" data-toggle="tooltip" title="设置" data-role="set"><i class="fa fa-fw fa-gear"></i></a>
						            				<a href="updateDefenceTeacher/${entity.arrangeId}" class="operion-item" data-toggle="tooltip" title="分配答辩老师" data-role="set"><i class="fa fa-fw fa-group-person"></i></a>
						            			</c:if>
						            			<c:if test="${isBtnView}">
						            				<a href="view/${entity.arrangeId}" class="operion-item" data-toggle="tooltip" title="查看" data-role="view"><i class="fa fa-fw fa-view-more"></i></a>
						            			</c:if>
						            		</td>
						            	</tr>
		            				</c:if>
						        </c:forEach>
						    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="11">暂无数据</td>
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
</section>
		
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

//设置毕业论文安排
$('body').on('click', '[data-role="set"]', function(event) {
	event.preventDefault();
	var _this=this;
  	$.mydialog({
	  id:'set-range',
	  width:2000,
	  height:2000,
	  zIndex:11000,
	  content: 'url:'+$(_this).attr('href')
	});
})
.on('click', '[data-role="view"]', function(event) {
	event.preventDefault();
	var _this=this;
  	$.mydialog({
	  id:'view-range',
	  width:2000,
	  height:2000,
	  zIndex:11000,
	  content: 'url:'+$(_this).attr('href')
	});
});

</script>


</body>
</html>
