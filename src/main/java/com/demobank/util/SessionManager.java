package com.demobank.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class SessionManager {

    private final Map<String, String> activeSessions = new ConcurrentHashMap<>();

    public void addSession(String username, String token) {
        activeSessions.put(username, token);
    }

    public void removeSession(String username) {
        activeSessions.remove(username);
    }

    public boolean isActiveSession(String username, String token) {
        return token.equals(activeSessions.get(username));
    }

    public void clearAllSessions() {
        activeSessions.clear();
    }
}