<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>选择学位专业列表</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>
<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">从其他专业中复制学位申请条件</h3>
	</div>
	<div class="scroll-box">
		<div class="box-body">
			<div class="box-border pad20">
				<select class="form-control center-block" id="specialty" style="width:300px;">
					<option>--请选择专业--</option>
					<c:forEach items="${specialtyColleges}" var="item">
						<option <c:if test="${item.id==param.collegeSpecialtyId}">selected="selected"</c:if>  value="${item.id}">${item.gjtSpecialty.specialtyName}</option>
					</c:forEach>
				</select>
		    </div>
	
			<div class="clearfix margin_b10 margin_t10 box-header with-border no-pad-left">
				<h3 class="cnt-box-title f16 text-bold">学位申请条件</h3>
			</div>
			<c:forEach items="${requirements}" var="item">
			<div class="panel panel-default flat">
                <div class="panel-heading">
                   	<h3 class="panel-title text-bold f14"> 
                      必修课程平均成绩
                  	</h3>
                </div>
                <table class="table reqItem" data-json='{
                	"reqType":"${item.requirementType}",
                	"operator":"${item.operator}",
                	"reqParam":"${item.requirementParam}",
                	"reqDesc":"${item.requirementDesc}"
                }'>
                	<tbody>
                		<tr>
                			<td width="70" class="pad-l15">类型：</td>
                			<td>${typeMap[item.requirementType]}</td>
                		</tr>
                		<c:if test="${not empty(item.requirementParam)}">
	                		<tr>
	                			<td class="pad-l15">参数：</td>
	                			<td>${item.operatorTag }${item.requirementParam}</td>
	                		</tr>
                		</c:if>
                		<tr>
                			<td class="pad-l15">描述：</td>
                			<td>${item.requirementDesc }</td>
                		</tr>
                	</tbody>
                </table>	
            </div>
			</c:forEach>
		</div>
	</div>
</div>
<div class="text-right pop-btn-box pad">
	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="save-data">确定</button>
</div>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<script type="text/javascript">

$('.scroll-box').height($(frameElement).height()-106)

//确定选择
$("[data-role='save-data']").click(function(event) {
  	event.preventDefault();
  	$('.reqItem').each(function(){
  	  	var json=$(this).data('json');
  	  	parent.$('[data-role="add-conditions"]').click();
  	  	var $item=parent.$('.conditionsItem:last');
  	  	$item.find('.reqType').val(json.reqType);
  	  	$item.find('.operator').val(json.operator);
  	  	$item.find('.reqParam').val(json.reqParam);
  	  	$item.find('.reqDesc').val(json.reqDesc);
	});
	parent.resetTab();
  	parent.$.closeDialog(frameElement.api);
});


//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api);
});

$('#specialty').change(function(){
    location.href='${ctx}/graduation/degreeCollege/copyDegree?collegeId=${param.collegeId}&collegeSpecialtyId='+$(this).val();
});

</script>
</body>
</html>