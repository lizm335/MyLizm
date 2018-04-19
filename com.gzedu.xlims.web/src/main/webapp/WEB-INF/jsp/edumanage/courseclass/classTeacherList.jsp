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
		<li><a href="#">教学管理</a></li>
		<li><a href="#">课程班级</a></li>
		<li class="active">${classInfo.bjmc}</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs nav-tabs-lg">
			<li><a href="${ctx}/edumanage/courseclass/classMemberManageList/1/${classId }">班级学员</a></li>
			<li <c:if test="${type=='2'}"> class="active"</c:if>><a href="${ctx}/edumanage/courseclass/classMemberManageList/2/${classId }">辅导老师</a></li>
			<li <c:if test="${type=='4'}"> class="active"</c:if>><a href="${ctx}/edumanage/courseclass/classMemberManageList/3/${classId }">督导老师</a></li>
		</ul>		
		<div class="tab-pane" id="tab_top_2">
		<c:if test="${type=='2'}">
		<form class="form-horizontal" action="${ctx}/edumanage/courseclass/classMemberManageList/2/${classId }">
		</c:if>
		<c:if test="${type=='4'}">
		<form class="form-horizontal" action="${ctx}/edumanage/courseclass/classMemberManageList/4/${classId }">
		</c:if>
			<%-- <div class="box box-border">
			    <div class="box-body">
			        <div class="row pad-t15">
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">账号</label>
			              <div class="col-sm-9">
			                <input name="search_LIKE_gjtEmployeeInfo.zgh" value="${param['search_LIKE_gjtEmployeeInfo.zgh']}" type="text" class="form-control">
			              </div>
			            </div>
			          </div>

			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">姓名</label>
			              <div class="col-sm-9">
			                <input name="search_LIKE_gjtEmployeeInfo.xm" value="${param['search_LIKE_gjtEmployeeInfo.xm']}" type="text" class="form-control">
			              </div>
			            </div>
			          </div>

			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">课程</label>
			              <div class="col-sm-9">
			                <input name="search_LIKE_bjmc" value="${param['search_LIKE_bjmc']}" type="text" class="form-control">
			              </div>
			            </div>
			          </div>

			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">班级</label>
			              <div class="col-sm-9">
			                <input name="search_LIKE_bjmc" value="${param['search_LIKE_bjmc']}" type="text" class="form-control">
			              </div>
			            </div>
			          </div>
			        </div>
			    </div><!-- /.box-body -->
			    <div class="box-footer text-right">
		          <button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
		          <button type="button" class="btn min-width-90px btn-default">重置</button>
		        </div><!-- /.box-footer-->
			</div> --%>
			
			<div class="box box-border margin-bottom-none">
				<div class="box-header with-border">
					<%-- <h3 class="box-title pad-t5">
					<c:if test="${type=='2'}">辅导老师列表</c:if>
					<c:if test="${type=='4'}">督导老师列表</c:if>
					</h3> --%>
					<div class="pull-right">						
						<c:if test="${type=='2'}">
							<shiro:hasPermission name="/edumanage/courseclass/list$allocationFD">
								<a href="${ctx}/edumanage/courseclass/teacherChoiceTree/2/${classId }" role="button" class="btn btn-default btn-sm" data-role="configure-teacher">分配辅导老师
								</a>
							</shiro:hasPermission>
						</c:if>
						<c:if test="${type=='4'}">
							<shiro:hasPermission name="/edumanage/courseclass/list$allocationDD">
								<a href="${ctx}/edumanage/courseclass/teacherChoiceTree/4/${classId }" role="button" class="btn btn-default btn-sm" data-role="configure-teacher">分配督导老师
								</a>
							</shiro:hasPermission>
						</c:if>
						
					</div>
				</div>
				<div class="box-body">
					<div class="table-responsive">
						<table class="table table-bordered table-striped vertical-mid text-center table-font">
							<thead>
				              <tr>
				              	<th>照片</th>
				              	<th>账号</th>
				              	<th>姓名</th>
				                <th>课程</th>
				                <th>班级</th>
				                <th>登录次数</th>
				                <th>登录时长<br>（小时）</th>
				                <th>操作</th>
				              </tr>
				            </thead>
				            <tbody>
				            <c:choose>
								<c:when test="${not empty teacher}">
				            	<tr>
				            		<td>
				            			<c:choose>
				            				<c:when test="${not empty teacher.zp }">
				            					<img src="${teacher.zp}" class="img-circle" width="50" height="50" />
				            				</c:when>
				            				<c:otherwise>
				            					<img src="${ctx}/static/dist/img/images/no-img.png" class="img-circle" width="50" height="50" />
				            					<p class="gray9">未上传</p>
				            				</c:otherwise>
				            			</c:choose>
				            		</td>
				            		<td>
				            			<c:choose>
				            				<c:when test="${not empty teacher.gjtUserAccount}">
				            					${teacher.gjtUserAccount.loginAccount}
				            				</c:when>
				            				<c:otherwise>--</c:otherwise>
				            			</c:choose>
				            		</td>
				            		<td>
				            			${teacher.xm}
				            		</td>
				            		<td>
				            			
				            			${classInfo.gjtCourse.kcmc }<br>
				            			<span class="gray9">（${classInfo.gjtCourse.kch}）</span>	
				            		</td>
				            		<td>
				            			
				            			${classInfo.bjmc }<br>		            			
				            		</td>
				            		<td>
				            			0
				            		</td>
				            		<td>
				            			0
				            		</td>
				            		<td>
				            			<a href="${ctx}/edumanage/courseclass/simulation?id=${teacher.employeeId}&termcourseId=${termcourseId}&classId=${classId}" 
				            			target="_blank" class="operion-item" data-toggle="tooltip" title="模拟登录"><i class="fa fa-fw fa-simulated-login"></i></a>
				            		</td>
				            	</tr>   
							</c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="8">暂无数据</td>
								</tr>
							</c:otherwise>
							</c:choose>   	
				            </tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
//批量调班
$("[data-role='batch']").click(function(event) {
	event.preventDefault();
	$.mydialog({
	  id:'batch',
	  width:500,
	  height:326,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')
	});
});

// 单击选中所有行
$(".table").on("click",".select-all",function(){
  var $tbl=$(this).closest('.table');
  if($(this).is(":checked")){
    $tbl.find('tr').addClass("selected");
    $tbl.find(':checkbox').prop("checked",true);
  }
  else{
    $tbl.find('tr').removeClass("selected");
    $tbl.find(':checkbox').prop("checked",false);
  }
})
.on('click', 'tbody tr', function (event) { 
  var $tbl=$(this).closest(".table");
  var ck=$(this).find(":checkbox");
  $(this).toggleClass('selected');
  if(!$(event.target).is(":checkbox")){
    if(!ck.is(":checked")){
      ck.prop("checked",true);
    }
    else{
      ck.prop("checked",false); 
    }
  }
  if(!ck.is(":checked")){
    $tbl.find(".select-all").prop("checked",false);
  }
});

//配置老师
$('[data-role="configure-teacher"]').click(function(event) {
	event.preventDefault();
  	$.mydialog({
	  id:'configure-teacher',
	  width:760,
	  height:500,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')
	});
});
</script>
</body>
</html>
