package com.example.chess.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.chess.domain.MessageForm;
import com.example.chess.domain.MessageType;
import com.example.chess.service.IWebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * webSocket支撑类
 *
 * @author lrf
 */

public class WebSocketServerSupport implements IWebSocketServer {
    
    private static final Logger log = LoggerFactory.getLogger(WebSocketServerSupport.class);

    /**
     * 静态变量，用来记录当前在线连接数，应该把它设置线程安全的
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全set，用来存放每个客户端对应的webSocket对象
     */
    private static final Map<String, WebSocketServerSupport> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收userId
     */
    private String userId;

    @OnOpen
    @Override
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
        } else {
            // 更新在线人数
            addOnlineCount();
        }
        webSocketMap.put(userId, this);
        log.info("用户连接：" + userId + "，当前在线人数为：" + getOnlineCount());
        try {
            String message = "您的好友`%s`上线了";
            // 广播好友上线信息
            for (String key : webSocketMap.keySet()) {
                if (!key.equals(userId)) {
                    webSocketMap.get(key).send(userId, String.format(message, userId), MessageType.MESSAGE);
                }
            }
        } catch (IOException e) {
            log.error("用户：" + userId + "，网络异常！！！");
        }

    }

    @OnMessage
    @Override
    public void onMessage(String message, Session session) {
        log.info("用户消息：" + userId + "，报文：" + message);
        // 可以群发消息
        // 消息保存到数据库，redis
        if (message != null) {
            try {
                // 解析发送的报文
                MessageForm form = JSONObject.parseObject(message, MessageForm.class);
                if (form != null && form.getTo() != null) {
                    if (webSocketMap.containsKey(form.getTo())) {
                        if (form.getMessage() == null) {
                            MessageType messageType = MessageType.get(form.getType());
                            if (messageType != null) {
                                form.setMessage(String.format(messageType.getContent(), form.getFrom()));
                            }
                        }
                        webSocketMap.get(form.getTo()).send(form);
                    } else {
                        log.error("请求的userId" + form.getTo() + "不在该服务器上");
                    }
                } else {
                    log.error("解析报文失败，" + message.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    @Override
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            // 从set中删除
            subOnlineCount();
        }
        try {
            String message = "您的好友`%s`下线了";
            // 广播好友下线信息
            for (String key : webSocketMap.keySet()) {
                webSocketMap.get(key).send(userId, String.format(message, userId), MessageType.CLOSE_CONNECTION);
            }
            log.info("用户退出：" + userId + "，当前在线人数为：" + getOnlineCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnError
    @Override
    public void onError(Session session, Throwable error) {
        log.error("用户错误：" + this.userId + "，原因：" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务端主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 服务端主动推送内容
     * @param to
     * @param message
     * @param type
     * @throws IOException
     */
    public void send(String to, String message, MessageType type) throws IOException {
        // 推送信息
        if (type.getCode() != MessageType.MESSAGE.getCode()) {
            message = String.format(type.getContent(), to);
        }
        send(new MessageForm("服务器", to, message, type.getCode()));
    }

    /**
     * 实现服务端主动推送
     */
    public void send(MessageForm form) throws IOException {
        this.session.getBasicRemote().sendText(JSONObject.toJSONString(form));
    }

    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        log.info("发送消息到：" + userId + "，报文：" + message);
        if (userId != null && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessage(message);
        } else {
            log.error("用户" + userId + "，不在线！");
        }
    }
    
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
    
    public static synchronized void addOnlineCount() {
        WebSocketServerSupport.onlineCount ++;
    }
    
    public static synchronized void subOnlineCount() {
        WebSocketServerSupport.onlineCount --;
    }

    /**
     * 获取所有在线用户列表
     */
    public synchronized Set<String> getOnlineUserList() {
        return webSocketMap.keySet();
    }

    /**
     * 获取好友列表
     */
    public synchronized Set<String> getUserList(String userId) {
        return getOnlineUserList().stream().filter(user -> !user.equals(userId)).collect(Collectors.toSet());
    }

    /**
     * 获取webSocketMap
     */
    public Map<String, WebSocketServerSupport> getWebSocketMap() {
        return webSocketMap;
    }
}
