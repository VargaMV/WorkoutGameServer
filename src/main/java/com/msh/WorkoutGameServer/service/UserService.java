package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.model.user.LoginUser;
import com.msh.WorkoutGameServer.model.user.User;

public interface UserService {
    User save(LoginUser user);

    User findByName(String name);

    boolean isUsernameTaken(LoginUser user);

    boolean isPasswordValid(LoginUser user);

    void setCurrentGameId(String name, String gameId);
}
