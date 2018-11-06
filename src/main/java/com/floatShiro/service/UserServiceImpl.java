package com.floatShiro.service;


import com.floatShiro.mapper.UserMapping;
import com.floatShiro.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService{

    @Resource
    public UserMapping userMapping;

    @Override
    public User findByUserName(String username) {
        return userMapping.findByUserName(username);
    }
}
