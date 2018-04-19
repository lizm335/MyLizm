<%--
  Created by IntelliJ IDEA.
  User: Min
  Date: 2017/5/23
  Time: 17:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>查看历史成绩</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>

<div class="box no-border no-shadow margin-bottom-none">
    <div class="box-header with-border">
        <h3 class="box-title">查看历史成绩</h3>
    </div>
    <div class="box-body pad-l20 pad-r20">
        <div class="slim-Scroll">
            <table width="100%" class="table-basic text-center table-gray-th">
                <c:choose>
                    <c:when test="${not empty resultList}">
                        <c:forEach items="${resultList}" var="item">
                            <thead>
                            <tr>
                                <th colspan="4">
                                    <div class="text-left">${item.TERM_NAME}</div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${item.HISTORY_MSG}" var="data">
                                <tr>
                                    <td class="padding10">
                                        <div class="margin_b5">${data.XCX_SCORE}</div>
                                        <div>形成性成绩<c:if test="${data.XCX_PERCENT ne ''}">（${data.XCX_PERCENT}%）</c:if></div>
                                    </td>
                                    <td class="padding10">
                                        <div class="margin_b5">${data.ZJX_SCORE}</div>
                                        <div>终结性成绩<c:if test="${data.XCX_PERCENT ne ''}">（${100-data.XCX_PERCENT}%）</c:if></div>
                                    </td>
                                    <td class="padding10">
                                        <div class="margin_b5">${data.ZCJ_SCORE}</div>
                                        <div>总成绩</div>
                                    </td>
                                    <td class="padding10">
                                        <c:if test="${data.STATUS eq '0'}">
                                            <div class="text-green margin_b5">通过</div>
                                            <div>状态</div>
                                        </c:if>
                                        <c:if test="${data.STATUS eq '1'}">
                                            <div class="text-red margin_b5">不通过</div>
                                            <div>状态</div>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div>暂无成绩记录！</div>
                    </c:otherwise>
                </c:choose>
            </table>

        </div>
    </div>
</div>
<div class="text-right pop-btn-box pad">
    <button type="button" class="btn btn-default min-width-90px" data-role="close-pop">关闭</button>
</div>
<script type="text/javascript">
    $('.slim-Scroll').slimScroll({
        height: $(window).height()-126,
        size: '5px'
    });

    //关闭窗口
    $('[data-role="close-pop"]').click(function(event) {
        parent.$.closeDialog(frameElement.api);
    });

</script>
</body>
</html>

