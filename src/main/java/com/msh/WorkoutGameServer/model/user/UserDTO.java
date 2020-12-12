package com.msh.WorkoutGameServer.model.user;

import com.msh.WorkoutGameServer.model.Rank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "Users")
public class UserDTO {
    private String username;
    private String currentGameId = "";
    private int crowns;
    private int score;
    private Rank rank;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.currentGameId = user.getCurrentGameId();
        this.crowns = user.getCrowns();
        this.score = user.getScore();
        this.rank = user.getRank();
    }
}
