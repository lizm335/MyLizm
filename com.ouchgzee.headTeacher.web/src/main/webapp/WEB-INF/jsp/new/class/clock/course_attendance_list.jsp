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
        	课程考勤
    </h1>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li class="active">课程考勤</li>
    </ol>
</section>
<section class="content">
    <form class="form-horizontal" id="listForm">
        <div class="nav-tabs-custom margin-bottom-non">
	           <ul class="nav nav-tabs nav-tabs-lg">
	               <li class="active">
	               	     <a href="${ctx}/home/class/clock/list" >课程考勤</a>
	               </li>
	               <li >
	              		 <a href="${ctx}/home/class/clock/studentAttendanceList" >学员考勤</a>
	               </li>
	           </ul>
                <div class="box">
                    <div class="box-body">
                        <div class="row pad-t15">
                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">开课学期</label>
                                    <div class="col-sm-9">
                                        <select class="selectpicker show-tick form-control" name="GRADE_ID" id="GRADE_ID" data-size="5" data-live-search="true">
                                            <option value="all" selected="">全部学期</option>
                                            <c:forEach items="${gradeMap}" var="entry">
                                                    <option value="${entry.key}" <c:if test="${currentGradeId eq entry.key }">selected="selected"</c:if>>
                                                    	${entry.value}
                                                    </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">课程</label>
                                    <div class="col-sm-9">
                                        <input type="hidden" name="TIME_FLG" value="" id="time_flg">
                                        <select class="selectpicker show-tick form-control" name="COURSE_ID" id="COURSE_ID" data-size="5" data-live-search="true">
                                            <option value="" selected="selected">请选择</option>
                                            <c:forEach items="${courseMap}" var="map">
                                                <option value="${map.key}" <c:if test="${param.COURSE_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- /.box-body -->
                    <div class="box-footer">
                        <div class="pull-right"><button type="reset" class="btn btn-default">重置</button></div>
                        <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary" id="submit_buttion">搜索</button></div>
                    </div>
                </div>
            <div class="box box-border margin-bottom-none">
                <div class="box-body">
                    <div class="filter-tabs clearfix">
                        <ul class="list-unstyled">
                            <li class="js-timeFlag" data-value="">
                            		全部
                            </li>
                            <li class="js-timeFlag" data-value="1">
                            		开课中
                            </li>
                            <li class="js-timeFlag" data-value="2">
                            	待开课
                            </li>
                            <li class="js-timeFlag" data-value="3">
                            	已结束
                            </li>
                        </ul>
                        <div class="pull-right no-margin">
                            <a href="${ctx}/home/class/clock/exportCourseAttendancePage/${pageInfoNum}" data-role="export" class="btn btn-default btn-sm btn-outport"><i class="fa fa-fw fa-sign-out"></i> 导出考勤信息</a>
                        </div>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-bordered table-striped vertical-mid text-center table-font">
                            <thead>
                            <tr>
                                <th>开课信息</th>
                                <th>选课人数</th>
                                <th>学习总次数/<br>平均学习次数</th>
                                <th>学习总时长/<br>平均学习时长</th>
                                <th>7天以上<br>未学习人数</th>
                                <th>3天以上<br>未学习人数</th>
                                <th>3天以内<br>未学习人数</th>
                                <th>从未学习人数</th>
                                <th>当前在学人数</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${not empty pageInfo.content}">
                                    <c:forEach items="${pageInfo.content}" var="item">
                                        <tr>
                                            <td>
                                                <div class="text-left">
                                                    开课学期：${item.GRADE_NAME}<br>
                                                    课程名称：${item.KCMC}<br>
                                                    开课状态：<c:choose>
                                                    <c:when test="${item.TIME_FLG eq '1'.charAt(0) }">
                                                        <span class="text-green">开课中</span>
                                                    </c:when>
                                                    <c:when test="${item.TIME_FLG eq '2'.charAt(0) }">
                                                        <span class="text-orange">待开课</span>
                                                    </c:when>
                                                    <c:when test="${item.TIME_FLG eq '3'.charAt(0) }">
                                                        <span class="gray9">已结束</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="gray9">已结束</span>
                                                    </c:otherwise>
                                                </c:choose>
                                                </div>
                                            </td>
                                            <td>${item.REC_COUNT}</td>
                                            <td>${item.SUM_LOGIN_COUNT}/${item.AVG_LOGIN_COUNT}</td>
                                            <td>${item.SUM_LOGIN_TIME}/${item.AVG_LOGIN_TIME}</td>
                                            <td>${item.DAY7_LOGIN}</td>
                                            <td>${item.DAY3_7_LOGIN}</td>
                                            <td>${item.DAY3_LOGIN}</td>
                                            <td>${item.NO_DAY_LOGIN}</td>
                                            <td>${item.ONLINE_STUDENT_COUNT}</td>
                                            <td>
                                                <div class="data-operion">
                                                    <a href="${ctx}/home/class/clock/courseAttendanceDetails/${item.COURSE_ID}?initGradId=${item.GRADE_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                	<tr><td align="center" colspan="15">暂无数据</td></tr>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                        <tags:pagination page="${pageInfo}" paginationSize="10" />
                    </div>
                </div>
            </div>
        </div>
    </form>
</section>
<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>
<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script src="${ctx}/static/plugins/custom-model/custom-model.js"></script>
<script type="text/javascript">
	$(function(){
		//init
		var currentFlg = "${param.TIME_FLG}";
		var params  = $("#listForm").serialize();
		
		$.getJSON("${ctx}/home/class/clock/courseAttendanceCounts?" + params).done(function(data){
			
			$(".js-timeFlag").each(function(){
				var $this = $(this);
				var timeFlag = $this.attr("data-value");
				var key = "LOGIN_STATE_COUNT" + timeFlag;
				$this.append("("+data[key]+")");
				//是否激活
				if(timeFlag == currentFlg){
					$this.addClass("actived");
				}
			
			});
			
		});
		
		
	});
	
    //按钮切换
    $(".js-timeFlag").click(function () {
    	var $this = $(this);
    	var timeFlag = $this.attr("data-value");
    	$("#time_flg").val(timeFlag);
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
