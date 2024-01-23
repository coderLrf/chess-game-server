package com.example.chess.domain;

/**
 * 消息类型
 * 
 * @author lrf
 */
public enum MessageType {

    /**
     * 发起挑战
     */
    CHALLENGE(1, "发起挑战", "`%s`玩家向您发起了挑战", "challengeEvent"),

    /**
     * 接收挑战
     */
    TAKE_CHALLENGE(2, "接收挑战", "`%s`玩家接收了您的挑战", "takeChallengeEvent"),

    /**
     * 拒绝挑战
     */
    REJECT_CHALLENGE(3, "拒绝挑战", "`%s`玩家拒绝了您的挑战", "rejectChallengeEvent"),

    /**
     * 挑战者忙
     */
    BUSY(4, "挑战者忙", "`%s`玩家目前处于状态忙，请选择其他挑战者", "busyEvent"),

    /**
     * 走棋
     */
    MOVE(5, "走棋", null, "moveEvent"),

    /**
     * 认输
     */
    GIVE_UP(6, "认输", "`%s`玩家认输了，您赢了", "giveUpEvent"),

    /**
     * 聊天消息
     */
    MESSAGE(8, "聊天消息", null, "messageEvent"),

    /**
     * 面將
     */
    ISFACE(9, "面將", "`%s`玩家面將了", "isfaceEvent"),

    /**
     * 请求悔棋
     */
    PULL_BACK(10, "悔棋", "`%s`玩家请求悔棋", "pullBackEvent"),

    /**
     * 拒绝悔棋
     */
    REJECT_PULL_BACK(11, "拒绝悔棋", "`%s`玩家拒绝您的悔棋", "rejectPullBackEvent"),

    /**
     * 同意悔棋
     */
    RESOLVE_PULL_BACK(12, "同意悔棋", "`%s`玩家同意您的悔棋", "resolvePullBackEvent"),

    /**
     * 断开连接
     */
    CLOSE_CONNECTION(13, "断开连接", "`%s`玩家与服务器失去连接", "closeConnectionEvent")
    ;

    MessageType(int code, String title, String content, String event) {
        this.code = code;
        this.title = title;
        this.content = content;
        this.event = event;
    }

    private int code;
    
    private String title;
    
    private String content;
    
    private String event;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
    
    public static MessageType get(int code) {
        for (MessageType type : values()) {
            if (code == type.getCode()) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据code获取事件类型
     */
    public static String getEvent(int code) {
        MessageType type = get(code);
        if (type != null) {
            return type.getEvent();
        }
        return null;
    }
}
