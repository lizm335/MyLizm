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
<form id="listForm" action="${ctx}/edumanage/onlinelesson/queryOnlineActivityStudent">
<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">已选择学员</h3>
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
                	<button type="submit" class="btn btn-primary min-width-90px">搜索</button>
                </div>
            </div>
        </div>
        <div class="box-body pad-l15 pad-r15">
        	<div class="pad-t5 pad-b10 text-right" data-id="batch-box">
        		<button type="button" class="btn btn-sm btn-default min-width-90px" data-role="batch-del">批量删除</button>
        	</div>
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
	                        <th width="100">操作</th>
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
		                        <td>
		                        	<u class="text-blue" role="button" data-id="${item.STUDENT_ID }" data-role="del-item">删除</u>
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
$('.scroll-box').height($(frameElement).height()-138-$('[data-id="form"]').outerHeight(true)-$('[data-id="batch-box"]').height());

//单选
$('body').on('click', '[data-id="sel-item"]', function(event) {
	if( $(this).prop('checked') ){
		$(this).closest('tr').addClass('on');
	}
	else{
		$(this).closest('tr').removeClass('on');
	}
})
//全选
$('body').on('click','[data-id="sel-all"]',function(event) {
    if($(this).prop('checked')){
        $('[data-id="sel-item"]').prop('checked',true);
        $('.tbody tr').addClass('on');
    }
    else{
        $('[data-id="sel-item"]').prop('checked',false);
        $('.tbody tr').removeClass('on');
    }
})

//删除单行
$('body').on('click', '[data-role="del-item"]', function(event) {
    var self=$(this);
    $.get('${ctx}/edumanage/onlinelesson/deleteOnlineActivityStudent',{studentIds:[$(this).data("id")]},function(data){
		if(data.successful){
		    self.closest('tr').remove();
		    parent.$('#stunum').text(data.obj);
		    if( $('.tbody tr').length==0){
				location.reload();
		     }
		}else{
			alert(data.message);
		}
	},'json');
	
});

//确定
$('[data-role="sure"]').click(function(event) {
    parent.$.closeDialog(frameElement.api)
});

//select2
$('.select2').select2();

//批量删除
$('[data-role="batch-del"]').on('click', function(event) {
	if($('[data-id="sel-item"]:checked').length<=0){
		$.resultDialog(
			{
				type:0,
				msg:'请选择学员',
				timer:1500
			}
		)
	}
	else{
		$.alertDialog({
	        id:'delete',
	        width:300,
	        height:250,
	        zIndex:11000,
	        ok:function(){//“确定”按钮的回调方法
		        var self=this;
	            var studentIds=[];
	    	 	$('.tbody tr.on').each(function(){
	    	 	   studentIds.push($(this).data('id'));
	        	});
	            $.get('${ctx}/edumanage/onlinelesson/deleteOnlineActivityStudent',{studentIds:studentIds},function(data){
					if(data.successful){
					    $('.tbody tr.on').remove();
					    parent.$('#stunum').text(data.obj);
				        $.closeDialog(self);
				        if( $('.tbody tr').length==0){
							location.reload();
					     }
					}else{
						alert(data.message);
					}
				},'json');
	        },
	        content:'<div class="margin_b10 text-center f16"><i class="fa fa-fw fa-exclamation-circle text-orange vertical-mid" style="font-size:42px;"></i><span class="inline-block vertical-middle">确定要删除所选学员？</span></div>'
	    });
	}
});
</script>
</body>
</html>
