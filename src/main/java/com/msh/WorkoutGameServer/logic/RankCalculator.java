package com.msh.WorkoutGameServer.logic;

import com.msh.WorkoutGameServer.model.Rank;

public class RankCalculator {

    public static Rank calculateRank(int score) {
        if (score < 200) {
            return Rank.Stone;
        } else if (score < 500) {
            return Rank.Bronze;
        } else if (score < 750) {
            return Rank.Silver;
        } else if (score < 1000) {
            return Rank.Gold;
        } else if (score < 1500) {
            return Rank.Diamond;
        } else if (score < 1700) {
            return Rank.Platinum;
        } else if (score < 1900) {
            return Rank.ValyrianSteel;
        } else {
            return Rank.Vibranium;
        }
    }
}
