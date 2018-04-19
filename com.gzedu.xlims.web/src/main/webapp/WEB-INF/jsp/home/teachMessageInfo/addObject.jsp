<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>管理系统</title>
<style type="text/css">
html,body{
  height: 100%;
  overflow: hidden;
}

.select-group-list[data-role="receive-person"] .fa:before {
  content: "\f00d";
}
.select-group-list .media {
  margin-top: 0;
}
.select-group-list .media .media-body p {
  font-size: 12px;
  color: #999;
  margin-bottom: 0;
}
.select-group-list .fa {
  float: right;
  font-size: 24px;
  margin-top: 7px;
  cursor: pointer;
  visibility: hidden;
}
.select-group-list img {
  width: 36px;
  height: 36px;
}
.select-group-list li {
  padding: 5px 10px;
  overflow: hidden;
  margin: 5px 0;
}
.select-group-list li:hover {
  background-color: #3c8dbc;
  color: #fff;
}
.select-group-list li:hover .media-body p {
  color: #fff;
}
.select-group-list li:hover .fa {
  visibility: visible;
}
</style>
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/jslibs_new.jsp"%>
<div class="box no-border no-shadow">
	<div class="box-header with-border">
		<h3 class="box-title">添加接收对象</h3>
	</div>
	<form action="${ctx }/admin/teachMessageInfo/addObject">
	<div class="box-body">
		<div class="pad">
	          <select name="gradeId" class="form-control inline-block margin_r10 vertical-middle select2" 
	           style="width:200px;"	data-size="5" data-live-search="true">
                  <option value="" selected="selected">选择学期</option>
                  <c:forEach items="${gradeMap}" var="map">
                      <option value="${map.key}" <c:if test="${map.key == gradeId}">selected='selected'</c:if>>${map.value}</option>
                  </c:forEach>
              </select>
	        <input type="text" name="courseName" class="form-control margin_r10 inline-block vertical-middle" 
	        	placeholder="请输入课程名称" style="width:150px;" value="${courseName }">
	        <button class="btn btn-primary vertical-middle">搜索</button>
	    </div>
	    <div class="distribut-teacher table-block full-width no-margin margin_t10">
	        <div class="table-cell-block" style="width:40%;">
	          <div class="panel panel-default">
	            <!-- Default panel contents -->
	            <div class="panel-heading text-center">
	              <b>待添加课程（<span data-role="has-left">${listSize }</span>/<span data-role="total">${listSize }</span>）</b>
	            </div>
	            <div class="panel-body">
	              <div class="scroll-sim">
	                <ul class="list-unstyled select-group-list" data-role="all-person">
	                	<c:forEach items="${list }" var="item">
		                  <li>
		                    <i class="fa fa-fw fa-arrow-circle-o-right" data-role="oper"></i>
		                    <div class="media">
		                      <div class="media-left">
		                        <img src="${css }/ouchgzee_com/platform/xlbzr_css/dist/img/images/no-img.png" class="img-circle">
		                      </div>
		                      <div class="media-body">
		                        <div class="name">${item.GRADE_NAME }${item.KCMC }</div>
		                         <div class="gradeIds" style="display: none;">${item.TERM_ID }</div>
		                         <div class="courseIds" style="display: none;">${item.COURSE_ID }</div>
		                        <p>${item.NUM }人选课</p>
		                      </div>
		                    </div>
		                  </li>
	                </c:forEach>
	                </ul>
	              </div>
	            </div>
	          </div>
	        </div>
	        <div class="table-cell-block vertical-middle text-center">
	          <div class="form-group">
	            <button class="btn btn-primary" data-role="add"><span class="pad">添加全部&gt;</span></button>
	          </div>
	          <div class="form-group margin-bottom-none">
	            <button class="btn btn-danger" data-role="remove"><span class="pad">删除全部&gt;</span></button>
	          </div>
	        </div>
	        <div class="table-cell-block" style="width:40%;">
	          <div class="panel panel-default">
	            <!-- Default panel contents -->
	            <div class="panel-heading text-center">
	              <b>已添加课程（<span data-role="has-sel">0</span>）</b>
	            </div>
	            <div class="panel-body">
	              <div class="scroll-sim">
	                <ul class="list-unstyled select-group-list" data-role="receive-person">
	                </ul>
	              </div>
	            </div>
	          </div>
	        </div>
	    </div>
	</div>
	</form>
</div>


<div class="text-right pop-btn-box pad clearfix">
	<span class="pull-left pad-t10 gray6 caculate-txt">已添加 <b class="text-light-blue" data-role="has-sel">1</b> 个课程</span>
  	<span class="pull-left pad-t10 gray6 caculate-txt" style="display:none;">已选择 <b class="text-light-blue" data-role="has-sel">1</b> 个班级</span>

	<button type="button" class="btn btn-default min-width-90px" data-role="close-pop">取消</button>
	<button type="button" class="btn btn-success min-width-90px margin_l15" data-role="btn-sure-add">确定</button>
</div>

<script type="text/javascript">
$(".select2").select2({language: "zh-CN"});

$('.scroll-sim').slimScroll({
    height: '270px',
    size: '5px',
    alwaysVisible:true
});

//关闭 弹窗
$("button[data-role='close-pop']").click(function(event) {
	parent.$.closeDialog(frameElement.api);
});

//确定
$('[data-role="btn-sure-add"]').click(function(event) {
  /*插入业务逻辑*/
  var $liCollectors=$("ul[data-role='receive-person']:visible").children('li');
  var $container=parent.$('.stu-list-box');
  var html='<li>'
            +'<span>{name}<input type="hidden" name="courseIds" value="{courseId}"/>'
            +'<input type="hidden" name="gradeIds" value="{gradeId}"/></span>'
            +'<i class="fa fa-fw fa-remove" data-toggle="tooltip" title="删除"></i>'
           +'</li>';
  var r=[];
  $liCollectors.each(function(index, el) {
    var tmp=html;
    tmp=tmp.replace('{name}',$(this).find(".name").text());
    tmp=tmp.replace('{courseId}',$(this).find(".courseIds").text());
    tmp=tmp.replace('{gradeId}',$(this).find(".gradeIds").text());
    r.push(tmp);
  });

  if(r.length>0){
  	$container.append(r.join(''));
  }

  parent.$.closeDialog(frameElement.api);
});

$(".distribut-teacher")
//添加全部
.on("click",".btn[data-role='add']",function(){
  var $containerLeft=$("ul[data-role='all-person']:visible");
  var $containerRight=$("ul[data-role='receive-person']:visible");

  $containerRight.prepend(
    $containerLeft.children('li')
  );

  countInfo();
})
//移除全部
.on("click",".btn[data-role='remove']",function(){
  var $containerLeft=$("ul[data-role='all-person']:visible");
  var $containerRight=$("ul[data-role='receive-person']:visible");

  $containerLeft.prepend(
    $containerRight.children('li')
  );

  countInfo();
})
//添加/删除单个元素
.on('click', '[data-role="oper"]', function(event) {
  event.preventDefault();
  var $containerLeft=$("ul[data-role='all-person']:visible");
  var $containerRight=$("ul[data-role='receive-person']:visible");
  var $ul=$(this).closest('ul');
  var $li=$(this).closest('li');
  //添加
  if( $ul.attr('data-role')=='all-person' ){
    $containerRight.prepend($li);
  }
  //删除
  else{
    $containerLeft.prepend($li);
  }
  countInfo();
});

//统计数目
function countInfo(){
  var $containerLeft=$("ul[data-role='all-person']:visible");
  var $containerRight=$("ul[data-role='receive-person']:visible");

  var $left=$('[data-role="has-left"]:visible');
  var $sel=$('[data-role="has-sel"]:visible');

  $left.text( $containerLeft.children('li').length );
  $sel.text( $containerRight.children('li').length );
}


</script>

</body>
</html>

