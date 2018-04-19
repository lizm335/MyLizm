<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<title>班主任平台</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="fixed skin-blue">
	<div class="wrapper">
		<!-- Left side column. contains the logo and sidebar -->
		<header class="main-header">
			<a href="${ctx}/home/manyClass" class="logo"> <!-- mini logo for sidebar mini 50x50 pixels --> <span class="logo-mini">
					<i class="fa fa-fw fa-tv"></i>
				</span> <!-- logo for regular state and mobile devices --> <span class="logo-lg">
					<i class="fa fa-fw fa-tv"></i>班主任平台
				</span>
			</a>
			<nav class="navbar navbar-static-top" role="navigation">
				<div class="navbar-custom-menu">
					<ul class="nav navbar-nav">
						<li class="first-level-wrap">
							<div class="dropdown operion-more-menu">
								<a class="dropdown-toggle" data-toggle="dropdown" href="#"> 更多 <i class="fa fa-fw fa-caret-down"></i>
								</a>

								<ul class="dropdown-menu">
									<li>
										<a role="menuitem" href="homepage.html">班级首页</a>
										<ul class="menu-temp hide">
											<li class="header">班级首页</li>
											<li class="treeview">
												<a href="homepage.html"> <i class="fa fa-home"></i> <span>班级首页</span></a>
											</li>
											<li class="treeview">
												<a href="即将开放页面.html"> <i class="fa fa-home"></i> <span>即将开放页面</span></a>
											</li>
										</ul>
									</li>
									<li>
										<a role="menuitem" href="#">教学教务服务</a>
										<ul class="menu-temp hide">
											<li class="header">教学教务服务</li>
											<li class="treeview">
												<a href="学员资料.html"> <i class="fa fa-file-text-o f16"></i> <span>学员管理</span></a>
											</li>
											<li class="treeview">
												<a href="学员学籍.html"> <i class="fa fa-credit-card f16"></i> <span>学籍信息</span></a>
											</li>
											<li class="treeview">
												<a href="教材管理.html"> <i class="fa fa-paste f16"></i> <span>教材管理</span></a>
											</li>
										</ul>
									</li>
									<li>
										<a role="menuitem" href="#">学习管理</a>
										<ul class="menu-temp hide">
											<li class="header">学习管理</li>
											<li class="treeview">
												<a href="学员考勤.html"> <i class="fa fa-calendar-check-o f16"></i> <span>学员考勤</span></a>
											</li>
											<li class="treeview">
												<a href="学员学情.html"> <i class="fa fa-signal f16"></i> <span>学员学情</span></a>
											</li>
											<li class="treeview">
												<a href="成绩查询.html"> <i class="fa fa-newspaper-o f14"></i> <span>成绩与学分</span></a>
											</li>
											<li class="treeview">
												<a href="考试管理.html"> <i class="fa fa-dating"></i> <span>考试预约</span></a>
											</li>
											<li class="treeview">
												<a href="毕业管理.html"> <i class="fa fa-photo f16"></i> <span>论文管理</span></a>
											</li>
										</ul>
									</li>
									<li>
										<a role="menuitem" href="#">班级服务</a>
										<ul class="menu-temp hide">
											<li class="header">班级服务</li>
											<li class="treeview">
												<a href="答疑管理.html"> <i class="fa fa-question-circle"></i> <span>答疑管理</span></a>
											</li>
											<li class="treeview">
												<a href="通知公告.html"> <i class="fa fa-envelope-o f16"></i> <span>通知公告</span></a>
											</li>


											<li class="treeview">
												<a href="督促提醒.html"> <i class="fa fa-invoices"></i> <span>督促提醒</span></a>
											</li>
											<li class="treeview">
												<a href="服务记录.html"> <i class="fa fa-service"></i> <span>服务记录</span></a>
											</li>
											<li class="treeview">
												<a href="班级活动.html"> <i class="fa fa-activity"></i> <span>班级活动</span></a>
											</li>
										</ul>
									</li>
								</ul>
							</div>
						</li>

						<li class="pull-right dropdown user user-menu">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown"> <c:choose>
									<c:when test="${not empty info.zp }">
										<img src="${info.zp}" class="user-image" alt="User Image" onerror="this.src='${ctx }/static/images/headImg04.png'" />
									</c:when>
									<c:otherwise>
										<img src="${ctx }/static/images/headImg04.png" class="user-image" alt="User Image" />
									</c:otherwise>
								</c:choose> <span class="hidden-xs"> ${info.xm} </span></a>
							<ul class="dropdown-menu">
								<%-- <li role="presentation">
									<a role="menuitem" tabindex="-1" href="${ctx}/home/personal/updateInfo" data-id='personal-info' data-page-role="single-page" title="个人资料管理">编辑个人资料</a>
								</li>
								<li role="presentation">
									<a href="${ctx}/home/class/report/list" data-page-role="single-page">工作日报周报</a>
								</li> --%>
								<li role="presentation">
									<a role="menuitem" tabindex="-1" href="${ctx}/logout">退出平台</a>
								</li>
							</ul>
						</li>
					</ul>
				</div>
				<ul class="first-level-menu">
					<li class="cur">班级列表</li>
					<li onclick="location.href='${ctx}/home/manyFeedSubject'">答疑辅导</li>
				</ul>
			</nav>
		</header>
		<div class="content-wrapper no-margin">
			<div class="scroll-box">
				<div class="pad">
					<div class="nav-tabs-custom no-shadow margin-bottom-none">
						<ul class="nav nav-tabs nav-tabs-lg">
							<li  <c:if test="${type eq '1' }">class="active"</c:if> >
								<a href="${ctx}/home/manyClass?type=1" >当前班级(${opCount})</a>
							</li>
							<li <c:if test="${type eq '0' }">class="active"</c:if>>
								<a href="${ctx}/home/manyClass?type=0" >已关闭班级(${clCount})</a>
							</li>
						</ul>

						<div class="tab-content">
							<div class="tab-pane active" id="tab_${type ==1?'1':'2' }">
								<form class="form-horizontal" id="listForm" action="${ctx }/home/manyClass">
								<input type="hidden" name="type" value="${type }">
								<div class="clearfix form-horizontal margin_t10">
									<div class="col-sm-3">
										<div class="form-group">
											<label class="col-sm-2 control-label text-left text-nowrap">年级</label>
											<div class="col-sm-10">
												<select name="search_EQ_gjtGrade.gradeId" class="form-control selectpicker show-tick"
                      								data-size="8" data-live-search="true" id="grade_id">
													<option value="">- 请选择 -</option>
													<c:forEach var="item" items="${gradeMap}">
														<option value="${item.key}" <c:if test="${param['search_EQ_gjtGrade.gradeId']==item.key}">selected</c:if>>${item.value}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
									<div class="col-sm-3">
										<div class="form-group">
											<label class="col-sm-4 control-label text-nowrap">班级名称</label>
											<div class="col-sm-8">
												<input type="text" name="search_LIKE_bjmc" class="form-control" value="${param.search_LIKE_bjmc}" placeholder="请输入班级" id="bjmc" />
											</div>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<button type="submit" class="btn btn-primary margin_r10">搜索</button>
											<c:if test="${type eq '1' }">
												<a role="button" href="${ctx}/home/class/classesConditionExportPage" data-value="" id="export_stud_condition"
												class="btn btn-default pull-right margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出学员学情</a> <a role="button"
												href="${ctx}/home/class/classesStudExportPage" data-value="" id="export_stud_info" class="btn btn-default pull-right" data-role="export"><i
												class="fa fa-fw fa-sign-out"></i> 导出学员信息</a>
											</c:if>
										</div>
									</div>
								</div>
								<div class="table-responsive">
									<table class="table table-bordered table-striped table-cell-ver-mid text-center">
										<thead>
											<tr>
												<th width="50"><input type="checkbox" data-role="sel-all"></th>
												<th width="10%">学期</th>
												<th width="">班级名称</th>
												<th width="10%">班级人数</th>
												<th width="">所属机构</th>
												<th width="10%">代办事项</th>
												<%--<th width="15%">创建时间</th>--%>
												<th width="15%">操作</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${not empty infos && infos.numberOfElements > 0}">
													<c:forEach items="${infos.content}" var="info">
														<c:if test="${not empty info}">
															<tr>
																<td><input type="checkbox" data-role="sel-item" name="class_id" value="${info.classId}"></td>
																<td>${info.gjtGrade.gradeName}</td>
																<td>${info.bjmc}</td>
																<td>${info.colStudentNum}/${info.bjrn}</td>
																<td>${info.gjtOrg.orgName}</td>
																<td>0</td>
																<td><a href="${ctx }/home/class/go/${info.classId}" class="f12 text-nowrap margin_r10" target="_blank">进入班级</a> 
																<c:if test="${type == 1 }">
																<a href="#" class="f12 text-nowrap" data-role="close-class" val="${info.classId}">关闭班级</a></td>
																</c:if>
																<c:if test="${type == 0 }">
																	<a href="#" class="f12 text-nowrap" data-role="re-open-class" val="${info.classId}">重开班级</a>
																</c:if>
															</tr>
														</c:if>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<tr>
														<td align="center" colspan="15">暂无数据</td>
													</tr>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
									<tags:pagination page="${infos}" paginationSize="10" />
								</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- ./浮动工具 -->
	<!-- IE8及以下的浏览器弹出提示窗口 -->
	<div id="dialog-message" title="温馨提示" style="display: none">
		<p style="padding: 20px 10px; margin: 0;">本管理系统支持IE9以上版本浏览器，为了获取更好的体验，建议 使用Firefox、Chrome、Opera等浏览器。</p>
	</div>

	<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
	<script type="text/javascript">
	 //页面高度自适应
    $(window).on('resize',function(){
        $(".scroll-box").height($(window).height()-$('.main-header').height())
    }).trigger('resize');

	$(document).ready(function () {
		var objId;
		// 关闭班级
		$('#tab_1').confirmation({
			selector: "[data-role='close-class']",
			html:true,
			placement:'top',
			content:'<div class="f12 gray9 margin_b10">关闭后的班级不纳入代办事项统计</div>',
			title:'<span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span><span class="f12">请确认是否关闭班级？</span>',
			btnOkClass    : 'btn btn-xs btn-primary',
			btnOkLabel    : '确认',
			btnOkIcon     : '',
			btnCancelClass  : 'btn btn-xs btn-default margin_l10',
			btnCancelLabel  : '取消',
			btnCancelIcon   : '',
			popContentWidth : 180,
			onShow:function(event,element){
				if($(element).attr('val') != null) {
					objId = $(element).attr('val');
				}
			},
			onConfirm:function(event,element){
				if(objId != null) {
					var id = objId;
					objId = null;
					$.post(ctx+'/home/class/close',{ids:id},function(data){
						if(data.successful){
							window.location.href=window.location.href;
						}else{
							$.alert({
								title: '失败',
								icon: 'fa fa-exclamation-circle',
								confirmButtonClass: 'btn-primary',
								content: '班级关闭失败！',
								confirmButton: '确认',
								confirm:function(){
									showFail(data);
								}
							});
						}
					},"json");
				}
			},
			onCancel:function(event, element){

			}
		});

		// 重开班级
		$('#tab_2').confirmation({
			selector: "[data-role='re-open-class']",
			html:true,
			placement:'top',
			content:'<div class="f12 gray9 margin_b10">重开后的班级将纳入代办事项统计</div>',
			title:'<span class="glyphicon glyphicon-exclamation-sign text-red margin_r10"></span><span class="f12">请确认是否重开班级？</span>',
			btnOkClass    : 'btn btn-xs btn-primary',
			btnOkLabel    : '确认',
			btnOkIcon     : '',
			btnCancelClass  : 'btn btn-xs btn-default margin_l10',
			btnCancelLabel  : '取消',
			btnCancelIcon   : '',
			popContentWidth : 180,
			onShow:function(event,element){
				if($(element).attr('val') != null) {
					objId = $(element).attr('val');
				}
			},
			onConfirm:function(event,element){
				if(objId != null) {
					var id = objId;
					objId = null;
					$.post(ctx+'/home/class/reopen',{ids:id},function(data){
						if(data.successful){
							window.location.href=window.location.href;
						}else{
							$.alert({
								title: '失败',
								icon: 'fa fa-exclamation-circle',
								confirmButtonClass: 'btn-primary',
								content: '班级重开失败！',
								confirmButton: '确认',
								confirm:function(){
									showFail(data);
								}
							});
						}
					},"json");
				}
			},
			onCancel:function(event, element){

			}
		});

		

	});
    //全选
    $('body').on('click','[data-role="sel-all"]',function(event) {
        var $cks=$(this).closest('table').find('[data-role="sel-item"]');
        if($(this).prop('checked')){
            $cks.prop('checked',true);
            $("#export_stud_condition").attr("data-value","all");
            $("#export_stud_info").attr("data-value","all");
        }
        else{
            $cks.prop('checked',false);
            $("#export_stud_condition").attr("data-value","");
            $("#export_stud_info").attr("data-value","");
        }
    });

    //导出
    $('[data-role="export"]').click(function(event) {
        event.preventDefault();
        var _this=this;
        var $cks=$('.table tbody').find('[data-role="sel-item"]:checked');
		var grade_id = $("#grade_id").val();
        var bjmc = $.trim($("#bjmc").val());

        var class_ids = "";
        if($("#export_stud_condition").attr("data-value")==="all"){
            class_ids = "all";
		}else {
        	class_ids = $cks.map(function(){return $(this).val()}).get().join(",");
		}

        if($cks.length<=0){
            $.alertDialog({
                id:'confirm',
                width:320,
                height:240,
                zIndex:11000,
                cancel:function(){//“确定”按钮的回调方法
                    $.closeDialog(this);
                },
                cancelLabel:'关闭',
                ok:false,
                content:[
                    '<div class="text-center">',
                    '<i class="fa fa-exclamation-circle text-orange vertical-mid margin_r10" style="font-size:54px;"></i>',
                    '<span class="f16 inline-block vertical-mid text-left">',
                    '请选择导出班级',
                    '</span>',

                    '</div>'].join('')
            });
        }
        else{
            $.mydialog({
                id:'export',
                width:600,
                height:415,
                zIndex:11000,
                content: 'url:'+$(_this).attr('href')+"?class_ids="+class_ids+"&grade_id="+grade_id+"&bjmc="+bjmc
            });
        }
    });	
	</script>
</body>
</html>

