package com.example.demo.dao;

import com.example.demo.ORMEntity.LostFound;
import java.util.List;

public interface LostFoundMapper {
    List<LostFound> queryInfoByType(Integer type);

    LostFound queryInfoById(Integer id);

    int addLostFound(LostFound lostFound);
}
