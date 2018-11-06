package com.floatShiro.mapper;

import com.floatShiro.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapping {
    User findByUserName(@Param("username")String username);
}
