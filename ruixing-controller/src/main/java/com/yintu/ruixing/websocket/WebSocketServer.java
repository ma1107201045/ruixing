package com.yintu.ruixing.websocket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:mlf
 * @date:2020/6/1 14:00
 */

//@Component
//@ServerEndpoint("/websocket")
public class WebSocketServer {
//    private final static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
//    /**
//     * 存放不同连接的回话信息以及额外的信息
//     */
//    public static Map<String, SessionInfo> webSocketSession = new ConcurrentHashMap<>();
//
//    public WebSocketServer() {
//        logger.info("WebSocketServer初始化.......");
//    }
//
//    @OnOpen
//    public void onOpen(Session session) {
//        logger.info("onOpen..............");
//        SessionInfo sessionInfo = new SessionInfo();
//        sessionInfo.setSession(session);
//        webSocketSession.put(session.getId(), sessionInfo);
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose(Session session) {
//        logger.info("onClose..............");
//        String sessionId = session.getId();
//        SessionInfo sessionInfo = webSocketSession.get(sessionId);
//        if (sessionInfo != null && sessionInfo.getSession() != null) {
//            try {
//                sessionInfo.getSession().close();
//            } catch (IOException e) {
//                logger.error(e.getMessage());
//                e.printStackTrace();
//            } finally {
//                webSocketSession.remove(session.getId());
//            }
//        }
//
//    }
//
//    /**
//     * @param session 每个会话
//     * @param error   错误信息
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//        logger.error("onError.................错误消息：" + error.getMessage());
//    }
//
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param message 客户端发送过来的消息
//     */
//    @OnMessage
//    public void onMessage(Session session, String message) {
//        logger.info("onMessage.............." + message);
//        String sessionId = session.getId();
//        SessionInfo sessionInfo = webSocketSession.get(sessionId);
//        JSONObject jo = JSONObject.parseObject(message);
//        JSONArray ja = jo.getJSONArray("stationIds");
//        if (sessionInfo != null) {
//            sessionInfo.setCid(ja.toJavaObject(Integer[].class));
//        }
//
//    }
//
//    /**
//     *
//     * @param cheZhanId  车站id
//     * @param taskId  任务id
//     */
//    public void sendMessage(Integer cheZhanId, Integer taskId) {
//        for (String sessionId : webSocketSession.keySet()) {
//            SessionInfo s = webSocketSession.get(sessionId);
//            if (s != null && s.getCid() != null) {
//                for (Integer cid : s.getCid()) {
//                    if (cid != null && cid.equals(cheZhanId)) {
//                        Session session = s.getSession();
//                        if (session != null && session.isOpen()) {
//                            try {
//                                JSONObject jo = new JSONObject();
//                                jo.put("taskId", taskId);
//                                session.getBasicRemote().sendText(jo.toJSONString());
//                                logger.info("sendMessage.............." + jo);
//                            } catch (IOException e) {
//                                logger.error(e.getMessage());
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//    }


}
