package com.msh.WorkoutGameServer.database;

import com.msh.WorkoutGameServer.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameDataAccess extends MongoRepository<Game, String> {
    Game findFirstByOrderByIdDesc();
}

