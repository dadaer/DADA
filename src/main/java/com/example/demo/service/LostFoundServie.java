package com.example.demo.service;

import com.example.demo.ORMEntity.LostFound;

import java.util.List;

public interface LostFoundServie {
    List<LostFound> queryInfoByType(Integer type);

    LostFound queryInfoById(Integer id);

    int addLostFound(LostFound lostFound);
}
