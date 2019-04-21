package com.example.demo.service.Impl;

import com.example.demo.ORMEntity.LostFound;
import com.example.demo.dao.LostFoundMapper;
import com.example.demo.service.LostFoundServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LostFoundServiceImpl implements LostFoundServie {

    @Autowired
    private LostFoundMapper lostFoundMapper;

    @Override
    @Transactional
    public List<LostFound> queryInfoByType(Integer type) {
        return lostFoundMapper.queryInfoByType(type);
    }

    @Override
    @Transactional
    public LostFound queryInfoById(Integer id) {
        return lostFoundMapper.queryInfoById(id);
    }

    @Override
    @Transactional
    public int addLostFound(LostFound lostFound) {
        return lostFoundMapper.addLostFound(lostFound);
    }
}
