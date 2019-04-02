package com.example.demo.service.Impl;

import com.example.demo.ORMEntity.Volunteers;
import com.example.demo.dao.VolunteersMapper;
import com.example.demo.service.VolunteersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteersServiceImpl implements VolunteersService {

    @Autowired
    private VolunteersMapper volunteersMapper;


    @Override
    public List<Volunteers> queryVolunteers() {
        return volunteersMapper.queryVolunteers();
    }

    @Override
    public Volunteers queryVolunteersById(Integer id) {
        return volunteersMapper.queryVolunteersById(id);
    }
}
