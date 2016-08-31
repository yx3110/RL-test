package ttt_game;

import Util.StateActionPair;
import com.sun.glass.ui.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Created by Yang Xu on 2016/8/24.
 */
public class Game {

    public static final int trainingEpisodes = 100000;
    public static final int gamesPlayed = 500;
    public final static int SIZE = 3;
    public final static int FIELD = SIZE * SIZE;

    public final static int CROSS = 0;
    public final static int CIRCLE = 1;
    public final static int RANDOM = 2;
    public final static int NONE = 2;

    private static final String dataURL = "/Users/eclipse/RL-test/data.ser";

    @Getter
    private final static boolean training = false;

    private Random random;

    private int winner;

    @Getter
    private Player[] players;
    @Getter
    private Player curPlayer;

    private Player mainPlayer;
    @Setter
    private Player secPlayer;
    @Getter
    private int[] board;


    public Game(Player MainPlayer, Player SecPlayer) {
        this.mainPlayer = MainPlayer;
        this.board = new int[FIELD];
        this.players = new Player[2];
        this.secPlayer = SecPlayer;
        random = new Random();
    }

    public static void main(String[] args) {

        Player main = new DPRLPlayer();
        Player sec = new RandomPlayer();
        Game game = new Game(main, sec);
        game.mainPlayer.setGame(game);
        game.secPlayer.setGame(game);
        game.mainPlayer.setMark(CIRCLE);
        game.secPlayer.setMark(CROSS);
        game.players[CIRCLE] = game.mainPlayer;
        game.players[CROSS] = game.secPlayer;

        int win = 0;
        int lose = 0;
        int draw = 0;
        if (!training) {
            game.mainPlayer.loadLearningResult(dataURL);
            for (int i = 0; i < gamesPlayed; i++) {
                game.match(RANDOM);
                if (game.winner == game.mainPlayer.getMark()) win++;
                else if (game.winner == game.secPlayer.getMark()) lose++;
                else draw++;
            }
            game.mainPlayer.printTable();
            System.out.print("Win:" + win + "\nLose:" + lose + "\ndraw:" + draw + "\n");
        } else {
            for (int i = 0; i < trainingEpisodes; i++) {
                game.match(RANDOM);
                game.mainPlayer.feedback(game.winner);
            }

            game.mainPlayer.saveLearningResult();

            /*
            //selfPlay
            Player selfPlayer = new DPRLPlayer();
            selfPlayer.setGame(game);
            selfPlayer.setMark(CROSS);
            game.setSecPlayer(selfPlayer);
            game.players[CROSS] = game.secPlayer;
            for (int i = 0; i < trainingEpisodes; i++) {
                game.match(RANDOM);
                game.mainPlayer.feedback(game.winner);
                game.secPlayer.feedback(game.winner);
            }

            game.mainPlayer.saveLearningResult();*/
        }
    }


    public void match(int mode) {
        winner = NONE;
        board = new int[FIELD];
        switch (mode) {
            case CROSS:
                curPlayer = players[CROSS];
                break;
            case CIRCLE:
                curPlayer = players[CIRCLE];
                break;
            case RANDOM:
                curPlayer = players[random.nextInt(2)];
        }

        for (int i = 0; i < FIELD; i++) {
            board[i] = NONE;
        }
        while (!gameIsOver()) {
            curPlayer.playTurn();
            if (curPlayer.getMark() == CROSS) {
                curPlayer = players[CIRCLE];
            } else curPlayer = players[CROSS];
        }
    }

    public boolean play(int mark, int nextMove) {
        System.out.println(mark + " next move at position:" + nextMove);
        if (nextMove < FIELD && nextMove >= 0 && board[nextMove] == NONE && mark != NONE) {
            this.board[nextMove] = mark;
            System.out.println(curPlayer.getMark() + " played at position:" + nextMove);
            return true;
        } else return false;
    }

    private boolean gameIsOver() {
        // check rows;
        boolean res;
        for (int i = 0; i < SIZE; i++) {
            res = true;
            int target = board[SIZE * i];
            for (int j = 0; j < SIZE; j++) {
                int cur = board[j + SIZE * i];
                if (cur == NONE || target != cur) {
                    break;
                }
                if (j + SIZE * i == SIZE * (i + 1) - 1) {
                    winner = target;
                    return res;
                }
            }
        }
        // check columns
        for (int i = 0; i < SIZE; i++) {
            res = true;
            int target = board[i];
            for (int j = 0; j < SIZE; j++) {
                int cur = board[i + j * SIZE];
                if (cur == NONE || target != cur) {
                    break;
                }
                if (j == SIZE - 1 && res) {
                    winner = board[i];
                    return res;
                }
            }
        }
        //check diagonal
        int target = board[0];
        for (int i = 0; i < SIZE; i++) {
            res = true;
            int cur = board[i * SIZE + i];
            if (cur == NONE || target != cur) {
                break;
            }
            if (i == SIZE - 1) {
                winner = board[0];
                return res;
            }
        }

        target = board[SIZE - 1];
        for (int i = 1; i <= SIZE; i++) {
            res = true;
            int cur = board[i * SIZE - i];
            if (cur == NONE || target != cur) {
                break;
            }
            if (i == SIZE - 1 && res) return res;
        }
        if (this.getFreeCells().size() == 0) {
            winner = NONE;
            return true;
        }
        return false;
    }

    public List<Integer> getFreeCells() {
        List<Integer> res = new LinkedList<Integer>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == NONE) {
                res.add(i);
            }
        }
        return res;
    }
}
