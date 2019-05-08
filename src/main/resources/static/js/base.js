$(function () {
    //跳转页面保持状态高亮js
    var urlstr = location.href;
    var urlstatus = false;
    $(".sidebar-menu .nav-header").each(function () {
        $(this).click(function () {
            $(this).next("ul").toggleClass("collapse in");
        })
    });

})