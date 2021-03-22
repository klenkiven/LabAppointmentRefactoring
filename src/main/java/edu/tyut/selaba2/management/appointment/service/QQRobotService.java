package edu.tyut.selaba2.management.appointment.service;

import com.alibaba.fastjson.JSONObject;
import edu.tyut.selaba2.management.appointment.constant.QQRobotConstant;
import edu.tyut.selaba2.management.appointment.domain.Guest;
import edu.tyut.selaba2.management.appointment.domain.SpaceUsageRecord;
import edu.tyut.selaba2.management.appointment.utils.MD5Util;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.sql.Timestamp;

/**
 * QQ机器人服务端
 * @author Antares
 *
 * QQ机器人的客户端服务
 * @author KlenKiven
 */
@Service
public class QQRobotService {

    /**
     * 向QQ机器人发送普通消息
     *
     * @param message 发送的消息
     * @return 返回QQ机器人状态
     */
    public int messagePost(String message) {
        return messagePost(message, null);
    }

    /**
     * 向QQ机器人发送消息，可以指定任何人
     *
     * @param message 发送的消息
     * @param target 指定发送者的列表
     * @return 返回QQ机器人状态
     */
    public int messagePost(String message, Long[] target) {

        // 请求JSON生成
        JSONObject requestContext = new JSONObject();
        requestContext.put("message", message);
        requestContext.put("at", target);

        // 获得响应
        JSONObject responseJson = executePostRequest(QQRobotConstant.SEND_MESSAGE, requestContext);

        // 服务器请求处理
        int serverState = Integer.parseInt((String)responseJson.get("code").toString());
        String msg = (String) responseJson.get("msg");
        System.out.println("[INFO] QQ Robot Message: " + msg);
        System.out.println("[INFO] QQ Robot Response Code: " + serverState);

        return QQRobotConstant.SERVER_OK;
    }

    /**
     * 内部方法 用于 POST 请求，进一步降低程序的耦合度
     *
     * @param router 访问路由
     * @param requestContext 请求内容
     * @return 响应内容
     */
    private JSONObject executePostRequest(String router, JSONObject requestContext) {

        JSONObject responseJson = null;

        // 计算token
        String token = MD5Util.getHash(QQRobotConstant.SECRET);

        // 发送HTTP请求
        HttpPost postRequest = new HttpPost(QQRobotConstant.URL + router + "/?token=" + token);
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        try {
            // 请求处理
            String requestJson = requestContext.toJSONString();
            StringEntity entity = new StringEntity(requestJson, "UTF-8");
            postRequest.setEntity(entity);

            // HTTP客户端执行请求
            CloseableHttpClient client = HttpClients.createDefault();
            HttpResponse response = client.execute(postRequest);

            // HTTP客户端解析响应
            int statusCode = response.getStatusLine().getStatusCode();
            String responseEntity = EntityUtils.toString(response.getEntity());// 响应内容解析
            if (statusCode != HttpStatus.SC_OK) {  // 处理错误码
                System.err.println("[Error] QQ机器人 服务错误");
            }
            responseJson = JSONObject.parseObject(responseEntity);

        } catch (IOException e) {
            System.err.println("[Error] QQ机器人 服务器访问错误");
        }

        return responseJson;
    }

    /**
     * 创建一个消息
     *
     * @param guest 访客信息
     * @param spaceUsageRecord 空间使用信息
     * @return 返回一个字符串
     */
    public String composeMessage(Guest guest, SpaceUsageRecord spaceUsageRecord) {

        String msg = "【预约通知】"+ guest.getName() + "预约了实验室\n" +
                "\t当前时间为" + new Timestamp(System.currentTimeMillis()) + "\n" +
                "【预约人信息】\n" +
                "\t姓名：" + guest.getName() + "\n" +
                "\t工号：" + guest.getGuestId() + "\n" +
                "\t联系方式：" + guest.getPhoneNum() + "\n" +
                "【预约信息】\n" +
                "\t预约开始时间：" + spaceUsageRecord.getStartTime() + "\n" +
                "\t预约时长：" + (spaceUsageRecord.getEndTime().getTime() - spaceUsageRecord.getStartTime().getTime())/1000/60 + "分钟\n" +
                "\t预约人数：" + spaceUsageRecord.getPeopleNum() + "人\n" +
                "";

        return msg;
    }
}
