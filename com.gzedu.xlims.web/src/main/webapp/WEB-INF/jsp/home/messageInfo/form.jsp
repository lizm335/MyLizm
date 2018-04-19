<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>
</head>
<%@ include file="/WEB-INF/jsp/common/jslibs_new.jsp"%>
<body class="inner-page-body">
	<section class="content-header clearfix">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="homepage.html"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="${ctx }/admin/messageInfo/list">通知公告</a>
			</li>
			<li class="active">新增通知公告</li>
		</ol>
	</section>
	<section class="content">
		<form id="theform" class="form-horizontal" role="form" action="${ctx }/admin/messageInfo/${action}" method="post">
			<input id="action" type="hidden" name="action" value="${action }">
			<input type="hidden" name="messageId" value="${info.messageId }">
			<div class="box margin-bottom-none">
				<div class="box no-margin school-set-box">
					<div class="box-body">
						<div class="form-horizontal reset-form-horizontal margin_t10">
							<div class="form-group">
								<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>标题</label>
								<div class="col-md-8 col-sm-10">
									<div class="position-relative">
										<input type="text" name="infoTheme" value="${info.infoTheme }" class="form-control" placeholder="标题" datatype="*" nullmsg="请填写标题" errormsg="请填写标题" />
									</div>
								</div>
							</div>

							<div class="form-group">
								<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>通知类型</label>
								<div class="col-md-8 col-sm-10">
									<div class="position-relative">
										<select class="form-control select2" name="infoType" datatype="*" nullmsg="请选择通知类型" errormsg="请选择类型" data-size="8" data-live-search="true">
											<option value="">请选择</option>
											<c:forEach items="${infoTypeMap}" var="map">
												<option value="${map.key}" <c:if test="${infoTypeId == map.key}">selected='selected'</c:if>>${map.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<c:if test="${action eq 'create' }">
								<div class="form-group">
									<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>接受身份</label>
									<div class="col-md-8 col-sm-10">
										<div class="position-relative">
											<c:forEach items="${getUserRoleMap }" var="map" varStatus="var">
												<label class="text-no-bold"> <input type="checkbox" name="roleType" class="minimal" value="${map.key }" data-name="receiver"
													datatype="*" nullmsg="请选择接受身份" errormsg="请选择接受身份" <c:if test="${var.first}">checked</c:if>> ${map.value }
												</label>
											</c:forEach>
										</div>
									</div>
								</div>
								<div class="form-group hide-block" data-id="more-items">
									<label class="col-md-2 col-sm-2 control-label">接收范围</label>
									<div class="col-md-8 col-sm-10">
										<div class="full-width width-border-dashed pad20 margin_t10 position-relative">
											<div class="form-group">
												<span class="left10">*接收范围：只针对学员</span>
											</div>
											<div class="form-group">
												<div class="col-sm-10">
													<select name="gradeId" class="form-control select2" multiple="multiple" data-placeholder="全部年级" style="width: 100%;">
														<c:forEach items="${gradeMap}" var="map">
															<option value="${map.key}">${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</div>
											<div class="form-group">
												<div class="col-sm-10">
													<select name="pycc" class="selectpicker show-tick form-control" data-size="10" data-live-search="true">
														<option value="" selected="selected">全部层次</option>
														<c:forEach items="${pyccMap}" var="map">
															<option value="${map.key}">${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</div>
											<div class="form-group margin-bottom-none">
												<div class="col-sm-10">
													<select name="specialtyId" class="form-control select2" multiple="multiple" data-placeholder="全部专业" style="width: 100%;">
														<c:forEach items="${specialtyMap}" var="map">
															<option value="${map.key}">${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 col-sm-2 control-label no-pad-top">是否推送到微信</label>
									<div class="col-md-8 col-sm-10 position-relative">
										<div class="radio form-control-static no-padding">
											<label class="margin_r15"> 
												<input type="checkbox"  name="isWeiXin"  value="1" class="minimal" checked="checked"> 
											</label> 
										</div>
									</div>
								</div>
							</c:if>

						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"><small class="text-red">*</small>通知内容</label>
							<div class="col-md-8 col-sm-10 position-relative">
								<div class="textarea-box">
									<textarea id="editor1" name="infoContent" rows="10" cols="80" class="full-width position-absolute show" datatype="*" nullmsg="请填写通知内容"
										errormsg="请填写通知内容">${info.infoContent }</textarea>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label">上传附件</label>
							<div class="col-md-8 col-sm-10">
								<input type="text" id="fileName" name="fileName" value="${info.fileName }" readonly="readonly"> 
								<input type="hidden" id="fileUrl" name="fileUrl" value="${info.fileUrl }">
								<input value="选择文件" type="button" onclick="javascript:uploadFile('fileName','fileUrl');" /> 
								<input value="删除附件" type="button" id="deleteFile" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label no-pad-top">通知置顶</label>
							<div class="col-md-8 col-sm-10 position-relative">
								<div class="radio form-control-static no-padding">
									<label class="margin_r15"> 
										<input type="radio" data-role="top" name="stick" data-value="0" value="0" <c:if test="${empty  isStick or isStick==false  }">checked</c:if>> 不置顶
									</label> 
									<label> 
										<input type="radio" data-role="top" name="stick" data-value="1" value="1" <c:if test="${isStick}">checked</c:if> > 置顶
									</label>
								</div>
							</div>
						</div>
						<div class="form-group" style="display: none;" data-id="top-time">
							<div class="clearfix">
								<label class="col-md-2 col-sm-2 control-label">置顶时间</label>
								<div class="col-md-8 col-sm-10 position-relative">
									<select name="days" class="form-control" nullmsg="请选择置顶时长！" errormsg="请选择置顶时长！" data-id="top-time-form-control">
										<option value="">请选择置顶时间</option>
										<option value="7">置顶1个星期</option>
										<option value="15">置顶半个月</option>
										<option value="30">置顶1个月</option>
										<option value="90">置顶3个月</option>
										<option value="182">置顶半年</option>
										<option value="365">置顶1年</option>
										<option value="3650">一直置顶</option>
										<option value="0" data-value="custom">自定义置顶时间</option>
									</select>
								</div>
							</div>
							<div class="clearfix margin_t15" style="display: none;" data-id="custom-time">
								<label class="col-md-2 col-sm-2 control-label"></label>
								<div class="col-md-8 col-sm-10 position-relative">
									<input type="text" name="times" class="form-control" placeholder="设置置顶结束时间" nullmsg="请设置置顶结束时间" errormsg="时间格式不对" data-role="datetime">
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-md-2 col-sm-2 control-label"></label>
							<div class="col-md-8 col-sm-10">
								<button type="submit" id="btnSubmit" class="btn btn-success min-width-90px margin_r15" data-role="submite">发布</button>
								<button type="button" class="btn btn-default min-width-90px" data-role="cancel" onclick="history.back()">取消</button>
							</div>
						</div>
					</div>
				</div>
				<!-- /.box-body -->
			</div>
			</div>
			
		</form>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

	<jsp:include page="/eefileupload/upload.jsp" />
	<script type="text/javascript">
	$(function() {
		//Initialize Select2 Elements
		$(".select2").select2({language: "zh-CN"});
		//iCheck for checkbox and radio inputs
		$('input.minimal').iCheck({
			checkboxClass : 'icheckbox_minimal-blue',
			radioClass : 'iradio_minimal-blue'
		});
		//初始化全部下拉
		$("div[data-id='more-items']").slideDown("fast");
		/* .on("ifChecked",function(e){
			var $e=$(e.target);
			$e.attr('checked',true);
			if($e.is(":radio")){
				if($e.val()=="student"){
					$("div[data-id='more-items']").slideDown("fast");
				}
				else{
					$("div[data-id='more-items']").slideUp("fast");
				}
			}
		}).on("ifUnchecked",function(e){
			$(e.target).attr('checked',false);
		}); */

		//编辑器
		CKEDITOR.replace('editor1', {
			on : {
				change : function(evt) {
					evt.sender.element.$.value = evt.editor.getData();
				},
				instanceReady : ckeditorPaste
			}
		});
		
		//表单提交验证
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
		      ajaxPost:true,
		      ignoreHidden:true,//是否忽略验证不可以见的表单元素
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
							window.location.href='${ctx}/admin/messageInfo/putList';
						}
				},1000); 
			}
	    });
		    
		//删除上传附件	
		$('#deleteFile').click(function() {
			$('#fileName').val('');
			$('#fileUrl').val('');
		});

		//置顶选项
		$('[data-role="top"]').click(function(event) {
			if ($(this).prop('checked')
					&& $(this).attr('data-value') == 1) {
				$('[data-id="top-time"]').show();
				$('[data-id="top-time-form-control"]').attr('datatype','*');
			} else {
				$('[data-id="top-time"]').hide();
				$('[data-id="top-time-form-control"]').removeAttr('datatype').removeClass('Validform_error');
			}
		});

		//自定义置顶时间
		$('[data-id="top-time-form-control"]').on('change',function(event) {
			if ($(this).children().eq(this.selectedIndex).attr('data-value') == 'custom') {
				$('[data-id="custom-time"]').show();
				$('[data-role="datetime"]').attr('datatype', '*');
			} else {
				$('[data-id="custom-time"]').hide();
				$('[data-role="datetime"]').removeAttr('datatype').removeClass('Validform_error');
			}
		});
		/*日期控件*/
		$('[data-role="datetime"]').datepicker({
			language : 'zh-CN',
			format : 'yyyy-mm-dd',
			startDate:increaseOnedate(new Date())
		});
	});
</script>
</body>
</html>