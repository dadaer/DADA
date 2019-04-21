package com.example.demo.dao;

import com.example.demo.ORMEntity.LostFound;
import com.example.demo.ORMEntity.LoveWall;

import java.util.List;

public interface LoveWallMapper {
    List<LoveWall> queryInfos();

    int addLoveWall(LoveWall loveWall);
}
