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
		<li class="active">答辩安排</li>
	</ol>
</section>
<section class="content">
	<form id="listForm" class="form-horizontal">
	<input type="hidden" name="search_EQ_status" value="">
	<input type="hidden" name="search_GTE_status" value="">
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
	              <label class="control-label col-sm-3 text-nowrap">指导老师</label>
	              <div class="col-sm-9">
	                <select name="search_EQ_guideTeacher" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
						<option value="" selected='selected'>请选择</option>
						<c:forEach items="${guideTeachers}" var="map">
							<option value="${map.employeeId}" <c:if test="${map.employeeId==param['search_EQ_guideTeacher']}">selected='selected'</c:if>>${map.xm}</option>
						</c:forEach>
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
		  <h3 class="box-title pad-t5">答辩安排列表</h3>
		  <div class="pull-right no-margin">
		  	<c:if test="${isBtnImport}">
		    	<a href="javascript:void(0)" class="btn btn-default btn-sm btn-import"><i class="fa fa-fw fa-sign-in"></i> 批量导入答辩安排</a>
		    </c:if>
		  	<c:if test="${isBtnImportScore}">
		    	<a href="javascript:void(0)" class="btn btn-default btn-sm btn-importScore"><i class="fa fa-fw fa-sign-in"></i> 批量导入答辩成绩</a>
		    </c:if>
		  	<c:if test="${isBtnExport}">
		    	<a href="#" class="btn btn-default btn-sm btn-export" data-toggle="modal" data-target="#exportModal"><i class="fa fa-fw fa-sign-out"></i> 批量导出答辩安排</a>
		    </c:if>
		  </div>
		</div>
		<div class="box-body">
			<div class="filter-tabs filter-tabs2 clearfix">
				<ul class="list-unstyled">
					<li lang=":input[name='search_EQ_status'],:input[name='search_GTE_status']" <c:if test="${empty param['search_EQ_status'] && empty param['search_GTE_status']}">class="actived"</c:if>>全部（${stayDefence + defence + hasDefence}）</li>
					<li value="9" role=":input[name='search_EQ_status']" lang=":input[name='search_GTE_status']" <c:if test="${param['search_EQ_status'] == 9 }">class="actived"</c:if>>待安排（${stayDefence}）</li>
					<li value="10" role=":input[name='search_EQ_status']" lang=":input[name='search_GTE_status']" <c:if test="${param['search_EQ_status'] == 10 }">class="actived"</c:if>>已安排（${defence}）</li>
					<li value="11" role=":input[name='search_GTE_status']" lang=":input[name='search_EQ_status']" <c:if test="${param['search_GTE_status'] == 11 }">class="actived"</c:if>>已结束（${hasDefence}）</li>
				</ul>
			</div>
			<div>
				<table class="table table-bordered vertical-mid text-center table-font">
					<thead class="with-bg-gray">
		              <tr>
		              	<th>论文计划</th>
		                <th>个人信息</th>
		                <th>报读信息</th>
		                <th>指导老师</th>
		                <th>初评成绩</th>
		                <th>答辩老师</th>
		                <th>答辩形式</th>
		                <th>答辩时间</th>
		                <th>答辩地点</th>
		                <th>答辩成绩</th>
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
						            			${entity.gjtThesisPlan.thesisPlanName}<br>
						            			<div class="gray9">（${entity.gjtThesisPlan.thesisPlanCode}）</div>
						            		</td>
						            		<td>
						            			<div class="text-left">
						            				姓名：${entity.gjtStudentInfo.xm} <c:if test="${entity.applyDegree == 1}"><i class="fa fa-graduate-1 text-orange f14"></i></c:if> <br>
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
						            				<c:when test="${not empty entity.guideTeacher1}">
						            					${entity.guideTeacher1.xm}
						            				</c:when>
						            				<c:otherwise>--</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${not empty entity.reviewScore}">
						            					${entity.reviewScore}
						            				</c:when>
						            				<c:otherwise>--</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${not empty entity.defenceTeacher1 || not empty entity.defenceTeacher2}">
						            					<c:forEach items="${fn:split(entity.defenceTeacher1, ',')}" var="id">
							            					<c:forEach items="${defenceTeachers}" var="defenceTeacher">
							            						<c:if test="${defenceTeacher.employeeId == id}">
							            							${defenceTeacher.xm}, 
							            						</c:if>
							            					</c:forEach>
						            					</c:forEach>
						            					<c:forEach items="${fn:split(entity.defenceTeacher2, ',')}" var="id">
							            					<c:forEach items="${defenceTeachers}" var="defenceTeacher">
							            						<c:if test="${defenceTeacher.employeeId == id}">
							            							${defenceTeacher.xm}, 
							            						</c:if>
							            					</c:forEach>
						            					</c:forEach>
						            				</c:when>
						            				<c:otherwise>--</c:otherwise>
						            			</c:choose>
						            		
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${entity.needDefence == 1}">
						            					<c:choose>
						            						<c:when test="${entity.defenceType == 1}">
						            							现场答辩
						            						</c:when>
						            						<c:when test="${entity.defenceType == 2}">
						            							远程答辩
						            						</c:when>
						            						<c:otherwise>--</c:otherwise>
						            					</c:choose>
						            				</c:when>
						            				<c:otherwise>无需答辩</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${not empty entity.gjtThesisDefencePlan}">
						            					${entity.gjtThesisDefencePlan.defenceTime}
						            				</c:when>
						            				<c:otherwise>--</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${not empty entity.gjtThesisDefencePlan}">
						            					${entity.gjtThesisDefencePlan.defenceAddress}
						            				</c:when>
						            				<c:otherwise>--</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${not empty entity.defenceScore}">
						            					${entity.defenceScore}
						            				</c:when>
						            				<c:otherwise>--</c:otherwise>
						            			</c:choose>
						            		</td>
						            		<td>
						            			<c:choose>
						            				<c:when test="${entity.status == 9}">
						            					<span class="text-orange">待安排</span>
						            				</c:when>
						            				<c:when test="${entity.status == 10}">
						            					<span class="text-green">已安排</span>
						            				</c:when>
						            				<c:when test="${entity.status > 10}">
						            					已结束
						            				</c:when>
						            			</c:choose>
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

<div id="exportModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog">
        <form id="exportForm" name="exportForm" action="${ctx}/thesisDefencePlan/export" method="get" target="temp_target">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                    <h4 class="modal-title">批量导出答辩安排</h4>
                </div>
                <div class="modal-body">
                	<div>
                		<p>
                		请选择需要导出答辩安排的论文计划
	                    </p>    
						<div class="col-sm-9">
							<select id="m_thesisPlanId" name="thesisPlanId" class="selectpicker show-tick form-control"
								data-size="5" data-live-search="true">
								<c:forEach items="${thesisPlanMap}" var="map">
									<option value="${map.key}">${map.value}</option>
								</c:forEach>
							</select>
						</div>
						
					</div>
                </div>
                <br>
                <br>
                <br>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default pull-left" data-dismiss="modal">取消</button>
                    <button type="submit" class="btn btn-primary" id="m_export">导出</button>
                </div>
            </div><!-- /.modal-content -->
        </form>
    </div><!-- /.modal-dialog -->
</div>
		
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
 
<script type="text/javascript">

$(function() {
	$(".btn-import").click(function(event) {
		$.mydialog({
		  id:'import',
		  width:600,
		  height:415,
		  zIndex:11000,
		  content: 'url:importForm'
		});
	});

	$(".btn-importScore").click(function(event) {
		$.mydialog({
		  id:'import',
		  width:600,
		  height:415,
		  zIndex:11000,
		  content: 'url:importScoreForm'
		});
	});
});

</script>
</body>
</html>
