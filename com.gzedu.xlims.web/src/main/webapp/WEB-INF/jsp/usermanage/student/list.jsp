<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>学生管理系统</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	
	$('.updatePwd').click(function(){
		 var $this = $(this);
		 var id=$(this).attr('val');
          $.confirm({
              title: '提示',
              content: '确认重置密码？',
              confirmButton: '确认',
              icon: 'fa fa-warning',
              cancelButton: '取消',  
              confirmButtonClass: 'btn-primary',
              closeIcon: true,
              closeIconClass: 'fa fa-close',
              confirm: function () { 
              	 $.post("updatePwd.html",{id:id},function(data){
              		if(data.successful){
              			showSueccess(data);
              			$this.parent().parent().parent().remove(); 
              		}else{
              			$.alert({
                   	    title: '失败',
                   	    icon: 'fa fa-exclamation-circle',
                   	    confirmButtonClass: 'btn-primary',
                   	    content: '网络异常！',
                   	    confirmButton: '确认',
                   	    confirm:function(){
                   	    	showFail(data);
                   	    }
                   	});
              		}
              },"json"); 
              } 
          });
      });
	
})
</script>

</head>
<body class="inner-page-body">
	<section class="content-header">
		
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">基础信息</a></li>
			<li class="active">学员管理</li>
		</ol>
	</section>


		<section class="content" data-id="0">
			<form class="form-horizontal" id="listForm" action="list.html">
			<div class="box">
				<div class="box-body">
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
							<label class="control-label col-sm-3">院校</label>
							<div class="col-sm-9">
								<select name="search_EQ_gjtSchoolInfo.id" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${schoolInfoMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtSchoolInfo.id']}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-4">
							<label class="control-label col-sm-3">学习中心</label>
							<div class="col-sm-9">
								<select name="search_EQ_gjtStudyCenter.id" class="selectpicker show-tick form-control" 	data-size="10" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${studyCenterMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param['search_EQ_gjtStudyCenter.id']}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-4">
								<label class="control-label col-sm-3">学期</label>
								<div class="col-sm-9">
									<select  name="search_EQ_gjtGrade.gradeId"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${gradeMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtGrade.gradeId']}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
					</div>
					<div class="row reset-form-horizontal clearbox">
						<div class="col-md-4">
								<label class="control-label col-sm-3">姓名</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_xm" value="${param.search_LIKE_xm }">
								</div>
							</div>
						
						<div class="col-md-4">
							<label class="control-label col-sm-3">层次</label>
							<div class="col-sm-9">
								<select name ="search_EQ_pycc" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${pyccMap}" var="map">
										<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_pycc}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-4">
							<label class="control-label col-sm-3">专业名称</label>
							<div class="col-sm-9">
								<select name="search_EQ_gjtSpecialty.specialtyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
									<option value="" selected="selected">请选择</option>
									<c:forEach items="${specialtyMap}" var="map">
										<option value="${map.key}" <c:if test="${map.key==param['search_EQ_gjtSpecialty.specialtyId']}">selected='selected'</c:if>>${map.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
						<c:choose>
							<c:when test="${not empty param.search_EQ_userType ||not empty param.search_EQ_xjzt||not empty  param.search_EQ_xh||not empty  param.search_EQ_sfzh||not empty  param.search_LIKE_scCo}">
									<div class="form-search-more" >
							</c:when>
							<c:otherwise>
									<div class="form-search-more" style="display: none;">
							</c:otherwise>
						</c:choose>
						<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">学员类型</label>
								<div class="col-sm-9">
									<dic:selectBox typeCode="USER_TYPE" name="search_EQ_userType" code="${param.search_EQ_userType}" otherAttrs='class="selectpicker show-tick form-control" data-size="5" data-live-search="true"' />
								</div>
							</div>
							<div class="col-md-4">
								<label class="control-label col-sm-3">学籍状态</label>
								<div class="col-sm-9">
									<select  name="search_EQ_xjzt"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
										<option value="" selected="selected">请选择</option>
										<c:forEach items="${rollTypeMap}" var="map">
											<option value="${map.key}" <c:if test="${map.key==param.search_EQ_xjzt}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="col-md-4">
								<label class="control-label col-sm-3">学号</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_EQ_xh" value="${param.search_EQ_xh }">
								</div>
							</div>
						</div>
						<div class="row reset-form-horizontal clearbox">
							<div class="col-md-4">
								<label class="control-label col-sm-3">身份证号码</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_EQ_sfzh" value="${param.search_EQ_sfzh }">
								</div>
							</div>
							<div class="col-md-4">
								<label class="control-label col-sm-3">单位名称</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" name="search_LIKE_scCo" value="${param.search_LIKE_scCo }">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="box-footer">
 					<div class="search-more-in">
						高级搜索<i class="fa fa-fw fa-caret-down"></i>
					</div> 
					<div class="btn-wrap">
						<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
					</div>
					<div class="btn-wrap">
						<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
					</div>
				</div>
			</div>
	

	<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
	<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		

	<div class="box">
		<div class="box-header with-border">
			<div class="fr">
	<!-- 			<div class="btn-wrap fl">
					<button class="btn btn-block btn-success btn-outport">
						<i class="fa fa-fw fa-sign-out"></i> 批量导出
					</button>
				</div>
				<div class="btn-wrap fl">
					<button class="btn btn-block btn-success btn-import">
						<i class="fa fa-fw fa-sign-in"></i> 批量导入
					</button>
				</div> -->
		<!-- 		<div class="btn-wrap fl">
					<button class="btn btn-block btn-success btn-audit">
						<i class="fa fa-fw fa-calendar-check-o"></i> 审核
					</button>
				</div> -->
		<!-- 		<div class="btn-wrap fl">
					<button class="btn btn-default btn-sm">
						<i class="fa fa-fw fa-plus"></i> 新增
					</button>
				</div> -->
				<div class="btn-wrap fl">
					<a href="javascript:void(0);" class="btn btn-block btn-danger btn-del del-checked">
						<i class="fa fa-fw fa-trash-o"></i> 删除
					</a>
				</div>
			</div>
		</div>
		<div class="box-body">
			<div id="dtable_wrapper"
				class="dataTables_wrapper form-inline dt-bootstrap no-footer">
				<div class="row">
					<div class="col-sm-6"></div>
					<div class="col-sm-6"></div>
				</div>

				<div class="row">
					<div class="col-sm-12">
						<table id="dtable"
							class="table table-bordered table-striped table-container">
							<thead>
								<tr>
									<th><input type="checkbox" class="select-all"
										id="selectAll"></th>
									<th>个人信息</th>
									<th>报读信息</th>
									<th>学籍状态</th>
									<th>学员类型</th>
									<th>身份证号</th>
									<th>联系方式</th>
									<th>机构信息</th>
									<th>创建日期</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
						<c:choose>
							<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content }" var="student">
									<tr>
										<td><input type="checkbox" value="${student.studentId }"
											name="ids" class="checkbox"></td>
										<td>
												学号：${student.xh}<br/>
												性别：${student.xbm==1?'男':'女'}<br/>
												姓名：${student.xm}
										</td>
										<td>
												学期：${student.gjtGrade.gradeName}<br/>
												层次：${pyccMap[student.pycc]} <br/>
												专业：${student.gjtSpecialty.zymc}
										</td>
										<td>
											${rollTypeMap[student.xjzt]}
										</td>
										<td>
											<dic:getLabel typeCode="USER_TYPE" code="${student.userType}" />
										</td>
										<td>
											<shiro:hasPermission name="/personal/index$privacyJurisdiction">
											${student.sfzh}
											</shiro:hasPermission>
										</td>
										<td>
											邮箱：${student.dzxx}<br/>
											手机号：
											<shiro:hasPermission name="/personal/index$privacyJurisdiction">
											${student.sjh}
											</shiro:hasPermission>
											<br/>
										 	单位：<c:if test="${fn:length(student.scCoAddr)>16 }">
															<a style="color: black " title="${student.scCoAddr}">${fn:substring(student.scCoAddr,0,16)}...</a>
														</c:if>    
														<c:if test="${fn:length(student.scCoAddr)<17 }">
															${student.scCoAddr }
														</c:if>
										</td>
										<td>
											学校：${student.gjtSchoolInfo.xxmc}<br/>
											学习中心：${student.gjtStudyCenter.scName}
										</td>
										<td><fmt:formatDate value="${student.createdDt}"
												pattern="yyyy-MM-dd HH:mm" /></td>
										<td>
											<div class="data-operion">
												<a href="queryById/${student.studentId}"	class="operion-item operion-edit" title="查看编辑">
													<i class="fa fa-fw fa-edit"></i></a> 
												<%-- <a href="queryById/${student.studentId}" 	class="operion-item operion-view" title="查看">
													<i class="fa fa-fw fa-eye"></i></a>  --%>
												<a href="javascript:;"	class="operion-item operion-del del-one" val="${student.studentId}"
													title="删除" data-tempTitle="删除">
													<i class="fa fa-fw fa-trash-o"></i></a>
													
												<div class="dropdown yahei">
													<a class="dropdown-toggle" data-toggle="dropdown" href="#">
														更多 <span class="caret"></span>
													</a>
													<ul class="dropdown-menu">
														<li role="presentation">
															<a href="javascript:;" 	class="operion-item operion-edit updatePwd" val="${student.gjtUserAccount.id}"
																title="密码重置" >
															<i class="fa fa-fw fa-edit"></i>密码重置</a>
														</li>
														<li role="presentation">
															<a href="simulation.html?id=${student.studentId}" 	class="operion-item operion-edit " 
																target="_blank"	title="模拟登陆" ><i class="fa fa-sign-in"></i>模拟登陆</a>
														</li>
														<%-- <li>
															<a href="print.html?id=${student.studentId}" 	class="operion-item operion-print " 
																target="_blank"	title="信息打印" ><i class="fa fa-print"></i>信息打印</a>
														</li> --%>
													</ul>
												</div>
											</div>
										</td>
									</tr>
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
				</div>
				 <tags:pagination page="${pageInfo}" paginationSize="5" /> 
			</div>
		</div>
	</div>
	</section>
	</form>
</body>
</html>