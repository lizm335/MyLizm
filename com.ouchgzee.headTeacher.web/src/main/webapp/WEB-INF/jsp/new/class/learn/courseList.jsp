<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 学员学情</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
    <h1>
        	课程学情
    </h1>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li class="active">课程学情</li>
    </ol>
</section>
<section class="content">
    <form class="form-horizontal" id="listForm">
        <div class="nav-tabs-custom margin-bottom-non">
        	 <ul class="nav nav-tabs nav-tabs-lg">
	            <li class="active">
	            	<a href="${ctx}/home/class/learn/courseList" >课程学情</a>
	            </li>
	            <li>
	            	<a href="${ctx}/home/class/learn/list"  >学员学情</a>
	            </li>
        	</ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_top_1">
                    <div class="box box-border">
                        <div class="box-body">
                                <div class="row reset-form-horizontal pad-t15">
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label class="control-label col-sm-3 text-nowrap">开课学期</label>
                                            <div class="col-sm-9">
                                                <select class="selectpicker show-tick form-control" name="GRADE_ID" id="GRADE_ID" data-size="5" data-live-search="true">
                                                    <option value="all" selected="">全部学期</option>
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
                                            <label class="control-label col-sm-3 text-nowrap">课程</label>
                                            <div class="col-sm-9">
                                                <select class="selectpicker show-tick form-control" name="COURSE_ID" id="course_id" data-size="5" data-live-search="true">
                                                    <option value="" selected="selected">请选择</option>
                                                    <c:forEach items="${courseMap}" var="map">
                                                        <option value="${map.key}" <c:if test="${param.COURSE_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label class="control-label col-sm-3 text-nowrap">学习成绩</label>
                                            <div class="col-sm-9">
                                                <div class="input-group">
                                                    <div class="input-group-btn">
                                                        <select class="form-control input-group-select bg-white" name="SCORE_FLG">
                                                            <option value="EQ" <c:if test="${empty param.SCORE_FLG || param.SCORE_FLG eq 'EQ' }">selected="selected"</c:if>>等于</option>
                                                            <option value="GT" <c:if test="${param.SCORE_FLG eq 'GT' }">selected="selected"</c:if>>大于</option>
                                                            <option value="GTE" <c:if test="${param.SCORE_FLG eq 'GTE' }">selected="selected"</c:if>>大于等于</option>
                                                            <option value="LT" <c:if test="${param.SCORE_FLG eq 'LT' }">selected="selected"</c:if>>小于</option>
                                                            <option value="LTE" <c:if test="${param.SCORE_FLG eq 'LTE' }">selected="selected"</c:if>>小于等于</option>
                                                        </select>
                                                    </div>
                                                    <input type="text" name="AVG_STUDY_SCORE" class="form-control" value="${param.AVG_STUDY_SCORE }">
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label class="control-label col-sm-3 text-nowrap">学习进度</label>
                                            <div class="col-sm-9">
                                                <div class="input-group">
                                                    <div class="input-group-btn">
                                                        <select class="form-control input-group-select bg-white" name="SCHEDULE_FLG">
                                                            <option value="EQ" <c:if test="${empty param.SCHEDULE_FLG || param.SCHEDULE_FLG eq 'EQ' }">selected="selected"</c:if>>等于</option>
                                                            <option value="GT" <c:if test="${param.SCHEDULE_FLG eq 'GT' }">selected="selected"</c:if>>大于</option>
                                                            <option value="GTE" <c:if test="${param.SCHEDULE_FLG eq 'GTE' }">selected="selected"</c:if>>大于等于</option>
                                                            <option value="LT" <c:if test="${param.SCHEDULE_FLG eq 'LT' }">selected="selected"</c:if>>小于</option>
                                                            <option value="LTE" <c:if test="${param.SCHEDULE_FLG eq 'LTE' }">selected="selected"</c:if>>小于等于</option>
                                                        </select>
                                                    </div>
                                                    <input type="text" name="AVG_SCHEDULE" class="form-control" value="${param.AVG_SCHEDULE }">
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label class="control-label col-sm-3 text-nowrap">通过率</label>
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
                                                    <input type="text" name="PASS_PERCENT" class="form-control" value="${param.PASS_PERCENT }">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="box-footer">
                                    <div class="pull-right"><button type="button" class="btn btn-default">重置</button></div>
                                    <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
                                </div>
                        </div>
                    </div>

                    <div class="box box-border margin-bottom-none">
                        <div class="box-header with-border">
                            <div class="fr">
                                <a href="${ctx}/home/class/learn/exportCourseConditionList/${pageInfoTotalElements}" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出课程学情信息</a>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="table-responsive">
                                <table class="table table-bordered table-striped table-cell-ver-mid text-center">
                                    <thead>
                                    <tr>
                                        <th>开课信息</th>
                                        <th>选课人数</th>
                                        <th>平均<br>学习进度</th>
                                        <th>平均<br>学习次数</th>
                                        <th>平均<br>学习时长(小时)</th>
                                        <th>成绩</th>
                                        <th>已通过人数</th>
                                        <th>未通过人数</th>
                                        <th>学习中人数</th>
                                        <th>登记中人数</th>
                                        <th>未学习人数</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${not empty pageInfo.content}">
                                            <c:forEach items="${pageInfo.content}" var="entity">
                                                <tr>
                                                    <td>
                                                        <div class="text-left">
                                                            开课学期：${entity.GRADE_NAME }<br>
                                                            课程名称：${entity.KCMC }<br>
                                                            开课状态：<c:choose>
                                                            <c:when test="${entity.TIME_FLG eq '1'.charAt(0) }">
                                                                <span class="text-green">开课中</span>
                                                            </c:when>
                                                            <c:when test="${entity.TIME_FLG eq '2'.charAt(0) }">
                                                                <span class="text-orange">待开课</span>
                                                            </c:when>
                                                            <c:when test="${entity.TIME_FLG eq '3'.charAt(0) }">
                                                                <span class="gray9">已结束</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="gray9">已结束</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        </div>
                                                    </td>
                                                    <td>${entity.REC_COUNT }</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty entity.AVG_SCHEDULE }">${entity.AVG_SCHEDULE }%</c:when>
                                                            <c:otherwise>--</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty entity.AVG_LOGIN_COUNT }">${entity.AVG_LOGIN_COUNT }</c:when>
                                                            <c:otherwise>--</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty entity.AVG_LOGIN_TIME }">${entity.AVG_LOGIN_TIME }</c:when>
                                                            <c:otherwise>--</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <div class="text-left">
                                                            平均学习成绩：${entity.AVG_STUDY_SCORE }<br>
                                                            平均考试成绩：${entity.AVG_EXAM_SCORE }<br>
                                                            平均总成绩：${entity.AVG_TOTAL_SCORE }
                                                        </div>
                                                    </td>
                                                    <td>${entity.SUM_PASS_COUNT }<br><span class="gray9">(${entity.AVG_PASS_COUNT }%)</span></td>
                                                    <td>${entity.SUM_UNPASS_COUNT}<br><span class="gray9">(${entity.AVG_UNPASS_COUNT }%)</span></td>
                                                    <td>${entity.SUM_STUDY_IMG }<br><span class="gray9">(${entity.STUDY_IMG_PERCENT }%)</span></td>
                                                    <td>${entity.SUM_REGISTER_COUNT }<br><span class="gray9">(${entity.AVG_REGISTER_COUNT }%)</span></td>
                                                    <td>${entity.SUM_NEVER_STUDY }<br><span class="gray9">(${entity.NEVER_STUDY_PERCENT }%)</span></td>
                                                    <td>
                                                        <a href="${ctx}/home/class/learn/courseDetails/${entity.COURSE_ID}?initGradeId=${entity.GRADE_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr><td colspan="11">暂无数据！</td></tr>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>
                            <tags:pagination page="${pageInfo}" paginationSize="10"/>
                        </div>
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

    //切换统计条
    function onchangState(str,obj) {
        if(str==='all'){
            $("select[name='examState']").val('');
            $("select[name='studyState']").val('');
        }else {
            if(str=='studyState'){
                $("select[name='studyState']").val(obj);
            }else {
                $("select[name='examState']").val(obj);
            }
        }
        $("#listForm").click();
    }
    //导出班级课程学情
    function exportLeranInfo(url) {
        var form = $("<form>");//定义form表单
        form.attr("style","display:none");
        form.attr("target","");
        form.attr("method","post");
        form.attr("action",url);
        $("body").append(form);
        form.submit();//表单提交
    }
</script>
</body>
</html>
