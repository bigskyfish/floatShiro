package com.floatShiro.service;

import com.floatShiro.model.User;

public interface UserService {


    User findByUserName(String username);
}
