package Util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by eclipse on 29/08/2016.
 */
public class Values implements Serializable {
    @Getter@Setter
    private double rawValue;
    @Getter@Setter
    private double timeVisited;
    @Getter
    private double normalizedValue;

    public Values(){
        rawValue = 0.0;
        normalizedValue = 0.0;
        timeVisited = 0.0;
    }

    private void normalize(){
        normalizedValue = (rawValue / timeVisited) ;
    }

    private void addRawValue(double change){
        rawValue = rawValue+change;
    }

    public void updateValue(double change){
        addRawValue(change);
        timeVisited++;
        normalize();
    }
}
