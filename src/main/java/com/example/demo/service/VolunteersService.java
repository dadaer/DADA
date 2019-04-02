package com.example.demo.service;

import com.example.demo.ORMEntity.Volunteers;

import java.util.List;

public interface VolunteersService {
    List<Volunteers> queryVolunteers();

    Volunteers queryVolunteersById(Integer id);
}
