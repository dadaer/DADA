package com.example.demo.service;

import com.example.demo.ORMEntity.LostFound;
import com.github.pagehelper.Page;


public interface LostFoundServie {
    Page<LostFound> queryInfoByType(Integer type);

    LostFound queryInfoById(Integer id);

    int addLostFound(LostFound lostFound);
}
