<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
    <title>班主任平台 - 个人资料</title>
    <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">

<section class="content-header">
    <h1>
        个人资料管理
    </h1>
    <ol class="breadcrumb">
        <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
        <li class="active">个人资料管理</li>
    </ol>
</section>
<section class="content">
    <div class="alert alert-success" <c:if test="${empty feedback || !feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-check'></i>${feedback.message}</div>
    <div class="alert alert-danger" <c:if test="${empty feedback || feedback.successful}"> hidden="hidden"</c:if>><button data-dismiss='alert' class='close'>×</button><i class='icon fa fa-warning'></i>${feedback.message}</div>

    <div class="box no-border margin-bottom-none">
        <div class="nav-tabs-custom margin-bottom-none reset-nav-tabs-custom">
            <ul class="nav nav-tabs">
                <li><a href="${ctx}/home/personal/updateInfo">个人资料</a></li>
                <li class="active"><a href="${ctx}/home/personal/updateMobilePhone" data-toggle="tab">更换手机号</a></li>
                <li ><a href="${ctx}/home/personal/updatePwd" >修改密码</a></li>
                <li><a href="${ctx}/home/personal/updateHeadPortrait">头像管理</a></li>
            </ul>
            <div class="tab-content">
                <form id="theform" class="form-horizontal theform" role="form" action="${ctx }/home/personal/updateMobilePhone" method="post">
                    <input id="action" type="hidden" name="action" value="${action }">
                    <div class="tab-pane active" id="tab_notice_2">
                        <div class="form-horizontal reset-form-horizontal margin_t10">
                        	<div class="form-group">
                                <label class="col-md-2 col-sm-2 control-label">原绑定手机号：</label>
                                <div class="col-md-8 col-sm-10 position-relative">
                                    <input type="text" name="oldSjh"id="oldSjh" class="form-control"  value="${info.sjh }" readonly="readonly" >
                                    <div class="tooltip left" role="tooltip">
                                        <div class="tooltip-arrow"></div>
                                        <div class="tooltip-inner"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>原手机验证码：</label>
                                <div class="col-md-8 col-sm-10 position-relative">
                                	<div class="input-group">
                                    	<input type="text" name="oldVerifyCode" id="oldVerifyCode" class="form-control" placeholder="验证码" autocomplete="off" datatype="*" nullmsg="请填写验证码！">
                                    	<div class="input-group-btn">
                                    		<input type="button"  id="sendOld" value="发送验证码" class="btn btn-primary">
                                    	</div>
                                    </div>
                                    <div class="tooltip left" role="tooltip">
                                        <div class="tooltip-arrow"></div>
                                        <div class="tooltip-inner"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>绑定新手机:</label>
                                <div class="col-md-8 col-sm-10 position-relative">
                                    <input type="text" name="newSjh" id="newSjh" class="form-control" placeholder="新手机号码" autocomplete="off" datatype="*11-11" nullmsg="请填写新手机号码！" errormsg="手机号码不正确！">
                                    <div class="tooltip left" role="tooltip">
                                        <div class="tooltip-arrow"></div>
                                        <div class="tooltip-inner"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>新手机验证码：</label>
                                <div class="col-md-8 col-sm-10 position-relative">
                                	<div class="input-group">
                                    	<input type="text" name="newVerifyCode" id="newVerifyCode" class="form-control" placeholder="验证码" autocomplete="off" datatype="*" nullmsg="请填写验证码！">
                                    	<div class="input-group-btn">
                                    		<input type="button"  id="sendNew" value="发送验证码" class="btn btn-primary">
                                    	</div>
                                    </div>
                                    <div class="tooltip left" role="tooltip">
                                        <div class="tooltip-arrow"></div>
                                        <div class="tooltip-inner"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group margin_t20">
                                <label class="col-md-2 col-sm-2 control-label"></label>
                                <div class="col-md-8 col-sm-10">
                                    <button type="button" class="btn btn-success min-width-90px margin_r15 btn-sure-edit">保存</button>
                                    <button type="button" class="btn btn-default min-width-90px btn-cancel-edit">取消</button>
                                </div>
                            </div>
                        </div>
                    </div><!-- /.tab-pane -->
                </form>
            </div><!-- /.tab-content -->
        </div><!-- nav-tabs-custom -->
    </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="application/javascript">
    /*确认发送*/
    var $theform=$(".theform");
    $.Tipmsg.r='';
    var postIngIframe;
    var postForm=$theform.Validform({
        showAllError:true,
        tiptype:function(msg,o,cssctl){
            //msg：提示信息;
            //o:{obj:*,type:*,curform:*},
            //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
            //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态,
            //curform为当前form对象;
            //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
            var msgBox=o.obj.closest('.position-relative').children('.tooltip');

            msgBox.children('.tooltip-inner').text(msg);
            if(msgBox.hasClass('left')){
                msgBox.css({
                    width:130,
                    left:-120,
                    top:5
                })
            }
            else{
                msgBox.css({
                    top:-23
                })
            }

            switch(o.type){
                case 3:
                    msgBox.addClass('in');
                    break;
                default:
                    msgBox.removeClass('in');
                    break;
            }
        },
        beforeSubmit:function(curform){
            if(curform.find(".Validform_error").length>0){
                return false;
            }
            postIngIframe=$.dialog({
                title: false,
                contentHeight:20,
                closeIcon:false,
                content: '<div class="text-center">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>',
                columnClass:'col-xs-4 col-xs-offset-4'
            });
        },
        callback:function(data){
            //返回数据data是json对象，{"info":"demo info","status":"y"}
            //info: 输出提示信息;
            //status: 返回提交数据的状态,是否提交成功。如可以用"y"表示提交成功，"n"表示提交失败，在ajax_post.php文件返回数据里自定字符，主要用在callback函数里根据该值执行相应的回调操作;
            //你也可以在ajax_post.php文件返回更多信息在这里获取，进行相应操作；
            //ajax遇到服务端错误时也会执行回调，这时的data是{ status:**, statusText:**, readyState:**, responseText:** }；

            //这里执行回调操作;
            //注意：如果不是ajax方式提交表单，传入callback，这时data参数是当前表单对象，回调函数会在表单验证全部通过后执行，然后判断是否提交表单，如果callback里明确return false，则表单不会提交，如果return true或没有return，则会提交表单。
        }
    });
    //保存修改
    $(".btn-sure-edit").click(function() {
        /*插入业务逻辑*/
        var index=$(".btn-sure-edit").index(this);
        postForm.eq(index).submitForm();
    });
    /*取消*/
    $(".btn-cancel-edit").click(function(event) {
        var index=$(".btn-cancel-edit").index(this);
        postForm.eq(index).resetForm();
    });
    
    //发送旧手机验证码
    $('#sendOld').click(function(){
    	var oldSjh=$('#oldSjh').val();
    	if(oldSjh==''){
    		alert('手机号码异常，请联系学支管理员！');
    		return ;
    	}else{
    		$.post('${ctx}/home/common/sendMobilePhone.html}',{mobilePhone:oldSjh,type:1},function(data){
    			alert(data.message);
    		},'json');
    	}
    });
    
    //校验验证码是否正确
    $('#oldVerifyCode').change(function(){
    	var code=$('#oldVerifyCode').val();
    	if(code.length==6){
    		$.post('${ctx}/home/common/doSmsValidateCode.html}',{code:code},function(data){
    			alert(data.message);
    		},'json');
    	}
    });
    
    
    //发送新手机验证码
    $('#sendNew').click(function(){
    	var newSjh=$('#newSjh').val();
    	if(newSjh==''){
    		alert('新手机号码不能为空！');
    		return ;
    	}else{
    		$.post('${ctx}/home/common/sendMobilePhone.html}',{mobilePhone:newSjh},function(data){
    			alert(data.message);
    		},'json');
    	}
    });
    
    //校验新手机验证码是否正确
    $('#newVerifyCode').change(function(){
    	var code=$('#newVerifyCode').val();
    	if(code.length==6){
    		$.post('${ctx}/home/common/doSmsNewValidateCode.html}',{code:code},function(data){
    			alert(data.message);
    		},'json');
    	}
    });
    
   
    
</script>
</body>
</html>
