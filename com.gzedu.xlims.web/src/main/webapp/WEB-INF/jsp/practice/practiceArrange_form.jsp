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
<body>

<form id="theform" action="${ctx }/practiceArrange/update" method="post">
<input type="hidden" name="arrangeId" value="${entity.arrangeId}">
<div class="box no-border no-shadow">
	<div class="box-header with-border">
      <h3 class="box-title">设置社会实践安排</h3>
    </div>
    <div class="box-body scroll-box">
		<table class="table-gray-th">
			<tr>
				<th width="20%" class="text-center">专业代码</th>
				<td>
					${entity.gjtSpecialtyBase.specialtyCode}
				</td>
			</tr>
			<tr>
				<th class="text-center">专业名称</th>
				<td>
					${entity.gjtSpecialtyBase.specialtyName}
				</td>
			</tr>
			<tr>
				<th class="text-center">层次</th>
				<c:set var="pycc">${entity.gjtSpecialtyBase.specialtyLayer}</c:set>
				<td>${pyccMap[pycc]}</td>
			</tr>
			<tr>
				<th class="text-center">
					社会实践申请人数
				</th>
				<td>
					${entity.applyNum}人
					<small class="gray9">
						已分配  <b class="text-orange guideNum">${fn:length(entity.gjtPracticeAdvisers)}</b> 个老师，已分配指导 <b class="text-orange distributionNum"></b> 人，剩余 <b class="text-orange remainderNum"></b> 人待分配
						
					</small>
				</td>
			</tr>
			<tr>
				<th class="text-center">
					<small class="text-red">*</small>社会实践指导老师
					<div class="margin_t5">
						<button class="btn btn-default bg-white btn-sm" type="button" data-role="add-person-1" data-rel="1">
							<i class="fa fa-fw fa-plus"></i>
							添加老师
						</button>
					</div>
				</th>
				<td>
					<div class="clearfix" style="margin:0 -5px;" data-box="1">
						<c:forEach items="${entity.gjtPracticeAdvisers}" var="adviser">
							<div class="alert col-sm-4 col-xs-6 pad-l5 no-pad-top no-pad-bottom pad-r5 margin_b10 fade in">
								<div class="box box-border no-shadow pad no-margin">
				                    <button type="button" class="close" data-dismiss="alert">×</button>
				                    <div class="media no-margin">
				                    	<div class="media-left text-center">
				                    		<img src="${adviser.teacher.zp}" class="img-circle" width="50" height="50">
				                    		<div class="margin_t5">${adviser.teacher.xm}</div>
				                    	</div>
				                    	<div class="media-body media-middle">
				                    		共已分配指导 ${adviser.guideNum1} 人
				                    		<div class="margin_t10">
				                    			本专业分配指导：
				                    			
				                    			<input type="text" name="adviserNums" value="${adviser.guideNum2}" class="form-control input-xs inline-block" style="width:45px;"
				                    				onkeyup="changeValue(this)"  onafterpaste="changeValue(this)">
				                    			
				                    			人
				                    			<input type="hidden" name="advisers" value="${adviser.teacherId}">
				                    		</div>
				                    	</div>
				                    </div>
				                </div>
							</div>
						</c:forEach>
					</div>
				</td>
			</tr>
		</table>

    </div>
</div>
<div class="pop-btn-box pad text-right">
	<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="cancel">取消</button>
	<button type="submit" class="btn btn-success min-width-90px" data-role="sure">确认分配</button>
</div>
</form>

<script>
$('.scroll-box').height($(window).height()-126);

var applyNum = '${entity.applyNum}';
function resetNum() {
	var guideNum = 0;
	var distributionNum = 0;
	$('input[name="adviserNums"]').each(function(index, el) {
		guideNum ++;
		if ($(this).val() != '') {
			distributionNum += parseInt($(this).val());
		}
	});
	$(".guideNum").text(guideNum);
	$(".distributionNum").text(distributionNum);
	$(".remainderNum").text(applyNum - distributionNum);
}

function changeValue(obj) {
	obj.value = obj.value.replace(/\D/g,'');
	resetNum();
}

;(function(){
	
	resetNum();
	
	if('${action}' == 'view'){
		$(':input').attr("disabled","disabled");
		$('[data-role="cancel"]').removeAttr("disabled");
		$('[data-role="sure"]').remove();  
		$('.close').remove();  
	}
	
	var $theform=$("#theform");

	var htmlTemp='<div class="tooltip top" role="tooltip" style="white-space: nowrap;">'
          +'<div class="tooltip-arrow"></div>'
          +'<div class="tooltip-inner"></div>'
          +'</div>';
	$theform.find(":input[datatype]").each(function(index, el) {
		$(this).after(htmlTemp);
	});

	$.Tipmsg.r='';
	var postIngIframe;
	var postForm=$theform.Validform({
	  showAllError:true,
	  ajaxPost:true,
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
		  if($('input[name="advisers"]').length == 0) {
	            alert('请添加社会实践指导老师');
	            return false;
	      }
		  
		  var flag = true;
		  $('input[name="adviserNums"]').each(function(index, el) {
				if (el.value == '' || el.value == 0) {
					flag = false;
				}
		  });
		  if (!flag) {
			  alert('指导人数必须大于0');
	          return false;
		  }
		  
		  if ($(".remainderNum").text() != 0) {
			  alert('指导老师设置的指导人数总和必须等于社会实践申请人数');
	          return false;
		  }
		  
		  $('[data-role="sure"]').attr("disabled","disabled");
	  },
	  callback:function(data){
		  if (data.successful) {
			  parent.$.closeDialog(frameElement.api);
			  parent.location.href = "${ctx}/practiceArrange/list";
		  } else {
			  alert(data.message);
			  $('[data-role="sure"]').removeAttr("disabled");
		  }
	  }
	});

})();

//添加论文指导老师
var arrangeId = '${entity.arrangeId}';
$('body')
.on('click','[data-role="add-person-1"]',function(event) {
	var advisers = '';
	$('input[name="advisers"]').each(function(index, el) {
		advisers = advisers + el.value + ',';
	});
	if (advisers != '') {
		advisers = advisers.substring(0, advisers.length - 1);
	}
	
	var _this=this;
  	$.mydialog({
	  id:'add-person',
	  width:760,
	  height:490,
	  zIndex:11000,
	  urlData:{
	  	container:$(_this).attr('data-rel')
	  },
	  content: 'url:${ctx}/practiceArrange/choiceGuideTeacher?arrangeId='+arrangeId+"&teacherIds="+advisers
	});
});

/*取消*/
$('[data-role="cancel"]').click(function(event) {
  	parent.$.closeDialog(frameElement.api);
});

</script>

</body>
</html>


					

