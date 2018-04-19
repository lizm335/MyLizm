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
    </script>
</head>
<body class="inner-page-body">
<section class="content-header clearfix">
    <ol class="breadcrumb oh">
        <li><a href="#"><i class="fa fa-home"></i>首页</a></li>
        <li><a href="#">教学管理</a></li>
        <li class="active">任教管理</li>
    </ol>
</section>

<section class="content" data-id="0">
    <form class="form-horizontal" action="list" id="listForm">
    	<input type="hidden" name="STATE" id="STATE" value="${param.STATE }">
        <div class="box">
            <div class="box-body">
                <div class="row pad-t15">
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">账号</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="ZGH" value="${param.ZGH }">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">姓名</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" name="XM" value="${param.XM }">
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">任教机构</label>
                            <div class="col-sm-9">
                                <select name="CENTER_ID" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
                                    <option value="">请选择</option>
                                    <c:forEach items="${studyCenterMap}" var="map">
                                        <option value="${map.key}"  <c:if test="${map.key==param.CENTER_ID}">selected='selected'</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">任教课程</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="KCMC" id="kcmc" data-size="5" data-live-search="true">
									<option value="">请选择</option>
									<c:forEach items="${courseMap}" var="map">
										<option value="${map.key}" <c:if test="${param.KCMC eq map.key }">selected="selected"</c:if>>${map.value}</option>
									</c:forEach>
								</select>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">任教类型</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="TEACHER_TYPE" id="specialty_id" data-size="5" data-live-search="true">
				                	<option value="">请选择</option>
				                	<option value="1">主持教师</option>
				                	<option value="2">责任老师</option>
				                	<option value="3">辅导老师</option>
				                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- /.box-body -->
            <div class="box-footer text-right">
                <button type="submit" class="btn min-width-90px btn-primary margin_r15 search">搜索</button>
                <button type="button" class="btn min-width-90px btn-default margin_r15 btn-reset">重置</button>
            </div><!-- /.box-footer-->
        </div>

        <div class="box margin-bottom-none">
            <div class="box-header with-border">
                <h3 class="box-title pad-t5">信息列表</h3>
                <div class="pull-right no-margin">
                	<!-- <a href="javascript:;" class="btn btn-default btn-sm margin_l10" data-role="export">
	                	<i class="fa fa-fw fa-download"></i>批量导入任教信息
	                </a>
	                <a href="javascript:;" class="btn btn-default btn-sm margin_l10" data-role="export">
	                	<i class="fa fa-fw fa-download"></i>批量导出任教信息
	                </a> -->
	                <a href="javascript:;" class="btn btn-default btn-sm margin_l10" data-role="sync">
	                	<i class="fa fa-exchange"></i>同步信息
	                </a>
	                <a href="javascript:;" class="btn btn-default btn-sm margin_l10" data-role="addTeachCourse">
	                	<i class="fa fa-fw fa-plus"></i>新增任教信息
	                </a>
                </div>
            </div>
            <div class="box-body">
                <div class="filter-tabs clearfix">
                    <ul class="list-unstyled">
                        <li class="<c:if test="${empty param.STATE}">actived</c:if>" onclick="choiceXJ('')">全部(<span id="stateCountAll">0</span>)</li>
                        <li class="<c:if test="${param.STATE eq 'Y'}">actived</c:if>" onclick="choiceXJ('Y')">已启用(<span id="stateYCount">0</span>)</li>
                        <li class="<c:if test="${param.STATE eq 'N'}">actived</c:if>" onclick="choiceXJ('N')">未启用(<span id="stateNCount">0</span>)</li>
                    </ul>
                </div>
                <div class="table-responsive" style="overflow: hidden">
                    <table class="table table-bordered table-striped vertical-mid table-font">
                        <thead>
                        <tr>
                            <th class="text-center">头像</th>
                            <th class="text-center">账号</th>
                            <th class="text-center">姓名</th>
                            <th class="text-center">任教课程</th>
                            <th class="text-center">任教机构</th>
                            <th class="text-center">任教类型</th>
                            <th class="text-center">启用状态</th>
                            <th class="text-center">同步状态</th>
                            <th class="text-center">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${pageInfo.content }" var="entity">
                        	<tr>
                            <td class="text-center">
								<c:if test="${not empty entity.ZP }">
									<img src="${entity.ZP }" class="img-circle" width="50" height="50" onerror="this.src='${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png'">
								</c:if>
								<c:if test="${empty entity.ZP }">
									<img src="${css }/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="img-circle" width="50" height="50">
								</c:if>
							</td>
							<td>${entity.ZGH }</td>
							<td>${entity.XM }</td>
							<td>
								<div class="text-center">
									${entity.KCMC }<br>
									(${entity.KCH })<br>
								</div>
							</td>
							<td>
								<div class="text-center">
									${entity.SC_NAME }<br>
									(${entity.SC_CODE })<br>
								</div>
							</td>
							<td class="text-center">
								${entity.TEACHER_TYPE == '1'?'主持老师':entity.TEACHER_TYPE == '2'?'责任老师':'辅导老师' }
							</td>
							<td class="text-center">
								<c:choose>
									<c:when test="${entity.STATE eq '1' }">
										<span class="text-green">已启用</span>
									</c:when>
									<c:otherwise>
										<span class="gray9">未启用</span>
									</c:otherwise>
								</c:choose>
							</td>
							<td class="text-center">
								<c:choose>
									<c:when test="${entity.IS_SYNCHRO eq 'Y' }">
										<span class="text-green">已同步</span>
									</c:when>
									<c:otherwise>
										<span class="gray9">未同步</span>
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<div class="text-center">
									<c:if test="${entity.STATE eq '1' }">
										<a href="javascript:void(0);" data-role="stop-one" data-val="${entity.COURSE_TEACHER_ID }" class="operion-item" title="停用任教信息" data-tempTitle="停用任教信息">
											<i class="fa fa-fw fa-minus-circle"></i>
										</a>
									</c:if>
									<c:if test="${entity.STATE eq '2' }">
										<a href="javascript:void(0);" data-role="open-one" data-val="${entity.COURSE_TEACHER_ID }" class="operion-item" title="启用任教信息" data-tempTitle="启用任教信息">
											<i class="fa fa-fw fa-play"></i>
										</a>
									</c:if>
									<a href="javascript:void(0);" class="operion-item" data-role="view-more" data-val="${entity.COURSE_TEACHER_ID }" title="查看详情" data-tempTitle="查看详情">
										<i class="fa fa-fw fa-tags"></i>
									</a>
									<c:if test="${entity.IS_SYNCHRO eq 'N' }">
										<a href="javascript:void(0);" data-role="del-one" data-val="${entity.COURSE_TEACHER_ID }" class="operion-item" title="删除任教信息" data-tempTitle="删除任教信息">
											<i class="fa fa-fw fa-trash-o"></i>
										</a>
									</c:if>
								</div>
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
 	// 导出
    $('[data-role="export"]').click(function(event) {
        event.preventDefault();
        var data = $("#listForm").serialize();
        var self=this;
        $.mydialog({
            id:'expRecResult',
            width:600,
            height:415,
            zIndex:11000,
            content: 'url:${ctx}/edumanage/choosemanage/expRecResult?'+data
        });
    });
 	
 	// 同步
    $('[data-role="sync"]').click(function(event) {
        event.preventDefault();
        var data = $("#listForm").serialize();
        $.mydialog({
            id:'expRecResult',
            width:650,
            height:455,
            zIndex:11000,
            content: 'url:/edumanage/teachCourse/getSyncList?'+data
        });
    });
 	
    $('[data-role="addTeachCourse"]').click(function(event) {
        event.preventDefault();
        $.mydialog({
            id:'teachCourse',
            width:600,
            height:500,
            zIndex:11000,
            content: 'url:${ctx}/edumanage/teachCourse/toAddTeachCourse'
        });
    });
    
    //查看详情
    $('[data-role="view-more"]').click(function(event) {
        event.preventDefault();
        var id=$(this).attr('data-val');
        $.mydialog({
            id:'teachCourse',
            width:600,
            height:500,
            zIndex:11000,
            content: 'url:${ctx}/edumanage/teachCourse/toViewInfo?COURSE_TEACHER_ID='+id
        });
    });
    
    // 删除任教信息
    $("[data-role='del-one']").click(function(event) {
    	 event.preventDefault();
		 var id=$(this).attr('data-val');
         $.confirm({
             title: '提示',
             content: '是否删除该任教信息？',
             confirmButton: '确认',
             icon: 'fa fa-warning',
             cancelButton: '取消',  
             confirmButtonClass: 'btn-primary',
             closeIcon: true,
             closeIconClass: 'fa fa-close',
             confirm: function () { 
             	 $.get("/edumanage/teachCourse/delTeacherInfo?COURSE_TEACHER_ID="+id,function(data){
             		$(".search").click();
             	},"json"); 
             } 
         });
     });
    
 	// 停用任教信息
    $("[data-role='stop-one']").click(function(event) {
    	 event.preventDefault();
		 var id=$(this).attr('data-val');
         $.confirm({
             title: '提示',
             content: '是否停用该任教信息？',
             confirmButton: '确认',
             icon: 'fa fa-warning',
             cancelButton: '取消',  
             confirmButtonClass: 'btn-primary',
             closeIcon: true,
             closeIconClass: 'fa fa-close',
             confirm: function () { 
             	 $.get("/edumanage/teachCourse/uptTeacherInfo?STATE=2&COURSE_TEACHER_ID="+id,function(data){
             		$(".search").click();
             	},"json"); 
             } 
         });
     });
 
 	// 启用任教信息
    $("[data-role='open-one']").click(function(event) {
    	 event.preventDefault();
		 var id=$(this).attr('data-val');
         $.confirm({
             title: '提示',
             content: '是否启用该任教信息？',
             confirmButton: '确认',
             icon: 'fa fa-warning',
             cancelButton: '取消',  
             confirmButtonClass: 'btn-primary',
             closeIcon: true,
             closeIconClass: 'fa fa-close',
             confirm: function () { 
             	 $.get("/edumanage/teachCourse/uptTeacherInfo?STATE=1&COURSE_TEACHER_ID="+id,function(data){
             		$(".search").click();
             	},"json"); 
             } 
         });
     });
  	
  	 function choiceXJ(sync_status) {
  		 $("#STATE").val(sync_status);
  		 $("#listForm").submit();
  	 }
  	 
  	 function getTeachCourseCount() {
  		$.ajax({
            type: "POST",
            dataType: "json",
            url: "/edumanage/teachCourse/getTeachCourseCount",
            data: $('#listForm').serialize(),
            success: function (result) {
            	$("#stateCountAll").html(result.stateCountAll);
            	$("#stateYCount").html(result.stateYCount);
            	$("#stateNCount").html(result.stateNCount);
            },
            error : function() {
            }
        });
  	 }
  	 getTeachCourseCount();
</script>
</body>
</html>
