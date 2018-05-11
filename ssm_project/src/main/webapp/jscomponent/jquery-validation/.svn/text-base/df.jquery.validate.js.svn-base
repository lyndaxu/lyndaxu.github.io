$(function () {
    //加载样式
    $('<style type="text/css">.form-validator-error {background: none repeat scroll 0 0 lightPink; }</style>').appendTo('head');
    //初始化validate
    $.validator.setDefaults({
        submitHandler:function (form) {
            form.submit();
        },
        errorPlacement:function (error, element) {
            if (element.is(':radio') || element.is(':checkbox')) { //如果是radio或checkbox
                var eid = element.attr('name'); //获取元素的name属性
                var type = element.is(':radio') ? 'radio' : 'checkbox';
                var r = $(':' + type + '[name="' + eid + '"]');
                r.parent().addClass('form-validator-error');
                try {
                    r.parent().poshytip('destroy');
                } catch (e) {
//                            alert(e);
                }
                if (error.html() != undefined && $.trim(error.html()) != '') {
                    r.parent().poshytip({
                        content:error.html(),
                        className:'tip-yellowsimple'
                    });
                }
            } else if (element.is('select')) {
                if (element.val() == '')
                    element.addClass('form-validator-error');
                try {
                    element.parent().poshytip('destroy');
                } catch (e) {
                }
                if (error.html() != undefined && $.trim(error.html()) != '') {
                    element.parent().poshytip({
                        content:error.html(),
                        className:'tip-yellowsimple'
                    });
                }
            } else if (element.is(':text') || element.is('textarea') || element.is(':password') || element.is(':file')) {
                $(element).addClass('form-validator-error');
                try {
                    $(element).poshytip('destroy');
                } catch (e) {
//                            alert(e);
                }
                if (error.html() != undefined && $.trim(error.html()) != '') {
                    $(element).poshytip({
                        content:error.html(),
                        className:'tip-yellowsimple'
                    });
                }
            }

        },
        success:function (lable, element) {
            if (element.is(':text') || element.is('textarea') || element.is(':password') || element.is(':file')) {
                $(element).removeClass("form-validator-error");
                try {
                    $(element).poshytip('destroy');
                } catch (e) {
//                            alert(e);
                }
            } else if (element.is(':radio') || element.is(':checkbox')) {
                var type = element.is(':radio') ? 'radio' : 'checkbox';
                var r = $(':' + type + '[name="' + element.attr('name') + '"]');
                r.parent().removeClass('form-validator-error');
                r.parent().poshytip('destroy');
            } else if (element.is('select')) {
                if (element.hasClass("form-validator-error")) {
                    $(element).removeClass("form-validator-error");
                }
                try {
                    element.parent().poshytip('destroy');
                } catch (e) {
//                            alert(e);
                }
            }
        }
    });
    try{
        $.metadata.setType("attr", "validate");//message内容根据name
    }catch (e){}
});