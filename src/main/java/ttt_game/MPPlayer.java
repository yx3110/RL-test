package ttt_game;

/**
 * Created by Yang Xu on 2016/9/7.
 */
public class MPPlayer extends Player {
    @Override
    public int play() {
        if(this.getGame().isTraining()) return playTraining();
        else return playTrained();
    }

    private int playTrained() {
        int res = 0;
        return res;
    }

    private int playTraining() {
        int res = 0;
        return res;
    }

    @Override
    public void feedback(int winnerMark) {

    }

    @Override
    public void saveLearningResult() {

    }

    @Override
    public void loadLearningResult(String dataURL) {

    }

    @Override
    public void printTable() {

    }
}
