package com.example.demo.service.Impl;

import com.example.demo.dao.PassageMapper;
import com.example.demo.service.PassageService;
import com.example.demo.ORMEntity.Passage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassageServiceImpl implements PassageService {

    @Autowired
    private PassageMapper passageMapper;

    @Override
    public List<Passage> queryPassage() {
        return passageMapper.queryPassage();
    }
}
