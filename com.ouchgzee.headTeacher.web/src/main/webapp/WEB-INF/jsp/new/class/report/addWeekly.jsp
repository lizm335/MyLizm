<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<title>写周报</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
  <h1>
  新建周报
  </h1>
  <ol class="breadcrumb">
    <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
    <li><a href="${ctx}/home/class/report/list">工作计划</a></li>
    <li class="active">新建周报</li>
  </ol>
</section>
<section class="content">
  <div class="box margin-bottom-none">
    <div class="box-body pad20">
      <form id="theform" action="${ctx }/home/class/report/doCreateReportWeekly" method="post">
        <div class="slim-Scroll">
          <div class="table-block full-width position-relative add-remind-tbl">
            <div class="table-row">
              <div class="table-cell-block media-middle add-remind-lbl">
                <b>周报日期</b>
              </div>
              <div class="table-cell-block position-relative">
              <input type="hidden" name="createdDt" id="createdDt" value=""/>
              <input type="hidden" name="beginTime" id="beginTime" value=""/>
              <input type="hidden" name="endTime" id="endTime" value=""/>
                <p class="form-control-static"></p>
              </div>
            </div>
            <div class="table-row">
              <div class="table-cell-block add-remind-lbl">
                <div><b>本周总结</b></div>
              </div>
              <div class="table-cell-block position-relative">
                <textarea name="summary" class="form-control" rows="5" placeholder="提示性文字，如本周主要完成了哪些工作任务，遇到什么问题，如何解决之类的。"  datatype="*" nullmsg="请填写本周总结！" errormsg="请填写本周总结！"></textarea>
                <div class="tooltip top" role="tooltip">
                  <div class="tooltip-arrow"></div>
                  <div class="tooltip-inner"></div>
                </div>
              </div>
            </div>
            <div class="table-row">
              <div class="table-cell-block add-remind-lbl">
                <div><b>下周计划</b></div>
              </div>
              <div class="table-cell-block position-relative">
                <textarea name="nextplan" class="form-control" rows="5" placeholder="提示性文字，如对下周需要完成的工作任务制定具体计划。"  datatype="*" nullmsg="请填写下周计划！" errormsg="请填写下周计划！"></textarea>
                <div class="tooltip top" role="tooltip">
                  <div class="tooltip-arrow"></div>
                  <div class="tooltip-inner"></div>
                </div>
                <div class="gray9 margin_t5 margin_b15">注：周报只能在执行主任点评前修改！</div>
              </div>
            </div>
            <div class="table-row">
              <div class="table-cell-block add-remind-lbl">
                 
              </div>
              <div class="table-cell-block position-relative">
                <button type="button" class="btn btn-success min-width-90px  btn-sure-edit">保存</button>
                <button type="button" class="btn btn-default min-width-90px btn-cancel-edit margin_l15" onclick="window.history.back()">取消</button>
              </div>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script src="${ctx}/static/plugins/fullcalendar/fullcalendar.min.js"></script>
<script type="text/javascript">
var vWeekOfYear=moment().format("W");//算出这年的第几周
var vWeekOfDay=moment().format("E");//算出这周的周几
var vStartDate=moment().add(-vWeekOfDay+1,'days');
var vEndDate=moment().add(7-vWeekOfDay,'days');
$(".form-control-static").html(vStartDate.format("YYYY年")+" "+vStartDate.format("W周")+" "+vStartDate.format("M月D日")+"-"+vEndDate.format("M月D日"));
$("#beginTime").val(vStartDate.format("YYYY")+"-"+vStartDate.format("MM-DD")+" 00:00");
$("#endTime").val(vStartDate.format("YYYY")+"-"+vEndDate.format("MM-DD")+" 59:59");

var currentDate = moment().format("YYYY-MM-DD HH:mm");
$("#createdDt").val(currentDate);

/*确认发送*/
var $theform=$("#theform");
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

    var msgBox=o.obj.closest('.table-cell-block').children('.tooltip');

    msgBox.children('.tooltip-inner').text(msg);
    if(msgBox.hasClass('left')){
      msgBox.css({
        width:110,
        left:-100,
        top:15
      })
      if(o.obj.is(":checkbox")){
        msgBox.css({
          top:0
        })
      }
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
    if($theform.find(".Validform_error").length>0){
      return false;
    }
    postIngIframe=$.dialog({
        title: false,
        opacity: 0,
        contentHeight:20,
        closeIcon:false,
        content: '<div class="text-center">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>',
        columnClass:'col-xs-4 col-xs-offset-4'
    });
  },
  callback:function(data){
    //这里执行回调操作;
    //如果callback里明确return alse，则表单不会提交，如果return true或没有return，则会提交表单。

    //if("成功提交") 就执行下面语句
    setTimeout(function(){//此句模拟交互，程序时请去掉
      var rIframe=$.dialog({
          title: false,
          opacity: 0,
          contentHeight:20,
          closeIcon:false,
          content: '<div class="text-center">数据提交成功...<i class="icon fa fa-check-circle"></i></div>',
          columnClass:'col-xs-4 col-xs-offset-4'
      });
      setTimeout(function(){
        postIngIframe.close();
        rIframe.close();
      },2000);
    },2000);//此句模拟交互，程序时请去掉
  }
});

$(".btn-sure-edit").click(function(event) {
  //插入业务逻辑
  /*
  ajaxPost(flag,sync,url)【返回值：Validform】
  以ajax方式提交表单。flag为true时，跳过验证直接提交，sync为true时将以同步的方式进行ajax提交。
  */
	postForm.submitForm();
});
</script>
</body>
</html>