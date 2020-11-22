package com.msh.WorkoutGameServer.model.message.out;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class StockResponse extends SimpleResponse {
    private Map<String, Integer> all;
    private Map<String, Integer> own;
    private int ownMoney;

    public StockResponse() {

    }

    public StockResponse(String from, String text, String response, Map<String, Integer> all, Map<String, Integer> own, int money) {
        super(from, text, response);
        this.all = all;
        this.own = own;
        this.ownMoney = money;
    }
}
