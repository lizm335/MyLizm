<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<title>管理系统 - 答疑服务-帮学员提问-选择老师</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<form id="listForm" action="${ctx }/admin/messageInfo/findCourse">
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">选择课程</h3>
	</div>
    <div >
    	<div class="border-bottom pad-t15 pad-b15 clearfix" data-id="form">
    		<div class="clearfix">
                <div class="col-xs-4">
                    <div class="media">
		              <div class="media-left media-middle text-nowrap">课程代码</div>
		              <div class="media-body">
		                <input class="form-control" name="search_EQ_kch" value="${param.search_EQ_kch }">
		              </div>
		            </div>
                </div>
                <div class="col-xs-4">
                    <div class="media">
		              <div class="media-left media-middle text-nowrap">课程名称</div>
		              <div class="media-body">
		                <input class="form-control" name="search_LIKE_kcmc" value="${param.search_LIKE_kcmc }">
		              </div>
		            </div>
                </div>
                <div class="col-xs-4">
                	<div class="media">
		              <div class="media-left media-middle text-nowrap">课程类型</div>
		              <div class="media-body">
		                <select class="form-control" name="search_EQ_courseCategory">
							<option value=''>全部</option>
							<c:forEach items="${courserTypeMap }" var="map">
								<option value="${map.key }" <c:if test="${param.search_EQ_courseCategory eq map.key }">selected</c:if>>${map.value }</option>
							</c:forEach>
						</select>
		              </div>
		              <div class="media-right">
		              	<button type="submit" class="btn btn-primary">搜索</button>
		              </div>
		            </div>
                </div>
            </div>
        </div>
        <div class="box-body pad-l15 pad-r15 pad-t15">
        	<div class="scroll-box" data-id="box">
	            <table class="table-gray-th text-center table-font">
	                <thead>
	                    <tr>
	                        <th width="60">
	                            <input type="checkbox" data-id="sel-all">
	                        </th>
	                        <th width="20%">课程代码</th>
	                        <th>课程名称</th>
	                        <th width="20%">课程类型</th>
	                    </tr>
	                </thead>
	                <tbody>
	                	<c:choose>
							<c:when test="${not empty pageInfo.content}">
								<c:forEach items="${pageInfo.content}" var="info">
									<c:if test="${not empty info}">
			                    <tr>
			                    	<td>
			                            <input type="checkbox" name="r1" data-id="sel-item" data-json='{
			                            	"majorName":"${info.kcmc }",
			                            	"id":"${info.courseId }"
			                           	 }'>
			                        </td>
			                        <td>
			                        	${info.kch }
			                        </td>
			                        <td>
			                        	${info.kcmc }
			                        </td>
			                        <td>
			                        	${courserTypeMap[info.courseCategory]} 
			                        </td>
			                    </tr>
	                    	</c:if>
	                    </c:forEach>
	                    </c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="5">暂无数据</td>
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
<div class="text-right pop-btn-box pad">
	<span class="pull-left pad-t5">
  		本次共选择 <b class="text-light-blue select-total">0</b> 个课程
  	</span>
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="sure">确定</button>
</div>
</form>

<script type="text/javascript">
//关闭 弹窗
$("[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

//设置内容主体高度
$('.scroll-box').height($(frameElement).height()-133-$('[data-id="form"]').outerHeight(true));

//单选
$('body').on('click', '[data-id="sel-item"]', function(event) {
	if( $(this).prop('checked') ){
		$(this).closest('tr').addClass('on');
	}
	else{
		$(this).closest('tr').removeClass('on');
	}
	$('.select-total').text($('tbody tr.on').length);
})
//全选
.on('click','[data-id="sel-all"]',function(event) {
    if($(this).prop('checked')){
        $('[data-id="sel-item"]').prop('checked',true);
        $('tbody tr').addClass('on');
    }
    else{
        $('[data-id="sel-item"]').prop('checked',false);
        $('tbody tr').removeClass('on');
    }
    $('.select-total').text($('tbody tr.on').length);
});

//确定
$('[data-role="sure"]').click(function(event) {
    var $ck=$('[data-id="sel-item"]:checked');
    if($ck.length>0){
        var htmlTemp='\
        				<span class="btn btn-default active">\
        				<input type="checkbox" checked name="courseIds" value="{1}_{0}" data-id="{1}"> {0}\
					    	<i class="fa fa-remove pad-l5" data-toggle="tooltip" title="删除" data-role="remove-sel"></i>\
					  	</span>\
        			';
        var result=[];
        $ck.each(function(index, el) {
            var obj=$(this).data('json');
            result.push(htmlTemp.format(obj.majorName,obj.id))
        });


        var $container=parent.$('[data-role="add-course"]');

        $container.siblings('[data-id="sel-all"]').removeClass('active')
		.children(':checkbox').prop('checked',false);

        $container.before(result.join(''));
    }

    parent.$.closeDialog(frameElement.api)
});
</script>
</body>
</html>

