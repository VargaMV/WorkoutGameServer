package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.database.UserDataAccess;
import com.msh.WorkoutGameServer.model.user.LoginUser;
import com.msh.WorkoutGameServer.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDataAccess userDataAccess;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Override
    public User save(LoginUser loginUser) {
        if (!isUsernameTaken(loginUser)) {
            User newUser = new User(
                    loginUser.getUsername(),
                    bcryptEncoder.encode(loginUser.getPassword())
            );
            return userDataAccess.save(newUser);
        }
        return null;
    }

    @Override
    public User findByName(String name) {
        return userDataAccess.findByUsername(name);
    }

    @Override
    public boolean isUsernameTaken(LoginUser loginUser) {
        return (userDataAccess.findByUsername(loginUser.getUsername()) != null);
    }

    @Override
    public boolean isPasswordValid(LoginUser loginUser) {
        User user = findByName(loginUser.getUsername());
        if (user != null) {
            return bcryptEncoder.matches(loginUser.getPassword(), user.getPassword());
        }
        return false;
    }

    @Override
    public void setCurrentGame(String name, String gameId) {
        User user = findByName(name);
        user.setCurrentGameId(gameId);
        userDataAccess.save(user);
    }
}
