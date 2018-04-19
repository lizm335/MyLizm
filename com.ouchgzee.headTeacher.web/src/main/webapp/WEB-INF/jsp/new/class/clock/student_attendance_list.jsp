<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 学员考勤</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
    <h1>
     	   学员考勤
    </h1>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li class="active">学员考勤</li>
    </ol>
</section>
<section class="content">
    <form class="form-horizontal" id="listForm">
        <div class="nav-tabs-custom margin-bottom-non">
            <ul class="nav nav-tabs nav-tabs-lg">
                <li>
                	<a href="${ctx}/home/class/clock/list" >课程考勤</a>
                </li>
                <li class="active">
               		 <a href="${ctx}/home/class/clock/studentAttendanceList" >学员考勤</a>
                </li>
            </ul>
            <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
            <div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>
                <div class="box">
                    <div class="box-body">
                        <div class="row reset-form-horizontal pad-t15">
                            <div class="col-sm-4 col-xs-6">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">学习中心</label>
                                    <div class="col-sm-9">
                                        <select name="XXZX_ID" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true" id="XXZX_ID">
                                            <option value="">请选择</option>
                                            <c:forEach items="${studyCenterMap}" var="map">
                                                <option value="${map.key}"  <c:if test="${map.key==param.XXZX_ID}">selected='selected'</c:if>>${map.value}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">姓名</label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control" name="XM" value="${param.XM }">
                                        <input type="hidden" name="STUDY_STATUS" value="${param.STUDY_STATUS}" id="study_status">
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

                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">专业层次</label>
                                    <div class="col-sm-9">
                                        <select class="selectpicker show-tick form-control" name="PYCC" id="pycc" data-size="5" data-live-search="true">
                                            <option value="" selected="selected">请选择</option>
                                            <c:forEach items="${pyccMap}" var="map">
                                                <option value="${map.key}" <c:if test="${param.PYCC eq map.key}">selected='selected'</c:if>>
                                                        ${map.value}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">入学学期</label>
                                    <div class="col-sm-9">
                                        <select class="selectpicker show-tick form-control" name="GRADE_ID" id="GRADE_ID" data-size="5" data-live-search="true">
                                            <option value="all" selected="selected">全部学期</option>
                                            <c:forEach items="${gradeMap}" var="map">
                                                <c:if test="${empty param.GRADE_ID}">
                                                    <option value="${map.key}" <c:if test="${currentGradeId eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                                </c:if>
                                                <c:if test="${not empty param.GRADE_ID}">
                                                    <option value="${map.key}" <c:if test="${param.GRADE_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">报读专业</label>
                                    <div class="col-sm-9">
                                        <select class="selectpicker show-tick form-control" name="SPECIALTY_ID" id="specialty_id" data-size="5" data-live-search="true">
                                            <option value="" selected="selected">请选择</option>
                                            <c:forEach items="${specialtyMap}" var="map">
                                                <option value="${map.key}" <c:if test="${param.SPECIALTY_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                    <div class="box-footer">
                        <div class="pull-right"><button type="reset" class="btn btn-default">重置</button></div>
                        <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary" id="submit_buttion">搜索</button></div>
                    </div>
                </div>

            <div class="box margin-bottom-none">
                <div class="box-body">
                    <div class="filter-tabs clearfix">
                        <ul class="list-unstyled">
                            <li class="js-studyStatus"  data-value="">全部</li>
                            <li class="js-studyStatus"  data-value="0">在线</li>
                            <li class="js-studyStatus"  data-value="1">7天以上未学习</li>
                            <li class="js-studyStatus"  data-value="2">3天以上未学习</li>
                            <li class="js-studyStatus"  data-value="3">3天内未学习</li>
                            <li class="js-studyStatus"  data-value="4">从未学习</li>
                        </ul>
                        <div class="fr">
                            <a href="${ctx}/home/class/clock/exportAttendanceList/${pageInfoTotal}" data-role="export" class="btn btn-default btn-sm btn-outport"><i class="fa fa-fw fa-sign-out"></i> 导出学员考勤统计表</a>
                        </div>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-bordered table-striped vertical-mid text-center table-font">
                            <thead>
                            <tr>
                                <th>头像</th>
                                <th>个人信息</th>
                                <th width="26%">报读信息</th>
                                <th>学习总次数</th>
                                <th>学习总时长(小时)</th>
                                <th>主要应用终端</th>
                                <th>最近学习日期</th>
                                <th>当前在线状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${not empty pageInfo.content}">
                                    <c:forEach items="${pageInfo.content}" var="entity">
                                        <tr>
                                            <td class="text-center" width="100">
                                                <c:choose>
                                                    <c:when test="${not empty entity.ZP}">
                                                        <img src="${entity.ZP}" class="img-circle" width="50" height="50">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="${ctx }/static/images/headImg04.png" class="img-circle" width="50" height="50">
                                                    </c:otherwise>
                                                </c:choose>
                                                <a href="#" class="btn btn-xs btn-default bg-white no-shadow btn-block margin_t5">
                                                    <i class="fa fa-ee-online f24 vertical-middle text-green position-relative" style="top: -2px;"></i>
                                                    <span class="gray9">交流</span>
                                                </a>
                                            </td>
                                            <td>
                                                <div class="text-left">
                                                    姓名：${entity.XM } <br>
                                                    学号：${entity.XH } <br>
                                                    手机：${entity.SJH }
                                                </div>
                                            </td>
                                            <td>
                                                <div class="text-left">
                                                    专业层次：${entity.PYCC_NAME } <br>
                                                    入学年级：${entity.YEAR_NAME} <br>
                                                    入学学期：${entity.GRADE_NAME }<br>
                                                    报读专业：${entity.ZYMC } <c:if test="${not empty entity.RULE_CODE}">(${entity.RULE_CODE})</c:if> <br>
                                                    教务班级：${entity.BJMC }<br>
                                                    学习中心：${entity.SC_NAME}
                                                </div>
                                            </td>
                                            <td>${entity.LOGIN_TIMES }</td>
                                            <td>${entity.LOGIN_TIME_COUNT }</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${entity.ALL_ONLINE_COUNT ne 0}">
                                                        <c:if test="${entity.PC_ONLINE_PERCENT gt entity.APP_ONLINE_PERCENT}">PC<div class="gray9">（${entity.PC_ONLINE_PERCENT}%）</div></c:if>
                                                        <c:if test="${entity.PC_ONLINE_PERCENT lt entity.APP_ONLINE_PERCENT}">APP<div class="gray9">（${entity.APP_ONLINE_PERCENT}%）</div></c:if>
                                                    </c:when>
                                                    <c:otherwise>--</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                               <c:if test="${empty entity.MAX_LAST_LOGIN_TIME }">--</c:if>
											   <c:if test="${not empty entity.MAX_LAST_LOGIN_TIME }">${entity.MAX_LAST_LOGIN_TIME }</c:if>
                                            </td>
                                            <td>
                                                <c:if test="${entity.IS_ONLINE eq 'Y'}"><div class="text-green">在线<br><c:if test="${not empty entity.DEVICE}">(${entity.DEVICE}在线)</c:if></div></c:if>
                                                <c:if test="${entity.IS_ONLINE eq 'N'}">
                                                    <div class="gray9">离线<br>
                                                        <c:if test="${entity.LOGIN_COUNT eq '0' or empty entity.LOGIN_COUNT}">(从未学习)</c:if>
                                                        <c:if test="${not empty entity.LEFT_DAY and entity.LEFT_DAY>0 and entity.LOGIN_COUNT ne '0'}">(${entity.LEFT_DAY}天未学习)</c:if>
                                                    </div>
                                                </c:if>
                                                <c:if test="${empty entity.IS_ONLINE}">
                                                    <div class="gray9">离线<br>(从未学习)</div>
                                                </c:if>
                                            </td>
                                            <td>
                                                <div class="data-operion">
                                                    <a href="${ctx}/home/class/clock/studentLoginDetail/${entity.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise><tr><td colspan="9">暂无数据！</td></tr></c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                    <tags:pagination page="${pageInfo}" paginationSize="10" />
                </div>
            </div>
        </div>
    </form>
</section>
<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>
<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script src="${ctx}/static/plugins/custom-model/custom-model.js"></script>
<script type="text/javascript">


    $(function() {
    	//init
    	var currentStudyStatus = "${param.STUDY_STATUS}";
    	var initUrl = "${ctx}/home/class/clock/studentAttendanceCounts";
    	var params = $("#listForm").serialize();
    	$.ajax({
    		url:initUrl,
    		type:"post",
    		data:params, 
    		dataType:"json"
    	}).done(function(data){
    		$(".js-studyStatus").each(function(index, dom){
    			var $dom = $(dom);
    			var value = $dom.attr("data-value");
    			var key = "STUDY_STATUS_COUNT" + value;
    			$dom.append("(" + data[key] + ")");
    			
    			if(currentStudyStatus == value){
    				$dom.addClass("actived");
    			}
    		});
    	});
    	
    });
    
    //按钮切换
    $(".js-studyStatus").click(function () {
        var $this = $(this);
        var value = $(this).attr("data-value");
        $("#study_status").val(value);
        $("#submit_buttion").click();
    });

    // 导出
    $('[data-role="export"]').click(function(event) {
        event.preventDefault();
        var self=this;
        $.mydialog({
            id:'export',
            width:600,
            height:415,
            zIndex:11000,
            content: 'url:'+$(this).attr('href')
        });
    });

  
</script>
</body>
</html>
