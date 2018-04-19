<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/new/common/taglibs.jsp"%>

<!DOCTYPE html>
<html class="reset">
<head>
  <title>班主任平台 - 通知公告</title>
  <%@ include file="/WEB-INF/jsp/new/common/jslibs.jsp"%>
<style type="text/css">
html,body{
  height: 100%;
  overflow: hidden;
}
</style>
</head>
<body>
<div class="box no-border no-shadow margin-bottom-none">
  <div class="box-header with-border">
    <h3 class="box-title f16 text-bold">添加接收对象</h3>
  </div>
</div>
  <form action="addObject.html" method="get">
<div class="nav-tabs-custom no-shadow margin-bottom-none margin_t15">
  <ul class="nav nav-tabs my-tabs">
    <li class="active" style="margin-left:10px;"><a href="#tab_top_1">按学员</a></li>
    <li><a href="#tab_top_2">按班级</a></li>
  </ul>
  <div class="tab-content position-relative">
    <div class="tab-pane active" id="tab_top_1">
      <div class="text-right position-absolute" style="top:-43px;right:10px;">
        <select name="searchClassId" class="form-control inline-block margin_r10 vertical-middle" style="width:200px;">
        <c:forEach items="${classList }" var="classInfo">
          <option value="${classInfo.CLASS_ID }"  <c:if test="${classInfo.CLASS_ID eq searchClassId }">selected='selected'</c:if>>${classInfo.BJMC }</option>
        </c:forEach>
        </select>
        <input type="text" name="searchName" class="form-control margin_r10 inline-block vertical-middle"
         placeholder="请输入学员姓名" style="width:150px;" value="${searchName }">
        <button class="btn btn-primary vertical-middle">搜索</button>
      </div>
      <div class="distribut-teacher table-block full-width margin_t10">

        <div class="table-cell-block" style="width:40%;">
          <div class="panel panel-default">
            <!-- Default panel contents -->
            <div class="panel-heading text-center">
              <b>待添加学员（<span data-role="has-left">${studentSize }</span>/<span data-role="total">${studentSize }</span>）</b>
            </div>
            <div class="panel-body">
              <div class="scroll-sim">
                <ul class="list-unstyled select-group-list" data-role="all-person">
                	<c:forEach items="${studetnList }" var="map">
                		<li>
		                    <i class="fa fa-fw fa-arrow-circle-o-right" data-role="oper"></i>
		                    <div class="media">
		                      <div class="media-left">
		                      	<c:if test="${empty map.AVATAR }">
		                       	 <img src="${css}/ouchgzee_com/platform/xlbzr_css/dist/img/images/no-img.png" class="img-circle">
		                      	</c:if>
		                      	<c:if test="${not empty map.AVATAR }">
		                       	 <img src="${map.AVATAR }" class="img-circle">
		                      	</c:if>
		                      </div>
		                      <div class="media-body">
		                        <div class="name">${map.XM }</div>
		                        <div class="userIds" style="display: none;">${map.ID }</div>
		                        <p>${map.BJMC }</p>
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
            <button class="btn btn-primary" data-role="add" type="button"><span class="pad">添加全部&gt;</span></button>
          </div>
          <div class="form-group margin-bottom-none">
            <button class="btn btn-danger" data-role="remove" type="button"><span class="pad">删除全部&gt;</span></button>
          </div>
        </div>
        <div class="table-cell-block" style="width:40%;">
          <div class="panel panel-default">
            <!-- Default panel contents -->
            <div class="panel-heading text-center">
              <b>已添加学员（<span data-role="has-sel">0</span>）</b>
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
    <div class="tab-pane" id="tab_top_2">
    <!--   <div class="text-right position-absolute" style="top:-43px;right:10px;">
        <input type="text" class="form-control margin_r10 inline-block vertical-middle" placeholder="请输入班级名称" style="width:150px;">
        <button class="btn btn-primary vertical-middle">搜索</button>
      </div> -->
      <div class="distribut-teacher table-block full-width margin_t10">

        <div class="table-cell-block" style="width:40%;">
          <div class="panel panel-default">
            <!-- Default panel contents -->
            <div class="panel-heading text-center">
              <b>待添加班级（<span data-role="has-left">${classSize }</span>/<span data-role="total">${classSize }</span>）</b>
            </div>
            <div class="panel-body">
              <div class="scroll-sim">
                <ul class="list-unstyled select-group-list" data-role="all-person">
                <c:forEach items="${classList }" var="classInfo">
                  <li>
                    <i class="fa fa-fw fa-arrow-circle-o-right" data-role="oper"></i>
                    <div class="media">
                      <div class="media-body">
                        <div class="name">${classInfo.BJMC }</div>
                        <div class="classIds" style="display: none;">${classInfo.CLASS_ID }</div>
                        <p>班级人数:${classInfo.CLASSCOUNT }人</p>
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
            <button class="btn btn-primary" data-role="add" type="button"><span class="pad">添加全部&gt;</span></button>
          </div>
          <div class="form-group margin-bottom-none">
            <button class="btn btn-danger" data-role="remove" type="button"><span class="pad">删除全部&gt;</span></button>
          </div>
        </div>
        <div class="table-cell-block" style="width:40%;">
          <div class="panel panel-default">
            <!-- Default panel contents -->
            <div class="panel-heading text-center">
              <b>已添加班级（<span data-role="has-sel">0</span>）</b>
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
  </div>
</div>
</form>
<div class="dialog-btn-box affix full-width" style="bottom:8px;left:0;margin-top:0;">
  <span class="pull-left pad-t10 gray6 caculate-txt">已选择 <b class="text-light-blue" data-role="has-sel">0</b> 个接收人</span>
  <span class="pull-left pad-t10 gray6 caculate-txt" style="display:none;">已选择 <b class="text-light-blue" data-role="has-sel">0</b> 个班级</span>
  <button class="btn btn-default min-width-90px btn-cancel-edit margin_r15">取消</button>
  <button class="btn btn-primary min-width-90px  btn-sure-edit">确定</button>
</div>

<%@ include file="/WEB-INF/jsp/new/common/footerjslibs.jsp"%>

<script type="text/javascript">

$('.scroll-sim').slimScroll({
    height: '270px',
    size: '5px',
    alwaysVisible:true
});

// 标签页
$('.my-tabs a').click(function (e) {
  e.preventDefault();
  var index=$(this).parent().index();
  $(this).tab('show');

  $(".caculate-txt").hide().eq(index).show();
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

/*取消*/
$(".btn-cancel-edit").click(function(event) {
  parent.$("#add-person-iframe").closest('.jconfirm-box').find('.closeIcon').click();
});

/*确认*/
$(".btn-sure-edit").click(function(event) {
  /*插入业务逻辑*/
  var $liCollectors=$("ul[data-role='receive-person']:visible").children('li');
  var $container=parent.$('.stu-list-box');
  var html='<li>'
            +'<span>{name}<input type="hidden" name="userIds" value="{userId}"/> '
            +'<input type="hidden" name="classIds" value="{classId}"/></span>'
            +'<i class="fa fa-fw fa-remove" data-toggle="tooltip" title="删除"></i>'
           +'</li>';
  $liCollectors.each(function(index, el) {
    var tmp=html;
    tmp=tmp.replace('{name}',$(this).find(".name").text());
    tmp=tmp.replace('{userId}',$(this).find(".userIds").text());
    tmp=tmp.replace('{classId}',$(this).find(".classIds").text());
    $container.append(tmp);
  });
  
  parent.checkAddPer();

  $(".btn-cancel-edit").click();
});
</script>
</body>
</html>
