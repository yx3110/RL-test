package IO;

import Util.StateActionPair;
import javafx.beans.property.ObjectProperty;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Yang Xu on 2016/8/26.
 */
@SuppressWarnings("ALL")
public class FileIO {

    public static void saveMap(Map<StateActionPair,Double> map){
        Properties properties = new Properties();
        for (Map.Entry<StateActionPair,Double> entry:map.entrySet()){
            properties.put(entry.getKey(),entry.getValue());
        }
        try {
            properties.store(new FileOutputStream("data"),null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<StateActionPair,Double> loadMap(String addr){
        HashMap<StateActionPair,Double> res = new HashMap<StateActionPair, Double>();
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(addr));
        } catch (IOException e) {
            return res;
        }
        for(Object key:properties.keySet()){
            res.put((StateActionPair) key,(Double)properties.get(key));
        }
        return res;
    }
}
