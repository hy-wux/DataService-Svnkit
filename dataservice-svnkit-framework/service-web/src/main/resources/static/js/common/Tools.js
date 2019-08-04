var Tools = {
    alert: function (info, iconIndex) {
        parent.layer.msg(info, {
            icon: iconIndex
        });
    },
    info: function (info) {
        Tools.alert(info, 0);
    },
    success: function (info) {
        Tools.alert(info, 1);
    },
    error: function (info) {
        Tools.alert(info, 2);
    },
    confirm: function (tip, ensure) { //询问框
        parent.layer.confirm(tip, {
            btn: ['确定', '取消']
        }, function (index) {
            ensure();
            parent.layer.close(index);
        }, function (index) {
            parent.layer.close(index);
        });
    },
    log: function (info) {
        console.log(info);
    }
}