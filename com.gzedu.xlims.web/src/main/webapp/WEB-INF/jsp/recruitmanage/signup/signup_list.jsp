<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>>报读信息</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
 <script src="${ctx}/static/plugins/echarts/echarts.min.js"></script>
 <script type="text/javascript" src="${ctx}/static/plugins/echarts/china.js"></script>
 <script type="text/javascript">
function choice(flag){
	$("#changetype").val(flag);
	$("#listForm").submit();
}
</script>
</head>
<body class="inner-page-body">
<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li>招生管理</li>
		<li class="active">报读信息</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/recruitmanage/signup/list" >报读信息</a></li>
			<li ><a href="${ctx}/recruitmanage/signup/statistics"  >报读统计</a></li>
		</ul>
		<div class="tab-content">
			<div class="tab-pane active" id="tab_top_1">
			<form class="form-horizontal" id="listForm" action="list.html">
			<input id="changetype" type="hidden" name="search_EQ_auditState" value="${param.search_EQ_auditState}">
				<div class="box box-border">
				    <div class="box-body">
				      <form class="form-horizontal">
				        <div class="row pad-t15">
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">学号</label>
				              <div class="col-sm-9">
				                <input type="text" class="form-control" name="search_LIKE_gjtStudentInfo.xh" value="${param.search_LIKE_gjtStudentInfo.xh}">
				              </div>
				            </div>
				          </div>
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">姓名</label>
				              <div class="col-sm-9">
				                <input type="text" class="form-control" name="search_LIKE_name" value="${param.search_LIKE_name}">
				              </div>
				            </div>
				          </div>
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">学习中心</label>
				              <div class="col-sm-9">
				                <select class="form-control select2" name="search_EQ_xxzxId" vlaue="${param.search_EQ_xxzxId}">
				                  <option value="">--请选择--</option>
				                   <c:forEach items="${schoolInfoMap}" var="map">
				                   	<option value="${map.key}"<c:if test="${map.key==param['search_EQ_xxzxId']}">selected='selected'</c:if> >${map.value}</option>
				                   </c:forEach>
				                </select>
				              </div>
				            </div>
				          </div>

				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">专业</label>
				              <div class="col-sm-9">
				                <select class="form-control" name="search_EQ_signupSpecialtyId" value="${param.search_EQ_signupSpecialtyId}">
				                  <option value="">--请选择--</option>
				                  <c:forEach items="${specialtyMap}" var="map">
									<option value="${map.key}"<c:if test="${map.key==param['search_EQ_signupSpecialtyId']}">selected='selected'</c:if> >${map.value}</option>
					  				</c:forEach>
				                </select>
				              </div>
				            </div>
				          </div>
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">年级</label>
				              <div class="col-sm-9">
				                <select class="form-control" name="search_EQ_gjtStudentInfo.gjtGrade.gradeId" value="${param.search_EQ_gjtStudentInfo.gjtGrade.gradeId}">
				                  <option value="">--请选择--</option>
				                 <c:forEach items="${gradeMap}" var="map">
				                  <option value="${map.key}"<c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtGrade.gradeId']}">selected='selected'</c:if> >${map.value}</option>
				                 </c:forEach>
				                </select>
				              </div>
				            </div>
				          </div>
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">层次</label>
				              <div class="col-sm-9 ">
				              	<select class="form-control" name="search_EQ_gjtStudentInfo.pycc" value="${param.search_EQ_gjtStudentInfo.pycc}">
				                  <option value="">--请选择--</option>
				                 <c:forEach items="${pyccMap}" var="map">
	                  				<option value="${map.key}"<c:if test="${map.key==param['search_EQ_gjtStudentInfo.pycc']}">selected='selected'</c:if> >${map.value}</option>
	                  			 </c:forEach>
				                </select>
				              </div>
				            </div>
				          </div>
				        </div>
				      
				    </div><!-- /.box-body -->
				    <div class="box-footer">
				      <div class="pull-right"><button type="button" class="btn min-width-90px btn-default">重置</button></div>
				      <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
				    </div><!-- /.box-footer-->
				</div>
				<div class="box">
					<div class="box-header with-border">
						<h3 class="box-title pad-t5">信息列表</h3>
						<div class="fr">
						    <a href="${ctx}/recruitmanage/signup/exportInfo?${searchParams}" target="_blank" class="btn btn-success btn-outport">
							<i class="fa fa-fw fa-sign-out"></i> 批量下载学生报读资料</a>
			            </div>	
				    </div>
				    <div class="filter-tabs clearfix">
					<ul class="list-unstyled">
						<!-- 
						<li class="active"><a href="#" data-toggle="tab">待提交(131)</a></li>
						<li><a href="#" data-toggle="tab">待审核(40)</a></li>
						<li><a href="#" data-toggle="tab">审核通过(40)</a></li>
						<li><a href="#" data-toggle="tab">审核不通过(40)</a></li>
						 -->
						<li <c:if test="${param.search_EQ_auditState == '4'}">class="active"</c:if>><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_auditState == '4'|| param.search_EQ_auditState == null|| param.search_EQ_auditState==''}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_auditState != '4'&& param.search_EQ_auditState != null}">aria-expanded="false"</c:if> onclick="choice('4')">待提交(${StaySubmitNum})</a></li>
		  				<li <c:if test="${param.search_EQ_auditState == '3'}">class="active"</c:if>><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_auditState == '3'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_auditState != '3'}">aria-expanded="false"</c:if> onclick="choice('3')">待审核(${StayCheckNum})</a></li>
		  				<li <c:if test="${param.search_EQ_auditState == '1'}">class="active"</c:if>><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_auditState == '1'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_auditState != '1'}">aria-expanded="false"</c:if> onclick="choice('1')">审核通过(${AuditStatePassNum})</a></li>
		  				<li <c:if test="${param.search_EQ_auditState == '0'}">class="active"</c:if>><a href="#" data-toggle="tab" <c:if test="${param.search_EQ_auditState == '0'}">aria-expanded="true"</c:if><c:if test="${param.search_EQ_auditState != '0'}">aria-expanded="false"</c:if> onclick="choice('0')">审核不通过(${AuditStateNoPassNum})</a></li>
					</ul>
					</div>
					<div class="box-body">
					<div id="dtable_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
						<div class="row">
						  <div class="col-sm-6"></div>
						  <div class="col-sm-6"></div>
					    </div>
						<!--<div class="table-responsive">  -->
						<div class="row">
						<div class="col-sm-12">
					        <table class="table table-bordered table-striped vertical-mid table-font">
					          <thead>
					            <tr>
					              <th class="text-center">个人信息</th>
					              <th class="text-center">报读信息</th>
					              <th class="text-center">报名时间</th>
					              <th class="text-center">学生来源</th>
					              <th class="text-center">资料审批</th>
					             <!--<th class="no-padding" data-role="menu-th">
					                <div class="dropdown custom-dropdown">
					                  <a href="javascript:;" class="dropdown-toggle text-nowrap text-center" role="button">
					                    	资料审批
					                    <span class="caret"></span>
					                  </a>
					                  <div class="dropdown-menu">
					                    <ul class="list-unstyled">
					                      <li>
					                        <i class="fa fa-fw"></i> 未提交（8）
					                        <input type="checkbox">
					                      </li>
					                      <li>
					                        <i class="fa fa-fw"></i> 待审核（21）
					                        <input type="checkbox">
					                      </li>
					                      <li>
					                        <i class="fa fa-fw"></i> 审核通过（1201）
					                        <input type="checkbox">
					                      </li>
					                      <li>
					                        <i class="fa fa-fw"></i> 审核不通过（15）
					                        <input type="checkbox">
					                      </li>
					                    </ul> 
					                    <div class="text-center margin_t5">
					                      <button class="btn btn-primary btn-xs" data-role="sure-btn">确定</button>
					                      <button class="btn btn-default btn-xs margin_l10" data-role="close-btn">关闭</button>
					                    </div>
					                  </div>
					                  
					                </div>
					              </th>-->
					              <th class="text-center">学习中心</th>
					              <th class="text-center">操作</th>
					            </tr>
					          </thead>
					          <tbody>
					          <c:forEach items="${infos.getContent()}" var="info">					         
					            <tr>
					              <td>
					               	 姓名：${info.name}（<dic:getLabel typeCode="Sex" code="${info.gjtStudentInfo.xbm}" />）<br>
					              	  学号：${info.gjtStudentInfo.xh}<br>
					              	 <shiro:hasPermission name="/personal/index$privacyJurisdiction">
						               	 手机：${info.gjtStudentInfo.sjh}<br>
						                     身份证：${info.gjtStudentInfo.sfzh}
					                </shiro:hasPermission>
					              </td>
					              <td>
					               	 层次：<c:forEach items="${pyccMap}" var="map">
		            					<c:if test="${info.gjtStudentInfo.pycc==map.key}">${map.value}</c:if>		            		
		            					</c:forEach><br>
					                                                 年级：${info.gjtStudentInfo.gjtGrade.gradeName}<br>
					                                                 专业：${info.gjtStudentInfo.gjtSpecialty.zymc}
					              </td>
					              <td class="text-center">
					                <fmt:formatDate value="${info.createdDt}" pattern="yyyy-MM-dd"/>
					              </td>
					              <td class="text-center">
					                ${info.auditSource }                             
					              </td>
					              <td class="text-center">
					                <span class="text-green">
					                 <c:if test="${info.auditState==0}">审核不通过</c:if>	
									 <c:if test="${info.auditState==1}">审核通过</c:if>	
									 <c:if test="${info.auditState==3}">待审核</c:if>	
									 <c:if test="${info.auditState==4}">未提交</c:if>	
					                </span>
					              </td>
					              <td class="text-center">
					               ${schoolInfoMap[info.xxzxId]}					               
					              </td>
					              <c:if test="${info.auditState==0}">
					              <td class="text-center">
					              	<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="审核学习资料"><i class="fa fa-fw fa-eye"></i></a>					              	
					              	<!--<a href="#" class="operion-item" data-toggle="tooltip" title="审核学习资料"><i class="fa fa-fw fa-shxxjl"></i></a> -->
					                <a href="${ctx}/recruitmanage/signup/view?signupId=${info.signupId}&studentId=${info.gjtStudentInfo.studentId}" class="operion-item operion-view" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
					              </td>
					              </c:if>
					              <c:if test="${info.auditState==1}">
					              <td class="text-center">
					              	<a href="#" class="operion-item" data-toggle="tooltip" title="审核学习资料"><i class="fa fa-fw fa-shxxjl"></i></a>
					              	<a href="#" class="operion-item" data-toggle="tooltip" title="打印报名登记表"><i class="fa fa-fw fa-print"></i></a>
					              	<a href="#" class="operion-item" data-toggle="tooltip" title="下载报读资料"><i class="fa fa-fw fa-download"></i></a>
					                <a href="${ctx}/recruitmanage/signup/view?signupId=${info.signupId}&studentId=${info.gjtStudentInfo.studentId}" class="operion-item operion-view" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
					              </td>
					              </c:if>
					              <c:if test="${info.auditState==3}">
					              <td class="text-center">
					              	<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="审核学习资料"><i class="fa fa-fw fa-eye"></i></a>					              	
					              	<!--<a href="#" class="operion-item" data-toggle="tooltip" title="审核学习资料"><i class="fa fa-fw fa-shxxjl"></i></a> -->
					                <a href="${ctx}/recruitmanage/signup/view?signupId=${info.signupId}&studentId=${info.gjtStudentInfo.studentId}" class="operion-item operion-view" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
					              </td>
					              </c:if>
					              <c:if test="${info.auditState==4}">
					              <td class="text-center">
					              	<!--<a href="#" class="operion-item" data-toggle="tooltip" title="审核学习资料"><i class="fa fa-fw fa-shxxjl"></i></a> -->
					                <a href="${ctx}/recruitmanage/signup/view?signupId=${info.signupId}&studentId=${info.gjtStudentInfo.studentId}" class="operion-item operion-view" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
					              </td>
					              </c:if>
					            </tr>
					            </c:forEach>
					          </tbody>
					        </table>					       
					      </div>
					    </div>
					    <tags:pagination page="${infos}" paginationSize="5" />
					</div>
				 </div>
				</div>
			 </form>
			</div>
			<div class="tab-pane" id="tab_top_2">
				<div class="row">
		          <div class="col-md-6">
		            <!-- 报读资料统计 -->
		            <div class="panel panel-default">
		              <div class="panel-heading no-pad-top">
		                <div class="row">
		                  <div class="col-sm-4">
		                    <h3 class="panel-title text-bold text-nowrap pad-t15">报读资料统计</h3>
		                  </div>
		                  <div class="col-sm-8 row-offset-3px">
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="search_EQ_gjtStudentInfo.gjtGrade.gradeId" value="${param.search_EQ_gjtStudentInfo.gjtGrade.gradeId}">
		                          <option value="">--全部年级--</option>
		                          <c:forEach items="${gradeMap}" var="map">
				                  <option value="${map.key}"<c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtGrade.gradeId']}">selected='selected'</c:if> >${map.value}</option>
				                  </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="search_EQ_gjtStudentInfo.pycc" value="${param.search_EQ_gjtStudentInfo.pycc}">
		                         <option value="">--全部层次--</option>
				                 <c:forEach items="${pyccMap}" var="map">
	                  				<option value="${map.key}"<c:if test="${map.key==param['search_EQ_gjtStudentInfo.pycc']}">selected='selected'</c:if> >${map.value}</option>
	                  			 </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs" name="search_EQ_signupSpecialtyId" value="${param.search_EQ_signupSpecialtyId}">
		                           <option value="">--全部专业--</option>
				                  <c:forEach items="${specialtyMap}" var="map">
									<option value="${map.key}"<c:if test="${map.key==param['search_EQ_signupSpecialtyId']}">selected='selected'</c:if> >${map.value}</option>
					  			  </c:forEach>		                          
		                        </select>
		                      </div>
		                    </div>
		                  </div>
		                </div>
		              </div>
		              <div class="panel-body">
		                <div id="ecBar4" data-role="chart" style="height:300px"></div>
		              </div>
		            </div>
		          </div>
		          <div class="col-md-6">
		            <!-- 区域统计 -->
		            <div class="panel panel-default">
		              <div class="panel-heading no-pad-top">
		                <div class="row">
		                  <div class="col-sm-4">
		                    <h3 class="panel-title text-bold text-nowrap pad-t15">报读缴费统计</h3>
		                  </div>
		                  <div class="col-sm-8 row-offset-3px">
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option value="">--全部年级--</option>
		                          <c:forEach items="${gradeMap}" var="map">
				                  <option value="${map.key}"<c:if test="${map.key==param['search_EQ_gjtStudentInfo.gjtGrade.gradeId']}">selected='selected'</c:if> >${map.value}</option>
				                  </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option value="">--全部层次--</option>
				                 <c:forEach items="${pyccMap}" var="map">
	                  				<option value="${map.key}"<c:if test="${map.key==param['search_EQ_gjtStudentInfo.pycc']}">selected='selected'</c:if> >${map.value}</option>
	                  			 </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                         <option value="">--全部专业--</option>
				                  <c:forEach items="${specialtyMap}" var="map">
									<option value="${map.key}"<c:if test="${map.key==param['search_EQ_signupSpecialtyId']}">selected='selected'</c:if> >${map.value}</option>
					  			  </c:forEach>
		                        </select>
		                      </div>
		                    </div>
		                  </div>
		                </div>
		              </div>
		              <div class="panel-body">
		                <div id="ecBar5" data-role="chart" style="height:300px"></div>
		              </div>
		            </div>
		            
		          </div>
		        </div>

		        <div class="row">
		          <div class="col-md-6">
		            <!-- 学习中心统计 -->
		            <div class="panel panel-default">
		              <div class="panel-heading no-pad-top">
		                <div class="row">
		                  <div class="col-sm-4">
		                    <h3 class="panel-title text-bold text-nowrap pad-t15">学习中心统计</h3>
		                  </div>
		                  <div class="col-sm-8 row-offset-3px">
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option>全部年级</option>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option>全部层次</option>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option>全部专业</option>
		                        </select>
		                      </div>
		                    </div>
		                  </div>
		                </div>
		              </div>
		              <div class="panel-body">
		                <div id="mapChart1" data-role="chart" style="height:300px"></div>
		              </div>
		            </div>
		          </div>
		          <div class="col-md-6">
		            <!-- 区域统计 -->
		            <div class="panel panel-default">
		              <div class="panel-heading no-pad-top">
		                <div class="row">
		                  <div class="col-sm-4">
		                    <h3 class="panel-title text-bold text-nowrap pad-t15">区域统计</h3>
		                  </div>
		                  <div class="col-sm-8 row-offset-3px">
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option>全部年级</option>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option>全部层次</option>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option>全部专业</option>
		                        </select>
		                      </div>
		                    </div>
		                  </div>
		                </div>
		              </div>
		              <div class="panel-body">
		                <div id="mapChart2" data-role="chart" style="height:300px"></div>
		              </div>
		            </div>
		            
		          </div>
		        </div>

		        <div class="row">
		          <div class="col-md-6">
		            <!-- 学历层次统计 -->
		            <div class="panel panel-default">
		              <div class="panel-heading no-pad-top">
		                <div class="row">
		                  <div class="col-sm-4">
		                    <h3 class="panel-title text-bold text-nowrap pad-t15">学历层次统计</h3>
		                  </div>
		                  <div class="col-sm-8 row-offset-3px">
		                    <div class="col-sm-4 col-sm-offset-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option>全部年级</option>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option>全部专业</option>
		                        </select>
		                      </div>
		                    </div>
		                  </div>
		                </div>
		              </div>
		              <div class="panel-body">
		                <div id="pieChart2" data-role="chart" style="height:270px"></div>
		              </div>
		            </div>
		          </div>
		          <div class="col-md-6">
		            <!-- 报读专业统计 -->
		            <div class="panel panel-default">
		              <div class="panel-heading no-pad-top">
		                <div class="row">
		                  <div class="col-sm-4">
		                    <h3 class="panel-title text-bold text-nowrap pad-t15">报读专业统计</h3>
		                  </div>
		                  <div class="col-sm-8 row-offset-3px">
		                    <div class="col-sm-4 col-sm-offset-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option>全部年级</option>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option>全部层次</option>
		                        </select>
		                      </div>
		                    </div>
		                  </div>
		                </div>
		              </div>
		              <div class="panel-body">
		                <div style="height:270px;overflow:auto;">
		                  <dl class="spe-list">
		                    <dt class="text-no-bold f12">工商管理（企业生产运营管理方向）</dt>
		                    <dd class="margin_t5">
		                      <div class="progress">
		                        <div class="progress-bar progress-bar-green" style="width: 20%">
		                          <span>200(20%)</span>
		                        </div>
		                      </div>
		                    </dd>

		                    <dt class="text-no-bold f12">学前教育（学前教师教育方向）</dt>
		                    <dd class="margin_t5">
		                      <div class="progress">
		                        <div class="progress-bar progress-bar-aqua" style="width: 10%">
		                          <span>100(10%)</span>
		                        </div>
		                      </div>
		                    </dd>

		                    <dt class="text-no-bold f12">学前教育（学前教师教育方向）</dt>
		                    <dd class="margin_t5">
		                      <div class="progress">
		                        <div class="progress-bar progress-bar-yellow" style="width: 40%">
		                          <span>400(40%)</span>
		                        </div>
		                      </div>
		                    </dd>

		                    <dt class="text-no-bold f12">学前教育（学前教师教育方向）</dt>
		                    <dd class="margin_t5">
		                      <div class="progress">
		                        <div class="progress-bar progress-bar-primary" style="width: 3%">
		                          <span>30(3%)</span>
		                        </div>
		                      </div>
		                    </dd>

		                    <dt class="text-no-bold f12">学前教育（学前教师教育方向）</dt>
		                    <dd class="margin_t5">
		                      <div class="progress">
		                        <div class="progress-bar progress-bar-red" style="width: 3%">
		                          <span>30(3%)</span>
		                        </div>
		                      </div>
		                    </dd>
		                    <dt class="text-no-bold f12">工商管理（企业生产运营管理方向）</dt>
		                    <dd class="margin_t5">
		                      <div class="progress">
		                        <div class="progress-bar progress-bar-green" style="width: 20%">
		                          <span>200(20%)</span>
		                        </div>
		                      </div>
		                    </dd>

		                    <dt class="text-no-bold f12">学前教育（学前教师教育方向）</dt>
		                    <dd class="margin_t5">
		                      <div class="progress">
		                        <div class="progress-bar progress-bar-aqua" style="width: 10%">
		                          <span>100(10%)</span>
		                        </div>
		                      </div>
		                    </dd>

		                    <dt class="text-no-bold f12">学前教育（学前教师教育方向）</dt>
		                    <dd class="margin_t5">
		                      <div class="progress">
		                        <div class="progress-bar progress-bar-yellow" style="width: 40%">
		                          <span>400(40%)</span>
		                        </div>
		                      </div>
		                    </dd>

		                    <dt class="text-no-bold f12">学前教育（学前教师教育方向）</dt>
		                    <dd class="margin_t5">
		                      <div class="progress">
		                        <div class="progress-bar progress-bar-primary" style="width: 3%">
		                          <span>30(3%)</span>
		                        </div>
		                      </div>
		                    </dd>

		                    <dt class="text-no-bold f12">学前教育（学前教师教育方向）</dt>
		                    <dd class="margin_t5">
		                      <div class="progress">
		                        <div class="progress-bar progress-bar-red" style="width: 3%">
		                          <span>30(3%)</span>
		                        </div>
		                      </div>
		                    </dd>
		                  </dl>
		                </div>
		              </div>
		            </div>
		          </div>
		        </div>

		        <div class="row">
		          <div class="col-md-6">
		            <!-- 用户属性统计 -->
		            <div class="panel panel-default">
		              <div class="panel-heading no-pad-top">
		                <div class="row">
		                  <div class="col-sm-4">
		                    <h3 class="panel-title text-bold text-nowrap pad-t15">用户属性统计</h3>
		                  </div>
		                  <div class="col-sm-8 row-offset-3px">
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option>全部年级</option>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option>全部层次</option>
		                        </select>
		                      </div>
		                    </div>
		                    <div class="col-sm-4">
		                      <div class="form-group margin_t10 margin-bottom-none">
		                        <select class="form-control input-xs">
		                          <option>全部专业</option>
		                        </select>
		                      </div>
		                    </div>
		                  </div>
		                </div>
		              </div>
		              <div class="panel-body">
		                <h3 class="cnt-box-title f16 margin_b10">性别</h3>
		                <div class="row">
		                  <div class="col-xs-8 per-icon-box margin_t10">
		                    <i class="fa fa-fw fa-female text-red"></i>
		                    <i class="fa fa-fw fa-female text-red"></i>
		                    <i class="fa fa-fw fa-female text-red"></i>
		                    <i class="fa fa-fw fa-female text-red"></i>
		                    <i class="fa fa-fw fa-female"></i>
		                    <i class="fa fa-fw fa-female"></i>
		                    <i class="fa fa-fw fa-female"></i>
		                    <i class="fa fa-fw fa-female"></i>
		                    <i class="fa fa-fw fa-female"></i>
		                    <i class="fa fa-fw fa-female"></i>
		                  </div>
		                  <div class="col-xs-2 text-red text-nowrap">
		                    <div class="f24">40%</div>
		                    <div>女:400人</div>
		                  </div>
		                </div>
		                <div class="row">
		                  <div class="col-xs-8 per-icon-box margin_t10">
		                    <i class="fa fa-fw fa-male text-blue"></i>
		                    <i class="fa fa-fw fa-male text-blue"></i>
		                    <i class="fa fa-fw fa-male text-blue"></i>
		                    <i class="fa fa-fw fa-male text-blue"></i>
		                    <i class="fa fa-fw fa-male"></i>
		                    <i class="fa fa-fw fa-male"></i>
		                    <i class="fa fa-fw fa-male"></i>
		                    <i class="fa fa-fw fa-male"></i>
		                    <i class="fa fa-fw fa-male"></i>
		                    <i class="fa fa-fw fa-male"></i>
		                  </div>
		                  <div class="col-xs-2 text-blue text-nowrap">
		                    <div class="f24">60%</div>
		                    <div>男:600人</div>
		                  </div>
		                </div>

		                <h3 class="cnt-box-title f16 margin_t10">年龄</h3>
		                <div id="barChart" data-role="chart" style="height:130px;"></div>
		              </div>
		            </div>
		          </div>
		        </div>
			</div>
		</div>
	</div>
</section>
<!-- 底部 -->
<!-- 
<footer class="site-footer">
  <div class="clearfix pad15">
    <div class="pull-left">
      <ul class="list-inline">
        <li class="li-1">
          <img src="dist/img/images/class-logo.png">
          <span class="f16 hidden-xs margin_l10 gray6 sp-1">教学教务组织平台</span>
        </li>
        <li class="li-2">
          <span class="f12 hidden-xs gray6 margin_r10 sp-2">powered by</span>
          <img src="dist/img/images/xueyun-logo.png">
        </li>
      </ul>
    </div>
    <div class="pull-right">
      教育服务热线： <br>
      <span>020-969300</span><br>
      <small class="gray9">周一至周五 9:00-18:00</small>
    </div>
  </div>
</footer>
 -->
<script src="${ctx}/static/plugins/select2/select2.full.min.js"></script>

<script type="text/javascript">
//select2
//$(".select2").select2();
	// filter tabs
	$(".filter-tabs li").click(function(event) {
		if($(this).hasClass('actived')){
			$(this).removeClass('actived');
		}
		else{
			$(this).addClass('actived');
		}
	});

	// filter menu
	$(".custom-dropdown")
	.on('click', "[data-role='sure-btn']", function(event) {
	  var $box=$(this).closest('.custom-dropdown');
	  $(this).closest('th').removeClass('on');
	  $box.find("li").removeClass('actived');
	  $box.find(":checkbox").attr("checked",false);
	})
	.on('click', "[data-role='close-btn']", function(event) {
	  $(this).closest('th').removeClass('on');
	})
	.on('click', 'li', function(event) {
	  if($(this).hasClass('actived')){
	    $(this).removeClass('actived')
	    .find(":checkbox").attr("checked",false);
	  }
	  else{
	    $(this).addClass('actived')
	    .find(":checkbox").attr("checked",true);
	  }
	});

	$('th[data-role="menu-th"]')
	.on('mouseover', function(event) {
	  $(this).addClass('on');
	})
	.on('mouseout', function(event) {
	  if(!$(this).children('.custom-dropdown').hasClass('open')){
	    $(this).removeClass('on');
	  }
	});


	// 标签页
	$('.nav-tabs-lg a').click(function (e) {
	  e.preventDefault();
	  $(this).tab('show');
	  if($(this).attr("href")=='#tab_top_2'){
	    var $tabItem=$(".tab-pane:last");
	    if($tabItem.find('[data-role="chart"]').children('div').width()==0){
	      $(window).resize();
	    }
	  }
	});
	/* echarts 图表 */

	// 年龄
	var ecBar=(function(){
	  var obj={};
	  obj.data=[
	    {
	      name:'00后',
	      value:10,
	      itemStyle:{
	        normal:{
	          color:'#f56954'
	        }
	      }
	    },
	    {
	      name:'90后',
	      value:30,
	      itemStyle:{
	        normal:{
	          color:'#00a65a'
	        }
	      }
	    },
	    {
	      name:'80后',
	      value:20,
	      itemStyle:{
	        normal:{
	          color:'#00c0ef'
	        }
	      }
	    },
	    {
	      name:'70后',
	      value:20,
	      itemStyle:{
	        normal:{
	          color:'#ffa412'
	        }
	      }
	    },
	    {
	      name:'60后',
	      value:10,
	      itemStyle:{
	        normal:{
	          color:'#3c8dbc'
	        }
	      }
	    },
	    {
	      name:'60前',
	      value:10,
	      itemStyle:{
	        normal:{
	          color:'#8b52cc'
	        }
	      }
	    }
	  ];
	  /*function sum(arr){
	    var r=0;
	    for (var i = 0; i < arr.length; i++) {
	      r+=arr[i].value;
	    };
	    return r;
	  };*/

	  obj.chart=echarts.init(document.getElementById('barChart'));
	  obj.chart.setOption({
	    tooltip : {
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        },
	        formatter:'{b}：{c}%'
	        /*formatter:function (params, ticket, callback) {
	          return params[0].name+'：'+params[0].value
	          +' '+Math.ceil(params[0].value/sum(obj.data)*100)+'%';
	        }*/
	    },
	    grid: {
	        left:0,
	        right:20,
	        bottom: 0,
	        top:20,
	        containLabel: true
	    },
	    xAxis : [
	        {
	            type : 'category',
	            data : ['00后', '90后', '80后', '70后', '60后', '60前'],
	            axisTick: {
	                alignWithLabel: true
	            },
	            axisLine:{
	              lineStyle:{
	                color:'#d2d6de',
	                width:1
	              }
	            },
	            axisLabel:{
	              textStyle:{
	                color:'#666666'
	              }
	            },
	            splitArea:{
	              show:true
	            }

	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
	            min:0,
	            max:100,
	            axisLine:{
	              //show:false
	              lineStyle:{
	                color:'#d2d6de',
	                width:1
	              }
	            },
	            axisLabel:{
	              textStyle:{
	                color:'#666666'
	              },
	              formatter:'{value}%'
	            },
	            axisTick:{
	              //show:false
	            },
	            splitLine:{
	              //show:false
	            }
	        }
	    ],
	    series : [
	        {
	            name:'直接访问',
	            type:'bar',
	            barWidth: '50%',
	            label:{
	              normal:{
	                position:'top',
	                show:true,
	                formatter: '{c}%'
	              }
	            },
	            data:obj.data
	        }
	    ]
	  });
	  return obj;
	})();
	// 学习中心统计
	var ecMap2=(function(){
	  var obj={};
	  obj.chart=echarts.init(document.getElementById('mapChart1'));

	  var geoCoordMap = {
	    "武汉":[114.31,30.52],
	    "广州市麓湖学习中心":[113.282949,23.152462]
	  };

	  var convertData = function (data) {
	      var res = [];
	      for (var i = 0; i < data.length; i++) {
	          var geoCoord = geoCoordMap[data[i].name];
	          if (geoCoord) {
	              res.push({
	                  name: data[i].name,
	                  value: geoCoord.concat(data[i].value)
	              });
	          }
	      }
	      return res;
	  };

	  obj.chart.setOption({
	    tooltip: {
	        trigger: 'item',
	        formatter: function (params) {
	            return params.name + ' : ' + params.value[2]+'K';
	        }
	    },
	    
	    geo: {
	        map: 'china',
	        zoom:1.2,
	        roam: true,
	        label: {
	            emphasis: {
	                show: true
	            }
	        },
	        itemStyle:{
	          normal:{
	            color:'#ffebe9'
	          },
	          emphasis:{
	            color:'#ff5945'
	          }
	        }
	    },
	    series: [
	        {
	          name: '学习中心统计',
	          type: 'scatter',
	          coordinateSystem: 'geo',
	          data: convertData([
	              {name: "武汉", value: 13},
	              {name: "广州市麓湖学习中心", value: 22}
	          ]),
	          symbol:'pin',
	          symbolSize: 20,
	          label: {
	              normal: {
	                  show: false
	              },
	              emphasis: {
	                  show: false
	              }
	          },
	          itemStyle: {
	              emphasis: {
	                  borderColor: '#fff',
	                  borderWidth: 1
	              }
	          }
	        }
	    ]
	  });
	  return obj;
	})();

	// 区域统计
	var ecMap=(function(){
	  var obj={};
	  
	  obj.mapChartData=[
	    {name: '北京',value: 7 },
	    {name: '天津',value: 2 },
	    {name: '上海',value: 12 },
	    {name: '重庆',value: 14 },
	    {name: '河北',value: 8 },
	    {name: '河南',value: 1 },
	    {name: '云南',value: 15 },
	    {name: '辽宁',value: 5 },
	    {name: '黑龙江',value: 9 },
	    {name: '湖南',value: 7 },
	    {name: '安徽',value: 17 },
	    {name: '山东',value: 2 },
	    {name: '新疆',value: 4 },
	    {name: '江苏',value: 14 },
	    {name: '浙江',value: 2 },
	    {name: '江西',value: 7},
	    {name: '湖北',value: 9 },
	    {name: '广西',value: 19 },
	    {name: '甘肃',value: 7 },
	    {name: '山西',value: 3 },
	    {name: '内蒙古',value: 5 },
	    {name: '陕西',value: 7 },
	    {name: '吉林',value: 8 },
	    {name: '福建',value: 12 },
	    {name: '贵州',value: 10 },
	    {name: '广东',value: 24 },
	    {name: '青海',value: 4 },
	    {name: '西藏',value: 7 },
	    {name: '四川',value: 2 },
	    {name: '宁夏',value: 18 },
	    {name: '海南',value: 14 },
	    {name: '台湾',value: 16 },
	    {name: '香港',value: 4 },
	    {name: '澳门',value: 7 }
	  ];
	  obj.chart=echarts.init(document.getElementById('mapChart2'));
	  obj.chart.setOption({
	    tooltip: {
	        trigger: 'item',
	        formatter:'{b}：{c}k'
	    },
	    visualMap: {
	        type:'piecewise',
	        inverse:true,
	        itemGap:1,
	        textStyle:{color:'#a96158'},
	        pieces:[
	          {min:0,max:5,label:'0-5k',color:'#ffe9e7'},
	          {min:5,max:10,label:'5k-10k',color:'#ffc9c2'},
	          {min:10,max:15,label:'10k-15k',color:'#ffada3'},
	          {min:15,max:20,label:'15k-20k',color:'#ff9184'},
	          {min:20,label:'20k+',color:'#ff5844'}
	        ],
	        left: 'left',
	        top: 'bottom',
	    },
	    series: [
	        {
	            name: '区域统计',
	            type: 'map',
	            mapType: 'china',
	            roam: true,
	            layoutCenter: ['50%', '50%'],
	            layoutSize:'120%',
	            label: {
	                normal: {
	                    show: false
	                },
	                emphasis: {
	                    show: true

	                }
	            },
	            data:obj.mapChartData
	        }
	    ]
	  });
	  return obj;
	})();
	/*
	setTimeout(function(){
	  mapChartData=[
	    {name: '北京',value: 17 },
	    {name: '天津',value: 12 },
	    {name: '上海',value: 12 },
	    {name: '重庆',value: 14 },
	    {name: '河北',value: 8 },
	    {name: '河南',value: 1 },
	    {name: '云南',value: 15 },
	    {name: '辽宁',value: 5 },
	    {name: '黑龙江',value: 9 },
	    {name: '湖南',value: 7 },
	    {name: '安徽',value: 17 },
	    {name: '山东',value: 2 },
	    {name: '新疆',value: 14 },
	    {name: '江苏',value: 14 },
	    {name: '浙江',value: 2 },
	    {name: '江西',value: 7},
	    {name: '湖北',value: 9 },
	    {name: '广西',value: 19 },
	    {name: '甘肃',value: 7 },
	    {name: '山西',value: 3 },
	    {name: '内蒙古',value: 5 },
	    {name: '陕西',value: 7 },
	    {name: '吉林',value: 8 },
	    {name: '福建',value: 12 },
	    {name: '贵州',value: 10 },
	    {name: '广东',value: 24 },
	    {name: '青海',value: 4 },
	    {name: '西藏',value: 7 },
	    {name: '四川',value: 2 },
	    {name: '宁夏',value: 18 },
	    {name: '海南',value: 14 },
	    {name: '台湾',value: 16 },
	    {name: '香港',value: 4 },
	    {name: '澳门',value: 7 }
	  ];
	  ecMap.mapChart.setOption({
	    series:[{
	      data:mapChartData
	    }]
	  });
	},2000);
	*/
	// 学历层次统计
	var ecPie2=(function(){
	  var obj={};
	  obj.chart=echarts.init(document.getElementById('pieChart2'));
	  obj.chart.setOption({
	      legend: {
	          top:10,
	          right:10,
	          orient:'vertical',
	          data:['本科','专科','中职']
	      },
	      tooltip : {
	          trigger: 'item',
	          formatter: "{b} : {c} ({d}%)"
	      },
	      series : [
	          {

	              name: '学历层次统计',
	              type: 'pie',
	              center: ['50%', '50%'],
	              label:{
	                normal:{
	                  formatter: "{b}:{c}({d}%)"
	                }
	              },
	              data:[
	                  {value:335, name:'本科',itemStyle:{
	                      normal:{
	                        color:'#3c8dbc'
	                      }
	                    }
	                  },
	                  {value:234, name:'专科',itemStyle:{
	                      normal:{
	                        color:'#00a65a'
	                      }
	                    }
	                  },
	                  {value:134, name:'中职',itemStyle:{
	                      normal:{
	                        color:'#f56954'
	                      }
	                    }
	                  }
	              ],
	              itemStyle: {
	                  normal:{
	                    borderWidth:1,
	                    borderColor:'#fff'
	                  },
	                  emphasis: {
	                      shadowBlur: 10,
	                      shadowOffsetX: 0,
	                      shadowColor: 'rgba(0, 0, 0, 0.5)'
	                  }
	              }
	          }
	      ]
	  });
	  return obj;
	})();

	//报读资料统计
	var ecBar4=(function(){
		var obj={};
		obj.data=[
		  {
		    name:'未提交',
		    value:20,
		    itemStyle:{
		      normal:{
		        color:'#f56954'
		      }
		    }
		  },
		  {
		    name:'待审核',
		    value:60,
		    itemStyle:{
		      normal:{
		        color:'#00a65a'
		      }
		    }
		  },
		  {
		    name:'审核通过',
		    value:760,
		    itemStyle:{
		      normal:{
		        color:'#00c0ef'
		      }
		    }
		  },
		  {
		    name:'重新提交',
		    value:60,
		    itemStyle:{
		      normal:{
		        color:'#ffa412'
		      }
		    }
		  },
		  {
		    name:'审核不通过',
		    value:60,
		    itemStyle:{
		      normal:{
		        color:'#3c8dbc'
		      }
		    }
		  }
		];

		obj.chart=echarts.init(document.getElementById('ecBar4'));
		obj.chart.setOption({
		  tooltip : {
		      trigger: 'axis',
		      axisPointer : {
		          type : 'shadow'        
		      },
		      formatter:'{b}：{c}'
		  },
		  grid: {
		      left:10,
		      right:10,
		      bottom: 10,
		      top:20,
		      containLabel: true
		  },
		  xAxis : [
		      {
		          type : 'category',
		          data : ['未提交', '待审核', '审核通过','重新提交','审核不通过'],
		          axisTick: {
		              alignWithLabel: true
		          },
		          axisLine:{
		            lineStyle:{
		              color:'#d2d6de',
		              width:1
		            }
		          },
		          axisLabel:{
		            textStyle:{
		              color:'#666666'
		            }
		          }

		      }
		  ],
		  yAxis : [
		      {
		          type : 'value',
		          axisLine:{
		            lineStyle:{
		              color:'#d2d6de',
		              width:1
		            }
		          },
		          axisLabel:{
		            textStyle:{
		              color:'#666666'
		            },
		            formatter:'{value}'
		          }
		      }
		  ],
		  series : [
		      {
		          name:'报读资料统计',
		          type:'bar',
		          barWidth: '50%',
		          label:{
		            normal:{
		              position:'top',
		              show:true,
		              formatter: '{c}'
		            }
		          },
		          data:obj.data
		      }
		  ]
		});
		return obj;
	})();

	//报读缴费统计
	var ecBar5=(function(){
		var obj={};
		obj.data=[
		  {
		    name:'已缴费',
		    value:20,
		    itemStyle:{
		      normal:{
		        color:'#f56954'
		      }
		    }
		  },
		  {
		    name:'未缴费',
		    value:60,
		    itemStyle:{
		      normal:{
		        color:'#00a65a'
		      }
		    }
		  },
		  {
		    name:'已支付',
		    value:760,
		    itemStyle:{
		      normal:{
		        color:'#00c0ef'
		      }
		    }
		  }
		];

		obj.chart=echarts.init(document.getElementById('ecBar5'));
		obj.chart.setOption({
		  tooltip : {
		      trigger: 'axis',
		      axisPointer : {
		          type : 'shadow'        
		      },
		      formatter:'{b}：{c}'
		  },
		  grid: {
		      left:10,
		      right:10,
		      bottom: 10,
		      top:20,
		      containLabel: true
		  },
		  xAxis : [
		      {
		          type : 'category',
		          data : ['已缴费', '未缴费', '已支付'],
		          axisTick: {
		              alignWithLabel: true
		          },
		          axisLine:{
		            lineStyle:{
		              color:'#d2d6de',
		              width:1
		            }
		          },
		          axisLabel:{
		            textStyle:{
		              color:'#666666'
		            }
		          }

		      }
		  ],
		  yAxis : [
		      {
		          type : 'value',
		          axisLine:{
		            lineStyle:{
		              color:'#d2d6de',
		              width:1
		            }
		          },
		          axisLabel:{
		            textStyle:{
		              color:'#666666'
		            },
		            formatter:'{value}'
		          }
		      }
		  ],
		  series : [
		      {
		          name:'报读缴费统计',
		          type:'bar',
		          barWidth: '50%',
		          label:{
		            normal:{
		              position:'top',
		              show:true,
		              formatter: '{c}'
		            }
		          },
		          data:obj.data
		      }
		  ]
		});
		return obj;
	})();
	$(window).bind("resize",function(){
	  ecBar.chart.resize();
	  ecPie2.chart.resize();
	  ecMap.chart.resize();
	  ecMap2.chart.resize();
	  ecBar4.chart.resize();
	  ecBar5.chart.resize();
	});

	//$("#signupInfo").click(function(){
		 // alert(11);
	//});


</script>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>
