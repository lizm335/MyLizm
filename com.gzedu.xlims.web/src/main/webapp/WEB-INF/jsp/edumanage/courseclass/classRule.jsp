<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>设置自动分班规则</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
</head>
<body>

<div class="box no-border no-shadow margin-bottom-none">
	<div class="box-header with-border">
		<h3 class="box-title">设置自动分班规则</h3>
	</div>
	<div class="box-body slim-Scroll" id="rules">
		<form id="inputForm"  role="form" method="post">
		<input type="hidden" name="xxid" value="${rulesClass.xxid}">
		<!-- <input type="hidden" id="pointClassNum" name="pointClassNum"> -->
		<div class="input-group input-group-sm">
			<p class="input-group-addon">学期：</p>
			<select class="selectpicker show-tick form-control"data-size="8" data-live-search="true" id="gradeId" name="gradeId">
				<c:forEach items="${gradeMap}" var="map">
					<option value="${map.key}"  <c:if test="${map.key==gradeId || map.key==currentGradeId}">selected='selected'</c:if> >
					${map.value}</option>
				</c:forEach>
			</select>	
		</div>
		<h4 class="f14">分班规则：</h4>
		<div class="panel panel-default flat margin_b10">
			<div class="panel-heading pad-t5 pad-b5">
				<div class="checkbox no-margin" >
			    	<label style="padding-left:0">
			        	按学习中心分班
			    	</label>
				</div>
			</div>
			<div class="panel-body pad-t10">
				<div>命名规则：年级+专业+层次+学习中心+序号</div>
				<div class="gray9">
					<small>例如：2016春管理学高起专麓湖学习中心01班
					</small>
				</div>
				<div class="input-group input-group-sm margin_t10">
					<p class="input-group-addon">人数上限：</p>
					<input type="number" maxlength="5" value="${centerPointNum}" class="form-control"  name="pointClassNum1" id="pointClassNum1" val="pointClassNum">	
					<div class="input-group-addon gray9 no-border no-pad-right">默认500，超出上限则自动生成一个新的班级</div>
				</div>
				<div class="margin_t10">
					<button type="button" class="btn btn-primary btn-sm" data-role="add-center">请选择需指定分班规则的学习中心</button>
				</div>
				<table class="table table-bordered vertical-middle text-center table-font margin_t10">
					<thead class="with-bg-gray">
						<tr>
							<th>学习中心编号</th>
							<th>学习中心名称</th>							
							<th>操作</th>
						</tr>
					</thead>
					<tbody data-id="center-box">
					 <c:forEach items="${rulesClassCenter}" var="studyCenter">
						<tr id="tr">
							<td>${studyCenter.getGjtStudyCenter().getGjtOrg().getCode()}</td>
							<input type="hidden" id="studyCenterId" name="xxzxId" value="${studyCenter.xxzxId}">
							<td id="td">${studyCenter.getGjtStudyCenter().getGjtOrg().getOrgName()}</td>
							<td>
								<!-- <a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove"><i class="fa fa-trash-o text-red"></i></a> -->
							</td>
						</tr>
					 </c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div class="panel panel-default flat margin-bottom-none">
			<div class="panel-heading pad-t5 pad-b5">
				<div class="checkbox no-margin">
			    	<label style="padding-left:0">			        	
			        	按年级专业层次总体分班
			    	</label>
				</div>
			</div>
			<div class="panel-body pad-t10">
				<div>命名规则：年级+专业+层次+序号</div>
				<div class="gray9">
					<small>例如：2016春管理学高起专01班
					</small>
				</div>
				<div class="input-group input-group-sm margin_t10">
					<p class="input-group-addon">人数上限：</p>
					<input type="number" maxlength="5" class="form-control" value="${rulesClass.pointClassNum}" id="pointClassNum2" val="pointClassNum" name="pointClassNum2">	
					<div class="input-group-addon gray9 no-border no-pad-right">默认500，超出上限则自动生成一个新的班级</div>
				</div>
			</div>
		</div>
	</form>
	</div>
</div>
<div class="text-right pop-btn-box pad" id="textRight">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
	<button type="button" id="button" class="btn btn-success min-width-90px" data-role="sure">确认</button>
</div>
<script type="text/javascript"> 
//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api)
});
//确认
$("button[data-role='sure']").click(function(event) {
	 /* var $checkedIds = $("input[type='checkbox']:enabled:checked");
	 console.log($checkedIds.size());
		if ($checkedIds.size() ==0) {
			alert('至少选择其中一项分班规则');
			return false;
		}
	var check1=$("input[val='pointClassType1']:enabled:checked");
	if(check1.size()>0){
		var tdnode=$('#tr').find("td");
		if(tdnode.length<0){
			alert('请选择需指定分班规则的学习中心');
			return false;
		}	
	} */
	var type2Num=$('#pointClassNum2').val();
	if(type2Num==''){
		alert('【按年级专业层次总体分班】人数上限不能为空!');
		$('#pointClassNum2').focus();
		return;
	}
	//检查是否有相同的学习中心
	 var arr = [];
	 $("#tr #td").each(function(i){//针对tb表格下的所有td进行遍历
        var name=$(this).text();//返回当前td下的值      
        if(name!=''||name!=null){
        	arr.push(name);
        }     
	})
	 var nary=arr.sort();
	 for(var i=0;i<arr.length;i++){
		 if (nary[i]==nary[i+1]){
			 alert("按学习中心分班规则中存在相同的【"+nary[i]+"】学习中心,请重新选择！");
			 return false;
		 	}
		}
	 if(arr.length>0){
		 var type1Num=$('#pointClassNum1').val();
			if(type1Num==''){
				alert('【按学习中心分班】人数上限不能为空!');
				$('#pointClassNum1').focus();
				return;
			} 
	 }	 
	 var postIngIframe=$.mydialog({
		  id:'dialog-1',
		  width:150,
		  height:50,
		  backdrop:false,
		  fade:false,
		  showCloseIco:false,
		  zIndex:11000,
		  content: '<div class="text-center pad-t15">数据提交中...<i class="fa fa-refresh fa-spin"></i></div>'
		});
	$.post('${ctx}/edumanage/rulesClass/addRule',$('#inputForm').serialize(),function(data){
		 if(data.successful){
			 postIngIframe.find(".text-center.pad-t15").html('数据提交成功...<i class="icon fa fa-check-circle"></i>')
		      /*关闭弹窗*/
		      setTimeout(function(){
		        parent.$.closeDialog(frameElement.api)
		      },1000)
		 }else{
			 alert(data.message);
			 $.closeDialog(postIngIframe);
		 }
	 },'json');
	 return;
});
$('#gradeId').change(function(){
	var gradeId=$('#gradeId').val();
	 $.ajax({
			type: "GET",
			url: "${ctx}/edumanage/rulesClass/queryGjtRuleInfo",
        	data:{gradeId:gradeId},
       	 	dataType: "json",
        	async: true,	// 是否异步
        	success:function(datas){        		
        		$("input[val='pointClassNum']").val('');
        		$("tbody[data-id='center-box']").html('');
        		if(JSON.stringify(datas)!='{}'){ //通过 JSON 自带的 stringify() 方法来判断对象是否为空
        			/* if(!datas.isSubmit){
	        			$('#button').removeAttr("disabled");	        			
	        		}else{	        			
	        			$('#button').attr("disabled","true");
	        		} */
        			if(datas.studyCenterMap!=null&&datas.centerPointType=='1'){
        				$('#pointClassNum1').val(datas.centerPointNum);
        				var content=datas.studyCenterMap;
        				for(var data in content){
        					$("tbody[data-id='center-box']").append(
	        						'<tr id="tr">'+
	    							'<td>'+content[data].code+'</td>'+
	    							'<input type="hidden" id="studyCenterId" name="xxzxId" value="'+content[data].xxzxId+'">'+
	    							'<td>'+content[data].orgName+'</td>'+
	    							'<td>'+
	    								/* '<a href="#" class="operion-item operion-view" data-toggle="tooltip" title="删除" data-role="remove"><i class="fa fa-trash-o text-red"></i></a>'+ */
	    							'</td>'+
	    						'</tr>');	
        				}      				
        			}
	        		if(datas.isDataInfo!=false&&datas.gradePointType=='2'){
	    				$('#pointClassNum2').val(datas.gradePointNum);
	        		}
	        		
        		}        		
        	}				
		});
})

//添加学习中心
$('[data-role="add-center"]').click(function(event) {
	var wh=$(window).height();
	$.mydialog({
	  id:'add-course',
	  width:800,
	  height:(wh>500?500:wh),
	  zIndex:1200,
	  content: 'url:${ctx}/edumanage/rulesClass/addStudyCenter' 
	});
});
$('.slim-Scroll').slimScroll({
    height: 375,
    size: '5px'
});
//删除学习中心
$('body').on('click', '[data-role="remove"]', function(event) {
  $(this).closest('tr').remove();
});
</script>
</body>
</html>