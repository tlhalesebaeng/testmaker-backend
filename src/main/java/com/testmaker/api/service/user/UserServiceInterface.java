package com.testmaker.api.service.user;

import com.testmaker.api.entity.User;

public interface UserServiceInterface {
    User getByUsername(String username);
}
