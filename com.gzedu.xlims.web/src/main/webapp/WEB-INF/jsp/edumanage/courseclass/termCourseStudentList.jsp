<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>课程班级管理-班级人员管理</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">课程管理 </a></li>
		<li class="active">查看选课学员</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom no-margin">
		<form id="listForm" class="form-horizontal" action="${ctx}/edumanage/courseclass/termCourseStudentList">
			<input type="hidden" name="termCourseId" value="${param.termCourseId}"> 
		<div class="tab-pane active" id="tab_top_1">		
			<div class="box box-border">
			    <div class="box-body">
			        <div class="row pad-t15">
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">学号</label>
			              <div class="col-sm-9">
			                <input name="search_LIKE_xh" value="${param['search_LIKE_xh']}" type="text" class="form-control">
			              </div>
			            </div>
			          </div>

					<div class="col-sm-4">
					  <div class="form-group">
					    <label class="control-label col-sm-3 text-nowrap">姓名</label>
					    <div class="col-sm-9">
					      <input name="search_LIKE_xm" value="${param['search_LIKE_xm']}" type="text" class="form-control">
					    </div>
					  </div>
					</div>
			          
					<div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">专业层次</label>
			              <div class="col-sm-9">
			                <select name="search_EQ_pycc" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
								<option value="">请选择</option>
								<c:forEach items="${pyccMap}" var="map">
									<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_pycc}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
			              </div>
			            </div>
			        </div>
			          
			        <div class="col-sm-4">
					  <div class="form-group">
					    <label class="control-label col-sm-3 text-nowrap">专业</label>
					    <div class="col-sm-9">
					      <input name="search_LIKE_zymc" value="${param['search_LIKE_zymc']}" type="text" class="form-control">
					    </div>
					  </div>
					</div>
					
					<div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">学期</label>
			              <div class="col-sm-9">
			                <select name="search_EQ_gradeId" class="selectpicker show-tick form-control" 	 data-size="5" data-live-search="true">
								<option value="">请选择</option>
								<c:forEach items="${gradeMap}" var="map">
									<option value="${map.key}"  <c:if test="${map.key==param.search_EQ_gradeId}">selected='selected'</c:if>>${map.value}</option>
								</c:forEach>
							</select>
			              </div>
			            </div>
			        </div>

			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">学习状态</label>
			              <div class="col-sm-9">
			                <select name="search_EQ_status" class="selectpicker show-tick form-control" 
								data-size="5" data-live-search="true">
								<option value="" selected="selected">全部</option>
			                  	<option value="1" <c:if test="${1==param['search_EQ_status']}">selected='selected'</c:if>>已通过</option>
			                  	<option value="2" <c:if test="${2==param['search_EQ_status']}">selected='selected'</c:if>>未通过</option>
			                </select>
			              </div>
			            </div>
			          </div>

			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">学习中心</label>
			              <div class="col-sm-9">
			                <input name="search_LIKE_xxzx" value="${param['search_LIKE_xxzx']}" type="text" class="form-control">
			              </div>
			            </div>
			          </div>

			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">所在单位</label>
			              <div class="col-sm-9">
			                <input name="search_LIKE_scco" value="${param['search_LIKE_scco']}" type="text" class="form-control">
			              </div>
			            </div>
			          </div>
			        </div>
			    </div><!-- /.box-body -->
			    <div class="box-footer text-right">
		          <button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
		          <button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
		        </div><!-- /.box-footer-->
			</div>
			<div class="box box-border margin-bottom-none">
				<div class="box-header with-border">
					<h3 class="box-title pad-t5">学员列表</h3>
					<div class="pull-right">
						<shiro:hasPermission name="/edumanage/courseclass/list$batchChangeClass">
						<a href="${ctx }/edumanage/classstudent/moveClass/${classId}" role="button" class="btn btn-default btn-sm" data-role="batch">批量调班</a>
						</shiro:hasPermission>
					</div>
				</div>
				<div class="box-body">
					<div class="table-responsive">
						<table class="table table-bordered table-striped vertical-mid text-center table-font">
							<thead>
				              <tr>
				              	<th><input type="checkbox" class="select-all no-margin"></th>
				              	<th>照片</th>
				              	<th>个人信息</th>
				                <th>报读信息</th>
				                <th>课程</th>
				                <th>学习成绩</th>
				                <th>学习进度</th>
				                <th>登录次数</th>
				                <th>登录时长<br>（分钟）</th>
				                <th>学习状态</th>
				                <th>操作</th>
				              </tr>
				            </thead>
				            <tbody>
				            <c:choose>
								<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.getContent() }" var="item">
				            	<tr>
				            		<td><input type="checkbox" name="studentIds" value="${item.studentId}"></td>
				            		<td>
				            			<c:choose>
				            				<c:when test="${not empty item.zp }">
				            					<img src="${item.zp}" class="img-circle" width="50" height="50" />
				            				</c:when>
				            				<c:otherwise>
				            					<img src="${ctx}/static/dist/img/images/no-img.png" class="img-circle" width="50" height="50" />
				            					<p class="gray9">未上传</p>
				            				</c:otherwise>
				            			</c:choose>
				            		</td>
				            		<td>
					            		<ul class="list-unstyled text-left">
				            				<li>姓名：${item.xm }</li>
				            				<li>学号：${item.xh }</li>
				            				<li>学习中心：${item.scName }</li>
				            				<li>所在单位：${item.scco }</li>
				            			</ul>
				            		</td>
				            		<td>
					            		<ul class="list-unstyled text-left">
				            				<li>层次：${pyccMap[item.pycc]}</li>
				            				<li>年级：${item.yearName }</li>
				            				<li>学期：${item.gradeName }</li>
				            				<li>专业：${item.zymc }</li>
				            			</ul>
				            		</td>
				            		<td>
				            			${item.courseName }<br>
				            			<span class="gray9">（${item.courseCode }）</span>
				            		</td>
				            		<td>
				            			<c:choose>
				            				<c:when test="${not empty item.examScore}">
				            					${item.examScore}
				            				</c:when>
				            				<c:when test="${not empty item.score}">
				            					${item.score}
				            				</c:when>
				            				<c:otherwise>
				            					0
				            				</c:otherwise>
				            			</c:choose>
				            		</td>
				            		<td>
				            			<c:choose>
				            				<c:when test="${not empty item.progress}">
				            					${item.progress}%
				            				</c:when>
				            				<c:otherwise>0%</c:otherwise>
				            			</c:choose>
				            		</td>
				            		<td>
				            			<c:choose>
				            				<c:when test="${not empty item.loginTimes}">
				            					${item.loginTimes}
				            				</c:when>
				            				<c:otherwise>0</c:otherwise>
				            			</c:choose>
				            		</td>
				            		<td>
				            			<c:choose>
				            				<c:when test="${not empty item.loginLength}">
				            					${item.loginLength}
				            				</c:when>
				            				<c:otherwise>0</c:otherwise>
				            			</c:choose>
				            		</td>
			            			<c:choose>
			            				<c:when test="${item.status==1}">
			            					<td class="text-green">已通过</td>
			            				</c:when>
			            				<c:otherwise>
	            							<td class="text-red">未通过</td>
	            						</c:otherwise>
			            			</c:choose>
				            		<td>
				            			<a href="${ctx}/usermanage/student/simulation.html?id=${item.studentId}" 
				            			target="_blank" class="operion-item" data-toggle="tooltip" title="模拟登录"><i class="fa fa-fw fa-simulated-login"></i></a>
				            		</td>
				            	</tr>					            	
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
						<tags:pagination page="${pageInfo}" paginationSize="5" /> 
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

</script>
</body>
</html>
