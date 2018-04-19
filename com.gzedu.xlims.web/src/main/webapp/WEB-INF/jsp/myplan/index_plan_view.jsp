<%--
  Created by IntelliJ IDEA.
  User: Min
  Date: 2017/8/24
  Time: 14:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<c:choose>
    <c:when test="${not empty plan}">
        <table class="table f12 vertical-middle text-center margin-bottom-none">
            <thead class="with-bg-gray">
                <tr>
                    <th width="12%">级别</th>
                    <th>标题</th>
                    <th width="18%">计划完成时间</th>
                    <th width="16%">开始时间</th>
                    <th width="16%">结束时间</th>
                    <th width="12%">状态</th>
                </tr>
            </thead>
            <tbody class="my-task">
                <c:forEach items="${plan}" var="entity">
                    <tr>
                        <td>
                            <c:choose>
                                <c:when test="${entity.PLAN_LEVEL eq '1'}"><small class="label label-default text-no-bold">一般</small></c:when>
                                <c:when test="${entity.PLAN_LEVEL eq '2'}"><small class="label label-warning text-no-bold">优先</small></c:when>
                                <c:when test="${entity.PLAN_LEVEL eq '3'}"><small class="label label-danger text-no-bold">紧急</small></c:when>
                                <c:otherwise>--</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="text-left">
                                ${entity.PLAN_TITLE}
                            </div>
                        </td>
                        <td>${entity.PLAN_FINISH_TIME}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty entity.START_DATE}">${entity.START_DATE}</c:when>
                                <c:otherwise>--</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty entity.END_DATE}">${entity.END_DATE}</c:when>
                                <c:otherwise>--</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${entity.PLAN_STATUS eq '0'}"><span class="gray9">未开始</span></c:when>
                                <c:when test="${entity.PLAN_STATUS eq '1'}"><span class="text-orange">进行中</span></c:when>
                                <c:when test="${entity.PLAN_STATUS eq '2'}"><span class="text-green">已完成</span></c:when>
                                <c:otherwise>--</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <table width="100%" height="100%">
            <tbody>
            <tr>
                <td>
                    <div class="text-center gray9 f12">
                        <i class="fa fa-fw fa-exclamation-circle f18"></i>
                        <span>你暂无计划，<a href="${ctx}/myPlan/createPlan"><u>去新建一个计划</u></a>！</span>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

