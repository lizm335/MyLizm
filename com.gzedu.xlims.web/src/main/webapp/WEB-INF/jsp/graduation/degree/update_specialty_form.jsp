<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>专业规则</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">
	<section class="content-header">
		<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
		<ol class="breadcrumb">
			<li>
				<a href="#">
					<i class="fa fa-home"></i> 首页
				</a>
			</li>
			<li>
				<a href="#">毕业管理</a>
			</li>
			<li>
				<a href="#">学位设置</a>
			</li>
			<li class="active">新增专业</li>
		</ol>
	</section>
	<section class="content">
		<form id="theform">
			<div class="box margin-bottom-none">
				<div class="box-header with-border">
					<h3 class="box-title">新增专业</h3>
				</div>
				<div class="box-body">
					<table class="table-gray-th">
						<tr>
							<th width="15%" class="text-right">院校名称</th>
							<td width="35%">${college.collegeName}</td>

							<th width="15%" class="text-right">专业名称

								<button type="button" data-toggle="tooltip" title="添加专业" class="btn btn-default btn-xs pull-right bg-white margin_l10" data-role="add-major">
									<i class="fa fa-fw fa-plus"></i>
								</button>

							</th>
							<td width="35%">
								<div class="box-border position-relative" style="min-height: 34px;">
									<div class="clearfix pad5 no-pad-top row-offset-3px" data-id="major-box"></div>
									<div class="tooltip top" id="scpeicaltyTip" role="tooltip" style="bottom: 100%; margin-bottom: -5px;"><div class="tooltip-arrow"></div><div class="tooltip-inner">请添加专业</div></div>
								</div>
							</td>
						</tr>
						<tr>
							<th class="text-right">适应学生范围<br>（入学时间）
							</th>
							<td>
								<div class="position-relative">
									<div class="input-group">
										<select id="gradeId" class="form-control margin_r10" datatype="*" nullmsg="请选择入学学期" errormsg="请选择入学学期">
											<option value=''>请选择入学学期</option>
											<c:forEach items="${gradeMap}" var="map">
												<option value="${map.key}">${map.value}</option>
											</c:forEach>
										</select>
										<div class="input-group-addon no-border">后入学</div>
									</div>
								</div>
							</td>

							<th class="text-right">授予学士学位的名称</th>
							<td>
								<div class="position-relative">
									<input id="degreeName" class="form-control" type="text" placeholder="请填写学位名称" datatype="*" nullmsg="请填写学位名称" errormsg="请填写学位名称">
								</div>
							</td>
						</tr>
					</table>
					<div class="clearfix margin_b10 margin_t20 box-header with-border no-pad-left">
						<h3 class="cnt-box-title f16 text-bold">学位申请条件</h3>
						<span class="text-blue" role="button" data-toggle="tooltip" title="从其他专业中复制学位申请条件" data-role="copy"><i class="fa fa-fw fa-copy"></i></span>
					</div>
					<div data-id="conditions-box">
						<div class="box no-border no-shadow margin_b10 conditionsItem">
							<div class="panel panel-default flat margin-bottom-none">
								<div class="panel-heading clearfix">
									<button type="button" class="btn btn-box-tool pull-right" data-widget="remove">
										<i class="fa fa-times"></i>
									</button>
									<h3 class="panel-title text-bold f14 pad-t5 reqTitle">条件1</h3>
								</div>
								<table class="table">
									<tbody>
										<tr>
											<td width="70" class="pad-l15">
												<div class="pad-t5">类型：</div>
											</td>
											<td>
												<div class="position-relative">
													<select class="form-control reqType" datatype="*" nullmsg="请选择类型" errormsg="请选择类型">
														<option value=''>--请选择--</option>
														<c:forEach items="${baseTypeMap}" var="map">
															<option value="${map.key}">${map.value}</option>
														</c:forEach>
													</select>
												</div>
											</td>
										</tr>
										<tr class="paramTr">
											<td class="pad-l15">
												<div class="pad-t5">参数：</div>
											</td>
											<td>
												<div class="input-group">
													<div class="input-group-btn" style="vertical-align: top;">
														<select class="form-control input-group-select bg-white operator">
															<c:forEach items="${operatorEnum}" var="item">
																<option value="${item.value}">${item.name}</option>
															</c:forEach>
														</select>
													</div>
													<div class="position-relative">
														<input type="text" class="form-control reqParam" placeholder="请填写数值" datatype="/^(0|([1-9]\d*))(\.\d+)?$/" nullmsg="请选择参数" errormsg="请填写数值">
													</div>
													<div class="input-group-addon f12 no-border no-pad-right">
														<div class="text-left text-orange">
															注：该参数用于设定系统自动判定条件，<br>例如：成绩必须大于等于多少分
														</div>
													</div>
												</div>
											</td>
										</tr>
										<tr>
											<td class="pad-l15">
												<div class="pad-t5">描述：</div>
											</td>
											<td>
												<div class="position-relative">
													<textarea class="form-control reqDesc" rows="6" placeholder="请填写描述" datatype="*" nullmsg="请填写描述" errormsg="请填写描述"></textarea>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<a href="javascript:;" role="button" class="btn btn-block text-green nobg" data-role="add-conditions">
						<i class="fa fa-fw fa-plus"></i>新增学位申请条件
					</a>
				</div>
				<div class="box-footer text-right">
					<button type="button" class="btn btn-default min-width-90px margin_r10">取消</button>
					<button type="submit" class="btn btn-primary min-width-90px" data-role="sure">保存</button>
				</div>
			</div>
		</form>
	</section>
	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<!--“学位条件” 模板-->
<script type="text/template" id="conditions-temp">
  <div class="box no-border no-shadow margin_b10 conditionsItem">
    <div class="panel panel-default flat margin-bottom-none">
        <div class="panel-heading clearfix">
            <button type="button" class="btn btn-box-tool pull-right" data-widget="remove"><i class="fa fa-times"></i></button>
            <h3 class="panel-title text-bold f14 pad-t5 reqTitle"> 
              条件1
            </h3>
        </div>
        <table class="table">
          <tbody>
            <tr>
              <td width="70" class="pad-l15">
                <div class="pad-t5">
                  类型：
                </div>
              </td>
              <td>
                <div class="position-relative">
                  <select class="form-control reqType" datatype="*" nullmsg="请选择类型" errormsg="请选择类型">
                    <option value=''>--请选择--</option>
                    <c:forEach items="${baseTypeMap}" var="map">
						<option value="${map.key}">${map.value}</option>
					</c:forEach>
                  </select>
                </div>
              </td>
            </tr>
            <tr class="paramTr">
              <td class="pad-l15">
                <div class="pad-t5">
                  参数：
                </div>
              </td>
              <td>
                <div class="input-group">
                  <div class="input-group-btn" style="vertical-align: top;">
                   <select class="form-control input-group-select bg-white operator">
					  <c:forEach items="${operatorEnum}" var="item">
						 <option value="${item.value}">${item.name}</option>
					  </c:forEach>
				   </select>
                  </div>
                  <div class="position-relative">
                    <input type="text" class="form-control reqParam" placeholder="请填写数值" datatype="/^(0|([1-9]\d*))(\.\d+)?$/" nullmsg="请选择参数" errormsg="请填写数值">
                  </div>
                  <div class="input-group-addon f12 no-border no-pad-right">
                    <div class="text-left text-orange">
                      注：该参数用于设定系统自动判定条件，<br>例如：成绩必须大于等于多少分
                    </div>
                  </div>
                </div>
              </td>
            </tr>
            <tr>
              <td class="pad-l15">
                <div class="pad-t5">
                  描述：
                </div>
              </td>
              <td>
                <div class="position-relative">
                  <textarea class="form-control reqDesc" rows="6" placeholder="请填写描述" datatype="*" nullmsg="请填写描述" errormsg="请填写描述"></textarea>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
    </div>
  </div>
</script>
	<script type="text/javascript">
//选择专业
$('[data-role="add-major"]').click(function(event) {
  $.mydialog({
    id:'add-major',
    width:800,
    height:550,
    zIndex:9999,
    content: 'url:${ctx}/graduation/degreeCollege/chooseSpecialtyList'
  });
});

//选择专业
$('[data-role="copy"]').click(function(event) {
  $.mydialog({
    id:'copy',
    width:700,
    height:600,
    zIndex:9999,
    content: 'url:${ctx}/graduation/degreeCollege/copyDegree?collegeId=${param.collegeId}'
  });
});

$('[data-role="add-conditions"]').click(function(event) {
  event.preventDefault();
  $('[data-id="conditions-box"]').append( $('#conditions-temp').html());
  resetTab();
});


function resetTab(){
	$('.reqTitle').each(function(index,el){
		$(this).text('条件'+(index+1))
	});
    $('.reqType').each(function(){
		$(this).trigger('change');
	});
}

$('body').on('change','.reqType',function(){
    $tr=$(this).closest('tbody').find('.paramTr');
    if($(this).val()=='1'||$(this).val()=='2'||$.trim($(this).val())==''){
		$tr.show();
	}else{
	    $tr.hide();
	};
});

$('body').on('click','[data-widget="remove"]',function(){
    var $item=$(this).closest('.conditionsItem');
    $item.hide(500,function(){
		$item.remove();
		resetTab();
	})
});



	//表单验证
	;
	(function() {
	    var $theform = $("#theform");
	    var htmlTemp = '<div class="tooltip top" role="tooltip">' + '<div class="tooltip-arrow"></div>' + '<div class="tooltip-inner"></div>' + '</div>';
	    $theform.find(":input[datatype]").each(function(index, el) {
		$(this).closest('.position-relative').append(htmlTemp);
	    });

	    $.Tipmsg.r = '';
	    var postForm = $theform.Validform({
			ignoreHidden : true,
			showAllError : false,
			ajaxPost : true,
			tiptype : function(msg, o, cssctl) {
			    if (!o.obj.is("form")) {
				var msgBox = o.obj.closest('.position-relative').find('.tooltip');
	
				msgBox.css({
				    bottom : "100%",
				    'margin-bottom' : -5
				})
				msgBox.children('.tooltip-inner').text(msg);
	
				switch (o.type) {
				case 3:
				    msgBox.addClass('in');
				    break;
				default:
				    msgBox.removeClass('in');
				    break;
				}
			    }
			},
			beforeSubmit : function(curform) {
			    if ($('[data-id="major-box"] .sid').length == 0) {
					$('#scpeicaltyTip').addClass('in');
					scrollTo(0, 0);
					return false;
			    }
			    if ($('.conditionsItem').length == 0) {
					alert('请添加学位条件');
					return false;
			    }
			    var postIngIframe = $.formOperTipsDialog({
					text : '数据提交中...',
					iconClass : 'fa-refresh fa-spin'
			    });
			    var specialtyIds = [];
			    $('[data-id="major-box"] .sid').each(function() {
					specialtyIds.push($(this).val());
			    });
			    var requirements = [];
			    $('.conditionsItem').each(function() {
					var reqType = $(this).find('.reqType').val();
					var reqName = $(this).find('.reqType option:selected').text()
					var operator = $(this).find('.operator').val();
					var reqParam = $(this).find('.reqParam').val();
					var reqDesc = $(this).find('.reqDesc').val()
					var req = {
					    reqType : reqType,
					    reqName : reqName,
					    reqDesc : reqDesc
					}
					if (reqType == '1' || reqType == '2') {
					    req.operator = operator;
					    req.reqParam = reqParam;
					}
					requirements.push(JSON.stringify(req));
			    });
			    $.post('${ctx}/graduation/degreeCollege/createSpecialtyDegreeCollege', {
					collegeId : '${param.collegeId}',
					specialtyIds : specialtyIds,
					gradeId : $('#gradeId').val(),
					degreeName : $('#degreeName').val(),
					requirements : requirements
			    }, function(data) {
					if(data.successful){
					    postIngIframe.find('[data-role="tips-text"]').html('数据提交成功...');
						postIngIframe.find('[data-role="tips-icon"]').attr('class', 'fa fa-check-circle');
						location.href='${ctx}/graduation/degreeCollege/specialtyList';
					}else{
						alert(data.message);
					    $.closeDialog(postIngIframe);
					}
			    }, 'json');
			    return false;
			}
	    });
	})();
    </script>
</body>
</html>