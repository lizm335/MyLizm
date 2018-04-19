<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教材计划</a></li>
		<li class="active">发布教材计划</li>
	</ol>
</section>
<section class="content">
	<div class="box margin-bottom-none">
		<div class="box-body">
			<form id="inputForm" action="${ctx }/textbookPlan/${action}" method="post">
				<input id="action" type="hidden" name="action" value="${action}">
            	<input type="hidden" name="planId" value="${entity.planId}">
		        <div class="form-horizontal reset-form-horizontal">
		        	<c:choose>
		        	
		        		<c:when test="${action=='create'}">
							<div class="form-group">
								<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>所属学期</label>
								<div id="gradeDiv" class="col-sm-6 position-relative">
									<select id="gradeId" name="gradeId" class="selectpicker show-tick form-control" data-size="5" data-live-search="true" datatype="*" nullmsg="请选择所属学期！" errormsg="请选择所属学期！">
										<option value="" selected="selected">请选择学期</option>
										<c:forEach items="${termMap}" var="map">
											<option value="${map.key}"<c:if test="${map.key==entity.gradeId}">selected='selected'</c:if> >${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>适用学期</label>
								<div class="col-sm-6 position-relative">
									<select name="gradeIds" class="form-control select2" datatype="*" multiple="multiple" data-placeholder="全部学期" style="width: 100%;">
										<c:forEach items="${termMap}" var="map">
											<option value="${map.key}" >${map.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>计划编号</label>
								<div id="planCodeDiv" class="col-sm-6 position-relative">
									<input id="planCode" name="planCode" value="${entity.planCode}" type="text" class="form-control" placeholder="建议输入年+月份作为编号" datatype="*" nullmsg="请填写计划编号！" errormsg="请填写计划编号！">
								</div>
							</div>
		        		</c:when>
		        		
		        		<c:otherwise>
		        			<div class="form-group">
								<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>所属学期</label>
								<div class="col-sm-6 position-relative">
									<input type="text" class="form-control" value="${entity.gjtGrade.gradeName}" readOnly="true" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>适用学期</label>
								<div class="col-sm-6 position-relative">
									<select name="" class="form-control select2" multiple="multiple" data-placeholder="全部学期"   style="width: 100%;">
										<c:forEach items="${entity.gjtTextbookGradeList}" var="map">
											<option value="${map.gjtGrade.gradeId}" selected="selected">${map.gjtGrade.gradeName}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>计划编号</label>
								<div class="col-sm-6 position-relative">
									<input value="${entity.planCode}" type="text" class="form-control"  readOnly="true">
								</div>
							</div>
		        		</c:otherwise>
		        	</c:choose>
		        	
					<div class="form-group">
						<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>计划名称</label>
						<div class="col-sm-6 position-relative">
							<input name="planName" value="${entity.planName}" type="text" class="form-control" placeholder="计划名称" datatype="*" nullmsg="请填写计划名称！" errormsg="请填写计划名称！">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>收货地址确认时间</label>
						<div class="col-sm-6">
							<div class="input-group-custom" data-role="daterangetime-group">
			            		<div class="form-control-box position-relative">
			            			<input name="oaddressConfirmSdate" value="<fmt:formatDate value='${entity.oaddressConfirmSdate}' type='date' pattern='yyyy-MM-dd'/>"
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" 
			            				nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly>
			            		</div>
			            		<p class="input-group-addon">~</p>
			            		<div class="form-control-box position-relative">
			            			<input name="oaddressConfirmEdate" value="<fmt:formatDate value='${entity.oaddressConfirmEdate}' type='date' pattern='yyyy-MM-dd'/>" 
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" 
			            				nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly>
			            		</div>
	            			</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>教材发放编排时间</label>
						<div class="col-sm-6">
							<div class="input-group-custom" data-role="daterangetime-group">
			            		<div class="form-control-box position-relative">
			            			<input name="arrangeSdate" value="<fmt:formatDate value='${entity.arrangeSdate}' type='date' pattern='yyyy-MM-dd'/>" 
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间"
			            				nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" data-date-orientation="top" readonly>
			            		</div>
			            		<p class="input-group-addon">~</p>
			            		<div class="form-control-box position-relative">
			            			<input name="arrangeEdate" value="<fmt:formatDate value='${entity.arrangeEdate}' type='date' pattern='yyyy-MM-dd'/>" 
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" 
			            				nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" data-date-orientation="top" readonly>
			            		</div>
	            			</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>教材订购时间</label>
						<div class="col-sm-6">
							<div class="input-group-custom" data-role="daterangetime-group">
			            		<div class="form-control-box position-relative">
			            			<input name="orderSdate" value="<fmt:formatDate value='${entity.orderSdate}' type='date' pattern='yyyy-MM-dd'/>" 
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" 
			            				nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-start" readonly>
			            		</div>
			            		<p class="input-group-addon">~</p>
			            		<div class="form-control-box position-relative">
			            			<input name="orderEdate" value="<fmt:formatDate value='${entity.orderEdate}' type='date' pattern='yyyy-MM-dd'/>"  
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" 
			            				nullmsg="请填写时间" errormsg="日期格式不对" data-role="date-end" readonly>
			            		</div>
	            			</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>教材发放时间</label>
						<div class="col-sm-6">
							<div class="input-group-custom" data-role="daterangetime-group">
			            		<div class="form-control-box position-relative">
			            			<input name="odistributeSdate" value="<fmt:formatDate value='${entity.odistributeSdate}' type='date' pattern='yyyy-MM-dd'/>" 
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" 
			            				errormsg="日期格式不对" data-role="date-start" readonly>
			            		</div>
			            		<p class="input-group-addon">~</p>
			            		<div class="form-control-box position-relative">
			            			<input name="odistributeEdate" value="<fmt:formatDate value='${entity.odistributeEdate}' type='date' pattern='yyyy-MM-dd'/>" 
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" 
			            				errormsg="日期格式不对" data-role="date-end" readonly>
			            		</div>
	            			</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label text-no-bold"><small class="text-red">*</small>教材反馈处理时间</label>
						<div class="col-sm-6">
							<div class="input-group-custom" data-role="daterangetime-group">
			            		<div class="form-control-box position-relative">
			            			<input name="ofeedbackSdate" value="<fmt:formatDate value='${entity.ofeedbackSdate}' type='date' pattern='yyyy-MM-dd'/>"
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="起始时间" nullmsg="请填写时间" 
			            				errormsg="日期格式不对" data-role="date-start" readonly>
			            		</div>
			            		<p class="input-group-addon">~</p>
			            		<div class="form-control-box position-relative">
			            			<input name="ofeedbackEdate" value="<fmt:formatDate value='${entity.ofeedbackEdate}' type='date' pattern='yyyy-MM-dd'/>"
			            				type="text" class="form-control bg-white" datatype="/^(\d{4})\-(\d{1,2})\-(\d{1,2})$/i" placeholder="结束时间" nullmsg="请填写时间" 
			            				errormsg="日期格式不对" data-role="date-end" readonly>
			            		</div>
	            			</div>
						</div>
					</div>
				</div>

				<c:if test="${action != 'view'}">
					<div class="row-offset-10px clearfix text-center">
						<div class="box-footer">
							<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="cancel" onclick="history.back()">取消</button>
							<button type="submit" class="btn btn-success min-width-90px " data-role="save1">确认发布</button>
						</div>
					</div>
				</c:if>
			</form>
			
			<c:if test="${action == 'view'}">
				<div class="cnt-box-header with-border margin_t20 margin_b20">
				  <h3 class="cnt-box-title">审核记录：</h3>
				</div>
	
				<div class="approval-list clearfix">
					<c:forEach items="${entity.gjtTextbookPlanApprovals}" var="approval" varStatus="status">
						<dl class="approval-item">
							<dt class="clearfix">
								<b class="a-tit gray6">
									<c:choose>
										<c:when test="${approval.operaType == 1}">
											发布计划
										</c:when>
										<c:when test="${approval.operaType == 3}">
											重新发布计划
										</c:when>
										<c:otherwise>
											${approval.createdUser.priRoleInfo.roleName}审核
										</c:otherwise>
									</c:choose>
								</b>
								<c:choose>
					          		<c:when test="${approval.operaRole == 1}">
					          			<span class="gray9 text-no-bold f12 margin_l10">
					          				${approval.createdUser.priRoleInfo.roleName}：${approval.createdUser.realName}  <fmt:formatDate value='${approval.createdDt}' type='date' pattern='yyyy-MM-dd HH:mm'/> 发布
					          			</span>
					          			<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
					          		</c:when>
					          		<c:otherwise>
					          			<span class="gray9 text-no-bold f12 margin_l10">
					          				<fmt:formatDate value='${approval.createdDt}' type='date' pattern='yyyy-MM-dd HH:mm'/>
					          				<c:choose>
					          					<c:when test="${entity.status == 3 && status.last}">
					          						<span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
	                    							<label class="state-lb text-green">审核通过</label>
					          					</c:when>
					          					<c:otherwise>
					          						<span class="fa fa-fw fa-times-circle text-red"></span>
	  							                    <label class="state-lb text-red">审核不通过</label>
					          					</c:otherwise>
					          				</c:choose>
					          			</span>
					          		</c:otherwise>
					          	</c:choose>
							</dt>
							<c:if test="${approval.operaRole == 2}">
								<dd>
				                    <div class="txt">
				                        <p>${approval.description}</p>
				                        <div class="gray9 text-right">审批人：${approval.createdUser.realName}</div>
				                        <i class="arrow-top"></i>
				                    </div>
				                </dd>
							</c:if>
						</dl>
					</c:forEach>
					
					<c:if test="${entity.status == 1 && param.isApproval}">
						<dl class="approval-item white-border">
		                  <dt class="clearfix">
		                    <b class="a-tit gray6">审批人审核</b>
		                    <span class="fa fa-fw fa-dot-circle-o text-yellow"></span>
		                    <label class="state-lb pending-state">待审核</label>
		                  </dt>
		                  <dd>
		                    <form id="approvalForm" class="theform" action="${ctx}/textbookPlan/approval" method="post">
				        	  <input type="hidden" id="planId" name="id" value="${entity.planId}">
				        	  <input type="hidden" id="operaType" name="operaType" value="">
		                      <div class="col-xs-12 no-padding position-relative">
		                        <textarea id="description" name="description" class="form-control" rows="3" placeholder="请输入审批评语或重交、不通过的原因和指引" datatype="*" nullmsg="请输入内容！" errormsg="请输入内容！"></textarea>
		                      </div>
		                      <div>
		                        <button type="button" class="btn min-width-90px btn-success margin_r10 margin_t10" data-role="btn-pass">通过</button>
		                        <button type="button" class="btn min-width-90px btn-warning margin_r10 margin_t10" data-role="btn-not-pass">不通过</button>
		                      </div>
		                    </form>
		                  </dd>
		              </dl>
					</c:if>
	            </div>
			</c:if>
		</div>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">
	$(function() {
		$(".select2").select2({language: "zh-CN"});
		if($('#action').val() == 'update'){
		$('select').attr("disabled","disabled");
		}
		if($('#action').val() == 'view'){
			$(':input').attr("disabled","disabled");
			$('[data-role="back-off"]').removeAttr("disabled");
			$('[data-role="cancel"]').removeAttr("disabled");
			$('[data-role="save1"]').remove();  

			$('[data-role="btn-pass"]').removeAttr("disabled");
			$('[data-role="btn-not-pass"]').removeAttr("disabled");
			$('#planId').removeAttr("disabled");
			$('#operaType').removeAttr("disabled");
			$('#description').removeAttr("disabled");
		}
		
		$("[data-role='btn-pass']").click(function() {
			if ($("#description").val() == "") {
				alert("请输入审批评语！");
				return false;
			}
			$("#operaType").val(1);
			$("#approvalForm").submit();
		})
		
		$("[data-role='btn-not-pass']").click(function() {
			if ($("#description").val() == "") {
				alert("请输入审批评语！");
				return false;
			}
			$("#operaType").val(2);
			$("#approvalForm").submit();
		})
	});
	
	;(function(){
		var $inputForm=$("#inputForm");

		var htmlTemp='<div class="tooltip top" role="tooltip" style="white-space: nowrap;">'
              +'<div class="tooltip-arrow"></div>'
              +'<div class="tooltip-inner"></div>'
              +'</div>';
		$inputForm.find(":input[datatype]").each(function(index, el) {
			$(this).after(htmlTemp);
		});

		$.Tipmsg.r='';
		var postIngIframe;
		var postForm=$inputForm.Validform({
		  //showAllError:true,
		  ajaxPost:true,
		  datatype:{
		  	'dFrom':function(gets,obj,curform,regxp){
		  		var reg=/^\d+$/;
		  		if(reg.test(gets)){
		  			var $relGroup=obj.closest('.input-group-custom');
		  			var $dEnd=$relGroup.find('[datatype="dEnd"]');
		  			if( parseInt(obj.val()) > parseInt($dEnd.val()) ){
		  				return '“开始天数”必须小于“结束天数”';
		  			}
		  		}
		  		else{
		  			return '时间格式不对';
		  		}

		  		return true;
		  	},
		  	'dEnd':function(gets,obj,curform,regxp){
		  		var reg=/^\d+$/;
		  		if(reg.test(gets)){
		  			var $relGroup=obj.closest('.input-group-custom');
		  			var $dFrom=$relGroup.find('[datatype="dFrom"]');
		  			if( parseInt(obj.val()) < parseInt($dFrom.val()) ){
		  				return '“结束天数”必须大于“开始天数”';
		  			}
		  		}
		  		else{
		  			return '时间格式不对';
		  		}

		  		return true;
		  	}
		  },
		  tiptype:function(msg,o,cssctl){
		    //msg：提示信息;
		    //o:{obj:*,type:*,curform:*},
		    //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
		    //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, 
		    //curform为当前form对象;
		    //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
		    if(!o.obj.is("form")){
			    var msgBox=o.obj.siblings('.tooltip');
			    if(msgBox.length<=0){
			    	var $t=$(htmlTemp);
			    	o.obj.after($t);
			    	msgBox=$t;
			    }

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
			        bottom:28
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
			 var validator=true;     
	         /* if($("#gradeId").val()!==" "){
	             $.ajax({  
	                 type : "post",  
	                 url : "${ctx}/textbookPlan/checkGradeId",  
	                 dataType:'json',
	                 data : {gradeId:$('#gradeId').val()},  
	                 async : false,  
	                 success : function(data){  
	                     if(data.successful){
	                         validator=false;
	                     }  
	                 }
	             });
	         }
	    	 var $gradeDiv=$("#gradeDiv");
	         if (!validator) {
	        	 $gradeDiv.find('.tooltip').addClass('in').css('bottom', '100%').find('.tooltip-inner').text('所属学期已存在');
	        	 $gradeDiv.focus();
	        	 return false;
	         } else {
	        	 $gradeDiv.find('.tooltip').removeClass('in');
	         } */
	         
	         if($("#planCode").val()!==" "){
	             $.ajax({  
	                 type : "post",  
	                 url : "${ctx}/textbookPlan/checkCode",  
	                 dataType:'json',
	                 data : {planCode:$('#planCode').val()},  
	                 async : false,  
	                 success : function(data){  
	                     if(data.successful){
	                         validator=false;
	                     }  
	                 }
	             });
	         }
	    	 var $planCodeDiv=$("#planCodeDiv");
	         if (!validator) {
	        	 $planCodeDiv.find('.tooltip').addClass('in').css('bottom', '100%').find('.tooltip-inner').text('计划编号已存在');
	        	 $("#planCode").focus();
	        	 return false;
	         } else {
	        	 $planCodeDiv.find('.tooltip').removeClass('in');
	         }
			  
		    postIngIframe=$.formOperTipsDialog({
	    		text:'数据提交中...',
	    		iconClass:'fa-refresh fa-spin'
	    	});
		  },
		  callback:function(data){
		    //这里执行回调操作;
		    //如果callback里明确return alse，则表单不会提交，如果return true或没有return，则会提交表单。

		    if (data.successful) {
		    	window.location.href = ctx + "/textbookPlan/list";
		    } else {
		    	$.closeDialog(postIngIframe);
		    	alert(data.message);
		    }
			
		  }
		});

	})();

	;(function(){
		/*日期控件*/
		$('[data-role="daterangetime-group"]').each(function(i,e){
			var startDate=$('[data-role="date-start"]',e);
			var endDate=$('[data-role="date-end"]',e);
			//开始时间			
			startDate.datepicker({
			  language:'zh-CN',
			  format:'yyyy-mm-dd',
			  todayHighlight:true,
			  //orientation:'top'		  
			  //autoclose:true
			}).on('changeDate', function(e) {
				var add=increaseOnedate(e.target.value);
				endDate.datepicker('setStartDate',add);
			});
			//结束时间
			endDate.datepicker({
			  language:'zh-CN',
			  format:'yyyy-mm-dd',
			  todayHighlight:true,
			  //orientation:'top'		  
			  //autoclose:true
			}).on('changeDate', function(e) {
				var d=decreaseOnedate(e.target.value);
				startDate.datepicker('setEndDate',d);
			}).on('focus',function(){
				if(this.value==""&&startDate.val()==""){
					startDate.focus();
					endDate.datepicker('hide');
				}
			});
		});
	})();
</script>
</body>
</html>
