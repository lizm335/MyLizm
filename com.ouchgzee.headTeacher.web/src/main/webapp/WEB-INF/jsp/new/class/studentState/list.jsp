<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 学员状态</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
    <h1>
        学员状态
    </h1>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li class="active">学员状态</li>
    </ol>
</section>
<section class="content">
    <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
    <div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

    <form class="form-horizontal">
        <div class="box">
            <div class="box-body">
                <div class="row reset-form-horizontal pad-t15">
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3">报读产品</label>
                            <div class="col-sm-9">
                                <select name="search_specialtyId" class="form-control selectpicker show-tick" <%--multiple--%> data-size="10" data-live-search="true">
                                    <option value="">- 请选择 -</option>
                                    <c:forEach var="item" items="${specialtyMap}">
                                        <option value="${item.key}" <c:if test="${param['search_specialtyId']==item.key}">selected</c:if>>${item.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3">学员姓名</label>
                            <div class="col-sm-9">
                                <input class="form-control" type="text" name="search_xm" value="${param['search_xm']}">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label col-sm-3">学习状态</label>
                            <div class="col-sm-9">
                                <select name="search_learningState" class="form-control selectpicker show-tick">
                                    <option value="">- 请选择 -</option>
                                    <option value="1" <c:if test="${param['search_learningState']=='1'}">selected</c:if>>已激活</option>
                                    <option value="0" <c:if test="${param['search_learningState']=='0'}">selected</c:if>>未激活</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="box-footer">
                <div class="pull-right"><button type="reset" class="btn btn-default">重置</button></div>
                <div class="pull-right margin_r15"><button type="submit" class="btn min-width-90px btn-primary">搜索</button></div>
            </div>
        </div>
    </form>

    <div class="box margin-bottom-none">
        <div class="box-header with-border">
            <div class="fr">
                <a href="exportInfo?sortType=${sortType}&${searchParams}" target="_blank" class="btn btn-success btn-outport"><i class="fa fa-fw fa-sign-out"></i> 导出状态信息</a>
            </div>
        </div>
        <div class="box-body">
            <table id="dtable" class="table table-bordered table-striped table-container">
                <thead>
                <tr>
                    <th><input type="checkbox" class="select-all"></th>
                    <th>学员姓名</th>
                    <th>报读时间</th>
                    <th>报读产品</th>
                    <th>年级</th>
                    <th>学习状态</th>
                    <th>资料完善</th>
                    <th>缴费状态</th>
                    <th>学籍状态</th>
                    <th>毕业状态</th>
                    <th>学位证书</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty infos && infos.numberOfElements > 0}">
                        <c:forEach items="${infos.content}" var="info">
                            <c:if test="${not empty info}">
                                <tr>
                                    <td><input type="checkbox" name="ids"
                                               data-id="${info.studentId}" data-name="check-id"
                                               value="${info.studentId}"></td>
                                    <td>${info.xm}</td>
                                    <td><fmt:formatDate value="${info.signupDt}" type="date" /></td>
                                    <td>${info.zymc}</td>
                                    <td>${info.gradeName}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${info.learningState==0}">未激活</c:when>
                                            <c:otherwise>已激活</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${info.dataState==1}">已完善</c:when>
                                            <c:otherwise>待完善</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${info.feeStatus==1}">已缴费</c:when>
                                            <c:otherwise>未缴费</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><dic:getLabel typeCode="StudentNumberStatus" code="${info.xjzt}" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${info.receiveStatus==1||info.receiveStatus==2}">已毕业</c:when>
                                            <c:otherwise>未毕业</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${info.certificateNum}</td>
                                    <td>
                                        <div class="data-operion">
                                            <a href="view/${info.studentId}?learningState=${info.learningState}&feeStatus=${info.feeStatus}" class="operion-item" data-toggle="tooltip" title="查看详情"><i class="fa fa-fw fa-view-more"></i></a>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td align="center" colspan="15">暂无数据</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>

            <tags:pagination page="${infos}" paginationSize="10" />
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
</body>
</html>
