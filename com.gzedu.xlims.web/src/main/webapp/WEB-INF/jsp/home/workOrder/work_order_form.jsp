<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>工单管理</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="#">我的工单</a>
			</li>
			<li class="active">新建工单</li>
		</ol>
	</section>
	<section class="content">
		<form id="theform" action="${action }" method="post">
			<input type="hidden" name="id" value="${entity.id }">
			<input type="hidden" name="action" value="${action }">
			<div class="box no-margin school-set-box">
				<div class="box-body">
					<div class="form-horizontal reset-form-horizontal margin_t10">
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>标题</label>
							<div class="col-md-8 col-sm-10">
								<div class="position-relative">
									<input type="text" name="title" value="${entity.title }" class="form-control" placeholder="标题" datatype="*" nullmsg="请填写标题" errormsg="请填写标题">
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>类型</label>
							<div class="col-md-8 col-sm-10">
								<div class="position-relative">
									<select name="workOrderType" class="form-control select2" datatype="*" nullmsg="请选择类型" errormsg="请选择类型" >
										<option value="">请选择类型</option>
										<c:forEach items="${workTypeMap}" var="map">
											<option value="${map.key}" <c:if test="${entity.workOrderType eq map.key}">selected</c:if>>${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>级别</label>
							<div class="col-md-8 col-sm-10">
								<div class="position-relative">
									<select name="priority" class="form-control" datatype="*" nullmsg="请选择优先级级别" errormsg="请选择优先级级别" >
										<option value="">请选择优先级</option>
										<option value="0" <c:if test="${entity.workOrderType eq '0'}">selected</c:if>>一般</option>
										<option value="1" <c:if test="${entity.workOrderType eq '1'}">selected</c:if>>优先</option>
										<option value="2" <c:if test="${entity.workOrderType eq '2'}">selected</c:if>>紧急</option>
									</select>
								</div>
							</div>
						</div>
						<c:if test="${action eq 'create'}">
							<div class="form-group">
								<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>指派给</label>
								<div class="col-md-8 col-sm-10">
									<div class="table-block control-group">
										<div class="table-cell-block">
											<div class="position-relative">
												<ul class="list-unstyled stu-list-box clearfix" data-id="assign">
												</ul>
											</div>
										</div>
										<div class="table-cell-block pad-l10">
											<button type="button" class="btn btn-block btn-default" data-role="add-person-btn">添加指派对象</button>
										</div>
									</div>
								</div>
							</div>
						</c:if>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>内容</label>
							<div class="col-md-8 col-sm-10">
								<div class="position-relative">
									<textarea id="editor4" name="content" rows="10" class="full-width position-absolute show" datatype="*"
										nullmsg="请填写内容" errormsg="请填写内容">
										${entity.content }
										</textarea>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>要求完成时间</label>
							<div class="col-md-8 col-sm-10">
								<div class="position-relative">
									<input type="text" name="demandDates" value="${entity.demandDate }" class= "form-control" placeholder="完成时间" datatype="*" nullmsg="请填写时间" errormsg="时间格式不对" data-role="datetime">
								</div>
							</div>
						</div>
						<c:if test="${action eq 'create'}">
							<div class="form-group">
								<label class="col-md-2 col-sm-2 control-label">抄送给</label>
								<div class="col-md-8 col-sm-10">
									<div class="table-block control-group">
										<div class="table-cell-block">
											<ul class="list-unstyled stu-list-box clearfix" data-id="cc">
											</ul>
										</div>
										<div class="table-cell-block pad-l10">
											<button type="button" class="btn btn-block btn-default" data-role="add-cc-btn">添加抄送对象</button>
										</div>
									</div>
								</div>
							</div>
						</c:if>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label">附件</label>
							<div class="col-md-8 col-sm-10">
								<div class="table-block control-group">
									<div class="table-cell-block">
										<input type="text" id="fileName" name="fileName" value="${entity.fileName }" readonly="readonly"> 
										<input type="hidden" id="fileUrl" name="fileUrl" value="${entity.fileUrl }">
										<input value="选择文件" type="button" onclick="javascript:uploadFile('fileName','fileUrl');" /> 
										<input value="删除附件" type="button" id="deleteFile" />
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"></label>
							<div class="col-md-8 col-sm-10">
								<button type="submit" class="btn btn-success min-width-90px margin_r15" data-role="submit">
								<c:if test="${action eq 'create' }">发布</c:if>
								<c:if test="${action eq 'update' }">修改</c:if>
								</button>
								<button type="button" class="btn btn-default min-width-90px" data-role="cancel" onclick="history.back()">取消</button>
							</div>
						</div>
				</div>
				<!-- /.box-body -->
			</div>
		</form>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
		<jsp:include page="/eefileupload/upload.jsp" />
	<script type="text/javascript">
	
	//删除上传附件	
	$('#deleteFile').click(function() {
		$('#fileName').val('');
		$('#fileUrl').val('');
	});

	//下拉渲染
	$(".select2").select2({language: "zh-CN"});
	
	/*日期控件*/
	$('[data-role="datetime"]').datepicker({
		language:'zh-CN',
		format:'yyyy-mm-dd'
	});
	//删除添加的人员
	$(".stu-list-box").on("click",".fa-remove",function(){
	  $(this).parent().remove();
	});

	//编辑器
	CKEDITOR.replace('editor4',{
		on:{
			change:function(evt){
				evt.sender.element.$.value=evt.editor.getData();
			},
			instanceReady:ckeditorPaste
		}
	});

	//添加指派对象
	$('[data-role="add-person-btn"]').on('click', function(event) {
		event.preventDefault();
		$.mydialog({
		  id:'task',
		  width:900,
		  height:500,
		  zIndex:11000,
		  urlData:{
		  	frame:'parent',//接收窗口
		  	acceptBox:'[data-id="assign"]'//接收结果的容器
		  },
		  content: 'url:${ctx}/home/workOrder/findRole?type=1'
		});
	});

	//抄送给
	$('[data-role="add-cc-btn"]').on('click', function(event) {
		event.preventDefault();
		$.mydialog({
		  id:'task',
		  width:900,
		  height:500,
		  zIndex:11000,
		  urlData:{
		  	frame:'parent',//接收窗口
		  	acceptBox:'[data-id="cc"]'//接收结果的容器
		  },
		  content: 'url:${ctx}/home/workOrder/findRole?type=2'
		});
	});
		/*失败状态
		$.resultDialog(
			{
				type:2,
				msg:'发布失败！',
				timer:1500,
				width:150,
				height:50
			}
		);
		*/
		/*成功状态
		$.resultDialog(
			{
				type:1,
				msg:'发布成功！',
				timer:1500,
				width:150,
				height:50
			}
		);
		*/
		/*服务器繁忙状态
		$.resultDialog(
			{
				type:0,
				msg:'服务器繁忙，请稍后再试！',
				timer:1500,
				width:265,
				height:50
			}
		);
		*/

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
	    var postIngIframe;
	    var postForm=$theform.Validform({
	      showAllError:true,
	      ignoreHidden:true,//是否忽略验证不可以见的表单元素
	      ajaxPost:true,
	      tiptype:function(msg,o,cssctl){
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

	        postIngIframe=$.formOperTipsDialog({
	          text:'数据提交中...',
	          iconClass:'fa-refresh fa-spin'
	        });
	      },
	      callback:function(data){
	        	postIngIframe.find('[data-role="tips-text"]').html(data.message);
				postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
				setTimeout(function(){
					$.closeDialog(postIngIframe);
						if(data.successful==true){
							window.location.href='${ctx}/home/workOrder/putList';
						}
				},2000); 
	      }
	    });

	})();
	</script>

</body>
</html>
