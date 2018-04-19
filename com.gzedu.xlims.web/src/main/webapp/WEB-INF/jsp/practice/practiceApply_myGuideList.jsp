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
		<li><a href="#">实践管理</a></li>
		<li class="active">我的指导</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<div class="box">
	  <div class="box-body">
	      <div class="row pad-t15">
	        <div class="col-sm-4 col-xs-6">
	          <div class="form-group">
	            <label class="control-label col-sm-3 text-nowrap">实践计划</label>
	            <div class="col-sm-9">
	               <select name="search_EQ_practicePlanId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${practicePlanMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==practicePlanId}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	            </div>
	          </div>
	        </div>
	        <div class="col-sm-4 col-xs-6">
	          <div class="form-group">
	            <label class="control-label col-sm-3 text-nowrap">学期</label>
	            <div class="col-sm-9">
	                <select name="search_EQ_gjtStudentInfo.nj" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${gradeMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.nj']}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	            </div>
	          </div>
	        </div>
	        <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">层次</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_gjtStudentInfo.gjtSpecialty.pycc" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${pyccMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtSpecialty.pycc']}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	        </div>
	      </div>
	      <div class="row pad-t15">
	        <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">专业</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${specialtyMap}" var="map">
							<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtSpecialty.specialtyId']}">selected='selected'</c:if>>${map.value}</option>
						</c:forEach>
					</select>
	              </div>
	            </div>
	        </div>
	        <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">姓名</label>
	              <div class="col-sm-9">
	                <input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xm" value="${param['search_LIKE_gjtStudentInfo.xm']}">
	              </div>
	            </div>
	        </div>
	        <div class="col-xs-6 col-sm-4">
	            <div class="form-group">
	              <label class="control-label col-sm-3 text-nowrap">学号</label>
	              <div class="col-sm-9">
	                <input class="form-control" type="text" name="search_LIKE_gjtStudentInfo.xh" value="${param['search_LIKE_gjtStudentInfo.xh']}">
	              </div>
	            </div>
	        </div>
	      </div>
	      <div class="row pad-t15">
	        <div class="col-xs-6 col-sm-4">
				<div class="form-group">
					<label class="control-label col-sm-3 text-nowrap">未指导</label>
					<div class="col-sm-9">
						<select name="search_EQ_isGuide" class="form-control">
							<option value="" <c:if test="${empty param['search_EQ_isGuide']}">selected='selected'</c:if>>是</option>
							<option value="1" <c:if test="${param['search_EQ_isGuide'] == 1}">selected='selected'</c:if>>否</option>
							<option value="2" <c:if test="${param['search_EQ_isGuide'] == 2}">selected='selected'</c:if>>全部</option>
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
		  <h3 class="box-title pad-t5">我的指导列表</h3>
		</div>
		<div class="box-body">
			<div>
				<table class="table table-bordered vertical-mid text-center table-font">
					<thead class="with-bg-gray">
		              <tr>
		              	<th>实践计划</th>
		                <th>个人信息</th>
		                <th>报读信息</th>
		                <th>最后提交时间</th>
		                <th>提交文件</th>
		                <th>提交内容</th>
		                <th>指导次数</th>
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
						            			${entity.gjtPracticePlan.practicePlanName}<br>
						            			<div class="gray9">（${entity.gjtPracticePlan.practicePlanCode}）</div>
						            		</td>
						            		<td>
						            			<div class="text-left">
						            				姓名：${entity.gjtStudentInfo.xm} <br>
						            				学号：${entity.gjtStudentInfo.xh} <br>
						            				<shiro:hasPermission name="/personal/index$privacyJurisdiction">
						            				手机：${entity.gjtStudentInfo.sjh}
						            				</shiro:hasPermission>
						            			</div>
						            		</td>
						            		<td>
						            			<div class="text-left">
						            				层次：${pyccMap[entity.gjtStudentInfo.gjtSpecialty.pycc]} <br>
						            				学期：${entity.gjtStudentInfo.gjtGrade.gradeName} <br>
						            				专业：${entity.gjtStudentInfo.gjtSpecialty.zymc}
						            			</div>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${not empty entity.guideRecord}">
						            					<fmt:formatDate value="${entity.guideRecord.createdDt}" pattern="yyyy-MM-dd HH:mm"/>
						            				</c:when>
						            				<c:otherwise>--</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${not empty entity.guideRecord}">
						            					<a href="http://eezxyl.gzedu.com?furl=${entity.guideRecord.attachment}" data-role="addon-preview">${entity.guideRecord.attachmentName}</a>
						            				</c:when>
						            				<c:otherwise>--</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${not empty entity.guideRecord}">
						            					${entity.guideRecord.content}
						            				</c:when>
						            				<c:otherwise>--</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			${entity.guideTimes}
						            		</td>
						            		<td>
						            			${practiceStatusMap[entity.status]}
						            		</td>
						            		<td>
						            			<c:if test="${isBtnView}">
						            				<a href="${ctx}/practiceApply/view?practicePlanId=${entity.practicePlanId}&studentId=${entity.studentId}&isTeacher=true" class="operion-item" data-toggle="tooltip" title="查看详情" data-role="set"><i class="fa fa-fw fa-view-more"></i></a>
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
</section>
		
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
 
<script type="text/javascript">

$(function() {
	
	//附件预览
	$('[data-role="addon-preview"]').click(function(event) {
	  event.preventDefault();
	  var _this=this;
	  var $pop=$.alertDialog({
	    id:'addon-preview',
	    title:'在线预览',
	    width:$(window).width(),
	    height:$(window).height(),
	    zIndex:11000,
	    content: '',
	    cancelLabel:'关闭',
	    cancelCss:'btn btn-default min-width-90px margin_r15',
	    okLabel:'下载文档',
	    okCss:'btn btn-primary min-width-90px'
	  });

	  //载入附件内容
	  $('.box-body',$pop).addClass('overlay-wrapper position-relative').html([
	    '<iframe src="'+$(_this).attr('href')+'" id="Iframe-addon-preview" name="Iframe-addon-preview" frameborder="0" scrolling="auto" style="width:100%;height:100%;position:absolute;left:0;top:0;"></iframe>',
	    '<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>'
	  ].join(''));

	  $('#Iframe-addon-preview').on('load',function(){
	    $('.overlay',$pop).hide();
	  });
	});
	
});

</script>
</body>
</html>
