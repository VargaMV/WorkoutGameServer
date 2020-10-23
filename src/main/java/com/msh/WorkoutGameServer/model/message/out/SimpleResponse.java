package com.msh.WorkoutGameServer.model.message.out;

import com.msh.WorkoutGameServer.model.message.SimpleMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleResponse<T> extends SimpleMessage {
    private T response;

    public SimpleResponse() {
    }

    public SimpleResponse(String from, String text, T response) {
        super(from, text);
        this.response = response;
    }
}
