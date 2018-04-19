<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
	<title>班主任平台 - 班级活动</title>
	<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header">
	<h1>
		新增班级活动
	</h1>
	<button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="${ctx}/home/class/activity/list">班级活动</a></li>
		<li class="active">
			${isAdd==true?'新增班级活动':'修改班级活动'}
		</li>
	</ol>
</section>
<section class="content">
  <div class="box margin-bottom-none">
    <div class="box-body">
      <form id="theform" action="${ctx }/home/class/activity/${isAdd==true?'add':'update'}" method="post"> 
        <input type="hidden" name="id" value="${info.id }" id="activityId">
        <div class="form-horizontal reset-form-horizontal margin_t10">
          <div class="form-group">
            <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>活动名称</label>
            <div class="col-md-8 col-sm-10  position-relative">
              <input type="text" name="activityTitle"  value="${info.activityTitle}" class="form-control" datatype="*" nullmsg="请填写活动名称！" errormsg="请填写活动名称！" placeholder="活动名称">
              <div class="tooltip left" role="tooltip">
                <div class="tooltip-arrow"></div>
                <div class="tooltip-inner"></div>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>活动介绍</label>
            <div class="col-md-8 col-sm-10 position-relative">
              <div class="textarea-box">
                <textarea id="editor1" name="activityIntroduce"  rows="10" cols="80" placeholder="请输入内容">${info.activityIntroduce}</textarea>
              </div>
              <div class="tooltip left" role="tooltip">
                <div class="tooltip-arrow"></div>
                <div class="tooltip-inner"></div>
              </div>
            </div>
          </div>
          <%-- <div class="form-group">
            <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>活动图片</label>
            <div class="col-md-8 col-sm-10 margin_t5 position-relative">
              <div class="margin_b10">
                <button type="button" class="btn btn-default min-width-90px btn-sm btn-add-img-addon">添加图片附件</button>
             	
              </div>
              <div class="upload-box">
              	<c:forEach items="${activityPictures}" var="item" varStatus="vstatus">
              			<div class="upload-image-box position-relative activityPictures">
						  <div class="upload-image-container">
						    <div class="table-cell-block">
						      <img src="${item}" class="imgClass${vstatus.index }" id="imgSrc${vstatus.index }">
							  <input type="hidden" id="imgs${vstatus.index }" name="activityPicture" value="${item }"/>
						    </div>
						  </div>
						  <div class="btn btn-flat btn-sm btn-block btn-default upload-image-btn">
						    点击添加
						    <input type="button" class="btn-file"  nullmsg="请添加活动图！" errormsg="请添加活动图！"
						 		onclick="javascript:uploadImage('imgSrc${vstatus.index }','imgs${vstatus.index }',null, null, uploadCallback);">
						  </div>
						  <div class="tooltip top" role="tooltip">
						    <div class="tooltip-arrow"></div>
						    <div class="tooltip-inner"></div>
						  </div>
						  <div class="remove-addon">
						    <span data-toggle="tooltip" title="删除">
						      <i class="fa fa-fw fa-close"></i>
						    </span>
						  </div>
						</div>
              	</c:forEach>
              </div>
            </div>
          </div> --%>
          <div class="form-group">
            <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>活动时间</label>
            <div class="col-md-8 col-sm-10  position-relative">
              <div class="input-group" >
                 <input type="text" id="beginTime" name="beginTime" value="<fmt:formatDate value="${info.beginTime}" pattern="yyyy-MM-dd HH:mm" />" 
                 	class="form-control single-datetime2" datatype="checkTime" nullmsg="请填写活动开始时间" errormsg="日期格式不对">
                  <span class="input-group-addon">－</span>
                  <input type="text" id="endTime" name="endTime" value="<fmt:formatDate value="${info.endTime}" pattern="yyyy-MM-dd HH:mm" />" 
                  class="form-control single-datetime2" datatype="checkTime" nullmsg="请填写活动结束时间" errormsg="日期格式不对">
              </div>
               
              <div class="tooltip left" role="tooltip">
                <div class="tooltip-arrow"></div>
                <div class="tooltip-inner"></div>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>活动地点</label>
            <div class="col-md-8 col-sm-10  position-relative">
              <input type="text" name="activityAddress"  value="${info.activityAddress}"   class="form-control" placeholder="活动地点" datatype="*" nullmsg="请填写活动地点！" errormsg="请填写活动地点！">
              <div class="tooltip left" role="tooltip">
                <div class="tooltip-arrow"></div>
                <div class="tooltip-inner"></div>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>活动宣传图</label>
            <div class="col-md-8 col-sm-10 margin_t5 position-relative">
            	
            	<div  class=" upload-image-box position-relative  ">
				<div  class="upload-image-container">
					<div class="table-cell-block">					 
						<img id="headImgId" class="imgClass" src="${info.publicityPicture}"/>
					</div>
				</div>
				<div class="btn btn-flat btn-sm btn-block btn-default upload-image-btn">
			    添加活动宣传图
			   <input class="btn-file" value="添加宣传图片" type="button" datatype="*" nullmsg="请添加活动宣传图！" errormsg="请添加活动宣传图！"
								 onclick="javascript:uploadImage('headImgId','headImgUrl');">
			  </div>
				
			</div>
              
			<input id="headImgUrl" type="hidden"  name ="publicityPicture" value="${info.publicityPicture}"/>
			
              <div class="tooltip top" role="tooltip">
                <div class="tooltip-arrow"></div>
                <div class="tooltip-inner"></div>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>人数上限</label>
            <div class="col-md-8 col-sm-10  position-relative">
              <input type="text"  value="${info.ceilingNum}" name="ceilingNum" class="form-control" placeholder="人数上限" datatype="n" nullmsg="请填写上限人数！" errormsg="请填写数字！">
              <div class="tooltip left" role="tooltip">
                <div class="tooltip-arrow"></div>
                <div class="tooltip-inner"></div>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>是否收费</label>
            <div class="col-md-8 col-sm-10 position-relative">
              <div class="chkbox">
                <label>
                  <input type="radio" name="isFree" class="minimal" value="1" <c:if test="${empty info.isFree || info.isFree==1}"> checked="checked"</c:if>>
									<span class="text-no-bold">否</span>
                </label>
                <label class="left10">
                  <input type="radio" name="isFree" class="minimal" value="2" <c:if test="${info.isFree==2}"> checked="checked"</c:if>>
				 <span class="text-no-bold">是</span>
                </label>
              </div>
              <div class="tooltip top" role="tooltip">
                  <div class="tooltip-arrow"></div>
                  <div class="tooltip-inner"></div>
                </div>
            </div>
          </div>
          <div class="form-group act-cost-box" <c:if test="${info.isFree==2}"> style="display: block;"</c:if>>
            <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>活动费用</label>
            <div class="col-md-8 col-sm-10 position-relative">
              <div class="row">
                <div class="col-xs-3">
                  <div class="input-group">
                    <input type="text"  value="${info.chargeMoney}" name="chargeMoney" class="form-control" nullmsg="请填写活动费用！" errormsg="请填写数字！">
                    <div class="input-group-addon">
                      元/人
                    </div>
                  </div>
                </div>
              </div>
              <div class="tooltip left" role="tooltip">
                <div class="tooltip-arrow"></div>
                <div class="tooltip-inner"></div>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>是否审核</label>
            <div class="col-md-8 col-sm-10 position-relative">
              <div class="chkbox">
                <label>
                  <input type="radio" name="auditStatus" class="minimal" value="0" <c:if test="${empty info.auditStatus || info.auditStatus==0}"> checked="checked"</c:if>>
				 <span class="text-no-bold">否</span>
                </label>
                <label class="left10">
                  <input type="radio" name="auditStatus" class="minimal" value="1"  <c:if test="${info.auditStatus==1}"> checked="checked"</c:if>>
			 	 <span class="text-no-bold">是</span>
                </label>
              </div>
              <div class="tooltip top" role="tooltip">
                <div class="tooltip-arrow"></div>
                <div class="tooltip-inner"></div>
              </div>
            </div>
          </div>

          <div class="form-group margin_t20">
            <label class="col-md-2 col-sm-2 control-label"></label>
            <div class="col-md-8 col-sm-10">
              <button type="button" class="btn btn-success min-width-90px margin_r15 btn-sure-edit">发布</button>
              <button type="button" class="btn btn-default min-width-90px btn-cancel-edit">取消</button>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</section>


<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

 <jsp:include page="/eefileupload/upload.jsp"/> 

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>


<script type="text/template" id="temp">
<div class="upload-image-box position-relative activityPictures">
  <div class="upload-image-container">
    <div class="table-cell-block">
      <img src="${ctx}/static/dist/img/images/id-image.png" class="imgClass" id="imgSrc">
	  <input type="hidden" id="imgs" name="activityPicture" value=""/>
    </div>
  </div>
  <div class="btn btn-flat btn-sm btn-block btn-default upload-image-btn">
    点击添加
    <input type="button" class="btn-file"  nullmsg="请添加活动图！" errormsg="请添加活动图！"
 		onclick="javascript:uploadImage('imgSrc','imgs',null, null, uploadCallback);">
  </div>
  <div class="tooltip top" role="tooltip">
    <div class="tooltip-arrow"></div>
    <div class="tooltip-inner"></div>
  </div>
  <div class="remove-addon">
    <span data-toggle="tooltip" title="删除">
      <i class="fa fa-fw fa-close"></i>
    </span>
  </div>
</div>
</script>

<!-- ckeditor --> 
<script src="${ctx}/static/plugins/ckeditor/ckeditor.js"></script>
<script type="text/javascript">

function uploadCallback() {
	var list = document.getElementsByClassName('imgClass');
	/*for(var i=0;i<list.length;i++) {
		list[i].src=$("#headImgUrl").val();
	}*/
}	



//编辑器，复制图片，把base64替换为URL.
CKEDITOR.replace('editor1',{
	on:{
		change:function(evt){
			evt.sender.element.$.value=evt.editor.getData();
		},
		instanceReady:function (){
			if(window.CKEDITOR){
				var ckOjx=this;
				var $doc=$(this.document.$.body);
				/* Paste in chrome.*/
				if(CKEDITOR.env.webkit)
				{
					$doc.bind('paste', function(ev)
					{
						var $this    = $(this);
						var original = ev.originalEvent;
						if(original.clipboardData && window.FileReader){
							var file = original.clipboardData.items[0].getAsFile();
							if(file)
							{
								var reader = new FileReader();
								reader.onload = function(e) 
								{
									var result = e.target.result; 
				 				 	$.post(ctx+'/home/class/activity/addImg',{imgBase:result},function(data){
				 				 		var imgUrl=data.message;
				 				 		html = '<img src="' + imgUrl + '" alt="" />';
										CKEDITOR.instances[ckOjx.name].insertHtml(html);
									},'json');  
								};
								reader.readAsDataURL(file);
							}
						}
					});
				}
				/* Paste in firfox and other firfox.*/
				else if(!CKEDITOR.env.gecko)
				{
					$doc.bind('paste', function(ev)
					{
						setTimeout(function()
						{
							var html = $doc.html();
							if(html.search(/<img src="data:.+;base64,/) > -1)
							{
								//$doc.html(html.replace(/<img src="data:.+;base64,.*".*\/>/, ''));
								//$.post('url-post', {editor: html}, function(data){$doc.html(data);});
							}
						}, 80);
					});
				}
			}
		}
	}
});


//iCheck for checkbox and radio inputs
$('input.minimal').iCheck({
  checkboxClass: 'icheckbox_minimal-blue',
  radioClass: 'iradio_minimal-blue'
}).on("ifChecked",function(e){
  var $e=$(e.target);
  $e.attr('checked',true);
  if($e.attr("name")=="isFree" && $e.val()=='2'){
    $(".act-cost-box").show().find('.form-control').attr({
      datatype: 'n'
    });
  }
}).on("ifUnchecked",function(e){
  var $e=$(e.target);
  $e.attr('checked',false);
  if($e.attr("name")=="isFree" && $e.val()=='2'){
    $(".act-cost-box").hide().find('.form-control').removeAttr('datatype').removeClass('Validform_error')
  }
});


var $activityId=$('#activityId').val();
var $href='';
if($activityId==''){
	$href='${ctx}/home/class/activity/list';
}else{
	$href='${ctx}/home/class/activity/view/'+$activityId;
}

/*确认发送*/
var $theform=$("#theform");
$.Tipmsg.r='';
var postIngIframe;
var postForm=$theform.Validform({
  showAllError:true,
  datatype:{
	  "checkTime":function(gets,obj,curform){
		  var reg1=/^(\d{4})\-(\d{2})\-(\d{2}) (\d{2}):(\d{2})$/i;
		  if(!reg1.test(gets)){
			  return false;
		  }
		  var beginDate=$("#beginTime").val();  
		  var endDate=$("#endTime").val();  
		  var d1 = new Date(beginDate.replace(/\-/g, "\/"));  
		  var d2 = new Date(endDate.replace(/\-/g, "\/"));  
		   if(beginDate!=""&&endDate!=""&&d1 >=d2) {  
		   		return "开始时间不能大于结束时间！";  
		  }
	  }
  },
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
    if($theform.find(".Validform_error").length>0){
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
	  var rIframe=$.dialog({
          title: false,
          contentHeight:20,
          closeIcon:false,
          content: '<div class="text-center">'+data.message+data.type+'<i class="icon fa fa-check-circle"></i></div>',
          columnClass:'col-xs-4 col-xs-offset-4',
      }); 
		  setTimeout(function(){
	 		 if(data.successful==true){
		        postIngIframe.close();
		        rIframe.close();
		        window.location.href=$href;
		      }else{
		    	  postIngIframe.close();
			      rIframe.close();
		      }
		   },2000);
	  }
    //这里执行回调操作;
    //如果callback里明确return alse，则表单不会提交，如果return true或没有return，则会提交表单。

    //if("成功提交") 就执行下面语句
    //此句模拟交互，程序时请去掉
/*     setTimeout(function(){
      var rIframe=$.dialog({
          title: false,
          contentHeight:20,
          closeIcon:false,
          content: '<div class="text-center">数据提交成功...<i class="icon fa fa-check-circle"></i></div>',
          columnClass:'col-xs-4 col-xs-offset-4',
      }); */
      /*关闭弹窗*/
/*       setTimeout(function(){
        postIngIframe.close();
        rIframe.close();
      },2000)
    },2000); */
    //此句模拟交互，程序时请去掉
  //}
});

function checkTextarea(){
  var $editor=$("#editor1");
  var $textareaBox=$(".textarea-box");
  var msgBox=$editor.closest('.position-relative').children('.tooltip');
  msgBox.css({
    width:130,
    left:-120,
    top:5
  })

  if(CKEDITOR.instances.editor1.getData()!==""){
    $editor.val(CKEDITOR.instances.editor1.getData());
    $textareaBox.removeClass('Validform_error');
    msgBox.removeClass('in');
  }
  else{
    $textareaBox.addClass('Validform_error');
    msgBox.children('.tooltip-inner').text('请填写活动介绍！');
    msgBox.addClass('in');
  }
}
$(".btn-sure-edit").click(function(event) {
  /*插入业务逻辑*/
  checkTextarea();
  postForm.ajaxPost();
});
/*取消*/
$(".btn-cancel-edit").click(function(event) {
  self.history.back();
});

var $addDiv=50;
/*添加多图*/
$(".btn-add-img-addon").click(function(event) {
	$addDiv++;
  $(".upload-box").append($("#temp").html());
  var $div=$(".activityPictures:last");
  $div.find('img').attr('id','imgSrc'+$addDiv);
  $div.find('#imgs').attr('id','imgs'+$addDiv);
/*   onclick="javascript:uploadImage('imgSrc','imgs',null, null, uploadCallback) */
  $div.find('.btn-file').attr('onclick',"javascript:uploadImage('imgSrc"+$addDiv+"','imgs"+$addDiv+"',null, null, uploadCallback)");

});

$("body").on('click', '.remove-addon', function(event) {
  event.preventDefault();
  $(this).parent().remove();
});

/*日期控件*/
$('.input-daterange input').datepicker({
  language:'zh-CN'
});



</script>

</body>
</html>
