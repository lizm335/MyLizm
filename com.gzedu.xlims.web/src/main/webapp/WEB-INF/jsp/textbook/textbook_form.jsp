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
$(function() {
	var oldTextbookCode = $("#textbookCode").val();
	if($('#action').val() == 'view'){
		$(':input').attr("disabled","disabled");
		$('[data-role="back-off"]').removeAttr("disabled");
		$('.btn-save-edit').remove();  
		$('.btn-cancel-edit').remove();  
	}
	
	var price = $('#inputForm').find("input[name='price']").val();
	var discountPrice = $('#inputForm').find("input[name='discountPrice']").val();
	if (price.match(/^[1-9][0-9]*(\.[0-9]{1,2})?$/) && discountPrice.match(/^[1-9][0-9]*(\.[0-9]{1,2})?$/)) {
		$("#ratio").text((discountPrice/price * 100).toFixed(2) + "%");
	}
	
	//参考： http://bv.doc.javake.cn/examples/ 
    $('#inputForm').bootstrapValidator({
    		excluded : [ ':disabled' ],//验证下拉必需加
            fields: {
        		isbn: {
                    validators: {
                        notEmpty: {
                        	message: "请填写ISBN码"
                        }
                    }
                },
            	textbookCode: {
                    validators: {
                        notEmpty: {
                        	message: "请填写书号"
                        },
                        callback: {  
                            message: '书号已存在',  
                            callback: function(value, validator) {  
                            	var validator=true;     
                            	if(oldTextbookCode != $("#textbookCode").val() && $("#textbookCode").val()!==" "){
	                            	$.ajax({  
	                                    type : "post",  
	                                    url : "${ctx}/textbook/checkTextbookCode",  
	                                    dataType:'json',
	                                    data : {textbookCode:$('#textbookCode').val()},  
	                                    async : false,  
	                                    success : function(data){  
	                                    	if(data.successful){
												validator=false;
											}  
	                                    }
	                            	});
                            	}
                              return validator;
                            }  
                         }
                    }
                },
                textbookName: {
                    validators: {
                        notEmpty: {
                        	message: "请填写教材名称"
                        }
                    }
                },
                price: {
                    validators: {
                        /* notEmpty: {
                        	message: "请填写原价"
                        }, */
                        regexp: {
                        	regexp: /^[1-9][0-9]*(\.[0-9]{1,2})?$/,
                            message: "请填写有效数字"
                        },
                        callback: {
        					callback: function(value, validator, $field, options){
    							var discountPrice = $('#inputForm').find("input[name='discountPrice']").val();
        						if (value.match(/^[1-9][0-9]*(\.[0-9]{1,2})?$/) && discountPrice.match(/^[1-9][0-9]*(\.[0-9]{1,2})?$/)) {
        							$("#ratio").text((discountPrice/value * 100).toFixed(2) + "%");
        						} else {
        							$("#ratio").text("");
        						}
        						
        						return true;
        					}
        				}
                    }
                },
                discountPrice: {
                    validators: {
                        /* notEmpty: {
                        	message: "请填写定价"
                        }, */
                        regexp: {
                        	regexp: /^[1-9][0-9]*(\.[0-9]{1,2})?$/,
                            message: "请填写有效数字"
                        },
                        callback: {
        					callback: function(value, validator, $field, options){
    							var price = $('#inputForm').find("input[name='price']").val();
    							if (value.match(/^[1-9][0-9]*(\.[0-9]{1,2})?$/) && price.match(/^[1-9][0-9]*(\.[0-9]{1,2})?$/)) {
        							$("#ratio").text((value/price * 100).toFixed(2) + "%");
        						} else {
        							$("#ratio").text("");
        						}
        						
        						return true;
        					}
        				}
                    }
                },
            }
        });
	
  	//选择课程
	$("[data-role='select-pop']").click(function(event) {
		event.preventDefault();
		$.mydialog({
		  id:'select-course',
		  width:1000,
		  height:550,
		  zIndex:11000,
		  content: 'url:' + ctx + '/edumanage/course/choiceMultiCourseList'
		});
	});

	//删除课程
	$(".select-container-ul").on('click', '.select2-selection__choice__remove', function(event) {
		event.preventDefault();
		$("#"+$(this).attr("aria-describedby")).remove();
		$(this).parent().remove();
	});
	
	$("input[name='status']").click(function() {
		if ($(this).val() == 1) {
			$("#reasonType").hide();
			$("#reason").hide();
		} else {
			$("#reasonType").show();
			$("#reason").show();
		}
	});
	
	$(".btn-save-edit").click(function() {
		/* if ($(".select-container-ul").find("li").length == 0) {
			alert("请选择使用课程");
			return false;
		} */
		
		if (!$("#reason").is(":hidden")) {
			if ($("textarea[name='reason']").val() == "") {
				alert("请填写原因");
				return false;
			}
		}
	});
});


	
//选择课程后回调
function choiceCourseCallback(courseIds, names, orders){
	var $container = $(".select-container-ul");
	var html = [
	  '<li class="select2-selection__choice">',
	    '<span class="select2-selection__choice__remove" title="删除" data-toggle="tooltip" data-container="body" data-role="delete">×</span>',
	    '<span class="select2-name" title="#name#" data-toggle="tooltip" data-container="body"  data-order="#order#">#name#</span>',
	    '<input type="hidden" name = "courseIds" value="#courseId#"/>',			      
	    '</li>'
	];
	
	for (var i = 0; i < courseIds.length; i++) {
		var tmp=html.join("");
	    tmp=tmp.replace(/#name#/g, names[i]);
	    tmp=tmp.replace(/#order#/g, orders[i]);
	    tmp=tmp.replace(/#courseId#/g, courseIds[i]);
	    $container.append(tmp);
	}
	
}
</script> 

</head>
<body class="inner-page-body">

<section class="content-header clearfix">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">教材管理</a></li>
		<li class="active">新增教材</li>
	</ol>
</section>
<section class="content">
	<div class="box no-margin">
		<div class="box-body pad-t15">
		<form id="inputForm" class="form-horizontal" role="form" action="${ctx }/textbook/${action}" method="post">
			<input id="action" type="hidden" name="action" value="${action}">
            <input type="hidden" name="textbookId" value="${entity.textbookId}">
			<div class="form-horizontal reset-form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>ISBN</label>
					<div class="col-sm-7">
						<input type="text" id="isbn" name="isbn" class="form-control" placeholder="ISBN" value="${entity.isbn}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>书号</label>
					<div class="col-sm-7">
						<input type="text" id="textbookCode" name="textbookCode" class="form-control" placeholder="书号" value="${entity.textbookCode}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">货位号</label>
					<div class="col-sm-7">
						<input type="text" name="positionNo" class="form-control" placeholder="货位号" value="${entity.positionNo}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>教材名称</label>
					<div class="col-sm-7">
						<input type="text" name="textbookName" class="form-control" placeholder="教材名称" value="${entity.textbookName}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">作者</label>
					<div class="col-sm-7">
						<input type="text" name="author" class="form-control" placeholder="作者" value="${entity.author}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">出版社</label>
					<div class="col-sm-7">
						<input type="text" name="publishing" class="form-control" placeholder="出版社" value="${entity.publishing}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">版次</label>
					<div class="col-sm-7">
						<input type="text" name="revision" class="form-control" placeholder="版次" value="${entity.revision}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">原价</label>
					<div class="col-sm-7">
						<div class="input-group">
							<span class="input-group-addon">￥</span>
							<input type="text" name="price" class="form-control" placeholder="原价" value="${entity.price}">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">定价</label>
					<div class="col-sm-7">
						<div class="input-group">
							<span class="input-group-addon">￥</span>
							<input type="text" name="discountPrice" class="form-control" placeholder="定价" value="${entity.discountPrice}">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">折扣比例</label>
					<div class="col-sm-7">
						<div class="select2-container select2-container--default show oh">
							<div id="ratio" class="select2-selection--multiple" style="height: 32px; line-height: 32px;">
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">封面</label>
					<div class="col-sm-7">
						<input type="button" class="btn  btn-default" value="添加封面" onclick="uploadImage('imgId','imgPath');" />
						<div class="inline-block vertical-middle">
							<ul class="img-list clearfix">
								<li>
									<img id="imgId" src="${entity.cover }" class="user-image" style="cursor: pointer;" >
									<input id="imgPath" type="hidden" value="${entity.cover }" name="cover">
								</li>
							</ul>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><small class="text-red">*</small>状态</label>
					<div class="col-sm-7">
						<div class="radio">
	                        <label>
	                          <input type="radio" name="status" value="1" <c:if test="${entity.status == 1 || empty entity.textbookId }">checked</c:if> />启用
	                        </label>
	                        <label class="margin_l15">
	                          <input type="radio" name="status" value="0" <c:if test="${entity.status == 0 && not empty entity.textbookId  }">checked</c:if> />停用
	                        </label>
	                    </div>
					</div>
				</div>
				<div id="reasonType" class="form-group" <c:if test="${entity.status == 1 || empty entity.textbookId }">style="display: none;"</c:if> >
					<label class="col-sm-2 control-label"><small class="text-red">*</small>原因</label>
					<div class="col-sm-7">
						<div class="radio">
	                        <label>
	                          <input type="radio" name="reasonType" value="1" <c:if test="${entity.reasonType == 1 || empty entity.reasonType }">checked</c:if> />改版
	                        </label>
	                        <label class="margin_l15">
	                          <input type="radio" name="reasonType" value="2" <c:if test="${entity.reasonType == 2 }">checked</c:if> />更换
	                        </label>
	                    </div>
					</div>
				</div>
				<div id="reason" class="form-group" <c:if test="${entity.status == 1 || empty entity.textbookId }">style="display: none;"</c:if> >
					<label class="col-sm-2 control-label"> </label>
					<div class="col-sm-7">
						<textarea rows="5" class="form-control" name="reason" placeholder="请填写具体原因">${entity.reason }</textarea>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-7 col-sm-offset-2">
						<button type="submit" class="btn btn-success min-width-90px margin_r15 btn-save-edit">保存</button>
						<button class="btn btn-default min-width-90px btn-cancel-edit" onclick="history.back()">取消</button>
					</div>
				</div>
			</div>
		</form>
		</div>
	</div>
</section>
<jsp:include page="/eefileupload/upload.jsp"/>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

</body>
</html>
