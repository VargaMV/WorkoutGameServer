package com.msh.WorkoutGameServer.model.message.out;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ExerciseInfoResponse extends SimpleResponse {
    private Map<String, Integer> information;

    public ExerciseInfoResponse() {

    }

    public ExerciseInfoResponse(String from, String text, String response, Map<String, Integer> information) {
        super(from, text, response);
        this.information = information;
    }
}
