<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>招生管理 | 报读信息</title>
	<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
	<ol class="breadcrumb">
		<li><a href="javascript:;"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="javascript:;">招生管理</a></li>
		<li class="active">报读信息</li>
	</ol>
</section>
<section class="content">
	<%--<div class="nav-tabs-custom no-margin">
		<ul class="nav nav-tabs nav-tabs-lg">
			
			<li class="active"><a href="#" data-toggle="tab" onclick="changType('1')">报读信息</a></li>
			<li><a href="#" data-toggle="tab" onclick="changType('2')">报读统计</a></li>
			-->
		<!-- 
			<li class="active"><a href="${ctx}/recruitmanage/signup/list" >报读信息</a></li>
			<li ><a href="${ctx}/recruitmanage/signup/statistics"  >报读统计</a></li>
		 
		</ul>--%>
		<form id="listForm" class="form-horizontal">
		<!--  <div class="tab-content" id="scoreContent">
			<div class="tab-pane active" id="tab_top_1">-->
				<div class="box ">
					<div class="box-body">
					
							<div class="row pad-t15">
							
								<div class="col-sm-4">
									<div class="form-group">
										<label class="control-label col-sm-3 text-nowrap">学号</label>
										<div class="col-sm-9">
											<input type="text" class="form-control" name="XH" value="${param.XH }">
										</div>
									</div>
								</div>
								
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
										<label class="control-label col-sm-3 text-nowrap">学习中心</label>
										<div class="col-sm-9">
											<select class="form-control" name="STUDY_ID" id="study_id" data-size="5" data-live-search="true">
												<option value="" selected="selected">请选择</option>
												<c:forEach items="${schoolInfoMap}" var="map">
													<option value="${map.key}"  <c:if test="${map.key==param.STUDY_ID}">selected='selected'</c:if>>${map.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
								
								<div class="col-sm-4">
									<div class="form-group">
										<label class="control-label col-sm-3 text-nowrap">专业</label>
										<div class="col-sm-9">
											<select class="form-control" name="SPECIALTY_ID" id="specialty_id" data-size="5" data-live-search="true">
												<option value="">请选择</option>
												<c:forEach items="${specialtyMap}" var="map">
													<option value="${map.key}" <c:if test="${param.SPECIALTY_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
								
								<div class="col-sm-4">
									<div class="form-group">
										<label class="control-label col-sm-3 text-nowrap">学期</label>
										<div class="col-sm-9">
											<select class="form-control" name="GRADE_ID" id="grade_id" data-size="5" data-live-search="true">
												<option value="" selected="selected">请选择</option>
												<c:forEach items="${gradeMap}" var="map">
													<option value="${map.key}" <c:if test="${map.key==((not empty param['GRADE_ID']) ? param['GRADE_ID'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
								
								<div class="col-sm-4">
									<div class="form-group">
										<label class="control-label col-sm-3 text-nowrap">层次</label>
										<div class="col-sm-9">
											<select class="form-control" name="PYCC_ID" id="pycc_id" data-size="5" data-live-search="true">
												<option value="" selected="selected">请选择</option>
												<c:forEach items="${pyccMap}" var="map">
													<option value="${map.key}" <c:if test="${map.key==param.PYCC_ID }">selected='selected'</c:if>>${map.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
					</div>
					<div class="box-footer text-right">
						<div class="pull-right">
							<button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
						</div>
						<div class="pull-right margin_r15">
							<button type="submit" class="btn min-width-90px btn-primary">搜索</button>
						</div>
					</div>
				</div>
				<div class="box margin-bottom-none">
					<div class="box-header with-border">
						<!-- <h3 class="box-title pad-t5">信息列表</h3> -->
						<div class="pull-right">
							<shiro:hasPermission name="/recruitmanage/signup/list$exportSignup">
								<a href="${ctx}/recruitmanage/signup/exportView" data-role="view-download" data-container="body" target="_blank" class="btn btn-default btn-outport">
								<i class="fa fa-fw fa-sign-out"></i>导出报名统计表</a>
							</shiro:hasPermission>
						</div>
					</div>
					<div class="box-body">
					<!-- 
						<div class="filter-tabs clearfix">
							<ul class="list-unstyled">
								<li class="actived">待提交(131)</li>
								<li>待审核(40)</li>
								<li>审核通过(40)</li>
								<li>审核不通过(40)</li>
							</ul>
						</div>
					 -->
						<div>
							<table class="table table-bordered table-striped vertical-mid table-font">
								<thead>
									<tr>
										<th width="70" class="text-center">照片</th>
										<th class="text-center">个人信息</th>
					              		<th class="text-center">报读信息</th>
					              		<th class="text-center">报读来源</th>
						              	<th class="text-center">学习中心</th>
						              	<th class="no-padding" data-role="menu-th">
						              		<div class="dropdown custom-dropdown">
						              			<a href="javascript:;" class="dropdown-toggle text-nowrap text-center" role="button">
						              				资料审批
						              				<span class="caret"></span>
						              			</a>
							              		<div class="dropdown-menu">
							              			<ul class="list-unstyled">
							              				<li>
							              					<i class="fa fa-fw"></i>审核不通过（${COUNT_MAP.AUDIT_STATE_0 }）
							              					<input type="checkbox" name="AUDIT_STATE" value="0"/>
							              				</li>
							              				<li>
							              					<i class="fa fa-fw"></i>审核通过（${COUNT_MAP.AUDIT_STATE_1 }）
							              					<input type="checkbox" name="AUDIT_STATE" value="1"/>
							              				</li>
							              				<li>
							              					 <i class="fa fa-fw"></i>审核中（${COUNT_MAP.AUDIT_STATE_2 }）
							              					 <input type="checkbox" name="AUDIT_STATE" value="2"/>
							              				</li>
							              				<li>
							              					<i class="fa fa-fw"></i>待审核（${COUNT_MAP.AUDIT_STATE_3 }）
							              					<input type="checkbox" name="AUDIT_STATE" value="3"/>
							              				</li>
							              				<li>
							              					<i class="fa fa-fw"></i>未提交（${COUNT_MAP.AUDIT_STATE_4 }）
							              					<input type="checkbox" name="AUDIT_STATE" value="4"/>
							              				</li>
							              			</ul>
							              			<div class="text-center margin_t5">
							              				<button class="btn btn-primary btn-xs" data-role="sure-btn">确定</button>
							              				<!-- 
							              				<button class="btn btn-default btn-xs margin_l10" data-role="close-btn">关闭</button>
							              				 -->
							              			</div>
							              		</div>
						              		</div>
						              		
						              	</th>
						              	<th class="no-padding" data-role="menu-th">
						              		<div class="dropdown custom-dropdown">
						              			<a href="javascript:;" class="dropdown-toggle text-nowrap text-center" role="button">
						              				学籍状态
						              				<span class="caret"></span>
						              			</a>
						              			<div class="dropdown-menu">
							              			<ul class="list-unstyled">
							              				<li>
							              					<i class="fa fa-fw"></i>待注册（${COUNT_MAP.XJZT_STATE_1 }）
							              					<input type="checkbox" name="XJZT_STATE" value="3"/>
							              				</li>
							              				<li>
							              					<i class="fa fa-fw"></i>注册中（${COUNT_MAP.XJZT_STATE_2 }）
							              					<input type="checkbox" name="XJZT_STATE" value="0"/>
							              				</li>
							              				<li>
							              					 <i class="fa fa-fw"></i>在籍（${COUNT_MAP.XJZT_STATE_3 }）
							              					 <input type="checkbox" name="XJZT_STATE" value="2"/>
							              				</li>
							              				<li>
							              					<i class="fa fa-fw"></i>退学（${COUNT_MAP.XJZT_STATE_4 }）
							              					<input type="checkbox" name="XJZT_STATE" value="5"/>
							              				</li>
							              				<li>
							              					<i class="fa fa-fw"></i>休学（${COUNT_MAP.XJZT_STATE_5 }）
							              					<input type="checkbox" name="XJZT_STATE" value="4"/>
							              				</li>
							              				<li>
							              					<i class="fa fa-fw"></i>转学（${COUNT_MAP.XJZT_STATE_6 }）
							              					<input type="checkbox" name="XJZT_STATE" value="10"/>
							              				</li>
							              				<li>
							              					<i class="fa fa-fw"></i>毕业（${COUNT_MAP.XJZT_STATE_7 }）
							              					<input type="checkbox" name="XJZT_STATE" value="8"/>
							              				</li>
							              			</ul>
							              			<div class="text-center margin_t5">
							              				<button class="btn btn-primary btn-xs" data-role="sure-btn">确定</button>
							              				<!-- 
							              				<button class="btn btn-default btn-xs margin_l10" data-role="close-btn1">关闭</button>
							              				 -->
							              			</div>
							              		</div>
						              		</div>
						              		
						              	</th>
						              	<th class="no-padding" data-role="menu-th">
						              		<div class="dropdown custom-dropdown">
						              			<a href="javascript:;" class="dropdown-toggle text-nowrap text-center" role="button">
						              				缴费状态
						              				<span class="caret"></span>
						              			</a>
						              			<div class="dropdown-menu">
							              			<ul class="list-unstyled">
														<%--
							              				<li>
							              					<i class="fa fa-fw"></i>未缴费（${COUNT_MAP.CHARGE_0 }）
							              					<input type="checkbox" name="CHARGE" value="0"/>
							              				</li>
							              				<li>
							              					<i class="fa fa-fw"></i>已缴费（${COUNT_MAP.CHARGE_1 }）
							              					<input type="checkbox" name="CHARGE" value="1"/>
							              				</li>
														<li>
															<i class="fa fa-fw"></i>已退费（${COUNT_MAP.CHARGE_2 }）
															<input type="checkbox" name="CHARGE" value="2"/>
														</li>
														--%>

														<li>
															<i class="fa fa-fw"></i>已全额缴费（${COUNT_MAP.CHARGE_0 }）
															<input type="checkbox" name="CHARGE" value="0"/>
														</li>
														<li>
															<i class="fa fa-fw"></i>已部分缴费（${COUNT_MAP.CHARGE_1 }）
															<input type="checkbox" name="CHARGE" value="1"/>
														</li>
														<li>
															<i class="fa fa-fw"></i>待缴费（${COUNT_MAP.CHARGE_2 }）
															<input type="checkbox" name="CHARGE" value="2"/>
														</li>
														<li>
															<i class="fa fa-fw"></i>已欠费（${COUNT_MAP.CHARGE_3 }）
															<input type="checkbox" name="CHARGE" value="3"/>
														</li>
							              			</ul>
							              			<div class="text-center margin_t5">
							              				<button class="btn btn-primary btn-xs" data-role="sure-btn">确定</button>
							              				<!-- 
							              				<button class="btn btn-default btn-xs margin_l10" data-role="close-btn">关闭</button>
							              				 -->
							              			</div>
							              		</div>
						              		</div>
						              		
						              	</th>
						              	<th class="text-center">操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${pageInfo.content }" var="entity">
										<tr>
											<td class="text-center">
												<img src="${not empty entity.AVATAR ? entity.AVATAR : ctx.concat('/static/dist/img/images/no-img.png')}" class="img-circle" width="50" height="50" onerror="this.src='${ctx}/static/dist/img/images/no-img.png'"/>
												<c:if test="${empty entity.AVATAR}">
													<p class="gray9">未上传</p>
												</c:if>
											</td>
											<td>
												<div class="text-left">
													姓名：${entity.XM } （<dic:getLabel typeCode="Sex" code="${entity.XBM}" />）<br>
													学号：${entity.XH } <br>
													<shiro:hasPermission name="/personal/index$privacyJurisdiction">
														手机：${entity.SJH } <br>
														身份证：${entity.SFZH }
													</shiro:hasPermission>
												</div>
											</td>
											<td width="200px">
						          				<div class="text-left">
						          					层次：${entity.PYCC_NAME } <br>
						            				学期：${entity.GRADE_NAME } <br>
						            				专业：${entity.ZYMC } <br>
						            				时间：${entity.CREATED_DT } 
						          				</div>
					          				</td>
					          				<td class="text-center">
												<c:choose>
													<c:when test="${entity.AUDIT_SOURCE eq 'null' or entity.AUDIT_SOURCE eq '--'}">
														--
													</c:when>
													<c:otherwise>${entity.AUDIT_SOURCE}</c:otherwise>
												</c:choose>
											</td class="text-center">
					          				<td class="text-center">${entity.SC_NAME }</td>
					          				<td class="text-center">
												<c:if test="${entity.AUDIT_STATE eq '审核通过'}">
													<span class="text-green">${entity.AUDIT_STATE }</span>
												</c:if>
												<c:if test="${entity.AUDIT_STATE eq '审核不通过'}">
													<span class="text-red">${entity.AUDIT_STATE }</span>
												</c:if>
												<c:if test="${(entity.AUDIT_STATE eq '审核中') || (entity.AUDIT_STATE eq '待审核') || (entity.AUDIT_STATE eq '未审核')}">
													<span class="text-orange">${entity.AUDIT_STATE }</span>
												</c:if>
                                                <c:if test="${entity.AUDIT_STATE eq '--'}">
                                                    ${entity.AUDIT_STATE }
                                                </c:if>
											</td>
					          				<td class="text-center"> ${entity.XJZT }</td>
					          				<td class="text-center">
												<c:if test="${(entity.CHARGE eq '已全额缴费') || (entity.CHARGE eq '已部分缴费')}">
													<span class="text-green">${entity.CHARGE }<br></span>
												</c:if>
												<c:if test="${entity.CHARGE eq '待缴费'}">
													<span class="text-orange">${entity.CHARGE }<br></span>
												</c:if>
												<c:if test="${entity.CHARGE eq '已欠费'}">
													<span class="text-red">${entity.CHARGE }<br></span>
												</c:if>
												<c:if test="${entity.CHARGE eq '--'}">
													${entity.CHARGE }
												</c:if>
												<c:if test="${entity.CHARGE_FLG eq '1'}">
													<c:if test="${not empty entity.CHARGE_DESCRIB }">
														<small class="gray9">（${entity.CHARGE_DESCRIB}）</small>
													</c:if>
												</c:if>
											</td>
					          				<td>
					            				<a href="${ctx}/recruitmanage/signup/querySignUpDetail?STUDENT_ID=${entity.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="查看详情" data-role="view-more" data-container="body"><i class="fa fa-fw fa-view-more"></i></a>
					            			</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<tags:pagination page="${pageInfo}" paginationSize="5" />
						</div>
						
					</div>
				</div>
				
			<!--  </div>
		</div>-->
		</form>
	<!-- </div> -->
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<script src="${css}/ouchgzee_com/platform/xllms_css/plugins/select2/select2.full.min.js"></script> 
<script type="text/javascript">
$(function() {
	$("#study_id").selectpicker();
	$("#specialty_id").selectpicker();
	$("#pycc_id").selectpicker();
	$("#grade_id").selectpicker();
	
	/**
	 $("[data-role='view-more']").click(function(event) {
	        event.preventDefault();
	        $.mydialog({
	        	id:'view-more',
				width:'100%',
				height:$(window).height()*0.98,
			    zIndex:11000,
			    showCloseIco:false,
			    iframeScroll:'yes',
			    onLoaded:function(){
			    	var $box=$('[data-id="'+this.id+'"]');
			    	$box.css('padding-right',0);
			    },
				content: 'url:'+$(this).attr("href")
	        });
	    });
	*/
	
	$("[data-role='view-download']").click(function(event){
		event.preventDefault();
		$.mydialog({
        	id:'view-more',
			width:'500',
			height:'600',
		    zIndex:11000,
		    iframeScroll:'yes',
		    onLoaded:function(){
		    	var $box=$('[data-id="'+this.id+'"]');
		    	$box.css('padding-right',0);
		    },
			content: 'url:'+$(this).attr("href")
        });
	});
})

//Initialize Select2 Elements
$(".select2").select2();
$(".filter-tabs li").click(function(event) {
	if($(this).hasClass('actived')){
		$(this).removeClass('actived');
	}
	else{
		$(this).addClass('actived');
	}
});

$(".custom-dropdown")
.on('click', "[data-role='sure-btn']", function(event) {
  var $box=$(this).closest('.custom-dropdown');
  $(this).closest('th').removeClass('on');
  $box.find("li").removeClass('actived');
  //$box.find(":checkbox").attr("checked",false);
  var audit_state = [];
  $('input[name="AUDIT_STATE"]:checked').each(function(event){
	  audit_state.push($(this).val());
  });
  
  var xjzt_state = [];
  $('input[name="XJZT_STATE"]:checked').each(function(event){
	  xjzt_state.push($(this).val());
  });
  
  var charge = [];
  $('input[name="CHARGE"]:checked').each(function(event){
	  charge.push($(this).val());
  });
  
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
.on('mouseenter', function(event) {
  $(this).addClass('on');
})
.on('mouseleave', function(event) {
  if(!$(this).children('.custom-dropdown').hasClass('open')){
    $(this).removeClass('on');
    //$(this).removeClass('actived').find(":checkbox").attr("checked",false);
  }
 // setTimeout(function(){
 //	  $('.custom-dropdown').find('li').removeClass('actived').find(":checkbox").attr("checked",false);
 // },50)
  
});
function changType(type) {
	if (type=="1") {
		window.location.href = "list";
	} else {
		window.location.href = "statistics";
	}
}

</script>
</body>
</html>