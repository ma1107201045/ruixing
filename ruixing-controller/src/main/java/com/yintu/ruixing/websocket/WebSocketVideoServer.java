package com.yintu.ruixing.websocket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint("/websocket/video/{meetingNum}")
public class WebSocketVideoServer {
    private final static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static final Map<Integer, Set<Session>> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 会议号
     */
    private Integer meetingNum;

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;


    public WebSocketVideoServer() {
        logger.info("WebSocketVideoServer初始化.......");
    }

    @OnOpen
    public void onOpen(@PathParam("meetingNum") Integer meetingNum, Session session) {
        this.meetingNum = meetingNum;
        this.session = session;
        boolean flag = false;
        for (Integer key : webSocketMap.keySet()) {
            if (key.equals(meetingNum)) {
                Set<Session> sessions = webSocketMap.get(key);
                sessions.add(session);
                flag = true;
                break;
            }
        }
        if (!flag) {
            Set<Session> sessions = new HashSet<>();
            sessions.add(session);
            webSocketMap.put(meetingNum, sessions);
            logger.info("创建会议成功，房间号成功" + meetingNum);
        }
        //发送房间人数
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        for (Integer s : webSocketMap.keySet()) {
            if (s.equals(meetingNum)) {
                Set<Session> sessions = webSocketMap.get(s);
                for (Session session1 : sessions) {
                    if (session1.getId().equals(session.getId())) {
                        jo.put("userId", session1.getId());
                    } else {
                        ja.add(session1.getId());
                    }
                }
                break;
            }
        }
        JSONObject data = new JSONObject();
        data.put("type", "init");
        jo.put("data", data);
        jo.put("userIds", ja);
        try {
            this.session.getBasicRemote().sendText(jo.toJSONString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message) {
        this.sendMessage(message);
    }

    /**
     * @param error 错误信息
     */
    @OnError
    public void onError(Throwable error) {
        logger.error("onError.................错误消息：" + error.getMessage());
    }

    @OnClose
    public void onClose() throws IOException {
        for (Integer key : webSocketMap.keySet()) {
            if (key.equals(this.meetingNum)) {
                Set<Session> sessions = webSocketMap.get(key);
                for (Session session : sessions) {
                    if (session.equals(this.session)) {
                        sessions.remove(session);
                        logger.info("onClose.................id：" + this.session.getId());
                        break;
                    }
                }
                for (Session session : sessions) {
                    JSONObject jo = new JSONObject();
                    JSONObject data = new JSONObject();
                    data.put("type", "close");
                    jo.put("data", data);
                    jo.put("fromOfUserId", this.session.getId());
                    session.getBasicRemote().sendText(jo.toJSONString());
                }
                if (sessions.isEmpty()) {
                    webSocketMap.remove(key);
                }
                break;
            }
        }
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) {
        Set<Integer> keys = webSocketMap.keySet();
        for (Integer key : keys) {
            if (key.equals(this.meetingNum)) {
                Set<Session> sessions = webSocketMap.get(key);
                JSONObject jo = JSONObject.parseObject(message);
                String messageType = jo.getJSONObject("data").getString("type");
                if ("offer".equals(messageType) || "candidate".equals(messageType) || "answer".equals(messageType)) {
                    for (Session session : sessions) {
                        try {
                            if (session.getId().equals(jo.getString("toOfUserId"))) {
                                synchronized (session) {
                                    session.getBasicRemote().sendText(jo.toJSONString());
                                }
                                logger.info("消息类型" + messageType);
                                logger.info("发送者id:" + this.session.getId());
                                logger.info("接受者id:" + session.getId());
                                break;
                            }
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    }
                }
            }
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketVideoServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketVideoServer.onlineCount--;
    }
}
