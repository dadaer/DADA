package com.example.demo.dao;

import com.example.demo.ORMEntity.LostFound;
import com.github.pagehelper.Page;

public interface LostFoundMapper {
    Page<LostFound> queryInfoByType(Integer type);

    LostFound queryInfoById(Integer id);

    int addLostFound(LostFound lostFound);
}
