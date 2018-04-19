<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<title>班主任平台 - 答疑管理</title>
<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
</head>
<body>
	<form id="theform" action="${ctx}/home/class/newFeedback/${action }" method="post">
		<input type="hidden" name="subjectId" value="${subjectEntity.subjectId }">
		<input type="hidden" name="replyId" value="${replyEntity.replyId }">
		<div class="box no-border no-shadow margin-bottom-none">
			<div class="box-header with-border">
				<h3 class="box-title">${action eq 'createOften'?'发布新的常见问题':'修改常见问题' }</h3>
			</div>
		    <div class="scroll-box">
		    	<div class="box-body pad-t15">
		    		<table class="table no-border">
		    			<tr>
		            		<th width="130" class="text-right">
		            			<div class="pad-t5">
		            				<span class="text-red">*</span>
		            				常见问题类型
		            			</div>
		            		</th>
		            		<td>
		                  		<div class="position-relative">
		            				<select name="oftenType" class="form-control select2" datatype="*">
		            					<option value="">请选择</option>
		            					<c:forEach items="${oftenTypeMap }" var="map">
		            						<option value="${map.key }" <c:if test="${subjectEntity.oftenType eq map.key }">selected</c:if> >${map.value }</option>
		            					</c:forEach>
		            				</select>
		            			</div>
		                	</td>
		            	</tr>
		            	<tr>
		            		<th width="130" class="text-right">
		            			<div class="pad-t5">
		            				常见问题标签
		            			</div>
		            		</th>
		            		<td>
		                  		<div class="position-relative">
		            				<input type="text" name="isCommendType"  value="${subjectEntity.isCommendType }"
		            					placeholder="标签用于检索，多个请用英文分号“;”隔开" class="form-control ">
		            			</div>
		                	</td>
		            	</tr>
		            	<tr>
		            		<th width="130" class="text-right">
		            			<div class="pad-t5">
		            				<span class="text-red">*</span>
		            				常见问题标题
		            			</div>
		            		</th>
		            		<td>
		                  		<div class="position-relative">
		            				<input type="text"  name="title"  class="form-control" value="${subjectEntity.subjectTitle }"
		            				placeholder="常见问题标题" datatype="*" nullmsg="请填写常见问题标题" errormsg="请填写常见问题标题">
		            			</div>
		                	</td>
		            	</tr>
		            	<tr>
		            		<th class="text-right">
		            			<div class="pad-t5">
		            				<span class="text-red">*</span>
		            				常见问题内容
		            			</div>
		            		</th>
		            		<td>
		            			<div class="position-relative text-left">
		            				<textarea class="form-control" name="content1"  datatype="*" nullmsg="请填写常见问题内容" errormsg="请填写常见问题内容" rows="5">
		            					${subjectEntity.subjectContent }
		            				</textarea>
		            			</div>
		            		</td>
		            	</tr>
		                <tr>
		                    <th width="130" class="text-right">
		                        <div class="pad-t5">
		                            常见问题图片
		                        </div>
		                    </th>
		                    <td>
		                    	<div class="addImages1">
			                        <div class="position-relative">
			                            <button class="btn min-width-90px btn-default btn-add-img-addon" type="button">上传图片</button>
			                            <small class="gray9">* 支持jpg/png/gif类型图片，每张不超过5mb大小</small>
			                        </div>
			                        <ul class="list-inline img-gallery upload-box">
			                        	<c:forEach items="${subjectImgList }" var="item">
			                        		<li> 
			                        			<img src="${item }">
			                        			<input type="hidden" name="imgUrl1" value="${item }"> 
			                        			<span class="glyphicon glyphicon-remove-sign" data-toggle="tooltip" title="删除">
			                        			</span>
			                        		</li>
			                        	</c:forEach>
			                        </ul>
		                        </div>
		                    </td>
		                </tr>
		                <tr>
		                    <th class="text-right">
		                        <div class="pad-t5">
		                            <span class="text-red">*</span>
		                            回答
		                        </div>
		                    </th>
		                    <td>
		                        <div class="position-relative text-left">
		                            <textarea class="form-control" name="content2" placeholder="回答" datatype="*"  errormsg="请填写回答内容" rows="5">
		                            	${replyEntity.replyContent }
		                            </textarea>
		                        </div>
		                    </td>
		                </tr>
		                <tr>
		                    <th width="130" class="text-right">
		                        <div class="pad-t5">
		                            回答图片
		                        </div>
		                    </th>
		                    <td>
		                    	<div class="addImages2">
			                        <div class="position-relative ">
			                            <button class="btn min-width-90px btn-default btn-add-img-addon" type="button">上传图片</button>
			                            <small class="gray9">* 支持jpg/png/gif类型图片，每张不超过5mb大小</small>
			                        </div>
		                            <ul class="list-inline img-gallery upload-box">
		                            	<c:forEach items="${replyImgList }" var="item">
			                            	<li> 
			                            		<img src="${item }">
			                            		<input type="hidden" name="imgUrl2" value="${item }">
			                            	 	<span class="glyphicon glyphicon-remove-sign" data-toggle="tooltip" title="删除">
			                            	 	</span>
			                            	</li>
		                            	</c:forEach>
									</ul>
								</div>
		                    </td>
		                </tr>
		            </table>
		    	</div>
		    </div>
		</div>
		<div class="text-right pop-btn-box pad">
			<button type="button" class="btn btn-default min-width-90px margin_r15" data-role="close-pop">取消</button>
			<button type="submit" class="btn btn-success min-width-90px" data-role="sure">确定</button>
		</div>
	</form>


<jsp:include page="/eefileupload/upload.jsp" /> 
<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>
<script type="text/javascript">
	$('.select2').select2();
	
	/*不需要置顶按钮*/
	$('.jump-top').remove();
	
	//添加图片，1和2
	$('body').on('click','.btn-add-img-addon',function() {
		var index=$('.btn-add-img-addon').index(this);
		var $container=$(this).parent().next('.upload-box');
		uploadImageNew($container, 'imgUrl'+(index+1) );
	});	
	
	/*删除图片*/
	$("body").on('click', '.glyphicon-remove-sign', function(event) {
		event.preventDefault();
		 $(this).closest('li').remove();
	});
	
	//关闭 弹窗
	$("button[data-role='close-pop']").click(function(event) {
		parent.$.closeDialog(frameElement.api)
	});

	//设置内容主体高度
	$('.scroll-box').height($(frameElement).height()-106);

	//删除添加的人员
	$(".stu-list-box").on("click",".fa-remove",function(){
	  $(this).parent().remove();
	});

	//确认
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
    	ajaxPost:true,
      tiptype:function(msg,o,cssctl){
        if(!o.obj.is("form")){
            var msgBox=o.obj.closest('.position-relative').find('.tooltip');
            msgBox.children('.tooltip-inner').text(msg);
            if(msgBox.hasClass('left')){
              msgBox.css({
                width:130,
                left:-120,
                top:5
              })
            } else{
              msgBox.css({
                bottom:"100%",
                'margin-bottom':-5
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
        postIngIframe=$.formOperTipsDialog({
          text:'数据提交中...',
          iconClass:'fa-refresh fa-spin'
        });      
      }, callback:function(data){
            postIngIframe.find('[data-role="tips-text"]').html(data.message);
            postIngIframe.find('[data-role="tips-icon"]').attr('class','fa fa-check-circle');
            /*关闭弹窗*/
            setTimeout(function(){
              parent.location.href=parent.location.href;
            },1500)
      }
    });

	</script>
</body>
</html>
