package com.msh.WorkoutGameServer.model.message.out;

import com.msh.WorkoutGameServer.model.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapStateResponse extends SimpleResponse {
    private Field[][] map;

    public MapStateResponse() {
    }

    public MapStateResponse(String from, String text, String response, Field[][] map) {
        super(from, text, response);
        this.map = map;
    }
}
