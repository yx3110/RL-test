package ttt_game;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;

/**
 * Created by Yang Xu on 2016/8/24.
 */
public class RandomPlayer extends Player {

    public int play() {
        Random random = new Random();
        List<Integer> freeCells = this.getGame().getFreeCells();
        int index = random.nextInt(freeCells.size());
        return freeCells.get(index);
    }

    public void feedback(int winnerMark) {
    }

    public void saveLearningResult() {

    }

    public void loadLearningResult(String dataURL) {

    }

    @Override
    public void printTable() {

    }
}
