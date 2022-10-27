package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.dao.entity.WebSocketMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import javax.ws.rs.Path;

/**
 * Created by test on 2018/7/20.
 */
@RestController
public class WebSocketServerController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    /**
     * 订阅课程消息 老师订阅这个消息
     * 返回 subsribe/{courseId}/getAlreadySynced   只需要老师订阅 这个主题，学生不需要，用来通知老师 谁已经同步了
     * @param courseId
     * @param msg
     * @return
     * @throws Exception
     */
    @MessageMapping("/subscribe/teacher/{courseId}")
    @SendTo("/subscribe/getAlreadySynced")
    public WebSocketMessage subscribeTeacherMessgae(@DestinationVariable("courseId") String courseId, WebSocketMessage message) throws Exception {
        logger.info("收到客户端发来的同步的消息:"+message.getUserId()+courseId);
        return  message;
    }

    /**
     * 学生订阅信息
     * @param courseId
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/subscribe/student/{courseId}")
    @SendTo("/subscribe/getAlreadySynced")
    public WebSocketMessage subscribeStudentMessgae(@DestinationVariable("courseId") String courseId, WebSocketMessage message) throws Exception {
        logger.info("收到客户端发来的同步的消息:"+message.getUserId());
        return  message;
    }

    /**
     * 通知老师，谁订阅了课程
     * @param courseId
     * @param message
     * @return
     */
    @SubscribeMapping("/subscribe/getAlreadySynced")
    public WebSocketMessage subscribeSynced(WebSocketMessage message){
        logger.info("已经进行同步的学生:"+message.getUserId());
        return message;

    }

    /**
     * 通知学生，进行同步操作
     * @param courseId
     * @param message
     * @return
     */
    @MessageMapping("/subscribe/teacher/{courseId}/operate")
    @SendTo("/topic/subscribe/student/SyncOperate")
    public WebSocketMessage noticeSynceOperate(@DestinationVariable("courseId") String courseId,WebSocketMessage message){
        logger.info("已经进行同步的学生:"+message.getUserId());
        return message;
    }

    /**
     * 学生订阅，同步操作
     * @param courseId
     * @param message
     * @return
     */
    @SubscribeMapping("/topic/subscribe/student/SyncOperate")
    public WebSocketMessage syncOperate(WebSocketMessage message){
        logger.info("已经进行同步的学生:"+message.getUserId());
        return message;
    }
}
