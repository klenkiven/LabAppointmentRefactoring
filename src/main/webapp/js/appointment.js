//UI前端设计请直接调用此方法，并对TODO内容进行修改即可使用
let timeoutID=-1
function appointment(){
    let id=$("#form_id").val()
    let name=$("#form_name").val()
    let tel=$("#form_tel").val()
    let beginTime=$("#form_beginTime").val()
    let endTime=$("#form_endTime").val()
    let userCount=$("#form_userCount").val()
    let verification=$("#form_verification").val()
    let date=$("#form_date").val()
    let checkinResult=checkinAppointmentAttr(id,name,tel,date,beginTime,endTime,userCount)
    if(checkinResult===true){
        sendAppointmentAjaxRequest(id,name,tel,date,beginTime,endTime,userCount,verification);
        let button=$("#form_button")
        button.attr("disabled","disabled")
        button.attr("value","正在预约");
        timeoutID=setTimeout(function(){
            button.removeAttr("disabled")
            button.attr("value","点击预约");
        },10000)
    }
}

function checkinAppointmentAttr(id,name,tel,date,beginTime,endTime,userCount){
    //验证id
    let id_regexp=/^[0-9]{10}$/
    if(id.search(id_regexp)!==0){
        alert("您填写的学号/工号不正确，请输入正确的学号/工号,长度应为10位")
        return false
    }
    //验证name
    let name_regexp=/^[\u4e00-\u9fa5]{2,4}$/
    if(name.search(name_regexp)!==0){
        alert("您填写的姓名不正确，请填写中文名字")
        return false
    }
    //验证tel
    let tel_regexp=/^[0-9]{11}$/
    if(tel.search(tel_regexp)!==0){
        alert("您的联系方式不正确，请正确填写您的手机号码，共11位")
        return false
    }
    //验证userCount
    if(userCount===""||parseInt(userCount)<1){
        alert("您的填写的申请使用人数不正确，请填入一个正整数")
        return false
    }
    //验证date
    let date_regexp= /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/
    if(date.search(date_regexp)!==0){
        alert("您的申请使用日期格式不正确\n正确的日期格式为yyyy-MM-dd")
        return false
    }
    if(parseInt(date.substr(0,4))>=2038){
        alert("您的申请使用日期不合法，请修改为正常的日期")
        return false
    }
    //验证beginTime/endTime
    let time_regexp=/^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])$/;
    if(beginTime.search(time_regexp)!==0){
        alert("您的开始使用时间格式不正确\n正确的时间格式为HH:mm\n(24小时制)")
        return false
    }
    if(endTime.search(time_regexp)!==0){
        alert("您的结束使用时间格式不正确\n正确的时间格式为HH:mm\n(24小时制)")
        return false
    }
    return true
}

function sendAppointmentAjaxRequest(id,name,tel,date,beginTime,endTime,userCount,verification){
    $.ajax({
        url:"api/appointment",
        type:"post",
        timeout:10000,
        data:{
            id:id,
            name:name,
            tel:tel,
            beginTime:""+date+" "+beginTime,
            endTime:""+date+" "+endTime,
            userCount:userCount,
            verification:verification
        },
        error:function(XMLHttpRequest, textStatus){
            alert("很抱歉\n请求/响应时发生了错误\n"+textStatus)
        },
        success:function(data, textStatus){
            let status=data.status
            switch (status){
                case 0:
                    alert("预约成功\n请于"+date+" "+beginTime+"\n开始使用实验室")
                    let button=$("#form_button")
                    button.attr("disabled","disabled")
                    button.attr("value","预约成功！");
                    clearTimeout(timeoutID)
                    break;
                case 2:
                    alert("抱歉\n您预约的时间冲突,请尝试其他时间")
                    break;
                case 3:
                    alert("抱歉\n您提交的姓名未能通过验证，请修改重试")
                    break;
                case 4:
                    alert("抱歉\n您的手机号码不合法或您输入的手机号和之前记录的手机号不符，请修改重试")
                    break;
                case 5:
                    alert("抱歉\n您预约的时间不合法，请修改重试")
                    break;
                case 6:
                    alert("抱歉\n您填写的使用人数不合法，请修改重试")
                    break;
                case 7:
                    alert("抱歉\n您的验证码不正确，请联系实验室获取验证码")
                    break;
                case 8:
                    alert("抱歉\n您的工号/学号不正确，请修改重试")
                    break;
                case 9:
                    alert("抱歉\n您申请的使用时间段暂时不能预约，请选择其他时间")
                    break;
                case 10:
                    alert("抱歉\n您申请的使用时间过长，请缩短您的使用时间")
                    break;
                case 11:
                    alert("抱歉\n您申请的使用人数过多，请减少申请人数")
                    break;
                case 12:
                    alert("抱歉\n您的申请中存在错误，请修改后重试")
                    break;
                case -1:
                    alert("很抱歉\n服务器错误")
                    break;
                default:
                    alert("很抱歉\n发生了未知错误")
                    break;
            }
        }
    })
}
function autoGetVerification(){
    let $verification=$("#form_verification")
    $.ajax({
        url:"api/getVerification",
        type:"get",
        timeout:3000,
        success:function(data){
            $verification.val(data);
        }
    })
}
