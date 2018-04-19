<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>管理系统-毕业申请</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
	<link rel="stylesheet" href="${ctx}/static/dist/css/signup-info.css">
</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb oh">
			<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
			<li><a href="#">毕业管理</a></li>
			<li><a href="${ctx}/graduation/applyCertif/list">毕业申请</a></li>
			<li class="active">学员申请详情</li>
		</ol>
	</section>

	<section class="content">
		<div class="box">
			<div class="box-body">
				<div class="row">
					<div class="col-sm-4">
						<div class="media pad">
							<div class="media-left" style="padding-right:25px;">
								<img src="${not empty info.gjtGraduationRegister.photo ? info.gjtGraduationRegister.photo : ctx.concat('/static/dist/img/images/no-img.png')}" class="img-rounded" width="120" height="150" onerror="this.src='${ctx}/static/dist/img/images/no-img.png'" alt="User Image" />
								<c:if test="${empty info.gjtGraduationRegister.photo}">
									<div class="gray9 text-center f12">未上传新华社照片</div>
								</c:if>
							</div>
							<div class="media-body">
								<h3 class="margin_t10">
									${info.gjtStudentInfo.xm}
								</h3>
								<ul class="list-unstyled">
									<li>学号：${info.gjtStudentInfo.xh}</li>
									<li>学期：${info.gjtStudentInfo.gjtGrade.gradeName}</li>
									<li>层次：<dic:getLabel typeCode="TrainingLevel" code="${info.gjtStudentInfo.pycc }" /></li>
									<li>专业：${info.gjtStudentInfo.gjtSpecialty.zymc} <small class="gray9">（${info.gjtStudentInfo.gjtSpecialty.ruleCode}）</small> </li>
								</ul>

							</div>
						</div>
					</div>
					<div class="col-sm-8">
						<table class="table no-border vertical-middle no-margin text-center margin_t20" height="120" style="table-layout: fixed;">
							<tbody>
							<tr>
								<td width="19%" style="border-left:1px solid #e5e5e5">
									${info.graduationCondition==1?'<div class="f20 text-green">已满足</div>':'<span class="f20 text-orange">未满足</span>'}
									<div class="gray6 margin_t5" style="line-height:1.2">是否满足<br>毕业申请条件</div>
								</td>
								<td width="19%" style="border-left:1px solid #e5e5e5">
									${info.applyDegree==1?'<span class="f20 text-green">是</span>':'<span class="f20 text-orange">否</span>'}
									<div class="gray6 margin_t5" style="line-height:1.2">是否<br>申请学位</div>
								</td>
								<td style="border-left:1px solid #e5e5e5">
									<div class="f18" style="word-break: break-all;line-height:1.2">201703948392039393</div>
									<div class="gray6 margin_t5">电子注册号</div>
								</td>
								<td width="17%" style="border-left:1px solid #e5e5e5">
									<c:choose>
										<c:when test="${info.auditState==1}"><span class="f20 text-green">审核通过</span></c:when>
										<c:when test="${info.auditState==2}"><span class="f20 text-red">审核不通过</span></c:when>
										<c:otherwise><span class="f20 text-orange">审核中</span></c:otherwise>
									</c:choose>
									<div class="gray6 margin_t5">毕业状态</div>
								</td>
								<td width="22%" style="border-left:1px solid #e5e5e5">
									<a role="button" href="${ctx}/graduation/applyCertif/download?studentId=${info.gjtStudentInfo.studentId}" class="btn btn-default"><i class="fa fa-download"></i> 下载毕业登记表</a>
								</td>
							</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>

		<div class="box no-border margin-bottom-none">
			<div class="nav-tabs-custom no-margin">
				<ul class="nav nav-tabs nav-tabs-lg">
					<li class="active"><a href="#tab_notice_1" data-toggle="tab">毕业条件查询</a></li>
					<li ><a href="#tab_notice_2" data-toggle="tab">审核记录</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="tab_notice_1">
						<h3 class="cnt-box-title f16 margin_b10">专业规则要求</h3>
						<table class="table-gray-th table-font text-center vertical-middle">
							<tbody>
							<th>最低毕业学分</th>
							<td>71</td>

							<th>已通过学分</th>
							<td>
								76
								<div class="text-green">（已达标）</div>
							</td>

							<th>中央电大考试学分</th>
							<td>37</td>

							<th>已通过中央电大考试学分</th>
							<td>
								37
								<div class="text-green">（已达标）</div>
							</td>
							</tbody>
						</table>

						<h3 class="cnt-box-title f16 margin_b10 margin_t20">成绩明细</h3>
						<table class="table table-bordered vertical-mid text-center margin-bottom-none table-font">
							<thead class="with-bg-gray">
							<tr>
								<th>课程模块</th>
								<th>模块<br>最低学分</th>
								<th>模块<br>中央最低学分</th>
								<th>模块<br>已通过学分</th>
								<th>课程代码</th>
								<th>课程名称</th>
								<th>课程性质</th>
								<th>课程类型</th>
								<th>学分</th>
								<th>开设学期</th>
								<th>考试单位</th>
								<th>成绩</th>
								<th>是否达标<br>毕业条件</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td rowspan="2">公共基础课</td>
								<td>13</td>
								<td>7</td>
								<td>13</td>
								<td>00815</td>
								<td>计算机应用基础</td>
								<td>选修</td>
								<td>非统设</td>
								<td>4</td>
								<td>1</td>
								<td>中央</td>
								<td>61</td>
								<td rowspan="2">
									<span class="text-green">已达标</span>
								</td>
							</tr>
							<tr>
								<td>13</td>
								<td>7</td>
								<td>13</td>
								<td>02969</td>
								<td>中国特色社会主义理论体系概论</td>
								<td>必修</td>
								<td>统设</td>
								<td>4</td>
								<td>1</td>
								<td>省</td>
								<td>61</td>
							</tr>
							<tr>
								<td rowspan="2">专业基础课</td>
								<td>13</td>
								<td>7</td>
								<td>13</td>
								<td>00815</td>
								<td>计算机应用基础</td>
								<td>选修</td>
								<td>非统设</td>
								<td>4</td>
								<td>1</td>
								<td>中央</td>
								<td>61</td>
								<td rowspan="2">
									<span class="text-green">已达标</span>
								</td>
							</tr>
							<tr>
								<td>13</td>
								<td>7</td>
								<td>13</td>
								<td>02969</td>
								<td>中国特色社会主义理论体系概论</td>
								<td>必修</td>
								<td>统设</td>
								<td>4</td>
								<td>1</td>
								<td>省</td>
								<td>61</td>
							</tr>
							</tbody>
						</table>

						<h3 class="cnt-box-title f16 margin_b10 margin_t20">毕业申请条件</h3>
						<table class="table table-striped table-bordered vertical-mid text-center margin-bottom-none">
							<tbody>
							<tr>
								<td>1</td>
								<td>学籍期内</td>
								<td>学籍有效期内</td>
								<td class="text-green">已达标</td>
							</tr>
							<tr>
								<td>2</td>
								<td>学分要求</td>
								<td>取得教学计划规定的最低毕业总学分71分</td>
								<td class="text-green">已达标</td>
							</tr>
							<tr>
								<td>3</td>
								<td>最短年限</td>
								<td>学生必须达到最短学习年限2.5年</td>
								<td class="text-green">已达标</td>
							</tr>
							<tr>
								<td>4</td>
								<td>思想品德</td>
								<td>思想品德经鉴定符合要求.5年</td>
								<td class="text-green">已达标</td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="tab-pane " id="tab_notice_2">
						<div class="approval-list clearfix">
							<c:forEach var="record" items="${flowRecordList}" varStatus="s">
								<c:if test="${record.auditOperatorRole==1}">
									<dl class="approval-item">
										<dt class="clearfix">
											<b class="a-tit gray6">${s.index==0?'学员申请毕业':'学员重申毕业'}</b>
											<span class="gray9 text-no-bold f12"><fmt:formatDate value="${record.auditDt}" type="both"/></span>
											<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
											<label class="state-lb text-green">提交申请</label>
										</dt>
									</dl>
								</c:if>
								<c:if test="${record.auditOperatorRole!=1}">
									<dl class="approval-item" <c:if test="${s.index==(fn:length(flowRecordList)-1)}">style="border-left: 4px solid rgb(255,255,255);"</c:if>>
										<dt class="clearfix">
											<b class="a-tit gray6">${record.auditOperatorRole==2?'学习中心初审':record.auditOperatorRole==3?'分部复审':record.auditOperatorRole==4?'总部终审':''}</b>
											<c:if test="${record.auditState==0}">
												<span class="fa fa-fw fa-dot-circle-o text-orange"></span>
												<label class="state-lb text-orange">待审核</label>
											</c:if>
											<c:if test="${record.auditState==1}">
												<span class="gray9 text-no-bold f12"><fmt:formatDate value="${record.auditDt}" type="both"/></span>
												<span class="fa fa-fw fa-check-circle text-green"></span>
												<label class="state-lb text-green">审核通过</label>
											</c:if>
											<c:if test="${record.auditState==2}">
												<span class="gray9 text-no-bold f12"><fmt:formatDate value="${record.auditDt}" type="both"/></span>
												<span class="fa fa-fw fa-times-circle text-red"></span>
												<label class="state-lb text-red">审核不通过</label>
											</c:if>
										</dt>
										<c:if test="${record.auditState==0}">
											<c:if test="${item.xjzt!='5'}">
												<c:choose>
													<c:when test="${record.auditOperatorRole==auditOperatorRole&&openMode=='1'}">
														<dd>
															<div class="col-xs-12 no-padding position-relative">
																<textarea name="auditContent" class="form-control" rows="3" placeholder="请输入资料审核备注，例如该学员的资料确认无误" datatype="*1-200" nullmsg="请输入内容！" errormsg="字数不能超过200"></textarea>
															</div>
															<div>
																<button type="button" class="btn min-width-90px btn-warning margin_r10 margin_t10 btn-audit" val="2" data-form-id="5">审核不通过</button>
																<button type="button" class="btn min-width-90px btn-success margin_r10 margin_t10 btn-audit" val="1" data-form-id="5">审核通过</button>
															</div>
														</dd>
													</c:when>
												</c:choose>
											</c:if>
										</c:if>
										<c:if test="${record.auditState!=0}">
											<dd>
												<div class="txt">
													<p>${record.auditContent}</p>
													<div class="gray9 text-right">审核人：${record.auditOperator}</div>
													<i class="arrow-top"></i>
												</div>
											</dd>
										</c:if>
									</dl>
								</c:if>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript" src="${ctx}/static/js/edumanage/schoolRollInfo/school_roll_info_form.js"></script>
<script type="text/javascript">
	(function($) {

		var action = $('form#inputForm :input[name="action"]').val();
		if(action == 'audit') {
			$('ul.nav-tabs li:last a').trigger('click');
		}
		if($('button.btn-audit')[0] == undefined) {
			$('[data-role="upload-img"]').hide();
		}
	})(jQuery);
</script>
</body>
</html>
