<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>学情分析</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:;">学习管理</a></li>
		<li class="active">学情分析</li>
	</ol>
</section>
<section class="content">
	<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs nav-tabs-lg">
			<li><a href="javascript:;" data-toggle="tab" onclick="changType('1')">课程学情</a></li>
			<li><a href="javascript:;" data-toggle="tab" onclick="changType('2')">课程班学情</a></li>
			<li class="active"><a href="javascript:;" data-toggle="tab" onclick="changType('3')">教务班学情</a></li>
			<li><a href="javascript:;" data-toggle="tab" onclick="changType('4')">学员学情</a></li>
			<li><a href="javascript:;" data-toggle="tab" onclick="changType('5')">专业学情</a></li>
		</ul>
		<form id="listForm" class="form-horizontal">
		<div class="tab-content">
			<div class="tab-pane active" id="tab_top_2">
				<div class="box box-border">
				    <div class="box-body">
				        <div class="row pad-t15">
				          
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">学期</label>
				              <div class="col-sm-9">
				                <input type="text" class="form-control" name="GRADE_NAME" value="${param.GRADE_NAME }">
				              </div>
				            </div>
				          </div>

				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">教务班</label>
				              <div class="col-sm-9">
				                <input type="text" class="form-control" name="BJMC" value="${param.BJMC }">
				              </div>
				            </div>
				          </div>

				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">平均完成课程比例</label>
				              <div class="col-sm-9">
				                <div class="input-group">
			                      <div class="input-group-btn">
			                        <select class="form-control input-group-select bg-white" name="PASS_FLG">
			                          <option value="EQ" <c:if test="${empty param.PASS_FLG || param.PASS_FLG eq 'EQ' }">selected="selected"</c:if>>等于</option>
			                          <option value="GT" <c:if test="${param.PASS_FLG eq 'GT' }">selected="selected"</c:if>>大于</option>
			                          <option value="GTE" <c:if test="${param.PASS_FLG eq 'GTE' }">selected="selected"</c:if>>大于等于</option>
			                          <option value="LT" <c:if test="${param.PASS_FLG eq 'LT' }">selected="selected"</c:if>>小于</option>
			                          <option value="LTE" <c:if test="${param.PASS_FLG eq 'LTE' }">selected="selected"</c:if>>小于等于</option>
			                        </select>
			                      </div>
			                      <input type="text" name="PASS_BL" class="form-control" value="${param.PASS_BL }">
			                    </div>
				              </div>
				            </div>
				          </div>
				          <%--
				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">姓名</label>
				              <div class="col-sm-9">
				                <input type="text" class="form-control" name="XM" value="${param.XM }">
				              </div>
				            </div>
				          </div>

				          <div class="col-sm-4">
				            <div class="form-group">
				              <label class="control-label col-sm-3 text-nowrap">学号</label>
				              <div class="col-sm-9">
				                <input type="text" class="form-control" name="XH" value="${param.XH }">
				              </div>
				            </div>
				          </div>
				          --%>

						<div class="col-sm-4">
							<div class="form-group">
								<label class="control-label col-sm-3 text-nowrap">平均完成考试比例</label>
								<div class="col-sm-9">
									<div class="input-group">
										<div class="input-group-btn">
											<select class="form-control input-group-select bg-white" name="EXAM_FLG">
												<option value="EQ" <c:if test="${empty param.EXAM_FLG || param.EXAM_FLG eq 'EQ' }">selected="selected"</c:if>>等于</option>
												<option value="GT" <c:if test="${param.EXAM_FLG eq 'GT' }">selected="selected"</c:if>>大于</option>
												<option value="GTE" <c:if test="${param.EXAM_FLG eq 'GTE' }">selected="selected"</c:if>>大于等于</option>
												<option value="LT" <c:if test="${param.EXAM_FLG eq 'LT' }">selected="selected"</c:if>>小于</option>
												<option value="LTE" <c:if test="${param.EXAM_FLG eq 'LTE' }">selected="selected"</c:if>>小于等于</option>
											</select>
										</div>
										<input type="text" name="PASS_EXAM_BL" class="form-control" value="${param.PASS_EXAM_BL }">
									</div>
								</div>
							</div>
				        </div>
						<shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
							<div class="col-sm-4">
								<div class="form-group">
									<label class="control-label col-sm-3 text-nowrap">平均已获学分比例</label>
									<div class="col-sm-9">
										<div class="input-group">
											<div class="input-group-btn">
												<select class="form-control input-group-select bg-white" name="PASS_FLG">
													<option value="EQ" <c:if test="${empty param.XF_FLG || param.XF_FLG eq 'EQ' }">selected="selected"</c:if>>等于</option>
													<option value="GT" <c:if test="${param.XF_FLG eq 'GT' }">selected="selected"</c:if>>大于</option>
													<option value="GTE" <c:if test="${param.XF_FLG eq 'GTE' }">selected="selected"</c:if>>大于等于</option>
													<option value="LT" <c:if test="${param.XF_FLG eq 'LT' }">selected="selected"</c:if>>小于</option>
													<option value="LTE" <c:if test="${param.XF_FLG eq 'LTE' }">selected="selected"</c:if>>小于等于</option>
												</select>
											</div>
											<input type="text" name="XF_BL" class="form-control" value="${param.XF_BL }">
										</div>
									</div>
								</div>
							</div>
						</shiro:lacksPermission>
				    </div><!-- /.box-body -->
				    <div class="box-footer text-right">
			          <button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
			          <button type="button" class="btn min-width-90px btn-default">重置</button>
			        </div><!-- /.box-footer-->
				</div>
				<div class="box box-border margin-bottom-none">
					<div class="box-body">
						<div class="table-responsive">
							<table class="table table-bordered table-striped vertical-mid text-center table-font">
								<thead>
					              <tr>
					              	<th>学期</th>
					              	<th width="300px;">教务班</th>
					                <th width="140px;">班级人数</th>
									<shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
					                	<th>平均已获学分/<br>最低毕业学分/<br>总学分</th>
									</shiro:lacksPermission>
					                <th>平均完成课程数/<br>课程总数</th>
					                <th>平均完成考试数/<br>考试科目总数</th>
					                <th>操作</th>
					              </tr>
					            </thead>
					            <tbody>
					            	<c:forEach items="${pageInfo.content}" var="entity">
					            	<tr>
					            		<td>
											${entity.GRADE_NAME}
											<%--
					            			<div class="text-left">
						            			姓名：${entity.XM }<br>
						            			学号：${entity.XH }<br>
						            			手机：${entity.SJH }
					            			</div>
											--%>
					            		</td>
					            		<td class="text-left">
											${entity.BJMC}
					            			<%--
					            			<div class="text-left">
						            			层次：${entity.PYCC_NAME }<br>
						            			学期：${entity.GRADE_NAME }<br>
						            			专业：${entity.ZYMC }
					            			</div>
					            			--%>
					            		</td>
					            		<td>
					            			${entity.STUDENT_COUNT }
					            		</td>
										<shiro:lacksPermission name="/studymanage/getCourseStudyList$schoolModel">
											<td>
												<div>
													<span class="f18">${entity.SUM_GET_CREDITS }/</span>${entity.ZDBYXF }/${entity.ZXF }
												</div>
												<div class="gray9">
													（已获得${entity.XF_BL }%）
												</div>
											</td>
										</shiro:lacksPermission>
					            		<td>
					            			<div>
					            				<span class="f18">${entity.PASS_REC_COUNT }/</span>${entity.REC_COUNT }
					            			</div>
					            			<div class="gray9">
					            				（已完成${entity.PASS_BL }%）
					            			</div>
					            		</td>
					            		<td>
											<div>
												<span class="f18">${entity.PASS_EXAM_COUNT }/</span>${entity.REC_COUNT }
											</div>
											<div class="gray9">
												（已完成${entity.PASS_EXAM_BL }%）
											</div>
					            		</td>
					            		<td>
					            			<a href="${ctx}/studymanage/getTeachClassDetails/${entity.CLASS_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
					            		</td>
					            	</tr>
					            	</c:forEach>
					            </tbody>
							</table>
							<tags:pagination page="${pageInfo}" paginationSize="5" />
						</div>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
function changType(type) {
	if (type=="1") {
		window.location.href = "getCourseStudyList";
	} else if (type=="2") {
		window.location.href = "getCourseClassList";
	} else if (type=="3") {
		window.location.href = "getTeachClassList";
	} else if (type=="4") {
		window.location.href = "getStudentCourseList";
	} else if (type=="5") {
		window.location.href = "getStudentMajorList";
	}
}
</script>
</body>
</html>