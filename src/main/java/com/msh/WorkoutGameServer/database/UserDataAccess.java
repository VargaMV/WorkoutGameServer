package com.msh.WorkoutGameServer.database;

import com.msh.WorkoutGameServer.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataAccess extends MongoRepository<User, String> {
    User findByUsername(String name);

    void deleteByUsername(String name);
}

