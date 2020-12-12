package com.msh.WorkoutGameServer.logic;

public class PriceCalculator {

    public static int calculate(int ownedNumber, double exponent) {
        return (int) Math.round(Math.pow(ownedNumber, exponent));
    }

    public static int calculateNext(int ownedNumber, double exponent) {
        return (int) Math.round(Math.pow(ownedNumber + 1, exponent));
    }
}
