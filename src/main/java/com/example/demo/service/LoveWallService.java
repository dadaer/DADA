package com.example.demo.service;

import com.example.demo.ORMEntity.LoveWall;
import com.github.pagehelper.Page;

public interface LoveWallService {
    Page<LoveWall> queryInfos();

    int addLoveWall(LoveWall loveWall);

}
