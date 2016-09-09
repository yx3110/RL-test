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
    public static final double drawReward = 0.5;

    private static final double epsilon = 0.8;

    public DPRLPlayer(){
        this.valueMap = new HashMap<>();
        this.stateActionSequence = new ArrayList<>();
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
        for (int i=0;i<stateActionSequence.size();i++){
            StateActionPair curPair = stateActionSequence.get(i);
            for(int j=0;j<curPair.getState().length;j++){
                System.out.print(curPair.getState()[j]+",");
                if(j == curPair.getState().length-1){
                    System.out.println(" ");
                }
            }
            System.out.println(" Action:"+curPair.getAction());
        }
        if (winnerMark==getMark()){
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
        this.stateActionSequence = new ArrayList<>();
    }

    public void saveLearningResult() {
        fileIO.saveMap(valueMap);
    }

    public void loadLearningResult(String dataURL) {
        this.valueMap = fileIO.loadMap(dataURL);
    }

    @Override
    public void printTable() {
        for(StateActionPair curPair:valueMap.keySet()){
            Values curValues = valueMap.get(curPair);
            for(int i=0;i<curPair.getState().length;i++){
                System.out.print(curPair.getState()[i]+",");
                if(i == curPair.getState().length-1){
                    System.out.println(" ");
                }
            }
            System.out.println(" Action:"+curPair.getAction()+" Visited:" +curValues.getTimeVisited()+" rawValue:"+curValues.getRawValue()+" normalized:"+curValues.getNormalizedValue());
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
            if(curVal>val){
                res = curMove;
                val = curVal;
            }
        }
        return res;
    }
    public int playTraining(){
        int[] state = getGame().getBoard();
        List<Integer> availableMoves = this.getGame().getFreeCells();
        // int res = availableMoves.get(random.nextInt(availableMoves.size()));

        //epsilon
        int res = availableMoves.get(0);
        double epsilonTest = ThreadLocalRandom.current().nextDouble(0, 1);
        if(epsilonTest>=epsilon){
            res = availableMoves.get(random.nextInt(availableMoves.size()));
        }else{
            boolean contains = false;
            double val = 0;
            for(int i =0;i<availableMoves.size();i++){
                int curAction = availableMoves.get(i);
                StateActionPair curPair = new StateActionPair(state,curAction);
                if(valueMap.containsKey(curPair)&&valueMap.get(curPair).getNormalizedValue()>val){
                    contains = true;
                    res = availableMoves.get(i);
                    val = valueMap.get(curPair).getNormalizedValue();
                }
            }
            if (!contains){
                res = availableMoves.get(random.nextInt(availableMoves.size()));
            }
        }

        stateActionSequence.add(new StateActionPair(state,res));

        return res;
    }

}
