package com.example.demo.service.Impl;

import com.example.demo.ORMEntity.LoveWall;
import com.example.demo.dao.LoveWallMapper;
import com.example.demo.service.LoveWallService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LoveWallServiceImpl implements LoveWallService {

    @Autowired
    private LoveWallMapper loveWallMapper;

    @Override
    @Transactional
    public Page<LoveWall> queryInfos() {
        return loveWallMapper.queryInfos();
    }

    @Override
    @Transactional
    public int addLoveWall(LoveWall loveWall) {
        return loveWallMapper.addLoveWall(loveWall);
    }
}
