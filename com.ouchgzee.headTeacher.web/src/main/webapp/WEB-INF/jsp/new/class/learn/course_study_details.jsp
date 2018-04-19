<%--
  Created by IntelliJ IDEA.
  User: Min
  Date: 2017/6/17
  Time: 18:02
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>班主任平台 - 学员学情</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
    <button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
        <li><a href="#">学习管理</a></li>
        <li><a href="#">学情分析</a></li>
        <li class="active">课程学情明细</li>
    </ol>
</section>
<section class="content">
    <div class="nav-tabs-custom no-margin">
        <form id="listForm" class="form-horizontal">
            <div class="tab-content">
                <div class="tab-pane active" id="tab_top_1">
                    <div class="box box-border">
                        <div class="box-body">
                            <div class="row pad-t15">
                                <div class="col-sm-4">
                                    <div class="form-group">
                                        <label class="control-label col-sm-3 text-nowrap">开课学期</label>
                                        <div class="col-sm-9">
                                            <select class="selectpicker show-tick form-control" name="gradeId" id="gradeId" data-size="5" data-live-search="true">
                                                <option value="all" selected="selected">全部学期</option>
                                                <c:forEach items="${gradeMap}" var="map">
                                                       <option value="${map.key}" <c:if test="${currentGradeId eq map.key }">selected="selected"</c:if>>${map.value}</option>
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
                                            <select class="selectpicker show-tick form-control" name="SPECIALTY_ID" id="specialty_id" data-size="5" data-live-search="true">
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
                                        <label class="control-label col-sm-3 text-nowrap">学习进度</label>
                                        <div class="col-sm-9">
                                            <div class="input-group">
                                                <div class="input-group-btn">
                                                    <select class="form-control input-group-select bg-white" name="scheduleSymbol">
                                                        <option value="=" <c:if test="${empty param.scheduleSymbol || param.scheduleSymbol eq '=' }">selected="selected"</c:if>>等于</option>
                                                        <option value="&gt;" <c:if test="${param.scheduleSymbol eq '>' }">selected="selected"</c:if>>大于</option>
                                                        <option value="&gt;=" <c:if test="${param.scheduleSymbol eq '>=' }">selected="selected"</c:if>>大于等于</option>
                                                        <option value="&lt;" <c:if test="${param.scheduleSymbol eq '<' }">selected="selected"</c:if> >小于</option>
                                                        <option value="&lt;=" <c:if test="${param.scheduleSymbol eq '<=' }">selected="selected"</c:if>>小于等于</option>
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
                                                        <option value="&gt;" <c:if test="${param.examSymbol eq '>' }">selected="selected"</c:if>>大于</option>
                                                        <option value="&gt;=" <c:if test="${param.examSymbol eq '>=' }">selected="selected"</c:if>>大于等于</option>
                                                        <option value="&lt;" <c:if test="${param.examSymbol eq '<' }">selected="selected"</c:if>>小于</option>
                                                        <option value="&lt;=" <c:if test="${param.examSymbol eq '<=' }">selected="selected"</c:if>>小于等于</option>
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
                    <div class="box box-border margin-bottom-none">
                        <div class="box-header with-border">
                            <%--<h3 class="box-title pad-t5">课程学情明细列表</h3>--%>
                            <div class="filter-tabs clearfix pull-left no-margin">
<%--
                                <ul class="list-unstyled">
                                    <li <c:if test="${empty param.EXAM_STATE}">class="actived"</c:if>>全部(${all})</li>
                                    <li <c:if test="${param.EXAM_STATE eq '0'}">class="actived"</c:if>>已通过(${pass})</li>
                                    <li <c:if test="${param.EXAM_STATE eq '1'}">class="actived"</c:if>>未通过(${unpass})</li>
                                </ul>
--%>
                            </div>
                            <div class="pull-right no-margin">
                                <a href="${ctx}/home/class/learn/exportCourseLearnConditionDetails/${pageInfo.getTotalElements()}" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出课程学情明细表</a>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="table-responsive">
                                <table class="table table-bordered table-striped vertical-mid text-center table-font">
                                    <thead>
                                    <tr>
                                        <th>头像</th>
                                        <th>个人信息</th>
                                        <th>报读信息</th>
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
                                                            手机：${entity.SJH}
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
                                                        <c:if test="${empty entity.SCHEDULE}">--</c:if>
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
                                                        <c:if test="${entity.EXAM_STATE eq '学习中'}"><span class="text-orange">学习中</span></c:if>
                                                        <c:if test="${entity.EXAM_STATE eq '登记中'}"><span class="">登记中</span></c:if>
                                                        <c:if test="${entity.EXAM_STATE eq '--'}"><span class="">--</span></c:if>
                                                    </td>
                                                    <td><a href="${ctx}/home/class/learn/courseCondition/${entity.TERMCOURSE_ID}/${entity.STUDENT_ID}/${entity.COURSE_ID}" class="operion-item" data-toggle="tooltip" title="查看课程学情明细"><i class="fa fa-fw fa-view-more"></i></a></td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise><tr><td colspan="12">暂无数据！</td></tr></c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                                <tags:pagination page="${pageInfo}" paginationSize="5" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
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

    //切换到课程学情
    function changType(type) {
        if (type=="1") {
            window.location.href = "${ctx}/home/class/learn/courseList";
        } else {
            window.location.href = "${ctx}/home/class/learn/list";
        }
    }
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

