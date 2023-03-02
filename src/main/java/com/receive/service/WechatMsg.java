package com.receive.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.entity.DataEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: ohhhh
 * @description: WechatMsg
 * @author: wangnie
 * @create: 2022-06-24 09:31
 **/


@Component
public class WechatMsg {

    @Value("${WeChat.appId}")
    private String appId;

    @Value("${WeChat.appsecret}")
    private String appsecret;

    @Value("${WeChat.msgTemplate}")
    private String msgTemplate;


    public static void main(String[] args) {

    }
    /**
     * 获取token
     *
     * @return token
     */
    public String getToken() {
        // 授予形式
        String grant_type = "client_credential";
        //应用ID
        String appid = appId;
        //密钥
        String secret = appsecret;
        // 接口地址拼接参数
        String getTokenApi = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" + grant_type + "&appid=" + appid
                + "&secret=" + secret;
        String tokenJsonStr = doGetPost(getTokenApi, "GET", null);
        JSONObject tokenJson = JSONObject.parseObject(tokenJsonStr);
        String token = tokenJson.get("access_token").toString();
        System.out.println("获取到的TOKEN : " + token);
        return token;
    }
    /***
     * 发送消息
     *
     * @param token
     */
    public void SendWeChatMsg(String token,String name,String unitName,String remark) {
        // 接口地址
        String sendMsgApi = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
        //openId
        String toUser = "oqBFR6AMeaAKjpyt8Jb4eNk74UoI";
        //消息模板ID
        String template_id = msgTemplate;
        //整体参数map
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //消息主题显示相关map
        Map<String, Object> dataMap = new HashMap<String, Object>();
        //根据自己的模板定义内容和颜色
        dataMap.put("first",new DataEntity(name,"#173177"));
        dataMap.put("keyword1",new DataEntity(unitName,"#173177"));
        dataMap.put("remark",new DataEntity(remark,"#173177"));
        paramMap.put("touser", toUser);
        paramMap.put("template_id", template_id);
        paramMap.put("data", dataMap);
        System.out.println(doGetPost(sendMsgApi,"POST",paramMap));
    }
    /**
     * 调用接口 post
     * @param apiPath
     */
    public String doGetPost(String apiPath,String type,Map<String,Object> paramMap){
        OutputStreamWriter out = null;
        InputStream is = null;
        String result = null;
        try{
            URL url = new URL(apiPath);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod(type) ; // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            if(type.equals("POST")){
                out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
                out.append(JSON.toJSONString(paramMap));
                out.flush();
                out.close();
            }
            // 读取响应
            is = connection.getInputStream();
            int length = (int) connection.getContentLength();// 获取长度
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                result = new String(data, "UTF-8"); // utf-8编码
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  result;
    }
}
