package com.example.chess.server;

import com.example.chess.service.impl.WebSocketServerSupport;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;

/**
 * websocket服务
 *
 * @author lrf
 */

@ServerEndpoint("/webSocket/{userId}")
@Service
public class WebSocketServer extends WebSocketServerSupport {
}
