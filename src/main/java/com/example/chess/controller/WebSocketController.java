package com.example.chess.controller;

import com.example.chess.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Set;

/**
 * webSocket前端控制器
 *
 * @author lrf
 */

@RestController
@RequestMapping("/web")
public class WebSocketController {
    
    @Autowired
    private WebSocketServer socketServer;

    /**
     * 获取在线用户列表
     */
    @GetMapping("/onlineList")
    public Set<String> getOnlineList() {
        return socketServer.getOnlineUserList();
    }

    /**
     * 检测用户id是否已被使用
     */
    @GetMapping("/{userId}")
    public boolean checkUserId(@PathVariable("userId") String userId) {
        return socketServer.getWebSocketMap().containsKey(userId);
    }

    /**
     * 获取好友用户列表
     */
    @GetMapping("/userList/{userId}")
    public Set<String> getUserList(@PathVariable("userId") String userId) {
        return socketServer.getUserList(userId);
    }

}
