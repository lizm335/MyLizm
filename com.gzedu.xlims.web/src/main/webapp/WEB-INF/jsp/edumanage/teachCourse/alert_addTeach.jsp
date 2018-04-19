<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>新增任教信息</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
</head>
<body>
<c:if test="${empty resultMap }">
<form id="theform" action="addCourseTeacher">
</c:if>
<c:if test="${!empty resultMap }">
<form id="theform" action="uptTeacherInfo">
</c:if>
    <div class="box no-border no-shadow margin-bottom-none">
        <div class="box-header with-border">
        	<c:if test="${!empty resultMap }">
        		<h3 class="box-title">编辑任教信息</h3>
        	</c:if>
            <c:if test="${empty resultMap }">
        		<h3 class="box-title">新增任教信息</h3>
        	</c:if>
        </div>
        <div class="scroll-box">
            <div class="box-body">
                <table class="table border-none">
                    <tr>
                        <input type="hidden" name="COURSE_TEACHER_ID" value="${resultMap.COURSE_TEACHER_ID }" hidden/>
                        <td width="100" class="border-none">
                            <div class="pad-t5">
                                	任教老师：
                            </div>
                        </td>
                        <td class="border-none">
                        	<c:if test="${empty resultMap }">
                            <button type="button" class="btn btn-default min-width-90px" data-role="add-course-teacher">
                                <i class="fa fa-fw fa-plus"></i> 添加老师
                            </button>
                            </c:if>
                            <div data-id="teach-box">
							<c:if test="${!empty resultMap }">
								<table class="table table-bordered vertical-mid text-center table-font margin-bottom-none margin_t10" data-id="tbl">
						        <thead class="with-bg-gray">
						            <tr>
						                <th>帐号</th>
						                <th>姓名</th>
						                <th>类型</th>
						            </tr>
						        </thead>
						        <tbody>
						            <tr>
						                <td>${resultMap.ZGH }</td>
						                <td>${resultMap.XM }</td>
						                <td>${resultMap.TEACHER_TYPE == '1'?'主持老师':resultMap.TEACHER_TYPE == '2'?'责任老师':'辅导老师' }</td>
						            </tr>
						        </tbody>
						    </table>
							</c:if>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="border-none">
                            <div class="pad-t5">
                                	任教课程：
                            </div>
                        </td>
                        <td class="border-none">
                            <div class="position-relative">
                                <select name="COURSE_IDS" <c:if test="${!empty resultMap }">disabled="true"</c:if> 
                                	class="form-control select2 full-width" multiple="multiple" data-placeholder="请选择任教课程" 
                                	datatype="*" nullmsg="请选择任教课程" errormsg="请选择任教课程">
                                    <option value="">请选择</option>
									<c:forEach items="${courseMap}" var="map">
										<option value="${map.key}" <c:if test="${resultMap.COURSE_ID eq map.key }">selected="selected"</c:if>>${map.value}</option>
									</c:forEach>
                                </select>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="border-none">
                            <div class="pad-t5">
                                	任教机构：
                            </div>
                        </td>
                        <td class="border-none">
                            <div class="position-relative">
                                <select name="CENTER_ID" class="form-control full-width" data-placeholder="请选择任教机构" datatype="*" nullmsg="请选择任教机构" errormsg="请选择任教机构">
                                    <option value="">请选择</option>
                                    <c:forEach items="${studyCenterMap}" var="map">
                                        <option value="${map.key}"  <c:if test="${map.key==resultMap.CENTER_ID}">selected='selected'</c:if>>${map.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </td>
                    </tr>

                    <tr>
                        <td class="border-none">
                            <div class="pad-t5">
                                	任教类型：
                            </div>
                        </td>
                        <td class="border-none">
                            <div class="position-relative">
                                <select name="TEACHER_TYPE" class="form-control full-width" data-placeholder="请选择任教类型" datatype="*" nullmsg="请选择任教类型" errormsg="请选择任教类型">
                                    <option value="">请选择</option>
				                	<option value="1" <c:if test="${resultMap.TEACHER_TYPE == '1'}">selected='selected'</c:if>>主持教师</option>
				                	<option value="2" <c:if test="${resultMap.TEACHER_TYPE == '2'}">selected='selected'</c:if>>责任老师</option>
				                	<option value="3" <c:if test="${resultMap.TEACHER_TYPE == '3'}">selected='selected'</c:if>>辅导老师</option>
                                </select>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>

    <div class="text-right pop-btn-box pad">
        <button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
        <button type="submit" class="btn btn-success min-width-90px" data-role="sure">保存</button>
    </div>
</form>
<script type="text/javascript">
//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
    parent.$.closeDialog(frameElement.api);
});

//设置内容主体高度
$('.scroll-box').height($(frameElement).height()-106);

//select2
$('.select2').select2();
//确认
;(function(){
    var $theform=$("#theform");

    var htmlTemp='<div class="tooltip top" role="tooltip">'
          +'<div class="tooltip-arrow"></div>'
          +'<div class="tooltip-inner"></div>'
          +'</div>';
    $theform.find(".position-relative").each(function(index, el) {
        $(this).append(htmlTemp);
    });

    $.Tipmsg.r='';
    var postForm=$theform.Validform({
      //showAllError:true,
      //ignoreHidden:true,//是否忽略验证不可以见的表单元素
      tiptype:function(msg,o,cssctl){
        //msg：提示信息;
        //o:{obj:*,type:*,curform:*},
        //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
        //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, 
        //curform为当前form对象;
        //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
        if(!o.obj.is("form")){
            var msgBox=o.obj.closest('.position-relative').find('.tooltip');

            msgBox.css({
                'z-index':2,
                bottom:"100%",
                'margin-bottom':-5
            }).children('.tooltip-inner').text(msg);

            switch(o.type){
              case 3:
                msgBox.addClass('in');
                break;
              default:
                msgBox.removeClass('in');
                break;
            }
        }
      },
      beforeSubmit:function(curform){

        var postIngIframe=$.formOperTipsDialog({
          text:'数据提交中...',
          iconClass:'fa-refresh fa-spin'
        });
        
        var uri = $("#theform").attr("action");
        $.ajax({
            type: "POST",
            dataType: "json",
            url: uri,
            data: $('#theform').serialize(),
            success: function (result) {
            	if(result > 0){
            		postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
            	}else{
            		postIngIframe.find('[data-role="tips-text"]').html('数据提交失败...');
            	}
            	postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
            	setTimeout(function(){
                    parent.$.closeDialog(frameElement.api);
                    parent.$(".search").click();
                },2000);
            },
            error : function() {
            }
        });

        return false;//阻止表单自动提交
      },
      callback:function(data){
          
      }
    });

})();

//添加老师
$('[data-role="add-course-teacher"]').on('click', function(event) {
    event.preventDefault();
    parent.$.mydialog({
        id:'add-course-teacher',
        width:700,
        height:550,
        zIndex:11000,
        content: 'url:${ctx}/edumanage/teachCourse/teacherList'
    });
});

//删除已选老师
$(document).on('click', '[data-role="remove-teacher"]', function(event) {
    event.preventDefault();
    $('[data-id="tbl"]').remove();
});
</script>
</body>
</html>
