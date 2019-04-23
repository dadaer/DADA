package com.example.demo.dao;

import com.example.demo.ORMEntity.User;


public interface UserMapper {
    User queryUserByStuNum(String stuNum);

    int addUser(User user);
}
