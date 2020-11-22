package com.msh.WorkoutGameServer.model.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "Users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String currentGameId = "";
    private int crowns;
    private int score;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String id, String username, String password, String currentGameId, int crowns, int score) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.currentGameId = currentGameId;
        this.crowns = crowns;
        this.score = score;
    }

    public void addCrowns(int newCrowns) {
        crowns += newCrowns;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", currentGameId='" + currentGameId + '\'' +
                ", crowns=" + crowns +
                ", score=" + score +
                '}';
    }
}
