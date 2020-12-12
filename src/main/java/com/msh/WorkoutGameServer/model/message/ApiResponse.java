package com.msh.WorkoutGameServer.model.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {

    private Boolean success;
    private String message;
    private Object result;

    public ApiResponse(Boolean success, String message, Object result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

}
