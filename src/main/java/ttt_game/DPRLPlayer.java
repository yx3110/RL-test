package ttt_game;

import IO.FileIO;
import Util.StateActionPair;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Created by Yang Xu on 2016/8/24.
 */
public class DPRLPlayer extends Player {
    @Setter@Getter
    private Map<StateActionPair,Double> valueMap;
    private List<StateActionPair> stateActionSequence;

    private Random random;

    private static final FileIO fileIO = new FileIO();
    public static final int winReward = 1;
    public static final int loseReward = -1;
    public static final int drawReward = 0;

    public DPRLPlayer(String mapAddr){
        this.valueMap = fileIO.loadMap(mapAddr);
        this.stateActionSequence = new LinkedList<StateActionPair>();
        random = new Random();
    }

    public int play() {
        if(this.getGame().isTraining()){
            return playTraining();
        }else return playTrained();
    }

    public void feedback(int winnerMark) {
        if (winnerMark==this.getMark()){
            for(StateActionPair curPair:stateActionSequence){
                if(valueMap.containsKey(curPair)){
                    valueMap.put(curPair,valueMap.get(curPair)+winReward);
                }else{
                    valueMap.put(curPair, (double) winReward);
                }
            }
        }else if(winnerMark ==0){
            for(StateActionPair curPair:stateActionSequence){
                if(valueMap.containsKey(curPair)){
                    valueMap.put(curPair,valueMap.get(curPair)+drawReward);
                }else{
                    valueMap.put(curPair, (double) drawReward);
                }
            }
        }else {
            for(StateActionPair curPair:stateActionSequence){
                if(valueMap.containsKey(curPair)){
                    valueMap.put(curPair,valueMap.get(curPair)+loseReward);
                }else{
                    valueMap.put(curPair, (double) loseReward);
                }
            }
        }
    }



    public int playTrained(){
        int[] availableMoves = this.getGame().getFreeCells();
        int res = 0;
        double val = 0;
        for(int i = 0;i<availableMoves.length;i++){
            int curMove = availableMoves[i];
            int[] curState = this.getGame().getBoard();
            StateActionPair curPair = new StateActionPair(curState,curMove);
            double curVal = getValueMap().get(curPair);
            if(curVal>val){
                res = curMove;
                val = curVal;
            }
        }
        return res;
    }
    public int playTraining(){
        int[] state = getGame().getBoard();
        int[] availableMoves = this.getGame().getFreeCells();
        int res = availableMoves[random.nextInt(availableMoves.length)];
        this.stateActionSequence.add(new StateActionPair(state,res));
        return res;
    }
}
