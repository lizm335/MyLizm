<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>班主任平台</title>

<%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>

</head>
<body class="inner-page-body">
<section class="content-header">
  <button class="btn btn-default btn-sm pull-right min-width-90px offset-margin-tb-15" data-role="back-off">返回</button>
  <div class="pull-left">
    您所在位置：
  </div>
  <ol class="breadcrumb">
    <li><a href="${ctx}/home/class/index"><i class="fa fa-home"></i> 首页</a></li>
    <li class="active"><a href="#">毕业管理</a></li>
    <li class="active">毕业设计详情</li>
  </ol>
</section>
<section class="content">
  <div class="box">
    <div class="box-body">
      <div class="media pad">
        <div class="media-left" style="padding-right:25px;">
          <img src="${studentInfo.avatar}" class="img-circle" style="width:112px;height:112px;" alt="User Image">
        </div>
        <div class="media-body">
          <h3 class="margin_t10">${studentInfo.xm}</h3>
          <div class="row">
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>学号:</b> <span>${studentInfo.xh}</span>
            </div>
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>手机:</b>
              <span>${studentInfo.sjh}</span>
            </div>
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>邮箱:</b>
              <span>${studentInfo.dzxx}</span>
            </div>
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>层次:</b> <span>${trainingLevelMap[studentInfo.pycc]}</span>
            </div>
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>年级:</b>
              <span>${studentInfo.gjtGrade.gradeName}</span>
            </div>
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>专业:</b>
              <span>${studentInfo.gjtSpecialty.zymc}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="box-footer">
      <div class="row stu-info-status">
        <div class="col-xs-6 col-sm-2_5">
          <div class="f24 text-center">
			<c:choose>
				<c:when test="${not empty thesisApply}">
              		<c:choose>
              			<c:when test="${thesisApply.applyDegree == 0}">无需学位</c:when>
              			<c:otherwise>申请学位</c:otherwise>
              		</c:choose>
				</c:when>
				<c:otherwise>
					--
				</c:otherwise>
			</c:choose>
          </div>
          <div class="text-center gray6">学位意向</div>
        </div>
        <div class="col-xs-6 col-sm-2_5">
          	<c:choose>
				<c:when test="${not empty thesisApply}">
              		<div class="f24 text-center">${thesisStatusMap[thesisApply.status]}</div>
				</c:when>
				<c:otherwise>
					<div class="f24 text-center text-red">未申请</div>
				</c:otherwise>
			</c:choose>
          <div class="text-center gray6">论文状态</div>
        </div>
        <div class="col-xs-6 col-sm-2_5">
          	<c:choose>
				<c:when test="${not empty thesisApply}">
              		<c:choose>
              			<c:when test="${thesisApply.status > 6}"><div class="f24 text-center text-green">已确认</div></c:when>
              			<c:otherwise><div class="f24 text-center text-orange">待确认</div></c:otherwise>
              		</c:choose>
				</c:when>
				<c:otherwise>
					--
				</c:otherwise>
			</c:choose>
          <div class="text-center gray6">论文终稿确认</div>
        </div>
        <div class="col-xs-6 col-sm-2_5">
        	<c:choose>
				<c:when test="${not empty practiceApply}">
              		<div class="f24 text-center">${practiceStatusMap[practiceApply.status]}</div>
				</c:when>
				<c:otherwise>
					<div class="f24 text-center text-red">未申请</div>
				</c:otherwise>
			</c:choose>
          <div class="text-center gray6">社会实践状态</div>
        </div>
        <div class="col-xs-6 col-sm-2_5 text-center">
        	<c:if test="${not empty thesisApply}">
        		<c:if test="${thesisApply.status == 6}">
        		  <button class="btn btn-default btn-sm margin_t10 confirmB">
		            <i class="fa fa-submit-sth f20 vertical-middle"></i>
		            	确认论文终稿
		          </button>
        		</c:if>
        	</c:if>
        </div>
      </div>
    </div>
  </div>

  <div class="box no-border margin-bottom-none">
    <div class="nav-tabs-custom">
      <ul class="nav nav-tabs nav-tabs-lg">
        <li class="active"><a href="#tab_notice_1" data-toggle="tab">毕业设计</a></li>
        <li><a href="#tab_notice_2" data-toggle="tab">社会实践</a></li>
      </ul>
      <div class="tab-content">
        <div class="tab-pane active" id="tab_notice_1">
        <c:if test="${not empty thesisApply}">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h3 class="panel-title text-bold">论文</h3>
            </div>
            <div class="panel-body">
              <div class="row">
                <div class="col-sm-5 margin_t10 margin_b10">
                  <div class="media">
                    <div class="media-left">
                    	<c:choose>
                    		<c:when test="${not empty thesisApply.guideTeacher1}">
                    			<img src="${thesisApply.guideTeacher1.zp}" class="img-circle" width="80" height="80" alt="User Image">
                    		</c:when>
                    		<c:otherwise>
                    			<img src="${css}/ouchgzee_com/platform/xlbzr_css/dist/img/no-img.png" class="img-circle" width="80" height="80" alt="User Image">
                    		</c:otherwise>
                    	</c:choose>
                    </div>
                    <div class="media-body">
                      <div class="clearfix stu-info-status">
                        <div class="col-xs-6">
                          <div class="f24 text-center">
                          	<c:choose>
	                    		<c:when test="${not empty thesisApply.guideTeacher1}">
	                    			${thesisApply.guideTeacher1.xm}
	                    		</c:when>
	                    		<c:otherwise>
	                    			--
	                    		</c:otherwise>
	                    	</c:choose>
                          </div>
                          <div class="text-center gray6">论文指导老师</div>
                        </div>
                        <div class="col-xs-6">
                          <div class="f24 text-center">
                          	<c:choose>
	                    		<c:when test="${not empty thesisApply.reviewScore}">
	                    			${thesisApply.reviewScore}
	                    		</c:when>
	                    		<c:otherwise>
	                    			--
	                    		</c:otherwise>
	                    	</c:choose>
                          </div>
                          <div class="text-center gray6">论文初评成绩</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-sm-7 margin_t10 margin_b10">
                  <div class="media">
                    <div class="media-left">
                      <img src="${css}/ouchgzee_com/platform/xlbzr_css/dist/img/images/no-img.png" class="img-circle" width="80" height="80">
                    </div>
                    <div class="media-body">
                      <div class="clearfix stu-info-status">
                        <div class="col-xs-4">
                          <div class="f24 text-center">--</div>
                          <div class="text-center gray6">论文答辩老师</div>
                        </div>
                        <div class="col-xs-4">
                          <div class="f24 text-center">--</div>
                          <div class="text-center gray6">
                          	<c:choose>
	                    		<c:when test="${not empty thesisApply.defenceScore}">
	                    			${thesisApply.defenceScore}
	                    		</c:when>
	                    		<c:otherwise>
	                    			--
	                    		</c:otherwise>
	                    	</c:choose>
                          </div>
                        </div>
                        <div class="col-xs-4">
                          <div class="f24 text-center">
                          	<c:choose>
	                    		<c:when test="${not empty thesisApply.needDefence}">
	                    			<c:choose>
										<c:when test="${thesisApply.needDefence == 0}">
											无需答辩
										</c:when>
										<c:when test="${thesisApply.needDefence == 1}">
											现场答辩
										</c:when>
										<c:when test="${thesisApply.needDefence == 2}">
											远程答辩
										</c:when>
									</c:choose>
	                    		</c:when>
	                    		<c:otherwise>
	                    			--
	                    		</c:otherwise>
	                    	</c:choose>
                          </div>
                          <div class="text-center gray6">答辩形式</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="panel-footer no-bg">
              <div class="graduate-progress">
                <div class="bar"></div>
                <ul class="list-unstyled">
                  <li class="actived">
                    <div class="txt">申请论文</div>
                    <div class="time"><fmt:formatDate value="${thesisApply.gjtGraduationBatch.applyThesisEndDt}" pattern="yyyy.MM.dd"/></div>
                  </li>
                  <li <c:if test="${thesisApply.status > 0}">class="actived"</c:if>>
                    <div class="txt">提交开题报告</div>
                    <div class="time"><fmt:formatDate value="${thesisApply.gjtGraduationBatch.submitProposeEndDt}" pattern="yyyy.MM.dd"/></div>
                  </li>
                  <li <c:if test="${thesisApply.status > 1}">class="actived"</c:if>>
                    <div class="txt">开题报告定稿</div>
                    <div class="time"><fmt:formatDate value="${thesisApply.gjtGraduationBatch.confirmProposeEndDt}" pattern="yyyy.MM.dd"/></div>
                  </li>
                  <li <c:if test="${thesisApply.status > 2}">class="actived"</c:if>>
                    <div class="txt">提交论文初稿</div>
                    <div class="time"><fmt:formatDate value="${thesisApply.gjtGraduationBatch.submitThesisEndDt}" pattern="yyyy.MM.dd"/></div>
                  </li>
                  <li <c:if test="${thesisApply.status > 3}">class="actived"</c:if>>
                    <div class="txt">论文定稿</div>
                    <div class="time"><fmt:formatDate value="${thesisApply.gjtGraduationBatch.confirmThesisEndDt}" pattern="yyyy.MM.dd"/></div>
                  </li>
                  <li <c:if test="${thesisApply.status > 6}">class="actived"</c:if>>
                    <div class="txt">论文初评</div>
                    <div class="time"><fmt:formatDate value="${thesisApply.gjtGraduationBatch.reviewThesisDt}" pattern="yyyy.MM.dd"/></div>
                  </li>
                  <li <c:if test="${thesisApply.status > 9}">class="actived"</c:if>>
                    <div class="txt">论文答辩</div>
                    <div class="time"><fmt:formatDate value="${thesisApply.gjtGraduationBatch.defenceThesisDt}" pattern="yyyy.MM.dd"/></div>
                  </li>
                  <li <c:if test="${thesisApply.status == 13}">class="actived"</c:if>>
                    <div class="txt">完成</div>
                  </li>
                </ul>
              </div>
              <div class="pad-t10 margin_b20">
                <div class="approval-list approval-list-2 clearfix">
                	<dl class="approval-item">
		                <dt class="clearfix">
		                  <div class="a-tit">
		                    <b>申请论文 </b>
		                  </div>
		                  <span class="time state-lb"><fmt:formatDate value="${thesisApply.createdDt}" pattern="yyyy.MM.dd HH:mm"/></span>
		                </dt>
		              </dl>
		              <c:forEach items="${thesisProgList}" var="prog" varStatus="status">
			              <dl class="approval-item">
			                <dt class="clearfix">
			                  <div class="a-tit">
			                    <b>${thesisProgressCodeMap[prog.progressCode]} </b>
			                  </div>
			                  <span class="time state-lb"><fmt:formatDate value="${prog.createdDt}" pattern="yyyy.MM.dd HH:mm"/></span>
			                </dt>
			                <dd>
				                <c:if test="${prog.progressCode eq GraduationProgressCodeEnum.THESIS_STAY_OPEN.code}">
				                  <div class="txt media">
				                    <div class="media-left">
				                      <img src="${thesisApply.guideTeacher1.zp }" class="img-circle" alt="User Image" width="45" height="45">
				                      <div class="text-center f12">${thesisApply.guideTeacher1.xm }</div>
				                    </div>
				                  </div>
		                    	</c:if>
		                    	<c:forEach items="${thesisProgRecord[prog.progressId]}" var="record">
					                  <div class="txt media">
					                    <div class="media-left">
					                    	<c:choose>
			                					<c:when test="${record.isStudent == 1}">
			                						<img src="${record.gjtStudentInfo.avatar}" class="img-circle" alt="User Image" width="45" height="45">
				                          			<div class="text-center f12">${record.gjtStudentInfo.xm}</div>
			                					</c:when>
			                					<c:otherwise>
			                						<img src="${record.teacher.zp}" class="img-circle" alt="User Image" width="45" height="45">
				                          			<div class="text-center f12">${record.teacher.xm}</div>
			                					</c:otherwise>
			                				</c:choose>
					                    </div>
					                    <div class="media-body pad-l15 f12">
					                      <div><fmt:formatDate value="${record.createdDt}" pattern="yyyy.MM.dd HH:mm"/></div>
					                      <div class="margin_t5">${record.content}</div>
					                      <div class="margin_t5">
					                        <a href="${record.attachment}">${record.attachmentName}</a>
					                      </div>
					                    </div>
					                  </div>
		                    	</c:forEach>
			                </dd>
			              </dl>
			          </c:forEach>
                </div>
              </div>
            </div>
          </div>
        </c:if>
        </div>
        
        <div class="tab-pane" id="tab_notice_2">
        <c:if test="${not empty practiceApply}">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h3 class="panel-title text-bold">社会实践</h3>
            </div>
            <div class="panel-body">
              <div class="row">
                <div class="col-sm-6 margin_t10 margin_b10">
                  <div class="media">
                    <div class="media-left">
                      <c:choose>
                    		<c:when test="${not empty practiceApply.guideTeacher1}">
                    			<img src="${practiceApply.guideTeacher1.zp}" class="img-circle" width="80" height="80" alt="User Image">
                    		</c:when>
                    		<c:otherwise>
                    			<img src="${css}/ouchgzee_com/platform/xlbzr_css/dist/img/no-img.png" class="img-circle" width="80" height="80" alt="User Image">
                    		</c:otherwise>
                    	</c:choose>
                    </div>
                    <div class="media-body">
                      <div class="clearfix stu-info-status">
                        <div class="col-xs-6">
                          <div class="f24 text-center">
                          	<c:choose>
	                    		<c:when test="${not empty practiceApply.guideTeacher1}">
	                    			${practiceApply.guideTeacher1.xm}
	                    		</c:when>
	                    		<c:otherwise>
	                    			--
	                    		</c:otherwise>
	                    	</c:choose>
                          </div>
                          <div class="text-center gray6">社会实践指导老师</div>
                        </div>
                        <div class="col-xs-6">
                          <div class="f24 text-center">
                          	<c:choose>
	                    		<c:when test="${not empty practiceApply.reviewScore}">
	                    			${practiceApply.reviewScore}
	                    		</c:when>
	                    		<c:otherwise>
	                    			--
	                    		</c:otherwise>
	                    	</c:choose>
                          </div>
                          <div class="text-center gray6">社会实践成绩</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="panel-footer no-bg">
              <div class="graduate-progress">
                <div class="bar"></div>
                <ul class="list-unstyled">
                  <li class="actived">
                    <div class="txt">申请社会实践</div>
                    <div class="time"><fmt:formatDate value="${practiceApply.gjtGraduationBatch.applyPracticeEndDt}" pattern="yyyy.MM.dd"/></div>
                  </li>
                  <li <c:if test="${practiceApply.status > 0}">class="actived"</c:if>>
                    <div class="txt">提交初稿</div>
                    <div class="time"><fmt:formatDate value="${practiceApply.gjtGraduationBatch.submitPracticeEndDt}" pattern="yyyy.MM.dd"/></div>
                  </li>
                  <li <c:if test="${practiceApply.status > 1}">class="actived"</c:if>>
                    <div class="txt">定稿</div>
                    <div class="time"><fmt:formatDate value="${practiceApply.gjtGraduationBatch.confirmPracticeEndDt}" pattern="yyyy.MM.dd"/></div>
                  </li>
                  <li <c:if test="${practiceApply.status > 2}">class="actived"</c:if>>
                    <div class="txt">初评</div>
                    <div class="time"><fmt:formatDate value="${practiceApply.gjtGraduationBatch.reviewPracticeDt}" pattern="yyyy.MM.dd"/></div>
                  </li>
                  <li <c:if test="${practiceApply.status == 13}">class="actived"</c:if>>
                    <div class="txt">完成</div>
                  </li>
                </ul>
              </div>
              <div class="pad-t10 margin_b20">
                <div class="approval-list approval-list-2 clearfix">
                  <dl class="approval-item">
	                <dt class="clearfix">
	                  <div class="a-tit">
	                    <b>申请社会实践 </b>
	                  </div>
	                  <span class="time state-lb"><fmt:formatDate value="${practiceApply.createdDt}" pattern="yyyy.MM.dd HH:mm"/></span>
	                </dt>
	              </dl>
	              <c:forEach items="${practiceProgList}" var="prog" varStatus="status">
		              <dl class="approval-item">
		                <dt class="clearfix">
		                  <div class="a-tit">
		                    <b>${practiceProgressCodeMap[prog.progressCode]} </b>
		                  </div>
		                  <span class="time state-lb"><fmt:formatDate value="${prog.createdDt}" pattern="yyyy.MM.dd HH:mm"/></span>
		                </dt>
		                <dd>
			                <c:if test="${prog.progressCode eq GraduationProgressCodeEnum.PRACTICE_STAY_OPEN.code}">
			                  <div class="txt media">
			                    <div class="media-left">
			                      <img src="${practiceApply.guideTeacher1.zp }" class="img-circle" alt="User Image" width="45" height="45">
			                      <div class="text-center f12">${practiceApply.guideTeacher1.xm }</div>
			                    </div>
			                  </div>
	                    	</c:if>
	                    	<c:forEach items="${practiceProgRecord[prog.progressId]}" var="record">
				                  <div class="txt media">
				                    <div class="media-left">
				                    	<c:choose>
		                					<c:when test="${record.isStudent == 1}">
		                						<img src="${record.gjtStudentInfo.avatar}" class="img-circle" alt="User Image" width="45" height="45">
			                          			<div class="text-center f12">${record.gjtStudentInfo.xm}</div>
		                					</c:when>
		                					<c:otherwise>
		                						<img src="${record.teacher.zp}" class="img-circle" alt="User Image" width="45" height="45">
			                          			<div class="text-center f12">${record.teacher.xm}</div>
		                					</c:otherwise>
		                				</c:choose>
				                    </div>
				                    <div class="media-body pad-l15 f12">
				                      <div><fmt:formatDate value="${record.createdDt}" pattern="yyyy.MM.dd HH:mm"/></div>
				                      <div class="margin_t5">${record.content}</div>
				                      <div class="margin_t5">
				                        <a href="${record.attachment}">${record.attachmentName}</a>
				                      </div>
				                    </div>
				                  </div>
	                    	</c:forEach>
		                </dd>
		              </dl>
		          </c:forEach>
                </div>
              </div>
            </div>
          </div>
        </c:if>
        </div>
      </div>
    </div>
  </div>
</section>

<%@ include file="/WEB-INF/jsp/new/layouts/footer.jsp"%>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

<script>
$(".graduate-progress").each(function(index, el) {
  $("li",el).each(function(i, e) {
    var length=$('li',el).length;
    $(this).css('left',i*(100/(length-1))+"%");
    if($(this).is(".actived:last")){
      //$(".graduate-progress .bar").width($(this).position().left)
      $(".graduate-progress .bar").css('width',this.style.left);
    }
  });
});

var applyId = '${thesisApply.applyId}';
$('.confirmB').click(function(){
     $.confirm({
         title: '提示',
         content: '是否确认收到论文终稿？',
         confirmButton: '确认',
         icon: 'fa fa-warning',
         cancelButton: '取消',  
         confirmButtonClass: 'btn-primary',
         closeIcon: true,
         closeIconClass: 'fa fa-close',
         confirm: function () { 
        	 $.post("confirm",{applyId:applyId},function(data){
        	   		if(data.successful){
        	   			showMessage(data);
        	   			window.location.reload();
        	   		}else{
        	   			showMessage(data);
        	   		}
        	   },"json"); 
         } 
     });
 });
</script>
</body>
</html>
