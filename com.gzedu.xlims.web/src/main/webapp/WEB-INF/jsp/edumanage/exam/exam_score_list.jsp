<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>考试成绩</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
$(function() {
	
	
	$("[data-role='import']").click(function(event) {
		event.preventDefault();
		$.mydialog({
		  id:'import',
		  width:600,
		  height:550,
		  zIndex:11000,
		  content: 'url:'+$(this).attr('href')
		});
	});
	
	$('[data-role="export"]').click(function(event) {
		  $.mydialog({
		    id:'export',
		    width:600,
		    height:415,
		    zIndex:11000,
		    content: 'url:exportSource'
		  });
	});
	
	$("#btn-reset").click(function(){
		$("#search_LIKE_xm").val("");
		$("#search_LIKE_xh").val("");
		$("#search_LIKE_courseName").val("");
	});
	
});

function importConfirm() {
	if($('#file').val() == '') {
		alert('请先选择文件');
		return false;
	}
	$("#importForm").submit();
}



function tabClick2(id){
	$('#tabType').val(id);
	$('#listForm').submit();
}
</script>

</head>
<body class="inner-page-body">
<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">考试管理</a></li>
		<li class="active">考试成绩</li>
	</ol>
</section>
		
<section class="content">
	<form id="listForm" class="form-horizontal" action="list.html">
		<input type="hidden" id="tabType" name="search_EQ_scoreState" value="${param['search_EQ_scoreState']}">
		<!-- 搜索栏 start --> 
		<div class="box">
			<div class="box-body"> 
				<div class="row reset-form-horizontal clearbox">
					<div class="row pad-t15">
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">姓名</label>
			              <div class="col-sm-8">
			                <input class="form-control" type="text" name="search_LIKE_xm" id="search_LIKE_xm" value="${param['search_LIKE_xm']}">
			              </div>
			            </div>
			          </div>
		
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">学号</label>
			              <div class="col-sm-8">
			                <input class="form-control" type="text" name="search_LIKE_xh" id="search_LIKE_xh" value="${param['search_LIKE_xh']}">
			              </div>
			            </div>
			          </div>
		
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">成绩状态</label>
			              <div class="col-sm-8">
			                <select id="search_EQ_recordState" name="search_EQ_recordState" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="" selected="selected">请选择</option>
								<option value="0" >已通过</option>
								<option value="1" >不通过</option>
								<option value="2" >待登记</option>
								<option value="3" >登记中</option>
							</select>
			              </div>
			            </div>
			          </div> 
		
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">专业</label>
			              <div class="col-sm-8">
			                <select name="search_EQ_specialtyId" class="selectpicker show-tick form-control" 
								data-size="5" data-live-search="true" id="search_EQ_specialtyId">
								<option value="" selected="selected">请选择</option>
								<c:forEach items="${specialtyMap}" var="map">
									<option value="${map.key}"<c:if test="${map.key==param['search_EQ_specialtyId']}">selected='selected'</c:if> >${map.value}</option>
								</c:forEach>
							</select>
			              </div>
			            </div>
			          </div>
		
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">课程</label>
			              <div class="col-sm-8">
			                <input class="form-control" type="text" name="search_LIKE_courseName" id="search_LIKE_courseName" value="${param['search_LIKE_courseName']}">
			              </div>
			            </div>
			          </div>				          
		
			          <div class="col-sm-4">
			            <div class="form-group">
			              <label class="control-label col-sm-3 text-nowrap">考试形式</label>
			              <div class="col-sm-8">
			                <select id="examBatchCode2" name="search_EQ_examSate" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
								<option value="" selected="selected">请选择</option>
								<c:forEach items="${examTypeMap}" var="map">
									<option value="${map.key}"<c:if test="${map.key==param['search_EQ_examSate']}">selected='selected'</c:if> >${map.value}</option>
								</c:forEach>
							</select>
			              </div>
			            </div>
			          </div>
			        </div>
			        <div id="more-search-0" class="row collapse">
		              <div class="col-sm-4 col-xs-6">
		                <div class="form-group">
		                  <label class="control-label col-sm-3 text-nowrap">身份证</label>
		                  <div class="col-sm-8">
		                    <input type="text" class="form-control" name="search_LIKE_identityCard" id="search_LIKE_identityCard">
		                  </div>
		                </div>
		              </div>
		
		              <div class="col-sm-4 col-xs-6">
		                <div class="form-group">
		                  <label class="control-label col-sm-3 text-nowrap">年级</label>
		                  <div class="col-sm-8">
		                    <select  name="search_EQ_examGrade" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" id="search_EQ_examGrade">
		                    	<option value="" selected="selected">请选择</option>
		                    	<c:forEach items="${gradeMap}" var="map">
		                    		<option value="${map.key}"<c:if test="${map.key==param['search_EQ_examGrade']}">selected='selected'</c:if> >${map.value}</option>
		                    	</c:forEach>
		                      <option></option>
		                    </select>
		                  </div>
		                </div>
		              </div>
	            </div>
				</div>
			</div>
			
			<div class="box-footer text-right">
	          <button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
	          <button type="button" class="btn min-width-90px btn-default btn-reset" id="btn-reset">重置</button>
	          <div class="search-more-in no-float inline-block" data-toggle="collapse" data-target="#more-search-0">高级搜索<i class="fa fa-fw fa-angle-up"></i> </div>
	        </div>
		</div>
		<!-- 搜索栏 end --> 
		 
		<div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		<div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
		
		
		<div class="box box-border margin-bottom-none">
			<!-- 按钮栏 start -->
			<div class="box-header with-border">
				<h3 class="box-title pad-t5">考试成绩列表</h3>
				<div class="pull-right">
				<!-- 
					<a href="importUnifySource" role="button" class="btn btn-default btn-sm margin_r10" data-role="import"><i class="fa fa-fw fa-sign-in"></i> 导入免修\统考成绩</a>
					<a href="importRegisterSource" role="button" class="btn btn-default btn-sm margin_r10" data-role="import"><i class="fa fa-fw fa-sign-in"></i> 导入登记成绩</a>
					<button class="btn btn-default btn-sm" data-role="export" type="button" ><i class="fa fa-fw fa-sign-out"></i> 导出成绩</button>
				 -->
				</div>
			</div> 
			<!-- 按钮栏 end -->	


			<!-- 列表内容 start -->
			<div class="box-body">
				<div class="filter-tabs clearfix">
					<ul class="list-unstyled">
						<li class="actived" onclick="tabClick2(3)">全部(0)</li>
						<li <c:if test="${param['search_EQ_scoreState']==0}">class="actived"</c:if> onclick="tabClick2(0)">
						已通过(0)</li>
						<li <c:if test="${param['search_EQ_scoreState']==1}">class="actived"</c:if> onclick="tabClick2(1)">
						未通过(0)</li>
						<li onclick="tabClick2(3)">待登记(0)</li>
						<li onclick="tabClick2(4)">登记中(0)</li>
					</ul> 
				</div>
				<div class="table-responsive">
					<table id="list_table" class="table table-bordered table-striped vertical-mid text-center table-font">
						<thead>
							<tr> 
								<th>个人信息</th>
								<th>报读信息</th>
								<th>学期</th>
								<th>课程</th>
								<th>考试方式</th>
								<th>成绩比例</th>
								<th>学习成绩</th>
								<th>考试成绩	</th> 
								<th>总成绩</th>
								<th>成绩状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${pageInfo.content}" var="entity">
									<tr>
										<td>
											<div class="text-left">
											姓名:  ${entity.XM}<br>
											学号:  ${entity.XH}<br>
											<shiro:hasPermission name="/personal/index$privacyJurisdiction">
												手机号: ${entity.SJH}
											</shiro:hasPermission>
											</div>
										</td>
										<td>
											<div class="text-left">
													层次: ${pyccMap[entity.PYCC]} <br/> 
													年级: ${entity.GRADE_NAME} <br/>
													专业: ${entity.ZYMC} <br/>
											</div>
										</td>
										<td>
											第${entity.TERM_TYPE_CODE }学期
										</td>
										<td>
											${entity.KCMC }
										</td>
										<td>
											${examTypeMap[entity.EXAM_TYPE] }  
										</td>
										<td>
												学习成绩：${entity.STUDY_RATIO }%<br/>
												考试成绩：${entity.EXAM_RATIO }%
										</td>
										<td>
											${entity.COURSE_SCHEDULE}
										</td>
										<td>
											${entity.EXAM_SCORE}
										</td>														
										<td>
											${entity.TOTAL_POINTS}
										</td>
										<c:if test="${entity.SCORE_STATE=='0' or entity.TOTAL_POINTS>=60 }"><td class="text-green">通过</td></c:if>
											<c:if test="${entity.SCORE_STATE=='1' or entity.TOTAL_POINTS<60 }"><td class="text-red">未通过</td></c:if>
										<td>
												<a href="queryById.html?id=${entity.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="" data-original-title="查看详情">
													<i class="fa fa-fw fa-view-more"></i></a>
										</td>
									</tr>
							</c:forEach>
						</tbody>
					</table>
					<tags:pagination page="${pageInfo}" paginationSize="11" />
				</div>
			</div>
					
		</div>
	</form>
</section>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>
