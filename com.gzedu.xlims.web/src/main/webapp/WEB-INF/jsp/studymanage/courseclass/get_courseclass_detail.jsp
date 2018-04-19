<%--
  Created by IntelliJ IDEA.
  User: Min
  Date: 5/16/2017
  Time: 3:59 PM
  To change this template use File | Settings | File Templates.
--%>
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
    <button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
    <ol class="breadcrumb">
        <li><a href="javascript:"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="javascript:">学习管理</a></li>
        <li><a href="javascript:">课程班学情</a></li>
        <li class="active">课程班学情明细</li>
    </ol>
</section>
<section class="content">

    <form id="listForm" class="form-horizontal">
        <div class="box">
            <div class="box-body">
                <div class="row pad-t15">

                    <div class="col-sm-4 col-xs-6">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">学习中心</label>
                            <div class="col-sm-9">
                                <select name="XXZX_ID" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
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
                            <label class="control-label col-sm-3 text-nowrap">开课学期</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="gradeId" id="gradeId" data-size="5" data-live-search="true">
                                    <option value="all" selected="">全部学期</option>
                                    <c:forEach items="${gradeMap}" var="map">
                                        <c:if test="${empty grade_id}">
                                            <option value="${map.key}" <c:if test="${currentGradeId eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                        </c:if>
                                        <c:if test="${not empty grade_id}">
                                            <option value="${map.key}" <c:if test="${grade_id eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                        </c:if>
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
                            <label class="control-label col-sm-3 text-nowrap">报读专业</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="SPECIALTY_ID" id="SPECIALTY_ID" data-size="5" data-live-search="true">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${specialtyMap}" var="map">
                                        <option value="${map.key}" <c:if test="${param.SPECIALTY_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">入学学期</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="startGrade" id="startGrade" data-size="5" data-live-search="true">
                                    <option value="">全部学期</option>
                                    <c:forEach items="${gradeMap}" var="map">
                                        <option value="${map.key}" <c:if test="${param.startGrade eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">课程班</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="classId" id="class_id" data-size="5" data-live-search="true">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${courseclass}" var="map">
                                        <c:choose>
                                            <c:when test="${not empty param.classId}">
                                                <option value="${map.key}" <c:if test="${param.classId eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${map.key}" <c:if test="${classId eq map.key }">selected="selected"</c:if>>${map.value}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">学习进度</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-btn">
                                        <select class="form-control input-group-select bg-white" name="scheduleSymbol">
                                            <option value="=" <c:if test="${empty param.scheduleSymbol || param.scheduleSymbol eq '=' }">selected="selected"</c:if>>等于</option>
                                            <option value=">" <c:if test="${param.scheduleSymbol eq '>' }">selected="selected"</c:if>>大于</option>
                                            <option value=">=" <c:if test="${param.scheduleSymbol eq '>=' }">selected="selected"</c:if>>大于等于</option>
                                            <option value="<" <c:if test="${param.scheduleSymbol eq '<' }">selected="selected"</c:if>>小于</option>
                                            <option value="<=" <c:if test="${param.scheduleSymbol eq '<=' }">selected="selected"</c:if>>小于等于</option>
                                        </select>
                                    </div>
                                    <input type="text" name="schedule" class="form-control" value="${param.schedule }">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3 text-nowrap">学习状态</label>
                            <div class="col-sm-9">
                                <select class="selectpicker show-tick form-control" name="examState" id="examState" data-size="5" data-live-search="true">
                                    <option value="">全部状态</option>
                                    <option value="0" <c:if test="${param.examState eq '0' }">selected="selected"</c:if>>未通过</option>
                                    <option value="1" <c:if test="${param.examState eq '1' }">selected="selected"</c:if>>已通过</option>
                                    <option value="2" <c:if test="${param.examState eq '2' }">selected="selected"</c:if>>学习中</option>
                                    <option value="3" <c:if test="${param.examState eq '3' }">selected="selected"</c:if>>登记中</option>
                                    <option value="4" <c:if test="${param.examState eq '4' }">selected="selected"</c:if>>未学习</option>
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
                                        <select class="form-control input-group-select bg-white" name="examSymbol">
                                            <option value="=" <c:if test="${empty param.examSymbol || param.examSymbol eq '=' }">selected="selected"</c:if>>等于</option>
                                            <option value=">" <c:if test="${param.examSymbol eq '>' }">selected="selected"</c:if>>大于</option>
                                            <option value=">=" <c:if test="${param.examSymbol eq '>=' }">selected="selected"</c:if>>大于等于</option>
                                            <option value="<" <c:if test="${param.examSymbol eq '<' }">selected="selected"</c:if>>小于</option>
                                            <option value="<=" <c:if test="${param.examSymbol eq '<=' }">selected="selected"</c:if>>小于等于</option>
                                        </select>
                                    </div>
                                    <input type="text" name="examScore" class="form-control" value="${param.examScore }">
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div><!-- /.box-body -->
            <div class="box-footer text-right">
                <button type="submit" class="btn min-width-90px btn-primary margin_r15">搜索</button>
                <button type="reset" class="btn min-width-90px btn-default">重置</button>
            </div><!-- /.box-footer-->
        </div>
        <div class="box margin-bottom-none">
            <div class="box-header with-border">
                <h3 class="box-title pad-t5">课程班学情明细列表</h3>
                <div class="filter-tabs clearfix pull-left no-margin">
                </div>
                <shiro:hasPermission name="/studymanage/getCourseStudyList$export">
                    <div class="pull-right no-margin">
                        <a href="${ctx}/studymanage/commonCourseConditionDetailExport/${pageInfo.getTotalElements()}" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出课程班学情明细表</a>
                    </div>
                </shiro:hasPermission>
            </div>
            <div class="box-body">
                <div class="table-responsive">
                    <table class="table table-bordered table-striped vertical-mid text-center table-font">
                        <thead>
                        <tr>
                            <th>头像</th>
                            <th style="width: 12%">个人信息</th>
                            <th style="width: 15%">报读信息</th>
                            <th>开课信息</th>
                            <th>学习次数</th>
                            <th>学习时长</th>
                            <th>学习进度</th>
                            <th>学习成绩</th>
                            <th>考试成绩</th>
                            <th>总成绩</th>
                            <th>状态</th>
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
                                                姓名：${entity.XM}<br>
                                                学号：${entity.XH}<br>
                                                <shiro:hasPermission name="/personal/index$privacyJurisdiction">
                                                手机：${entity.SJH}
                                                </shiro:hasPermission>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="text-left">
                                                专业层次：${entity.PYCC_NAME }<br>
                                                入学年级：${entity.YEAR_NAME }<br>
                                                入学学期：${entity.START_GRADE }<br>
                                                报读专业：${entity.ZYMC }
                                            </div>
                                        </td>
                                        <td>
                                            <div class="text-left">
                                                开课学期：${entity.GRADE_NAME }<br>
                                                课程名称：${entity.KCMC }<br>
                                                课程代码：${entity.KCH }<br>
                                                课程班级：${entity.BJMC }<br>
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
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty entity.LOGIN_COUNT }">${entity.LOGIN_COUNT }</c:when>
                                                <c:otherwise>--</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty entity.LOGIN_TIME }">${entity.LOGIN_TIME }</c:when>
                                                <c:otherwise>--</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:if test="${ empty entity.SCHEDULE}">--</c:if>
                                            <c:if test="${not empty entity.SCHEDULE}">${entity.SCHEDULE }%</c:if>
                                        </td>
                                        <td>
                                            <c:if test="${empty entity.EXAM_SCORE }">--</c:if>
                                            <c:if test="${not empty entity.EXAM_SCORE }">${entity.EXAM_SCORE }</c:if>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${entity.EXAM_STATE eq '学习中'}">--</c:when>
                                                <c:otherwise>
                                                    <c:if test="${empty entity.EXAM_SCORE1 }">--</c:if>
                                                    <c:if test="${not empty entity.EXAM_SCORE1 }">${entity.EXAM_SCORE1 }</c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${entity.EXAM_STATE eq '学习中'}">--</c:when>
                                                <c:when test="${entity.EXAM_STATE eq '--'}">--</c:when>
                                                <c:otherwise>
                                                    <c:if test="${empty entity.EXAM_SCORE2}">--</c:if>
                                                    <c:if test="${not empty entity.EXAM_SCORE2}">${entity.EXAM_SCORE2 }<br><span class="gray9"><c:if test="${not empty entity.COURSE_SCHEDULE}">(形考比例${entity.COURSE_SCHEDULE}%)</c:if></span></c:if>
                                                </c:otherwise>
                                            </c:choose>

                                        </td>
                                        <td>
                                            <c:if test="${entity.EXAM_STATE eq '已通过'}"><span class="text-green">已通过</span><br><span class="gray9">(学情已锁定)</span></c:if>
                                            <c:if test="${entity.EXAM_STATE eq '未通过'}"><span class="text-red">未通过</span><br><span class="gray9">(学情已锁定)</span></c:if>
                                            <c:if test="${entity.EXAM_STATE eq '学习中'}">
                                                <c:if test="${entity.LOGIN_COUNT eq '0' or empty entity.LOGIN_COUNT}"><span class="gray9">未学习</span></c:if>
                                                <c:if test="${entity.LOGIN_COUNT ne '0' and not empty entity.LOGIN_COUNT}"><span class="text-orange">学习中</span></c:if>
                                            </c:if>
                                            <c:if test="${entity.EXAM_STATE eq '登记中'}"><span class="">登记中</span></c:if>
                                            <c:if test="${entity.EXAM_STATE eq '--'}"><span class="">--</span></c:if>
                                        </td>
                                        <td><a href="${ctx}/studymanage/courseCondition/${entity.TERMCOURSE_ID}/${entity.STUDENT_ID}/${entity.COURSE_ID}" class="operion-item" data-toggle="tooltip" title="查看课程学情明细"><i class="fa fa-fw fa-view-more"></i></a></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                        </tbody>
                    </table>
                    <div class="text-orange">注：已通过、未通过状态的学情数据，是以登记成绩时的学情数据为准，登记后学情数据将会被锁定，将不会再同步更新最新的学情数据！</div>
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

