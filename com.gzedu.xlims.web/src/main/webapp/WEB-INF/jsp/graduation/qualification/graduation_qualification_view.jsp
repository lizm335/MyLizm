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
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">毕业管理</a></li>
    <li><a href="#">资格查询</a></li>
		<li class="active">学员申请详情</li>
	</ol>
</section>
<section class="content">
	<div class="box">
    <div class="box-body">
      <div class="row">
        <div class="col-sm-4">
          <div class="media pad">
            <div class="media-left" style="padding-right:25px;">
             
             <c:if test="${empty info.photoUrl }">
              <img src="http://172.16.170.119:801/ouchgzee_com/platform/xllms_css/dist/img/images/no-img.png" class="img-circle" width="112" height="112" alt="User Image">
              <div class="gray9 text-center f12">未上传新华社照片</div>
              </c:if>
              
              <c:if test="${not empty info.photoUrl }">
              	 <img src="${info.photoUrl }" class="img-circle" width="112" height="112" alt="User Image">
              </c:if>
              
            </div>
            <div class="media-body">
              <h3 class="margin_t10">
                	${info.gjtStudentInfo.xm }
              </h3>
              
              <ul class="list-unstyled">
                <li>学号：${info.gjtStudentInfo.xh }</li>
                <li>学期：${info.gjtStudentInfo.gjtGrade.gradeName}</li>
                <li>层次：${pyccMap[info.gjtStudentInfo.pycc]}</li>
                <li>专业：${info.gjtStudentInfo.gjtSpecialty.zymc} <small class="gray9">（${info.gjtStudentInfo.gjtSpecialty.ruleCode}）</small> </li>
              </ul>
              
            </div>
          </div>
        </div>
        <div class="col-sm-8">
            <table class="table no-border vertical-middle no-margin text-center margin_t20" height="120" style="table-layout: fixed;">
              <tbody>
                <tr>
                  <td width="" style="border-left:1px solid #e5e5e5">
                  	<c:choose>
                  		<c:when test="${info.auditState eq '11' }">
                  			<div class="f20 text-green">已满足</div>
                  			<!-- <div class="gray9">（2016年10月，已毕业）</div> -->
                  		</c:when>
                  		<c:when test="${info.auditState eq '1' }">
                  			<div class="text-orange">已满足</div>
		            		<div class="gray9">（未申请毕业）</div>
                  		</c:when>
                  		<c:when test="${info.auditState eq '6' }">
                  			<div class="text-orange">已满足</div>
		            		<div class="gray9">（已申请毕业）</div>
                  		</c:when>
                  		<c:when test="${info.auditState eq '2' or info.auditState eq '12'}">
                  			<div class="text-red">不满足</div>
                  		</c:when>
                  	</c:choose>
                    <div class="gray6 margin_t5" style="line-height:1.2">是否满足毕业申请条件</div>
                  </td>
                  <td width="33%" style="border-left:1px solid #e5e5e5">
                  <c:if test="${info.applyDegree eq '0' }">
           				<div class="f20 text-orange">未申请学位</div>
           			</c:if>
           			<c:if test="${info.applyDegree eq '1' }">
           				<c:choose>
            				<c:when test="${not empty info.eleRegistrationNumber }">
            					<div class="text-green">已获得学位</div>
            				</c:when>
            				<c:otherwise>
            					<div class="text-green">已申请学位</div>
            				</c:otherwise>
            			</c:choose>
           			</c:if>
                   <!--  <div class="f20 text-green">已获得学位</div>
                    <div class="gray9">（2016年10月，东北财经大学）</div> -->
                    <div class="gray6 margin_t5" style="line-height:1.2">是否申请学位</div>
                  </td>
                  <td width="33%" style="border-left:1px solid #e5e5e5">
                    <div class="f18" style="word-break: break-all;line-height:1.2">${info.eleRegistrationNumber }</div>
                    <div class="gray6 margin_t5">电子注册号</div>
                  </td>
                </tr>
              </tbody>
            </table>
        </div>
      </div>
    </div>
  </div>

  <div class="box no-border margin-bottom-none">
    <div class="nav-tabs-custom no-margin">
      <ul class="nav nav-tabs nav-tabs-lg">
        <li class="active"><a href="#tab_notice_1" data-toggle="tab">毕业条件查询</a></li>
        <li ><a href="#tab_notice_2" data-toggle="tab">学位条件查询</a></li>
      </ul>
      <div class="tab-content">
        <div class="tab-pane active" id="tab_notice_1">
            <h3 class="cnt-box-title f16 margin_b10">专业规则要求</h3>
            <table class="table-gray-th table-font text-center vertical-middle">
              <tbody>
                <th>最低毕业学分</th>
                <td>71</td>

                <th>已通过学分</th>
                <td>
                    ${totalCredits}
                   <c:choose>
                    	<c:when test="${totalCredits>=specialty.zdbyxf }">
                    		<div class="text-green">（已达标）</div>
                    	</c:when>
                    	<c:otherwise>
                    		<div class="text-red">（未达标）</div>
                    	</c:otherwise>
                    </c:choose>
                </td>

                <th>中央电大考试学分</th>
                <td>${specialty.zyddksxf}</td>
                <th>已通过中央电大考试学分</th>
                <td>
                    ${centerCredits}
                    <c:choose>
                    <c:when test="${centerCredits>=specialty.zyddksxf }">
                    		<div class="text-green">（已达标）</div>
                    	</c:when>
                    	<c:otherwise>
                    		<div class="text-red">（未达标）</div>
                    	</c:otherwise>
                    </c:choose>
                </td>
              </tbody>
            </table>

            <h3 class="cnt-box-title f16 margin_b10 margin_t20">成绩明细</h3>
            <table class="table table-bordered vertical-mid text-center margin-bottom-none table-font">
              <thead class="with-bg-gray">
                <tr>
                  <th>课程模块</th>
                  <th>模块<br>最低学分</th>
                  <th>模块<br>中央最低学分</th>
                  <th>模块<br>已通过学分</th>
                  <th>课程代码</th>
                  <th>课程名称</th>
                  <th>课程性质</th>
                  <th>课程类型</th>
                  <th>学分</th>
                  <th>开设学期</th>
                  <th>考试单位</th>
                  <th>成绩</th>
                  <th>是否达标<br>毕业条件</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach items="${modelList}" var="item">
              		<c:forEach items="${item.achieveList}" var="a" varStatus="i">
              			<tr>
	              			<c:if test="${i.index==0}">
	              				 <td rowspan="${fn:length(item.achieveList)}">${item.modelName}</td>
	              				 <td rowspan="${fn:length(item.achieveList)}">${item.totalscore}</td>
	              				 <td rowspan="${fn:length(item.achieveList)}">${item.crtvuScore}</td>
	              				 <td rowspan="${fn:length(item.achieveList)}">${item.getCredits}</td>
	              			</c:if>
	              			<td>${a.courseCode}</td>
	              			<td>${a.courseName}</td>
	              			<td>
	              				<c:if test="${a.courseNature=='0'}">必修</c:if> 
								<c:if test="${a.courseNature=='1'}">选修</c:if> 
								<c:if test="${a.courseNature=='2'}">补修</c:if>
	              			</td>
	              			<td>${a.courseType=="0"?"统设":"非统设"}</td>
	              			<td>${a.courseScore}</td>
	              			<td>${a.term}</td>
	              			<td>${a.examUnit}</td>
	              			<td>${a.examScore}</td>
	              			<c:if test="${i.index==0}">
	              				<c:choose>
	              					<c:when test="${item.getCredits>=item.totalscore}">
	              						<td rowspan="${fn:length(item.achieveList)}">
	              							<span class="text-green">已达标</span>
	              						</td>
	              					</c:when>
	              					<c:otherwise>
	              						<td rowspan="${fn:length(item.achieveList)}">
	              							<span class="text-red">未达标</span>
	              						</td>
	              					</c:otherwise>
	              				</c:choose>
	              			</c:if>
              			</tr>
              		</c:forEach>
              	</c:forEach>
              </tbody>
            </table>

            <h3 class="cnt-box-title f16 margin_b10 margin_t20">毕业申请条件</h3>
            <table class="table table-striped table-bordered vertical-mid text-center margin-bottom-none">
              <tbody>
                <tr>
                  <td>1</td>
                  <td>学籍期内</td>
                  <td>学籍有效期内</td>
                  <td class="text-green">已达标</td>
                </tr>
                <tr>
                  <td>2</td>
                  <td>学分要求</td>
                  <td>取得教学计划规定的最低毕业总学分71分</td>
                  <td >
               		<c:if test="${totalCredits>=specialty.zdbyxf }">
                		<span class="text-green">已达标</span>	
               		</c:if>
               		<c:if test="${totalCredits<specialty.zdbyxf }">
                		<span class="text-red">未达标</span>	
               		</c:if>
                  </td>
                </tr>
                <tr>
                  <td>3</td>
                  <td>最短年限</td>
                  <td>学生必须达到最短学习年限2.5年</td>
                  <td class="text-green">已达标</td>
                </tr>
                <tr>
                  <td>4</td>
                  <td>思想品德</td>
                  <td>思想品德经鉴定符合要求</td>
                  <td class="text-green">已达标</td>
                </tr>
              </tbody>
            </table>
        </div>
        <div class="tab-pane " id="tab_notice_2">
          <div class="box box-border">
            <div class="box-header with-border">
              <h3 class="box-title pad-t5">申请学位的专业：工商管理</h3>
            </div>
            <div style="margin:-1px;">
              <table class="table table-bordered table-cell-ver-mid text-center margin-bottom-none">
                <thead class="table-gray-th">
                  <th width="8%">序号</th>
                  <th width="15%">学位要求项</th>
                  <th>学位要求描述</th>
                  <th width="19%">状态</th>
                </thead>
                <tbody>
                  <tr>
                    <td>1</td>
                    <td>必修课程平均成绩</td>
                    <td>
                      <div class="text-left">
                        完成本科开放教育教学计划规定的教学内容，修满教学计划规定的学分，经审核准予毕业，获得毕业资格，并且统设必修课程平均成绩达到75分以上（含75分）
                      </div>
                    </td>
                    <td>
                      <div class="text-green">--</div>
                    </td>
                  </tr>
                  <tr>
                    <td>2</td>
                    <td>其它课程平均成绩</td>
                    <td>
                      <div class="text-left">
                        其他课程平均成绩达到70分以上（含70分），免修、免考课程视同成绩达标。
                      </div>
                    </td>
                    <td>
                      <div class="text-green">--</div>
                    </td>
                  </tr>
                  <tr>
                    <td>3</td>
                    <td>学位英语要求</td>
                    <td>
                      <div class="text-left">
                        （1）通过东北财经大学工商管理专业开放教育学士学位英语水平考试。<br>
                        （2）或申请学位前四年内通过国家大学英语四级考试（含其他语种）。
                      </div>
                    </td>
                    <td>
                      <div class="text-orange">--</div>
                    </td>
                  </tr>
                  <tr>
                    <td>4</td>
                    <td>学位论文指南学分费用</td>
                    <td>
                      <div class="text-left">
                        学位论文指南学分费：380 元
                      </div>
                    </td>
                    <td>
                      <div class="text-green">--</div>
                    </td>
                  </tr>
                  <tr>
                    <td>5</td>
                    <td>毕业设计（论文）要求</td>
                    <td>
                      <div class="text-left">
                        要求：良以上（含良）（80分）
                      </div>
                    </td>
                    <td>
                      <div class="text-orange">--</div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>


	<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
	<script type="text/javascript">
	</script>
</body>
</html>
