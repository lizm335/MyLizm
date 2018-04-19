<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<title>班主任平台 - 答疑管理</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body>
	<form id="theform" action="${ctx}/home/class/newFeedback/add" method="post">
		<div class="box no-border no-shadow margin-bottom-none">
			<div class="box-header with-border">
				<h3 class="box-title">替学员提问题</h3>
			</div>
			<div class="scroll-box">
				<div class="box-body">
					<table class="table no-border">
						<colgroup>
							<col width="100"></col>
							<col></col>
						</colgroup>
						<tbody>
							<tr>
								<td class="text-right">
									<div class="pad-t5">
										<span class="text-red">*</span>
										问题标题
									</div>
								</td>
								<td>
									<div class="position-relative">
										<input type="text" name="title" class="form-control" placeholder="问题标题" 
										datatype="*" nullmsg="请填写问题标题" errormsg="请填写问题标题">
									</div>
								</td>
							</tr>
							<tr>
								<td class="text-right">
									<div class="pad-t5">
										<span class="text-red">*</span>
										问题内容
									</div>
								</td>
								<td>
									<div class="position-relative">
										<textarea class="form-control" name="content" placeholder="问题内容" datatype="*"
											 nullmsg="请填写问题内容" errormsg="请填写问题内容" rows="5"></textarea>
									</div>
								</td>
							</tr>
							<tr>
								<td class="text-right">
									<div class="pad-t5">图片附件</div>
								</td>
								<td>
									<div class="addImages2">
				                        <div class="position-relative ">
				                            <button class="btn min-width-90px btn-default btn-add-img-addon" type="button">上传图片</button>
				                            <small class="gray9">* 支持jpg/png/gif类型图片，每张不超过5mb大小</small>
				                        </div>
			                            <ul class="list-inline img-gallery upload-box">
										</ul>
									</div>
								</td>
							</tr>
							<tr>
								<td class="text-right">
									<div class="pad-t5">
										<span class="text-red">*</span>
										提问学员
									</div>
								</td>
								<td>
									<div class="position-relative">
										<button type="button" class="btn min-width-90px btn-default" data-role="sel-stu">选择学员</button>

										<table class="table-gray-th text-center margin_t15 f12">
											<thead>
												<tr>
													<th width="12%">姓名</th>
													<th width="12%">学号</th>
													<th width="12%">层次</th>
													<th width="12%">学期</th>
													<th>专业</th>
													<th width="10%">操作</th>
												</tr>
											</thead>
											<div class="tooltip top" role="tooltip" data-id="receiver-tips" style="z-index: 2; bottom: 100%; margin-bottom: -5px;">
							                  <div class="tooltip-arrow"></div>
							                  <div class="tooltip-inner"></div>
							                </div>
											<tbody data-id="stu-box" class="student-list-box">
												
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<td class="text-right">
									<div class="pad-t5">
										<span class="text-red">*</span>
										提问对象
									</div>
								</td>
								<td>
									<div class="position-relative">
										<button type="button" class="btn min-width-90px btn-default" data-role="add-object">选择对象</button>

										<table class="table-gray-th text-center margin_t15 f12">
											<thead>
												<tr>
													<th width="18%">姓名</th>
													<th width="18%">帐号</th>
													<th width="20%">角色</th>
													<th>任教课程</th>
													<th width="10%">操作</th>
												</tr>
											</thead>
											<div class="tooltip top" role="tooltip" data-id="receiver-tip" style="z-index: 2; bottom: 100%; margin-bottom: -5px;">
							                  <div class="tooltip-arrow"></div>
							                  <div class="tooltip-inner"></div>
							                </div>
											<tbody data-id="object-box" class="teacher-list-box">
												
											</tbody>
											
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="text-right pop-btn-box pad clearfix">
			<!--<button type="button" class="btn btn-primary min-width-90px pull-left" data-role="transfer-orient">转回原处（黄小米班主任）</button>-->

			<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
			<button type="submit" class="btn btn-success min-width-90px" data-role="sure">确定</button>
		</div>
	</form>
<jsp:include page="/eefileupload/upload.jsp" /> 
<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
//关闭 弹窗
$("[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});

//设置内容主体高度
$('.scroll-box').height($(frameElement).height()-106);

//确认
var $theform=$("#theform");
var htmlTemp='<div class="tooltip top" role="tooltip">'
      +'<div class="tooltip-arrow"></div>'
      +'<div class="tooltip-inner"></div>'
      +'</div>';
$theform.find(".position-relative").each(function(index, el) {
    $(this).append(htmlTemp);
});

$.Tipmsg.r='';
var postIngIframe;
var postForm=$theform.Validform({
	ajaxPost:true,
  tiptype:function(msg,o,cssctl){
    if(!o.obj.is("form")){
        var msgBox=o.obj.closest('.position-relative').find('.tooltip');
        msgBox.children('.tooltip-inner').text(msg);
        if(msgBox.hasClass('left')){
          msgBox.css({
            width:130,
            left:-120,
            top:5
          })
        } else{
          msgBox.css({
            bottom:"100%",
            'margin-bottom':-5
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
    }
  },
  beforeSubmit:function(curform){
	  if( $('.student-list-box tr').length<=0 ){
	  		$('html,body').scrollTop( 0 );
	  		$('[data-id="receiver-tips"]').addClass('in').children('.tooltip-inner').text('请添加学员');
	  		return false;
		  }else{
		  		$('[data-id="receiver-tips"]').removeClass('in')
		  } 
	  
	  if( $('.teacher-list-box tr').length<=0 ){
    		$('html,body').scrollTop( 0 );
    		$('[data-id="receiver-tip"]').addClass('in').children('.tooltip-inner').text('请添加提问对象');
    		return false;
    	}else{
    		$('[data-id="receiver-tip"]').removeClass('in')
    	} 
	  
	 
	  
    postIngIframe=$.formOperTipsDialog({
      text:'数据提交中...',
      iconClass:'fa-refresh fa-spin'
    });      
  }, callback:function(data){
        postIngIframe.find('[data-role="tips-text"]').html(data.message);
        postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
        /*关闭弹窗*/
        setTimeout(function(){
          parent.location.href=parent.location.href;
        },1500)
  }
});


/*删除图片*/
$("body").on('click', '.img-gallery .glyphicon-remove-sign', function(event) {
  event.preventDefault();
  $(this).closest('li').remove();
})
//删除提问学员/对象
.on('click', '[data-role="remove"]', function(event) {
    event.preventDefault();
    $(this).closest('tr').remove();
    $('[data-id="object-box"]').empty();
})
//选择提问对象
.on('click', '[data-role="add-object"]', function(event) {
	var studentId=$('#studentId').val();
	if(studentId==undefined){
		alert('请先选择学员');
		return false;
	}
    $.mydialog({
        id:'add-object',
        width:850,
        height:750,
        zIndex:11000,
        content: 'url:'+ctx+'/home/class/newFeedback/findAnswerUser?studentId='+studentId
      });
})
//选择提问学员
.on('click', '[data-role="sel-stu"]', function(event) {
    $.mydialog({
        id:'add-object',
        width:850,
        height:750,
        zIndex:11000,
        content: 'url:'+ctx+'/home/class/newFeedback/findAskStudent'
      });
});

/*不需要置顶按钮*/
$('.jump-top').remove();

//添加图片
$('body').on('click','.btn-add-img-addon',function() {
	var index=$('.btn-add-img-addon').index(this);
	var $container=$(this).parent().next('.upload-box');
	uploadImageNew($container, 'imgUrls' );
});	

</script>
</body>
</html>
