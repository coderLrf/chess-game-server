package com.example.chess.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 消息表单
 *
 * @author lrf
 */

public class MessageForm {

    /**
     * 发起者
     */
    private String from;

    /**
     * 接受者
     */
    private String to;

    /**
     * 发送的内容
     */
    private String message;

    /**
     * 消息的类型
     */
    private int type;

    /**
     * 推送的事件类型
     */
    private String event;

    /**
     * 推送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public MessageForm() {
    }

    public MessageForm(String from, String to, String message, int type) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEvent() {
        return MessageType.getEvent(this.type);
    }

    public Date getCreateTime() {
        return new Date();
    }

}
