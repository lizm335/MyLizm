/**
 * 公共控件<br/>
 * 省市县级联使用$.select_area($city, $area, province, city, area)
 * 省市县级联使用$.select_district($city, $area, district)
 * 省市级联使用$.select_city($city, province, city)
 * Created by Administrator on 2016/6/8.
 */
(function($){

    // 项目根路径
    var basePath = (typeof(ctx) != "undefined" ? ctx : "");

    /**
     * 省市县级联选择
     * @param $city
     * @param $area
     * @param province
     * @param city
     * @param area
     */
    $.fn.select_area = function($city, $area, province, city, area) {
        var $province = $(this);
        // 级联省份-市区县
        $province.change(function () {
            $city[0].options.length = 1;
            $area[0].options.length = 1;
            if($province.val() == "") {
                if($city.hasClass('selectpicker'))
                    $city.selectpicker('refresh');
                if($area.hasClass('selectpicker'))
                    $area.selectpicker('refresh');
                return;
            }
            $.getJSON(basePath+'/area/getChildrens', {pcode:$province.val()}, function (data) {
                $.each(data.obj, function(i,item){
                    $city[0].options.add(new Option(item.name, item.code));
                });
                if($city.hasClass('selectpicker'))
                    $city.selectpicker('refresh');
                if($area.hasClass('selectpicker'))
                    $area.selectpicker('refresh');
            });
        });
        // 级联城市-区县
        $city.change(function () {
            $area[0].options.length = 1;
            if($city.val() == "") {
                if($area.hasClass('selectpicker'))
                    $area.selectpicker('refresh');
                return;
            }
            $.getJSON(basePath+'/area/getChildrens', {pcode:$province.val(),ccode:$city.val()}, function (data) {
                $.each(data.obj, function(i,item){
                    $area[0].options.add(new Option(item.name, item.code));
                });
                if($area.hasClass('selectpicker'))
                    $area.selectpicker('refresh');
            });
        });
        // 加载选项框
        $province[0].options.length = 0;
        $city[0].options.length = 0;
        $area[0].options.length = 0;
        $province[0].options.add(new Option("- 请选择 -", ""));
        $city[0].options.add(new Option("- 请选择 -", ""));
        $area[0].options.add(new Option("- 请选择 -", ""));
        // 省
        $.getJSON(basePath+'/area/getChildrens', {pcode:1}, function (data) {
            $.each(data.obj, function(i,item){
                if(item.code == province) {
                    $province.append('<option value="'+item.code+'" selected>'+item.name+'</option>');
                } else {
                    $province[0].options.add(new Option(item.name, item.code));
                }
            });
            if($province.hasClass('selectpicker'))
                $province.selectpicker('refresh');
        });
        if(typeof(province) != "undefined") {
            // 市
            $.getJSON(basePath+'/area/getChildrens', {pcode:province}, function (data) {
                $.each(data.obj, function(i,item){
                    if(item.code == city) {
                        $city.append('<option value="'+item.code+'" selected>'+item.name+'</option>');
                    } else {
                        $city[0].options.add(new Option(item.name, item.code));
                    }
                });
                if($city.hasClass('selectpicker'))
                    $city.selectpicker('refresh');
            });
            if(typeof(city) != "undefined") {
                // 县
                $.getJSON(basePath+'/area/getChildrens', {pcode:province, ccode:city}, function (data) {
                    $.each(data.obj, function(i,item){
                        if(item.code == area) {
                            $area.append('<option value="'+item.code+'" selected>'+item.name+'</option>');
                        } else {
                            $area[0].options.add(new Option(item.name, item.code));
                        }
                    });
                    if($area.hasClass('selectpicker'))
                        $area.selectpicker('refresh');
                });
            }
        }
    }

    /**
     * 省市县级联选择
     * @param $city
     * @param $area
     * @param district
     */
    $.fn.select_district = function($city, $area, district) {
        if(typeof(district) != "undefined" && district != '') {
            var province = district.substr(0, 2) + "0000";
            var city = district.substr(0, 4) + "00";
            var area = district;
            this.select_area($city, $area, province, city, area);
        } else {
            this.select_area($city, $area);
        }
    }

    /**
     * 省市级联选择
     * @param $city
     * @param province
     * @param city
     */
    $.fn.select_city = function($city, province, city) {
        var $province = $(this);
        // 级联省份-市
        $province.change(function () {
            $city[0].options.length = 1;
            if($province.val() == "") {
                if($city.hasClass('selectpicker'))
                    $city.selectpicker('refresh');
                return;
            }
            $.getJSON(basePath+'/area/getChildrens', {pcode:$province.val()}, function (data) {
                $.each(data.obj, function(i,item){
                    $city[0].options.add(new Option(item.name, item.code));
                });
                if($city.hasClass('selectpicker'))
                    $city.selectpicker('refresh');
            });
        });
        // 加载选项框
        $province[0].options.length = 0;
        $city[0].options.length = 0;
        $province[0].options.add(new Option("- 请选择 -", ""));
        $city[0].options.add(new Option("- 请选择 -", ""));
        // 省
        $.getJSON(basePath+'/area/getChildrens', {pcode:1}, function (data) {
            $.each(data.obj, function(i,item){
                if(item.code == province) {
                    $province.append('<option value="'+item.code+'" selected>'+item.name+'</option>');
                } else {
                    $province[0].options.add(new Option(item.name, item.code));
                }
            });
            if($province.hasClass('selectpicker'))
                $province.selectpicker('refresh');
        });
        if(typeof(province) != "undefined") {
            // 市
            $.getJSON(basePath+'/area/getChildrens', {pcode:province}, function (data) {
                $.each(data.obj, function(i,item){
                    if(item.code == city) {
                        $city.append('<option value="'+item.code+'" selected>'+item.name+'</option>');
                    } else {
                        $city[0].options.add(new Option(item.name, item.code));
                    }
                });
                if($city.hasClass('selectpicker'))
                    $city.selectpicker('refresh');
            });
        }
    }

    /*------------------------------------------------ 华丽丽的分割线 ------------------------------------------------*/

    //Date range picker
    $('.daterange-control').daterangepicker({
        // startDate: moment().startOf('day'),
        // endDate: moment(),
        // minDate: '2012-01-01',    //最小时间
        maxDate : moment(), //最大时间
        /*dateLimit : {
         days : 30
         }, //起止时间的最大间隔*/
        showDropdowns : true,
        showWeekNumbers : false, //是否显示第几周
        timePicker : false, //是否显示小时和分钟
        timePickerIncrement : 60, //时间的增量，单位为分钟
        timePicker12Hour : false, //是否使用12小时制来显示时间
        ranges : {
            //'最近1小时': [moment().subtract('hours',1), moment()],
            '今日': [moment().startOf('day'), moment()],
            '昨日': [moment().subtract('days', 1).startOf('day'), moment().subtract('days', 1).endOf('day')],
            '最近7日': [moment().subtract('days', 6), moment()],
            '最近30日': [moment().subtract('days', 29), moment()],
            '当月': [moment().startOf('month'), moment().endOf('month')],
            '上个月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        opens : 'right', //日期选择框的弹出位置
        buttonClasses : [ 'btn btn-small btn-sm' ],
        applyClass : 'btn-success',
        cancelClass : 'btn-default',
        format : 'YYYY-MM-DD', //控件中from和to 显示的日期格式
        separator : ' - ',
        locale : {
            applyLabel : '确定',
            cancelLabel : '取消',
            fromLabel : '起始时间',
            toLabel : '结束时间',
            customRangeLabel : '自定义',
            daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
            monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
            firstDay : 1
        }
    }, function(start, end, label) {//格式化日期显示框
        // $('#daterange-control').val(start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD'));
    });

    //Date range picker
    $('.daterange-time').daterangepicker({
        maxDate : moment(), //最大时间
        showDropdowns : true,
        showWeekNumbers : false, //是否显示第几周
        timePicker : true, //是否显示小时和分钟
        timePickerIncrement : 5, //时间的增量，单位为分钟
        timePicker12Hour : false, //是否使用12小时制来显示时间
        format : 'YYYY-MM-DD HH:mm', //控件中from和to 显示的日期格式
        separator : ' - ',
        opens : 'right', //日期选择框的弹出位置
        locale : {
            applyLabel : '确定',
            cancelLabel : '取消',
            fromLabel : '起始时间',
            toLabel : '结束时间',
            customRangeLabel : '自定义',
            daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
            monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
            firstDay : 1
        }
    });

    /*日期控件*/
    $('.reservation').datepicker({
        language:'zh-CN',
        endDate: new Date()
    });
    /*var singleDatePickerOpt = {
        singleDatePicker: true,
        maxDate : moment(), //最大时间
        showDropdowns : true,
        format : 'YYYY-MM-DD',
        locale : {
            daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
            monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
            firstDay : 1
        }
    };
    $('.reservation').daterangepicker(singleDatePickerOpt);*/

    $('.reservation2').daterangepicker({
        singleDatePicker: true,
        maxDate : moment(), //最大时间
        showDropdowns : true,
        format : 'YYYYMMDD',
        locale : {
            daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
            monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
            firstDay : 1
        }
    });

    var singleDateTimePickerOpt = {
        singleDatePicker: true,
        maxDate : moment(), //最大时间
        showDropdowns : true,
        timePicker : true, //是否显示小时和分钟
        timePickerIncrement : 5, //时间的增量，单位为分钟
        timePicker12Hour : false, //是否使用12小时制来显示时间
        format : 'YYYY-MM-DD HH:mm', //控件中from和to 显示的日期格式
        locale : {
            applyLabel : '确定',
            cancelLabel : '取消',
            daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
            monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
            firstDay : 1
        }
    };
    $('.single-datetime').daterangepicker(singleDateTimePickerOpt);
    
    var singleDateTimePickerOpt2 = {
        singleDatePicker: true,
        showDropdowns : true,
        timePicker : true, //是否显示小时和分钟
        timePickerIncrement : 5, //时间的增量，单位为分钟
        timePicker12Hour : false, //是否使用12小时制来显示时间
        format : 'YYYY-MM-DD HH:mm', //控件中from和to 显示的日期格式
        locale : {
            applyLabel : '确定',
            cancelLabel : '取消',
            daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
            monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
            firstDay : 1
        }
    };
    $('.single-datetime2').daterangepicker(singleDateTimePickerOpt2);

})(jQuery);
