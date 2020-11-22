package com.msh.WorkoutGameServer.model.user;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginUser {
    private String username;
    private String password;

    public LoginUser() {
    }

    public LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
