package ttt_game;

import IO.FileIO;
import Util.StateActionPair;
import Util.Values;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Yang Xu on 2016/8/24.
 */
public class DPRLPlayer extends Player {
    @Setter@Getter
    private Map<StateActionPair, Values> valueMap;

    private List<StateActionPair> stateActionSequence;

    private Random random;

    private static final FileIO fileIO = new FileIO();
    public static final double winReward = 1;
    public static final double loseReward = -1;
    public static final double drawReward = 0.3;

    private static final double epsilon = 0.1;

    public DPRLPlayer(){
        this.valueMap = new HashMap<>();
        this.stateActionSequence = new LinkedList<>();
        random = new Random();
    }

    public int play() {
        if(this.getGame().isTraining()){
            return playTraining();
        }else return playTrained();
    }

    private void updateTable(StateActionPair curPair,double reward){
        Values curValues;
        if(valueMap.containsKey(curPair)){
            curValues = valueMap.get(curPair);
        }else{
            curValues = new Values();
        }
        curValues.updateValue(reward);
        valueMap.put(curPair,curValues);
    }



    public void feedback(int winnerMark) {
        if (winnerMark==this.getMark()){
            for(StateActionPair curPair:stateActionSequence){
                updateTable(curPair,winReward);
            }
        }else if(winnerMark == 2){
            for(StateActionPair curPair:stateActionSequence){
                updateTable(curPair,drawReward);
            }
        }else {
            for(StateActionPair curPair:stateActionSequence){
                updateTable(curPair,loseReward);
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

    @Override
    public void printTable() {
        for(Values curValues:valueMap.values()){
            System.out.println("visited:" +curValues.getTimeVisited()+"rawValue:"+curValues.getRawValue()+"normalized:"+curValues.getNormalizedValue());
        }
    }


    public int playTrained(){
        List<Integer> availableMoves = this.getGame().getFreeCells();
        int res = availableMoves.get(0);
        double val = 0;
        for(int i = 0;i<availableMoves.size();i++){
            int curMove = availableMoves.get(i);
            int[] curState = this.getGame().getBoard();
            StateActionPair curPair = new StateActionPair(curState,curMove);
            double curVal;
            if(!valueMap.containsKey(curPair)){
                curVal = 0;
            }else {
                curVal = getValueMap().get(curPair).getNormalizedValue();
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
        int res = availableMoves.get(0);
        double epsilonTest = ThreadLocalRandom.current().nextDouble(0, 1);
        if(epsilonTest>epsilon){
            res = availableMoves.get(random.nextInt(availableMoves.size()));
        }else{
            double val = 0;
            for(int i =0;i<availableMoves.size();i++){
                int curAction = availableMoves.get(i);
                StateActionPair curPair = new StateActionPair(state,curAction);
                if(valueMap.containsKey(curPair)&&valueMap.get(curPair).getNormalizedValue()>val){
                    res = availableMoves.get(i);
                    val = valueMap.get(curPair).getNormalizedValue();
                }
            }
        }
        this.stateActionSequence.add(new StateActionPair(state,res));
        return res;
    }

}
