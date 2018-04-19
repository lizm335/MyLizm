/*
说明：自定制模态框modal弹出框
用法 
$.mydialog({
  id:'upload-1',
  width:760,
  height:490,
  content: 'url:xx.html' // 'html'
});
*/

;(function($){

	var dialogDefault={
		id:Math.floor(Math.random()*1000000),
		width:100,
    	height:50,
    	content:'',
    	fade:true,//此属性，当为false时，只对非iframe情况有效
    	backdrop:true,//点击遮罩层是否关闭
    	transparent:false,//背景色透明设置
    	showCloseIco:true,
    	markOpacity:0.5,//遮罩层透明设置
    	zIndex:10,
    	onLoaded:function(){}
	};
	var htmlTmp=[//弹窗模板-加载iframe
		'<div class="modal import-modal" data-id="{0}">',
			'<div class="modal-dialog">',
				'<div class="modal-content overlay-wrapper import-modal-content">',
					'<div class="overlay">',
						'<i class="fa fa-refresh fa-spin"></i>',
					'</div>',
					'<iframe id="Iframe-{0}" name="Iframe-{0}" scrolling="no" frameborder="0" width="100%" height="100%"></iframe>',
				'</div>',
				'{1}',//设置关闭ico
			'</div>',
		'</div>'
	];
	var htmlTmp2=[//弹窗模板-非iframe
		'<div class="modal import-modal" data-id="{0}">',
			'<div class="modal-dialog">',
				'<div class="modal-content overlay-wrapper import-modal-content">',
					'<div class="modal-body no-padding"></div>',
				'</div>',
				'{1}',//设置关闭ico
			'</div>',
		'</div>'
	];
	$.extend({
		mydialog:function(setting){
			var opt =$.extend({},dialogDefault,setting);
			var i=opt.content.indexOf("url");
			var tmp;

			if(i!=-1){
				tmp=htmlTmp.join("");
			}
			else{
				tmp=htmlTmp2.join("");
			}

			tmp=tmp.replace(/\{0\}/g,opt.id);
			tmp=tmp.replace(/\{1\}/g,opt.showCloseIco?'<button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>':'');

			var $modelBox=$(tmp);
			var $modelContent=$modelBox.find(".modal-content");

			/*设置弹框的样式*/
			$modelBox.find(".modal-dialog").width(opt.width);
			$modelContent.css({
				height:opt.height,
				'margin-top':($(window).height()-opt.height)/2
			});
			if(opt.transparent){
				$modelContent.css("background","none")
			}
			if(!opt.backdrop){
				$modelBox.attr("data-backdrop","static");
			}
			if(opt.fade){
				$modelBox.addClass('fade');
			}
			$modelBox.css("z-index",opt.zIndex)

			/*设置弹框的样式*/

			$("body").append($modelBox);

			if(i!=-1){
				var $iframe=$('#Iframe-'+opt.id);
				var url=opt.content.substring(i+4);
				
				$modelBox
				.modal('show')
				.on('shown.bs.modal', function(event) {
				    $iframe
				    .prop('src',url)
				    .on('load', function(event) {
				      $(this).siblings('.overlay').hide();
				      $iframe.get(0).api=$modelBox;
				      
				      opt.onLoaded();
				    });
				});
			}
			else{
				$modelBox.find(".modal-body").html(opt.content);
				$modelBox.modal('show').on('shown.bs.modal', function(event) {
					
					opt.onLoaded();
				});
			}

			// 设置遮罩层透明度
			$modelBox.next(".modal-backdrop").css({
				opacity: opt.markOpacity,
				'z-index': parseInt(opt.zIndex)-1
			});

			$modelBox
			.on('hidden.bs.modal', function(event) {
			    $modelBox.remove();

			    // 如果是多弹窗显示
			    if($('.import-modal',document.body).length>0){
			    	$('body').addClass('modal-open')
			    }
			});
			
			//兼容ie9及以下浏览器
			if(document.all && !window.atob){
			    $modelBox.trigger("shown.bs.modal");
			}

			$(window).resize(function(event) {
				var differ=($(window).height()-opt.height)/2;
				$modelContent.css({
					'margin-top':differ>0?differ:0
				});
			});

			return $modelBox;
		},
		closeDialog:function($modelBox){
			$modelBox.modal('hide');
		}
	});
})(jQuery);