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
<section class="content-header">
  <div class="pull-left">
    您所在位置：
  </div>
  <ol class="breadcrumb">
    <li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
    <li class="active">个人资料管理</li>
  </ol>
</section>
<section class="content">
  <div class="box no-border margin-bottom-none">
    <div class="nav-tabs-custom margin-bottom-none">
      <ul class="nav nav-tabs nav-tabs-lg">
        <li class="active"><a href="#tab_notice_1" data-toggle="tab">个人资料</a></li>
        <li><a href="#tab_notice_2" data-toggle="tab">修改密码</a></li>
        <!-- <li><a href="#tab_notice_3" data-toggle="tab">头像管理</a></li> -->
        <li><a href="#tab_notice_4" data-toggle="tab">电子签名</a></li>
      </ul>
      <div class="tab-content">
        <div class="tab-pane active" id="tab_notice_1">
          <form class="theform" action="/personal/update"> 
          	<input type="hidden" name="id" value="${personal.id }">
            <div class="form-horizontal reset-form-horizontal margin_t10">
              <div class="form-group">
                <label class="col-md-2 col-sm-2 control-label no-pad-top">平台账号</label>
                <div class="col-md-8 col-sm-10  position-relative">
                  <p class="no-margin">${personal.account }</p>
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-2 col-sm-2 control-label no-pad-top">院校</label>
                <div class="col-md-8 col-sm-10  position-relative">
                  <p class="no-margin">${personal.orgName }</p>
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-2 col-sm-2 control-label no-pad-top">角色</label>
                <div class="col-md-8 col-sm-10  position-relative">
                  <p class="no-margin">${personal.roleName }</p>
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>真实姓名</label>
                <div class="col-md-8 col-sm-10 position-relative">
                  <input type="text" class="form-control" name="realName" value="${personal.realName }" datatype="*" nullmsg="请填写姓名！" errormsg="请填写姓名！">
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>绑定手机</label>
                <div class="col-md-8 col-sm-10 position-relative">
                  <p class="form-control-static">
                    <span data-role="phone-txt">${personal.mobile }</span>
                    <a href="#" data-role="phone-change"><u>更换</u></a>
                  </p>
                </div>
              </div>
              <!-- <div class="form-group">
                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>绑定手机</label>
                <div class="col-md-8 col-sm-10 position-relative">
                  <input type="text" class="form-control" placeholder="手机号码" datatype="m" nullmsg="请填写手机号码！" errormsg="请填写手机号码！">
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>验证码</label>
                <div class="col-md-8 col-sm-10 position-relative">
                  <div class="pull-right margin_l10">
                    <button type="button" class="btn btn-success" data-role="get-ver-code">获取验证码</button>
                  </div>
                  <div class="oh">
                    <input type="text" class="form-control" placeholder="验证码" datatype="*" nullmsg="请填写验证码！" errormsg="请填写验证码！">
                  </div>
                </div>
              </div> -->
              <div class="form-group">
                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>联系电话</label>
                <div class="col-md-8 col-sm-10 position-relative">
                  <input type="text" class="form-control" name="phone" value="${personal.phone }" placeholder="联系电话，格式：区号-号码" datatype="*" nullmsg="请填写联系电话！" errormsg="联系电话格式不对！">
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>邮箱地址</label>
                <div class="col-md-8 col-sm-10 position-relative">
                  <input type="text" class="form-control" name="email" value="${personal.email }" placeholder="邮箱地址" datatype="e" nullmsg="请填写邮箱地址！" errormsg="邮箱格式错误！" >
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
          </form>
        </div><!-- /.tab-pane -->
        <div class="tab-pane" id="tab_notice_2">
          <form class="theform" action="/personal/resetPassword"> 
            <div class="form-horizontal reset-form-horizontal margin_t10">
              <div class="form-group">
                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>旧密码</label>
                <div class="col-md-8 col-sm-10 position-relative">
                  <input type="password" class="form-control" name="oldPassword" placeholder="旧密码" datatype="*" nullmsg="请填写旧密码！" errormsg="请填写旧密码！">
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>新密码</label>
                <div class="col-md-8 col-sm-10 position-relative">
                  <input type="password" class="form-control" placeholder="新密码" datatype="*" nullmsg="请填写新密码！" errormsg="请填写新密码！" name="newPassword">
                </div>
              </div>
              <div class="form-group">
                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>确认密码</label>
                <div class="col-md-8 col-sm-10 position-relative">
                  <input type="password" class="form-control" placeholder="确认密码" datatype="*" recheck="newPassword" name="newPassword2" nullmsg="请填写确认密码！" errormsg="新密码与确认密码不一致！">
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
          </form>
        </div><!-- /.tab-pane --> 
        <div class="tab-pane" id="tab_notice_3">
          <div class="clearfix pad20">
            <h4 class="text-bold margin_b20">当前头像</h4>
            <form class="theform">
              <ul class="list-unstyled user-pic-preview clearfix">
                <li class="li-1">
                  <img src="${css}/ouchgzee_com/platform/xlbzr_css/dist/img/images/user-pic.jpg" class="img-circle center-block" alt="User Image">
                  <p class="f12 gray9 margin_t10">
                    仅支持JPG、JPEG、GIF、PNG图片文件
                    且建议大小小于5M
                  </p>
                  <div class="btn btn-flat btn-sm btn-block btn-default upload-image-btn margin_b20 position-relative">
                    上传照片
                    <input type="file" class="btn-file" datatype="*" nullmsg="请上传照片" errormsg="请上传照片">
                  </div>
                </li>
                <li class="li-2">
                  <img src="${css}/ouchgzee_com/platform/xlbzr_css/dist/img/images/user-pic.jpg" class="img-circle center-block" alt="User Image">
                  <p class="f12 gray9 margin_t10 text-center">
                    大尺寸头像 <br>
                    (100 x 100)
                  </p>
                </li>
                <li class="li-3">
                  <img src="${css}/ouchgzee_com/platform/xlbzr_css/dist/img/images/user-pic.jpg" class="img-circle center-block" alt="User Image">
                  <p class="f12 gray9 margin_t10 text-center">
                    中尺寸头像 <br>
                    (60 x 60)
                  </p>
                </li>
                <li class="li-4">
                  <img src="${css}/ouchgzee_com/platform/xlbzr_css/dist/img/images/user-pic.jpg" class="img-circle center-block" alt="User Image">
                  <p class="f12 gray9 margin_t10 text-center">
                    小尺寸头像 <br>
                    (40 x 40)
                  </p>
                </li>
              </ul>
              <div class="margin_t20 text-center">
                <button type="button" class="btn btn-success min-width-90px margin_r15 btn-sure-edit">提交</button>
                <!-- <button type="button" class="btn btn-default min-width-90px btn-cancel-edit">取消</button> -->
              </div>
            </form>
          </div>
        </div><!-- /.tab-pane -->
        <div class="tab-pane" id="tab_notice_4">
            <form id="updateSignPhotoForm" action="/personal/updateSignPhoto" method="post">
              <div id="qrcodeDiv" class="box box-border margin-bottom-none no-shadow flat margin_t10 text-center">
				<div class="box-body">
					<div id="qrcode"></div>
					<div class="gray9">手机扫一扫，签名确认</div>
				</div>
			  </div>
			  <div id="autographDiv" class="box box-border margin-bottom-none no-shadow flat margin_t10 text-center" style="display: none;">
				<div class="box-body">
					<img id="autographImg" src="${personal.signPhoto}">
					<input type="hidden" id="autograph" name="signPhoto" value="${personal.signPhoto}" />
					<div class="margin_t10">
						<button type="button" class="btn btn-warning min-width-90px re-sign">重新签名</button>
					</div>
				</div>
			  </div>
			  <div class="margin_t20 text-center">
			  	<input id="autographCheck" type="checkbox"> 本人申明：以上为本人所签，仅用于本平台功能业务，如用于其他地方均无效。
			  </div>
              <div class="margin_t20 text-center">
                <button id="updateSignPhoto" type="button" class="btn btn-success min-width-90px margin_r15">保存</button>
              </div>
            </form>
        </div><!-- /.tab-pane -->
      </div><!-- /.tab-content -->
    </div><!-- nav-tabs-custom -->
  </div>
</section>

<script type="text/javascript" src="${ctx}/static/jquery-qrcode/jquery.qrcode.min.js"></script>

<script type="text/javascript">

/*确认发送*/
var $theform=$(".theform");

var htmlTemp='<div class="tooltip top" role="tooltip">'
              +'<div class="tooltip-arrow"></div>'
              +'<div class="tooltip-inner"></div>'
              +'</div>';
    $theform.find(":input[datatype]").each(function(index, el) {
      $(this).after(htmlTemp);
    });

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
        content: '<div class="text-center pad-t10">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>',
        columnClass:'col-xs-4 col-xs-offset-4'
    });
  },
  callback:function(data){
    //这里执行回调操作;
    //如果callback里明确return alse，则表单不会提交，如果return true或没有return，则会提交表单。
	console.info(data);
    //if("成功提交") 就执行下面语句
    var iconClass = "";
    if(data.result == 'success') {
    	iconClass= "fa fa-check-circle";
    } else {
    	iconClass= "fa fa-close"; 
    }
    setTimeout(function(){//此句模拟交互，程序时请去掉
      var rIframe=$.dialog({
          title: false,
          contentHeight:20,
          closeIcon:false,
          content: '<div class="text-center pad-t10">'+data.message+'<i class="icon '+iconClass+'"></i></div>',
          columnClass:'col-xs-4 col-xs-offset-4',
      });
      /*关闭弹窗*/
      setTimeout(function(){
        postIngIframe.close();
        rIframe.close();
      },2000)
    },2000);//此句模拟交互，程序时请去掉
  }
});

$(".btn-sure-edit").click(function(event) {
  /*插入业务逻辑*/
  var index=$(".btn-sure-edit").index(this);
  postForm.eq(index).ajaxPost();
});
/*取消*/
$(".btn-cancel-edit").click(function(event) {
  var index=$(".btn-cancel-edit").index(this);
  postForm.eq(index).resetForm();
});

// 更换绑定手机
$('[data-role="phone-change"]').on('click', function(event) {
  event.preventDefault();
  var $that=$(this);    
  $.mydialog({
    id:'phone-change',
    width:560,
    height:320,
    zIndex:11000,
    content: 'url:/personal/toUnBindMobile'
  });
});

//调用公共签名插件
$('#qrcode').my_qrcode(function(obj) {
	$('#autograph').val(obj);
	$('#autographImg').attr('src', obj);
	$('#qrcodeDiv').hide();
	$('#autographDiv').show();
});

$('.re-sign').click(function(){
	$('#autograph').val('');
	$('#qrcode').empty();
	$('#qrcodeDiv').show();
	$('#autographDiv').hide();
	
	$('#qrcode').my_qrcode(function(obj) {
		$('#autograph').val(obj);
		$('#autographImg').attr('src', obj);
		$('#qrcodeDiv').hide();
		$('#autographDiv').show();
	});
});

var autograph = $("#autograph").val();
if (autograph != '') {
	$('#qrcodeDiv').hide();
	$('#autographDiv').show();
}

$("#updateSignPhoto").click(function(event) {
  var autograph = $("#autograph").val();
  if (autograph == '') {
	  alert("请签名");
	  return false;
  }
  if(!$('#autographCheck').is(':checked')) {
	  alert("请勾选本人申明");
	  return false;
  }
  $.post("updateSignPhoto",{signPhoto:autograph},function(data){
 		alert(data.message);
 },"json"); 
});

</script>
</body>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</html>