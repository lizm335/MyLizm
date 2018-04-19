<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>管理系统</title>

    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
    <ol class="breadcrumb oh">
        <li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
        <li class="active">授课管理</li>
    </ol>
</section>
<section class="content">
    <form id="listForm" class="form-horizontal">

        <div class="nav-tabs-custom no-margin">
            <ul class="nav nav-tabs nav-tabs-lg">
                <li <c:if test="${param['search_EQ_schoolTeach'] == 'onlineLesson' || empty param['search_EQ_schoolTeach']}">class="active"</c:if>><a href="list?search_EQ_schoolTeach=onlineLesson" target="_self">直播详情</a></li>
                <li <c:if test="${param['search_EQ_schoolTeach'] == 'Resource' || empty param['search_EQ_schoolTeach']}">class="active"</c:if>><a href="list?search_EQ_schoolTeach=" target="_self">共享资料</a></li>
            </ul>

            <div class="tab-content">
                <div class="box box-border">
                    <div class="box-body">
                        <div class="row pad-t15">
                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">课程代码</label>
                                    <div class="col-sm-9">
                                        <input class="form-control" type="text" name="search_LIKE_kch" value="${param.search_LIKE_kch}">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">课程名称</label>
                                    <div class="col-sm-9">
                                        <input class="form-control" type="text" name="search_LIKE_kcmc" value="${param.search_LIKE_kcmc}">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">教学方式</label>
                                    <div class="col-sm-9">
                                        <select name="search_EQ_wsjxzk" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
                                            <option value="" selected='selected'>请选择</option>
                                            <c:forEach items="${wsjxzkMap}" var="s">
                                                <option value="${s.key}"  <c:if test="${s.key==param['search_EQ_wsjxzk']}">selected='selected'</c:if>>${s.value}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <c:choose>
                                <c:when test="${param['search_EQ_courseNature'] != '3'}">
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label class="control-label col-sm-3 text-nowrap">适用专业</label>
                                            <div class="col-sm-9">
                                                <select name="search_LIKE_syzy" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
                                                    <option value="" selected='selected'>请选择</option>
                                                    <c:forEach items="${syzyMap}" var="s">
                                                        <option value="${s.key}"  <c:if test="${s.key==param['search_LIKE_syzy']}">selected='selected'</c:if>>${s.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label class="control-label col-sm-3 text-nowrap">可替换课</label>
                                            <div class="col-sm-9">
                                                <select name="search_LIKE_replaceCourseId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true">
                                                    <option value="" selected='selected'>请选择</option>
                                                    <c:forEach items="${replaceCourses}" var="course">
                                                        <option value="${course.courseId}"  <c:if test="${course.courseId==param['search_LIKE_replaceCourseId']}">selected='selected'</c:if>>${course.kcmc}(${course.kch})</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="control-label col-sm-3 text-nowrap">状态</label>
                                    <div class="col-sm-9">
                                        <select name="search_EQ_isEnabled" class="selectpicker show-tick form-control" 	data-size="5" data-live-search="true">
                                            <option value="">请选择</option>
                                            <option value="1" <c:if test="${param.search_EQ_isEnabled=='1'}">selected='selected'</c:if>>已启用</option>
                                            <option value="2" <c:if test="${param.search_EQ_isEnabled=='2'}">selected='selected'</c:if>>建设中</option>
                                            <option value="0" <c:if test="${param.search_EQ_isEnabled=='0'}">selected='selected'</c:if>>暂无资源</option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                        </div>

                    </div><!-- /.box-body -->
                    <div class="box-footer">
                        <div class="btn-wrap">
                            <button type="button" class="btn min-width-90px btn-default btn-reset">重置</button>
                        </div>
                        <div class="btn-wrap">
                            <button type="submit" class="btn min-width-90px btn-primary">搜索</button>
                        </div>
                    </div><!-- /.box-footer-->
                </div>
                <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
                <div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss="alert" class="close">×</button>${feedback.message}</div>
                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title pad-t5">课程列表</h3>
                        <div class="fr">
                            <div class="btn-wrap fl">
                                <c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnCreateFormal) || (param['search_EQ_courseNature'] == '2' && isBtnCreateExperience) || (param['search_EQ_courseNature'] == '3' && isBtnCreateReplace)}">
                                    <a href="create?courseNature=${param['search_EQ_courseNature']}" class="btn btn-default btn-sm">	<i class="fa fa-fw fa-plus"></i> 新建课程</a>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <div class="box-body">
                        <div class="filter-tabs filter-tabs2 clearfix">
                            <ul class="list-unstyled">
                                <li lang=":input[name='search_EQ_isEnabled']" <c:if test="${empty param['search_EQ_isEnabled']}">class="actived"</c:if>>全部(${isEnabledNum + isNoResourcesNum + isConstructionNum})</li>
                                <li value="1" role=":input[name='search_EQ_isEnabled']" <c:if test="${param['search_EQ_isEnabled'] == 1 }">class="actived"</c:if>>已启用(${isEnabledNum})</li>
                                <li value="2" role=":input[name='search_EQ_isEnabled']" <c:if test="${param['search_EQ_isEnabled'] == '2' }">class="actived"</c:if>>建设中(${isConstructionNum})</li>
                                <li value="0" role=":input[name='search_EQ_isEnabled']" <c:if test="${param['search_EQ_isEnabled'] == '0' }">class="actived"</c:if>>暂无资源(${isNoResourcesNum})</li>
                            </ul>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped vertical-mid text-center table-font">
                                <thead>
                                <tr>
                                    <th>课程代码</th>
                                    <th>课程名称</th>
                                    <c:choose>
                                        <c:when test="${param['search_EQ_courseNature'] != '3'}">
                                            <th>课程属性</th>
                                            <!-- <th>适用行业</th> -->
                                            <th>适用专业</th>
                                            <shiro:lacksPermission name="/studymanage//edumanage/course/list$schoolModel">
                                                <th>学分</th>
                                            </shiro:lacksPermission>
                                            <th>学时</th>
                                        </c:when>
                                        <c:otherwise>
                                            <th>课程类型</th>
                                            <th>教学方式</th>
                                            <th>可替换课</th>
                                        </c:otherwise>
                                    </c:choose>
                                    <!-- <th>所属院校</th> -->
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${not empty pageInfo.content}">
                                        <c:forEach items="${pageInfo.content}" var="entity">
                                            <c:if test="${not empty entity}">
                                                <tr>
                                                    <td>
                                                        <input type="hidden" name="courseId" value="${entity.courseId}">
                                                            ${entity.kch}
                                                    </td>
                                                    <td>
                                                        <span class="kcmc">${entity.kcmc}</span>
                                                    </td>
                                                    <c:choose>
                                                        <c:when test="${param['search_EQ_courseNature'] != '3'}">
                                                            <td style="text-align: left;">
                                                                    <%-- 课程类型：${courseTypeMap[entity.courseType]}<br> --%>

                                                                教学方式：${wsjxzkMap[entity.wsjxzk]}<br>

                                                                    <%-- 课程性质：${courseNatureMap[entity.courseNature]}<br> --%>

                                                                课程层次：${pyccMap[entity.pycc]}<br>

                                                                    <%-- 学科：${subjectMap[entity.subject]}<br>

                                                                    学科门类：${categoryMap[entity.subject][entity.category]} --%>

                                                                课程类型：
                                                                <%-- <c:choose>
                                                                    <c:when test="${entity.courseCategory == 0}">普通课程</c:when>
                                                                    <c:when test="${entity.courseCategory == 1}">社会实践</c:when>
                                                                    <c:when test="${entity.courseCategory == 2}">毕业论文</c:when>
                                                                </c:choose> --%>
                                                                ${courseCategoryMap[entity.courseCategory]} 
                                                            </td>
                                                            <%-- <td>
                                                                ${syhyMap[entity.syhy]}
                                                            </td> --%>
                                                            <td>
                                                                <c:if test="${not empty entity.syzy}">
                                                                    <c:forEach items="${fn:split(entity.syzy, ',')}" var="syzy">
                                                                        ${syzyMap[syzy]} <br>
                                                                    </c:forEach>
                                                                </c:if>
                                                            </td>
                                                            <shiro:lacksPermission name="/studymanage//edumanage/course/list$schoolModel">
                                                                <td>${entity.credit}</td>
                                                            </shiro:lacksPermission>
                                                            <td>${entity.hour}</td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td>
                                                                <%-- <c:choose>
                                                                    <c:when test="${entity.courseCategory == 0}">普通课程</c:when>
                                                                    <c:when test="${entity.courseCategory == 1}">社会实践</c:when>
                                                                    <c:when test="${entity.courseCategory == 2}">毕业论文</c:when>
                                                                </c:choose> --%>
                                                                ${courseCategoryMap[entity.courseCategory]} 
                                                            </td>
                                                            <td>${wsjxzkMap[entity.wsjxzk]}</td>
                                                            <td>
                                                                <c:if test="${not empty entity.replaceCourseId}">
                                                                    <c:forEach items="${fn:split(entity.replaceCourseId, ',')}" var="courseId">
                                                                        <c:forEach items="${replaceCourses}" var="replaceCourse">
                                                                            <c:if test="${replaceCourse.courseId eq courseId}">
                                                                                ${replaceCourse.kcmc}(${replaceCourse.kch}) <br>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </c:forEach>
                                                                </c:if>
                                                            </td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                        <%-- <td>${entity.gjtOrg.orgName}</td> --%>
                                                    <c:if test="${entity.isEnabled==1}"><td class="text-green">已启用</td></c:if>
                                                    <c:if test="${entity.isEnabled==0}"><td class="text-red">暂无资源</td></c:if>
                                                    <c:if test="${entity.isEnabled==2}"><td class="text-orange">建设中</td></c:if>

                                                    <td class="data-operion">
                                                            <%-- <c:if test="${user.gjtOrg.id == entity.gjtOrg.id }"> --%>
                                                            <%-- <c:if test="${entity.isEnabled==1}">
                                                            <a href="share/${entity.courseId}"
                                                                class="operion-item" title="分享"
                                                                data-toggle="tooltip" title="共享课程" data-role='share'>
                                                                <i class="fa fa-share-alt"></i></a>
                                                            </c:if> --%>

                                                        <c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnViewFormal) || (param['search_EQ_courseNature'] == '2' && isBtnViewExperience) || (param['search_EQ_courseNature'] == '3' && isBtnViewReplace)}">
                                                            <a href="view/${entity.courseId}?courseNature=${param['search_EQ_courseNature']}" class="operion-item" title="查看">
                                                                <i class="fa fa-fw fa-view-more"></i></a>
                                                        </c:if>

                                                        <c:if test="${entity.isEnabled == 0 || entity.isEnabled == 2}">
                                                            <c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnUpdateFormal) || (param['search_EQ_courseNature'] == '2' && isBtnUpdateExperience) || (param['search_EQ_courseNature'] == '3' && isBtnUpdateReplace)}">
                                                                <a href="update/${entity.courseId}?courseNature=${param['search_EQ_courseNature']}"	class="operion-item" title="编辑">
                                                                    <i class="fa fa-edit"></i></a>
                                                            </c:if>

                                                            <c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnDeleteFormal) || (param['search_EQ_courseNature'] == '2' && isBtnDeleteExperience) || (param['search_EQ_courseNature'] == '3' && isBtnDeleteReplace)}">
                                                                <a href="javascript:void(0);" class="operion-item operion-del del-one" val="${entity.courseId}" title="删除" data-tempTitle="删除">
                                                                    <i class="fa fa-fw fa-trash-o"></i></a>
                                                            </c:if>
                                                        </c:if>

                                                        <c:if test="${entity.isEnabled == 0}">
                                                            <c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnConstructionFormal) || (param['search_EQ_courseNature'] == '2' && isBtnConstructionExperience) || (param['search_EQ_courseNature'] == '3' && isBtnConstructionReplace)}">
                                                                <a href="javascript:void(0);" class="operion-item" data-toggle="tooltip" title="设置为建设中" data-role='construction'>
                                                                    <i class="fa fa-wrench"></i></a>
                                                            </c:if>
                                                        </c:if>

                                                        <c:if test="${entity.isEnabled == 2}">
                                                            <c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnEnableFormal) || (param['search_EQ_courseNature'] == '2' && isBtnEnableExperience) || (param['search_EQ_courseNature'] == '3' && isBtnEnableReplace)}">
                                                                <a href="javascript:void(0);" class="operion-item" data-toggle="tooltip" title="设置为已启用" data-role='issue'>
                                                                    <i class="fa fa-play"></i></a>
                                                            </c:if>
                                                            <c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnNoResourcesFormal) || (param['search_EQ_courseNature'] == '2' && isBtnNoResourcesExperience) || (param['search_EQ_courseNature'] == '3' && isBtnNoResourcesReplace)}">
                                                                <a href="javascript:void(0);" class="operion-item" data-toggle="tooltip" title="设置为暂无资源" data-role='stop'>
                                                                    <i class="fa fa-warning"></i></a>
                                                            </c:if>
                                                        </c:if>

                                                        <c:if test="${entity.isEnabled == 1}">
                                                            <c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnConstructionFormal) || (param['search_EQ_courseNature'] == '2' && isBtnConstructionExperience) || (param['search_EQ_courseNature'] == '3' && isBtnConstructionReplace)}">
                                                                <a href="javascript:void(0);" class="operion-item" data-toggle="tooltip" title="设置为建设中" data-role='construction'>
                                                                    <i class="fa fa-wrench"></i></a>
                                                            </c:if>
                                                        </c:if>

                                                        <c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnChangeCourseNature) || (param['search_EQ_courseNature'] == '2' && isBtnChangeCourseNature)}">
                                                            <a href="javascript:void(0);" class="operion-item" data-toggle="tooltip" title="转换课程性质" data-role='change'>
                                                                <i class="fa fa-exchange"></i></a>
                                                        </c:if>

                                                        <c:if test="${((empty param['search_EQ_courseNature'] || param['search_EQ_courseNature'] == '1') && isBtnPreviewFormal) || (param['search_EQ_courseNature'] == '2' && isBtnPreviewExperience) || (param['search_EQ_courseNature'] == '3' && isBtnPreviewReplace)}">
                                                            <a href="${oclassHost}/processControl/previewCourse.do?formMap.COURSE_ID=${entity.courseId}" target="_blank" class="operion-item" title="预览课程">
                                                                <i class="fa fa-fw fa-simulated-login"></i></a>
                                                        </c:if>
                                                            <%-- </c:if> --%>

                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td align="center" colspan="9">暂无数据</td>
                                        </tr>
                                    </c:otherwise>
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
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">


</script>
</body>
</html>
