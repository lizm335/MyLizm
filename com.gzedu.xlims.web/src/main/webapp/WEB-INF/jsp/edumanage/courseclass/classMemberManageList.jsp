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
			<li class="active"><a href="${ctx}/edumanage/courseclass/classMemberManageList/1/${classId }">班级学员</a></li>
			<li><a href="${ctx}/edumanage/courseclass/classMemberManageList/2/${classId }" >辅导老师</a></li>
			<li><a href="${ctx}/edumanage/courseclass/classMemberManageList/3/${classId }">督导老师</a></li>
		</ul>
		<form id="listForm" class="form-horizontal" action="${ctx}/edumanage/courseclass/classMemberManageList/1/${classId }">
		<div class="tab-pane active" id="tab_top_1">		
			<div class="box box-border">
			    <div class="box-body">
			        <div class="row pad-t15">
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">学号</label>
			              <div class="col-sm-9">
			                <input name="search_LIKE_gjtStudentInfo.xh" value="${param['search_LIKE_gjtStudentInfo.xh']}" type="text" class="form-control">
			              </div>
			            </div>
			          </div>

			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">姓名</label>
			              <div class="col-sm-9">
			                <input name="search_LIKE_gjtStudentInfo.xm" value="${param['search_LIKE_gjtStudentInfo.xm']}" type="text" class="form-control">
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
						<shiro:hasPermission name="/edumanage/courseclass/getClassList$batchChangeClass">
						<%-- <a href="${ctx }/edumanage/classstudent/moveClass/${classId}" role="button" class="btn btn-default btn-sm" data-role="batch">批量调班</a> --%>
						<a href="${ctx}/edumanage/courseclass/changeClass?CLASS_ID=${classId }" role="button" class="btn btn-default btn-sm" data-role="batch">批量调班</a>
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
				                <th>课程</th>
				                <th>班级</th>
				                <th>学习成绩</th>
				                <td>学习进度</td>
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
				            			姓名：${item.studnetName }<br>
				            			学号：${item.studentCode }<br>
				            			学习中心：${item.studyCenter }<br>
				            			所在单位：${item.co }
				            		</td>
				            		<td>
				            			${item.courseName }<br>
				            			<span class="gray9">（${item.courseCode }）</span>
				            		</td>
				            		<td>
				            			${item.className}				            		
				            		</td>
				            		<td>
				            			<c:choose>
				            				<c:when test="${not empty item.examScore}">
				            					${item.examScore}
				            				</c:when>
				            				<c:otherwise>
				            					${item.score}
				            				</c:otherwise>
				            			</c:choose>
				            		</td>
				            		<td>
				            			<c:choose>
				            				<c:when test="${not empty item.progress}">
				            					${item.progress}%
				            				</c:when>
				            				<c:otherwise></c:otherwise>
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
				            			<%-- <a href="http://xlims.gzedu.com/jsp/intranet/module/studentinfo/simulate_login.jsp?studentId=${item.gjtStudentInfo.studentId}&account=${item.gjtStudentInfo.gjtUserAccount.id}" 
				            			target="_blank" class="operion-item" data-toggle="tooltip" title="模拟登录"><i class="fa fa-fw fa-simulated-login"></i></a> --%>
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
//批量调班
/* $("[data-role='batch']").click(function(event) {
	event.preventDefault();
	var $studentIds = $("input[name='studentIds']:enabled:checked");
	if ($studentIds.size() ==0) {
		$.alert({
	   	    title: '提示',
	   	    icon: 'fa fa-exclamation-circle',
	   	  	confirmButtonClass: 'btn-primary',
	   	    content: '请先选择学员！',
	   	    confirmButton: '确认'
     	});
		return false;
	}
	$.mydialog({
	  id:'batch',
	  width:800,
	  height:400,
	  zIndex:11000,
	  content: 'url:'+$(this).attr('href')+'?'+$studentIds.serialize()
	});
}); */

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
