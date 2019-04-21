package com.example.demo.service;

import com.example.demo.ORMEntity.LoveWall;

import java.util.List;

public interface LoveWallService {
    List<LoveWall> queryInfos();

    int addLoveWall(LoveWall loveWall);

}
