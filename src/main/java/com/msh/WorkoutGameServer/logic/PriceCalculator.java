package com.msh.WorkoutGameServer.logic;

public class PriceCalculator {

    public static double exponent = 1;

    public static int calculate(int ownedNumber) {
        return (int) Math.round(Math.pow(ownedNumber, exponent));
    }

    public static int calculateNext(int ownedNumber) {
        return (int) Math.round(Math.pow(ownedNumber + 1, exponent));
    }
}
