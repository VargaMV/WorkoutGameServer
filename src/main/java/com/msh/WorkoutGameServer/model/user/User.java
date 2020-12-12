package com.msh.WorkoutGameServer.model.user;

import com.msh.WorkoutGameServer.logic.RankCalculator;
import com.msh.WorkoutGameServer.model.Rank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

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
    private Rank rank;
    //Game name / position
    private Map<String, Integer> results;

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
        this.rank = RankCalculator.calculateRank(score);
        this.results = new HashMap<>();
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
                ", rank=" + rank +
                ", results=" + results +
                '}';
    }
}
