/**
 * 通用方法封装处理
 */
(function ($) {
    $.extend({
        // 表格封装处理
        table: {
            _option: {},
            // 初始化表格
            init: function (options) {
                $.table._option = options;
                $('#bootstrap-table').bootstrapTable({
                    url: options.url,                                                                                // 请求后台的URL（*）
                    contentType: "application/x-www-form-urlencoded",                                                // 编码类型
                    method: 'post',                                                                                  // 请求方式（*）
                    cache: false,                                                                                   // 是否使用缓存
                    sortable: true,                                                                                 // 是否启用排序
                    sortStable: true,                                                                               // 设置为 true 将获得稳定的排序
                    sortName: $.common.isEmpty(options.orderField) ? "" : options.orderField,                        // 排序列名称
                    sortOrder: $.common.isEmpty(options.orderType) ? "asc" : options.orderType,                      // 排序方式  asc 或者 desc
                    pagination: $.common.visible(options.pagination),                                                // 是否显示分页（*）
                    pageNumber: 1,                                                                                   // 初始化加载第一页，默认第一页
                    pageSize: 10,                                                                                    // 每页的记录行数（*）
                    pageList: [10, 25, 50],                                                                          // 可供选择的每页的行数（*）
                    iconSize: 'outline',                                                                             // 图标大小：undefined默认的按钮尺寸 xs超小按钮sm小按钮lg大按钮
                    toolbar: '#toolbar',                                                                             // 指定工作栏
                    sidePagination: "server",                                                                        // 启用服务端分页
                    search: $.common.visible(options.search),                                                        // 是否显示搜索框功能
                    showRefresh: $.common.visible(options.showRefresh),                                              // 是否显示刷新按钮
                    showColumns: $.common.visible(options.showColumns),                                              // 是否显示隐藏某列下拉框
                    showToggle: $.common.visible(options.showToggle),                                                // 是否显示详细视图和列表视图的切换按钮
                    showExport: $.common.visible(options.showExport),                                                // 是否支持导出文件
                    // queryParams: $.common.isEmpty(options.queryParams) ? $.table.queryParams : options.queryParams,  // 传递参数（*）
                    queryParams: $.table.queryParams,                                                                // 传递参数（*）
                    columns: options.columns,                                                                        // 显示列信息（*）
                    responseHandler: $.table.responseHandler,                                                        // 回调函数
                    onEditableSave: function (field, row, oldValue, $el) {
                        $.ajax({
                            type: "post",
                            url: options.modifyUrl,
                            data: row,
                            dataType: 'json',
                            success: function (data, status) {
                                if (status == "success") {
                                    $.modal.msgSuccess(data.message);
                                }
                            },
                            error: function () {
                                $.modal.msgError("请求失败");
                            },
                            complete: function () {
                            }
                        });
                    }                                                                                                // 行内编辑保存
                });
            },
            // 查询条件
            queryParams: function (params) {
                var param = {
                    // 传递参数查询参数
                    size: params.limit,
                    current: params.offset / params.limit + 1,
                    searchValue: params.search,
                    orderField: params.sort,
                    orderType: params.order
                };
                for (var attr in $.table._option.queryParams) {
                    param[attr] = $.table._option.queryParams[attr];
                }
                return param;
            },
            // 请求获取数据后处理回调函数
            responseHandler: function (res) {
                if (res.code == web_status.SUCCESS) {
                    return {rows: res.rows, total: res.total};
                } else {
                    $.modal.alertWarning(res.message);
                    return {rows: [], total: 0};
                }
            },
            // 搜索
            search: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                var params = $("#bootstrap-table").bootstrapTable('getOptions');
                params.queryParams = function (params) {
                    var search = {};
                    $.each($("#" + currentId).serializeArray(), function (i, field) {
                        search[field.name] = field.value;
                    });
                    search.size = params.limit;
                    search.current = params.offset / params.limit + 1;
                    search.searchValue = params.search;
                    search.orderField = params.sort;
                    search.orderType = params.order;
                    return search;
                }
                $("#bootstrap-table").bootstrapTable('refresh', params);
            },
            // 下载
            exportExcel: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                $.modal.loading("正在导出数据，请稍后...");
                $.post($.table._option.exportUrl, $("#" + currentId).serialize(), function (result) {
                    if (result.code == web_status.SUCCESS) {
                        window.location.href = ctx + "common/download?fileName=" + result.message + "&delete=" + true;
                    } else {
                        $.modal.alertError(result.message);
                    }
                    $.modal.closeLoading();
                });
            },
            // 下载模板
            importTemplate: function() {
                $.get($.table._option.importTemplateUrl, function(result) {
                    if (result.code == web_status.SUCCESS) {
                        window.location.href = ctx + "common/download?fileName=" + result.message + "&delete=" + true;
                    } else {
                        $.modal.alertError(result.message);
                    }
                });
            },
            // 导入数据
            importExcel: function(formId,formParam) {
                var currentId = $.common.isEmpty(formId) ? 'importForm' : formId;
                $.form.reset(currentId);
                layer.open({
                    type: 1,
                    area: ['400px', '230px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: '导入' + $.table._option.modalName + '数据',
                    content: $('#' + currentId),
                    btn: ['<i class="fa fa-check"></i> 导入', '<i class="fa fa-remove"></i> 取消'],
                    btnclass: ['btn btn-primary', 'btn btn-danger'],
                    // 弹层外区域关闭
                    shadeClose: true,
                    btn1: function(index, layero){
                        var file = layero.find('#file').val();
                        if (file == '' || (!$.common.endWith(file, '.xls') && !$.common.endWith(file, '.xlsx'))){
                            $.modal.msgWarning("请选择后缀为 “.xls”或“.xlsx”的文件。");
                            return false;
                        }
                        var index = layer.load(2, {shade: false});
                        $.modal.disable();
                        var formData = new FormData();
                        formData.append("file", $('#file')[0].files[0]);
                        formData.append("updateSupport", $("input[name='updateSupport']").is(':checked'));
                        $.ajax({
                            url: $.table._option.importUrl.replace("{formParam}", formParam),
                            data: formData,
                            cache: false,
                            contentType: false,
                            processData: false,
                            type: 'POST',
                            success: function (result) {
                                if (result.code == web_status.SUCCESS) {
                                    $.modal.closeAll();
                                    $.modal.alertSuccess(result.message);
                                    $.table.refresh();
                                } else {
                                    layer.close(index);
                                    $.modal.enable();
                                    $.modal.alertError(result.message);
                                }
                            }
                        });
                    }
                });
            },
            // 刷新
            refresh: function () {
                $("#bootstrap-table").bootstrapTable('refresh', {
                    url: $.table._option.url,
                    silent: true
                });
            },
            // 查询选中列值
            selectColumns: function (column) {
                return $.map($('#bootstrap-table').bootstrapTable('getSelections'), function (row) {
                    return row[$.table._option.columns[column].field];
                });
            },
            // 查询选中首列值
            selectFirstColumns: function () {
                return $.map($('#bootstrap-table').bootstrapTable('getSelections'), function (row) {
                    return row[$.table._option.columns[1].field];
                });
            },
            // 回显数据字典
            selectDictName: function (_dicts, _value) {
                var actions = [];
                $.each(_dicts, function (index, dict) {
                    if (dict.codeValue == _value) {
                        actions.push("<span class='badge badge-" + dict.listStyle + "'>" + dict.codeText + "</span>");
                        return false;
                    }
                });
                return actions.join('');
            },
            // 回显数据字典
            showDictName: function (_dicts, _value, _code, _name) {
                var actions = [];
                $.each(_dicts, function (index, dict) {
                    if (dict[_code] == _value) {
                        actions.push(dict[_name]);
                        return false;
                    }
                });
                return actions.join('');
            }
        },
        // 表格树封装处理
        treeTable: {
            _option: {},
            _treeTable: {},
            // 初始化表格
            init: function (options) {
                $.table._option = options;
                var treeTable = $('#bootstrap-table').bootstrapTreeTable({
                    code: options.id,             // 用于设置父子关系
                    parentCode: options.parentId, // 用于设置父子关系
                    type: 'get',                   // 请求方式（*）
                    url: options.url,              // 请求后台的URL（*）
                    ajaxParams: {},               // 请求数据的ajax的data属性
                    expandColumn: '0',            // 在哪一列上面显示展开按钮
                    striped: false,               // 是否各行渐变色
                    bordered: true,               // 是否显示边框
                    expandAll: $.common.visible(options.expandAll), // 是否全部展开
                    columns: options.columns
                });
                $.treeTable._treeTable = treeTable;
            },
            // 条件查询
            search: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                var params = {};
                $.each($("#" + currentId).serializeArray(), function (i, field) {
                    params[field.name] = field.value;
                });
                $.treeTable._treeTable.bootstrapTreeTable('refresh', params);
            },
            // 刷新
            refresh: function () {
                $.treeTable._treeTable.bootstrapTreeTable('refresh');
            },
        },
        // 表单封装处理
        form: {
            // 表单重置
            reset: function(formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                $("#" + currentId)[0].reset();
            },
            // 获取选中复选框项
            selectCheckeds: function (name) {
                var checkeds = "";
                $('input:checkbox[name="' + name + '"]:checked').each(function (i) {
                    if (0 == i) {
                        checkeds = $(this).val();
                    } else {
                        checkeds += ("," + $(this).val());
                    }
                });
                return checkeds;
            },
            // 获取选中下拉框项
            selectSelects: function (name) {
                var selects = "";
                $('#' + name + ' option:selected').each(function (i) {
                    if (0 == i) {
                        selects = $(this).val();
                    } else {
                        selects += ("," + $(this).val());
                    }
                });
                return selects;
            }
        },
        // 弹出层封装处理
        modal: {
            // 显示图标
            icon: function (type) {
                var icon = "";
                if (type == modal_status.WARNING) {
                    icon = 0;
                } else if (type == modal_status.SUCCESS) {
                    icon = 1;
                } else if (type == modal_status.FAIL) {
                    icon = 2;
                } else {
                    icon = 3;
                }
                return icon;
            },
            // 消息提示
            msg: function (content, type) {
                if (type != undefined) {
                    layer.msg(content, {icon: $.modal.icon(type), time: 1000, shift: 5});
                } else {
                    layer.msg(content);
                }
            },
            // 错误消息
            msgError: function (content) {
                $.modal.msg(content, modal_status.FAIL);
            },
            // 成功消息
            msgSuccess: function (content) {
                $.modal.msg(content, modal_status.SUCCESS);
            },
            // 警告消息
            msgWarning: function (content) {
                $.modal.msg(content, modal_status.WARNING);
            },
            // 弹出提示
            alert: function (content, type) {
                layer.alert(content, {
                    icon: $.modal.icon(type),
                    title: "系统提示",
                    btn: ['确认'],
                    btnclass: ['btn btn-primary'],
                });
            },
            // 消息提示并关闭刷新父窗体
            msgReload: function (msg, type) {
                layer.msg(msg, {
                        icon: $.modal.icon(type),
                        time: 500,
                        shade: [0.1, '#8F8F8F']
                    },
                    function () {
                        // $.modal.reload();
                        parent.layer.closeAll();
                        parent.$.table.refresh();
                        parent.$.treeTable.refresh();
                    });
            },
            // 错误提示
            alertError: function (content) {
                $.modal.alert(content, modal_status.FAIL);
            },
            // 成功提示
            alertSuccess: function (content) {
                $.modal.alert(content, modal_status.SUCCESS);
            },
            // 警告提示
            alertWarning: function (content) {
                $.modal.alert(content, modal_status.WARNING);
            },
            // 关闭窗体
            close: function () {
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            },
            // 关闭全部窗体
            closeAll: function () {
                layer.closeAll();
            },
            // 确认窗体
            confirm: function (content, callBack) {
                layer.confirm(content, {
                    icon: 3,
                    title: "系统提示",
                    btn: ['确认', '取消'],
                    btnclass: ['btn btn-primary', 'btn btn-danger'],
                }, function (index) {
                    layer.close(index);
                    callBack(true);
                });
            },
            // 弹出层指定宽度
            open: function (title, url, width, height) {
                //如果是移动端，就使用自适应大小弹窗
                if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                    width = 'auto';
                    height = 'auto';
                }
                if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "404.html";
                }
                if ($.common.isEmpty(width)) {
                    width = ($(window).width() - 100);
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 100);
                }
                layer.open({
                    type: 2,
                    area: [width + 'px', height + 'px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: title,
                    content: url,
                    btn: ['提交', '关闭'],
                    yes: function (index, layero) {
                        $(layero).find("iframe")[0].contentWindow.submitClick();
                    },
                    cancel: function () {
                    }
                });
            },
            // 弹出层指定宽度
            view: function (title, url, width, height) {
                //如果是移动端，就使用自适应大小弹窗
                if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                    width = 'auto';
                    height = 'auto';
                }
                if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "404.html";
                }
                if ($.common.isEmpty(width)) {
                    width = ($(window).width() - 100);
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 100);
                }
                layer.open({
                    type: 2,
                    area: [width + 'px', height + 'px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: title,
                    content: url,
                    btn: ['关闭']
                });
            },
            // 弹出层全屏
            openFull: function (title, url, width, height) {
                //如果是移动端，就使用自适应大小弹窗
                if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                    width = 'auto';
                    height = 'auto';
                }
                if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "404.html";
                }
                if ($.common.isEmpty(width)) {
                    width = 1200;
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 100);
                }
                var index = layer.open({
                    type: 2,
                    area: [width + 'px', height + 'px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: title,
                    content: url
                });
                layer.full(index);
            },
            // 禁用按钮
            disable: function() {
                var doc = window.top == window.parent ? window.document : window.parent.document;
                $("a[class*=layui-layer-btn]", doc).addClass("layer-disabled");
            },
            // 启用按钮
            enable: function() {
                var doc = window.top == window.parent ? window.document : window.parent.document;
                $("a[class*=layui-layer-btn]", doc).removeClass("layer-disabled");
            },
            // 打开遮罩层
            loading: function (message) {
                $.blockUI({message: '<div class="loaderbox"><div class="loading-activity"></div> ' + message + '</div>'});
            },
            // 关闭遮罩层
            closeLoading: function () {
                setTimeout(function () {
                    $.unblockUI();
                }, 50);
            },
            // 重新加载
            reload: function () {
                parent.location.reload();
            }
        },
        // 操作封装处理
        operate: {
            // 添加信息
            add: function (id) {
                var url = $.common.isEmpty(id) ? $.table._option.createUrl : $.table._option.createUrl.replace("{id}", id);
                $.modal.open("添加" + $.table._option.modalName, url);
            },
            // 修改信息
            edit: function (id) {
                var url = $.table._option.updateUrl.replace("{id}", id);
                $.modal.open("修改" + $.table._option.modalName, url);
            },
            // 查看信息
            view: function (id) {
                var url = $.table._option.viewUrl.replace("{id}", id);
                $.modal.view("查看" + $.table._option.modalName, url);
            },
            // 提交数据
            submit: function (url, type, dataType, data) {
                $.modal.loading("正在处理中，请稍后...");
                var config = {
                    url: url,
                    type: type,
                    dataType: dataType,
                    data: data,
                    success: function (result) {
                        $.operate.ajaxSuccess(result);
                    }
                };
                $.ajax(config)
            },
            // post请求传输
            post: function (url, data) {
                $.operate.submit(url, "post", "json", data);
            },
            // 删除信息
            remove: function (id) {
                $.modal.confirm("确定删除该条" + $.table._option.modalName + "信息吗？", function () {
                    var url = $.common.isEmpty(id) ? $.table._option.removeUrl : $.table._option.removeUrl.replace("{id}", id);
                    var data = {"ids": id};
                    $.operate.submit(url, "post", "json", data);
                });
            },
            // 删除信息
            removeRow: function (data) {
                $.modal.confirm("确定删除该条" + $.table._option.modalName + "信息吗？", function () {
                    $.operate.submit($.table._option.removeUrl, "post", "json", data);
                });
            },
            // 批量删除信息
            batRemove: function () {
                var rows = $.common.isEmpty($.table._option.id) ? $.table.selectFirstColumns() : $.table.selectColumns($.table._option.id);
                if (rows.length == 0) {
                    $.modal.alertWarning("请至少选择一条记录");
                    return;
                }
                $.modal.confirm("确认要删除选中的" + rows.length + "条数据吗?", function () {
                    var url = $.table._option.removeUrl;
                    var data = {"ids": rows.join()};
                    $.operate.submit(url, "post", "json", data);
                });
            },
            // 批量删除信息
            batchRemoveRows: function (data, key) {
                var rows = $.common.isEmpty($.table._option.id) ? $.table.selectFirstColumns() : $.table.selectColumns($.table._option.id);
                if (rows.length == 0) {
                    $.modal.alertWarning("请至少选择一条记录");
                    return;
                }
                $.modal.confirm("确认要删除选中的" + rows.length + "条数据吗?", function () {
                    var url = $.table._option.removeUrl;
                    data[key] = rows.join();
                    $.operate.submit(url, "post", "json", data);
                });
            },
            // 添加信息 全屏
            addFull: function (id) {
                var url = $.common.isEmpty(id) ? $.table._option.createUrl : $.table._option.createUrl.replace("{id}", id);
                $.modal.openFull("添加" + $.table._option.modalName, url);
            },
            // 修改信息 全屏
            editFull: function (id) {
                var url = $.table._option.updateUrl.replace("{id}", id);
                $.modal.openFull("修改" + $.table._option.modalName, url);
            },
            // 保存信息
            save: function (url, data) {
                $.modal.loading("正在处理中，请稍后...");
                var config = {
                    url: url,
                    type: "post",
                    dataType: "json",
                    data: data,
                    success: function (result) {
                        $.operate.saveSuccess(result);
                    }
                };
                $.ajax(config)
            },
            // 保存结果弹出message刷新table表格
            ajaxSuccess: function (result) {
                $.modal.closeLoading();
                if (result.code == web_status.SUCCESS) {
                    $.modal.msgSuccess(result.message);
                    if(!$.common.isEmpty($.table)){
                        $.table.refresh();
                    }
                    if(!$.common.isEmpty($.treeTable)){
                        $.treeTable.refresh();
                    }
                } else {
                    $.modal.alertError(result.message);
                }
            },
            // 保存结果提示message
            saveSuccess: function (result) {
                if (result.code == web_status.SUCCESS) {
                    $.modal.msgReload("保存成功,正在刷新数据请稍后……", modal_status.SUCCESS);
                } else {
                    $.modal.alertError(result.message);
                }
                $.modal.closeLoading();
            }
        },
        // 通用方法封装处理
        common: {
            // 判断字符串是否为空
            isEmpty: function (value) {
                if (value == null || this.trim(value) == "") {
                    return true;
                }
                return false;
            },
            // 是否显示数据 为空默认为显示
            visible: function (value) {
                if (this.isEmpty(value) || value == true) {
                    return true;
                }
                return false;
            },
            // 空格截取
            trim: function (value) {
                if (value == null) {
                    return "";
                }
                return value.toString().replace(/(^\s*)|(\s*$)|\r|\n/g, "");
            },
            // 指定随机数返回
            random: function (min, max) {
                return Math.floor((Math.random() * max) + min);
            },
            startWith: function(value, start) {
                var reg = new RegExp("^" + start);
                return reg.test(value)
            },
            endWith: function(value, end) {
                var reg = new RegExp(end + "$");
                return reg.test(value)
            }
        }
    });
})(jQuery);

/** 消息状态码 */
web_status = {
    SUCCESS: 200,
    FAIL: 500
};

/** 弹窗状态码 */
modal_status = {
    SUCCESS: "success",
    FAIL: "error",
    WARNING: "warning"
};