
(function($){
    $.fn.citySelect = function(options){
        var opt =$.extend({},$.fn.citySelect.defaults,options);
        return this.each(function(){
            var $container = $(this);
            var $Pro=$container.find("[data-selType='Pro']");
            var $City=$container.find("[data-selType='City']");
            var $District=$container.find("[data-selType='District']");
            var $itemSelBox=$container.find(".itemSelBox");
            var selType="";
			if($itemSelBox.length<=0){
				$itemSelBox=$('<ul class="itemSelBox dropdown-menu"></ul>');
				$container.append($itemSelBox);
			}
            
            $itemSelBox.bind({
                mouseenter:function(){
                    $(this).show();
                },
                mouseleave:function(){
                    $(this).hide();
                }
            });

            $container.mouseleave(function(){
                $itemSelBox.hide();
            })

            $container.find("[data-selType]").click(function(){
                var $that=$(this);
                var v="",vText="";
                selType=$that.attr("data-selType");
				vText=$that.find("input:hidden").val();
				
                switch(selType){
                    case "Pro":
                        v="1";
                        break;

                    case "City":
						v=$Pro.attr("data-id");
                        break;

                    case "District":
						v=$City.attr("data-id");
                        break;
                    default:
                        break;
                }

                $itemSelBox.empty();
                $.each(cityJSON,function(key,val){
                    if( val[1]==v ){
                        $itemSelBox.append("<li data-key="+key+"><a href='javascript:;'>"+val[0]+"</a></li>")
                    }
                });

                $itemSelBox.children("li").click(function(){
                    var v=$(this).attr("data-key");
                    var text=$(this).text();
                    $that.children("span").html(text);
                    $that.children("input[type='hidden']").val(text);
                    selType=$that.attr("data-selType");
                    
                    switch(selType){
                        case "Pro":
							$Pro.attr("data-id",v);
                            $.each(cityJSON,function(key,val){
                                if(val[1]==v){
									$City.attr("data-id",key);
                                    $City.children("span").html(val[0]);
									
                                    $City.children("input[type='hidden']").val(val[0]);

                                    $District.children("span").html("\u533a\u002f\u53bf");
									$District.attr("data-id","");
									$District.find("input:hidden").val("")
                                    return false;
                                }
                            });
                            break;

                        case "City":
							$City.attr("data-id",v);
                            $.each(cityJSON,function(key,val){
                                if(val[1]==v){
                                    $District.children("span").html(val[0]);
									$District.attr("data-id",key);
                                    $District.children("input[type='hidden']").val(val[0]);
                                    return false;
                                }
                            });
                            break;
						
                        default:
                            break;
                    }
                    $itemSelBox.hide();
                })
                if($itemSelBox.children().length>0){
                    $itemSelBox.show();
                }
            })

            $container.click(function(event){
                var $this=$(event.target);
                if($this.hasClass("select-box")||$this.hasClass("select-in")||$this.hasClass("select-ico")||$this.parent().hasClass("select-ico")){
                    $container.find('[data-seltype="Pro"]').click();
                }
            });
			
			function init(){
				$container.find("[data-selType]").each(function(){
					var $that=$(this);
					var v="",vText="";
					selType=$that.attr("data-selType");
					vText=$that.find("span").text();
					
					switch(selType){
						case "Pro":
							$.each(cityJSON,function(k,v){
								if(v[1]=="1"){
									if(vText.substring(0,2)==v[0].substring(0,2)){
										$that.attr("data-id",k);
										return false;
									}
								}
							});
							break;

						default:
							$.each(cityJSON,function(k,v){
								if(v[1]!="1"){
									if(vText.substring(0,2)==v[0].substring(0,2)){
										$that.attr("data-id",k);
										return false;
									}
								}
							});
							break;
					}
				});
			}
			init();
			
        });
    }
	
    $.fn.citySelect.defaults = {
        
    }
})(jQuery);