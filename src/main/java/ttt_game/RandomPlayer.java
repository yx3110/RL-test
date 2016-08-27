package ttt_game;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

/**
 * Created by Yang Xu on 2016/8/24.
 */
public class RandomPlayer extends Player {

    public int play() {
        Random random = new Random();
        int[] freeCells = this.getGame().getFreeCells();
        return 0;
    }

    public void feedback(int winnerMark) {
    }
}
