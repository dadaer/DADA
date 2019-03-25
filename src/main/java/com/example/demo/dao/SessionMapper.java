package com.example.demo.dao;

import com.example.demo.ORMEntity.Session;

public interface SessionMapper {
     void insertSession(Session session);
     Session querySessionBySession(String sessionId);
}
