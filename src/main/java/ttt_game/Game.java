package ttt_game;

import com.sun.glass.ui.Size;
import lombok.Getter;

import java.util.Random;

/**
 * Created by Yang Xu on 2016/8/24.
 */
public class Game {
    private boolean isOver = false;
    private int turn = 0;

    public static final int trainingEpisodes = 5000;
    public static final int gamesPlayed = 500;
    public final static int SIZE = 3;
    public final static int FIELD = SIZE * SIZE;

    public final static int CROSS = 0;
    public final static int CIRCLE = 1;
    public final static int RANDOM = 2;
    public final static int NONE = 2;

    private static final String dataURL = "data";

    @Getter
    private final static boolean training = true;

    private Random random;

    private int winner;

    @Getter
    private Player[] players;
    @Getter
    private Player curPlayer;

    private Player mainPlayer;
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
        Player secPlayer = new RandomPlayer();
        Player mainPlayer = new DPRLPlayer(dataURL);
        Game game = new Game(mainPlayer, secPlayer);
        mainPlayer.setGame(game);
        secPlayer.setGame(game);
        mainPlayer.setMark(CIRCLE);
        secPlayer.setMark(CROSS);
        game.players[CIRCLE] = mainPlayer;
        game.players[CROSS] = secPlayer;

        int win = 0;
        int lose = 0;
        int draw = 0;
        if (!training) {
            for (int i = 0; i < gamesPlayed; i++) {
                game.match(RANDOM);
                if (game.winner == mainPlayer.getMark()) win++;
                else if (game.winner == secPlayer.getMark()) lose++;
                else draw++;
            }
            System.out.print("Win:"+win+"\nLose:"+lose+"\ndraw:"+draw+"\n");
        }else{
            for(int i = 0;i<trainingEpisodes;i++){
                game.match(RANDOM);
                mainPlayer.feedback(game.winner);
                }
        }
    }



    public void match(int mode) {
        winner = 0;
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
        if (nextMove < FIELD && nextMove >= 0 && board[nextMove] == NONE && mark != NONE) {
            this.board[nextMove] = mark;
            return true;
        } else return false;
    }

    private boolean gameIsOver() {
        // check rows;
        boolean res;
        for (int i = 0; i < SIZE; i++) {
            res = true;
            int target = board[SIZE * i];
            for (int j = 0; j < SIZE * i + SIZE; j++) {
                int cur = board[j + SIZE * i];
                if (cur == NONE || target != cur) {
                    break;
                }
                if (j + SIZE * i == SIZE * (i + 1) - 1 && res) {
                    winner = board[SIZE*i];
                    return res;
                }
            }
        }
        // check rows
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
            if (i == SIZE - 1 && res) {
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
        if(this.getFreeCells().length==0){
            winner = board[SIZE-1];
            return true;
        }
        return false;
    }

    public int[] getFreeCells() {
        int size = 0;
        for(int i = 0;i<board.length;i++){
            if(board[i] == NONE){
                size++;
            }
        }
        int[] res = new int[size];
        int index =0;
        for(int i =0;i<board.length;i++){
            if(board[i]!=NONE){
                res[index] = board[i];
                index++;
            }
        }
        return res;
    }
}
