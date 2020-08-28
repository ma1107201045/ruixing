package com.yintu.ruixing.websocket;

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
        for (Integer s : webSocketMap.keySet()) {
            System.out.println(webSocketMap.get(s).size());
        }
    }

    @OnMessage
    public void onMessage(String message) {
        try {
            this.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param error 错误信息
     */
    @OnError
    public void onError(Throwable error) {
        logger.error("onError.................错误消息：" + error.getMessage());
    }

    @OnClose
    public void onClose() {
        for (Integer key : webSocketMap.keySet()) {
            if (key.equals(this.meetingNum)) {
                Set<Session> sessions = webSocketMap.get(key);
                for (Session session : sessions) {
                    if (session.equals(this.session)) {
                        sessions.remove(session);
                        break;
                    }
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
    public void sendMessage(String message) throws IOException {
        logger.info("发送者sessionId：" + this.session.getId());
        Set<Integer> keys = webSocketMap.keySet();
        for (Integer key : keys) {
            if (key.equals(this.meetingNum)) {
                Set<Session> sessions = webSocketMap.get(key);
                for (Session session : sessions) {
                    if (this.session != session) {
                        session.getBasicRemote().sendText(message);
                        logger.info("转发者sessionId：" + session.getId() + message);
                    }
                }
                break;
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
