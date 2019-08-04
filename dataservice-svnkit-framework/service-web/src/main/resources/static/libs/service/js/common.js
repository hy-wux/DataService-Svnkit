/**
 * 通用方法封装处理
 */

$(function(){
	// 复选框事件绑定
	if ($.fn.select2 !== undefined) {
		$("select.form-control:not(.noselect2)").each(function () {
			$(this).select2().on("change", function () {
				$(this).valid();
			})
		})
	}
	if ($(".i-checks").length > 0) {
	    $(".i-checks").iCheck({
	        checkboxClass: "icheckbox_square-green",
	        radioClass: "iradio_square-green",
	    })
	}
	if ($(".picker").length > 0) {
		layui.use('laydate', function() {
		    var laydate = layui.laydate;
		    laydate.render({ elem: '.date', theme: 'molv' });
		});
	}
});

/** 创建选项卡 */
function createMenuItem(dataUrl, menuName) {
    dataIndex = $.common.random(1,100),
    flag = true;
    if (dataUrl == undefined || $.trim(dataUrl).length == 0) return false;
    var topWindow = $(window.parent.document);
    if($('.J_menuTabs', topWindow).length > 0) { // 存在选项卡菜单
        // 选项卡菜单已存在
        $('.J_menuTab', topWindow).each(function () {
            if ($(this).data('id') == dataUrl) {
                if (!$(this).hasClass('active')) {
                    $(this).addClass('active').siblings('.J_menuTab').removeClass('active');
                    $('.page-tabs-content').animate({marginLeft: ""}, "fast");
                    // 显示tab对应的内容区
                    $('.J_mainContent .J_iframe', topWindow).each(function () {
                        if ($(this).data('id') == dataUrl) {
                            $(this).show().siblings('.J_iframe').hide();
                            return false;
                        }
                    });
                }
                flag = false;
                return false;
            }
        });
        // 选项卡菜单不存在
        if (flag) {
            var str = '<a href="javascript:;" class="active J_menuTab" data-id="' + dataUrl + '">' + menuName + ' <i class="fa fa-times-circle"></i></a>';
            $('.J_menuTab', topWindow).removeClass('active');

            // 添加选项卡对应的iframe
            var str1 = '<iframe class="J_iframe" name="iframe' + dataIndex + '" width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless></iframe>';
            $('.J_mainContent', topWindow).find('iframe.J_iframe').hide().parents('.J_mainContent').append(str1);

            // 添加选项卡
            $('.J_menuTabs .page-tabs-content', topWindow).append(str);
        }
    } else { // 不存在选项卡菜单
        window.location.href = dataUrl;
    }
    return false;
}

function selectDeptTree() {
    var url = ctx + "upms/dept/selectDeptTree";
    $.modal.open("选择部门", url, '380', '380');
}

/** 设置全局ajax超时处理 */
$.ajaxSetup({
    complete: function(XMLHttpRequest, textStatus) {
        if (textStatus == "parsererror") {
        	$.modal.confirm("登陆超时！请重新登陆！", function() {
        		window.location.href = ctx + "login";
        	})
        }
    }
});
