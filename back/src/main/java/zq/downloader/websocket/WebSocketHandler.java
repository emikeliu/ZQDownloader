package zq.downloader.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import zq.downloader.service.DownloadService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class WebSocketHandler implements org.springframework.web.socket.WebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();
    private Timer timer;

    private ObjectMapper mapper;

    private DownloadService service;

    @Autowired
    public void setService(DownloadService service) {
        this.service = service;
    }

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("OK"));
        sessions.add(session);
        startTimer();
    }

    private void startTimer() {
        if (timer != null) return;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pushInfo();
            }
        }, 2000, 2000);
    }

    private void pushInfo() {
        for (WebSocketSession session : sessions) {
            try {
                if (session != null)
                    session.sendMessage(new TextMessage(mapper.writeValueAsString(service.queryAllTasks())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.close();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);
        updateTimerStatus();
    }

    private void updateTimerStatus() {
        if (sessions.isEmpty()) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
