package ttt_game;

/**
 * Created by Yang Xu on 2016/9/9.
 */
public class SARSAQPlayer extends Player {

    @Override
    public int play() {
        if(this.getGame().isTraining()){
            return playTraining();
        }else return playTrained();
    }

    private int playTrained() {
        return 0;
    }

    private int playTraining() {
        return 0;
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
