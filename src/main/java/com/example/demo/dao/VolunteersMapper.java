package com.example.demo.dao;

import com.example.demo.ORMEntity.Volunteers;

import java.util.List;

public interface VolunteersMapper {
    List<Volunteers> queryVolunteers();

    Volunteers queryVolunteersById(Integer id);
}
