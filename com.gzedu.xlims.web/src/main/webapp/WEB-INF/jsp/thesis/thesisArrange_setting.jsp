<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
	/* var thesisPlanId = '${thesisPlan.thesisPlanId}';
	if (!thesisPlanId) {
		alert("当前没有可设置的毕业论文安排！");
		history.back();
	} */
</script>
	
</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">论文管理</a></li>
		<li><a href="#">论文安排</a></li>
		<li class="active">设置毕业论文安排</li>
	</ol>
</section>
<section class="content">
	<div class="box margin-bottom-none">
		<form id="theform" class="theform" role="form" action="${ctx}/thesisArrange/create" method="post">
			<input type="hidden" name="specialtyBaseIds" value=""/>
			<input type="hidden" name="canApplyNums" value=""/>
			<div class="box-body">
				<div class="form-horizontal">
					<div class="row">
						<div class="col-sm-4 col-xs-6">
				          <div class="form-group">
				            <label class="control-label col-sm-3 text-nowrap">论文计划</label>
				            <div class="col-sm-9">
				              <select id="thesisPlanId" name="thesisPlanId" class="selectpicker show-tick form-control" data-size="8">
								  <c:forEach items="${list }" var="thesisPlan">
								  	<option value="${thesisPlan.thesisPlanId}" <c:if test="${thesisPlanId eq thesisPlan.thesisPlanId }">selected</c:if>>${thesisPlan.thesisPlanName}</option>
								  </c:forEach>
							  </select>
				            </div>
				          </div>
				        </div>
					</div>
				</div>
				<p><b>可申请毕业论文的专业</b></p>
	
				<table class="table table-bordered vertical-mid text-center table-font">
					<thead class="with-bg-gray">
		              <tr>
		              	<th>
		              		<input type="checkbox" class="select-all no-margin">
		              	</th>
		                <th>专业代码</th>
		                <th>专业名称</th>
		                <th>层次</th>
		                <th>可申请人数</th>
		              </tr>
		            </thead>
		            <tbody>
						<c:forEach var="specialty" items="${specialtyList}">
			            	<tr>
			            		<td>
			            			<input type="checkbox" name="ids" value="${specialty.specialtyBaseId}">
			            		</td>
			            		<td>${specialty.specialtyCode}</td>
			            		<td>${specialty.specialtyName}</td>
			            		<td>${specialty.trainingLevel}</td>
			            		<td><span class="canApplyNum">${specialty.canApplyNum}</span></td>
			            	</tr>
			            </c:forEach>
		            </tbody>
				</table>
	
			</div>
			<div class="box-footer">
			    <div class="pull-right"><button type="submit" class="btn min-width-90px btn-primary" data-role="sure">确认安排</button></div>
			    <div class="pull-right margin_r15"><button type="button" class="btn min-width-90px btn-default" onclick="history.back()">取消</button></div>
			</div>
		</form>
	</div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

<script type="text/javascript">

$('#thesisPlanId').change(function(){
	location.href="${ctx}/thesisArrange/create?thesisPlanId="+$(this).val();
});

$(".table").on('click', 'tbody tr', function (event) { 
  var $tbl=$(this).closest(".table");
  var ck=$(this).find(":checkbox");
  $(this).toggleClass('selected');
  if(!$(event.target).is(":checkbox")){
    if(!ck.is(":checked")){
      ck.prop("checked",true);
    }
    else{
      ck.prop("checked",false); 
    }
  }
  if(!ck.is(":checked")){
    $tbl.find(".select-all").prop("checked",false);
  }
})
.on("click",".select-all",function(){
  var $tbl=$(this).closest(".table");
  if($(this).is(":checked")){
    $tbl.find('tr').addClass("selected");
    $tbl.find(':checkbox').prop("checked",true);
  }
  else{
    $tbl.find('tr').removeClass("selected");
    $tbl.find(':checkbox').prop("checked",false);
  }
});

//表单验证
;(function(){
	var $theform=$(".theform");

	var htmlTemp='<div class="tooltip top" role="tooltip">'
	      +'<div class="tooltip-arrow"></div>'
	      +'<div class="tooltip-inner"></div>'
	      +'</div>';
	$theform.find(":input[datatype]").each(function(index, el) {
		$(this).after(htmlTemp);
	});

	$.Tipmsg.r='';
	var postForm=$theform.Validform({
	  showAllError:false,
	  ajaxPost:true,
	  tiptype:function(msg,o,cssctl){
	    if(!o.obj.is("form")){
	    	//msg：提示信息;
		    //o:{obj:*,type:*,curform:*},
		    //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
		    //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, 
		    //curform为当前form对象;
		    //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;

		    var msgBox=o.obj.closest('.position-relative').find('.tooltip');

		    msgBox.css({
		        bottom:"100%",
		        'margin-bottom':-5
		    })
		    msgBox.children('.tooltip-inner').text(msg);

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
		  
		if($(':input[name="ids"]:checked').length==0) {
            alert('请选择专业');
            return false;
        }
        var ids = '';
        var nums = '';
        $(':input[name="ids"]:checked').each(function(i, v) {
        	ids = ids + v.value + ',';
        	nums = nums + $(this).closest("tr").find(".canApplyNum").text() + ",";
        });
        $(':input[name="specialtyBaseIds"]').val(ids.substring(0, ids.length - 1));  
        $(':input[name="canApplyNums"]').val(nums.substring(0, nums.length - 1));  
		
	    window.postIngIframe=$.formOperTipsDialog({
			text:'数据提交中...',
			iconClass:'fa-refresh fa-spin'
		});
	  },
	  callback:function(data){
		  if (data.successful) {
			  window.location.href = "${ctx}/thesisArrange/list";
		  } else {
			  alert(data.message);
		  }
	  }
	});

})();

</script>
</body>
</html>
