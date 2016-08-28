package ttt_game;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yang Xu on 2016/8/24.
 */
public abstract class Player {
    @Getter
    @Setter
    private int mark;
    @Getter
    @Setter
    private Game game;
    @Getter
    @Setter
    private int nextMove;

    public abstract int play();

    public final void playTurn(){
        this.nextMove = play();
        game.play(mark,nextMove);
    }

    public abstract void feedback(int winnerMark);
    public abstract void saveLearningResult();

    public abstract void loadLearningResult(String dataURL);
}
