<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<title>管理系统 - 发布新活动</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body overlay-wrapper">
	<section class="content-header clearfix">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="homepage.html"><i class="fa fa-home"></i> 首页</a>
			</li>
			<li>
				<a href="${ctx }/admin/messageInfo/list">短信通知</a>
			</li>
			<li class="active">新增短信通知</li>
		</ol>
	</section>
	<section class="content">
		<form id="theform" class="form-horizontal" role="form" action="${ctx }/admin/mobileMessage/${action}" method="post">
			<input id="action" type="hidden" name="action" value="${action }">
			 <input type="hidden" name="id" value="${info.id }">
			<input type="hidden" name="newMessageId" id="newMessageId" value="${newMessageId }">
			<div class="box no-margin school-set-box">
				<div class="box-body">
					<div class="form-horizontal reset-form-horizontal margin_t10">
						<div class="form-group">
							<label class="col-sm-2 control-label"><small class="text-red">*</small>类型</label>
							<div class="col-sm-4">
								<div class="position-relative">
									<select class="form-control select2" data-id="sms-type" name="type" datatype="*" nullmsg="请选择通知类型" errormsg="请选择类型" data-size="8" data-live-search="true">
										<option value="">请选择</option>
										<%-- <c:forEach items="${infoTypeMap}" var="map">
											<option value="${map.key}" <c:if test="${infoTypeId == map.key}">selected='selected'</c:if>>${map.value}</option>
										</c:forEach> --%>
										
										<option data-val="0" value="1" <c:if test="${info.type eq '1'}">selected='selected'</c:if>>普通短信</option>
										<%-- <option data-val="1" value="2" <c:if test="${info.type eq '2'}">selected='selected'</c:if>>模板短信</option> --%>
										
									</select>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label"><small class="text-red">*</small>签名</label>
							<div class="col-sm-4">
								<div class="position-relative">
									<select class="form-control select2" name="signature" datatype="*" nullmsg="请选择签名" errormsg="请选择签名">
										<option value="">请选择签名</option>
										<option value="1" <c:if test="${info.signature eq '1'}">selected='selected'</c:if>>国开在线</option>
									</select>
								</div>
							</div>
						</div>

						<div>
							<div data-role="tab-cnt">
								<div class="form-group">
									<label class="col-sm-2 control-label no-pad-top"><small class="text-red">*</small>内容</label>
									<div class="col-sm-9">
										<div class="position-relative">
											<textarea class="form-control" name="content" data-rel="tab-cnt" datatype="vNoEmpty" nullmsg="请填写短信内容" errormsg="请填写短信内容" rows="8" data-id="sms-cn">
												
											</textarea>
										</div>
										<div class="gray9 f12 margin_t5">
											短信字数：
											<span class="text-orange" data-id="char-number">0</span>
											个字，条数：
											<span class="text-orange" data-id="sms-number">0</span>
											条（每条短信含签名限制70个字，超过自动拆分成多条发送）
										</div>
									</div>
								</div>
								
								<c:if test="${action eq 'create' }">
								<div class="form-group">
									<label class="col-sm-2 control-label"><small class="text-red">*</small>接收</label>
									<div class="col-sm-9">
										<div class="box-border">
											<input type="hidden" name="isAppoint" value="0">
											<ul class="nav nav-tabs pad-l15 pad-r15">
												<li class="active">
													<a href="#tab_top_1_1" data-toggle="tab" id="condition_yes">按条件筛选</a>
												</li>
												<li>
													<a href="#tab_top_1_2" data-toggle="tab" id="condition_no">指定收件人</a>
												</li>
											</ul>
											<div class="tab-content">
												<div class="tab-pane active" id="tab_top_1_1">
													<div class="pad15">
														<table class="table-panel border-bottom-none vertical-middle">
															<colgroup>
																<col width="100" />
																<col />
															</colgroup>
															<tbody>
																<tr>
																	<td>学期：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default active" data-id="sel-all">
																				<input type="checkbox" checked value="1" name="gradeIdAll"> 全部
																			</span>
																			<c:forEach items="${gradeMap }" var="map">
																				<span class="btn">
																					<input type="checkbox" name="gradeIds" value="${map.key }_${map.value }" data-id="${map.key }"> ${map.value }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>层次：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default active" data-id="sel-all">
																				<input type="checkbox" checked value="1" name="pyccIdAll"> 全部
																			</span>
																			<c:forEach items="${pyccMap }" var="map">
																				<span class="btn">
																					<input type="checkbox" name="pyccIds" value="${map.key }_${map.value }" data-id="${map.key }"> ${map.value }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>专业：</td>
																	<td>
																		<div class="item-sel-box clearfix" data-role="no-toggle">
																			<span class="btn btn-default active" data-id="sel-all">
																				<input type="checkbox" checked name="specialtyIdAll" value="1"> 全部
																			</span>
																			<button class="btn btn-default" type="button" data-role="add-major">
																				<i class="fa fa-fw fa-plus"></i>
																			</button>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>课程：</td>
																	<td>
																		<div class="item-sel-box clearfix" data-role="no-toggle">
																			<span class="btn btn-default active" data-id="sel-all">
																				<input type="checkbox" checked value="1" name="courseIdAll"> 全部
																			</span>
																			<button class="btn btn-default" type="button" data-role="add-course">
																				<i class="fa fa-fw fa-plus"></i>
																			</button>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>学生类型：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default active" data-id="sel-all">
																				<input type="checkbox" checked value="1" name="userTypeAll"> 全部
																			</span>
																			<c:forEach items="${userTypeMap }" var="map">
																				<span class="btn">
																					<input type="checkbox" name="userTypes" value="${map.key }_${map.value }" data-id="${map.key }"> ${map.value }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td>学籍状态：</td>
																	<td>
																		<div class="item-sel-box clearfix">
																			<span class="btn btn-default active" data-id="sel-all">
																				<input type="checkbox" checked value="1" name="xjztTypeAll"> 全部
																			</span>
																			<c:forEach items="${xjztMap }" var="map">
																				<span class="btn">
																					<input type="checkbox" name="xjztTypes" value="${map.key }_${map.value }" data-id="${map.key }"> ${map.value }
																				</span>
																			</c:forEach>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td colspan="2" class="">
																		<div class="text-center">
																			根据以上条件共筛选出 <u class="text-blue" role="button" data-role="view-sel-stu1"> -- </u> 名学生作为收件人
																			<button type="button" class="btn min-width-90px btn-default margin_r5" data-role="seachCount">查询人数</button>
																		</div>
																	</td>
																</tr>
																<tr>
																	<td class="no-padding"></td>
																	<td class="no-padding"></td>
																</tr>
															</tbody>
														</table>
													</div>
												</div>
												<div class="tab-pane" id="tab_top_1_2">
													<div class="pad15 border-bottom">
														<button type="button" class="btn min-width-90px btn-default margin_r5" data-role="import">批量导入学员</button>
														<button type="button" class="btn min-width-90px btn-default" data-role="sel-online">在线选择学员</button>
													</div>
													<div class="text-center pad15">
														根据以上条件共筛选出 <u class="text-blue" role="button" data-role="view-sel-stu"> --</u> 名学生作为收件人
														<button type="button" class="btn min-width-90px btn-default margin_r5" data-role="seachImportCount">查询人数</button>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								</c:if>
							</div>

							<div data-role="tab-cnt" style="display: none">
								<div class="form-group">
									<label class="col-sm-2 control-label"><small class="text-red">*</small>模板</label>
									<div class="col-sm-4">
										<div class="position-relative">
											<select class="form-control" datatype="*" nullmsg="请选择模板" errormsg="请选择模板">
												<option value="">请选择模板</option>
												<option value="1">自定义模板</option>
												<option value="2">续费催缴</option>
												<option value="3">网考通知</option>
											</select>
										</div>
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-9 col-sm-offset-2">
										<div class="position-relative">
											<textarea class="form-control" data-rel="tab-cnt" datatype="vNoEmpty" nullmsg="请填写模板内容" errormsg="请填写模板内容" rows="8">亲爱的{1}同学您好，恭喜您成功报读龙岗社区学院{2}专业中专学历，请登录{3}进行学习，您的学习账号：{4}，初始密码：{5}，如有任何疑问请拨打咨询电话4000969300，祝您学习愉快！</textarea>
										</div>
										<div class="gray9 f12 margin_t5">每条短信含签名限制70个字，超过自动拆分成多条发送</div>
									</div>
								</div>
								<div class="row">
									<label class="col-sm-2 control-label"><small class="text-red">*</small>变量</label>
									<div class="col-sm-9">
										<div>
											<button type="button" class="btn btn-sm btn-default margin_r5" data-role='import' style="width: 120px;">从EXCEL导入</button>
											<span class="inline-block f12 gray9"> 导入变量内容（xls格式，除姓名、学号外，其他列为变量值，Excel内容请尽量使用文本格式） </span>
										</div>
										<div class="margin_t15 table-responsive">
											<table class="table table-bordered text-center table-font margin_b10">
												<thead class="with-bg-gray">
													<tr>
														<td>姓名</td>
														<td>学号</td>
														<td>{1}</td>
														<td>{2}</td>
														<td>{3}</td>
														<td>{4}</td>
														<td>{5}</td>
														<td>操作</td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<td><a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除"><i class="fa fa-trash-o text-red"></i></a></td>
													</tr>
												</tbody>
											</table>
											<div class="page-container clearfix">
												<div class="pageing-info">
													<div class="dataTables_info">
														共 2 页，到第 <input type="text" class="form-control jump-page-input" value="1"> 页
														<button class="btn btn-block btn-default sure-btn">确定</button>
													</div>
												</div>
												<div class="pageing-list">
													<div class="dataTables_paginate paging_simple_numbers">
														<ul class="pagination">
															<li class="paginate_button previous disabled">
																<a href="#">上一页</a>
															</li>
															<li class="paginate_button active">
																<a href="#">1</a>
															</li>
															<li class="paginate_button">
																<a href="#">2</a>
															</li>
															<li class="paginate_button">
																<a href="#">3</a>
															</li>
															<li class="paginate_button">
																<a href="#">4</a>
															</li>
															<li class="paginate_button disabled">
																<a href="#">…</a>
															</li>
															<li class="paginate_button">
																<a href="#">5</a>
															</li>
															<li class="paginate_button next">
																<a href="#">下一页</a>
															</li>
														</ul>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="box-footer text-center pad20">
					<div>
						<button type="submit" id="btnSubmit" class="btn btn-success min-width-90px margin_r15" data-role="submite">
						<c:if test="${action eq 'create' }">
						发布
						</c:if>
						<c:if test="${action eq 'update' }">
							修改
						</c:if>
						</button>
						<button type="button" class="btn btn-default min-width-90px" data-role="cancel" onclick="history.back()">取消</button>
					</div>
				</div>
				
			</div>
		</form>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
	var newMessageId=$('#newMessageId').val();
$(function() {
		$(".select2").select2({language: "zh-CN"});
		
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
		      datatype:{
		        	"vNoEmpty":function(gets,obj,curform,regxp){
		        		var $rel=obj.closest('[data-role="'+obj.data('rel')+'"]');

		        		if(regxp['*'].test(gets) && $rel.is(':visible')){return true;}

		        		return false;
		        	}
		        },
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
							window.location.href='${ctx}/admin/mobileMessage/list';
						}
				},2000); 
			}
	    });
		    
		
		//查询导入和在线选择人数
		$('[data-role="seachImportCount"]').click(function(){
			 $.get('${ctx}/admin/messageInfo/getImportStudentCount',{messageId:newMessageId},function(data){
					$('[data-role="view-sel-stu"]').text(data.obj);
				},'json'); 
		});
		
		//条件查询人数
		$('[data-role="seachCount"]').click(function(){
			var gradeIds="";
			var gradeIdAll=$('[name="gradeIdAll"]:checked').val();
			$('[name="gradeIds"]:checked').each(function(i){
				gradeIds+=$(this).data('id')+",";
			}); 
			
			var pyccIds="";
			var pyccIdAll=$('[name="pyccIdAll"]:checked').val();
			$('[name="pyccIds"]:checked').each(function(i){
				pyccIds+=$(this).data('id')+",";
			}); 
			
			var specialtyIds="";
			var specialtyIdAll=$('[name="specialtyIdAll"]:checked').val();
			$('[name="specialtyIds"]:checked').each(function(i){
				specialtyIds+=$(this).data('id')+",";
			}); 
			
			var courseIds="";
			var courseIdAll=$('[name="courseIdAll"]:checked').val();
			$('[name="courseIds"]:checked').each(function(i){
				courseIds+=$(this).data('id')+",";
			}); 
			
			var userTypes="";
			var userTypeAll=$('[name="userTypeAll"]:checked').val();
			$('[name="userTypes"]:checked').each(function(i){
				userTypes+=$(this).data('id')+",";
			}); 
			
			var xjztTypes="";
			var xjztTypeAll=$('[name="xjztTypeAll"]:checked').val();
			$('[name="xjztTypes"]:checked').each(function(i){
				xjztTypes+=$(this).data('id')+",";
			}); 
			
			$('#overlay').show();
			 $.post('${ctx}/admin/messageInfo/getStudentCount',{
					gradeIds:gradeIds,
					gradeIdAll:gradeIdAll,
					pyccIds:pyccIds,
					pyccIdAll:pyccIdAll,
					specialtyIds:specialtyIds,
					specialtyIdAll:specialtyIdAll,
					courseIds:courseIds,
					courseIdAll:courseIdAll,
					userTypes:userTypes,
					userTypeAll:userTypeAll,
					xjztTypes:xjztTypes,
					xjztTypeAll:xjztTypeAll			
				},function(data){
					$('#overlay').hide();
					$('[data-role="view-sel-stu1"]').text(data.obj);
				},'json'); 
				
			});
	});
	
	

	//选项操作
	$(document).on('click','.item-sel-box .btn:not([data-role])',function(e){
		//如果点击的是删除操作，那么就不执行下面操作
		if(e.target.nodeName=='I'){
			return;
		} 
		
		if( $(this).hasClass('active') ){
			$(this).removeClass('active').children(':checkbox').prop('checked',false);
		}
		else{
			$(this).addClass('active').children(':checkbox').prop('checked',true);
		}
		var $selItems=$(this).siblings('span.btn');
		//如果点击的是“全选”
		if( $(this).attr('data-id') ){
			if($(this).hasClass('active')){
				$selItems.addClass('active').children(':checkbox').prop('checked',true);
			}else{
				$selItems.removeClass('active').children(':checkbox').prop('checked',false);
			}
		}else{//点击单一选项
			/* if($(this).hasClass('active')){ */
				$(this).siblings('[data-id="sel-all"]').removeClass('active').children(':checkbox').prop('checked',false);
			/* } */
		}
	}).on('click', '[data-role="remove-sel"]', function(event) {//删除 专业 / 课程 
		var $p=$(this).parent();
		var $brothers=$p.siblings('span.btn.active');
		$p.remove();
	}).on('click', '[data-role="add-major"]', function(event) {//添加专业
		$.mydialog({
		    id:'export',
		    width:850,
		    height:550,
		    zIndex:11000,
		    content: 'url:${ctx}/admin/messageInfo/findSpecialty'
		});
	}).on('click', '[data-role="add-course"]', function(event) {//添加课程
		$.mydialog({
		    id:'export',
		    width:850,
		    height:550,
		    zIndex:11000,
		    content: 'url:${ctx}/admin/messageInfo/findCourse'
		});
	}).on('click', '[data-role="import"]', function(event) {//批量导入学员
	  event.preventDefault();
	  $.mydialog({
	    id:'import',
	    width:600,
	    height:415,
	    zIndex:11000,
	    content: 'url:${ctx}/admin/messageInfo/importStudent?newMessageId='+newMessageId
	  });
	}).on('click', '[data-role="sel-online"]', function(event) {//在线选择学员
	  $.mydialog({
	    id:'sel-online-student',
	    width:800,
	    height:600,
	    zIndex:11000,
	    content: 'url:${ctx}/admin/messageInfo/searchOlineStudent?newMessageId='+newMessageId
	  });
	}).on('click', '[data-role="view-sel-stu1"]', function(event) {//查看根据条件选择的学员
		var gradeIds="";
		var gradeIdAll=$('[name="gradeIdAll"]:checked').val();
		$('[name="gradeIds"]:checked').each(function(i){
			gradeIds+=$(this).data('id')+",";
		}); 
		
		var pyccIds="";
		var pyccIdAll=$('[name="pyccIdAll"]:checked').val();
		$('[name="pyccIds"]:checked').each(function(i){
			pyccIds+=$(this).data('id')+",";
		}); 
		
		var specialtyIds="";
		var specialtyIdAll=$('[name="specialtyIdAll"]:checked').val();
		$('[name="specialtyIds"]:checked').each(function(i){
			specialtyIds+=$(this).data('id')+",";
		}); 
		
		var courseIds="";
		var courseIdAll=$('[name="courseIdAll"]:checked').val();
		$('[name="courseIds"]:checked').each(function(i){
			courseIds+=$(this).data('id')+",";
		}); 
		
		var userTypes="";
		var userTypeAll=$('[name="userTypeAll"]:checked').val();
		$('[name="userTypes"]:checked').each(function(i){
			userTypes+=$(this).data('id')+",";
		}); 
		
		var xjztTypes="";
		var xjztTypeAll=$('[name="xjztTypeAll"]:checked').val();
		$('[name="xjztTypes"]:checked').each(function(i){
			xjztTypes+=$(this).data('id')+",";
		}); 
		
	  $.mydialog({
	    id:'view-student1',
	    width:800,
	    height:750,
	    zIndex:11000,
	    urlData:{
	    	gradeIds:gradeIds,
			gradeIdAll:gradeIdAll,
			pyccIds:pyccIds,
			pyccIdAll:pyccIdAll,
			specialtyIds:specialtyIds,
			specialtyIdAll:specialtyIdAll,
			courseIds:courseIds,
			courseIdAll:courseIdAll,
			userTypes:userTypes,
			userTypeAll:userTypeAll,
			xjztTypes:xjztTypes,
			xjztTypeAll:xjztTypeAll	
	    },  //frameElement.data.gradeIds
	    content: 'url: ${ctx}/admin/messageInfo/searchStudent?isTranse=Y'
	  });
	}).on('click', '[data-role="view-sel-stu"]', function(event) {//查看已导入的学员
	  $.mydialog({
	    id:'view-student',
	    width:800,
	    height:600,
	    zIndex:11000,
	    content: 'url:${ctx}/admin/messageInfo/searchImportStudent?newMessageId='+newMessageId
	  });
	}).on('change keydown keyup','[data-id="sms-cn"]', function(event) {//限制输入字数
		var $smsNum=$('[data-id="sms-number"]'),
			$charNum=$('[data-id="char-number"]'),
	    	val = $(this).val(),
	    	sLen=val.length,
	    	reg=/[\n\t\r]/gm,
	    	count=0;//统计条数
	    val=val.replace(reg,'');//删除换行符
	
	    if(sLen>0){
	    	count=1;
	    	if(sLen>64){
		    	var dif=sLen-64;
		    	if(dif%67==0){
		    		count+=dif/67;
		    	}
		    	else{
		    		count+=parseInt(dif/67)+1;
		    	}
		    }
	    }
	    $smsNum.text(count);
	    $charNum.text(sLen);
	    this.value=val;
	}).on('change', '[data-id="sms-type"]', function(event) {//短信类型
		var selectedItem=this.options[this.selectedIndex];
		$('[data-role="tab-cnt"]').eq(selectedItem.getAttribute('data-val')).show().siblings().hide();
	}).on('change', '[data-role="import"]', function(event) {
		$.mydialog({
		  id:'import',
		  width:600,
		  height:415,
		  zIndex:11000,
		  content: 'url:批量导入督导教师.html'
		});
	});
	
	$('[data-id="sms-cn"]').val('${info.content}');
	
</script>

	<!-- 加载的时候转圈圈 -->
	<div class="overlay" style="display: none; position: fixed;" id="overlay">
		<i class="fa fa-refresh fa-spin"></i>
	</div>
</body>
</html>