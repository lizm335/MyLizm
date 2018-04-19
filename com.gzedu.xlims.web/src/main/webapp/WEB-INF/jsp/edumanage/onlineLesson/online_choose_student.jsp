<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<!DOCTYPE html>
<html class="reset">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>管理系统</title>

    <%@ include file="/WEB-INF/jsp/common/jslibs.jsp" %>

<style type="text/css">
html,body{
	height: 100%;
	overflow: hidden;
}
</style>
</head>
<body>
<form id="listForm" action="${ctx}/edumanage/onlinelesson/onlineChooseStudent">
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">在线选择学员</h3>
	</div>
    <div >
    	<div class="border-bottom pad-t15 clearfix" data-id="form">
    		<div class="form-horizontal">
                <div class="col-xs-4">
                    <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">层次</label>
		              <div class="col-xs-9">
		                <select class="form-control select2" name="search_pyccId" data-placeholder="全部层次" style="width: 100%;">
							<option value=''></option>
							<c:forEach items="${pyccMap}" var="map">
								<option ${param.search_pyccId==map.key?'selected="selected"':'' } value='${map.key }'>${map.value }</option>
							</c:forEach>
						</select>
		              </div>
		            </div>
                </div>
                <div class="col-xs-4">
                    <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">学期</label>
		              <div class="col-xs-9">
		                <select class="form-control select2" name="search_gradeId" data-placeholder="全部学期" style="width: 100%;">
							<option value=''></option>
							<c:forEach items="${gradeMap}" var="map">
								<option ${param.search_gradeId==map.key?'selected="selected"':'' } value='${map.key }'>${map.value }</option>
							</c:forEach>
						</select>
		              </div>
		            </div>
                </div>
                <div class="col-xs-4">
                    <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">专业</label>
		              <div class="col-xs-9">
		                <select class="form-control select2" name="search_specialtyId" data-placeholder="全部专业" style="width: 100%;">
							<option value=''></option>
							<c:forEach items="${gradeMap}" var="map">
								<option ${param.search_specialtyId==map.key?'selected="selected"':'' } value='${map.key }'>${map.value }</option>
							</c:forEach>
						</select>
		              </div>
		            </div>
                </div>
                <div class="col-xs-4">
                    <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">学籍</label>
		              <div class="col-xs-9">
		                <select class="form-control" name="search_signupStatus" style="width: 100%;">
							<option value=''>选择学籍状态</option>
							<c:forEach items="${signupStatusMap}" var="map">
								<option ${param.search_signupStatus==map.key?'selected="selected"':'' } value='${map.key }'>${map.value }</option>
							</c:forEach>
						</select>
		              </div>
		            </div>
                </div>
                <div class="col-xs-4">
                    <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">学习中心</label>
		              <div class="col-xs-9">
		                <select class="form-control select2" name="search_studyCenterId" data-placeholder="全部学习中心" style="width: 100%;">
							<option value=''></option>
							<c:forEach items="${studyCenterMap}" var="map">
								<option ${param.search_studyCenterId==map.key?'selected="selected"':'' } value='${map.key }'>${map.value }</option>
							</c:forEach>
						</select>
		              </div>
		            </div>
                </div>
                <div class="col-xs-4">
                    <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">姓名</label>
		              <div class="col-xs-9">
		                <input class="form-control" name="search_studentName" value="${param.search_studentName }" />
		              </div>
		            </div>
                </div>
                <div class="col-xs-4">
                    <div class="form-group">
		              <label class="control-label col-xs-3 text-nowrap">学号</label>
		              <div class="col-xs-9">
		                <input class="form-control" name="search_studentCode" value="${param.search_studentCode }" />
		              </div>
		            </div>
                </div>
                <div class="col-xs-8 text-right">
                	<input type="checkbox" id="selectall"  />选择全部 &nbsp;&nbsp;&nbsp;
                	<button type="submit" class="btn btn-primary min-width-90px">搜索</button>
                </div>
            </div>
        </div>
        <div class="box-body pad-l15 pad-r15">
        	<div class="scroll-box" data-id="box">
	            <table class="table-gray-th text-center table-font">
	                <thead>
	                    <tr>
	                        <th width="60">
	                            <input type="checkbox" data-id="sel-all">
	                        </th>
	                        <th width="80">头像</th>
	                        <th>个人信息</th>
	                        <th width="38%">报读信息</th>
	                    </tr>
	                </thead>
	                <tbody class="tbody">
	                	<c:forEach  items="${pageInfo.content }" var="item">
	                		 <tr data-id="${item.STUDENT_ID }">
		                    	<td>
		                            <input type="checkbox" name="r1" data-id="sel-item">
		                        </td>
		                        <td>
		                        	<c:set var="defaultImg" value="${ css.concat('/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png')}"></c:set>
		                        	<img src="${not empty(item.AVATAR)?item.AVATAR:defaultImg}" class="img-circle" width="55" height="55">
		                        </td>
		                        <td>
		                        	<ul class="list-unstyled text-left">
		                        		<li>姓名：${item.XM }</li>
		                        		<li>学号：${item.XH }</li>
		                        		<li>手机：${item.SJH }</li>
		                        		<li>学籍：${signupStatusMap[item.XJZT] }</li>
		                        	</ul>
		                        </td>
		                        <td>
		                        	<ul class="list-unstyled text-left">
		                        		<li>层次：${pyccMap[item.PYCC]}</li>
		                        		<li>学期：${gradeMap[item.GRADE_ID]}</li>
		                        		<li>专业：${specialtyMap[item.SPECIALTY_ID] }</li>
		                        		<li>学习中心：${studyCenterMap[item.XXZX_ID] }</li>
		                        	</ul>
		                        </td>
		                    </tr>
	                	</c:forEach>
	                   	<c:if test="${empty(pageInfo.content ) }">
	                   		<tr> <td colspan="5">暂无数据</td> </tr>
	                   	</c:if>
	                </tbody>
	            </table>
	            <tags:pagination page="${pageInfo}" paginationSize="10" />
            </div>
    	</div>
    </div>
</div>
<div class="text-right pop-btn-box pad">
	<span class="pull-left pad-t5">
  		本次共选择 <b class="text-light-blue select-total">0</b> 个学员
  	</span>
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px" data-role="sure">确定</button>
</div>
</form>


<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
<script type="text/javascript">
//关闭 弹窗
$("[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

//设置内容主体高度
$('.scroll-box').height($(frameElement).height()-133-$('[data-id="form"]').outerHeight(true));

//单选
$('body').on('change', '[data-id="sel-item"]', function(event) {
	if( $(this).prop('checked') ){
		$(this).closest('tr').addClass('on');
	}
	else{
		$(this).closest('tr').removeClass('on');
	}
	$('.select-total').text($('tbody tr.on').length);
	$('#selectall').prop('checked',false);
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
    $('#selectall').prop('checked',false);
});

$('#selectall').change(function(){
    if($(this).is(':checked')){
	 	$('[data-id="sel-item"]').prop('checked',true);
	    $('tbody tr').addClass('on');
	    $('.select-total').text('${pageInfo.totalElements}');
	}else{
	    $('[data-id="sel-item"]').prop('checked',false);
	    $('tbody tr').removeClass('on');
	    $('.select-total').text(0);
	}
});

//确定
$('[data-role="sure"]').click(function(event) {
    var studentIds=[];
    var isSelectAll=$('#selectall').is(':checked');
    if(isSelectAll==false){
		$('.tbody tr.on').each(function(){
		   studentIds.push($(this).data('id'));
		});
	}
	
	if(studentIds.length==0 && isSelectAll==false){
	    parent.$.closeDialog(frameElement.api);
	    return;
	}
	var url='${ctx}/edumanage/onlinelesson/saveChooseStudent';
	if(isSelectAll){
	    url+='?'+$('#listForm').serialize();
	}
	var postIngIframe = $.formOperTipsDialog({
		text : '数据提交中...',
		iconClass : 'fa-refresh fa-spin'
	});
	$.get(url,{studentIds:studentIds},function(data){
	   		$.closeDialog(postIngIframe);
			if(data.successful){
			    parent.$('#stunum').text(data.obj);
			    parent.$.closeDialog(frameElement.api);
			}else{
				alert(data.message);
			}
		},'json');
});

//select2
$('.select2').select2();
</script>
</body>
</html>
