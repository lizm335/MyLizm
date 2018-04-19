/**
 * Created by 大智若愚的小懂 on 2016/10/19.
 */

/**
 * 打印当前页面
 */
function printpage() {
    window.print();
}

/**
 * 打印局部div
 * @param printpage 局部div的ID
 */
function printdiv(printpage) {
    var headhtml = "<html><head><title></title></head><body>";
    var foothtml = "</body>";
    // 获取div中的html内容
    var newhtml = document.all.item(printpage).innerHTML;
    // 获取div中的html内容，jquery写法如下
    // var newhtml= $("#" + printpage).html();

    // 获取原来的窗口界面body的html内容，并保存起来
    var oldhtml = document.body.innerHTML;

    // 给窗口界面重新赋值，赋自己拼接起来的html内容
    document.body.innerHTML = headhtml + newhtml + foothtml;
    // 调用window.print方法打印新窗口
    window.print();

    // 将原来窗口body的html值回填展示
    document.body.innerHTML = oldhtml;
    return false;
}