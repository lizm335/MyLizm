<%@page import="com.gzedu.xlims.pojo.system.StudyYear"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb oh">
			<li>
				<a href="homepage.html">
					<i class="fa fa-home"></i> 首页
				</a>
			</li>
			<li>
				<a href="#">毕业管理</a>
			</li>
			<li>
				<a href="#">学位申请</a>
			</li>
			<li class="active">学员申请详情</li>
		</ol>
	</section>
	<section class="content">
		<div class="box">
			<div class="box-body">
				<div class="row">
					<div class="col-sm-4">
						<div class="media pad">
							<div class="media-left" style="padding-right: 25px;">
								<img src="${not empty info.gjtGraduationRegister.photo ? info.gjtGraduationRegister.photo : ctx.concat('/static/dist/img/images/no-img.png')}" class="img-rounded" width="112" height="112" onerror="this.src='${ctx}/static/dist/img/images/no-img.png'" alt="User Image" />
								<c:if test="${empty info.gjtGraduationRegister.photo}">
									<div class="gray9 text-center f12">未上传新华社照片</div>
								</c:if>
							</div>
							<div class="media-body">
								<h3 class="margin_t10">${info.gjtStudentInfo.xm}</h3>

								<ul class="list-unstyled">
									<li>学号：${info.gjtStudentInfo.xh }</li>
									<li>学期：${info.gjtStudentInfo.gjtGrade.gradeName}</li>
									<li>
										层次：
										<dic:getLabel typeCode="TrainingLevel" code="${info.gjtStudentInfo.pycc }" />
									</li>
									<li>
										专业：${info.gjtStudentInfo.gjtSpecialty.zymc} <small class="gray9">（${info.gjtStudentInfo.gjtSpecialty.ruleCode}）</small>
									</li>
								</ul>

							</div>
						</div>
					</div>
					<div class="col-sm-8">
						<table class="table no-border vertical-middle no-margin text-center margin_t20" height="120" style="table-layout: fixed;">
							<tbody>
								<tr>
									<td width="19%" style="border-left: 1px solid #e5e5e5">${info.graduationCondition==1?'<div class="f20 text-green">已满足</div>':'<div class="f20 text-orange">未满足</div>'}
										<div class="gray6 margin_t5" style="line-height: 1.2">
											是否满足<br>学位申请条件
										</div>
									</td>

									<td style="border-left: 1px solid #e5e5e5">
										<div class="f18" style="word-break: break-all; line-height: 1.2">201703948392039393</div>
										<div class="gray6 margin_t5">电子注册号</div>
									</td>
									<td width="19%" style="border-left: 1px solid #e5e5e5">
										<div>
											<c:if test="${not empty(info.gjtDegreeCollege.cover) }">
												<img src="${info.gjtDegreeCollege.cover}" class="img-circle" width="80" height="80" alt="College Image">
											</c:if>
										</div>
										<div class="gray6 margin_t5" style="line-height: 1.2">申请“${info.gjtDegreeCollege.collegeName }”的学位</div>
									</td>
									<td width="17%" style="border-left: 1px solid #e5e5e5"><c:choose>
											<c:when test="${info.auditState==1}">
												<div class="f20 text-green">审核通过</div>
											</c:when>
											<c:when test="${info.auditState==2}">
												<div class="f20 text-red">审核不通过</div>
											</c:when>
											<c:otherwise>
												<div class="f20 text-orange">待审核</div>
											</c:otherwise>
										</c:choose>
										<div class="gray6 margin_t5">学位状态</div>
									</td>
									<td width="22%" style="border-left: 1px solid #e5e5e5">
										<a role="button" href="${ctx}/graduation/degreemanager/download?search_EQ_studentId=${info.studentId}" class="btn btn-default">
											<i class="fa fa-download"></i> 下载学位申请资料
										</a>
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
					<li class="active">
						<a href="#tab_notice_1" data-toggle="tab">学位条件查询</a>
					</li>
					<li>
						<a href="#tab_notice_2" data-toggle="tab">审核记录</a>
					</li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="tab_notice_1">
						<div class="box box-border">
							<div class="box-header with-border">
								<h3 class="box-title pad-t5">申请学位的专业：${info.gjtSpecialtyBase.specialtyName}</h3>
							</div>
							<div style="margin: -1px;">
								<table class="table table-bordered table-cell-ver-mid text-center margin-bottom-none">
									<thead class="table-gray-th">
										<th width="8%">序号</th>
										<th width="15%">学位要求项</th>
										<th>学位要求描述</th>
										<th width="19%">状态</th>
										<th width="10%">操作</th>
									</thead>
									<tbody>
										<c:forEach items="${requirements}" var="req" varStatus="i">
										<tr>
											<td>${i.index+1}</td>
											<td>${baseTypeMap[req.requirementType]}</td>
											<td>
												<div class="text-left">${req.requirementDesc}</div>
											</td>
											<td>
												<c:choose>
													<c:when test="${req.isPass}"><div class="text-green">已通过</div></c:when>
													<c:when test="${!req.isPass}"><div class="text-red">未通过</div></c:when>
												</c:choose>
												
											</td>
											<td><a href="${ctx}/graduation/degreemanager/queryCourseScore?studentId=${info.studentId}" class="operion-item" data-toggle="tooltip" title="查看详情" data-container="body" data-role="more-1">
													<i class="fa fa-view-more"></i>
												</a></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>

						<div class="box box-border">
							<div class="box-header with-border">
								<h3 class="box-title pad-t5">学位申请资料</h3>
							</div>
							<div style="margin: -1px;">
								<div class="panel panel-default margin-bottom-none flat">
									<div class="panel-heading">
										<h3 class="panel-title text-bold">
											<span class="margin-r-5">身份证原件正反面扫描件</span>
										</h3>
									</div>
									<div class="panel-body">
										<div class="row">
											<div class="col-sm-6 margin_b15">
												<div class="cert-wrap">
													<h4 class="cert-title text-center f16">身份证正面</h4>
													<div class="cert-box has-upload cert-box-1">
														<a href="javascript:void(0);" class="info-img-box">
															<img class="info-img" src="${info.idcardFrontUrl}" alt="User Image">
														</a>
														<a href="#" data-role="lightbox" data-large-img="${info.idcardFrontUrl}" class="light-box">点击放大</a>
													</div>
												</div>
											</div>
											<div class="col-sm-6 margin_b15">
												<div class="cert-wrap">
													<h4 class="cert-title text-center f16">身份证反面</h4>
													<div class="cert-box has-upload cert-box-1">

														<a href="javascript:void(0);" class="info-img-box">
															<img class="info-img" src="${info.idcardBackUrl}" alt="User Image">
														</a>
														<a href="#" data-role="lightbox" data-large-img="${info.idcardBackUrl}" class="light-box">点击放大</a>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="panel panel-default margin-bottom-none flat">
									<div class="panel-heading">
										<h3 class="panel-title text-bold">
											<span class="margin-r-5">学位论文查重初检报告</span>
										</h3>
									</div>
									<div class="panel-body">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<div class="cert-wrap">
													<div class="cert-box has-upload cert-box-1">
														<a href="javascript:void(0);" class="info-img-box">
															<img class="info-img" src="${info.paperCheckUrl}" alt="User Image">
														</a>
														<a href="#" data-role="lightbox" data-large-img="${info.paperCheckUrl}" class="light-box">点击放大</a>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="panel panel-default margin-bottom-none flat">
									<div class="panel-heading">
										<h3 class="panel-title text-bold">
											<span class="margin-r-5">毕业设计（论文）审批</span>
										</h3>
									</div>
									<div class="panel-body text-center">
										<img src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504933492682&di=35551cad5ab9fe348f48f5986790b18e&imgtype=0&src=http%3A%2F%2Fupload.news.cecb2b.com%2F2013%2F1023%2F1382502422575.jpg" width="200">
										<div class="margin_t10 f16">${info.gjtStudentInfo.xm}的毕业论文</div>
										<div class="margin_t30">
											<a href="http://eezxyl.gzedu.com?furl=${info.paperUrl}" role="button" class="btn min-width-90px btn-warning margin_r10" data-role="addon-preview">在线预览</a>


											<a href="${info.paperUrl}" role="button" class="btn min-width-90px btn-primary">下载查看</a>
										</div>
									</div>
								</div>

								<div class="panel panel-default margin-bottom-none flat">
									<div class="panel-heading">
										<h3 class="panel-title text-bold">
											<span class="margin-r-5">学位申请表</span>
										</h3>
									</div>
									<div class="panel-body">
										<h4 class="f16 text-bold text-center">国家开放大学(中央广播电视大学)学士学位审批表</h4>
										<div class="row pad-t15">
											<div class="col-sm-8 col-sm-offset-2">
												<table class="table table-bordered vertical-mid text-center" style="table-layout: fixed;">
													<tbody>
														<tr>
															<th>姓名</th>
															<td>${info.gjtStudentInfo.xm}</td>

															<th>性别</th>
															<td><dic:getLabel typeCode="Sex" code="${info.gjtStudentInfo.xbm }" /></td>
															<td rowspan="3">
																<c:if test="${not empty(info.gjtStudentInfo.zp)}">
																	<img src="${info.gjtStudentInfo.zp }" width="100" height="120">
																</c:if>
																<c:if test="${empty(info.gjtStudentInfo.zp)}">
																	<img src="${css}/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" width="100" height="120">
																</c:if>
															</td>
														</tr>
														<tr>
															<th>身份证号</th>
															<td colspan="3">${info.gjtStudentInfo.sfzh }</td>
														</tr>
														<tr>
															<th>出生年日</th>
															<td colspan="3">${info.gjtStudentInfo.csrq}</td>
														</tr>
														<tr>
															<th>学号（全码）</th>
															<td>${info.gjtStudentInfo.xh}</td>

															<th>电子注册号</th>
															<td colspan="2">51161</td>
														</tr>
														<tr>
															<th>毕业时间</th>
															<td colspan="4">2017 年 一 月（ 春季 ）</td>
														</tr>
														<tr>
															<td colspan="5">个人申请</td>
														</tr>
														<tr>
															<td colspan="5">
																<div class="text-left pad">${info.applyContent }</div>
															</td>
														</tr>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
								<c:if test="${info.auditState==0 && lastRecord.auditState==0&&lastRecord.auditOperatorRole==auditRole}">
									<div class="panel panel-default margin-bottom-none flat">
										<input type="hidden" id="recordId" value="${lastRecord.flowRecordId}" />
										<div class="panel-heading">
											<h3 class="panel-title text-bold">
												<span class="margin-r-5">审批</span>
											</h3>
										</div>
										<div class="panel-body text-center">
											<textarea class="form-control" id="content" rows="6" placeholder="请审核学生上传的学位资料是否符合要求，并填写审核评语"></textarea>
										</div>
										<div class="panel-footer text-right">
											<button type="button" class="btn min-width-90px btn-warning margin_r10 btn-save-edit audit" onclick="saveRecord(2)">审核不通过</button>
											<button type="button" class="btn min-width-90px btn-success" data-role="btn-pass" onclick="saveRecord(1)" >审核通过</button>
										</div>
									</div>
								</c:if>
								

							</div>
						</div>
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
	<script type="text/javascript">
/*********'图片放大'**********/
;(function($){
  //图片放大
  $('<link/>',{
    rel:"stylesheet",
    type:"text/css",
    href:'http://css.gzedu.com/common/js/fancybox/jquery.fancybox.css',
    'data-id':'require-css'
  }).appendTo($('head'));
  $.getScript('http://css.gzedu.com/common/js/fancybox/jquery.fancybox.pack.js',function(){

    $(".light-box").on('click', function(event) {
      var imgUrl=$(this).data('large-img');

      $.fancybox({
        'type'      : 'image',
        'href'      :imgUrl
        
      });
    });
  });
})(jQuery);

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
    cancelCss:'btn btn-default min-width-90px',
    ok:false
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

//必修课程平均成绩详情
$('[data-role="more-1"]').click(function(event) {
  event.preventDefault();
  var _this=this;
  $.mydialog({
    id:'more-1',
    width:$(window).width(),
    height:$(window).height(),
    zIndex:11000,
    content: 'url:'+$(this).attr('href')
  });
});

//学位英语水平考试审核
$('[data-role="audit"]').click(function(event) {
  event.preventDefault();
    $.mydialog({
    id:'audit',
    width:600,
    height:600,
    zIndex:11000,
    content: 'url:'+$(this).attr('href')
  });
});

//学位英语水平考试审核详情
$('[data-role="more-2"]').click(function(event) {
  event.preventDefault();
    $.mydialog({
    id:'more-2',
    width:600,
    height:500,
    zIndex:11000,
    content: 'url:'+$(this).attr('href')
  });
});


//学位论文指南学分费用缴费记录
$('[data-role="more-3"]').click(function(event) {
  event.preventDefault();
    $.mydialog({
    id:'more-3',
    width:600,
    height:400,
    zIndex:11000,
    content: 'url:'+$(this).attr('href')
  });
});

function saveRecord(state){
	if($.trim($('#content').val())==''){
		alert('请填写审核评语');
		$('#content').focus();
		return;
	}
	$.post('${ctx}/graduation/degreemanager/saveFlowRecord',{
		applyId:'${param.applyId}',
		content:$('#content').val(),
		recordId:$('#recordId').val(),
		state:state
	},function(data){
		if(data.successful){
			location.href='${ctx}/graduation/degreemanager/degreeApplyList';
		}else{
			alert(data.message);
		}
	},'json');
    
}
</script>
</body>
</html>
