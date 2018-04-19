<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>管理系统-学籍信息</title>
    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

    <script type="text/javascript">
        $(function() {
            $('.updatePwd').click(function(){
                var id=$(this).attr('val');
                $.confirm({
                    title: '提示',
                    content: '确认重置密码？',
                    confirmButton: '确认',
                    icon: 'fa fa-warning',
                    cancelButton: '取消',
                    confirmButtonClass: 'btn-primary',
                    closeIcon: true,
                    closeIconClass: 'fa fa-close',
                    confirm: function () {
                        $.post("updatePwd.html",{id:id},function(data){
                            if(data.successful){
                                showSueccess(data);
                            }else{
                                $.alert({
                                    title: '失败',
                                    icon: 'fa fa-exclamation-circle',
                                    confirmButtonClass: 'btn-primary',
                                    content: '网络异常！',
                                    confirmButton: '确认',
                                    confirm:function(){
                                        showFail(data);
                                    }
                                });
                            }
                        },"json");
                    }
                });
            });

            $('.enteringSignup').click(function(){
                var $this = $(this);
                var id=$(this).attr('val');
                $.confirm({
                    title: '提示',
                    content: '确认录入学籍？',
                    confirmButton: '确认',
                    icon: 'fa fa-warning',
                    cancelButton: '取消',
                    confirmButtonClass: 'btn-primary',
                    closeIcon: true,
                    closeIconClass: 'fa fa-close',
                    confirm: function () {
                        $.post("enteringSignup.html",{studentId:id},function(data){
                            if(data.successful){
                                showSueccess(data);
                                <c:if test="${param.search_EQ_xjzt == '3'}">
                                $this.parent().parent().remove();
                                </c:if>
                            }else{
                                $.alert({
                                    title: '失败',
                                    icon: 'fa fa-exclamation-circle',
                                    confirmButtonClass: 'btn-primary',
                                    content: '网络异常！',
                                    confirmButton: '确认',
                                    confirm:function(){
                                        showFail(data);
                                    }
                                });
                            }
                        },"json");
                    }
                });
            });


            $("[data-role='import']").click(function(event) {
                $.mydialog({
                    id:'import',
                    width:600,
                    height:415,
                    zIndex:11000,
                    content: 'url:toImport'
                });
            });

            $("[data-role='export']").click(function(event) {
                $("#schoolId").val("");
                $("#listForm").attr("target","_blank").attr("action","exportStuInfo").submit();
                $("#listForm").attr("target","").attr("action","list.html")
            });

            $(".search").click(function(event) {
                event.preventDefault;
                $("#schoolId").val("");
                $("#listForm").submit();
            });

            $("[data-role='outZip']").click(function (event) {
                event.preventDefault;
                var schoolId = "${(not empty param['search_EQ_gjtSchoolInfo.id']) ? param['search_EQ_gjtSchoolInfo.id'] : defaultXxId}";
                $("#schoolId").val(schoolId);
                $("#listForm").attr("target","_blank").attr("action","exportNotRegInfo").submit();
                $("#listForm").attr("target","").attr("action","list.html");
            });    
            //正式生打印
    		$("[data-role='formal_print']").click(function(event) {
    			event.preventDefault();
    			$.mydialog({
    			  id:'formal_print',
    			  width:600,
    			  height:500,
    			  zIndex:11000,
    			  content: 'url:'+$(this).attr('url')
    			});
    		});        
      //跟读生打印
		$("[data-role='follow_print']").click(function(event) {
			event.preventDefault();
			$.mydialog({
			  id:'follow_print',
			  width:540,
			  height:450,
			  zIndex:11000,
			  content: 'url:'+$(this).attr('url')
			});
		});
		//转专业
	    $("[data-role='major-change']").click(function(event) {
	    	event.preventDefault();
	    	$.mydialog({
	    	  id:'batch',
	    	  width:650,
	    	  height:400,
	    	  zIndex:11000,
	    	  content: 'url:'+$(this).attr('href')
	    	});
	    });
		
	  	//转学期
	    $("[data-role='grade-change']").click(function(event) {
	    	event.preventDefault();
	    	$.mydialog({
	    	  id:'batch',
	    	  width:750,
	    	  height:400,
	    	  zIndex:11000,
	    	  content: 'url:'+$(this).attr('href')
	    	});
	    });
    });
        
        function choiceXJ(flag){
            $("#xjzt").val(flag);
            $("#listForm").attr("target","").attr("action","list.html").submit();
        }
    </script>
</head>
<body class="inner-page-body">
<section class="content-header clearfix">
    <ol class="breadcrumb oh">
        <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="#">学籍管理</a></li>
        <li class="active">学籍信息</li>
    </ol>
</section>

<section class="content" data-id="0">
    <form class="form-horizontal" id="listForm" action="list.html" method="post">
        <input id="xjzt" type="hidden" name="search_EQ_xjzt" value="${param.search_EQ_xjzt}">
        <input id="schoolId" type="hidden" name="search_schoolId">
        <div class="box">
            <div class="box-body">
                <div class="row pad-t15">
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">学号</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="search_EQ_xh" value="${param.search_EQ_xh }">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">姓名</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="search_LIKE_xm" value="${param.search_LIKE_xm }">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">学习中心</label>
                            <div class="col-sm-9">
                                <select name="search_EQ_studyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${studyCenterMap}" var="map">
                                        <option value="${map.key}"  <c:if test="${map.key==param.search_EQ_studyId}">selected='selected'</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">专业名称</label>
                            <div class="col-sm-9">
                                <select name="search_EQ_specialtyId" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${specialtyMap}" var="map">
                                        <option value="${map.key}" <c:if test="${map.key==param.search_EQ_specialtyId}">selected='selected'</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">学期</label>
                            <div class="col-sm-9">
                                <select  name="search_EQ_viewStudentInfo.gradeId"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${gradeMap}" var="map">
                                        <option value="${map.key}" <c:if test="${map.key==((not empty param['search_EQ_viewStudentInfo.gradeId']) ? param['search_EQ_viewStudentInfo.gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">层次</label>
                            <div class="col-sm-9">
                                <select  name="search_EQ_pycc"  class="selectpicker show-tick form-control"	data-size="5" data-live-search="true">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${pyccMap}" var="map">
                                        <option value="${map.key}" <c:if test="${map.key==param['search_EQ_pycc']}">selected='selected'</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="more-search" class="row collapse <c:if test="${not empty param.search_EQ_sfzh || not empty param.search_EQ_userType || not empty param.search_LIKE_scCo || not empty param.search_EQ_xbm }">in</c:if>">
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">身份证号</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="search_EQ_sfzh" value="${param.search_EQ_sfzh }">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">学员类型</label>
                            <div class="col-sm-9">
                                <c:if test="${sessionScope.current_user.loginAccount=='admin'}">
                                <dic:selectBox name="search_EQ_userType" typeCode="USER_TYPE" code="${param.search_EQ_userType }" otherAttrs='class="selectpicker show-tick form-control" data-size="5" data-live-search="true"' />
                                </c:if>
                                <c:if test="${sessionScope.current_user.loginAccount!='admin'}">
                                <dic:selectBox name="search_EQ_userType" typeCode="USER_TYPE" code="${param.search_EQ_userType }" excludes="21,31" otherAttrs='class="selectpicker show-tick form-control" data-size="10" data-live-search="true"' />
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">工作单位</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="search_LIKE_scCo" value="${param.search_LIKE_scCo }">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">性别</label>
                            <div class="col-sm-9">
                                <dic:selectBox typeCode="Sex" name="search_EQ_xbm" code="${param.search_EQ_xbm}" otherAttrs='class="selectpicker show-tick form-control"' />
                            </div>
                        </div>
                    </div>
                </div>

            </div><!-- /.box-body -->
            <div class="box-footer text-right">
                <button type="submit" class="btn min-width-90px btn-primary margin_r15 search">搜索</button>
                <button type="button" class="btn min-width-90px btn-default margin_r15 btn-reset">重置</button>
                <div class="search-more-in no-float inline-block" data-toggle="collapse" data-target="#more-search">高级搜索<i class="fa fa-fw fa-angle-up <c:if test="${not empty param.search_EQ_sfzh || not empty param.search_EQ_userType || not empty param.search_LIKE_scCo || not empty param.search_EQ_xbm }">fa-angle-down</c:if>"></i> </div>
            </div><!-- /.box-footer-->
        </div>

        <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
        <div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>

        <div class="box margin-bottom-none">
            <div class="box-header with-border">
                <h3 class="box-title pad-t5">信息列表</h3>
                <div class="pull-right no-margin">
                    <shiro:hasPermission name="/edumanage/roll/list$exportNotRegInfo">
                        <a  class="btn btn-default btn-sm" target="_blank" data-role="outZip">
                            <i class="fa fa-fw fa-download"></i> 下载未注册学员学籍资料
                        </a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/edumanage/roll/list$importStuInfo">
                        <a class="btn btn-default btn-sm margin_l10" data-role="import"><i class="fa fa-fw fa-sign-in"></i> 批量导入修改学籍信息</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/edumanage/roll/list$exportStuInfo">
                        <a class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 按条件批量导出</a>
                    </shiro:hasPermission>
                </div>
            </div>
            <div class="box-body">
                <div class="filter-tabs clearfix">
                    <ul class="list-unstyled">
                        <li <c:if test="${empty param.search_EQ_xjzt}">class="actived"</c:if> value="" onclick="choiceXJ('')">全部(${countXjztMap['']})</li>
                        <c:forEach items="${rollTypeMap}" var="map">
                            <li <c:if test="${param.search_EQ_xjzt == map.key}">class="actived"</c:if> value="${map.key}" onclick="choiceXJ('${map.key}')">${map.value}(${not empty countXjztMap[map.key] ? countXjztMap[map.key] : 0})</li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="table-responsive" style="overflow: hidden">
                    <table class="table table-bordered table-striped vertical-mid table-font">
                        <thead>
                        <tr>
                            <!-- <th><input type="checkbox" class="select-all" id="selectAll"></th> -->
                            <th width="70" class="text-center">照片</th>
                            <th width="20%" class="text-center">个人信息</th>
                            <th width="20%" class="text-center">报读信息</th>
                            <th class="text-center">学习中心</th>
                            <th width="100" class="text-center">
                                学籍状态
                            </th>
                            <th width="100" class="text-center">
                                资料审批
                            </th>
                            <th width="100" class="text-center">
                                学员类型
                            </th>
                            <th class="text-center" width="14%">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${pageInfo.content }" var="student">
                            <tr>
                                    <%-- <td><input type="checkbox" value="${student.studentId }" name="ids" class="checkbox"></td> --%>
                                <td class="text-center">
                                    <img src="${not empty student.avatar ? student.avatar : ctx.concat('/static/dist/img/images/no-img.png')}" class="img-circle" width="50" height="50" onerror="this.src='${ctx}/static/dist/img/images/no-img.png'"/>
                                    <c:if test="${empty student.avatar}">
                                        <p class="gray9">未上传</p>
                                    </c:if>
                                </td>
                                <td>
		                                    姓名：${student.xm}<br/>
		                                    性别：<dic:getLabel typeCode="Sex" code="${student.xbm }" /><br/>
		                                    学号：${student.xh}<br/>
		                            <shiro:hasPermission name="/personal/index$privacyJurisdiction">
		                             手机号：${student.sjh}<br/>
		                                    身份证：${student.sfzh}
		                            </shiro:hasPermission>
                                </td>
                                <td>
                                    层次：<dic:getLabel typeCode="TrainingLevel" code="${student.pycc }" /><br/>
                                    学期：${student.gradeName}<br/>
                                    专业：${student.specialtyName}
                                </td>
                                <td class="text-center">
                                        ${student.xxzxName} <br>

                                </td>
                                <td class="text-center ${student.xjzt=='3'?'text-orange':''}">
                                    <dic:getLabel typeCode="StudentNumberStatus" code="${student.xjzt }" />
                                </td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${student.signupAuditState=='1'}"><span class="text-green">审核通过</span></c:when>
                                        <c:when test="${student.signupAuditState=='0'}"><span class="text-red">审核不通过<c:if test="${not empty student.flowAuditOperatorRole}"><br/>(${student.flowAuditOperatorRole-1}/3)</span></c:if></span></c:when>
                                        <c:otherwise>
                                            <c:if test="${empty student.flowAuditOperatorRole}"><span class="text-orange">未审核</span></c:if>
                                            <c:if test="${not empty student.flowAuditOperatorRole}">
                                                <c:if test="${student.flowAuditOperatorRole==4}">
                                                    <span class="text-orange">待审核</span>
                                                </c:if>
                                                <c:if test="${student.flowAuditOperatorRole!=4}">
                                                    <span class="text-orange">审核中<br/>(${student.flowAuditOperatorRole-1}/3)</span>
                                                </c:if>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </td>  
                                <td class="text-center">
                                    <dic:getLabel typeCode="USER_TYPE" code="${student.userType}" />
                                </td>
                                <td class="text-center">
                                        <%--<c:if test="${student.xjzt=='3'}">
                                            <a href="#" class="operion-item enteringSignup" val="${student.studentId}" data-toggle="tooltip" title="录入学籍"><i class="fa fa-fw fa-shxxjl"></i></a>
                                        </c:if>--%>
                                    <shiro:hasPermission name="/edumanage/roll/list$view">
                                        <a href="view/${student.studentId}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                                    </shiro:hasPermission>
                                    <shiro:hasPermission name="/edumanage/roll/list$simulation">
                                        <a href="simulation.html?id=${student.studentId}" class="operion-item operion-login" data-toggle="tooltip" title="模拟登录" target="_blank"><i class="fa fa-fw fa-simulated-login"></i></a>
                                    </shiro:hasPermission>
                                    <shiro:hasPermission name="/edumanage/roll/list$analogLogin">
                                         <a href="analogLogin?id=${student.studentId}" class="operion-item operion-login" data-toggle="tooltip" title="模拟登录个人中心" target="_blank" data-role="data-analogLogin"><i class="fa fa-fw fa-share"></i></a>
                                    </shiro:hasPermission>
                                    <shiro:hasPermission name="/edumanage/roll/list$outStuWord">
                                        <a href="outStuWord/${student.studentId}" class="operion-item operion-login" data-toggle="tooltip" title="报名表下载" target="_blank"><i class="fa fa-fw fa-download"></i></a>
                                    </shiro:hasPermission>
                                    <shiro:hasPermission name="/edumanage/roll/list$reset">
                                        <a href="#" class="operion-item operion-reset updatePwd" val="${student.accountId}" data-toggle="tooltip" title="重置密码"><i class="fa fa-fw fa-lock"></i></a>
                                    </shiro:hasPermission>
                                    <shiro:hasPermission name="/edumanage/roll/list$studentCardPrint">
                                    <c:if test="${student.userType=='11'}">
                                       <a href="javascript:void(0);" url="${ctx }/edumanage/roll/studentCardPrint/${student.studentId}/${student.userType}" class="operion-item" title="正式学员打印" data-role="formal_print"><i class="fa fa-fw fa-print"></i></a>
                                    </c:if>
                                    <c:if test="${student.userType=='12'}">
                                       <a href="javascript:void(0);" url="${ctx }/edumanage/roll/studentCardPrint/${student.studentId}/${student.userType}" class="operion-item" title="跟读学员打印" data-role="follow_print"><i class="fa fa-fw fa-print"></i></a>
                                    </c:if>
                                    </shiro:hasPermission>
                                    <shiro:hasPermission name="/edumanage/roll/list$toForwordStudentMajor">
                                    <a href="${ctx }/edumanage/roll/toForwordStudentMajor/${student.studentId}" class="operion-item operion-login" data-toggle="tooltip" title="转专业" data-role="major-change"><i class="fa fa-fw fa-sign-out"></i></a>                               
                                	</shiro:hasPermission>
                                	<shiro:hasPermission name="/edumanage/roll/list$toForwordStudentGradeId">
                                	 <a href="${ctx }/edumanage/roll/toForwordStudentGradeId/${student.studentId}" class="operion-item operion-login" data-toggle="tooltip" title="转学期" data-role="grade-change"><i class="fa fa-fw fa-sign-out"></i></a>
                                	</shiro:hasPermission>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <tags:pagination page="${pageInfo}" paginationSize="5" />
                </div>
            </div>
        </div>
    </form>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">
    //高级搜索
    $("#more-search").on('shown.bs.collapse', function(event) {
        event.preventDefault();
        $('[data-target="#more-search"] .fa').removeClass('fa-angle-up').addClass('fa-angle-down');
    }).on('hidden.bs.collapse', function(event) {
        event.preventDefault();
        $('[data-target="#more-search"] .fa').removeClass('fa-angle-down').addClass('fa-angle-up');
    });
</script>
</body>
</html>
