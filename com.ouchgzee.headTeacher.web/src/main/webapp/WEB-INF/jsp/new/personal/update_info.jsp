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
                <li class="active"><a href="${ctx}/home/personal/updateInfo" data-toggle="tab">个人资料</a></li>
                <%-- <li><a href="${ctx}/home/personal/updateMobilePhone" >更换手机号</a></li> --%>
                <li><a href="${ctx}/home/personal/updatePwd">修改密码</a></li>
                <li><a href="${ctx}/home/personal/updateHeadPortrait">头像管理</a></li>
            </ul>
            <div class="tab-content">
                <form id="theform" class="form-horizontal theform" role="form" action="${ctx }/home/personal/updateInfo" method="post">
                    <input id="action" type="hidden" name="action" value="${action }">
                    <div class="tab-pane active" id="tab_notice_1">
                        <div class="form-horizontal reset-form-horizontal margin_t10">
				              <div class="form-group">
				                <label class="col-md-2 col-sm-2 control-label">平台账号</label>
				                <div class="col-md-8 col-sm-10  position-relative">
				                  <p class="form-control-static">${info.gjtUserAccount.loginAccount }</p>
				                </div>
				              </div>
				              <div class="form-group">
				                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>真实姓名</label>
				                <div class="col-md-8 col-sm-10 position-relative">
				                  <input type="text" value="${info.xm }" name="xm" class="form-control" placeholder="真实姓名" datatype="*" nullmsg="请填写姓名！" errormsg="请填写姓名！">
				                  <div class="tooltip left" role="tooltip">
				                    <div class="tooltip-arrow"></div>
				                    <div class="tooltip-inner"></div>
				                  </div>
				                </div>
				              </div>
				              <%-- <div class="form-group">
				                <label class="col-md-2 col-sm-2 control-label">手机号码</label>
				                <div class="col-md-8 col-sm-10 position-relative">
				                  <input type="text" value="${info.sjh }" disabled="disabled" class="form-control" placeholder="手机号码"  nullmsg="请填写手机号码！" errormsg="请填写手机号码！">
				                  <div class="tooltip left" role="tooltip">
				                    <div class="tooltip-arrow"></div>
				                    <div class="tooltip-inner"></div>
				                  </div>
				                </div>
				              </div> --%>
				              
				              <div class="form-group">
				                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>绑定手机</label>
				                <div class="col-md-8 col-sm-10 position-relative">
				                  <p class="form-control-static">
				                    <span data-role="phone-txt">${info.sjh }</span>
				                    <a href="#" data-role="phone-change"><u>更换</u></a>
				                  </p>
				                </div>
				              </div>
				              
				              <div class="form-group">
				                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>联系电话</label>
				                <div class="col-md-8 col-sm-10 position-relative">
				                  <input type="text" value="${info.lxdh }" name="lxdh" class="form-control" placeholder="联系电话" datatype="/(^(0[0-9]{2,3})-([2-9][0-9]{6,7})+(-[0-9]{1,4})?$)/" nullmsg="请填写联系电话！" errormsg="请填写联系电话！">
				                  <div class="tooltip left" role="tooltip">
				                    <div class="tooltip-arrow"></div>
				                    <div class="tooltip-inner"></div>
				                  </div>
				                </div>
				              </div>
				              <div class="form-group">
				                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>QQ号码</label>
				                <div class="col-md-8 col-sm-10 position-relative">
				                  <input type="text" value="${info.qq}" name="qq" class="form-control" placeholder="QQ号码" datatype="n2-16" nullmsg="请填写QQ号码！" errormsg="请填写正确QQ号码！">
				                  <div class="tooltip left" role="tooltip">
				                    <div class="tooltip-arrow"></div>
				                    <div class="tooltip-inner"></div>
				                  </div>
				                </div>
				              </div>
				              <div class="form-group">
				                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>邮箱地址</label>
				                <div class="col-md-8 col-sm-10 position-relative">
				                  <input type="text" value="${info.dzxx }" name="dzxx" class="form-control" placeholder="邮箱地址" datatype="e" nullmsg="请填写邮箱地址！" errormsg="邮箱格式错误！"  name="email">
				                  <div class="tooltip left" role="tooltip">
				                    <div class="tooltip-arrow"></div>
				                    <div class="tooltip-inner"></div>
				                  </div>
				                </div>
				              </div>
				              <div class="form-group">
				                <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>性别</label>
				                <div class="col-md-8 col-sm-10 position-relative">
				                  <div class="full-width">
				                    <label class="text-no-bold">
				                      <input type="radio" name="xbm" class="minimal" value="1"<c:if test="${empty info.xbm or info.xbm==1}">checked</c:if> >
				                      男
				                    </label>
				                    <label class="text-no-bold left10">
				                      <input type="radio" name="xbm" class="minimal" value="2"<c:if test="${info.xbm==2}">checked</c:if>>
				                      女
				                    </label>
				                  </div>
				                </div>
				              </div>
				              <div class="form-group">
				                <label class="col-md-2 col-sm-2 control-label">个人描述</label>
				                <div class="col-md-8 col-sm-10 position-relative">
				                  <textarea name="individualitySign" class="form-control" rows="5" placeholder="请输入描述内容！" >${info.individualitySign}</textarea>
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
    //iCheck for checkbox and radio inputs
    $('input.minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on("ifChecked",function(e){
        var $e=$(e.target);
        $e.attr('checked',true);
    }).on("ifUnchecked",function(e){
        $(e.target).attr('checked',false);
    });


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
    
    
    $('[data-role="phone-change"]').on('click', function(event) {
        event.preventDefault();
        var $that=$(this);    
        $.mydialog({
          id:'phone-change',
          width:560,
          height:320,
          zIndex:11000,
          content: 'url:updateMobilePhone.html'
        });
   });

</script>
</body>
</html>
