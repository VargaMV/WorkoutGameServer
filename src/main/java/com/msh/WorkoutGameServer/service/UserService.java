package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.model.user.LoginUser;
import com.msh.WorkoutGameServer.model.user.User;

import java.util.List;

public interface UserService {
    User save(LoginUser user);

    User findByName(String name);

    List<User> findAll();

    boolean isPasswordValid(LoginUser user);

    void setCurrentGameId(String name, String gameId);

    void deleteByName(String name);

    void deleteAll();
}
