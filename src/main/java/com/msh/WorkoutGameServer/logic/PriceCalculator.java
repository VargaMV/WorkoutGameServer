package com.msh.WorkoutGameServer.logic;

public class PriceCalculator {

    public static double calculateNextN(int ownedNumber, double base, int amount) {
        int n = ownedNumber + 1;
        return Math.round((Math.pow(base, n) * ((Math.pow(base, amount) - 1) / (base - 1))) * 100) / 100.0;
    }
}
