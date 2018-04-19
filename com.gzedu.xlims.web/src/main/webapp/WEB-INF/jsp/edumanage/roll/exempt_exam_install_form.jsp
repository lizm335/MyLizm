<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统-免修免考</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body class="inner-page-body">
<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">学籍管理</a></li>
		<li><a href="#">免修免考</a></li>
		<c:if test="${action=='view'}">
			<li class="active">免修免考详情</li>
		</c:if>
		<c:if test="${action=='update'}">
			<li class="active">编辑免修免考</li>
		</c:if>
		
	</ol>
</section>
<section class="content">
	<form  class="theform" role="form" action="${ctx}/edumanage/exemptExamInstall/${action}" method="post">
	<input id="action" type="hidden" name="action" value="${action}">
	<input id="action" type="hidden" name="search_EQ_installId" value="${install.installId}">
		<div class="box no-margin">
			<div class="box-body pad20">
				<table class="table-gray-th margin_b15">
					<tbody>
						<tr>
							<th width="15%" class="text-right"><small class="text-red">*</small> 统考课程：</th>
							<td width="35%">
								<div class="position-relative">
	                                <select name="search_EQ_courseId" class="form-control select2 full-width" data-placeholder="请选择统考课程" datatype="*" nullmsg="请选择统考课程" errormsg="请选择统考课程" <c:if test="${action=='view'||action=='update'}">disabled='disabled'</c:if>>
	                                    <option vaule=""></option>
	                                    <c:forEach items="${courseMap}" var="map">
                                        	<option value="${map.key}"  <c:if test="${map.key==install.courseId}">selected='selected'</c:if>>${map.value}</option>
                                    	</c:forEach>
	                                </select>
	                            </div>
							</td>

							<th width="15%" class="text-right"><small class="text-red"></small> 适用学期：</th>
							<td width="35%">
								<div class="position-relative">
	                                <select name="gradeId" class="form-control select2 full-width" multiple="multiple" <c:if test="${action=='view'||action=='update'}">disabled='disabled'</c:if>>
	                                    <c:forEach items="${gradeMap}" var="map">
	                                     <c:forEach items="${resultMap}" var="grade">
                                        	<option value="${map.key}_${map.value}" <c:if test="${map.key==grade.key}">selected='selected'</c:if>>${map.value}</option>
                                    	 </c:forEach>
                                    	</c:forEach>
	                                </select>
	                            </div>
							</td>
						</tr>
					</tbody>
				</table>
				<table class="table table-bordered table-striped vertical-mid text-center f12 margin-bottom-none" data-role="set-model">
					<thead>
						<tr>
							<th >序号</th>
							<th >材料证明清单</th>
							<th >备注</th>
							<th>是否需要参加线上考试</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody data-role="box">
					<c:forEach items="${material}" var="material" varStatus="vs">
						<tr>
							<td>${vs.index+1}</td>
							<td>
								<div class="position-relative">
									<input name="materialName" type="text" value="${material.materialName}" class="form-control text-center input-xs" value="国家级考级证书" datatype="*" nullmsg="请填写材料证明" errormsg="请填写材料证明" placeholder="请填写材料证明" <c:if test="${action=='view'}">disabled='disabled'</c:if>>
					            </div>
							</td>
							<td>
								<div class="position-relative">
									<textarea name="memo" value="${material.memo}" class="form-control f12 pad5" rows="3" placeholder="请填写备注" datatype="*" nullmsg="请填写备注" errormsg="请填写备注" <c:if test="${action=='view'}">disabled='disabled'</c:if>>非计算机类专业,获得全国计算机等级考试一级B或以上级别证书</textarea>
					            </div>
							</td>
							<td>
								<div class="position-relative">
									<select name="isOnlineExam" class="form-control f12 input-xs" datatype="*" nullmsg="是否需要参加线上考试" errormsg="是否需要参加线上考试" <c:if test="${action=='view'}">disabled='disabled'</c:if>>
										<option value="" selected>请选择</option>
										<option <c:if test="${material.isOnlineExam==0}">selected='selected'</c:if>>不需要</option>
										<option <c:if test="${material.isOnlineExam==1}">selected='selected'</c:if>>需要</option>
									</select>
								</div>
							</td>
							<td>
							<c:if test="${action!='view'}">
								<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove-item" id="remove-item"><i class="fa fa-trash-o text-red"></i></a>
							</c:if>
							</td>
						</tr>
						<tr>
						</c:forEach>												
					</tbody>
				</table>
				<c:if test="${action=='update'}">
					<a href="javascript:;" role="button" class="btn btn-block text-green nobg" id="add-model" data-role="add-model"><i class="fa fa-fw fa-plus"></i>添加材料证明</a>
				</c:if>				
			</div>
			<div class="box-footer text-center">
				<c:if test="${action=='update'}">
					<button type="submit" id="release" class="btn btn-success min-width-90px margin_r15" >发布</button>
				</c:if>				
				<button type="button" class="btn btn-default min-width-90px" data-role="back-off">取消</button>
			</div>
		</div>
	</form>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<!--材料证明 模板-->
<script type="text/template" id="tpl">
	<tr>
		<td>{0}</td>
		<td>
			<div class="position-relative">
				<input name="materialName" type="text" class="form-control text-center input-xs" datatype="*" nullmsg="请填写材料证明" errormsg="请填写材料证明" placeholder="请填写材料证明">
				<div class="tooltip top" role="tooltip" style="z-index: 2; bottom: 100%; margin-bottom: -5px;"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>
	        </div>
		</td>
		<td>
			<div class="position-relative">
				<textarea name="memo" class="form-control f12 pad5" rows="3" placeholder="请填写备注" datatype="*" nullmsg="请填写备注" errormsg="请填写备注"></textarea>
				<div class="tooltip top" role="tooltip" style="z-index: 2; bottom: 100%; margin-bottom: -5px;"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>
	        </div>
		</td>
		<td>
			<div class="position-relative">
				<select name="isOnlineExam" class="form-control f12 input-xs" datatype="*" nullmsg="是否需要参加线上考试" errormsg="是否需要参加线上考试">
					<option value="">请选择</option>
					<option value="0">不需要</option>
					<option value="1">需要</option>
				</select>
				<div class="tooltip top" role="tooltip" style="z-index: 2; bottom: 100%; margin-bottom: -5px;"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>
			</div>
		</td>
		<td>
			<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove-item" id="remove-item"><i class="fa fa-trash-o text-red"></i></a>
		</td>
	</tr>
</script>
<script type="text/javascript">
//select2
$('.select2').select2();

//表单验证
;(function(){
    var $theform=$(".theform");

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
      //showAllError:true,
      //ignoreHidden:true,//是否忽略验证不可以见的表单元素
      ajaxPost:true,
      tiptype:function(msg,o,cssctl){
        //msg：提示信息;
        //o:{obj:*,type:*,curform:*},
        //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
        //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, 
        //curform为当前form对象;
        //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
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
      		postIngIframe=$.mydialog({
  			  id:'dialog-1',
  			  width:150,
  			  height:50,
  			  backdrop:false,
  			  fade:true,
  			  showCloseIco:false,
  			  zIndex:11000,
  			  content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
  			});		                 
          },
          callback:function(data){
        	  if(data.successful){
        		  postIngIframe.find(".text-center.pad-t15").html('数据提交成功...<i class="icon fa fa-check-circle"></i>');
                    /*关闭弹窗*/
                    setTimeout(function(){                    	
                      parent.$.closeDialog(postIngIframe);
                    },2000) 
                    location.href='${ctx}/edumanage/exemptExamInstall/list';
        	  }else{
        		  alert(data.message);
        		  $.closeDialog(postIngIframe); 
        	  }       	  
          }
    });
})();


$('[data-role="add-model"]').on('click', function(event) {
	event.preventDefault();
	var $box=$('[data-role="box"]');
	var tpl=$('#tpl').html();
	$box.append(tpl.format($box.children().length+1));
});
//删除材料证明清单
 $('body').on('click', '[data-role="remove-item"]', function(event) {
  event.preventDefault();
  $(this).closest('tr').remove();
});
</script>
</body>
</html>
