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
        	学员学情
    </h1>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li class="active">学员学情</li>
    </ol>
</section>
<section class="content">
    <div class="nav-tabs-custom margin-bottom-non">
        <ul class="nav nav-tabs nav-tabs-lg">
            <li>
            	<a href="${ctx}/home/class/learn/courseList"  >课程学情</a>
            </li>
            <li class="active">
            	<a href="${ctx}/home/class/learn/list"  >学员学情</a>
            </li>
        </ul>
        <div class="content">
            <div class="box">
                <form class="form-horizontal" method="post" id="listForm">
                    <div class="box-body">
                        <div class="row reset-form-horizontal pad-t15">

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
                                                <option value="${map.key}" <c:if test="${map.key==param.PYCC}">selected='selected'</c:if>>
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
<%--
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3">学习进度</label>
                                    <div class="col-sm-9">
                                        <select name="studyState" class="form-control selectpicker show-tick">
                                            <option value="">全部</option>
                                            <option value="1" <c:if test="${param['studyState']==1}">selected</c:if>>正常</option>
                                            <option value="2" <c:if test="${param['studyState']==2}">selected</c:if>>落后</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3">学习成绩</label>
                                    <div class="col-sm-9">
                                        <select name="examState" class="form-control selectpicker show-tick">
                                            <option value="">全部</option>
                                            <option value="1" <c:if test="${param['examState']==1}">selected</c:if>>合格</option>
                                            <option value="2" <c:if test="${param['examState']==2}">selected</c:if>>不合格</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
--%>
                        </div>
                    </div>
                    <div class="box-footer">
                        <div class="pull-right"><button type="reset" class="btn btn-default">重置</button></div>
                        <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary" id="submit_form">搜索</button></div>
                    </div>
                </form>
            </div>
            <div class="box box-border margin-bottom-none">
                <div class="box-header with-border">
                    <%--
                      <div class="filter-tabs clearfix">
                          <ul class="list-unstyled">
                              <li id="search_actived_0" data-num="0" <c:if test="${empty param['studyState'] and empty param['examState']}">class="actived"</c:if>>全部(${infoNum.ALL_STUDENT})</li>
                              <li id="search_actived_1" data-num="1" <c:if test="${param['studyState']==2}">class="actived"</c:if>>学习进度落后(${infoNum.STUDY_BEHIND})</li>
                              <li id="search_actived_2" data-num="2" <c:if test="${param['studyState']==1}">class="actived"</c:if>>学习进度正常(${infoNum.STUDY_NORMAL})</li>
                              <li id="search_actived_3" data-num="3" <c:if test="${param['examState']==2}">class="actived"</c:if>>学习成绩不合格(${infoNum.SCORE_PASS})</li>
                              <li id="search_actived_4" data-num="4" <c:if test="${param['examState']==1}">class="actived"</c:if>>学习成绩合格(${infoNum.SCORE_UNPASS})</li>
                          </ul>
                      </div>
                    --%>
                    <div class="filter-tabs clearfix">
                        <%--
                      <ul class="list-unstyled">
                          <li id="search_actived_0" data-num="0" <c:if test="${param['passState'] eq '0'}">class="actived"</c:if> >不满足最低毕业学分(${infoNum.unpass})</li>
                          <li id="search_actived_1" data-num="1" <c:if test="${param['passState'] eq '1'}">class="actived"</c:if> >已满足最低毕业学分(${infoNum.pass})</li>
                      </ul>
                        --%>
                    </div>
                    <div class="fr">
                        <a href="${ctx}/home/class/learn/exportStudentConditionList/${pageInfoTotalElements}" class="btn btn-default btn-sm margin_l10" data-role="export"><i class="fa fa-fw fa-sign-out"></i> 导出学员学情统计表</a>
                    </div>
                    <h3 class="box-title pad-t5">学情列表</h3>
                </div>
                <div class="box-body">
                    <div class="table-responsive">
                        <table id="dtable" class="table table-bordered table-striped table-cell-ver-mid text-center">
                            <thead>
                            <tr>
                            <%--
                                <th><input type="checkbox" class="select-all"></th>
                                <th>学员姓名</th>
                                <th>学习总次数</th>
                                <th>学习总时长</th>
                                <th>上次学习间隔天数</th>
                                <th>全部课程</th>
                                <th>学习进度落后课程</th>
                                <th>学习进度正常课程</th>
                                <th>学习成绩不合格课程</th>
                                <th>学习成绩合格课程</th>
                                <th>操作</th>
                            --%>
                                <th>头像</th>
                                <th width="15%">个人信息</th>
                                <th width="22%">报读信息</th>
                                <shiro:hasRole name="班主任">
                                    <th>已获学分/<br/>总学分</th>
                                </shiro:hasRole>
                                <th>课程<br/>总数</th>
                                <th>已通过<br/>课程数</th>
                                <th>未通过<br/>课程数</th>
                                <th>学习中<br/>课程数</th>
                                <th>未学习<br/>课程数</th>
                                <th>学习<br/>总次数</th>
                                <th>学习<br/>总时长</th>
                                <th>当前<br/>学习状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${not empty pageInfo.content}">
                                    <c:forEach items="${pageInfo.content}" var="entity">
                                        <tr>
                                            <td class="text-center" width="90">
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
                                                    姓名：${entity.XM }<br>
                                                    学号：${entity.XH }<br>
                                                    手机：${entity.SJH }
                                                </div>
                                            </td>
                                            <td class="text-left">
                                                <div class="text-left">
                                                    专业层次：${entity.PYCC_NAME }<br>
                                                    入学年级：${entity.YEAR_NAME }<br>
                                                    入学学期：${entity.START_GRADE }<br>
                                                    报读专业：${entity.ZYMC }<br>
                                                    教务班级：${entity.BJMC }<br>
                                                    学习中心：${entity.SC_NAME}
                                                </div>
                                            </td>
                                            <shiro:hasRole name="班主任">
                                                <td>
                                                    <div>
                                                        <span class="f18">${entity.SUM_GET_CREDITS }/</span>${entity.ZXF }<br>

                                                        <c:if test="${entity.XJZT eq '8' }">
                                                            <span class="text-green">已毕业</span>
                                                        </c:if>
                                                        <c:if test="${entity.XJZT ne '8' }">
                                                            <c:choose>
                                                                <c:when test="${entity.SUM_GET_CREDITS>0 && entity.ZDBYXF>0 && entity.SUM_GET_CREDITS >=entity.ZDBYXF}">
                                                                    <span class="text-green">已满足毕业要求</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="text-orange">未满足毕业要求</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                    </div>
                                                </td>
                                            </shiro:hasRole>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty entity.REC_COUNT }">--</c:when>
                                                    <c:otherwise>${entity.REC_COUNT }</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty entity.PASS_REC_COUNT }">--</c:when>
                                                    <c:otherwise>${entity.PASS_REC_COUNT }</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty entity.UNPASS_REC_COUNT }">--</c:when>
                                                    <c:otherwise>${entity.UNPASS_REC_COUNT }</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty entity.LEARNING_REC_COUNT }">--</c:when>
                                                    <c:otherwise>${entity.LEARNING_REC_COUNT }</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty entity.UNLEARN_REC_COUNT }">--</c:when>
                                                    <c:otherwise>${entity.UNLEARN_REC_COUNT }</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty entity.LOGIN_TIMES }">--</c:when>
                                                    <c:otherwise>${entity.LOGIN_TIMES }</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty entity.LOGIN_TIME_COUNT }">--</c:when>
                                                    <c:otherwise>${entity.LOGIN_TIME_COUNT }</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty entity.LOGIN_TIMES }">
                                                        <span class="gray9">离线<br>(从未学习)</span>
                                                    </c:when>
                                                    <c:when test="${entity.IS_ONLINE eq 'Y'}">
                                                        <span class="text-green">在线<br>(${entity.DEVICE}在线)</span>
                                                    </c:when>
                                                    <c:when test="${entity.IS_ONLINE eq 'N'}">
															<span class="gray9">离线<br>
																<c:choose>
                                                                    <c:when test="${empty entity.LOGIN_TIMES or entity.LOGIN_TIMES eq '0'}">(从未学习)</c:when>
                                                                    <c:when test="${not empty entity.LOGIN_TIMES and entity.LOGIN_TIMES ne '0' and not empty entity.LAST_LOGIN_TIME}">(${entity.LAST_LOGIN_TIME}天未学习)</c:when>
                                                                    <c:otherwise>(从未学习)</c:otherwise>
                                                                </c:choose>
															</span>
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <div class="data-operion">
                                                    <a href="studentStudyDetail/${entity.STUDENT_ID}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td align="center" colspan="13">暂无数据</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                    <tags:pagination page="${pageInfo}" paginationSize="10" />
                </div>
            </div>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
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

 
    $("#search_actived_0,#search_actived_1").click(function () {
        var data = Number($(this).attr("data-num"));
        switch (data){
            case 0:
                $("input[name='passState']").val("0");//不满足
                break;
            case 1:
                $("input[name='passState']").val("1");//满足
                break;
            default: $("input[name='passState']").val("");
        }
        $("#submit_form").click();
    });
<%-- 原来的项目 据说优化，先注释，说不定哪天又要
--%>
</script>
</body>
</html>
