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
        int index = random.nextInt(freeCells.length);
        return freeCells[index];
    }

    public void feedback(int winnerMark) {
    }
}
