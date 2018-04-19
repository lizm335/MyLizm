<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>教师列表</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
</head>
<body>
<form id="listForm" action="teacherList">
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">添加老师</h3>
	</div>
    <div class="scroll-box">
    	<div class="box-body">
    		<div class="row">
                <div class="col-xs-3">
                    <input class="form-control" placeholder="账号" name="ZGH" value="${param.ZGH }">
                </div>
                <div class="col-xs-3 no-padding">
                    <input class="form-control" placeholder="姓名" name="XM" value="${param.XM }">
                </div>
                <div class="col-xs-3 no-pad-right">
                    <select class="form-control" name="EMPLOYEE_TYPE">
                        <option value="" <c:if test="${param.EMPLOYEE_TYPE eq '' }">selected="selected"</c:if>>选择类型</option>
                        <option value="1" <c:if test="${param.EMPLOYEE_TYPE eq '1' }">selected="selected"</c:if>>班主任</option>
                        <option value="2" <c:if test="${param.EMPLOYEE_TYPE eq '2' }">selected="selected"</c:if>>辅导老师</option>
                        <option value="4" <c:if test="${param.EMPLOYEE_TYPE eq '4' }">selected="selected"</c:if>>督导老师</option>
                        <option value="10" <c:if test="${param.EMPLOYEE_TYPE eq '10' }">selected="selected"</c:if>>论文教师</option>
                        <option value="3" <c:if test="${param.EMPLOYEE_TYPE eq '3' }">selected="selected"</c:if>>其它</option>
                    </select>
                </div>
                <div class="col-xs-2">
                    <button type="submit" class="btn btn-block btn-primary">搜索</button>
                </div>
            </div>
            <table class="table-gray-th text-center margin_t15">
                <thead>
                    <tr>
                        <th width="60">选择</th>
                        <th>账号</th>
                        <th>姓名</th>
                        <th>类型</th>
                    </tr>
                </thead>
                <tbody>
                	<c:forEach items="${pageInfo.content }" var="entity">
                    <tr>
                        <td>
                            <input type="radio" name="r1" data-id="sel-item" data-json='{
                                "id":"${entity.EMPLOYEE_ID }",
                                "name":"${entity.ZGH }",
                                "username":"${entity.XM }",
                                "role":"${entity.EMPLOYEE_TYPE}"
                            }'>
                        </td>
                        <td>${entity.ZGH }</td>
                        <td>${entity.XM }</td>
                        <td>${entity.EMPLOYEE_TYPE}</td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
            <tags:pagination page="${pageInfo}" paginationSize="5" />
    	</div>
    </div>
</div>
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="sure">确定</button>
</div>
</form>
<script type="text/template" id="tpl">
    <table class="table table-bordered vertical-mid text-center table-font margin-bottom-none margin_t10" data-id="tbl">
        <thead class="with-bg-gray">
            <tr>
                <th>帐号</th>
                <th>姓名</th>
                <th>类型</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>{0}</td>
                <td>{1}</td>
                <td>{2}</td>
                <td>
                    <input type="hidden" name="TEACHER_ID" value="{3}">
                    <a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove-teacher"><i class="fa fa-trash-o text-red"></i></a>
                </td>
            </tr>
        </tbody>
    </table>
</script>
<script type="text/javascript">
//关闭 弹窗
$("[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

//设置内容主体高度
$('.scroll-box').height($(frameElement).height()-106);

//确定
$('[data-role="sure"]').click(function(event) {
    var $ck=$('[data-id="sel-item"]:checked');
    if($ck.length>0){
        var htmlTemp=$('#tpl').html();
        var obj=$ck.data('json');
        htmlTemp=htmlTemp.format(obj.username,obj.name,obj.role,obj.id);

        parent.$('iframe[name="Iframe-teachCourse"]').contents().find('[data-id="teach-box"]').html(htmlTemp);
    }
    parent.$.closeDialog(frameElement.api);
});
</script>
</body>
</html>
