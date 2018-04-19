(function($){
    $.fn.sunny_scroll = function(options){
        var opt =$.extend({},$.fn.sunny_scroll.defaults,options);
        return this.each(function(){
            var container = $(this);
            var _content_list = container.children("ul");
            var _content_list_first = container.children("ul:first");
            var _lef_btn=$("<div>",{"class":opt.arrow_left}).prependTo(container);
            var _rig_btn=$("<div>",{"class":opt.arrow_right}).prependTo(container);

            var _page=0,//当前页码，索引从0开始，代表第1页
                _arrLi,//用于转化数组为"1,1,1"这样的格式
                _arrPage,//用于各分页存放该页的对象索引
                _arrCurrentPageWidth,//用于各分页的实际宽度
                _page_count,//页码总数
                _allLi,
                _tmp,
                _totalWith;
            
            function initFunc(){//初始化函数
                _arrLi=new Array();
                _arrPage=new Array();
                _arrCurrentPageWidth=new Array();
                _page_count=0;
                _tmp=0;
                _totalWith=0;

                //分割形成每页
                _allLi=_content_list_first.children("li:visible");
                _allLi.each(function(_j){
                    _tmp+=_allLi.eq(_j).outerWidth(true);
                    if(_tmp<container.width()){
                        _arrLi.push(_j);
                        _arrPage[_page_count]=_arrLi;
                    }
                    else{
                        _arrLi=[];
                        _arrLi.push(_j);
                        _arrPage[++_page_count]=_arrLi;
                        _tmp=0;//该页统计完毕，恢复初始值0
                        _tmp+=_allLi.eq(_j).outerWidth(true);
                    }
                });
                
                //统计各分页的实际宽度，并存放到arrCurrentPageWidth数组中
                for(_j=0;_j<_arrPage.length;_j++){
                    var _t=_arrPage[_j];
                    var _tL=0;
                    for(_m=0;_m<_t.length;_m++){
                        _tL+=$(_allLi[_t[_m]]).outerWidth(true);
                    }
                    _totalWith+=_tL;
                    _arrCurrentPageWidth.push(_tL);
                }
                _page_count=_arrPage.length;//获取页码总数 
            }

            initFunc();

            container.hover(
                function(){
                    if(_page_count>1){
                        if(_page==0){
                            _rig_btn.show();
                            _lef_btn.hide();
                        }
                        else if(_page==_page_count-1){
                            _lef_btn.show();
                            _rig_btn.hide();
                        }
                        else{
                            _lef_btn.show();
                            _rig_btn.show();
                        }
                    }
                },
                function(){
                    _lef_btn.hide();
                    _rig_btn.hide();
                }
            );

            //上页按钮  
            _lef_btn.click(function(){    //绑定click事件  
                if( !_content_list.is(":animated") ){    //判断“内容展示区域”是否正在处于动画  
                    if(_page>0){
                        jumpPage(_page-1);
                        _page--;
                    }
                } 
            }); 
            //下页按钮  
            _rig_btn.click(function(e){    //绑定click事件  
                if( !_content_list.is(":animated") ){    //判断“内容展示区域”是否正在处于动画  
                    if(_page<_page_count-1){
                      jumpPage(_page+1);
                      _page++;
                    }
                }
            });

            //翻页函数
            function jumpPage(jPage){
                if(jPage<0){
                    jPage=0;
                }
                else if(jPage > _page_count-1){
                    jPage=_page_count-1;
                }

                if(jPage==0){
                    _lef_btn.hide().addClass('tofirst');
                    _rig_btn.show().removeClass("tolast");
                }
                else if(jPage==_page_count-1){
                    _lef_btn.show().removeClass('tofirst');
                    _rig_btn.hide().addClass("tolast");
                }
                else{
                    _lef_btn.show().removeClass('tofirst');
                    _rig_btn.show().removeClass("tolast");
                }

                if(_page_count<=1){
                    _lef_btn.addClass('tofirst');
                    _rig_btn.addClass("tolast");
                }

                var step=0;
                if(jPage<_page){
                    for(var i=jPage;i<_page;i++){
                        step+=_arrCurrentPageWidth[i];
                    }
                    _content_list.stop(true,true).animate({ left : '+='+step }, opt.scrollTime);
                }
                else if(jPage>_page){
                    for(var i=_page;i<jPage;i++){
                        step+=_arrCurrentPageWidth[i];
                    }
                    _content_list.stop(true,true).animate({ left : '-='+step }, opt.scrollTime);
                }
                else{
                    for(var i=0;i<jPage;i++){
                        step+=_arrCurrentPageWidth[i];
                    }
                    _content_list.stop(true,true).animate({ left : '-'+step }, opt.scrollTime);
                }
            }

            /*
            *跳转当前标签所处的分页
            *例子:$container.trigger("tabScroll.jumpCurPage"); 
            */
            container.bind('tabScroll.jumpCurPage', function(event) {
                if(_arrPage.length>1){
                    var $curTab=_content_list_first.find("li."+opt.curTabClass);

                    $.each(_arrPage, function(index, val) {
                         if($.inArray($curTab.index(), val)!=-1){
                            _page=index;
                            jumpPage(_page);
                            return false;
                         }
                    });
                }
            });

            /*
            *跳转翻页:此事件是为了外部调用设置
            *例子:$container.trigger("tabScroll.jumpPage",[ 2 ]); 跳转到第2页
            */
            container.bind('tabScroll.jumpPage', function(event,jPage) {
                _page=jPage-1;
                jumpPage(_page);
            });
           
            //窗口尺寸改变时，重新初始化
            container.bind("tabScroll.refresh",function(){
                if(container.is(":visible")){
                    initFunc();
                    if(_page<0){
                        _page=0;
                    }
                    else if(_page>_page_count-1){
                        _page=_page_count-1;
                    }
                    jumpPage(_page);
                    _lef_btn.hide();
                    _rig_btn.hide();
                }
            });

            /*window.onresize=function(){
                container.trigger("tabScroll.refresh");
            };*/
        });
    }

    $.fn.sunny_scroll.defaults = {
        arrow_left:'tabs_btn_left',//左按钮class名
        arrow_right:'tabs_btn_right',//右按钮class名
        scrollTime:'slow',//切换时长
        curTabClass:'cur'//当前标签的class选择器
    }
})(jQuery);