package com.msh.WorkoutGameServer.model.message.out;

import com.msh.WorkoutGameServer.model.message.SimpleMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleResponse extends SimpleMessage {
    private String response;

    public SimpleResponse() {
    }

    public SimpleResponse(String from, String text, String response) {
        super(from, text);
        this.response = response;
    }
}
