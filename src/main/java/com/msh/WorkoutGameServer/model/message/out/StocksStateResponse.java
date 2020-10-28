package com.msh.WorkoutGameServer.model.message.out;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class StocksStateResponse extends SimpleResponse {
    private Map<String, Integer> stocks;

    public StocksStateResponse() {

    }

    public StocksStateResponse(String from, String text, String response, Map<String, Integer> stocks) {
        super(from, text, response);
        this.stocks = stocks;
    }
}
