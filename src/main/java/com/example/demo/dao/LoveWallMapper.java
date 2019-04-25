package com.example.demo.dao;

import com.example.demo.ORMEntity.LoveWall;
import com.github.pagehelper.Page;

public interface LoveWallMapper {
    Page<LoveWall> queryInfos();

    int addLoveWall(LoveWall loveWall);
}
