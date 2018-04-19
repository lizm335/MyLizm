<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>

<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>

<script type="text/javascript">
function accept() {
	$.confirm({
        title: '提示',
        content: '确认受理？',
        confirmButton: '确认',
        icon: 'fa fa-warning',
        cancelButton: '取消',  
        confirmButtonClass: 'btn-primary',
        closeIcon: true,
        closeIconClass: 'fa fa-close',
        confirm: function () { 
          	 $.get("accept",{applyId:'${graduationRecord.applyId}'},function(data){
            		if(data.successful){
            			window.location.reload();
            		}else{
            			alert(data.message);
            		}
            },"json"); 
        	 //window.location.href = "${ctx}/graduation/record/accept?applyId=${graduationRecord.applyId}";
        } 
    });
}

function firstTrial() {
	$ .ajax({
		  type: "POST",
		  url: $('#trialForm').attr("action"),
		  data: $('#trialForm').serialize(),
		  dataType: "json",
		  cache: false,
		  success: function(data) {
			  if(data.successful){
      			window.location.reload();
	      	  }else{
	      		alert(data.message);
	      	  }
		  },
		  error: function() {
			  alert("请求超时！");
		  }
	});
}
</script>

</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">毕业管理</a></li>
		<li><a href="#">毕业学员管理</a></li>
		<li class="active">学员详情</li>
	</ol>
</section>

<section class="content">
	<div class="box">
    <div class="box-body">
      <div class="media pad">
        <div class="media-left" style="padding-right:25px;">
          <img src="${graduationRecord.gjtStudentInfo.avatar}" class="img-circle" style="width:112px;height:112px;" alt="User Image">
        </div>
        <div class="media-body">
          <h3 class="margin_t10">
            	${graduationRecord.gjtStudentInfo.xm}
            <small class="f14">(<c:if test="${graduationRecord.gjtStudentInfo.xbm == 1}">男</c:if><c:if test="${graduationRecord.gjtStudentInfo.xbm == 2}">女</c:if>)</small>
          </h3>
          <div class="row">
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>学号:</b> <span>${graduationRecord.gjtStudentInfo.xh}</span>
            </div>
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>层次:</b>
              <span>${trainingLevelMap[graduationRecord.gjtStudentInfo.pycc]}</span>
            </div>
            <div class="col-xs-6 col-sm-4 pad-b5">
              <b>专业:</b> <span>${specialtyMap[graduationRecord.gjtStudentInfo.gjtSpecialty.specialtyId]}</span>
            </div>

          </div>
        </div>
      </div>
      
    </div>
    <div class="box-footer">
      <div class="row stu-info-status">
        <div class="col-sm-3 col-xs-6">
          <div class="f24 text-center">${recordStatusMap[graduationRecord.isReceive]}</div>
          <div class="text-center gray6">毕业状态</div>
        </div>
        <div class="col-sm-3 col-xs-6">
          <div class="f24 text-center">
          	<c:choose>
				<c:when test="${graduationRecord.isReceive > 3}">
					<c:choose>
						<c:when test="${not empty graduationRecord.graduationCertificateUrl}">
							已上传
						</c:when>
						<c:otherwise>
							待上传
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					--
				</c:otherwise>
			</c:choose>
          </div>
          <div class="text-center gray6">电子证书状态</div>
        </div>
        <div class="col-sm-3 col-xs-6">
          <div class="f24 text-center">
          	<c:choose>
				<c:when test="${graduationRecord.isReceive == 4}">
					待领取
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${graduationRecord.isReceive == 5}">
							已领取
						</c:when>
						<c:otherwise>
							--
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
          </div>
          <div class="text-center gray6">毕业证书原件状态</div>
        </div>
        <div class="col-sm-3 col-xs-6">
          <div class="text-center table-block full-width" style="height:64px;">
            <div class="table-cell-block vertical-middle">
              <div class="row">
                <div class="col-md-6 col-sm-12">
                  <a class="btn btn-default btn-block btn-sm"  target="_blank"
                  	href="http://www.study.tt.ouchgzee.com/view/viewGraduationRegister.do?studentId=${graduationRecord.gjtStudentInfo.studentId}">
                    <i class="fa fa-eye f18 vertical-middle"></i>
                    	预览登记表
                  </a>
                </div>
                <c:if test="${graduationRecord.isReceive == 0}">
	                <div class="col-md-6 col-sm-12">
	                  <button class="btn btn-default btn-block btn-sm" onclick="accept()">
	                    <i class="fa fa-ksyy f18 vertical-middle"></i>
	                   		 受理申请
	                  </button>
	                </div>
                </c:if>
                <c:if test="${graduationRecord.isReceive == 1}">
	                <div class="col-md-6 col-sm-12">
	                  <button class="btn btn-default btn-block btn-sm" data-toggle="modal" data-target="#trialModal">
	                    <i class="fa fa-ksyy f18 vertical-middle"></i>
	                   		 审核
	                  </button>
	                </div>
                </c:if>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="nav-tabs-custom">
    <ul class="nav nav-tabs nav-tabs-lg">
      <li class="active"><a href="#tab_notice_1" data-toggle="tab">毕业资格</a></li>
      <c:if test="${graduationRecord.applyDegree == 1}">
      	<li><a href="#tab_notice_2" data-toggle="tab">学位状态</a></li>
      </c:if>
    </ul>
    <div class="tab-content">
      <div class="tab-pane active" id="tab_notice_1">
        <div class="panel panel-default">
          <div class="panel-heading">
            <h3 class="panel-title text-bold">学分状态</h3>
          </div>
          <div class="panel-body">
            <div class="table-responsive">
              <table class="table table-bordered table-hover vertical-mid table-font text-center margin-bottom-none">
                <thead>
                  <tr>
                    <th>模块</th>
                    <th>课程</th>
                    <th>课程学分</th>
                    <th>已获学分</th>
                    <th>模块获得学分</th>
                    <th>模块达标学分</th>
                    <th>达标状态</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="item" items="${moduleScoreMap}"> 
                  	<c:set var="totalScore" value="0"></c:set>
                  	<c:forEach var="moduleScore" items="${item.value}" varStatus="status">
                  		<c:if test="${moduleScore[3] >= 60}">
                  			<c:set var="totalScore" value="${totalScore + moduleScore[2]}"></c:set>
                  		</c:if>
                  	</c:forEach>
                  	<c:forEach var="moduleScore" items="${item.value}" varStatus="status">
                  		<c:choose>
                  			<c:when test="${status.first}">
                			  <tr>
			                    <td rowspan="${fn:length(item.value)}">${moduleScore[0]}</td>
			                    <td class="bg-fafafa">${moduleScore[1]}</td>
			                    <td class="bg-fafafa">${moduleScore[2]}</td>
			                    <td class="bg-fafafa">
			                    	<c:choose>
			                    		<c:when test="${moduleScore[3] >= 60}">
			                    			${moduleScore[2]}
			                    		</c:when>
			                    		<c:otherwise>0</c:otherwise>
			                    	</c:choose>
			                    </td>
			                    <td rowspan="${fn:length(item.value)}"> ${totalScore}</td>
			                    <td rowspan="${fn:length(item.value)}"> ${moduleScore[4]}</td>
		                    	<c:choose>
		                    		<c:when test="${empty moduleScore[4]}">
		                    			<td rowspan="${fn:length(item.value)}">--</td>
		                    		</c:when>
		                    		<c:when test="${totalScore >= moduleScore[4]}">
		                    			<td rowspan="${fn:length(item.value)}">已达标</td>
		                    		</c:when>
		                    		<c:otherwise>
		                    			<td rowspan="${fn:length(item.value)}" class="text-red">未达标</td>
		                    		</c:otherwise>
		                    	</c:choose>
			                  </tr>
                  			</c:when>
                  			<c:otherwise>
                  			  <tr>
			                    <td>${moduleScore[1]}</td>
			                    <td>${moduleScore[2]}</td>
			                    <td class="bg-fafafa">
			                    	<c:choose>
			                    		<c:when test="${moduleScore[3] >= 60}">
			                    			${moduleScore[2]}
			                    		</c:when>
			                    		<c:otherwise>0</c:otherwise>
			                    	</c:choose>
			                    </td>
			                  </tr>
                  			</c:otherwise>
                  		</c:choose>
                  	</c:forEach>  
				  </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
        </div>
        
        <c:if test="${graduationRecord.gjtStudentInfo.pycc eq 2 || graduationRecord.gjtStudentInfo.pycc eq 8}">
	        <div class="panel panel-default">
	            <div class="panel-heading">
	              <h3 class="panel-title text-bold">全国统考课程达标状态</h3>
	            </div>
	            <div class="panel-body">
	              <div class="table-responsive">
	                <table class="table table-bordered table-striped vertical-mid table-font text-center margin-bottom-none">
	                  <thead>
	                    <tr>
	                      <th>课程名称</th>
	                      <th>考试类型</th>
	                      <th>统考成绩</th>
	                      <th>状态</th>
	                    </tr>
	                  </thead>
	                  <tbody>
	                    <tr>
	                      <td>大学英语（B）</td>
	                      <td>全国统考</td>
	                      <td>${score1}</td>
	                      <c:choose>
	                    		<c:when test="${score1 >= 60}">
	                    			<td>已达标</td>
	                    		</c:when>
	                    		<c:otherwise>
	                    			<td class="text-red">未达标</td>
	                    		</c:otherwise>
	                    	</c:choose>
	                    </tr>
	                    <tr>
	                      <td>计算机应用基础（本）</td>
	                      <td>全国统考</td>
	                      <td>${score2}</td>
	                      <c:choose>
	                    		<c:when test="${score2 >= 60}">
	                    			<td>已达标</td>
	                    		</c:when>
	                    		<c:otherwise>
	                    			<td class="text-red">未达标</td>
	                    		</c:otherwise>
	                    	</c:choose>
	                    </tr>
	                  </tbody>
	                </table>
	              </div>
	            </div>
	        </div>
        </c:if>
      </div>
      
      <c:if test="${graduationRecord.applyDegree == 1}">
      <div class="tab-pane" id="tab_notice_2">
        <div class="custom-alert">
          <span class="f18">学位申请院校：</span>
          <span class="f24">${graduationRecord.gjtDegreeCollege.collegeName}</span>
        </div>
        <div class="panel panel-default">
          <div class="panel-heading">
            <h3 class="panel-title text-bold">
            	${graduationRecord.gjtDegreeCollege.collegeName}${specialtyMap[graduationRecord.gjtStudentInfo.gjtSpecialty.specialtyId]}专业学位申请条件
            </h3>
          </div>
          <div class="panel-body">
            <div class="approval-list clearfix">
            	<c:forEach var="degreeRequirement" items="${degreeRequirements}">
            		<c:choose>
            			<c:when test="${degreeRequirement.requirementType != 0}">
		            		<dl class="approval-item">
			                    <dt class="clearfix">
			                      <b class="a-tit gray6">${degreeRequirement.requirementName}≥${degreeRequirement.requirementParam}分</b>
			                      <c:choose>
			                      	<c:when test="${degreeRequirement.actualValue >= degreeRequirement.requirementParam}">
				                      <span class="fa fa-fw fa-check-circle text-green"></span>
				                      <label class="state-lb text-green">已达成(${degreeRequirement.actualValue})</label>
			                      	</c:when>
			                      	<c:otherwise>
					                    <span class="fa fa-fw fa-exclamation-circle text-red"></span>
					                    <label class="state-lb text-red">未达成(${degreeRequirement.actualValue})</label>
			                      	</c:otherwise>
			                      </c:choose>
			                    </dt>
			                </dl>
            			</c:when>
            			<c:otherwise>
		            		<dl class="approval-item">
			                    <dt class="clearfix">
			                      <b class="a-tit gray6">${degreeRequirement.requirementName}</b>
			                    </dt>
			                    <dd>
			                      <div class="panel panel-default margin_b10">
			                        <div class="panel-body">
			                          <div class="oh">
			                            	${degreeRequirement.requirementDesc}
			                          </div>
			                        </div>
			                      </div>
			                    </dd>
			                </dl>
            			</c:otherwise>
            		</c:choose>
            	</c:forEach>
            	
                <dl class="approval-item">
                    <dt class="clearfix">
                      <b class="a-tit gray6">学位英语成绩单</b>
                    </dt>
                    <dd>
                      <div class="panel panel-default margin_b10">
                      	<c:choose>
                      		<c:when test="${not empty graduationRecord.englishCertificateUrl}">
		                        <div class="panel-body">
		                          <span class="text-green pull-right margin_l15">已上传</span>
		                        </div>
		                        <div class="panel-footer">
		                          	附件：<a href="${graduationRecord.englishCertificateUrl}" target="_blank">学位英语成绩单</a>
		                        </div>
                      		</c:when>
                      		<c:otherwise>
		                        <div class="panel-body">
		                          <span class="text-red pull-right margin_l15">未上传</span>
		                        </div>
		                        <div class="panel-footer">
		                          	附件：无
		                        </div>
                      		</c:otherwise>
                      	</c:choose>
                      </div>
                    </dd>
                </dl>
            </div>
          </div>
        </div>
      </div>
      </c:if>
    </div>
  </div>
</section>

<div id="trialModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="trialLabel" aria-hidden="true"> 
 	<div class="modal-dialog">
 		<div class="modal-content">
     <div class="modal-header"> 
         <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
         <h4 class="modal-title" id="trialLabel">审核</h4>
     </div> 
     <div class="modal-body"> 
     	<form method="post" id="trialForm" class="form-horizontal" role="form" action="${ctx}/graduation/record/firstTrial">
			<input type="hidden" id="applyId" name="applyId" value="${graduationRecord.applyId}"/>
			<div class="box-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">毕业条件:</label>
					<div class="col-sm-8">
						<select id="graduationCondition" name="graduationCondition" class="selectpicker show-tick form-control" data-size="5">
							<option value="0">未达标</option>
							<option value="1" selected="selected">已达标</option>
						</select>
					</div>
				</div>
				<c:if test="${graduationRecord.applyDegree == 1}">
					<div class="form-group">
						<label class="col-sm-3 control-label">学位条件:</label>
						<div class="col-sm-8">
							<select id="degreeCondition" name="degreeCondition" class="selectpicker show-tick form-control" data-size="5">
								<option value="0">未达标</option>
								<option value="1" selected="selected">已达标</option>
							</select>
						</div>
					</div>
				</c:if>
				<div class="form-group">
					<label class="col-sm-3 control-label">初审结果:</label>
					<div class="col-sm-8">
						<select id="firstTrial" name="firstTrial" class="selectpicker show-tick form-control" data-size="5">
							<option value="0">未通过</option>
							<option value="1">通过</option>
							<option value="2" selected="selected">待定</option>
						</select>
					</div>
				</div>
			</div>
         </form>
     </div> 
     <div class="modal-footer"> 
     	<button type="button" onclick="firstTrial()" class="btn btn-success">确定</button>
        <a href="#" class="btn btn-default" data-dismiss="modal">关闭</a> 
     </div> 
     	</div>
     </div>
 </div>

</body>
</html>