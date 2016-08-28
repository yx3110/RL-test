package Util;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by eclipse on 27/08/2016.
 */
public class StateActionPair implements Serializable{

    private int[] state;
    private int action;
    public StateActionPair(int[] state,int action){
        this.state = state;
        this.action = action;
    }

    @Override
    public boolean equals(Object obj) {
        StateActionPair other = (StateActionPair) obj;
        return Arrays.equals(state,other.state) && this.action == other.action;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                        append(state).
                        append(action).
                        toHashCode();
    }

    public String print() {
        StringBuilder resBuilder = new StringBuilder();
        resBuilder.append("{");
        for(int cur:state){
            resBuilder.append(cur);
            resBuilder.append(",");
        }
        resBuilder.append("}");
        resBuilder.append(action);
        return resBuilder.toString();
    }
}
