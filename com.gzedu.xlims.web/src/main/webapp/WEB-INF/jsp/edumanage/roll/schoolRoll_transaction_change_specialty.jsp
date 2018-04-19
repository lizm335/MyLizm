<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE html>
<html class="reset">
<head>
 <meta charset="utf-8">
 <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>学习异动明细</title>
<%@ include file="/WEB-INF/jsp/common/jslibs.jsp"%>
<link rel="stylesheet" href="${ctx}/static/dist/css/signup-info.css">
</head>
<body class="inner-page-body">

<section class="content-header">
	<button class="btn btn-default btn-sm pull-right min-width-60px" data-role="back-off">返回</button>
	<ol class="breadcrumb oh">
		<li><a href="homepage.html"><i class="fa fa-home"></i> 首页</a></li>
		<li><a href="#">学籍管理</a></li>
		<li><a href="#">学籍资料</a></li>
		<li class="active">学籍资料明细</li>
	</ol>
</section>
<section class="content">
	<div class="box">
    <div class="box-body">
      <div class="media pad">
        <div class="media-left" style="padding-right:25px;">
          <img src="http://172.16.170.119:801/ouchgzee_com/platform/xllms_css/dist/img/user7-128x128.jpg" class="img-circle" width="112" height="112" alt="User Image">
          <a href="#" class="btn btn-xs btn-default bg-white no-shadow btn-block margin_t5">
            <i class="fa fa-ee-online f24 vertical-middle text-green position-relative" style="top: -2px;"></i>
            <span class="gray9">
            交流
            </span>
          </a>
        </div>
        <div class="media-body">
          <h3 class="margin_t10">
            夏 艳
            <small class="f14">(女)</small>
          </h3>
          <div class="row">
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>学号:</b> <span>44018977901</span>
            </div>
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>手机:</b>
              <span>18928871190</span>
            </div>
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>层次:</b> <span>高起专</span>
            </div>
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>年级:</b> <span>2015</span>
            </div>
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>学期:</b> <span>2015春</span>
            </div>
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>学习中心:</b> <span>麓湖学习中心</span>
            </div>
            <div class="col-xs-6 col-md-4 pad-b5">
              <b>专业:</b> <span>计算机网络技术（网络管理）</span>
            </div>
          </div>
        </div>
      </div>
      
    </div>
    <div class="box-footer">
      <div class="row stu-info-status">
        <div class="col-sm-2_5 col-xs-6">
          <div class="f24 text-center">在籍</div>
          <div class="text-center gray6">学籍状态</div>
        </div>
        <div class="col-sm-2_5 col-xs-6">
          <div class="f24 text-center">正式学员</div>
          <div class="text-center gray6">学员类型</div>
        </div>
        <div class="col-sm-2_5 col-xs-6">
          <div class="f24 text-center">转专业</div>
          <div class="text-center gray6">异动类型</div>
        </div>
        <div class="col-sm-2_5 col-xs-6 no-margin no-pad-top">
          <div class="f24 text-center text-orange line-height-1em">
            待审核
            <div class="gray9 f14">（学习中心）</div>
          </div>
          <div class="text-center gray6">异动状态</div>
        </div>
        <div class="col-sm-2_5 col-xs-6 text-center">
          <button class="btn btn-default btn-sm margin_t10">
            <i class="fa fa-simulated-login f20 vertical-middle"></i>
            模拟登录
          </button>
        </div>
      </div>
    </div>
  </div>

  <div class="box margin-bottom-none">
    <div class="box-body">
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-1" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">转专业</span>
          </h3>
        </div>
        <div id="info-box-1" class="collapse in">
          <div class="panel-body content-group">
            <div class="cnt-box-body">
              <table width="100%">
                <tbody>
                  <tr>
                    <td width="46%">
                      <div class="panel panel-default margin-bottom-none">
                        <div class="panel-heading">
                          <h3 class="panel-title">
                              现读专业
                          </h3>
                        </div>
                        <table class="table table-bordered">
                          <tbody>
                            <tr>
                              <td class="pad-l15 pad-r15">层次：本科</td>
                            </tr>
                            <tr>
                              <td class="pad-l15 pad-r15">年级：2016秋</td>
                            </tr>
                            <tr>
                              <td class="pad-l15 pad-r15">班号：27362673722</td>
                            </tr>
                            <tr>
                              <td class="pad-l15 pad-r15">专业：工商管理</td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </td>
                    <td class="text-center">
                      <i class="fa fa-fw fa-angle-double-right f24"></i>
                    </td>

                    <td width="46%">
                      <div class="panel panel-default margin-bottom-none">
                        <div class="panel-heading">
                          <h3 class="panel-title">
                              需转专业
                          </h3>
                        </div>
                        <table class="table table-bordered">
                          <tbody>
                            <tr>
                              <td class="pad-l15 pad-r15">层次：本科</td>
                            </tr>
                            <tr>
                              <td class="pad-l15 pad-r15">年级：2016秋</td>
                            </tr>
                            <tr>
                              <td class="pad-l15 pad-r15">班号：27362673722</td>
                            </tr>
                            <tr>
                              <td class="pad-l15 pad-r15">专业：行政管理</td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>

              <table width="100%">
                <tbody>
                  <tr>
                    <td width="46%" valign="top">
                      <h3 class="f16">现专业课程：</h3>
                      <table class="table table-bordered table-font vertical-middle text-center">
                        <thead class="with-bg-gray">
                          <tr>
                            <th>学期</th>
                            <th>课程模块</th>
                            <th>课程名称</th>
                            <th>学习进度</th>
                            <th>学习成绩</th>
                            <th>考试成绩</th>
                            <th>总成绩</th>
                            <th>成绩状态</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td rowspan="2">
                              第一学期
                            </td>
                            <td>公共基础课</td>
                            <td>
                              学习指南
                              <div class="gray9">（KC00001）</div>
                            </td>
                            <td>20%</td>
                            <td>20</td>
                            <td>--</td>
                            <td>--</td>
                            <td class="text-orange">学习中</td>
                          </tr>
                          <tr>
                            <td>公共基础课</td>
                            <td>
                              职业英语（专）
                              <div class="gray9">（KC00002）</div>
                            </td>
                            <td>100%</td>
                            <td>80</td>
                            <td>80</td>
                            <td>80</td>
                            <td class="text-green">已通过</td>
                          </tr>

                          <tr>
                            <td rowspan="2">
                              第二学期
                            </td>
                            <td>通识课</td>
                            <td>
                              中国特色社会主义理论体系概论
                              <div class="gray9">（KC00003）</div>
                            </td>
                            <td>100%</td>
                            <td>80</td>
                            <td>80</td>
                            <td>80</td>
                            <td class="text-green">已通过</td>
                          </tr>
                          <tr>
                            <td>专业课</td>
                            <td>
                              管理学基础
                              <div class="gray9">（KC00004）</div>
                            </td>
                            <td>--</td>
                            <td>--</td>
                            <td>--</td>
                            <td>--</td>
                            <td class="gray9">未学习</td>
                          </tr>
                        </tbody>
                      </table>
                      
                    </td>
                    <td class="text-center">
                      <i class="fa fa-fw fa-angle-double-right f24"></i>
                    </td>

                    <td width="46%" valign="top">
                      <h3 class="f16">转专业后课程：</h3>
                      <table class="table table-bordered table-font vertical-middle text-center">
                        <thead class="with-bg-gray">
                          <tr>
                            <th>学期</th>
                            <th>课程模块</th>
                            <th>课程名称</th>
                            <th>继承原专业课程</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td rowspan="2">
                              第一学期
                            </td>
                            <td>通识课</td>
                            <td>
                              中国特色社会主义理论体系概论
                              <div class="gray9">（KC00003）</div>
                            </td>
                            <td>
                              <a class="text-blue" href="#">
                                中国特色社会主义理论体系概论
                                <div>（KC00003）</div>
                              </a>
                            </td>
                          </tr>
                          <tr>
                            <td>公共基础课</td>
                            <td>
                              职业英语（专）
                              <div class="gray9">（KC00002）</div>
                            </td>
                            <td>
                              <a class="text-blue" href="#">
                                职业英语（专）
                                <div>（KC00002）</div>
                              </a>
                            </td>
                          </tr>

                          <tr>
                            <td rowspan="2">
                              第二学期
                            </td>
                            <td>公共基础课</td>
                            <td>
                              学习指南
                              <div class="gray9">（KC00001）</div>
                            </td>
                            <td>
                              <a class="text-blue" href="#">
                                +选择继承原专业课程
                              </a>
                            </td>
                          </tr>
                          <tr>
                            <td>专业课</td>
                            <td>
                              管理学
                              <div class="gray9">（KC00044）</div>
                            </td>
                            <td>
                              <a class="text-blue" href="#">
                                +选择继承原专业课程
                              </a>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                      
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-2" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">转专业理由</span>
          </h3>
        </div>
        <div id="info-box-2" class="collapse in">
          <form class="theform">
            <div class="panel-body content-group overlay-wrapper position-relative">
              <div class="cnt-box-body">
                <div class="row">
                  <div class="col-sm-4">
                    <div class="bg-primary pad15 text-center">其它原因</div>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-3" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">签名确认</span>
          </h3>
        </div>
        <div id="info-box-3" class="collapse in">
          <div class="panel-body content-group">
            <div class="cnt-box-body">
              <div class="text-center pad15">
                显示签名图片
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="panel panel-default margin_t10">
        <div class="panel-heading">
          <h3 class="panel-title text-bold" data-toggle="collapse" data-target="#info-box-4" role="button"> 
              <i class="fa fa-fw fa-chevron-circle-down reset-fa-chevron-circle"></i>
              <span class="margin-r-5">审核流程</span>
          </h3>
        </div>
        <div id="info-box-4" class="collapse in">
          <div class="panel-body">
            <div class="approval-list clearfix">
              <dl class="approval-item">
                    <dt class="clearfix">
                      <b class="a-tit gray6">李志斌（学员）</b>
                      <span class="gray9 text-no-bold f12">2016-10-29 10:30</span>
                      <span class="fa fa-fw fa-dot-circle-o text-light-blue"></span>
                      <label class="state-lb">学员申请</label>
                    </dt>
                </dl>
                <dl class="approval-item">
                    <dt class="clearfix">
                      
                      <b class="a-tit gray6">学习中心审批</b>
                      <span class="fa fa-fw fa-check-circle text-green"></span>
                      <label class="state-lb text-green">审核通过</label>
                    </dt>
                    <dd>
                      <div class="txt">
                          <div class="marin_b10">同意</div>
                          <div class="text-right gray9">
                            审核人：学习中心管理员<br>
                            2017-05-27
                          </div>
                          
                      </div>
                    </dd>
                </dl>
                <dl class="approval-item">
                    <dt class="clearfix">
                      <b class="a-tit gray6">学籍科审批</b>
                      <span class="fa fa-fw fa-dot-circle-o text-orange"></span>
                      <label class="state-lb text-orange">待审核</label>
                    </dt>
                    <dd>
                      <div class="txt">
                          <table class="table no-border margin-bottom-none">
                            <tr>
                              <td width="110">审核意见：</td>
                              <td>
                                <textarea class="form-control" rows="5" placeholder="请输入审核意见" datatype="*" nullmsg="请输入审核意见" errormsg="请输入审核意见"></textarea>
                              </td>
                            </tr>
                            <tr>
                              <td>
                                <div class="pad-t5">
                                上传凭证：
                                </div>
                              </td>
                              <td>
                                <div>
                                  <button type="button" class="btn min-width-90px btn-default">选择文件</button>
                                </div>
                                <ul class="list-inline img-gallery img-gallery-md margin-bottom-none" style="margin-right:-5px;">
                                  <li>
                                      <img src="https://p0.ssl.cdn.btime.com/t01b20966b3a344afa8.jpg">
                                      <span class="glyphicon glyphicon-remove-sign" data-toggle="tooltip" data-html="true" title="<div style='width:40px;'>删除</div>" data-oper="remove"></span>
                                      <a href="#" class="img-fancybox f12" data-large-img="https://p0.ssl.cdn.btime.com/t01b20966b3a344afa8.jpg">点击放大</a>
                                  </li>
                                </ul>
                              </td>
                            </tr>
                            <tr>
                              <td>
                                <div class="pad-t5">
                                上传审批表：
                                </div>
                              </td>
                              <td>
                                <div>
                                  <button type="button" class="btn min-width-90px btn-default">选择文件</button>
                                </div>
                                <ul class="list-inline no-margin">
                                  <li class="margin_t10 margin_r10 no-padding">
                                    <span class="btn bg-light-blue text-nowrap">
                                      <b class="text-no-bold">国家开放大学转专业审批表.doc</b>
                                      <i class="glyphicon glyphicon-remove" data-toggle="tooltip" title="删除" data-oper="remove"></i>
                                    </span>
                                  </li>
                                </ul>
                              </td>
                            </tr>
                          </table>
                          <div class="text-right pad-t10 margin_t10 border-top" style="border-color:#ddd">
                            <button type="button" class="btn min-width-90px btn-warning margin_r10 btn-save-edit">审核不通过</button>
                            <button type="button" class="btn min-width-90px btn-success" data-role="btn-pass">审核通过</button>
                          </div>
                      </div>
                      
                    </dd>
                </dl>
                
                <dl class="approval-item">
                    <dt class="clearfix">
                      <b class="a-tit gray6">学籍科审批</b>
                      <span class="fa fa-fw fa-check-circle text-green"></span>
                      <label class="state-lb text-green">审核通过</label>
                    </dt>
                    <dd>
                      <div class="txt">
                          <div class="marin_b10">
                            同意！

                            <ul class="list-inline img-gallery img-gallery-md margin_t10" style="margin-right:-5px;">
                              <li>
                                  <img src="https://p0.ssl.cdn.btime.com/t01b20966b3a344afa8.jpg">
                                  <a href="#" class="img-fancybox f12" data-large-img="https://p0.ssl.cdn.btime.com/t01b20966b3a344afa8.jpg">点击放大</a>
                              </li>
                            </ul>
                            <div class="margin_t10">
                              <a href="#" class="text-underline">国家开放大学转专业审批表.doc</a>
                            </div>
                          </div>
                          <div class="text-right gray9">
                            审核人：王晓敏<br>
                            2017-05-27
                          </div>
                          
                      </div>
                    </dd>
                </dl>

                <dl class="approval-item white-border">
                    <dt class="clearfix">
                      <b class="a-tit gray6">学籍科审批</b>
                      <span class="fa fa-fw fa-times-circle text-red"></span>
                      <label class="state-lb text-red">审核不通过</label>
                    </dt>
                    <dd>
                      <div class="txt">
                          <div class="marin_b10">
                            不同意！
                          </div>
                          <div class="text-right gray9">
                            审核人：王晓敏<br>
                            2017-05-27
                          </div>
                          
                      </div>
                    </dd>
                </dl>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<jsp:include page="/eefileupload/upload.jsp" />
<script type="text/javascript">
//图片放大
;(function(){
  //加载图片放大的相关皮肤
  $('<link/>',{
    rel:"stylesheet",
    type:"text/css",
    href:'http://css.gzedu.com/common/js/fancybox/jquery.fancybox.css',
    'data-id':'require-css'
  }).appendTo($('head'));
  $.getScript('http://css.gzedu.com/common/js/fancybox/jquery.fancybox.pack.js',function(){

    $(".img-gallery").on('click', '.img-fancybox', function(event) {
      event.preventDefault();
      var $img=$(this).closest('.img-gallery').find('.img-fancybox');
          var index=$img.index(this);
          var imgUrls=$.map($img, function(item, index) {
            return $(item).attr('data-large-img');
          });

          $.fancybox(imgUrls, {
            'transitionIn'    : 'none',
            'transitionOut'   : 'none',
            'type'            : 'image',
            'index'           : index,
            'scrollOutside' :false,
            'changeFade'      : 0,
            'loop'      :false,
            beforeShow    :function(){
              this.wrap.parent().css('overflow', 'hidden');
            }
          });
    });
  });
})();

//删除
$('body').on('click','[data-oper="remove"]',function(event) {
  event.preventDefault();
  $(this).closest('li').remove();
});
</script>
</body>
</html>