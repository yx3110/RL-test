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

    public DPRLPlayer(){
        this.valueMap = new HashMap<StateActionPair, Double>();
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
        }else if(winnerMark == 2){
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
        this.stateActionSequence = new LinkedList<StateActionPair>();
    }

    public void saveLearningResult() {
        fileIO.saveMap(valueMap);
    }

    public void loadLearningResult(String dataURL) {
        this.valueMap = fileIO.loadMap(dataURL);
    }


    public int playTrained(){
        List<Integer> availableMoves = this.getGame().getFreeCells();
        int res = 0;
        double val = 0;
        for(int i = 0;i<availableMoves.size();i++){
            int curMove = availableMoves.get(i);
            int[] curState = this.getGame().getBoard();
            StateActionPair curPair = new StateActionPair(curState,curMove);
            double curVal;
            if(!valueMap.containsKey(curPair)){
                curVal = 0;
            }else {
                curVal = getValueMap().get(curPair);
            }
            if(curVal>=val){
                res = curMove;
                val = curVal;
            }
        }
        return res;
    }
    public int playTraining(){
        int[] state = getGame().getBoard();
        List<Integer> availableMoves = this.getGame().getFreeCells();
        int res = availableMoves.get(random.nextInt(availableMoves.size()));
        this.stateActionSequence.add(new StateActionPair(state,res));
        return res;
    }
}
