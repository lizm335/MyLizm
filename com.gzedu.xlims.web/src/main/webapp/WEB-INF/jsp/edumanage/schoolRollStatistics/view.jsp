<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>管理系统-学籍统计</title>
    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
    <section class="content-header">
        <ol class="breadcrumb oh">
            <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
            <li><a href="#">学籍管理</a></li>
            <li class="active">学籍统计</li>
        </ol>
    </section>

    <section class="content">
        <div class="box margin-bottom-none">
            <div class="box-body">
                <div class="row">
                    <div class="col-md-6">
                        <!-- 学籍状态统计 -->
                        <div class="panel panel-default">
                            <div class="panel-heading no-pad-top">
                                <div class="row">
                                    <div class="col-sm-4">
                                        <h3 class="panel-title text-bold text-nowrap pad-t15">学籍状态统计</h3>
                                    </div>
                                    <div class="col-sm-8 row-offset-3px" id="signupInfo">
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" id="gradeId" name="gradeId" data-live-search="true">
                                                    <option value="">全部学期</option>
                                                    <c:forEach items="${gradeMap}" var="map">
                                                        <option value="${map.key}" <c:if test="${map.key==((not empty param['gradeId']) ? param['gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="pycc" id="pycc" data-live-search="true">
                                                    <option value="">全部层次</option>
                                                    <c:forEach items="${pyccMap}" var="map">
                                                        <option value="${map.key}">${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="signupSpecialtyId" id="signupSpecialtyId" data-live-search="true">
                                                    <option value="">全部专业</option>
                                                    <c:forEach items="${specialtyMap}" var="map">
                                                        <option value="${map.key}">${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-body">
                                <h3 class="cnt-box-title f16">异动统计</h3>
                                <div id="pieChart" data-role="chart" style="height:170px" class="margin_b15"></div>

                                <h3 class="cnt-box-title f16 margin_b10">学籍状态</h3>
                                <div class="row stu-info-status">
                                    <div class="col-sm-3 col-xs-6">
                                        <div class="text-green f24 text-center"><span id="alreadyRegNum">0</span></div>
                                        <div class="text-center gray6 f12">已注册</div>
                                    </div>
                                    <div class="col-sm-3 col-xs-6">
                                        <div class="text-red f24 text-center"><span id="notRegNum">0</span></div>
                                        <div class="text-center gray6 f12">未注册</div>
                                    </div>
                                    <div class="col-sm-3 col-xs-6">
                                        <div class="text-blue f24 text-center"><span id="perfectSignupNum">0</span></div>
                                        <div class="text-center gray6 f12">资料完整</div>
                                    </div>
                                    <div class="col-sm-3 col-xs-6">
                                        <div class="text-yellow f24 text-center"><span id="notPerfectSignupNum">0</span></div>
                                        <div class="text-center gray6 f12">资料不完整</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <!-- 用户属性统计 -->
                        <div class="panel panel-default">
                            <div class="panel-heading no-pad-top">
                                <div class="row">
                                    <div class="col-sm-4">
                                        <h3 class="panel-title text-bold text-nowrap pad-t15">用户属性统计</h3>
                                    </div>
                                    <div class="col-sm-8 row-offset-3px" id="signUpUser">
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="USER_GRADE_ID" id="user_grade_id" data-live-search="true">
                                                    <option value="">全部学期</option>
                                                    <c:forEach items="${gradeMap}" var="map">
                                                        <option value="${map.key}" <c:if test="${map.key==((not empty param['gradeId']) ? param['gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="USER_PYCC_ID" id="user_pycc_id" data-live-search="true">
                                                    <option value="">全部层次</option>
                                                    <c:forEach items="${pyccMap}" var="map">
                                                        <option value="${map.key}">${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="USER_SPECIALTY_ID" id="user_specialty_id" data-live-search="true">
                                                    <option value="">全部专业</option>
                                                    <c:forEach items="${specialtyMap}" var="map">
                                                        <option value="${map.key}"<c:if test="${map.key==param['search_EQ_signupSpecialtyId']}">selected='selected'</c:if> >${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-body">
                                <h3 class="cnt-box-title f16 margin_b15">性别</h3>
                                <div class="row">
                                    <div class="col-xs-8 per-icon-box margin_t10" id="femalePic">
                                    </div>
                                    <div class="col-xs-2 text-red text-nowrap">
                                        <div class="f24"><span id="femalePercent">0</span>%</div>
                                        <div>女:<span id="female">0</span>人</div>
                                    </div>
                                </div>
                                <div class="row margin_t10">
                                    <div class="col-xs-8 per-icon-box margin_t10" id="malePic">
                                    </div>
                                    <div class="col-xs-2 text-blue text-nowrap">
                                        <div class="f24"><span id="malePercent">0</span>%</div>
                                        <div>男:<span id="male">0</span>人</div>
                                    </div>
                                </div>

                                <h3 class="cnt-box-title f16">年龄</h3>
                                <div id="barChart" data-role="chart" style="height:130px;"></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <!-- 学习中心统计 -->
                        <div class="panel panel-default">
                            <div class="panel-heading no-pad-top">
                                <div class="row">
                                    <div class="col-sm-4">
                                        <h3 class="panel-title text-bold text-nowrap pad-t15">学习中心统计</h3>
                                    </div>
                                    <div class="col-sm-8 row-offset-3px" id="signUpCenter">
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="CENTER_GRADE_ID" id="center_grade_id" data-live-search="true">
                                                    <option value="">全部学期</option>
                                                    <c:forEach items="${gradeMap}" var="map">
                                                        <option value="${map.key}" <c:if test="${map.key==((not empty param['gradeId']) ? param['gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <div class="form-group margin_t10 margin-bottom-none">
                                                    <select class="form-control input-xs" name="CENTER_PYCC_ID" id="center_pycc_id" data-live-search="true">
                                                        <option value="">全部层次</option>
                                                        <c:forEach items="${pyccMap}" var="map">
                                                            <option value="${map.key}">${map.value}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="CENTER_SPECIALTY_ID" id="center_specialty_id" data-live-search="true">
                                                    <option value="">全部专业</option>
                                                    <c:forEach items="${specialtyMap}" var="map">
                                                        <option value="${map.key}"<c:if test="${map.key==param['search_EQ_signupSpecialtyId']}">selected='selected'</c:if> >${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-body">
                                <div id="mapChart1" data-role="chart" style="height:270px"></div>
                            </div>
                        </div>
                    </div>
                    <%--
                    <div class="col-md-6">
                        <!-- 区域统计 -->
                        <div class="panel panel-default">
                            <div class="panel-heading no-pad-top">
                                <div class="row">
                                    <div class="col-sm-4">
                                        <h3 class="panel-title text-bold text-nowrap pad-t15">区域统计</h3>
                                    </div>
                                    <div class="col-sm-8 row-offset-3px" id="payCost">
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="AREA_GRADE_ID" id="area_grade_id" data-live-search="true">
                                                    <option value="">全部学期</option>
                                                    <c:forEach items="${gradeMap}" var="map">
                                                        <option value="${map.key}" <c:if test="${map.key==((not empty param['gradeId']) ? param['gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="AREA_PYCC_ID" id="area_pycc_id" data-live-search="true">
                                                    <option value="">全部层次</option>
                                                    <c:forEach items="${pyccMap}" var="map">
                                                        <option value="${map.key}">${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="AREA_SPECIALTY_ID" id="area_specialty_id" data-live-search="true">
                                                    <option value="">全部专业</option>
                                                    <c:forEach items="${specialtyMap}" var="map">
                                                        <option value="${map.key}"<c:if test="${map.key==param['search_EQ_signupSpecialtyId']}">selected='selected'</c:if> >${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-body">
                                <div id="mapChart2" data-role="chart" style="height:300px"></div>
                            </div>
                        </div>

                    </div>
                     --%>
                    <div class="col-md-6">
                        <!-- 学历层次统计 -->
                        <div class="panel panel-default">
                            <div class="panel-heading no-pad-top">
                                <div class="row">
                                    <div class="col-sm-4">
                                        <h3 class="panel-title text-bold text-nowrap pad-t15">学历层次统计</h3>
                                    </div>
                                    <div class="col-sm-8 row-offset-3px" id="signUpPycc">
                                        <div class="col-sm-4 col-sm-offset-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="PYCC_GRADE_ID" id="pycc_grade_id" data-live-search="true">
                                                    <option value="">全部学期</option>
                                                    <c:forEach items="${gradeMap}" var="map">
                                                        <option value="${map.key}" <c:if test="${map.key==((not empty param['gradeId']) ? param['gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="PYCC_SPECIALTY_ID" id="pycc_specialty_id" data-live-search="true">
                                                    <option value="">全部专业</option>
                                                    <c:forEach items="${specialtyMap}" var="map">
                                                        <option value="${map.key}"<c:if test="${map.key==param['search_EQ_signupSpecialtyId']}">selected='selected'</c:if> >${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-body">
                                <div id="pieChart2" data-role="chart" style="height:270px"></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <!-- 报读专业统计 -->
                        <div class="panel panel-default">
                            <div class="panel-heading no-pad-top">
                                <div class="row">
                                    <div class="col-sm-4">
                                        <h3 class="panel-title text-bold text-nowrap pad-t15">报读专业统计</h3>
                                    </div>
                                    <div class="col-sm-8 row-offset-3px" id="specialInfo">
                                        <div class="col-sm-4 col-sm-offset-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="SPECIAL_GRADE_ID" id="special_grade_id" data-live-search="true">
                                                    <option value="">全部学期</option>
                                                    <c:forEach items="${gradeMap}" var="map">
                                                        <option value="${map.key}" <c:if test="${map.key==((not empty param['gradeId']) ? param['gradeId'] : defaultGradeId)}">selected='selected'</c:if>>${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="form-group margin_t10 margin-bottom-none">
                                                <select class="form-control input-xs" name="SPECIAL_PYCC_ID" id="special_pycc_id" data-live-search="true">
                                                    <option value="">全部层次</option>
                                                    <c:forEach items="${pyccMap}" var="map">
                                                        <option value="${map.key}">${map.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-body">
                                <div id="specialChart" data-role="chart" style="height:270px;">
                                    <div style="height:270px;overflow:auto;" id="specialInfoChart">

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 底部 -->
    <%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

    <!-- echarts 3 -->
    <script src="${ctx}/static/plugins/echarts/echarts.min.js"></script>
    <script type="text/javascript" src="${ctx}/static/plugins/echarts/china.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/edumanage/schoolRollStatistics/statistics_view.js"></script>
</body>
</html>
