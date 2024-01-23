package com.example.chess.service;

import javax.websocket.Session;

/**
 * websocket 接口服务
 * 
 * @author lrf
 */
public interface IWebSocketServer {

    /**
     * 连接建立成功调用
     */
    void onOpen(Session session, String userId);

    /**
     * 收到客户端消息后调用
     */
    void onMessage(String message, Session session);

    /**
     * 连接关闭调用
     */
    void onClose();

    /**
     * 发生错误/异常时调用    
     */
    void onError(Session session, Throwable error);
}
